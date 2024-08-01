package com.jewelleryshop.management.service;

import java.util.List;

import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.model.vendor.VendorUpdateRequest;

public interface VendorService {
	Vendor createVendor(VendorUpdateRequest vendorUpdateRequest);

	Vendor getVendorById(String id);

	List<Vendor> findAllVendors();

}
