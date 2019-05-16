package edu.scut.jeebbs.controller;

import edu.scut.jeebbs.display.DailyStockDisplay;
import edu.scut.jeebbs.display.LatestDDXDisplay;
import edu.scut.jeebbs.domain.DailyDDXResponse;
import edu.scut.jeebbs.domain.Stock;
import edu.scut.jeebbs.utils.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
public class CustomController {


    @RequestMapping(value = "/test", method = GET)
    public String jsptest(ModelMap map) {
//
        return "test";
    }


    @RequestMapping(value = "/custom", method = GET)
    public String custom() {
//        throw new IllegalArgumentException();
        throw new ConstraintViolationException("testdd", new HashSet<>());
//        return "custom";
    }


    @RequestMapping(value = "/latest", method = RequestMethod.GET)
    public String index(ModelMap map){

        LatestDDXDisplay ldd = new LatestDDXDisplay();

        List<Stock> stocklist = new ArrayList<>(ldd.getStockIdNPrice());

        map.put("stocklist", stocklist);

        //log.info("fffffff" + stocklist.get(0).getCur());
        return "index";
    }

    @RequestMapping(value = "/singlehistorydaily", method = RequestMethod.GET)
    public String SingleHistoryDaily(ModelMap map) throws ParseException, IOException {
        Map<String, List<Stock>> name2historyprices = new HashMap<>();


        // later, we will improve it, which is
        // dynamically obtaining to-be-queried stocks from user's submitted form.
        List<Integer> QueriedStocks = new ArrayList<>();
        //QueriedStocks.addAll();

        QueriedStocks.add(1);
        QueriedStocks.add(2);
        QueriedStocks.add(3);

        List<Stock> keyformap = new ArrayList<>();
        for (int i = 0; i < QueriedStocks.size(); i++) {
            DailyStockDisplay dd = new DailyStockDisplay();
            Stock stock = Helper.StockCodes2NamesByFile().get(QueriedStocks.get(i));
            keyformap.add(stock);
            dd.setStockCode(String.format("%06d", stock.getId()));

            // obtain current stock's history prices and their corresponding date from web.
            // each element is of Stock type.
            List<Stock> stocklist = new ArrayList<>(dd.getStockIdNPrice());

            name2historyprices.put(stock.getName(), stocklist);
        }
        map.put("key", keyformap);
        map.put("stock", name2historyprices);
        return "singlehistorydaily";

    }


}
