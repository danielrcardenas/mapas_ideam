// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

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

import co.gov.ideamredd.reportes.entities.InformacionReporteCobertura;

/**
 * Clase usada para crear los graficos de coberturas.
 */
@Stateless
public class CrearGraficosCobertura {

	@EJB
	ConsultarAsociadosReporte car;

	private DefaultCategoryDataset result;
	private static final String archivoReportes = "co/gov/ideamredd/reportes/dao/reportesWPS";
	private static final ResourceBundle infoReportes = ResourceBundle
			.getBundle(archivoReportes);
	@SuppressWarnings("unused")
	private String periodo;
	private String[] periodolista;
	private DefaultPieDataset resultConsolidado;

	public DefaultCategoryDataset createDataset(String periodo,
			Integer idReporte, int tipoGrafica) {
		this.periodo = periodo;
		result = new DefaultCategoryDataset();
		ArrayList<InformacionReporteCobertura> b = car
				.consultarInfoCobertura(idReporte);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteCobertura ib = b.get(j);
			adicionarInfo(result, ib, tipoGrafica, j);
		}
		return result;
	}

	public DefaultPieDataset createPieDataset(String periodo,
			Integer idReporte, int tipoGrafica) {
		this.periodo = periodo;
		DefaultPieDataset datos = new DefaultPieDataset();
		ArrayList<InformacionReporteCobertura> b = car
				.consultarInfoCobertura(idReporte);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteCobertura ib = b.get(j);
			adicionarInfoPie(datos, ib, tipoGrafica, j);
		}
		return datos;
	}

	public TimeSeriesCollection createTimeseriesDataset(String[] periodo,
			Integer[] idReporte, int tipoGrafica) {
		this.periodolista = periodo;
		TimeSeriesCollection datos = new TimeSeriesCollection();
		adicionarInfoTimeseries(datos, tipoGrafica, idReporte);
		return datos;
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
		DefaultCategoryDataset resultConsolidadoBar = null;
		while (i < Integer.valueOf(infoReportes
				.getString("BosqueNoBosqueCategorias"))) {
			resultConsolidadoBar = new DefaultCategoryDataset();
			adicionarInfoBarConsolidado(resultConsolidadoBar, porcentajes);
			i++;
		}
		return resultConsolidadoBar;
	}

	public TimeSeriesCollection createTimeSDatasetConsolidado(
			ArrayList<Double> porcentajesBE, ArrayList<Double> porcentajesNBE,
			ArrayList<Double> porcentajesDE, ArrayList<Double> porcentajesRE,
			ArrayList<Double> porcentajesSI, ArrayList<String> nombresPer) {
		TimeSeriesCollection resultConsolidadoTimeS = null;
		resultConsolidadoTimeS = new TimeSeriesCollection();
		adicionarInfoTimeSConsolidado(resultConsolidadoTimeS, porcentajesBE,
				porcentajesNBE, porcentajesDE, porcentajesRE, porcentajesSI,
				nombresPer);
		return resultConsolidadoTimeS;
	}

	// Para llenar los datos de las graficas de la pagina web
	public DefaultCategoryDataset llenarDatosGrafWeb(String[] periodo,
			Integer idReporte, int indicePeriodo, int tipoGrafica,
			ArrayList<Double[][][]> webDatosGraficas) {

		result = new DefaultCategoryDataset();
		ArrayList<InformacionReporteCobertura> b = car
				.consultarInfoCobertura(idReporte);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteCobertura ib = b.get(j);
			adicionarInfoWeb(result, ib, tipoGrafica, indicePeriodo, j,
					webDatosGraficas);
		}
		return result;
	}

	public DefaultCategoryDataset llenarDatosConsolidadoGrafWeb(
			ArrayList<Double> porcentajes, String[] periodos,
			ArrayList<Double[][]> webConsolidadoNacional) {
		int i = 0;
		DefaultCategoryDataset resultConsolidadoBar = null;
		resultConsolidadoBar = new DefaultCategoryDataset();
		adicionarInfoWebConsolidado(resultConsolidadoBar, i, porcentajes,
				periodos, webConsolidadoNacional);
		i++;
		return resultConsolidadoBar;
	}

	private void adicionarInfo(DefaultCategoryDataset result,
			InformacionReporteCobertura ib, int tipoGrafica, int i) {
		if (tipoGrafica == 0) {
			result.addValue(ib.getPorcBosque(), "Bosque Estable",
					ib.getNombre());
			result.addValue(ib.getPorcNoBosque(), "No Bosque Estable",
					ib.getNombre());
			result.addValue(ib.getPorcDeforestacion(), "Deforestacion",
					ib.getNombre());
			result.addValue(ib.getPorcRegeneracion(), "Regeneracion",
					ib.getNombre());
			result.addValue(ib.getPorcSinInfo(), "Sin Informacion Estable",
					ib.getNombre());
		} else if (tipoGrafica == 1) {
			result.addValue(ib.getPorcBosque(), ib.getNombre(),
					"Bosque Estable");
		} else if (tipoGrafica == 2) {
			result.addValue(ib.getPorcNoBosque(), ib.getNombre(),
					"No Bosque Estable");
		} else if (tipoGrafica == 3) {
			result.addValue(ib.getPorcDeforestacion(), ib.getNombre(),
					"Deferostacion");
		} else if (tipoGrafica == 4) {
			result.addValue(ib.getPorcRegeneracion(), ib.getNombre(),
					"Regeneracion");
		} else if (tipoGrafica == 5) {
			result.addValue(ib.getPorcSinInfo(), ib.getNombre(),
					"Sin Informacion Estable");
		}
	}

	// adicion de datos para las graficas en la pagina web
	private void adicionarInfoWeb(DefaultCategoryDataset result,
			InformacionReporteCobertura ib, Integer tipoGrafica,
			int indicePeriodo, int indiceDivision,
			ArrayList<Double[][][]> webDatosGraficas) {

		if (tipoGrafica == 1) {
			webDatosGraficas.get(indicePeriodo)[indiceDivision][0][0] = ib
					.getPorcBosque();
		} else if (tipoGrafica == 2) {
			webDatosGraficas.get(indicePeriodo)[indiceDivision][1][0] = ib
					.getPorcNoBosque();
		} else if (tipoGrafica == 3) {
			webDatosGraficas.get(indicePeriodo)[indiceDivision][2][0] = ib
					.getPorcDeforestacion();
		} else if (tipoGrafica == 4) {
			webDatosGraficas.get(indicePeriodo)[indiceDivision][3][0] = ib
					.getPorcRegeneracion();
		} else if (tipoGrafica == 5) {
			webDatosGraficas.get(indicePeriodo)[indiceDivision][4][0] = ib
					.getPorcSinInfo();
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
				webConsolidadoNacional.get(y)[3][0] = porcentajes.get(x + 3);
				webConsolidadoNacional.get(y)[4][0] = porcentajes.get(x + 4);
				dat = x + 3;
				break;
			}
		}

	}

	private void adicionarInfoPie(DefaultPieDataset result,
			InformacionReporteCobertura ib, int tipoGrafica, int i) {
		if (tipoGrafica == 0) {
			result.setValue("Bosque Estable", ib.getPorcBosque());
			result.setValue("No Bosque Estable", ib.getPorcNoBosque());
			result.setValue("Deforestacion", ib.getPorcNoBosque());
			result.setValue("Regeneracion", ib.getPorcNoBosque());
			result.setValue("Sin Informacion Estable", ib.getPorcNoBosque());
		} else if (tipoGrafica == 1) {
			result.setValue(ib.getNombre(), ib.getPorcBosque());
			// "Bosque Estable"
		} else if (tipoGrafica == 2) {
			result.setValue(ib.getNombre(), ib.getPorcNoBosque());
			// "No Bosque Estable"
		} else if (tipoGrafica == 3) {
			result.setValue(ib.getNombre(), ib.getPorcDeforestacion());
			// "Deferostacion"
		} else if (tipoGrafica == 4) {
			result.setValue(ib.getNombre(), ib.getPorcRegeneracion());
			// "Regeneracion"
		} else if (tipoGrafica == 5) {
			result.setValue(ib.getNombre(), ib.getPorcSinInfo());
			// "Sin Informacion Estable"
		}
	}

	private void adicionarInfoTimeseries(TimeSeriesCollection result,
			Integer tipoGrafica, Integer[] idReporte) {

		ArrayList<TimeSeries> timeSeries = new ArrayList<TimeSeries>();

		ArrayList<InformacionReporteCobertura> b = car
				.consultarInfoCobertura(idReporte[0]);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteCobertura ib = b.get(j);
			timeSeries.add(new TimeSeries(ib.getNombre()));
		}

		if (tipoGrafica == 0) {
			for (int j = 0; j < idReporte.length; j++) {
				b = car.consultarInfoCobertura(idReporte[j]);
				for (int k = 0; k < b.size(); k++) {
					InformacionReporteCobertura ib = b.get(k);
					timeSeries.get(k).addOrUpdate(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodolista[j].substring(0, 4))),
									ib.getPorcBosque()));
					timeSeries.get(k).addOrUpdate(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodolista[j].substring(5, 9))),
									ib.getPorcBosque()));
				}
			}
		} else if (tipoGrafica == 1) {
			for (int j = 0; j < idReporte.length; j++) {
				b = car.consultarInfoCobertura(idReporte[j]);
				for (int k = 0; k < b.size(); k++) {
					InformacionReporteCobertura ib = b.get(k);
					timeSeries.get(k).addOrUpdate(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodolista[j].substring(0, 4))),
									ib.getPorcNoBosque()));
					timeSeries.get(k).addOrUpdate(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodolista[j].substring(5, 9))),
									ib.getPorcNoBosque()));
				}
			}
		} else if (tipoGrafica == 2) {
			for (int j = 0; j < idReporte.length; j++) {
				b = car.consultarInfoCobertura(idReporte[j]);
				for (int k = 0; k < b.size(); k++) {
					InformacionReporteCobertura ib = b.get(k);
					timeSeries.get(k).addOrUpdate(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodolista[j].substring(0, 4))),
									ib.getPorcDeforestacion()));
					timeSeries.get(k).addOrUpdate(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodolista[j].substring(5, 9))),
									ib.getPorcDeforestacion()));
				}
			}
		} else if (tipoGrafica == 3) {
			for (int j = 0; j < idReporte.length; j++) {
				b = car.consultarInfoCobertura(idReporte[j]);
				for (int k = 0; k < b.size(); k++) {
					InformacionReporteCobertura ib = b.get(k);
					timeSeries.get(k).addOrUpdate(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodolista[j].substring(0, 4))),
									ib.getPorcRegeneracion()));
					timeSeries.get(k).addOrUpdate(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodolista[j].substring(5, 9))),
									ib.getPorcRegeneracion()));
				}
			}
		} else if (tipoGrafica == 4) {
			for (int j = 0; j < idReporte.length; j++) {
				b = car.consultarInfoCobertura(idReporte[j]);
				for (int k = 0; k < b.size(); k++) {
					InformacionReporteCobertura ib = b.get(k);
					timeSeries.get(k).addOrUpdate(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodolista[j].substring(0, 4))),
									ib.getPorcSinInfo()));
					timeSeries.get(k).addOrUpdate(
							new TimeSeriesDataItem(new Year(Integer
									.valueOf(periodolista[j].substring(5, 9))),
									ib.getPorcNoBosque()));
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
		result.setValue(infoReportes.getString("reporte.cobertura.item1"),
				porcentajes.get(0));
		result.setValue(infoReportes.getString("reporte.cobertura.item2"),
				porcentajes.get(3));
		result.setValue(infoReportes.getString("reporte.cobertura.item3"),
				porcentajes.get(2));
		result.setValue(infoReportes.getString("reporte.cobertura.item4"),
				porcentajes.get(4));
		result.setValue(infoReportes.getString("reporte.cobertura.item5"),
				porcentajes.get(1));
	}

	private void adicionarInfoBarConsolidado(DefaultCategoryDataset result,
			ArrayList<Double> porcentajes) {

		result.addValue(porcentajes.get(0),
				infoReportes.getString("reporte.cobertura.item1"), "");
		result.addValue(porcentajes.get(3),
				infoReportes.getString("reporte.cobertura.item2"), "");
		result.addValue(porcentajes.get(2),
				infoReportes.getString("reporte.cobertura.item3"), "");
		result.addValue(porcentajes.get(4),
				infoReportes.getString("reporte.cobertura.item4"), "");
		result.addValue(porcentajes.get(1),
				infoReportes.getString("reporte.cobertura.item5"), "");
	}

	private void adicionarInfoTimeSConsolidado(TimeSeriesCollection result,
			ArrayList<Double> porcentajesBE, ArrayList<Double> porcentajesNBE,
			ArrayList<Double> porcentajesDE, ArrayList<Double> porcentajesRE,
			ArrayList<Double> porcentajesSI, ArrayList<String> nombresPer) {

		// reporte.cobertura.item1=Bosque Estable(ha)
		// reporte.cobertura.item2=No Bosque Estable(ha)
		// reporte.cobertura.item3=Deforestación(ha)
		// reporte.cobertura.item4=Regeneración()ha
		// reporte.cobertura.item5=Sin Información(ha)

		TimeSeries timeSeriesBE = new TimeSeries(
				infoReportes.getString("reporte.cobertura.item1"));
		TimeSeries timeSeriesNBE = new TimeSeries(
				infoReportes.getString("reporte.cobertura.item2"));
		TimeSeries timeSeriesDE = new TimeSeries(
				infoReportes.getString("reporte.cobertura.item3"));
		TimeSeries timeSeriesRE = new TimeSeries(
				infoReportes.getString("reporte.cobertura.item4"));
		TimeSeries timeSeriesSI = new TimeSeries(
				infoReportes.getString("reporte.cobertura.item5"));

		for (int i = 0; i < nombresPer.size(); i++) {
			timeSeriesBE.addOrUpdate(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i).substring(0, 4))), porcentajesBE
					.get(i)));
			timeSeriesBE.addOrUpdate(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i).substring(5, 9))), porcentajesBE
					.get(i)));

			timeSeriesNBE.addOrUpdate(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i).substring(0, 4))),
					porcentajesNBE.get(i)));
			timeSeriesNBE.addOrUpdate(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i).substring(5, 9))),
					porcentajesNBE.get(i)));

			timeSeriesDE.addOrUpdate(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i).substring(0, 4))), porcentajesDE
					.get(i)));
			timeSeriesDE.addOrUpdate(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i).substring(5, 9))), porcentajesDE
					.get(i)));

			timeSeriesRE.addOrUpdate(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i).substring(0, 4))), porcentajesRE
					.get(i)));
			timeSeriesRE.addOrUpdate(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i).substring(5, 9))), porcentajesRE
					.get(i)));

			timeSeriesSI.addOrUpdate(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i).substring(0, 4))), porcentajesSI
					.get(i)));
			timeSeriesSI.addOrUpdate(new TimeSeriesDataItem(new Year(Integer
					.valueOf(nombresPer.get(i).substring(5, 9))), porcentajesSI
					.get(i)));
		}
		result.addSeries(timeSeriesBE);
		result.addSeries(timeSeriesNBE);
		result.addSeries(timeSeriesDE);
		result.addSeries(timeSeriesRE);
		result.addSeries(timeSeriesSI);
	}

	public JFreeChart createChartBarras(final CategoryDataset dataset,
			int grafica, DefaultPieDataset datapie, String periodo) {
		JFreeChart chart;
		if (dataset != null)
			if (grafica == 0)
				chart = ChartFactory.createStackedBarChart3D(
						infoReportes
								.getString("reporte.cobertura.tituloGrafica")
								+ " " + periodo, "", "Porcentaje(%)", dataset,
						PlotOrientation.HORIZONTAL, true, false, false);
			else {
				String titulo = "";
				if (grafica == 1)
					titulo = infoReportes
							.getString("reporte.cobertura.tituloGrafica2");
				else if (grafica == 2)
					titulo = infoReportes
							.getString("reporte.cobertura.tituloGrafica3");
				else if (grafica == 3)
					titulo = infoReportes
							.getString("reporte.cobertura.tituloGrafica4");
				else if (grafica == 4)
					titulo = infoReportes
							.getString("reporte.cobertura.tituloGrafica5");
				else if (grafica == 5)
					titulo = infoReportes
							.getString("reporte.cobertura.tituloGrafica6");

				chart = ChartFactory.createBarChart3D(titulo + " " + periodo,
						"", "Porcentaje(%)", dataset, PlotOrientation.VERTICAL,
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
						infoReportes
								.getString("reporte.cobertura.tituloGrafica")
								+ " " + periodo, pdataset, true, true, false);
			} else {
				String titulo = "";
				if (grafica == 1)
					titulo = infoReportes
							.getString("reporte.cobertura.tituloGrafica2");
				else if (grafica == 2)
					titulo = infoReportes
							.getString("reporte.cobertura.tituloGrafica3");
				else if (grafica == 3)
					titulo = infoReportes
							.getString("reporte.cobertura.tituloGrafica4");
				else if (grafica == 4)
					titulo = infoReportes
							.getString("reporte.cobertura.tituloGrafica5");
				else if (grafica == 5)
					titulo = infoReportes
							.getString("reporte.cobertura.tituloGrafica6");

				chart = ChartFactory.createPieChart(titulo + " " + periodo,
						pdataset, true, true, false);
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
				titulo = infoReportes
						.getString("reporte.cobertura.tituloGrafica2");
			} else if (grafica == 1) {
				titulo = infoReportes
						.getString("reporte.cobertura.tituloGrafica3");
			} else if (grafica == 2) {
				titulo = infoReportes
						.getString("reporte.cobertura.tituloGrafica4");
			} else if (grafica == 3) {
				titulo = infoReportes
						.getString("reporte.cobertura.tituloGrafica5");
			} else if (grafica == 4)
				titulo = infoReportes
						.getString("reporte.cobertura.tituloGrafica6");

			chart = ChartFactory.createTimeSeriesChart(titulo, "Periodo",
					"Valor", dataset, true, false, false);
		}
		return chart;
	}

	// ***************************Fin agregar info Consolidado
	// nacional*********************************

	public JFreeChart createPieChartConsolidado(final CategoryDataset dataset,
			int grafica, DefaultPieDataset datapie, String periodo) {
		JFreeChart chart = null;
		chart = ChartFactory.createPieChart(
				infoReportes.getString("reporte.cobertura.tituloGrafica") + " "
						+ periodo, datapie, true, true, false);

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
				infoReportes.getString("reporte.cobertura.tituloGrafica") + " "
						+ periodo, "", "Valor", data, PlotOrientation.VERTICAL,
				true, true, false);
		return chart;
	}

	public JFreeChart createTimeSChartConsolidado(
			final CategoryDataset dataset, int grafica,
			TimeSeriesCollection data, String periodo) {
		JFreeChart chart = null;
		chart = ChartFactory.createTimeSeriesChart(
				infoReportes.getString("reporte.cobertura.tituloGrafica"),
				"Periodo", "Valor", data, true, false, false);
		return chart;
	}

}
