// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.usuario.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.usuario.entities.Perfil;
import co.gov.ideamredd.usuario.entities.Usuario;
import co.gov.ideamredd.usuario.security.Encript;
import co.gov.ideamredd.util.SMBC_Log;

/**
 * Bean que se encarga de actualizar un usuario
 */
@Stateless public class ActualizarUsuario {

	@EJB
	ConexionBD					conexion;
	@EJB
	ConsultaUsuario_Usuario		consUs		= new ConsultaUsuario_Usuario();

	private Logger				log;
	private Connection			conn;
	private Integer				idUsuario;
	private String				un_Nombre;
	private String				un_login;
	private String				un_ApellidoUno;
	private String				un_ApellidoDos;
	private Integer				un_TipoIdentificacion;
	private String				una_Identificacion;
	private Integer				un_TipoPersona;
	private Integer				un_Pais;
	private String				una_Direccion;
	private String				un_TelefonoOficina;
	private String				un_Celular;
	private String				un_CorreoElectronico;
	private String				una_Clave;
	private String				es_Activo;
	private Date				una_FechaCreacion;
	private Integer				un_Departamento;
	private Integer				un_Municipio;
	private String				una_Organizacion;
	private String				un_Cargo;
	private ArrayList<Integer>	licencia	= new ArrayList<Integer>();
	private ArrayList<Integer>	actividad	= new ArrayList<Integer>();
	private ArrayList<Integer>	roles		= new ArrayList<Integer>();
	private Perfil				perfil;

	/**
	 * Metodo de modificacion de usuario.
	 */
	public boolean modificarUsuario() {
		boolean resultado = false;
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			// conn = conexion.establecerConexion();
			conn = ConexionBD.establecerConexion();
			CallableStatement actualizarUsuario = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Actualiza(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			actualizarUsuario.setInt("una_Llave", idUsuario);
			actualizarUsuario.setString("un_Nombre", un_Nombre);
			actualizarUsuario.setString("un_login", un_login);
			actualizarUsuario.setString("un_ApellidoUno", null);
			actualizarUsuario.setString("un_ApellidoDos", null);
			actualizarUsuario.setInt("un_TipoIdentificacion", un_TipoIdentificacion);
			actualizarUsuario.setString("una_Identificacion", una_Identificacion);
			actualizarUsuario.setInt("un_TipoPersona", un_TipoPersona);
			actualizarUsuario.setInt("un_Pais", un_Pais);
			actualizarUsuario.setString("una_Direccion", una_Direccion);
			actualizarUsuario.setString("un_TelefonoOficina", un_TelefonoOficina);
			actualizarUsuario.setString("un_Celular", un_Celular);
			actualizarUsuario.setString("un_CorreoElectronico", un_CorreoElectronico);
			if (!una_Clave.equals("")) {
				actualizarUsuario.setString("una_Clave", Encript.getEncodedPassword(una_Clave));
			}
			else {
				actualizarUsuario.setString("una_Clave", null);
			}
			actualizarUsuario.setString("es_Activo", es_Activo);
			actualizarUsuario.setDate("una_FechaCreacion", una_FechaCreacion);
			actualizarUsuario.setInt("un_depto", un_Departamento);
			actualizarUsuario.setInt("un_municipio", un_Municipio);
			actualizarUsuario.setString("una_organizacion", null);
			actualizarUsuario.setString("un_cargo", null);
			actualizarUsuario.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			actualizarUsuario.registerOutParameter("una_Respuesta", OracleTypes.NUMBER);
			actualizarUsuario.execute();
			System.out.println(actualizarUsuario.getObject("un_Resultado"));

			if (Integer.valueOf(actualizarUsuario.getInt("una_Respuesta")) == 1) {
				if (!licencia.isEmpty()) {
					insertarLicenciasPersona(idUsuario);
				}
				// Usuario auxUs = consUs.consultarUsuarioPorDoc(Integer.valueOf(una_Identificacion));
				// actualizarPersonaApollo(auxUs);
				resultado = true;
			}
			log.info("[modificarUsuario] Termino");
			actualizarUsuario.close();
			conn.close();
		}
		catch (Exception e) {

			log.info("[modificarUsuario] Fallo");
			e.printStackTrace();
		}
		return resultado;

	}

