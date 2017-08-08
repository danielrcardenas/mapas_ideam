// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.proyecto.dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import co.gov.ideamredd.lenguaje.LenguajeI18N;
import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.proyecto.entities.ConteoProyecto;
import co.gov.ideamredd.proyecto.dao.ConsultaProyecto;
import co.gov.ideamredd.reportes.entities.InformacionReporteBiomasa;

/**
 * Clase usada para generar el reporte xls de proyectos.
 */
public class ExcelReporteProyectos {

	private XSSFWorkbook		libro;
	private XSSFCellStyle		estiloTitulo, estiloSerie, estiloDato;
	private XSSFSheet			hojaTOTAL, hojaESTADO, hojaTENENCIA, hojaACTIVIDAD;
	private int					indiceColumna;
	private String				nombreReporte;
	private String				tipo, desde, hasta;
	ResourceBundle				msj;
	ArrayList<ConteoProyecto>	conteoProyectosTOTAL, conteoProyectosESTADO, conteoProyectosTENENCIA, conteoProyectosACTIVIDAD;
	Auxiliar					aux			= new Auxiliar();
	private int					pictureIdx	= 0;
	private XSSFDrawing			drawing;

	public XSSFWorkbook construirLibroReporte(String tipo, String desde, String hasta) {

		this.tipo = tipo;
		this.desde = desde;
		this.hasta = hasta;

		LenguajeI18N i18n = new LenguajeI18N();
		i18n.setLenguaje("es");
		i18n.setPais("CO");
		msj = i18n.obtenerMensajeIdioma();

		crearLibro();
		estiloTitulo = estiloTituloReporte(libro);
		estiloSerie = estiloSerieReporte(libro);
		estiloDato = estiloDatoReporte(libro);
		nombreReporte = "Proyectos";
		libro.setSheetName(0, "TOTAL");
		libro.setSheetName(1, "ESTADO");
		libro.setSheetName(2, "TENENCIA");
		libro.setSheetName(3, "ACTIVIDAD");
		generarHojaTOTAL();
		generarHojaESTADO();
		generarHojaTENENCIA();
		generarHojaACTIVIDAD();

		return libro;
	}

	private void generarHojaTOTAL() {
		conteoProyectosTOTAL = ConsultaProyecto.contarProyectos(tipo, "TOTAL", desde, hasta);
		ConteoProyecto conteoProyecto = new ConteoProyecto();
		int conteo = 0;
		String vtipo = "";
		String str_title = msj.getString("reportes.proyectos.descripcion_TOTAL");
		String str_titulo_conteo = msj.getString("conteo");
		String str_titulo_tipo = msj.getString("reportes.proyectos.titulo_" + tipo);
		String str_titulo_agregacion = msj.getString("reportes.proyectos.TOTAL");
		String str_titulo_categoria = str_titulo_tipo + " - " + str_titulo_agregacion;

		XSSFRow fila_titulo = hojaTOTAL.createRow(0);

		XSSFCell titulo_conteo = fila_titulo.createCell(0);
		titulo_conteo.setCellValue(msj.getString("conteo"));
		titulo_conteo.setCellStyle(estiloTitulo);

		if (!tipo.equals("NAL")) {
			XSSFCell titulo_total = fila_titulo.createCell(1);
			titulo_total.setCellValue(msj.getString("reportes.proyectos." + tipo));
			titulo_total.setCellStyle(estiloTitulo);
		}

		DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();

		for (int i = 0; i < conteoProyectosTOTAL.size(); i++) {
			conteoProyecto = conteoProyectosTOTAL.get(i);
			conteo = conteoProyecto.getConteo();
			XSSFRow fila = hojaTOTAL.createRow(i + 1);

			XSSFCell celda_conteo = fila.createCell(0);
			celda_conteo.setCellValue(conteo);
			celda_conteo.setCellStyle(estiloDato);

			if (!tipo.equals("NAL")) {
				vtipo = aux.nz(conteoProyecto.getDimension1(), "");
				XSSFCell celda_vtipo = fila.createCell(1);
				celda_vtipo.setCellValue(vtipo);
				celda_vtipo.setCellStyle(estiloSerie);
			}

			if (!tipo.equals("NAL")) {
				defaultCategoryDataset.addValue(conteo, vtipo, str_titulo_conteo);
			}
			else {
				defaultCategoryDataset.addValue(conteo, "Total", str_titulo_conteo);
			}
		}

		hojaTOTAL.autoSizeColumn(0);
		hojaTOTAL.autoSizeColumn(1);
		hojaTOTAL.autoSizeColumn(2);
		hojaTOTAL.autoSizeColumn(3);

		insertarImagen(createChartBarras(str_title, str_titulo_categoria, str_titulo_conteo, defaultCategoryDataset), 3, hojaTOTAL);

	}

