package com.jewelleryshop.management.model.vendor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Vendor {

	@Id
	private String id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "contactdetails_id", referencedColumnName = "id")
	private ContactDetails contactDetails;
}