	/**
	 * Metodo de actualizacion de usuario apollo. No usar, la actualización de usuarios se está llevando exclusivamente a través del componente AdmIF
	 */
	// private void actualizarPersonaApollo(Usuario u) {
	// try {
	// log = SMBC_Log.Log(RegistroUsuarios.class);
	// // conn = conexion.establecerConexion();
	// conn = ConexionBD.establecerConexion();
	// CallableStatement usuarioapollo = conn.prepareCall("{call RED_PK_APOLLO.ActualizarUsuarioApollo(?,?,?,?,?,?,?,?,?)}");
	// usuarioapollo.setInt("un_id", Integer.valueOf(u.getIdentificacion()));
	// usuarioapollo.setString("un_nombre", u.getNombre());
	// usuarioapollo.setString("una_clave", una_Clave);
	// usuarioapollo.setString("un_telefono", u.getTelefonoOficina().length() > 14 ? u.getTelefonoOficina().substring(0, 14) : u.getTelefonoOficina());
	// usuarioapollo.setString("un_correo", u.getCorreoElectronico());
	// usuarioapollo.setInt("un_mcipio", u.getMunicipio());
	// usuarioapollo.setInt("un_depto", u.getDepto());
	// usuarioapollo.setInt("un_pais", u.getPais());
	// usuarioapollo.registerOutParameter("un_resultado", OracleTypes.VARCHAR);
	// usuarioapollo.execute();
	// System.out.println(usuarioapollo.getObject("un_Resultado"));
	// usuarioapollo.close();
	// conn.close();
	// log.info("[actualizarPersonaApollo] Termino");
	// }
	// catch (Exception e) {
	// log.error("[actualizarPersonaApollo] Fallo");
	// e.printStackTrace();
	// }
	// }

	/**
	 * Metodo de modificacion de usuario.
	 */
	public boolean modificarUsuario(String[] rol) {
		boolean ok = true;
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			// conn = conexion.establecerConexion();
			conn = ConexionBD.establecerConexion();
			CallableStatement actualizarUsuario = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Actualiza(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			actualizarUsuario.setInt("una_Llave", idUsuario);
			actualizarUsuario.setString("un_Nombre", un_Nombre);
			actualizarUsuario.setString("un_ApellidoUno", un_ApellidoUno);
			actualizarUsuario.setString("un_ApellidoDos", un_ApellidoDos);
			actualizarUsuario.setInt("un_TipoIdentificacion", un_TipoIdentificacion);
			actualizarUsuario.setString("una_Identificacion", una_Identificacion);
			actualizarUsuario.setInt("un_TipoPersona", un_TipoPersona);
			actualizarUsuario.setInt("un_Pais", un_Pais);
			actualizarUsuario.setString("una_Direccion", una_Direccion);
			actualizarUsuario.setString("un_TelefonoOficina", un_TelefonoOficina);
			actualizarUsuario.setString("un_Celular", un_Celular);
			actualizarUsuario.setString("un_CorreoElectronico", un_CorreoElectronico);
			actualizarUsuario.setString("una_Clave", Encript.getEncodedPassword(una_Clave));
			actualizarUsuario.setString("es_Activo", es_Activo);
			actualizarUsuario.setDate("una_FechaCreacion", una_FechaCreacion);
			actualizarUsuario.setInt("un_depto", un_Departamento);
			actualizarUsuario.setInt("un_municipio", un_Municipio);
			actualizarUsuario.setString("una_organizacion", una_Organizacion);
			actualizarUsuario.setString("un_cargo", un_Cargo);
			actualizarUsuario.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			actualizarUsuario.execute();
			if (actividad.size() == 1) {
				if (actividad.get(0) == 1)
					roles.add(0, 1);
				else if (actividad.get(0) == 2)
					roles.add(0, 13);
				else
					roles.add(0, 14);
			}
			else if (actividad.size() == 2) {
				if (actividad.get(0) == 1 && actividad.get(1) == 2) {
					roles.add(0, 1);
					roles.add(1, 13);
				}
				else if (actividad.get(0) == 1 && actividad.get(1) == 3) {
					roles.add(0, 1);
					roles.add(1, 14);
				}
				else {
					roles.add(0, 13);
					roles.add(1, 14);
				}
			}
			else if (actividad.size() == 3) {
				roles.add(0, 1);
				roles.add(1, 13);
				roles.add(2, 14);
			}
			perfil = null;
			if (perfil != null && !roles.isEmpty()) insertarActividadPersona();
			if (rol != null) insertarRolPersona(rol);

			log.info("[modificarUsuario] Termino");
			actualizarUsuario.close();
			conn.close();
		}
		catch (Exception e) {
			log.info("[modificarUsuario] Fallo");
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			ok = false;
		}
		return ok;
	}

