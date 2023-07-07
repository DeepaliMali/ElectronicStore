package com.deepali.electronicstore.dto;

import com.deepali.electronicstore.validate.ImageNameValid;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {


    private String userId;

    @Size(min=3,max=20,message ="Invalid Name !!" )
    private String name;

   // @Email(message = "Invalid User Email!!")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",message = "Invalid UserEmail !!")
    @NotBlank(message = "Email is required !!")

    private String email;

    @NotBlank(message = "Password is required !!")
    private String password;

    @Size(min=4,max=6,message = "Invalid Gender !!")
    private String gender;

    @NotBlank(message = "Write something about yourself !!")
    private String about;

    @ImageNameValid
    private String imageName;


}
