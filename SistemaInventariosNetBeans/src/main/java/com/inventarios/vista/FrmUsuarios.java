package com.inventarios.vista;

import com.inventarios.dao.RolDAO;
import com.inventarios.dao.UsuarioDAO;
import com.inventarios.modelo.Rol;
import com.inventarios.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmUsuarios extends JFrame {

    private JTextField txtUsuario;
    private JTextField txtNombre;
    private JTextField txtCorreo;

    private JPasswordField txtPassword;

    private JComboBox<Rol> cboRol;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnCambiarPassword;
    private JButton btnCambiarEstado;

    private final UsuarioDAO usuarioDAO;
    private final RolDAO rolDAO;

    private int idUsuarioSeleccionado = 0;

    public FrmUsuarios() {

        usuarioDAO = new UsuarioDAO();
        rolDAO = new RolDAO();

        configurarVentana();
        inicializarComponentes();

        cargarRoles();
        cargarUsuarios();
        limpiarFormulario();
    }

    // =========================================================
    // VENTANA
    // =========================================================

    private void configurarVentana() {

        setTitle("Gestión de Usuarios y Seguridad");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // =========================================================
    // COMPONENTES
    // =========================================================

    private void inicializarComponentes() {

        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // -----------------------------------------------------
        // TÍTULO
        // -----------------------------------------------------

        JLabel titulo = new JLabel(
                "GESTIÓN DE USUARIOS Y SEGURIDAD",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 22)
        );

        principal.add(titulo, BorderLayout.NORTH);

        // -----------------------------------------------------
        // FORMULARIO
        // -----------------------------------------------------

        JPanel formulario = new JPanel(
                new GridBagLayout()
        );

        formulario.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del usuario"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(5, 5, 5, 5);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        txtUsuario = new JTextField();
        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtPassword = new JPasswordField();

        cboRol = new JComboBox<>();

        agregarCampo(
                formulario,
                gbc,
                0,
                "Usuario:",
                txtUsuario
        );

        agregarCampo(
                formulario,
                gbc,
                1,
                "Nombre completo:",
                txtNombre
        );

        agregarCampo(
                formulario,
                gbc,
                2,
                "Correo:",
                txtCorreo
        );

        agregarCampo(
                formulario,
                gbc,
                3,
                "Contraseña:",
                txtPassword
        );

        agregarCampo(
                formulario,
                gbc,
                4,
                "Rol:",
                cboRol
        );

        principal.add(
                formulario,
                BorderLayout.WEST
        );

        // -----------------------------------------------------
        // TABLA
        // -----------------------------------------------------

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Usuario",
                        "Nombre completo",
                        "Correo",
                        "Rol",
                        "Estado"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabla.setRowHeight(25);

        tabla.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        seleccionarUsuario();
                    }
                });

        JScrollPane scroll =
                new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createTitledBorder(
                        "Usuarios registrados"
                )
        );

        principal.add(
                scroll,
                BorderLayout.CENTER
        );

        // -----------------------------------------------------
        // BOTONES
        // -----------------------------------------------------

        JPanel botones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        btnNuevo =
                new JButton("NUEVO");

        btnGuardar =
                new JButton("GUARDAR");

        btnActualizar =
                new JButton("ACTUALIZAR");

        btnCambiarPassword =
                new JButton("CAMBIAR CONTRASEÑA");

        btnCambiarEstado =
                new JButton("ACTIVAR / DESACTIVAR");

        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnCambiarPassword);
        botones.add(btnCambiarEstado);

        principal.add(
                botones,
                BorderLayout.SOUTH
        );

        setContentPane(principal);

        // -----------------------------------------------------
        // EVENTOS
        // -----------------------------------------------------

        btnNuevo.addActionListener(
                e -> nuevoUsuario()
        );

        btnGuardar.addActionListener(
                e -> guardarUsuario()
        );

        btnActualizar.addActionListener(
                e -> actualizarUsuario()
        );

        btnCambiarPassword.addActionListener(
                e -> cambiarPassword()
        );

        btnCambiarEstado.addActionListener(
                e -> cambiarEstado()
        );
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String texto,
            JComponent componente
    ) {

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;

        panel.add(
                new JLabel(texto),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        componente.setPreferredSize(
                new Dimension(260, 30)
        );

        panel.add(
                componente,
                gbc
        );
    }

    // =========================================================
    // CARGAR ROLES
    // =========================================================

    private void cargarRoles() {

        cboRol.removeAllItems();

        List<Rol> roles =
                rolDAO.listar();

        for (Rol rol : roles) {
            cboRol.addItem(rol);
        }
    }

    // =========================================================
    // CARGAR USUARIOS
    // =========================================================

    private void cargarUsuarios() {

        modeloTabla.setRowCount(0);

        List<Usuario> usuarios =
                usuarioDAO.listar();

        for (Usuario u : usuarios) {

            modeloTabla.addRow(
                    new Object[]{
                            u.getIdUsuario(),
                            u.getUsuario(),
                            u.getNombreCompleto(),
                            u.getCorreo(),
                            u.getNombreRol(),
                            u.isEstado()
                                    ? "ACTIVO"
                                    : "INACTIVO"
                    }
            );
        }
    }

    // =========================================================
    // SELECCIONAR USUARIO
    // =========================================================

private void seleccionarUsuario() {

    int fila = tabla.getSelectedRow();

    if (fila < 0) {
        return;
    }

    idUsuarioSeleccionado =
            Integer.parseInt(
                    modeloTabla.getValueAt(fila, 0).toString()
            );

    String usuario =
            modeloTabla.getValueAt(fila, 1).toString();

    String nombre =
            modeloTabla.getValueAt(fila, 2).toString();

    String correo =
            modeloTabla.getValueAt(fila, 3).toString();

    String nombreRol =
            modeloTabla.getValueAt(fila, 4).toString();

    txtUsuario.setText(usuario);
    txtNombre.setText(nombre);
    txtCorreo.setText(correo);

    // La contraseña no se muestra.
    txtPassword.setText("");
    txtPassword.setEditable(false);

    // Seleccionar el rol correspondiente.
    for (int i = 0; i < cboRol.getItemCount(); i++) {

        Rol rol = cboRol.getItemAt(i);

        if (rol.getNombre().equalsIgnoreCase(nombreRol)) {

            cboRol.setSelectedIndex(i);
            break;
        }
    }

    // =====================================================
    // ACTIVAR BOTONES AL SELECCIONAR UN USUARIO
    // =====================================================

    btnGuardar.setEnabled(false);
    btnActualizar.setEnabled(true);
    btnCambiarPassword.setEnabled(true);
    btnCambiarEstado.setEnabled(true);
}
    // =========================================================
    // NUEVO
    // =========================================================

private void nuevoUsuario() {

    limpiarFormulario();

    txtUsuario.setEditable(true);
    txtPassword.setEditable(true);

    btnGuardar.setEnabled(true);
    btnActualizar.setEnabled(false);
    btnCambiarPassword.setEnabled(false);
    btnCambiarEstado.setEnabled(false);

    txtUsuario.requestFocus();
}
    // =========================================================
    // LIMPIAR
    // =========================================================

    private void limpiarFormulario() {

        idUsuarioSeleccionado = 0;

        txtUsuario.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");

        txtPassword.setEditable(true);

        if (cboRol.getItemCount() > 0) {
            cboRol.setSelectedIndex(0);
        }

        tabla.clearSelection();

        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnCambiarPassword.setEnabled(false);
        btnCambiarEstado.setEnabled(false);
    }

    // =========================================================
    // GUARDAR
    // =========================================================

    private void guardarUsuario() {

        if (idUsuarioSeleccionado != 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Presione NUEVO para registrar otro usuario.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String usuario =
                txtUsuario.getText().trim();

        String nombre =
                txtNombre.getText().trim();

        String correo =
                txtCorreo.getText().trim();

        String password =
                new String(
                        txtPassword.getPassword()
                );

        if (usuario.isEmpty()
                || nombre.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complete usuario, nombre y contraseña.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (cboRol.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un rol.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Rol rol =
                (Rol) cboRol.getSelectedItem();

        Usuario u =
                new Usuario();

        u.setUsuario(usuario);
        u.setPassword(password);
        u.setNombreCompleto(nombre);
        u.setCorreo(correo);
        u.setIdRol(rol.getIdRol());
        u.setEstado(true);

        if (usuarioDAO.insertar(u)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario registrado correctamente.",
                    "Usuarios",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarUsuarios();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el usuario.\n"
                    + "Verifique que el nombre de usuario no exista.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // ACTUALIZAR
    // =========================================================

    private void actualizarUsuario() {

        if (idUsuarioSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un usuario.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String usuario =
                txtUsuario.getText().trim();

        String nombre =
                txtNombre.getText().trim();

        String correo =
                txtCorreo.getText().trim();

        if (usuario.isEmpty()
                || nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Complete usuario y nombre completo.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Rol rol =
                (Rol) cboRol.getSelectedItem();

        if (rol == null) {
            return;
        }

        Usuario u =
                new Usuario();

        u.setIdUsuario(
                idUsuarioSeleccionado
        );

        u.setUsuario(usuario);
        u.setNombreCompleto(nombre);
        u.setCorreo(correo);
        u.setIdRol(rol.getIdRol());

        if (usuarioDAO.actualizar(u)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario actualizado correctamente.",
                    "Usuarios",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarUsuarios();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el usuario.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // CAMBIAR CONTRASEÑA
    // =========================================================

    private void cambiarPassword() {

        if (idUsuarioSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un usuario.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JPasswordField campo =
                new JPasswordField();

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        campo,
                        "Nueva contraseña",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        String password =
                new String(
                        campo.getPassword()
                ).trim();

        if (password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "La contraseña no puede estar vacía.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (usuarioDAO.cambiarPassword(
                idUsuarioSeleccionado,
                password
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Contraseña actualizada correctamente.",
                    "Seguridad",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cambiar la contraseña.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // ACTIVAR / DESACTIVAR
    // =========================================================

    private void cambiarEstado() {

        if (idUsuarioSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un usuario.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int fila =
                tabla.getSelectedRow();

        if (fila < 0) {
            return;
        }

        String estadoActual =
                modeloTabla
                        .getValueAt(fila, 5)
                        .toString();

        boolean nuevoEstado =
                estadoActual.equals("INACTIVO");

        String mensaje =
                nuevoEstado
                        ? "¿Desea activar este usuario?"
                        : "¿Desea desactivar este usuario?";

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        mensaje,
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        if (usuarioDAO.cambiarEstado(
                idUsuarioSeleccionado,
                nuevoEstado
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    nuevoEstado
                            ? "Usuario activado correctamente."
                            : "Usuario desactivado correctamente.",
                    "Usuarios",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarUsuarios();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo cambiar el estado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}