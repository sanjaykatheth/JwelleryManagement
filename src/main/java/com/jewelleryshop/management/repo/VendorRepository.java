package com.jewelleryshop.management.repo;

import java.util.List;
import java.util.Optional;

import com.jewelleryshop.management.model.vendor.Vendor;

public interface VendorRepository {
	Vendor save(Vendor vendor);

	Vendor findById(String id);

	List<Vendor> findAllVendors();
}