package com.livv.TwitterAlert;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by gheorghe on 24/09/2017.
 */
@Repository
public class FolloweeDao {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Followee findFollowee(Long userId) {
        return (Followee) sessionFactory.getCurrentSession().get(Followee.class, userId);
    }

    public Followee storeFollowee(Followee followee) {
        return (Followee) sessionFactory.getCurrentSession().merge(followee);
    }

    public List<Followee> list() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Followee.class);
        return (List<Followee>)criteria.list();
    }
}
