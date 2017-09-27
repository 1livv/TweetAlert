package com.livv.TwitterAlert;

/**
 * Created by gheorghe on 24/09/2017.
 */
public class CreateUserReq {

    private String userName;

    private String phoneNumber;

    public  CreateUserReq() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
