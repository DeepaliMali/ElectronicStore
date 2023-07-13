package com.deepali.electronicstore.service.impl;
import com.deepali.electronicstore.controllers.UserController;
import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.dto.UserDto;
import com.deepali.electronicstore.entities.User;
import com.deepali.electronicstore.exception.ResourceNotFoundException;
import com.deepali.electronicstore.helper.Helper;
import com.deepali.electronicstore.paylods.AppConstants;
import com.deepali.electronicstore.repository.UserRepository;
import com.deepali.electronicstore.service.UserService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class userServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    /**
     * @author Deepali
     * @apiNote Creates new User
     */
    @Override
    public UserDto createUser(UserDto userDto) {

        logger.info("Initializing createUser method of Service");
        //generate unique id
        String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);
        User user=mapper.map(userDto,User.class);
        User savedUser=userRepository.save(user);
        UserDto newDto=mapper.map(user,UserDto.class);
        logger.info("Execution completed of createUser method");
        return newDto;

    }

    /**
     * @author Deepali
     * @apiNote Updates existing user
     */
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        logger.info("Initializing updateUser method of Service for id:"+userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with this id"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        User updatedUser = userRepository.save(user);
        UserDto updatedDto = mapper.map(user, UserDto.class);
        logger.info("Execution completed of updateUser method of service for id:"+userId);
        return updatedDto;
    }

    /**
     * @author Deepali
     * @apiNote Deletes User from Database
     */
    @Override
    public void deleteUser(String userId) {

        logger.info("Initializing deleteUser method of Service for id:"+userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with this id"));
        userRepository.delete(user);
        logger.info("Execution completed of deleteUser method of service for id:"+userId);

    }


    /**
     * @author Deepali
     * @apiNote Retrieves list of all users from Database
     */
    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        logger.info("Initializing getAllUsers method of Service");

        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        //pageNumber starts from 0
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class);
        logger.info("Execution completed of getAllUsers method of service");
        return pageableResponse;

    }

    /**
     * @author Deepali
     * @apiNote Retrieves a user by specified id
     */
    @Override
    public UserDto getUserById(String userId) {

        logger.info("Initializing getUserById method of service for id:"+userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with this id"));
        logger.info("Execution completed of getUserById method of service for id:"+userId);
        return mapper.map(user,UserDto.class);
    }

    /**
     * @author Deepali
     * @apiNote Retrieves a user by specified email
     */
    @Override
    public UserDto getUserByEmail(String email) {

        logger.info("Initializing getUserByEmail method of service for email:"+email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with this email"));
        logger.info("Execution completed of getUserByEmail method of service for id:"+email);
        return mapper.map(user,UserDto.class);
    }

    /**
     * @author Deepali
     * @apiNote Retrieves a user by specified keyword
     */
    @Override
    public List<UserDto> searchUser(String keyword) {

        logger.info("Initializing  searchUser method of service");
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> mapper.map(user,UserDto.class)).collect(Collectors.toList());
        logger.info("Execution completed of searchUser method of service");
        return dtoList;
    }


}
