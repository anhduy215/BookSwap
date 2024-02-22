package com.example.bookswapplatform.service.impl;

import com.example.bookswapplatform.common.Pagination;
import com.example.bookswapplatform.dto.BaseResponseDTO;
import com.example.bookswapplatform.dto.CategoryDTO;
import com.example.bookswapplatform.dto.SubCategoriesDTO;
import com.example.bookswapplatform.dto.SubSubCategoriesDTO;
import com.example.bookswapplatform.service.CategoryService;
import com.example.bookswapplatform.entity.Book.MainCategory;
import com.example.bookswapplatform.exception.ResourceNotFoundException;
import com.example.bookswapplatform.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<BaseResponseDTO> getAllCategory() {
        List<MainCategory> mainCategories = categoryRepository.findByParentCategoryIsNull();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();

        for (MainCategory mainCategory : mainCategories) {
            CategoryDTO mainCategoryDTO = new CategoryDTO();
            mainCategoryDTO.setName(mainCategory.getName());
            mainCategoryDTO.setId(mainCategory.getId());
            mainCategoryDTO.setSubCategoriesDTOS(getSubcategories(mainCategory.getSubCategories()));
            categoryDTOs.add(mainCategoryDTO);
        }

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully",null, categoryDTOs));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getCategory() {
        List<MainCategory> mainCategories = categoryRepository.findByParentCategoryIsNull();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();

        for (MainCategory mainCategory: mainCategories
             ) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setName(mainCategory.getName());
            categoryDTO.setSubCategoriesDTOS(null);
            categoryDTOs.add(categoryDTO);
        }
        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully",null, categoryDTOs));
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getSubCategory(String category) {
        MainCategory mainCategory = categoryRepository.findByNameAndParentCategory(category, null)
                .orElseThrow(()->new ResourceNotFoundException("Category with name :"+category+" Not Found!"));
        Set<MainCategory> subCategories = mainCategory.getSubCategories();
        Set<SubCategoriesDTO> subCategoriesDTOS = new HashSet<>();
        if(subCategories.isEmpty()) {
            return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully",null, null));
        } else {
            for (MainCategory subCategory : subCategories
            ) {
                SubCategoriesDTO subCategoriesDTO = new SubCategoriesDTO();
                subCategoriesDTO.setName(subCategory.getName());
                subCategoriesDTO.setSubSubCategoriesDTOS(null);
                subCategoriesDTOS.add(subCategoriesDTO);
            }
            return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, subCategoriesDTOS));
        }
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getSubSubCategory(String subCategory) {
        MainCategory mainCategory = categoryRepository.findByName(subCategory)
                .orElseThrow(()->new ResourceNotFoundException("Sub category with name :"+subCategory+" Not Found!"));
        Set<MainCategory> subSubCategories = mainCategory.getSubSubCategories();
        Set<SubSubCategoriesDTO> subSubCategoriesDTOS = new HashSet<>();
        if(subSubCategories.isEmpty()) {
            return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully",null, null));
        } else {
            for (MainCategory subSubCategory: subSubCategories
                 ) {
                SubSubCategoriesDTO subSubCategoriesDTO = new SubSubCategoriesDTO();
                subSubCategoriesDTO.setName(subSubCategory.getName());
                subSubCategoriesDTOS.add(subSubCategoriesDTO);
            }
            return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully",null, subSubCategoriesDTOS));
        }
    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllSubCategory() {

        List<MainCategory> mainCategories = categoryRepository.findByParentCategoryIsNull();
        List<SubCategoriesDTO> subCategoriesDTOS = new ArrayList<>();
        for (MainCategory mainCategory : mainCategories) {

            for (MainCategory subcategory : mainCategory.getSubCategories()) {
                SubCategoriesDTO subcategoryDTO = new SubCategoriesDTO();
                subcategoryDTO.setName(subcategory.getName());
                subcategoryDTO.setId(subcategory.getId());
                subCategoriesDTOS.add(subcategoryDTO);
            }

        }

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, subCategoriesDTOS));

    }

    @Override
    public ResponseEntity<BaseResponseDTO> getAllSubSubCategory() {
        List<MainCategory> mainCategories = categoryRepository.findByParentCategoryIsNull();
        List<SubSubCategoriesDTO> subSubCategoriesDTOS = new ArrayList<>();
        for (MainCategory mainCategory : mainCategories) {

            for (MainCategory subcategory : mainCategory.getSubCategories()) {

                for (MainCategory subSubCategory : subcategory.getSubSubCategories()) {
                    SubSubCategoriesDTO subSubCategoriesDTO = new SubSubCategoriesDTO();
                    subSubCategoriesDTO.setName(subSubCategory.getName());
                    subSubCategoriesDTO.setId(subSubCategory.getId());
                    subSubCategoriesDTOS.add(subSubCategoriesDTO);
                }
            }

        }

        return ResponseEntity.ok(new BaseResponseDTO(LocalDateTime.now(), HttpStatus.OK, "Successfully", null, subSubCategoriesDTOS));
    }


    private List<SubCategoriesDTO> getSubcategories(Set<MainCategory> subcategories) {
        List<SubCategoriesDTO> subcategoryDTOs = new ArrayList<>();

        for (MainCategory subcategory : subcategories) {
            SubCategoriesDTO subcategoryDTO = new SubCategoriesDTO();

            subcategoryDTO.setName(subcategory.getName());
            subcategoryDTO.setId(subcategory.getId());
            subcategoryDTO.setSubSubCategoriesDTOS(getSubSubCategories(subcategory.getSubSubCategories()));
            subcategoryDTOs.add(subcategoryDTO);
        }

        return subcategoryDTOs;
    }

    private List<SubSubCategoriesDTO> getSubSubCategories(Set<MainCategory> subSubCategories) {
        List<SubSubCategoriesDTO> subSubCategoriesDTOS = new ArrayList<>();

        for (MainCategory subSubCategory : subSubCategories) {
            SubSubCategoriesDTO subSubCategoriesDTO = new SubSubCategoriesDTO();
            subSubCategoriesDTO.setName(subSubCategory.getName());
            subSubCategoriesDTO.setId(subSubCategory.getId());
            subSubCategoriesDTOS.add(subSubCategoriesDTO);
        }

        return subSubCategoriesDTOS;
    }
}
