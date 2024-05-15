package com.example.game2.db;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class PlayerDataAccess {

    private static  SessionFactory sessionFactory ;

    public PlayerDataAccess(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addPlayerToBD(MyEntity player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(player);
            transaction.commit();
        }
    }

    public static void updatePlayerInBD(MyEntity player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
        }
    }

    public static List<MyEntity> getAllPlayersFromBD() {
        List<MyEntity> result;

        try(Session session = sessionFactory.openSession()) {
            Criteria criteria = session.createCriteria(MyEntity.class)
                    .addOrder(Order.desc("wins"));
            result = criteria.list();
        }

        return result;
    }
    public MyEntity getPlayerFromBD(String name) {
        MyEntity player;
        try (Session session = sessionFactory.openSession()) {
            player = (MyEntity) session.createCriteria(MyEntity.class)
                    .add(Restrictions.eq("name", name))
                    .uniqueResult();
        }
        return player;
    }
}
