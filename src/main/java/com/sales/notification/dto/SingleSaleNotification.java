package com.sales.notification.dto;

public class SingleSaleNotification extends Notification {
	private float unitPrice;

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Override
	public String toString() {
		return "SingleSaleNotification [unitPrice=" + unitPrice + ", messageType=" + messageType + ", productName="
				+ productType + "]";
	}
	
}
