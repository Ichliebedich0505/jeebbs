package edu.scut.jeebbs.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@RequiredArgsConstructor
@Data
public class News {
    @Id
    @GeneratedValue
    private Long id;
    private final String source;
    private final String title;
    private final String href;
    private final String abstracts;
    private final Date date;

}
