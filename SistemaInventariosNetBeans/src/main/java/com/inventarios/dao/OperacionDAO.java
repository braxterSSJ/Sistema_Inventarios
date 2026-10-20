package com.inventarios.dao;

import com.inventarios.conexion.Conexion;
import com.inventarios.modelo.ItemOperacion;

import java.sql.*;
import java.util.List;

public class OperacionDAO {

    private static final double IGV = 0.18;

    public int registrarCompra(
            int idProveedor,
            int idUsuario,
            String numeroCompra,
            List<ItemOperacion> items
    ) throws SQLException {

        if (items == null || items.isEmpty()) {
            throw new SQLException("Debe agregar al menos un producto.");
        }

        if (numeroCompra == null || numeroCompra.isBlank()) {
            numeroCompra = generarNumero("C");
        }

        try (Connection cn = Conexion.conectar()) {

            cn.setAutoCommit(false);

            try {

                int idCompra = registrarCabeceraCompra(
                        cn,
                        numeroCompra,
                        idProveedor,
                        idUsuario
                );

                for (ItemOperacion item : items) {

                    agregarDetalleCompra(
                            cn,
                            idCompra,
                            item
                    );
                }

                cn.commit();

                return idCompra;

            } catch (Exception e) {

                try {
                    cn.rollback();
                } catch (SQLException ex) {
                    System.err.println(
                            "Error al hacer rollback: "
                            + ex.getMessage()
                    );
                }

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }

                throw new SQLException(
                        "Error al registrar la compra: "
                        + e.getMessage(),
                        e
                );

            } finally {

                try {
                    cn.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public int registrarVenta(
            int idCliente,
            int idUsuario,
            String numeroVenta,
            String metodoPago,
            List<ItemOperacion> items
    ) throws SQLException {

        if (items == null || items.isEmpty()) {
            throw new SQLException("Debe agregar al menos un producto.");
        }

        if (numeroVenta == null || numeroVenta.isBlank()) {
            numeroVenta = generarNumero("V");
        }

        if (metodoPago == null || metodoPago.isBlank()) {
            metodoPago = "EFECTIVO";
        }

        try (Connection cn = Conexion.conectar()) {

            cn.setAutoCommit(false);

            try {

                int idVenta = registrarCabeceraVenta(
                        cn,
                        numeroVenta,
                        idCliente,
                        idUsuario,
                        metodoPago
                );

                for (ItemOperacion item : items) {

                    agregarDetalleVenta(
                            cn,
                            idVenta,
                            item
                    );
                }

                cn.commit();

                return idVenta;

            } catch (Exception e) {

                try {
                    cn.rollback();
                } catch (SQLException ex) {
                    System.err.println(
                            "Error al hacer rollback: "
                            + ex.getMessage()
                    );
                }

                if (e instanceof SQLException) {
                    throw (SQLException) e;
                }

                throw new SQLException(
                        "Error al registrar la venta: "
                        + e.getMessage(),
                        e
                );

            } finally {

                try {
                    cn.setAutoCommit(true);
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private int registrarCabeceraCompra(
            Connection cn,
            String numeroCompra,
            int idProveedor,
            int idUsuario
    ) throws SQLException {

        String sql = "{CALL sp_registrar_compra(?,?,?)}";

        try (CallableStatement cs = cn.prepareCall(sql)) {

            cs.setString(1, numeroCompra);
            cs.setInt(2, idProveedor);
            cs.setInt(3, idUsuario);

            boolean tieneResultado = cs.execute();

            if (!tieneResultado) {
                throw new SQLException(
                        "No se recibió el ID de la compra."
                );
            }

            try (ResultSet rs = cs.getResultSet()) {

                if (rs != null && rs.next()) {
                    return rs.getInt("id_compra");
                }
            }
        }

        throw new SQLException(
                "No se pudo obtener el ID de la compra."
        );
    }

    private int registrarCabeceraVenta(
            Connection cn,
            String numeroVenta,
            int idCliente,
            int idUsuario,
            String metodoPago
    ) throws SQLException {

        String sql = "{CALL sp_registrar_venta(?,?,?,?)}";

        try (CallableStatement cs = cn.prepareCall(sql)) {

            cs.setString(1, numeroVenta);

            if (idCliente > 0) {
                cs.setInt(2, idCliente);
            } else {
                cs.setNull(2, Types.INTEGER);
            }

            cs.setInt(3, idUsuario);
            cs.setString(4, metodoPago);

            boolean tieneResultado = cs.execute();

            if (!tieneResultado) {
                throw new SQLException(
                        "No se recibió el ID de la venta."
                );
            }

            try (ResultSet rs = cs.getResultSet()) {

                if (rs != null && rs.next()) {
                    return rs.getInt("id_venta");
                }
            }
        }

        throw new SQLException(
                "No se pudo obtener el ID de la venta."
        );
    }

    private void agregarDetalleCompra(
            Connection cn,
            int idCompra,
            ItemOperacion item
    ) throws SQLException {

        String sql = "{CALL sp_agregar_detalle_compra(?,?,?,?)}";

        try (CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, idCompra);
            cs.setInt(2, item.getIdProducto());
            cs.setInt(3, item.getCantidad());
            cs.setDouble(4, item.getPrecio());

            cs.execute();

            cerrarResultados(cs);
        }
    }

    private void agregarDetalleVenta(
            Connection cn,
            int idVenta,
            ItemOperacion item
    ) throws SQLException {

        String sql = "{CALL sp_agregar_detalle_venta(?,?,?,?,?)}";

        try (CallableStatement cs = cn.prepareCall(sql)) {

            cs.setInt(1, idVenta);
            cs.setInt(2, item.getIdProducto());
            cs.setInt(3, item.getCantidad());
            cs.setDouble(4, item.getPrecio());

            // Por ahora no utilizamos descuentos.
            cs.setDouble(5, 0.00);

            cs.execute();

            cerrarResultados(cs);
        }
    }

    private void cerrarResultados(
            CallableStatement cs
    ) throws SQLException {

        while (cs.getMoreResults()
                || cs.getUpdateCount() != -1) {
        }
    }

    private String generarNumero(String prefijo) {

        long tiempo = System.currentTimeMillis();

        return prefijo + "-" + tiempo;
    }
}