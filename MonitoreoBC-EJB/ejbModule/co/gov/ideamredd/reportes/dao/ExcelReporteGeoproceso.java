// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

import java.awt.Color;
import java.awt.SystemColor;
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
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import co.gov.ideamredd.lenguaje.LenguajeI18N;
import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.proyecto.entities.ConteoProyecto;
import co.gov.ideamredd.proyecto.dao.ConsultaProyecto;
import co.gov.ideamredd.reportes.entities.AgregadoReporte;
import co.gov.ideamredd.reportes.entities.InformacionReporteBiomasa;

/**
 * Clase usada para generar el reporte xls de proyectos.
 */
public class ExcelReporteGeoproceso {

	private XSSFWorkbook	libro;
	private XSSFCellStyle	estiloTitulo, estiloSerie, estiloDato;
	XSSFSheet				hojaTABLA, hojaGRAFICOS;
	ResourceBundle			msj;
	Auxiliar				aux			= new Auxiliar();
	private int				pictureIdx	= 0;
	private XSSFDrawing		drawing;

	/**
	 * Construye el libro xls del reporte
	 * 
	 * @param id_reporte
	 * @param idioma
	 * @return libro del reporte en xls
	 */
	public XSSFWorkbook construirLibroReporte(String id_reporte, String idioma) {

		LenguajeI18N i18n = new LenguajeI18N();
		i18n.setLenguaje("es");
		i18n.setPais("CO");
		if (idioma.toLowerCase().equals("en")) {
			i18n.setLenguaje("en");
			i18n.setPais("US");
		}
		msj = i18n.obtenerMensajeIdioma();

		crearLibro();
		estiloTitulo = estiloTituloReporte(libro);
		estiloSerie = estiloSerieReporte(libro);
		estiloDato = estiloDatoReporte(libro);

		libro.setSheetName(0, "DATOS");
		libro.setSheetName(1, "GRAFICOS");
		generarhojaTABLA(id_reporte);

		return libro;
	}

