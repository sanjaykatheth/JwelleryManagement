package com.jewelleryshop.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jewelleryshop.management.exception.ResourceNotFoundException;
import com.jewelleryshop.management.model.vendor.AccountDepartment;
import com.jewelleryshop.management.model.vendor.BankDetails;
import com.jewelleryshop.management.model.vendor.FirmDetail;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.service.VendorService;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@PostMapping
	public ResponseEntity<Vendor> createVendor(@RequestParam("vendorRequest") String vendorRequest,
			@RequestParam(value = "businessCardUrl", required = false) MultipartFile businessCardUrl,
			@RequestParam(value = "profileImageUrl", required = false) MultipartFile profileImageUrl) {
		Vendor vendor = vendorService.saveVendorContactDetails(vendorRequest, businessCardUrl, profileImageUrl);
		return new ResponseEntity<>(vendor, HttpStatus.CREATED);
	}

	@PutMapping("/{vendorId}/firm-details")
	public ResponseEntity<Void> updateFirmDetails(@PathVariable String vendorId,
			@RequestBody List<FirmDetail> firmDetail) {
		try {
			vendorService.updateFirmDetails(vendorId, firmDetail);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Resource not found
		}
	}

	@PutMapping("/{vendorId}/gallery")
	public ResponseEntity<Void> updateGallery(@PathVariable("vendorId") String vendorId,
			@RequestPart("productGallery") String productGallery,
			@RequestPart("productImages") List<MultipartFile> productImages) {

		vendorService.updateVendorGallery(vendorId, productGallery, productImages);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping("/{vendorId}/bank-details")
	public ResponseEntity<Void> updateBankDetails(@PathVariable("vendorId") String vendorId,
			@RequestBody List<BankDetails> bankDetails) {
		vendorService.updateVendorBankDetails(vendorId, bankDetails);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@PutMapping("/{vendorId}/account-department")
	public ResponseEntity<String> updateAccountDepartment(@PathVariable("vendorId") String vendorId,
			@RequestBody AccountDepartment accountDepartment) {

		vendorService.updateAccountDepartment(vendorId, accountDepartment);
		return ResponseEntity.ok("Account department updated successfully");
	}

	@DeleteMapping("/{vendorId}")
	public ResponseEntity<HttpStatus> deleteVendor(@PathVariable String vendorId) {
		vendorService.deleteVendor(vendorId);
		return ResponseEntity.ok(HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<Vendor> getVendor(@PathVariable String id) {
		Vendor vendor = vendorService.getVendorById(id);
		if (vendor != null) {
			return new ResponseEntity<>(vendor, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping
	public Page<Vendor> getAllVendors(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return vendorService.findAllVendors(page, size);
	}

	@GetMapping("/images/{filename}")
	public ResponseEntity<Resource> getImages(@PathVariable String filename) throws Exception {
		return vendorService.serveImages(filename);

	}
}
