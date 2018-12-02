package edu.scut.jeebbs;





import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.DeserializationFeature;
import lombok.Data;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
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

    @Data
    static class Stock{
        int id;
        Float cur;

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
            mediaTypeList = new ArrayList<>(converter.getSupportedMediaTypes());
            mediaTypeList.add(MediaType.TEXT_HTML);
            ((MappingJackson2HttpMessageConverter)converter).getObjectMapper().
                    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            ((MappingJackson2HttpMessageConverter)converter).setSupportedMediaTypes(mediaTypeList);
        }


    }

    @Test
    public void test(){
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http")
                .host("ddx.gubit.cn").path("/xg/ddxlist.php")
                .queryParam("t", Math.random())
                .queryParam("getlsdata", 1)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer", "http://ddx.gubit.cn/xg/ddx.html");
        headers.setAccept(Collections.singletonList(MediaType.ALL));

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<DdxResponse> responseEntity = client.exchange(uriComponents.encode().toUri(), HttpMethod.GET, httpEntity, DdxResponse.class);

        DdxResponse response = responseEntity.getBody();

        response.getData();
    }


}
