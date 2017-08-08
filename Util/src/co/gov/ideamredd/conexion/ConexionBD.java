// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.conexion;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Métodos para conectarse a la base de datos
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class ConexionBD {

	/**
	 * Retorna una conexión a la base de datos
	 * 
	 * @return conexión a la base de datos
	 */
	public static DataSource getConnection() {
		InitialContext initialContext;
		DataSource ds = null;
		try {
			initialContext = new InitialContext();
			ds = (DataSource) initialContext.lookup("java:/SMBC_DS");
		}
		catch (NamingException e) {
			e.printStackTrace();
		}
		return ds;
	}

}
