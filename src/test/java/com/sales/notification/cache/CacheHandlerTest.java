package com.sales.notification.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sales.notification.dto.MultiSaleNotification;
import com.sales.notification.dto.Notification;
import com.sales.notification.dto.SalesAdjustmentNotification;
import com.sales.notification.dto.SingleSaleNotification;
import com.sales.notification.enums.MessageTypeEnum;
import com.sales.notification.enums.OperationEnum;

public class CacheHandlerTest {
	private static final String APPLE = "apple";
	private static final float APPLE_PRICE = 20f;

	private static final String ORANGE = "orange";
	private static final float ORANGE_PRICE_16 = 16f;
	private static final float ORANGE_PRICE_30 = 30f;

	private static final String CHAIR = "chair";
	private static final float CHAIR_PRICE = 12000f;

	@Before
	public void cleanup() {
		CacheHandler.getInstance().getAllSales().clear();
		CacheHandler.getInstance().getSalesAdjustments().clear();

		Assert.assertEquals(0, CacheHandler.getInstance().getAllSales().size());
		Assert.assertEquals(0, CacheHandler.getInstance().getProductSalesReport().size());
		Assert.assertEquals(0, CacheHandler.getInstance().getSalesAdjustments().size());
	}

	@Test
	public void singleSaleNotfication() {
		// Populate cache
		Notification notification = buildSingleSaleNotification(APPLE, APPLE_PRICE);
		CacheHandler.getInstance().updateCache(notification);

		notification = buildSingleSaleNotification(ORANGE, ORANGE_PRICE_16);
		CacheHandler.getInstance().updateCache(notification);

		notification = buildSingleSaleNotification(CHAIR, CHAIR_PRICE);
		CacheHandler.getInstance().updateCache(notification);

		notification = buildSingleSaleNotification(ORANGE, ORANGE_PRICE_30);
		CacheHandler.getInstance().updateCache(notification);

		// validate cache sizes
		Assert.assertEquals(4, CacheHandler.getInstance().getAllSales().size());
		Assert.assertEquals(3, CacheHandler.getInstance().getProductSalesReport().size());

		// validate item quantity
		Assert.assertEquals(2, CacheHandler.getInstance().getProductSalesReport().get(ORANGE).get().getQuantity());

		// validate sale values
		Assert.assertEquals(CHAIR_PRICE, CacheHandler.getInstance().getProductSalesReport().get(CHAIR).get().getTotalSalesValue(), 0f);
		Assert.assertEquals(ORANGE_PRICE_16 + ORANGE_PRICE_30, CacheHandler.getInstance().getProductSalesReport().get(ORANGE).get().getTotalSalesValue(), 0f);

	}

	@Test
	public void multiSaleNotfication() {
		// Populate cache
		Notification notification = buildMultiSaleNotification(APPLE, APPLE_PRICE, 1);
		CacheHandler.getInstance().updateCache(notification);

		notification = buildMultiSaleNotification(ORANGE, ORANGE_PRICE_16, 2);
		CacheHandler.getInstance().updateCache(notification);

		notification = buildMultiSaleNotification(CHAIR, CHAIR_PRICE, 4);
		CacheHandler.getInstance().updateCache(notification);

		notification = buildMultiSaleNotification(APPLE, APPLE_PRICE, 3);
		CacheHandler.getInstance().updateCache(notification);

		// validate cache sizes
		Assert.assertEquals(4, CacheHandler.getInstance().getAllSales().size());
		Assert.assertEquals(3, CacheHandler.getInstance().getProductSalesReport().size());

		// validate item quantity
		Assert.assertEquals(2, CacheHandler.getInstance().getProductSalesReport().get(ORANGE).get().getQuantity());
		Assert.assertEquals(4, CacheHandler.getInstance().getProductSalesReport().get(APPLE).get().getQuantity());
		Assert.assertEquals(4, CacheHandler.getInstance().getProductSalesReport().get(CHAIR).get().getQuantity());

		// validate sale values
		Assert.assertEquals(CHAIR_PRICE * 4, CacheHandler.getInstance().getProductSalesReport().get(CHAIR).get().getTotalSalesValue(), 0f);
		Assert.assertEquals(ORANGE_PRICE_16 * 2, CacheHandler.getInstance().getProductSalesReport().get(ORANGE).get().getTotalSalesValue(), 0f);
		Assert.assertEquals((APPLE_PRICE * 3) + APPLE_PRICE, CacheHandler.getInstance().getProductSalesReport().get(APPLE).get().getTotalSalesValue(), 0f);

	}

