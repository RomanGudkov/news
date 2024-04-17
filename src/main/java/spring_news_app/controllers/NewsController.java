package spring_news_app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_news_app.dto.NewsDto;
import spring_news_app.message.Message;
import spring_news_app.service.NewsCRUDService;

import java.util.Collection;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsCRUDService newsService;
    private Message messageNotFound;

    public NewsController(NewsCRUDService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsById(@PathVariable Long id) {
        if (newsService.getById(id) == null) {
            messageNotFound = new Message("Новость");
            messageNotFound.notFound(id);
            return new ResponseEntity<>(messageNotFound, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(newsService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<NewsDto>> getNewsAll() {
        return new ResponseEntity<>(newsService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Collection<NewsDto>> getAllById(@PathVariable Long id) {
        return new ResponseEntity<>(newsService.getAllById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createNews(@RequestBody NewsDto newsDto) {
        return new ResponseEntity<>(newsService.create(newsDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<NewsDto> updateNews(@RequestBody NewsDto news) {
        newsService.update(news);
        return new ResponseEntity<>(newsService.getById(news.getId()), HttpStatus.OK);
    }
}