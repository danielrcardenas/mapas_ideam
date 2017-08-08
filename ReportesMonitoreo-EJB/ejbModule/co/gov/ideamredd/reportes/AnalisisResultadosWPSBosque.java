package co.gov.ideamredd.reportes;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.entities.InformacionReporteBosque;
import co.gov.ideamredd.entities.Reportes;
import co.gov.ideamredd.util.Util;

@Stateless
public class AnalisisResultadosWPSBosque extends AnalisisResultadosWPS {

	@EJB
	IngresaInformacionReporte infr;
	
	private InformacionReporteBosque bosque;
	private Integer numeroPixeles = 0;
	private Reportes reporte = new Reportes();
	private Integer periodoUno = -1;
	private Integer periodoDos = -1;
	private Integer idReporte;
	private Integer totalBosque = 0;
	private Integer totalNoBosque = 0;
	private Integer totalSinInfo = 0;

	public void nuevoAnalisisResultadosWPSBosque(Integer tipoReporte, Integer ItemCruce,
			String nombresMapa, Integer periodos[]) {
		if (periodos.length == 1)
			periodoUno = periodos[0];
		else {
			periodoUno = periodos[0];
			periodoDos = periodos[1];
		}
		ArrayList<String> valores = super.obtenerPixelesGDAL(tipoReporte,
				ItemCruce, nombresMapa);
		int index = 0;
		for (int i = 0; i < valores.size(); i += 3) {
			bosque = (InformacionReporteBosque) getInfo().get(index);
			bosque.setBosque(Double.valueOf(valores.get(i)));
			bosque.setNoBosque(Double.valueOf(valores.get(i + 1)));
			bosque.setSinInformacion(Double.valueOf(valores.get(i + 2)));
			index++;
		}
//		 for (int i = 0; i < nombresMapa.size(); i++) {
//		 DataBuffer e = super.obtenerPixeles(tipoReporte, ItemCruce,
//		 nombresMapa.get(i));
//		 for (int j = 0; j < e.getSize(); j++) {
//		 if (e.getElem(j) > 0) {
//		 numeroPixeles++;
//		 organizarInformacion(e.getElem(j));
//		 }
//		 }
//		 }
		System.gc();
		if (valores.size() > 0) {
			idReporte = registrarReporte();
			calcularHectareas(getInfo(), tipoReporte);
			sumarPonderaciones(getInfo());
			registrarInfo(getInfo());
			getInfo().clear();
		}
	}

	private void registrarInfo(ArrayList<Object> info) {
		for (int i = 1; i < info.size(); i++) {
			InformacionReporteBosque b = (InformacionReporteBosque) info.get(i);
			infr.IngresarInfoBosque(b);
		}
	}

	private void calcularHectareas(ArrayList<Object> info, Integer tipoReporte) {
		obtenerTotales(info);
		//Integer factor = 0;
		BigDecimal factor;
		numeroPixeles = getNumeroPixeles();
		for (int i = 0; i < info.size(); i++) {
			Integer areaDivision;
			InformacionReporteBosque b = (InformacionReporteBosque) info.get(i);
			areaDivision = b.getBosque().intValue()
					+ b.getNoBosque().intValue()
					+ b.getSinInformacion().intValue();
			b.setIdReporte(idReporte);
			if (tipoReporte.equals(1))
				factor  = new BigDecimal(0.09295872);//factor de tama�o de pixel 30.26 * 30.72
			else
				factor = new BigDecimal(625);
			if (areaDivision != 0) {
				b.setPorcentajeBosque(Util.calcularPorcentaje(totalBosque, b
						.getBosque().intValue()));
				b.setPorcentajeNoBosque(Util.calcularPorcentaje(totalNoBosque, b
						.getNoBosque().intValue()));
				b.setPorcentajeSinInfo(Util.calcularPorcentaje(totalSinInfo, b
						.getSinInformacion().intValue()));
				b.setBosque(Util.hectariasPixeles(b.getBosque().intValue(),
						factor));
				b.setNoBosque(Util.hectariasPixeles(b.getNoBosque().intValue(),
						factor));
				b.setSinInformacion(Util.hectariasPixeles(b.getSinInformacion()
						.intValue(), factor));
				if (i != 0)
					b.setPorcentaje(Util.calcularPorcentaje(numeroPixeles,
							areaDivision));
				else
					b.setPorcentaje(new Double(0));
			} else {
				b.setBosque(new Double(0));
				b.setNoBosque(new Double(0));
				b.setSinInformacion(new Double(0));
				b.setPorcentaje(new Double(0));
				b.setPorcentajeBosque(new Double(0));
				b.setPorcentajeNoBosque(new Double(0));
				b.setPorcentajeSinInfo(new Double(0));
			}
		}
	}

