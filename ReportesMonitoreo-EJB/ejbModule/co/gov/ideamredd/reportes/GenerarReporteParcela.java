package co.gov.ideamredd.reportes;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.biomasa.ConsultaBiomasa;
import co.gov.ideamredd.entities.BiomasaYCarbono;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.MetodologiaBiomasa;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.Parcela;
import co.gov.ideamredd.entities.Proposito;
import co.gov.ideamredd.entities.Proyecto;
import co.gov.ideamredd.entities.Temporalidad;
import co.gov.ideamredd.entities.TipoInventario;
import co.gov.ideamredd.parcela.dao.ConsultarAsociadosParcela;
import co.gov.ideamredd.util.GeneradorArchivosExcel;
import co.gov.ideamredd.util.Util;

@Stateless
public class GenerarReporteParcela {

	@EJB
	ConsultarAsociadosParcela cap;
	@EJB
	ConsultaBiomasa cb;

	private ArrayList<Parcela> parcelas;
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
		if (parcelas.size() > 1)
			nombreReporte = "Consolidado Parcelas " + Util.obtenerFechaActual();
		else {
			Parcela p = parcelas.get(0);
			nombreReporte = "Reporte Parcelas " + p.getNombre();
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
			encabezado.add(Util.obtenerClave("parcela.codigo", encabezados));
		if (parametros.get("nombre").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.nombre", encabezados));
		if (parametros.get("codigoCampo").equals("on"))
			encabezado.add(Util
					.obtenerClave("parcela.codigoCampo", encabezados));
		if (parametros.get("proyecto").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.proyectoR", encabezados));
		if (parametros.get("tipoParcela").equals("on"))
			encabezado.add(Util
					.obtenerClave("parcela.tipoParcela", encabezados));
		if (parametros.get("fecha").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.fechaE", encabezados));
		if (parametros.get("aprov").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.evidencia", encabezados));
		if (parametros.get("tipoInventario").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.tipoIn", encabezados));
		if (parametros.get("descripcion").equals("on"))
			encabezado.add(Util
					.obtenerClave("parcela.descParcela", encabezados));
		if (parametros.get("proposito").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.proposito", encabezados));
		if (parametros.get("observaciones").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.observaciones",
					encabezados));
		if (parametros.get("foto").equals("on"))
			encabezado
					.add(Util.obtenerClave("parcela.nombreFoto", encabezados));
		if (parametros.get("geometria").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.geometria", encabezados));
		if (parametros.get("puntos").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.coord", encabezados));
		if (parametros.get("medidas").equals("on"))
			encabezado
					.add(Util.obtenerClave("parcela.largoAncho", encabezados));
		if (parametros.get("pais").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.pais", encabezados));
		if (parametros.get("departamento").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.depto", encabezados));
		if (parametros.get("municipio").equals("on"))
			encabezado.add(Util.obtenerClave("parcela.municipio", encabezados));
		if (parametros.get("fgda").equals("on")) {
			encabezado
					.add(Util.obtenerClave("parcela.nombreFgda", encabezados));
			encabezado.add(Util.obtenerClave("parcela.contactoFgda",
					encabezados));
			encabezado
					.add(Util.obtenerClave("parcela.correoFgda", encabezados));
		}
		if (parametros.get("autor").equals("on")) {
			encabezado.add(Util
					.obtenerClave("parcela.nombreAutor", encabezados));
			encabezado.add(Util.obtenerClave("parcela.contactoAutor",
					encabezados));
			encabezado.add(Util
					.obtenerClave("parcela.correoAutor", encabezados));
		}
		if (parametros.get("coleccion").equals("on")) {
			encabezado.add(Util.obtenerClave("parcela.direccionColeccion",
					encabezados));
			encabezado.add(Util.obtenerClave("parcela.telefonoColeccion",
					encabezados));
			encabezado.add(Util.obtenerClave("parcela.nombreEncargado",
					encabezados));
			encabezado.add(Util.obtenerClave("parcela.correoEncargado",
					encabezados));
		}
		if (parametros.get("custodio").equals("on")) {
			encabezado.add(Util.obtenerClave("parcela.nombreCustodio",
					encabezados));
			encabezado.add(Util.obtenerClave("parcela.contactoCustodio",
					encabezados));
			encabezado.add(Util.obtenerClave("parcela.correoCustodio",
					encabezados));
		}
		if (parametros.get("investigador").equals("on")) {
			encabezado.add(Util.obtenerClave("parcela.nombreInvestigador",
					encabezados));
			encabezado.add(Util.obtenerClave("parcela.correoInvestigador",
					encabezados));
		}
		if (parametros.get("biomasa").equals("on")) {
			encabezado.add(Util.obtenerClave("parcela.biomasa", encabezados));
			encabezado.add(Util.obtenerClave("parcela.carbono", encabezados));
			encabezado.add(Util.obtenerClave("parcela.fechaGeneracion",
					encabezados));
			encabezado
					.add(Util.obtenerClave("parcela.nombreMeto", encabezados));
			encabezado.add(Util.obtenerClave("parcela.descripcionMeto",
					encabezados));
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
			if (parametros.get("proyecto").equals("on")) {
				if (p.getPerteneceProyecto().equals("1")) {
					Proyecto proyecto = cap.ConsultaProyectoParcela(p
							.getConsecutivo());
					armarStringInformacion(proyecto.getConsecutivo() + "-"
							+ proyecto.getNombre());
				} else
					armarStringInformacion("No pertenece a ningun proyecto REDD");
			}
			if (parametros.get("tipoParcela").equals("on")) {
				Temporalidad t = cap.ConsultarTemporalidadParcela(p
						.getTemporalidad());
				armarStringInformacion(t.getNombre());
			}
			if (parametros.get("fecha").equals("on"))
				armarStringInformacion(p.getFechaEstablecimiento().toString());
			if (parametros.get("aprov").equals("on"))
				armarStringInformacion(p.getAprovechamiento().equals("0") ? "No"
						: "Si");
			if (parametros.get("tipoInventario").equals("on")) {
				TipoInventario ti = cap.ConsultarTipoInventarioParcela(p
						.getInventario());
				armarStringInformacion(ti.getNombre());
			}
			if (parametros.get("descripcion").equals("on"))
				armarStringInformacion(p.getDescripcion());
			if (parametros.get("proposito").equals("on")) {
				Proposito pro = cap.ConsultarPropositoParcela(p.getProposito());
				armarStringInformacion(pro.getNombre());
			}
			if (parametros.get("observaciones").equals("on"))
				armarStringInformacion(p.getObservaciones());
			if (parametros.get("foto").equals("on"))
				armarStringInformacion(p.getNombreImagen());
			String[] geo = cap.ConsultarGeoParcela(p.getConsecutivo());
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
			if (parametros.get("medidas").equals("on")) {
				if (p.getLargoParcela().equals(new BigDecimal(-1)))
					armarStringInformacion(p.getRadioParcela().toString());
				else
					armarStringInformacion(p.getLargoParcela().toString() + "-"
							+ p.getAnchoParcela().toString());
			}
			if (parametros.get("pais").equals("on")) {
				Pais pais = cap.ConsultarPaisParcela(p.getConsecutivo());
				armarStringInformacion(pais.getNombre());
			}
			if (parametros.get("departamento").equals("on")) {
				String departamentos = "";
				ArrayList<Depto> deptos = cap.consultaDeptoParcela(p
						.getConsecutivo());
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
				ArrayList<Municipios> municipio = cap
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
			if (parametros.get("fgda").equals("on")) {
				if (p.getFgda() == 1) {
					Contacto fgda = cap.ConsultarContactoParcela(p.getIdFgda(),
							-1);
					armarStringInformacion(fgda.getNombre());
					armarStringInformacion(fgda.getNombre());
					armarStringInformacion(fgda.getCorreo());
				} else {
					Contacto fgda = cap.ConsultarContactoParcela(p.getIdFgda(),
							-1);
					armarStringInformacion(fgda.getOrganizacion());
					armarStringInformacion(fgda.getNombre());
					armarStringInformacion(fgda.getCorreo());
				}
			}
			if (parametros.get("autor").equals("on")) {
				if (p.getTipoAutor() == 1) {
					Contacto a = cap.ConsultarContactoParcela(p.getIdAutor(),
							-1);
					armarStringInformacion(a.getNombre());
					armarStringInformacion(a.getNombre());
					armarStringInformacion(a.getCorreo());
				} else {
					Contacto a = cap.ConsultarContactoParcela(p.getIdAutor(),
							-1);
					armarStringInformacion(a.getOrganizacion());
					armarStringInformacion(a.getNombre());
					armarStringInformacion(a.getCorreo());
				}
			}
			if (parametros.get("coleccion").equals("on")) {
				if (p.getColecciones().equals("1")) {
					Contacto c = cap.ConsultarContactoParcela(
							p.getIdColeccion(), -1);
					armarStringInformacion(c.getDireccion());
					armarStringInformacion(c.getTelefono());
					Contacto e = cap.ConsultarContactoParcela(
							p.getIdEncargado(), -1);
					armarStringInformacion(e.getNombre());
					armarStringInformacion(e.getCorreo());
				} else {
					armarStringInformacion("No posee coleccion");
					armarStringInformacion("No posee coleccion");
					armarStringInformacion("No posee Encargado de las determinaciones");
					armarStringInformacion("No posee Encargado de las determinaciones");
				}
			}
			if (parametros.get("custodio").equals("on")) {
				if (p.getTipoCustodio() == 1) {
					Contacto cus = cap.ConsultarContactoParcela(
							p.getIdCustodio(), -1);
					armarStringInformacion(cus.getNombre());
					armarStringInformacion(cus.getNombre());
					armarStringInformacion(cus.getCorreo());
				} else {
					Contacto cus = cap.ConsultarContactoParcela(
							p.getIdCustodio(), -1);
					armarStringInformacion(cus.getOrganizacion());
					armarStringInformacion(cus.getNombre());
					armarStringInformacion(cus.getCorreo());
				}
			}
			if (parametros.get("investigador").equals("on")) {
				if (p.getInvestigador().equals("1")) {
					Contacto in = cap.ConsultarContactoParcela(
							p.getIdInvetigador(), -1);
					armarStringInformacion(in.getNombre());
					armarStringInformacion(in.getCorreo());
				} else {
					armarStringInformacion("No posee investigador");
					armarStringInformacion("No posee investigador");
				}
			}
			BiomasaYCarbono bc = cap.ConsultarUltimaBiomasaParcela(p
					.getConsecutivo());

			if (parametros.get("biomasa").equals("on")) {
				if (bc != null) {
					MetodologiaBiomasa m = cb.consultarMetodologiaBiomasa(
							bc.getMetodologia(), "");
					armarStringInformacion(bc.getBiomasa().toString());
					armarStringInformacion(bc.getCarbono().toString());
					armarStringInformacion(bc.getFechaInicio().toString());
					armarStringInformacion(m.getNombre());
					armarStringInformacion(m.getDescripcion());
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

	public ArrayList<Parcela> getParcelas() {
		return parcelas;
	}

	public void setParcelas(ArrayList<Parcela> parcelas) {
		this.parcelas = parcelas;
	}

}
