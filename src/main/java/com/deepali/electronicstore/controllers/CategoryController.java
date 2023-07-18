package com.deepali.electronicstore.controllers;

import com.deepali.electronicstore.dto.CategoryDto;
import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.paylods.ApiResponseMessage;
import com.deepali.electronicstore.paylods.AppConstants;
import com.deepali.electronicstore.service.CategoryService;
import com.deepali.electronicstore.service.impl.CategoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    //create

    /**
     *
     * @author Deepali
     * @apiNote creates new category
     */
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        logger.info("Initializing createCategory method of CategoryController");
        //call service to create object
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        logger.info("Execution completed of createCategory method in CategoryController");
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    /**
     *
     * @author Deepali
     * @apiNote updates existing category
     */
    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
                                                      @PathVariable String categoryId )
    {
        logger.info("Initializing updateCategory method of CategoryController");
        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        logger.info("Execution completed of updateCategory method in CategoryController");
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);

    }


    //delete
    /**
     *
     * @author Deepali
     * @apiNote deletes existing category
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId)
    {
        logger.info("Initializing deleteCategory method of CategoryController");
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category Deleted Successfully !!").status(HttpStatus.OK).success(true).build();
        logger.info("Execution completed of deleteCategory method in CategoryController");
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    //get all
    /**
     *
     * @author Deepali
     * @apiNote Retrieves all categories from database
     */
    @GetMapping
    public ResponseEntity<PageableResponse> getAll(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "PageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue ="asc",required = false) String sortDir)


    {
        logger.info("Initializing getAll method of CategoryController");
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Execution completed of getAll method in CategoryController");
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);

    }

    //get single
    /**
     *
     * @author Deepali
     * @apiNote Retrieves category from database by specified id
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId)
    {
        logger.info("Initializing getSingle method of CategoryController");
        CategoryDto categoryDto = categoryService.get(categoryId);
        logger.info("Execution completed of getSingle method in CategoryController");
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }
}
