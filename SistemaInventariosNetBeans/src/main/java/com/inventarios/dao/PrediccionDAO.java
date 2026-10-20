package com.inventarios.dao;

import com.inventarios.conexion.Conexion;
import com.inventarios.modelo.Prediccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrediccionDAO {

    /**
     * Ejecuta el procedimiento que reconstruye
     * las predicciones utilizando las ventas
     * de los últimos 30 días.
     * @return 
     */
    public boolean generarPredicciones() {

        String sql = "{CALL sp_generar_predicciones()}";

        try (Connection cn = Conexion.conectar();
             CallableStatement cs = cn.prepareCall(sql)) {

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.err.println(
                    "Error al generar predicciones: "
                    + e.getMessage()
            );
            return false;
        }
    }

    /**
     * Obtiene las predicciones junto con
     * la información del producto.
     * @return 
     */
    public List<Prediccion> listar() {

        List<Prediccion> lista = new ArrayList<>();

        String sql =
                "SELECT " +
                "pr.id_prediccion, " +
                "pr.id_producto, " +
                "p.codigo, " +
                "p.nombre, " +
                "pr.periodo, " +
                "pr.ventas_historicas, " +
                "pr.promedio_diario, " +
                "pr.demanda_estimada, " +
                "pr.stock_actual, " +
                "pr.dias_cobertura, " +
                "pr.recomendacion, " +
                "pr.fecha_prediccion " +
                "FROM prediccion pr " +
                "INNER JOIN producto p " +
                "ON pr.id_producto = p.id_producto " +
                "ORDER BY pr.fecha_prediccion DESC, p.nombre";

        try (Connection cn = Conexion.conectar();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Prediccion p = new Prediccion();

                p.setIdPrediccion(
                        rs.getInt("id_prediccion")
                );

                p.setIdProducto(
                        rs.getInt("id_producto")
                );

                p.setCodigoProducto(
                        rs.getString("codigo")
                );

                p.setNombreProducto(
                        rs.getString("nombre")
                );

                p.setPeriodo(
                        rs.getString("periodo")
                );

                p.setVentasHistoricas(
                        rs.getInt("ventas_historicas")
                );

                p.setPromedioDiario(
                        rs.getDouble("promedio_diario")
                );

                p.setDemandaEstimada(
                        rs.getDouble("demanda_estimada")
                );

                p.setStockActual(
                        rs.getInt("stock_actual")
                );

                double dias =
                        rs.getDouble("dias_cobertura");

                if (rs.wasNull()) {
                    p.setDiasCobertura(null);
                } else {
                    p.setDiasCobertura(dias);
                }

                p.setRecomendacion(
                        rs.getString("recomendacion")
                );

                p.setFechaPrediccion(
                        rs.getTimestamp("fecha_prediccion")
                );

                lista.add(p);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar predicciones: "
                    + e.getMessage()
            );
        }

        return lista;
    }
}