package co.gov.ideamredd.dao;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import co.gov.ideamredd.conexion.ConexionBD;
//import co.gov.ideamredd.biomasa.ConsultaBiomasa;
import co.gov.ideamredd.entities.BiomasaYCarbono;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.ContactoParcela;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.Organizacion;
//import co.gov.ideamredd.entities.MetodologiaBiomasa;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.Parcela;
import co.gov.ideamredd.entities.Proposito;
//import co.gov.ideamredd.entities.Proyecto;
import co.gov.ideamredd.entities.Temporalidad;
import co.gov.ideamredd.entities.TipoInventario;
//import co.gov.ideamredd.parcela.dao.ConsultarAsociadosParcela;
import co.gov.ideamredd.util.GeneradorArchivosExcel;
import co.gov.ideamredd.util.Util;

@Stateless
public class GenerarReporteParcela {

	@EJB
	private ConsultarAsociadosParcela consultarAsociadosParcela;

	private String idParcelas;
	private ArrayList<Parcela> parcelas;
	private ArrayList<String> encabezado = new ArrayList<String>();
	private ArrayList<String> informacion = new ArrayList<String>();
	private HashMap<String, String> parametros;
	private String info = "";
	private String nombreReporte;
	// private static final String archivoEncabezados =
	// "co/gov/ideamredd/reportes/encabezados";
	// private static final ResourceBundle encabezados = ResourceBundle
	// .getBundle(archivoEncabezados);

	private static final Integer FGDA = 1;
	private static final Integer Propietario = 2;
	private static final Integer Custodio = 3;
	private static final Integer Investigador = 4;
	private static final Integer Coleccion = 5;
	private static final Integer Encargado = 6;
	private static final Integer Brigadista = 7;
	private static final Integer Supervisor = 8;
	private ArrayList<ContactoParcela> contactos;

	public String[] generarReporte(String path) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		String reporte[] = new String[3];
		parcelas = new ArrayList<Parcela>();
		String[] pars = idParcelas.split(",");
		if (pars.length > 0) {
			for (int x = 0; x < pars.length; x++) {
				String[] ident = pars[x].split(";");
				ids.add(Integer.valueOf(ident[0]));
			}
			for (int i = 0; i < ids.size(); i++) {
				Parcela p = consultarAsociadosParcela
						.obtenerParcela(ids.get(i));
				parcelas.add(p);
			}
		}

