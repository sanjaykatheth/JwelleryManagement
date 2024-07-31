package com.jewelleryshop.management.service;

import java.util.List;
import java.util.Optional;

import com.jewelleryshop.management.model.vendor.ContactDetails;
import com.jewelleryshop.management.model.vendor.Vendor;

public interface VendorService {
	Vendor createVendor(Vendor vendor);

	Vendor getVendorById(String id);

	Vendor updateVendorContactDetails(String id, ContactDetails contactDetails);

	List<Vendor> findAllVendors();

}
