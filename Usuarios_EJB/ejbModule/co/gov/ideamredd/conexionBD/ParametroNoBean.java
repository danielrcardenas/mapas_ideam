package co.gov.ideamredd.conexionBD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

public class ParametroNoBean { 
	
	private ConexionBDNoBean conexion;
	
	private Connection conn;
	private DataSource dataSource;
	
	public String getParametro(String nombreParametro){
		String parametro=null;
		
		try {
			conexion=new ConexionBDNoBean();
			dataSource=conexion.getConnection();
			conn = dataSource.getConnection();
			CallableStatement consultarParameto = conn
					.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?,?,?)}");
			consultarParameto.setString("un_Nombre", nombreParametro);
			consultarParameto.registerOutParameter("una_Ruta",
					OracleTypes.CURSOR);
			consultarParameto.registerOutParameter("sentencia",
					OracleTypes.VARCHAR, 250);
			consultarParameto.execute();
			System.out.println(consultarParameto.getObject("sentencia"));
			ResultSet resultSet = (ResultSet) consultarParameto
					.getObject("una_Ruta");
			
			while (resultSet.next()) {
				parametro=resultSet.getString(1);
			}

			resultSet.close();
			consultarParameto.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return parametro;
	}

}
