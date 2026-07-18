package utils;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MOTOR DE EVIDENCIAS: EvidenceHelper
 * Esta clase utiliza la librería Apache POI para generar documentos Word (.docx).
 * Permite crear portadas, tablas de datos, registrar pasos con capturas y manejar estados de éxito o falla.
 */
public class EvidenceHelper {
    private XWPFDocument document;
    private String tituloPrueba;
    private String analista;
    private String historiaUsuario;
    private String descripcionCaso;
    private XWPFTableCell celdaResultado;

    /**
     * Constructor: Inicializa el documento y crea la portada automáticamente.
     */
    public EvidenceHelper(String tituloPrueba, String analista, String historiaUsuario, String descripcionCaso) {
        this.document = new XWPFDocument();
        this.tituloPrueba = tituloPrueba;
        this.analista = analista;
        this.historiaUsuario = historiaUsuario;
        this.descripcionCaso = descripcionCaso;
        crearPortada();
    }
    
    /**
     * Constructor legacy para compatibilidad
     */
    public EvidenceHelper(String tituloPrueba, String analista) {
        this(tituloPrueba, analista, "No especificada", tituloPrueba);
    }

    /**
     * Lógica para generar la primera página del reporte.
     * Incluye logo (si existe), título del caso, analista y fecha.
     */
    private void crearPortada() {
        // Carga dinámica del logo desde recursos
        File portadaImg = new File("src/test/resources/portada.png");
        if (!portadaImg.exists()) {
            portadaImg = new File("src/test/resources/logo.png");
        }

        if (portadaImg.exists()) {
            try (java.io.FileInputStream fis = new java.io.FileInputStream(portadaImg)) {
                XWPFParagraph pPortada = document.createParagraph();
                pPortada.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun rPortada = pPortada.createRun();
                // Insertar imagen con dimensiones escaladas
                rPortada.addPicture(fis, Document.PICTURE_TYPE_PNG, portadaImg.getName(), Units.toEMU(422), Units.toEMU(675));
                rPortada.addBreak(BreakType.PAGE);
            } catch (Exception e) {
                System.err.println("Error insertando portada: " + e.getMessage());
            }
        }

        // Títulos y Tabla de Información General
        XWPFParagraph pTitulo = document.createParagraph();
        pTitulo.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun rTitulo = pTitulo.createRun();
        rTitulo.setBold(true);
        rTitulo.setFontSize(24);
        rTitulo.setText("REPORTE DE EVIDENCIA DE AUTOMATIZACIÓN");
        rTitulo.addBreak();

        XWPFTable table = document.createTable(6, 2);
        table.setWidth("100%");

        setCelda(table.getRow(0).getCell(0), "Proyecto:", true);
        setCelda(table.getRow(0).getCell(1), "Automatización Selenium + Screenplay", false);
        
        setCelda(table.getRow(1).getCell(0), "Historia de Usuario:", true);
        setCelda(table.getRow(1).getCell(1), historiaUsuario, false);
        
        setCelda(table.getRow(2).getCell(0), "Caso de Prueba:", true);
        setCelda(table.getRow(2).getCell(1), descripcionCaso, false);

        setCelda(table.getRow(3).getCell(0), "Analista QA:", true);
        setCelda(table.getRow(3).getCell(1), analista, false);

        setCelda(table.getRow(4).getCell(0), "Fecha Ejecución:", true);
        setCelda(table.getRow(4).getCell(1), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), false);

        setCelda(table.getRow(5).getCell(0), "Resultado Final:", true);
        celdaResultado = table.getRow(5).getCell(1);
        setCelda(celdaResultado, "PASADO (Exitoso)", false);

        XWPFParagraph pSalto = document.createParagraph();
        pSalto.createRun().addBreak(BreakType.PAGE);
    }

    /** Helper para dar formato a las celdas de la tabla de portada */
    private void setCelda(XWPFTableCell celda, String texto, boolean esNegrita) {
        XWPFParagraph p = celda.getParagraphs().get(0);
        XWPFRun r = p.createRun();
        r.setBold(esNegrita);
        r.setFontSize(11);
        r.setFontFamily("Segoe UI");
        r.setText(texto);
    }

    /** Cambia el texto de la portada a rojo si la prueba falla en cualquier punto */
    public void marcarComoFallido() {
        if (celdaResultado != null) {
            XWPFParagraph p = celdaResultado.getParagraphs().get(0);
            for (int i = p.getRuns().size() - 1; i >= 0; i--) p.removeRun(i);
            XWPFRun r = p.createRun();
            r.setBold(true);
            r.setColor("FF0000"); // Rojo
            r.setText("FALLADO (Con Errores)");
        }
    }

    /** Agrega un bloque de paso con título, descripción y captura de pantalla */
    public void agregarPaso(String nombrePaso, String descripcion, byte[] imagenBytes) {
        XWPFParagraph pPaso = document.createParagraph();
        XWPFRun rPaso = pPaso.createRun();
        rPaso.setBold(true);
        rPaso.setFontSize(14);
        rPaso.setColor("1A365D");
        rPaso.setText(nombrePaso);

        XWPFParagraph pDesc = document.createParagraph();
        pDesc.createRun().setText("Descripción: " + descripcion);
        pDesc.createRun().addBreak();

        if (imagenBytes != null && imagenBytes.length > 0) {
            try {
                XWPFParagraph pImg = document.createParagraph();
                pImg.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun rImg = pImg.createRun();
                rImg.addPicture(new ByteArrayInputStream(imagenBytes), Document.PICTURE_TYPE_PNG, "ss.png", Units.toEMU(450), Units.toEMU(250));
            } catch (Exception e) {}
        }
    }

    /** Similar a agregarPaso pero con estilo visual de error (texto en rojo) */
    public void agregarPasoFallido(String nombrePaso, String descripcion, byte[] imagenBytes) {
        XWPFParagraph pPaso = document.createParagraph();
        XWPFRun rPaso = pPaso.createRun();
        rPaso.setBold(true);
        rPaso.setColor("FF0000"); // Rojo
        rPaso.setText(nombrePaso);

        XWPFParagraph pDesc = document.createParagraph();
        pDesc.createRun().setText("Falla: " + descripcion);
        
        // ... lógica de imagen igual ...
    }

    /** Guarda físicamente el archivo en la ruta destino especificada */
    public void guardarDocumento(String rutaDestino) {
        try {
            File file = new File(rutaDestino);
            file.getParentFile().mkdirs();
            try (FileOutputStream out = new FileOutputStream(file)) {
                document.write(out);
            }
            document.close();
        } catch (Exception e) {
            System.err.println("Error guardando Word: " + e.getMessage());
        }
    }
}