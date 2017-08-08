package co.gov.ideamredd.reportes;

import java.util.ResourceBundle;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CrearLibroReporteCobertura {

	private XSSFWorkbook libro;
	private static final String archivoReportes = "co/gov/ideamredd/reportes/reportesWPS";
	private static final ResourceBundle infoReportes = ResourceBundle
			.getBundle(archivoReportes);
	private XSSFCellStyle estilo;
	private XSSFCellStyle estiloTitulo2;
	private XSSFCellStyle estilo2;
	private XSSFRow[] filas;
	private String nombreReporte;

	public XSSFWorkbook construirLibroReporte(Integer territorio,
			String[] periodo) {
		XSSFSheet hoja = crearLibro();
		nombreReporte = "CambioDeCoberturaBoscosa";
		estilo = estiloTituloReporte(libro);
		estiloTitulo2 = estiloTituloReporte2(libro);
		estilo2 = estiloDivisionReporte(libro);
		crearEncabezado(hoja, territorio, periodo);
		return libro;
	}

	private void crearEncabezado(XSSFSheet hoja, Integer territorio,
			String[] periodo) {
		if (territorio != 4) {
			agregarDivision(hoja, periodo);
			agregarTituloPeriodo(hoja, territorio);
		} else {
			agregarCoberturaConsolidado(hoja);
			agregarDivisionConsolidado(hoja, periodo);
			nombreReporte += "ConsolidadoNacional";
		}
	}

	private void agregarDivision(XSSFSheet hoja, String[] periodo) {
		Integer indiceTitulos = 3;
		int mergeTitulo = indiceTitulos;
		Integer numeroItems = Integer.valueOf(infoReportes
				.getString("CambioCoberturaBoscosaItems"));
		filas = new XSSFRow[periodo.length * numeroItems];
		int cont = 0;
		for (int i = 0; i < periodo.length; i++) {
			int j = 0;
			while (j < numeroItems) {
				XSSFRow titulo = hoja.createRow(indiceTitulos);
				XSSFCell info = titulo.createCell(2);
				titulo.createCell(1).setCellStyle(estilo2);
				info.setCellValue(infoReportes
						.getString("reporte.cobertura.item" + (j + 1)));
				info.setCellStyle(estilo2);
				filas[cont] = titulo;
				indiceTitulos++;
				j++;
				cont++;
			}
			XSSFRow per;
			if ((i + 1) == 1)
				per = filas[0];
			else
				per = filas[(numeroItems * (i + 1)) - numeroItems];

			XSSFCell tituloPer = per.createCell(1);
			tituloPer.setCellValue(periodo[i]);
			nombreReporte += periodo[i];
			tituloPer.setCellStyle(estilo2);
			CellRangeAddress r = new CellRangeAddress(mergeTitulo,
					indiceTitulos - 1, 1, 1);
			hoja.addMergedRegion(r);
			mergeTitulo = indiceTitulos;
		}
	}

	private void agregarTituloPeriodo(XSSFSheet hoja, Integer territorio) {
		Integer indexDivision = 3;
		int mergeTitulo = indexDivision;
		String tituloDivision = "";
		String nombreItem = "";
		Integer numeroItems;
		hoja.setColumnWidth(0, 1000);
		hoja.setColumnWidth(1, 3500);
		hoja.setColumnWidth(2, 6000);
		XSSFRow titulo = hoja.createRow(1);
		XSSFRow titulo2 = hoja.createRow(2);
		XSSFCell periodoAnalizado = titulo.createCell(1);
		periodoAnalizado.setCellStyle(estilo);
		periodoAnalizado.setCellValue("Periodo analizado");
		titulo2.createCell(1).setCellStyle(estilo);
		XSSFCell indicador = titulo.createCell(2);
		indicador.setCellStyle(estilo);
		indicador.setCellValue("Indicador");
		titulo2.createCell(2).setCellStyle(estilo);
		XSSFCell division = titulo.createCell(indexDivision);
		division.setCellStyle(estilo);
		if (territorio == 1) {
			tituloDivision += infoReportes.getString("DivisionTerritorial1");
			libro.setSheetName(0,
					infoReportes.getString("DivisionTerritorial1"));
			numeroItems = Integer
					.valueOf(infoReportes.getString("cantidadCar"));
			nombreItem = "nombreCar";
			nombreReporte += "CAR";
		} else if (territorio == 2) {
			tituloDivision += infoReportes.getString("DivisionTerritorial2");
			libro.setSheetName(0,
					infoReportes.getString("DivisionTerritorial2"));
			numeroItems = Integer.valueOf(infoReportes
					.getString("cantidadBosque"));
			nombreItem = "nobreBosque";
			nombreReporte += "Departamentos";
		} else {
			tituloDivision += infoReportes.getString("DivisionTerritorial3");
			libro.setSheetName(0,
					infoReportes.getString("DivisionTerritorial3"));
			numeroItems = Integer.valueOf(infoReportes
					.getString("cantidadHidro"));
			nombreItem = "nombreHidro";
			nombreReporte += "AreaHidrografica";
		}
		division.setCellValue(tituloDivision);
		numeroItems -= 1;
		for (int i = 0; i < numeroItems; i++) {
			hoja.setColumnWidth(indexDivision, 3300);
			if (titulo.getCell(indexDivision) == null)
				titulo.createCell(indexDivision).setCellStyle(estilo);
			XSSFCell item = titulo2.createCell(indexDivision);
			item.setCellStyle(estiloTitulo2);
			item.setCellValue(infoReportes.getString(nombreItem + (i + 1)));
			indexDivision++;
		}
		CellRangeAddress r = new CellRangeAddress(titulo.getRowNum(),
				titulo2.getRowNum(), 1, 1);
		CellRangeAddress r2 = new CellRangeAddress(titulo.getRowNum(),
				titulo2.getRowNum(), 2, 2);
		CellRangeAddress r3 = new CellRangeAddress(titulo.getRowNum(),
				titulo.getRowNum(), mergeTitulo, indexDivision - 1);
		CellRangeAddress r4 = new CellRangeAddress(titulo.getRowNum(),
				titulo2.getRowNum(), indexDivision, indexDivision);
		hoja.addMergedRegion(r);
		hoja.addMergedRegion(r2);
		hoja.addMergedRegion(r3);
		hoja.addMergedRegion(r4);
	}

	private XSSFSheet crearLibro() {
		libro = new XSSFWorkbook();
		XSSFSheet hoja = libro.createSheet();
		return hoja;
	}

	private void agregarCoberturaConsolidado(XSSFSheet hoja) {
		filas = new XSSFRow[5];
		XSSFRow fila1 = hoja.createRow(3);
		XSSFRow fila2 = hoja.createRow(4);
		XSSFRow fila3 = hoja.createRow(5);
		XSSFRow fila4 = hoja.createRow(6);
		XSSFRow fila5 = hoja.createRow(7);
		hoja.setColumnWidth(0, 1000);
		hoja.setColumnWidth(1, 6000);
		XSSFCell bosque = fila1.createCell(1);
		XSSFCell noBosque = fila2.createCell(1);
		XSSFCell sinInfo = fila3.createCell(1);
		XSSFCell deforestacion = fila4.createCell(1);
		XSSFCell regeneracion = fila5.createCell(1);
		bosque.setCellStyle(estilo2);
		noBosque.setCellStyle(estilo2);
		sinInfo.setCellStyle(estilo2);
		deforestacion.setCellStyle(estilo2);
		regeneracion.setCellStyle(estilo2);
		bosque.setCellValue("Bosque");
		noBosque.setCellValue("No Bosque");
		sinInfo.setCellValue("Sin Informacion");
		deforestacion.setCellValue("Deforestacion");
		regeneracion.setCellValue("Regeneracion");
		filas[0] = fila1;
		filas[1] = fila2;
		filas[2] = fila3;
		filas[3] = fila4;
		filas[4] = fila5;
	}

	private void agregarDivisionConsolidado(XSSFSheet hoja, String[] periodo) {
		int indiceColumna = 2;
		String titulo = "Periodo de analisis /\n Cobertura";
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

	public static XSSFCellStyle estiloTituloReporte2(XSSFWorkbook libro) {
		XSSFCellStyle estiloCelda = libro.createCellStyle();
		estiloCelda.setWrapText(true);
		estiloCelda.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		estiloCelda.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		estiloCelda.setFillForegroundColor((short) 31);
		estiloCelda.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeightInPoints((short) 8);
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
}
