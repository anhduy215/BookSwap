package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.BookSystemDTO;
import com.example.bookswapplatform.entity.Book.Author;
import com.example.bookswapplatform.entity.Book.BookSystem;
import com.example.bookswapplatform.repository.BookSystemRepository;
import com.example.bookswapplatform.service.BookSystemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookSystemServiceImpl implements BookSystemService {
    private final BookSystemRepository bookSystemRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity<BaseResponseDTO> getBookByTitle(String title) {
        List<BookSystem> bookSystems = bookSystemRepository.findByTitleContaining(title);
        if(bookSystems.isEmpty()) {
            return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, null));
        }
        List<BookSystemDTO> bookSystemDTOS = new ArrayList<>();
        for (BookSystem bookSystem : bookSystems
                ) {
            bookSystemDTOS.add(convertToDTO(bookSystem));
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, bookSystemDTOS));

    }
    private BookSystemDTO convertToDTO(BookSystem bookSystem) {
        if (bookSystem == null) {
            return null;
        }
        BookSystemDTO bookSystemDTO = modelMapper.map(bookSystem, BookSystemDTO.class);
        bookSystemDTO.setMainCategory(bookSystem.getMainCategory().getName());
        Set<String> authorNames = new HashSet<>();
        Set<Author> authors = bookSystem.getAuthors();
        for (Author author: authors
        ) {
            authorNames.add(author.getName());
        }
        bookSystemDTO.setAuthors(authorNames);
        // map subCategory
        if(bookSystem.getSubCategory() == null) {
            bookSystemDTO.setSubCategory(null);
        }else {
            bookSystemDTO.setSubCategory(bookSystem.getSubCategory());
        }
        // map subSubCategory
        if(bookSystem.getSubSubCategory() == null) {
            bookSystemDTO.setSubSubCategory(null);
        } else {
            bookSystemDTO.setSubSubCategory(bookSystem.getSubSubCategory());
        }
        return bookSystemDTO;
    }
}
