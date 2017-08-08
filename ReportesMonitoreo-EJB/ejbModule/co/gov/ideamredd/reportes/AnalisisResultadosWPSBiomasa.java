package co.gov.ideamredd.reportes;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.entities.InformacionReporteBiomasa;
import co.gov.ideamredd.entities.Reportes;
import co.gov.ideamredd.util.Util;

@Stateless
public class AnalisisResultadosWPSBiomasa extends AnalisisResultadosWPS {

	@EJB
	IngresaInformacionReporte infr;

	private Reportes reporte = new Reportes();
	private Integer periodoUno = -1;
	private int indice = 0;
	private Integer idReporte;
	private static final String archivoBiomasa = "co/gov/ideamredd/reportes/reportesWPS";
	private static final ResourceBundle infoBiomasa = ResourceBundle
			.getBundle(archivoBiomasa);
	private ArrayList<InformacionReporteBiomasa> ponderado = new ArrayList<InformacionReporteBiomasa>();
	private ArrayList<InformacionReporteBiomasa> amazonas = new ArrayList<InformacionReporteBiomasa>();
	private ArrayList<InformacionReporteBiomasa> caribe = new ArrayList<InformacionReporteBiomasa>();
	private ArrayList<InformacionReporteBiomasa> magdalena = new ArrayList<InformacionReporteBiomasa>();
	private ArrayList<InformacionReporteBiomasa> orinoco = new ArrayList<InformacionReporteBiomasa>();
	private ArrayList<InformacionReporteBiomasa> pacifico = new ArrayList<InformacionReporteBiomasa>();

	private ArrayList<Integer> totalesBosques = new ArrayList<Integer>();

	public void nuevoAnalisisResultadosWPSBiomasa(Integer tipoReporte,
			Integer itemCruce, String nombresMapa, Integer periodo) {
		periodoUno = periodo;
		super.setPeriodoUno(periodoUno);
		ArrayList<String> valores = super.obtenerPixelesGDAL(tipoReporte,
				itemCruce, nombresMapa);
		organizarDatosBiomasa(valores, tipoReporte);

		System.gc();
		if (valores.size() > 0) {
			idReporte = registrarReporte();
			if (tipoReporte == 7) {
				registrarInfoConsolidado(getInfo());
			} else {
				// sumarPonderaciones();
				guardarInfo();
			}
			getInfo().clear();
		}
	}

	private void guardarInfo() {

		registrarInfo(magdalena, 1);
		registrarInfo(orinoco, 2);
		registrarInfo(amazonas, 3);
		registrarInfo(pacifico, 4);
		registrarInfo(caribe, 5);
	}

	private void organizarDatosBiomasa(ArrayList<String> valores,
			Integer tipoReporte) {
		int index = 0;
		int cont = 0;
		if (tipoReporte == 7)
			for (int i = 0; i < getInfo().size(); i++) {
				InformacionReporteBiomasa biomasa = (InformacionReporteBiomasa) getInfo()
						.get(index);
				biomasa.setNumero(Integer.valueOf(valores.get(cont)));
				cont++;
				biomasa.setTipoBosque(valores.get(cont));
				cont++;
				biomasa.setNombreTipoBosque(valores.get(cont));
				cont++;
				biomasa.setBiomasa(BigDecimal.valueOf(Double.valueOf(valores
						.get(cont))));
				cont++;
				biomasa.setArea(Util.hectariasPixeles(Integer.valueOf(valores
						.get(cont)), new BigDecimal(0.09295872)));
				cont++;
				biomasa.setCarbono(biomasa.getBiomasa().multiply(
						BigDecimal.valueOf(0.5)));
				biomasa.setBA(biomasa.getBiomasa().multiply(
						BigDecimal.valueOf(biomasa.getArea())));
				biomasa.setC(biomasa.getCarbono().multiply(
						BigDecimal.valueOf(biomasa.getArea())));
				biomasa.setCO2(biomasa.getC()
						.multiply(BigDecimal.valueOf(3.96)));
				index++;
			}
		else {
			crearObjetosContenedores(
					Integer.valueOf(infoBiomasa.getString("biomasa.cantidad."
							+ periodoUno)), ponderado);
			crearObjetosContenedores(
					Integer.valueOf(infoBiomasa.getString("biomasa.cantidad."
							+ periodoUno)), magdalena);
			crearObjetosContenedores(
					Integer.valueOf(infoBiomasa.getString("biomasa.cantidad."
							+ periodoUno)), orinoco);
			crearObjetosContenedores(
					Integer.valueOf(infoBiomasa.getString("biomasa.cantidad."
							+ periodoUno)), amazonas);
			crearObjetosContenedores(
					Integer.valueOf(infoBiomasa.getString("biomasa.cantidad."
							+ periodoUno)), pacifico);
			crearObjetosContenedores(
					Integer.valueOf(infoBiomasa.getString("biomasa.cantidad."
							+ periodoUno)), caribe);
			cargarValores(valores, ponderado, 0);
			cargarValores(valores, magdalena, 1);
			cargarValores(valores, orinoco, 2);
			cargarValores(valores, amazonas, 3);
			cargarValores(valores, pacifico, 4);
			cargarValores(valores, caribe, 5);
		}
	}

