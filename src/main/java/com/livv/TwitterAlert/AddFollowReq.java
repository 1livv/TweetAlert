package com.livv.TwitterAlert;

/**
 * Created by gheorghe on 18/09/2017.
 */
public class AddFollowReq {

    /*replace this with user name*/
    private Long userID;

    public AddFollowReq() {

    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
