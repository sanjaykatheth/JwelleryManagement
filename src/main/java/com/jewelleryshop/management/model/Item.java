package com.jewelleryshop.management.model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Item {

	@Id
	private int itemId;
	private String itemName;
	private double weight;
	private String material;
	private StuddedStones studdedStones;
	private String typeOfStone;
	private double weightOfStones;
	private int numberOfStones;

	@Data
	static class StuddedStones {
		@Id
		private int id;
		private String stone;
		private double weightStone;
		private int numberOfStone;
	}
}

