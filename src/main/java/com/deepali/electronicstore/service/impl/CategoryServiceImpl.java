package com.deepali.electronicstore.service.impl;

import com.deepali.electronicstore.dto.CategoryDto;
import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.entities.Category;
import com.deepali.electronicstore.exception.ResourceNotFoundException;
import com.deepali.electronicstore.helper.Helper;
import com.deepali.electronicstore.repository.CategoryRepository;
import com.deepali.electronicstore.service.CategoryService;
import com.deepali.electronicstore.service.UserService;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    /**
     * @author Deepali
     * @apiNote This method creates new Category
     *
     */
    public CategoryDto create(CategoryDto categoryDto) {

        logger.info("Initializing create method in CategoryServiceImpl");
        //creating categoryId randomly
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        CategoryDto categoryDto1 = mapper.map(savedCategory, CategoryDto.class);
        logger.info("Execution completed of create method in CategoryServiceImpl ");
        return categoryDto1;
    }

    @Override
    /**
     * @author Deepali
     * @apiNote This method updates existing Category
     *
     */
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {

        logger.info("Initializing update method in CategoryServiceImpl");
        //get category of given id
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id!!"));

       //update category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);

        CategoryDto categoryDto1 = mapper.map(updatedCategory, CategoryDto.class);
        logger.info("Execution completed of  update method in CategoryServiceImpl ");
        return categoryDto1;
    }

    @Override
    /**
     * @author Deepali
     * @apiNote This method deletes existing Category
     *
     */
    public void delete(String categoryId) {

        logger.info("Initializing delete method of CategoryServiceImpl");
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id!!"));
        categoryRepository.delete(category);
        logger.info("Execution completed of  delete  method in CategoryServiceImpl");

    }

    /**
     * @author Deepali
     * @apiNote This method retrieves all categories from database
     *
     */
    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {

        logger.info("Initializing getAll method of CategoryServiceImpl");

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable= PageRequest.of(pageNumber,pageSize);

        Page<Category> page = categoryRepository.findAll(pageable);

        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        logger.info("Execution completed of getAll method in CategoryServiceImpl");
        return pageableResponse;
    }

    /**
     * @author Deepali
     * @apiNote This method retrieves category by specified id
     * */
    @Override
    public CategoryDto get(String categoryId) {

        logger.info("Initializing get method of CategoryServiceImpl");
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id!!"));
        CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
        return categoryDto;

    }
}
