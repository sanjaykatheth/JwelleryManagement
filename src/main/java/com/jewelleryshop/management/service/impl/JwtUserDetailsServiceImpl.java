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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	 User user = userRepository.findByEmail(email)
                 .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return User.build(user);
    }
}