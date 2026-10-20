package com.inventarios.vista;

import com.inventarios.dao.ClienteDAO;
import com.inventarios.dao.OperacionDAO;
import com.inventarios.dao.ProductoOperacionDAO;
import com.inventarios.dao.ProveedorDAO;
import com.inventarios.modelo.Cliente;
import com.inventarios.modelo.ItemOperacion;
import com.inventarios.modelo.Producto;
import com.inventarios.modelo.Proveedor;
import com.inventarios.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FrmOperaciones extends JFrame {

    private final Usuario usuario;
    private final boolean compra;

    private final OperacionDAO dao = new OperacionDAO();
    private final ProductoOperacionDAO pdao = new ProductoOperacionDAO();
    private final ProveedorDAO prdao = new ProveedorDAO();
    private final ClienteDAO cdao = new ClienteDAO();

    private JComboBox<Proveedor> proveedor;
    private JComboBox<Cliente> cliente;
    private JComboBox<Producto> producto;
    private JComboBox<String> pago;

    private JTextField cantidad;
    private JTextField precio;
    private JTextField comprobante;

    private JTable tabla;
    private DefaultTableModel modelo;

    private final List<ItemOperacion> items = new ArrayList<>();

    private JLabel total;

    public FrmOperaciones(Usuario usuario, boolean compra) {

        this.usuario = usuario;
        this.compra = compra;

        setTitle(
                compra
                        ? "Sistema de Inventarios - Registrar Compra"
                        : "Sistema de Inventarios - Registrar Venta"
        );

        setSize(1100, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        construir();
        cargar();
    }

    // ============================================================
    // CONSTRUCCIÓN DE LA INTERFAZ
    // ============================================================

    private void construir() {

        JPanel principal = new JPanel(new BorderLayout(10, 10));

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        // --------------------------------------------------------
        // TÍTULO
        // --------------------------------------------------------

        JLabel titulo = new JLabel(
                compra
                        ? "REGISTRO DE COMPRAS"
                        : "REGISTRO DE VENTAS",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );

        titulo.setBorder(
                BorderFactory.createEmptyBorder(
                        5, 5, 15, 5
                )
        );

        principal.add(titulo, BorderLayout.NORTH);

// --------------------------------------------------------
// PANEL DE DATOS
// --------------------------------------------------------

JPanel datos = new JPanel(
        new GridLayout(
                3,
                4,
                8,
                8
        )
);

// --------------------------------------------------------
// COMPONENTES
// --------------------------------------------------------

comprobante = new JTextField();

cantidad = new JTextField("1");

precio = new JTextField();

producto = new JComboBox<>();

proveedor = new JComboBox<>();

cliente = new JComboBox<>();

pago = new JComboBox<>(
        new String[]{
                "EFECTIVO",
                "TARJETA",
                "YAPE",
                "PLIN",
                "TRANSFERENCIA"
        }
);

// --------------------------------------------------------
// CAMPOS
// --------------------------------------------------------

if (compra) {

    // FILA 1
    datos.add(new JLabel("Proveedor:"));
    datos.add(proveedor);

    datos.add(new JLabel("N° Compra:"));
    datos.add(comprobante);

    // FILA 2
    datos.add(new JLabel("Producto:"));
    datos.add(producto);

    datos.add(new JLabel("Cantidad:"));
    datos.add(cantidad);

    // FILA 3
    datos.add(new JLabel("Precio compra:"));
    datos.add(precio);

    datos.add(new JLabel(""));
    datos.add(new JLabel(""));

} else {

    // FILA 1
    datos.add(new JLabel("Cliente:"));
    datos.add(cliente);

    datos.add(new JLabel("N° Venta:"));
    datos.add(comprobante);

    // FILA 2
    datos.add(new JLabel("Producto:"));
    datos.add(producto);

    datos.add(new JLabel("Cantidad:"));
    datos.add(cantidad);

    // FILA 3
    datos.add(new JLabel("Precio venta:"));
    datos.add(precio);

    datos.add(new JLabel("Método de pago:"));
    datos.add(pago);
}
        // --------------------------------------------------------
        // PANEL SUPERIOR
        // --------------------------------------------------------

        JPanel superior = new JPanel(
                new BorderLayout(10, 10)
        );

        superior.add(
                datos,
                BorderLayout.CENTER
        );

        // --------------------------------------------------------
        // BOTONES
        // --------------------------------------------------------

        JButton agregar = new JButton(
                "AGREGAR PRODUCTO"
        );

        JButton quitar = new JButton(
                "QUITAR"
        );

        JButton registrar = new JButton(
                compra
                        ? "REGISTRAR COMPRA"
                        : "REGISTRAR VENTA"
        );

        JButton limpiar = new JButton(
                "LIMPIAR"
        );

        JPanel botones = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        8,
                        5
                )
        );

        botones.add(agregar);
        botones.add(quitar);
        botones.add(registrar);
        botones.add(limpiar);

        superior.add(
                botones,
                BorderLayout.SOUTH
        );

        // --------------------------------------------------------
        // TABLA
        // --------------------------------------------------------

        modelo = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Código",
                        "Producto",
                        "Cantidad",
                        "Precio",
                        "Subtotal"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        tabla.setRowHeight(25);

        tabla.getTableHeader().setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        JScrollPane scroll = new JScrollPane(
                tabla
        );

        // --------------------------------------------------------
        // TOTAL
        // --------------------------------------------------------

        total = new JLabel(
                "TOTAL: S/ 0.00"
        );

        total.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        total.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );

        JPanel inferior = new JPanel(
                new BorderLayout()
        );

        inferior.add(
                scroll,
                BorderLayout.CENTER
        );

        inferior.add(
                total,
                BorderLayout.SOUTH
        );

        // --------------------------------------------------------
        // AGREGAR TODO
        // --------------------------------------------------------

        principal.add(
                superior,
                BorderLayout.NORTH
        );

        principal.add(
                inferior,
                BorderLayout.CENTER
        );

        setContentPane(principal);

        // --------------------------------------------------------
        // EVENTOS
        // --------------------------------------------------------

        producto.addActionListener(
                e -> actualizarPrecio()
        );

        agregar.addActionListener(
                e -> agregarProducto()
        );

        quitar.addActionListener(
                e -> quitarProducto()
        );

        registrar.addActionListener(
                e -> registrarOperacion()
        );

        limpiar.addActionListener(
                e -> limpiar()
        );
    }

    // ============================================================
    // CARGAR DATOS
    // ============================================================

    private void cargar() {

        cargarProductos();

        if (compra) {

            cargarProveedores();

        } else {

            cargarClientes();
        }

        actualizarPrecio();
    }

    // ============================================================
    // PRODUCTOS
    // ============================================================

    private void cargarProductos() {

        producto.removeAllItems();

        List<Producto> productos =
                pdao.listar();

        for (Producto p : productos) {

            producto.addItem(p);
        }

        actualizarPrecio();
    }

    // ============================================================
    // PROVEEDORES
    // ============================================================

    private void cargarProveedores() {

        proveedor.removeAllItems();

        List<Proveedor> proveedores =
                prdao.listar();

        for (Proveedor p : proveedores) {

            proveedor.addItem(p);
        }
    }

    // ============================================================
    // CLIENTES
    // ============================================================

    private void cargarClientes() {

        cliente.removeAllItems();

        List<Cliente> clientes =
                cdao.listar();

        for (Cliente c : clientes) {

            cliente.addItem(c);
        }
    }

    // ============================================================
    // ACTUALIZAR PRECIO
    // ============================================================

    private void actualizarPrecio() {

        Producto p =
                (Producto) producto.getSelectedItem();

        if (p == null) {

            precio.setText("");

            return;
        }

        double valor;

        if (compra) {

            valor = p.getPrecioCompra();

        } else {

            valor = p.getPrecioVenta();
        }

        precio.setText(
                String.format(
                        Locale.US,
                        "%.2f",
                        valor
                )
        );
    }

    // ============================================================
    // AGREGAR PRODUCTO
    // ============================================================

    private void agregarProducto() {

        try {

            Producto p =
                    (Producto) producto.getSelectedItem();

            if (p == null) {

                throw new IllegalArgumentException(
                        "Seleccione un producto."
                );
            }

            // ----------------------------------------------------
            // CANTIDAD
            // ----------------------------------------------------

            String textoCantidad =
                    cantidad.getText().trim();

            if (textoCantidad.isEmpty()) {

                throw new IllegalArgumentException(
                        "Ingrese la cantidad."
                );
            }

            int q;

            try {

                q = Integer.parseInt(
                        textoCantidad
                );

            } catch (NumberFormatException e) {

                throw new IllegalArgumentException(
                        "La cantidad debe ser un número entero."
                );
            }

            if (q <= 0) {

                throw new IllegalArgumentException(
                        "La cantidad debe ser mayor que cero."
                );
            }

            // ----------------------------------------------------
            // PRECIO
            // ----------------------------------------------------

            String textoPrecio =
                    precio.getText().trim();

            if (textoPrecio.isEmpty()) {

                throw new IllegalArgumentException(
                        "Ingrese el precio."
                );
            }

            double pr;

            try {

                pr = Double.parseDouble(
                        textoPrecio.replace(",", ".")
                );

            } catch (NumberFormatException e) {

                throw new IllegalArgumentException(
                        "El precio ingresado no es válido."
                );
            }

            if (pr < 0) {

                throw new IllegalArgumentException(
                        "El precio no puede ser negativo."
                );
            }

            // ----------------------------------------------------
            // VALIDACIÓN DE STOCK PARA VENTA
            // ----------------------------------------------------

            if (!compra) {

                int cantidadYaAgregada =
                        obtenerCantidadProducto(
                                p.getIdProducto()
                        );

                int cantidadTotal =
                        cantidadYaAgregada + q;

                if (cantidadTotal > p.getStock()) {

                    throw new IllegalArgumentException(
                            "Stock insuficiente.\n\n"
                            + "Producto: "
                            + p.getNombre()
                            + "\nStock disponible: "
                            + p.getStock()
                            + "\nCantidad solicitada: "
                            + cantidadTotal
                    );
                }
            }

            // ----------------------------------------------------
            // SI EL PRODUCTO YA EXISTE
            // ----------------------------------------------------

            for (ItemOperacion item : items) {

                if (
                        item.getIdProducto()
                        == p.getIdProducto()
                ) {

                    item.setCantidad(
                            item.getCantidad() + q
                    );

                    item.setPrecio(pr);

                    refrescarTabla();

                    cantidad.setText("1");

                    return;
                }
            }

            // ----------------------------------------------------
            // NUEVO PRODUCTO
            // ----------------------------------------------------

            ItemOperacion item =
                    new ItemOperacion(
                            p,
                            q,
                            pr
                    );

            items.add(item);

            refrescarTabla();

            cantidad.setText("1");

        } catch (IllegalArgumentException e) {

            mostrarAdvertencia(
                    e.getMessage()
            );
        }
    }

    // ============================================================
    // OBTENER CANTIDAD YA AGREGADA
    // ============================================================

    private int obtenerCantidadProducto(
            int idProducto
    ) {

        int cantidadTotal = 0;

        for (ItemOperacion item : items) {

            if (
                    item.getIdProducto()
                    == idProducto
            ) {

                cantidadTotal +=
                        item.getCantidad();
            }
        }

        return cantidadTotal;
    }

    // ============================================================
    // QUITAR PRODUCTO
    // ============================================================

    private void quitarProducto() {

        int fila =
                tabla.getSelectedRow();

        if (fila < 0) {

            mostrarAdvertencia(
                    "Seleccione un producto de la tabla."
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea quitar el producto seleccionado?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                respuesta
                == JOptionPane.YES_OPTION
        ) {

            items.remove(fila);

            refrescarTabla();
        }
    }

    // ============================================================
    // REFRESCAR TABLA
    // ============================================================

    private void refrescarTabla() {

        modelo.setRowCount(0);

        double subtotalGeneral = 0;

        for (ItemOperacion item : items) {

            double subtotal =
                    item.getSubtotal();

            subtotalGeneral += subtotal;

            modelo.addRow(
                    new Object[]{
                            item.getIdProducto(),
                            item.getCodigo(),
                            item.getNombre(),
                            item.getCantidad(),
                            String.format(
                                    Locale.US,
                                    "%.2f",
                                    item.getPrecio()
                            ),
                            String.format(
                                    Locale.US,
                                    "%.2f",
                                    subtotal
                            )
                    }
            );
        }

        total.setText(
                String.format(
                        Locale.US,
                        "SUBTOTAL: S/ %.2f",
                        subtotalGeneral
                )
        );
    }

    // ============================================================
    // REGISTRAR OPERACIÓN
    // ============================================================

    private void registrarOperacion() {

        try {

            // ----------------------------------------------------
            // VALIDAR PRODUCTOS
            // ----------------------------------------------------

            if (items.isEmpty()) {

                throw new IllegalArgumentException(
                        "Debe agregar al menos un producto."
                );
            }

            // ----------------------------------------------------
            // VALIDAR USUARIO
            // ----------------------------------------------------

            if (usuario == null) {

                throw new IllegalArgumentException(
                        "No se encontró el usuario de la sesión."
                );
            }

            // ----------------------------------------------------
            // COMPRA
            // ----------------------------------------------------

            if (compra) {

                registrarCompra();

            } else {

                // ------------------------------------------------
                // VENTA
                // ------------------------------------------------

                registrarVenta();
            }

        } catch (SQLException e) {

            mostrarError(
                    "Error de base de datos:\n"
                    + obtenerMensajeSQLException(e)
            );

        } catch (IllegalArgumentException e) {

            mostrarAdvertencia(
                    e.getMessage()
            );

        } catch (Exception e) {

            mostrarError(
                    "Ocurrió un error inesperado:\n"
                    + e.getMessage()
            );
        }
    }

    // ============================================================
    // REGISTRAR COMPRA
    // ============================================================

    private void registrarCompra()
            throws SQLException {

        Proveedor p =
                (Proveedor) proveedor.getSelectedItem();

        if (p == null) {

            throw new IllegalArgumentException(
                    "Seleccione un proveedor."
            );
        }

        // --------------------------------------------------------
        // NÚMERO DE COMPRA
        // --------------------------------------------------------

        String numeroCompra =
                comprobante.getText().trim();

        if (numeroCompra.isEmpty()) {

            numeroCompra =
                    generarNumero("C");
        }

        // --------------------------------------------------------
        // CONFIRMACIÓN
        // --------------------------------------------------------

        double subtotal =
                calcularSubtotal();

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea registrar la compra?\n\n"
                        + "Proveedor: "
                        + p.getRazonSocial()
                        + "\nN° Compra: "
                        + numeroCompra
                        + "\nSubtotal: S/ "
                        + String.format(
                                Locale.US,
                                "%.2f",
                                subtotal
                        )
                        + "\nIGV: 18%"
                        + "\n\n"
                        + "La base de datos calculará "
                        + "el total automáticamente.",
                        "Confirmar compra",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                respuesta
                != JOptionPane.YES_OPTION
        ) {

            return;
        }

        // --------------------------------------------------------
        // REGISTRAR EN BD
        // --------------------------------------------------------

        int idCompra =
                dao.registrarCompra(
                        p.getIdProveedor(),
                        usuario.getIdUsuario(),
                        numeroCompra,
                        items
                );

        // --------------------------------------------------------
        // MENSAJE
        // --------------------------------------------------------

        JOptionPane.showMessageDialog(
                this,
                "Compra registrada correctamente.\n\n"
                + "ID de compra: "
                + idCompra
                + "\nN° Compra: "
                + numeroCompra,
                "Operación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        limpiar();
    }

    // ============================================================
    // REGISTRAR VENTA
    // ============================================================

    private void registrarVenta()
            throws SQLException {

        Cliente c =
                (Cliente) cliente.getSelectedItem();

        if (c == null) {

            throw new IllegalArgumentException(
                    "Seleccione un cliente."
            );
        }

        // --------------------------------------------------------
        // MÉTODO DE PAGO
        // --------------------------------------------------------

        String metodoPago =
                (String) pago.getSelectedItem();

        if (
                metodoPago == null
                || metodoPago.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Seleccione un método de pago."
            );
        }

        // --------------------------------------------------------
        // NÚMERO DE VENTA
        // --------------------------------------------------------

        String numeroVenta =
                comprobante.getText().trim();

        if (numeroVenta.isEmpty()) {

            numeroVenta =
                    generarNumero("V");
        }

        // --------------------------------------------------------
        // CONFIRMACIÓN
        // --------------------------------------------------------

        double subtotal =
                calcularSubtotal();

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea registrar la venta?\n\n"
                        + "Cliente: "
                        + c.getNombreCompleto()
                        + "\nN° Venta: "
                        + numeroVenta
                        + "\nMétodo de pago: "
                        + metodoPago
                        + "\nSubtotal: S/ "
                        + String.format(
                                Locale.US,
                                "%.2f",
                                subtotal
                        )
                        + "\nIGV: 18%"
                        + "\n\n"
                        + "La base de datos calculará "
                        + "el total automáticamente.",
                        "Confirmar venta",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                respuesta
                != JOptionPane.YES_OPTION
        ) {

            return;
        }

        // --------------------------------------------------------
        // REGISTRAR EN BD
        // --------------------------------------------------------

        int idVenta =
                dao.registrarVenta(
                        c.getIdCliente(),
                        usuario.getIdUsuario(),
                        numeroVenta,
                        metodoPago,
                        items
                );

        // --------------------------------------------------------
        // MENSAJE
        // --------------------------------------------------------

        JOptionPane.showMessageDialog(
                this,
                "Venta registrada correctamente.\n\n"
                + "ID de venta: "
                + idVenta
                + "\nN° Venta: "
                + numeroVenta,
                "Operación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        limpiar();
    }

    // ============================================================
    // CALCULAR SUBTOTAL SOLO PARA MOSTRAR CONFIRMACIÓN
    // ============================================================

    private double calcularSubtotal() {

        double subtotal = 0;

        for (ItemOperacion item : items) {

            subtotal +=
                    item.getSubtotal();
        }

        return subtotal;
    }

    // ============================================================
    // GENERAR NÚMERO
    // ============================================================

    private String generarNumero(
            String prefijo
    ) {

        return prefijo
                + "-"
                + System.currentTimeMillis();
    }

    // ============================================================
    // LIMPIAR FORMULARIO
    // ============================================================

    private void limpiar() {

        items.clear();

        modelo.setRowCount(0);

        comprobante.setText("");

        cantidad.setText("1");

        if (compra) {

            if (proveedor.getItemCount() > 0) {

                proveedor.setSelectedIndex(0);
            }

        } else {

            if (cliente.getItemCount() > 0) {

                cliente.setSelectedIndex(0);
            }

            if (pago.getItemCount() > 0) {

                pago.setSelectedIndex(0);
            }
        }

        cargarProductos();

        refrescarTabla();
    }

    // ============================================================
    // OBTENER MENSAJE SQL
    // ============================================================

    private String obtenerMensajeSQLException(
            SQLException e
    ) {

        StringBuilder mensaje =
                new StringBuilder();

        SQLException actual = e;

        while (actual != null) {

            if (
                    actual.getMessage() != null
                    && !actual.getMessage().isBlank()
            ) {

                if (mensaje.length() > 0) {

                    mensaje.append("\n");
                }

                mensaje.append(
                        actual.getMessage()
                );
            }

            actual =
                    actual.getNextException();
        }

        return mensaje.toString();
    }

    // ============================================================
    // MENSAJE DE ADVERTENCIA
    // ============================================================

    private void mostrarAdvertencia(
            String mensaje
    ) {

        if (
                mensaje == null
                || mensaje.isBlank()
        ) {

            mensaje =
                    "Revise los datos ingresados.";
        }

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // ============================================================
    // MENSAJE DE ERROR
    // ============================================================

    private void mostrarError(
            String mensaje
    ) {

        if (
                mensaje == null
                || mensaje.isBlank()
        ) {

            mensaje =
                    "Ocurrió un error inesperado.";
        }

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