	private void crearObjetosContenedores(Integer cantidad,
			ArrayList<InformacionReporteBiomasa> vector) {
		for (int i = 0; i < cantidad; i++) {
			InformacionReporteBiomasa bio = new InformacionReporteBiomasa();
			vector.add(bio);
		}

	}

	private void cargarValores(ArrayList<String> valores,
			ArrayList<InformacionReporteBiomasa> vector, int numeroVector) {
		int index = 0;
		for (int i = 0; i < vector.size(); i++) {
			InformacionReporteBiomasa biomasa = (InformacionReporteBiomasa) vector
					.get(index);
			biomasa.setNumero(Integer.valueOf(valores.get(indice)));
			biomasa.setArea(Util.hectariasPixeles(Integer.valueOf(valores
					.get(indice)), new BigDecimal(0.09295872)));
			cargarInfoBiomasa(biomasa, index);
			biomasa.setCarbono(biomasa.getBiomasa().multiply(
					BigDecimal.valueOf(0.5)));
			biomasa.setBA(biomasa.getBiomasa().multiply(
					BigDecimal.valueOf(biomasa.getArea())));
			biomasa.setC(biomasa.getCarbono().multiply(
					BigDecimal.valueOf(biomasa.getArea())));
			biomasa.setCO2(biomasa.getC().multiply(BigDecimal.valueOf(3.96)));
			obtenerTotales(numeroVector, i, valores.get(indice), vector.size());
			index++;
			indice++;
		}
	}

	private void obtenerTotales(int numeroVector, int i, String valor, int k) {
		for (int a = 0; a < k; a++) {
			totalesBosques.add(0);
		}

		if (numeroVector != 0) {
			Integer total = totalesBosques.get(i) + Integer.valueOf(valor);
			totalesBosques.add(i, total);
		}
	}

	private void cargarInfoBiomasa(InformacionReporteBiomasa biomasa, int index) {
		String dato = infoBiomasa.getString("biomasa." + (index + 1) + "."
				+ periodoUno);
		String[] info = dato.split(";");
		biomasa.setBiomasa(BigDecimal.valueOf(Double.valueOf(info[0])));
		biomasa.setTipoBosque(info[1]);
		biomasa.setNombreTipoBosque(info[2]);
	}

	private void registrarInfo(ArrayList<InformacionReporteBiomasa> vector,
			Integer area) {
		for (int i = 1; i < vector.size(); i++) {
			InformacionReporteBiomasa b = (InformacionReporteBiomasa) vector
					.get(i);
			b.setIdReporte(idReporte);
			b.setAreaHidrografica(area);
			infr.IngresarInfoBiomasa(b);
		}
	}

