package com.jewelleryshop.management.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jewelleryshop.management.model.vendor.BankDetails;
import com.jewelleryshop.management.model.vendor.FirmDetail;
import com.jewelleryshop.management.model.vendor.Vendor;

public interface VendorService {

	Vendor getVendorById(String id);

	List<Vendor> findAllVendors();

	void updateFirmDetails(String vendorId, FirmDetail firmDetail);

	void updateVendorGallery(String vendorId, String productGalleryJson2, List<MultipartFile> productImages);

	void saveVendorContactDetails(String vendorRequestString, MultipartFile businessCardUrl,
			MultipartFile profileImageUrl);

	void updateVendorBankDetails(String vendorId, List<BankDetails> bankDetails);

}
