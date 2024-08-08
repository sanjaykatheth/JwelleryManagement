package com.jewelleryshop.management.model.vendor;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDepartment {

	private String id;
	private String pendingOrderValue;
	private String readyOrderValue;
	private List<OpeningBalance> openingBalance;
	private List<CurrentBalance> currentBalance;
	private List<TotalPurchaseTillDate> totalPurchaseTillDate;
}
