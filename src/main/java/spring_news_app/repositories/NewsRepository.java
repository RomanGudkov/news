package spring_news_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring_news_app.entity.News;

public interface NewsRepository extends JpaRepository<News, Long> {
}
