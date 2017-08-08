package co.gov.ideamredd.conexionBD;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
/**
 * 
 * @author Daniel Rodriguez Cardenas
 * Clase que realiza la conexión con la base de datos de 
 * acuerdo con la configuración de JBOSS
 * en el context, la conexión SMBC_DS
 *
 */
public class ConexionBD {

	/**
	 * Obtiene la conexión a la base deatos
	 * @return Fuente de datos DataSource
	 */
    public static DataSource getConnection() {
	InitialContext initialContext;
	DataSource ds = null;
	try {
	    initialContext = new InitialContext();
	    ds = (DataSource) initialContext.lookup("java:SMBC_DS");
	} catch (NamingException e) {
	    e.printStackTrace();
	}
	return ds;
    }
}
