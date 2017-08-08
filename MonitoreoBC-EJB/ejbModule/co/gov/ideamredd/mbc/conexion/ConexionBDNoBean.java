// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.conexion;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Clase que realiza la conexion con la base de datos de forma clasica.
 */

public class ConexionBDNoBean {

	public DataSource getConnection() {
		InitialContext initialContext;
		DataSource ds = null;
		try {
			initialContext = new InitialContext();
			ds = (DataSource) initialContext.lookup("java:SMBC_DS");
			//ds = (DataSource) initialContext.lookup("java:/SMBC_DS");
			//ds = (DataSource) initialContext.lookup("java:jboss/datasources/SMBC_DS");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return ds;
	}

}