	private void generarHojaESTADO() {
		conteoProyectosESTADO = ConsultaProyecto.contarProyectos(tipo, "ESTADO", desde, hasta);
		ConteoProyecto conteoProyecto = new ConteoProyecto();
		int conteo = 0;
		String vtipo = "";
		String vagregacion = "";
		String str_title = msj.getString("reportes.proyectos.descripcion_ESTADO");
		String str_titulo_conteo = msj.getString("conteo");
		String str_titulo_tipo = msj.getString("reportes.proyectos.titulo_" + tipo);
		String str_titulo_agregacion = msj.getString("reportes.proyectos.ESTADO");
		String str_titulo_categoria = str_titulo_tipo + " - " + str_titulo_agregacion;

		XSSFRow fila_titulo = hojaESTADO.createRow(0);

		indiceColumna = 0;

		XSSFCell titulo_conteo = fila_titulo.createCell(indiceColumna);
		titulo_conteo.setCellValue(str_titulo_conteo);
		titulo_conteo.setCellStyle(estiloTitulo);

		if (!tipo.equals("NAL")) {
			indiceColumna++;
			XSSFCell titulo_vtipo = fila_titulo.createCell(indiceColumna);
			titulo_vtipo.setCellValue(str_titulo_tipo);
			titulo_vtipo.setCellStyle(estiloTitulo);
		}

		indiceColumna++;
		XSSFCell titulo_agregacion = fila_titulo.createCell(indiceColumna);
		titulo_agregacion.setCellValue(str_titulo_agregacion);
		titulo_agregacion.setCellStyle(estiloTitulo);

		DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();

		for (int i = 0; i < conteoProyectosESTADO.size(); i++) {
			conteoProyecto = conteoProyectosESTADO.get(i);
			conteo = conteoProyecto.getConteo();
			XSSFRow fila = hojaESTADO.createRow(i + 1);

			indiceColumna = 0;

			XSSFCell celda_conteo = fila.createCell(0);
			celda_conteo.setCellValue(conteo);
			celda_conteo.setCellStyle(estiloDato);

			if (!tipo.equals("NAL")) {
				indiceColumna++;
				vtipo = aux.nz(conteoProyecto.getDimension1(), "");
				XSSFCell celda_vtipo = fila.createCell(indiceColumna);
				celda_vtipo.setCellValue(vtipo);
				celda_vtipo.setCellStyle(estiloSerie);
			}

			indiceColumna++;
			vagregacion = aux.nz(conteoProyecto.getDimension2(), "");
			XSSFCell celda_vagregacion = fila.createCell(indiceColumna);
			celda_vagregacion.setCellValue(vagregacion);
			celda_vagregacion.setCellStyle(estiloSerie);
			
			if (!tipo.equals("NAL")) {
				defaultCategoryDataset.addValue(conteo, vtipo + " - " + vagregacion, str_titulo_conteo);
			}
			else {
				defaultCategoryDataset.addValue(conteo, vtipo, str_titulo_conteo);
			}
		}

		hojaESTADO.autoSizeColumn(0);
		hojaESTADO.autoSizeColumn(1);
		hojaESTADO.autoSizeColumn(2);
		hojaESTADO.autoSizeColumn(3);
		
		insertarImagen(createChartBarras(str_title, str_titulo_categoria, str_titulo_conteo, defaultCategoryDataset), 4, hojaESTADO);

	}

