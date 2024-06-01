package com.example.game2.db;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
public class PlayerDataAccess {

    private static SessionFactory sessionFactory;

    public PlayerDataAccess(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addPlayerToBD(MyEntity player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            MyEntity myEntity = getPlayerFromBD(player.getName());
            if (myEntity == null) {
                session.save(player);
                transaction.commit();
            }
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
        List<MyEntity> rating;

        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM MyEntity ORDER BY wins DESC";
            Query<MyEntity> query = session.createQuery(hql, MyEntity.class);
            rating = query.getResultList();
        }
        return rating;
    }

    public MyEntity getPlayerFromBD(String name) {
        MyEntity player;
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM MyEntity WHERE name = :name";
            Query<MyEntity> query = session.createQuery(hql, MyEntity.class);
            query.setParameter("name", name);
            player = query.uniqueResult();
        }
        return player;
    }
}
