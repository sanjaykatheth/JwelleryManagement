package com.jewelleryshop.management.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Lot {
	@Id
    private String id;
    private String vendorId;
    private String typeOfLot;
    private double sizeOfLot;
    private List<Item> itemsIncluded;
    private double priceOfLot;
    private String dateOfPurchase;
    private String status;

}