	private void sumarPonderaciones(ArrayList<Object> info) {
		DecimalFormat df = new DecimalFormat("########.##");
		InformacionReporteBosque iBosque = (InformacionReporteBosque) info
				.get(0);
		for (int i = 1; i < info.size(); i++) {
			InformacionReporteBosque ib = (InformacionReporteBosque) info
					.get(i);
			Double valorBosque = Util.obtenerInfoProcentaje(
					iBosque.getBosque(), ib.getPorcentaje());
			Double valorNoBosque = Util.obtenerInfoProcentaje(iBosque
					.getNoBosque(), ib.getPorcentaje());
			Double valorSinInfo = Util.obtenerInfoProcentaje(iBosque
					.getSinInformacion(), ib.getPorcentaje());
			ib.setBosque(Double.valueOf(df.format(ib.getBosque() + valorBosque)
					.replace(",", ".")));
			ib.setNoBosque(Double.valueOf(df.format(
					ib.getNoBosque() + valorNoBosque).replace(",", ".")));
			ib.setSinInformacion(Double.valueOf(df.format(
					ib.getSinInformacion() + valorSinInfo).replace(",", ".")));
		}
	}

	private void obtenerTotales(ArrayList<Object> info) {
		for (int i = 0; i < info.size(); i++) {
			InformacionReporteBosque b = (InformacionReporteBosque) info.get(i);
			totalBosque += b.getBosque().intValue();
			totalNoBosque += b.getNoBosque().intValue();
			totalSinInfo += b.getSinInformacion().intValue();
		}
	}

//	private void organizarInformacion(int dato) {
//		BigDecimal result = new BigDecimal(dato / super.getNumeroItems());
//		int index = 0;
//		int mod = dato % super.getNumeroItems();
//		if (mod != 0)
//			index = result.intValue();
//		else
//			index = result.intValue() - 1;
//		bosque = (InformacionReporteBosque) super.getInfo().get(index);
//		if (mod == 1)
//			bosque.setBosque(bosque.getBosque() + 1);
//		else if (mod == 2)
//			bosque.setNoBosque(bosque.getNoBosque() + 1);
//		else
//			bosque.setSinInformacion(bosque.getSinInformacion() + 1);
//	}

	/*public static void main(String a[]) {
//		String mapa = Util.obtenerNombreArchivo("bnb", "area", "1990", "fina");
		String mapa="C:/ima/bnbareahidro2000.img";
//		ArrayList<String> mapas = new ArrayList<String>();
//		 mapas.add("temporal");
		Integer[] p = { 1990 };
		new AnalisisResultadosWPSBosque(1, 3, mapa, p);
	}*/

	private Integer registrarReporte() {
		reporte.setTipoReporte(super.getTipoReporte());
		reporte.setDivision(super.getItemCruce());
		reporte.setFechaGeneracion(new Date(System.currentTimeMillis()));
		reporte.setPeriodoUno(periodoUno);
		reporte.setPeriodoDos(periodoDos);
		return infr.IngresarReporte(reporte);
	}

	public Integer getPeriodoUno() {
		return periodoUno;
	}

	public void setPeriodoUno(Integer periodoUno) {
		this.periodoUno = periodoUno;
	}

	public Integer getPeriodoDos() {
		return periodoDos;
	}

	public void setPeriodoDos(Integer periodoDos) {
		this.periodoDos = periodoDos;
	}

}
