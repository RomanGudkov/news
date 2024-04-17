package spring_news_app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import spring_news_app.dto.CategoryDto;
import spring_news_app.message.Message;
import spring_news_app.service.CategoryCRUDService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    Message message;
    private final CategoryCRUDService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        if (categoryService.getById(id) == null) {
            message = new Message("Категория");
            return new ResponseEntity<>(message.notFound(id), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoryService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.create(categoryDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.create(categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        message = new Message("Категория");
        return categoryService.deleteById(id)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(message.notFound(id), HttpStatus.NOT_FOUND);
    }
}
