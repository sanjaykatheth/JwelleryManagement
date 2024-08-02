package com.jewelleryshop.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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

	public void saveVendorContactDetails(String updateContactDetails, MultipartFile businessCardUrl,
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
			return;
		}

		// Save files and update URLs
		if (businessCardUrl != null && !businessCardUrl.isEmpty()) {
			String businessCardPath = imageUtil.saveFile(businessCardUrl, "business_cards");
			vendorUpdateRequest.setBusinessCardUrl(businessCardPath);
		}

		if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
			String profileImagePath = imageUtil.saveFile(profileImageUrl, "profile_images");
			vendorUpdateRequest.setProfileImageUrl(profileImagePath);
		}
		Vendor vendor = new Vendor();
		vendor.setContactDetails(vendorUpdateRequest);

		vendorRepository.save(vendor);
	}

	@Override
	public void updateFirmDetails(String vendorId, FirmDetail firmDetail) {
		if (vendorId == null) {
			throw new IllegalArgumentException("Vendor ID is missing from product gallery data");
		}
		Vendor vendor = vendorRepository.findById(vendorId);
		if (vendor != null) {
			vendor.setFirmDetail(firmDetail);
			vendorRepository.save(vendor);
		}
	}

	@Override
	public void updateVendorGallery(String vendorId, String productGalleryJson, List<MultipartFile> productImages) {
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
			List<String> imageUrls = new ArrayList<>(); // Store image URLs
			for (MultipartFile productImage : productImages) {
				String imageId = UUID.randomUUID().toString();
				String imageUrl = imageUtil.saveFile(productImage, imageId);
				imageUrls.add(imageUrl); // Add URL to list
			}

			productGallery.setProductImage(imageUrls);
		}
		vendor.setGallery(productGallery);
		vendorRepository.save(vendor);

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
