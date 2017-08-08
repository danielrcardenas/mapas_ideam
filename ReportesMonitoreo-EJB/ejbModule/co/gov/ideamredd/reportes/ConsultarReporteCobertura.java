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
import org.jfree.data.time.TimeSeriesCollection;

import co.gov.ideamredd.conexionBD.Parametro;
import co.gov.ideamredd.entities.InformacionReporteBosque;
import co.gov.ideamredd.entities.InformacionReporteCobertura;
import co.gov.ideamredd.util.Util;

@Stateless
public class ConsultarReporteCobertura {

	@EJB
	ConsultarAsociadosReporte car;
	@EJB
	CrearGraficosCobertura graficos;
	@EJB
	private Parametro parametro;

	private XSSFWorkbook libro;
	private static final String archivoReportes = "co/gov/ideamredd/reportes/reportesWPS";
	private static final ResourceBundle infoReportes = ResourceBundle
			.getBundle(archivoReportes);
	private int pictureIdx = 0;
	private XSSFDrawing drawing;
	private CrearLibroReporteCobertura libroReporte = new CrearLibroReporteCobertura();
	private ArrayList<Double> porcentajes = new ArrayList<Double>();
	private String rutaArchivo;
	private ArrayList<Double> porcentajesPie = new ArrayList<Double>();
	private ArrayList<Double> porcentajesTimeBE = new ArrayList<Double>();
	private ArrayList<Double> porcentajesTimeNBE = new ArrayList<Double>();
	private ArrayList<Double> porcentajesTimeDE = new ArrayList<Double>();
	private ArrayList<Double> porcentajesTimeRE = new ArrayList<Double>();
	private ArrayList<Double> porcentajesTimeSI = new ArrayList<Double>();
	private ArrayList<String> nombrePeriodos = new ArrayList<String>();
	private String[] Periodo;
	private String[] nombresCARs = { infoReportes.getString("nombreCar1"),
			infoReportes.getString("nombreCar2"),
			infoReportes.getString("nombreCar3"),
			infoReportes.getString("nombreCar4"),
			infoReportes.getString("nombreCar5"),
			infoReportes.getString("nombreCar6"),
			infoReportes.getString("nombreCar7"),
			infoReportes.getString("nombreCar8"),
			infoReportes.getString("nombreCar9"),
			infoReportes.getString("nombreCar10"),
			infoReportes.getString("nombreCar11"),
			infoReportes.getString("nombreCar12"),
			infoReportes.getString("nombreCar13"),
			infoReportes.getString("nombreCar14"),
			infoReportes.getString("nombreCar15"),
			infoReportes.getString("nombreCar16"),
			infoReportes.getString("nombreCar17"),
			infoReportes.getString("nombreCar18"),
			infoReportes.getString("nombreCar19"),
			infoReportes.getString("nombreCar20"),
			infoReportes.getString("nombreCar21"),
			infoReportes.getString("nombreCar22"),
			infoReportes.getString("nombreCar23"),
			infoReportes.getString("nombreCar24"),
			infoReportes.getString("nombreCar25"),
			infoReportes.getString("nombreCar26"),
			infoReportes.getString("nombreCar27"),
			infoReportes.getString("nombreCar28"),
			infoReportes.getString("nombreCar29"),
			infoReportes.getString("nombreCar30"),
			infoReportes.getString("nombreCar31"),
			infoReportes.getString("nombreCar32"),
			infoReportes.getString("nombreCar33"),
			infoReportes.getString("nombreCar34") };
	private String[] nombresDedapartamentos = {
			infoReportes.getString("nobreBosque1"),
			infoReportes.getString("nobreBosque2"),
			infoReportes.getString("nobreBosque3"),
			infoReportes.getString("nobreBosque4"),
			infoReportes.getString("nobreBosque5"),
			infoReportes.getString("nobreBosque6"),
			infoReportes.getString("nobreBosque7"),
			infoReportes.getString("nobreBosque8"),
			infoReportes.getString("nobreBosque9"),
			infoReportes.getString("nobreBosque10"),
			infoReportes.getString("nobreBosque11"),
			infoReportes.getString("nobreBosque12"),
			infoReportes.getString("nobreBosque13"),
			infoReportes.getString("nobreBosque14"),
			infoReportes.getString("nobreBosque15"),
			infoReportes.getString("nobreBosque16"),
			infoReportes.getString("nobreBosque17"),
			infoReportes.getString("nobreBosque18"),
			infoReportes.getString("nobreBosque19"),
			infoReportes.getString("nobreBosque20"),
			infoReportes.getString("nobreBosque21"),
			infoReportes.getString("nobreBosque22"),
			infoReportes.getString("nobreBosque23"),
			infoReportes.getString("nobreBosque24"),
			infoReportes.getString("nobreBosque25"),
			infoReportes.getString("nobreBosque26"),
			infoReportes.getString("nobreBosque27"),
			infoReportes.getString("nobreBosque28"),
			infoReportes.getString("nobreBosque29"),
			infoReportes.getString("nobreBosque30"),
			infoReportes.getString("nobreBosque31"),
			infoReportes.getString("nobreBosque32"),
			infoReportes.getString("nobreBosque33") };
	private String[] nombresAreasHidrograficas = {
			infoReportes.getString("nombreHidro1"),
			infoReportes.getString("nombreHidro2"),
			infoReportes.getString("nombreHidro3"),
			infoReportes.getString("nombreHidro4"),
			infoReportes.getString("nombreHidro5") };

