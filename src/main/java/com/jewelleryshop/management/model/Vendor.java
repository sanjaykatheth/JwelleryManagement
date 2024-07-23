package com.jewelleryshop.management.model;

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
	private String name;
	private String phoneNumber;
	private String emailAddress;
	private String photograph;
	private String address;
	private String businessType;
	private Lot lotDetails;
}