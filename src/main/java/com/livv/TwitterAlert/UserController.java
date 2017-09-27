package com.livv.TwitterAlert;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;

/**
 * Created by gheorghe on 24/09/2017.
 */
@Controller
public class UserController {

    private UserService userService;

    private static Logger log = Logger.getLogger(UserController.class);

    @Autowired
    public void setUserService(UserService userService) { this.userService = userService; }

    @RequestMapping(value = "createUser", consumes = "application/json", produces = "application/json",
    method = RequestMethod.POST)
    public ResponseEntity<TalertResponse> createUser(@RequestBody CreateUserReq createUserReq) {

        log.info("Got request to create user [ " + createUserReq.getUserName() + ": " + createUserReq.getPhoneNumber());

        if (createUserReq.getUserName() == null || createUserReq.getUserName().isEmpty()) {
            return new ResponseEntity<TalertResponse>(new TalertResponse("username cannot be null"),
                    HttpStatus.BAD_REQUEST);
        }

        if (createUserReq.getPhoneNumber() == null || createUserReq.getPhoneNumber().isEmpty()) {
            return new ResponseEntity<TalertResponse>(new TalertResponse("phone number cannot be null"),
                    HttpStatus.BAD_REQUEST);
        }

        if (!validatePhoneNumber(createUserReq.getPhoneNumber())) {
            return new ResponseEntity<TalertResponse>(new TalertResponse("invalid phone number"),
                    HttpStatus.BAD_REQUEST);
        }

       if (userService.userExists(createUserReq.getUserName())) {
            return new ResponseEntity<TalertResponse>(new TalertResponse("the user already exists"),
                    HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setPhoneNumber(createUserReq.getPhoneNumber());
        newUser.setUserName(createUserReq.getUserName());
        newUser.setUserStatus(UserStatus.PENDING);
        newUser.setDateJoined(new Timestamp(System.currentTimeMillis()));

        try {
            newUser = userService.storeUser(newUser);
            //todo --> validation code via sms
            return new ResponseEntity<TalertResponse>(new TalertResponse(newUser, "OK"), HttpStatus.OK);
        }
        catch(Exception e) {
            log.error("failed to save the use to the database");
            return new ResponseEntity<TalertResponse>(new TalertResponse("failed to save to db"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value ="confirmRegistration", consumes = "application/json", produces = "application/json",
    method = RequestMethod.POST)
    public ResponseEntity<TalertResponse> confirmRegistration() {
        //todo
        return null;
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        Boolean result = false;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phonenumber = phoneNumberUtil.parse(phoneNumber, "");
            result = phoneNumberUtil.isValidNumber(phonenumber);
        }
        catch(NumberParseException e) {
            log.error("phone validation failed " + phoneNumber);
        }

        return result;
    }
}
