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
import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
//import co.gov.ideamredd.usuario.entities.Usuario;
import co.gov.ideamredd.util.entities.Usuario;
import co.gov.ideamredd.usuario.security.Encript;
import co.gov.ideamredd.util.SMBC_Log;

/**
 * Clase que realiza un Registro de un usuario
 */
@Stateless
public class RegistroUsuarios {

	Logger log;

	String un_Nombre;
	String un_ApellidoUno;
	String un_ApellidoDos;
	Integer un_TipoIdentificacion;
	String una_Identificacion;
	Integer un_TipoPersona;
	Integer un_Pais;
	String una_Direccion;
	String un_TelefonoOficina;
	String un_Celular;
	String un_CorreoElectronico;
	String una_Clave;
	String es_Activo;
	Date una_FechaCreacion;
	Integer idPersona;
	String respuesta;
	Integer un_Departamento;
	Integer un_Municipio;
	String una_Organizacion;
	String un_Cargo;
	String un_login;

	ArrayList<Integer> roles = new ArrayList<Integer>();
	ArrayList<Integer> licencia = new ArrayList<Integer>();
	Usuario u;

	@EJB
	ConsultaUsuario_Usuario consUs;

	/**
	 * Metodo para registrar usuarios.
	 */
	public boolean registrarUsuario() {

		boolean status = false;
		CallableStatement registrarUsuario;

		Integer consecutivo = null;
		Connection conn = null;

		try {
			log = SMBC_Log.Log(RegistroUsuarios.class);

			conn = ConexionBD.establecerConexion();

			String str_consecutivo = ConexionBD.obtenerDato("SELECT RED_USUARIO_SEQ.nextval FROM DUAL", "", conn);
			if (Auxiliar.tieneAlgo(str_consecutivo)) {
				consecutivo = Integer.parseInt(str_consecutivo);
				// ConexionBD.ejecutarSQL("ALTER TRIGGER RED_USUARIO_TRG DISABLE", conn);

				registrarUsuario = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Inserta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
	
				registrarUsuario.setInt("un_Consecutivo", consecutivo);
				registrarUsuario.setString("un_Nombre", un_Nombre);
				registrarUsuario.setString("un_ApellidoUno", null);
				registrarUsuario.setString("un_ApellidoDos", null);
				registrarUsuario.setInt("un_TipoIdentificacion", un_TipoIdentificacion);
				registrarUsuario.setString("una_Identificacion", una_Identificacion);
				registrarUsuario.setInt("un_TipoPersona", un_TipoPersona);
				registrarUsuario.setInt("un_Pais", un_Pais);
				registrarUsuario.setString("una_Direccion", una_Direccion == "" ? " " : una_Direccion);
				registrarUsuario.setString("un_TelefonoOficina", un_TelefonoOficina);
				registrarUsuario.setString("un_Celular", un_Celular);
				registrarUsuario.setString("un_CorreoElectronico", un_CorreoElectronico);
				registrarUsuario.setString("una_Clave", Encript.getEncodedPassword(una_Clave));
				registrarUsuario.setString("es_Activo", es_Activo);
				registrarUsuario.setDate("una_FechaCreacion", una_FechaCreacion);
				registrarUsuario.setInt("un_depto", un_Departamento);
				registrarUsuario.setInt("un_municipio", un_Municipio);
				registrarUsuario.setString("una_organizacion", una_Organizacion);
				registrarUsuario.setString("un_cargo", un_Cargo);
				registrarUsuario.setString("un_login", un_login);
	
				registrarUsuario.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
				registrarUsuario.registerOutParameter("una_Respuesta", OracleTypes.NUMBER);
				registrarUsuario.registerOutParameter("un_Mensaje", OracleTypes.VARCHAR);
				registrarUsuario.execute();
	
				// ConexionBD.ejecutarSQL("ALTER TRIGGER RED_USUARIO_TRG ENABLE", conn);
				
				
	
				Usuario auxUs = new Usuario(
						consecutivo, 
						un_Nombre, 
						un_TipoIdentificacion, 
						una_Identificacion,
						un_TipoPersona, 
						un_Pais, 
						una_Direccion, 
						un_TelefonoOficina, 
						un_Celular, 
						un_CorreoElectronico, 
						es_Activo, 
						un_Departamento, 
						un_Municipio, 
						un_login
						);
	
				String mensaje = registrarUsuario.getString("un_Mensaje");
				System.out.println(mensaje);
				
				System.out.println(registrarUsuario.getObject("un_Resultado"));
	
				if (registrarUsuario.getObject("un_Resultado").equals("TRUE")) {
					respuesta = "USUARIO REGISTRADO EXITOSAMENTE";
	
					insertarRolPersona(auxUs);
					// registrarPersonaApollo(auxUs);
					if (licencia != null) {
						insertarLicenciasPersona(auxUs);
					}
					status = true;
				} else {
					respuesta = "IDENTIFICACION O CORREO ELECTRONICO YA REGISTRADO";
					status = false;
				}
				registrarUsuario.close();
			}
			
			conn.close();
			log.info("[registrarUsuario] Termino");
		} catch (Exception e) {
			log.error("[registrarUsuario] Fallo");
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return status;
	}

	/**
	 * Metodo para registrar un usuario.
	 */
	public boolean registrarUsuario(String[] rol) {
		boolean status = false;
		CallableStatement registrarUsuario;

		Integer consecutivo = null;

		try {
			roles = new ArrayList<Integer>();
			log = SMBC_Log.Log(RegistroUsuarios.class);
			Connection conn = ConexionBD.establecerConexion();

			String str_consecutivo = ConexionBD.obtenerDato("SELECT RED_USUARIO_SEQ.nextval FROM DUAL", "", conn);
			if (Auxiliar.tieneAlgo(str_consecutivo)) {
				consecutivo = Integer.parseInt(str_consecutivo);
				// ConexionBD.ejecutarSQL("ALTER TRIGGER RED_USUARIO_TRG DISABLE", conn);

				registrarUsuario = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Inserta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
				registrarUsuario.setInt("un_Consecutivo", consecutivo);
				registrarUsuario.setString("un_Nombre", un_Nombre);
				registrarUsuario.setString("un_ApellidoUno", un_ApellidoUno);
				registrarUsuario.setString("un_ApellidoDos", un_ApellidoDos == "" ? " " : un_ApellidoDos);
				registrarUsuario.setInt("un_TipoIdentificacion", un_TipoIdentificacion);
				registrarUsuario.setString("una_Identificacion", una_Identificacion);
				registrarUsuario.setInt("un_TipoPersona", un_TipoPersona);
				registrarUsuario.setInt("un_Pais", un_Pais);
				registrarUsuario.setString("una_Direccion", una_Direccion == "" ? " " : una_Direccion);
				registrarUsuario.setString("un_TelefonoOficina", un_TelefonoOficina);
				registrarUsuario.setString("un_Celular", un_Celular);
				registrarUsuario.setString("un_CorreoElectronico", un_CorreoElectronico);
				registrarUsuario.setString("una_Clave", Encript.getEncodedPassword(una_Clave));
				registrarUsuario.setString("es_Activo", es_Activo);
				registrarUsuario.setDate("una_FechaCreacion", una_FechaCreacion);
				registrarUsuario.setInt("un_depto", un_Departamento);
				registrarUsuario.setInt("un_municipio", un_Municipio);
				registrarUsuario.setString("una_organizacion", una_Organizacion);
				registrarUsuario.setString("un_cargo", un_Cargo);
				registrarUsuario.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
				registrarUsuario.registerOutParameter("una_Respuesta", OracleTypes.NUMBER);
				registrarUsuario.execute();
				System.out.println(registrarUsuario.getObject("un_Resultado"));
				registrarUsuario.close();

				if (Integer.valueOf(registrarUsuario.getInt("una_Respuesta")) == 1) {
					respuesta = "USUARIO REGISTRADO EXITOSAMENTE";
					idPersona = null;
					setUsuario(consUs.consultarUsuario(idPersona));
					Integer perfil = insertarPerfilPersona();
					if (perfil > 0) {
						registrarRolesPersonaApollo();
						status = true;
					}
				} else {
					respuesta = "IDENTIFICACION O CORREO ELECTRONICO YA REGISTRADO";
				}
			}

			conn.close();

			log.info("[registrarUsuario] Termino");
		} catch (Exception e) {
			log.error("[registrarUsuario] Fallo");
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * Metodo para registrar un perfil de un usuario.
	 */
	private Integer insertarPerfilPersona() {
		Integer perfil = -1;
		try {
			log = SMBC_Log.Log(RegistroUsuarios.class);
			Connection conn = ConexionBD.establecerConexion();
			CallableStatement perfilPersona = conn.prepareCall("{call RED_PK_USUARIOS.PerfilPersona_Inserta(?,?,?)}");
			perfilPersona.setInt("una_Persona", idPersona);
			perfilPersona.registerOutParameter("un_Perfil", OracleTypes.NUMBER);
			perfilPersona.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			perfilPersona.execute();
			System.out.println(perfilPersona.getObject("un_Resultado"));
			if (perfilPersona.getObject("un_Resultado").toString().contains("Insertado"))
				perfil = perfilPersona.getInt("un_Perfil");
			perfilPersona.close();
			conn.close();
			log.info("[insertarPerfilPersona] Termino");
		} catch (Exception e) {
			log.error("[insertarPerfilPersona] Fallo");
			e.printStackTrace();
		}
		return perfil;
	}

	/**
	 * Metodo para registrar el rol a un usuario.
	 */
	private void insertarRolPersona(Usuario auxUs) {
		try {
			log = SMBC_Log.Log(RegistroUsuarios.class);
			CallableStatement actividadPersona = null;
			Connection conn = ConexionBD.establecerConexion();
			actividadPersona = conn.prepareCall("{call RED_PK_USUARIOS.ROLPERSONA_INSERTA(?,?,?)}");
			actividadPersona.setInt("UN_ROL", 1);
			actividadPersona.setInt("UNA_PERSONA", auxUs.getIdUsuario());
			actividadPersona.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			actividadPersona.execute();
			System.out.println(actividadPersona.getObject("un_Resultado"));
			actividadPersona.close();
			conn.close();
			log.info("[insertarRolPersona] Termino");
		} catch (Exception e) {
			log.error("[insertarRolPersona] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para registrar licencias a un usuario.
	 */
	private void insertarLicenciasPersona(Usuario auxUs) {
		try {
			log = SMBC_Log.Log(RegistroUsuarios.class);
			CallableStatement registrarLicencias;
			registrarLicencias = null;
			Connection conn = ConexionBD.establecerConexion();
			for (int i = 0; i < licencia.size(); i++) {
				registrarLicencias = conn.prepareCall("{call RED_PK_USUARIOS.LicenciaPersona_Inserta(?,?,?)}");
				registrarLicencias.setInt("una_Licencia", licencia.get(i));
				registrarLicencias.setInt("una_Persona", auxUs.getIdUsuario());
				registrarLicencias.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
				registrarLicencias.execute();
				registrarLicencias.close();
			}
			conn.close();
			log.info("[insertarLicenciasPersona] Termino");
		} catch (Exception e) {
			log.error("[insertarLicenciasPersona] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para registrar un usuario a apollo.
	 */
	private void registrarPersonaApollo(Usuario u) {

		// String query = "insert into principals"
		// +
		// "(actor_ogi,actor_grupo,actor_ciudad,actor_municipio,actor_direccion,actor_nombre,"
		// +
		// "actor_cargo,actor_telefono,actor_email,actor_resp_nombre,actor_resp_cargo,actor_imagen,actor_area,actor_validado,subgrupo_id,usuario_id) "
		// + "values("
		// + "'"+u.getNombre()+"',"
		// + "'"+u.getClave()+"',"
		// + "'"+u.get+"',"
		// + "'"+actor.getMunicipio()+"' "
		// + ")";

		try {
			log = SMBC_Log.Log(RegistroUsuarios.class);
			Connection conn = ConexionBD.establecerConexion();
			CallableStatement usuarioapollo = conn.prepareCall("{call RED_PK_APOLLO.RegistrarUsuarioApollo(?,?,?,?,?,?,?,?,?)}");

			usuarioapollo.setInt("un_id", Integer.valueOf(u.getIdentificacion()));
			usuarioapollo.setString("un_nombre", u.getNombre());
			usuarioapollo.setString("una_clave", una_Clave);
			usuarioapollo.setString("un_telefono", u.getTelefonoOficina().length() > 14 ? u.getTelefonoOficina().substring(0, 14) : u.getTelefonoOficina());
			usuarioapollo.setString("un_correo", u.getCorreoElectronico());
			usuarioapollo.setInt("un_mcipio", u.getMunicipio());
			usuarioapollo.setInt("un_depto", u.getDepto());
			usuarioapollo.setInt("un_pais", u.getPais());
			usuarioapollo.registerOutParameter("un_resultado", OracleTypes.VARCHAR);
			usuarioapollo.execute();
			System.out.println(usuarioapollo.getObject("un_Resultado"));
			usuarioapollo.close();

			conn.close();
			log.info("[registrarPersonaApollo] Termino");
		} catch (Exception e) {
			log.error("[registrarPersonaApollo] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para registra un usuario a apollo.
	 */
	private void registrarRolesPersonaApollo() {
		try {
			roles = new ArrayList<Integer>();
			log = SMBC_Log.Log(RegistroUsuarios.class);
			Connection conn = ConexionBD.establecerConexion();
			for (int i = 0; i < roles.size(); i++) {
				CallableStatement licenciaPersona = conn.prepareCall("{call RED_PK_APOLLO.AsignarPermisosUsuario(?,?,?)}");
				licenciaPersona.setInt("un_rol", 1);
				licenciaPersona.setInt("un_id", idPersona);
				licenciaPersona.registerOutParameter("un_resultado", OracleTypes.VARCHAR);
				licenciaPersona.execute();
				System.out.println(licenciaPersona.getObject("un_resultado"));
				licenciaPersona.close();
			}
			conn.close();
			log.info("[registrarRolesPersonaApollo] Termino");
		} catch (Exception e) {
			log.error("[registrarRolesPersonaApollo] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para eliminar persona de apollo.
	 */
	private void quitarRolesPersonaApollo() {
		try {
			roles = new ArrayList<Integer>();
			log = SMBC_Log.Log(RegistroUsuarios.class);
			Connection conn = ConexionBD.establecerConexion();
			for (int i = 0; i < roles.size(); i++) {
				CallableStatement licenciaPersona = conn.prepareCall("{call RED_PK_APOLLO.DenegarPermisosUsuario(?,?,?)}");
				licenciaPersona.setInt("un_rol", roles.get(i));
				licenciaPersona.setInt("un_id", idPersona);
				licenciaPersona.registerOutParameter("un_resultado", OracleTypes.VARCHAR);
				licenciaPersona.execute();
				System.out.println(licenciaPersona.getObject("un_Resultado"));
				licenciaPersona.close();
			}
			conn.close();
			log.info("[quitarRolesPersonaApollo] Termino");
		} catch (Exception e) {
			log.error("[quitarRolesPersonaApollo] Fallo");
			e.printStackTrace();
		}
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

	public String getUna_Identificacion() {
		return una_Identificacion;
	}

	public void setUna_Identificacion(String unaIdentificacion) {
		una_Identificacion = unaIdentificacion;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setLicencia(ArrayList<Integer> licencia) {
		this.licencia = licencia;
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

	public Usuario getUsuario() {
		return u;
	}

	public void setUsuario(Usuario usuario) {
		this.u = usuario;
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

	
	public String getUn_login() {
		return un_login;
	}

	
	public void setUn_login(String un_login) {
		this.un_login = un_login;
	}

	
	
}
