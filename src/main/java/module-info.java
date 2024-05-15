module com.example.game2 {
    requires org.hibernate.orm.core;
    requires org.postgresql.jdbc;
    requires java.persistence;
    requires java.naming;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    requires com.google.gson;

    opens com.example.game2 to javafx.fxml, org.postgresql.jdbc, com.google.gson;
    opens com.example.game2.db to java.persistence, org.hibernate.orm.core, com.google.gson;
    exports com.example.game2.db;
    exports com.example.game2;
    exports com.example.game2.models;
    opens com.example.game2.models to javafx.fxml, com.google.gson;
    exports com.example.game2.server;
    opens com.example.game2.server to javafx.fxml, com.google.gson;


}