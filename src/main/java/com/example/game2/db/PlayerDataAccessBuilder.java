package com.example.game2.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class PlayerDataAccessBuilder {

    private static final SessionFactory sessionFactory;
    private static final PlayerDataAccess playerDataAccess;

    static {
        try {
            var configuration = new Configuration()
                    .addAnnotatedClass(MyEntity.class);
            sessionFactory = configuration.buildSessionFactory();
            playerDataAccess = new PlayerDataAccess(sessionFactory);
        } catch (Throwable ex) {
            System.err.println("Инициализация SessionFactory не удалась: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static PlayerDataAccess build() {
        return playerDataAccess;
    }
}
