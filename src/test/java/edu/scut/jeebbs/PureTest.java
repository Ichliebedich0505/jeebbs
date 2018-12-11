package edu.scut.jeebbs;

import edu.scut.jeebbs.display.DailyStockDisplay;
import edu.scut.jeebbs.utils.Helper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PureTest {

    @Test
    public void testPrintPath() throws FileNotFoundException {

        System.out.println("the project path is: "+ResourceUtils.getURL("").getPath());
        //System.out.println("the project path is: "+ResourceUtils.CLASSPATH_URL_PREFIX);
    }


    @Test
    public void TestStr2Int(){


        if(Integer.valueOf("00001") == 1){
            System.out.println(Integer.valueOf("00001") +" is equal to 1 in the context!!!");
        }
    }

    @Test
    public void TestRead() throws IOException {

        Helper.init();
        Helper.StockCodes2NamesByFile();
    }

    @Test
    public void TestStrFormat(){
        System.out.println("Format of id should be: " + String.format("%06d", 1));
    }

    @Test
    public void TestResponse() throws IOException, ParseException {

        DailyStockDisplay dsd = new DailyStockDisplay();
        dsd.getStockIdNPrice();
    }

    @Test
    public void TestStrF(){

        String str = "aaaaaaaaabbbbaioobbbbbbfjeiwofjweo.....[][]";
        System.out.println("the first bbbb on index: "+ str.indexOf("bbbb"));



    }

    @Test
    public void TestStrEnd(){
        String str = "123456";

        if(str.charAt(6) == '\0'){
            System.out.println("can be reached");
        }

    }

}
