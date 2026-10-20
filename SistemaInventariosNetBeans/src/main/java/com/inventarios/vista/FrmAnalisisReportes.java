package com.inventarios.vista;

import com.inventarios.dao.ReporteDAO;
import com.inventarios.util.ExportadorReporte;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

public class FrmAnalisisReportes extends JFrame {

    private final ReporteDAO reporteDAO;

    private JLabel lblProductos;
    private JLabel lblStockBajo;
    private JLabel lblAgotados;
    private JLabel lblSobrestock;
    private JLabel lblUnidades;
    private JLabel lblValorInventario;
    private JLabel lblVentas;
    private JLabel lblCompras;
    private JLabel lblCantidadVentas;
    private JLabel lblCantidadCompras;

    private JTable tablaInventario;
    private JTable tablaVentas;
    private JTable tablaCompras;
    private JTable tablaMasVendidos;
    private JTable tablaKardex;

    private JTextField txtBuscarInventario;

    private JTextField txtFechaDesdeVentas;
    private JTextField txtFechaHastaVentas;

    private JTextField txtFechaDesdeCompras;
    private JTextField txtFechaHastaCompras;

    private JTextField txtFechaDesdeKardex;
    private JTextField txtFechaHastaKardex;

    private DefaultTableModel modeloInventario;
    private DefaultTableModel modeloVentas;
    private DefaultTableModel modeloCompras;
    private DefaultTableModel modeloMasVendidos;
    private DefaultTableModel modeloKardex;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public FrmAnalisisReportes() {

        reporteDAO = new ReporteDAO();

        configurarVentana();
        inicializarComponentes();

        cargarDashboard();
        cargarInventario();
        cargarVentas();
        cargarCompras();
        cargarMasVendidos();
        cargarKardex();
    }

    // =========================================================
    // VENTANA
    // =========================================================

