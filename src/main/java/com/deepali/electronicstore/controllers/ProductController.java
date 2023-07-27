package com.deepali.electronicstore.controllers;

import com.deepali.electronicstore.dto.ImageResponse;
import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.dto.ProductDto;
import com.deepali.electronicstore.paylods.ApiResponseMessage;
import com.deepali.electronicstore.paylods.AppConstants;
import com.deepali.electronicstore.service.FileService;
import com.deepali.electronicstore.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private FileService fileService;

    @Value("${product.image.path}")
    private String imagePath;
    private static final Logger logger= LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    //create

    /**
     * @author Deepali
     * @apiNote Creates new Products in Database
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
    {
        logger.info("Initializing createProduct method of ProductController");
        ProductDto createdProducts = productService.create(productDto);
        logger.info("Execution completed of createProduct method of ProductController");
        return new ResponseEntity<>(createdProducts, HttpStatus.CREATED);

    }
    
    
    //update

    /**
     * @author Deepali
     * @apiNote Updates existing Products available in database
     */
    @PutMapping("{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId, @RequestBody ProductDto productDto)
    {
        logger.info("Initializing updateProduct method of ProductController for id"+productId);
        ProductDto updatedProduct = productService.update(productDto,productId);
        logger.info("Execution completed of updateProduct method of ProductController for id"+productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.CREATED);

    }


    //delete

    /**
     * @author Deepali
     * @apiNote Deletes Products from Database
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> delete(@PathVariable String productId)
    {
        logger.info("Initializing delete method of ProductController for id"+productId);
        productService.delete(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder().message(AppConstants.DELETE_PRODUCT).status(HttpStatus.OK).success(true).build();
        logger.info("Execution completed of delete method of ProductController for id"+productId);
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }


    //get single

    /**
     * @author Deepali
     * @apiNote Fetch Product from Database of specified id
     */
    @GetMapping("{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId)
    {
        logger.info("Initializing createProduct method of ProductController for id"+productId);
        ProductDto productDto = productService.get(productId);
        logger.info("Execution completed of createProduct method of ProductController for id"+productId);
        return new ResponseEntity<>(productDto, HttpStatus.OK);

    }

    
    //getAll

    /**
     * @author Deepali
     * @apiNote Fetch all Products from database
     */
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "PageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_BY_TITLE,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstants.SORT_DIR,required = false) String sortDir)
    {
        logger.info("Initializing getAll method of ProductController");
        PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Execution completed of getAll method of ProductController");
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }
    
    //getAll live

    /**
     * @author Deepali
     * @apiNote Fetch all live Products from database
     */
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "PageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_BY_TITLE,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstants.SORT_DIR,required = false) String sortDir)
    {
        logger.info("Initializing getAllLive method of ProductController");
        PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        logger.info("Execution completed of getAllLive method of ProductController");
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }



    //search

    /**
     * @author Deepali
     * @apiNote Search Products from Database by specified keyword
     */
    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProducts(
            @PathVariable String query,
            @RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_BY_TITLE,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstants.SORT_DIR,required = false) String sortDir)
    {
        logger.info("Initializing searchProducts method of ProductController");
        PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(query,pageNumber, pageSize, sortBy, sortDir);
        logger.info("Execution completed of searchProducts method of ProductController");
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }


    //upload image

    /**
     * @author Deepali
     * @apiNote Uploads Product image
     */
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadImage(@PathVariable String productId,
                                                     @RequestParam("productImage") MultipartFile image) throws IOException {
        logger.info("Initializing uploadImage method of ProductController");
        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.get(productId);
        productDto.setProductImageName(fileName);
        ProductDto updatedProduct = productService.update(productDto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImageName()).message("Product Image is Successfully Uploaded !!").status(HttpStatus.CREATED).success(true).build();
        logger.info("Execution completed of method uploadProductImage");
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    //serve image

    /**
     * @author Deepali
     * @apiNote Serves User image
     */
    @GetMapping(value = "image/{productId}")
    public  void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {

        logger.info("Initializing ServeProductImage method of ProductController");
        ProductDto productDto = productService.get(productId);
        logger.info("Product Image:{}",productDto.getProductImageName());
        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        logger.info("Execution completed of method serveProductImage");


    }






}
