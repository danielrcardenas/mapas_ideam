// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.bosqueencifras.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.bosqueencifras.entities.ProporcionBosque;
import co.gov.ideamredd.bosqueencifras.entities.TasaDeforestacion;
import co.gov.ideamredd.bosqueencifras.entities.CapaWMS;
import co.gov.ideamredd.mbc.conexion.ConexionBD;

/**
 * Métodos para generar el reporte de bosque en cifras
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class BosqueEnCifras {
	
	/**
	 * Retorna una lista de proporciones de bosque
	 * 
	 * @return arreglo de proporciones de bosque por período
	 */
	public static ArrayList<ProporcionBosque> consultarProporcionesBosque() {
		Connection conn = ConexionBD.establecerConexion();

		ArrayList<ProporcionBosque> proporcionesBosque = new ArrayList<ProporcionBosque>();
		
		try {
			CallableStatement stmt = conn.prepareCall("{call RED_PK_BOSQUE_EN_CIFRAS.ProporcionBosque(?,?)}");
			stmt.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			stmt.registerOutParameter("una_sentencia", OracleTypes.VARCHAR);
			stmt.execute();
			
			ResultSet resultSet = (ResultSet) stmt.getObject("un_Resultado");
			while (resultSet.next()) {
				ProporcionBosque proporcionBosque = new ProporcionBosque();
				proporcionBosque.setId_reporte(resultSet.getString("RPRT_CONSECUTIVO"));
				proporcionBosque.setPeriodo(resultSet.getString("PERIODO"));
				proporcionBosque.setAreaBosque(resultSet.getDouble("AREA_BOSQUE"));
				proporcionBosque.setAreaTotal(resultSet.getDouble("AREA_TOTAL"));
				proporcionBosque.setPorcentaje(resultSet.getDouble("PORCENTAJE"));
				proporcionesBosque.add(proporcionBosque);
			}
			
			String sql = stmt.getString("una_sentencia");
			System.out.println(sql);
			
			resultSet.close();
			stmt.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return proporcionesBosque;
	}

	/**
	 * Retorna un arreglo de Tasas de deforestación por período
	 * 
	 * @return arreglo de tasas de deforestación por período
	 */
	public static ArrayList<TasaDeforestacion> consultarTasasDeforestacion() {
		Connection conn = ConexionBD.establecerConexion();
		
		ArrayList<TasaDeforestacion> tasasDeforestacion = new ArrayList<TasaDeforestacion>();
		
		try {
			CallableStatement stmt = conn.prepareCall("{call RED_PK_BOSQUE_EN_CIFRAS.TasaDeforestacion(?,?)}");
			stmt.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			stmt.registerOutParameter("una_sentencia", OracleTypes.VARCHAR);
			stmt.execute();
			
			ResultSet resultSet = (ResultSet) stmt.getObject("un_Resultado");
			while (resultSet.next()) {
				TasaDeforestacion tasaDeforestacion = new TasaDeforestacion();
				tasaDeforestacion.setId_reporte(resultSet.getString("RPRT_CONSECUTIVO"));
				tasaDeforestacion.setPeriodo(resultSet.getString("PERIODO"));
				tasaDeforestacion.setAreaBosqueInicial(resultSet.getDouble("AREA_BOSQUE_INICIAL"));
				tasaDeforestacion.setAreaBosqueFinal(resultSet.getDouble("AREA_BOSQUE_FINAL"));
				tasaDeforestacion.setAreaDeforestada(resultSet.getDouble("AREA_DEFORESTADA"));
				tasaDeforestacion.setTasaDeforestacion(resultSet.getDouble("TASA_DEFORESTACION"));
				tasasDeforestacion.add(tasaDeforestacion);
			}
			
			String sql = stmt.getString("una_sentencia");
			System.out.println(sql);
			
			resultSet.close();
			stmt.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return tasasDeforestacion;
	}
	
	/**
	 * Retorna una lista de capas WMS de tipo BNB para ser desplegadas en el visor
	 * 
	 * @return arreglo de capas WMS de tipo BNB
	 */
	public static ArrayList<CapaWMS> consultarCapasBNB() {
		Connection conn = ConexionBD.establecerConexion();
		
		ArrayList<CapaWMS> capasWMS = new ArrayList<CapaWMS>();
		
		try {
			CallableStatement stmt = conn.prepareCall("{call RED_PK_BOSQUE_EN_CIFRAS.CapasWMS_BNB(?,?)}");
			stmt.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			stmt.registerOutParameter("una_sentencia", OracleTypes.VARCHAR);
			stmt.execute();
			
			ResultSet resultSet = (ResultSet) stmt.getObject("un_Resultado");
			while (resultSet.next()) {
				CapaWMS capaWMS = new CapaWMS();
				capaWMS.setPeriodo(resultSet.getString("PERIODO"));
				capaWMS.setIdentimagen(resultSet.getString("IDENTIMAGEN"));
				capasWMS.add(capaWMS);
			}
			
			String sql = stmt.getString("una_sentencia");
			System.out.println(sql);
			
			resultSet.close();
			stmt.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return capasWMS;
	}
	
	/**
	 * Retorna una lista de capas WMS de tipo Cambio de Cobertura para ser desplegadas en el visor
	 * 
	 * @return arreglo de capas WMS de tipo Cambio de Cobertura
	 */
	public static ArrayList<CapaWMS> consultarCapasCC() {
		Connection conn = ConexionBD.establecerConexion();
		
		ArrayList<CapaWMS> capasWMS = new ArrayList<CapaWMS>();
		
		try {
			CallableStatement stmt = conn.prepareCall("{call RED_PK_BOSQUE_EN_CIFRAS.CapasWMS_CC(?,?)}");
			stmt.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			stmt.registerOutParameter("una_sentencia", OracleTypes.VARCHAR);
			stmt.execute();
			
			ResultSet resultSet = (ResultSet) stmt.getObject("un_Resultado");
			while (resultSet.next()) {
				CapaWMS capaWMS = new CapaWMS();
				capaWMS.setPeriodo(resultSet.getString("PERIODO"));
				capaWMS.setIdentimagen(resultSet.getString("IDENTIMAGEN"));
				capasWMS.add(capaWMS);
			}
			
			String sql = stmt.getString("una_sentencia");
			System.out.println(sql);
			
			resultSet.close();
			stmt.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return capasWMS;
	}
	

}
