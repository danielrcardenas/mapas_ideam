package co.gov.ideamredd.dao;

import java.math.BigDecimal;




import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Folder;

import org.apache.log4j.Logger;

import java.io.File;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBDParcelas;
import co.gov.ideamredd.entities.BiomasaYCarbono;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.ContactoParcela;
import co.gov.ideamredd.entities.Organizacion;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.util.Util;

@Stateless
public class RegistrarParcela {

	@EJB
	private ConexionBDParcelas conexionBD;

	@EJB
	private InsertarAsociadosParcela asociadosParcela;

	@EJB
	private MetadatoParcela metadato;

	@EJB
	private ConsultarAsociadosParcela consultarAsociadosParcela;

	private String un_Nombre;
	private String un_Proyecto;
	private String una_Fecha;
	private String un_Aprovechamiento;
	private BigDecimal un_Area = new BigDecimal(-1);
	private String un_InventarioP;
	private Integer un_Estado;
	private Integer una_Temporalidad;
	private Integer un_TipoInventario;
	private BigDecimal un_Largo;
	private BigDecimal un_Ancho;
	private BigDecimal un_Radio;
	private String unas_Observaciones;
	private String una_Ubicacion;
	private String un_NombreArchivo;
	private Integer un_Proposito;
	private Integer idParcela;
	private String geometria;
	private String codigoCampo;
	private Integer forma;
	private Integer idProyecto;
	private Integer idMetadato;
	private String una_Descripcion;
	private String publica;
	private String coor;
	private ContactoParcela contactoParcela;

	private Hashtable<String, Object> contactos = new Hashtable<String, Object>();

	private Connection conn;
	private static Logger log;

