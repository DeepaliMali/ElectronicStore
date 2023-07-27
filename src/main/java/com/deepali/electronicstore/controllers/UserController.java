package com.deepali.electronicstore.controllers;

import com.deepali.electronicstore.dto.ImageResponse;
import com.deepali.electronicstore.dto.PageableResponse;
import com.deepali.electronicstore.dto.ProductDto;
import com.deepali.electronicstore.dto.UserDto;
import com.deepali.electronicstore.paylods.ApiResponseMessage;
import com.deepali.electronicstore.paylods.AppConstants;
import com.deepali.electronicstore.service.FileService;
import com.deepali.electronicstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);


    /**
     * @author Deepali
     * @apiNote Creates a new user
     */
    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid  @RequestBody UserDto userDto)
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
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value="pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value="sortBy",defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue =AppConstants.SORT_DIR,required = false) String sortDir

    )
    {
        logger.info("Initializing getAllUsers method");
        PageableResponse<UserDto> allUsers = userService.getAllUsers(pageNumber,pageSize,sortBy,sortDir);
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
        logger.info("Execution completed of method getUserById for id"+userId);
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
        logger.info("Execution completed of method getUserByEmail for email"+email);
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


    /**
     * @author Deepali
     * @apiNote Uploads user profile image
     */
    //upload user image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@RequestPart("userImage") MultipartFile image,
                                                     @PathVariable String userId) throws IOException {
        logger.info("Initializing uploadUserImage method of UserController for user"+userId);
        String imageName = fileService.uploadFile(image, imageUploadPath);

        UserDto user = userService.getUserById(userId);
        user.setImageName(imageName);

        UserDto userDto = userService.updateUser(user,userId);

        ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).message("Image Uploaded Successfully").build();
        logger.info("Execution completed of uploadUserImage method for user"+userId);
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    /**
     *
     * @author Deepali
     * @apiNote Serves User Profile image
     */
    //serve image
    @GetMapping(value = "image/{userId}")
    public  void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

        logger.info("Initializing serveUserImage method of UserController for user"+userId);
        UserDto user = userService.getUserById(userId);
        logger.info("User Image:{}",user.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        logger.info("Execution completed of serveUserImage method for user"+userId);



    }


}
