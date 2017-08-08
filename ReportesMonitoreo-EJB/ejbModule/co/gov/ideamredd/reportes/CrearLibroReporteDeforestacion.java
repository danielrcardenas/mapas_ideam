package co.gov.ideamredd.reportes;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.util.CellRangeAddress;
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
import co.gov.ideamredd.entities.InformacionReporteDeforestacion;

@Stateless
public class CrearLibroReporteDeforestacion {

	@EJB
	ConsultarAsociadosReporte car;
	@EJB
	private Parametro parametro;

	private CrearGraficosDeforestacion cgd = new CrearGraficosDeforestacion();

	private XSSFWorkbook libro;
	private static final String archivoReportes = "co/gov/ideamredd/reportes/reportesWPS";
	private static final ResourceBundle infoReportes = ResourceBundle
			.getBundle(archivoReportes);
	private XSSFCellStyle estilo;
	private XSSFCellStyle estilo2;
	private XSSFRow[] filas;
	private ArrayList<XSSFRow> filasTitulos;
	private ArrayList<Integer> numeroRegiones = new ArrayList<Integer>();
	private ArrayList<String[]> porcentajes = new ArrayList<String[]>();
	private String[] nombreArbustales;
	private String[] nombreAreaAgricolas;
	private String[] nombreAreasUrbanizadas;
	private String[] nombreCultivosPermanentes;
	private String[] nombreCultivosTransitorios;
	private String[] nombreHerbazales;
	private String[] nombreOtrasAreas;
	private String[] nombrePastos;
	private String[] nombreSuperficieAgua;
	private String[] nombreVegetacionAcuatica;
	private String[] nombreVegetacionSecundaria;
	private String[] nombreZonasQuemadas;

	private String[] hectareasArbustales;
	private String[] hectareasAreaAgricolas;
	private String[] hectareasAreasUrbanizadas;
	private String[] hectareasCultivosPermanentes;
	private String[] hectareasCultivosTransitorios;
	private String[] hectareasHerbazales;
	private String[] hectareasOtrasAreas;
	private String[] hectareasPastos;
	private String[] hectareasSuperficieAgua;
	private String[] hectareasVegetacionAcuatica;
	private String[] hectareasVegetacionSecundaria;
	private String[] hectareasZonasQuemadas;

	private String[] porcentajeArbustales;
	private String[] porcentajeAreaAgricolas;
	private String[] porcentajeAreasUrbanizadas;
	private String[] porcentajeCultivosPermanentes;
	private String[] porcentajeCultivosTransitorios;
	private String[] porcentajeHerbazales;
	private String[] porcentajeOtrasAreas;
	private String[] porcentajePastos;
	private String[] porcentajeSuperficieAgua;
	private String[] porcentajeVegetacionAcuatica;
	private String[] porcentajeVegetacionSecundaria;
	private String[] porcentajeZonasQuemadas;

	private int indexFilas = 0;
	private double totalHectareas = 0;
	private double totalPorcentaje = 0;
	private double TotalConsolidado;
	private int pictureIdx = 0;
	private XSSFDrawing drawing;
	private String nombreReporte;
	private String rutaArchivo;
	DecimalFormat df = new DecimalFormat("#########.##");

	private String Periodo;
	private String[] nombresAreasHidrograficas = {
			infoReportes.getString("nombreHidro1"),
			infoReportes.getString("nombreHidro2"),
			infoReportes.getString("nombreHidro3"),
			infoReportes.getString("nombreHidro4"),
			infoReportes.getString("nombreHidro5") };
	private String[] nombresTiposBosque = {
			infoReportes.getString("reporte.deforestacion.item1"),
			infoReportes.getString("reporte.deforestacion.item2"),
			infoReportes.getString("reporte.deforestacion.item3"),
			infoReportes.getString("reporte.deforestacion.item4"),
			infoReportes.getString("reporte.deforestacion.item5"),
			infoReportes.getString("reporte.deforestacion.item6"),
			infoReportes.getString("reporte.deforestacion.item7"),
			infoReportes.getString("reporte.deforestacion.item8"),
			infoReportes.getString("reporte.deforestacion.item9"),
			infoReportes.getString("reporte.deforestacion.item10"),
			infoReportes.getString("reporte.deforestacion.item11"),
			infoReportes.getString("reporte.deforestacion.item12") };

	// Datos para graficas en la pagina web
	// Arreglo de 3 dimensiones para los datos de los tipos de bosque de
	// Cada Area hidrografica.
	private ArrayList<Double[][]> webAreasHidrograficas;
	private ArrayList<Double[]> webConsolidadoNacional;