	// Datos para graficas en la pagina web
	private ArrayList<Double[][][]> webDatosGraficas;// Periodo-->Division-->BosqueVar-->Valor
	private ArrayList<Double[][]> webConsolidadoNacional;// Periodo-->BosqueVar-->Valor

	public void consultarReporte(Integer territorio, String[] periodo,
			Integer[] idReporte) {
		libro = libroReporte.construirLibroReporte(territorio, periodo);
		File reporte = new File(parametro.getParametro("ruta_reportes")
				+ libroReporte.getNombreReporte() + ".xlsx");

		Integer indice = Integer.valueOf(infoReportes
				.getString("reporte.cobertura.cantidadGraficos"));

		Periodo = new String[periodo.length];
		for (int i = 0; i < periodo.length; i++) {
			Periodo[i] = periodo[i];
		}

		if (territorio != 4) {
			webDatosGraficas = new ArrayList<Double[][][]>();
			if (territorio == 1) {
				for (int i = 0; i < periodo.length; i++) {
					webDatosGraficas
							.add(new Double[Integer.valueOf(infoReportes
									.getString("cantidadCarSinPonderado"))][5][1]);
				}
			}
			if (territorio == 2) {
				for (int i = 0; i < periodo.length; i++) {
					webDatosGraficas
							.add(new Double[Integer.valueOf(infoReportes
									.getString("CantidadBosqueSinPonderado"))][5][1]);
				}
			}
			if (territorio == 3) {
				for (int i = 0; i < periodo.length; i++) {
					webDatosGraficas
							.add(new Double[Integer.valueOf(infoReportes
									.getString("numeroAreasHidrograficas"))][5][1]);
				}
			}

			for (int i = 0; i < periodo.length; i++) {
				for (int j = 0; j < indice; j++) {
					graficos.llenarDatosGrafWeb(periodo, idReporte[i], i, j,
							webDatosGraficas);
				}
			}

		} else {
			poblarReporteWebConsolidado(idReporte, periodo);
			webConsolidadoNacional = new ArrayList<Double[][]>();
			for (int i = 0; i < periodo.length; i++) {
				webConsolidadoNacional.add(new Double[5][1]);
			}
			graficos.llenarDatosConsolidadoGrafWeb(porcentajes, periodo,
					webConsolidadoNacional);
		}

		if (!reporte.exists()) {
			if (territorio != 4) {
				poblarReporte(idReporte, periodo);

				ArrayList<DefaultCategoryDataset> data = new ArrayList<DefaultCategoryDataset>();
				ArrayList<DefaultPieDataset> dataPie = new ArrayList<DefaultPieDataset>();
				ArrayList<TimeSeriesCollection> dataTimeseries = new ArrayList<TimeSeriesCollection>();
				ArrayList<String> periodosAux = new ArrayList<String>();

				for (int i = 0; i < periodo.length; i++) {
					for (int j = 0; j < indice; j++) {
						data.add(graficos.createDataset(periodo[i],
								idReporte[i], j));
						dataPie.add(graficos.createPieDataset(periodo[i],
								idReporte[i], j));
						periodosAux.add(periodo[i]);
					}
				}
				int graf = 0;
				int columna = 2;
				for (int j = 0; j < data.size(); j++) {
					insertarImagenBarras(graficos.createChartBarras(
							data.get(j), graf, null, periodosAux.get(j)), graf,
							columna);
					if (graf >= 5) {
						graf = 0;
						columna = columna + 15;
					} else {
						graf++;
					}
				}

				graf = 0;
				columna = 2;
				for (int j = 0; j < dataPie.size(); j++) {
					insertarImagenPie(graficos.createChartTortas(
							dataPie.get(j), graf, periodosAux.get(j)), graf,
							columna);
					if (graf >= 5) {
						graf = 0;
						columna = columna + 15;
					} else {
						graf++;
					}
				}

				if (idReporte.length > 1) {
					for (int j = 0; j < 5; j++) {
						dataTimeseries.add(graficos.createTimeseriesDataset(
								periodo, idReporte, j));
					}
					for (int j = 0; j < dataTimeseries.size(); j++) {
						insertarImagenTimeseries(
								graficos.createChartTimeseries(
										dataTimeseries.get(j), j), j);
					}
				}

			} else {
				poblarReporteConsolidado(idReporte, periodo);
				DefaultPieDataset dataP = null;
				DefaultCategoryDataset dataB = null;
				TimeSeriesCollection dataT = null;
				for (int i = 0; i < periodo.length; i++) {
					dataB = graficos.createBarDatasetConsolidado(porcentajes);
					int j = 0;
					while (j < Integer.valueOf(infoReportes
							.getString("BosqueNoBosqueCategorias"))) {
						porcentajes.remove(0);
						j++;
					}
					insertarImagenConsolidado(
							graficos.createBarChartConsolidado(null, 0, dataB,
									periodo[i]), i, 2, 1);
				}
				for (int i = 0; i < periodo.length; i++) {
					dataP = graficos
							.createPieDatasetConsolidado(porcentajesPie);
					int j = 0;
					while (j < Integer.valueOf(infoReportes
							.getString("BosqueNoBosqueCategorias"))) {
						porcentajesPie.remove(0);
						j++;
					}
					insertarImagenConsolidado(
							graficos.createPieChartConsolidado(null, 0, dataP,
									periodo[i]), i, 2, 2);

				}
				if (periodo.length > 1) {
					dataT = graficos.createTimeSDatasetConsolidado(
							porcentajesTimeBE, porcentajesTimeNBE,
							porcentajesTimeDE, porcentajesTimeRE,
							porcentajesTimeSI, nombrePeriodos);
					insertarImagenConsolidado(
							graficos.createTimeSChartConsolidado(null, 0,
									dataT, ""), 0, 2, 3);
				}
			}
			escribirReportes(parametro.getParametro("ruta_reportes"),
					libroReporte.getNombreReporte(), libro);
		}
		rutaArchivo = reporte.getAbsolutePath();
	}