		encabezado.clear();
		informacion.clear();
		GeneradorArchivosExcel excel = new GeneradorArchivosExcel();
		seleccionarEncabezado();
		seleccionarInformacion();
		if (parcelas.size() > 1)
			nombreReporte ="Consolidado_Parcelas_" + Util.obtenerFechaActual();
		else {
			Parcela p = parcelas.get(0);
			nombreReporte = "Reporte_Parcelas_" + p.getNombre();
		}
		String archivo []=  {excel.generarReporte(encabezado, informacion,
						nombreReporte)};
//		if (!archivo.equals("")) {
//			reporte[0] = archivo;
//			// reporte[1] =
//			// LecturaArchivo.getTablaReporte(LecturaArchivo.lecturaArchivo(archivo));
//			// reporte[2]="";
//		}
		return archivo;

	}

	private void seleccionarEncabezado() {
		if (parametros.get("codigo").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.codigo"));
		if (parametros.get("nombre").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.nombre"));
		if (parametros.get("codigoCampo").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.codigoCampo"));
//		if (parametros.get("proyecto").equals("on"))
//			encabezado.add(Util.obtenerTitulosParcelas("parcela.proyectoR"));
		if (parametros.get("tipoParcela").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.tipoParcela"));
		if (parametros.get("fecha").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.fechaE"));
		if (parametros.get("aprov").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.evidencia"));
		if (parametros.get("tipoInventario").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.tipoIn"));
		if (parametros.get("descripcion").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.descParcela"));
		if (parametros.get("proposito").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.proposito"));
		if (parametros.get("observaciones").equals("on"))
			encabezado
					.add(Util.obtenerTitulosParcelas("parcela.observaciones"));
		if (parametros.get("archivo").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.nombreFoto"));
		if (parametros.get("geometria").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.geometria"));
		if (parametros.get("puntos").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.coord"));
		if (parametros.get("pais").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.pais"));
		if (parametros.get("departamento").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.depto"));
		if (parametros.get("municipio").equals("on"))
			encabezado.add(Util.obtenerTitulosParcelas("parcela.municipio"));
		if (parametros.get("fgda").equals("on")) {
			encabezado.add(Util.obtenerTitulosParcelas("parcela.nombreFgda"));
			encabezado.add(Util.obtenerTitulosParcelas("parcela.contactoFgda"));
			encabezado.add(Util.obtenerTitulosParcelas("parcela.correoFgda"));
		}
		if (parametros.get("propietario").equals("on")) {
			encabezado.add(Util.obtenerTitulosParcelas("parcela.nombreAutor"));
			encabezado
					.add(Util.obtenerTitulosParcelas("parcela.contactoAutor"));
			encabezado.add(Util.obtenerTitulosParcelas("parcela.correoAutor"));
		}
//		if (parametros.get("coleccion").equals("on")) {
//			encabezado.add(Util
//					.obtenerTitulosParcelas("parcela.direccionColeccion"));
//			encabezado.add(Util
//					.obtenerTitulosParcelas("parcela.telefonoColeccion"));
//			encabezado.add(Util
//					.obtenerTitulosParcelas("parcela.nombreEncargado"));
//			encabezado.add(Util
//					.obtenerTitulosParcelas("parcela.correoEncargado"));
//		}
		if (parametros.get("custodio").equals("on")) {
			encabezado.add(Util
					.obtenerTitulosParcelas("parcela.nombreCustodio"));
			encabezado.add(Util
					.obtenerTitulosParcelas("parcela.contactoCustodio"));
			encabezado.add(Util
					.obtenerTitulosParcelas("parcela.correoCustodio"));
		}
		if (parametros.get("otrosConts").equals("on")) {
			encabezado.add(Util
					.obtenerTitulosParcelas("parcela.nombreInvestigador"));
			encabezado.add(Util
					.obtenerTitulosParcelas("parcela.correoInvestigador"));
		}
		if (parametros.get("biomasa").equals("on")) {
			encabezado.add(Util.obtenerTitulosParcelas("parcela.biomasa"));
			encabezado.add(Util.obtenerTitulosParcelas("parcela.carbono"));
			encabezado.add(Util
					.obtenerTitulosParcelas("parcela.fechaGeneracion"));
			encabezado.add(Util.obtenerTitulosParcelas("parcela.nombreMeto"));
			encabezado.add(Util
					.obtenerTitulosParcelas("parcela.descripcionMeto"));
		}
	}

	private void seleccionarInformacion() {

		for (int i = 0; i < parcelas.size(); i++) {
			info = "";
			Parcela p = parcelas.get(i);
			if (parametros.get("codigo").equals("on"))
				armarStringInformacion(p.getConsecutivo().toString());
			if (parametros.get("nombre").equals("on"))
				armarStringInformacion(p.getNombre());
			if (parametros.get("codigoCampo").equals("on"))
				armarStringInformacion(p.getCodigoCampo());
			if (parametros.get("tipoParcela").equals("on")) {
				Temporalidad t = consultarAsociadosParcela
						.ConsultarTemporalidadParcela(p.getTemporalidad());
				armarStringInformacion(t.getNombre());
			}
			if (parametros.get("fecha").equals("on"))
				armarStringInformacion(p.getFechaEstablecimiento().toString());
			if (parametros.get("aprov").equals("on"))
				armarStringInformacion(p.getAprovechamiento().equals("0") ? "No"
						: "Si");
			if (parametros.get("tipoInventario").equals("on")) {
				TipoInventario ti = consultarAsociadosParcela
						.ConsultarTipoInventarioParcela(p.getInventario());
				armarStringInformacion(ti.getNombre());
			}
			if (parametros.get("descripcion").equals("on"))
				armarStringInformacion(p.getDescripcion());
			if (parametros.get("proposito").equals("on")) {
				Proposito pro = consultarAsociadosParcela
						.ConsultarPropositoParcela(p.getProposito());
				armarStringInformacion(pro.getNombre());
			}
			if (parametros.get("observaciones").equals("on"))
				armarStringInformacion(p.getObservaciones());
			if (parametros.get("archivo").equals("on"))
				armarStringInformacion(p.getNombreImagen());
			String[] geo = consultarAsociadosParcela.ConsultarGeoParcela(p
					.getConsecutivo());
			if (parametros.get("geometria").equals("on")) {
				if (geo != null) {
					if (geo[0].equals("1"))
						armarStringInformacion("Punto");
					else if (geo[0].equals("2"))
						armarStringInformacion("Trayecto");
					else if (geo[0].equals("3"))
						armarStringInformacion("Poligono");
				} else
					armarStringInformacion("");
			}
			if (parametros.get("puntos").equals("on"))
				if (geo != null)
					armarStringInformacion(geo[1]);
				else
					armarStringInformacion("");
			if (parametros.get("pais").equals("on")) {
				Pais pais = consultarAsociadosParcela.ConsultarPaisParcela(p
						.getConsecutivo());
				armarStringInformacion(pais.getNombre());
			}
			if (parametros.get("departamento").equals("on")) {
				String departamentos = "";
				ArrayList<Depto> deptos = consultarAsociadosParcela
						.consultaDeptoParcela(p.getConsecutivo());
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
				String municipios = "";
				ArrayList<Municipios> municipio = consultarAsociadosParcela
						.ConsultaMunicipioParcela(p.getConsecutivo());
				for (int j = 0; j < municipio.size(); j++) {
					Municipios m = municipio.get(j);
					if (municipios.equals(""))
						municipios = m.getNombre();
					else
						municipios += "," + m.getNombre();
				}
				armarStringInformacion(municipios);
			}
			contactos = consultarAsociadosParcela
					.consultaContactosParcelaId(Integer.valueOf(p
							.getConsecutivo()));
			if (parametros.get("fgda").equals("on")) {
				consultarContacto(FGDA);
			}
			if (parametros.get("propietario").equals("on")) {
				consultarContacto(Propietario);
			}
			if (parametros.get("custodio").equals("on")) {
				consultarContacto(Custodio);
			}
			if (parametros.get("otrosConts").equals("on")) {
				consultarContacto(Investigador);
				consultarContacto(Coleccion);
				consultarContacto(Encargado);
				consultarContacto(Brigadista);
				consultarContacto(Supervisor);
			}
			BiomasaYCarbono bc = consultarAsociadosParcela
					.ConsultarUltimaBiomasaParcela(p.getConsecutivo());

			if (parametros.get("biomasa").equals("on")) {
				if (bc != null) {
					// MetodologiaBiomasa m =
					// ConsultaBiomasa.consultarMetodologiaBiomasa(
					// bc.getMetodologia(),"");
					armarStringInformacion(bc.getBiomasa().toString());
					armarStringInformacion(bc.getCarbono().toString());
					armarStringInformacion(bc.getFechaInicio().toString());
					// armarStringInformacion(m.getNombre());
					// armarStringInformacion(m.getDescripcion());
				} else {
					armarStringInformacion("No posee dato de biomasa");
					armarStringInformacion("No posee dato de carbono");
					armarStringInformacion("No posee fecha de generacion");
					armarStringInformacion("No posee datos de metodologia");
					armarStringInformacion("No posee datos de metodologia");
				}
			}

			informacion.add(info);
		}
	}

	private void consultarContacto(Integer tipoContacto) {
		for (int i = 0; i < contactos.size(); i++) {
			ContactoParcela cp = contactos.get(i);
			if (cp != null)
				if (cp.getIdClase() == tipoContacto) {
					if (cp.getIdContacto() != 0) {
						Contacto contacto = consultarAsociadosParcela
								.ConsultarContactoId(cp.getIdContacto());
						armarStringInformacion(contacto.getNombre());
						armarStringInformacion(contacto.getTelefono());
						armarStringInformacion(contacto.getCorreo());
						if (!contacto.getMovil().equals(""))
							armarStringInformacion(contacto.getMovil());
					} else {
						Organizacion org = consultarAsociadosParcela
								.ConsultarOrganizacionId(cp.getIdOrganizacion());
						armarStringInformacion(org.getNombre());
						armarStringInformacion(org.getTelefono().toString());
						armarStringInformacion(org.getCorreo());
						if (!org.getDireccion().equals(""))
							armarStringInformacion(org.getDireccion());
					}
				}
		}

	}

	private void armarStringInformacion(String dato) {
		if (info.equals(""))
			info = dato;
		else
			info += "�" + dato;
	}

	public HashMap<String, String> getParametros() {
		return parametros;
	}

	public void setParametros(HashMap<String, String> parametros) {
		this.parametros = parametros;
	}

	public String getIdParcelas() {
		return idParcelas;
	}

	public void setIdParcelas(String idParcelas) {
		this.idParcelas = idParcelas;
	}

}
