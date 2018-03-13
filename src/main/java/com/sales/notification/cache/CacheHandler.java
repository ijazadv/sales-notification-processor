package com.sales.notification.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sales.notification.dto.MultiSaleNotification;
import com.sales.notification.dto.Notification;
import com.sales.notification.dto.SalesAdjustmentNotification;
import com.sales.notification.dto.SingleSaleNotification;
import com.sales.notification.enums.OperationEnum;

/**
 * A handler to maintain Sales cache.
 * 
 * @author mia
 *
 */
public class CacheHandler {
	// private static Map<Long, Sale> timestampToSaleMap = new HashMap<>();
	private static List<Sale> allSales = new ArrayList<>();
	private static Map<String, Optional<Sale>> productTypeToSaleMap = new HashMap<>();
	private static List<SalesAdjustmentNotification> salesAdjustmentNotifications = new ArrayList<>();

	private CacheHandler() {
	}

	/**
	 * 
	 * @param notification
	 * 
	 */
	public static void updateCache(Notification notification) {
		if (notification instanceof SingleSaleNotification) {
			SingleSaleNotification singleSale = (SingleSaleNotification) notification;
			Sale sale = new Sale(singleSale.getProductType(), 1, singleSale.getUnitPrice());
			allSales.add(sale);

		} else if (notification instanceof MultiSaleNotification) {
			MultiSaleNotification multiSale = (MultiSaleNotification) notification;
			Sale sale = new Sale(multiSale.getProductType(), multiSale.getUnitCount(), multiSale.getUnitPrice() * multiSale.getUnitCount());
			allSales.add(sale);

		} else if (notification instanceof SalesAdjustmentNotification) {
			SalesAdjustmentNotification salesAdjustment = (SalesAdjustmentNotification) notification;

			if (OperationEnum.ADD == salesAdjustment.getAdjustmentOperation()) {
				allSales.stream().filter(s -> s.getProductType().equals(notification.getProductType())).forEach(s -> addSale(s, salesAdjustment.getAdjustmentAmount()));
				salesAdjustmentNotifications.add(salesAdjustment);

			} else if (OperationEnum.SUBTRACT == salesAdjustment.getAdjustmentOperation()) {
				allSales.stream().filter(s -> s.getProductType().equals(notification.getProductType())).forEach(s -> subtractSale(s, salesAdjustment.getAdjustmentAmount()));
				salesAdjustmentNotifications.add(salesAdjustment);

			} else if (OperationEnum.MULTIPLY == salesAdjustment.getAdjustmentOperation()) {
				allSales.stream().filter(s -> s.getProductType().equals(notification.getProductType())).forEach(s -> multiplySale(s, salesAdjustment.getAdjustmentAmount()));
				salesAdjustmentNotifications.add(salesAdjustment);

			}
		}

	}

	public static List<Sale> getAllSales() {
		return allSales;
	}

	public static Map<String, Optional<Sale>> getProductSalesReport() {
		productTypeToSaleMap = null;
		productTypeToSaleMap = allSales.stream().collect(Collectors.groupingBy(Sale::getProductType, Collectors.reducing(CacheHandler::merge)));
		return productTypeToSaleMap;
	}

	public static List<SalesAdjustmentNotification> getSalesAdjustments() {
		return salesAdjustmentNotifications;
	}

	public static Sale merge(Sale a, Sale b) {
		if (a.getProductType().equals(b.getProductType())) {
			return new Sale(a.getProductType(), a.getQuantity() + b.getQuantity(), a.getTotalSalesValue() + b.getTotalSalesValue());
		}

		return null;
	}

	private static void addSale(Sale sale, float amount) {
		sale.setTotalSalesValue(sale.getTotalSalesValue() + amount);
	}

	private static void subtractSale(Sale sale, float amount) {
		if (0 < sale.getTotalSalesValue() - amount) {
			sale.setTotalSalesValue(sale.getTotalSalesValue() - amount);
		}
	}

	private static void multiplySale(Sale sale, float amount) {
		sale.setTotalSalesValue(sale.getTotalSalesValue() * amount);
	}

}