	public XSSFWorkbook construirLibroReporte(Integer idReporte,
			Integer territorio, String periodo) {
		indexFilas = 0;
		totalHectareas = 0;
		totalPorcentaje = 0;
		pictureIdx = 0;
		ArrayList<InformacionReporteDeforestacion> infoDeforestacion = car
				.consultarInfoDeforestacion(idReporte);
		if (territorio == 4 || territorio == 5)
			nombreReporte = "Deforestacion";
		else if (territorio == 6 || territorio == 7)
			nombreReporte = "Regeneracion";

		nombreReporte += periodo;
		XSSFSheet hoja = crearLibro();
		estilo = estiloTituloReporte(libro);
		estilo2 = estiloDivisionReporte(libro);

		crearEncabezado(hoja, infoDeforestacion, territorio, periodo);
		return libro;
	}

	private void crearEncabezado(XSSFSheet hoja,
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion,
			Integer territorio, String periodos) {

		libro.setSheetName(0, "Tabla");
		agregarTitulo(hoja, territorio);
		agregarTituloDivision(hoja, infoDeforestacion, territorio);
		agregarNombreDivision(infoDeforestacion, territorio);
		agregarTitulosItems(hoja, territorio);
		indexFilas += 2;
		XSSFRow totales = hoja.createRow(indexFilas);
		XSSFCell total = totales.createCell(1);
		total.setCellStyle(estilo2);
		total.setCellValue("Total");
		if (territorio != 4 && territorio != 6) {
			totales.createCell(2).setCellStyle(estilo2);
			CellRangeAddress r1 = new CellRangeAddress(indexFilas, indexFilas,
					1, 2);
			hoja.addMergedRegion(r1);
			nombreReporte += "AreasHidrograficas";
			XSSFCell totalH = totales.createCell(3);
			totalH.setCellValue(df.format(totalHectareas));
			totalH.setCellStyle(estilo2);
			XSSFCell totalP = totales.createCell(4);
			totalP.setCellValue(df.format(totalPorcentaje));
			totalP.setCellStyle(estilo2);
		} else {
			nombreReporte += "ConsolidadoNacional";
			XSSFCell totalH = totales.createCell(2);
			totalH.setCellValue(totalHectareas);
			totalH.setCellStyle(estilo2);
			XSSFCell totalP = totales.createCell(3);
			totalP.setCellValue(totalPorcentaje);
			totalP.setCellStyle(estilo2);
		}
		Integer indice = 0;

		// *************Bloque de Graficas Web**********************
		Periodo = periodos;

		webAreasHidrograficas = new ArrayList<Double[][]>();
		for (int i = 0; i < 5; i++) {
			webAreasHidrograficas.add(new Double[12][1]);
		}
		webConsolidadoNacional = new ArrayList<Double[]>();
		for (int i = 0; i < 12; i++) {
			webConsolidadoNacional.add(new Double[1]);
		}
		Integer numAreasHidro = Integer.valueOf(infoReportes
				.getString("numeroAreasHidrograficas"));

		if (territorio != 4 && territorio != 6) {
			// "AreasHidrograficas";
			indice = Integer.valueOf(infoReportes
					.getString("reporte.deforestacion.cantidadGraficos"));
			for (int j = 0; j < numAreasHidro; j++) {
				cgd.llenarDatosGrafWeb(periodos, infoDeforestacion, j,
						webAreasHidrograficas);
			}
		} else {
			// "ConsolidadoNacional";
			indice = Integer
					.valueOf(infoReportes
							.getString("reporte.deforestacion.cantidadGraficosConsolidado"));
			cgd.createWebConsolidado(periodos, infoDeforestacion, 0,
					webConsolidadoNacional, porcentajes, TotalConsolidado);
		}

		DefaultPieDataset[] data = new DefaultPieDataset[indice];
		DefaultCategoryDataset[] dataB = new DefaultCategoryDataset[indice];
		for (int i = 0; i < indice; i++) {
			if (territorio != 4 && territorio != 6) {
				data[i] = cgd.createDataset(infoDeforestacion, i);
				dataB[i] = cgd.createBarDataset(infoDeforestacion, i);
			} else {
				data[i] = cgd.createDatasetConsolidado(null, porcentajes,
						TotalConsolidado);
				dataB[i] = cgd.createBarDatasetConsolidado(null, porcentajes,
						TotalConsolidado);
			}
		}
		if (territorio != 4 && territorio != 6) {
			for (int j = 0; j < dataB.length; j++) {
				insertarImagen(cgd.createBarChart(dataB[j], periodos,
						nombresAreasHidrograficas[j]), j, 2, 1);
			}
			for (int j = 0; j < data.length; j++) {
				insertarImagen(cgd.createChart(data[j], periodos,
						nombresAreasHidrograficas[j]), j, 2, 2);
			}
		} else {
			for (int j = 0; j < data.length; j++) {
				insertarImagen(
						cgd.createBarChartConsolidado(dataB[j], periodos), j,
						2, 1);
				insertarImagen(cgd.createChartConsolidado(data[j], periodos),
						j, 2, 2);
			}
		}
		File reporte = new File(parametro.getParametro("ruta_reportes")
				+ nombreReporte + ".xlsx");
		if (!reporte.exists()) {
			escribirReportes(parametro.getParametro("ruta_reportes"),
					nombreReporte, libro);
		}
		rutaArchivo = reporte.getAbsolutePath();
	}