	/**
	 * Genera la hoja de la tabla de datos
	 * 
	 * @param id_reporte
	 */
	private void generarhojaTABLA(String id_reporte) {
		// OBTENER EL ARRAYLIST DE DATOS DEL REPORTE
		ArrayList<AgregadoReporte> agregadosReporte;
		agregadosReporte = ConsultaReportes.consolidarReporte(id_reporte);
		AgregadoReporte agregadoReporte = new AgregadoReporte();

		String periodouno = "";
		String periododos = "";
		String idDivisionTerritorio = "";
		String divisionTerritorio = "";
		String divisionTerritorial = "";
		String clasificacion = "";
		Double areaha = 0d;
		Double incertidumbre = 0d;
		Double error_estandar = 0d;
		Double baj = 0d;
		Double bat = 0d;
		Double caj = 0d;
		Double cat = 0d;
		Double coe = 0d;
		Double coet = 0d;

		idDivisionTerritorio = ConsultaReportes.idDivisionTerritorial(id_reporte);
		divisionTerritorio = ConsultaReportes.nombreDivisionTerritorial(idDivisionTerritorio, "es");
		String id_tiporeporte = ConsultaReportes.idTipoReporte(id_reporte);

		long area = 0;
		String vtipo = "";
		String str_titulo_division_territorial = divisionTerritorio;
		String str_titulo_periodouno = msj.getString("periodouno");
		String str_titulo_periododos = msj.getString("periododos");
		String str_titulo_clasificacion = msj.getString("clasificacion");
		String str_titulo_areaha = msj.getString("areaha");
		String str_titulo_incertidumbre = msj.getString("incertidumbre");
		String str_titulo_error_estandar = msj.getString("error_estandar");
		String str_titulo_baj = msj.getString("baj");
		String str_titulo_bat = msj.getString("bat");
		String str_titulo_caj = msj.getString("caj");
		String str_titulo_cat = msj.getString("cat");
		String str_titulo_coe = msj.getString("coe");
		String str_titulo_coet = msj.getString("coet");

		XSSFRow fila_titulo = hojaTABLA.createRow(0);

		XSSFCell titulo_division_territorial = fila_titulo.createCell(0);
		titulo_division_territorial.setCellValue(str_titulo_division_territorial);
		titulo_division_territorial.setCellStyle(estiloTitulo);
		XSSFCell titulo_clasificacion = fila_titulo.createCell(1);
		titulo_clasificacion.setCellValue(str_titulo_clasificacion);
		titulo_clasificacion.setCellStyle(estiloTitulo);
		XSSFCell titulo_areaha = fila_titulo.createCell(2);
		titulo_areaha.setCellValue(str_titulo_areaha);
		titulo_areaha.setCellStyle(estiloTitulo);

		if (id_tiporeporte.equals("7")) {
			XSSFCell titulo_incertidumbre = fila_titulo.createCell(3);
			titulo_incertidumbre.setCellValue(str_titulo_incertidumbre);
			titulo_incertidumbre.setCellStyle(estiloTitulo);
			XSSFCell titulo_error_estandar = fila_titulo.createCell(4);
			titulo_error_estandar.setCellValue(str_titulo_error_estandar);
			titulo_error_estandar.setCellStyle(estiloTitulo);
			XSSFCell titulo_baj = fila_titulo.createCell(5);
			titulo_baj.setCellValue(str_titulo_baj);
			titulo_baj.setCellStyle(estiloTitulo);
			XSSFCell titulo_bat = fila_titulo.createCell(6);
			titulo_bat.setCellValue(str_titulo_bat);
			titulo_bat.setCellStyle(estiloTitulo);
			XSSFCell titulo_caj = fila_titulo.createCell(7);
			titulo_caj.setCellValue(str_titulo_caj);
			titulo_caj.setCellStyle(estiloTitulo);
			XSSFCell titulo_cat = fila_titulo.createCell(8);
			titulo_cat.setCellValue(str_titulo_cat);
			titulo_cat.setCellStyle(estiloTitulo);
			XSSFCell titulo_coe = fila_titulo.createCell(9);
			titulo_coe.setCellValue(str_titulo_coe);
			titulo_coe.setCellStyle(estiloTitulo);
			XSSFCell titulo_coet = fila_titulo.createCell(10);
			titulo_coet.setCellValue(str_titulo_coet);
			titulo_coet.setCellStyle(estiloTitulo);
		}

		DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();

		for (int i = 0; i < agregadosReporte.size(); i++) {
			agregadoReporte = agregadosReporte.get(i);
			divisionTerritorial = agregadoReporte.getDivisionTerritorial();
			clasificacion = agregadoReporte.getClasificacion();
			areaha = agregadoReporte.getArea();
			incertidumbre = agregadoReporte.getIncertidumbre();
			error_estandar = agregadoReporte.getError();
			baj = agregadoReporte.getBaj();
			bat = agregadoReporte.getBat();
			caj = agregadoReporte.getCaj();
			cat = agregadoReporte.getCat();
			coe = agregadoReporte.getCoe();
			coet = agregadoReporte.getCoet();

			XSSFRow fila = hojaTABLA.createRow(i + 1);

			XSSFCell celda_division_territorial = fila.createCell(0);
			celda_division_territorial.setCellValue(divisionTerritorial);
			celda_division_territorial.setCellStyle(estiloDato);
			XSSFCell celda_clasificacion = fila.createCell(1);
			celda_clasificacion.setCellValue(clasificacion);
			celda_clasificacion.setCellStyle(estiloDato);
			XSSFCell celda_areaha = fila.createCell(2);
			celda_areaha.setCellValue(areaha);
			celda_areaha.setCellStyle(estiloDato);

			if (id_tiporeporte.equals("7")) {
				XSSFCell celda_incertidumbre = fila.createCell(3);
				celda_incertidumbre.setCellValue(incertidumbre);
				celda_incertidumbre.setCellStyle(estiloDato);
				XSSFCell celda_error_estandar = fila.createCell(4);
				celda_error_estandar.setCellValue(error_estandar);
				celda_error_estandar.setCellStyle(estiloDato);
				XSSFCell celda_baj = fila.createCell(5);
				celda_baj.setCellValue(baj);
				celda_baj.setCellStyle(estiloDato);
				XSSFCell celda_bat = fila.createCell(6);
				celda_bat.setCellValue(bat);
				celda_bat.setCellStyle(estiloDato);
				XSSFCell celda_caj = fila.createCell(7);
				celda_caj.setCellValue(caj);
				celda_caj.setCellStyle(estiloDato);
				XSSFCell celda_cat = fila.createCell(8);
				celda_cat.setCellValue(cat);
				celda_cat.setCellStyle(estiloDato);
				XSSFCell celda_coe = fila.createCell(9);
				celda_coe.setCellValue(coe);
				celda_coe.setCellStyle(estiloDato);
				XSSFCell celda_coet = fila.createCell(10);
				celda_coet.setCellValue(coet);
				celda_coet.setCellStyle(estiloDato);
			}

			defaultCategoryDataset.addValue(areaha, clasificacion, divisionTerritorial);
		}

		hojaTABLA.autoSizeColumn(0);
		hojaTABLA.autoSizeColumn(1);
		hojaTABLA.autoSizeColumn(2);
		hojaTABLA.autoSizeColumn(3);
		hojaTABLA.autoSizeColumn(4);
		hojaTABLA.autoSizeColumn(5);
		hojaTABLA.autoSizeColumn(6);
		hojaTABLA.autoSizeColumn(7);
		hojaTABLA.autoSizeColumn(8);
		hojaTABLA.autoSizeColumn(9);

		insertarImagen(createChartBarras(msj.getString("areaha"), str_titulo_division_territorial, str_titulo_areaha, defaultCategoryDataset, id_tiporeporte), 1, hojaGRAFICOS);

	}

