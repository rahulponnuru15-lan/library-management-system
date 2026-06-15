package com.example.library.loan;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findAllByOrderByBorrowedOnDesc();

    long countByStatus(LoanStatus status);
}
