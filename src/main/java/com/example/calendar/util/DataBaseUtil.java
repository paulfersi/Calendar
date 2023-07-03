package com.example.calendar.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseUtil {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Inizializza la connessione al database SQLite
                connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
