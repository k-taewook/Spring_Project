package com.mysite.career.review.user.service;

import com.mysite.career.review.user.dto.UserDto;
import com.mysite.career.review.user.entity.User;
import com.mysite.career.review.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(@Valid UserDto userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword1()))
                .email(userDto.getEmail())
                .gender(userDto.getGender())
                .department(userDto.getDepartment())
                .registration(userDto.getRegistration())
                .build();

        userRepository.save(user);
    }

    public User getUser(String name) {
        User user = userRepository.findByUsername(name).orElseThrow(() -> new EntityNotFoundException("해당 사용자가 존재하지 않습니다. : " + name));
        return user;
    }
}
