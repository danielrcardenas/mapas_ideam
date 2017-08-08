// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;

public class ActualizaReportes {

	private Logger log;
	private Connection conn;
	private DataSource dataSource = ConexionBD.getConnection();

	public void actualizarReporte(Integer idReporte, Integer estado) { 
		String query = "update red_reportes set rprt_publicado = "+estado+" where rprt_consecutivo="+idReporte;
		
		try {
			log = SMBC_Log.Log(ActualizaReportes.class);
			conn = dataSource.getConnection();
			Statement consulta = conn.createStatement();
			consulta.executeUpdate(query);
			consulta.close();
			conn.close();
			log.info("[actualizarReporte] Termino");
		} catch (Exception e) {
			log.error("[actualizarReporte] Fallo");
			e.printStackTrace();
		}
	}
	
}
