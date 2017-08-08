// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

/**
 * clase para consultar los parametros del sistema de forma basica.
 */
public class ParametroNoBean {

	public String getParametro(String nombreParametro) {
		ConexionBDNoBean	conexion;

		Connection			conn = null;
		DataSource			dataSource;
		
		String parametro = null;

		try {
			conexion = new ConexionBDNoBean();
			dataSource = conexion.getConnection();
			conn = dataSource.getConnection();
			CallableStatement consultarParameto = conn.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?,?,?)}");
			consultarParameto.setString("un_Nombre", nombreParametro);
			consultarParameto.registerOutParameter("una_Ruta", OracleTypes.CURSOR);
			consultarParameto.registerOutParameter("sentencia", OracleTypes.VARCHAR, 250);
			consultarParameto.execute();
//			System.out.println(consultarParameto.getObject("sentencia"));
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
		}

		return parametro;
	}

}
