package com.jewelleryshop.management.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.jewelleryshop.management.model.Vendor;
import com.jewelleryshop.management.repo.VendorRepository;

@Service
public class VendorRepositoryImpl implements VendorRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Vendor save(Vendor vendor) {
		return mongoTemplate.save(vendor);
	}

}
