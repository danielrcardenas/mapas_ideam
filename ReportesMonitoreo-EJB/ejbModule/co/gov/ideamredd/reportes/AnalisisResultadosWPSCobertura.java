package co.gov.ideamredd.reportes;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.entities.InformacionReporteCobertura;
import co.gov.ideamredd.entities.Reportes;
import co.gov.ideamredd.util.Util;

@Stateless
public class AnalisisResultadosWPSCobertura extends AnalisisResultadosWPS {

	@EJB
	IngresaInformacionReporte infr;
	
	private InformacionReporteCobertura cobertura;
	private Integer numeroPixeles = 0;
	private Reportes reporte = new Reportes();
	private Integer periodoUno = -1;
	private Integer periodoDos = -1;
	private Integer idReporte;
	private Integer totalBosque = 0;
	private Integer totalNoBosque = 0;
	private Integer totalSinInfo = 0;
	private Integer totalDeforestacion = 0;
	private Integer totalRegeneracion = 0;

	public void nuevoAnalisisResultadosWPSCobertura(Integer tipoReporte,
			Integer ItemCruce, String nombresMapas,
			Integer periodos[]) {
		if (periodos.length == 1)
			periodoUno = periodos[0];
		else {
			periodoUno = periodos[0];
			periodoDos = periodos[1];
		}
		ArrayList<String> valores = super.obtenerPixelesGDAL(tipoReporte, ItemCruce, nombresMapas);
		int index=0;
		for(int i=0;i<valores.size();i+=6){
			cobertura = (InformacionReporteCobertura) getInfo().get(index);
			cobertura.setBosqueEstable(Double.valueOf(valores.get(i)));
			cobertura.setDeforestacion(Double.valueOf(valores.get(i+1)));
			cobertura.setSinInformacionEstable(Double.valueOf(valores.get(i+2)));
			cobertura.setRegeneracion(Double.valueOf(valores.get(i+3)));
			cobertura.setNoBosqueEstable(Double.valueOf(valores.get(i+4)));
			cobertura.setBosquePeriodoUno(Double.valueOf(valores.get(i+5)));
			index++;
		}
		System.gc();
 		idReporte = registrarReporte();
		calcularHectareas(getInfo());
//		sumarPonderaciones(getInfo());
		registrarInfo(getInfo());
		getInfo().clear();

	}

//	private void organizarInformacion(int dato) {
//		BigDecimal result = new BigDecimal(dato / super.getNumeroItems());
//		int index = 0;
//		int mod = dato % super.getNumeroItems();
//		if (mod != 0)
//			index = result.intValue();
//		else
//			index = result.intValue() - 1;
//		cobertura = (InformacionReporteCobertura) super.getInfo().get(index);
//		if (mod == 1)
//			cobertura.setBosqueEstable(cobertura.getBosqueEstable() + 1);
//		else if (mod == 2)
//			cobertura.setDeforestacion(cobertura.getDeforestacion() + 1);
//		else if (mod == 3)
//			cobertura.setSinInformacionEstable(cobertura
//					.getSinInformacionEstable() + 1);
//		else if (mod == 4)
//			cobertura.setRegeneracion(cobertura.getRegeneracion() + 1);
//		else if (mod == 5)
//			cobertura.setNoBosqueEstable(cobertura.getNoBosqueEstable() + 1);
//		else if (mod == 6)
//			cobertura.setBosquePeriodoUno(cobertura.getBosquePeriodoUno()+1);
//	}

	private void obtenerTotales(ArrayList<Object> info) {
		for (int i = 0; i < info.size(); i++) {
			InformacionReporteCobertura b = (InformacionReporteCobertura) info
					.get(i);
			totalBosque += b.getBosqueEstable().intValue();
			totalNoBosque += b.getNoBosqueEstable().intValue();
			totalSinInfo += b.getSinInformacionEstable().intValue();
			totalDeforestacion += b.getDeforestacion().intValue();
			totalRegeneracion += b.getRegeneracion().intValue();
		}
	}

