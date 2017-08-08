package co.gov.ideamredd.reportes;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
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
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import co.gov.ideamredd.conexionBD.Parametro;
import co.gov.ideamredd.entities.InformacionReporteBiomasa;

@Stateless
public class ConsultarReporteBiomasa {

	@EJB
	private CrearGraficosBiomasa graficos;

	@EJB
	private ConsultarAsociadosReporte car;

	@EJB
	private Parametro parametro;

	private XSSFWorkbook libro;
	private XSSFDrawing drawing;
	private int pictureIdx = 0;
	private static final String archivoReportes = "co/gov/ideamredd/reportes/reportesWPS";
	private static final ResourceBundle infoReportes = ResourceBundle
			.getBundle(archivoReportes);
	private CrearLibroReporteBiomasa libroReporte;
	private String rutaArchivo;
	private String Periodo;
	private String[] nombresAreasHidrograficas = {
			infoReportes.getString("nombreHidro2"),
			infoReportes.getString("nombreHidro3"),
			infoReportes.getString("nombreHidro4"),
			infoReportes.getString("nombreHidro1"),
			infoReportes.getString("nombreHidro5") };
	private String[] nombresTiposBosque = {
			infoReportes.getString("tp.biomasa1"),
			infoReportes.getString("tp.biomasa2"),
			infoReportes.getString("tp.biomasa3"),
			infoReportes.getString("tp.biomasa4"),
			infoReportes.getString("tp.biomasa5"),
			infoReportes.getString("tp.biomasa6"),
			infoReportes.getString("tp.biomasa7"),
			infoReportes.getString("tp.biomasa8"),
			infoReportes.getString("tp.biomasa9"),
			infoReportes.getString("tp.biomasa10"),
			infoReportes.getString("tp.biomasa11"),
			infoReportes.getString("tp.biomasa12"),
			infoReportes.getString("tp.biomasa13"),
			infoReportes.getString("tp.biomasa14"),
			infoReportes.getString("tp.biomasa15") };

	// Datos para graficas en la pagina web
	// Arreglo de 3 dimensiones para los datos de los tipos de bosque de
	// Cada Area hidrografica.
	private ArrayList<BigDecimal[][]> webAreasHidrograficas;
	private ArrayList<BigDecimal[]> webConsolidadoNacional;

