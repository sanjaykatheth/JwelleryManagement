package com.jewelleryshop.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelleryshop.management.model.Student;
import com.jewelleryshop.management.model.Vendor;
import com.jewelleryshop.management.service.VendorService;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@Autowired
	private  Student student;
	
	@PostMapping
	public Vendor createVendor(@RequestBody Vendor vendor) {
		 System.out.println(student);
		 return vendorService.saveVendor(vendor);
	}
}