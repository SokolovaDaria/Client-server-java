package com.example.game2.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class PlayerDataAccessBuilder {

    private static final SessionFactory sessionFactory;

    // Загрузка конфигурации Hibernate и создание SessionFactory
    static {
        try {
            // Загружаем конфигурацию Hibernate из файла hibernate.cfg.xml
            var configuration = new Configuration()
                    .addAnnotatedClass(MyEntity.class);
            // Создаем SessionFactory на основе конфигурации Hibernate
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Инициализация SessionFactory не удалась: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Метод для создания и возврата экземпляра PlayerDataAccess
    public static PlayerDataAccess build() {
        return new PlayerDataAccess(sessionFactory);
    }
}
