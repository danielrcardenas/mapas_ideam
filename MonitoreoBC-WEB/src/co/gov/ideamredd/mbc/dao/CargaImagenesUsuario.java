// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;
import co.gov.ideamredd.mbc.conexion.ConexionBD;

/**
 * Métodos para cargar imágenes de usuario 
 *
 */
public class CargaImagenesUsuario {
	
	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();
	
	/**
	 * Metodo para consultar imagenes de usuario aceptadas.
	 */
	public static ArrayList<String> cargaImagenesUsuarioAceptadas() {
		ArrayList<String> imagenes = new ArrayList<String>();
		String query = "select * from red_imagenusuario where imus_cons_estadoimagen=2";
		
		try {
			conn = dataSource.getConnection();
			Statement consulta = conn.createStatement();
			ResultSet resultado = consulta.executeQuery(query);
			
			String datos="";
			while (resultado.next()) {
				datos="";
				datos=datos+resultado.getString("imus_nombre")+"::";
				datos=datos+resultado.getString("imus_path")+"::";
				datos=datos+resultado.getString("imus_descripcion");
				imagenes.add(datos);
			}
			
			resultado.close();
			consulta.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imagenes;
	}
	
}
