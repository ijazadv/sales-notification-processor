package com.sales.notification.dto;

import com.sales.notification.enums.OperationEnum;

public class SalesAdjustmentNotification extends Notification {
	private OperationEnum adjustmentOperation;
	private float adjustmentAmount;

	public OperationEnum getAdjustmentOperation() {
		return adjustmentOperation;
	}

	public void setAdjustmentOperation(OperationEnum adjustmentOperation) {
		this.adjustmentOperation = adjustmentOperation;
	}

	public float getAdjustmentAmount() {
		return adjustmentAmount;
	}

	public void setAdjustmentAmount(float adjustmentAmount) {
		this.adjustmentAmount = adjustmentAmount;
	}

	@Override
	public String toString() {
		return "SalesAdjustmentNotification [adjustmentOperation=" + adjustmentOperation + ", adjustmentAmount="
				+ adjustmentAmount + ", messageType=" + messageType + ", productType="
				+ productType + "]";
	}
	
}