	private void insertarImagen(JFreeChart chart, int grafica, int columna,
			int hoja) {
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
			hoja2 = libro.getSheetAt(hoja);
		}
		CreationHelper helper = libro.getCreationHelper();
		ClientAnchor anchor = helper.createClientAnchor();
		switch (hoja) {
		case 1:
			libro.setSheetName(hoja, "Graficos Barras");
			break;
		case 2:
			libro.setSheetName(hoja, "Graficos Tortas");
			break;
		}

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

	private void agregarTitulo(XSSFSheet hoja, Integer territorio) {
		XSSFRow titulo = hoja.createRow(1);
		XSSFCell cobertura = titulo.createCell(1);
		cobertura.setCellStyle(estilo);
		cobertura.setCellValue("Cobertura");
		if (territorio != 4 && territorio != 6) {
			XSSFCell region = titulo.createCell(2);
			region.setCellStyle(estilo);
			region.setCellValue("Region");
			XSSFCell hect = titulo.createCell(3);
			hect.setCellStyle(estilo);
			hect.setCellValue(infoReportes.getString("hectareas"));
			XSSFCell porc = titulo.createCell(4);
			porc.setCellStyle(estilo);
			porc.setCellValue(infoReportes.getString("procentaje"));
		} else {
			XSSFCell hect = titulo.createCell(2);
			hect.setCellStyle(estilo);
			hect.setCellValue(infoReportes.getString("hectareas"));
			XSSFCell porc = titulo.createCell(3);
			porc.setCellStyle(estilo);
			porc.setCellValue(infoReportes.getString("procentaje"));
		}
	}

	private void agregarTitulosItems(XSSFSheet hoja, Integer territorio) {
		int numeroFila = 2;
		int merge;
		Integer numeroItems = Integer.valueOf(infoReportes
				.getString("DeforestacionTipoCoberturaCategorias"));

		for (int i = 0; i < filasTitulos.size(); i++) {
			XSSFCell c = filasTitulos.get(i).createCell(1);
			c.setCellStyle(estilo2);
			c.setCellValue(infoReportes.getString("reporte.deforestacion.item"
					+ (i + 1)));
		}

		if (territorio == 4 || territorio == 6) {
			hoja.setColumnWidth(1, 6000);
			hoja.setColumnWidth(2, 4000);
			hoja.setColumnWidth(3, 3500);
			hoja.setColumnWidth(4, 3500);
			for (int i = 0; i < numeroItems; i++) {
				merge = numeroFila;
				numeroFila++;
			}
		} else {
			hoja.setColumnWidth(1, 4000);
			hoja.setColumnWidth(2, 3500);
			hoja.setColumnWidth(3, 3500);
			for (int i = 0; i < numeroItems; i++) {
				if (numeroRegiones.get(i) != 0) {
					merge = numeroFila;
					numeroFila++;
					int k = 2;
					if ((numeroRegiones.get(i) - 1) != 0) {
						while (k <= numeroRegiones.get(i)) {
							numeroFila++;
							k++;
						}
					}
					CellRangeAddress r1 = new CellRangeAddress(merge,
							numeroFila - 1, 1, 1);
					hoja.addMergedRegion(r1);
				}
			}
		}

	}

