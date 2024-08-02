package com.jewelleryshop.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jewelleryshop.management.exception.ResourceNotFoundException;
import com.jewelleryshop.management.model.vendor.FirmDetail;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.service.VendorService;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@PostMapping
	public ResponseEntity<Vendor> createVendor(@RequestParam("vendorRequest") String vendorRequestString,
			@RequestParam("businessCardUrl") MultipartFile businessCardUrl,
			@RequestParam("profileImageUrl") MultipartFile profileImageUrl) {
		vendorService.saveVendorContactDetails(vendorRequestString, businessCardUrl, profileImageUrl);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/{vendorId}/firm-details")
	public ResponseEntity<Void> updateFirmDetails(@PathVariable String vendorId, @RequestBody FirmDetail firmDetail) {
		try {
			vendorService.updateFirmDetails(vendorId, firmDetail);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Resource not found
		}
	}

	@PutMapping("/{vendorId}/gallery")
	public ResponseEntity<String> updateGallery(@PathVariable("vendorId") String vendorId,
			@RequestParam("productGallery") String productGallery,
			@RequestParam("productImages") List<MultipartFile> productImages) {

		vendorService.updateVendorGallery(vendorId, productGallery, productImages);
		return null;
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
	public List<Vendor> getAllVendors() {
		return vendorService.findAllVendors(); // Retrieves all vendors
	}
}