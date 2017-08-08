package co.gov.ideamredd.reportes;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.time.Year;
import org.jfree.util.Rotation;

import co.gov.ideamredd.entities.InformacionReporteBosque;

@Stateless
public class CrearGraficosBNB {

	@EJB
	ConsultarAsociadosReporte car;

	private DefaultCategoryDataset result;
	private static final String archivoReportes = "co/gov/ideamredd/reportes/reportesWPS";
	private static final ResourceBundle infoReportes = ResourceBundle
			.getBundle(archivoReportes);
	private String[] periodo;
	private DefaultPieDataset resultConsolidado;
	private DefaultCategoryDataset resultConsolidadoBar;
	private TimeSeriesCollection resultConsolidadoTimeS;
	private String nombreImagen;

	public DefaultCategoryDataset createDataset(String[] periodo,
			Integer idReporte, int tipoGrafica) {
		this.periodo = periodo;
		result = new DefaultCategoryDataset();
		ArrayList<InformacionReporteBosque> b = car
				.consultarInfoBosque(idReporte);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteBosque ib = b.get(j);
			adicionarInfo(result, ib, tipoGrafica, 0);
		}
		return result;
	}

	public DefaultPieDataset createPieDataset(String[] periodo,
			Integer idReporte, int tipoGrafica) {
		this.periodo = periodo;
		DefaultPieDataset datos = new DefaultPieDataset();
		ArrayList<InformacionReporteBosque> b = car
				.consultarInfoBosque(idReporte);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteBosque ib = b.get(j);
			adicionarInfoPie(datos, ib, tipoGrafica, 0);
		}
		return datos;
	}

	public TimeSeriesCollection createTimeseriesDataset(String[] periodo,
			Integer[] idReporte, int tipoGrafica) {
		this.periodo = periodo;
		TimeSeriesCollection datos = new TimeSeriesCollection();
		adicionarInfoTimeseries(datos, tipoGrafica, idReporte);
		return datos;
	}

	// Para llenar los datos de las graficas de la pagina web
	public DefaultCategoryDataset llenarDatosGrafWeb(String[] periodo,
			Integer idReporte, int indicePeriodo, int tipoGrafica,
			ArrayList<Double[][][]> webDatosGraficas) {
		this.periodo = periodo;
		result = new DefaultCategoryDataset();
		ArrayList<InformacionReporteBosque> b = car
				.consultarInfoBosque(idReporte);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteBosque ib = b.get(j);
			adicionarInfoWeb(result, ib, tipoGrafica, indicePeriodo, j,
					webDatosGraficas);
		}
		return result;
	}

	public DefaultCategoryDataset llenarDatosConsolidadoGrafWeb(
			ArrayList<Double> porcentajes, String[] periodos,
			ArrayList<Double[][]> webConsolidadoNacional) {
		int i = 0;
		resultConsolidadoBar = new DefaultCategoryDataset();
		adicionarInfoWebConsolidado(resultConsolidadoBar, i, porcentajes,
				periodos, webConsolidadoNacional);
		i++;
		return resultConsolidadoBar;
	}

	// ************************** creacion info para consolidado
	// nacional*****************************
	public DefaultPieDataset createPieDatasetConsolidado(
			ArrayList<Double> porcentajes) {
		int i = 0;
		while (i < Integer.valueOf(infoReportes
				.getString("BosqueNoBosqueCategorias"))) {
			resultConsolidado = new DefaultPieDataset();
			adicionarInfoPieConsolidado(resultConsolidado, porcentajes);
			i++;
		}
		return resultConsolidado;
	}

	public DefaultCategoryDataset createBarDatasetConsolidado(
			ArrayList<Double> porcentajes) {
		int i = 0;
		while (i < Integer.valueOf(infoReportes
				.getString("BosqueNoBosqueCategorias"))) {
			resultConsolidadoBar = new DefaultCategoryDataset();
			adicionarInfoBarConsolidado(resultConsolidadoBar, porcentajes);
			i++;
		}
		return resultConsolidadoBar;
	}

	public TimeSeriesCollection createTimeSDatasetConsolidado(
			ArrayList<Double> porcentajesB, ArrayList<Double> porcentajesNB,
			ArrayList<Double> porcentajesSI, ArrayList<String> nombresPer) {
		resultConsolidadoTimeS = new TimeSeriesCollection();
		adicionarInfoTimeSConsolidado(resultConsolidadoTimeS, porcentajesB,
				porcentajesNB, porcentajesSI, nombresPer);
		return resultConsolidadoTimeS;
	}

	// **************************Fin creacion info para consolidado
	// nacional*****************************

	private void adicionarInfo(DefaultCategoryDataset result,
			InformacionReporteBosque ib, Integer tipoGrafica, int i) {
		if (tipoGrafica == 0) {
			result.addValue(ib.getPorcentajeBosque(), "Bosque", ib.getNombre());
			result.addValue(ib.getPorcentajeNoBosque(), "No Bosque",
					ib.getNombre());
			result.addValue(ib.getPorcentajeSinInfo(), "Sin Informacion",
					ib.getNombre());
		} else if (tipoGrafica == 1) {
			result.addValue(ib.getPorcentajeBosque(), ib.getNombre(), "Bosque");
		} else if (tipoGrafica == 2) {
			result.addValue(ib.getPorcentajeNoBosque(), ib.getNombre(),
					"No Bosque");
		} else if (tipoGrafica == 3) {
			result.addValue(ib.getPorcentajeSinInfo(), ib.getNombre(),
					"Sin Informacion");
		}
	}

	private void adicionarInfoPie(DefaultPieDataset result,
			InformacionReporteBosque ib, Integer tipoGrafica, int i) {
		if (tipoGrafica == 0) {
			result.setValue(ib.getNombre(), ib.getPorcentajeBosque());
			result.setValue(ib.getNombre(), ib.getPorcentajeNoBosque());
			result.setValue(ib.getNombre(), ib.getPorcentajeSinInfo());
		} else if (tipoGrafica == 1) {
			result.setValue(ib.getNombre(), ib.getPorcentajeBosque());
		} else if (tipoGrafica == 2) {
			result.setValue(ib.getNombre(), ib.getPorcentajeNoBosque());
		} else if (tipoGrafica == 3) {
			result.setValue(ib.getNombre(), ib.getPorcentajeSinInfo());
		}
	}

	private void adicionarInfoTimeseries(TimeSeriesCollection result,
			Integer tipoGrafica, Integer[] idReporte) {

		ArrayList<TimeSeries> timeSeries = new ArrayList<TimeSeries>();

		ArrayList<InformacionReporteBosque> b = car
				.consultarInfoBosque(idReporte[0]);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteBosque ib = b.get(j);
			timeSeries.add(new TimeSeries(ib.getNombre()));
		}

		if (tipoGrafica == 0) {
			for (int j = 0; j < idReporte.length; j++) {
				b = car.consultarInfoBosque(idReporte[j]);
				for (int k = 0; k < b.size(); k++) {
					InformacionReporteBosque ib = b.get(k);
					timeSeries.get(k).add(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodo[j])), ib
									.getPorcentajeBosque()));
				}
			}
		} else if (tipoGrafica == 1) {
			for (int j = 0; j < idReporte.length; j++) {
				b = car.consultarInfoBosque(idReporte[j]);
				for (int k = 0; k < b.size(); k++) {
					InformacionReporteBosque ib = b.get(k);
					timeSeries.get(k).add(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodo[j])), ib
									.getPorcentajeNoBosque()));
				}
			}
		} else if (tipoGrafica == 2) {
			for (int j = 0; j < idReporte.length; j++) {
				b = car.consultarInfoBosque(idReporte[j]);
				for (int k = 0; k < b.size(); k++) {
					InformacionReporteBosque ib = b.get(k);
					timeSeries.get(k).add(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodo[j])), ib
									.getPorcentajeSinInfo()));
				}
			}
		}

		for (int i = 0; i < timeSeries.size(); i++) {
			result.addSeries(timeSeries.get(i));
		}

	}

	// ************************** adicion info para consolidado
	// nacional*****************************
	private void adicionarInfoPieConsolidado(DefaultPieDataset result,
			ArrayList<Double> porcentajes) {
		result.setValue(infoReportes.getString("reporte.bnb.item1"),
				porcentajes.get(0));
		result.setValue(infoReportes.getString("reporte.bnb.item2"),
				porcentajes.get(1));
		result.setValue(infoReportes.getString("reporte.bnb.item3"),
				porcentajes.get(2));
	}

	private void adicionarInfoBarConsolidado(DefaultCategoryDataset result,
			ArrayList<Double> porcentajes) {
		result.addValue(porcentajes.get(0),
				infoReportes.getString("reporte.bnb.item1"), "Bosque");
		result.addValue(porcentajes.get(1),
				infoReportes.getString("reporte.bnb.item2"), "Bosque");
		result.addValue(porcentajes.get(2),
				infoReportes.getString("reporte.bnb.item3"), "Bosque");
	}

	private void adicionarInfoTimeSConsolidado(TimeSeriesCollection result,
			ArrayList<Double> porcentajesB, ArrayList<Double> porcentajesNB,
			ArrayList<Double> porcentajesSI, ArrayList<String> nombresPer) {

		TimeSeries timeSeriesB = new TimeSeries(
				infoReportes.getString("reporte.bnb.item1"));
		TimeSeries timeSeriesNB = new TimeSeries(
				infoReportes.getString("reporte.bnb.item2"));
		TimeSeries timeSeriesSI = new TimeSeries(
				infoReportes.getString("reporte.bnb.item3"));

		for (int i = 0; i < nombresPer.size(); i++) {
			timeSeriesB.add(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i))), porcentajesB.get(i)));
			timeSeriesNB.add(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i))), porcentajesNB.get(i)));
			timeSeriesSI.add(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i))), porcentajesSI.get(i)));
		}
		result.addSeries(timeSeriesB);
		result.addSeries(timeSeriesNB);
		result.addSeries(timeSeriesSI);
	}

	// ***************************Fin agregar info Consolidado
	// nacional*********************************

	private void adicionarInfoWeb(DefaultCategoryDataset result,
			InformacionReporteBosque ib, Integer tipoGrafica,
			int indicePeriodo, int indiceDivision,
			ArrayList<Double[][][]> webDatosGraficas) {

		if (tipoGrafica == 1) {
			webDatosGraficas.get(indicePeriodo)[indiceDivision][0][0] = ib
					.getPorcentajeBosque();
		} else if (tipoGrafica == 2) {
			webDatosGraficas.get(indicePeriodo)[indiceDivision][1][0] = ib
					.getPorcentajeNoBosque();
		} else if (tipoGrafica == 3) {
			webDatosGraficas.get(indicePeriodo)[indiceDivision][2][0] = ib
					.getPorcentajeSinInfo();
		}

	}

	private void adicionarInfoWebConsolidado(DefaultCategoryDataset result,
			int indiceTBosque, ArrayList<Double> porcentajes,
			String[] periodos, ArrayList<Double[][]> webConsolidadoNacional) {

		int dat = 0;
		int x = 0;
		for (int y = 0; y < periodos.length; y++) {
			x = dat;
			while (x < porcentajes.size()) {
				webConsolidadoNacional.get(y)[0][0] = porcentajes.get(x);
				webConsolidadoNacional.get(y)[1][0] = porcentajes.get(x + 1);
				webConsolidadoNacional.get(y)[2][0] = porcentajes.get(x + 2);
				dat = x + 3;
				break;
			}
		}

	}

	public JFreeChart createPieChartConsolidado(final CategoryDataset dataset,
			int grafica, DefaultPieDataset datapie, String periodo) {
		JFreeChart chart = null;
		chart = ChartFactory.createPieChart(
				infoReportes.getString("reporte.bnb.tituloGrafica") + periodo,
				datapie, true, true, false);

		final PiePlot plot = (PiePlot) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		plot.setNoDataMessage("No data to display");
		return chart;
	}

	public JFreeChart createBarChartConsolidado(final CategoryDataset dataset,
			int grafica, DefaultCategoryDataset data, String periodo) {
		JFreeChart chart = null;
		chart = ChartFactory.createBarChart3D(
				infoReportes.getString("reporte.bnb.tituloGrafica") + periodo,
				"Bosque/No Bosque/Sin Info", "Valor", data,
				PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}

	public JFreeChart createTimeSChartConsolidado(
			final CategoryDataset dataset, int grafica,
			TimeSeriesCollection data, String periodo) {
		JFreeChart chart = null;
		chart = ChartFactory.createTimeSeriesChart("Bosque/No Bosque/Sin Info",
				"Periodo", "Valor", data, true, false, false);
		return chart;
	}

	public JFreeChart createChartBarras(final CategoryDataset dataset,
			int grafica, DefaultPieDataset datapie, String periodo) {
		JFreeChart chart = null;
		if (dataset != null)
			if (grafica == 0)
				chart = ChartFactory.createStackedBarChart3D(
						infoReportes.getString("reporte.bnb.tituloGrafica")
								+ periodo, "", "Porcentaje(%)", dataset,
						PlotOrientation.HORIZONTAL, true, false, false);
			else {
				String titulo = "";
				if (grafica == 1)
					titulo = infoReportes
							.getString("reporte.bnb.tituloGrafica2") + periodo;
				else if (grafica == 2)
					titulo = infoReportes
							.getString("reporte.bnb.tituloGrafica3") + periodo;
				else if (grafica == 3)
					titulo = infoReportes
							.getString("reporte.bnb.tituloGrafica4") + periodo;

				chart = ChartFactory.createBarChart3D(titulo, "",
						"Porcentaje(%)", dataset, PlotOrientation.VERTICAL,
						true, false, true);
			}
		else {
			chart = ChartFactory.createPieChart3D(
					infoReportes.getString("reporte.bnb.tituloGrafica")
							+ periodo, datapie, true, true, false);

			final PiePlot3D plot = (PiePlot3D) chart.getPlot();
			plot.setStartAngle(290);
			plot.setDirection(Rotation.CLOCKWISE);
			plot.setForegroundAlpha(0.5f);
			plot.setNoDataMessage("No data to display");
		}
		return chart;
	}

	public JFreeChart createChartTortas(DefaultPieDataset pdataset,
			int grafica, String periodo) {
		JFreeChart chart = null;
		if (pdataset != null) {
			if (grafica == 0) {
				chart = ChartFactory.createPieChart(
						infoReportes.getString("reporte.bnb.tituloGrafica")
								+ periodo, pdataset, true, true, false);
			} else {
				String titulo = "";
				if (grafica == 1) {
					titulo = infoReportes
							.getString("reporte.bnb.tituloGrafica2") + periodo;
				} else if (grafica == 2) {
					titulo = infoReportes
							.getString("reporte.bnb.tituloGrafica3") + periodo;
				} else if (grafica == 3)
					titulo = infoReportes
							.getString("reporte.bnb.tituloGrafica4") + periodo;

				chart = ChartFactory.createPieChart(titulo, pdataset, true,
						true, false);
			}
		}
		return chart;
	}

	public JFreeChart createChartTimeseries(TimeSeriesCollection dataset,
			int grafica) {
		JFreeChart chart = null;
		if (dataset != null) {
			String titulo = "";
			if (grafica == 0) {
				titulo = infoReportes.getString("reporte.bnb.tituloGrafica2");
			} else if (grafica == 1) {
				titulo = infoReportes.getString("reporte.bnb.tituloGrafica3");
			} else if (grafica == 2)
				titulo = infoReportes.getString("reporte.bnb.tituloGrafica4");

			chart = ChartFactory.createTimeSeriesChart(titulo, "Periodo",
					"Valor", dataset, true, false, false);
		}
		return chart;
	}

	public String getNombreImagen() {
		return nombreImagen;
	}

}
