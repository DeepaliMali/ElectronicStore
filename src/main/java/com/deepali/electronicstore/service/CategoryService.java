package com.deepali.electronicstore.service;

import com.deepali.electronicstore.dto.CategoryDto;
import com.deepali.electronicstore.dto.PageableResponse;

public interface CategoryService {

    //create
    CategoryDto create(CategoryDto categoryDto);

    //update
    CategoryDto update(CategoryDto categoryDto,String categoryId);

    //delete
    void delete(String categoryId);

    //getAll
    PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    //get single category
    CategoryDto get(String categoryId);

    //search


}
