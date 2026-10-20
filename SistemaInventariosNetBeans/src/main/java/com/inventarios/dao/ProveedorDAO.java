package com.inventarios.dao;
import com.inventarios.conexion.Conexion; import com.inventarios.modelo.Proveedor; import java.sql.*; import java.util.*;
public class ProveedorDAO {
 public List<Proveedor> listar(){List<Proveedor> l=new ArrayList<>();String s="SELECT * FROM proveedor WHERE estado=1 ORDER BY razon_social";
 try(Connection c=Conexion.conectar();PreparedStatement p=c.prepareStatement(s);ResultSet r=p.executeQuery()){while(r.next())l.add(new Proveedor(r.getInt("id_proveedor"),r.getString("ruc"),r.getString("razon_social"),r.getString("contacto"),r.getString("telefono"),r.getString("correo"),r.getString("direccion"),r.getBoolean("estado")));}catch(SQLException e){System.err.println(e.getMessage());}return l;}
}
