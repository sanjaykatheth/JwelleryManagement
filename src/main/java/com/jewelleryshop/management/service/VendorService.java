package com.jewelleryshop.management.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.model.vendor.VendorUpdateRequest;

public interface VendorService {
	Vendor createVendor(VendorUpdateRequest vendorUpdateRequest, MultipartFile businessCardUrl, MultipartFile profileImageUrl);

	Vendor getVendorById(String id);

	List<Vendor> findAllVendors();

}