	/**
	 * Define el estilo del título
	 * 
	 * @param libro
	 * @return estilo de título
	 */
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

	/**
	 * Define el estilo de las celdas de serie
	 * 
	 * @param libro
	 * @return estilo de las celdas de serie
	 */
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

	/**
	 * Define el estilo de las celdas de datos
	 * 
	 * @param libro
	 * @return estilo de las celdas de datos
	 */
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

	/**
	 * Crea el libro
	 */
	private void crearLibro() {
		libro = new XSSFWorkbook();
		hojaTABLA = libro.createSheet();
		hojaGRAFICOS = libro.createSheet();
	}

	/**
	 * Inserta la imagen del gráfico en la hoja
	 * 
	 * @param chart
	 * @param columna
	 * @param hoja
	 */
	private void insertarImagen(JFreeChart chart, int columna, XSSFSheet hoja) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
		BufferedImage image = chart.createBufferedImage(800, 1200);

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

	/**
	 * Genera el gráfico de barras
	 * 
	 * @param title
	 * @param categoryAxisLabel
	 * @param valueAxisLabel
	 * @param dataset
	 * @param id_tiporeporte
	 * @return gráfico de barras
	 */
	public JFreeChart createChartBarras(String title, String categoryAxisLabel, String valueAxisLabel, final CategoryDataset dataset, String id_tiporeporte) {

		JFreeChart chart = null;
		if (dataset != null) {
			chart = ChartFactory.createBarChart3D(title, categoryAxisLabel, valueAxisLabel, dataset, PlotOrientation.HORIZONTAL, true, true, false);

			if (id_tiporeporte.equals("1") || id_tiporeporte.equals("3")) {
				CategoryPlot cplot = (CategoryPlot) chart.getPlot();

				// CAMBIAR EL COLOR DEL FONDO
				cplot.setBackgroundPaint(SystemColor.inactiveCaption);

				// CAMBIAR EL COLOR DE LAS BARRAS
				((BarRenderer) cplot.getRenderer()).setBarPainter(new StandardBarPainter());

				BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();
				if (id_tiporeporte.equals("1")) {
					r.setSeriesPaint(0, Color.decode("#3D8944"));
					r.setSeriesPaint(1, Color.decode("#F4F4D8"));
					r.setSeriesPaint(2, Color.decode("#FFA500"));
				}
				if (id_tiporeporte.equals("3")) {
					r.setSeriesPaint(0, Color.decode("#3D8944"));
					r.setSeriesPaint(1, Color.decode("#FF0000"));
					r.setSeriesPaint(2, Color.decode("#880000"));
					r.setSeriesPaint(3, Color.decode("#F4F4D8"));
					r.setSeriesPaint(4, Color.decode("#0000FF"));
					r.setSeriesPaint(5, Color.decode("#FFA500"));
				}
			}
		}
		return chart;
	}

}