	/**
	 * Metodo de modificacion de clave de usuario.
	 */
	public void modificarClaveUsuario(String clavePlainText, String document) {
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			// conn = conexion.establecerConexion();
			conn = ConexionBD.establecerConexion();
			CallableStatement actualizarClave = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Actualiza_Clave(?,?,?)}");
			actualizarClave.setInt("una_Llave", idUsuario);
			actualizarClave.setString("una_Clave", una_Clave);
			actualizarClave.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			actualizarClave.execute();
			System.out.println(actualizarClave.getObject("un_Resultado"));
			actualizarClave.close();

			// actualizarClave = conn
			// .prepareCall("{call RED_PK_APOLLO.Usuario_Actualiza_Clave(?,?,?)}");
			// actualizarClave.setInt("una_Llave", document);
			// actualizarClave.setString("una_Clave", clavePlainText);
			// actualizarClave.registerOutParameter("un_Resultado",
			// OracleTypes.VARCHAR);
			// actualizarClave.execute();
			// System.out.println(actualizarClave.getObject("un_Resultado"));
			//
			// log.info("[modificarClaveUsuario] Termino");
			// actualizarClave.close();

			conn.close();
		}
		catch (Exception e) {
			log.info("[modificarClaveUsuario] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo de modificacion de rol de usuario.
	 */
	public void modificarRolUsuario(int un_Rol, int idUs) {
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			// conn = conexion.establecerConexion();
			conn = ConexionBD.establecerConexion();
			CallableStatement actualizarRol = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Actualiza_Rol(?,?)}");
			actualizarRol.setInt("UN_IDUSUARIO", idUs);
			actualizarRol.setInt("un_IDRol", un_Rol);
			actualizarRol.execute();

			log.info("[modificarRolUsuario] Termino");
			actualizarRol.close();
			conn.close();
		}
		catch (Exception e) {
			log.info("[modificarRolUsuario] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para asociar actividades a un usuario.
	 */
	public void insertarActividadPersona() {
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			// conn = conexion.establecerConexion();
			conn = ConexionBD.establecerConexion();
			CallableStatement eliminarActividadUsuario = conn.prepareCall("{call RED_PK_USUARIOS.ActividadPersona_Elimina(?,?)}");
			eliminarActividadUsuario.setInt("una_Persona", perfil.getIdperfilusuario());
			eliminarActividadUsuario.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			eliminarActividadUsuario.execute();
			eliminarActividadUsuario.close();
			for (int i = 0; i < roles.size(); i++) {
				CallableStatement actividadPersona = conn.prepareCall("{call RED_PK_USUARIOS.ActividadPersona_Inserta(?,?,?)}");
				actividadPersona.setInt("un_perfil", perfil.getIdperfilusuario());
				actividadPersona.setInt("un_rol", roles.get(i));
				actividadPersona.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
				actividadPersona.execute();
				System.out.println(actividadPersona.getObject("un_Resultado"));

				actividadPersona.close();
			}
			conn.close();
			log.info("[insertarActividadPersona] Termino");
		}
		catch (Exception e) {
			log.info("[insertarActividadPersona] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para guardar un usuario.
	 */
	public void insertarRolPersona(String[] roles) {
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			// conn = conexion.establecerConexion();
			conn = ConexionBD.establecerConexion();
			for (int i = 0; i < roles.length; i++) {
				CallableStatement actividadPersona = conn.prepareCall("{call RED_PK_USUARIOS.ActividadPersona_Inserta(?,?,?)}");
				actividadPersona.setInt("un_perfil", perfil.getIdperfilusuario());
				actividadPersona.setString("un_rol", roles[i]);
				actividadPersona.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
				actividadPersona.execute();
				System.out.println(actividadPersona.getObject("un_Resultado"));
				actividadPersona.close();
			}
			conn.close();
			log.info("[insertarRolPersona] Termino");
		}
		catch (Exception e) {
			log.info("[insertarRolPersona] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para asociar licencias a una persona.
	 */
	private void insertarLicenciasPersona(Integer idUsuario) {
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			// conn = conexion.establecerConexion();
			conn = ConexionBD.establecerConexion();
			CallableStatement registrarLicencias;
			registrarLicencias = null;
			CallableStatement eliminarLicencias;
			eliminarLicencias = null;

			eliminarLicencias = conn.prepareCall("{call RED_PK_USUARIOS.LicenciaPersona_Elimina(?,?)}");
			eliminarLicencias.setInt("una_Persona", idUsuario);
			eliminarLicencias.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			eliminarLicencias.execute();
			System.out.println(eliminarLicencias.getObject("un_Resultado"));
			eliminarLicencias.close();

			for (int i = 0; i < licencia.size(); i++) {
				registrarLicencias = conn.prepareCall("{call RED_PK_USUARIOS.LicenciaPersona_Inserta(?,?,?)}");
				registrarLicencias.setInt("una_Licencia", licencia.get(i));
				registrarLicencias.setInt("una_Persona", idUsuario);
				registrarLicencias.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
				registrarLicencias.execute();
				System.out.println(registrarLicencias.getObject("un_Resultado"));
				registrarLicencias.close();
			}
			conn.close();
			log.info("[insertarLicenciasPersona] Termino");
		}
		catch (Exception e) {
			log.error("[insertarLicenciasPersona] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para activacion de un usuario.
	 */
	public void activarUsuario(int idUsuario) {
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			CallableStatement activacion;
			activacion = null;
			// conn = conexion.establecerConexion();
			conn = ConexionBD.establecerConexion();
			activacion = conn.prepareCall("{call RED_PK_USUARIOS.UsuarioActivar(?,?)}");
			activacion.setInt("un_Id", idUsuario);
			activacion.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			activacion.execute();
			System.out.println(activacion.getObject("un_Resultado"));
			activacion.close();
			conn.close();
			log.info("[activarUsuario] Termino");
		}
		catch (Exception e) {
			log.error("[activarUsuario] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para desactivar un usuario.
	 */
	public void desactivarUsuario(int idUsuario) {
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			CallableStatement activacion;
			activacion = null;
			// conn = conexion.establecerConexion();
			conn = ConexionBD.establecerConexion();
			activacion = conn.prepareCall("{call RED_PK_USUARIOS.UsuarioDesactivar(?,?)}");
			activacion.setInt("un_Id", idUsuario);
			activacion.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			activacion.execute();
			System.out.println(activacion.getObject("un_Resultado"));
			activacion.close();
			conn.close();
			log.info("[desactivarUsuario] Termino");
		}
		catch (Exception e) {
			log.error("[desactivarUsuario] Fallo");
			e.printStackTrace();
		}
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUn_Nombre() {
		return un_Nombre;
	}

	public void setUn_Nombre(String unNombre) {
		un_Nombre = unNombre;
	}

	public String getUn_ApellidoUno() {
		return un_ApellidoUno;
	}

	public void setUn_ApellidoUno(String unApellidoUno) {
		un_ApellidoUno = unApellidoUno;
	}

	public String getUn_ApellidoDos() {
		return un_ApellidoDos;
	}

	public void setUn_ApellidoDos(String unApellidoDos) {
		un_ApellidoDos = unApellidoDos;
	}

	public Integer getUn_TipoIdentificacion() {
		return un_TipoIdentificacion;
	}

	public void setUn_TipoIdentificacion(Integer unTipoIdentificacion) {
		un_TipoIdentificacion = unTipoIdentificacion;
	}

	public String getUna_Identificacion() {
		return una_Identificacion;
	}

	public void setUna_Identificacion(String unaIdentificacion) {
		una_Identificacion = unaIdentificacion;
	}

	public Integer getUn_TipoPersona() {
		return un_TipoPersona;
	}

	public void setUn_TipoPersona(Integer unTipoPersona) {
		un_TipoPersona = unTipoPersona;
	}

	public Integer getUn_Pais() {
		return un_Pais;
	}

	public void setUn_Pais(Integer unPais) {
		un_Pais = unPais;
	}

	public String getUna_Direccion() {
		return una_Direccion;
	}

	public void setUna_Direccion(String unaDireccion) {
		una_Direccion = unaDireccion;
	}

	public String getUn_TelefonoOficina() {
		return un_TelefonoOficina;
	}

	public void setUn_TelefonoOficina(String unTelefonoOficina) {
		un_TelefonoOficina = unTelefonoOficina;
	}

	public String getUn_Celular() {
		return un_Celular;
	}

	public void setUn_Celular(String unCelular) {
		un_Celular = unCelular;
	}

	public String getUn_CorreoElectronico() {
		return un_CorreoElectronico;
	}

	public void setUn_CorreoElectronico(String unCorreoElectronico) {
		un_CorreoElectronico = unCorreoElectronico;
	}

	public String getUna_Clave() {
		return una_Clave;
	}

	public void setUna_Clave(String unaClave) {
		una_Clave = unaClave;
	}

	public String getEs_Activo() {
		return es_Activo;
	}

	public void setEs_Activo(String esActivo) {
		es_Activo = esActivo;
	}

	public Date getUna_FechaCreacion() {
		return una_FechaCreacion;
	}

	public void setUna_FechaCreacion(Date unaFechaCreacion) {
		una_FechaCreacion = unaFechaCreacion;
	}

	public ArrayList<Integer> getActividad() {
		return actividad;
	}

	public void setActividad(ArrayList<Integer> actividad) {
		this.actividad = actividad;
	}

	public Integer getUn_Departamento() {
		return un_Departamento;
	}

	public void setUn_Departamento(Integer unDepartamento) {
		un_Departamento = unDepartamento;
	}

	public Integer getUn_Municipio() {
		return un_Municipio;
	}

	public void setUn_Municipio(Integer unMunicipio) {
		un_Municipio = unMunicipio;
	}

	public String getUna_Organizacion() {
		return una_Organizacion;
	}

	public void setUna_Organizacion(String unaOrganizacion) {
		una_Organizacion = unaOrganizacion;
	}

	public String getUn_Cargo() {
		return un_Cargo;
	}

	public void setUn_Cargo(String unCargo) {
		un_Cargo = unCargo;
	}

	/**
	 * @return the licencia
	 */
	public ArrayList<Integer> getLicencia() {
		return licencia;
	}

	/**
	 * @param licencia
	 *            the licencia to set
	 */
	public void setLicencia(ArrayList<Integer> licencia) {
		this.licencia = licencia;
	}

	/**
	 * @return the perfil
	 */
	public Perfil getPerfil() {
		return perfil;
	}

	/**
	 * @param perfil
	 *            the perfil to set
	 */
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	
	public String getUn_login() {
		return un_login;
	}

	
	public void setUn_login(String un_login) {
		this.un_login = un_login;
	}

	
	
}
