// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.descarga.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;

import oracle.jdbc.OracleTypes;

@Stateless
public class ConsultarDatasets {

	@EJB
	static ConexionBD conexion;

	private static Logger log=Logger.getLogger("SMBCLog");
	private static Connection conn;
	
	/**
	 * Obtiene la lista de datasets registrado en apollo
	 * @return Arreglo con la informaci�n de los dataset
	 */
	 public static void getDatasets(){
		 
			try {
				conn = conexion.establecerConexion();
				CallableStatement consultaRutaProvider = conn
						.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
				consultaRutaProvider.registerOutParameter("una_Ruta",
						OracleTypes.CURSOR);
				consultaRutaProvider.registerOutParameter("sentencia",
						OracleTypes.VARCHAR);
				consultaRutaProvider.execute();
				ResultSet r = (ResultSet) consultaRutaProvider.getObject("una_Ruta");
				
				while (r.next()) {
					
				}
				
				r.close();
				consultaRutaProvider.close();
				conn.close();
				log.info(ConsultarDatasets.class+" Termino la consulta de datos");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(ConsultarDatasets.class+" Error en la consulta de datos ", e);
			}
			
	}
}
