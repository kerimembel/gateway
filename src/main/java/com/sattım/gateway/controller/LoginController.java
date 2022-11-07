package com.sattım.gateway.controller;

import com.sattım.gateway.bean.auth.AuthResponse;
import com.sattım.gateway.bean.auth.LoginRequest;
import com.sattım.gateway.service.ILoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final ILoginService iLoginService;

    @CrossOrigin("*")
    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = iLoginService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return getAuthResponseResponseEntity(token);
    }

    @CrossOrigin("*")
    @PostMapping("/signout")
    @ResponseBody
    public ResponseEntity<AuthResponse> logout(@RequestHeader(value = "Authorization") String token) {
        HttpHeaders headers = new HttpHeaders();
        if (iLoginService.logout(token)) {
            return new ResponseEntity<>(new AuthResponse("logged out"), headers, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new AuthResponse("Logout Failed"), headers, HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/valid/token")
    @ResponseBody
    public Boolean isValidToken(@RequestHeader(value = "Authorization") String token) {
        return true;
    }

    @PostMapping("/signin/token")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<AuthResponse> createNewToken(@RequestHeader(value = "Authorization") String token) {
        String newToken = iLoginService.createNewToken(token);
        return getAuthResponseResponseEntity(newToken);
    }

    private ResponseEntity<AuthResponse> getAuthResponseResponseEntity(String newToken) {

        HttpHeaders headers = new HttpHeaders();
        List<String> headerList = new ArrayList<>();
        List<String> exposeList = new ArrayList<>();
        headerList.add("Content-Type");
        headerList.add(" Accept");
        headerList.add("X-Requested-With");
        headerList.add("Authorization");
        headers.setAccessControlAllowHeaders(headerList);
        exposeList.add("Authorization");
        headers.setAccessControlExposeHeaders(exposeList);
        headers.set("Authorization", newToken);
        return new ResponseEntity<>(new AuthResponse(newToken), headers, HttpStatus.CREATED);
    }
}
