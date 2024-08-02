package com.jewelleryshop.management.model.vendor;


import java.util.List;

import lombok.Data;

@Data
public class Product {

	private List<String> productImage;
	private String deal;
	private float rate;
	private String quality;
	private String desc;

}
