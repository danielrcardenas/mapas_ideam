package co.gov.ideamredd.conexionBD;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
@Stateless
public class ConexionBDParcelas {
	
	@Resource(mappedName = "java:/SMBC_DS")
	private DataSource dataSource;
	
	private Connection conn;
	
	public Connection establecerConexion(){
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

}
