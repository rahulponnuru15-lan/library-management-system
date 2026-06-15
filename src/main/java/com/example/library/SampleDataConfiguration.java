package com.example.library;

import com.example.library.book.Book;
import com.example.library.book.BookRepository;
import com.example.library.member.Member;
import com.example.library.member.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleDataConfiguration {

    @Bean
    CommandLineRunner loadSampleData(
            BookRepository bookRepository,
            MemberRepository memberRepository) {
        return args -> {
            if (bookRepository.count() == 0) {
                bookRepository.save(book(
                        "Clean Code",
                        "Robert C. Martin",
                        "9780132350884",
                        3));
                bookRepository.save(book(
                        "Effective Java",
                        "Joshua Bloch",
                        "9780134685991",
                        2));
            }

            if (memberRepository.count() == 0) {
                memberRepository.save(member("Aarav Sharma", "aarav@example.com"));
                memberRepository.save(member("Maya Patel", "maya@example.com"));
            }
        };
    }

    private Book book(String title, String author, String isbn, int copies) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setTotalCopies(copies);
        book.setAvailableCopies(copies);
        return book;
    }

    private Member member(String name, String email) {
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        return member;
    }
}
