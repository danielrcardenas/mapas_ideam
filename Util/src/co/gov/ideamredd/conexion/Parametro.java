// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

/**
 * Clase que representa un parámetro del sistema y permite consultarlo
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class Parametro {

	private static Connection	conn;
	private static DataSource	dataSource	= ConexionBD.getConnection();

	/**
	 * Retorna el valor de un parámetro de cierto nombre
	 * 
	 * @param nombreParametro
	 * @return String con el valor del parámetro
	 */
	public static String getParametro(String nombreParametro) {
		String parametro = null;
		try {
			conn = dataSource.getConnection();
			CallableStatement consultarParameto = conn.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?,?,?)}");
			consultarParameto.setString("un_Nombre", nombreParametro);
			consultarParameto.registerOutParameter("una_Ruta", OracleTypes.CURSOR);
			consultarParameto.registerOutParameter("sentencia", OracleTypes.VARCHAR, 250);
			consultarParameto.execute();
			// System.out.println(consultarParameto.getObject("sentencia"));
			ResultSet resultSet = (ResultSet) consultarParameto.getObject("una_Ruta");

			while (resultSet.next()) {
				parametro = resultSet.getString(1);
			}

			resultSet.close();
			consultarParameto.close();
			conn.close();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return parametro;
	}

}
