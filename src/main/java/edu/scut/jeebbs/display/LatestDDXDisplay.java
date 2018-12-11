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
import edu.scut.jeebbs.utils.Helper;
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

        client = Helper.setClient();
    }

    public List<Stock> getStockIdNPrice(){
        UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http")
                .host("ddx.gubit.cn").path("/xg/ddxlist.php")
                .queryParam("t", Math.random())
                .queryParam("getlsdate", 1)
                .build();

        ResponseEntity<DDXResponse> responseEntity = Helper.getResponseEntity(this.client,
                uriComponents.encode().toUri(),"http://ddx.gubit.cn/xg/ddx.html",
                DDXResponse.class, String.class, MediaType.ALL);

        DDXResponse response = responseEntity.getBody();

        List<Stock> stockList = new ArrayList<>(response.getData());

        return stockList;
    }


}
