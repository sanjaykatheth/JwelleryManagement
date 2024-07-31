package com.jewelleryshop.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelleryshop.management.model.vendor.ContactDetails;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.service.VendorService;

@RestController
@RequestMapping("/api/vendors/")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@PostMapping
	public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
		Vendor createdVendor = vendorService.createVendor(vendor);
		return new ResponseEntity<>(createdVendor, HttpStatus.CREATED);
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

	@PatchMapping("{id}/contact")
	public ResponseEntity<Vendor> updateVendorContactDetails(@PathVariable String id,
			@RequestBody ContactDetails contactDetails) {
		Vendor updatedVendor = vendorService.updateVendorContactDetails(id, contactDetails);
		if (updatedVendor != null) {
			return new ResponseEntity<>(updatedVendor, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	
	public List<Vendor> getAllVendors() {
        return vendorService.findAllVendors(); // Retrieves all vendors
    }
}