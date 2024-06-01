package com.example.game2.db;
import javax.persistence.*;

@Entity
@Table(name = "player")
public class MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;

    public String getName() {
        return name;
    }

    public Integer getWins() {
        return wins;
    }

    private Integer wins;

    public void incrementWins() {
        wins++;
    }

    private MyEntity() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MyEntity myEntity;

        private Builder() {
            myEntity = new MyEntity();
        }

        public Builder name(String name) {
            myEntity.name = name;
            return this;
        }

        public Builder wins(Integer wins) {
            myEntity.wins = wins;
            return this;
        }

        public MyEntity build() {
            return myEntity;
        }
    }

}