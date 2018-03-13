# sales-notification-processor

This a small sales notification processor which can process 3 types of the messages. see examples at bottom of this file.
Message Types: SS = Single Sale (i.e sale of single item), MS = Multi Sale (Sale of multiple items of same product), SA = Sales Adjustment (Sales adjustments for all sales of specific product type)
Notifications must be dropped to "incomming notifications" which then moved to "archived notifications" folder after processing the message.
Notification is a json files in file name format message_<timestamps or a sequence number>.json
Notifications are processed in ascending order.

Invalid sales adjustments, i.e. negative values after applying the adjustments is ignored => operation will not be performed.

This application can process configurable number of messages (see app.properties in resources in order to see configuration), 
after processing the configured message, application will shutdown. But before shutting down it will show all report of all adjustments.
This application also show reports of each product sales after a configurable number of messages for details see app.config.  

You can also find test messages in "test messages" folder

// 1st Type of message => Single Sale Notification
{
	"messageType": "SS",
	"productType": "Apple",
	"unitPrice": 5.0
}

// 2nd Type of message => Multi Sale Notification
{
	"messageType": "MS",
	"productType": "Apple",
	"unitPrice": 2.0,
	"unitCount": 3
}

// 3rd Type of message => Sales Adjustment Notification
{
	"messageType": "SA",
	"productType": "Apple",
	"adjustmentOperation": "add",
	"adjustmentAmount": 2.0
}


