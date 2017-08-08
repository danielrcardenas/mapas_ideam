package co.gov.ideamredd.conexionBD;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Stateless
public class ConexionBD {

	@Resource(mappedName = "java:/SMBC_DS")
	private DataSource dataSource;

	private Connection conn;

	public Connection establecerConexion() {
		try {
			if (conn != null) {
				conn.close();
			}
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

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