	private void poblarReporteConsolidado(Integer[] idReporte, String[] periodo) {
		Integer indexArea = 2;
		Integer indexPorcetaje = 3;
		Double totalBosque = new Double(0);
		Double totalNoBosque = new Double(0);
		Double totalSinInfo = new Double(0);
		Double totalDeforestacion = new Double(0);
		Double totalRegeneracion = new Double(0);
		libro.setSheetName(0, "Consolidado");
		XSSFRow fb = libroReporte.getFilas()[0];
		XSSFRow fnb = libroReporte.getFilas()[1];
		XSSFRow fsi = libroReporte.getFilas()[2];
		XSSFRow fd = libroReporte.getFilas()[3];
		XSSFRow fr = libroReporte.getFilas()[4];

		porcentajes.clear();
		porcentajesPie.clear();
		porcentajesTimeBE.clear();
		porcentajesTimeNBE.clear();
		porcentajesTimeDE.clear();
		porcentajesTimeRE.clear();
		porcentajesTimeSI.clear();
		nombrePeriodos.clear();

		for (int k = 0; k < periodo.length; k++) {
			XSSFCell ib = fb.createCell(indexArea);
			XSSFCell inb = fnb.createCell(indexArea);
			XSSFCell isi = fsi.createCell(indexArea);
			XSSFCell id = fd.createCell(indexArea);
			XSSFCell ir = fr.createCell(indexArea);
			XSSFCell pb = fb.createCell(indexPorcetaje);
			XSSFCell pnb = fnb.createCell(indexPorcetaje);
			XSSFCell psi = fsi.createCell(indexPorcetaje);
			XSSFCell pd = fd.createCell(indexPorcetaje);
			XSSFCell pr = fr.createCell(indexPorcetaje);
			ArrayList<InformacionReporteCobertura> info = car
					.consultarInfoCobertura(idReporte[k]);
			for (int j = 0; j < info.size(); j++) {
				InformacionReporteCobertura b = info.get(j);
				totalBosque += b.getBosqueEstable();
				totalNoBosque += b.getNoBosqueEstable();
				totalSinInfo += b.getSinInformacionEstable();
				totalDeforestacion += b.getDeforestacion();
				totalRegeneracion += b.getRegeneracion();
			}
			Double total = totalBosque + totalNoBosque + totalSinInfo
					+ totalRegeneracion + totalDeforestacion;
			ib.setCellStyle(estiloDivisionReporte(libro));
			inb.setCellStyle(estiloDivisionReporte(libro));
			isi.setCellStyle(estiloDivisionReporte(libro));
			id.setCellStyle(estiloDivisionReporte(libro));
			ir.setCellStyle(estiloDivisionReporte(libro));
			ib.setCellValue(totalBosque);
			inb.setCellValue(totalNoBosque);
			isi.setCellValue(totalSinInfo);
			id.setCellValue(totalDeforestacion);
			ir.setCellValue(totalRegeneracion);
			Double pBosque = Util.calcularPorcentaje(total.intValue(),
					totalBosque.intValue());
			pb.setCellValue(pBosque);
			pb.setCellStyle(estiloDivisionReporte(libro));
			Double pNoBosque = Util.calcularPorcentaje(total.intValue(),
					totalNoBosque.intValue());
			pnb.setCellStyle(estiloDivisionReporte(libro));
			pnb.setCellValue(pNoBosque);
			Double pSinInfo = Util.calcularPorcentaje(total.intValue(),
					totalSinInfo.intValue());
			psi.setCellStyle(estiloDivisionReporte(libro));
			psi.setCellValue(pSinInfo);
			Double pDeforestacion = Util.calcularPorcentaje(total.intValue(),
					totalDeforestacion.intValue());
			pd.setCellStyle(estiloDivisionReporte(libro));
			pd.setCellValue(pDeforestacion);
			Double pRegeneracion = Util.calcularPorcentaje(total.intValue(),
					totalRegeneracion.intValue());
			pr.setCellStyle(estiloDivisionReporte(libro));
			pr.setCellValue(pRegeneracion);
			porcentajes.add(pBosque);
			porcentajes.add(pNoBosque);
			porcentajes.add(pSinInfo);
			porcentajes.add(pDeforestacion);
			porcentajes.add(pRegeneracion);
			porcentajesPie.add(pBosque);
			porcentajesPie.add(pNoBosque);
			porcentajesPie.add(pSinInfo);
			porcentajesPie.add(pDeforestacion);
			porcentajesPie.add(pRegeneracion);
			porcentajesTimeBE.add(pBosque);
			porcentajesTimeNBE.add(pNoBosque);
			porcentajesTimeDE.add(pDeforestacion);
			porcentajesTimeRE.add(pRegeneracion);
			porcentajesTimeSI.add(pSinInfo);
			nombrePeriodos.add(periodo[k]);
			indexArea += 2;
			indexPorcetaje += 2;

		}
	}

