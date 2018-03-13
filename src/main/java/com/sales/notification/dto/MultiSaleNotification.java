package com.sales.notification.dto;

public class MultiSaleNotification extends Notification {
	private float unitPrice;
	private int unitCount;

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getUnitCount() {
		return unitCount;
	}

	public void setUnitCount(int unitCount) {
		this.unitCount = unitCount;
	}

	@Override
	public String toString() {
		return "MultiSaleNotification [unitPrice=" + unitPrice + ", unitCount=" + unitCount + ", messageType="
				+ messageType + ", productName=" + productType + "]";
	}
	
}
