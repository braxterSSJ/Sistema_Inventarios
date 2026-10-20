package com.inventarios.dao;

import com.inventarios.conexion.Conexion;
import com.inventarios.modelo.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {

    public List<Rol> listar() {

        List<Rol> lista = new ArrayList<>();

        String sql = """
            SELECT id_rol,
                   nombre,
                   descripcion,
                   estado
            FROM rol
            WHERE estado = 1
            ORDER BY nombre
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Rol rol = new Rol();

                rol.setIdRol(rs.getInt("id_rol"));
                rol.setNombre(rs.getString("nombre"));
                rol.setDescripcion(rs.getString("descripcion"));
                rol.setEstado(rs.getBoolean("estado"));

                lista.add(rol);
            }

        } catch (SQLException e) {

            System.err.println(
                    "RolDAO - Error al listar roles: "
                    + e.getMessage()
            );
        }

        return lista;
    }
}