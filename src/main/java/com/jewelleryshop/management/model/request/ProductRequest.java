package com.jewelleryshop.management.model.request;

import lombok.Data;

@Data
public class ProductRequest {
	private String deal;
	private float rate;
	private String quality;
	private String desc;
}