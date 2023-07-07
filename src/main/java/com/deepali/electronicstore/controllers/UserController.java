package com.deepali.electronicstore.controllers;

import com.deepali.electronicstore.dto.UserDto;
import com.deepali.electronicstore.paylods.ApiResponseMessage;
import com.deepali.electronicstore.paylods.AppConstants;
import com.deepali.electronicstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);


    /**
     * @author Deepali
     * @apiNote Creates a new user
     */
    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
    {
        logger.info("Initializing createUser method");
        UserDto userDto1 = userService.createUser(userDto);
        logger.info("Execution completed of method createUser");
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    /**
     * @author Deepali
     * @apiNote Updates existing user
     */
    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("userId") String userId,
            @RequestBody UserDto userDto)
    {
        logger.info("Initializing updateUser method for id:"+userId);
        UserDto updateUserDto = userService.updateUser(userDto, userId);
        logger.info("Execution completed of method updateUser for id:"+userId);
        return new ResponseEntity<>(updateUserDto,HttpStatus.OK);
    }

    /**
     * @author Deepali
     * @apiNote deletes user from database
     */
    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId)
    {
        logger.info("Initializing deleteUser method for id:"+userId);
        userService.deleteUser(userId);
        logger.info("Execution completed of method deleteUser for id:"+userId);
        return new ResponseEntity<>(AppConstants.DELETE_USER,HttpStatus.OK);
    }



    /**
     * @author Deepali
     * @apiNote Retrieves list of all users from database
     */
    //getall
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        logger.info("Initializing getAllUsers method");
        List<UserDto> allUsers = userService.getAllUsers();
        logger.info("Execution completed of method getAllUser");
        return new ResponseEntity<>(allUsers,HttpStatus.OK);

    }




    /**
     * @author Deepali
     * @apiNote Retrieves user by specified id
     */
    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId)
    {
        logger.info("Initializing getUserById method for id:"+userId);
        UserDto userById = userService.getUserById(userId);
        logger.info("Execution completed of method getUserById for id"+userById);
        return new ResponseEntity<>(userById,HttpStatus.OK);
    }

    /**
     * @author Deepali
     * @apiNote Retrieves user by specified email
     */
    //get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
    {
        logger.info("Initializing getUserByEmail method for email:"+email);
        UserDto userByEmail = userService.getUserByEmail(email);
        logger.info("Execution completed of method getUserByEmail for email"+userByEmail);
        return new ResponseEntity<>(userByEmail,HttpStatus.OK);
    }

    /**
     * @author Deepali
     * @apiNote Retrieves user by specified keyword
     */
    //search
    @GetMapping("/search/{keywords}")
    public ResponseEntity <List<UserDto>> searchUser(@PathVariable String keywords)
    {
        logger.info("Initializing searchUser method");
        List<UserDto> userDtos = userService.searchUser(keywords);
        logger.info("Execution completed of method searchUser for keyword"+keywords);
        return new ResponseEntity<>(userDtos,HttpStatus.OK);
    }

}
