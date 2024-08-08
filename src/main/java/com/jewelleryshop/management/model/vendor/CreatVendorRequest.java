package com.jewelleryshop.management.model.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatVendorRequest {
	private String firmName;
	private String firmType;
}