	private void poblarReporteWebConsolidado(Integer[] idReporte,
			String[] periodo) {
		Double totalBosque = new Double(0);
		Double totalNoBosque = new Double(0);
		Double totalSinInfo = new Double(0);
		Double totalDeforestacion = new Double(0);
		Double totalRegeneracion = new Double(0);

		porcentajes.clear();
		porcentajesPie.clear();
		porcentajesTimeBE.clear();
		porcentajesTimeNBE.clear();
		porcentajesTimeDE.clear();
		porcentajesTimeRE.clear();
		porcentajesTimeSI.clear();
		nombrePeriodos.clear();

		for (int k = 0; k < periodo.length; k++) {
			ArrayList<InformacionReporteCobertura> info = car
					.consultarInfoCobertura(idReporte[k]);
			for (int j = 0; j < info.size(); j++) {
				InformacionReporteCobertura b = info.get(j);
				totalBosque += b.getBosqueEstable();
				totalNoBosque += b.getNoBosqueEstable();
				totalSinInfo += b.getSinInformacionEstable();
				totalDeforestacion += b.getDeforestacion();
				totalRegeneracion += b.getRegeneracion();
			}
			Double total = totalBosque + totalNoBosque + totalSinInfo
					+ totalRegeneracion + totalDeforestacion;
			Double pBosque = Util.calcularPorcentaje(total.intValue(),
					totalBosque.intValue());
			Double pNoBosque = Util.calcularPorcentaje(total.intValue(),
					totalNoBosque.intValue());
			Double pSinInfo = Util.calcularPorcentaje(total.intValue(),
					totalSinInfo.intValue());
			Double pDeforestacion = Util.calcularPorcentaje(total.intValue(),
					totalDeforestacion.intValue());
			Double pRegeneracion = Util.calcularPorcentaje(total.intValue(),
					totalRegeneracion.intValue());
			porcentajes.add(pBosque);
			porcentajes.add(pNoBosque);
			porcentajes.add(pSinInfo);
			porcentajes.add(pDeforestacion);
			porcentajes.add(pRegeneracion);

		}
	}

