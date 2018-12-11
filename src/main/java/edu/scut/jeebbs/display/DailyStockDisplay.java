package edu.scut.jeebbs.display;

import edu.scut.jeebbs.domain.DDXResponse;
import edu.scut.jeebbs.domain.Stock;
import edu.scut.jeebbs.utils.Helper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyStockDisplay {

    private RestTemplate client;
    private String referrer = "http://ddx.gubit.cn/drawdde.html";
    private List<Stock> StockCodes2Names ;
    //there stands a separator between old and new state of stock
    private String separator = "old<=>";

    //history data are separated by a vertical line
    private String _separator = "|";
    private String stockCode = "000001";

    public DailyStockDisplay() throws IOException {
        this.setClient();
        StockCodes2Names = Helper.StockCodes2NamesByFile();
    }

    private void setClient(){
        client = Helper.setClient();
    }

    public List<Stock> getStockIdNPrice() throws ParseException {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("ddx.gubit.cn")
                .path("/ygetddedata.php")
                .queryParam("code", stockCode)
                .queryParam("m", getParamM())
                .build();

        ResponseEntity<String> responseEntity = Helper.getResponseEntity(client,
                uriComponents.encode().toUri(), referrer,
                String.class, String.class,
                MediaType.ALL);

        //return a pure stream in string form.
        String response = responseEntity.getBody();
        //extract useful info from the stream
        List<Stock> stockList = ParseStockHistoryFromStream(response);

        System.out.println("the stream is like: " + stockList);
        return stockList;
    }

    //old<=> means the start of history data.
    private List<Stock> ParseStockHistoryFromStream(String stream) throws ParseException {
        List<Stock> stockList = new ArrayList<>();
        Stock newStock ;
        float price = 0.0f;
        String datestr = "";
        String date;
        StringBuffer sb = new StringBuffer("");

        //where the first digit of price at and every round the start index at
        int startInd = stream.indexOf(this.separator) + 6;
        //the last index of the line is the date I want
        int verticalLineInd = stream.indexOf("|");
        int curInd ;
        while (startInd != -1) {
            curInd = startInd;

            newStock = new Stock();
            while (stream.charAt(curInd) != ',') {
                sb.append(stream.charAt(curInd));
                curInd++;
            }

            //transform the string-format price to float-format
            price = Float.valueOf(sb.toString());
            sb = new StringBuffer("");
            curInd = verticalLineInd - 1;

            //
            while (stream.charAt(curInd) != ',') {
                sb.append(stream.charAt(curInd));
                curInd--;
            }
            sb.reverse();


            datestr = sb.toString();
            date = datestr;
            newStock.setCur(price);
            newStock.setDate(date);
            stockList.add(newStock);

            sb = new StringBuffer("");
            if (stream.length() - 1 != verticalLineInd) {
                stream = stream.substring(verticalLineInd + 1);
                startInd = 0;
            }
            else {
                startInd = -1;
            }
            verticalLineInd = stream.indexOf("|");
        }

        return stockList;
    }


    private String getStockCode(){

        return String.format("%06d", StockCodes2Names.get(0).getId());
    }

    private double getParamM(){

        return Math.random();
    }
}
