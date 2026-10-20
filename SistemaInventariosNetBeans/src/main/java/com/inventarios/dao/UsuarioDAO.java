package com.inventarios.dao;

import com.inventarios.conexion.Conexion;
import com.inventarios.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // =========================================================
    // LOGIN
    // =========================================================

    public Usuario validarLogin(String usuario, String password) {

        String sql = """
            SELECT u.id_usuario,
                   u.usuario,
                   u.password,
                   u.nombre_completo,
                   u.correo,
                   u.id_rol,
                   r.nombre AS nombre_rol,
                   u.estado
            FROM usuario u
            INNER JOIN rol r ON u.id_rol = r.id_rol
            WHERE u.usuario = ?
              AND u.password = ?
              AND u.estado = 1
            LIMIT 1
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(1, usuario);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Usuario u = new Usuario();

                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setPassword(rs.getString("password"));
                    u.setNombreCompleto(
                            rs.getString("nombre_completo")
                    );
                    u.setCorreo(rs.getString("correo"));
                    u.setIdRol(rs.getInt("id_rol"));
                    u.setNombreRol(rs.getString("nombre_rol"));
                    u.setEstado(rs.getBoolean("estado"));

                    return u;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "UsuarioDAO - Error al validar usuario: "
                    + e.getMessage()
            );
        }

        return null;
    }

    // =========================================================
    // LISTAR USUARIOS
    // =========================================================

    public List<Usuario> listar() {

        List<Usuario> lista = new ArrayList<>();

        String sql = """
            SELECT u.id_usuario,
                   u.usuario,
                   u.password,
                   u.nombre_completo,
                   u.correo,
                   u.id_rol,
                   r.nombre AS nombre_rol,
                   u.estado
            FROM usuario u
            INNER JOIN rol r ON u.id_rol = r.id_rol
            ORDER BY u.nombre_completo
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Usuario u = new Usuario();

                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setUsuario(rs.getString("usuario"));
                u.setPassword(rs.getString("password"));
                u.setNombreCompleto(
                        rs.getString("nombre_completo")
                );
                u.setCorreo(rs.getString("correo"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setNombreRol(rs.getString("nombre_rol"));
                u.setEstado(rs.getBoolean("estado"));

                lista.add(u);
            }

        } catch (SQLException e) {

            System.err.println(
                    "UsuarioDAO - Error al listar usuarios: "
                    + e.getMessage()
            );
        }

        return lista;
    }

    // =========================================================
    // INSERTAR
    // =========================================================

    public boolean insertar(Usuario u) {

        String sql = """
            INSERT INTO usuario
            (
                usuario,
                password,
                nombre_completo,
                correo,
                id_rol,
                estado
            )
            VALUES (?, ?, ?, ?, ?, 1)
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNombreCompleto());
            ps.setString(4, u.getCorreo());
            ps.setInt(5, u.getIdRol());

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "UsuarioDAO - Error al insertar usuario: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // ACTUALIZAR
    // =========================================================

    public boolean actualizar(Usuario u) {

        String sql = """
            UPDATE usuario
            SET usuario = ?,
                nombre_completo = ?,
                correo = ?,
                id_rol = ?
            WHERE id_usuario = ?
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getNombreCompleto());
            ps.setString(3, u.getCorreo());
            ps.setInt(4, u.getIdRol());
            ps.setInt(5, u.getIdUsuario());

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "UsuarioDAO - Error al actualizar usuario: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // CAMBIAR CONTRASEÑA
    // =========================================================

    public boolean cambiarPassword(int idUsuario, String password) {

        String sql = """
            UPDATE usuario
            SET password = ?
            WHERE id_usuario = ?
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setString(1, password);
            ps.setInt(2, idUsuario);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "UsuarioDAO - Error al cambiar contraseña: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // ACTIVAR / DESACTIVAR
    // =========================================================

    public boolean cambiarEstado(int idUsuario, boolean estado) {

        String sql = """
            UPDATE usuario
            SET estado = ?
            WHERE id_usuario = ?
            """;

        try (
                Connection cn = Conexion.conectar();
                PreparedStatement ps = cn.prepareStatement(sql)
        ) {

            ps.setBoolean(1, estado);
            ps.setInt(2, idUsuario);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.err.println(
                    "UsuarioDAO - Error al cambiar estado: "
                    + e.getMessage()
            );

            return false;
        }
    }
}
