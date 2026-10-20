package com.inventarios.util;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportadorReporte {

    private ExportadorReporte() {
    }

    // =========================================================
    // EXPORTAR A EXCEL
    // =========================================================

    public static void exportarExcel(
            JTable tabla,
            String nombreReporte
    ) {

        if (tabla == null || tabla.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    null,
                    "No hay datos para exportar.",
                    "Exportar Excel",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        javax.swing.JFileChooser selector =
                new javax.swing.JFileChooser();

        selector.setDialogTitle(
                "Guardar reporte Excel"
        );

        selector.setSelectedFile(
                new File(
                        nombreReporte
                                + "_"
                                + fechaArchivo()
                                + ".xlsx"
                )
        );

        int resultado =
                selector.showSaveDialog(null);

        if (resultado
                != javax.swing.JFileChooser.APPROVE_OPTION) {

            return;
        }

        File archivo =
                selector.getSelectedFile();

        if (!archivo.getName()
                .toLowerCase()
                .endsWith(".xlsx")) {

            archivo =
                    new File(
                            archivo.getAbsolutePath()
                                    + ".xlsx"
                    );
        }

        try (Workbook workbook =
                     new XSSFWorkbook()) {

            Sheet hoja =
                    workbook.createSheet(
                            nombreReporte
                    );

            TableModel modelo =
                    tabla.getModel();

            // -------------------------------------------------
            // ENCABEZADOS
            // -------------------------------------------------

            Row filaEncabezado =
                    hoja.createRow(0);

            for (int columna = 0;
                 columna < modelo.getColumnCount();
                 columna++) {

                Cell celda =
                        filaEncabezado.createCell(
                                columna
                        );

                celda.setCellValue(
                        modelo.getColumnName(
                                columna
                        )
                );
            }

            // -------------------------------------------------
            // DATOS
            // -------------------------------------------------

            for (int fila = 0;
                 fila < tabla.getRowCount();
                 fila++) {

                Row filaExcel =
                        hoja.createRow(
                                fila + 1
                        );

                for (int columna = 0;
                     columna < modelo.getColumnCount();
                     columna++) {

                    int filaModelo =
                            tabla.convertRowIndexToModel(
                                    fila
                            );

                    Object valor =
                            modelo.getValueAt(
                                    filaModelo,
                                    columna
                            );

                    Cell celda =
                            filaExcel.createCell(
                                    columna
                            );

                    if (valor != null) {

                        celda.setCellValue(
                                valor.toString()
                        );
                    }
                }
            }

            // -------------------------------------------------
            // AJUSTAR COLUMNAS
            // -------------------------------------------------

            for (int columna = 0;
                 columna < modelo.getColumnCount();
                 columna++) {

                hoja.autoSizeColumn(
                        columna
                );
            }

            // -------------------------------------------------
            // GUARDAR
            // -------------------------------------------------

            try (FileOutputStream salida =
                         new FileOutputStream(
                                 archivo
                         )) {

                workbook.write(salida);
            }

            JOptionPane.showMessageDialog(
                    null,
                    "Reporte Excel generado correctamente.\n\n"
                            + archivo.getAbsolutePath(),
                    "Exportación completada",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al exportar a Excel:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // EXPORTAR A PDF
    // =========================================================

    public static void exportarPDF(
            JTable tabla,
            String nombreReporte
    ) {

        if (tabla == null || tabla.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    null,
                    "No hay datos para exportar.",
                    "Exportar PDF",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        javax.swing.JFileChooser selector =
                new javax.swing.JFileChooser();

        selector.setDialogTitle(
                "Guardar reporte PDF"
        );

        selector.setSelectedFile(
                new File(
                        nombreReporte
                                + "_"
                                + fechaArchivo()
                                + ".pdf"
                )
        );

        int resultado =
                selector.showSaveDialog(null);

        if (resultado
                != javax.swing.JFileChooser.APPROVE_OPTION) {

            return;
        }

        File archivo =
                selector.getSelectedFile();

        if (!archivo.getName()
                .toLowerCase()
                .endsWith(".pdf")) {

            archivo =
                    new File(
                            archivo.getAbsolutePath()
                                    + ".pdf"
                    );
        }

        Document documento =
                new Document(
                        PageSize.A4.rotate(),
                        25,
                        25,
                        30,
                        30
                );

        try {

            PdfWriter.getInstance(
                    documento,
                    new FileOutputStream(
                            archivo
                    )
            );

            documento.open();

            // -------------------------------------------------
            // TÍTULO
            // -------------------------------------------------

            Font fuenteTitulo =
                    new Font(
                            Font.HELVETICA,
                            18,
                            Font.BOLD
                    );

            Paragraph titulo =
                    new Paragraph(
                            nombreReporte,
                            fuenteTitulo
                    );

            titulo.setAlignment(
                    Element.ALIGN_CENTER
            );

            documento.add(titulo);

            documento.add(
                    new Paragraph(
                            "Fecha de generación: "
                                    + fechaLegible()
                    )
            );

            documento.add(
                    new Paragraph(" ")
            );

            // -------------------------------------------------
            // TABLA
            // -------------------------------------------------

            TableModel modelo =
                    tabla.getModel();

            PdfPTable tablaPDF =
                    new PdfPTable(
                            modelo.getColumnCount()
                    );

            tablaPDF.setWidthPercentage(
                    100
            );

            // -------------------------------------------------
            // ENCABEZADOS
            // -------------------------------------------------

            Font fuenteEncabezado =
                    new Font(
                            Font.HELVETICA,
                            8,
                            Font.BOLD
                    );

            for (int columna = 0;
                 columna < modelo.getColumnCount();
                 columna++) {

                PdfPCell celda =
                        new PdfPCell(
                                new Phrase(
                                        modelo.getColumnName(
                                                columna
                                        ),
                                        fuenteEncabezado
                                )
                        );

                celda.setHorizontalAlignment(
                        Element.ALIGN_CENTER
                );

                tablaPDF.addCell(celda);
            }

            // -------------------------------------------------
            // DATOS
            // -------------------------------------------------

            Font fuenteDatos =
                    new Font(
                            Font.HELVETICA,
                            7
                    );

            for (int fila = 0;
                 fila < tabla.getRowCount();
                 fila++) {

                int filaModelo =
                        tabla.convertRowIndexToModel(
                                fila
                        );

                for (int columna = 0;
                     columna < modelo.getColumnCount();
                     columna++) {

                    Object valor =
                            modelo.getValueAt(
                                    filaModelo,
                                    columna
                            );

                    PdfPCell celda =
                            new PdfPCell(
                                    new Phrase(
                                            valor == null
                                                    ? ""
                                                    : valor.toString(),
                                            fuenteDatos
                                    )
                            );

                    tablaPDF.addCell(celda);
                }
            }

            documento.add(
                    tablaPDF
            );

            documento.close();

            JOptionPane.showMessageDialog(
                    null,
                    "Reporte PDF generado correctamente.\n\n"
                            + archivo.getAbsolutePath(),
                    "Exportación completada",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            if (documento.isOpen()) {
                documento.close();
            }

            JOptionPane.showMessageDialog(
                    null,
                    "Error al exportar a PDF:\n"
                            + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // FECHA PARA NOMBRE DEL ARCHIVO
    // =========================================================

    private static String fechaArchivo() {

        return new SimpleDateFormat(
                "yyyyMMdd_HHmmss"
        ).format(
                new Date()
        );
    }

    // =========================================================
    // FECHA LEGIBLE
    // =========================================================

    private static String fechaLegible() {

        return new SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss"
        ).format(
                new Date()
        );
    }
}