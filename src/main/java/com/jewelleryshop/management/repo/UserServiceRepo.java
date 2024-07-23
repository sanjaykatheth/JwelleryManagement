package com.jewelleryshop.management.repo;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jewelleryshop.management.model.Role;
import com.jewelleryshop.management.model.User;

public interface UserServiceRepo {
    User saveUser(User user);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Page<User> findByRolesIn(Set<Role> roles, Pageable pageable);

    Optional<User> findByEmail(String email);
}