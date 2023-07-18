package com.deepali.electronicstore.service;

import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.dto.ProductDto;

import java.util.List;

public interface ProductService {

        //create
        ProductDto create(ProductDto productDto);

        //update
        ProductDto update(ProductDto productDto,String productId);

        //delete
        void delete(String productId);

        //get single
        ProductDto get(String productId);

        //get all
        PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

        //get all:live
        PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir);


        //search products
        PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir);


}
