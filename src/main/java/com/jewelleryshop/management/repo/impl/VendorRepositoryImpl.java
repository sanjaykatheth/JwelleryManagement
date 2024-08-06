package com.jewelleryshop.management.repo.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.jewelleryshop.management.exception.VendorNotFoundException;
import com.jewelleryshop.management.model.vendor.SearchVendorRequest;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.repo.VendorRepository;
import com.jewelleryshop.management.service.impl.VendorServiceImpl;

@Service
public class VendorRepositoryImpl implements VendorRepository {

	private static final Logger logger = LoggerFactory.getLogger(VendorServiceImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Vendor save(Vendor vendor) {
		return mongoTemplate.save(vendor);
	}

	@Override
	public Vendor findById(String id) {
		if (id == null || !ObjectId.isValid(id)) {
			throw new VendorNotFoundException("Invalid ID format: " + id);
		}
		Vendor vendor = mongoTemplate.findById(new ObjectId(id), Vendor.class);
		if (vendor == null) {
			throw new VendorNotFoundException("Vendor not found with ID: ");
		}
		return vendor;
	}

	@Override
	public List<Vendor> findAllVendors(Pageable pageable) {
		Query query = new Query();
		long count = mongoTemplate.count(query, Vendor.class); // Get total count of records
		List<Vendor> vendors = mongoTemplate.find(query.with(pageable), Vendor.class);
		return vendors;
	}

	@Override
	public Vendor deleteByID(String vendorId) {
		if (vendorId == null || !ObjectId.isValid(vendorId)) {
			throw new IllegalArgumentException("Invalid vendor ID format: " + vendorId);
		}

		Query query = new Query(Criteria.where("_id").is(new ObjectId(vendorId)));
		Vendor vendor = mongoTemplate.findAndRemove(query, Vendor.class);

		if (vendor == null) {
			throw new VendorNotFoundException("Vendor not found with ID: " + vendorId);
		}

		return vendor;
	}

	@Override
	public  Page<Vendor> searchVendor(SearchVendorRequest vendorSearchRequest ,Pageable pageable) {
		Criteria criteria = new Criteria();

		// Get the search key and value from the request
		String name = vendorSearchRequest.getName();

		if (name != null && !name.isEmpty()) {
			Criteria nameCriteria = Criteria.where("contactDetails.name").regex(".*" + name + ".*", "i");
			criteria.andOperator(nameCriteria);
		}
		Query query = new Query(criteria);
        long count = mongoTemplate.count(query, Vendor.class);
        List<Vendor> vendors = mongoTemplate.find(query.with(pageable), Vendor.class);
        
        return new PageImpl<>(vendors, pageable, count);

	}

}