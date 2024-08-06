package com.jewelleryshop.management.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.jewelleryshop.management.model.vendor.AccountDepartment;
import com.jewelleryshop.management.model.vendor.BankDetails;
import com.jewelleryshop.management.model.vendor.FirmDetail;
import com.jewelleryshop.management.model.vendor.SearchVendorRequest;
import com.jewelleryshop.management.model.vendor.Vendor;

public interface VendorService {

	Vendor getVendorById(String id);

	Page<Vendor> findAllVendors(int page, int size);

	void updateVendorGallery(String vendorId, String productGalleryJson2, List<MultipartFile> productImages);

	Vendor saveVendorContactDetails(String vendorRequestString, MultipartFile businessCardUrl,
			MultipartFile profileImageUrl);

	void updateVendorBankDetails(String vendorId, List<BankDetails> bankDetails);

	void updateAccountDepartment(String vendorId, AccountDepartment accountDepartment);

	void updateFirmDetails(String vendorId, List<FirmDetail> firmDetail);

	void deleteVendor(String vendorId);

	ResponseEntity<Resource> serveImages(String filenames);

	Page<Vendor> searchVendor(SearchVendorRequest vendorSearchRequest, int page, int size);

}
