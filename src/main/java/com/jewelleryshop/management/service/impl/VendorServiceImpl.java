package com.jewelleryshop.management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jewelleryshop.management.model.enums.VendorStage;
import com.jewelleryshop.management.model.vendor.ContactDetails;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.repo.VendorRepository;
import com.jewelleryshop.management.service.VendorService;

@Service
public class VendorServiceImpl implements VendorService {

	@Autowired
	private VendorRepository vendorRepository;

	@Override
	public Vendor createVendor(Vendor vendor) {

		vendor.setStage(VendorStage.CONTACT);
		return vendorRepository.save(vendor);
	}

	@Override
	public Vendor getVendorById(String id) {
		return vendorRepository.findById(id);
	}

	public Vendor updateVendorContactDetails(String id, ContactDetails contactDetails) {
		Vendor vendor = vendorRepository.findById(id);

		if (vendor != null) {
			// Update contact details and move to next stage
			vendor.setContactDetails(contactDetails);
			vendor = moveToNextStage(vendor); // Update the stage based on the updated field
			return vendorRepository.save(vendor);
		} else {
			// Handle the case where the vendor is not found
			return null;
		}
	}

	@Override
	public List<Vendor> findAllVendors() {
		List<Vendor> vendor = vendorRepository.findAllVendors();
		return vendor;
	}

	public Vendor moveToNextStage(Vendor vendor) {
		switch (vendor.getStage()) {
		case CONTACT:
			if (vendor.getContactDetails() != null) {
				vendor.setStage(VendorStage.DETAILS);
			}
			break;
		case DETAILS:
			if (vendor.getDetails() != null) {
				vendor.setStage(VendorStage.FORM_DETAIL);
			}
			break;
		case FORM_DETAIL:
			if (vendor.getFormDetail() != null) {
				vendor.setStage(VendorStage.BANK_DETAILS);
			}
			break;
		case BANK_DETAILS:
			if (vendor.getBankDetailList() != null) {
				vendor.setStage(VendorStage.ACCOUNT_DEPARTMENT);
			}
			break;
		case ACCOUNT_DEPARTMENT:
			if (vendor.getAccountDepartment() != null) {
				vendor.setStage(VendorStage.PAYMENT_TERMS);
			}
			break;
		case PAYMENT_TERMS:
			if (vendor.getPaymentTerms() != null) {
				vendor.setStage(VendorStage.GALLERY);
			}
			break;
		case GALLERY:
			if (vendor.getGallery() != null && !vendor.getGallery().isEmpty()) {
				vendor.setStage(VendorStage.COMPLETED);
			}
			break;
		default:
			break;
		}
		return vendor;
	}

}
