
// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.ejb.Stateless;
import javax.sql.DataSource;

//import javax.ejb.EJB;
//import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.internal.OracleTypes;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.mbc.conexion.Parametro;
import co.gov.ideamredd.util.SMBC_Log;

@Stateless
public class ConsultarRutaWPS {

	//@EJB
	//ConexionBD conexion;

	private Connection conn;
	private Logger log;
	private DataSource dataSource = ConexionBD.getConnection();
	
	public String ObtenerRutaWPS(String keyword) {
		
		ResultSet rutaSalidaWPSrs = null;
		String rutaSalidaWPS = "";
		try {
			log = SMBC_Log.Log(ConsultarRutaWPS.class);
			conn = dataSource.getConnection();
			CallableStatement consultarWPSRuta = conn
					.prepareCall("{call RED_PK_APOLLO.ConsultarRutaWPS(?,?,?)}");
			consultarWPSRuta.setString("una_descipcion",
					keyword);
			consultarWPSRuta.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarWPSRuta.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultarWPSRuta.execute();
			System.out.println(consultarWPSRuta.getObject("un_Resultado"));
			rutaSalidaWPSrs =(ResultSet) consultarWPSRuta.getObject("un_Resultado");
			
			while (rutaSalidaWPSrs.next()) {
				rutaSalidaWPS = rutaSalidaWPSrs.getString(1);
				String ruta_base_imagenes_apollo = "";
				String ruta_base_imagenes_smbc = "";

				try {
					Parametro parametro = new Parametro();
					parametro = new Parametro();
					if (parametro != null) {
						ruta_base_imagenes_apollo = parametro.getParametro("ruta_base_imagenes_apollo");
						ruta_base_imagenes_smbc = parametro.getParametro("ruta_base_imagenes_smbc");
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				rutaSalidaWPS = rutaSalidaWPS.replace(ruta_base_imagenes_apollo, ruta_base_imagenes_smbc);
				if (rutaSalidaWPS.indexOf("/") >= 0) {
					rutaSalidaWPS = rutaSalidaWPS.replace("\\", "/");
				}
			}

			log.info("[ObtenerRutaWPS] Termino");
			System.out.println("[ObtenerRutaWPS] Termino");
			rutaSalidaWPSrs.close();
			consultarWPSRuta.close();			
			conn.close();
		} catch (Exception e) {
			log.error("[ObtenerRutaWPS] Fallo");
			System.out.println("[ObtenerRutaWPS] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return rutaSalidaWPS;
	}

	/**
	 * Busca si existe alguna configuracion en la tabla de parametros para establecer que el reporte sera generado con imagenes locales o desde apollo
	 * 
	 * @return boolean indica si traer o no la ruta de la imagen desde apollo
	 */
	public boolean traerRutaDeImagenDeApollo() {
		
	boolean traerRutaDeImagenDeApollo = false;
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaRutaProvider = conn
					.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
			consultaRutaProvider.setString("un_Nombre", "TRAER_RUTA_IMAGENES_REPORTES_DESDE_APOLLO");
			consultaRutaProvider.registerOutParameter("una_Ruta",
					OracleTypes.CURSOR);
			consultaRutaProvider.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultaRutaProvider.execute();
			ResultSet r = (ResultSet) consultaRutaProvider
					.getObject("una_Ruta");
			while (r.next()) {
				traerRutaDeImagenDeApollo = r.getString(1).toLowerCase().contains("si");
			}
			
			r.close();
			consultaRutaProvider.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("No se encuentra el parametro TRAER_RUTA_IMAGENES_REPORTES_DESDE_APOLLO en la tabla PARAMETROS, se asigno el valor de falso por defecto");
			return false;
		}
		return traerRutaDeImagenDeApollo;
	}
	
}
