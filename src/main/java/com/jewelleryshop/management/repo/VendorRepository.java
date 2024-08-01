package com.jewelleryshop.management.repo;

import java.util.List;

import com.jewelleryshop.management.model.vendor.Vendor;

public interface VendorRepository {
	Vendor save(Vendor vendor);

	Vendor findById(String id); // Changed to Optional<Vendor>

	List<Vendor> findAllVendors();

	Vendor updateVendorStage(Vendor savedVendor);
}