    private void configurarVentana() {

        setTitle("Análisis y Reportes");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );
    }

    // =========================================================
    // COMPONENTES
    // =========================================================

    private void inicializarComponentes() {

        JPanel principal =
                new JPanel(new BorderLayout(10, 10));

        principal.setBorder(
                new EmptyBorder(15, 15, 15, 15)
        );

        JLabel titulo =
                new JLabel(
                        "ANÁLISIS Y REPORTES",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        principal.add(
                titulo,
                BorderLayout.NORTH
        );

        JTabbedPane pestañas =
                new JTabbedPane();

        pestañas.addTab(
                "Dashboard",
                crearDashboard()
        );

        pestañas.addTab(
                "Inventario",
                crearPanelInventario()
        );

        pestañas.addTab(
                "Ventas",
                crearPanelVentas()
        );

        pestañas.addTab(
                "Compras",
                crearPanelCompras()
        );

        pestañas.addTab(
                "Más vendidos",
                crearPanelMasVendidos()
        );

        pestañas.addTab(
                "Kardex",
                crearPanelKardex()
        );

        principal.add(
                pestañas,
                BorderLayout.CENTER
        );

        JButton btnCerrar =
                new JButton("CERRAR");

        btnCerrar.addActionListener(
                e -> dispose()
        );

        JPanel inferior =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        inferior.add(btnCerrar);

        principal.add(
                inferior,
                BorderLayout.SOUTH
        );

        setContentPane(principal);
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private JPanel crearDashboard() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                2,
                                5,
                                15,
                                15
                        )
                );

        panel.setBorder(
                new EmptyBorder(
                        25,
                        25,
                        25,
                        25
                )
        );

        lblProductos =
                crearIndicador(
                        "PRODUCTOS",
                        "0"
                );

        lblStockBajo =
                crearIndicador(
                        "STOCK BAJO",
                        "0"
                );

        lblAgotados =
                crearIndicador(
                        "AGOTADOS",
                        "0"
                );

        lblSobrestock =
                crearIndicador(
                        "SOBRESTOCK",
                        "0"
                );

        lblUnidades =
                crearIndicador(
                        "UNIDADES EN STOCK",
                        "0"
                );

        lblValorInventario =
                crearIndicador(
                        "VALOR INVENTARIO",
                        "S/ 0.00"
                );

        lblVentas =
                crearIndicador(
                        "VENTAS",
                        "S/ 0.00"
                );

        lblCompras =
                crearIndicador(
                        "COMPRAS",
                        "S/ 0.00"
                );

        lblCantidadVentas =
                crearIndicador(
                        "N.º VENTAS",
                        "0"
                );

        lblCantidadCompras =
                crearIndicador(
                        "N.º COMPRAS",
                        "0"
                );

        panel.add(
                crearPanelIndicador(lblProductos)
        );

        panel.add(
                crearPanelIndicador(lblStockBajo)
        );

        panel.add(
                crearPanelIndicador(lblAgotados)
        );

        panel.add(
                crearPanelIndicador(lblSobrestock)
        );

        panel.add(
                crearPanelIndicador(lblUnidades)
        );

        panel.add(
                crearPanelIndicador(lblValorInventario)
        );

        panel.add(
                crearPanelIndicador(lblVentas)
        );

        panel.add(
                crearPanelIndicador(lblCompras)
        );

        panel.add(
                crearPanelIndicador(lblCantidadVentas)
        );

        panel.add(
                crearPanelIndicador(lblCantidadCompras)
        );

        JButton btnActualizar =
                new JButton(
                        "ACTUALIZAR DASHBOARD"
                );

        btnActualizar.addActionListener(
                e -> cargarDashboard()
        );

        JPanel contenedor =
                new JPanel(
                        new BorderLayout()
                );

        contenedor.add(
                panel,
                BorderLayout.CENTER
        );

        contenedor.add(
                btnActualizar,
                BorderLayout.SOUTH
        );

        return contenedor;
    }

    // =========================================================
    // INVENTARIO
    // =========================================================

    private JPanel crearPanelInventario() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JPanel superior =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        superior.add(
                new JLabel("Buscar:")
        );

        txtBuscarInventario =
                new JTextField(25);

        JButton btnBuscar =
                new JButton("BUSCAR");

        JButton btnActualizar =
                new JButton("ACTUALIZAR");
        
        JButton btnExcel =
                new JButton("EXPORTAR EXCEL");

        JButton btnPDF =
                new JButton("EXPORTAR PDF");

        superior.add(
                txtBuscarInventario
        );

        superior.add(btnBuscar);
        superior.add(btnActualizar);
        
        superior.add(btnExcel);
        superior.add(btnPDF);

        panel.add(
                superior,
                BorderLayout.NORTH
        );

        modeloInventario =
                new DefaultTableModel(
                        new Object[]{
                                "Código",
                                "Producto",
                                "Categoría",
                                "Proveedor",
                                "Stock",
                                "Mínimo",
                                "Máximo",
                                "P. Compra",
                                "P. Venta"
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

        tablaInventario =
                new JTable(modeloInventario);

        tablaInventario.setAutoCreateRowSorter(
                true
        );

        panel.add(
                new JScrollPane(tablaInventario),
                BorderLayout.CENTER
        );

        btnBuscar.addActionListener(
                e -> cargarInventario()
        );

        btnActualizar.addActionListener(
                e -> cargarInventario()
        );
        
        btnExcel.addActionListener(
                e -> ExportadorReporte.exportarExcel(
                        tablaInventario,
                        "Reporte de Inventario"
                )
        );

        btnPDF.addActionListener(
                e -> ExportadorReporte.exportarPDF(
                        tablaInventario,
                        "Reporte de Inventario"
                )
        );

        return panel;
    }

    // =========================================================
    // VENTAS
    // =========================================================

    private JPanel crearPanelVentas() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JPanel filtros =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        filtros.add(
                new JLabel("Fecha desde:")
        );

        txtFechaDesdeVentas =
                new JTextField(10);

        filtros.add(
                txtFechaDesdeVentas
        );

        filtros.add(
                new JLabel("Fecha hasta:")
        );

        txtFechaHastaVentas =
                new JTextField(10);

        filtros.add(
                txtFechaHastaVentas
        );

        JButton btnBuscar =
                new JButton("BUSCAR");

        JButton btnMostrarTodo =
                new JButton("MOSTRAR TODO");

        JButton btnExcel =
        new JButton("EXPORTAR EXCEL");

        JButton btnPDF =
                new JButton("EXPORTAR PDF");
        
        
        filtros.add(btnBuscar);
        filtros.add(btnMostrarTodo);
        
        filtros.add(btnExcel);
        filtros.add(btnPDF);

        panel.add(
                filtros,
                BorderLayout.NORTH
        );

        modeloVentas =
                new DefaultTableModel(
                        new Object[]{
                                "N.º Venta",
                                "Fecha",
                                "Cliente",
                                "Usuario",
                                "Método pago",
                                "Subtotal",
                                "Impuesto",
                                "Total",
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

        tablaVentas =
                new JTable(modeloVentas);

        tablaVentas.setAutoCreateRowSorter(
                true
        );

        panel.add(
                new JScrollPane(tablaVentas),
                BorderLayout.CENTER
        );

        btnBuscar.addActionListener(
                e -> buscarVentasPorFecha()
        );

        btnMostrarTodo.addActionListener(
                e -> {

                    limpiarFechasVentas();
                    cargarVentas();
                }
        );
        
        btnExcel.addActionListener(
                e -> ExportadorReporte.exportarExcel(
                        tablaVentas,
                        "Reporte de Ventas"
                    )
        );

        btnPDF.addActionListener(
                e -> ExportadorReporte.exportarPDF(
                        tablaVentas,
                        "Reporte de Ventas"
                )
        );

        return panel;
    }

    // =========================================================
    // COMPRAS
    // =========================================================

    private JPanel crearPanelCompras() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JPanel filtros =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        filtros.add(
                new JLabel("Fecha desde:")
        );

        txtFechaDesdeCompras =
                new JTextField(10);

        filtros.add(
                txtFechaDesdeCompras
        );

        filtros.add(
                new JLabel("Fecha hasta:")
        );

        txtFechaHastaCompras =
                new JTextField(10);

        filtros.add(
                txtFechaHastaCompras
        );

        JButton btnBuscar =
                new JButton("BUSCAR");

        JButton btnMostrarTodo =
                new JButton("MOSTRAR TODO");
        
        JButton btnExcel =
               new JButton("EXPORTAR EXCEL");

        JButton btnPDF =
                new JButton("EXPORTAR PDF");       

        filtros.add(btnBuscar);
        filtros.add(btnMostrarTodo);
        
        filtros.add(btnExcel);
        filtros.add(btnPDF);

        panel.add(
                filtros,
                BorderLayout.NORTH
        );

        modeloCompras =
                new DefaultTableModel(
                        new Object[]{
                                "N.º Compra",
                                "Fecha",
                                "Proveedor",
                                "Usuario",
                                "Subtotal",
                                "Impuesto",
                                "Total",
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

        tablaCompras =
                new JTable(modeloCompras);

        tablaCompras.setAutoCreateRowSorter(
                true
        );

        panel.add(
                new JScrollPane(tablaCompras),
                BorderLayout.CENTER
        );

        btnBuscar.addActionListener(
                e -> buscarComprasPorFecha()
        );

        btnMostrarTodo.addActionListener(
                e -> {

                    limpiarFechasCompras();
                    cargarCompras();
                }
        );
        
        btnExcel.addActionListener(
                e -> ExportadorReporte.exportarExcel(
                        tablaCompras,
                        "Reporte de Compras"
                )
        );

        btnPDF.addActionListener(
                e -> ExportadorReporte.exportarPDF(
                        tablaCompras,
                        "Reporte de Compras"
                )
        );

        return panel;
    }

    // =========================================================
    // MÁS VENDIDOS
    // =========================================================

    private JPanel crearPanelMasVendidos() {

    JPanel panel = new JPanel(
            new BorderLayout()
    );

    modeloMasVendidos = new DefaultTableModel(
            new Object[]{
                "Código",
                "Producto",
                "Cantidad vendida",
                "Importe vendido"
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

    tablaMasVendidos = new JTable(modeloMasVendidos);

    tablaMasVendidos.setAutoCreateRowSorter(true);

    panel.add(
            new JScrollPane(tablaMasVendidos),
            BorderLayout.CENTER
    );

    // Panel de botones
    JPanel panelBotones = new JPanel();

    JButton btnActualizar = new JButton("ACTUALIZAR");
    JButton btnExcel = new JButton("EXPORTAR EXCEL");
    JButton btnPDF = new JButton("EXPORTAR PDF");

    // Actualizar
    btnActualizar.addActionListener(
            e -> cargarMasVendidos()
    );

    // Exportar Excel
    btnExcel.addActionListener(
            e -> ExportadorReporte.exportarExcel(
                    tablaMasVendidos,
                    "Productos Más Vendidos"
            )
    );

    // Exportar PDF
    btnPDF.addActionListener(
            e -> ExportadorReporte.exportarPDF(
                    tablaMasVendidos,
                    "Productos Más Vendidos"
            )
    );

    // Agregar botones al panel
    panelBotones.add(btnActualizar);
    panelBotones.add(btnExcel);
    panelBotones.add(btnPDF);

    panel.add(
            panelBotones,
            BorderLayout.SOUTH
    );

    return panel;
}
    // =========================================================
    // KARDEX
    // =========================================================

    private JPanel crearPanelKardex() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JPanel filtros =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        filtros.add(
                new JLabel("Fecha desde:")
        );

        txtFechaDesdeKardex =
                new JTextField(10);

        filtros.add(
                txtFechaDesdeKardex
        );

        filtros.add(
                new JLabel("Fecha hasta:")
        );

        txtFechaHastaKardex =
                new JTextField(10);

        filtros.add(
                txtFechaHastaKardex
        );

        JButton btnBuscar =
                new JButton("BUSCAR");

        JButton btnMostrarTodo =
                new JButton("MOSTRAR TODO");
        
        JButton btnExcel =
                new JButton("EXPORTAR EXCEL");

        JButton btnPDF =
                new JButton("EXPORTAR PDF");        

        filtros.add(btnBuscar);
        filtros.add(btnMostrarTodo);
        
        filtros.add(btnExcel);
        filtros.add(btnPDF);

        panel.add(
                filtros,
                BorderLayout.NORTH
        );

        modeloKardex =
                new DefaultTableModel(
                        new Object[]{
                                "Fecha",
                                "Código",
                                "Producto",
                                "Movimiento",
                                "Origen",
                                "Referencia",
                                "Cantidad",
                                "Stock anterior",
                                "Stock nuevo",
                                "Usuario",
                                "Observación"
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

        tablaKardex =
                new JTable(modeloKardex);

        tablaKardex.setAutoCreateRowSorter(
                true
        );

        panel.add(
                new JScrollPane(tablaKardex),
                BorderLayout.CENTER
        );

        btnBuscar.addActionListener(
                e -> buscarKardexPorFecha()
        );

        btnMostrarTodo.addActionListener(
                e -> {

                    limpiarFechasKardex();
                    cargarKardex();
                }
        );
        
        btnExcel.addActionListener(
                e -> ExportadorReporte.exportarExcel(
                        tablaKardex,
                        "Reporte Kardex"
                )
        );

        btnPDF.addActionListener(
                e -> ExportadorReporte.exportarPDF(
                        tablaKardex,
                        "Reporte Kardex"
                )
        );

        return panel;
    }

    // =========================================================
    // BÚSQUEDA DE VENTAS POR FECHA
    // =========================================================

    private void buscarVentasPorFecha() {

        String desde =
                txtFechaDesdeVentas.getText().trim();

        String hasta =
                txtFechaHastaVentas.getText().trim();

        if (!validarRangoFechas(
                desde,
                hasta
        )) {
            return;
        }

        modeloVentas.setRowCount(0);

        List<Object[]> datos =
                reporteDAO.reporteVentasPorFechas(
                        desde,
                        hasta
                );

        for (Object[] fila : datos) {
            modeloVentas.addRow(fila);
        }

        JOptionPane.showMessageDialog(
                this,
                "Se encontraron "
                        + datos.size()
                        + " registros de ventas.",
                "Resultado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // BÚSQUEDA DE COMPRAS POR FECHA
    // =========================================================

    private void buscarComprasPorFecha() {

        String desde =
                txtFechaDesdeCompras.getText().trim();

        String hasta =
                txtFechaHastaCompras.getText().trim();

        if (!validarRangoFechas(
                desde,
                hasta
        )) {
            return;
        }

        modeloCompras.setRowCount(0);

        List<Object[]> datos =
                reporteDAO.reporteComprasPorFechas(
                        desde,
                        hasta
                );

        for (Object[] fila : datos) {
            modeloCompras.addRow(fila);
        }

        JOptionPane.showMessageDialog(
                this,
                "Se encontraron "
                        + datos.size()
                        + " registros de compras.",
                "Resultado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // BÚSQUEDA DE KARDEX POR FECHA
    // =========================================================

    private void buscarKardexPorFecha() {

        String desde =
                txtFechaDesdeKardex.getText().trim();

        String hasta =
                txtFechaHastaKardex.getText().trim();

        if (!validarRangoFechas(
                desde,
                hasta
        )) {
            return;
        }

        modeloKardex.setRowCount(0);

        List<Object[]> datos =
                reporteDAO.reporteKardexPorFechas(
                        desde,
                        hasta
                );

        for (Object[] fila : datos) {
            modeloKardex.addRow(fila);
        }

        JOptionPane.showMessageDialog(
                this,
                "Se encontraron "
                        + datos.size()
                        + " movimientos en el Kardex.",
                "Resultado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // VALIDAR FECHAS
    // =========================================================

    private boolean validarRangoFechas(
            String desde,
            String hasta
    ) {

        if (desde.isEmpty() || hasta.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar la fecha desde y la fecha hasta.",
                    "Fechas requeridas",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        try {

            LocalDate fechaDesde =
                    LocalDate.parse(
                            desde,
                            formatoFecha
                    );

            LocalDate fechaHasta =
                    LocalDate.parse(
                            hasta,
                            formatoFecha
                    );

            if (fechaDesde.isAfter(fechaHasta)) {

                JOptionPane.showMessageDialog(
                        this,
                        "La fecha desde no puede ser posterior a la fecha hasta.",
                        "Rango incorrecto",
                        JOptionPane.WARNING_MESSAGE
                );

                return false;
            }

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Formato de fecha incorrecto.\n\n"
                            + "Utilice el formato:\n"
                            + "AAAA-MM-DD\n\n"
                            + "Ejemplo: 2026-10-07",
                    "Fecha incorrecta",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    // =========================================================
    // LIMPIAR FECHAS
    // =========================================================

    private void limpiarFechasVentas() {

        if (txtFechaDesdeVentas != null) {
            txtFechaDesdeVentas.setText("");
        }

        if (txtFechaHastaVentas != null) {
            txtFechaHastaVentas.setText("");
        }
    }

    private void limpiarFechasCompras() {

        if (txtFechaDesdeCompras != null) {
            txtFechaDesdeCompras.setText("");
        }

        if (txtFechaHastaCompras != null) {
            txtFechaHastaCompras.setText("");
        }
    }

    private void limpiarFechasKardex() {

        if (txtFechaDesdeKardex != null) {
            txtFechaDesdeKardex.setText("");
        }

        if (txtFechaHastaKardex != null) {
            txtFechaHastaKardex.setText("");
        }
    }

    // =========================================================
    // CARGAR DASHBOARD
    // =========================================================

    private void cargarDashboard() {

        lblProductos.setText(
                indicador(
                        "PRODUCTOS",
                        String.valueOf(
                                reporteDAO.totalProductos()
                        )
                )
        );

        lblStockBajo.setText(
                indicador(
                        "STOCK BAJO",
                        String.valueOf(
                                reporteDAO.productosStockBajo()
                        )
                )
        );

        lblAgotados.setText(
                indicador(
                        "AGOTADOS",
                        String.valueOf(
                                reporteDAO.productosAgotados()
                        )
                )
        );

        lblSobrestock.setText(
                indicador(
                        "SOBRESTOCK",
                        String.valueOf(
                                reporteDAO.productosSobrestock()
                        )
                )
        );

        lblUnidades.setText(
                indicador(
                        "UNIDADES EN STOCK",
                        String.valueOf(
                                reporteDAO.unidadesStock()
                        )
                )
        );

        lblValorInventario.setText(
                indicador(
                        "VALOR INVENTARIO",
                        moneda(
                                reporteDAO.valorInventario()
                        )
                )
        );

        lblVentas.setText(
                indicador(
                        "VENTAS",
                        moneda(
                                reporteDAO.totalVentas()
                        )
                )
        );

        lblCompras.setText(
                indicador(
                        "COMPRAS",
                        moneda(
                                reporteDAO.totalCompras()
                        )
                )
        );

        lblCantidadVentas.setText(
                indicador(
                        "N.º VENTAS",
                        String.valueOf(
                                reporteDAO.cantidadVentas()
                        )
                )
        );

        lblCantidadCompras.setText(
                indicador(
                        "N.º COMPRAS",
                        String.valueOf(
                                reporteDAO.cantidadCompras()
                        )
                )
        );
    }

    // =========================================================
    // CARGAR INVENTARIO
    // =========================================================

    private void cargarInventario() {

        if (modeloInventario == null) {
            return;
        }

        modeloInventario.setRowCount(0);

        String filtro =
                txtBuscarInventario == null
                        ? ""
                        : txtBuscarInventario.getText();

        List<Object[]> datos =
                reporteDAO.reporteInventario(
                        filtro
                );

        for (Object[] fila : datos) {
            modeloInventario.addRow(fila);
        }
    }

    // =========================================================
    // CARGAR VENTAS
    // =========================================================

    private void cargarVentas() {

        if (modeloVentas == null) {
            return;
        }

        modeloVentas.setRowCount(0);

        List<Object[]> datos =
                reporteDAO.reporteVentas();

        for (Object[] fila : datos) {
            modeloVentas.addRow(fila);
        }
    }

    // =========================================================
    // CARGAR COMPRAS
    // =========================================================

    private void cargarCompras() {

        if (modeloCompras == null) {
            return;
        }

        modeloCompras.setRowCount(0);

        List<Object[]> datos =
                reporteDAO.reporteCompras();

        for (Object[] fila : datos) {
            modeloCompras.addRow(fila);
        }
    }

    // =========================================================
    // CARGAR MÁS VENDIDOS
    // =========================================================

    private void cargarMasVendidos() {

        if (modeloMasVendidos == null) {
            return;
        }

        modeloMasVendidos.setRowCount(0);

        List<Object[]> datos =
                reporteDAO.productosMasVendidos();

        for (Object[] fila : datos) {
            modeloMasVendidos.addRow(fila);
        }
    }

    // =========================================================
    // CARGAR KARDEX
    // =========================================================

    private void cargarKardex() {

        if (modeloKardex == null) {
            return;
        }

        modeloKardex.setRowCount(0);

        List<Object[]> datos =
                reporteDAO.reporteKardex();

        for (Object[] fila : datos) {
            modeloKardex.addRow(fila);
        }
    }

    // =========================================================
    // INDICADOR
    // =========================================================

    private JLabel crearIndicador(
            String titulo,
            String valor
    ) {

        JLabel label =
                new JLabel(
                        indicador(
                                titulo,
                                valor
                        ),
                        SwingConstants.CENTER
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        return label;
    }

    private String indicador(
            String titulo,
            String valor
    ) {

        return "<html><center>"
                + "<b>"
                + titulo
                + "</b><br><br>"
                + "<font size='5'>"
                + valor
                + "</font>"
                + "</center></html>";
    }

    // =========================================================
    // PANEL INDICADOR
    // =========================================================

    private JPanel crearPanelIndicador(
            JLabel label
    ) {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        BorderFactory.createEmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        panel.add(
                label,
                BorderLayout.CENTER
        );

        return panel;
    }

    // =========================================================
    // MONEDA
    // =========================================================

    private String moneda(double valor) {

        NumberFormat formato =
                NumberFormat.getCurrencyInstance(
                        new Locale(
                                "es",
                                "PE"
                        )
                );

        return formato.format(valor);
    }
}