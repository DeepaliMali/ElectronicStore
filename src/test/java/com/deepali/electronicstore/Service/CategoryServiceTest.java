package com.deepali.electronicstore.Service;

import com.deepali.electronicstore.dto.CategoryDto;
import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.dto.UserDto;
import com.deepali.electronicstore.entities.Category;
import com.deepali.electronicstore.entities.User;
import com.deepali.electronicstore.repository.CategoryRepository;
import com.deepali.electronicstore.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    Category category,category1,category2;

    @BeforeEach
    public void init()
    {
        category=Category.builder()
                .title("Mobiles")
                .coverImage("abc.png")
                .description("This category related to Mobiles")
                .build();

         category1=Category.builder()
                .title("Mobiles")
                .coverImage("abc.png")
                .description("This category related to Mobiles")
                .build();

         category2=Category.builder()
                .title("TV")
                .coverImage("abc.png")
                .description("This category related to TV")
                .build();

    }

    @Test
    public void createCategoryTest()
    {
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        Category category1 = categoryRepository.save(category);

        System.out.println(category1.getTitle());
        Assertions.assertNotNull(category1);
        Assertions.assertEquals("Mobiles",category1.getTitle());
    }

    @Test
    public void updateCategoryTest()
    {
        String categoryId="categoryIdTest";

        CategoryDto categoryDto=CategoryDto.builder()
                        .title("Mobiles & Accessories")
                        .coverImage("xyz.png")
                        .description("This category related to Mobiles and their Accessories")
                        .build();
        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        System.out.println(updatedCategory.getTitle());

        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(categoryDto.getTitle(),updatedCategory.getTitle());

    }

    @Test
    public void deleteCategoryTest()
    {
        String categoryId="categoryIdTest";

        Mockito.when(categoryRepository.findById("categoryIdTest")).thenReturn(Optional.of(category));
        categoryService.delete(categoryId);
        Mockito.verify(categoryRepository,Mockito.times(1)).delete(category);
    }

    @Test
    public void getAllCategoryTest()
    {

        List<Category> categoryList = Arrays.asList(category, category1, category2);
        Page<Category> page=new PageImpl<>(categoryList);
        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<CategoryDto> all = categoryService.getAll(1, 2, "title", "ascending");
        Assertions.assertEquals(3,all.getContent().size());


    }

    @Test
    public void getCategoryTest()
    {
        String categoryId="categoryIdTest";
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        //actual call of service method
        CategoryDto categoryDto = categoryService.get(categoryId);

        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(categoryDto.getTitle(),category.getTitle(),"Title not matched");


    }






}
