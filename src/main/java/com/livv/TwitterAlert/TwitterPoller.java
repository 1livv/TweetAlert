package com.livv.TwitterAlert;

import com.twitter.hbc.*;
import com.twitter.hbc.core.*;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by gheorghe on 18/09/2017.
 */
@Service
public class TwitterPoller {

    private BlockingQueue<String> msgQueue;

    private List<Long>  toFollow;

    private  Client client;

    private Config config;

    private Hosts hosts;

    private StatusesFilterEndpoint hosebirdEndpoint;

    private Authentication auth;

    private TweetConsumer tweetConsumer;

    private Thread consumerThread;

    private static Logger log = Logger.getLogger(TwitterPoller.class);

    private FolloweeService followeeService;

    private NotificationPool notificationPool;

    @Autowired
    public void setConfig(Config config) {
        this.config = config;
    }

    @Autowired
    public void setFolloweeService(FolloweeService followeeService) { this.followeeService = followeeService; }

    @PostConstruct
    public void postConstruct() {
        msgQueue = new LinkedBlockingQueue<String>(
                Integer.parseInt(config.getProperty("talert.messageQueue.size")));

        //toFollow = config.getPropertyAsList("talert.toFollowList", (x) -> Long.parseLong(x));
        toFollow = followeeService.listIds();

        hosts = new HttpHosts(Constants.STREAM_HOST);
        hosebirdEndpoint = new StatusesFilterEndpoint();
        hosebirdEndpoint.followings(toFollow);

        auth = new OAuth1(config.getProperty("talert.consumer.key"), config.getProperty("talert.consumer.secret"),
                config.getProperty("talert.consumer.token"), config.getProperty("talert.consumer.tokenSecret"));
        client = getClient();
        client.connect();

        int noThreads = Integer.parseInt(config.getProperty("talert.notificationPool.threads"));
        int queueSize = Integer.parseInt(config.getProperty("talert.notificationPool.queueSize"));
        long timeout = Long.parseLong(config.getProperty("talert.notificationPool.timeout"));
        notificationPool = new NotificationPool(noThreads, queueSize, timeout);

        startConsumer();
    }

    public boolean addToFollow(Long userId) {

        log.info("adding " + userId.toString() + " to the list");
        Client newClient = null;
        try {
            toFollow.add(userId);
            client.stop();

            //this might lose messages!!!!!!!*/
            consumerThread.join();
            newClient = getClient();
        }
        catch(Exception e) {
            log.error("failed to restart clients");
            client.reconnect();
            startConsumer();
            return false;
        }

        if (newClient != null) {
            log.warn("restart succesfull");
            client = newClient;
            client.connect();
            startConsumer();
            return true;
        }

        return false;
    }

    private Client getClient() {
        hosebirdEndpoint.followings(toFollow);
        ClientBuilder clientBuilder = new ClientBuilder().name("livv-talert")
                .hosts(hosts).endpoint(hosebirdEndpoint).authentication(auth)
                .processor(new StringDelimitedProcessor(msgQueue));

        Client result = clientBuilder.build();
        return result;
    }

    private void startConsumer() {
        tweetConsumer = new TweetConsumer().withClient(client).withMsgQueue(msgQueue)
                .withPool(notificationPool);
        consumerThread  = new Thread(tweetConsumer);
        consumerThread.start();
    }
}
