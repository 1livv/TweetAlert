package com.livv.TwitterAlert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by gheorghe on 24/09/2017.
 */
@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) { this.userDao = userDao; }

    @Transactional
    public User storeUser(User user) {
        return userDao.storeUser(user);
    }

    @Transactional
    public boolean userExists(String userName) {
        User user = userDao.findUser(userName);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Transactional
    public User getUser(String userName) {
        return userDao.findUser(userName);
    }
}
