package test;


import java.sql.*;


public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String USER = "root";
    private static final String PASSWORD = "";

   public static Connection getConnection() {
    try {
        System.out.println("Tentative de connexion à la base de données...");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "");
    } catch (SQLException e) {
        System.err.println("Erreur de connexion : " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}



}