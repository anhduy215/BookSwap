package com.example.bookswapplatform.controller;

import com.example.bookswapplatform.common.FilterRequest;
import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.BookFilterRequest;
import com.example.bookswapplatform.dto.BookImageDTO;
import com.example.bookswapplatform.dto.BookRequest;
import com.example.bookswapplatform.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/book")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_USER')")
public class BookController {
    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponseDTO> createBook (Principal principal, @Valid @RequestBody BookRequest bookRequest) {
            return bookService.createBook(principal.getName(), bookRequest);
    }

    @GetMapping("/title")
    public ResponseEntity<BaseResponseDTO> getBookByTitle (@RequestParam String title) {
        return bookService.findByTitle(title);

    }
    @PostMapping("/image")
    public ResponseEntity<BaseResponseDTO> setImage (@RequestParam UUID bookId, @RequestBody BookImageDTO bookImageDTO) {
        return bookService.setImageBook(bookId, bookImageDTO);
    }

    @GetMapping("/available")
    public ResponseEntity<BaseResponseDTO> getUserBooksAvailable (Principal principal,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "3") int size) {
        return bookService.getUserBooksAvailable(principal, page, size);
    }

    @GetMapping("/is-done")
    public ResponseEntity<BaseResponseDTO> getUserBooksIsDone (Principal principal,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "3") int size) {
        return bookService.getUserBooksIsDone(principal, page, size);
    }

    @GetMapping("/in-post")
    public ResponseEntity<BaseResponseDTO> getUserBooksInPost (Principal principal,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "3") int size) {
        return bookService.getUserBooksInPost(principal, page, size);
    }
    @PutMapping("/modify")
    public ResponseEntity<BaseResponseDTO> modifyBook(Principal principal,
                                                      @RequestParam UUID bookId,
                                                      @Valid @RequestBody BookRequest bookRequest) {
        return bookService.modifyBook(principal, bookId, bookRequest);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseDTO> deleteBook(Principal principal, @RequestParam UUID bookId) {
        return bookService.deleteBook(principal, bookId);
    }
    @GetMapping("id")
    public ResponseEntity<BaseResponseDTO> findBookById(@RequestParam UUID bookId) {
        return bookService.findById(bookId);
    }
    @PostMapping("/filter")
    public ResponseEntity<BaseResponseDTO> filterBook(
            @Min(value = 0, message = "pageNumber must be greater than or equal to 0")
            @RequestParam(defaultValue = "0") int page,

            @Min(value = 1, message = "pageSize must be greater than or equal to 1")
            @Max(value = 100, message = "pageSize must be less than or equal to 100")
            @RequestParam(defaultValue = "6") int size,

            @Parameter(description = "Sort by (EX: title, price, newPercent,...)")
            @RequestParam(defaultValue = "title") String sortBy,

            @Parameter(description = "Sort order (EX: asc, desc)")
            @RequestParam(defaultValue = "desc") String sortOrder,

            @RequestParam(required = false) String keyWord,
            @RequestBody(required = false) BookFilterRequest bookFilterRequest
    ) {
        return bookService.filterBook(page, size, sortBy, sortOrder, keyWord, bookFilterRequest);
    }
}
