package co.gov.ideamredd.conexionBD;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConexionBD {

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
