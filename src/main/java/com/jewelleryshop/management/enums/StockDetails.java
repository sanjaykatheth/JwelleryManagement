package com.jewelleryshop.management.enums;

public enum StockDetails {
	GOLD(StockType.GOLD, 1,Unit.GM, TransactionType.CREDIT),SILVER(StockType.SILVER, 1,Unit.GM, TransactionType.CREDIT), PLATINUM(StockType.PLATINUM,1,Unit.GM, TransactionType.CREDIT), DIAMOND(StockType.DIAMOND,1,Unit.GM, TransactionType.CREDIT), GEMS(StockType.GEMS,1,Unit.GM, TransactionType.CREDIT), OTHERS(StockType.OTHERS,1,Unit.GM, TransactionType.CREDIT);
	public final StockType stockType;	
	public final int value;
    public final Unit unit;
    public final TransactionType transactionType;

    private StockDetails(StockType stockType, int value, Unit unit, TransactionType transactionType ) {
    	this.stockType = stockType;
        this.value = value;
        this.unit = unit;
        this.transactionType = transactionType;
    }
	
}
