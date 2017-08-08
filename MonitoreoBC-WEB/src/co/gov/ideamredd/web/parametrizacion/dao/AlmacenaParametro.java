// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.parametrizacion.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Connection;
import java.util.ArrayList;
import javax.sql.DataSource;
import oracle.jdbc.OracleTypes;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.parametrizacion.entities.Noticias;
import co.gov.ideamredd.parametrizacion.entities.Rutas;


/**
 * Carga los datos iniciales de las rutas de configuraci�n. La ruta de Metadatos y Thumbnails vienen del archivo providers.fac de apollo, mientras que el resto vienen de la base de datos
 */
public class AlmacenaParametro {

	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();
	private static ArrayList<Rutas> listaRutasApollo;
	private static Logger log=Logger.getLogger("SMBCLog");

	public static String almacenarParametro(int PRTR_CONSECUTIVO, String PRTR_RUTA) {
		String resultado = ""; 
		try {
			conn = dataSource.getConnection();
			CallableStatement stmt = conn.prepareCall("{call RED_PK_PARAMETROS.Parametro_Actualiza(?,?,?,?)}");
			stmt.setInt("una_Llave", PRTR_CONSECUTIVO);
			stmt.setString("una_Ruta", PRTR_RUTA);
			stmt.registerOutParameter("una_Respuesta", OracleTypes.INTEGER);
			stmt.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			stmt.registerOutParameter("una_Respuesta", OracleTypes.NUMBER);
			stmt.execute();
			resultado = "OK";
			
			//resultado = (String) stmt.getObject("una_Respuesta");
			
			// System.out.println(stmt.getObject("una_Respuesta"));
			stmt.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				conn = null;
			}
		}
		return resultado;
	}
	

}
