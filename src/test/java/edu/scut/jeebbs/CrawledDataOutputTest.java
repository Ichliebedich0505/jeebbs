package edu.scut.jeebbs;





import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CrawledDataOutputTest {

    @Data
    static class DdxResponse{
        int page;
        int total;

        @JsonFormat(pattern = "yyyy-MM-dd")
        Date curDate;

        List<Stock> data;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date updataDate;
    }

    @JsonDeserialize(using = StockDeserializer.class)
    @Data
    static class Stock{
        Integer id;
        Float cur;

    }

    static class StockDeserializer extends JsonDeserializer<Stock>{

        @Override
        public Stock deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

            Stock stock = new Stock();

            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            stock.setCur(node.get(1).floatValue());
            stock.setId(node.get(0).intValue());
            return stock;
        }
    }

    //@Autowired
    private RestTemplate client;

    @Before
    public void setClient(){

        RestTemplateBuilder builder = new RestTemplateBuilder();
        client = builder.build();

        List<HttpMessageConverter<?>> converterList = client.getMessageConverters();

        HttpMessageConverter<?> converter;
        List<MediaType> mediaTypeList;
        for(int i = 0; i < converterList.size(); i++){
            converter = converterList.get(i);
            if (converter instanceof MappingJackson2HttpMessageConverter) {

                mediaTypeList = new ArrayList<>(converter.getSupportedMediaTypes());
                mediaTypeList.add(MediaType.TEXT_HTML);
                ((MappingJackson2HttpMessageConverter)converter).getObjectMapper().
                        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ((MappingJackson2HttpMessageConverter)converter).setSupportedMediaTypes(mediaTypeList);
            }
        }


    }

    @Test
    public void test(){
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http")
                .host("ddx.gubit.cn").path("/xg/ddxlist.php")
                .queryParam("t", Math.random())
                .queryParam("getlsdate", 1)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer", "http://ddx.gubit.cn/xg/ddx.html");
        headers.setAccept(Collections.singletonList(MediaType.ALL));

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<DdxResponse> responseEntity = client.exchange(uriComponents.encode().toUri(), HttpMethod.GET, httpEntity, DdxResponse.class);

        DdxResponse response = responseEntity.getBody();

        List<Stock> stockList = new ArrayList<>(response.getData());

        for(Stock s : stockList){
            log.info("股票编号是：" + s.id);
            log.info("股票指数是："+ s.cur);
        }
    }


}
