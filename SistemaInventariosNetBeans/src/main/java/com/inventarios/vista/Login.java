package com.inventarios.vista;

import com.inventarios.conexion.Conexion;
import com.inventarios.controlador.LoginController;
import com.inventarios.modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JLabel lblEstado;

    private final LoginController controller;

    public Login() {
        controller = new LoginController();
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Sistema Inteligente de Inventarios - Login");
        setSize(460, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void inicializarComponentes() {

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel titulo = new JLabel("SISTEMA DE INVENTARIOS", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel subtitulo = new JLabel(
                "Gestión, análisis y predicción",
                SwingConstants.CENTER
        );
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel encabezado = new JPanel(new GridLayout(2, 1));
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 5, 7, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblPassword = new JLabel("Contraseña:");

        txtUsuario = new JTextField();
        txtPassword = new JPasswordField();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        formulario.add(lblUsuario, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formulario.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formulario.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formulario.add(txtPassword, gbc);

        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 14));

        lblEstado = new JLabel(" ", SwingConstants.CENTER);

        JPanel inferior = new JPanel(new BorderLayout(5, 8));
        inferior.add(btnIngresar, BorderLayout.CENTER);
        inferior.add(lblEstado, BorderLayout.SOUTH);

        principal.add(encabezado, BorderLayout.NORTH);
        principal.add(formulario, BorderLayout.CENTER);
        principal.add(inferior, BorderLayout.SOUTH);

        setContentPane(principal);

        btnIngresar.addActionListener(e -> iniciarSesion());
        txtPassword.addActionListener(e -> iniciarSesion());

        getRootPane().setDefaultButton(btnIngresar);
    }

    private void iniciarSesion() {

        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarError("Ingrese usuario y contraseña.");
            return;
        }

        btnIngresar.setEnabled(false);
        lblEstado.setText("Validando...");

        SwingUtilities.invokeLater(() -> {

            if (!Conexion.probarConexion()) {
                mostrarError("No se pudo conectar con MySQL.");
                btnIngresar.setEnabled(true);
                return;
            }

            Usuario usuarioLogueado =
                    controller.iniciarSesion(usuario, password);

            if (usuarioLogueado != null) {

                lblEstado.setText("Acceso correcto.");

                JOptionPane.showMessageDialog(
                        this,
                        "Bienvenido(a), "
                                + usuarioLogueado.getNombreCompleto()
                                + "\nRol: "
                                + usuarioLogueado.getNombreRol(),
                        "Inicio de sesión",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                MenuPrincipal menu =
                        new MenuPrincipal(usuarioLogueado);

                menu.setVisible(true);

            } else {

                mostrarError("Usuario o contraseña incorrectos.");
                txtPassword.setText("");
                txtPassword.requestFocus();
                btnIngresar.setEnabled(true);
            }
        });
    }

    private void mostrarError(String mensaje) {
        lblEstado.setText(mensaje);
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}
