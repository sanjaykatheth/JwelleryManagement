package com.jewelleryshop.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.jewelleryshop.management.model.User;

@Service
public class UserServiceImpl {

	@Autowired
	private MongoTemplate mongoTemplate;

	public User saveUser(User user) {
		return mongoTemplate.save(user);
	}
}
