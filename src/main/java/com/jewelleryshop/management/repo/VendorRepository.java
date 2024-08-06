package com.jewelleryshop.management.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jewelleryshop.management.model.vendor.Vendor;

public interface VendorRepository {
	Vendor save(Vendor vendor);

	Vendor findById(String id); // Changed to Optional<Vendor>

	List<Vendor> findAllVendors(Pageable pageable);

	Vendor deleteByID(String vendorId);
}