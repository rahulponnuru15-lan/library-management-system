package com.example.library;

import com.example.library.book.Book;
import com.example.library.loan.LoanRequest;
import com.example.library.member.Member;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("bookCount", libraryService.countBooks());
        model.addAttribute("memberCount", libraryService.countMembers());
        model.addAttribute("activeLoanCount", libraryService.countActiveLoans());
        model.addAttribute("loans", libraryService.getLoans());
        return "dashboard";
    }

    @GetMapping("/books")
    public String books(Model model) {
        model.addAttribute("books", libraryService.getBooks());
        if (!model.containsAttribute("book")) {
            model.addAttribute("book", new Book());
        }
        return "books";
    }

    @PostMapping("/books")
    public String addBook(
            @Valid @ModelAttribute("book") Book book,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("books", libraryService.getBooks());
            return "books";
        }
        try {
            libraryService.addBook(book);
            redirectAttributes.addFlashAttribute("success", "Book added successfully.");
            return "redirect:/books";
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("isbn", "duplicate", exception.getMessage());
            model.addAttribute("books", libraryService.getBooks());
            return "books";
        }
    }

    @GetMapping("/members")
    public String members(Model model) {
        model.addAttribute("members", libraryService.getMembers());
        if (!model.containsAttribute("member")) {
            model.addAttribute("member", new Member());
        }
        return "members";
    }

    @PostMapping("/members")
    public String addMember(
            @Valid @ModelAttribute("member") Member member,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("members", libraryService.getMembers());
            return "members";
        }
        try {
            libraryService.addMember(member);
            redirectAttributes.addFlashAttribute("success", "Member added successfully.");
            return "redirect:/members";
        } catch (IllegalArgumentException exception) {
            bindingResult.rejectValue("email", "duplicate", exception.getMessage());
            model.addAttribute("members", libraryService.getMembers());
            return "members";
        }
    }

    @GetMapping("/loans")
    public String loans(Model model) {
        model.addAttribute("loans", libraryService.getLoans());
        model.addAttribute("books", libraryService.getBooks());
        model.addAttribute("members", libraryService.getMembers());
        if (!model.containsAttribute("loanRequest")) {
            model.addAttribute("loanRequest", new LoanRequest());
        }
        return "loans";
    }

    @PostMapping("/loans")
    public String borrowBook(
            @Valid @ModelAttribute("loanRequest") LoanRequest loanRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            prepareLoanPage(model);
            return "loans";
        }
        try {
            libraryService.borrowBook(loanRequest.getBookId(), loanRequest.getMemberId());
            redirectAttributes.addFlashAttribute("success", "Book issued successfully.");
            return "redirect:/loans";
        } catch (IllegalArgumentException | IllegalStateException exception) {
            model.addAttribute("error", exception.getMessage());
            prepareLoanPage(model);
            return "loans";
        }
    }

    @PostMapping("/loans/{loanId}/return")
    public String returnBook(@PathVariable Long loanId, RedirectAttributes redirectAttributes) {
        try {
            libraryService.returnBook(loanId);
            redirectAttributes.addFlashAttribute("success", "Book returned successfully.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/loans";
    }

    private void prepareLoanPage(Model model) {
        model.addAttribute("loans", libraryService.getLoans());
        model.addAttribute("books", libraryService.getBooks());
        model.addAttribute("members", libraryService.getMembers());
    }
}
