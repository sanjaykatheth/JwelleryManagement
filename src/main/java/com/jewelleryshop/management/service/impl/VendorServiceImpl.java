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
	public Vendor createVendor(VendorUpdateRequest vendorRequest, MultipartFile businessCardUrl,
			MultipartFile profileImageUrl) {
		Vendor vendor = saveVendor(vendorRequest,businessCardUrl,profileImageUrl);
		Vendor updatedVendor = vendorRepository.updateVendorStage(vendor);
		return updatedVendor;
	}

	private Vendor saveVendor(VendorUpdateRequest vendorRequest,MultipartFile businessCardUrl,
			MultipartFile profileImageUrl) {
		logger.debug("Saving vendor with request: {}", vendorRequest);
		Vendor vendor;

		if (StringUtils.isEmpty(vendorRequest.getVendorId()) && VENDOR_SETUP.equals(vendorRequest.getStep())) {
			vendor = new Vendor();
		} else {
			if (StringUtils.isEmpty(vendorRequest.getVendorId())) {
				throw new IllegalArgumentException("Vendor ID is required");
			}

			vendor = vendorRepository.findById(vendorRequest.getVendorId());
			if (vendor == null) {
				throw new IllegalArgumentException("Vendor not found with ID: " + vendorRequest.getVendorId());
			}
		}
		if (vendorRequest.getStep() != null) {
			switch (vendorRequest.getStep()) {
			case VENDOR_SETUP:
				vendor.setStage(VendorStage.INITIAL);
				break;
			case CONTACT_DETAILS:
                ContactDetails contactDetails = vendorRequest.getContactDetails();
                if (contactDetails != null) {
                    // Set contact details
                    vendor.setContactDetails(contactDetails);

                    // Process and set business card URL if provided
                    if (businessCardUrl != null && !businessCardUrl.isEmpty()) {
                        String businessCardUrlStr = uploadFile(businessCardUrl);
                        contactDetails.setBusinessCardUrl(businessCardUrlStr);
                    }

                    // Process and set profile image URL if provided
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        String profileImageUrlStr = uploadFile(profileImageUrl);
                        contactDetails.setProfileImageUrl(profileImageUrlStr);
                    }
                }
			case FIRM_DETAILS:
				vendor.setFirmDetail(vendorRequest.getFirmDetail());
				break;
			case BANK_DETAILS:
				vendor.setBankDetailList(vendorRequest.getBankDetailList());
				break;
			case ACCOUNT_DEPARTMENT:
				vendor.setAccountDepartment(vendorRequest.getAccountDepartment());
				break;
			case PAYMENT_TERMS:
				vendor.setPaymentTerms(vendorRequest.getPaymentTerms());
				break;
			case GALLERY:
				vendor.setGallery(vendorRequest.getGallery());
				break;
			case VENDOR_STAGE:
				vendor.setStage(vendorRequest.getStage());
				break;
			default:
				throw new IllegalArgumentException("Invalid step");
			}
		}
		Vendor savedVendor = vendorRepository.save(vendor);
		logger.info("Vendor saved with ID: {}", savedVendor.getId());
		return savedVendor;
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
