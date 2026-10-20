package com.inventarios.dao;

import com.inventarios.conexion.Conexion;
import com.inventarios.modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public List<Cliente> listar() {

        List<Cliente> lista = new ArrayList<>();

        String sql =
                "SELECT id_cliente, documento, nombre_completo, " +
                "telefono, correo, direccion, estado " +
                "FROM cliente " +
                "WHERE estado = 1 " +
                "ORDER BY nombre_completo";

        try (
                Connection c = Conexion.conectar();
                PreparedStatement p = c.prepareStatement(sql);
                ResultSet r = p.executeQuery()
        ) {

            while (r.next()) {

                Cliente x = new Cliente();

                x.setIdCliente(
                        r.getInt("id_cliente")
                );

                x.setDocumento(
                        r.getString("documento")
                );

                x.setNombreCompleto(
                        r.getString("nombre_completo")
                );

                x.setTelefono(
                        r.getString("telefono")
                );

                x.setCorreo(
                        r.getString("correo")
                );

                x.setDireccion(
                        r.getString("direccion")
                );

                x.setEstado(
                        r.getBoolean("estado")
                );

                lista.add(x);
            }

        } catch (SQLException e) {

            System.err.println(
                    "ClienteDAO - Error al listar clientes: "
                    + e.getMessage()
            );
        }

        return lista;
    }
}