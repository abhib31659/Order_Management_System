package com.ecommerce.user_service.service;

import com.ecommerce.user_service.dto.AuthResponse;
import com.ecommerce.user_service.dto.LoginRequest;
import com.ecommerce.user_service.dto.RegisterRequest;
import com.ecommerce.user_service.dto.UserProfileResponse;
import com.ecommerce.user_service.entity.Role;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.exception.UserNotFoundException;
import com.ecommerce.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ecommerce.user_service.security.JwtService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    //// This part is for New Registration for USER......
    public AuthResponse register(RegisterRequest request){

        if(userRepository.existsByEmail(request.getEmail())){

            return AuthResponse.builder()
                    .message("Email already registered !")
                    .build();
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .message("User Registered Successfully.")
                .build();
    }

    //// This part is for Login......
    public AuthResponse login(LoginRequest request){

        Optional<User> optionalUser =
                userRepository.findByEmail(
                        request.getEmail()
                );

        if(optionalUser.isEmpty()){
            return AuthResponse.builder()
                    .message("User not found !")
                    .build();
        }

        User user = optionalUser.get();

        boolean isPasswordValid =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if(!isPasswordValid){
            return AuthResponse.builder()
                    .message("Invalid Password !")
                    .build();
        }

        /// / JWT token generation when password gets verified and need to generate jwt token
        String jwtToken =
                jwtService.generateToken(
                        user.getEmail()
                );

        return AuthResponse.builder()
                .message("Login Successful")
                .token(jwtToken)
                .build();
    }

    /// /User profile details after authentication user details need to be fetched from user entity(DB)
    public UserProfileResponse getProfile(String email){
        User user =
                userRepository.findByEmail(email)
//                        .orElseThrow(() -> new RuntimeException(
//                                "User not found!"
//                                )  /*Before The Custom Global Exception Handler Class IN exception Package*/
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found!"
                                )
                        );

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

}
