package com.sales.notification;

import java.io.File;
import java.io.IOException;

import com.sales.notification.handler.NotificationHandler;

public class Driver {

	public static void main(String[] args) {
		try {
			File notifications = new File(Constant.INCOMMING_NOTIFICATION_DIRECTORY);
			File archivedNotifications = new File(Constant.INCOMMING_NOTIFICATION_DIRECTORY,
					Constant.ARCHIVED_NOTIFICATION_DIRECTORY);
			
			if(! notifications.exists()) {
				notifications.mkdirs();
			}
			
			if(! archivedNotifications.exists()) {
				archivedNotifications.mkdirs();
			}
			
			new NotificationHandler().start(notifications);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
