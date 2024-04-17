package spring_news_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring_news_app.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findFirstByTitleLike(String title);
}