	private void calcularHectareas(ArrayList<Object> info) {
		obtenerTotales(info);
		for (int i = 0; i < info.size(); i++) {
			Integer areaDivision;
			InformacionReporteCobertura b = (InformacionReporteCobertura) info
					.get(i);
			areaDivision = b.getBosqueEstable().intValue()
					+ b.getNoBosqueEstable().intValue()
					+ b.getSinInformacionEstable().intValue()
					+ b.getDeforestacion().intValue()
					+ b.getRegeneracion().intValue();
			b.setIdReporte(idReporte);
			BigDecimal factor;
			//if (tipoReporte.equals(1))
				factor  = new BigDecimal(0.09295872);
			//else
			//	factor = new BigDecimal(625);
			if (areaDivision != 0) {
				b.setPorcBosque(Util.calcularPorcentaje(totalBosque, b
						.getBosqueEstable().intValue()));
				b.setPorcNoBosque(Util.calcularPorcentaje(totalNoBosque, b
						.getNoBosqueEstable().intValue()));
				b.setPorcSinInfo(Util.calcularPorcentaje(totalSinInfo, b
						.getSinInformacionEstable().intValue()));
				b.setPorcDeforestacion(Util.calcularPorcentaje(
						totalDeforestacion, b.getDeforestacion().intValue()));
				b.setPorcRegeneracion(Util.calcularPorcentaje(
						totalRegeneracion, b.getRegeneracion().intValue()));
				b.setBosqueEstable(Util.hectariasPixeles(b.getBosqueEstable()
						.intValue(),factor));
				b.setNoBosqueEstable(Util.hectariasPixeles(b
						.getNoBosqueEstable().intValue(),factor));
				b.setSinInformacionEstable(Util.hectariasPixeles(b
						.getSinInformacionEstable().intValue(),factor));
				b.setPorcentaje(Util.calcularPorcentaje(numeroPixeles,
						areaDivision));
				b.setDeforestacion(Util.hectariasPixeles(b.getDeforestacion()
						.intValue(),factor));
				b.setRegeneracion(Util.hectariasPixeles(b.getRegeneracion()
						.intValue(),factor));
			} else {
				b.setBosqueEstable(new Double(0));
				b.setNoBosqueEstable(new Double(0));
				b.setSinInformacionEstable(new Double(0));
				b.setDeforestacion(new Double(0));
				b.setRegeneracion(new Double(0));
				b.setPorcentaje(new Double(0));
				b.setPorcBosque(new Double(0));
				b.setPorcNoBosque(new Double(0));
				b.setPorcSinInfo(new Double(0));
				b.setPorcDeforestacion(new Double(0));
				b.setPorcRegeneracion(new Double(0));
			}
		}
	}

	/*public static void main(String a[]) {
		String mapa = "C:/ima/salcambiocriterioseptem.img";
//		ArrayList<String> mapas = new ArrayList<String>();
//		mapas.add("cambiodeptoinferior902000");
//		mapas.add("cambiodeptosuperior902000");
		Integer[] p = { 1990,2000};
		new AnalisisResultadosWPSCobertura(3, 2,
				mapa, p);
	}*/
	
//	private void sumarPonderaciones(ArrayList<Object> info) {
//		DecimalFormat df = new DecimalFormat("########.##");
//		InformacionReporteCobertura iCobertura = (InformacionReporteCobertura) info
//				.get(0);
//		for (int i = 1; i < info.size(); i++) {
//			InformacionReporteCobertura ic = (InformacionReporteCobertura) info
//					.get(i);
//			Double valorBosque = Util.obtenerInfoProcentaje(
//					iCobertura.getBosqueEstable(), ic.getPorcentaje());
//			Double valorNoBosque = Util.obtenerInfoProcentaje(iCobertura
//					.getNoBosqueEstable(), ic.getPorcentaje());
//			Double valorDeforestacion = Util.obtenerInfoProcentaje(iCobertura
//					.getDeforestacion(), ic.getPorcentaje());
//			Double valorRegeneracion = Util.obtenerInfoProcentaje(iCobertura
//					.getRegeneracion(), ic.getPorcentaje());
//			Double valorSinInfo = Util.obtenerInfoProcentaje(iCobertura
//					.getSinInformacionEstable(), ic.getPorcentaje());
//			Double valSinInfo=valorBosque+valorNoBosque+valorDeforestacion+valorRegeneracion+valorSinInfo;
//			ic.setBosqueEstable(Double.valueOf(df.format(ic.getBosqueEstable() + valorBosque).replace(",", ".")));
//			ic.setNoBosqueEstable(Double.valueOf(df.format(ic.getNoBosqueEstable() + valorNoBosque).replace(",", ".")));
//			ic.setDeforestacion(Double.valueOf(df.format(ic.getDeforestacion() + valorDeforestacion).replace(",", ".")));
//			ic.setRegeneracion(Double.valueOf(df.format(ic.getRegeneracion() + valorRegeneracion).replace(",", ".")));
//			ic.setSinInformacionEstable(Double.valueOf(df.format(ic.getSinInformacionEstable() + valSinInfo).replace(",", ".")));
//		}
//	}
	
	private void registrarInfo(ArrayList<Object> info) {
		for (int i = 1; i < info.size(); i++) {
			InformacionReporteCobertura c = (InformacionReporteCobertura) info.get(i);
			infr.IngresarInfoCobertura(c);
		}
	}

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
