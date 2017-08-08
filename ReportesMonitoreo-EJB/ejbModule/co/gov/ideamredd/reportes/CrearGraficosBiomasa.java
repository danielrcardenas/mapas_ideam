package co.gov.ideamredd.reportes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import co.gov.ideamredd.entities.InformacionReporteBiomasa;

@Stateless
public class CrearGraficosBiomasa {

	@EJB
	ConsultarAsociadosReporte car;

	private static final String archivoReportes = "co/gov/ideamredd/reportes/reportesWPS";
	private static final ResourceBundle infoReportes = ResourceBundle
			.getBundle(archivoReportes);
	@SuppressWarnings("unused")
	private String[] periodo;
	private DefaultCategoryDataset result;
	private DefaultPieDataset resultPie;

	public DefaultCategoryDataset createBarDataset(String[] periodo,
			Integer idReporte, int areaHidro, String grafica) {
		this.periodo = periodo;
		result = new DefaultCategoryDataset();
		ArrayList<InformacionReporteBiomasa> b = car
				.consultarInfoBiomasa(idReporte);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteBiomasa ib = b.get(j);
			adicionarInfo(result, ib, j, areaHidro, 0, grafica);
		}
		return result;
	}

	public DefaultPieDataset createPieDataset(String[] periodo,
			Integer idReporte, int areaHidro, String grafica) {
		this.periodo = periodo;
		resultPie = new DefaultPieDataset();
		ArrayList<InformacionReporteBiomasa> b = car
				.consultarInfoBiomasa(idReporte);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteBiomasa ib = b.get(j);
			adicionarInfoPie(resultPie, ib, areaHidro, 0, grafica);
		}
		return resultPie;
	}

	public DefaultCategoryDataset createBarDatasetConsolidado(
			ArrayList<InformacionReporteBiomasa> datos, String[] periodo,
			Integer idReporte, String grafica) {
		this.periodo = periodo;
		result = new DefaultCategoryDataset();
		for (int j = 0; j < datos.size(); j++) {
			InformacionReporteBiomasa ib = datos.get(j);
			adicionarInfoConsolidado(result, ib, 0, grafica);
		}
		return result;
	}

	public DefaultCategoryDataset createWebConsolidado(
			ArrayList<InformacionReporteBiomasa> datos, String[] periodo,
			Integer idReporte, String grafica,
			ArrayList<BigDecimal[]> webConsolidadoNacional) {
		this.periodo = periodo;
		result = new DefaultCategoryDataset();
		for (int j = 0; j < datos.size(); j++) {
			InformacionReporteBiomasa ib = datos.get(j);
			adicionarWebInfoConsolidado(result, ib, j, grafica,
					webConsolidadoNacional);
		}
		return result;
	}

	public DefaultPieDataset createPieDatasetConsolidado(
			ArrayList<InformacionReporteBiomasa> datos, String[] periodo,
			Integer idReporte, String grafica) {
		this.periodo = periodo;
		resultPie = new DefaultPieDataset();
		for (int j = 0; j < datos.size(); j++) {
			InformacionReporteBiomasa ib = datos.get(j);
			adicionarInfoPieConsolidado(resultPie, ib, 0, grafica);
		}
		return resultPie;
	}

	public DefaultCategoryDataset llenarDatosGrafWeb(String[] periodo,
			Integer idReporte, String grafica,
			ArrayList<BigDecimal[][]> webAreasHidrograficas) {
		this.periodo = periodo;
		result = new DefaultCategoryDataset();
		ArrayList<InformacionReporteBiomasa> b = car
				.consultarInfoBiomasa(idReporte);
		for (int j = 0; j < b.size(); j++) {
			InformacionReporteBiomasa ib = b.get(j);
			adicionarWebGrafInfo(result, ib, j, 0, grafica,
					webAreasHidrograficas);
		}
		return result;
	}

	private void adicionarInfo(DefaultCategoryDataset result,
			InformacionReporteBiomasa ibio, int j, Integer areaHidro, int i,
			String grafica) {

		BigDecimal valor = null;
		if (grafica.equals("Biomasa")) {
			valor = ibio.getBA();
		} else if (grafica.equals("Carbono")) {
			valor = ibio.getC();
		}

		if (areaHidro == 0) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro2"))) {
				result.addValue(valor, ibio.getNombreTipoBosque(),
						ibio.getNombreTipoBosque());
			}
		}
		if (areaHidro == 1) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro3"))) {
				result.addValue(valor, ibio.getNombreTipoBosque(),
						ibio.getNombreTipoBosque());
			}
		}
		if (areaHidro == 2) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro4"))) {
				result.addValue(valor, ibio.getNombreTipoBosque(),
						ibio.getNombreTipoBosque());
			}
		}
		if (areaHidro == 3) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro1"))) {
				result.addValue(valor, ibio.getNombreTipoBosque(),
						ibio.getNombreTipoBosque());
			}
		}
		if (areaHidro == 4) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro5"))) {
				result.addValue(valor, ibio.getNombreTipoBosque(),
						ibio.getNombreTipoBosque());
			}

		}

	}

	private void adicionarWebGrafInfo(DefaultCategoryDataset result,
			InformacionReporteBiomasa ibio, int j, int i,
			String grafica, ArrayList<BigDecimal[][]> webAreasHidrograficas) {

		int posCoB = 0;
		int areaHidro = ibio.getAreaHidrografica()-1;

		BigDecimal valor = null;
		if (grafica.equals("Biomasa")) {
			valor = ibio.getBA();
			posCoB = 0;
		} else if (grafica.equals("Carbono")) {
			valor = ibio.getC();
			posCoB = 1;
		}

		if (areaHidro == 0) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro2"))) {
				result.addValue(valor, ibio.getNombreTipoBosque(),
						ibio.getNombreTipoBosque());

				webAreasHidrograficas.get(areaHidro)[valorTipoBosque(ibio
						.getNombreTipoBosque())][posCoB] = valor;
			}
		}
		if (areaHidro == 1) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro3"))) {
				result.addValue(valor, ibio.getNombreTipoBosque(),
						ibio.getNombreTipoBosque());

				webAreasHidrograficas.get(areaHidro)[valorTipoBosque(ibio
						.getNombreTipoBosque())][posCoB] = valor;
			}
		}
		if (areaHidro == 2) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro4"))) {
				result.addValue(valor, ibio.getNombreTipoBosque(),
						ibio.getNombreTipoBosque());

				webAreasHidrograficas.get(areaHidro)[valorTipoBosque(ibio
						.getNombreTipoBosque())][posCoB] = valor;
			}
		}
		if (areaHidro == 3) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro1"))) {
				result.addValue(valor, ibio.getNombreTipoBosque(),
						ibio.getNombreTipoBosque());

				webAreasHidrograficas.get(areaHidro)[valorTipoBosque(ibio
						.getNombreTipoBosque())][posCoB] = valor;
			}
		}
		if (areaHidro == 4) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro5"))) {
				result.addValue(valor, ibio.getNombreTipoBosque(),
						ibio.getNombreTipoBosque());

				webAreasHidrograficas.get(areaHidro)[valorTipoBosque(ibio
						.getNombreTipoBosque())][posCoB] = valor;
			}

		}

	}

	private void adicionarInfoPie(DefaultPieDataset result,
			InformacionReporteBiomasa ibio, Integer areaHidro, int i,
			String grafica) {

		BigDecimal valor = null;
		if (grafica.equals("Biomasa")) {
			valor = ibio.getBA();
		} else if (grafica.equals("Carbono")) {
			valor = ibio.getC();
		}

		if (areaHidro == 0) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro2"))) {
				result.setValue(ibio.getNombreTipoBosque(), valor);
			}
		}
		if (areaHidro == 1) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro3"))) {
				result.setValue(ibio.getNombreTipoBosque(), valor);
			}
		}
		if (areaHidro == 2) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro4"))) {
				result.setValue(ibio.getNombreTipoBosque(), valor);
			}
		}
		if (areaHidro == 3) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro1"))) {
				result.setValue(ibio.getNombreTipoBosque(), valor);
			}
		}
		if (areaHidro == 4) {
			if (ibio.getNombreAreaHidrografica().equals(
					infoReportes.getString("nombreHidro5"))) {
				result.setValue(ibio.getNombreTipoBosque(), valor);
			}

		}

	}

	private void adicionarInfoConsolidado(DefaultCategoryDataset result,
			InformacionReporteBiomasa ibio, int i, String grafica) {

		BigDecimal valor = null;
		if (grafica.equals("Biomasa")) {
			valor = ibio.getBA();
		} else if (grafica.equals("Carbono")) {
			valor = ibio.getC();
		}
		result.addValue(valor, ibio.getNombreTipoBosque(),
				ibio.getNombreTipoBosque());

	}

	private void adicionarWebInfoConsolidado(DefaultCategoryDataset result,
			InformacionReporteBiomasa ibio, int j, String grafica,
			ArrayList<BigDecimal[]> webConsolidadoNacional) {

		BigDecimal valor = null;
		if (grafica.equals("Biomasa")) {
			valor = ibio.getBA();
			webConsolidadoNacional.get(valorTipoBosque(ibio
					.getNombreTipoBosque()))[0] = valor;
		} else if (grafica.equals("Carbono")) {
			valor = ibio.getC();
			webConsolidadoNacional.get(valorTipoBosque(ibio
					.getNombreTipoBosque()))[1] = valor;
		}

	}

	private void adicionarInfoPieConsolidado(DefaultPieDataset result,
			InformacionReporteBiomasa ibio, int i, String grafica) {

		BigDecimal valor = null;
		if (grafica.equals("Biomasa")) {
			valor = ibio.getBA();
		} else if (grafica.equals("Carbono")) {
			valor = ibio.getC();
		}

		result.setValue(ibio.getNombreTipoBosque(), valor);

	}

	public JFreeChart createChartBarras(final CategoryDataset dataset,
			int grafica, DefaultPieDataset datapie, String periodo, int areaH) {
		JFreeChart chart = null;
		if (dataset != null) {
			String titulo = "";
			if (areaH == 0)
				titulo = infoReportes.getString("nombreHidro2");
			if (areaH == 1)
				titulo = infoReportes.getString("nombreHidro3");
			if (areaH == 2)
				titulo = infoReportes.getString("nombreHidro4");
			if (areaH == 3)
				titulo = infoReportes.getString("nombreHidro1");
			if (areaH == 4)
				titulo = infoReportes.getString("nombreHidro5");

			if (grafica == 0) {
				chart = ChartFactory.createStackedBarChart3D(titulo
						+ " / Carbono " + periodo, "", "C(t)", dataset,
						PlotOrientation.HORIZONTAL, true, false, false);
			} else {
				chart = ChartFactory.createStackedBarChart3D(titulo
						+ " / Biomasa " + periodo, "", "BA(t)", dataset,
						PlotOrientation.HORIZONTAL, true, false, false);
			}
		}
		return chart;
	}

	public JFreeChart createChartPie(DefaultPieDataset dataset, int grafica,
			DefaultPieDataset datapie, String periodo, int areaH) {
		JFreeChart chart = null;
		if (dataset != null) {
			String titulo = "";
			if (areaH == 0)
				titulo = infoReportes.getString("nombreHidro2");
			if (areaH == 1)
				titulo = infoReportes.getString("nombreHidro3");
			if (areaH == 2)
				titulo = infoReportes.getString("nombreHidro4");
			if (areaH == 3)
				titulo = infoReportes.getString("nombreHidro1");
			if (areaH == 4)
				titulo = infoReportes.getString("nombreHidro5");

			if (grafica == 0) {
				chart = ChartFactory.createPieChart(titulo + " / Carbono "
						+ periodo, dataset, true, false, false);
			} else {
				chart = ChartFactory.createPieChart(titulo + " / Biomasa "
						+ periodo, dataset, true, false, false);
			}
		}
		return chart;
	}

	public JFreeChart createChartBarrasConsolidado(
			final CategoryDataset dataset, int grafica,
			DefaultPieDataset datapie, String periodo) {
		JFreeChart chart = null;
		if (dataset != null) {
			if (grafica == 0) {
				chart = ChartFactory.createStackedBarChart3D("Carbono "
						+ periodo, "", "C(t)", dataset,
						PlotOrientation.HORIZONTAL, true, false, false);
			} else {
				chart = ChartFactory.createStackedBarChart3D("Biomasa "
						+ periodo, "", "BA(t)", dataset,
						PlotOrientation.HORIZONTAL, true, false, false);
			}
		}
		return chart;
	}

	public JFreeChart createChartPieConsolidado(DefaultPieDataset dataset,
			int grafica, DefaultPieDataset datapie, String periodo) {
		JFreeChart chart = null;
		if (dataset != null) {
			if (grafica == 0) {
				chart = ChartFactory.createPieChart("Carbono " + periodo,
						dataset, true, false, false);
			} else {
				chart = ChartFactory.createPieChart("Biomasa " + periodo,
						dataset, true, false, false);
			}
		}
		return chart;
	}

	public int valorTipoBosque(String tipoB) {

		if (tipoB.equals("Bosque muy seco Tropical")) {
			return 0;
		}
		if (tipoB.equals("Bosque seco Tropical")) {
			return 1;
		}
		if (tipoB.equals("Bosque humedo Tropical")) {
			return 2;
		}
		if (tipoB.equals("Bosque humedo Pre-montano")) {
			return 3;
		}
		if (tipoB.equals("Bosque muy humedo Pre-montano")) {
			return 4;
		}
		if (tipoB.equals("Bosque humedo Montano-bajo")) {
			return 5;
		}
		if (tipoB.equals("Bosque muy humedo Montano-bajo")) {
			return 6;
		}
		if (tipoB.equals("Bosque seco Pre-montano")) {
			return 7;
		}
		if (tipoB.equals("Bosque muy humedo Montano")) {
			return 8;
		}
		if (tipoB.equals("Bosque pluvial Montano")) {
			return 9;
		}
		if (tipoB.equals("Bosque pluvial Pre-montano")) {
			return 10;
		}
		if (tipoB.equals("Bosque seco Montano-bajo")) {
			return 11;
		}
		if (tipoB.equals("Bosque humedo Montano")) {
			return 12;
		}
		if (tipoB.equals("Bosque pluvial Montano-bajo")) {
			return 13;
		}
		if (tipoB.equals("Bosque pluvial Tropical")) {
			return 14;
		}
		return 0;

	}

}