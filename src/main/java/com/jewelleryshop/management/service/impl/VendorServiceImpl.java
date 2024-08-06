package com.jewelleryshop.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jewelleryshop.management.exception.ResourceNotFoundException;
import com.jewelleryshop.management.model.vendor.AccountDepartment;
import com.jewelleryshop.management.model.vendor.BankDetails;
import com.jewelleryshop.management.model.vendor.ContactDetails;
import com.jewelleryshop.management.model.vendor.FirmDetail;
import com.jewelleryshop.management.model.vendor.Product;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.repo.VendorRepository;
import com.jewelleryshop.management.service.VendorService;
import com.jewelleryshop.management.util.ImageUtil;

@Service
public class VendorServiceImpl implements VendorService {

	private static final Logger logger = LoggerFactory.getLogger(VendorServiceImpl.class);

	@Autowired
	private ImageUtil imageUtil;

	@Autowired
	private VendorRepository vendorRepository;

	public Vendor saveVendorContactDetails(String updateContactDetails, MultipartFile businessCardUrl,
			MultipartFile profileImageUrl) {
		logger.debug("Saving vendor with request: {}", updateContactDetails);

		Gson gsonObj = new Gson();
		ContactDetails vendorUpdateRequest = null;
		try {
			vendorUpdateRequest = gsonObj.fromJson(updateContactDetails, ContactDetails.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		if (vendorUpdateRequest == null) {
			logger.error("Failed to parse contact details");
			return null;
		}

		String imageId = UUID.randomUUID().toString();
		if (businessCardUrl != null && !businessCardUrl.isEmpty()) {
			String businessCardPath = imageUtil.saveImagePath(businessCardUrl, imageId);
			vendorUpdateRequest.setBusinessCardUrl(businessCardPath);
		}

		if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
			String profileImagePath = imageUtil.saveImagePath(profileImageUrl, imageId);
			vendorUpdateRequest.setProfileImageUrl(profileImagePath);
		}
		Vendor vendor = new Vendor();
		vendor.setContactDetails(vendorUpdateRequest);

		return vendorRepository.save(vendor);
	}

	@Override
	public void updateFirmDetails(String vendorId, List<FirmDetail> firmDetailList) {
		logger.debug("Updating firm details for vendor ID: {}", vendorId);
		if (vendorId == null) {
			throw new IllegalArgumentException("Vendor ID is missing from product gallery data");
		}
		Vendor vendor = vendorRepository.findById(vendorId);
		if (vendor != null) {
			for (FirmDetail firmDetail : firmDetailList) {
				firmDetail.setId(new ObjectId().toString());
			}
			vendor.setFirmDetail(firmDetailList);
			vendorRepository.save(vendor);
		} else {
			logger.error("Vendor not found with ID: {}", vendorId);
			throw new ResourceNotFoundException("Vendor not found with ID: " + vendorId);
		}
	}

	@Override
	public void updateVendorGallery(String vendorId, String productGalleryJson, List<MultipartFile> productImages) {
		logger.debug("Updating vendor gallery for vendor ID: {}", vendorId);
		Gson gson = new Gson();
		Product productGallery = null;
		try {
			productGallery = gson.fromJson(productGalleryJson, Product.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();

		}
		if (vendorId == null) {
			throw new IllegalArgumentException("Vendor ID is missing from product gallery data");
		}

		Vendor vendor = vendorRepository.findById(vendorId);
		if (!CollectionUtils.isEmpty(productImages)) {
			List<String> imageUrls = new ArrayList<>();
			for (MultipartFile productImage : productImages) {
				String imageId = UUID.randomUUID().toString();
				String imageUrl = imageUtil.saveImagePath(productImage, imageId);
				imageUrls.add(imageUrl);
			}

			productGallery.setProductImage(imageUrls);
		}
		vendor.setGallery(productGallery);
		vendorRepository.save(vendor);

	}

	@Override
	public void updateVendorBankDetails(String vendorId, List<BankDetails> bankDetails) {
		logger.debug("Updating vendor bank details for vendor ID: {}", vendorId);
		if (vendorId == null || bankDetails == null) {
			throw new IllegalArgumentException("Vendor ID or bank details are missing");
		}

		Vendor vendor = vendorRepository.findById(vendorId);
		if (vendor != null) {
			vendor.setBankDetailList(bankDetails);
			vendorRepository.save(vendor);
		} else {
			throw new ResourceNotFoundException("Vendor not found with ID: " + vendorId);
		}
	}

	@Override
	public void updateAccountDepartment(String vendorId, AccountDepartment accountDepartment) {
		logger.debug("Updating account department for vendor ID: {}", vendorId);
		if (vendorId == null || accountDepartment == null) {
			throw new IllegalArgumentException("Vendor ID or account department information is missing");
		}
		Vendor vendor = vendorRepository.findById(vendorId);
		if (vendor != null) {
			vendor.setAccountDepartment(accountDepartment);
			vendorRepository.save(vendor);
		} else {
			throw new ResourceNotFoundException("Vendor not found with ID: " + vendorId);
		}
	}

	@Override
	public Vendor getVendorById(String id) {
		logger.debug("Fetching vendor by ID: {}", id);
		Vendor vendor = vendorRepository.findById(id);
		if (vendor == null) {
			logger.error("Vendor not found with ID: {}", id);
			throw new ResourceNotFoundException("Vendor not found with ID: " + id);
		}
		return vendor;

	}

	@Override
	public Page<Vendor> findAllVendors(int page, int size) {
		logger.debug("Fetching all vendors with page: {}, size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		Page<Vendor> vendor = vendorRepository.findAllVendors(pageable);
		return vendor;
	}

	@Override
	public void deleteVendor(String vendorId) {
		logger.debug("Deleting vendor with ID: {}", vendorId);

		if (vendorId == null || !ObjectId.isValid(vendorId)) {
			logger.error("Invalid vendor ID format: {}", vendorId);
			throw new IllegalArgumentException("Invalid vendor ID format: " + vendorId);
		}

		Vendor vendor = vendorRepository.findById(vendorId);
		if (vendor != null) {
			vendorRepository.deleteByID(vendorId);
			logger.info("Vendor deleted successfully with ID: {}", vendorId);
		} else {
			logger.error("Vendor not found with ID: {}", vendorId);
			throw new ResourceNotFoundException("Vendor not found with ID: " + vendorId);
		}
	}
}