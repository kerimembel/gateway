package com.sattım.gateway.service.impl;

import com.sattım.gateway.bean.auth.JwtToken;
import com.sattım.gateway.bean.auth.Role;
import com.sattım.gateway.bean.auth.User;
import com.sattım.gateway.exception.CustomException;
import com.sattım.gateway.repository.JwtTokenRepository;
import com.sattım.gateway.repository.UserRepository;
import com.sattım.gateway.security.JwtTokenProvider;
import com.sattım.gateway.service.ILoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginService implements ILoginService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    password));
            User user = userRepository.findByEmail(username);
            if (user == null || user.getRole() == null || user.getRole().isEmpty()) {
                throw new CustomException("Invalid username or password.", HttpStatus.UNAUTHORIZED);
            }
            //NOTE: normally we don't need to add "ROLE_" prefix. Spring does automatically for us.
            //Since we are using custom token using JWT we should add ROLE_ prefix
            return jwtTokenProvider.createToken(username, user.getRole().stream()
                    .map((Role role) -> "ROLE_" + role.getRole()).collect(Collectors.toList()));

        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean logout(String token) {
        jwtTokenRepository.delete(new JwtToken(token));
        return true;
    }

    @Override
    public Boolean isValidToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public String createNewToken(String token) {
        String username = jwtTokenProvider.getUsername(token);
        List<String> roleList = jwtTokenProvider.getRoleList(token);
        return jwtTokenProvider.createToken(username, roleList);
    }
}
