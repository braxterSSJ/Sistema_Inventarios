package com.inventarios.vista;

import com.inventarios.modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private final Usuario usuario;

    public MenuPrincipal(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Sistema Inteligente de Inventarios");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializar();
    }

    private void inicializar() {

        JPanel principal = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel(
                "SISTEMA INTELIGENTE DE GESTIÓN DE INVENTARIOS",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        titulo.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        10,
                        20,
                        10
                )
        );

        JLabel usuarioLabel = new JLabel(
                "Usuario: " + usuario.getNombreCompleto()
                        + "  |  Rol: " + usuario.getNombreRol()
        );

        usuarioLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );

        JPanel contenido =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                15,
                                15
                        )
                );

        contenido.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        50,
                        30,
                        50
                )
        );

        agregarBoton(
                contenido,
                "USUARIOS Y SEGURIDAD"
        );

        agregarBoton(
                contenido,
                "GESTIÓN DE INVENTARIO"
        );

        agregarBoton(
                contenido,
                "COMPRAS Y VENTAS"
        );

        agregarBoton(
                contenido,
                "ANÁLISIS Y REPORTES"
        );

        agregarBoton(
                contenido,
                "PREDICCIÓN Y ALERTAS"
        );

        JButton salir =
                new JButton("CERRAR SESIÓN");

        salir.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        contenido.add(salir);

        principal.add(
                titulo,
                BorderLayout.NORTH
        );

        principal.add(
                contenido,
                BorderLayout.CENTER
        );

        principal.add(
                usuarioLabel,
                BorderLayout.SOUTH
        );

        setContentPane(principal);
    }

    private void agregarBoton(
            JPanel panel,
            String texto
    ) {

        JButton boton =
                new JButton(texto);

        boton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        boton.addActionListener(e -> {

            if (texto.equals(
                    "GESTIÓN DE INVENTARIO"
            )) {

                new FrmProductos()
                        .setVisible(true);

            } else if (texto.equals(
                    "COMPRAS Y VENTAS"
            )) {

                String[] opciones = {
                    "Registrar compra",
                    "Registrar venta"
                };

                int op =
                        JOptionPane.showOptionDialog(
                                this,
                                "Seleccione una operación",
                                "Compras y Ventas",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                opciones,
                                opciones[0]
                        );

                if (op == 0) {

                    new FrmOperaciones(
                            usuario,
                            true
                    ).setVisible(true);

                }

                if (op == 1) {

                    new FrmOperaciones(
                            usuario,
                            false
                    ).setVisible(true);
                }

            } else if (texto.equals(
                    "USUARIOS Y SEGURIDAD"
            )) {

                new FrmUsuarios()
                        .setVisible(true);

            } else if (texto.equals(
                    "ANÁLISIS Y REPORTES"
            )) {

                new FrmAnalisisReportes()
                        .setVisible(true);

            } else if (texto.equals(
                    "PREDICCIÓN Y ALERTAS"
            )) {

                new FrmPrediccionAlertas()
                        .setVisible(true);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Este módulo será desarrollado "
                        + "en las siguientes etapas.",
                        texto,
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        panel.add(boton);
    }
}