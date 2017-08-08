// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

import java.util.ResourceBundle;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase usada para generar el reporte xls de Bosque no bosque.
 */
public class CrearLibroReporteBNB {

	private XSSFWorkbook libro;
	private static final String archivoReportes = "co/gov/ideamredd/reportes/dao/reportesWPS";
	private static final ResourceBundle infoReportes = ResourceBundle
			.getBundle(archivoReportes);
	private XSSFCellStyle estilo;
	private XSSFCellStyle estilo2;
	private XSSFRow[] filas;
	private String nombreReporte;
	private XSSFSheet hoja;

	public XSSFWorkbook construirLibroReporte(Integer territorio,
			String[] periodo) {
		hoja = crearLibro();
		nombreReporte = "BosqueNoBosque";
		estilo = estiloTituloReporte(libro);
		estilo2 = estiloDivisionReporte(libro);
		crearEncabezado(hoja, territorio, periodo);
		return libro;
	}

	private void crearEncabezado(XSSFSheet hoja, Integer territorio,
			String[] periodo) {
		if (territorio != 4) {
			agregarTituloPeriodo(hoja, periodo, territorio);
			agregarDivision(hoja, territorio);
		} else {
			agregarCoberturaConsolidado(hoja);
			agregarDivisionConsolidado(hoja, periodo);
			nombreReporte += "ConsolidadoNacional";
		}
	}

	private void agregarDivision(XSSFSheet hoja, Integer territorio) {
		int indiceColumna = 3;
		Integer numeroItems;
		String item = "";
		if (territorio == 1) {
			numeroItems = Integer
					.valueOf(infoReportes.getString("cantidadCar"));
			item = "nombreCar";
			nombreReporte += "CAR";
		} else if (territorio == 2) {
			numeroItems = Integer.valueOf(infoReportes
					.getString("cantidadBosque"));
			item = "nobreBosque";
			nombreReporte += "Departamentos";
		} else {
			numeroItems = Integer.valueOf(infoReportes
					.getString("cantidadHidro"));
			item = "nombreHidro";
			nombreReporte += "AreasHidrograficas";
		}
		numeroItems -= 1;
		filas = new XSSFRow[numeroItems * 2];
		int cont = 0;
		for (int i = 0; i < numeroItems; i++) {
			XSSFRow fila = hoja.createRow(indiceColumna);
			XSSFRow fila1 = hoja.createRow(indiceColumna + 1);
			XSSFCell division = fila.createCell(1);
			fila1.createCell(1).setCellStyle(estilo2);
			division.setCellValue(infoReportes.getString(item + (i + 1)));
			division.setCellStyle(estilo2);
			CellRangeAddress mergeTitulo = new CellRangeAddress(
					fila.getRowNum(), fila1.getRowNum(),
					division.getColumnIndex(), division.getColumnIndex());
			hoja.addMergedRegion(mergeTitulo);
			XSSFCell area = fila.createCell(2);
			area.setCellValue(infoReportes.getString("hectareas"));
			area.setCellStyle(estilo2);
			XSSFCell porcentaje = fila1.createCell(2);
			porcentaje.setCellValue(infoReportes.getString("procentaje"));
			porcentaje.setCellStyle(estilo2);
			indiceColumna += 2;
			filas[cont] = fila;
			filas[cont + 1] = fila1;
			cont += 2;
		}
	}

	private void agregarTituloPeriodo(XSSFSheet hoja, String[] periodo,
			Integer territorio) {
		String titulo = "Coberturas /\n ";
		int indiceDivision = 3;

		titulo += infoReportes.getString("DivisionTerritorial"
				+ territorio.toString());
		libro.setSheetName(
				0,
				infoReportes.getString("DivisionTerritorial"
						+ territorio.toString()));
		XSSFRow fila1 = hoja.createRow(1);
		XSSFRow fila2 = hoja.createRow(2);
		hoja.setColumnWidth(0, 1000);
		XSSFCell coberturas = fila1.createCell(1);
		fila1.createCell(2).setCellStyle(estilo);
		fila2.createCell(1).setCellStyle(estilo);
		fila2.createCell(2).setCellStyle(estilo);
		coberturas.setCellValue(titulo);
		coberturas.setCellStyle(estilo);
		CellRangeAddress mergeTitulo = new CellRangeAddress(fila1.getRowNum(),
				fila2.getRowNum(), coberturas.getColumnIndex(),
				coberturas.getColumnIndex() + 1);
		hoja.addMergedRegion(mergeTitulo);
		hoja.setColumnWidth(1, 3800);
		hoja.setColumnWidth(2, 2200);
		for (int i = 0; i < periodo.length; i++) {
			int merge = indiceDivision;
			XSSFCell periodoAnalisis = fila1.createCell(indiceDivision);
			periodoAnalisis.setCellStyle(estilo);
			periodoAnalisis.setCellValue("Periodo de Analisis:" + periodo[i]);
			nombreReporte += periodo[i];
			Integer numeroItems = Integer.valueOf(infoReportes
					.getString("BosqueNoBosqueItems"));
			for (int j = 0; j < numeroItems; j++) {
				if (j != 0)
					fila1.createCell(indiceDivision).setCellStyle(estilo);
				XSSFCell bosque = fila2.createCell(indiceDivision);
				bosque.setCellValue(infoReportes.getString("reporte.bnb.item"
						+ (j + 1)));
				bosque.setCellStyle(estilo);
				hoja.setColumnWidth(indiceDivision, 4500);
				indiceDivision++;
			}
			CellRangeAddress r1 = new CellRangeAddress(fila1.getRowNum(),
					fila1.getRowNum(), merge, indiceDivision - 1);
			hoja.addMergedRegion(r1);
		}
	}

