package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.Usuario;
import co.gov.ideamredd.security.Encript;
import co.gov.ideamredd.util.SMBC_Log;

@Stateless
public class RegistroUsuarios {

	@EJB
	ConexionBD conexion;

	@EJB
	ConsultaUsuario consUs;

	private Logger log;
	private String un_Nombre;
	private String un_ApellidoUno;
	private String un_ApellidoDos;
	private Integer un_TipoIdentificacion;
	private String una_Identificacion;
	private Integer un_TipoPersona;
	private Integer un_Pais;
	private String una_Direccion;
	private String un_TelefonoOficina;
	private String un_Celular;
	private String un_CorreoElectronico;
	private String una_Clave;
	private String es_Activo;
	private Date una_FechaCreacion;
	private Integer idPersona;
	private String respuesta;
	private Integer un_Departamento;
	private Integer un_Municipio;
	private String una_Organizacion;
	private String un_Cargo;

	private ArrayList<Integer> roles = new ArrayList<Integer>();
	private ArrayList<Integer> licencia = new ArrayList<Integer>();
	private Connection conn;
	private Usuario u;

	public boolean registrarUsuario() {
		boolean status = false;
		CallableStatement registrarUsuario;

		try {
			log = SMBC_Log.Log(RegistroUsuarios.class);
			conn = conexion.establecerConexion();
			registrarUsuario = conn
					.prepareCall("{call RED_PK_USUARIOS.Usuario_Inserta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			registrarUsuario.setInt("un_Consecutivo",
					Integer.valueOf(una_Identificacion));
			registrarUsuario.setString("un_Nombre", un_Nombre);
			registrarUsuario.setString("un_ApellidoUno", null);
			registrarUsuario.setString("un_ApellidoDos", null);
			registrarUsuario.setInt("un_TipoIdentificacion",
					un_TipoIdentificacion);
			registrarUsuario
					.setString("una_Identificacion", una_Identificacion);
			registrarUsuario.setInt("un_TipoPersona", un_TipoPersona);
			registrarUsuario.setInt("un_Pais", un_Pais);
			registrarUsuario.setString("una_Direccion",
					una_Direccion == "" ? " " : una_Direccion);
			registrarUsuario
					.setString("un_TelefonoOficina", un_TelefonoOficina);
			registrarUsuario.setString("un_Celular", un_Celular);
			registrarUsuario.setString("un_CorreoElectronico",
					un_CorreoElectronico);
			registrarUsuario.setString("una_Clave",
					Encript.getEncodedPassword(una_Clave));
			registrarUsuario.setString("es_Activo", es_Activo);
			registrarUsuario.setDate("una_FechaCreacion", una_FechaCreacion);
			registrarUsuario.setInt("un_depto", un_Departamento);
			registrarUsuario.setInt("un_municipio", un_Municipio);
			registrarUsuario.setString("una_organizacion", null);
			registrarUsuario.setString("un_cargo", null);

			registrarUsuario.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			registrarUsuario.registerOutParameter("una_Respuesta",
					OracleTypes.NUMBER);
			registrarUsuario.execute();
			Usuario auxUs = new Usuario(Integer.valueOf(una_Identificacion
					.trim()), un_Nombre, un_TipoIdentificacion,
					una_Identificacion, un_TipoPersona, un_Pais, una_Direccion,
					un_TelefonoOficina, un_Celular, un_CorreoElectronico,
					es_Activo, un_Departamento, un_Municipio);

			System.out.println(registrarUsuario.getObject("un_Resultado"));
			
			if (registrarUsuario.getObject("un_Resultado").equals("TRUE")) {
				respuesta = "USUARIO REGISTRADO EXITOSAMENTE";

				insertarRolPersona(auxUs);
				registrarPersonaApollo(auxUs);
				if (licencia != null) {
					insertarLicenciasPersona(auxUs);
				}
				status = true;
			} else {
				respuesta = "IDENTIFICACION O CORREO ELECTRONICO YA REGISTRADO";
				status = false;
			}
			registrarUsuario.close();
			conn.close();
			log.info("[registrarUsuario] Termino");
		} catch (Exception e) {
			log.error("[registrarUsuario] Fallo");
			e.printStackTrace();
		}
		return status;
	}

	public boolean registrarUsuario(String[] rol) {
		boolean status = false;
		CallableStatement registrarUsuario;
		try {
			roles = new ArrayList<Integer>();
			log = SMBC_Log.Log(RegistroUsuarios.class);
			conn = conexion.establecerConexion();
			registrarUsuario = conn
					.prepareCall("{call RED_PK_USUARIOS.Usuario_Inserta(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			registrarUsuario.setInt("un_Consecutivo",
					Integer.valueOf(una_Identificacion));
			registrarUsuario.setString("un_Nombre", un_Nombre);
			registrarUsuario.setString("un_ApellidoUno", un_ApellidoUno);
			registrarUsuario.setString("un_ApellidoDos",
					un_ApellidoDos == "" ? " " : un_ApellidoDos);
			registrarUsuario.setInt("un_TipoIdentificacion",
					un_TipoIdentificacion);
			registrarUsuario
					.setString("una_Identificacion", una_Identificacion);
			registrarUsuario.setInt("un_TipoPersona", un_TipoPersona);
			registrarUsuario.setInt("un_Pais", un_Pais);
			registrarUsuario.setString("una_Direccion",
					una_Direccion == "" ? " " : una_Direccion);
			registrarUsuario
					.setString("un_TelefonoOficina", un_TelefonoOficina);
			registrarUsuario.setString("un_Celular", un_Celular);
			registrarUsuario.setString("un_CorreoElectronico",
					un_CorreoElectronico);
			registrarUsuario.setString("una_Clave",
					Encript.getEncodedPassword(una_Clave));
			registrarUsuario.setString("es_Activo", es_Activo);
			registrarUsuario.setDate("una_FechaCreacion", una_FechaCreacion);
			registrarUsuario.setInt("un_depto", un_Departamento);
			registrarUsuario.setInt("un_municipio", un_Municipio);
			registrarUsuario.setString("una_organizacion", una_Organizacion);
			registrarUsuario.setString("un_cargo", un_Cargo);
			registrarUsuario.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			registrarUsuario.registerOutParameter("una_Respuesta",
					OracleTypes.NUMBER);
			registrarUsuario.execute();
			System.out.println(registrarUsuario.getObject("un_Resultado"));
			registrarUsuario.close();
			conn.close();
			if (Integer.valueOf(registrarUsuario.getInt("una_Respuesta")) == 1) {
				respuesta = "USUARIO REGISTRADO EXITOSAMENTE";
				idPersona = null;
				setUsuario(consUs.consultarUsuario(idPersona));
				Integer perfil = insertarPerfilPersona();
				if (perfil > 0) {
					registrarRolesPersonaApollo();
					status = true;
				}
			} else
				respuesta = "IDENTIFICACION O CORREO ELECTRONICO YA REGISTRADO";

			log.info("[registrarUsuario] Termino");
		} catch (Exception e) {
			log.error("[registrarUsuario] Fallo");
			e.printStackTrace();
		}
		return status;
	}

