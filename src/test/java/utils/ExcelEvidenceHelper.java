package utils;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.Units;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MOTOR DE EVIDENCIAS EXCEL: ExcelEvidenceHelper
 * Esta clase utiliza Apache POI para generar reportes de evidencia en Excel (.xlsx)
 * basándose en el template "Certificacion - NOMBRE DE LA HISTORIA DE USUARIO.xlsx"
 * Mantiene la estructura y estilos del modelo original.
 */
public class ExcelEvidenceHelper {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private int pasoActual;
    private int filaActual;
    private String tituloPrueba;
    private String analista;
    private boolean pruebaPasada;

    /**
     * Constructor: Carga el template Excel y lo inicializa
     */
    public ExcelEvidenceHelper(String tituloPrueba, String analista, String descripcionCaso) {
        this.tituloPrueba = tituloPrueba;
        this.analista = analista;
        this.pasoActual = 0;
        this.pruebaPasada = true;

        try {
            // Cargar el template Excel
            String templatePath = "src/test/resources/Certificacion - NOMBRE DE LA HISTORIA DE USUARIO.xlsx";
            FileInputStream fis = new FileInputStream(templatePath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();

            // Llenar encabezados
            llenarEncabezados(descripcionCaso);
            
            // Encontrar la fila donde comienza "EJECUCION DEL CASO"
            filaActual = encontrarFilaEjecucion();
            if (filaActual == -1) {
                filaActual = 13; // Valor por defecto si no lo encuentra
            }
            
            System.out.println("[EXCEL] Iniciando desde fila: " + filaActual);
            
        } catch (Exception e) {
            System.err.println("Error cargando template Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Busca la fila donde está "EJECUCION DEL CASO"
     */
    private int encontrarFilaEjecucion() {
        for (int row = 0; row < sheet.getLastRowNum(); row++) {
            XSSFRow currentRow = sheet.getRow(row);
            if (currentRow == null) continue;
            
            for (int col = 0; col < currentRow.getLastCellNum(); col++) {
                XSSFCell cell = currentRow.getCell(col);
                if (cell != null && cell.getStringCellValue().contains("EJECUCION DEL CASO")) {
                    System.out.println("[EXCEL] Encontrada sección EJECUCION DEL CASO en fila: " + (row + 1));
                    return row + 2; // Empezar dos filas después
                }
            }
        }
        return -1;
    }

    /**
     * Llena los campos de encabezado del Excel (nombre, fecha, analista, descripción)
     * Reemplaza contenido EN las celdas mergeadas rojas
     */
    private void llenarEncabezados(String descripcionCaso) {
        try {
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String fechaSimple = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            System.out.println("[EXCEL] Procesando merged cells para encabezados...");
            
            // Procesar merged cells - reemplazar placeholders EN las celdas rojas
            for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                CellRangeAddress merged = sheet.getMergedRegion(i);
                int firstRow = merged.getFirstRow();
                int firstCol = merged.getFirstColumn();
                
                XSSFRow row = sheet.getRow(firstRow);
                if (row == null) continue;
                
                XSSFCell cell = row.getCell(firstCol);
                if (cell == null) continue;
                
                try {
                    String cellValue = cell.getStringCellValue().trim();
                    if (cellValue.isEmpty()) continue;
                    
                    // REEMPLAZAR el contenido de la celda mergeada por el valor
                    if (cellValue.contains("CERTIFICACION") && cellValue.contains("NOMBRE DE LA HISTORIA")) {
                        cell.setCellValue(tituloPrueba);
                        System.out.println("[EXCEL] ✓ CERTIFICACION reemplazado: " + tituloPrueba);
                    }
                    
                    else if (cellValue.contains("NOMBRE DEL QA") && cellValue.contains("EJECUTA")) {
                        cell.setCellValue(analista);
                        System.out.println("[EXCEL] ✓ NOMBRE DEL QA reemplazado: " + analista);
                    }
                    
                    else if (cellValue.contains("FECHA ACTUAL")) {
                        cell.setCellValue(fecha);
                        System.out.println("[EXCEL] ✓ FECHA ACTUAL reemplazada: " + fecha);
                    }
                    
                    else if (cellValue.contains("DESCRIPCION DEL CASO")) {
                        cell.setCellValue(descripcionCaso);
                        System.out.println("[EXCEL] ✓ DESCRIPCION reemplazada: " + descripcionCaso);
                    }
                } catch (Exception e) {
                    // Ignorar si no es string
                }
            }

        } catch (Exception e) {
            System.err.println("Error llenando encabezados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Agrega un paso de prueba con descripción y captura de pantalla
     */
    public void agregarPaso(String nombrePaso, String descripcion, byte[] imagenBytes) {
        try {
            pasoActual++;
            
            // Fila para el nombre del paso
            XSSFCell cellNombrePaso = getCellaSafe(filaActual, 1);
            if (cellNombrePaso != null) {
                cellNombrePaso.setCellValue(pasoActual + ". " + nombrePaso);
                aplicarEstiloRojo(cellNombrePaso);
                aplicarEstilo(cellNombrePaso, true, 11, "FF0000"); // Rojo, negrita
            }

            filaActual++;

            // Agregar descripción
            if (!descripcion.isEmpty()) {
                XSSFCell cellDesc = getCellaSafe(filaActual, 2);
                if (cellDesc != null) {
                    cellDesc.setCellValue(descripcion);
                    aplicarEstilo(cellDesc, false, 10, "000000");
                }
                filaActual++;
            }

            // Agregar captura de pantalla
            if (imagenBytes != null && imagenBytes.length > 0) {
                try {
                    // Insertar imagen
                    int pictureIdx = workbook.addPicture(imagenBytes, Workbook.PICTURE_TYPE_PNG);
                    XSSFDrawing drawing = sheet.createDrawingPatriarch();
                    
                    ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 2, filaActual, 8, filaActual + 15);
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                    
                    drawing.createPicture(anchor, pictureIdx);
                    
                    // Ajustar altura de filas para acomodar imagen
                    for (int i = filaActual; i < filaActual + 15; i++) {
                        XSSFRow row = sheet.getRow(i);
                        if (row == null) {
                            row = sheet.createRow(i);
                        }
                        row.setHeightInPoints(15);
                    }
                    
                    filaActual += 16;
                    
                } catch (Exception e) {
                    System.err.println("Error insertando imagen: " + e.getMessage());
                    filaActual += 2;
                }
            } else {
                filaActual += 2;
            }

            // Agregar espacio entre pasos
            filaActual += 2;

        } catch (Exception e) {
            System.err.println("Error agregando paso: " + e.getMessage());
        }
    }

    /**
     * Agrega un paso fallido (con estilos de error)
     */
    public void agregarPasoFallido(String nombrePaso, String descripcion, byte[] imagenBytes) {
        pruebaPasada = false;
        try {
            pasoActual++;
            
            // Nombre del paso en rojo
            XSSFCell cellNombrePaso = getCellaSafe(filaActual, 1);
            if (cellNombrePaso != null) {
                cellNombrePaso.setCellValue(pasoActual + ". " + nombrePaso + " [FALLIDO]");
                aplicarEstilo(cellNombrePaso, true, 11, "FF0000"); // Rojo
            }

            filaActual++;

            // Descripción del error
            if (!descripcion.isEmpty()) {
                XSSFCell cellDesc = getCellaSafe(filaActual, 2);
                if (cellDesc != null) {
                    cellDesc.setCellValue("ERROR: " + descripcion);
                    aplicarEstilo(cellDesc, false, 10, "FF0000");
                }
                filaActual++;
            }

            // Captura del error
            if (imagenBytes != null && imagenBytes.length > 0) {
                try {
                    int pictureIdx = workbook.addPicture(imagenBytes, Workbook.PICTURE_TYPE_PNG);
                    XSSFDrawing drawing = sheet.createDrawingPatriarch();
                    
                    ClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, 2, filaActual, 8, filaActual + 15);
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                    
                    drawing.createPicture(anchor, pictureIdx);
                    
                    for (int i = filaActual; i < filaActual + 15; i++) {
                        XSSFRow row = sheet.getRow(i);
                        if (row == null) {
                            row = sheet.createRow(i);
                        }
                        row.setHeightInPoints(15);
                    }
                    
                    filaActual += 16;
                } catch (Exception e) {
                    System.err.println("Error insertando imagen de error: " + e.getMessage());
                    filaActual += 2;
                }
            } else {
                filaActual += 2;
            }

            filaActual += 2;

        } catch (Exception e) {
            System.err.println("Error agregando paso fallido: " + e.getMessage());
        }
    }

    /**
     * Obtiene una celda de forma segura, creando la fila si no existe
     */
    private XSSFCell getCellaSafe(int rowIndex, int colIndex) {
        XSSFRow row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        XSSFCell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }
        return cell;
    }

    /**
     * Aplica estilo rojo a una celda (fondo rojo con texto blanco)
     */
    private void aplicarEstiloRojo(XSSFCell cell) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFColor color = new XSSFColor(new java.awt.Color(255, 0, 0), null); // Rojo
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        XSSFFont font = workbook.createFont();
        font.setColor(new XSSFColor(new java.awt.Color(255, 255, 255), null)); // Blanco
        font.setBold(true);
        style.setFont(font);
        
        cell.setCellStyle(style);
    }

    /**
     * Aplica estilo personalizado a una celda
     */
    private void aplicarEstilo(XSSFCell cell, boolean negrita, int tamaño, String colorHex) {
        XSSFCellStyle style = workbook.createCellStyle();
        
        XSSFFont font = workbook.createFont();
        font.setBold(negrita);
        font.setFontHeightInPoints((short) tamaño);
        
        if (!colorHex.equals("000000")) {
            try {
                int r = Integer.valueOf(colorHex.substring(0, 2), 16);
                int g = Integer.valueOf(colorHex.substring(2, 4), 16);
                int b = Integer.valueOf(colorHex.substring(4, 6), 16);
                font.setColor(new XSSFColor(new java.awt.Color(r, g, b), null));
            } catch (Exception e) {
                font.setColor(new XSSFColor(new java.awt.Color(0, 0, 0), null));
            }
        }
        
        style.setFont(font);
        style.setWrapText(true);
        cell.setCellStyle(style);
    }

    /**
     * Guarda el Excel en la ruta especificada
     */
    public void guardarDocumento(String rutaDestino) {
        try {
            File file = new File(rutaDestino);
            file.getParentFile().mkdirs();
            
            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
            
            workbook.close();
            System.out.println("Evidencia Excel guardada en: " + rutaDestino);
            
        } catch (Exception e) {
            System.err.println("Error guardando Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
