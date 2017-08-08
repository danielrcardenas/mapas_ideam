// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.descarga.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import oracle.jdbc.OracleTypes;

/**
 * Métodos para consultar datasets de apollo
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class ConsultarDatasets {

	private Connection		conexion;
	private DatosConexion	datosConexion;

	/**
	 * Consulta las descargas de los usuarios
	 * @param tipodato
	 * @param autor
	 * @return String con descripción de las descargas
	 */
	public String consultarDescargasUsuarios(String tipodato, String autor) {
		String cadenaDatos = "";

		tipodato = limpiarTexto(tipodato);
		autor = limpiarTexto(autor);

		String w_tipodato = "";
		if (!tipodato.equals("")) {
			w_tipodato = " AND LOWER(PTD.STRING_VALUE) LIKE '%' || LOWER('" + tipodato + "') || '%' ";
		}

		String w_autor = "";
		if (!autor.equals("")) {
			w_autor = " AND LOWER(I.TAGS) LIKE '%' || LOWER('" + autor + "') || '%' ";
			// w_autor = " AND I.CONTACT_DETAILS = (SELECT CONTACT_ID FROM APOLLO_2013.CONTACT_DETAILS WHERE LOWER(CONTACT_ADDRESS) = LOWER('"+autor+"')) ";
		}

		String query = "SELECT USR.USR_NOMBRE, TID.TPID_NOMBRE, USR.USR_ID, DTDES.DTDS_NOMBREDATASET, PTD.STRING_VALUE AS TIPO_DATO, DTDES.DTDS_FECHADESCARGA, TID.TPID_CONSECUTIVO " + "FROM RED_USUARIO USR, RED_TIPOIDENTIFICACION TID, RED_DETALLEDESCARGA DTDES, APOLLO_2013.CATALOG_ITEM I, APOLLO_2013.CATALOG_ITEM_PROPERTY PTD " + "WHERE USR.USR_CONS_TIPOIDENT = TID.TPID_CONSECUTIVO AND USR.USR_CONSECUTIVO = DTDES.DTDS_CONS_USUARIO AND I.CATALOG_ITEM_TYPE=4 AND PTD.PROPERTY_NAME LIKE \'TipoDato\' " + "AND PTD.CATALOG_ITEM_ID LIKE I.CATALOG_ITEM_ID AND I.TITLE=DTDES.DTDS_NOMBREDATASET " + w_tipodato + w_autor + " ORDER BY DTDES.DTDS_FECHADESCARGA";

		try {
			datosConexion = new DatosConexion();
			if (datosConexion.getUrl().equals("")) {
				return "";
			}
			if (datosConexion.getUsuario().equals("")) {
				return "";
			}
			if (datosConexion.getPassword().equals("")) {
				return "";
			}

			System.out.println("Conexión:" + datosConexion.getPropiedad("url") + "--" + datosConexion.getPropiedad("usuario"));
			conexion = DriverManager.getConnection(datosConexion.getPropiedad("url"), datosConexion.getPropiedad("usuario"), datosConexion.getPropiedad("password"));
			Statement consulta = conexion.createStatement();

			System.out.println("Consulta:" + query);
			ResultSet resultado = consulta.executeQuery(query);

			while (resultado.next()) {
				cadenaDatos = cadenaDatos + resultado.getString(1) + "," + resultado.getString(2) + "," + resultado.getString(3) + "," + resultado.getString(4) + "," + resultado.getString(5) + "," + resultado.getString(6) + ",";
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return cadenaDatos;

	}

	/**
	 * Limpia texto de posible código malicioso
	 * @param texto
	 * @return String de texto depurado
	 */
	public String limpiarTexto(String texto) {
		String textoLimpio = "";

		textoLimpio = nzObjStr(texto, "");

		textoLimpio = textoLimpio.replace("\t", "");
		textoLimpio = textoLimpio.replace("\n", "");
		textoLimpio = textoLimpio.replace("\r", "");
		textoLimpio = textoLimpio.replaceAll("[^a-zA-Z0-9áÁéÉíÍóÓúÚñÑß@$?¿.,!¡:_\\- ]", "");
		textoLimpio = textoLimpio.replaceAll("(?i)insert", "i n s e r t");
		textoLimpio = textoLimpio.replaceAll("(?i)update", "u p d a t e");
		textoLimpio = textoLimpio.replaceAll("(?i)delete", "d e l e t e");
		textoLimpio = textoLimpio.replaceAll("(?i)drop", "d r o p");
		textoLimpio = textoLimpio.replaceAll("(?i)alter", "a l t e r");
		textoLimpio = textoLimpio.trim();

		if (textoLimpio.length() > 31) {
			textoLimpio = textoLimpio.substring(0, 32);
		}

		return textoLimpio;
	}

	/**
	 * Método que retorna un String especificado si el Objeto dado es null
	 * 
	 * @param s
	 * @param valorSiEsNulo
	 * @return valorSiEsNulo si s==null, s de lo contrario
	 */
	public String nzObjStr(Object s, String valorSiEsNulo) {
		String resultado = null;

		if (s == null)
			resultado = valorSiEsNulo;
		else
			resultado = s.toString();

		return resultado;
	}

}
