package com.jewelleryshop.management.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jewelleryshop.management.model.enums.VendorStage;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.model.vendor.VendorUpdateRequest;
import com.jewelleryshop.management.repo.VendorRepository;
import com.jewelleryshop.management.service.VendorService;

import io.micrometer.common.util.StringUtils;

@Service
public class VendorServiceImpl implements VendorService {

	private static final Logger logger = LoggerFactory.getLogger(VendorServiceImpl.class);

	@Autowired
	private VendorRepository vendorRepository;

	@Override
	public Vendor createVendor(VendorUpdateRequest vendorRequest) {
		Vendor vendor = saveVendor(vendorRequest);
		Vendor updatedVendor = vendorRepository.updateVendorStage(vendor);
		return updatedVendor;
	}

	private Vendor saveVendor(VendorUpdateRequest vendorRequest) {
        logger.debug("Saving vendor with request: {}", vendorRequest);
		Vendor vendor;

		if (StringUtils.isEmpty(vendorRequest.getVendorId())) {
			vendor = new Vendor();
			vendor.setStage(VendorStage.INITIAL);

		} else {
			vendor = vendorRepository.findById(vendorRequest.getVendorId());
			if (vendor == null) {
				throw new IllegalArgumentException("Vendor not found with ID: " + vendorRequest.getVendorId());
			}
		}
		if (vendorRequest.getStep() != null) {
			switch (vendorRequest.getStep()) {
			case CONTACT_DETAILS:
				vendor.setContactDetails(vendorRequest.getContactDetails());
				break;
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

}
