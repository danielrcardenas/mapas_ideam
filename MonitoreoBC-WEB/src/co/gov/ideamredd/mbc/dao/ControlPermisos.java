// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.mbc.conexion.ConexionBD;

/**
 * Métodos para controlar el acceso de un usuario a las funcionalidades del sistema
 * 
 * @author Julio Sánchez
 *
 */
public class ControlPermisos {

	public static void prueba(String a) {
		System.out.println(a);
	}

	/**
	 * Metodo para verificar si un usuario tiene permisos.
	 */
	public static Boolean tienePermiso(Map<Integer, String> diccionarioPermisos, Integer idPermiso) {
		if (diccionarioPermisos == null) {
			return false;
		}
		
		if (idPermiso == null) {
			return false;
		}
		
		Boolean resultado = false;

		if (diccionarioPermisos.get(idPermiso) != null) {
			resultado = true;
		}

		return resultado;
	}

	/**
	 * Metodo para obtener permisos.
	 */
	public static Map<Integer, String> consultaPermisos(Integer idRol) {
		if (idRol == null) {
			return null;
		}
		
		Connection conn = null;
		
		Map<Integer, String> resultado = new HashMap<Integer, String>();
		try {
			conn = ConexionBD.establecerConexion();
			CallableStatement consultarPermisos = conn.prepareCall("{call RED_PK_TABLASTIPO.consultar_Permisos(?,?)}");
			consultarPermisos.setInt("un_IdRol", idRol);
			consultarPermisos.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultarPermisos.execute();
			ResultSet r = (ResultSet) consultarPermisos.getObject("un_Resultado");
			while (r.next()) {
				resultado.put(r.getInt("prrl_cons_permisos"), idRol.toString());
			}
			r.close();
			consultarPermisos.close();
			conn.close();
		} catch (Exception e) {
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

		return resultado;
	}

}
