package com.livv.TwitterAlert;

/**
 * Created by gheorghe on 13/09/2017.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/{user}")
public class TwitterAlertController {

    private TwitterPoller twitterPoller;

    private UserDao userDao;

    @Autowired
    public void setTwitterPoller(TwitterPoller twitterPoller) {
        this.twitterPoller = twitterPoller;
    }

    @Autowired
    public void setUserDao(UserDao userDao) { this.userDao = userDao; }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String sayHello(@PathVariable("user") String user) {
        return "hello " + user;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public String addUser(@RequestBody AddUserReq req) {
        List<Long> users = new ArrayList<>();
        users.add(req.getUserID());

        User user = new User();
        user.setPhoneNumber("adasdasd");
        user.setDateJoined(new Timestamp(System.currentTimeMillis()));
        user.setUserName("livica");
        userDao.saveUser(user);

        String result = "this was " + twitterPoller.addToFollow(users);
        return result;
    }

}

