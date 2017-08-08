// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.conexion;

import java.sql.Connection;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Métodos para conectarse a la base de datos
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class Conexion {

	@Resource(mappedName = "java:/SMBC_DS")
	private static DataSource	dataSource;

	private static Connection	conn;

	/**
	 * Establece la conexión con la base de datos
	 * 
	 * @return conexión a la base de datos
	 * @throws Exception
	 */
	public static Connection establecerConexion() throws Exception {
		conn = dataSource.getConnection();
		return conn;
	}

}
