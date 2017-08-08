package co.gov.ideamredd.mbc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import co.gov.ideamredd.mbc.conexionBD.ConexionBD;
import co.gov.ideamredd.mbc.entities.InfoCifras;
import co.gov.ideamredd.util.SMBC_Log;

public class ConsultaAlertasDeforestacion {
	
	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();

	private static Logger log;

	public static ArrayList<InfoCifras> consultarCifrasDepartamentos() {
		ArrayList<InfoCifras> cifrasDeptos= new ArrayList<InfoCifras>();
		try {
			conn = dataSource.getConnection();
			log = SMBC_Log.Log(ConsultaAlertasDeforestacion.class);
			CallableStatement consultarCifras = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTARULTIMOREPOIBOSQUE(?,?)}");
			consultarCifras.setInt("UNA_DIVISION", 2);
			consultarCifras.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarCifras.execute();
			
			ResultSet resultSet = (ResultSet) consultarCifras
					.getObject("un_Resultado");
			
			while (resultSet.next()) {
				InfoCifras cifras = new InfoCifras();
				cifras.setBosque(resultSet.getDouble(1));
				cifras.setNoBosque(resultSet.getDouble(2));
				cifras.setSinInfo(resultSet.getDouble(3));
				cifras.setNombre(resultSet.getString(4));
				cifrasDeptos.add(cifras);
			}
			
			resultSet.close();
			conn.close();
			log.info("[consultarCifrasDepartamentos] Termino");
		} catch (Exception e) {
			log.error("[consultarCifrasDepartamentos] Fallo");
			e.printStackTrace();
		}
		
		return cifrasDeptos;
		
	}
	
	public static ArrayList<InfoCifras> consultarCifrasCARs() {
		ArrayList<InfoCifras> cifrasCARs= new ArrayList<InfoCifras>();
		try {
			conn = dataSource.getConnection();
			log = SMBC_Log.Log(ConsultaAlertasDeforestacion.class);
			CallableStatement consultarCifras = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTARULTIMOREPOIBOSQUE(?,?)}");
			consultarCifras.setInt("UNA_DIVISION", 1);
			consultarCifras.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarCifras.execute();
			
			ResultSet resultSet = (ResultSet) consultarCifras
					.getObject("un_Resultado");
			
			while (resultSet.next()) {
				InfoCifras cifras = new InfoCifras();
				cifras.setBosque(resultSet.getDouble(1));
				cifras.setNoBosque(resultSet.getDouble(2));
				cifras.setSinInfo(resultSet.getDouble(3));
				cifras.setNombre(resultSet.getString(4));
				cifrasCARs.add(cifras);
			}
			
			resultSet.close();
			conn.close();
			log.info("[consultarCifrasCARs] Termino");
		} catch (Exception e) {
			log.error("[consultarCifrasCARs] Fallo");
			e.printStackTrace();
		}
		
		return cifrasCARs;
	}
	
	public static ArrayList<Double> consultarCifrasConsolidado() {
		ArrayList<Double> cifrasConsolidado= new ArrayList<Double>();
		try {
			conn = dataSource.getConnection();
			log = SMBC_Log.Log(ConsultaAlertasDeforestacion.class);
			CallableStatement consultarCifras = conn
					.prepareCall("{call RED_PK_REPORTES.CONSULTARULTIMOREPOIBOSQUE(?,?)}");
			consultarCifras.setInt("UNA_DIVISION", 1);
			consultarCifras.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarCifras.execute();
			
			ResultSet resultSet = (ResultSet) consultarCifras
					.getObject("un_Resultado");
			
			Double Sb=0.0,Sbnb=0.0,Ssi=0.0;
			while (resultSet.next()) {
				Sb=Sb+resultSet.getDouble(1);
				Sbnb= Sbnb+resultSet.getDouble(2);
				Ssi=Ssi+resultSet.getDouble(3);
			}
			cifrasConsolidado.add(definirDecimales((Sb/(Sb+Sbnb+Ssi))*100,3));
			cifrasConsolidado.add(definirDecimales((Sbnb/(Sb+Sbnb+Ssi))*100,3));
			cifrasConsolidado.add(definirDecimales((Ssi/(Sb+Sbnb+Ssi))*100,3));
			
			resultSet.close();
			conn.close();
			log.info("[consultarCifrasConsolidado] Termino");
		} catch (Exception e) {
			log.error("[consultarCifrasConsolidado] Fallo");
			e.printStackTrace();
		}
		
		return cifrasConsolidado;
	}
	
	public static double definirDecimales(double numero, int decimales) {
		return Math.round(numero * Math.pow(10, decimales))
				/ Math.pow(10, decimales);
	}

}