	private void agregarCoberturaConsolidado(XSSFSheet hoja) {
		libro.setSheetName(0, "Consolidado Nacional");
		filas = new XSSFRow[4];
		XSSFRow fila1 = hoja.createRow(3);
		XSSFRow fila2 = hoja.createRow(4);
		XSSFRow fila3 = hoja.createRow(5);
		XSSFRow fila4 = hoja.createRow(6);
		hoja.setColumnWidth(0, 1000);
		hoja.setColumnWidth(1, 6000);
		XSSFCell bosque = fila1.createCell(1);
		XSSFCell noBosque = fila2.createCell(1);
		XSSFCell sinInfo = fila3.createCell(1);
		XSSFCell total = fila4.createCell(1);
		bosque.setCellStyle(estilo2);
		noBosque.setCellStyle(estilo2);
		sinInfo.setCellStyle(estilo2);
		total.setCellStyle(estilo2);
		bosque.setCellValue("Bosque");
		noBosque.setCellValue("No Bosque");
		sinInfo.setCellValue("Sin Informacion");
		total.setCellValue("Total");
		filas[0] = fila1;
		filas[1] = fila2;
		filas[2] = fila3;
		filas[3] = fila4;
	}

	private void agregarDivisionConsolidado(XSSFSheet hoja, String[] periodo) {
		int indiceColumna = 2;
		String titulo = "Periodo de an�lisis /\n Cobertura";
		XSSFRow filaTitulo = hoja.createRow(1);
		XSSFRow filaTitulo2 = hoja.createRow(2);
		hoja.setColumnWidth(2, 3500);
		hoja.setColumnWidth(3, 3500);
		XSSFCell cellPeriodoCobertura = filaTitulo.createCell(1);
		XSSFCell cellPeriodoCobertura2 = filaTitulo2.createCell(1);
		for (int i = 0; i < periodo.length; i++) {
			XSSFCell per = filaTitulo.createCell(indiceColumna);
			filaTitulo.createCell(indiceColumna + 1).setCellStyle(estilo);
			XSSFCell area = filaTitulo2.createCell(indiceColumna);
			XSSFCell porcentaje = filaTitulo2.createCell(indiceColumna + 1);
			per.setCellValue("Cobertura " + periodo[i]);
			nombreReporte += periodo[i];
			per.setCellStyle(estilo);
			CellRangeAddress r1 = new CellRangeAddress(filaTitulo.getRowNum(),
					filaTitulo.getRowNum(), indiceColumna, indiceColumna + 1);
			hoja.addMergedRegion(r1);
			area.setCellValue(infoReportes.getString("hectareas"));
			area.setCellStyle(estilo);
			porcentaje.setCellValue(infoReportes.getString("procentaje"));
			porcentaje.setCellStyle(estilo);
			indiceColumna += 2;
		}
		cellPeriodoCobertura.setCellStyle(estilo);
		cellPeriodoCobertura2.setCellStyle(estilo);
		cellPeriodoCobertura.setCellValue(titulo);
		filaTitulo.setHeight((short) 300);
		filaTitulo2.setHeight((short) 300);
		CellRangeAddress r1 = new CellRangeAddress(filaTitulo.getRowNum(),
				filaTitulo2.getRowNum(), 1, 1);
		hoja.addMergedRegion(r1);
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

	public static XSSFCellStyle estiloDivisionReporte(XSSFWorkbook libro) {
		XSSFCellStyle estiloCelda = libro.createCellStyle();
		estiloCelda.setWrapText(true);
		estiloCelda.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		estiloCelda.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		// estiloCelda.setFillForegroundColor((short) 31);
		// estiloCelda.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeightInPoints((short) 8);
		fuente.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		fuente.setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		fuente.setColor((short) 8);
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

	public XSSFRow[] getFilas() {
		return filas;
	}

	public void setFilas(XSSFRow[] filas) {
		this.filas = filas;
	}

	public String getNombreReporte() {
		return nombreReporte;
	}

	public void setNombreReporte(String nombreReporte) {
		this.nombreReporte = nombreReporte;
	}

	public XSSFSheet getHoja() {
		return hoja;
	}

	public void setHoja(XSSFSheet hoja) {
		this.hoja = hoja;
	}
}
