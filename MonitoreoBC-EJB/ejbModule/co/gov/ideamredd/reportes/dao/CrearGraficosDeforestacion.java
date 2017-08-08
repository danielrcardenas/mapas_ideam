// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

import java.util.ArrayList;
import java.util.ResourceBundle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import co.gov.ideamredd.reportes.entities.InformacionReporteDeforestacion;

/**
 * Clase usada para generar los graficos de deforestacion.
 */
public class CrearGraficosDeforestacion {

	private DefaultPieDataset result;
	private static final String archivoReportes = "co/gov/ideamredd/reportes/dao/reportesWPS";
	private static final ResourceBundle infoReportes = ResourceBundle
			.getBundle(archivoReportes);

	public DefaultPieDataset createDataset(
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion,
			int indice) {
		result = new DefaultPieDataset();
		InformacionReporteDeforestacion id = (InformacionReporteDeforestacion) infoDeforestacion
				.get(indice);
		adicionarInfo(result, id, null);
		return result;
	}

	public DefaultCategoryDataset createBarDataset(
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion,
			int indice) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		InformacionReporteDeforestacion id = (InformacionReporteDeforestacion) infoDeforestacion
				.get(indice);
		adicionarBarInfo(result, id, null);
		return result;
	}

	public DefaultPieDataset createDatasetConsolidado(
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion,
			ArrayList<String[]> porcentajes, double TotalConsolidado) {
		result = new DefaultPieDataset();
		adicionarInfoConsolidado(result, null, porcentajes, TotalConsolidado);

		return result;
	}

	public DefaultCategoryDataset createBarDatasetConsolidado(
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion,
			ArrayList<String[]> porcentajes, double TotalConsolidado) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		adicionarBarInfoConsolidado(result, null, porcentajes, TotalConsolidado);

		return result;
	}

	public DefaultPieDataset llenarDatosGrafWeb(String periodos,
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion,
			int indice, ArrayList<Double[][]> webAreasHidrograficas) {
		result = new DefaultPieDataset();
		InformacionReporteDeforestacion id = (InformacionReporteDeforestacion) infoDeforestacion
				.get(indice);
		adicionarWebInfo(result, id, null, indice, webAreasHidrograficas);
		return result;
	}

	public DefaultCategoryDataset createWebConsolidado(String periodos,
			ArrayList<InformacionReporteDeforestacion> infoDeforestacion,
			int indice, ArrayList<Double[]> webAreasHidrograficas,
			ArrayList<String[]> porcentajes, double TotalConsolidado) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		adicionarInfoWebConsolidado(result, porcentajes, indice,
				webAreasHidrograficas, TotalConsolidado);

		return result;
	}

	private void adicionarInfo(DefaultPieDataset result,
			InformacionReporteDeforestacion ib, ArrayList<String[]> porcentajes) {
		result.setValue(infoReportes.getString("reporte.deforestacion.item1"),
				ib.getPorcAreasUrbanizadas());
		result.setValue(infoReportes.getString("reporte.deforestacion.item2"),
				ib.getPorcCultivosTransitorios());
		result.setValue(infoReportes.getString("reporte.deforestacion.item3"),
				ib.getPorcCultivosPermanentes());
		result.setValue(infoReportes.getString("reporte.deforestacion.item4"),
				ib.getPorcPastosPlantacionForestal());
		result.setValue(infoReportes.getString("reporte.deforestacion.item5"),
				ib.getPorcAreasAgricolas());
		result.setValue(infoReportes.getString("reporte.deforestacion.item6"),
				ib.getPorcHerbazales());
		result.setValue(infoReportes.getString("reporte.deforestacion.item7"),
				ib.getPorcArbustales());
		result.setValue(infoReportes.getString("reporte.deforestacion.item8"),
				ib.getPorcVegetacionSecundaria());
		result.setValue(infoReportes.getString("reporte.deforestacion.item9"),
				ib.getPorcZonasQuemadas());
		result.setValue(infoReportes.getString("reporte.deforestacion.item10"),
				ib.getPorcOtrasAreasSinVegetacion());
		result.setValue(infoReportes.getString("reporte.deforestacion.item11"),
				ib.getPorcVegetacionAcuatica());
		result.setValue(infoReportes.getString("reporte.deforestacion.item12"),
				ib.getPorcSuperficiesAgua());
	}

	private void adicionarWebInfo(DefaultPieDataset result,
			InformacionReporteDeforestacion ib,
			ArrayList<String[]> porcentajes, int indice,
			ArrayList<Double[][]> webAreasHidrograficas) {

		webAreasHidrograficas.get(indice)[0][0] = ib.getPorcAreasUrbanizadas();
		webAreasHidrograficas.get(indice)[1][0] = ib
				.getPorcCultivosTransitorios();
		webAreasHidrograficas.get(indice)[2][0] = ib
				.getPorcCultivosPermanentes();
		webAreasHidrograficas.get(indice)[3][0] = ib
				.getPorcPastosPlantacionForestal();
		webAreasHidrograficas.get(indice)[4][0] = ib.getPorcAreasAgricolas();
		webAreasHidrograficas.get(indice)[5][0] = ib.getPorcHerbazales();
		webAreasHidrograficas.get(indice)[6][0] = ib.getPorcArbustales();
		webAreasHidrograficas.get(indice)[7][0] = ib
				.getPorcVegetacionSecundaria();
		webAreasHidrograficas.get(indice)[8][0] = ib.getPorcZonasQuemadas();
		webAreasHidrograficas.get(indice)[9][0] = ib
				.getPorcOtrasAreasSinVegetacion();
		webAreasHidrograficas.get(indice)[10][0] = ib
				.getPorcVegetacionAcuatica();
		webAreasHidrograficas.get(indice)[11][0] = ib.getPorcSuperficiesAgua();
	}

	private void adicionarBarInfo(DefaultCategoryDataset result,
			InformacionReporteDeforestacion ib, ArrayList<String[]> porcentajes) {
		result.addValue(ib.getPorcAreasUrbanizadas(),
				infoReportes.getString("reporte.deforestacion.item1"),
				infoReportes.getString("reporte.deforestacion.item1"));
		result.addValue(ib.getPorcCultivosTransitorios(),
				infoReportes.getString("reporte.deforestacion.item2"),
				infoReportes.getString("reporte.deforestacion.item2"));
		result.addValue(ib.getPorcCultivosPermanentes(),
				infoReportes.getString("reporte.deforestacion.item3"),
				infoReportes.getString("reporte.deforestacion.item3"));
		result.addValue(ib.getPorcPastosPlantacionForestal(),
				infoReportes.getString("reporte.deforestacion.item4"),
				infoReportes.getString("reporte.deforestacion.item4"));
		result.addValue(ib.getPorcAreasAgricolas(),
				infoReportes.getString("reporte.deforestacion.item5"),
				infoReportes.getString("reporte.deforestacion.item5"));
		result.addValue(ib.getPorcHerbazales(),
				infoReportes.getString("reporte.deforestacion.item6"),
				infoReportes.getString("reporte.deforestacion.item6"));
		result.addValue(ib.getPorcArbustales(),
				infoReportes.getString("reporte.deforestacion.item7"),
				infoReportes.getString("reporte.deforestacion.item7"));
		result.addValue(ib.getPorcVegetacionSecundaria(),
				infoReportes.getString("reporte.deforestacion.item8"),
				infoReportes.getString("reporte.deforestacion.item8"));
		result.addValue(ib.getPorcZonasQuemadas(),
				infoReportes.getString("reporte.deforestacion.item9"),
				infoReportes.getString("reporte.deforestacion.item9"));
		result.addValue(ib.getPorcOtrasAreasSinVegetacion(),
				infoReportes.getString("reporte.deforestacion.item10"),
				infoReportes.getString("reporte.deforestacion.item10"));
		result.addValue(ib.getPorcVegetacionAcuatica(),
				infoReportes.getString("reporte.deforestacion.item11"),
				infoReportes.getString("reporte.deforestacion.item11"));
		result.addValue(ib.getPorcSuperficiesAgua(),
				infoReportes.getString("reporte.deforestacion.item12"),
				infoReportes.getString("reporte.deforestacion.item12"));
	}

	private void adicionarInfoConsolidado(DefaultPieDataset result,
			InformacionReporteDeforestacion ib,
			ArrayList<String[]> porcentajes, double TotalConsolidado) {

		for (int i = 0; i < porcentajes.size(); i++) {
			switch (i) {
			case 1:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item1"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 2:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item2"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 3:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item3"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 4:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item4"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 5:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item5"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 6:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item6"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 7:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item7"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 8:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item8"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 9:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item9"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 10:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item10"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 11:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item11"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			case 12:
				result.setValue(
						infoReportes.getString("reporte.deforestacion.item12"),
						porcentaje(porcentajes.get(i - 1), TotalConsolidado));
				break;
			}
		}
	}

	private void adicionarInfoWebConsolidado(DefaultCategoryDataset result,
			ArrayList<String[]> porcentajes, int indice,
			ArrayList<Double[]> webAreasHidrograficas, double TotalConsolidado) {

		for (int i = 0; i < porcentajes.size(); i++) {
			webAreasHidrograficas.get(i)[0] = porcentaje(porcentajes.get(i),
					TotalConsolidado);
		}
	}

	private void adicionarBarInfoConsolidado(DefaultCategoryDataset result,
			InformacionReporteDeforestacion ib,
			ArrayList<String[]> porcentajes, double TotalConsolidado) {

		for (int i = 0; i < porcentajes.size(); i++) {
			switch (i) {
			case 1:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item1"),
						infoReportes.getString("reporte.deforestacion.item1"));
				break;
			case 2:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item2"),
						infoReportes.getString("reporte.deforestacion.item2"));
				break;
			case 3:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item3"),
						infoReportes.getString("reporte.deforestacion.item3"));
				break;
			case 4:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item4"),
						infoReportes.getString("reporte.deforestacion.item4"));
				break;
			case 5:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item5"),
						infoReportes.getString("reporte.deforestacion.item5"));
				break;
			case 6:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item6"),
						infoReportes.getString("reporte.deforestacion.item6"));
				break;
			case 7:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item7"),
						infoReportes.getString("reporte.deforestacion.item7"));
				break;
			case 8:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item8"),
						infoReportes.getString("reporte.deforestacion.item8"));
				break;
			case 9:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item9"),
						infoReportes.getString("reporte.deforestacion.item9"));
				break;
			case 10:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item10"),
						infoReportes.getString("reporte.deforestacion.item10"));
				break;
			case 11:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item11"),
						infoReportes.getString("reporte.deforestacion.item11"));
				break;
			case 12:
				result.addValue(
						porcentaje(porcentajes.get(i - 1), TotalConsolidado),
						infoReportes.getString("reporte.deforestacion.item12"),
						infoReportes.getString("reporte.deforestacion.item12"));
				break;
			}
		}
	}

	private Double porcentaje(String[] info, double TotalConsolidado) {
		double totalPorcentajes = 0;
		for (int j = 0; j < info.length; j++) {
			if (!info[j].equals(""))
				totalPorcentajes += Double.valueOf(info[j]);
		}
		totalPorcentajes = ((totalPorcentajes / TotalConsolidado) * 100d);
		return definirDecimales(totalPorcentajes,4);
	}

	public double definirDecimales(double numero, int decimales) {
		return Math.round(numero * Math.pow(10, decimales))
				/ Math.pow(10, decimales);
	}

	public JFreeChart createChart(DefaultPieDataset dataset, String periodos,
			String areaHidro) {
		JFreeChart chart = ChartFactory.createPieChart(
				infoReportes.getString("reporte.deforestacion.tituloGrafica")
						+ " " + areaHidro + " " + periodos, dataset, true,
				true, false);

		final PiePlot plot = (PiePlot) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setNoDataMessage("No data to display");
		return chart;

	}

	public JFreeChart createBarChart(DefaultCategoryDataset dataset,
			String periodos, String areaHidro) {
		JFreeChart chart = ChartFactory.createStackedBarChart3D(
				infoReportes.getString("reporte.deforestacion.tituloGrafica")
						+ " " + areaHidro + " " + periodos, "", "Valor",
				dataset, PlotOrientation.HORIZONTAL, true, false, false);
		return chart;

	}

	public JFreeChart createChartConsolidado(DefaultPieDataset dataset,
			String periodos) {
		JFreeChart chart = ChartFactory.createPieChart(
				infoReportes.getString("reporte.deforestacion.tituloGrafica")
						+ " " + periodos, dataset, true, true, false);

		final PiePlot plot = (PiePlot) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setNoDataMessage("No data to display");
		return chart;

	}

	public JFreeChart createBarChartConsolidado(DefaultCategoryDataset dataset,
			String periodos) {
		JFreeChart chart = ChartFactory.createStackedBarChart3D(
				infoReportes.getString("reporte.deforestacion.tituloGrafica")
						+ " " + periodos, "", "Valor", dataset,
				PlotOrientation.HORIZONTAL, true, false, false);
		return chart;

	}

}