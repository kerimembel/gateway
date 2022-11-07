package com.sattım.gateway.bean.auth;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Role {

    private Integer id;
    private String role;
}
