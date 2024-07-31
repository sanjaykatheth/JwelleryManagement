package com.jewelleryshop.management.model.vendor;

import java.net.URI;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.jewelleryshop.management.enums.Designation;

import lombok.Data;

@Data
public class DealerInfo {

	@Id
	private String id;
	private Designation desgination;
	private String name;
	private String phoneNo;
	private String email;
	private List<URI> dealPersonPic;
	private List<URI> businessCardPic; 
	
}