	public void registrarParcela() {
		try {
//			log = SMBC_Log.log(this.getClass());
			adicionaParcela();
			if(!this.un_NombreArchivo.equals(""))
				descomprmirImagenes();
			registrarContactos(idParcela);
			agregaGeoParcela(idParcela);
			if (geometria.equals("2"))
				asociadosParcela.insertarAreaParcela(idParcela);
			// adicionarProyectoParcela();
			asociadosParcela.insertaAsociados(idParcela);
			metadato.setModificacion(false);
			metadato.setNombre(un_Nombre);
			metadato.setIdParcela(idParcela);
			metadato.leerPlantilla();
			idMetadato = asociadosParcela.insertaMetadato("Metadato"
					+ un_Nombre);
			asociadosParcela.insertaMetadatoParcela(idParcela, idMetadato);
//			registrarSIO();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void descomprmirImagenes() {
		try{
		File carpeta = new File(this.una_Ubicacion+"/"+this.un_Nombre);
		if(!carpeta.exists())
			carpeta.mkdir();
		Util.UnZip(this.una_Ubicacion+this.un_NombreArchivo, carpeta.getPath()+"/");
		File zip = new File(this.una_Ubicacion+this.un_NombreArchivo);
		Util.fileMove(zip.getAbsolutePath(), carpeta.getPath()+"/"+zip.getName());
		zip.delete();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void registrarSIO() throws Exception {
		String[] coordenadas = Util.obtenerMaxMinCoordenadas(coor);
		contactoParcela = consultarAsociadosParcela
				.consultaContactoPorParcelaClase(idParcela,
						Integer.valueOf(Constantes.opcionFGDA));
		if (contactoParcela.getIdContacto() != 0) {
			Contacto c = consultarAsociadosParcela
					.ConsultarContactoId(contactoParcela.getIdContacto());
			asociadosParcela.ingresarFuenteSiopera(consultarAsociadosParcela
					.ConsultaClaveSector(Constantes.idPrivado), c.getNombre());
		} else {
			Organizacion o = consultarAsociadosParcela
					.ConsultarOrganizacionId(contactoParcela
							.getIdOrganizacion());
			asociadosParcela.ingresarFuenteSiopera(consultarAsociadosParcela
					.ConsultaClaveSector(o.getSector()), o.getNombre());
		}
		asociadosParcela.ingresarPuntoSiopera(un_Nombre, una_Descripcion,
				coordenadas[0], coordenadas[1], idParcela);
		asociadosParcela.ingresarEstacionSiopera(un_Nombre);
	}

	public void registrarContactos(Integer idParcela) {
		Integer idContacto;
		try {
			if (contactos.get("selfgda").equals(Constantes.usuarioSession)
					|| contactos.get("selfgda").equals(Constantes.ideam)
					|| contactos.get("selfgda").equals(
							Constantes.consultarContacto)) {
				if (contactos.get("contactoFGDA") instanceof Contacto) {
					idContacto = ((Contacto) contactos.get("contactoFGDA"))
							.getConsecutivo();
					asociadosParcela.insertaContactoParcela(idParcela,
							idContacto, 0,
							Integer.valueOf((String) contactos.get("fgda")));
				} else {
					idContacto = ((Organizacion) contactos.get("contactoFGDA"))
							.getConsecutivo();
					asociadosParcela.insertaContactoParcela(idParcela, 0,
							idContacto,
							Integer.valueOf((String) contactos.get("fgda")));
				}
			} else {
				if (contactos.get("contactoFGDA") instanceof Contacto) {
					idContacto = asociadosParcela
							.insertaContacto(((Contacto) contactos
									.get("contactoFGDA")));
					asociadosParcela.insertaContactoParcela(idParcela,
							idContacto, 0,
							Integer.valueOf((String) contactos.get("fgda")));
				} else {
					idContacto = asociadosParcela
							.insertaOrganizacion(((Organizacion) contactos
									.get("contactoInv")));
					asociadosParcela.insertaContactoParcela(idParcela, 0,
							idContacto,
							Integer.valueOf((String) contactos.get("fgda")));
				}
			}
			if (contactos.get("selContPro").equals(Constantes.usuarioSession)
					|| contactos.get("selContPro").equals(Constantes.ideam)
					|| contactos.get("selContPro").equals(
							Constantes.consultarContacto)) {
				if (contactos.get("contactoPropietario") instanceof Contacto) {
					idContacto = ((Contacto) contactos
							.get("contactoPropietario")).getConsecutivo();
					asociadosParcela.insertaContactoParcela(idParcela,
							idContacto, 0, Integer.valueOf((String) contactos
									.get("propietario")));
				} else {
					idContacto = ((Organizacion) contactos
							.get("contactoPropietario")).getConsecutivo();
					asociadosParcela.insertaContactoParcela(idParcela, 0,
							idContacto, Integer.valueOf((String) contactos
									.get("propietario")));
				}
			} else {
				if (contactos.get("contactoPropietario") instanceof Contacto) {
					idContacto = asociadosParcela
							.insertaContacto(((Contacto) contactos
									.get("contactoPropietario")));
					asociadosParcela.insertaContactoParcela(idParcela,
							idContacto, 0, Integer.valueOf((String) contactos
									.get("propietario")));
				} else {
					idContacto = asociadosParcela
							.insertaOrganizacion(((Organizacion) contactos
									.get("contactoPropietario")));
					asociadosParcela.insertaContactoParcela(idParcela, 0,
							idContacto, Integer.valueOf((String) contactos
									.get("propietario")));
				}
			}
			if (contactos.get("selCustodio").equals(Constantes.usuarioSession)
					|| contactos.get("selCustodio").equals(Constantes.ideam)
					|| contactos.get("selCustodio").equals(
							Constantes.consultarContacto)) {
				if (contactos.get("contactoCustodio") instanceof Contacto) {
					idContacto = ((Contacto) contactos.get("contactoCustodio"))
							.getConsecutivo();
					asociadosParcela
							.insertaContactoParcela(idParcela, idContacto, 0,
									Integer.valueOf((String) contactos
											.get("custodio")));
				} else {
					idContacto = ((Organizacion) contactos
							.get("contactoCustodio")).getConsecutivo();
					asociadosParcela
							.insertaContactoParcela(idParcela, 0, idContacto,
									Integer.valueOf((String) contactos
											.get("custodio")));
				}
			} else {
				if (contactos.get("contactoCustodio") instanceof Contacto) {
					idContacto = asociadosParcela
							.insertaContacto(((Contacto) contactos
									.get("contactoCustodio")));
					asociadosParcela
							.insertaContactoParcela(idParcela, idContacto, 0,
									Integer.valueOf((String) contactos
											.get("custodio")));
				} else {
					idContacto = asociadosParcela
							.insertaOrganizacion(((Organizacion) contactos
									.get("contactoCustodio")));
					asociadosParcela
							.insertaContactoParcela(idParcela, 0, idContacto,
									Integer.valueOf((String) contactos
											.get("custodio")));
				}
			}
			if (contactos.get("Inv") != null)
				registrarOtrosContactos("Inv");
			if (contactos.get("Col") != null)
				registrarOtrosContactos("Col");
			if (contactos.get("Enc") != null)
				registrarOtrosContactos("Enc");
			if (contactos.get("Bri") != null)
				registrarOtrosContactos("Bri");
			if (contactos.get("Sup") != null)
				registrarOtrosContactos("Sup");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void registrarOtrosContactos(String clave) throws Exception {
		Integer idContacto = 0;
		if (contactos.get("sel" + clave).equals(Constantes.usuarioSession)
				|| contactos.get("sel" + clave).equals(Constantes.ideam)
				|| contactos.get("sel" + clave).equals(
						Constantes.consultarContacto)) {
			if (contactos.get("contacto" + clave) instanceof Contacto) {
				idContacto = ((Contacto) contactos.get("contacto" + clave))
						.getConsecutivo();
				asociadosParcela.insertaContactoParcela(idParcela, idContacto,
						0, Integer.valueOf((String) contactos.get(clave)));
			} else {
				idContacto = ((Organizacion) contactos.get("contacto" + clave))
						.getConsecutivo();
				asociadosParcela.insertaContactoParcela(idParcela, 0,
						idContacto,
						Integer.valueOf((String) contactos.get(clave)));
			}
		} else {
			if (contactos.get("contacto" + clave) instanceof Contacto) {
				idContacto = asociadosParcela
						.insertaContacto(((Contacto) contactos.get("contacto"
								+ clave)));
				asociadosParcela.insertaContactoParcela(idParcela, idContacto,
						0, Integer.valueOf((String) contactos.get(clave)));
			} else {
				idContacto = asociadosParcela
						.insertaOrganizacion(((Organizacion) contactos
								.get("contacto" + clave)));
				asociadosParcela.insertaContactoParcela(idParcela, 0,
						idContacto,
						Integer.valueOf((String) contactos.get(clave)));
			}
		}
	}

	// TODO: Arreglar cuando este lista la consulta de proyectos
	// private void adicionarProyectoParcela() {
	// try {
	// if (un_Proyecto.equals("1"))
	// asociadosParcela.insertaProyectoParcela(idParcela, idProyecto);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public void adicionaParcela() {
		try {
//			log.info("Inicia registro de la parcela en la base de datos");
			conn = conexionBD.establecerConexion();
			if (un_Radio != new BigDecimal(-1))
				un_Area = un_Radio;
			CallableStatement insertarParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.Parcela_Inserta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			insertarParcela.setString("un_nombre", un_Nombre);
			insertarParcela.setString("un_proyecto", "0");
			insertarParcela.setString("una_fecha", una_Fecha);
			insertarParcela.setString("un_aprovechamiento", un_Aprovechamiento);
			insertarParcela.setBigDecimal("un_area", un_Area);
			insertarParcela.setString("un_inventario", /*un_InventarioP*/"0");
			insertarParcela.setInt("una_temporalidad", una_Temporalidad);
			insertarParcela.setBigDecimal("un_largo", un_Largo);
			insertarParcela.setBigDecimal("un_ancho", un_Ancho);
			insertarParcela.setBigDecimal("un_radio", un_Radio);
			insertarParcela.setString("unas_observaciones", unas_Observaciones);
			insertarParcela.setString("una_ubicacion", una_Ubicacion);
			insertarParcela.setString("un_nombrearchivo", un_NombreArchivo);
			insertarParcela.setString("un_codigocampo", codigoCampo);
			insertarParcela.setString("una_descripcion", una_Descripcion);
			insertarParcela.setString("is_publica", /*publica*/"0");
			insertarParcela.setInt("un_idinventario", un_TipoInventario);
			insertarParcela.setInt("un_idEstado", 1);// TODO: Cambiar el estado
														// de
														// la parcela
														// dependiendo
														// del usuario que la
														// registra.
			insertarParcela.setInt("un_metadato", 0);
			insertarParcela.setInt("un_proposito", un_Proposito);
			insertarParcela.setInt("una_forma", forma);
			insertarParcela.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			insertarParcela.registerOutParameter("un_idParcela",
					OracleTypes.INTEGER);
			insertarParcela.execute();
			System.out.println(insertarParcela.getObject("un_Resultado"));
			idParcela = insertarParcela.getInt("un_idParcela");
			insertarParcela.close();
			conn.close();
//			log.info("Registro existoso de la parcela");
		} catch (Exception e) {
//			log.error("Error en el registro de la parcela");
			e.printStackTrace();
		}
	}

	private void agregaGeoParcela(Integer idParcela) {
		try {
//			log.info("Inicia el registro de la geometria de la parcela id:"
//					+ idParcela);
			conn = conexionBD.establecerConexion();
			CallableStatement adicionarGeometriaParcela = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Adiciona(?,?,?)}");

			adicionarGeometriaParcela.setInt("una_Llave", idParcela);
			adicionarGeometriaParcela.setString("una_Geometria", geometria);
			adicionarGeometriaParcela.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			adicionarGeometriaParcela.execute();
			System.out.println(adicionarGeometriaParcela
					.getObject("un_Resultado"));
			adicionarGeometriaParcela.close();
			conn.close();
//			log.info("Registro exitoso de la geometria de la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el registro de la geometria de la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}

	public String getUn_Nombre() {
		return un_Nombre;
	}

	public void setUn_Nombre(String unNombre) {
		un_Nombre = unNombre;
	}

	public String getUn_Proyecto() {
		return un_Proyecto;
	}

	public void setUn_Proyecto(String unProyecto) {
		un_Proyecto = unProyecto;
	}

	public String getUna_Fecha() {
		return una_Fecha;
	}

	public void setUna_Fecha(String unaFecha) {
		una_Fecha = unaFecha;
	}

	public String getUn_Aprovechamiento() {
		return un_Aprovechamiento;
	}

	public void setUn_Aprovechamiento(String unAprovechamiento) {
		un_Aprovechamiento = unAprovechamiento;
	}

	public Integer getUna_Temporalidad() {
		return una_Temporalidad;
	}

	public void setUna_Temporalidad(Integer unaTemporalidad) {
		una_Temporalidad = unaTemporalidad;
	}

	public String getUnas_Observaciones() {
		return unas_Observaciones;
	}

	public void setUnas_Observaciones(String unasObservaciones) {
		unas_Observaciones = unasObservaciones;
	}

	public String getUna_Ubicacion() {
		return una_Ubicacion;
	}

	public void setUna_Ubicacion(String unaUbicacion) {
		una_Ubicacion = unaUbicacion;
	}

	public String getUn_NombreArchivo() {
		return un_NombreArchivo;
	}

	public void setUn_NombreArchivo(String unNombreArchivo) {
		un_NombreArchivo = unNombreArchivo;
	}

	public Integer getIdParcela() {
		return idParcela;
	}

	public void setIdParcela(Integer idParcela) {
		this.idParcela = idParcela;
	}

	public String getGeometria() {
		return geometria;
	}

	public void setGeometria(String geometria) {
		this.geometria = geometria;
	}

	public Integer getUn_TipoInventario() {
		return un_TipoInventario;
	}

	public void setUn_TipoInventario(Integer unTipoInventario) {
		un_TipoInventario = unTipoInventario;
	}

	public Integer getUn_Proposito() {
		return un_Proposito;
	}

	public void setUn_Proposito(Integer unProposito) {
		un_Proposito = unProposito;
	}

	public String getCodigoCampo() {
		return codigoCampo;
	}

	public void setCodigoCampo(String codigoCampo) {
		this.codigoCampo = codigoCampo;
	}

	public Integer getForma() {
		return forma;
	}

	public void setForma(Integer forma) {
		this.forma = forma;
	}

	public Integer getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(Integer idProyecto) {
		this.idProyecto = idProyecto;
	}

	public String getUna_Descripcion() {
		return una_Descripcion;
	}

	public void setUna_Descripcion(String unaDescripcion) {
		una_Descripcion = unaDescripcion;
	}

	public Integer getUn_Estado() {
		return un_Estado;
	}

	public void setUn_Estado(Integer unEstado) {
		un_Estado = unEstado;
	}

	public BigDecimal getUn_Largo() {
		return un_Largo;
	}

	public void setUn_Largo(BigDecimal unLargo) {
		un_Largo = unLargo;
	}

	public BigDecimal getUn_Ancho() {
		return un_Ancho;
	}

	public void setUn_Ancho(BigDecimal unAncho) {
		un_Ancho = unAncho;
	}

	public BigDecimal getUn_Radio() {
		return un_Radio;
	}

	public void setUn_Radio(BigDecimal unRadio) {
		un_Radio = unRadio;
	}

	public String getPublica() {
		return publica;
	}

	public void setPublica(String publica) {
		this.publica = publica;
	}

	public String getUn_InventarioP() {
		return un_InventarioP;
	}

	public void setUn_InventarioP(String un_InventarioP) {
		this.un_InventarioP = un_InventarioP;
	}

	public Hashtable<String, Object> getContactos() {
		return contactos;
	}

	public void setContactos(Hashtable<String, Object> contactos) {
		this.contactos = contactos;
	}

	public String getCoor() {
		return coor;
	}

	public void setCoor(String coor) {
		this.coor = coor;
	}

}
