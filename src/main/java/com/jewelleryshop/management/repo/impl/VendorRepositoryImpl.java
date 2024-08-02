package com.jewelleryshop.management.repo.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.jewelleryshop.management.exception.VendorNotFoundException;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.repo.VendorRepository;

@Service
public class VendorRepositoryImpl implements VendorRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Vendor save(Vendor vendor) {
		return mongoTemplate.save(vendor);
	}

	@Override
	public Vendor findById(String id) {

		Vendor vendor = mongoTemplate.findById(new ObjectId(id), Vendor.class);
		if (vendor == null) {
			throw new VendorNotFoundException("Vendor not found with ID: " + vendor.getId());
		}
		return vendor;
	}

	@Override
	public List<Vendor> findAllVendors() {
		return mongoTemplate.findAll(Vendor.class);
	}

}