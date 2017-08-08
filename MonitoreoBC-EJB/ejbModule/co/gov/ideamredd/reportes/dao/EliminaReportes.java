// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;

public class EliminaReportes {

	private Logger log;
	private Connection conn;
	private DataSource dataSource = ConexionBD.getConnection();

	/**
	 * Metodo usado para eliminacion de reportes creados.
	 */
	public void eliminarReporte(Integer idReporte) {
		/*
		String query_infobiomasa = "delete from red_infobiomasa where INBM_CONS_REPORTE="+idReporte;
		String query_infobosque = "delete from RED_INFOBOSQUE where INBS_REPORTE="+idReporte;
		String query_infocobertura = "delete from RED_INFOCOBERTURA where INCB_REPORTE="+idReporte;
		String query_infodeforestacion = "delete from RED_INFODEFORESTACION where INDF_REPORTE="+idReporte;
		*/
		String query = "delete from red_reportes where rprt_consecutivo="+idReporte;
		
		try {
			log = SMBC_Log.Log(EliminaReportes.class);
			conn = dataSource.getConnection();
			Statement consulta = conn.createStatement();
			consulta.executeUpdate(query);
			consulta.close();
			conn.close();
			log.info("[eliminarReporte] Termino");
		} catch (Exception e) {
			log.error("[eliminarReporte] Fallo");
			e.printStackTrace();
		}
	}
	
}
