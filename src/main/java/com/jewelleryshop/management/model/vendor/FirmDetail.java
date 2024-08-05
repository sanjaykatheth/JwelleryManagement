package com.jewelleryshop.management.model.vendor;

import java.util.List;

import com.jewelleryshop.management.model.enums.DealerType;
import com.jewelleryshop.management.model.enums.Designation;

import lombok.Data;

@Data
public class FirmDetail {

	private String name;
	private String panNumber;
	private String aadhaarNumber;
	private String gstNo;
	private String address;
	private List<Client> clientList;
	private List<Branch> branchList;
	private List<DealerType> dealerType;
}