	public void consultarReporte(Integer tipoReporte, String[] periodo,
			Integer[] idReporte) {
		libroReporte = new CrearLibroReporteBiomasa();
		libro = libroReporte.construirLibroReporte(tipoReporte, periodo);
		File reporte = null;
		Periodo = periodo[0];

		webAreasHidrograficas = new ArrayList<BigDecimal[][]>();
		for (int i = 0; i < 5; i++) {
			webAreasHidrograficas.add(new BigDecimal[15][2]);
		}

		webConsolidadoNacional = new ArrayList<BigDecimal[]>();
		for (int i = 0; i < 15; i++) {
			webConsolidadoNacional.add(new BigDecimal[2]);
		}

		if (tipoReporte == 8) {

			reporte = new File(parametro.getParametro("ruta_reportes")
					+ libroReporte.getNombreReporte() + periodo[0] + ".xlsx");
			Integer numAreasHidro = Integer.valueOf(infoReportes
					.getString("numeroAreasHidrograficas"));

			// for (int j = 0; j < numAreasHidro; j++) {
			graficos.llenarDatosGrafWeb(periodo, idReporte[0], "Biomasa",
					webAreasHidrograficas);
			// graficos.llenarDatosGrafWeb(periodo, idReporte[0],
			// "Carbono", webAreasHidrograficas);
			// }

			if (!reporte.exists()) {
				poblarReporte(idReporte, tipoReporte);

				ArrayList<DefaultCategoryDataset> dataBio = new ArrayList<DefaultCategoryDataset>();
				ArrayList<DefaultCategoryDataset> dataCarbono = new ArrayList<DefaultCategoryDataset>();
				ArrayList<DefaultPieDataset> dataPieBio = new ArrayList<DefaultPieDataset>();
				ArrayList<DefaultPieDataset> dataPieCarbono = new ArrayList<DefaultPieDataset>();
				ArrayList<String> periodosAux = new ArrayList<String>();

				for (int j = 0; j < numAreasHidro; j++) {
					dataBio.add(graficos.createBarDataset(periodo,
							idReporte[0], j, "Biomasa"));
					dataCarbono.add(graficos.createBarDataset(periodo,
							idReporte[0], j, "Carbono"));
					dataPieBio.add(graficos.createPieDataset(periodo,
							idReporte[0], j, "Biomasa"));
					dataPieCarbono.add(graficos.createPieDataset(periodo,
							idReporte[0], j, "Carbono"));
					periodosAux.add(periodo[0]);
				}

				for (int j = 0; j < dataBio.size(); j++) {
					insertarImagen(graficos.createChartBarras(dataBio.get(j),
							1, null, periodosAux.get(0), j), j, 2);
					insertarImagen(
							graficos.createChartBarras(dataCarbono.get(j), 0,
									null, periodosAux.get(0), j), j, 15);
				}
				for (int j = 0; j < dataPieBio.size(); j++) {
					insertarImagenPie(graficos.createChartPie(
							dataPieBio.get(j), 1, null, periodosAux.get(0), j),
							j, 2);
					insertarImagenPie(graficos.createChartPie(
							dataPieCarbono.get(j), 0, null, periodosAux.get(0),
							j), j, 15);
				}
				escribirReportes(parametro.getParametro("ruta_reportes"),
						libroReporte.getNombreReporte() + periodo[0], libro);
			}
		} else {
			reporte = new File(parametro.getParametro("ruta_reportes")
					+ "Consolidado" + libroReporte.getNombreReporte()
					+ periodo[0] + ".xlsx");
			ArrayList<InformacionReporteBiomasa> datosBio = poblarReporteConsolidado(
					idReporte, tipoReporte);

			graficos.createWebConsolidado(datosBio, periodo, idReporte[0],
					"Biomasa", webConsolidadoNacional);
			graficos.createWebConsolidado(datosBio, periodo, idReporte[0],
					"Carbono", webConsolidadoNacional);

			if (!reporte.exists()) {
				ArrayList<DefaultCategoryDataset> dataBio = new ArrayList<DefaultCategoryDataset>();
				ArrayList<DefaultCategoryDataset> dataCarbono = new ArrayList<DefaultCategoryDataset>();
				ArrayList<DefaultPieDataset> dataPieBio = new ArrayList<DefaultPieDataset>();
				ArrayList<DefaultPieDataset> dataPieCarbono = new ArrayList<DefaultPieDataset>();
				ArrayList<String> periodosAux = new ArrayList<String>();

				dataBio.add(graficos.createBarDatasetConsolidado(datosBio,
						periodo, idReporte[0], "Biomasa"));
				dataCarbono.add(graficos.createBarDatasetConsolidado(datosBio,
						periodo, idReporte[0], "Carbono"));
				dataPieBio.add(graficos.createPieDatasetConsolidado(datosBio,
						periodo, idReporte[0], "Biomasa"));
				dataPieCarbono.add(graficos.createPieDatasetConsolidado(
						datosBio, periodo, idReporte[0], "Carbono"));
				periodosAux.add(periodo[0]);

				insertarImagen(graficos.createChartBarrasConsolidado(
						dataBio.get(0), 1, null, periodosAux.get(0)), 0, 2);
				insertarImagen(graficos.createChartBarrasConsolidado(
						dataCarbono.get(0), 0, null, periodosAux.get(0)), 0, 15);
				insertarImagenPie(graficos.createChartPieConsolidado(
						dataPieBio.get(0), 1, null, periodosAux.get(0)), 0, 2);
				insertarImagenPie(
						graficos.createChartPieConsolidado(
								dataPieCarbono.get(0), 0, null,
								periodosAux.get(0)), 0, 15);

				escribirReportes(parametro.getParametro("ruta_reportes"),
						"Consolidado" + libroReporte.getNombreReporte()
								+ periodo[0], libro);
			}
		}

		rutaArchivo = reporte.getAbsolutePath();
	}

