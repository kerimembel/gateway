package com.sattım.gateway.bean.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
@Getter
@Setter
public class User {
    @Id
    private String id;
    @Email(message = "*Please provide a valid email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    @NotEmpty(message = "*Please provide your name")
    private String password;
    @NotEmpty(message = "*Please provide your name")
    private String name;
    @NotEmpty(message = "*Please provide your last name")
    private String lastName;
    private Integer active = 1;
    private boolean isLocked = false;
    private boolean isExpired = false;
    private boolean isEnabled = true;
    private Set<Role> role;

}
