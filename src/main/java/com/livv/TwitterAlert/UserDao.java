package com.livv.TwitterAlert;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by gheorghe on 23/09/2017.
 */
@Repository
public class UserDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    public User storeUser(User user) {
         return (User)sessionFactory.getCurrentSession().merge(user);
    }

    public User findUser(String userName) {
        return (User)sessionFactory.getCurrentSession().get(User.class, userName);
    }
}
