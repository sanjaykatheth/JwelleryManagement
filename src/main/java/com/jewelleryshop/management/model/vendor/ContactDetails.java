package com.jewelleryshop.management.model.vendor;

import java.net.URI;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.jewelleryshop.management.model.enums.Designation;

@Entity
public class ContactDetails {

	@Id
	private int id;
	private Designation degination;
	private String name;
	private String phoneNo;
	private String email;
	private List<URI> images;
	@OneToOne(mappedBy ="contactdetails")
	private Vendor vendor;
}
