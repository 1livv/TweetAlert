package com.livv.TwitterAlert;

import javax.persistence.*;
import java.util.List;

/**
 * Created by gheorghe on 24/09/2017.
 */
@Entity
@Table(name = "followee")
public class Followee {

    private Long userId;

    List<String> notificationList;

    @Id
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "notification_list")
    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<String> notificationList) {
        this.notificationList = notificationList;
    }
}
