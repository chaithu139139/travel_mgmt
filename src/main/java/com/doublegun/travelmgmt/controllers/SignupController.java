package com.doublegun.travelmgmt.controllers;

import com.doublegun.travelmgmt.dto.ApiResponse;
import com.doublegun.travelmgmt.model.User;
import com.doublegun.travelmgmt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SignupController {

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ApiResponse registerUser(@RequestBody User user) {
        logger.info("registerUser() :: user :: {}", user);

        if(user==null)
            return new ApiResponse("Invalid user details received!!!", null, HttpStatus.BAD_REQUEST.value());

        try {
             userService.registerUser(user);
            return new ApiResponse("signed up successfully!!", null, HttpStatus.OK.value());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(e.getLocalizedMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
