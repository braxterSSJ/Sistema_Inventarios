package com.inventarios.dao;

import com.inventarios.conexion.Conexion;
import com.inventarios.modelo.Alerta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {

    /**
     * Ejecuta el procedimiento que reconstruye
     * las alertas pendientes según el estado
     * actual del inventario.
     * @return 
     */
    public boolean generarAlertas() {

        String sql = "{CALL sp_generar_alertas()}";

        try (Connection cn = Conexion.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.execute();
            return true;

        } catch (SQLException e) {

            System.err.println(
                    "Error al generar alertas: "
                    + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Lista las alertas mostrando también
     * el código y nombre del producto.
     * @return 
     */
    public List<Alerta> listar() {

        List<Alerta> lista = new ArrayList<>();

        String sql =
                "SELECT " +
                "a.id_alerta, " +
                "a.id_producto, " +
                "p.codigo, " +
                "p.nombre, " +
                "a.tipo_alerta, " +
                "a.mensaje, " +
                "a.nivel, " +
                "a.estado, " +
                "a.fecha_alerta, " +
                "a.fecha_atencion " +
                "FROM alerta a " +
                "INNER JOIN producto p " +
                "ON a.id_producto = p.id_producto " +
                "ORDER BY " +
                "CASE a.nivel " +
                "WHEN 'CRITICO' THEN 1 " +
                "WHEN 'ADVERTENCIA' THEN 2 " +
                "WHEN 'INFO' THEN 3 " +
                "ELSE 4 END, " +
                "a.fecha_alerta DESC";

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Alerta a = new Alerta();

                a.setIdAlerta(
                        rs.getInt("id_alerta")
                );

                a.setIdProducto(
                        rs.getInt("id_producto")
                );

                a.setCodigoProducto(
                        rs.getString("codigo")
                );

                a.setNombreProducto(
                        rs.getString("nombre")
                );

                a.setTipoAlerta(
                        rs.getString("tipo_alerta")
                );

                a.setMensaje(
                        rs.getString("mensaje")
                );

                a.setNivel(
                        rs.getString("nivel")
                );

                a.setEstado(
                        rs.getString("estado")
                );

                a.setFechaAlerta(
                        rs.getTimestamp("fecha_alerta")
                );

                a.setFechaAtencion(
                        rs.getTimestamp("fecha_atencion")
                );

                lista.add(a);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar alertas: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    /**
     * Marca una alerta como atendida.
     * @param idAlerta
     * @return 
     */
    public boolean atender(int idAlerta) {

        String sql =
                "UPDATE alerta " +
                "SET estado = 'ATENDIDA', " +
                "fecha_atencion = NOW() " +
                "WHERE id_alerta = ?";

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idAlerta);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al atender alerta: "
                    + e.getMessage()
            );

            return false;
        }
    }
}
