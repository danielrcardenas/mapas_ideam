package co.gov.ideamredd.conexionBD;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
/**
 * Clase que genera la conexión a la base de datos
 * la estructura es un objeto singleton de llamado unico 
 * que conecta a través del contexto SMBC_DS de JBOSS hacia una
 * base datos
 * @author Daniel Rodríguez
 *
 */
@Stateless
public class ConexionBD {

	@Resource(mappedName = "java:/SMBC_DS")
	private DataSource dataSource;

	private Connection conn;
	/**
	 * Genera la conexion con la base datos y retorna en caso satisfactorio
	 * la referencia al datasource
	 * @return Conexion
	 */
	public Connection establecerConexion() {
		try {
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
