package com.inventarios.vista;

import com.inventarios.dao.CategoriaDAO;
import com.inventarios.dao.ProductoDAO;
import com.inventarios.dao.ProveedorDAO;
import com.inventarios.modelo.Categoria;
import com.inventarios.modelo.Producto;
import com.inventarios.modelo.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;

public class FrmProductos extends JFrame {

    private final ProductoDAO dao = new ProductoDAO();
    private final CategoriaDAO catDAO = new CategoriaDAO();
    private final ProveedorDAO provDAO = new ProveedorDAO();

    private JTextField codigo;
    private JTextField nombre;
    private JTextField descripcion;
    private JTextField compra;
    private JTextField venta;

    private JTextField stock;
    private JTextField minimo;
    private JTextField maximo;

    private JTextField buscar;

    private JComboBox<Categoria> categoria;
    private JComboBox<Proveedor> proveedor;
    private JComboBox<String> unidad;

    private JTable tabla;
    private DefaultTableModel modelo;

    private int id = 0;

    public FrmProductos() {

        setTitle("Gestión de Inventario - Productos");
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        construir();
        cargarCombos();
        cargarTabla("");

        limpiar();
    }

    // ============================================================
    // CONSTRUIR FORMULARIO
    // ============================================================

    private void construir() {

        JPanel root = new JPanel(
                new BorderLayout(10, 10)
        );

        root.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 12, 12, 12
                )
        );

        // --------------------------------------------------------
        // TÍTULO
        // --------------------------------------------------------

        JLabel titulo = new JLabel(
                "GESTIÓN DE INVENTARIO - PRODUCTOS",
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
                        5, 5, 10, 5
                )
        );

        root.add(
                titulo,
                BorderLayout.NORTH
        );

        // --------------------------------------------------------
        // CAMPOS
        // --------------------------------------------------------

        codigo = new JTextField();
        nombre = new JTextField();
        descripcion = new JTextField();

        compra = new JTextField();
        venta = new JTextField();

        stock = new JTextField("0");
        minimo = new JTextField("5");
        maximo = new JTextField("100");

        unidad = new JComboBox<>(
                new String[]{
                        "UNIDAD",
                        "CAJA",
                        "DOCENA",
                        "KG",
                        "LITRO",
                        "PAQUETE"
                }
        );

        categoria = new JComboBox<>();
        proveedor = new JComboBox<>();

        // --------------------------------------------------------
        // PANEL DE DATOS
        // --------------------------------------------------------

        JPanel formulario = new JPanel(
                new GridLayout(
                        6,
                        4,
                        8,
                        8
                )
        );

        // FILA 1
        formulario.add(
                new JLabel("Código:")
        );

        formulario.add(codigo);

        formulario.add(
                new JLabel("Nombre:")
        );

        formulario.add(nombre);

        // FILA 2
        formulario.add(
                new JLabel("Descripción:")
        );

        formulario.add(descripcion);

        formulario.add(
                new JLabel("Unidad:")
        );

        formulario.add(unidad);

        // FILA 3
        formulario.add(
                new JLabel("Categoría:")
        );

        formulario.add(categoria);

        formulario.add(
                new JLabel("Proveedor:")
        );

        formulario.add(proveedor);

        // FILA 4
        formulario.add(
                new JLabel("Precio compra:")
        );

        formulario.add(compra);

        formulario.add(
                new JLabel("Precio venta:")
        );

        formulario.add(venta);

        // FILA 5
        formulario.add(
                new JLabel("Stock actual:")
        );

        formulario.add(stock);

        formulario.add(
                new JLabel("Stock mínimo:")
        );

        formulario.add(minimo);

        // FILA 6
        formulario.add(
                new JLabel("Stock máximo:")
        );

        formulario.add(maximo);

        formulario.add(
                new JLabel("")
        );

        formulario.add(
                new JLabel("")
        );

        JPanel top = new JPanel(
                new BorderLayout(8, 8)
        );

        top.add(
                formulario,
                BorderLayout.CENTER
        );

        // --------------------------------------------------------
        // BOTONES
        // --------------------------------------------------------

        JButton nuevo = new JButton(
                "NUEVO"
        );

        JButton guardar = new JButton(
                "GUARDAR"
        );

        JButton actualizar = new JButton(
                "ACTUALIZAR"
        );

        JButton eliminar = new JButton(
                "ELIMINAR"
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

        botones.add(nuevo);
        botones.add(guardar);
        botones.add(actualizar);
        botones.add(eliminar);
        botones.add(limpiar);

        top.add(
                botones,
                BorderLayout.SOUTH
        );

        // --------------------------------------------------------
        // BÚSQUEDA
        // --------------------------------------------------------

        buscar = new JTextField();

        JButton btnBuscar = new JButton(
                "BUSCAR"
        );

        JPanel busqueda = new JPanel(
                new BorderLayout(8, 8)
        );

        busqueda.add(
                new JLabel("Código / nombre:"),
                BorderLayout.WEST
        );

        busqueda.add(
                buscar,
                BorderLayout.CENTER
        );

        busqueda.add(
                btnBuscar,
                BorderLayout.EAST
        );

        // --------------------------------------------------------
        // TABLA
        // --------------------------------------------------------

        modelo = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Código",
                        "Producto",
                        "Categoría",
                        "Proveedor",
                        "Compra",
                        "Venta",
                        "Stock",
                        "Mín.",
                        "Máx.",
                        "Estado"
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

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabla.setRowHeight(25);

        tabla.getTableHeader().setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        tabla.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        seleccionar();
                    }

                });

        // --------------------------------------------------------
        // CENTRO
        // --------------------------------------------------------

        JPanel center = new JPanel(
                new BorderLayout(8, 8)
        );

        center.add(
                busqueda,
                BorderLayout.NORTH
        );

        center.add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        // --------------------------------------------------------
        // CONTENIDO
        // --------------------------------------------------------

        JPanel contenido = new JPanel(
                new BorderLayout(10, 10)
        );

        contenido.add(
                top,
                BorderLayout.NORTH
        );

        contenido.add(
                center,
                BorderLayout.CENTER
        );

        root.add(
                contenido,
                BorderLayout.CENTER
        );

        setContentPane(root);

        // --------------------------------------------------------
        // EVENTOS
        // --------------------------------------------------------

        nuevo.addActionListener(
                e -> nuevoProducto()
        );

        guardar.addActionListener(
                e -> guardar()
        );

        actualizar.addActionListener(
                e -> actualizar()
        );

        eliminar.addActionListener(
                e -> eliminar()
        );

        limpiar.addActionListener(
                e -> limpiar()
        );

        btnBuscar.addActionListener(
                e -> cargarTabla(
                        buscar.getText()
                )
        );

        buscar.addActionListener(
                e -> cargarTabla(
                        buscar.getText()
                )
        );
    }

    // ============================================================
    // CARGAR COMBOS
    // ============================================================

    private void cargarCombos() {

        categoria.removeAllItems();

        for (Categoria c : catDAO.listar()) {

            categoria.addItem(c);
        }

        proveedor.removeAllItems();

        for (Proveedor p : provDAO.listar()) {

            proveedor.addItem(p);
        }
    }

    // ============================================================
    // CARGAR TABLA
    // ============================================================

    private void cargarTabla(
            String filtro
    ) {

        modelo.setRowCount(0);

        for (Producto p : dao.listar(filtro)) {

            String estado =
                    determinarEstadoStock(p);

            modelo.addRow(
                    new Object[]{
                            p.getIdProducto(),
                            p.getCodigo(),
                            p.getNombre(),
                            p.getNombreCategoria(),
                            p.getNombreProveedor(),
                            String.format(
                                    Locale.US,
                                    "%.2f",
                                    p.getPrecioCompra()
                            ),
                            String.format(
                                    Locale.US,
                                    "%.2f",
                                    p.getPrecioVenta()
                            ),
                            p.getStock(),
                            p.getStockMinimo(),
                            p.getStockMaximo(),
                            estado
                    }
            );
        }
    }

    // ============================================================
    // ESTADO DEL STOCK
    // ============================================================

    private String determinarEstadoStock(
            Producto p
    ) {

        if (p.getStock() <= 0) {

            return "STOCK CRÍTICO";
        }

        if (p.getStock() <= p.getStockMinimo()) {

            return "STOCK BAJO";
        }

        if (p.getStock() > p.getStockMaximo()) {

            return "SOBRESTOCK";
        }

        return "NORMAL";
    }

    // ============================================================
    // LEER DATOS
    // ============================================================

    private Producto leer() {

        String cod =
                codigo.getText().trim();

        String nom =
                nombre.getText().trim();

        String desc =
                descripcion.getText().trim();

        if (cod.isEmpty()) {

            throw new IllegalArgumentException(
                    "Ingrese el código del producto."
            );
        }

        if (nom.isEmpty()) {

            throw new IllegalArgumentException(
                    "Ingrese el nombre del producto."
            );
        }

        if (
                categoria.getSelectedItem()
                == null
        ) {

            throw new IllegalArgumentException(
                    "Seleccione una categoría."
            );
        }

        if (
                proveedor.getSelectedItem()
                == null
        ) {

            throw new IllegalArgumentException(
                    "Seleccione un proveedor."
            );
        }

        if (
                unidad.getSelectedItem()
                == null
        ) {

            throw new IllegalArgumentException(
                    "Seleccione una unidad de medida."
            );
        }

        double pc =
                convertirDouble(
                        compra.getText(),
                        "precio de compra"
                );

        double pv =
                convertirDouble(
                        venta.getText(),
                        "precio de venta"
                );

        int st =
                convertirEntero(
                        stock.getText(),
                        "stock"
                );

        int mi =
                convertirEntero(
                        minimo.getText(),
                        "stock mínimo"
                );

        int ma =
                convertirEntero(
                        maximo.getText(),
                        "stock máximo"
                );

        if (pc < 0) {

            throw new IllegalArgumentException(
                    "El precio de compra no puede ser negativo."
            );
        }

        if (pv < 0) {

            throw new IllegalArgumentException(
                    "El precio de venta no puede ser negativo."
            );
        }

        if (st < 0) {

            throw new IllegalArgumentException(
                    "El stock no puede ser negativo."
            );
        }

        if (mi < 0) {

            throw new IllegalArgumentException(
                    "El stock mínimo no puede ser negativo."
            );
        }

        if (ma < 0) {

            throw new IllegalArgumentException(
                    "El stock máximo no puede ser negativo."
            );
        }

        if (ma <= mi) {

            throw new IllegalArgumentException(
                    "El stock máximo debe ser mayor que el stock mínimo."
            );
        }

        Categoria c =
                (Categoria) categoria.getSelectedItem();

        Proveedor p =
                (Proveedor) proveedor.getSelectedItem();

        Producto x = new Producto();

        x.setIdProducto(id);

        x.setCodigo(cod);

        x.setNombre(nom);

        x.setDescripcion(desc);

        x.setIdCategoria(
                c.getIdCategoria()
        );

        x.setIdProveedor(
                p.getIdProveedor()
        );

        x.setUnidadMedida(
                String.valueOf(
                        unidad.getSelectedItem()
                )
        );

        x.setPrecioCompra(pc);

        x.setPrecioVenta(pv);

        x.setStock(st);

        x.setStockMinimo(mi);

        x.setStockMaximo(ma);

        return x;
    }

    // ============================================================
    // CONVERTIR DOUBLE
    // ============================================================

    private double convertirDouble(
            String texto,
            String campo
    ) {

        if (
                texto == null
                || texto.trim().isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "Ingrese el " + campo + "."
            );
        }

        try {

            return Double.parseDouble(
                    texto.trim().replace(",", ".")
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "El " + campo
                    + " debe ser numérico."
            );
        }
    }

    // ============================================================
    // CONVERTIR ENTERO
    // ============================================================

    private int convertirEntero(
            String texto,
            String campo
    ) {

        if (
                texto == null
                || texto.trim().isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "Ingrese el " + campo + "."
            );
        }

        try {

            return Integer.parseInt(
                    texto.trim()
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "El " + campo
                    + " debe ser un número entero."
            );
        }
    }

    // ============================================================
    // GUARDAR
    // ============================================================

    private void guardar() {

        if (id != 0) {

            error(
                    "Para registrar un nuevo producto presione NUEVO."
            );

            return;
        }

        try {

            Producto productoNuevo = leer();

            if (
                    dao.insertar(
                            productoNuevo
                    )
            ) {

                ok(
                        "Producto registrado correctamente."
                );

                cargarTabla(
                        buscar.getText()
                );

                limpiar();

            } else {

                error(
                        "No se pudo registrar el producto.\n"
                        + "Verifique que el código no esté registrado."
                );
            }

        } catch (Exception e) {

            error(
                    e.getMessage()
            );
        }
    }

    // ============================================================
    // ACTUALIZAR
    // ============================================================

    private void actualizar() {

        if (id == 0) {

            error(
                    "Seleccione un producto de la tabla."
            );

            return;
        }

        try {

            Producto productoActualizado =
                    leer();

            /*
             * IMPORTANTE:
             * ProductoDAO.actualizar() NO modifica el stock.
             *
             * El stock actual se controla mediante:
             * - Compras
             * - Ventas
             * - Ajustes de inventario
             */

            if (
                    dao.actualizar(
                            productoActualizado
                    )
            ) {

                ok(
                        "Producto actualizado correctamente."
                );

                cargarTabla(
                        buscar.getText()
                );

                limpiar();

            } else {

                error(
                        "No se pudo actualizar el producto."
                );
            }

        } catch (Exception e) {

            error(
                    e.getMessage()
            );
        }
    }

    // ============================================================
    // ELIMINAR
    // ============================================================

    private void eliminar() {

        if (id == 0) {

            error(
                    "Seleccione un producto."
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea desactivar el producto seleccionado?",
                        "Confirmar desactivación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (
                respuesta
                != JOptionPane.YES_OPTION
        ) {

            return;
        }

        if (dao.eliminar(id)) {

            ok(
                    "Producto desactivado correctamente."
            );

            cargarTabla(
                    buscar.getText()
            );

            limpiar();

        } else {

            error(
                    "No se pudo desactivar el producto."
            );
        }
    }

    // ============================================================
    // SELECCIONAR PRODUCTO
    // ============================================================

    private void seleccionar() {

        int fila =
                tabla.getSelectedRow();

        if (fila < 0) {

            return;
        }

        id =
                Integer.parseInt(
                        String.valueOf(
                                modelo.getValueAt(
                                        fila,
                                        0
                                )
                        )
                );

        codigo.setText(
                String.valueOf(
                        modelo.getValueAt(
                                fila,
                                1
                        )
                )
        );

        nombre.setText(
                String.valueOf(
                        modelo.getValueAt(
                                fila,
                                2
                        )
                )
        );

        compra.setText(
                String.valueOf(
                        modelo.getValueAt(
                                fila,
                                5
                        )
                )
        );

        venta.setText(
                String.valueOf(
                        modelo.getValueAt(
                                fila,
                                6
                        )
                )
        );

        stock.setText(
                String.valueOf(
                        modelo.getValueAt(
                                fila,
                                7
                        )
                )
        );

        minimo.setText(
                String.valueOf(
                        modelo.getValueAt(
                                fila,
                                8
                        )
                )
        );

        maximo.setText(
                String.valueOf(
                        modelo.getValueAt(
                                fila,
                                9
                        )
                )
        );

        String nombreCategoria =
                String.valueOf(
                        modelo.getValueAt(
                                fila,
                                3
                        )
                );

        String nombreProveedor =
                String.valueOf(
                        modelo.getValueAt(
                                fila,
                                4
                        )
                );

        // --------------------------------------------------------
        // SELECCIONAR CATEGORÍA
        // --------------------------------------------------------

        for (
                int i = 0;
                i < categoria.getItemCount();
                i++
        ) {

            Categoria c =
                    categoria.getItemAt(i);

            if (
                    c != null
                    && c.getNombre()
                    .equalsIgnoreCase(
                            nombreCategoria
                    )
            ) {

                categoria.setSelectedIndex(i);

                break;
            }
        }

        // --------------------------------------------------------
        // SELECCIONAR PROVEEDOR
        // --------------------------------------------------------

        for (
                int i = 0;
                i < proveedor.getItemCount();
                i++
        ) {

            Proveedor p =
                    proveedor.getItemAt(i);

            if (
                    p != null
                    && p.getRazonSocial()
                    .equalsIgnoreCase(
                            nombreProveedor
                    )
            ) {

                proveedor.setSelectedIndex(i);

                break;
            }
        }

        /*
         * El stock actual queda bloqueado al editar.
         */
        stock.setEditable(false);

        stock.setToolTipText(
                "El stock se modifica mediante compras, ventas o ajustes."
        );
    }

    // ============================================================
    // NUEVO PRODUCTO
    // ============================================================

    private void nuevoProducto() {

        id = 0;

        codigo.setText("");
        nombre.setText("");
        descripcion.setText("");

        compra.setText("");
        venta.setText("");

        stock.setText("0");
        minimo.setText("5");
        maximo.setText("100");

        stock.setEditable(true);

        stock.setToolTipText(
                "Stock inicial del nuevo producto."
        );

        if (
                categoria.getItemCount()
                > 0
        ) {

            categoria.setSelectedIndex(0);
        }

        if (
                proveedor.getItemCount()
                > 0
        ) {

            proveedor.setSelectedIndex(0);
        }

        if (
                unidad.getItemCount()
                > 0
        ) {

            unidad.setSelectedIndex(0);
        }

        tabla.clearSelection();

        codigo.requestFocus();
    }

    // ============================================================
    // LIMPIAR
    // ============================================================

    private void limpiar() {

        id = 0;

        codigo.setText("");
        nombre.setText("");
        descripcion.setText("");

        compra.setText("");
        venta.setText("");

        stock.setText("0");
        minimo.setText("5");
        maximo.setText("100");

        stock.setEditable(true);

        stock.setToolTipText(
                "Stock inicial del nuevo producto."
        );

        if (
                categoria.getItemCount()
                > 0
        ) {

            categoria.setSelectedIndex(0);
        }

        if (
                proveedor.getItemCount()
                > 0
        ) {

            proveedor.setSelectedIndex(0);
        }

        if (
                unidad.getItemCount()
                > 0
        ) {

            unidad.setSelectedIndex(0);
        }

        tabla.clearSelection();

        codigo.requestFocus();
    }

    // ============================================================
    // MENSAJE OK
    // ============================================================

    private void ok(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Sistema de Inventarios",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ============================================================
    // MENSAJE ERROR
    // ============================================================

    private void error(String mensaje) {

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
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // ============================================================
    // MAIN DE PRUEBA
    // ============================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    try {

                        UIManager.setLookAndFeel(
                                UIManager
                                        .getSystemLookAndFeelClassName()
                        );

                    } catch (Exception ignored) {
                    }

                    new FrmProductos()
                            .setVisible(true);
                }
        );
    }
}