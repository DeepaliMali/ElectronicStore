package com.deepali.electronicstore.Service;

import com.deepali.electronicstore.dto.CategoryDto;
import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.dto.ProductDto;
import com.deepali.electronicstore.dto.UserDto;
import com.deepali.electronicstore.entities.Category;
import com.deepali.electronicstore.entities.Product;
import com.deepali.electronicstore.entities.User;
import com.deepali.electronicstore.repository.CategoryRepository;
import com.deepali.electronicstore.repository.ProductRepository;
import com.deepali.electronicstore.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceTest {


    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    Product product,product1,product2,product3;

    ProductDto productDto;

    Category category;

    @BeforeEach
    public void init()
    {
       category= Category.builder()
                .title("Mobiles")
                .coverImage("abc.png")
                .description("This category related to Mobiles")
                .build();

        product=Product.builder()
                .title("Iphone")
                .description("related to mobile category")
                .category(category)
                .price(80000).discountedPrice(70000)
                .stock(true).live(true)
                .addedDate(new Date())
                .quantity(5)
                .productImageName("abc.ong")
                .build();

         product1=Product.builder()
                .title("Iphone")
                .description("related to mobile category")
                .category(new Category())
                .price(80000).discountedPrice(70000)
                .stock(true).live(true)
                .addedDate(new Date())
                .quantity(5)
                .productImageName("abc.ong")
                .build();

         product2=Product.builder()
                .title("samsung TV")
                .description("related to TV category")
                .category(new Category())
                .price(80000).discountedPrice(50000)
                .stock(true).live(true)
                .addedDate(new Date())
                .quantity(5)
                .productImageName("tv.ong")
                .build();

         product3=Product.builder()
                .title("Headphone")
                .description("related to headphone category")
                .category(new Category())
                .price(25000).discountedPrice(20000)
                .stock(true).live(true)
                .addedDate(new Date())
                .quantity(5)
                .productImageName("headph.png")
                .build();


        ProductDto productDto=ProductDto.builder()
                .title("Iphone")
                .description("related to mobile category")
                .category(new CategoryDto())
                .price(80000).discountedPrice(70000)
                .stock(true).live(true)
                .addedDate(new Date())
                .quantity(5)
                .productImageName("abc.ong")
                .build();

    }

    @Test
    public void createProductTest()
    {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto product1 = productService.create(mapper.map(product, ProductDto.class));

        System.out.println(product1.getTitle());
        Assertions.assertNotNull(product1);
        Assertions.assertEquals("Iphone",product1.getTitle());
    }

    @Test
    public void updateProductTest()
    {
        String productId="productTest";

        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        ProductDto updatedProduct = productService.update(productDto, productId);
        System.out.println(updatedProduct.getTitle());

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(productDto.getTitle(),updatedProduct.getTitle(),"Title is not Validate");

    }

    @Test
    public void deleteProductTest()
    {
        String productId="productTest";

        Mockito.when(productRepository.findById("productTest")).thenReturn(Optional.of(product));
        productService.delete(productId);
        Mockito.verify(productRepository,Mockito.times(1)).delete(product);
    }

    @Test
    public void getProductTest()
    {
        String productId="productTest";
        Mockito.when(categoryRepository.findById("categoryIdTest")).thenReturn(Optional.of(category));

        //actual call of service method
        ProductDto productDto = productService.get(productId);

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals(product.getTitle(),productDto.getTitle(),"Title not matched");


    }

    @Test
    public void getAllProductTest()
    {
        List<Product> productList= Arrays.asList(product1,product2,product3);
        Page<Product> page=new PageImpl<>(productList);
        Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> all = productService.getAll(1, 2, "title", "ascending");
        Assertions.assertEquals(3,all.getContent().size());

    }

    @Test
    public void getAllLiveTest()
    {
        List<Product> productList= Arrays.asList(product1,product2,product3);
        Page<Product> page=new PageImpl<>(productList);
        Mockito.when(productRepository.findByLiveTrue((Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> all = productService.getAllLive(1, 2, "title", "ascending");
        Assertions.assertEquals(3,all.getContent().size());
    }

    @Test
    public void searchByTitleTest()
    {
        String keywords="mob";
        List<Product> productList= Arrays.asList(product1,product2,product3);
        Page<Product> page=new PageImpl<>(productList);

        Mockito.when(productRepository.findByTitleContaining(Mockito.anyString(),(Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> productDtos = productService.searchByTitle(keywords,1,2,"title","ascending");
        Assertions.assertEquals(3,productDtos.getPageSize(),"Size not matched !!");

    }

    @Test
    public void createWithCategoryTest()
    {
        String categoryId="categoryIdTest";

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        ProductDto product1 = productService.createWithCategory(mapper.map(product, ProductDto.class),categoryId);

        System.out.println(product1.getTitle());
        Assertions.assertNotNull(product1);
        Assertions.assertEquals("Iphone",product1.getTitle());


    }

    @Test
    public void updateWithCategoryTest()
    {
        String categoryId="categoryIdTest";
        String productId="productIdTest";

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        System.out.println(product1.getTitle());
        Assertions.assertNotNull(product1);
        Assertions.assertEquals("Iphone",product1.getTitle());

    }

    @Test
    public void getAllOfCategoryTest()
    {
        String categoryId="categoryIdTest";

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        List<Product> productList= Arrays.asList(product1,product2,product3);
        Page<Product> page=new PageImpl<>(productList);
        Mockito.when(productRepository.findByCategory(Mockito.any(),(Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<ProductDto> all = productService.getAllOfCategory(categoryId,1,2,"title","ascending");
        Assertions.assertEquals(3,all.getContent().size());

    }


}
