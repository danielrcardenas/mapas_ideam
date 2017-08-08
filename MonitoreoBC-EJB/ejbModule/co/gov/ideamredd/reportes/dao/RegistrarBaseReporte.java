// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;

public class RegistrarBaseReporte {

	private Logger log;
	private Connection conn;
	private DataSource dataSource = ConexionBD.getConnection();

	public Integer registrarReporte(Integer idReporte, Integer rprt_divisionterritorio,Integer rprt_peridouno,Integer rprt_periododos,Integer rprt_tiporeporte,Integer rprt_publicado) {
		Integer resultado=-1;
		
		String query = "insert into red_reportes(rprt_consecutivo,rprt_fechageneracion,rprt_divisionterritorio,rprt_periodouno,rprt_periododos,rprt_tiporeporte,rprt_publicado) " +
									"values("+idReporte+",sysdate,"+rprt_divisionterritorio+","+rprt_peridouno+","+rprt_periododos+","+rprt_tiporeporte+","+rprt_publicado+")";
		
		try {
			log = SMBC_Log.Log(RegistrarBaseReporte.class);
			conn = dataSource.getConnection();
			Statement consulta = conn.createStatement();
			
			consulta.executeUpdate(query);
			
			consulta.close();
			conn.close();
			log.info("[consultarImagenes] Termino");
		} catch (Exception e) {
			log.error("[consultarImagenes] Fallo");
			e.printStackTrace();
		}

		return resultado;
	}
	
	public Integer consultarUltimoIdReporte(){
		
		Integer resultado=-1;
		
		String query = "select MAX(rprt_consecutivo) from red_reportes";
		
		try {
			log = SMBC_Log.Log(RegistrarBaseReporte.class);
			conn = dataSource.getConnection();
			Statement consulta = conn.createStatement();
			ResultSet resultSet=consulta.executeQuery(query);
			while (resultSet.next()) {
				resultado=resultSet.getInt(1);
			}

			resultSet.close();
			consulta.close();
			conn.close();
			log.info("[consultarImagenes] Termino");
		} catch (Exception e) {
			log.error("[consultarImagenes] Fallo");
			e.printStackTrace();
		}

		return resultado;
		
	}
	
}
