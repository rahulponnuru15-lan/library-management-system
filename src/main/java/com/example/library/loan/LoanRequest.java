package com.example.library.loan;

import jakarta.validation.constraints.NotNull;

public class LoanRequest {

    @NotNull(message = "Select a book")
    private Long bookId;

    @NotNull(message = "Select a member")
    private Long memberId;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
