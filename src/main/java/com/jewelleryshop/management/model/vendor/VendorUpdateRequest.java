package com.jewelleryshop.management.model.vendor;

import java.util.List;

import com.jewelleryshop.management.model.enums.VendorStage;
import com.jewelleryshop.management.model.enums.VendorStep;

import lombok.Data;

@Data
public class VendorUpdateRequest {
	private String vendorId;
	private VendorStep step;
	private ContactDetails contactDetails;
	private FirmDetail firmDetail;
	private List<BankDetails> bankDetailList;
	private AccountDepartment accountDepartment;
	private List<ProductGallary> gallery;
	private VendorStage stage;
}
