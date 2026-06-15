package com.example.library;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.library.book.Book;
import com.example.library.book.BookRepository;
import com.example.library.loan.Loan;
import com.example.library.loan.LoanRepository;
import com.example.library.loan.LoanStatus;
import com.example.library.member.Member;
import com.example.library.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibraryServiceTests {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LoanRepository loanRepository;

    @BeforeEach
    void clearDatabase() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void borrowingBookCreatesLoanAndDecreasesAvailableCopies() {
        Book book = libraryService.addBook(book("Domain-Driven Design", "9780321125217", 2));
        Member member = libraryService.addMember(member("Riya Sen", "riya@example.com"));

        Loan loan = libraryService.borrowBook(book.getId(), member.getId());

        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(loan.getStatus()).isEqualTo(LoanStatus.BORROWED);
        assertThat(updatedBook.getAvailableCopies()).isEqualTo(1);
        assertThat(loan.getDueOn()).isEqualTo(loan.getBorrowedOn().plusDays(14));
    }

    @Test
    void returningBookRestoresAvailableCopy() {
        Book book = libraryService.addBook(book("Refactoring", "9780134757599", 1));
        Member member = libraryService.addMember(member("Kabir Rao", "kabir@example.com"));
        Loan loan = libraryService.borrowBook(book.getId(), member.getId());

        libraryService.returnBook(loan.getId());

        Loan returnedLoan = loanRepository.findById(loan.getId()).orElseThrow();
        Book updatedBook = bookRepository.findById(book.getId()).orElseThrow();
        assertThat(returnedLoan.getStatus()).isEqualTo(LoanStatus.RETURNED);
        assertThat(returnedLoan.getReturnedOn()).isNotNull();
        assertThat(updatedBook.getAvailableCopies()).isEqualTo(1);
    }

    @Test
    void duplicateIsbnIsRejected() {
        libraryService.addBook(book("First title", "1234567890", 1));

        assertThatThrownBy(() ->
                libraryService.addBook(book("Second title", "1234567890", 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ISBN");
    }

    private Book book(String title, String isbn, int copies) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor("Test Author");
        book.setIsbn(isbn);
        book.setTotalCopies(copies);
        return book;
    }

    private Member member(String name, String email) {
        Member member = new Member();
        member.setName(name);
        member.setEmail(email);
        return member;
    }
}
