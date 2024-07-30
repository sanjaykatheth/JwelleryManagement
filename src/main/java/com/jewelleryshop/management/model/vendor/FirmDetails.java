package com.jewelleryshop.management.model.vendor;

import java.net.URI;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.jewelleryshop.management.model.enums.Dealer;


public class FirmDetails {
	
	private int id;
	private String name;
	private String pan;
	private String aadhaarCard;
	private String gstNo;
	private String aadress;
	private List<String> bestClient;
	private List<String> branches;
	private Dealer dealer;
	private URI link;
}
