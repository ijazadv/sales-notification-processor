package com.sales.notification.handler;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sales.notification.dto.MultiSaleNotification;
import com.sales.notification.dto.Notification;
import com.sales.notification.dto.SalesAdjustmentNotification;
import com.sales.notification.dto.SingleSaleNotification;
import com.sales.notification.enums.MessageTypeEnum;

/**
 * 
 * @author mia
 *
 */
public class NotificationDeserializer implements JsonDeserializer<Notification> {

	@Override
	public Notification deserialize(JsonElement jsonElement, Type type,
			JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		String messageType = jsonObject.get("messageType").getAsString();

		if (MessageTypeEnum.SS == MessageTypeEnum.valueOf(messageType)) {
			return jsonDeserializationContext.deserialize(jsonElement, SingleSaleNotification.class);
			
		} else if (MessageTypeEnum.MS == MessageTypeEnum.valueOf(messageType)) {
			return jsonDeserializationContext.deserialize(jsonElement, MultiSaleNotification.class);
			
		} else if (MessageTypeEnum.SA == MessageTypeEnum.valueOf(messageType)) {
			return jsonDeserializationContext.deserialize(jsonElement, SalesAdjustmentNotification.class);
		}
		
		
		throw new JsonParseException("Illegal message type: "+messageType);
	}

}