	private void generarHojaTENENCIA() {
		conteoProyectosTENENCIA = ConsultaProyecto.contarProyectos(tipo, "TENENCIA", desde, hasta);
		ConteoProyecto conteoProyecto = new ConteoProyecto();
		int conteo = 0;
		String vtipo = "";
		String vagregacion = "";
		String str_title = msj.getString("reportes.proyectos.descripcion_TENENCIA");
		String str_titulo_conteo = msj.getString("conteo");
		String str_titulo_tipo = msj.getString("reportes.proyectos." + tipo);
		String str_titulo_agregacion = msj.getString("reportes.proyectos.TENENCIA");
		String str_titulo_categoria = str_titulo_tipo + " - " + str_titulo_agregacion;

		XSSFRow fila_titulo = hojaTENENCIA.createRow(0);

		indiceColumna = 0;

		XSSFCell titulo_conteo = fila_titulo.createCell(indiceColumna);
		titulo_conteo.setCellValue(msj.getString("conteo"));
		titulo_conteo.setCellStyle(estiloTitulo);

		if (!tipo.equals("NAL")) {
			indiceColumna++;
			XSSFCell titulo_vtipo = fila_titulo.createCell(indiceColumna);
			titulo_vtipo.setCellValue(msj.getString("reportes.proyectos." + tipo));
			titulo_vtipo.setCellStyle(estiloTitulo);
		}

		indiceColumna++;
		XSSFCell titulo_agregacion = fila_titulo.createCell(indiceColumna);
		titulo_agregacion.setCellValue(msj.getString("reportes.proyectos.TENENCIA"));
		titulo_agregacion.setCellStyle(estiloTitulo);

		DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();

		for (int i = 0; i < conteoProyectosTENENCIA.size(); i++) {
			conteoProyecto = conteoProyectosTENENCIA.get(i);
			conteo = conteoProyecto.getConteo();
			XSSFRow fila = hojaTENENCIA.createRow(i + 1);

			indiceColumna = 0;

			XSSFCell celda_conteo = fila.createCell(0);
			celda_conteo.setCellValue(conteo);
			celda_conteo.setCellStyle(estiloDato);

			if (!tipo.equals("NAL")) {
				indiceColumna++;
				vtipo = aux.nz(conteoProyecto.getDimension1(), "");
				XSSFCell celda_vtipo = fila.createCell(indiceColumna);
				celda_vtipo.setCellValue(vtipo);
				celda_vtipo.setCellStyle(estiloSerie);
			}

			indiceColumna++;
			vagregacion = aux.nz(conteoProyecto.getDimension2(), "");
			XSSFCell celda_vagregacion = fila.createCell(indiceColumna);
			celda_vagregacion.setCellValue(vagregacion);
			celda_vagregacion.setCellStyle(estiloSerie);
			
			if (!tipo.equals("NAL")) {
				defaultCategoryDataset.addValue(conteo, vtipo + " - " + vagregacion, str_titulo_conteo);
			}
			else {
				defaultCategoryDataset.addValue(conteo, vtipo, str_titulo_conteo);
			}
		}

		hojaTENENCIA.autoSizeColumn(0);
		hojaTENENCIA.autoSizeColumn(1);
		hojaTENENCIA.autoSizeColumn(2);
		hojaTENENCIA.autoSizeColumn(3);

		insertarImagen(createChartBarras(str_title, str_titulo_categoria, str_titulo_conteo, defaultCategoryDataset), 4, hojaTENENCIA);

	}

