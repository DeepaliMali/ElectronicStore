package com.deepali.electronicstore.service;


import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.dto.UserDto;

import java.util.List;

public interface UserService {


    //create
    UserDto createUser(UserDto userDto);

    //update
    UserDto  updateUser(UserDto userDto, String userId);


    //delete
    void deleteUser(String userId);

    //get all users
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String userId);

    //get single user by email
    UserDto getUserByEmail(String email);

    //search user
    List<UserDto> searchUser(String keyword);


}
