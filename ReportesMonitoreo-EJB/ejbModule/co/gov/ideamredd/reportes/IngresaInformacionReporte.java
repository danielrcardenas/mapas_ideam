package co.gov.ideamredd.reportes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.internal.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.InformacionReporteBiomasa;
import co.gov.ideamredd.entities.InformacionReporteBosque;
import co.gov.ideamredd.entities.InformacionReporteCobertura;
import co.gov.ideamredd.entities.InformacionReporteDeforestacion;
import co.gov.ideamredd.entities.Reportes;
import co.gov.ideamredd.util.SMBC_Log;

@Stateless
public class IngresaInformacionReporte {

	@EJB
	ConexionBD conexion;

	private Connection conn;
	private Logger log;

	public Integer IngresarReporte(Reportes reporte) {
		Integer idReporte = null;
		try {
			log = SMBC_Log.Log(IngresaInformacionReporte.class);
			conn = conexion.establecerConexion();
			CallableStatement insertarReporte = conn
					.prepareCall("{call RED_PK_REPORTES.InsertarReporte(?,?,?,?,?,?,?)}");
			insertarReporte.setDate("una_fechaGeneracion",
					reporte.getFechaGeneracion());
			insertarReporte.setInt("una_divisionTerritorio",
					reporte.getDivision());
			insertarReporte.setInt("un_periodoUno", reporte.getPeriodoUno());
			insertarReporte.setInt("un_periodoDos", reporte.getPeriodoDos());
			insertarReporte.setInt("un_tipoReporte", reporte.getTipoReporte());
			insertarReporte.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			insertarReporte.registerOutParameter("un_IdReporte",
					OracleTypes.NUMBER);
			insertarReporte.execute();
			System.out.println(insertarReporte.getObject("un_Resultado"));
			idReporte = Integer.valueOf(insertarReporte.getObject(
					"un_IdReporte").toString());

			log.info("[IngresarReporte] Termino");
			insertarReporte.close();
			conn.close();
		} catch (Exception e) {
			log.error("[IngresarReporte] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return idReporte;
	}

	public void IngresarInfoBosque(InformacionReporteBosque bosque) {
		try {
			log = SMBC_Log.Log(IngresaInformacionReporte.class);
			conn = conexion.establecerConexion();
			CallableStatement insertarInfoBosque = conn
					.prepareCall("{call RED_PK_REPORTES.InsertarInfoBosque(?,?,?,?,?,?,?,?,?,?)}");
			insertarInfoBosque.setInt("un_Reporte", bosque.getIdReporte());
			insertarInfoBosque.setDouble("un_Bosque", bosque.getBosque());
			insertarInfoBosque.setDouble("un_noBosque", bosque.getNoBosque());
			insertarInfoBosque.setDouble("una_sinInformacion",
					bosque.getSinInformacion());
			insertarInfoBosque.setDouble("un_porcentaje",
					bosque.getPorcentaje());
			insertarInfoBosque.setString("un_nombre", bosque.getNombre());
			insertarInfoBosque.setDouble("un_porcBosque",
					bosque.getPorcentajeBosque());
			insertarInfoBosque.setDouble("un_porcNoBosque",
					bosque.getPorcentajeNoBosque());
			insertarInfoBosque.setDouble("un_porcSinInfo",
					bosque.getPorcentajeSinInfo());
			insertarInfoBosque.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			insertarInfoBosque.execute();
			System.out.println(insertarInfoBosque.getObject("un_Resultado"));

			log.info("[IngresarInfoBosque] Termino");
			insertarInfoBosque.close();
			conn.close();
		} catch (Exception e) {
			log.error("[IngresarInfoBosque] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
	}

	public void IngresarInfoBiomasa(InformacionReporteBiomasa biomasa) {
		try {
			log = SMBC_Log.Log(IngresaInformacionReporte.class);
			conn = conexion.establecerConexion();
			CallableStatement insertarInfoBiomasa = conn
					.prepareCall("{call RED_PK_REPORTES.InsertarInfoBiomasa(?,?,?,?,?,?,?,?,?,?,?,?)}");
			insertarInfoBiomasa.setInt("un_Reporte", biomasa.getIdReporte());
			insertarInfoBiomasa.setInt("un_numero", biomasa.getNumero());
			insertarInfoBiomasa.setString("un_tipoBosque",
					biomasa.getTipoBosque());
			insertarInfoBiomasa.setString("un_nombreTipoBosque",
					biomasa.getNombreTipoBosque());
			insertarInfoBiomasa.setInt("un_areaHidrografica",
					biomasa.getAreaHidrografica());
			insertarInfoBiomasa.setDouble("un_area", biomasa.getArea());

			insertarInfoBiomasa.setBigDecimal("una_biomasa",
					biomasa.getBiomasa());
			insertarInfoBiomasa.setBigDecimal("un_carbono",
					biomasa.getCarbono());
			insertarInfoBiomasa.setBigDecimal("una_BA", biomasa.getBA());
			insertarInfoBiomasa.setBigDecimal("un_C", biomasa.getC());
			insertarInfoBiomasa.setBigDecimal("un_CO2", biomasa.getCO2());
			insertarInfoBiomasa.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			insertarInfoBiomasa.execute();
			System.out.println(insertarInfoBiomasa.getObject("un_Resultado"));
			
			log.info("[IngresarInfoBiomasa] Termino");
			insertarInfoBiomasa.close();
			conn.close();
		} catch (Exception e) {
			log.info("[IngresarInfoBiomasa] Fallo");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	public void IngresarInfoCobertura(InformacionReporteCobertura cobertura) {
		try {
			log = SMBC_Log.Log(IngresaInformacionReporte.class);
			conn = conexion.establecerConexion();
			CallableStatement insertarInfoCobertura = conn
					.prepareCall("{call RED_PK_REPORTES.InsertarInfoCobertura(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			insertarInfoCobertura
					.setInt("un_reporte", cobertura.getIdReporte());
			insertarInfoCobertura.setDouble("un_bosqueEstable",
					cobertura.getBosqueEstable());
			insertarInfoCobertura.setDouble("un_noBosqueEstable",
					cobertura.getNoBosqueEstable());
			insertarInfoCobertura.setDouble("una_sinInformacion",
					cobertura.getSinInformacionEstable());
			insertarInfoCobertura.setDouble("una_regeneracion",
					cobertura.getRegeneracion());
			insertarInfoCobertura.setDouble("una_deforestacion",
					cobertura.getDeforestacion());
			insertarInfoCobertura.setDouble("un_porcentaje",
					cobertura.getPorcentaje());
			insertarInfoCobertura.setString("un_nombre", cobertura.getNombre());
			insertarInfoCobertura.setDouble("un_bosquepuno",
					cobertura.getBosquePeriodoUno());
			insertarInfoCobertura.setDouble("un_porcbosque",
					cobertura.getPorcBosque());
			insertarInfoCobertura.setDouble("un_porcnobosque",
					cobertura.getPorcNoBosque());
			insertarInfoCobertura.setDouble("un_porcsininfo",
					cobertura.getPorcSinInfo());
			insertarInfoCobertura.setDouble("un_porcregenra",
					cobertura.getPorcRegeneracion());
			insertarInfoCobertura.setDouble("un_porcdeforesta",
					cobertura.getPorcDeforestacion());
			insertarInfoCobertura.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			insertarInfoCobertura.execute();
			System.out.println(insertarInfoCobertura.getObject("un_Resultado"));

			log.info("[IngresarInfoCobertura] Termino");
			insertarInfoCobertura.close();
			conn.close();
		} catch (Exception e) {
			log.error("[IngresarInfoCobertura] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
	}

	public void IngresarInfoDeforestacion(
			InformacionReporteDeforestacion deforestacion) {
		try {
			log = SMBC_Log.Log(IngresaInformacionReporte.class);
			conn = conexion.establecerConexion();
			CallableStatement insertarInfoDeforestacion = conn
					.prepareCall("{call RED_PK_REPORTES.InsertarInfoDeforestacion(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			insertarInfoDeforestacion.setInt("un_reporte",
					deforestacion.getIdReporte());
			insertarInfoDeforestacion.setDouble("unos_arbustales",
					deforestacion.getArbustales());
			insertarInfoDeforestacion.setDouble("un_areaAgricola",
					deforestacion.getAreasAgricolas());
			insertarInfoDeforestacion.setDouble("un_areaUbanizada",
					deforestacion.getAreasUrbanizadas());
			insertarInfoDeforestacion.setDouble("un_cultivoPermanente",
					deforestacion.getCultivosPermanentes());
			insertarInfoDeforestacion.setDouble("un_cultivoTransitorio",
					deforestacion.getCultivosTransitorios());
			insertarInfoDeforestacion.setDouble("unos_herbazales",
					deforestacion.getHerbazales());
			insertarInfoDeforestacion.setDouble("otraAreavegetacion",
					deforestacion.getOtrasAreasSinVegetacion());
			insertarInfoDeforestacion.setDouble("unos_pastos",
					deforestacion.getPastosPlantacionForestal());
			insertarInfoDeforestacion.setDouble("una_superficieAgua",
					deforestacion.getSuperficiesAgua());
			insertarInfoDeforestacion.setDouble("una_vegetacionAcuatica",
					deforestacion.getVegetacionAcuatica());
			insertarInfoDeforestacion.setDouble("una_vegetacionSecundaria",
					deforestacion.getVegetacionSecundaria());
			insertarInfoDeforestacion.setDouble("unas_zonasQuemadas",
					deforestacion.getZonasQuemadas());
			insertarInfoDeforestacion.setDouble("un_porcentaje",
					deforestacion.getPorcentaje());
			insertarInfoDeforestacion.setString("un_nombre",
					deforestacion.getNombre());
			insertarInfoDeforestacion.setDouble("un_porcArbustales",
					deforestacion.getPorcArbustales());
			insertarInfoDeforestacion.setDouble("un_porcAreaAgricola",
					deforestacion.getPorcAreasAgricolas());
			insertarInfoDeforestacion.setDouble("un_porcAreaUrbanizada",
					deforestacion.getPorcAreasUrbanizadas());
			insertarInfoDeforestacion.setDouble("un_porcCultivoPermanente",
					deforestacion.getPorcCultivosPermanentes());
			insertarInfoDeforestacion.setDouble("un_porcCultivoTransitorio",
					deforestacion.getPorcCultivosTransitorios());
			insertarInfoDeforestacion.setDouble("un_porcHerbazales",
					deforestacion.getPorcHerbazales());
			insertarInfoDeforestacion.setDouble("un_porcAreaSinVegetacion",
					deforestacion.getPorcOtrasAreasSinVegetacion());
			insertarInfoDeforestacion.setDouble("un_porcPastos",
					deforestacion.getPorcPastosPlantacionForestal());
			insertarInfoDeforestacion.setDouble("un_porcSuperficieAgua",
					deforestacion.getPorcSuperficiesAgua());
			insertarInfoDeforestacion.setDouble("un_porcVegetacionAcuativa",
					deforestacion.getPorcVegetacionAcuatica());
			insertarInfoDeforestacion.setDouble("un_porcVegetacionSecundaria",
					deforestacion.getPorcVegetacionSecundaria());
			insertarInfoDeforestacion.setDouble("un_porcZonasQuemadas",
					deforestacion.getPorcZonasQuemadas());
			insertarInfoDeforestacion.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			insertarInfoDeforestacion.execute();
			System.out.println(insertarInfoDeforestacion
					.getObject("un_Resultado"));

			log.info("[IngresarInfoDeforestacion] Termino");
			insertarInfoDeforestacion.close();
			conn.close();
		} catch (Exception e) {
			log.error("[IngresarInfoDeforestacion] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
	}

}
