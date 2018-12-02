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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DdxStockDataTest {

    @Test
    public void lsdateArray() {
        RestTemplate client = new RestTemplateBuilder().build();
        ResponseEntity<String> resp = client.getForEntity("http://ddx.gubit.cn/xg/js/datelist.js", String.class);
        if (resp.getStatusCode().is2xxSuccessful()) {
            String body = resp.getBody();
            if (!StringUtils.isEmpty(body)) {
                Pattern p = Pattern.compile("(\\d{4}\\-\\d{2}\\-\\d{2})");
                Matcher m = p.matcher(body);
                while (m.find()) {
                    System.out.println(m.group());
                }
            }

        }
    }

    @Data
    static class DdxResponce {
        List<Stock> data;
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date nowdate;
        int page;
        int total;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date updatetime;
    }

    @JsonDeserialize(using = StockDeserializer.class)
    @Data
    static class Stock {
        Integer id;
        Float cur;
    }

    static class StockDeserializer extends JsonDeserializer<Stock> {

        @Override
        public Stock deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Stock stock = new Stock();
            int id = node.get(0).intValue();
            float cur = node.get(1).floatValue();
            stock.setId(id);
            stock.setCur(cur);
            return stock;
        }
    }

    @Test
    public void fetchData() {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("http").host("ddx.gubit.cn").path("/xg/ddxlist.php")
                .queryParam("t", Math.random())
                .queryParam("getlsdate", 1)
//              .queryParam("lsdate", "2018-09-14")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer", "http://ddx.gubit.cn/xg/ddx.html");
        headers.setAccept(Collections.singletonList(MediaType.ALL));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<DdxResponce> resp = client.exchange(uri.encode().toUri(), HttpMethod.GET, entity, DdxResponce.class);
        DdxResponce ddxResponce = resp.getBody();
        Assert.assertNotNull(ddxResponce);
        List<Stock> stocks = ddxResponce.getData();
        Assert.assertNotNull(stocks);
        Assert.assertEquals(20, stocks.size());
        System.out.println(stocks);

    }

    private RestTemplate client;

    @Before
    public void setClient() {
        RestTemplateBuilder rtb = new RestTemplateBuilder();
        this.client = rtb.build();
        List<HttpMessageConverter<?>> converters = this.client.getMessageConverters();
        for (HttpMessageConverter<?> converter: converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                List<MediaType> mediaTypeList = new ArrayList<>(converter.getSupportedMediaTypes());
                mediaTypeList.add(MediaType.TEXT_HTML);
                ((MappingJackson2HttpMessageConverter) converter).getObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(mediaTypeList);
            }
        }

    }
}
