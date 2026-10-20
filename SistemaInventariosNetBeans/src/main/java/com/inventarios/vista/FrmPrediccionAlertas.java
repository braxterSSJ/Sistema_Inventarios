package com.inventarios.vista;

import com.inventarios.dao.AlertaDAO;
import com.inventarios.dao.PrediccionDAO;
import com.inventarios.modelo.Alerta;
import com.inventarios.modelo.Prediccion;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class FrmPrediccionAlertas extends JFrame {

    private PrediccionDAO prediccionDAO;
    private AlertaDAO alertaDAO;

    private JTable tablaPredicciones;
    private JTable tablaAlertas;

    private DefaultTableModel modeloPredicciones;
    private DefaultTableModel modeloAlertas;

    private JButton btnGenerarPredicciones;
    private JButton btnActualizarPredicciones;

    private JButton btnGenerarAlertas;
    private JButton btnActualizarAlertas;
    private JButton btnAtenderAlerta;

    private final DecimalFormat formatoDecimal =
            new DecimalFormat("0.00");

    private final SimpleDateFormat formatoFecha =
            new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public FrmPrediccionAlertas() {

        prediccionDAO = new PrediccionDAO();
        alertaDAO = new AlertaDAO();

        configurarVentana();
        inicializarComponentes();

        cargarPredicciones();
        cargarAlertas();
    }

    private void configurarVentana() {

        setTitle("PREDICCIÓN Y ALERTAS");

        setSize(1200, 750);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );
    }

    private void inicializarComponentes() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout());

        JLabel titulo =
                new JLabel(
                        "PREDICCIÓN Y ALERTAS DE INVENTARIO",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        titulo.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        10,
                        15,
                        10
                )
        );

        panelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        JTabbedPane pestañas =
                new JTabbedPane();

        pestañas.addTab(
                "PREDICCIÓN DE DEMANDA",
                crearPanelPredicciones()
        );

        pestañas.addTab(
                "ALERTAS DE INVENTARIO",
                crearPanelAlertas()
        );

        panelPrincipal.add(
                pestañas,
                BorderLayout.CENTER
        );

        JButton btnCerrar =
                new JButton("CERRAR");

        btnCerrar.addActionListener(
                e -> dispose()
        );

        JPanel panelCerrar =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        panelCerrar.add(btnCerrar);

        panelPrincipal.add(
                panelCerrar,
                BorderLayout.SOUTH
        );

        add(panelPrincipal);
    }

    // =========================================================
    // PREDICCIONES
    // =========================================================

    private JPanel crearPanelPredicciones() {

        JPanel panel =
                new JPanel(new BorderLayout());

        modeloPredicciones =
                new DefaultTableModel(
                        new Object[]{
                                "Código",
                                "Producto",
                                "Periodo",
                                "Ventas históricas",
                                "Promedio diario",
                                "Demanda estimada",
                                "Stock actual",
                                "Días cobertura",
                                "Recomendación"
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

        tablaPredicciones =
                new JTable(modeloPredicciones);

        tablaPredicciones.setAutoCreateRowSorter(
                true
        );

        tablaPredicciones.setRowHeight(25);

        tablaPredicciones.getColumnModel()
                .getColumn(8)
                .setPreferredWidth(350);

        JScrollPane scroll =
                new JScrollPane(
                        tablaPredicciones
                );

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        btnGenerarPredicciones =
                new JButton(
                        "GENERAR PREDICCIONES"
                );

        btnActualizarPredicciones =
                new JButton(
                        "ACTUALIZAR"
                );

        btnGenerarPredicciones.addActionListener(
                e -> generarPredicciones()
        );

        btnActualizarPredicciones.addActionListener(
                e -> cargarPredicciones()
        );

        JPanel panelBotones =
                new JPanel();

        panelBotones.add(
                btnGenerarPredicciones
        );

        panelBotones.add(
                btnActualizarPredicciones
        );

        panel.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private void generarPredicciones() {

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea generar nuevamente las predicciones?\n"
                        + "Se reemplazarán las predicciones actuales.",
                        "Generar predicciones",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        btnGenerarPredicciones.setEnabled(false);

        boolean resultado =
                prediccionDAO.generarPredicciones();

        btnGenerarPredicciones.setEnabled(true);

        if (resultado) {

            cargarPredicciones();

            JOptionPane.showMessageDialog(
                    this,
                    "Las predicciones fueron generadas correctamente.",
                    "Proceso completado",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible generar las predicciones.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarPredicciones() {

        modeloPredicciones.setRowCount(0);

        List<Prediccion> lista =
                prediccionDAO.listar();

        for (Prediccion p : lista) {

            String diasCobertura;

            if (p.getDiasCobertura() == null) {
                diasCobertura = "N/A";
            } else {
                diasCobertura =
                        formatoDecimal.format(
                                p.getDiasCobertura()
                        );
            }

            modeloPredicciones.addRow(
                    new Object[]{
                            p.getCodigoProducto(),
                            p.getNombreProducto(),
                            p.getPeriodo(),
                            p.getVentasHistoricas(),
                            formatoDecimal.format(
                                    p.getPromedioDiario()
                            ),
                            formatoDecimal.format(
                                    p.getDemandaEstimada()
                            ),
                            p.getStockActual(),
                            diasCobertura,
                            p.getRecomendacion()
                    }
            );
        }
    }

    // =========================================================
    // ALERTAS
    // =========================================================

    private JPanel crearPanelAlertas() {

        JPanel panel =
                new JPanel(new BorderLayout());

        modeloAlertas =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Código",
                                "Producto",
                                "Tipo de alerta",
                                "Mensaje",
                                "Nivel",
                                "Estado",
                                "Fecha"
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

        tablaAlertas =
                new JTable(modeloAlertas);

        tablaAlertas.setAutoCreateRowSorter(
                true
        );

        tablaAlertas.setRowHeight(28);

        tablaAlertas.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(400);

        tablaAlertas.setDefaultRenderer(
                Object.class,
                new RendererAlertas()
        );

        panel.add(
                new JScrollPane(
                        tablaAlertas
                ),
                BorderLayout.CENTER
        );

        btnGenerarAlertas =
                new JButton(
                        "GENERAR ALERTAS"
                );

        btnActualizarAlertas =
                new JButton(
                        "ACTUALIZAR"
                );

        btnAtenderAlerta =
                new JButton(
                        "ATENDER ALERTA"
                );

        btnGenerarAlertas.addActionListener(
                e -> generarAlertas()
        );

        btnActualizarAlertas.addActionListener(
                e -> cargarAlertas()
        );

        btnAtenderAlerta.addActionListener(
                e -> atenderAlerta()
        );

        JPanel panelBotones =
                new JPanel();

        panelBotones.add(
                btnGenerarAlertas
        );

        panelBotones.add(
                btnActualizarAlertas
        );

        panelBotones.add(
                btnAtenderAlerta
        );

        panel.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private void generarAlertas() {

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea actualizar las alertas de inventario?\n"
                        + "Las alertas pendientes actuales serán reconstruidas.",
                        "Generar alertas",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        btnGenerarAlertas.setEnabled(false);

        boolean resultado =
                alertaDAO.generarAlertas();

        btnGenerarAlertas.setEnabled(true);

        if (resultado) {

            cargarAlertas();

            JOptionPane.showMessageDialog(
                    this,
                    "Las alertas fueron generadas correctamente.",
                    "Proceso completado",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible generar las alertas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarAlertas() {

        modeloAlertas.setRowCount(0);

        List<Alerta> lista =
                alertaDAO.listar();

        for (Alerta a : lista) {

            String fecha = "";

            if (a.getFechaAlerta() != null) {

                fecha =
                        formatoFecha.format(
                                a.getFechaAlerta()
                        );
            }

            modeloAlertas.addRow(
                    new Object[]{
                            a.getIdAlerta(),
                            a.getCodigoProducto(),
                            a.getNombreProducto(),
                            a.getTipoAlerta(),
                            a.getMensaje(),
                            a.getNivel(),
                            a.getEstado(),
                            fecha
                    }
            );
        }
    }

    private void atenderAlerta() {

        int filaVista =
                tablaAlertas.getSelectedRow();

        if (filaVista == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una alerta para atender.",
                    "Atender alerta",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tablaAlertas.convertRowIndexToModel(
                        filaVista
                );

        int idAlerta =
                Integer.parseInt(
                        modeloAlertas.getValueAt(
                                filaModelo,
                                0
                        ).toString()
                );

        String estado =
                modeloAlertas.getValueAt(
                        filaModelo,
                        6
                ).toString();

        if ("ATENDIDA".equals(estado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "La alerta seleccionada ya está atendida.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea marcar esta alerta como ATENDIDA?",
                        "Confirmar atención",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        boolean resultado =
                alertaDAO.atender(idAlerta);

        if (resultado) {

            cargarAlertas();

            JOptionPane.showMessageDialog(
                    this,
                    "La alerta fue marcada como ATENDIDA.",
                    "Proceso completado",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible atender la alerta.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // RENDERER DE ALERTAS
    // =========================================================

    private static class RendererAlertas
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            Component componente =
                    super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column
                    );

            if (!isSelected) {

                int filaModelo =
                        table.convertRowIndexToModel(row);

                Object nivelObj =
                        table.getModel().getValueAt(
                                filaModelo,
                                5
                        );

                String nivel =
                        nivelObj == null
                                ? ""
                                : nivelObj.toString();

                if ("CRITICO".equals(nivel)) {

                    componente.setBackground(
                            new Color(
                                    255,
                                    205,
                                    205
                            )
                    );

                    componente.setForeground(
                            new Color(
                                    150,
                                    0,
                                    0
                            )
                    );

                } else if ("ADVERTENCIA".equals(nivel)) {

                    componente.setBackground(
                            new Color(
                                    255,
                                    235,
                                    190
                            )
                    );

                    componente.setForeground(
                            new Color(
                                    150,
                                    90,
                                    0
                            )
                    );

                } else {

                    componente.setBackground(
                            new Color(
                                    215,
                                    245,
                                    215
                            )
                    );

                    componente.setForeground(
                            new Color(
                                    0,
                                    110,
                                    0
                            )
                    );
                }

            } else {

                componente.setBackground(
                        table.getSelectionBackground()
                );

                componente.setForeground(
                        table.getSelectionForeground()
                );
            }

            return componente;
        }
    }
}