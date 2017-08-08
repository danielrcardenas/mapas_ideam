// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import co.gov.ideamredd.conexion.ConexionBD;
import co.gov.ideamredd.util.entities.Usuario;

import oracle.jdbc.OracleTypes;

/**
 * Métodos de utilidad para las clases WEB del sistema
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class UtilWeb {

	private static DataSource	dataSource	= ConexionBD.getConnection();
	// private static Connection conn;
	private static Logger		log;

	/**
	 * Consulta un usuario por login
	 * 
	 * @param login
	 * @return
	 */
	public static Usuario consultarUsuarioPorLogin(String login) {
		Usuario usuario = new Usuario();
		CallableStatement consultarUsuario = null;
		ResultSet resultSet = null;
		Connection conn = null;

		try {
			log = SMBC_Log.Log(UtilWeb.class);
			conn = dataSource.getConnection();
			consultarUsuario = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta_Login(?,?,?)}");
			consultarUsuario.setString("un_login", login);
			consultarUsuario.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultarUsuario.registerOutParameter("un_Mensaje", OracleTypes.VARCHAR);
			consultarUsuario.execute();
			System.out.println(consultarUsuario.getObject("un_Mensaje"));
			resultSet = (ResultSet) consultarUsuario.getObject("un_Resultado");

			while (resultSet.next()) {
				usuario.setIdUsuario(resultSet.getInt("USR_CONSECUTIVO"));
				usuario.setNombre(resultSet.getString("USR_NOMBRE"));
				usuario.setIdentificacion(resultSet.getObject("USR_ID").toString());
				usuario.setCorreoElectronico(resultSet.getString("USR_CORREOELECTRONIC"));
				usuario.setFechaCreacion(resultSet.getString("USR_FECHACREACION"));
				usuario.setActivo(resultSet.getString("USR_ACTIVO"));
				usuario.setPais(resultSet.getInt("USR_CONS_PAIS"));
				usuario.setTipoPersona(resultSet.getInt("USR_CONS_TIPOPERSONA"));
				usuario.setTipoIdentificacion(resultSet.getInt("USR_CONS_TIPOIDENT"));
				usuario.setDireccion(resultSet.getString("USR_DIRECCION"));
				usuario.setTelefonoOficina(resultSet.getString("USR_TELEFONO"));
				if (resultSet.getString("USR_MOVIL") != null) {
					usuario.setCelular(resultSet.getString("USR_MOVIL"));
				}
				usuario.setClave(resultSet.getString("USR_CLAVE"));
				if (resultSet.getString("USR_PRIMER_APELLIDO") != null) {
					usuario.setApellidoUno(resultSet.getString("USR_PRIMER_APELLIDO"));
				}
				if (resultSet.getString("USR_SEGUNDO_APELLIDO") != null) {
					usuario.setApellidoDos(resultSet.getString("USR_SEGUNDO_APELLIDO"));
				}
				if (resultSet.getString("USR_DEPTO") != null) {
					usuario.setDepto(resultSet.getInt("USR_DEPTO"));
				}
				if (resultSet.getString("USR_MUNICIPIO") != null) {
					usuario.setMunicipio(resultSet.getInt("USR_MUNICIPIO"));
				}
				if (resultSet.getString("USR_ORGANIZACION") != null) {
					usuario.setOrganizacion(resultSet.getString("USR_ORGANIZACION"));
				}
				if (resultSet.getString("USR_CARGO") != null) {
					usuario.setCargo(resultSet.getString("USR_CARGO"));
				}
				usuario.setLogin(resultSet.getString("USR_LOGIN"));
			}

			resultSet.close();
			consultarUsuario.close();
			conn.close();

			usuario.setRolNombre(consultarRolesUsuarioPorLogin(login));
			usuario.setRolId(consultarRolIdUsuarioPorLogin(login));

			log.info("[consultarUsuarioPorDoc] Termino");
		}
		catch (Exception e) {
			log.error("[consultarUsuarioPorDoc] Fallo");
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (consultarUsuario != null) {
				try {
					consultarUsuario.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return usuario;
	}

	/**
	 * Consulta los roles de un usuario por login
	 * 
	 * @param login
	 * @return arraylist de roles
	 */
	public static ArrayList<String> consultarRolesUsuarioPorLogin(String login) {
		ArrayList<String> roles = new ArrayList<String>();
		CallableStatement stmt = null;
		ResultSet resultSet = null;
		Connection conn = null;

		try {
			log = SMBC_Log.Log(UtilWeb.class);
			log.info("[consultarRolesUsuarioPorLogin] inicio de consulta");
			conn = dataSource.getConnection();
			stmt = conn.prepareCall("{call RED_PK_USUARIOS.nombreRolesUsuario(?,?)}");
			stmt.setString("un_login", login);
			stmt.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			stmt.execute();
			resultSet = (ResultSet) stmt.getObject("un_Resultado");
			while (resultSet.next()) {
				roles.add(resultSet.getString(1));
			}
			resultSet.close();
			stmt.close();
			conn.close();
			log.info("[consultarRolesUsuarioPorLogin] Termino");
		}
		catch (Exception e) {
			log.error("[consultarRolesUsuarioPorLogin] Fallo");
			log.error(e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
					stmt = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
					resultSet = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return roles;
	}

	/**
	 * Consulta el id de rol de un usuario por login
	 * 
	 * @param login
	 * @return rol principal
	 */
	public static Integer consultarRolIdUsuarioPorLogin(String login) {
		Integer rolId = 0;
		CallableStatement stmt = null;
		ResultSet resultSet = null;
		Connection conn = null;

		try {
			log = SMBC_Log.Log(UtilWeb.class);
			log.info("[consultarRolesUsuarioPorLogin] inicio de consulta");
			conn = dataSource.getConnection();
			stmt = conn.prepareCall("{call RED_PK_USUARIOS.idRolUsuario(?,?)}");
			stmt.setString("un_login", login);
			stmt.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			stmt.execute();
			resultSet = (ResultSet) stmt.getObject("un_Resultado");
			while (resultSet.next()) {
				rolId = resultSet.getInt(1);
			}
			resultSet.close();
			stmt.close();
			conn.close();
			log.info("[consultarRolIdUsuarioPorLogin] Termino");
		}
		catch (Exception e) {
			log.error("[consultarRolIdUsuarioPorLogin] Fallo");
			log.error(e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
					stmt = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
					resultSet = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return rolId;
	}

	/**
	 * Consulta un usuario por documento
	 * 
	 * @param docPersona
	 * @return
	 */
	public static Usuario consultarUsuarioPorDoc(String docPersona) {
		Usuario usuario = new Usuario();
		CallableStatement consultarUsuario = null;
		ResultSet resultSet = null;
		Connection conn = null;

		try {
			log = SMBC_Log.Log(UtilWeb.class);
			conn = dataSource.getConnection();
			consultarUsuario = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta_Doc(?,?,?)}");
			consultarUsuario.setString("un_Id", docPersona);
			consultarUsuario.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultarUsuario.registerOutParameter("un_Mensaje", OracleTypes.VARCHAR);
			consultarUsuario.execute();
			System.out.println(consultarUsuario.getObject("un_Mensaje"));
			resultSet = (ResultSet) consultarUsuario.getObject("un_Resultado");

			while (resultSet.next()) {
				usuario.setIdUsuario(resultSet.getInt(1));
				usuario.setNombre(resultSet.getString(2));
				usuario.setIdentificacion(resultSet.getObject(3).toString());
				usuario.setCorreoElectronico(resultSet.getString(4));
				usuario.setFechaCreacion(resultSet.getString(5));
				usuario.setActivo(resultSet.getString(6));
				usuario.setPais(resultSet.getInt(7));
				usuario.setTipoPersona(resultSet.getInt(8));
				usuario.setTipoIdentificacion(resultSet.getInt(9));
				usuario.setDireccion(resultSet.getString(10));
				usuario.setTelefonoOficina(resultSet.getString(11));
				if (resultSet.getString(12) != null) {
					usuario.setCelular(resultSet.getString(12));
				}
				usuario.setClave(resultSet.getString(13));
				if (resultSet.getString(14) != null) {
					usuario.setApellidoUno(resultSet.getString(14));
				}
				if (resultSet.getString(15) != null) {
					usuario.setApellidoDos(resultSet.getString(15));
				}
				if (resultSet.getString(16) != null) {
					usuario.setDepto(resultSet.getInt(16));
				}
				if (resultSet.getString(17) != null) {
					usuario.setMunicipio(resultSet.getInt(17));
				}
				if (resultSet.getString(18) != null) {
					usuario.setOrganizacion(resultSet.getString(18));
				}
				if (resultSet.getString(19) != null) {
					usuario.setCargo(resultSet.getString(19));
				}
			}

			resultSet.close();
			consultarUsuario.close();
			conn.close();

			usuario.setRolNombre(consultarRolesUsuarioPorDoc(docPersona));
			usuario.setRolId(consultarRolIdUsuarioPorDoc(docPersona));

			log.info("[consultarUsuarioPorDoc] Termino");
		}
		catch (Exception e) {
			log.error("[consultarUsuarioPorDoc] Fallo");
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (consultarUsuario != null) {
				try {
					consultarUsuario.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return usuario;
	}

	/**
	 * Consulta los roles de un usuario por documento de identificación
	 * 
	 * @param docPersona
	 * @return arraylist de roles
	 */
	public static ArrayList<String> consultarRolesUsuarioPorDoc(String docPersona) {
		ArrayList<String> roles = new ArrayList<String>();
		CallableStatement consultarRolesUsuario = null;
		ResultSet resultSet = null;
		Connection conn = null;

		try {
			log = SMBC_Log.Log(UtilWeb.class);
			log.info("[consultarRolesUsuarioPorDoc] inicio de consulta");
			conn = dataSource.getConnection();
			consultarRolesUsuario = conn.prepareCall("{call RED_PK_USUARIOS.consultarNombreRolesUsuario(?,?)}");
			consultarRolesUsuario.setString("un_idUsuario", docPersona);
			consultarRolesUsuario.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultarRolesUsuario.execute();
			resultSet = (ResultSet) consultarRolesUsuario.getObject("un_Resultado");
			while (resultSet.next()) {
				roles.add(resultSet.getString(1));
			}
			resultSet.close();
			consultarRolesUsuario.close();
			conn.close();
			log.info("[consultarRolesUsuarioPorDoc] Termino");
		}
		catch (Exception e) {
			log.error("[consultarRolesUsuarioPorDoc] Fallo");
			log.error(e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (consultarRolesUsuario != null) {
				try {
					consultarRolesUsuario.close();
					consultarRolesUsuario = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
					resultSet = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return roles;
	}

	/**
	 * Consulta el id de rol de un usuario por documento de identificación
	 * 
	 * @param docPersona
	 * @return rol principal
	 */
	public static Integer consultarRolIdUsuarioPorDoc(String docPersona) {
		Integer rolId = 0;
		CallableStatement consultarRolesUsuario = null;
		ResultSet resultSet = null;
		Connection conn = null;

		try {
			log = SMBC_Log.Log(UtilWeb.class);
			log.info("[consultarRolesUsuarioPorDoc] inicio de consulta");
			conn = dataSource.getConnection();
			consultarRolesUsuario = conn.prepareCall("{call RED_PK_USUARIOS.consultarIdRolUsuario(?,?)}");
			// consultarRolesUsuario.setInt("un_idUsuario", docPersona);
			consultarRolesUsuario.setString("un_idUsuario", docPersona);
			consultarRolesUsuario.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultarRolesUsuario.execute();
			resultSet = (ResultSet) consultarRolesUsuario.getObject("un_Resultado");
			while (resultSet.next()) {
				rolId = resultSet.getInt(1);
			}
			resultSet.close();
			consultarRolesUsuario.close();
			conn.close();
			log.info("[consultarRolIdUsuarioPorDoc] Termino");
		}
		catch (Exception e) {
			log.error("[consultarRolIdUsuarioPorDoc] Fallo");
			log.error(e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (consultarRolesUsuario != null) {
				try {
					consultarRolesUsuario.close();
					consultarRolesUsuario = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
					resultSet = null;
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return rolId;
	}

}
