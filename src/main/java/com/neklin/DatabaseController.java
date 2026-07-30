package com.neklin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DatabaseController {

    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_NAME = "todolist_db";
    private static final String FULL_URL = URL + DB_NAME;
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(FULL_URL, USER, PASSWORD);
    }

    // Инициализация БД и таблиц при запуске
    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + DB_NAME + "'");
            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
                System.out.println("✅ База данных '" + DB_NAME + "' создана");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Ошибка при проверке/создании БД: " + e.getMessage());
        }

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS list (
                    id SERIAL PRIMARY KEY,
                    uuid UUID DEFAULT gen_random_uuid() UNIQUE,
                    name VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.executeUpdate(createTableSQL);
            System.out.println("✅ Таблица 'list' готова");

        } catch (SQLException e) {
            System.out.println("❌ Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    public void connection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Успешное подключение к базе данных!");
        } catch (SQLException e) {
            System.out.println("❌ Ошибка подключения: " + e.getMessage());
        }
    }

    public ObservableList<TaskGetter> getAllTasks() {  // Используй свой класс Task
        ObservableList<TaskGetter> tasks = FXCollections.observableArrayList();
        String sql = "SELECT id, name, created_at FROM list ORDER BY id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Timestamp createdAt = rs.getTimestamp("created_at");
                tasks.add(new TaskGetter(id, name, createdAt));  // Создаем твой Task
            }

        } catch (SQLException e) {
            System.out.println("❌ Ошибка получения данных: " + e.getMessage());
        }
        return tasks;
    }

    public void addProduct(String name) {
        String sql = "INSERT INTO list (name) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("✅ '" + name + "' добавлен");
        } catch (SQLException e) {
            System.out.println("❌ Ошибка: " + e.getMessage());
        }
    }

    public boolean deleteTask(int id) {
        String sql = "DELETE FROM list WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("❌ Ошибка удаления: " + e.getMessage());
            return false;
        }
    }
}