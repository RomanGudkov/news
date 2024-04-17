package spring_news_app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring_news_app.dto.CategoryDto;
import spring_news_app.entity.Category;
import spring_news_app.repositories.CategoryRepository;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryCRUDService implements CRUDService<CategoryDto> {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto getById(Long id) {
        log.info("Get category by id " + id);
        return categoryRepository.findById(id).isEmpty()
                ? null
                : mapToDto(categoryRepository.findById(id).orElseThrow());
    }

    @Override
    public Collection<CategoryDto> getAll() {
        log.info("Get all category");
        return categoryRepository.findAll().stream()
                .map(CategoryCRUDService::mapToDto)
                .toList();
    }

    @Override
    public Collection<CategoryDto> getAllById(Long id) {
        return null;
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        log.info("Create category");
        Category category = mapToEntity(categoryDto);
        categoryRepository.save(category);
        return mapToDto(category);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        log.info("Update category");
        categoryRepository.save(mapToEntity(categoryDto));
        return mapToDto(categoryRepository.findById(categoryDto.getId()).orElseThrow());
    }

    @Override
    public Boolean deleteById(Long id) {
        log.info("Delete category by Id " + id);
        if (categoryRepository.findById(id).isEmpty()) {
            return false;
        }
        categoryRepository.deleteById(id);
        return true;
    }

    public static CategoryDto mapToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setTitle(category.getTitle());
        return categoryDto;
    }

    public static Category mapToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setTitle(categoryDto.getTitle());
        return category;
    }
}
