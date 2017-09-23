package com.livv.TwitterAlert;

import com.twitter.hbc.core.Client;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by gheorghe on 18/09/2017.
 */
public class TweetConsumer implements Runnable {

    private static Logger log = Logger.getLogger(TweetConsumer.class);

    private Client client;

    private BlockingQueue<String> msgQueue;


    public TweetConsumer withMsgQueue(BlockingQueue<String> msgQueue) {
        this.msgQueue = msgQueue;
        return this;
    }

    public TweetConsumer withClient(Client client) {
        this.client = client;
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
        }
    }
}
