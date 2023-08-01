package com.deepali.electronicstore.Service;


import com.deepali.electronicstore.dto.UserDto;
import com.deepali.electronicstore.entities.User;
import com.deepali.electronicstore.repository.UserRepository;
import com.deepali.electronicstore.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    User user;

    @BeforeEach
    public void init()
    {
        user = User.builder()
                .name("Deepali")
                .email("shewale.deeps@gmail.com")
                .about("Thi is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("abcd")
                .build();

    }

    //create user
    @Test
    public void createUserTest()
    {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto user1 = userService.createUser(mapper.map(user, UserDto.class));

        System.out.println(user1.getName());
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Deepali",user1.getName());

    }


    //update user
    @Test
    public void updateUserTest()
    {

    }



}
