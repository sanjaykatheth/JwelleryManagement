package com.jewelleryshop.management.repo.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.jewelleryshop.management.model.enums.VendorStage;
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
		return mongoTemplate.findById(new ObjectId(id), Vendor.class);
	}

	@Override
	public List<Vendor> findAllVendors() {
		return mongoTemplate.findAll(Vendor.class);
	}

	@Override
	public Vendor updateVendorStage(Vendor vendor) {
		VendorStage newStage = determineNewStage(vendor);
		Query query = new Query(Criteria.where("id").is(vendor.getId()));
		Update update = new Update().set("stage", newStage);
		mongoTemplate.updateFirst(query, update, Vendor.class);
		return mongoTemplate.findOne(query, Vendor.class);
	}

	private VendorStage determineNewStage(Vendor vendor) {
		switch (vendor.getStage()) {
		case INITIAL:
			if (vendor.getContactDetails() != null) {
				return VendorStage.CONTACT;
			}
			break;
		case CONTACT:
			if (vendor.getContactDetails() != null) {
				return VendorStage.FIRM_DETAIL;
			}
			break;
		case FIRM_DETAIL:
			if (vendor.getFirmDetail() != null) {
				return VendorStage.BANK_DETAILS;
			}
			break;
		// Add remaining cases for other stages
		default:
			break;
		}
		return vendor.getStage(); // Return existing stage if no change
	}
}