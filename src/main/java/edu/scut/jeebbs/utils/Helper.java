package edu.scut.jeebbs.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;

import edu.scut.jeebbs.domain.Stock;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Helper {

    private static List<Stock> stockCodesToNames = new ArrayList<>();
    private static String ProjectPath = "";
    private static String ResourceRelativePath = "src/main/resources";
    private static String ResourceAbsPath = "";
    private static String StockCodesFileName = "/StockCodes";

    public static void init() throws FileNotFoundException {
        ProjectPath = ResourceUtils.getURL("").getPath();
        ResourceAbsPath = ProjectPath + ResourceRelativePath;
    }


    public static List<Stock> StockCodes2NamesByFile() throws IOException {

        if(ProjectPath == null || ProjectPath == ""){
            init();
        }
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(ResourceAbsPath + StockCodesFileName)));
        String line = "";
        String code = "";
        String StockName = "";
        Stock perStock;
        int indOfComma = 0;
        int lineLen = 0;
        boolean flag = false;
        while ((line = br.readLine())!=null){
            if(!flag){
                System.out.println(line);

            }

            indOfComma = line.indexOf(',');
            lineLen = line.length();
            code = line.substring(0, indOfComma);
            StockName = line.substring(indOfComma+1, lineLen);


            if(!flag){
                System.out.println("first line's code and name are: " + code + ", " +StockName);
                flag = true;
            }
            perStock = new Stock();
            perStock.setId(Integer.valueOf(code));
            perStock.setName(StockName);
            stockCodesToNames.add(perStock);

        }


        return stockCodesToNames;
    }



    public static RestTemplate setClient(){

        RestTemplateBuilder builder = new RestTemplateBuilder();
        RestTemplate template = builder.build();

        List<HttpMessageConverter<?>> converters = new ArrayList(template.getMessageConverters());

        MediaType mediaType = MediaType.ALL;
        List<MediaType> mediaTypeList ;
        for(HttpMessageConverter<?> converter : converters){

            if(converter instanceof MappingJackson2HttpMessageConverter){
                mediaTypeList = new ArrayList<>(converter.getSupportedMediaTypes());
                mediaTypeList.add(mediaType);
                ((MappingJackson2HttpMessageConverter)converter).getObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(mediaTypeList);

            }


        }

        return template;

    }


    public static <T, E> ResponseEntity<T> getResponseEntity(RestTemplate client, URI url, String referrer, Class<T> responseType, Class<E> httpEntitytype, MediaType mediaType){

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(mediaType));
        headers.add("Referer",  referrer);
        HttpEntity<E> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<T> responseEntity = client.exchange(url, HttpMethod.GET, httpEntity, responseType);

        return responseEntity;
    }
}
