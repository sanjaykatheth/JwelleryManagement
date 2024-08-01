package com.jewelleryshop.management.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jewelleryshop.management.model.vendor.ContactDetails;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.model.vendor.VendorUpdateRequest;

public interface VendorService {
	Vendor createVendor(ContactDetails contactDetails, MultipartFile businessCardUrl, MultipartFile profileImageUrl);

	Vendor getVendorById(String id);

	List<Vendor> findAllVendors();

}