	private void generarHojaACTIVIDAD() {
		conteoProyectosACTIVIDAD = ConsultaProyecto.contarProyectos(tipo, "ACTIVIDAD", desde, hasta);
		ConteoProyecto conteoProyecto = new ConteoProyecto();
		int conteo = 0;
		String vtipo = "";
		String vagregacion = "";
		String str_title = msj.getString("reportes.proyectos.descripcion_ACTIVIDAD");
		String str_titulo_conteo = msj.getString("conteo");
		String str_titulo_tipo = msj.getString("reportes.proyectos." + tipo);
		String str_titulo_agregacion = msj.getString("reportes.proyectos.ACTIVIDAD");
		String str_titulo_categoria = str_titulo_tipo + " - " + str_titulo_agregacion;

		XSSFRow fila_titulo = hojaACTIVIDAD.createRow(0);

		indiceColumna = 0;

		XSSFCell titulo_conteo = fila_titulo.createCell(indiceColumna);
		titulo_conteo.setCellValue(msj.getString("conteo"));
		titulo_conteo.setCellStyle(estiloTitulo);

		if (!tipo.equals("NAL")) {
			indiceColumna++;
			XSSFCell titulo_vtipo = fila_titulo.createCell(indiceColumna);
			titulo_vtipo.setCellValue(msj.getString("reportes.proyectos." + tipo));
			titulo_vtipo.setCellStyle(estiloTitulo);
		}

		indiceColumna++;
		XSSFCell titulo_agregacion = fila_titulo.createCell(indiceColumna);
		titulo_agregacion.setCellValue(msj.getString("reportes.proyectos.ACTIVIDAD"));
		titulo_agregacion.setCellStyle(estiloTitulo);

		DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();

		for (int i = 0; i < conteoProyectosACTIVIDAD.size(); i++) {
			conteoProyecto = conteoProyectosACTIVIDAD.get(i);
			conteo = conteoProyecto.getConteo();
			XSSFRow fila = hojaACTIVIDAD.createRow(i + 1);

			indiceColumna = 0;

			XSSFCell celda_conteo = fila.createCell(0);
			celda_conteo.setCellValue(conteo);
			celda_conteo.setCellStyle(estiloDato);

			if (!tipo.equals("NAL")) {
				indiceColumna++;
				vtipo = aux.nz(conteoProyecto.getDimension1(), "");
				XSSFCell celda_vtipo = fila.createCell(indiceColumna);
				celda_vtipo.setCellValue(vtipo);
				celda_vtipo.setCellStyle(estiloSerie);
			}

			indiceColumna++;
			vagregacion = aux.nz(conteoProyecto.getDimension2(), "");
			XSSFCell celda_vagregacion = fila.createCell(indiceColumna);
			celda_vagregacion.setCellValue(vagregacion);
			celda_vagregacion.setCellStyle(estiloSerie);

			if (!tipo.equals("NAL")) {
				defaultCategoryDataset.addValue(conteo, vtipo + " - " + vagregacion, str_titulo_conteo);
			}
			else {
				defaultCategoryDataset.addValue(conteo, vtipo, str_titulo_conteo);
			}
		}

		hojaACTIVIDAD.autoSizeColumn(0);
		hojaACTIVIDAD.autoSizeColumn(1);
		hojaACTIVIDAD.autoSizeColumn(2);
		hojaACTIVIDAD.autoSizeColumn(3);

		insertarImagen(createChartBarras(str_title, str_titulo_categoria, str_titulo_conteo, defaultCategoryDataset), 4, hojaACTIVIDAD);

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

	public static XSSFCellStyle estiloSerieReporte(XSSFWorkbook libro) {

		XSSFCellStyle estiloCelda = libro.createCellStyle();
		estiloCelda.setWrapText(true);
		estiloCelda.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		estiloCelda.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeightInPoints((short) 10);
		fuente.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		fuente.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		return estiloCelda;
	}

	public static XSSFCellStyle estiloDatoReporte(XSSFWorkbook libro) {

		XSSFCellStyle estiloCelda = libro.createCellStyle();
		estiloCelda.setWrapText(true);
		estiloCelda.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		estiloCelda.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		XSSFFont fuente = libro.createFont();
		fuente.setFontHeightInPoints((short) 10);
		fuente.setFontName(XSSFFont.DEFAULT_FONT_NAME);
		fuente.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		return estiloCelda;
	}

	private void crearLibro() {

		libro = new XSSFWorkbook();
		hojaTOTAL = libro.createSheet();
		hojaESTADO = libro.createSheet();
		hojaTENENCIA = libro.createSheet();
		hojaACTIVIDAD = libro.createSheet();
	}

	public String getNombreReporte() {

		return nombreReporte;
	}

	public void setNombreReporte(String nombreReporte) {

		this.nombreReporte = nombreReporte;
	}

	private void insertarImagen(JFreeChart chart, int columna, XSSFSheet hoja) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
		BufferedImage image = chart.createBufferedImage(800, 600);

		try {
			ImageIO.write(image, "png", baos);
		}
		catch (IOException e) {
			System.out.println("Error de escritura");
		}

		byte[] b = baos.toByteArray();
		pictureIdx = libro.addPicture(b, XSSFWorkbook.PICTURE_TYPE_PNG);

		drawing = hoja.createDrawingPatriarch();

		CreationHelper helper = libro.getCreationHelper();
		ClientAnchor anchor = helper.createClientAnchor();

		anchor.setCol1(columna);
		anchor.setRow1(2);

		Picture pict = drawing.createPicture(anchor, pictureIdx);

		pict.resize();

	}

	public JFreeChart createChartBarras(String title, String categoryAxisLabel, String valueAxisLabel, final CategoryDataset dataset) {

		JFreeChart chart = null;
		if (dataset != null) {
			chart = ChartFactory.createBarChart3D(title, categoryAxisLabel, valueAxisLabel, dataset, PlotOrientation.HORIZONTAL, true, true, false);
		}
		return chart;
	}

	
}
