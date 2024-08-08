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
import com.jewelleryshop.management.model.vendor.CreatVendorRequest;
import com.jewelleryshop.management.model.vendor.FirmDetail;
import com.jewelleryshop.management.model.vendor.SearchVendorRequest;
import com.jewelleryshop.management.model.vendor.Vendor;
import com.jewelleryshop.management.service.VendorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@Operation(summary = "Create a new vendor", description = "Creates a new vendor with the provided details", responses = {
			@ApiResponse(responseCode = "201", description = "Vendor created successfully", content = @Content(schema = @Schema(implementation = Vendor.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input") })
	@PostMapping("/create")
	public ResponseEntity<Vendor> createVendor(@RequestBody CreatVendorRequest creatVendorRequest) {
		Vendor vendor = vendorService.saveVendorFirmInfo(creatVendorRequest);
		return new ResponseEntity<>(vendor, HttpStatus.CREATED);
	}

	@Operation(summary = "Update the vendor's contact details", description = "Updates the contact details of an existing vendor", responses = {
			@ApiResponse(responseCode = "200", description = "Contact details updated successfully"),
			@ApiResponse(responseCode = "404", description = "Vendor not found") })
	@PutMapping("/{vendorId}/contact-details")
	public ResponseEntity<Void> updateVendorContactDetails(@PathVariable String vendorId,
			@RequestParam("updateContact") String updateContact,
			@RequestParam(value = "businessCardUrl", required = false) MultipartFile businessCardUrl,
			@RequestParam(value = "profileImageUrl", required = false) MultipartFile profileImageUrl) {
		vendorService.saveVendorContactDetails(vendorId, updateContact, businessCardUrl, profileImageUrl);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Update vendor firm details", description = "Updates the firm details of an existing vendor", responses = {
			@ApiResponse(responseCode = "200", description = "Firm details updated successfully"),
			@ApiResponse(responseCode = "404", description = "Vendor not found") })
	@PutMapping("/{vendorId}/firm-details")
	public ResponseEntity<Void> updateVendorFirmDetails(@PathVariable String vendorId,
			@RequestBody FirmDetail firmDetail) {
		try {
			vendorService.updateFirmDetails(vendorId, firmDetail);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Update vendor gallery", description = "Updates the product gallery of an existing vendor", responses = {
			@ApiResponse(responseCode = "200", description = "Gallery updated successfully"),
			@ApiResponse(responseCode = "404", description = "Vendor not found") })
	@PutMapping("/{vendorId}/gallery")
	public ResponseEntity<Void> updateVendorGallery(@PathVariable("vendorId") String vendorId,
			@RequestPart("productGallery") String productGallery,
			@RequestPart("productImages") List<MultipartFile> productImages) {

		vendorService.updateVendorGallery(vendorId, productGallery, productImages);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Update vendor bank details", description = "Updates the bank details of an existing vendor", responses = {
			@ApiResponse(responseCode = "200", description = "Bank details updated successfully"),
			@ApiResponse(responseCode = "404", description = "Vendor not found") })
	@PutMapping("/{vendorId}/bank-details")
	public ResponseEntity<Void> updateVendorBankDetails(@Valid @PathVariable("vendorId") String vendorId,
			@RequestBody List<BankDetails> bankDetails) {
		vendorService.updateVendorBankDetails(vendorId, bankDetails);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@Operation(summary = "Update vendor account department", description = "Updates the account department details of an existing vendor", responses = {
			@ApiResponse(responseCode = "200", description = "Account department updated successfully"),
			@ApiResponse(responseCode = "404", description = "Vendor not found") })
	@PutMapping("/{vendorId}/account-department")
	public ResponseEntity<Void> updateVendorAccountDepartment(@PathVariable("vendorId") String vendorId,
			@RequestBody AccountDepartment accountDepartment) {

		vendorService.updateAccountDepartment(vendorId, accountDepartment);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Delete a vendor", description = "Deletes an existing vendor", responses = {
			@ApiResponse(responseCode = "200", description = "Vendor deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Vendor not found") })
	@DeleteMapping("/{vendorId}")
	public ResponseEntity<HttpStatus> deleteVendor(@PathVariable String vendorId) {
		vendorService.deleteVendor(vendorId);
		return ResponseEntity.ok(HttpStatus.OK);

	}

	@Operation(summary = "Get vendor by ID", description = "Retrieves a vendor by its ID", responses = {
			@ApiResponse(responseCode = "200", description = "Vendor retrieved successfully", content = @Content(schema = @Schema(implementation = Vendor.class))),
			@ApiResponse(responseCode = "404", description = "Vendor not found") })
	@GetMapping("/{id}")
	public ResponseEntity<Vendor> getVendor(@PathVariable String id) {
		Vendor vendor = vendorService.getVendorById(id);
		if (vendor != null) {
			return new ResponseEntity<>(vendor, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Get all vendors", description = "Retrieves a paginated list of all vendors", responses = {
			@ApiResponse(responseCode = "200", description = "List of vendors retrieved successfully", content = @Content(schema = @Schema(implementation = Vendor.class))), })
	@GetMapping
	public Page<Vendor> getAllVendors(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return vendorService.findAllVendors(page, size);
	}

	@GetMapping("/images/{filename}")
	public ResponseEntity<Resource> getImages(@PathVariable String filename) throws Exception {
		return vendorService.serveImages(filename);

	}

	@PostMapping("/search")
	public Page<Vendor> searchVendor(@RequestBody SearchVendorRequest vendorSearchRequest,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws Exception {
		return vendorService.searchVendor(vendorSearchRequest, page, size);

	}
}
