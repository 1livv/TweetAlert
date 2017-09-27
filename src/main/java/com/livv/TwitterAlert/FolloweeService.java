package com.livv.TwitterAlert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gheorghe on 24/09/2017.
 */
@Service
public class FolloweeService {

    private FolloweeDao followeeDao;

    @Autowired
    public void setFolloweeDao(FolloweeDao followeeDao) {
        this.followeeDao = followeeDao;
    }

    @Transactional
    public void addToNotificationList(Long userId, String phoneNumber) {
        Followee followee = followeeDao.findFollowee(userId);

        if (followee != null) {
            List<String> noticationList = followee.getNotificationList();
            if (! noticationList.contains(phoneNumber)) {
                noticationList.add(phoneNumber);
                followee.setNotificationList(noticationList);
            }
        }
        else {
            followee = new Followee();
            followee.setUserId(userId);
            followee.setNotificationList(Arrays.asList(phoneNumber));
        }

        followeeDao.storeFollowee(followee);
    }

    @Transactional
    public List<Long> listIds() {
        List<Followee> followees = followeeDao.list();
        return followees.stream().map((x) -> x.getUserId()).collect(Collectors.toList());
    }

    @Transactional
    public Followee getFollowee(Long userId) {
        return followeeDao.findFollowee(userId);
    }
}
