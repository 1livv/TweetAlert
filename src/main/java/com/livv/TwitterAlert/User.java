package com.livv.TwitterAlert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by gheorghe on 23/09/2017.
 */
@Entity
@Table(name = "USERS")
public class User {

    private String userName;

    private String phoneNumber;

    private Timestamp dateJoined;

    UserStatus userStatus;

    @Id
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "date_joined")
    public Timestamp getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Timestamp dateJoined) {
        this.dateJoined = dateJoined;
    }

    @Column(name = "user_status")
    public UserStatus getUserStatus() { return userStatus; }

    public void setUserStatus(UserStatus userStatus) { this.userStatus = userStatus; }
}

enum UserStatus {
    PENDING,
    ACTIVE
}