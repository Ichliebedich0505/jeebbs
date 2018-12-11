package edu.scut.jeebbs.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.scut.jeebbs.converter.StockDeserializer;
import lombok.Data;

import java.util.Date;

@JsonDeserialize(using = StockDeserializer.class)
@Data
public class Stock{
    Integer id;
    Float cur;
    String name;

    String date;
}