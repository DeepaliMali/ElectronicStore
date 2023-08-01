package com.deepali.electronicstore.Service;


import com.deepali.electronicstore.dto.PageableResponse;
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
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
                .gender("Female")
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
        String userId="dfhgr";
        UserDto userDto = UserDto.builder()
                .name("Deepali Mali")
                .email("shewale.deeps@gmail.com")
                .about("This is testing update method")
                .gender("Female")
                .imageName("xyz.png")
                .build();

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updatedUser = userService.updateUser(userDto, userId);
        System.out.println(updatedUser.getName());

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getName(),updatedUser.getName(),"Name is not Validate");

    }

    //delete user
    @Test
    public void deleteUserTest()
    {
        String userId="userIdabc";

        Mockito.when(userRepository.findById("userIdabc")).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }


    //get All Users

    @Test
    public void getAllUsersTest()
    {
      User  user1 = User.builder()
                .name("Dinesh")
                .email("shewale.deeps@gmail.com")
                .about("Thi is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("abcd")
                .build();

       User user2 = User.builder()
                .name("Durva")
                .email("shewale.deeps@gmail.com")
                .about("Thi is testing create method")
                .gender("Female")
                .imageName("abc.png")
                .password("abcd")
                .build();

        List<User> userList= Arrays.asList(user,user1,user2);
        Page<User> page=new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        PageableResponse<UserDto> allUsers = userService.getAllUsers(1,2,"name","ascending");
        Assertions.assertEquals(3,allUsers.getContent().size());
    }


    //get user by id
    @Test
    public void getUserByIdTest()
    {
        String userId="userIdTest";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //actual call of service method
        UserDto userDto = userService.getUserById(userId);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(),userDto.getName(),"Name not matched");


    }

    //get user by email
    @Test
    public void getUserByEmailTest()
    {
        String emailId="shewale.deeps@gmail.com";
        Mockito.when(userRepository.findByEmail(emailId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getUserByEmail(emailId);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getEmail(),userDto.getEmail(),"Email not matched");
    }

    //search user
    @Test
    public void searchUserTest()
    {
        User  user1 = User.builder()
                .name("Dinesh")
                .email("shewale.deeps@gmail.com")
                .about("Thi is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("abcd")
                .build();

        User  user2 = User.builder()
                .name("Durva")
                .email("durva@gmail.com")
                .about("Thi is testing create method")
                .gender("Female")
                .imageName("abc.png")
                .password("abcd")
                .build();

        User  user3 = User.builder()
                .name("Deepali")
                .email("shewale.deeps@gmail.com")
                .about("Thi is testing create method")
                .gender("Female")
                .imageName("abc.png")
                .password("abcd")
                .build();

        String keywords="Deepali";

        Mockito.when(userRepository.findByNameContaining(keywords)).thenReturn(Arrays.asList(user,user1,user2,user3));
        List<UserDto> userDtos = userService.searchUser(keywords);
        Assertions.assertEquals(4,userDtos.size(),"Size not matched !!");
    }




}
