package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import co.gov.ideamredd.conexionBD.ConexionBDParcelas;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
@Stateless
public class ConsultarCrucesMetadato {
	
	@EJB
	private  ConexionBDParcelas conexionBD;

	private  Connection conn;
	private  ArrayList<String> comunidad = new ArrayList<String>();
	private  ArrayList<String> resguardo = new ArrayList<String>();
	private  ArrayList<String> parques = new ArrayList<String>();
	
	
	public  ArrayList<String> consultaComunidadParcela(Integer idParcela) {
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement consultarComunidadParcela = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta_Comunidades(?,?,?)}");
			consultarComunidadParcela.setInt("un_Consecutivo", idParcela);
			consultarComunidadParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarComunidadParcela.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarComunidadParcela.execute();
			OracleResultSet r = (OracleResultSet) consultarComunidadParcela
					.getObject("un_Resultado");
			System.out.println(consultarComunidadParcela.getObject("un_Mensaje"));
			while (r.next()) {
				comunidad.add(r.getObject(1).toString());
			}
			r.close();
			consultarComunidadParcela.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comunidad;
	}
	
	public  ArrayList<String> consultaResguardoParcela(Integer idParcela) {
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement consultarresguardoParcela = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta_Resguardos(?,?,?)}");
			consultarresguardoParcela.setInt("un_Consecutivo", idParcela);
			consultarresguardoParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarresguardoParcela.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarresguardoParcela.execute();
			OracleResultSet r = (OracleResultSet) consultarresguardoParcela
					.getObject("un_Resultado");
			System.out.println(consultarresguardoParcela.getObject("un_Mensaje"));
			while (r.next()) {
				resguardo.add(r.getObject(1).toString());
			}
			r.close();
			consultarresguardoParcela.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resguardo;
	}
	
	public  ArrayList<String> consultaParqueParcela(Integer idParcela) {
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement consultarParqueParcela = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta_Parques(?,?,?)}");
			consultarParqueParcela.setInt("un_Consecutivo", idParcela);
			consultarParqueParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarParqueParcela.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarParqueParcela.execute();
			OracleResultSet r = (OracleResultSet) consultarParqueParcela
					.getObject("un_Resultado");
			System.out.println(consultarParqueParcela.getObject("un_Mensaje"));
			while (r.next()) {
				parques.add(r.getObject(1).toString());
			}
			r.close();
			consultarParqueParcela.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parques;
	}
	
}
