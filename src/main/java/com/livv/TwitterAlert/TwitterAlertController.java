package com.livv.TwitterAlert;

/**
 * Created by gheorghe on 13/09/2017.
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/{user}")
public class TwitterAlertController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String sayHello(@PathVariable("user") String user) {
        return "hello " + user;
    }
}
