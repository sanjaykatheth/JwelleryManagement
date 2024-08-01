package com.jewelleryshop.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.model.vendor.VendorUpdateRequest;
import com.jewelleryshop.management.service.VendorService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@PostMapping
	public ResponseEntity<Vendor> createVendor(@RequestParam("vendorRequest") String vendorRequestString,
			@RequestParam("businessCardUrl") MultipartFile businessCardUrl,
			@RequestParam("profileImageUrl") MultipartFile profileImageUrl) {

		Gson gsonObj = new Gson();
		VendorUpdateRequest vendorUpdateRequest = null;
		try {
			vendorUpdateRequest = gsonObj.fromJson(vendorRequestString, VendorUpdateRequest.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	    Vendor createdVendor = vendorService.createVendor(vendorUpdateRequest, businessCardUrl, profileImageUrl);
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

	@GetMapping
	public List<Vendor> getAllVendors() {
		return vendorService.findAllVendors(); // Retrieves all vendors
	}
}