	private void insertarImagenConsolidado(JFreeChart chart, int grafica,
			int columna, int hoja) {
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
		if (libro.getNumberOfSheets() < hoja + 1) {
			hoja2 = libro.createSheet();
			drawing = hoja2.createDrawingPatriarch();
		} else {
			hoja2 = libro.getSheetAt(1);
		}
		CreationHelper helper = libro.getCreationHelper();
		ClientAnchor anchor = helper.createClientAnchor();
		if (hoja == 1)
			libro.setSheetName(hoja, "Graficos Barra");
		if (hoja == 2)
			libro.setSheetName(hoja, "Graficos Torta");
		if (hoja == 3)
			libro.setSheetName(hoja, "Graficos Tendencia");

		anchor.setCol1(columna);
		if (grafica == 0)
			anchor.setRow1(2);
		else if (grafica == 1)
			anchor.setRow1(40);
		else if (grafica == 2)
			anchor.setRow1(78);
		else if (grafica == 3)
			anchor.setRow1(116);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();
	}

	private void insertarImagenBarras(JFreeChart chart, int grafica, int columna) {
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
		libro.setSheetName(1, "Graficos Barras");

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
		else if (grafica == 5)
			anchor.setRow1(192);

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
		XSSFSheet hoja2;
		if (libro.getNumberOfSheets() < 3) {
			hoja2 = libro.createSheet();
			drawing = hoja2.createDrawingPatriarch();
		} else {
			hoja2 = libro.getSheetAt(2);
		}
		CreationHelper helper = libro.getCreationHelper();
		ClientAnchor anchor = helper.createClientAnchor();
		libro.setSheetName(2, "Graficos Tortas");

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
		else if (grafica == 5)
			anchor.setRow1(192);

		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();
	}

	private void insertarImagenTimeseries(JFreeChart chart, int grafica) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
		BufferedImage image = chart.createBufferedImage(900, 700);
		try {
			ImageIO.write(image, "png", baos);
		} catch (IOException e) {
			System.out.println("Error de escritura");
		}
		byte[] b = baos.toByteArray();
		pictureIdx = libro.addPicture(b, XSSFWorkbook.PICTURE_TYPE_PNG);
		XSSFSheet hoja4;
		if (libro.getNumberOfSheets() < 4) {
			hoja4 = libro.createSheet();
			drawing = hoja4.createDrawingPatriarch();
		} else {
			hoja4 = libro.getSheetAt(3);
		}
		CreationHelper helper = libro.getCreationHelper();
		ClientAnchor anchor = helper.createClientAnchor();
		libro.setSheetName(3, "Graficos Tendencias");