	@Test
	public void saleAdjustmentsNotfication() {
		// Populate cache
		// single sale
		Notification notification = buildSingleSaleNotification(APPLE, APPLE_PRICE);
		CacheHandler.getInstance().updateCache(notification);

		notification = buildSingleSaleNotification(ORANGE, ORANGE_PRICE_16);
		CacheHandler.getInstance().updateCache(notification);

		// multisale
		notification = buildMultiSaleNotification(CHAIR, CHAIR_PRICE, 4);
		CacheHandler.getInstance().updateCache(notification);

		notification = buildMultiSaleNotification(APPLE, APPLE_PRICE, 3);
		CacheHandler.getInstance().updateCache(notification);

		// sales adjustment
		notification = buildSaleAdjustmentsNotification(ORANGE, OperationEnum.ADD, 2);
		CacheHandler.getInstance().updateCache(notification);

		notification = buildSaleAdjustmentsNotification(CHAIR, OperationEnum.MULTIPLY, 4);
		CacheHandler.getInstance().updateCache(notification);

		notification = buildSaleAdjustmentsNotification(APPLE, OperationEnum.SUBTRACT, 3);
		CacheHandler.getInstance().updateCache(notification);

		// validate cache sizes
		Assert.assertEquals(4, CacheHandler.getInstance().getAllSales().size());
		Assert.assertEquals(3, CacheHandler.getInstance().getProductSalesReport().size());
		Assert.assertEquals(3, CacheHandler.getInstance().getSalesAdjustments().size());

		// validate item quantity
		Assert.assertEquals(1, CacheHandler.getInstance().getProductSalesReport().get(ORANGE).get().getQuantity());
		Assert.assertEquals(4, CacheHandler.getInstance().getProductSalesReport().get(APPLE).get().getQuantity());
		Assert.assertEquals(4, CacheHandler.getInstance().getProductSalesReport().get(CHAIR).get().getQuantity());

		// validate sale values
		Assert.assertEquals(CHAIR_PRICE * 4 * 4, CacheHandler.getInstance().getProductSalesReport().get(CHAIR).get().getTotalSalesValue(), 0f);
		Assert.assertEquals(ORANGE_PRICE_16 + 2, CacheHandler.getInstance().getProductSalesReport().get(ORANGE).get().getTotalSalesValue(), 0f);
		Assert.assertEquals(APPLE_PRICE - 3 + (APPLE_PRICE * 3) - 3, CacheHandler.getInstance().getProductSalesReport().get(APPLE).get().getTotalSalesValue(), 0f);

	}

	private Notification buildSingleSaleNotification(String productType, float unitPrice) {
		SingleSaleNotification notification = new SingleSaleNotification();
		notification.setMessageType(MessageTypeEnum.SS);
		notification.setProductType(productType);
		notification.setUnitPrice(unitPrice);

		return notification;
	}

	private Notification buildMultiSaleNotification(String productType, float unitPrice, int quantity) {
		MultiSaleNotification notification = new MultiSaleNotification();
		notification.setMessageType(MessageTypeEnum.MS);
		notification.setProductType(productType);
		notification.setUnitPrice(unitPrice);
		notification.setUnitCount(quantity);

		return notification;
	}

	private Notification buildSaleAdjustmentsNotification(String productType, OperationEnum operation, float amount) {
		SalesAdjustmentNotification notification = new SalesAdjustmentNotification();
		notification.setMessageType(MessageTypeEnum.SA);
		notification.setProductType(productType);
		notification.setAdjustmentOperation(operation);
		notification.setAdjustmentAmount(amount);

		return notification;
	}
}
