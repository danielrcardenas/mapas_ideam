// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

/**
 * Clase para consultar los parametros del sistema.
 */

@Stateless public class Parametro {

	public static String getParametro(String nombreParametro) {

//		System.out.println("Consultando parámetro: " + nombreParametro);

		String parametro = null;

		String sql = "";

		ConexionBD conexion = new ConexionBD();
		Connection conn = conexion.establecerConexion();

		try {
			CallableStatement stmt = conn.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?,?,?)}");
			stmt.setString("un_Nombre", nombreParametro);
			stmt.registerOutParameter("una_Ruta", OracleTypes.CURSOR);
			stmt.registerOutParameter("sentencia", OracleTypes.VARCHAR, 250);

//			System.out.println("Ejecutando sql parametro: " + nombreParametro);

			stmt.execute();
			sql = stmt.getString("sentencia");
//			System.out.println(sql);

			if (!stmt.isClosed()) {
				ResultSet resultSet = (ResultSet) stmt.getObject("una_Ruta");
				if (!resultSet.isClosed()) {
					while (resultSet.next()) {
						parametro = resultSet.getString(1);
					}
				}
				resultSet.close();
				stmt.close();
			}
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(sql);
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
		}

		return parametro;
	}

}
