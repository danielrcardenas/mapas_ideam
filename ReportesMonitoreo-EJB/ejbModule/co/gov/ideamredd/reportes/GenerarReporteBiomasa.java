package co.gov.ideamredd.reportes;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.biomasa.ConsultaBiomasa;
import co.gov.ideamredd.entities.BiomasaYCarbono;
import co.gov.ideamredd.entities.MetodologiaBiomasa;
import co.gov.ideamredd.util.GeneradorArchivosExcel;
import co.gov.ideamredd.util.Util;

@Stateless
public class GenerarReporteBiomasa {

	@EJB
	ConsultaBiomasa cb;

	private ArrayList<BiomasaYCarbono> biomasas;
	private ArrayList<String> encabezado = new ArrayList<String>();
	private ArrayList<String> informacion = new ArrayList<String>();
	private HashMap<String, String> parametros;
	private String info = "";
	private String nombreReporte;
	private static final String archivoEncabezados = "co/gov/ideamredd/reportes/encabezados";
	private static final ResourceBundle encabezados = ResourceBundle
			.getBundle(archivoEncabezados);
	MetodologiaBiomasa m = null;

	public String[] generarReporte() {
		String reporte[] = new String[3];
		encabezado.clear();
		informacion.clear();
		GeneradorArchivosExcel excel = new GeneradorArchivosExcel();
		seleccionarEncabezado();
		seleccionarInformacion();
		if (biomasas.size() > 1)
			nombreReporte = "Consolidado Biomasa Parcelas "
					+ Util.obtenerFechaActual();
		else {
			BiomasaYCarbono b = biomasas.get(0);
			nombreReporte = "Reporte Biomasa " + b.getConsecutivo();
		}
		String archivo = excel.generarReporte(encabezado, informacion,
				nombreReporte);
		if (!archivo.equals("")) {
			reporte[0] = archivo;
			reporte[1] = LecturaArchivo.getTablaReporte(LecturaArchivo
					.lecturaArchivo(archivo));
			reporte[2] = "";
		}
		return reporte;

	}

	private void seleccionarEncabezado() {
		System.out.println();
		if (parametros.get("codigo").equals("on"))
			encabezado.add(Util.obtenerClave("biomasa.codigo", encabezados));
		if (parametros.get("biomasa").equals("on"))
			encabezado.add(Util.obtenerClave("biomasa.valor", encabezados));
		if (parametros.get("carbono").equals("on"))
			encabezado.add(Util.obtenerClave("biomasa.carbono", encabezados));
		if (parametros.get("fechainicio").equals("on"))
			encabezado.add(Util.obtenerClave("biomasa.fecha", encabezados));
		if (parametros.get("estado").equals("on"))
			encabezado.add(Util.obtenerClave("biomasa.estado", encabezados));
		if (parametros.get("codparcela").equals("on"))
			encabezado
					.add(Util.obtenerClave("biomasa.codparcela", encabezados));
		if (parametros.get("nommetodologia").equals("on"))
			encabezado.add(Util.obtenerClave("biomasa.nommetodologia",
					encabezados));
		if (parametros.get("descmetodologia").equals("on"))
			encabezado.add(Util.obtenerClave("biomasa.descmetodologia",
					encabezados));
	}

	private void seleccionarInformacion() {

		for (int i = 0; i < biomasas.size(); i++) {
			info = "";
			BiomasaYCarbono b = biomasas.get(i);
			MetodologiaBiomasa m = cb.consultarMetodologiaBiomasa(
					b.getMetodologia(), "");
			if (parametros.get("codigo").equals("on"))
				armarStringInformacion(b.getConsecutivo().toString());
			if (parametros.get("biomasa").equals("on"))
				armarStringInformacion(b.getBiomasa().toString());
			if (parametros.get("carbono").equals("on"))
				armarStringInformacion(b.getCarbono().toString());
			if (parametros.get("fechainicio").equals("on"))
				armarStringInformacion(b.getFechaInicio().toString());
			if (parametros.get("estado").equals("on"))
				armarStringInformacion(b.getEstado().toString());
			if (parametros.get("codparcela").equals("on"))
				armarStringInformacion(b.getIdParcela().toString());
			if (parametros.get("nommetodologia").equals("on"))
				armarStringInformacion(m.getNombre());
			if (parametros.get("descmetodologia").equals("on"))
				armarStringInformacion(m.getDescripcion());
			informacion.add(info);
		}
	}

	private void armarStringInformacion(String dato) {
		if (info.equals(""))
			info = dato;
		else
			info += "|!" + dato;
	}

	public HashMap<String, String> getParametros() {
		return parametros;
	}

	public void setParametros(HashMap<String, String> parametros) {
		this.parametros = parametros;
	}

	public ArrayList<BiomasaYCarbono> getParcelas() {
		return biomasas;
	}

	public void setBiomasas(ArrayList<BiomasaYCarbono> biomasas) {
		this.biomasas = biomasas;
	}

}
