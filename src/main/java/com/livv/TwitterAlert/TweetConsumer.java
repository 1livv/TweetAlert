package com.livv.TwitterAlert;

import com.twilio.exception.TwilioException;
import com.twitter.hbc.core.Client;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by gheorghe on 18/09/2017.
 */
public class TweetConsumer implements Runnable {

    private static Logger log = Logger.getLogger(TweetConsumer.class);

    private Client client;

    private BlockingQueue<String> msgQueue;

    private FolloweeService followeeService;

    private NotificationPool notificationPool;

    private NotificationSender notificationSender;

    public TweetConsumer(Config config) {
        this.notificationSender = new SMSSender(config);
    }

    public TweetConsumer withMsgQueue(BlockingQueue<String> msgQueue) {
        this.msgQueue = msgQueue;
        return this;
    }

    public TweetConsumer withClient(Client client) {
        this.client = client;
        return this;
    }

    public TweetConsumer withPool(NotificationPool notificationPool) {
        this.notificationPool = notificationPool;
        return this;
    }

    public TweetConsumer withFolloweeService(FolloweeService followeeService) {
        this.followeeService = followeeService;
        return this;
    }

    public void run() {

        String tweet;
        while (!client.isDone()) {
            try {
                tweet = msgQueue.poll(500, TimeUnit.MILLISECONDS);
                if (tweet != null) {
                    //send notification
                    log.info("got tweet:" + tweet);
                    try {
                        Status status = TwitterObjectFactory.createStatus(tweet);
                        Followee followee = followeeService.getFollowee(status.getUser().getId());
                        sendNotifications(followee.getNotificationList(), status);
                    }
                    catch (TwitterException e) {
                        log.error("error while parsing message " + tweet + "\n" + e);
                    }
                }
            }
            catch(InterruptedException e) {
                log.error("the queue was intrreputed ");
            }
        }

        log.info("client done emptying queue");
        while((tweet = msgQueue.poll()) != null) {
            //send notification here
            log.info("got tweet:" + tweet);
            try {
                Status status = TwitterObjectFactory.createStatus(tweet);
                Followee followee = followeeService.getFollowee(status.getId());
                sendNotifications(followee.getNotificationList(), status);
            }
            catch (TwitterException e) {
                log.error("error while parsing " + tweet + "\n" + e);
            }
        }
    }

    private void sendNotifications(List<String> phoneNumbers, Status status) {

        for (String number : phoneNumbers) {
            NotificationRunner notificationRunner = new NotificationRunner(status, number, notificationSender);
            notificationPool.execute(notificationRunner);
        }
    }
}
