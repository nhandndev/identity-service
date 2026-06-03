package com.nhan.identity_service.entity;

import com.nhan.identity_service.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @NotBlank(message = "Email is required")
    @Email
    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 4, max = 50, message = "Password must be between 4 and 50 characters")
    @Column(name = "password",nullable = false)
    private String password;

    @NotBlank(message = "firstName is required")
    @Column(name = "firstName",nullable = false)
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Column(name = "lastName",nullable = false)
    private String lastName;

    LocalDateTime createdAt;

//    @ManyToMany
    private Set<Role> roles;

}
