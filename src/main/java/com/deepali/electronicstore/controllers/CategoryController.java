package com.deepali.electronicstore.controllers;

import com.deepali.electronicstore.dto.*;
import com.deepali.electronicstore.paylods.ApiResponseMessage;
import com.deepali.electronicstore.paylods.AppConstants;
import com.deepali.electronicstore.service.CategoryService;
import com.deepali.electronicstore.service.FileService;
import com.deepali.electronicstore.service.ProductService;
import com.deepali.electronicstore.service.impl.CategoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger logger= LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${category.image.path}")
    private String imageUploadPath;

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
        logger.info("Initializing updateCategory method of CategoryController for id"+categoryId);
        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        logger.info("Execution completed of updateCategory method in CategoryController for id"+categoryId);
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
        logger.info("Initializing deleteCategory method of CategoryController for id"+categoryId);
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message("Category Deleted Successfully !!").status(HttpStatus.OK).success(true).build();
        logger.info("Execution completed of deleteCategory method in CategoryController for id"+categoryId);
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
            @RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "PageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstants.SORT_DIR,required = false) String sortDir)


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
        logger.info("Initializing getSingle method of CategoryController for id"+categoryId);
        CategoryDto categoryDto = categoryService.get(categoryId);
        logger.info("Execution completed of getSingle method in CategoryController for id"+categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }

    //create product with category
    /**
     * @author Deepali
     * @apiNote Creates Products Within Specified Category
     */
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategory(
                                            @PathVariable("categoryId") String categoryId,
                                            @RequestBody ProductDto productDto)
    {
        logger.info("Initializing createProductWithCategory method of ProductController for id"+categoryId);
        ProductDto productWithCategory = productService.createWithCategory(productDto, categoryId);
        logger.info("Execution Completed of method createProductWithCategory for id"+categoryId);
        return new ResponseEntity<>(productWithCategory,HttpStatus.CREATED);
    }

    //update category of product
    /**
     * @author Deepali
     * @apiNote Updates Category of Products
     */
    @PutMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<ProductDto> updateCategoryProduct(
            @PathVariable String categoryId,
            @PathVariable String productId)
    {
        logger.info("Initializing updateCategoryProduct method of ProductController for id"+categoryId);
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        logger.info("Execution Completed of method updateCategoryProduct for id"+categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    //get Products of categories
    /**
     * @author Deepali
     * @apiNote fetch the Products from database by specified category
     */
    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageableResponse<ProductDto>> getProductsOfCategory(
            @PathVariable String categoryId,
            @RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_BY_TITLE,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstants.SORT_DIR,required = false) String sortDir)
    {
        logger.info("Initializing getProductsWithCategory method of ProductController");
        PageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId,pageNumber,pageSize,sortBy,sortDir);
        logger.info("Execution Completed of method getProductsWithCategory");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * @author Deepali
     * @apiNote Uploads Category Cover image
     */
    //upload Category image
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestPart("categoryImage") MultipartFile image,
                                                         @PathVariable String categoryId) throws IOException {

        logger.info("Initializing uploadCategoryImage method of UserController for id"+categoryId);
        String imageName = fileService.uploadFile(image, imageUploadPath);

        CategoryDto category = categoryService.get(categoryId);
        category.setCoverImage(imageName);

        CategoryDto categoryDto = categoryService.update(category, categoryId);

        ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).message("Image Uploaded Successfully").build();
        logger.info("Execution completed of uploadCategoryImage method for id"+categoryId);
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    /**
     *
     * @author Deepali
     * @apiNote Serves Category Cover image
     */
    //serve image
    @GetMapping(value = "image/{categoryId}")
    public  void serveCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {

        logger.info("Initializing serveCategoryImage method of UserController for id"+categoryId);
        CategoryDto category = categoryService.get(categoryId);
        logger.info("User Image:{}",category.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        logger.info("Execution completed of serveCategoryImage method for id"+categoryId);



    }



}

