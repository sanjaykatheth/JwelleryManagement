package com.jewelleryshop.management.service.impl;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.jewelleryshop.management.model.vendor.ProductGallary;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.repo.VendorRepository;
import com.jewelleryshop.management.service.VendorService;
import com.jewelleryshop.management.util.ImageUtil;

@Service
public class VendorServiceImpl implements VendorService {

	private static final Logger logger = LoggerFactory.getLogger(VendorServiceImpl.class);

	@Value("${image.path}")
	private String imagePathPrefix;

	@Autowired
	private ImageUtil imageUtil;

	@Autowired
	private VendorRepository vendorRepository;

	@Value("${image.path.prefix}")
	private String baseUrl;

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
		ProductGallary productGallery = null;
		try {
			productGallery = gson.fromJson(productGalleryJson, ProductGallary.class);
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
		ContactDetails contactDetail = vendor.getContactDetails();

		// Set the business card URL
		String businessCardUrl = contactDetail.getBusinessCardUrl();
		if (businessCardUrl != null && !businessCardUrl.isEmpty()) {
			// Normalize the path to use forward slashes
			String normalizedBusinessCardUrl = businessCardUrl.replace("\\", "/");
			contactDetail.setBusinessCardUrl(baseUrl + "/images/" + normalizedBusinessCardUrl);
		}

		// Set the profile image URL
		String profileImageUrl = contactDetail.getProfileImageUrl();
		if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
			// Normalize the path to use forward slashes
			String normalizedProfileImageUrl = profileImageUrl.replace("\\", "/");
			contactDetail.setProfileImageUrl(baseUrl + "/images/" + normalizedProfileImageUrl);
		}
		ProductGallary gallary = vendor.getGallery();
		if (gallary == null) {
			gallary = new ProductGallary();
			vendor.setGallery(gallary);// Create an empty gallery
		}
		List<String> productImageUrls = gallary.getProductImage();
	    if (productImageUrls != null) {
	        // Normalize and update each URL in the list
	        List<String> updatedProductImageUrls = productImageUrls.stream()
	            .filter(url -> url != null && !url.isEmpty()) // Ensure URL is not null or empty
	            .map(url -> {
	                // Normalize the path to use forward slashes
	                String normalizedUrl = url.replace("\\", "/");
	                return baseUrl + "/images/" + normalizedUrl;
	            })
	            .collect(Collectors.toList());

	        gallary.setProductImage(updatedProductImageUrls);
	    }

	    return vendor;
	}



	@Override
	public Page<Vendor> findAllVendors(int page, int size) {
	    logger.debug("Fetching all vendors with page: {}, size: {}", page, size);
	    Pageable pageable = PageRequest.of(page, size);
	    List<Vendor> vendorList = vendorRepository.findAllVendors(pageable);
	    vendorList.forEach(vendor -> {
	        ContactDetails contactDetail = vendor.getContactDetails();
	      

	        // Process business card URL
	        String businessCardUrl = contactDetail.getBusinessCardUrl();
	        if (businessCardUrl != null && !businessCardUrl.isEmpty()) {
	            String normalizedBusinessCardUrl = businessCardUrl.replace("\\", "/");
	            contactDetail.setBusinessCardUrl(baseUrl + "/images/" + normalizedBusinessCardUrl);
	        }

	        // Process profile image URL
	        String profileImageUrl = contactDetail.getProfileImageUrl();
	        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
	            String normalizedProfileImageUrl = profileImageUrl.replace("\\", "/");
	            contactDetail.setProfileImageUrl(baseUrl + "/images/" + normalizedProfileImageUrl);
	        }
	        ProductGallary gallery = vendor.getGallery();
	        if (gallery == null) {
	            gallery = new ProductGallary(); // Create an empty gallery
	            vendor.setGallery(gallery); // Ensure the gallery is set in the vendor
	        }
	        // Process product images
	        List<String> productImageUrls = gallery.getProductImage();
	        if (productImageUrls != null) {
	            List<String> updatedProductImageUrls = productImageUrls.stream()
	                .filter(url -> url != null && !url.isEmpty()) // Ensure URL is not null or empty
	                .map(url -> {
	                    // Normalize the path to use forward slashes
	                    String normalizedUrl = url.replace("\\", "/");
	                    return baseUrl + "/images/" + normalizedUrl;
	                })
	                .collect(Collectors.toList());
	            gallery.setProductImage(updatedProductImageUrls);
	        }
	    });

	    // Return the updated Page<Vendor>
	    return new PageImpl<>(vendorList);
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

	@Override
	public ResponseEntity<Resource> serveImages(String filename) {
		String imageUrl = imagePathPrefix + "/" + filename;
		Path path = Paths.get(imageUrl);
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);

	}
}