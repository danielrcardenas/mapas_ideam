package co.gov.ideamredd.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBDParcelas;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.ContactoParcela;
import co.gov.ideamredd.entities.Organizacion;
import co.gov.ideamredd.entities.SioEstaciones;
import co.gov.ideamredd.entities.SioFuenteGeneradora;
import co.gov.ideamredd.entities.SioPuntosMonitoreo;
import co.gov.ideamredd.entities.TipoBosque;

@Stateless
public class ModificarParcela {

	@EJB
	public ConexionBDParcelas conexionBD;

	@EJB
	private EliminarAsociadosParcela eliminarAsociadosParcela;
	
	@EJB
	private InsertarAsociadosParcela asociadosParcela;

	@EJB
	private MetadatoParcela metadato;

	@EJB
	private ConsultarAsociadosParcela consultarAsociadosParcela;

	private Integer idParcela;
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
	private String geometria;
	private String codigoCampo;
	private Integer forma;
	private Integer idProyecto;
	private Integer idMetadato;
	private String una_Descripcion;
	private String publica;
	private String coor;
	private ContactoParcela contactoParcela;
	private ArrayList<Integer> un_Departamento = new ArrayList<Integer>();
	private ArrayList<Integer> un_Municipio = new ArrayList<Integer>();
	private ArrayList<TipoBosque> un_TipoBosque = new ArrayList<TipoBosque>();
	private ArrayList<String> una_Fisiografia = new ArrayList<String>();
	
	private Hashtable<String, Object> contactos = new Hashtable<String, Object>();

	private SioFuenteGeneradora fuenteGeneradora;
	private SioPuntosMonitoreo puntosMonitoreo;
	private SioEstaciones estaciones;

	private Connection conn;

