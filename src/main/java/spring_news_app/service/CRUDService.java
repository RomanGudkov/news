package spring_news_app.service;

import spring_news_app.dto.NewsDto;

import java.util.Collection;

public interface CRUDService<T> {

    T getById(Long id);

    Collection<T> getAll();

    Collection<T> getAllById(Long id);

    T create(T item);

    T update(T item);

    Boolean deleteById(Long id);
}
