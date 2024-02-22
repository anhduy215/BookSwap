package com.example.bookswapplatform.service;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.BookFilterRequest;
import com.example.bookswapplatform.dto.BookRequest;
import com.example.bookswapplatform.dto.BookImageDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.text.ParseException;
import java.util.UUID;

public interface BookService {
    ResponseEntity<BaseResponseDTO> createBook (String uid, BookRequest bookRequest);
    ResponseEntity<BaseResponseDTO> findByTitle (String title);
    ResponseEntity<BaseResponseDTO> setImageBook (UUID id, BookImageDTO bookImageDTO);
    ResponseEntity<BaseResponseDTO> getUserBooksAvailable (Principal principal, int pageNumber, int pageSize);
    ResponseEntity<BaseResponseDTO> getUserBooksIsDone (Principal principal, int pageNumber, int pageSize);
    ResponseEntity<BaseResponseDTO> getUserBooksInPost (Principal principal, int pageNumber, int pageSize);
    ResponseEntity<BaseResponseDTO> modifyBook(Principal principal, UUID bookId, BookRequest bookRequest);
    ResponseEntity<BaseResponseDTO> deleteBook(Principal principal, UUID bookId);
    ResponseEntity<BaseResponseDTO> findById(UUID bookId);
    ResponseEntity<BaseResponseDTO> filterBook(int pageNumber,
                                               int pageSize,
                                               String sortBy,
                                               String sortOrder, String keyWord, BookFilterRequest bookFilterRequest);

}