	private Integer insertarPerfilPersona() {
		Integer perfil = -1;
		try {
			log = SMBC_Log.Log(RegistroUsuarios.class);
			conn = conexion.establecerConexion();
			CallableStatement perfilPersona = conn
					.prepareCall("{call RED_PK_USUARIOS.PerfilPersona_Inserta(?,?,?)}");
			perfilPersona.setInt("una_Persona", idPersona);
			perfilPersona.registerOutParameter("un_Perfil", OracleTypes.NUMBER);
			perfilPersona.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			perfilPersona.execute();
			System.out.println(perfilPersona.getObject("un_Resultado"));
			if (perfilPersona.getObject("un_Resultado").toString()
					.contains("Insertado"))
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

	private void insertarRolPersona(Usuario auxUs) {
		try {
			log = SMBC_Log.Log(RegistroUsuarios.class);
			CallableStatement actividadPersona = null;
			conn = conexion.establecerConexion();
			actividadPersona = conn
					.prepareCall("{call RED_PK_USUARIOS.ROLPERSONA_INSERTA(?,?,?)}");
			actividadPersona.setInt("UN_ROL", 1);
			actividadPersona.setInt("UNA_PERSONA", auxUs.getIdUsuario());
			actividadPersona.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
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

	private void insertarLicenciasPersona(Usuario auxUs) {
		try {
			log = SMBC_Log.Log(RegistroUsuarios.class);
			CallableStatement registrarLicencias;
			registrarLicencias = null;
			conn = conexion.establecerConexion();
			for (int i = 0; i < licencia.size(); i++) {
				registrarLicencias = conn
						.prepareCall("{call RED_PK_USUARIOS.LicenciaPersona_Inserta(?,?,?)}");
				registrarLicencias.setInt("una_Licencia", licencia.get(i));
				registrarLicencias.setInt("una_Persona", auxUs.getIdUsuario());
				registrarLicencias.registerOutParameter("un_Resultado",
						OracleTypes.VARCHAR);
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

	private void registrarPersonaApollo(Usuario u) {
		try {
			log = SMBC_Log.Log(RegistroUsuarios.class);
			conn = conexion.establecerConexion();
			CallableStatement usuarioapollo = conn
					.prepareCall("{call RED_PK_APOLLO.RegistrarUsuarioApollo(?,?,?,?,?,?,?,?,?)}");
			usuarioapollo.setInt("un_id",
					Integer.valueOf(u.getIdentificacion()));
			usuarioapollo.setString("un_nombre", u.getNombre());
			usuarioapollo.setString("una_clave", una_Clave);
			usuarioapollo.setString("un_telefono", u.getTelefonoOficina()
					.length() > 14 ? u.getTelefonoOficina().substring(0, 14)
					: u.getTelefonoOficina());
			usuarioapollo.setString("un_correo", u.getCorreoElectronico());
			usuarioapollo.setInt("un_mcipio", u.getMunicipio());
			usuarioapollo.setInt("un_depto", u.getDepto());
			usuarioapollo.setInt("un_pais", u.getPais());
			usuarioapollo.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
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

	private void registrarRolesPersonaApollo() {
		try {
			roles = new ArrayList<Integer>();
			log = SMBC_Log.Log(RegistroUsuarios.class);
			conn = conexion.establecerConexion();
			for (int i = 0; i < roles.size(); i++) {
				CallableStatement licenciaPersona = conn
						.prepareCall("{call RED_PK_APOLLO.AsignarPermisosUsuario(?,?,?)}");
				licenciaPersona.setInt("un_rol", 1);
				licenciaPersona.setInt("un_id", idPersona);
				licenciaPersona.registerOutParameter("un_resultado",
						OracleTypes.VARCHAR);
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

	private void quitarRolesPersonaApollo() {
		try {
			roles = new ArrayList<Integer>();
			log = SMBC_Log.Log(RegistroUsuarios.class);
			conn = conexion.establecerConexion();
			for (int i = 0; i < roles.size(); i++) {
				CallableStatement licenciaPersona = conn
						.prepareCall("{call RED_PK_APOLLO.DenegarPermisosUsuario(?,?,?)}");
				licenciaPersona.setInt("un_rol", roles.get(i));
				licenciaPersona.setInt("un_id", idPersona);
				licenciaPersona.registerOutParameter("un_resultado",
						OracleTypes.VARCHAR);
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

}
