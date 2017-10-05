package com.livv.TwitterAlert;

import twitter4j.Status;

/**
 * Created by gheorghe on 27/09/2017.
 */
public class NotificationRunner implements Runnable {

    private Status status;

    private String phoneNumber;

    private NotificationSender notificationSender;

    public NotificationRunner(Status status, String phoneNumber, NotificationSender notificationSender) {
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.notificationSender = notificationSender;
    }

    public void run() {
        notificationSender.sendNotification(status.getText(), phoneNumber);
    }
}
