package co.gov.ideamredd.conexionBD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.ejb.Stateless;
import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;
/**
 * Utilitario que permite obtener parámetros desde la tabla parametro
 * de la basede datos. Utiliza el paquete Oracle Parametro_consulta
 * 
 * @author Daniel Rodríguez
 *
 */
@Stateless
public class Parametro { 
	
	
	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();
	private static ResultSet resultSet;
	private static CallableStatement consultarParameto;
	
	/**
	 * Método que devueve el valor de un parámetro al ingresar su nombre
	 * @param nombreParametro Nombre de la variable que se requiere
	 * @return Valor del parámetro encontrado en la base de datos
	 */
	public static String getParametro(String nombreParametro){
		String parametro=null;
		try {
			dataSource = ConexionBD.getConnection();
			conn = dataSource.getConnection();
			consultarParameto = conn
					.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?,?,?)}");
			consultarParameto.setString("un_Nombre", nombreParametro);
			consultarParameto.registerOutParameter("una_Ruta",
					OracleTypes.CURSOR);
			consultarParameto.registerOutParameter("sentencia",
					OracleTypes.VARCHAR, 250);
			consultarParameto.execute();
			System.out.println(consultarParameto.getObject("sentencia"));
			resultSet = (ResultSet) consultarParameto
					.getObject("una_Ruta");
			
			while (resultSet.next()) {
				parametro=resultSet.getString(1);
			}

			resultSet.close();
			consultarParameto.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeConection();
		}
		
		return parametro;
	}
	/**
	 * Cierra la conexión a la base de datos al finalizar el proceso de consulta
	 */
	private static void closeConection() {
		try {
			resultSet.close();
		} catch (Exception e) {
		}
		try {
			consultarParameto.close();
		} catch (Exception e) {
		}
		try {
			conn.close();
		} catch (Exception e) {
		}
	}

}
