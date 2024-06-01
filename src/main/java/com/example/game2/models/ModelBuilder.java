package com.example.game2.models;


import com.example.game2.db.PlayerDataAccessBuilder;

public class ModelBuilder {
    static Model model = new Model(PlayerDataAccessBuilder.build());
    static public Model build() {
        return model;
    }

}