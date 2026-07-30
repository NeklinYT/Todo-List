package com.neklin;

import java.sql.Timestamp;

public class TaskGetter {
    private int id;
    private String name;
    private Timestamp createdAt;

    public TaskGetter(int id, String name, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public Timestamp getCreatedAt() { return createdAt; }
}