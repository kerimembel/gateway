package com.sattım.gateway.bean.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class JwtToken {

    @Id
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }
}
