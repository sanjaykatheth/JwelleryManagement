package com.jewelleryshop.management.model.vendor;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "vendors")
public class Vendor {
	@Id
	private String id;
	private ContactDetails contactDetails;
	private List<DealerInfo> details;
	private List<FirmDetail> firmDetail;
	private List<BankDetails> bankDetailList;
	private AccountDepartment accountDepartment;
	private Product gallery;

}