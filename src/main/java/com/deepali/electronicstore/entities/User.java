package com.deepali.electronicstore.entities;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name="users")
public class User {

    @Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    private String userId;

    @Column(name="user_name",unique = true)
    private String name;
    private String email;

    @Column(name="user_password")
    private String password;

    private String gender;

    @Column(length=1000)
    private String about;

    @Column(name="user_imageName")
    private String imageName;



}
