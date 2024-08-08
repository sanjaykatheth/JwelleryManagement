package com.jewelleryshop.management.model.vendor;

import lombok.Data;

@Data
public abstract class BalanceDetails {

	private String type;
	private double value;
	private String unit;
	private double debit;
	private double credit;
}
