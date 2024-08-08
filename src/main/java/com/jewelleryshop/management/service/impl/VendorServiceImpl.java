package com.jewelleryshop.management.service.impl;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
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
import com.google.gson.reflect.TypeToken;
import com.jewelleryshop.management.exception.ResourceNotFoundException;
import com.jewelleryshop.management.model.vendor.AccountDepartment;
import com.jewelleryshop.management.model.vendor.BankDetails;
import com.jewelleryshop.management.model.vendor.ContactDetails;
import com.jewelleryshop.management.model.vendor.CreatVendorRequest;
import com.jewelleryshop.management.model.vendor.FirmDetail;
import com.jewelleryshop.management.model.vendor.ProductGallary;
import com.jewelleryshop.management.model.vendor.SearchVendorRequest;
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

	@Override
	public Vendor saveVendorFirmInfo(CreatVendorRequest creatVendorRequest) {
		Vendor vendor = new Vendor();
		vendor.setFirmName(creatVendorRequest.getFirmName());
		vendor.setFirmType(creatVendorRequest.getFirmType());
		return vendorRepository.save(vendor);
	}

	public void saveVendorContactDetails(String vendorId, String updateContactDetails, MultipartFile businessCardUrl,
			MultipartFile profileImageUrl) {
		logger.debug("Saving vendor with request: {}", updateContactDetails);

		Gson gsonObj = new Gson();
		List<ContactDetails> newContactDetails = null;
		try {
			newContactDetails = gsonObj.fromJson(updateContactDetails, new TypeToken<List<ContactDetails>>() {
			}.getType());
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		if (newContactDetails == null) {
			logger.error("Failed to parse contact details");
		}
		Vendor vendor = vendorRepository.findById(vendorId);
		String imageId = UUID.randomUUID().toString();
		if (businessCardUrl != null && !businessCardUrl.isEmpty()) {
			String businessCardPath = imageUtil.saveImagePath(businessCardUrl, imageId);
			for (ContactDetails contactDetail : newContactDetails) {
				contactDetail.setBusinessCardUrl(businessCardPath);
			}
		}

		if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
			String profileImagePath = imageUtil.saveImagePath(profileImageUrl, imageId);
			for (ContactDetails contactDetail : newContactDetails) {
				contactDetail.setProfileImageUrl(profileImagePath);
			}
		}

		// Update existing contact details or add new ones
		List<ContactDetails> existingContactDetails = vendor.getContactDetails();
		if (existingContactDetails == null) {
			existingContactDetails = new ArrayList<>();
		}
		existingContactDetails.addAll(newContactDetails);
		vendor.setContactDetails(existingContactDetails);
		vendorRepository.save(vendor);
	}

	@Override
	public void updateFirmDetails(String vendorId, FirmDetail firmDetail) {
		logger.debug("Updating firm details for vendor ID: {}", vendorId);
		if (vendorId == null) {
			throw new IllegalArgumentException("Vendor ID is missing from product gallery data");
		}
		Vendor vendor = vendorRepository.findById(vendorId);
		if (vendor == null) {
			logger.error("Vendor not found with ID: {}", vendorId);
			throw new ResourceNotFoundException("Vendor not found with ID: " + vendorId);
		}
		vendor.setFirmDetail(firmDetail);
		vendorRepository.save(vendor);
	}

	@Override
	public void updateVendorGallery(String vendorId, String productGalleryJson, List<MultipartFile> productImages) {
		logger.debug("Updating vendor gallery for vendor ID: {}", vendorId);

		Gson gson = new Gson();

		// Input Validation
		if (vendorId == null || vendorId.isEmpty()) {
			throw new IllegalArgumentException("Vendor ID is missing from product gallery data");
		}

		List<ProductGallary> updatedProductGallery = null;
		try {
			updatedProductGallery = gson.fromJson(productGalleryJson, new TypeToken<List<ProductGallary>>() {
			}.getType());
		} catch (JsonSyntaxException e) {
			logger.error("Invalid product gallery JSON format", e);
		}

		// Fetch Vendor and Existing Gallery (Handle Null Cases)
		Vendor vendor = vendorRepository.findById(vendorId);

		List<ProductGallary> existingProductGallery = vendor.getGallery();
		if (existingProductGallery == null) {
			existingProductGallery = new ArrayList<>();
		}

		if (!CollectionUtils.isEmpty(productImages)) {
			List<String> imageUrls = new ArrayList<>();
			for (MultipartFile productImage : productImages) {
				String imageId = UUID.randomUUID().toString();
				String imageUrl = imageUtil.saveImagePath(productImage, imageId);
				imageUrls.add(imageUrl);
			}

			for (ProductGallary productGallery : updatedProductGallery) {
				 if (productGallery.getProductImage() == null) {
			            productGallery.setProductImage(new ArrayList<>());
			        }
			        productGallery.getProductImage().addAll(imageUrls);
			    }
		}

		// Update Vendor Gallery and Save
		existingProductGallery.addAll(updatedProductGallery);
		vendor.setGallery(existingProductGallery);
		vendorRepository.save(vendor);

		logger.debug("Vendor gallery for vendor ID: {} updated successfully", vendorId);
	}

	@Override
	public void updateVendorBankDetails(String vendorId, List<BankDetails> bankDetails) {
		logger.debug("Updating vendor bank details for vendor ID: {}", vendorId);
		if (vendorId == null || bankDetails == null) {
			throw new IllegalArgumentException("Vendor ID or bank details are missing");
		}

		Vendor vendor = vendorRepository.findById(vendorId);
		if (vendor != null) {
			List<BankDetails> existingBankDetails = vendor.getBankDetailList();
			if (existingBankDetails == null) {
				existingBankDetails = new ArrayList<>();
			}
			for (BankDetails bankDetail : bankDetails) {
				bankDetail.validateAccountNumbers();
				bankDetail.setId(new ObjectId().toString()); // Generate a new ID
				existingBankDetails.add(bankDetail); // Add new bank detail to the existing list
			}

			vendor.setBankDetailList(existingBankDetails);
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
			accountDepartment.setId(new ObjectId().toString());
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

		// Process contact details
		List<ContactDetails> contactDetails = vendor.getContactDetails();
		if (contactDetails != null) {
			for (ContactDetails contactDetail : contactDetails) {
				String businessCardUrl = contactDetail.getBusinessCardUrl();
				if (businessCardUrl != null && !businessCardUrl.isEmpty()) {
					String normalizedBusinessCardUrl = businessCardUrl.replace("\\", "/");
					contactDetail.setBusinessCardUrl(baseUrl + "/images/" + normalizedBusinessCardUrl);
				}

				String profileImageUrl = contactDetail.getProfileImageUrl();
				if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
					String normalizedProfileImageUrl = profileImageUrl.replace("\\", "/");
					contactDetail.setProfileImageUrl(baseUrl + "/images/" + normalizedProfileImageUrl);
				}
			}
		}

		List<ProductGallary> galleryList = vendor.getGallery();
		if (galleryList == null) {
			galleryList = new ArrayList<>();
			vendor.setGallery(galleryList); // Initialize with an empty list if null
		}

		for (ProductGallary gallery : galleryList) {
			if (gallery != null && gallery.getProductImage() != null) {
				List<String> productImageUrls = gallery.getProductImage().stream()
						.filter(url -> url != null && !url.isEmpty())
						.map(url -> baseUrl + "/images/" + url.replace("\\", "/")).collect(Collectors.toList());
				gallery.setProductImage(productImageUrls);
			}
		}

		return vendor;
	}

	public Page<Vendor> findAllVendors(int page, int size) {
		logger.debug("Fetching all vendors with page: {}, size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		List<Vendor> vendorList = vendorRepository.findAllVendors(pageable);

		// Process each vendor and contact details
		for (Vendor vendor : vendorList) {
			List<ContactDetails> contactDetails = vendor.getContactDetails();
			if (contactDetails != null) {
				for (ContactDetails contactDetail : contactDetails) {
					String businessCardUrl = contactDetail.getBusinessCardUrl();
					if (businessCardUrl != null && !businessCardUrl.isEmpty()) {
						String normalizedBusinessCardUrl = businessCardUrl.replace("\\", "/");
						contactDetail.setBusinessCardUrl(baseUrl + "/images/" + normalizedBusinessCardUrl);
					}

					String profileImageUrl = contactDetail.getProfileImageUrl();
					if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
						String normalizedProfileImageUrl = profileImageUrl.replace("\\", "/");
						contactDetail.setProfileImageUrl(baseUrl + "/images/" + normalizedProfileImageUrl);
					}
				}
			}
			List<ProductGallary> galleryList = vendor.getGallery();
			if (galleryList == null) {
				galleryList = new ArrayList<>();
				vendor.setGallery(galleryList); // Initialize with an empty list if null
			}

			for (ProductGallary gallery : galleryList) {
				if (gallery != null && gallery.getProductImage() != null) {
					List<String> productImageUrls = gallery.getProductImage().stream()
							.filter(url -> url != null && !url.isEmpty())
							.map(url -> baseUrl + "/images/" + url.replace("\\", "/")).collect(Collectors.toList());
					gallery.setProductImage(productImageUrls);
				}
			}
		}

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

	@Override
	public Page<Vendor> searchVendor(SearchVendorRequest vendorSearchRequest, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return vendorRepository.searchVendor(vendorSearchRequest, pageable);

	}

	@Override
	public void updateFirmDetails(String vendorId, List<FirmDetail> firmDetail) {
		// TODO Auto-generated method stub

	}

}