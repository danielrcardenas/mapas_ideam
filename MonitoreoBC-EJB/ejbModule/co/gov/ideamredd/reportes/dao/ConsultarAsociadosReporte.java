// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.reportes.entities.InformacionReporteBiomasa;
import co.gov.ideamredd.reportes.entities.InformacionReporteBosque;
import co.gov.ideamredd.reportes.entities.InformacionReporteCobertura;
import co.gov.ideamredd.reportes.entities.InformacionReporteDeforestacion;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.util.Util;

/**
 * Clase usada para consultar los datos de los diferentes tipos de reporte.
 */
@Stateless
public class ConsultarAsociadosReporte {

	@EJB
	ConexionBD											conexion;

	private Connection									conn;
	private ArrayList<InformacionReporteBosque>			infoBosque;
	private ArrayList<InformacionReporteCobertura>		infoCobertura;
	private ArrayList<InformacionReporteDeforestacion>	infoDeforestacion;
	private ArrayList<InformacionReporteBiomasa>		infoBiomasa;
	private ArrayList<Integer>							idsAreaHidrografica		= new ArrayList<Integer>();
	private ArrayList<String>							nombresAreaHidrografica	= new ArrayList<String>();
	private Logger										log;
	private ResultSet									resultSet;

	public ArrayList<InformacionReporteBosque> consultarInfoBosque(Integer idReporte) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosReporte.class);
			conn = conexion.establecerConexion();
			infoBosque = new ArrayList<InformacionReporteBosque>();
			CallableStatement consultarInfoBosque = conn.prepareCall("{call RED_PK_REPORTES.ConsultarInfoBosque(?,?)}");
			consultarInfoBosque.setInt("un_reporte", idReporte);
			consultarInfoBosque.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultarInfoBosque.execute();
			resultSet = (ResultSet) consultarInfoBosque.getObject("un_Resultado");
			Double total = 0d;
			while (resultSet.next()) {
				InformacionReporteBosque info = new InformacionReporteBosque();
				info.setConsecutivo(Integer.valueOf(resultSet.getObject(1).toString()));
				info.setIdReporte(Integer.valueOf(resultSet.getObject(2).toString()));
				info.setBosque(Double.valueOf(resultSet.getObject(3).toString()));
				info.setNoBosque(Double.valueOf(resultSet.getObject(4).toString()));
				info.setSinInformacion(Double.valueOf(resultSet.getObject(5).toString()));
				info.setPorcentaje(Double.valueOf(resultSet.getObject(6).toString()));
				info.setNombre(resultSet.getObject(7).toString());

				total = info.getBosque() + info.getNoBosque() + info.getSinInformacion();
				info.setPorcentajeBosque(Util.calcularPorcentaje(total.intValue(), info.getBosque().intValue()));
				info.setPorcentajeNoBosque(Util.calcularPorcentaje(total.intValue(), info.getNoBosque().intValue()));
				info.setPorcentajeSinInfo(Util.calcularPorcentaje(total.intValue(), info.getSinInformacion().intValue()));

				infoBosque.add(info);
			}

			log.info("[consultarInfoBosque] Termino");
			resultSet.close();
			consultarInfoBosque.close();
			conn.close();
		}
		catch (Exception e) {
			log.error("[consultarInfoBosque] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) { e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return infoBosque;
	}

	public ArrayList<InformacionReporteBiomasa> consultarInfoBiomasa(Integer idReporte) {

		try {
			consultarAreaHidrografica();
			log = SMBC_Log.Log(ConsultarAsociadosReporte.class);
			conn = conexion.establecerConexion();
			infoBiomasa = new ArrayList<InformacionReporteBiomasa>();

			CallableStatement consultarInfoBosque = conn.prepareCall("{call RED_PK_REPORTES.CONSULTAR_INFOBIOMASA(?,?)}");
			consultarInfoBosque.setInt("UN_IDREPORTE", idReporte);
			consultarInfoBosque.registerOutParameter("UN_RESULTADO", OracleTypes.CURSOR);
			consultarInfoBosque.execute();
			resultSet = (ResultSet) consultarInfoBosque.getObject("UN_RESULTADO");
			int indice = 0;

			while (resultSet.next()) {
				InformacionReporteBiomasa info = new InformacionReporteBiomasa();
				info.setIdReporte(resultSet.getInt(1));
				info.setBiomasa(BigDecimal.valueOf(resultSet.getDouble(2)));
				info.setCarbono(BigDecimal.valueOf(resultSet.getDouble(3)));
				info.setBA(BigDecimal.valueOf(resultSet.getDouble(4)));
				info.setC(BigDecimal.valueOf(resultSet.getDouble(5)));
				info.setCO2(BigDecimal.valueOf(resultSet.getDouble(6)));
				info.setNivIncertidumbre(resultSet.getDouble(7));
				info.setNumero(resultSet.getInt(8));
				info.setTipoBosque(resultSet.getString(9));
				info.setNombreTipoBosque(resultSet.getString(10));
				info.setAreaHidrografica(resultSet.getInt(11));
				info.setArea(resultSet.getDouble(12));
				info.setConsecutivo(resultSet.getInt(13));
				indice = idsAreaHidrografica.indexOf(info.getAreaHidrografica());
				info.setNombreAreaHidrografica(nombresAreaHidrografica.get(indice));

				infoBiomasa.add(info);
			}

			log.info("[consultarInfoBiomasa] Termino");
			resultSet.close();
			consultarInfoBosque.close();
			conn.close();
		}
		catch (Exception e) {
			log.error("[consultarInfoBiomasa] Fallo");
			e.printStackTrace();
		}

		return infoBiomasa;

	}

	public ArrayList<InformacionReporteBiomasa> consultar_BC_X_AREAHIDROGRAFICA(Integer idReporte) {

		try {
			consultarAreaHidrografica();
			log = SMBC_Log.Log(ConsultarAsociadosReporte.class);
			conn = conexion.establecerConexion();
			infoBiomasa = new ArrayList<InformacionReporteBiomasa>();

			CallableStatement consultarInfoBosque = conn.prepareCall("{call RED_PK_REPORTES.CONSULTAR_BC_X_AH(?,?)}");
			consultarInfoBosque.setInt("UN_IDREPORTE", idReporte);
			consultarInfoBosque.registerOutParameter("UN_RESULTADO", OracleTypes.CURSOR);
			consultarInfoBosque.execute();

			resultSet = (ResultSet) consultarInfoBosque.getObject("UN_RESULTADO");
			int indice = 0;

			while (resultSet.next()) {
				InformacionReporteBiomasa info = new InformacionReporteBiomasa();

				info.setConsecutivo(resultSet.getInt("AH"));
				info.setNumero(resultSet.getInt("AH"));
				info.setAreaHidrografica(resultSet.getInt("AH"));
				info.setNombreAreaHidrografica(resultSet.getString("NOM_AH"));
				info.setIdReporte(resultSet.getInt("INBM_CONS_REPORTE"));
				info.setBiomasa(BigDecimal.valueOf(resultSet.getDouble("TON_BIOMASA")));
				info.setCarbono(BigDecimal.valueOf(resultSet.getDouble("TON_CARBONO")));
				info.setBA(BigDecimal.valueOf(resultSet.getDouble("SUMA_BIOMASA_AEREA")));
				info.setC(BigDecimal.valueOf(resultSet.getDouble("SUMA_CARBONO")));
				info.setCO2(BigDecimal.valueOf(resultSet.getDouble("SUMA_CO2")));
				info.setNivIncertidumbre(resultSet.getDouble("PROM_INCERTIDUMBRE"));
				info.setArea(resultSet.getDouble("SUMA_AREA"));

				infoBiomasa.add(info);
			}

			log.info("[consultarInfoBiomasa] Termino");
			resultSet.close();
			consultarInfoBosque.close();
			conn.close();
		}
		catch (Exception e) {
			log.error("[consultarInfoBiomasa] Fallo");
			e.printStackTrace();
		}

		return infoBiomasa;
	}

	public ArrayList<InformacionReporteBiomasa> consultar_BC_X_TIPOBOSQUE(Integer idReporte) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosReporte.class);
			conn = conexion.establecerConexion();
			infoBiomasa = new ArrayList<InformacionReporteBiomasa>();

			CallableStatement consultarInfoBosque = conn.prepareCall("{call RED_PK_REPORTES.CONSULTAR_BC_X_TB(?,?)}");
			consultarInfoBosque.setInt("UN_IDREPORTE", idReporte);
			consultarInfoBosque.registerOutParameter("UN_RESULTADO", OracleTypes.CURSOR);
			consultarInfoBosque.execute();

			resultSet = (ResultSet) consultarInfoBosque.getObject("UN_RESULTADO");
			int indice = 0;

			while (resultSet.next()) {
				InformacionReporteBiomasa info = new InformacionReporteBiomasa();

				info.setConsecutivo(resultSet.getInt("INDICE"));
				info.setNumero(resultSet.getInt("INDICE"));
				info.setTipoBosque(resultSet.getString("INBM_TIPOBOSQUE"));
				info.setNombreTipoBosque(resultSet.getString("INBM_NOMBREBOSQUE"));
				info.setIdReporte(resultSet.getInt("INBM_CONS_REPORTE"));
				info.setBiomasa(BigDecimal.valueOf(resultSet.getDouble("TON_BIOMASA")));
				info.setCarbono(BigDecimal.valueOf(resultSet.getDouble("TON_CARBONO")));
				info.setBA(BigDecimal.valueOf(resultSet.getDouble("SUMA_BIOMASA_AEREA")));
				info.setC(BigDecimal.valueOf(resultSet.getDouble("SUMA_CARBONO")));
				info.setCO2(BigDecimal.valueOf(resultSet.getDouble("SUMA_CO2")));
				info.setNivIncertidumbre(resultSet.getDouble("PROM_INCERTIDUMBRE"));
				info.setArea(resultSet.getDouble("SUMA_AREA"));

				infoBiomasa.add(info);
			}

			log.info("[consultarInfoBiomasa] Termino");
			resultSet.close();
			consultarInfoBosque.close();
			conn.close();
		}
		catch (Exception e) {
			log.error("[consultarInfoBiomasa] Fallo");
			e.printStackTrace();
		}

		return infoBiomasa;
	}

	public ArrayList<InformacionReporteBiomasa> consultar_BC_Nacional(Integer idReporte) {
		
		try {
			log = SMBC_Log.Log(ConsultarAsociadosReporte.class);
			conn = conexion.establecerConexion();
			infoBiomasa = new ArrayList<InformacionReporteBiomasa>();
			
			CallableStatement consultarInfoBosque = conn.prepareCall("{call RED_PK_REPORTES.CONSULTAR_BC_NACIONAL(?,?)}");
			consultarInfoBosque.setInt("UN_IDREPORTE", idReporte);
			consultarInfoBosque.registerOutParameter("UN_RESULTADO", OracleTypes.CURSOR);
			consultarInfoBosque.execute();
			
			resultSet = (ResultSet) consultarInfoBosque.getObject("UN_RESULTADO");
			int indice = 0;
			
			while (resultSet.next()) {
				InformacionReporteBiomasa info = new InformacionReporteBiomasa();
				
				info.setConsecutivo(resultSet.getInt("INBM_CONS_REPORTE"));
				info.setNumero(resultSet.getInt("INBM_CONS_REPORTE"));
				info.setIdReporte(resultSet.getInt("INBM_CONS_REPORTE"));
				info.setBiomasa(BigDecimal.valueOf(resultSet.getDouble("TON_BIOMASA")));
				info.setCarbono(BigDecimal.valueOf(resultSet.getDouble("TON_CARBONO")));
				info.setBA(BigDecimal.valueOf(resultSet.getDouble("SUMA_BIOMASA_AEREA")));
				info.setC(BigDecimal.valueOf(resultSet.getDouble("SUMA_CARBONO")));
				info.setCO2(BigDecimal.valueOf(resultSet.getDouble("SUMA_CO2")));
				info.setNivIncertidumbre(resultSet.getDouble("PROM_INCERTIDUMBRE"));
				info.setArea(resultSet.getDouble("SUMA_AREA"));
				
				infoBiomasa.add(info);
			}
			
			log.info("[consultarInfoBiomasa] Termino");
			resultSet.close();
			consultarInfoBosque.close();
			conn.close();
		}
		catch (Exception e) {
			log.error("[consultarInfoBiomasa] Fallo");
			e.printStackTrace();
		}
		
		return infoBiomasa;
	}
	
	public void consultarAreaHidrografica() {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosReporte.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarAreaH = conn.prepareCall("{call RED_PK_REPORTES.CONSULTAR_AREAHIDROGRAFICA(?)}");
			consultarAreaH.registerOutParameter("UN_RESULTADO", OracleTypes.CURSOR);
			consultarAreaH.execute();
			resultSet = (ResultSet) consultarAreaH.getObject("UN_RESULTADO");

			idsAreaHidrografica.clear();
			nombresAreaHidrografica.clear();

			while (resultSet.next()) {
				idsAreaHidrografica.add(resultSet.getInt(1));
				nombresAreaHidrografica.add(resultSet.getString(2));
			}

			log.info("[consultarAreaHidrografica] Termino");
			resultSet.close();
			consultarAreaH.close();
			conn.close();
		}
		catch (Exception e) {
			log.error("[consultarAreaHidrografica] Fallo");
			e.printStackTrace();
		}

	}

	public ArrayList<InformacionReporteCobertura> consultarInfoCobertura(Integer idReporte) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosReporte.class);
			conn = conexion.establecerConexion();
			infoCobertura = new ArrayList<InformacionReporteCobertura>();
			CallableStatement consultarInfoCobertura = conn.prepareCall("{call RED_PK_REPORTES.ConsultarInfoCobertura(?,?)}");
			consultarInfoCobertura.setInt("un_reporte", idReporte);
			consultarInfoCobertura.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultarInfoCobertura.execute();
			resultSet = (ResultSet) consultarInfoCobertura.getObject("un_Resultado");

			Double total = 0d;
			while (resultSet.next()) {
				InformacionReporteCobertura info = new InformacionReporteCobertura();
				info.setConsecutivo(Integer.valueOf(resultSet.getObject(1).toString()));
				info.setIdReporte(Integer.valueOf(resultSet.getObject(2).toString()));
				info.setBosqueEstable(resultSet.getDouble(3));
				info.setNoBosqueEstable(Double.valueOf(resultSet.getObject(4).toString()));
				info.setSinInformacionEstable(Double.valueOf(resultSet.getObject(5).toString()));
				info.setRegeneracion(Double.valueOf(resultSet.getObject(6).toString()));
				info.setDeforestacion(Double.valueOf(resultSet.getObject(7).toString()));
				info.setPorcentaje(Double.valueOf(resultSet.getObject(8).toString()));
				info.setNombre(resultSet.getObject(9).toString());
				/*
				 * info.setBosquePeriodoUno(Double.valueOf(resultSet.getObject(10 ) .toString()));
				 */

				total = info.getBosqueEstable() + info.getNoBosqueEstable() + info.getSinInformacionEstable() + info.getRegeneracion() + info.getDeforestacion();
				info.setPorcBosque(Util.calcularPorcentaje(total.intValue(), info.getBosqueEstable().intValue()));
				info.setPorcNoBosque(Util.calcularPorcentaje(total.intValue(), info.getNoBosqueEstable().intValue()));
				info.setPorcSinInfo(Util.calcularPorcentaje(total.intValue(), info.getSinInformacionEstable().intValue()));
				info.setPorcRegeneracion(Util.calcularPorcentaje(total.intValue(), info.getRegeneracion().intValue()));
				info.setPorcDeforestacion(Util.calcularPorcentaje(total.intValue(), info.getDeforestacion().intValue()));
				infoCobertura.add(info);
			}
			log.info("[consultarInfoCobertura] Termino");
			resultSet.close();
			consultarInfoCobertura.close();
			conn.close();
		}
		catch (Exception e) {
			log.error("[consultarInfoCobertura] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) { e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return infoCobertura;
	}

	public ArrayList<InformacionReporteDeforestacion> consultarInfoDeforestacion(Integer idReporte) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosReporte.class);
			conn = conexion.establecerConexion();
			infoDeforestacion = new ArrayList<InformacionReporteDeforestacion>();
			CallableStatement consultarInfoDeforestacion = conn.prepareCall("{call RED_PK_REPORTES.ConsultarInfoDeforestacion(?,?)}");
			consultarInfoDeforestacion.setInt("un_reporte", idReporte);
			consultarInfoDeforestacion.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultarInfoDeforestacion.execute();
			resultSet = (ResultSet) consultarInfoDeforestacion.getObject("un_Resultado");

			while (resultSet.next()) {
				InformacionReporteDeforestacion info = new InformacionReporteDeforestacion();
				info.setConsecutivo(Integer.valueOf(resultSet.getObject(1).toString()));
				info.setIdReporte(Integer.valueOf(resultSet.getObject(2).toString()));
				info.setArbustales(Double.valueOf(resultSet.getObject(3).toString()));
				info.setAreasAgricolas(Double.valueOf(resultSet.getObject(4).toString()));
				info.setAreasUrbanizadas(Double.valueOf(resultSet.getObject(5).toString()));
				info.setCultivosPermanentes(Double.valueOf(resultSet.getObject(6).toString()));
				info.setCultivosTransitorios(Double.valueOf(resultSet.getObject(7).toString()));
				info.setHerbazales(Double.valueOf(resultSet.getObject(8).toString()));
				info.setOtrasAreasSinVegetacion(Double.valueOf(resultSet.getObject(9).toString()));
				info.setPastosPlantacionForestal(Double.valueOf(resultSet.getObject(10).toString()));
				info.setSuperficiesAgua(Double.valueOf(resultSet.getObject(11).toString()));
				info.setVegetacionAcuatica(Double.valueOf(resultSet.getObject(12).toString()));
				info.setVegetacionSecundaria(Double.valueOf(resultSet.getObject(13).toString()));
				info.setZonasQuemadas(Double.valueOf(resultSet.getObject(14).toString()));
				info.setPorcentaje(Double.valueOf(resultSet.getObject(15).toString()));
				info.setNombre(resultSet.getObject(16).toString());
				info.setPorcArbustales(Double.valueOf(resultSet.getObject(17).toString()));
				info.setPorcAreasAgricolas(Double.valueOf(resultSet.getObject(18).toString()));
				info.setPorcAreasUrbanizadas(Double.valueOf(resultSet.getObject(19).toString()));
				info.setPorcCultivosPermanentes(Double.valueOf(resultSet.getObject(20).toString()));
				info.setPorcCultivosTransitorios(Double.valueOf(resultSet.getObject(21).toString()));
				info.setPorcHerbazales(Double.valueOf(resultSet.getObject(22).toString()));
				info.setPorcOtrasAreasSinVegetacion(Double.valueOf(resultSet.getObject(23).toString()));
				info.setPorcPastosPlantacionForestal(Double.valueOf(resultSet.getObject(24).toString()));
				info.setPorcSuperficiesAgua(Double.valueOf(resultSet.getObject(25).toString()));
				info.setPorcVegetacionAcuatica(Double.valueOf(resultSet.getObject(26).toString()));
				info.setPorcVegetacionSecundaria(Double.valueOf(resultSet.getObject(27).toString()));
				info.setPorcZonasQuemadas(Double.valueOf(resultSet.getObject(28).toString()));

				infoDeforestacion.add(info);
			}
			log.info("[consultarInfoDeforestacion] Termino");
			resultSet.close();
			consultarInfoDeforestacion.close();
			conn.close();
		}
		catch (Exception e) {
			log.error("[consultarInfoDeforestacion] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) { e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return infoDeforestacion;
	}

	public String consultarIdentImagen(Integer id_reporte) {

		if (id_reporte == null)
			return "";

		Statement stmt = null;
		ResultSet rset = null;

		String identImagen = "";
		String sql = "SELECT RPRT_IDENTIMAGEN FROM RED_REPORTES WHERE RPRT_CONSECUTIVO=" + id_reporte;

		try {
			log = SMBC_Log.Log(ConsultarAsociadosReporte.class);
			conn = conexion.establecerConexion();
			stmt = conn.createStatement();

			if (stmt.execute(sql)) {
				rset = stmt.getResultSet();

				if (rset.next()) {
					identImagen = rset.getString(1);
					if (identImagen == null)
						identImagen = "";
				}

				rset.close();
			}

			stmt.close();
		}
		catch (Exception e) {

		}

		return identImagen;
	}

}