	private void poblarReporte(Integer[] idReporte, Integer tipoReporte) {
		XSSFSheet hoja = libro.getSheetAt(0);
		int numeroFila = 3;
		int numeroColumnaN;
		int numeroColumna = 1;
		for (int i = 0; i < idReporte.length; i++) {
			ArrayList<InformacionReporteBiomasa> info = car
					.consultarInfoBiomasa(idReporte[i]);
			XSSFRow fila;
			numeroColumnaN = numeroColumna;

			for (int j = 0; j < info.size(); j++) {
				if (hoja.getRow(numeroFila) == null)
					fila = hoja.createRow(numeroFila);
				else
					fila = hoja.getRow(numeroFila);
				InformacionReporteBiomasa bio = info.get(j);
				if (i == 0)
					numeroColumna = 1;
				else
					numeroColumna = numeroColumnaN;
				insertarDato(fila, numeroColumna, bio.getNombreTipoBosque());
				numeroColumna++;
				if (tipoReporte == 8) {
					insertarDato(
							fila,
							numeroColumna,
							infoReportes.getString("nombreHidro"
									+ getRealAreaHidro(bio
											.getAreaHidrografica())));
					numeroColumna++;
				}
				insertarDato(fila, numeroColumna, bio.getArea().toString());
				numeroColumna++;
				insertarDato(fila, numeroColumna, bio.getBiomasa().toString());
				numeroColumna++;
				insertarDato(fila, numeroColumna, bio.getBA().toString());
				numeroColumna++;
				insertarDato(fila, numeroColumna, bio.getCarbono().toString());
				numeroColumna++;
				insertarDato(fila, numeroColumna, bio.getC().toString());
				numeroColumna++;
				insertarDato(fila, numeroColumna, bio.getCO2().toString());
				numeroColumna++;
				numeroFila++;
			}
		}
	}

