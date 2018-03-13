package com.sales.notification.cache;

public class Sale {
	private String productType;
	private int quantity;
	private float totalSalesValue;
	private long timestamp;
	
	public Sale(String productType, int totalSales, float totalSalesValue) {
		super();
		this.productType = productType;
		this.quantity = totalSales;
		this.totalSalesValue = totalSalesValue;
		this.timestamp = System.nanoTime();
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getTotalSalesValue() {
		return totalSalesValue;
	}

	public void setTotalSalesValue(float totalSalesValue) {
		this.totalSalesValue = totalSalesValue;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
