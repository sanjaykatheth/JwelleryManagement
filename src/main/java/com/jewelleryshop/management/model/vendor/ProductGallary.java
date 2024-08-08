package com.jewelleryshop.management.model.vendor;


import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductGallary {

	private String id;
	private List<String> productImage;
	private String deal;
	private float rate;
	private String quality;
	private String desc;
	private Date date;

}
