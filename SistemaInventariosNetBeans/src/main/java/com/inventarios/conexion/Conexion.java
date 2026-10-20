package com.inventarios.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
            "jdbc:mysql://localhost:3308/sistema_inventarios"
            + "?useSSL=false"
            + "&serverTimezone=America/Lima"
            + "&allowPublicKeyRetrieval=true";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Conexion() {
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean probarConexion() {
        try (Connection cn = conectar()) {
            return cn != null && !cn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return false;
        }
    }
}
