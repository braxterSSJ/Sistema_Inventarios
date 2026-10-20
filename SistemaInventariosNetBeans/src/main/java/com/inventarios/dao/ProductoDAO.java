package com.inventarios.dao;

import com.inventarios.conexion.Conexion;
import com.inventarios.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private final String SELECT="SELECT p.*,c.nombre AS nombre_categoria,pr.razon_social AS nombre_proveedor FROM producto p INNER JOIN categoria c ON p.id_categoria=c.id_categoria INNER JOIN proveedor pr ON p.id_proveedor=pr.id_proveedor ";

    public List<Producto> listar(String filtro){
        List<Producto> l=new ArrayList<>();
        String sql=SELECT+"WHERE p.estado=1 AND (p.codigo LIKE ? OR p.nombre LIKE ?) ORDER BY p.nombre";
        try(Connection c=Conexion.conectar();PreparedStatement p=c.prepareStatement(sql)){
            String f="%"+(filtro==null?"":filtro.trim())+"%";p.setString(1,f);p.setString(2,f);
            try(ResultSet r=p.executeQuery()){while(r.next())l.add(map(r));}
        }catch(SQLException e){System.err.println("ProductoDAO: "+e.getMessage());}
        return l;
    }

    public boolean insertar(Producto x){
        String sql="INSERT INTO producto(codigo,nombre,descripcion,id_categoria,id_proveedor,unidad_medida,precio_compra,precio_venta,stock,stock_minimo,stock_maximo) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try(Connection c=Conexion.conectar();PreparedStatement p=c.prepareStatement(sql)){
            set(p,x);p.executeUpdate();return true;
        }catch(SQLException e){System.err.println("ProductoDAO: "+e.getMessage());return false;}
    }

    public boolean actualizar(Producto x){
        String sql="UPDATE producto SET codigo=?,nombre=?,descripcion=?,id_categoria=?,id_proveedor=?,unidad_medida=?,precio_compra=?,precio_venta=?,stock_minimo=?,stock_maximo=? WHERE id_producto=?";
        try(Connection c=Conexion.conectar();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,x.getCodigo());p.setString(2,x.getNombre());p.setString(3,x.getDescripcion());
            p.setInt(4,x.getIdCategoria());p.setInt(5,x.getIdProveedor());p.setString(6,x.getUnidadMedida());
            p.setDouble(7,x.getPrecioCompra());p.setDouble(8,x.getPrecioVenta());p.setInt(9,x.getStockMinimo());
            p.setInt(10,x.getStockMaximo());p.setInt(11,x.getIdProducto());p.executeUpdate();return true;
        }catch(SQLException e){System.err.println("ProductoDAO: "+e.getMessage());return false;}
    }

    public boolean eliminar(int id){
        try(Connection c=Conexion.conectar();PreparedStatement p=c.prepareStatement("UPDATE producto SET estado=0 WHERE id_producto=?")){
            p.setInt(1,id);p.executeUpdate();return true;
        }catch(SQLException e){System.err.println("ProductoDAO: "+e.getMessage());return false;}
    }

    private void set(PreparedStatement p,Producto x)throws SQLException{
        p.setString(1,x.getCodigo());p.setString(2,x.getNombre());p.setString(3,x.getDescripcion());
        p.setInt(4,x.getIdCategoria());p.setInt(5,x.getIdProveedor());p.setString(6,x.getUnidadMedida());
        p.setDouble(7,x.getPrecioCompra());p.setDouble(8,x.getPrecioVenta());p.setInt(9,x.getStock());
        p.setInt(10,x.getStockMinimo());p.setInt(11,x.getStockMaximo());
    }

    private Producto map(ResultSet r)throws SQLException{
        Producto x=new Producto();
        x.setIdProducto(r.getInt("id_producto"));x.setCodigo(r.getString("codigo"));x.setNombre(r.getString("nombre"));
        x.setDescripcion(r.getString("descripcion"));x.setIdCategoria(r.getInt("id_categoria"));x.setNombreCategoria(r.getString("nombre_categoria"));
        x.setIdProveedor(r.getInt("id_proveedor"));x.setNombreProveedor(r.getString("nombre_proveedor"));x.setUnidadMedida(r.getString("unidad_medida"));
        x.setPrecioCompra(r.getDouble("precio_compra"));x.setPrecioVenta(r.getDouble("precio_venta"));x.setStock(r.getInt("stock"));
        x.setStockMinimo(r.getInt("stock_minimo"));x.setStockMaximo(r.getInt("stock_maximo"));x.setEstado(r.getBoolean("estado"));return x;
    }
}