	private void agregarTituloDivision(XSSFSheet hoja,
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion,
			Integer territorio) {
		int numeroFila = 2;
		int cont = 0;
		XSSFRow fila;
		contarRegiones(infoDeforestacion);
		if (territorio != 4 && territorio != 6)
			for (int i = 0; i < numeroRegiones.size(); i++) {
				if (numeroRegiones.get(i) != 0)
					for (int j = 1; j <= numeroRegiones.get(i); j++) {
						fila = hoja.createRow(numeroFila);
						numeroFila++;
						if (cont < filas.length)
							filas[cont] = fila;
						cont++;
					}
			}
		else {
			Integer numeroItems = Integer.valueOf(infoReportes
					.getString("DeforestacionTipoCoberturaCategorias"));
			for (int i = 0; i < numeroItems; i++) {
				fila = hoja.createRow(numeroFila);
				numeroFila++;
				if (cont < filas.length)
					filas[cont] = fila;
				cont++;
			}
		}
	}

	private void agregarNombreDivision(
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion,
			Integer territorio) {
		filasTitulos = new ArrayList<XSSFRow>();
		for (int i = 0; i < numeroRegiones.size(); i++) {
			switch (i) {
			case 0:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i),
						nombreAreasUrbanizadas, hectareasAreasUrbanizadas,
						porcentajeAreasUrbanizadas, infoDeforestacion,
						territorio);
				break;
			case 1:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i),
						nombreCultivosTransitorios,
						hectareasCultivosTransitorios,
						porcentajeCultivosTransitorios, infoDeforestacion,
						territorio);
				break;
			case 2:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i),
						nombreCultivosPermanentes,
						hectareasCultivosPermanentes,
						porcentajeCultivosPermanentes, infoDeforestacion,
						territorio);
				break;
			case 3:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i), nombrePastos,
						hectareasPastos, porcentajePastos, infoDeforestacion,
						territorio);
				break;
			case 4:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i), nombreAreaAgricolas,
						hectareasAreaAgricolas, porcentajeAreaAgricolas,
						infoDeforestacion, territorio);
				break;
			case 5:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i), nombreHerbazales,
						hectareasHerbazales, porcentajeHerbazales,
						infoDeforestacion, territorio);
				break;
			case 6:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i), nombreArbustales,
						hectareasArbustales, porcentajeArbustales,
						infoDeforestacion, territorio);
				break;
			case 7:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i),
						nombreVegetacionSecundaria,
						hectareasVegetacionSecundaria,
						porcentajeVegetacionSecundaria, infoDeforestacion,
						territorio);
				break;
			case 8:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i), nombreZonasQuemadas,
						hectareasZonasQuemadas, porcentajeZonasQuemadas,
						infoDeforestacion, territorio);
				break;
			case 9:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i), nombreOtrasAreas,
						hectareasOtrasAreas, porcentajeOtrasAreas,
						infoDeforestacion, territorio);
				break;
			case 10:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i),
						nombreVegetacionAcuatica, hectareasVegetacionAcuatica,
						porcentajeVegetacionAcuatica, infoDeforestacion,
						territorio);
				break;
			case 11:
				filasTitulos.add(filas[indexFilas]);
				seleccionarTitulos(numeroRegiones.get(i), nombreSuperficieAgua,
						hectareasSuperficieAgua, porcentajeSuperficieAgua,
						infoDeforestacion, territorio);
				break;
			}
		}
	}

	private void seleccionarTitulos(Integer numeroItems, String[] titulos,
			String[] hectareas, String[] porcentaje,
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion,
			Integer territorio) {
		if (territorio != 4 && territorio != 6) {
			for (int j = 0; j < numeroItems; j++) {
				XSSFCell b = ((XSSFRow) filas[indexFilas]).createCell(1);
				b.setCellStyle(estilo2);
				b.setCellValue(" ");
				XSSFCell c = ((XSSFRow) filas[indexFilas]).createCell(2);
				c.setCellStyle(estilo2);
				c.setCellValue(titulos[j]);
				XSSFCell h = ((XSSFRow) filas[indexFilas]).createCell(3);
				XSSFCell p = ((XSSFRow) filas[indexFilas]).createCell(4);
				h.setCellStyle(estilo2);
				p.setCellStyle(estilo2);
				h.setCellValue(df.format(Double.valueOf(hectareas[j])));
				p.setCellValue(df.format(Double.valueOf(porcentaje[j])));
				totalHectareas += Double.valueOf(hectareas[j]);
				totalPorcentaje += Double.valueOf(porcentaje[j]);
				indexFilas++;
			}
		} else {
			Double totalHectarea = new Double(0);
			Double totalPorcentajes = new Double(0);
			for (int j = 0; j < numeroItems; j++) {
				totalHectarea += Double.valueOf(hectareas[j]);
				totalPorcentajes += Double.valueOf(porcentaje[j]);
			}
			XSSFCell h = ((XSSFRow) filas[indexFilas]).createCell(2);
			XSSFCell p = ((XSSFRow) filas[indexFilas]).createCell(3);
			h.setCellStyle(estilo2);
			p.setCellStyle(estilo2);
			h.setCellValue(totalHectarea);
			p.setCellValue((totalPorcentajes/TotalConsolidado)*100);
			totalHectareas += totalHectarea;
			totalPorcentaje = 100;
			indexFilas++;
		}
	}

	private void contarRegiones(
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion) {
		numeroRegiones.clear();
		int regionArbustales = 0;
		int regionAreaAgricolas = 0;
		int regionAreasUrbanizadas = 0;
		int regionCultivosPermanentes = 0;
		int regionCultivosTransitorios = 0;
		int regionHerbazales = 0;
		int regionOtrasAreas = 0;
		int regionPastos = 0;
		int regionSuperficieAgua = 0;
		int regionVegetacionAcuatica = 0;
		int regionVegetacionSecundaria = 0;
		int regionZonasQuemadas = 0;

		String arbustales = "";
		String areasAgricolas = "";
		String areasUrbanizadas = "";
		String cultivosPermanentes = "";
		String cultivosTransitorios = "";
		String herbazales = "";
		String otrasAreas = "";
		String pastos = "";
		String superficieAgua = "";
		String vegetacionAcuatica = "";
		String vegetacionSecundaria = "";
		String zonasQuemas = "";

		String arbustalesHectareas = "";
		String areasAgricolasHectareas = "";
		String areasUrbanizadasHectareas = "";
		String cultivosPermanentesHectareas = "";
		String cultivosTransitoriosHectareas = "";
		String herbazalesHectareas = "";
		String otrasAreasHectareas = "";
		String pastosHectareas = "";
		String superficieAguaHectareas = "";
		String vegetacionAcuaticaHectareas = "";
		String vegetacionSecundariaHectareas = "";
		String zonasQuemasHectareas = "";

		String arbustalesPorcentajes = "";
		String areasAgricolasPorcentajes = "";
		String areasUrbanizadasPorcentajes = "";
		String cultivosPermanentesPorcentajes = "";
		String cultivosTransitoriosPorcentajes = "";
		String herbazalesPorcentajes = "";
		String otrasAreasPorcentajes = "";
		String pastosPorcentajes = "";
		String superficieAguaPorcentajes = "";
		String vegetacionAcuaticaPorcentajes = "";
		String vegetacionSecundariaPorcentajes = "";
		String zonasQuemasPorcentajes = "";

		for (int i = 0; i < infoDeforestacion.size(); i++) {
			InformacionReporteDeforestacion d = (InformacionReporteDeforestacion) infoDeforestacion
					.get(i);
			if (d.getPorcArbustales() > 0.1) {
				regionArbustales++;
				if (arbustales.equals(""))
					arbustales = d.getNombre();
				else
					arbustales = arbustales + ";" + d.getNombre();
				if (arbustalesHectareas.equals(""))
					arbustalesHectareas = d.getArbustales().toString();
				else
					arbustalesHectareas = arbustalesHectareas + ";"
							+ d.getArbustales().toString();
				if (arbustalesPorcentajes.equals(""))
					arbustalesPorcentajes = d.getPorcArbustales().toString();
				else
					arbustalesPorcentajes = arbustalesPorcentajes + ";"
							+ d.getPorcArbustales().toString();

			}
			if (d.getPorcAreasAgricolas() > 0.1) {
				regionAreaAgricolas++;
				if (areasAgricolas.equals(""))
					areasAgricolas = d.getNombre();
				else
					areasAgricolas = areasAgricolas + ";" + d.getNombre();
				if (areasAgricolasHectareas.equals(""))
					areasAgricolasHectareas = d.getAreasAgricolas().toString();
				else
					areasAgricolasHectareas = areasAgricolasHectareas + ";"
							+ d.getAreasAgricolas().toString();
				if (areasAgricolasPorcentajes.equals(""))
					areasAgricolasPorcentajes = d.getPorcAreasAgricolas()
							.toString();
				else
					areasAgricolasPorcentajes = areasAgricolasPorcentajes + ";"
							+ d.getPorcAreasAgricolas().toString();
			}
			if (d.getPorcAreasUrbanizadas() > 0.1) {
				regionAreasUrbanizadas++;
				if (areasUrbanizadas.equals(""))
					areasUrbanizadas = d.getNombre();
				else
					areasUrbanizadas = areasUrbanizadas + ";" + d.getNombre();
				if (areasUrbanizadasHectareas.equals(""))
					areasUrbanizadasHectareas = d.getAreasUrbanizadas()
							.toString();
				else
					areasUrbanizadasHectareas = areasUrbanizadasHectareas + ";"
							+ d.getAreasUrbanizadas().toString();
				if (areasUrbanizadasPorcentajes.equals(""))
					areasUrbanizadasPorcentajes = d.getPorcAreasUrbanizadas()
							.toString();
				else
					areasUrbanizadasPorcentajes = areasUrbanizadasPorcentajes
							+ ";" + d.getPorcAreasUrbanizadas().toString();

			}
			if (d.getPorcCultivosPermanentes() > 0.1) {
				regionCultivosPermanentes++;
				if (cultivosPermanentes.equals(""))
					cultivosPermanentes = d.getNombre();
				else
					cultivosPermanentes = cultivosPermanentes + ";"
							+ d.getNombre();
				if (cultivosPermanentesHectareas.equals(""))
					cultivosPermanentesHectareas = d.getCultivosPermanentes()
							.toString();
				else
					cultivosPermanentesHectareas = cultivosPermanentesHectareas
							+ ";" + d.getCultivosPermanentes().toString();
				if (cultivosPermanentesPorcentajes.equals(""))
					cultivosPermanentesPorcentajes = d
							.getPorcCultivosPermanentes().toString();
				else
					cultivosPermanentesPorcentajes = cultivosPermanentesPorcentajes
							+ ";" + d.getPorcCultivosPermanentes().toString();
			}
			if (d.getPorcCultivosTransitorios() > 0.1) {
				regionCultivosTransitorios++;
				if (cultivosTransitorios.equals(""))
					cultivosTransitorios = d.getNombre();
				else
					cultivosTransitorios = cultivosTransitorios + ";"
							+ d.getNombre();
				if (cultivosTransitoriosHectareas.equals(""))
					cultivosTransitoriosHectareas = d.getCultivosTransitorios()
							.toString();
				else
					cultivosTransitoriosHectareas = cultivosTransitoriosHectareas
							+ ";" + d.getCultivosTransitorios().toString();
				if (cultivosTransitoriosPorcentajes.equals(""))
					cultivosTransitoriosPorcentajes = d
							.getPorcCultivosTransitorios().toString();
				else
					cultivosTransitoriosPorcentajes = cultivosTransitoriosPorcentajes
							+ ";" + d.getPorcCultivosTransitorios().toString();
			}
			if (d.getPorcHerbazales() > 0.1) {
				regionHerbazales++;
				if (herbazales.equals(""))
					herbazales = d.getNombre();
				else
					herbazales = herbazales + ";" + d.getNombre();
				if (herbazalesHectareas.equals(""))
					herbazalesHectareas = d.getHerbazales().toString();
				else
					herbazalesHectareas = herbazalesHectareas + ";"
							+ d.getHerbazales().toString();
				if (herbazalesPorcentajes.equals(""))
					herbazalesPorcentajes = d.getPorcHerbazales().toString();
				else
					herbazalesPorcentajes = herbazalesPorcentajes + ";"
							+ d.getPorcHerbazales().toString();
			}
			if (d.getPorcOtrasAreasSinVegetacion() > 0.1) {
				regionOtrasAreas++;
				if (otrasAreas.equals(""))
					otrasAreas = d.getNombre();
				else
					otrasAreas = otrasAreas + ";" + d.getNombre();
				if (otrasAreasHectareas.equals(""))
					otrasAreasHectareas = d.getOtrasAreasSinVegetacion()
							.toString();
				else
					otrasAreasHectareas = otrasAreasHectareas + ";"
							+ d.getOtrasAreasSinVegetacion().toString();
				if (otrasAreasPorcentajes.equals(""))
					otrasAreasPorcentajes = d.getPorcOtrasAreasSinVegetacion()
							.toString();
				else
					otrasAreasPorcentajes = otrasAreasPorcentajes + ";"
							+ d.getPorcOtrasAreasSinVegetacion().toString();
			}
			if (d.getPorcPastosPlantacionForestal() > 0.1) {
				regionPastos++;
				if (pastos.equals(""))
					pastos = d.getNombre();
				else
					pastos = pastos + ";" + d.getNombre();
				if (pastosHectareas.equals(""))
					pastosHectareas = d.getPastosPlantacionForestal()
							.toString();
				else
					pastosHectareas = pastosHectareas + ";"
							+ d.getPastosPlantacionForestal().toString();
				if (pastosPorcentajes.equals(""))
					pastosPorcentajes = d.getPorcPastosPlantacionForestal()
							.toString();
				else
					pastosPorcentajes = pastosPorcentajes + ";"
							+ d.getPorcPastosPlantacionForestal().toString();
			}
			if (d.getPorcSuperficiesAgua() > 0.1) {
				regionSuperficieAgua++;
				if (superficieAgua.equals(""))
					superficieAgua = d.getNombre();
				else
					superficieAgua = superficieAgua + ";" + d.getNombre();
				if (superficieAguaHectareas.equals(""))
					superficieAguaHectareas = d.getSuperficiesAgua().toString();
				else
					superficieAguaHectareas = superficieAguaHectareas + ";"
							+ d.getSuperficiesAgua().toString();
				if (superficieAguaPorcentajes.equals(""))
					superficieAguaPorcentajes = d.getPorcSuperficiesAgua()
							.toString();
				else
					superficieAguaPorcentajes = superficieAguaPorcentajes + ";"
							+ d.getPorcSuperficiesAgua().toString();
			}
			if (d.getPorcVegetacionAcuatica() > 0.1) {
				regionVegetacionAcuatica++;
				if (vegetacionAcuatica.equals(""))
					vegetacionAcuatica = d.getNombre();
				else
					vegetacionAcuatica = vegetacionAcuatica + ";"
							+ d.getNombre();
				if (vegetacionAcuaticaHectareas.equals(""))
					vegetacionAcuaticaHectareas = d.getVegetacionAcuatica()
							.toString();
				else
					vegetacionAcuaticaHectareas = vegetacionAcuaticaHectareas
							+ ";" + d.getVegetacionAcuatica().toString();
				if (vegetacionAcuaticaPorcentajes.equals(""))
					vegetacionAcuaticaPorcentajes = d
							.getPorcVegetacionAcuatica().toString();
				else
					vegetacionAcuaticaPorcentajes = vegetacionAcuaticaPorcentajes
							+ ";" + d.getPorcVegetacionAcuatica().toString();
			}
			if (d.getPorcVegetacionSecundaria() > 0.1) {
				regionVegetacionSecundaria++;
				if (vegetacionSecundaria.equals(""))
					vegetacionSecundaria = d.getNombre();
				else
					vegetacionSecundaria = vegetacionSecundaria + ";"
							+ d.getNombre();
				if (vegetacionSecundariaHectareas.equals(""))
					vegetacionSecundariaHectareas = d.getVegetacionSecundaria()
							.toString();
				else
					vegetacionSecundariaHectareas = vegetacionSecundariaHectareas
							+ ";" + d.getVegetacionSecundaria().toString();
				if (vegetacionSecundariaPorcentajes.equals(""))
					vegetacionSecundariaPorcentajes = d
							.getPorcVegetacionSecundaria().toString();
				else
					vegetacionSecundariaPorcentajes = vegetacionSecundariaPorcentajes
							+ ";" + d.getPorcVegetacionSecundaria().toString();
			}
			if (d.getPorcZonasQuemadas() > 0.1) {
				regionZonasQuemadas++;
				if (zonasQuemas.equals(""))
					zonasQuemas = d.getNombre();
				else
					zonasQuemas = zonasQuemas + ";" + d.getNombre();
				if (zonasQuemasHectareas.equals(""))
					zonasQuemasHectareas = d.getZonasQuemadas().toString();
				else
					zonasQuemasHectareas = zonasQuemasHectareas + ";"
							+ d.getZonasQuemadas().toString();
				if (zonasQuemasPorcentajes.equals(""))
					zonasQuemasPorcentajes = d.getPorcZonasQuemadas()
							.toString();
				else
					zonasQuemasPorcentajes = zonasQuemasPorcentajes + ";"
							+ d.getPorcZonasQuemadas().toString();
			}
		}

		nombreArbustales = arbustales.split(";");
		nombreAreaAgricolas = areasAgricolas.split(";");
		nombreAreasUrbanizadas = areasUrbanizadas.split(";");
		nombreCultivosPermanentes = cultivosPermanentes.split(";");
		nombreCultivosTransitorios = cultivosTransitorios.split(";");
		nombreHerbazales = herbazales.split(";");
		nombreOtrasAreas = otrasAreas.split(";");
		nombrePastos = pastos.split(";");
		nombreSuperficieAgua = superficieAgua.split(";");
		nombreVegetacionAcuatica = vegetacionAcuatica.split(";");
		nombreVegetacionSecundaria = vegetacionSecundaria.split(";");
		nombreZonasQuemadas = zonasQuemas.split(";");

		hectareasArbustales = arbustalesHectareas.split(";");
		hectareasAreaAgricolas = areasAgricolasHectareas.split(";");
		hectareasAreasUrbanizadas = areasUrbanizadasHectareas.split(";");
		hectareasCultivosPermanentes = cultivosPermanentesHectareas.split(";");
		hectareasCultivosTransitorios = cultivosTransitoriosHectareas
				.split(";");
		hectareasHerbazales = herbazalesHectareas.split(";");
		hectareasOtrasAreas = otrasAreasHectareas.split(";");
		hectareasPastos = pastosHectareas.split(";");
		hectareasSuperficieAgua = superficieAguaHectareas.split(";");
		hectareasVegetacionAcuatica = vegetacionAcuaticaHectareas.split(";");
		hectareasVegetacionSecundaria = vegetacionSecundariaHectareas
				.split(";");
		hectareasZonasQuemadas = zonasQuemasHectareas.split(";");
		
		porcentajeArbustales = arbustalesHectareas.split(";");
		porcentajeAreaAgricolas = areasAgricolasHectareas.split(";");
		porcentajeAreasUrbanizadas = areasUrbanizadasHectareas.split(";");
		porcentajeCultivosPermanentes = cultivosPermanentesHectareas.split(";");
		porcentajeCultivosTransitorios = cultivosTransitoriosHectareas
				.split(";");
		porcentajeHerbazales = herbazalesHectareas.split(";");
		porcentajeOtrasAreas = otrasAreasHectareas.split(";");
		porcentajePastos = pastosHectareas.split(";");
		porcentajeSuperficieAgua = superficieAguaHectareas.split(";");
		porcentajeVegetacionAcuatica = vegetacionAcuaticaHectareas.split(";");
		porcentajeVegetacionSecundaria = vegetacionSecundariaHectareas
				.split(";");
		porcentajeZonasQuemadas = zonasQuemasHectareas.split(";");

		porcentajes.clear();
		porcentajes.add(porcentajeAreasUrbanizadas);
		porcentajes.add(porcentajeCultivosTransitorios);
		porcentajes.add(porcentajeCultivosPermanentes);
		porcentajes.add(porcentajePastos);
		porcentajes.add(porcentajeAreaAgricolas);
		porcentajes.add(porcentajeHerbazales);
		porcentajes.add(porcentajeArbustales);
		porcentajes.add(porcentajeVegetacionSecundaria);
		porcentajes.add(porcentajeZonasQuemadas);
		porcentajes.add(porcentajeOtrasAreas);
		porcentajes.add(porcentajeVegetacionAcuatica);
		porcentajes.add(porcentajeSuperficieAgua);

		TotalConsolidado = 0;
		Double sumaTemp = 0d;
		for (int x = 0; x < porcentajes.size(); x++) {
			sumaTemp = 0d;
			for (int y = 0; y < porcentajes.get(x).length; y++) {
				if (!porcentajes.get(x)[y].equals("")
						&& porcentajes.get(x)[y] != null)
					sumaTemp = sumaTemp + Double.valueOf(porcentajes.get(x)[y]);
			}
			TotalConsolidado = TotalConsolidado + sumaTemp;
		}

		int tamano = regionArbustales + regionAreaAgricolas
				+ regionAreasUrbanizadas + regionCultivosPermanentes
				+ regionCultivosTransitorios + regionHerbazales
				+ regionOtrasAreas + regionPastos + regionSuperficieAgua
				+ regionVegetacionAcuatica + regionVegetacionSecundaria
				+ regionZonasQuemadas;
		filas = new XSSFRow[tamano];
		numeroRegiones.add(regionAreasUrbanizadas);
		numeroRegiones.add(regionCultivosTransitorios);
		numeroRegiones.add(regionCultivosPermanentes);
		numeroRegiones.add(regionPastos);
		numeroRegiones.add(regionAreaAgricolas);
		numeroRegiones.add(regionHerbazales);
		numeroRegiones.add(regionArbustales);
		numeroRegiones.add(regionVegetacionSecundaria);
		numeroRegiones.add(regionZonasQuemadas);
		numeroRegiones.add(regionOtrasAreas);
		numeroRegiones.add(regionVegetacionAcuatica);
		numeroRegiones.add(regionSuperficieAgua);
	}

	public XSSFCellStyle estiloDivisionReporte(XSSFWorkbook libro) {
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

	public XSSFCellStyle estiloTituloReporte(XSSFWorkbook libro) {
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

	public String getRutaArchivo() {
		return rutaArchivo;
	}

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	public ArrayList<Double[][]> getListaDatosWeb() {
		return webAreasHidrograficas;
	}

	public ArrayList<Double[]> getListaDatosConsolidadoWeb() {
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

}
