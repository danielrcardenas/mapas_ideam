package co.gov.ideamredd.reportes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.entities.ActividadRedd;
import co.gov.ideamredd.entities.CAR;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.DocumentosAsociados;
import co.gov.ideamredd.entities.Metodologia;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.Proyecto;
import co.gov.ideamredd.entities.Tenencia;
import co.gov.ideamredd.entities.TipoBosque;
import co.gov.ideamredd.entities.Usuario;
import co.gov.ideamredd.parcela.dao.ConsultarAsociadosParcela;
import co.gov.ideamredd.proyecto.dao.ConsultarAsociadosProyecto;
import co.gov.ideamredd.util.GeneradorArchivosExcel;
import co.gov.ideamredd.util.Util;

@Stateless
public class GenerarReporteProyecto {

	@EJB
	ConsultarAsociadosProyecto capr;
	@EJB
	ConsultarAsociadosParcela cap;

	private ArrayList<Proyecto> proyectos;
	private ArrayList<String> encabezado = new ArrayList<String>();
	private ArrayList<String> informacion = new ArrayList<String>();
	private HashMap<String, String> parametros;
	private String info = "";
	private String nombreReporte;
	private static final String archivoEncabezados = "co/gov/ideamredd/reportes/encabezados";
	private static final ResourceBundle encabezados = ResourceBundle
			.getBundle(archivoEncabezados);

