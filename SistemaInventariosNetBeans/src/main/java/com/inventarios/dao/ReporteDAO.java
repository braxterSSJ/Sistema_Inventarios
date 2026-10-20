package com.inventarios.dao;


import java.util.ArrayList;
import java.util.List;
import com.inventarios.conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ReporteDAO {

    // =========================================================
    // DASHBOARD
    // =========================================================

    public int totalProductos() {

        String sql = """
            SELECT COUNT(*)
            FROM producto
            WHERE estado = 1
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println(
                    "ReporteDAO - totalProductos: "
                    + e.getMessage()
            );
        }

        return 0;
    }

    public int productosStockBajo() {

        String sql = """
            SELECT COUNT(*)
            FROM producto
            WHERE estado = 1
              AND stock > 0
              AND stock <= stock_minimo
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println(
                    "ReporteDAO - productosStockBajo: "
                    + e.getMessage()
            );
        }

        return 0;
    }

    public int productosAgotados() {

        String sql = """
            SELECT COUNT(*)
            FROM producto
            WHERE estado = 1
              AND stock <= 0
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println(
                    "ReporteDAO - productosAgotados: "
                    + e.getMessage()
            );
        }

        return 0;
    }

    public int productosSobrestock() {

        String sql = """
            SELECT COUNT(*)
            FROM producto
            WHERE estado = 1
              AND stock > stock_maximo
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println(
                    "ReporteDAO - productosSobrestock: "
                    + e.getMessage()
            );
        }

        return 0;
    }

    public int unidadesStock() {

        String sql = """
            SELECT COALESCE(SUM(stock), 0)
            FROM producto
            WHERE estado = 1
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println(
                    "ReporteDAO - unidadesStock: "
                    + e.getMessage()
            );
        }

        return 0;
    }

    public double valorInventario() {

        String sql = """
            SELECT COALESCE(
                SUM(stock * precio_compra),
                0
            )
            FROM producto
            WHERE estado = 1
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.err.println(
                    "ReporteDAO - valorInventario: "
                    + e.getMessage()
            );
        }

        return 0;
    }

    public double totalVentas() {

        String sql = """
            SELECT COALESCE(SUM(total), 0)
            FROM venta
            WHERE estado = 'REGISTRADA'
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.err.println(
                    "ReporteDAO - totalVentas: "
                    + e.getMessage()
            );
        }

        return 0;
    }

    public double totalCompras() {

        String sql = """
            SELECT COALESCE(SUM(total), 0)
            FROM compra
            WHERE estado = 'REGISTRADA'
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.err.println(
                    "ReporteDAO - totalCompras: "
                    + e.getMessage()
            );
        }

        return 0;
    }

    public int cantidadVentas() {

        String sql = """
            SELECT COUNT(*)
            FROM venta
            WHERE estado = 'REGISTRADA'
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println(
                    "ReporteDAO - cantidadVentas: "
                    + e.getMessage()
            );
        }

        return 0;
    }

    public int cantidadCompras() {

        String sql = """
            SELECT COUNT(*)
            FROM compra
            WHERE estado = 'REGISTRADA'
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println(
                    "ReporteDAO - cantidadCompras: "
                    + e.getMessage()
            );
        }

        return 0;
    }

    // =========================================================
    // REPORTE INVENTARIO
    // =========================================================

    public List<Object[]> reporteInventario(String filtro) {

        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT
                p.codigo,
                p.nombre,
                c.nombre AS categoria,
                pr.razon_social AS proveedor,
                p.stock,
                p.stock_minimo,
                p.stock_maximo,
                p.precio_compra,
                p.precio_venta
            FROM producto p
            INNER JOIN categoria c
                ON p.id_categoria = c.id_categoria
            INNER JOIN proveedor pr
                ON p.id_proveedor = pr.id_proveedor
            WHERE p.estado = 1
              AND (
                    p.codigo LIKE ?
                    OR p.nombre LIKE ?
                    OR c.nombre LIKE ?
                    OR pr.razon_social LIKE ?
                  )
            ORDER BY p.nombre
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            String f = "%" +
                    (filtro == null
                            ? ""
                            : filtro.trim())
                    + "%";

            ps.setString(1, f);
            ps.setString(2, f);
            ps.setString(3, f);
            ps.setString(4, f);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            new Object[]{
                                    rs.getString("codigo"),
                                    rs.getString("nombre"),
                                    rs.getString("categoria"),
                                    rs.getString("proveedor"),
                                    rs.getInt("stock"),
                                    rs.getInt("stock_minimo"),
                                    rs.getInt("stock_maximo"),
                                    rs.getDouble("precio_compra"),
                                    rs.getDouble("precio_venta")
                            }
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "ReporteDAO - reporteInventario: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    // =========================================================
    // REPORTE VENTAS
    // =========================================================

    public List<Object[]> reporteVentas() {

        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT
                v.numero_venta,
                v.fecha_venta,
                COALESCE(
                    c.nombre_completo,
                    'CLIENTE GENERAL'
                ) AS cliente,
                u.nombre_completo AS usuario,
                v.metodo_pago,
                v.subtotal,
                v.impuesto,
                v.total,
                v.estado
            FROM venta v
            LEFT JOIN cliente c
                ON v.id_cliente = c.id_cliente
            INNER JOIN usuario u
                ON v.id_usuario = u.id_usuario
            ORDER BY v.fecha_venta DESC
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                lista.add(
                        new Object[]{
                                rs.getString("numero_venta"),
                                rs.getTimestamp("fecha_venta"),
                                rs.getString("cliente"),
                                rs.getString("usuario"),
                                rs.getString("metodo_pago"),
                                rs.getDouble("subtotal"),
                                rs.getDouble("impuesto"),
                                rs.getDouble("total"),
                                rs.getString("estado")
                        }
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "ReporteDAO - reporteVentas: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    // =========================================================
    // REPORTE COMPRAS
    // =========================================================

    public List<Object[]> reporteCompras() {

        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT
                c.numero_compra,
                c.fecha_compra,
                p.razon_social AS proveedor,
                u.nombre_completo AS usuario,
                c.subtotal,
                c.impuesto,
                c.total,
                c.estado
            FROM compra c
            INNER JOIN proveedor p
                ON c.id_proveedor = p.id_proveedor
            INNER JOIN usuario u
                ON c.id_usuario = u.id_usuario
            ORDER BY c.fecha_compra DESC
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                lista.add(
                        new Object[]{
                                rs.getString("numero_compra"),
                                rs.getTimestamp("fecha_compra"),
                                rs.getString("proveedor"),
                                rs.getString("usuario"),
                                rs.getDouble("subtotal"),
                                rs.getDouble("impuesto"),
                                rs.getDouble("total"),
                                rs.getString("estado")
                        }
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "ReporteDAO - reporteCompras: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    // =========================================================
    // PRODUCTOS MÁS VENDIDOS
    // =========================================================

    public List<Object[]> productosMasVendidos() {

        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT
                p.codigo,
                p.nombre,
                SUM(dv.cantidad) AS cantidad_vendida,
                SUM(dv.subtotal) AS importe_vendido
            FROM detalle_venta dv
            INNER JOIN venta v
                ON dv.id_venta = v.id_venta
            INNER JOIN producto p
                ON dv.id_producto = p.id_producto
            WHERE v.estado = 'REGISTRADA'
            GROUP BY
                p.id_producto,
                p.codigo,
                p.nombre
            ORDER BY cantidad_vendida DESC
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                lista.add(
                        new Object[]{
                                rs.getString("codigo"),
                                rs.getString("nombre"),
                                rs.getInt("cantidad_vendida"),
                                rs.getDouble("importe_vendido")
                        }
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "ReporteDAO - productosMasVendidos: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    // =========================================================
    // KARDEX
    // =========================================================

    public List<Object[]> reporteKardex() {

        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT
                m.fecha_movimiento,
                p.codigo,
                p.nombre,
                m.tipo_movimiento,
                m.origen,
                m.referencia,
                m.cantidad,
                m.stock_anterior,
                m.stock_nuevo,
                COALESCE(
                    u.nombre_completo,
                    'SISTEMA'
                ) AS usuario,
                m.observacion
            FROM movimiento_inventario m
            INNER JOIN producto p
                ON m.id_producto = p.id_producto
            LEFT JOIN usuario u
                ON m.id_usuario = u.id_usuario
            ORDER BY m.fecha_movimiento DESC
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                lista.add(
                        new Object[]{
                                rs.getTimestamp(
                                        "fecha_movimiento"
                                ),
                                rs.getString("codigo"),
                                rs.getString("nombre"),
                                rs.getString(
                                        "tipo_movimiento"
                                ),
                                rs.getString("origen"),
                                rs.getString("referencia"),
                                rs.getInt("cantidad"),
                                rs.getInt("stock_anterior"),
                                rs.getInt("stock_nuevo"),
                                rs.getString("usuario"),
                                rs.getString("observacion")
                        }
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "ReporteDAO - reporteKardex: "
                    + e.getMessage()
            );
        }

        return lista;
    }
    
 // ==========================================================
// VENTAS POR RANGO DE FECHAS
// ==========================================================
public List<Object[]> reporteVentasPorFechas(String fechaDesde, String fechaHasta) {

    List<Object[]> lista = new ArrayList<>();

    String sql = """
        SELECT
            v.numero_venta,
            v.fecha_venta,
            COALESCE(c.nombre_completo, 'CLIENTE GENERAL') AS cliente,
            u.nombre_completo AS usuario,
            v.metodo_pago,
            v.subtotal,
            v.impuesto,
            v.total,
            v.estado
        FROM venta v
        LEFT JOIN cliente c
            ON v.id_cliente = c.id_cliente
        INNER JOIN usuario u
            ON v.id_usuario = u.id_usuario
        WHERE DATE(v.fecha_venta) BETWEEN ? AND ?
        ORDER BY v.fecha_venta DESC
        """;

    try (Connection cn = Conexion.conectar();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setString(1, fechaDesde);
        ps.setString(2, fechaHasta);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                lista.add(new Object[]{
                    rs.getString("numero_venta"),
                    rs.getTimestamp("fecha_venta"),
                    rs.getString("cliente"),
                    rs.getString("usuario"),
                    rs.getString("metodo_pago"),
                    rs.getBigDecimal("subtotal"),
                    rs.getBigDecimal("impuesto"),
                    rs.getBigDecimal("total"),
                    rs.getString("estado")
                });
            }
        }

    } catch (SQLException e) {
        System.err.println("Error reporteVentasPorFechas: " + e.getMessage());
    }

    return lista;
}


// ==========================================================
// COMPRAS POR RANGO DE FECHAS
// ==========================================================
public List<Object[]> reporteComprasPorFechas(String fechaDesde, String fechaHasta) {

    List<Object[]> lista = new ArrayList<>();

    String sql = """
        SELECT
            c.numero_compra,
            c.fecha_compra,
            p.razon_social AS proveedor,
            u.nombre_completo AS usuario,
            c.subtotal,
            c.impuesto,
            c.total,
            c.estado
        FROM compra c
        INNER JOIN proveedor p
            ON c.id_proveedor = p.id_proveedor
        INNER JOIN usuario u
            ON c.id_usuario = u.id_usuario
        WHERE DATE(c.fecha_compra) BETWEEN ? AND ?
        ORDER BY c.fecha_compra DESC
        """;

    try (Connection cn = Conexion.conectar();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setString(1, fechaDesde);
        ps.setString(2, fechaHasta);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                lista.add(new Object[]{
                    rs.getString("numero_compra"),
                    rs.getTimestamp("fecha_compra"),
                    rs.getString("proveedor"),
                    rs.getString("usuario"),
                    rs.getBigDecimal("subtotal"),
                    rs.getBigDecimal("impuesto"),
                    rs.getBigDecimal("total"),
                    rs.getString("estado")
                });
            }
        }

    } catch (SQLException e) {
        System.err.println("Error reporteComprasPorFechas: " + e.getMessage());
    }

    return lista;
}


// ==========================================================
// KARDEX POR RANGO DE FECHAS
// ==========================================================
public List<Object[]> reporteKardexPorFechas(String fechaDesde, String fechaHasta) {

    List<Object[]> lista = new ArrayList<>();

    String sql = """
        SELECT
            m.fecha_movimiento,
            p.codigo,
            p.nombre AS producto,
            m.tipo_movimiento,
            m.origen,
            m.referencia,
            m.cantidad,
            m.stock_anterior,
            m.stock_nuevo,
            COALESCE(u.nombre_completo, 'SISTEMA') AS usuario,
            m.observacion
        FROM movimiento_inventario m
        INNER JOIN producto p
            ON m.id_producto = p.id_producto
        LEFT JOIN usuario u
            ON m.id_usuario = u.id_usuario
        WHERE DATE(m.fecha_movimiento) BETWEEN ? AND ?
        ORDER BY m.fecha_movimiento DESC
        """;

    try (Connection cn = Conexion.conectar();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setString(1, fechaDesde);
        ps.setString(2, fechaHasta);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                lista.add(new Object[]{
                    rs.getTimestamp("fecha_movimiento"),
                    rs.getString("codigo"),
                    rs.getString("producto"),
                    rs.getString("tipo_movimiento"),
                    rs.getString("origen"),
                    rs.getString("referencia"),
                    rs.getInt("cantidad"),
                    rs.getInt("stock_anterior"),
                    rs.getInt("stock_nuevo"),
                    rs.getString("usuario"),
                    rs.getString("observacion")
                });
            }
        }

    } catch (SQLException e) {
        System.err.println("Error reporteKardexPorFechas: " + e.getMessage());
    }

    return lista;
}   
}