		anchor.setCol1(2);
		if (grafica == 0)
			anchor.setRow1(2);
		else if (grafica == 1)
			anchor.setRow1(45);
		else if (grafica == 2)
			anchor.setRow1(88);
		else if (grafica == 3)
			anchor.setRow1(132);
		else if (grafica == 4)
			anchor.setRow1(175);

		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();
	}

	private void poblarReporte(Integer[] idReporte, String[] periodo) {
		int indice = 3;
		int cont = 0;
		XSSFCellStyle estilo = estiloDivisionReporte(libro);
		for (int i = 0; i < idReporte.length; i++) {
			ArrayList<InformacionReporteCobertura> info = car
					.consultarInfoCobertura(idReporte[i]);
			indice = 3;
			for (int j = 0; j < info.size(); j++) {
				if (i == 0)
					cont = 0;
				else
					cont = 8 * i;
				InformacionReporteCobertura b = info.get(j);
				XSSFRow filaBosque = libroReporte.getFilas()[cont];
				XSSFCell bosque = filaBosque.createCell(indice);
				bosque.setCellStyle(estilo);
				bosque.setCellValue(b.getBosqueEstable());
				cont++;
				XSSFRow filaNoBosque = libroReporte.getFilas()[cont];
				XSSFCell noBosque = filaNoBosque.createCell(indice);
				noBosque.setCellStyle(estilo);
				noBosque.setCellValue(b.getNoBosqueEstable());
				cont++;
				XSSFRow filaDeforestacion = libroReporte.getFilas()[cont];
				XSSFCell deforestacion = filaDeforestacion.createCell(indice);
				deforestacion.setCellStyle(estilo);
				deforestacion.setCellValue(b.getDeforestacion());
				cont++;
				XSSFRow filaRegeneracion = libroReporte.getFilas()[cont];
				XSSFCell regeneracion = filaRegeneracion.createCell(indice);
				regeneracion.setCellStyle(estilo);
				regeneracion.setCellValue(b.getRegeneracion());
				cont++;
				XSSFRow filaSinInformacion = libroReporte.getFilas()[cont];
				XSSFCell sinInfo = filaSinInformacion.createCell(indice);
				sinInfo.setCellStyle(estilo);
				sinInfo.setCellValue(b.getSinInformacionEstable());
				cont++;
				XSSFRow filaBosquePerdido = libroReporte.getFilas()[cont];
				XSSFCell bosquePerdido = filaBosquePerdido.createCell(indice);
				bosquePerdido.setCellStyle(estilo);
				Double s = (new BigDecimal(b.getBosqueEstable())
						.add(new BigDecimal(b.getDeforestacion())))
						.doubleValue();
				Double bosquePUno = b.getDeforestacion() / s;// ))).doubleValue();//(new
																// BigDecimal(s).add(new
																// BigDecimal(b
				// .getBosqueEstable()))).doubleValue();
				Double pbp = (bosquePUno * 100);// b.getDeforestacion();
				if (pbp.isNaN()) {
					bosquePerdido.setCellValue(0);
					pbp = 0.0;
				} else
					bosquePerdido.setCellValue(pbp);
				cont++;
				XSSFRow filaDefoAnual = libroReporte.getFilas()[cont];
				XSSFCell defoAnual = filaDefoAnual.createCell(indice);
				defoAnual.setCellStyle(estilo);
				Integer dif = Util.obtenerDiferenciaAnos(periodo[i]);
				defoAnual.setCellValue(b.getDeforestacion() / dif);
				cont++;
				XSSFRow filaPorcentajeBosque = libroReporte.getFilas()[cont];
				XSSFCell pbpp = filaPorcentajeBosque.createCell(indice);
				pbpp.setCellStyle(estilo);
				pbpp.setCellValue(pbp / dif);
				indice++;
			}
		}
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

	public static boolean escribirReportes(String ruta, String nombreArchivo,
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

	/*
	 * public static void main(String[] a) { String p[] = { "1990-2000" };
	 * ConsultarReporteCobertura v = new ConsultarReporteCobertura(); Integer
	 * r[] = { 147 }; v.consultarReporte(2, p, r); }
	 */

	public ArrayList<Double[][][]> getListaDatosWeb() {
		return webDatosGraficas;
	}

	public ArrayList<Double[][]> getListaDatosWebConsolidado() {
		return webConsolidadoNacional;
	}

	public String[] getNombresCARs() {
		return nombresCARs;
	}

	public String[] getNombresDepartamentos() {
		return nombresDedapartamentos;
	}

	public String[] getNombresAreasHidrog() {
		return nombresAreasHidrograficas;
	}

	public String[] getPeriodos() {
		return Periodo;
	}

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public XSSFWorkbook getLibro() {
		return libro;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}
}