	private ArrayList<InformacionReporteBiomasa> poblarReporteConsolidado(
			Integer[] idReporte, Integer tipoReporte) {
		ArrayList<InformacionReporteBiomasa> bioFinal;
		XSSFSheet hoja = libro.getSheetAt(0);
		int numeroFila = 3;
		int numeroColumnaN;
		int numeroColumna = 1;
		ArrayList<InformacionReporteBiomasa> i = new ArrayList<InformacionReporteBiomasa>();
		InformacionReporteBiomasa b1 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b2 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b3 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b4 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b5 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b6 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b7 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b8 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b9 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b10 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b11 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b12 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b13 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b14 = new InformacionReporteBiomasa();
		InformacionReporteBiomasa b15 = new InformacionReporteBiomasa();
		// InformacionReporteBiomasa b16 = new InformacionReporteBiomasa();
		ArrayList<InformacionReporteBiomasa> info = car
				.consultarInfoBiomasa(idReporte[0]);
		for (int j = 0; j < info.size(); j = j + 15) {
			InformacionReporteBiomasa bio1 = info.get(j);
			InformacionReporteBiomasa bio2 = info.get(j + 1);
			InformacionReporteBiomasa bio3 = info.get(j + 2);
			InformacionReporteBiomasa bio4 = info.get(j + 3);
			InformacionReporteBiomasa bio5 = info.get(j + 4);
			InformacionReporteBiomasa bio6 = info.get(j + 5);
			InformacionReporteBiomasa bio7 = info.get(j + 6);
			InformacionReporteBiomasa bio8 = info.get(j + 7);
			InformacionReporteBiomasa bio9 = info.get(j + 8);
			InformacionReporteBiomasa bio10 = info.get(j + 9);
			InformacionReporteBiomasa bio11 = info.get(j + 10);
			InformacionReporteBiomasa bio12 = info.get(j + 11);
			InformacionReporteBiomasa bio13 = info.get(j + 12);
			InformacionReporteBiomasa bio14 = info.get(j + 13);
			InformacionReporteBiomasa bio15 = info.get(j + 14);
			// InformacionReporteBiomasa bio16 = info.get(j + 15);
			sumarValor(b1, bio1);
			sumarValor(b2, bio2);
			sumarValor(b3, bio3);
			sumarValor(b4, bio4);
			sumarValor(b5, bio5);
			sumarValor(b6, bio6);
			sumarValor(b7, bio7);
			sumarValor(b8, bio8);
			sumarValor(b9, bio9);
			sumarValor(b10, bio10);
			sumarValor(b11, bio11);
			sumarValor(b12, bio12);
			sumarValor(b13, bio13);
			sumarValor(b14, bio14);
			sumarValor(b15, bio15);
			// sumarValor(b16, bio16);

		}
		i.add(b1);
		i.add(b2);
		i.add(b3);
		i.add(b4);
		i.add(b5);
		i.add(b6);
		i.add(b7);
		i.add(b8);
		i.add(b9);
		i.add(b10);
		i.add(b11);
		i.add(b12);
		i.add(b13);
		i.add(b14);
		i.add(b15);
		bioFinal = i;
		// i.add(b16);
		for (int k = 0; k < i.size(); k++) {
			XSSFRow fila;
			numeroColumnaN = numeroColumna;
			if (hoja.getRow(numeroFila) == null)
				fila = hoja.createRow(numeroFila);
			else
				fila = hoja.getRow(numeroFila);
			InformacionReporteBiomasa bio = i.get(k);
			if (k == 0)
				numeroColumna = 1;
			else
				numeroColumna = numeroColumnaN;
			if (numeroColumna >= 8) {
				numeroColumna = 1;
			}
			insertarDato(fila, numeroColumna, bio.getNombreTipoBosque());
			numeroColumna++;
			insertarDato(fila, numeroColumna, bio.getArea().toString());
			numeroColumna++;
			insertarDato(fila, numeroColumna, bio.getBiomasa().toString());
			numeroColumna++;
			insertarDato(fila, numeroColumna, bio.getBA().toString());
			numeroColumna++;
			insertarDato(fila, numeroColumna, bio.getCarbono().toString());
			numeroColumna++;
			insertarDato(fila, numeroColumna, bio.getC().toString());
			numeroColumna++;
			insertarDato(fila, numeroColumna, bio.getCO2().toString());
			numeroColumna++;
			numeroFila++;
		}
		return bioFinal;
	}

	private void insertarImagen(JFreeChart chart, int grafica, int columna) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
		BufferedImage image = chart.createBufferedImage(800, 600);
		try {
			ImageIO.write(image, "png", baos);
		} catch (IOException e) {
			System.out.println("Error de escritura");
		}
		byte[] b = baos.toByteArray();
		pictureIdx = libro.addPicture(b, XSSFWorkbook.PICTURE_TYPE_PNG);
		XSSFSheet hoja2;
		if (libro.getNumberOfSheets() < 2) {
			hoja2 = libro.createSheet();
			drawing = hoja2.createDrawingPatriarch();
		} else {
			hoja2 = libro.getSheetAt(1);
		}
		CreationHelper helper = libro.getCreationHelper();
		ClientAnchor anchor = helper.createClientAnchor();
		libro.setSheetName(1, "Graficos Barra");

