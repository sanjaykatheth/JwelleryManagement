package com.jewelleryshop.management.model.vendor;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public abstract class BalanceDetails {

	@Id
	private String id;
	private String type;
	private double value;
	private String unit; 
	private double debit; 
	private double credit;
}
