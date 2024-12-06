package com.doublegun.travelmgmt.service;

import com.doublegun.travelmgmt.model.User;
import com.doublegun.travelmgmt.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {

        String email = user.getEmail();
        String password = user.getPassword();
        String username = user.getUsername();

        if (StringUtils.isEmpty(username))
            throw new RuntimeException("invalid username received!!!");
        if (StringUtils.isEmpty(password) || password.length() < 5)
            throw new RuntimeException("invalid password received!!!");
        if (StringUtils.isEmpty(email))
            throw new RuntimeException("invalid email received!!!");

        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            throw new RuntimeException("email already signedUP.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch the user from the database (use username to look up the user)
        User userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
    }
}
