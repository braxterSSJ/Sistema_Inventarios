package com.inventarios.dao;

import com.inventarios.conexion.Conexion;
import com.inventarios.modelo.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public List<Categoria> listar(){
        List<Categoria> l=new ArrayList<>();
        String sql="SELECT * FROM categoria WHERE estado=1 ORDER BY nombre";
        try(Connection c=Conexion.conectar();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next()) l.add(new Categoria(r.getInt("id_categoria"),r.getString("nombre"),r.getString("descripcion"),r.getBoolean("estado")));
        }catch(SQLException e){System.err.println("CategoriaDAO: "+e.getMessage());}
        return l;
    }
}
