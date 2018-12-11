package edu.scut.jeebbs.controller;

import edu.scut.jeebbs.display.DailyStockDisplay;
import edu.scut.jeebbs.display.LatestDDXDisplay;
import edu.scut.jeebbs.domain.DailyDDXResponse;
import edu.scut.jeebbs.domain.Stock;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
@RequestMapping(value = "/")
public class CustomController {

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
        DailyStockDisplay dd = new DailyStockDisplay();

        List<Stock> stocklist = new ArrayList<>(dd.getStockIdNPrice());

        map.put("stocklist", stocklist);
        return "singlehistorydaily";

    }


}
