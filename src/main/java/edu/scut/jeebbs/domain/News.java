package edu.scut.jeebbs.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class News {
    @Id
    @GeneratedValue
    private Long id;
    private String source;
    private String title;
    private String href;
    private String abstracts;
    private Date date;

    public News(String source, String title, String href, String abstracts, Date date) {
        this.source = source;
        this.title = title;
        this.href = href;
        this.abstracts = abstracts;
        this.date = date;
    }
}
