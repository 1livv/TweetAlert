package com.livv.TwitterAlert;

/**
 * Created by gheorghe on 27/09/2017.
 */
interface NotificationSender {

    public void sendNotification(String message, String destination);
}
