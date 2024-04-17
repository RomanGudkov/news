package spring_news_app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
public class NewsDto {
    private long id;
    private String title;
    private String text;
    private Instant date;
    private String category;
}