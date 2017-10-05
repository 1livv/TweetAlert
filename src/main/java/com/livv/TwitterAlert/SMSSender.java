package com.livv.TwitterAlert;

import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.log4j.Logger;

/**
 * Created by gheorghe on 27/09/2017.
 */
public class SMSSender implements NotificationSender {

    private static Config config;

    private static boolean initialized = false;

    private static Logger log = Logger.getLogger(SMSSender.class);

    private static String fromNumber;

    public SMSSender(Config config) {
        if (!initialized) {
            log.info("initializing twilio connector");
            try {
                String accountSid = config.getProperty("talert.twilio.accountSid");
                String authToken = config.getProperty("talert.twilio,authToken");
                fromNumber = config.getProperty("talert.twilio.from");
                Twilio.init(accountSid, authToken);
                this.config = config;
                initialized = true;
            }
            catch (TwilioException e) {
                log.error("cannot establish connection with twilio");
                throw e;
            }
        }
    }

    public void sendNotification(String message, String destination) {

        int numberOfRetries = Integer.parseInt(config.getProperty("talert.twilio.retries"));

        for (int index = 0; index < numberOfRetries; index++) {
            try {
                Message sms = Message.creator(new PhoneNumber(destination), new PhoneNumber(fromNumber),
                        message).create();
                log.error("attempt " + index + " to send message=" + message +
                        " to " + destination + " succeded " + sms.getSid());
                return;
            }
            catch (TwilioException e) {
                log.error("attempt " + index + " to send message=" + message +
                        " to " + destination + " failed" );
            }

            log.error("message= " + message + "to " + destination + " was dropped");
        }
    }
}
