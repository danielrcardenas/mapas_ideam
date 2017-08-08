package co.gov.ideamredd.reportes;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.entities.InformacionReporteDeforestacion;
import co.gov.ideamredd.entities.Reportes;
import co.gov.ideamredd.util.Util;

@Stateless
public class AnalisisResultadosWPSDeforestacion extends AnalisisResultadosWPS {

	@EJB
	IngresaInformacionReporte infr;
	
	private InformacionReporteDeforestacion deforestacion;
	private Integer numeroPixeles = 0;
	private Reportes reporte = new Reportes();
	private Integer periodoUno = -1;
	private Integer periodoDos = -1;
	private Integer idReporte;
	private Integer totalArbustales = 0;
	private Integer totalAreaAgricola = 0;
	private Integer totalAreaUrbanizada = 0;
	private Integer totalCultivoPermanente = 0;
	private Integer totalCultivoTransitorio = 0;
	private Integer totalHerbazales = 0;
	private Integer totalAreaSinVegetacion = 0;
	private Integer totalPastosPlantacion = 0;
	private Integer totalSuperficieAgua = 0;
	private Integer totalVegetacionAgua = 0;
	private Integer totalVegetacionSecundaria = 0;
	private Integer totalZonasQuemadas = 0;

	public void nuevoAnalisisResultadosWPSDeforestacion(Integer tipoReporte,
			Integer ItemCruce, String nombresMapas,
			Integer periodos[]) {
		if (periodos.length == 1)
			periodoUno = periodos[0];
		else {
			periodoUno = periodos[0];
			periodoDos = periodos[1];
		}
		ArrayList<String> valores = super.obtenerPixelesGDAL(tipoReporte,
				ItemCruce, nombresMapas);
		int index = 0;
		for (int i = 0; i < valores.size(); i += 12) {
			deforestacion = (InformacionReporteDeforestacion) getInfo().get(
					index);
			deforestacion.setAreasUrbanizadas(Double.valueOf(valores.get(i)));
			deforestacion.setCultivosTransitorios(Double.valueOf(valores
					.get(i + 1)));
			deforestacion.setCultivosPermanentes(Double.valueOf(valores
					.get(i + 2)));
			deforestacion.setPastosPlantacionForestal(Double.valueOf(valores
					.get(i + 3)));
			deforestacion.setAreasAgricolas(Double.valueOf(valores.get(i + 4)));
			deforestacion.setHerbazales(Double.valueOf(valores.get(i + 5)));
			deforestacion.setArbustales(Double.valueOf(valores.get(i + 6)));
			deforestacion.setVegetacionSecundaria(Double.valueOf(valores
					.get(i + 7)));
			deforestacion.setZonasQuemadas(Double.valueOf(valores.get(i + 8)));
			deforestacion.setOtrasAreasSinVegetacion(Double.valueOf(valores
					.get(i + 9)));
			deforestacion.setVegetacionAcuatica(Double.valueOf(valores
					.get(i + 10)));
			deforestacion.setSuperficiesAgua(Double
					.valueOf(valores.get(i + 11)));
			index++;
		}

		// for (int i = 0; i < nombresMapas.size(); i++) {
		// DataBuffer e = super.obtenerPixeles(tipoReporte, ItemCruce,
		// nombresMapas.get(i));
		// for (int j = 0; j < e.getSize(); j++) {
		// if (e.getElem(j) > 0) {
		// numeroPixeles++;
		// organizarInformacion(e.getElem(j));
		// }
		// }
		// }
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
//		deforestacion = (InformacionReporteDeforestacion) super.getInfo().get(
//				index);
//		if (mod == 0)
//			deforestacion.setAreasUrbanizadas(deforestacion
//					.getAreasUrbanizadas() + 1);
//		else if (mod == 1)
//			deforestacion.setCultivosTransitorios(deforestacion
//					.getCultivosTransitorios() + 1);
//		else if (mod == 2)
//			deforestacion.setCultivosPermanentes(deforestacion
//					.getCultivosPermanentes() + 1);
//		else if (mod == 3)
//			deforestacion.setPastosPlantacionForestal(deforestacion
//					.getPastosPlantacionForestal() + 1);
//		else if (mod == 4)
//			deforestacion
//					.setAreasAgricolas(deforestacion.getAreasAgricolas() + 1);
//		else if (mod == 5)
//			deforestacion.setHerbazales(deforestacion.getHerbazales() + 1);
//		else if (mod == 6)
//			deforestacion.setArbustales(deforestacion.getArbustales() + 1);
//		else if (mod == 7)
//			deforestacion.setVegetacionSecundaria(deforestacion
//					.getVegetacionSecundaria() + 1);
//		else if (mod == 8)
//			deforestacion
//					.setZonasQuemadas(deforestacion.getZonasQuemadas() + 1);
//		else if (mod == 9)
//			deforestacion.setOtrasAreasSinVegetacion(deforestacion
//					.getOtrasAreasSinVegetacion() + 1);
//		else if (mod == 10)
//			deforestacion.setVegetacionAcuatica(deforestacion
//					.getVegetacionAcuatica() + 1);
//		else if (mod == 11)
//			deforestacion
//					.setSuperficiesAgua(deforestacion.getSuperficiesAgua() + 1);
//	}

	private void obtenerTotales(ArrayList<Object> info) {
		for (int i = 0; i < info.size(); i++) {
			InformacionReporteDeforestacion b = (InformacionReporteDeforestacion) info
					.get(i);
			totalArbustales += b.getArbustales().intValue();
			totalAreaAgricola += b.getAreasAgricolas().intValue();
			totalAreaUrbanizada += b.getAreasUrbanizadas().intValue();
			totalCultivoPermanente += b.getCultivosPermanentes().intValue();
			totalCultivoTransitorio += b.getCultivosTransitorios().intValue();
			totalHerbazales += b.getHerbazales().intValue();
			totalAreaSinVegetacion += b.getOtrasAreasSinVegetacion().intValue();
			totalPastosPlantacion += b.getPastosPlantacionForestal().intValue();
			totalSuperficieAgua += b.getSuperficiesAgua().intValue();
			totalVegetacionAgua += b.getVegetacionAcuatica().intValue();
			totalVegetacionSecundaria += b.getVegetacionSecundaria().intValue();
			totalZonasQuemadas = b.getZonasQuemadas().intValue();
		}
	}

	private void calcularHectareas(ArrayList<Object> info) {
		obtenerTotales(info);
		for (int i = 0; i < info.size(); i++) {
			Integer areaDivision;
			InformacionReporteDeforestacion b = (InformacionReporteDeforestacion) info
					.get(i);
			areaDivision = b.getArbustales().intValue()
					+ b.getAreasAgricolas().intValue()
					+ b.getAreasUrbanizadas().intValue()
					+ b.getCultivosPermanentes().intValue()
					+ b.getCultivosTransitorios().intValue()
					+ b.getHerbazales().intValue()
					+ b.getOtrasAreasSinVegetacion().intValue()
					+ b.getPastosPlantacionForestal().intValue()
					+ b.getSuperficiesAgua().intValue()
					+ b.getVegetacionAcuatica().intValue()
					+ b.getVegetacionSecundaria().intValue()
					+ b.getZonasQuemadas().intValue();
			b.setIdReporte(idReporte);
			BigDecimal factor;
//			if (tipoReporte.equals(1))
				factor  = new BigDecimal(0.09295872);
//			else
//				factor = new BigDecimal(625);
			if (areaDivision != 0) {
				b.setPorcArbustales(Util.calcularPorcentaje(totalArbustales, b
						.getArbustales().intValue()));
				b.setPorcAreasAgricolas(Util.calcularPorcentaje(
						totalAreaAgricola, b.getAreasAgricolas().intValue()));
				b.setPorcAreasUrbanizadas(Util
						.calcularPorcentaje(totalAreaUrbanizada, b
								.getAreasUrbanizadas().intValue()));
				b.setPorcCultivosPermanentes(Util.calcularPorcentaje(
						totalCultivoPermanente, b.getCultivosPermanentes()
								.intValue()));
				b.setPorcCultivosTransitorios(Util.calcularPorcentaje(
						totalCultivoTransitorio, b.getCultivosTransitorios()
								.intValue()));
				b.setPorcHerbazales(Util.calcularPorcentaje(totalHerbazales, b
						.getHerbazales().intValue()));
				b.setPorcOtrasAreasSinVegetacion(Util.calcularPorcentaje(
						totalAreaSinVegetacion, b.getOtrasAreasSinVegetacion()
								.intValue()));
				b.setPorcPastosPlantacionForestal(Util.calcularPorcentaje(
						totalPastosPlantacion, b.getPastosPlantacionForestal()
								.intValue()));
				b
						.setPorcSuperficiesAgua(Util.calcularPorcentaje(
								totalSuperficieAgua, b.getSuperficiesAgua()
										.intValue()));
				b.setPorcVegetacionAcuatica(Util.calcularPorcentaje(
						totalVegetacionAgua, b.getVegetacionAcuatica()
								.intValue()));
				b.setPorcVegetacionSecundaria(Util.calcularPorcentaje(
						totalVegetacionSecundaria, b.getVegetacionSecundaria()
								.intValue()));
				b.setPorcZonasQuemadas(Util.calcularPorcentaje(
						totalZonasQuemadas, b.getZonasQuemadas().intValue()));
				b.setArbustales(Util.hectariasPixeles(b.getArbustales()
						.intValue(), factor));
				b.setAreasAgricolas(Util.hectariasPixeles(b.getAreasAgricolas()
						.intValue(), factor));
				b.setAreasUrbanizadas(Util.hectariasPixeles(b
						.getAreasUrbanizadas().intValue(), factor));
				b.setCultivosPermanentes(Util.hectariasPixeles(b
						.getCultivosPermanentes().intValue(), factor));
				b.setCultivosTransitorios(Util.hectariasPixeles(b
						.getCultivosTransitorios().intValue(), factor));
				b.setHerbazales(Util.hectariasPixeles(b.getHerbazales()
						.intValue(), factor));
				b.setOtrasAreasSinVegetacion(Util.hectariasPixeles(b
						.getOtrasAreasSinVegetacion().intValue(), factor));
				b.setPastosPlantacionForestal(Util.hectariasPixeles(b
						.getPastosPlantacionForestal().intValue(), factor));
				b.setSuperficiesAgua(Util.hectariasPixeles(b
						.getSuperficiesAgua().intValue(), factor));
				b.setVegetacionAcuatica(Util.hectariasPixeles(b
						.getVegetacionAcuatica().intValue(), factor));
				b.setVegetacionSecundaria(Util.hectariasPixeles(b
						.getVegetacionSecundaria().intValue(), factor));
				b.setZonasQuemadas(Util.hectariasPixeles(b.getZonasQuemadas()
						.intValue(), factor));
				b.setPorcentaje(Util.calcularPorcentaje(numeroPixeles,
						areaDivision));
			} else {
				b.setPorcArbustales(new Double(0));
				b.setPorcAreasAgricolas(new Double(0));
				b.setPorcAreasUrbanizadas(new Double(0));
				b.setPorcCultivosPermanentes(new Double(0));
				b.setPorcCultivosTransitorios(new Double(0));
				b.setPorcHerbazales(new Double(0));
				b.setPorcOtrasAreasSinVegetacion(new Double(0));
				b.setPorcPastosPlantacionForestal(new Double(0));
				b.setPorcSuperficiesAgua(new Double(0));
				b.setPorcVegetacionAcuatica(new Double(0));
				b.setPorcVegetacionSecundaria(new Double(0));
				b.setPorcZonasQuemadas(new Double(0));
				b.setPorcentaje(new Double(0));
			}
		}
	}

//	private void sumarPonderaciones(ArrayList<Object> info) {
//		DecimalFormat df = new DecimalFormat("########.##");
//		InformacionReporteDeforestacion iDeforestacion = (InformacionReporteDeforestacion) info
//				.get(0);
//		for (int i = 1; i < info.size(); i++) {
//			InformacionReporteDeforestacion ib = (InformacionReporteDeforestacion) info
//					.get(i);
//			Double valorArbustales = Util.obtenerInfoProcentaje(iDeforestacion
//					.getArbustales(), ib.getPorcentaje());
//			Double valorAreasAgricolas = Util.obtenerInfoProcentaje(
//					iDeforestacion.getAreasAgricolas(), ib.getPorcentaje());
//			Double valorAreasUrbanizadas = Util.obtenerInfoProcentaje(
//					iDeforestacion.getAreasUrbanizadas(), ib.getPorcentaje());
//			Double valorCultivosPermanentes = Util
//					.obtenerInfoProcentaje(iDeforestacion
//							.getCultivosPermanentes(), ib.getPorcentaje());
//			Double valorCultivosTransitorios = Util.obtenerInfoProcentaje(
//					iDeforestacion.getCultivosTransitorios(), ib
//							.getPorcentaje());
//			Double valorHerbazales = Util.obtenerInfoProcentaje(iDeforestacion
//					.getHerbazales(), ib.getPorcentaje());
//			Double valorAreasSinVegetacion = Util.obtenerInfoProcentaje(
//					iDeforestacion.getOtrasAreasSinVegetacion(), ib
//							.getPorcentaje());
//			Double valorPastos = Util.obtenerInfoProcentaje(iDeforestacion
//					.getPastosPlantacionForestal(), ib.getPorcentaje());
//			Double valorSuperficieAgua = Util.obtenerInfoProcentaje(
//					iDeforestacion.getSuperficiesAgua(), ib.getPorcentaje());
//			Double valorVegetacionAcuatica = Util.obtenerInfoProcentaje(
//					iDeforestacion.getVegetacionAcuatica(), ib.getPorcentaje());
//			Double valorVegetacionSecundaria = Util.obtenerInfoProcentaje(
//					iDeforestacion.getVegetacionSecundaria(), ib
//							.getPorcentaje());
//			Double valorZonasQuemadas = Util.obtenerInfoProcentaje(
//					iDeforestacion.getZonasQuemadas(), ib.getPorcentaje());
//
//			ib.setArbustales(Double.valueOf(df.format(
//					ib.getArbustales() + valorArbustales).replace(",", ".")));
//			ib.setAreasAgricolas(Double.valueOf(df.format(
//					ib.getAreasAgricolas() + valorAreasAgricolas).replace(",",
//					".")));
//			ib.setAreasUrbanizadas(Double.valueOf(df.format(
//					ib.getAreasUrbanizadas() + valorAreasUrbanizadas).replace(
//					",", ".")));
//			ib.setCultivosPermanentes(Double.valueOf(df.format(
//					ib.getCultivosPermanentes() + valorCultivosPermanentes)
//					.replace(",", ".")));
//			ib.setCultivosTransitorios(Double.valueOf(df.format(
//					ib.getCultivosTransitorios() + valorCultivosTransitorios)
//					.replace(",", ".")));
//			ib.setHerbazales(Double.valueOf(df.format(
//					ib.getHerbazales() + valorHerbazales).replace(",", ".")));
//			ib.setOtrasAreasSinVegetacion(Double.valueOf(df.format(
//					ib.getOtrasAreasSinVegetacion() + valorAreasSinVegetacion)
//					.replace(",", ".")));
//			ib.setPastosPlantacionForestal(Double.valueOf(df.format(
//					ib.getPastosPlantacionForestal() + valorPastos).replace(
//					",", ".")));
//			ib.setSuperficiesAgua(Double.valueOf(df.format(
//					ib.getSuperficiesAgua() + valorSuperficieAgua).replace(",",
//					".")));
//			ib.setVegetacionAcuatica(Double.valueOf(df.format(
//					ib.getVegetacionAcuatica() + valorVegetacionAcuatica)
//					.replace(",", ".")));
//			ib.setVegetacionSecundaria(Double.valueOf(df.format(
//					ib.getVegetacionSecundaria() + valorVegetacionSecundaria)
//					.replace(",", ".")));
//			ib.setZonasQuemadas(Double.valueOf(df.format(
//					ib.getZonasQuemadas() + valorZonasQuemadas).replace(",",
//					".")));
//
//		}
//	}

	private void registrarInfo(ArrayList<Object> info) {
		for (int i = 1; i < info.size(); i++) {
			InformacionReporteDeforestacion d = (InformacionReporteDeforestacion) info
					.get(i);
			infr.IngresarInfoDeforestacion(d);
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

	/*public static void main(String a[]) {
		String mapa = "";
//		ArrayList<String> mapas = new ArrayList<String>();
		// mapas.add("defregioninferior");
//		mapas.add("defregionsuperior");
		Integer[] p = { 1990 };
		new AnalisisResultadosWPSDeforestacion(5, 3, mapa, p);
	}*/

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
