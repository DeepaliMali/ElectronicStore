package com.deepali.electronicstore.Controller;

import com.deepali.electronicstore.dto.CategoryDto;
import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.dto.ProductDto;
import com.deepali.electronicstore.dto.UserDto;
import com.deepali.electronicstore.entities.Category;
import com.deepali.electronicstore.entities.Product;
import com.deepali.electronicstore.service.CategoryService;
import com.deepali.electronicstore.service.FileService;
import com.deepali.electronicstore.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @Autowired
    private ModelMapper mapper;

    @MockBean
    private FileService fileService;

    Category category;

    CategoryDto category1,category2,category3;
    Product product;
    ProductDto product1,product2,product3;


    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init()
    {
        category= Category.builder().title("Mobiles").coverImage("abc.png").description("This category related to Mobiles").build();

        CategoryDto category1= CategoryDto.builder().title("Mobiles").coverImage("mobile.png").description("This category related to Mobiles").build();
        CategoryDto category2= CategoryDto.builder().title("TV").coverImage("tv.png").description("This category related to TV's").build();
        CategoryDto category3= CategoryDto.builder().title("AC").coverImage("ac.png").description("This category related to AC units").build();


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

        ProductDto product1=ProductDto.builder()
                .title("Iphone")
                .description("related to mobile category")
                .category(new CategoryDto())
                .price(80000).discountedPrice(70000)
                .stock(true).live(true)
                .addedDate(new Date())
                .quantity(5)
                .productImageName("abc.ong")
                .build();

        ProductDto product2=ProductDto.builder()
                .title("samsung TV")
                .description("related to TV category")
                .category(new CategoryDto())
                .price(80000).discountedPrice(50000)
                .stock(true).live(true)
                .addedDate(new Date())
                .quantity(5)
                .productImageName("tv.ong")
                .build();

        ProductDto product3=ProductDto.builder()
                .title("Headphone")
                .description("related to headphone category")
                .category(new CategoryDto())
                .price(25000).discountedPrice(20000)
                .stock(true).live(true)
                .addedDate(new Date())
                .quantity(5)
                .productImageName("headph.png")
                .build();


    }

    //create category test

    @Test
    public  void createCategoryTest() throws Exception {
        CategoryDto dto = mapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.create(Mockito.any())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJasonString(category))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());


    }

    private String convertObjectToJasonString(Object user) {
        try {

            return new ObjectMapper().writeValueAsString(user);

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    //update category test

    @Test
    public void updateCategoryTest() throws Exception {

        String categoryId="categoryTest";
        CategoryDto dto = this.mapper.map(category, CategoryDto.class);

        Mockito.when(categoryService.update(Mockito.any(), Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/categories/" + categoryId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJasonString(category))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists());



    }

    //delete category test
    @Test
    public void deleteCategoryTest() throws Exception {
        String categoryId="categoryTest";

        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void getAllCategoriesTest() throws Exception {


        PageableResponse<CategoryDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(category1,category2,category3));
        pageableResponse.setLastPage(false);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalPages(100);
        pageableResponse.setPageNumber(100);
        pageableResponse.setTotalElement(1000);

        Mockito.when(categoryService.getAll(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }

    //get single category test

    @Test
    public void getSingleTest() throws Exception {

        String categoryId="categoryTest";
        CategoryDto category1= CategoryDto.builder().title("Mobiles").coverImage("mobile.png").description("This category related to Mobiles").build();

        Mockito.when(categoryService.get(Mockito.any())).thenReturn(category1);

        mockMvc.perform(MockMvcRequestBuilders.get("/categories/" + categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }


    //create product with category test


    @Test
    public void createProductWithCategoryTest() throws Exception {

        String categoryId="categoryTest";
        ProductDto dto = this.mapper.map(product, ProductDto.class);

        Mockito.when(productService.createWithCategory(Mockito.any(),Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/categories/"+categoryId+"/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJasonString(category))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());


    }

    //update category of products Test
    @Test
    public void updateCategoryProductTest() throws Exception {

        String categoryId="categoryTest";
        String productId="productTest";
        ProductDto dto = this.mapper.map(product, ProductDto.class);

        Mockito.when(productService.updateCategory(Mockito.any(),Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/categories/"+categoryId+"/products/"+productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJasonString(category))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    //get products of categories

    @Test
    public void getProductsOfCategoryTest() throws Exception {

        String categoryId="categoryTest";

        PageableResponse<ProductDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(product1,product2,product3));
        pageableResponse.setLastPage(false);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalPages(100);
        pageableResponse.setPageNumber(100);
        pageableResponse.setTotalElement(1000);

        Mockito.when(productService.getAllOfCategory(Mockito.any(),Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/categories/"+categoryId+"/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }



}
