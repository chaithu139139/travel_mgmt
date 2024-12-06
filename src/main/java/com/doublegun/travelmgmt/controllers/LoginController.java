package com.doublegun.travelmgmt.controllers;

import com.doublegun.travelmgmt.dto.ApiResponse;
import com.doublegun.travelmgmt.dto.LoginRequest;
import com.doublegun.travelmgmt.utils.JwtUtil;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest request) {
        logger.info("login() :: request :: {}", request);

        if (request==null || StringUtils.isEmpty(request.getEmail()) || StringUtils.isEmpty(request.getPassword()))
            return new ApiResponse("Invalid user details received!!!", null, HttpStatus.BAD_REQUEST.value());

        try {
            // Create authentication token using username and password
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

            // Authenticate the user using the AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // If authentication is successful, set the authentication in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT Token
            String jwtToken = jwtUtil.generateToken(authentication);

            // Return the token in the response
            return new ApiResponse("logged in successfully!!", jwtToken, HttpStatus.OK.value());
        } catch (Exception e){
            e.printStackTrace();
            return new ApiResponse(e.getLocalizedMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }
}
