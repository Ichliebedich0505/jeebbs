package edu.scut.jeebbs.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DDXResponse{
    int page;
    int total;

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date curDate;

    List<Stock> data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date updataDate;
}
