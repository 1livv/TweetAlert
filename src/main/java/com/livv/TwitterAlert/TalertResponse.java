package com.livv.TwitterAlert;

/**
 * Created by gheorghe on 24/09/2017.
 */
public class TalertResponse {

    private String message;

    private User user;

    public TalertResponse(){}

    public TalertResponse (String message) {
        this.message = message;
    }

    public TalertResponse(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
