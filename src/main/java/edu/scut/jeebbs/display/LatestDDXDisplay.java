package edu.scut.jeebbs.display;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.scut.jeebbs.domain.DDXResponse;
import edu.scut.jeebbs.domain.Stock;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
public class LatestDDXDisplay {





    private RestTemplate client;

    public LatestDDXDisplay(){
        this.setClient();
    }


    private void setClient(){

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

    public List<Stock> getStockIdNPrice(){
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http")
                .host("ddx.gubit.cn").path("/xg/ddxlist.php")
                .queryParam("t", Math.random())
                .queryParam("getlsdate", 1)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer", "http://ddx.gubit.cn/xg/ddx.html");
        headers.setAccept(Collections.singletonList(MediaType.ALL));

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<DDXResponse> responseEntity = client.exchange(uriComponents.encode().toUri(), HttpMethod.GET, httpEntity, DDXResponse.class);

        DDXResponse response = responseEntity.getBody();

        List<Stock> stockList = new ArrayList<>(response.getData());

        return stockList;
    }

    public void Display(){


    }


}
