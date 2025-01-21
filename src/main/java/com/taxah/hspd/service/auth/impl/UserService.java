package com.taxah.hspd.service.auth.impl;

import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.repository.auth.UserRepository;
import com.taxah.hspd.util.constant.Exceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(Exceptions.USER_NOT_FOUND_F, username)));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(Exceptions.USER_NOT_FOUND_F, username)));
    }
}
