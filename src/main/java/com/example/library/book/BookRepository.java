package com.example.library.book;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbnIgnoreCase(String isbn);

    List<Book> findAllByOrderByTitleAsc();
}
