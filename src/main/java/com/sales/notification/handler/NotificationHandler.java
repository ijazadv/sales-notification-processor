package com.sales.notification.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sales.notification.Constant;
import com.sales.notification.cache.CacheHandler;
import com.sales.notification.cache.Sale;
import com.sales.notification.dto.Notification;
import com.sales.notification.dto.SalesAdjustmentNotification;

/**
 * A primary class responsible for processing all the sales notifications and
 * generating reports based on received notifications.
 * 
 * @author mia
 *
 */
public class NotificationHandler {
	private Properties properties;

	public NotificationHandler() {
		// Read properties files for getting application specific user settings
		try {
			properties = new Properties();
			File file = new File("app.properties");
			properties.load(new FileInputStream(file));
		} catch (IOException e) {
			System.err.println("Failed to get app settings. " + Constant.APP_NAME + " will start with default settings");
		}
	}

	public void start(File incommingNotificationsDir) throws IOException {
		System.out.println("Sales notification process started.");

		int stopCounter = getPropertyInInt("stopCounter", "50");
		int productSalesReportCounter = getPropertyInInt("productSalesReportCounter", "10");
		Arrays.sort(incommingNotificationsDir.listFiles());

		while (true) {

			for (File file : incommingNotificationsDir.listFiles()) {
				if (isValidFile(file)) {
					process(file);

					if (0 == --productSalesReportCounter) {
						generateProductSalesReport();
						productSalesReportCounter = getPropertyInInt("productSalesReportCounter", "10");
					}

					if (0 == --stopCounter) {
						generateSalesAdjustmentsReport();
					}
				}

			}

		}
	}

	/**
	 * 
	 */
	private void generateSalesAdjustmentsReport() {
		System.out.println("Sales notification processor is pausing... \n");

		System.out.println("================ Sales Adjustment Report =================");
		System.out.println("Product Type	Operation 	Amount");
		System.out.println("==========================================================");
		CacheHandler.getInstance().getSalesAdjustments().forEach(this::printSalesAdjustments);

		System.exit(0);
	}

	private void generateProductSalesReport() {
		System.out.println("================== Product Sales Report ==================");
		System.out.println("Product Type		Quantity	 	Total Sales");
		System.out.println("==========================================================");

		CacheHandler.getInstance().getProductSalesReport().values().forEach(this::printSalesReportByProduct);

		System.out.println("\n\n");
	}

	private int getPropertyInInt(String name, String defaultVal) {
		return Integer.parseInt(properties.getProperty(name, defaultVal));
	}

	private void printSalesReportByProduct(Optional<Sale> sale) {
		if (sale.isPresent()) {
			System.out.println(sale.get().getProductType() + "			" + sale.get().getQuantity() + "		" + sale.get().getTotalSalesValue());
		}
	}

	private void printSalesAdjustments(SalesAdjustmentNotification salesAdjustmentNotification) {
		System.out.println(salesAdjustmentNotification.getProductType() + "		 " + salesAdjustmentNotification.getAdjustmentOperation() + "		"
				+ salesAdjustmentNotification.getAdjustmentAmount());

	}

	/**
	 * Unmarshals json file into Notification object and hand over the notification
	 * to cache for temporary storage.
	 * 
	 * @param file
	 */
	private void process(File file) {
		Gson gson = new GsonBuilder().registerTypeAdapter(Notification.class, new NotificationDeserializer()).create();
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			Notification notification = gson.fromJson(reader, Notification.class);
			CacheHandler.getInstance().updateCache(notification);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}

		// Archive notifications for audit
		try {
			Files.move(file.toPath(), Paths.get(Constant.INCOMMING_NOTIFICATION_DIRECTORY, Constant.ARCHIVED_NOTIFICATION_DIRECTORY, file.getName()),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	public boolean isValidFile(File file) {
		if (file.isFile() && file.getName().startsWith("message_") && file.getName().endsWith(".json")) {
			return true;
		}

		return false;
	}
}
