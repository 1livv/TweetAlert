package com.livv.TwitterAlert;

/**
 * Created by gheorghe on 13/09/2017.
 */

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/{user}")
public class TwitterAlertController {

    private TwitterPoller twitterPoller;

    private FolloweeService followeeService;

    private UserService userService;

    private static Logger log = Logger.getLogger(TwitterAlertController.class);

    @Autowired
    public void setTwitterPoller(TwitterPoller twitterPoller) {
        this.twitterPoller = twitterPoller;
    }

    @Autowired
    public void setFolloweeService(FolloweeService followeeService) { this.followeeService  = followeeService; }

    @Autowired
    public void setUserService(UserService userService) { this.userService = userService; }


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String sayHello(@PathVariable("user") String user) {
        return "hello " + user;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<TalertResponse> addUser(@RequestBody AddFollowReq req, @PathVariable(name = "user") String userName) {

        log.info("adding " +  req.getUserID() + " to list for " + userName);

        if (!userService.userExists(userName)) {
            return new ResponseEntity<TalertResponse>(new TalertResponse("the user does not exists"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = userService.getUser(userName);
        /*if (user.getUserStatus().equals(UserStatus.PENDING)) {
            return new ResponseEntity<TalertResponse>(new TalertResponse("the user has not confirmed"),
                    HttpStatus.BAD_REQUEST);
        }*/

        //todo validate userID

        try {
            followeeService.addToNotificationList(req.getUserID(),user.getPhoneNumber());
            twitterPoller.addToFollow(req.getUserID());
        }
        catch(Exception e) {
            log.error("failed to update list" +  e);
            return new ResponseEntity<TalertResponse>(new TalertResponse("failed to update list"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<TalertResponse>(new TalertResponse("OK"), HttpStatus.OK);
    }

}

