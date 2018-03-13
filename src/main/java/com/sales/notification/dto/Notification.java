package com.sales.notification.dto;

import com.sales.notification.enums.MessageTypeEnum;

public abstract class Notification {
	protected MessageTypeEnum messageType;
	protected String productType;
	
	public MessageTypeEnum getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageTypeEnum messageType) {
		this.messageType = messageType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
}
