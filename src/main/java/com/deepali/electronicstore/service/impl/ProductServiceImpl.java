package com.deepali.electronicstore.service.impl;

import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.dto.ProductDto;
import com.deepali.electronicstore.entities.Category;
import com.deepali.electronicstore.entities.Product;
import com.deepali.electronicstore.exception.ResourceNotFoundException;
import com.deepali.electronicstore.helper.Helper;
import com.deepali.electronicstore.repository.CategoryRepository;
import com.deepali.electronicstore.repository.ProductRepository;
import com.deepali.electronicstore.service.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger= LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     *
     * @author Deepali
     * @implNote  Creates new Product in database
     */
    @Override
    public ProductDto create(ProductDto productDto) {

        logger.info("Initializing create method of ProductServiceImpl");
        //product id
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);

        //added date
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        logger.info("Execution completed of create method in ProductServiceImpl");
        return mapper.map(savedProduct,ProductDto.class);
    }


    /**
     * @author Deepali
     * @implNote  Updates existing Product from database
     */
    @Override
    public ProductDto update(ProductDto productDto, String productId) {

        logger.info("Initializing update method of ProductServiceImpl for id"+productId);
        //fetch the product of given id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id"));

        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(product.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());

        Product updatedProduct = productRepository.save(product);

        logger.info("Execution completed of update method in ProductServiceImpl for id"+productId);

        return mapper.map(updatedProduct,ProductDto.class);
    }

    /**
     * @author Deepali
     * @implNote  Deletes Product from database
     */
    @Override
    public void delete(String productId) {

        logger.info("Initializing delete method of ProductServiceImpl for id"+productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id"));
        productRepository.delete(product);
        logger.info("Execution completed of delete method in ProductServiceImpl for id"+productId);

    }

    /**
     *
     * @author Deepali
     * @implNote  Fetch the product from database
     */
    @Override
    public ProductDto get(String productId) {

        logger.info("Initializing get method of ProductServiceImpl for id"+productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given id"));
        logger.info("Execution completed of get method in ProductServiceImpl for id"+productId);

        return mapper.map(product,ProductDto.class);
    }

    /**
     * @author Deepali
     * @implNote  Fetch all Products from Database
     */

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {

        logger.info("Initializing getAll method of ProductServiceImpl");
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        PageRequest pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);

        logger.info("Execution completed of getAll method in ProductServiceImpl");

        return Helper.getPageableResponse(page,ProductDto.class);
    }

    /**
     * @author Deepali
     * @implNote  Fetch all live Products from database
     */
    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir) {

        logger.info("Initializing getAllLive method of ProductServiceImpl");

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> page = productRepository.findByLiveTrue(pageable);

        logger.info("Execution completed of getAllLive method in ProductServiceImpl");

        return Helper.getPageableResponse(page,ProductDto.class);
    }

    /**
     * @author Deepali
     * @implNote  Search the Products from by specified Title
     */
    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir) {

        logger.info("Initializing searchByTitle method of ProductServiceImpl");

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);

        logger.info("Execution completed of searchByTitle method in ProductServiceImpl");

        return Helper.getPageableResponse(page,ProductDto.class);
    }

    //create product with category
    /**
     * @author Deepali
     * @implNote  Creates Products with Category
     */
    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

        logger.info("Initializing crateWithCategory method of ProductServiceImpl for id"+categoryId);
        //fetch the category
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id"));

        //product id
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);

        //added date
        productDto.setAddedDate(new Date());
        Product product = mapper.map(productDto, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        logger.info("Execution completed of crateWithCategory method in ProductServiceImpl for id"+categoryId);
        return mapper.map(savedProduct,ProductDto.class);
    }

    /**
     * @author Deepali
     * @implNote  update Products by given Category
     */
    @Override
    public ProductDto updateCategory(String productId, String categoryId) {

        logger.info("Initializing updateCategory method of ProductServiceImpl for id"+categoryId);
        //product fetch
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product with this id not found!!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not Found!!"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        logger.info("Execution completed of updateCategory method in ProductServiceImpl for id"+categoryId);
        return mapper.map(savedProduct,ProductDto.class);
    }

    /**
     * @author Deepali
     * @implNote  Fetch all Products of given Category
     */
    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {

        logger.info("Initializing getAllOfCategory method of ProductServiceImpl");
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not Found!!"));
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByCategory(category,pageable);
        logger.info("Execution completed of getAllOfCategory method in ProductServiceImpl");
        return Helper.getPageableResponse(page,ProductDto.class);
    }


}
