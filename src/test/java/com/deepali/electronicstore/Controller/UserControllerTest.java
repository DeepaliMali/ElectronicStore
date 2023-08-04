package com.deepali.electronicstore.Controller;

import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.dto.UserDto;
import com.deepali.electronicstore.entities.User;
import com.deepali.electronicstore.service.FileService;
import com.deepali.electronicstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @MockBean
    private FileService fileService;

    User user;

    @Autowired
    private MockMvc mockMvc;

    UserDto object1,object2,object3,object4;


    @BeforeEach
    public void init() {
        user = User.builder()
                .name("Deepali")
                .email("shewale.deeps@gmail.com")
                .about("Thi is testing create method")
                .gender("Female")
                .imageName("abc.png")
                .password("abcd")
                .build();

         object1 = UserDto.builder().name("Deepali").email("shewale.deeps@gmail.com").password("deepali123").about("Test").build();
         object2 = UserDto.builder().name("Dinesh").email("ddm@gmail.com").password("ddm123").about("Test").build();
         object3 = UserDto.builder().name("Durva").email("durva@gmail.com").password("durva123").about("Test").build();
         object4 = UserDto.builder().name("Chinmayee").email("chinmayee@gmail.com").password("chinmayee123").about("Test").build();

    }

    @Test
    public void createUserTest() throws Exception {

        UserDto dto = mapper.map(user, UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        //actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJasonString(user))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());


    }


    @Test
    public void updateUserTest() throws Exception {
        //users/{userId}+PUT request+json

        String userId = "userIdTest";

        UserDto dto = this.mapper.map(user, UserDto.class);
        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/users/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJasonString(user))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    private String convertObjectToJasonString(Object user) {
        try {

            return new ObjectMapper().writeValueAsString(user);

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    //get all users

    @Test
    public void getAllUsersTest() throws Exception {


        PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(object1, object2, object3, object4));
        pageableResponse.setLastPage(false);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalPages(100);
        pageableResponse.setPageNumber(100);
        pageableResponse.setTotalElement(1000);

        Mockito.when(userService.getAllUsers(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    //delete user

    @Test
    public void deleteUserTest() throws Exception {
        String userId = "userTest123";

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    //get user by id

    @Test
    public void getUserTest() throws Exception {

        String userId = "getUserTest";

        UserDto object = UserDto.builder().name("Deepali").email("shewale.deeps@gmail.com").password("deepali123").about("Test").build();
        Mockito.when(userService.getUserById(Mockito.any())).thenReturn(object);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }

    //get user by email
    @Test
    public void getUserByEmailTest() throws Exception {
        String email = "shewale.deeps@gmail.com";

        UserDto object = UserDto.builder().name("Deepali").email("shewale.deeps@gmail.com").password("deepali123").about("Test").build();
        Mockito.when(userService.getUserById(Mockito.any())).thenReturn(object);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/email/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    //search user
    @Test
    public void searchUserTest() throws Exception {

        String keyword = "s";

        List<UserDto> userDtos = Arrays.asList(object1, object2, object3, object4);

        Mockito.when(userService.searchUser(Mockito.anyString())).thenReturn(userDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/search/" + keyword)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    public void uploadUserImageTest() throws Exception {

        String fileName = "image1.jpg";
        String filePath = "image/users";
        String userId = "imageTest";

        Mockito.when(fileService.uploadFile(Mockito.any(), Mockito.anyString())).thenReturn(fileName);

        UserDto userDto = mapper.map(user, UserDto.class);
        Mockito.when(userService.getUserById(Mockito.anyString())).thenReturn(userDto);
        userDto.setImageName(fileName);

        Mockito.when(userService.updateUser(userDto, userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/image/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void serveUserImageTest() throws Exception {

        String imagePath = "image/users/";
        String userId = "serveImageTest";

        UserDto userDto = mapper.map(user, UserDto.class);
        Mockito.when(userService.getUserById(userId)).thenReturn(userDto);

        InputStream resource = new FileInputStream(userDto.getImageName());
        Mockito.when(fileService.getResource(Mockito.anyString(), Mockito.anyString())).thenReturn(resource);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/image/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();


    }

}
