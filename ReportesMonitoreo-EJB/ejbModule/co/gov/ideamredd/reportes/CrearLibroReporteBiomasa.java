package co.gov.ideamredd.reportes;

import java.util.ResourceBundle;


import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CrearLibroReporteBiomasa {

	private XSSFWorkbook libro;
	private static final String archivoReportes = "co/gov/ideamredd/reportes/reportesWPS";
	private static final ResourceBundle infoReportes = ResourceBundle
			.getBundle(archivoReportes);
	private XSSFCellStyle estilo;
	private XSSFSheet hoja;
	private int indiceColumna;
	private int inicio;
	private String nombreReporte;

	public XSSFWorkbook construirLibroReporte(Integer reporte, String[] periodo) {
		hoja = crearLibro();
		estilo = estiloTituloReporte(libro);
		crearEncabezado(periodo, reporte);
		nombreReporte = "Biomasa";
		libro.setSheetName(0,"Biomasa-Carbono");
		// Util.escribirReportes("C:/IT/", "prueba", libro);
		return libro;
	}

	private void crearEncabezado(String[] periodo, Integer reporte) {
		XSSFRow fila1 = hoja.createRow(2);
		XSSFRow tituloPeriodo = hoja.createRow(1);
		indiceColumna = 1;
		inicio = 1;
		if (reporte == 7)
			for (int i = 0; i < periodo.length; i++)
				crearTituloConsolidado(fila1, tituloPeriodo, periodo[i]);
		else
			for (int i = 0; i < periodo.length; i++)
				crearTitulo(fila1, tituloPeriodo, periodo[i]);
	}

	private void crearTituloConsolidado(XSSFRow fila1, XSSFRow tituloPeriodo,
			String periodo) {
		hoja.setColumnWidth(0, 1000);
		XSSFCell zonaVida = fila1.createCell(indiceColumna);
		zonaVida.setCellValue(infoReportes
				.getString("reporte.biomasa.zonaVida"));
		zonaVida.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 8000);
		indiceColumna++;
		XSSFCell area = fila1.createCell(indiceColumna);
		area.setCellValue(infoReportes.getString("reporte.biomasa.area"));
		area.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 4000);
		indiceColumna++;
		XSSFCell biomasa = fila1.createCell(indiceColumna);
		biomasa.setCellValue(infoReportes.getString("reporte.biomasa.biomasa"));
		biomasa.setCellStyle(estilo);
		indiceColumna++;
		XSSFCell ba = fila1.createCell(indiceColumna);
		ba.setCellValue(infoReportes.getString("reporte.biomasa.BA"));
		ba.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 4000);
		indiceColumna++;
		XSSFCell carbono = fila1.createCell(indiceColumna);
		carbono.setCellValue(infoReportes.getString("reporte.biomasa.carbono"));
		carbono.setCellStyle(estilo);
		indiceColumna++;
		XSSFCell c = fila1.createCell(indiceColumna);
		c.setCellValue(infoReportes.getString("reporte.biomasa.C"));
		c.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 4000);
		indiceColumna++;
		XSSFCell co2 = fila1.createCell(indiceColumna);
		co2.setCellValue(infoReportes.getString("reporte.biomasa.CO2E"));
		co2.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 4000);
		XSSFCell per = tituloPeriodo.createCell(inicio);
		per.setCellValue(periodo);
		per.setCellStyle(estilo);
		for (int i = inicio + 1; i == indiceColumna; i++) {
			tituloPeriodo.createCell(i).setCellStyle(estilo);
		}
		CellRangeAddress mergeTitulo = new CellRangeAddress(
				tituloPeriodo.getRowNum(), tituloPeriodo.getRowNum(), inicio,
				indiceColumna);
		hoja.addMergedRegion(mergeTitulo);
		indiceColumna++;
		inicio = indiceColumna;
	}

	private void crearTitulo(XSSFRow fila1, XSSFRow tituloPeriodo,
			String periodo) {
		hoja.setColumnWidth(0, 1000);
		XSSFCell zonaVida = fila1.createCell(indiceColumna);
		zonaVida.setCellValue(infoReportes
				.getString("reporte.biomasa.zonaVida"));
		zonaVida.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 8000);
		indiceColumna++;
		XSSFCell areaHidro = fila1.createCell(indiceColumna);
		areaHidro.setCellValue(infoReportes
				.getString("reporte.biomasa.areaHidrografica"));
		areaHidro.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 8000);
		indiceColumna++;
		XSSFCell area = fila1.createCell(indiceColumna);
		area.setCellValue(infoReportes.getString("reporte.biomasa.area"));
		area.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 4000);
		indiceColumna++;
		XSSFCell biomasa = fila1.createCell(indiceColumna);
		biomasa.setCellValue(infoReportes.getString("reporte.biomasa.biomasa"));
		biomasa.setCellStyle(estilo);
		indiceColumna++;
		XSSFCell ba = fila1.createCell(indiceColumna);
		ba.setCellValue(infoReportes.getString("reporte.biomasa.BA"));
		ba.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 4000);
		indiceColumna++;
		XSSFCell carbono = fila1.createCell(indiceColumna);
		carbono.setCellValue(infoReportes.getString("reporte.biomasa.carbono"));
		carbono.setCellStyle(estilo);
		indiceColumna++;
		XSSFCell c = fila1.createCell(indiceColumna);
		c.setCellValue(infoReportes.getString("reporte.biomasa.C"));
		c.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 4000);
		indiceColumna++;
		XSSFCell co2 = fila1.createCell(indiceColumna);
		co2.setCellValue(infoReportes.getString("reporte.biomasa.CO2E"));
		co2.setCellStyle(estilo);
		hoja.setColumnWidth(indiceColumna, 4000);
		XSSFCell per = tituloPeriodo.createCell(inicio);
		per.setCellValue(periodo);
		per.setCellStyle(estilo);
		for (int i = inicio + 1; i == indiceColumna; i++) {
			tituloPeriodo.createCell(i).setCellStyle(estilo);
		}
		CellRangeAddress mergeTitulo = new CellRangeAddress(
				tituloPeriodo.getRowNum(), tituloPeriodo.getRowNum(), inicio,
				indiceColumna);
		hoja.addMergedRegion(mergeTitulo);
		indiceColumna++;
		inicio = indiceColumna;
	}
	
	public static XSSFCellStyle estiloTituloReporte(XSSFWorkbook libro) {
		XSSFCellStyle estiloCelda = libro.createCellStyle();
		estiloCelda.setWrapText(true);
		estiloCelda.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		estiloCelda.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		estiloCelda.setFillForegroundColor((short) 44);
		estiloCelda.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeightInPoints((short) 10);
		fuente.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		fuente.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		fuente.setColor((short) 9);
		estiloCelda.setFont(fuente);
		estiloCelda.setBorderBottom((short) 1);
		estiloCelda.setBottomBorderColor((short) 30);
		estiloCelda.setBorderLeft((short) 1);
		estiloCelda.setLeftBorderColor((short) 30);
		estiloCelda.setBorderRight((short) 1);
		estiloCelda.setRightBorderColor((short) 30);
		estiloCelda.setBorderTop((short) 1);
		estiloCelda.setTopBorderColor((short) 30);
		return estiloCelda;
	}

	private XSSFSheet crearLibro() {
		libro = new XSSFWorkbook();
		XSSFSheet hoja = libro.createSheet();
		return hoja;
	}

	public String getNombreReporte() {
		return nombreReporte;
	}

	public void setNombreReporte(String nombreReporte) {
		this.nombreReporte = nombreReporte;
	}

}
