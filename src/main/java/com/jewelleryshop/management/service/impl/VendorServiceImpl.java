package com.jewelleryshop.management.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jewelleryshop.management.model.enums.VendorStage;
import com.jewelleryshop.management.model.vendor.ContactDetails;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.model.vendor.VendorUpdateRequest;
import com.jewelleryshop.management.repo.VendorRepository;
import com.jewelleryshop.management.service.VendorService;
import static com.jewelleryshop.management.model.enums.VendorStep.*;
import io.micrometer.common.util.StringUtils;

@Service
public class VendorServiceImpl implements VendorService {

	private static final Logger logger = LoggerFactory.getLogger(VendorServiceImpl.class);

	@Autowired
	private VendorRepository vendorRepository;

	@Override
	public Vendor createVendor(ContactDetails vendorRequest, MultipartFile businessCardUrl,
			MultipartFile profileImageUrl) {
		Vendor vendor = saveVendor(vendorRequest, businessCardUrl, profileImageUrl);
		return vendor;
	}

	private Vendor saveVendor(ContactDetails contactDetails, MultipartFile businessCardUrl,
			MultipartFile profileImageUrl) {
		logger.debug("Saving vendor with request: {}", contactDetails);
		Vendor vendor = new Vendor();
		vendor.setContactDetails(contactDetails);
		return vendorRepository.save(vendor);
	}

	@Override
	public Vendor getVendorById(String id) {
		return vendorRepository.findById(id);
	}

	@Override
	public List<Vendor> findAllVendors() {
		List<Vendor> vendor = vendorRepository.findAllVendors();
		return vendor;
	}

	private String uploadFile(MultipartFile file) {
		// Implement your file upload logic here
		// For example, save the file to a server or cloud storage and return the URL
		// This is a placeholder implementation
		String fileName = file.getOriginalFilename();
		String fileUrl = "http://yourserver.com/uploads/" + fileName;
		// Save the file to the server or cloud storage
		return fileUrl;
	}

}