	private void registrarInfoConsolidado(ArrayList<Object> vector) {
		for (int i = 1; i < vector.size(); i++) {
			InformacionReporteBiomasa b = (InformacionReporteBiomasa) vector
					.get(i);
			b.setIdReporte(idReporte);
			infr.IngresarInfoBiomasa(b);
		}
	}

//	private void sumarPonderaciones() {
//		calcularPorcentajes();
//		DecimalFormat df = new DecimalFormat("########.##");
//		for (int i = 0; i < ponderado.size(); i++) {
//			InformacionReporteBiomasa bioPonderado = ponderado.get(i);
//			InformacionReporteBiomasa bioAmazonas = amazonas.get(i);
//			InformacionReporteBiomasa bioOrinoco = orinoco.get(i);
//			InformacionReporteBiomasa bioMagdalena = magdalena.get(i);
//			InformacionReporteBiomasa bioPacifico = pacifico.get(i);
//			InformacionReporteBiomasa bioCaribe = caribe.get(i);
//
//			bioAmazonas.setArea(Double.valueOf(df.format(
//					bioAmazonas.getArea()
//							+ Util.obtenerInfoProcentaje(
//									bioPonderado.getArea(),
//									bioAmazonas.getPorcentaje())).replace(",",
//					".")));
//			bioOrinoco.setArea(Double.valueOf(df.format(
//					bioOrinoco.getArea()
//							+ Util.obtenerInfoProcentaje(
//									bioPonderado.getArea(),
//									bioOrinoco.getPorcentaje())).replace(",",
//					".")));
//			bioMagdalena.setArea(Double.valueOf(df.format(
//					bioMagdalena.getArea()
//							+ Util.obtenerInfoProcentaje(
//									bioPonderado.getArea(),
//									bioMagdalena.getPorcentaje())).replace(",",
//					".")));
//			bioPacifico.setArea(Double.valueOf(df.format(
//					bioPacifico.getArea()
//							+ Util.obtenerInfoProcentaje(
//									bioPonderado.getArea(),
//									bioPacifico.getPorcentaje())).replace(",",
//					".")));
//			bioCaribe.setArea(Double.valueOf(df.format(
//					bioCaribe.getArea()
//							+ Util.obtenerInfoProcentaje(
//									bioPonderado.getArea(),
//									bioCaribe.getPorcentaje())).replace(",",
//					".")));
//
//		}
//	}

//	private void calcularPorcentajes() {
//		for (int i = 0; i < ponderado.size(); i++) {
//			InformacionReporteBiomasa bioAmazonas = amazonas.get(i);
//			InformacionReporteBiomasa bioOrinoco = orinoco.get(i);
//			InformacionReporteBiomasa bioMagdalena = magdalena.get(i);
//			InformacionReporteBiomasa bioPacifico = pacifico.get(i);
//			InformacionReporteBiomasa bioCaribe = caribe.get(i);
//
//			bioMagdalena.setPorcentaje(Util.calcularPorcentaje(
//					totalesBosques.get(i), bioMagdalena.getNumero()));
//			bioAmazonas.setPorcentaje(Util.calcularPorcentaje(
//					totalesBosques.get(i), bioAmazonas.getNumero()));
//			bioOrinoco.setPorcentaje(Util.calcularPorcentaje(
//					totalesBosques.get(i), bioOrinoco.getNumero()));
//
//			bioPacifico.setPorcentaje(Util.calcularPorcentaje(
//					totalesBosques.get(i), bioPacifico.getNumero()));
//			bioCaribe.setPorcentaje(Util.calcularPorcentaje(
//					totalesBosques.get(i), bioCaribe.getNumero()));
//		}
//	}

	/*
	 * public static void main(String a[]) { // String mapa =
	 * "C:/ima/wpsnuevo/Colombia_Biomasa_Resolucion_Fina_2005.tif"; String mapa
	 * = "C:/Users/lpgarzon/Documents/pruebaWPS/biomasa2005.img"; Integer p =
	 * 2005; new AnalisisResultadosWPSBiomasa(8, 4, mapa, p); }
	 */

	private Integer registrarReporte() {

		reporte.setTipoReporte(super.getTipoReporte());
		reporte.setDivision(super.getItemCruce());
		reporte.setFechaGeneracion(new Date(System.currentTimeMillis()));
		reporte.setPeriodoUno(periodoUno);
		reporte.setPeriodoDos(-1);
		return infr.IngresarReporte(reporte);
	}

	public Integer getPeriodoUno() {
		return periodoUno;
	}

	public void setPeriodoUno(Integer periodoUno) {
		this.periodoUno = periodoUno;
	}

}