	public void actualizaParcela() {
		try {
			modificaDatosParcela();
			actualizarContactos(idParcela);
			modificaGeoParcela();
//			if (geometria.equals("3"))
//				InsertarAsociadosParcela.insertarAreaParcela(idParcela);
//			modificaDatosParcela();
//
//			
//			if (un_Proyecto.equals(0) || un_Proyecto.equals("-1"))
//				actualizarProyectoParcela();
//			EliminarAsociadosParcela.eliminarAsociados(idParcela);
//			InsertarAsociadosParcela.insertaAsociados(idParcela);
			// Contacto c = ConsultarAsociadosParcela.ConsultarContactoParcela(
			// un_IdGenerador, -1);
			// InsertarAsociadosParcela.ingresarFuenteSiopera(sector, c
			// .getNombre());
			// InsertarAsociadosParcela.ingresarPuntoSiopera(un_Nombre,
			// una_Descripcion, X, Y, idParcela);
			// InsertarAsociadosParcela.ingresarEstacionSiopera(un_Nombre);
			// modificarSioFgda();
			// modificarSioPunto();
			// modificarSioEstacion();
//			 MetadatoParcela metadato = new MetadatoParcela();
			metadato.setModificacion(true);
			 metadato.setNombre(un_Nombre);
			 metadato.setIdParcela(idParcela);
			 metadato.leerPlantilla();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actualizarContactos(Integer idParcela) {
		Integer idContacto;
		try {
			eliminarAsociadosParcela.eliminarContactoParcelaId(idParcela);
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

	private void actualizarProyectoParcela() throws SQLException {
		conn = conexionBD.establecerConexion();
		CallableStatement actualizarProyectoParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.ProyectoParcela_Actualiza(?,?,?)}");
		actualizarProyectoParcela.setInt("una_parcela", idParcela);
		actualizarProyectoParcela.setInt("un_proyecto", idProyecto);
		actualizarProyectoParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		actualizarProyectoParcela.execute();
		actualizarProyectoParcela.close();
		conn.close();
	}

	private void modificaDatosParcela() throws SQLException {
		conn = conexionBD.establecerConexion();
		CallableStatement actualizarParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.Actualizar_Parcela(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		actualizarParcela.setString("un_nombre", un_Nombre);
		actualizarParcela.setString("un_proyecto", "0");
		String[] f = una_Fecha.split("/");
		una_Fecha = f[1]+"/"+f[0]+"/"+f[2];
		actualizarParcela.setString("una_fecha", una_Fecha);
		actualizarParcela.setString("un_aprovechamiento", un_Aprovechamiento);
		actualizarParcela.setBigDecimal("un_area", un_Area);
		actualizarParcela.setString("un_inventario", un_InventarioP);
		actualizarParcela.setInt("una_temporalidad", una_Temporalidad);
		actualizarParcela.setBigDecimal("un_largo", un_Largo);
		actualizarParcela.setBigDecimal("un_ancho", un_Ancho);
		actualizarParcela.setBigDecimal("un_radio", un_Radio);
		actualizarParcela.setString("unas_observaciones", unas_Observaciones);
		actualizarParcela.setString("una_ubicacion", una_Ubicacion);
		actualizarParcela.setString("un_nombrearchivo", un_NombreArchivo);
		actualizarParcela.setString("un_codigocampo", codigoCampo);
		actualizarParcela.setString("una_descripcion", una_Descripcion);
		actualizarParcela.setString("is_publica", publica);
		actualizarParcela.setInt("un_idinventario", un_TipoInventario);
		actualizarParcela.setInt("un_idEstado", 1);// TODO: Cambiar el estado
													// de
													// la parcela
													// dependiendo
													// del usuario que la
													// registra.
		actualizarParcela.setInt("un_metadato", 0);
		actualizarParcela.setInt("un_proposito", un_Proposito);
		actualizarParcela.setInt("una_forma", forma);
		actualizarParcela.setInt("un_idParcela", idParcela);
		actualizarParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		actualizarParcela.execute();
		System.out.println(actualizarParcela.getObject("un_Resultado"));
		actualizarParcela.close();
		conn.close();
	}

	private void modificaGeoParcela() throws SQLException {
		conn = conexionBD.establecerConexion();
		CallableStatement actualizarGeometriaParcela = conn
				.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Actualiza(?,?,?)}");
		actualizarGeometriaParcela.setInt("una_Llave", idParcela);
		actualizarGeometriaParcela.setString("una_Geometria", geometria);
		actualizarGeometriaParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		actualizarGeometriaParcela.execute();
		System.out
				.println(actualizarGeometriaParcela.getObject("un_Resultado"));
		actualizarGeometriaParcela.close();
		conn.close();
	}

	private void modificarSioFgda() throws SQLException {
//		conn = dataSource.getConnection();
//		Contacto c = ConsultarAsociadosParcela.ConsultarContactoParcela(
//				un_IdGenerador, -1);
//		fuenteGeneradora = ConsultarAsociadosParcela.ConsultarSIOFGDA(c
//				.getNombre());
//		CallableStatement actualizarSioFgda = conn
//				.prepareCall("{call RED_PK_GEOMETRIA.ActualizarSIO_FGDA(?,?,?,?,?,?)}");
//		actualizarSioFgda.setDate("una_fecha_modificacion",
//				new Date(System.currentTimeMillis()));
//		actualizarSioFgda.setDate("una_fecha_aplicacion",
//				new Date(System.currentTimeMillis()));
//		actualizarSioFgda.setString("un_sector", sector);
//		actualizarSioFgda.setString("un_nombre", un_Nombre);
//		actualizarSioFgda.setInt("un_fgda_id", fuenteGeneradora.getFgdaId());
//		actualizarSioFgda.registerOutParameter("un_Resultado",
//				OracleTypes.VARCHAR);
//		actualizarSioFgda.execute();
//		System.out.println(actualizarSioFgda.getObject("un_Resultado"));
//		actualizarSioFgda.close();
//		conn.close();
	}

	private void modificarSioPunto() throws SQLException {
//		conn = dataSource.getConnection();
//		puntosMonitoreo = ConsultarAsociadosParcela.ConsultarSioPuntos(
//				un_Nombre, fuenteGeneradora.getFgdaId());
//		CallableStatement actualizarSioPunto = conn
//				.prepareCall("{call RED_PK_GEOMETRIA.ActualizaSIO_PUNTOS_MONITOREOS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
//		actualizarSioPunto.setDouble("un_punto_monitoreo_id",
//				puntosMonitoreo.getPuntoMonitoreoId());
//		actualizarSioPunto.setString("un_nombre", un_Nombre);
//		actualizarSioPunto.setDate("una_fecha_modificacion",
//				new Date(System.currentTimeMillis()));
//		actualizarSioPunto.setString("una_descripcion", una_Descripcion);
//		actualizarSioPunto.setDate("una_fecha_aplicacion",
//				new Date(System.currentTimeMillis()));
//		actualizarSioPunto.setDouble("una_latitud", Double.valueOf(X));
//		actualizarSioPunto.setDouble("una_longitud", Double.valueOf(Y));
//		String[] latitud = Util.convertirAGrados(X.toString()).split(",");
//		String[] longitud = Util.convertirAGrados(Y.toString()).split(",");
//		actualizarSioPunto.setInt("unos_grados_latitud",
//				Integer.valueOf(latitud[0]));
//		actualizarSioPunto.setInt("unos_minutos_latitud",
//				Integer.valueOf(latitud[1]));
//		actualizarSioPunto.setInt("unos_segundos_latitud",
//				Integer.valueOf(latitud[2]));
//		actualizarSioPunto.setInt("unos_grados_longitud",
//				Integer.valueOf(longitud[0]));
//		actualizarSioPunto.setInt("unos_minutos_longitud",
//				Integer.valueOf(longitud[1]));
//		actualizarSioPunto.setInt("unos_segundos_longitud",
//				Integer.valueOf(longitud[2]));
//		ArrayList<TipoBosque> t = InsertarAsociadosParcela
//				.consultaBosqueParcela(idParcela);
//		TipoBosque tb = t.get(0);
//		String[] tipo = tb.getAltitud().split("-");
//		actualizarSioPunto.setDouble("una_altitud", Double.valueOf(tipo[0]));
//		actualizarSioPunto.registerOutParameter("un_Resultado",
//				OracleTypes.VARCHAR);
//		actualizarSioPunto.execute();
//		System.out.println(actualizarSioPunto.getObject("un_Resultado"));
//		actualizarSioPunto.close();
//		conn.close();
	}

	private void modificarSioEstacion() throws SQLException {
//		conn = dataSource.getConnection();
//		estaciones = ConsultarAsociadosParcela.ConsultarSioEstaciones(
//				un_Nombre, puntosMonitoreo.getPuntoMonitoreoId());
//		CallableStatement actualizarSioEstacion = conn
//				.prepareCall("{call RED_PK_GEOMETRIA.ActualizaSIO_ESTACIONES(?,?,?,?,?)}");
//		actualizarSioEstacion.setDate("una_fecha_modificacion",
//				new Date(System.currentTimeMillis()));
//		actualizarSioEstacion.setString("un_nombre", un_Nombre);
//		actualizarSioEstacion.setDate("una_fecha_aplicacion",
//				new Date(System.currentTimeMillis()));
//		actualizarSioEstacion.setInt("un_id", estaciones.getIdEstacion());
//		actualizarSioEstacion.registerOutParameter("un_Resultado",
//				OracleTypes.VARCHAR);
//		actualizarSioEstacion.execute();
//		System.out.println(actualizarSioEstacion.getObject("un_Resultado"));
//		actualizarSioEstacion.close();
//		conn.close();
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

	public String getUn_Aprovechamiento() {
		return un_Aprovechamiento;
	}

	public void setUn_Aprovechamiento(String unAprovechamiento) {
		un_Aprovechamiento = unAprovechamiento;
	}

	public BigDecimal getUn_Area() {
		return un_Area;
	}

	public void setUn_Area(BigDecimal unArea) {
		un_Area = unArea;
	}

	public String getUn_InventarioP() {
		return un_InventarioP;
	}

	public void setUn_InventarioP(String unInventarioP) {
		un_InventarioP = unInventarioP;
	}

	public Integer getUna_Temporalidad() {
		return una_Temporalidad;
	}

	public void setUna_Temporalidad(Integer unaTemporalidad) {
		una_Temporalidad = unaTemporalidad;
	}

	public Integer getUn_TipoInventario() {
		return un_TipoInventario;
	}

	public void setUn_TipoInventario(Integer unTipoInventario) {
		un_TipoInventario = unTipoInventario;
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

	public void setUn_Ancho(BigDecimal bigDecimal) {
		un_Ancho = bigDecimal;
	}

	public BigDecimal getUn_Radio() {
		return un_Radio;
	}

	public void setUn_Radio(BigDecimal unRadio) {
		un_Radio = unRadio;
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

	public ArrayList<Integer> getUn_Departamento() {
		return un_Departamento;
	}

	public void setUn_Departamento(ArrayList<Integer> unDepartamento) {
		un_Departamento = unDepartamento;
	}

	public ArrayList<Integer> getUn_Municipio() {
		return un_Municipio;
	}

	public void setUn_Municipio(ArrayList<Integer> unMunicipio) {
		un_Municipio = unMunicipio;
	}

	public ArrayList<TipoBosque> getUn_TipoBosque() {
		return un_TipoBosque;
	}

	public void setUn_TipoBosque(ArrayList<TipoBosque> unTipoBosque) {
		un_TipoBosque = unTipoBosque;
	}

	public ArrayList<String> getUna_Fisiografia() {
		return una_Fisiografia;
	}

	public void setUna_Fisiografia(ArrayList<String> unaFisiografia) {
		una_Fisiografia = unaFisiografia;
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

	public String getUna_Descripcion() {
		return una_Descripcion;
	}

	public void setUna_Descripcion(String unaDescripcion) {
		una_Descripcion = unaDescripcion;
	}

	public Integer getIdProyecto() {
		return idProyecto;
	}

	public void setIdProyecto(Integer idProyecto) {
		this.idProyecto = idProyecto;
	}

	public String getUna_Fecha() {
		return una_Fecha;
	}

	public void setUna_Fecha(String una_Fecha) {
		this.una_Fecha = una_Fecha;
	}

	public Integer getUn_Estado() {
		return un_Estado;
	}

	public void setUn_Estado(Integer un_Estado) {
		this.un_Estado = un_Estado;
	}

	public Integer getIdMetadato() {
		return idMetadato;
	}

	public void setIdMetadato(Integer idMetadato) {
		this.idMetadato = idMetadato;
	}

	public String getCoor() {
		return coor;
	}

	public void setCoor(String coor) {
		this.coor = coor;
	}

	public Hashtable<String, Object> getContactos() {
		return contactos;
	}

	public void setContactos(Hashtable<String, Object> contactos) {
		this.contactos = contactos;
	}

	public String getPublica() {
		return publica;
	}

	public void setPublica(String publica) {
		this.publica = publica;
	}

}
