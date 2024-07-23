package com.jewelleryshop.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jewelleryshop.management.model.User;
import com.jewelleryshop.management.repo.impl.UserServiceRepoImpl;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    
	@Autowired
	UserServiceRepoImpl userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return User.build(user);
    }
}