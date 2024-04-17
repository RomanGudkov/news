package spring_news_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring_news_app.dto.NewsDto;
import spring_news_app.entity.Category;
import spring_news_app.entity.News;
import spring_news_app.repositories.CategoryRepository;
import spring_news_app.repositories.NewsRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class NewsCRUDService implements CRUDService<NewsDto> {

    private TreeSet<Long> numberNews = new TreeSet<>();
    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public NewsDto getById(Long id) {
        log.info("Get news by id" + id);
        return newsRepository.findById(id).isEmpty()
                ? null
                : mapToDto(newsRepository.findById(id).orElseThrow());
    }

    @Override
    public Collection<NewsDto> getAll() {
        log.info("Get all news");
        return newsRepository.findAll().stream()
                .map(NewsCRUDService::mapToDto)
                .toList();
    }

    @Override
    public Collection<NewsDto> getAllById(Long id) {
        log.info("Get all news by Id " + id);
        return newsRepository.findAll().stream()
                .filter(news -> news.getCategory().getId().equals(id))
                .map(NewsCRUDService::mapToDto)
                .toList();
    }

    @Override
    public NewsDto create(NewsDto newsDto) {
        log.info("Create news");
        Category category = categoryRepository.findFirstByTitleLike(newsDto.getCategory())
                .orElseThrow();
        News news = mapToEntity(newsDto);
        news.setCategory(category);
        newsRepository.save(news);
        return mapToDto(news);
    }

    @Override
    public NewsDto update(NewsDto newsDto) {
        log.info("Update news");
        Category category = categoryRepository.findFirstByTitleLike(newsDto.getCategory())
                .orElseThrow();
        News news = mapToEntity(newsDto);
        news.setCategory(category);
        newsRepository.save(news);
        return mapToDto(news);
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    public static News mapToEntity(NewsDto newsDto) {
        News news = new News();
        news.setId(newsDto.getId());
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        news.setDate(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        return news;
    }

    private static NewsDto mapToDto(News news) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(news.getId());
        newsDto.setTitle(news.getTitle());
        newsDto.setText(news.getText());
        newsDto.setDate(news.getDate());
        newsDto.setCategory(news.getCategory().getTitle());
        return newsDto;
    }
}