		anchor.setCol1(columna);
		if (grafica == 0)
			anchor.setRow1(2);
		else if (grafica == 1)
			anchor.setRow1(40);
		else if (grafica == 2)
			anchor.setRow1(78);
		else if (grafica == 3)
			anchor.setRow1(116);
		else if (grafica == 4)
			anchor.setRow1(154);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();
	}

	private void insertarImagenPie(JFreeChart chart, int grafica, int columna) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
		BufferedImage image = chart.createBufferedImage(800, 600);
		try {
			ImageIO.write(image, "png", baos);
		} catch (IOException e) {
			System.out.println("Error de escritura");
		}
		byte[] b = baos.toByteArray();
		pictureIdx = libro.addPicture(b, XSSFWorkbook.PICTURE_TYPE_PNG);
		XSSFSheet hoja3;
		if (libro.getNumberOfSheets() < 3) {
			hoja3 = libro.createSheet();
			drawing = hoja3.createDrawingPatriarch();
		} else {
			hoja3 = libro.getSheetAt(1);
		}
		CreationHelper helper = libro.getCreationHelper();
		ClientAnchor anchor = helper.createClientAnchor();
		libro.setSheetName(2, "Graficos Torta");

		anchor.setCol1(columna);
		if (grafica == 0)
			anchor.setRow1(2);
		else if (grafica == 1)
			anchor.setRow1(40);
		else if (grafica == 2)
			anchor.setRow1(78);
		else if (grafica == 3)
			anchor.setRow1(116);
		else if (grafica == 4)
			anchor.setRow1(154);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();
	}

	private void sumarValor(InformacionReporteBiomasa b,
			InformacionReporteBiomasa b1) {
		if (b.getArea() == null) {
			b.setArea(b1.getArea());
			b.setBA(b1.getBA());
			b.setNombreTipoBosque(b1.getNombreTipoBosque());
			b.setC(b1.getC());
			b.setCO2(b1.getCO2());
			b.setBiomasa(b1.getBiomasa());
			b.setCarbono(b1.getCarbono());
		} else {
			b.setArea(b.getArea() + b1.getArea());
			b.setBA(b.getBA().add(b1.getBA()));
			b.setC(b.getC().add(b1.getC()));
			b.setCO2(b.getCO2().add(b1.getCO2()));
			b.setBiomasa(b1.getBiomasa());
			b.setCarbono(b1.getCarbono());
			b.setNombreTipoBosque(b1.getNombreTipoBosque());
		}
	}

	private void insertarDato(XSSFRow fila, int indice, String info) {
		XSSFCell cell = fila.createCell((short) indice);
		cell.setCellValue(info);
	}

	public XSSFCellStyle estiloTituloReporte(XSSFWorkbook libro) {
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

	public boolean escribirReportes(String ruta, String nombreArchivo,
			XSSFWorkbook libro) {
		boolean esEscrito = true;
		try {
			String path = ruta + nombreArchivo + ".xlsx";
			FileOutputStream elFichero = new FileOutputStream(path);
			libro.write(elFichero);
			elFichero.close();
		} catch (Exception e) {
			e.printStackTrace();
			esEscrito = false;
		}
		return esEscrito;
	}

	public Integer getRealAreaHidro(Integer id) {
		Integer resultado = new Integer(-1);

		switch (id) {
		case 1:
			resultado = 2;
			break;
		case 2:
			resultado = 3;
			break;
		case 3:
			resultado = 4;
			break;
		case 4:
			resultado = 1;
			break;
		case 5:
			resultado = 5;
			break;
		}

		return resultado;
	}

	public ArrayList<BigDecimal[][]> getListaDatosWeb() {
		return webAreasHidrograficas;
	}

	public ArrayList<BigDecimal[]> getListaDatosConsolidadoWeb() {
		return webConsolidadoNacional;
	}

	public String[] getNombresAreasHidrog() {
		return nombresAreasHidrograficas;
	}

	public String[] getNombresTiposBosque() {
		return nombresTiposBosque;
	}

	public String getPeriodo() {
		return Periodo;
	}

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

}
