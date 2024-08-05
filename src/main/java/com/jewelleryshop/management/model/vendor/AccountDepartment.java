package com.jewelleryshop.management.model.vendor;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDepartment {
	@Id
	private String id;
	private String pendingOrderValue;
	private String readyOrderValue;
	private OpeningBalance openingBalance;
	private CurrentBalance currentBalancel;
	private TotalPurchaseTillDate totalPurchaseTillDate;
}
