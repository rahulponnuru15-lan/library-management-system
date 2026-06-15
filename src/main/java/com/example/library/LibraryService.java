package com.example.library;

import com.example.library.book.Book;
import com.example.library.book.BookRepository;
import com.example.library.loan.Loan;
import com.example.library.loan.LoanRepository;
import com.example.library.loan.LoanStatus;
import com.example.library.member.Member;
import com.example.library.member.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public LibraryService(
            BookRepository bookRepository,
            MemberRepository memberRepository,
            LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
    }

    public List<Book> getBooks() {
        return bookRepository.findAllByOrderByTitleAsc();
    }

    public List<Member> getMembers() {
        return memberRepository.findAllByOrderByNameAsc();
    }

    public List<Loan> getLoans() {
        return loanRepository.findAllByOrderByBorrowedOnDesc();
    }

    public long countBooks() {
        return bookRepository.count();
    }

    public long countMembers() {
        return memberRepository.count();
    }

    public long countActiveLoans() {
        return loanRepository.countByStatus(LoanStatus.BORROWED);
    }

    @Transactional
    public Book addBook(Book book) {
        if (bookRepository.existsByIsbnIgnoreCase(book.getIsbn())) {
            throw new IllegalArgumentException("A book with this ISBN already exists.");
        }
        book.setAvailableCopies(book.getTotalCopies());
        return bookRepository.save(book);
    }

    @Transactional
    public Member addMember(Member member) {
        if (memberRepository.existsByEmailIgnoreCase(member.getEmail())) {
            throw new IllegalArgumentException("A member with this email already exists.");
        }
        return memberRepository.save(member);
    }

    @Transactional
    public Loan borrowBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found."));

        if (book.getAvailableCopies() < 1) {
            throw new IllegalStateException("No copy of this book is currently available.");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);
        loan.setBorrowedOn(LocalDate.now());
        loan.setDueOn(LocalDate.now().plusDays(14));
        loan.setStatus(LoanStatus.BORROWED);

        bookRepository.save(book);
        return loanRepository.save(loan);
    }

    @Transactional
    public void returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found."));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new IllegalStateException("This loan has already been returned.");
        }

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        loan.setReturnedOn(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        bookRepository.save(book);
        loanRepository.save(loan);
    }
}