	public String[] generarReporte() {
		String reporte[] = new String[3];
		encabezado.clear();
		informacion.clear();
		GeneradorArchivosExcel excel = new GeneradorArchivosExcel();
		seleccionarEncabezado();
		seleccionarInformacion();
		if (proyectos.size() > 1)
			nombreReporte = "Consolidado Proyectos REDD "
					+ Util.obtenerFechaActual();
		else {
			Proyecto p = proyectos.get(0);
			nombreReporte = "Reporte Proyecto "
					+ (p.getNombre().length() > 20 ? p.getNombre().substring(0,
							20)
							+ "Def" : p.getNombre());
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
		if (parametros.get("nomprop").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.nomprop", encabezados));
		if (parametros.get("docprop").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.docprop", encabezados));
		if (parametros.get("celprop").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.celprop", encabezados));
		if (parametros.get("mailprop").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.mailprop", encabezados));
		if (parametros.get("codigo").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.codigo", encabezados));
		if (parametros.get("nombre").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.nombre", encabezados));
		if (parametros.get("descArea").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.desArea", encabezados));
		if (parametros.get("descBosque").equals("on"))
			encabezado.add(Util
					.obtenerClave("proyecto.descBosque", encabezados));
		if (parametros.get("fini").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.fini", encabezados));
		if (parametros.get("ffin").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.ffin", encabezados));
		if (parametros.get("duracion").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.duracion", encabezados));
		if (parametros.get("tenencia").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.tenencia", encabezados));
		if (parametros.get("actividades").equals("on"))
			encabezado
					.add(Util.obtenerClave("proyecto.actividad", encabezados));
		if (parametros.get("co2").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.co2", encabezados));
		if (parametros.get("meta").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.deforestacion",
					encabezados));
		if (parametros.get("metodologia").equals("on")) {
			encabezado.add(Util
					.obtenerClave("proyecto.nombreMeto", encabezados));
			encabezado.add(Util.obtenerClave("proyecto.descMeto", encabezados));
		}
		if (parametros.get("area").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.area", encabezados));
		if (parametros.get("tiposBosque").equals("on")) {
			encabezado.add(Util
					.obtenerClave("proyecto.tipoBosque", encabezados));
			encabezado.add(Util.obtenerClave("proyecto.altitud", encabezados));
			encabezado.add(Util.obtenerClave("proyecto.temperatura",
					encabezados));
			encabezado.add(Util.obtenerClave("proyecto.precipitacion",
					encabezados));
		}
		if (parametros.get("Pais").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.pais", encabezados));
		if (parametros.get("departamento").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.depto", encabezados));
		if (parametros.get("municipio").equals("on"))
			encabezado
					.add(Util.obtenerClave("proyecto.municipio", encabezados));
		if (parametros.get("car").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.car", encabezados));
		if (parametros.get("geometria").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.tipoGeometria",
					encabezados));
		if (parametros.get("puntos").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.coordenadas",
					encabezados));
		if (parametros.get("documentos").equals("on"))
			encabezado.add(Util.obtenerClave("proyecto.docsAsociados",
					encabezados));
	}

	private void seleccionarInformacion() {

		for (int i = 0; i < proyectos.size(); i++) {
			info = "";
			String tipoBosque = "";
			String altitud = "";
			String precipitacion = "";
			String temperatura = "";
			String nombreTenencia = "";
			String nombreActividad = "";
			String departamentos = "";
			String municipios = "";
			String car = "";

			Proyecto p = proyectos.get(i);
			Metodologia metod;
			metod = capr.ConsultarMetodologiaProyecto(p.getConsecutivo());
			Usuario u = cap.ConsultarContactoUsuario(p.getPropietario());
			if (parametros.get("nomprop").equals("on"))
				armarStringInformacion(u.getNombre() + " " + u.getApellidoUno()
						+ " " + u.getApellidoDos());
			if (parametros.get("docprop").equals("on"))
				armarStringInformacion(u.getIdentificacion());
			if (parametros.get("celprop").equals("on"))
				armarStringInformacion(u.getCelular());
			if (parametros.get("mailprop").equals("on"))
				armarStringInformacion(u.getCorreoElectronico());
			if (parametros.get("codigo").equals("on"))
				armarStringInformacion(p.getConsecutivo().toString());
			if (parametros.get("nombre").equals("on"))
				armarStringInformacion(p.getNombre());
			if (parametros.get("descArea").equals("on"))
				armarStringInformacion(p.getDescripcionArea() != null ? p
						.getDescripcionArea() : "");
			if (parametros.get("descBosque").equals("on"))
				armarStringInformacion(p.getTipoBosques() != null ? p
						.getTipoBosques() : "");
			if (parametros.get("fini").equals("on"))
				armarStringInformacion(p.getFechaInicio().toString());
			if (parametros.get("ffin").equals("on"))
				armarStringInformacion(p.getFechaFin().toString());
			if (parametros.get("duracion").equals("on"))
				armarStringInformacion(p.getDuracionProyecto().toString());
			if (parametros.get("tenencia").equals("on")) {
				ArrayList<Tenencia> t = null;
				if (t != null)
					t.clear();
				t = capr.ConsultarTenenciaProyecto(p.getConsecutivo());
				for (int j = 0; j < t.size(); j++) {
					Tenencia ten = t.get(j);
					if (nombreTenencia.equals(""))
						nombreTenencia = ten.getDescripcion();
					else
						nombreTenencia += "," + ten.getDescripcion();
				}
				armarStringInformacion(nombreTenencia);
			}
			if (parametros.get("actividades").equals("on")) {
				ArrayList<ActividadRedd> t = null;
				if (t != null)
					t.clear();
				t = capr.ConsultarActividadProyecto(p.getConsecutivo());
				for (int j = 0; j < t.size(); j++) {
					ActividadRedd act = t.get(j);
					if (nombreActividad.equals(""))
						nombreActividad = act.getNombre();
					else
						nombreActividad += "," + act.getNombre();
				}
				armarStringInformacion(nombreActividad);
			}
			if (parametros.get("co2").equals("on"))
				armarStringInformacion(p.getCo2Reducir().toString());
			if (parametros.get("meta").equals("on"))
				armarStringInformacion(p.getTasaDeforestar().toString());
			if (parametros.get("metodologia").equals("on")) {
				armarStringInformacion(metod.getNombre());
				armarStringInformacion(metod.getDescripcion());
			}
			if (parametros.get("area").equals("on"))
				armarStringInformacion(p.getArea()
						.divide(new BigDecimal(10000)).toString());
			if (parametros.get("tiposBosque").equals("on")) {
				ArrayList<TipoBosque> tb = null;
				if (tb != null)
					tb.clear();
				tb = capr.ConsultarTipoBosqueProyecto(p.getConsecutivo());
				for (int j = 0; j < tb.size(); j++) {
					TipoBosque tipoB = tb.get(j);
					tipoB.setTipoBosque(tipoB.getTipoBosque().replace(",", "."));
					tipoB.setAltitud(tipoB.getAltitud().replace(",", "."));
					tipoB.setTemperatura(tipoB.getTemperatura().replace(",",
							"."));
					tipoB.setPrecipitacion(tipoB.getPrecipitacion().replace(
							",", "."));
					if (tipoBosque.equals("")) {
						tipoBosque = tipoB.getTipoBosque();
						altitud = tipoB.getAltitud();
						precipitacion = tipoB.getPrecipitacion();
						temperatura = tipoB.getTemperatura();
					} else {
						tipoBosque += "," + tipoB.getTipoBosque();
						altitud += "," + tipoB.getAltitud();
						precipitacion += "," + tipoB.getPrecipitacion();
						temperatura += "," + tipoB.getTemperatura();
					}
				}

				armarStringInformacion(tipoBosque);
				armarStringInformacion(altitud);
				armarStringInformacion(temperatura);
				armarStringInformacion(precipitacion);
			}
			if (parametros.get("Pais").equals("on")) {
				Pais pais = capr.ConsultarPaisProyecto(p.getConsecutivo());
				armarStringInformacion(pais.getNombre());
			}
			if (parametros.get("departamento").equals("on")) {
				ArrayList<Depto> deptos = null;
				if (deptos != null)
					deptos.clear();
				deptos = capr.ConsultarDeptoProyecto(p.getConsecutivo());
				for (int j = 0; j < deptos.size(); j++) {
					Depto d = deptos.get(j);
					if (departamentos.equals(""))
						departamentos = d.getNombre();
					else
						departamentos += "," + d.getNombre();
				}
				armarStringInformacion(departamentos);
			}
			if (parametros.get("municipio").equals("on")) {
				ArrayList<Municipios> municipio = null;
				if (municipio != null)
					municipio.clear();
				municipio = capr.ConsultarMunicipioProyecto(p.getConsecutivo());
				for (int j = 0; j < municipio.size(); j++) {
					Municipios m = municipio.get(j);
					if (municipios.equals(""))
						municipios = m.getNombre();
					else
						municipios += "," + m.getNombre();
				}
				armarStringInformacion(municipios);
			}
			if (parametros.get("car").equals("on")) {
				ArrayList<CAR> cars = null;
				if (cars != null) {
					cars.clear();
				}
				cars = capr.ConsultarCarProyecto(p.getConsecutivo());
				for (int j = 0; j < cars.size(); j++) {
					CAR c = cars.get(j);
					if (car.equals(""))
						car = c.getNombre();
					else
						car += "," + c.getNombre();
				}
				armarStringInformacion(car);
			}
			String[] geo = capr.ConsultarGeoProyecto(p.getConsecutivo());
			if (parametros.get("geometria").equals("on"))
				if (geo[0].equals("1"))
					armarStringInformacion("Punto");
				else if (geo[0].equals("2"))
					armarStringInformacion("Trayecto");
				else if (geo[0].equals("3"))
					armarStringInformacion("Poligono");
			if (parametros.get("puntos").equals("on"))
				armarStringInformacion(geo[1]);
			ArrayList<DocumentosAsociados> docs = capr
					.ConsultarDocumentosProyecto(p.getConsecutivo());
			if (parametros.get("documentos").equals("on")) {
				String tmpDocs = " ";
				for (int d = 0; d < docs.size(); d++) {
					DocumentosAsociados doc = docs.get(d);
					tmpDocs += "-" + doc.getUn_NombreDocumento() + "\n";
				}
				armarStringInformacion(tmpDocs);
			}
			informacion.add(info);
		}
	}

	private void armarStringInformacion(String dato) {
		if (info.equals(""))
			info = dato;
		else
			info += "�" + dato;
	}

	public ArrayList<Proyecto> getProyectos() {
		return proyectos;
	}

	public void setProyectos(ArrayList<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	public HashMap<String, String> getParametros() {
		return parametros;
	}

	public void setParametros(HashMap<String, String> parametros) {
		this.parametros = parametros;
	}

}
