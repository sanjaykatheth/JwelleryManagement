package com.jewelleryshop.management.repo.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.jewelleryshop.management.model.Role;
import com.jewelleryshop.management.model.User;
import com.jewelleryshop.management.repo.UserServiceRepo;

@Service
public class UserServiceRepoImpl implements UserServiceRepo {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Override
	public User saveUser(User user) {
		return mongoTemplate.save(user);// TODO Auto-generated method stub
	}

	@Override
	public Optional<User> findByUsername(String username) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Boolean existsByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean existsByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<User> findByRolesIn(Set<Role> roles, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
