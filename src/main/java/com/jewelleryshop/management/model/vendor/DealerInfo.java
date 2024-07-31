package com.jewelleryshop.management.model.vendor;

import java.net.URI;
import java.util.List;

import com.jewelleryshop.management.enums.Designation;

public class DealerInfo {

	private int id;
	private Designation desgination;
	private String name;
	private String phoneNo;
	private String email;
	private List<URI> dealPersonPic;
	private List<URI> businessCardPic; 
	
}
