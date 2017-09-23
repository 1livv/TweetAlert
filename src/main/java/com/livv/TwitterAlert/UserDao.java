package com.livv.TwitterAlert;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by gheorghe on 23/09/2017.
 */
@Service
public class UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void saveUser(User user) {
        sessionFactory.getCurrentSession().save(user);
    }
}
