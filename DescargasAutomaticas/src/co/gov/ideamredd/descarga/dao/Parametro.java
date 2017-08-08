// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.descarga.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import oracle.jdbc.OracleTypes;

/**
 * Métodos para consultar los parámetros del sistema
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class Parametro {

	private Connection		conexion;
	private DatosConexion	datosConexion;

	/**
	 * Retorna un parámetro del sistema según su nombre
	 * 
	 * @param nombreParametro
	 * @return String del valor del parámetro
	 */
	public String getParametro(String nombreParametro) {
		String parametro = null;
		String url = "";
		String usuario = "";
		String password = "";

		try {
			datosConexion = new DatosConexion();
			url = datosConexion.getPropiedad("url");
			// System.out.println("URL:" + url);
			usuario = datosConexion.getPropiedad("usuario");
			// System.out.println("Usuario:" + usuario);
			password = datosConexion.getPropiedad("password");
			// System.out.println("Password:" + password);

			conexion = DriverManager.getConnection(url, usuario, password);

			CallableStatement consultarParameto = conexion.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?,?,?)}");

			consultarParameto.setString("un_Nombre", nombreParametro);
			consultarParameto.registerOutParameter("una_Ruta", OracleTypes.CURSOR);
			consultarParameto.registerOutParameter("sentencia", OracleTypes.VARCHAR, 250);
			consultarParameto.execute();

			// System.out.println(consultarParameto.getObject("sentencia"));
			ResultSet resultSet = (ResultSet) consultarParameto.getObject("una_Ruta");

			while (resultSet.next()) {
				parametro = resultSet.getString(1);
				//System.out.println("Parametro:" + nz(parametro, "null"));
			}

			resultSet.close();
			consultarParameto.close();
			conexion.close();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return parametro;
	}

	/**
	 * MÃ©todo que retorna un valor especificado si el String dado es null
	 * 
	 * @param s
	 * @param valorSiEsNulo
	 * @return valorSiEsNulo si s==null, s de lo contrario
	 */
	public String nz(String s, String valorSiEsNulo) {
		String resultado = "";

		if (s == null)
			resultado = valorSiEsNulo;
		else
			resultado = s;

		return resultado;
	}

}
