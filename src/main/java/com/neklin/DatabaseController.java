package com.neklin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DatabaseController {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_NAME = "todolist_db";
    private static final String FULL_URL = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(FULL_URL, USER, PASSWORD);
    }


    public String initializeDatabase() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Проверяем подключение и создаём БД
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();

            // Проверяем, существует ли БД
            rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + DB_NAME + "'");
            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE " + DB_NAME);
                System.out.println("✅ База данных '" + DB_NAME + "' создана");
            } else {
                System.out.println("✅ База данных '" + DB_NAME + "' уже существует");
            }

            // Закрываем ресурсы (чтобы переподключиться к новой БД)
            rs.close();
            stmt.close();
            conn.close();

            // Подключаемся к созданной БД и создаём таблицу
            conn = DriverManager.getConnection(FULL_URL, USER, PASSWORD);
            stmt = conn.createStatement();

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

            return null; // Если успешно

        } catch (SQLException e) {
            // Ловим ВСЕ ошибки в одном месте
            String msg = e.getMessage();
            System.out.println("❌ Ошибка: " + msg);

            if (msg.contains("Connection refused")) {
                return "❌ PostgreSQL не запущен! Запусти сервис PostgreSQL.";
            } else if (msg.contains("password authentication failed")) {
                return "❌ Пароль для пользователя 'admin' неверный!";
            } else if (msg.contains("FATAL")) {
                return "❌ Пользователь 'admin' не найден! Создай юзера с паролем admin.";
            } else if (msg.contains("already exists")) {
                return null; // Если БД уже есть - просто продолжаем
            } else {
                return "❌ Ошибка: " + msg;
            }
        } finally {
            // Закрываем ресурсы, чтобы не было утечек
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    public ObservableList<TaskGetter> getAllTasks() {
        ObservableList<TaskGetter> tasks = FXCollections.observableArrayList();
        String sql = "SELECT id, name, created_at FROM list ORDER BY id";

        try (Connection conn = DriverManager.getConnection(FULL_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Timestamp createdAt = rs.getTimestamp("created_at");
                tasks.add(new TaskGetter(id, name, createdAt));
            }

        } catch (SQLException e) {
            System.out.println("❌ Ошибка получения данных: " + e.getMessage());
        }
        return tasks;
    }

    public void addProduct(String name) {
        String sql = "INSERT INTO list (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(FULL_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("✅ '" + name + "' добавлен");
        } catch (SQLException e) {
            System.out.println("❌ Ошибка добавления: " + e.getMessage());
        }
    }

    public boolean deleteTask(int id) {
        String sql = "DELETE FROM list WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(FULL_URL, USER, PASSWORD);
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