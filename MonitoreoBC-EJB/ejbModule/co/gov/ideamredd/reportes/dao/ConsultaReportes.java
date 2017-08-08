// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.proyecto.entities.ActividadRedd;
import co.gov.ideamredd.reportes.entities.AgregadoReporte;
import co.gov.ideamredd.reportes.entities.DivisionTerritorio;
import co.gov.ideamredd.reportes.entities.Reporte;
import co.gov.ideamredd.reportes.entities.TipoReporte;
import co.gov.ideamredd.util.SMBC_Log;

/**
 * Clase usada para consulta de reportes.
 */

public class ConsultaReportes {


	public static ArrayList<Reporte> consultarReportes() { 
		Logger log = null;
		Connection conn = ConexionBD.establecerConexion();
		
		ArrayList<Reporte> imagenes = new ArrayList<Reporte>();
		Reporte reporte=null;
		
		String query = "select * from red_reportes ";
		
		try {
			log = SMBC_Log.Log(ConsultaReportes.class);
			Statement consulta = conn.createStatement();
			ResultSet resultSet = consulta.executeQuery(query);

			while (resultSet.next()) {
				reporte = new Reporte();
				reporte.setId(resultSet.getInt("RPRT_CONSECUTIVO"));	
				reporte.setFechaGeneracion(resultSet.getDate("RPRT_FECHAGENERACION"));
				reporte.setIdDivision(resultSet.getInt("RPRT_DIVISIONTERRITORIO"));
				reporte.setPeriodoUno(resultSet.getInt("RPRT_PERIODOUNO"));
				reporte.setPeriodoDos(resultSet.getInt("RPRT_PERIODODOS"));
				reporte.setIdTipoReporte(resultSet.getInt("RPRT_TIPOREPORTE"));
				reporte.setPublicado(resultSet.getBoolean("RPRT_PUBLICADO"));
				reporte.setIdentImagen(resultSet.getString("RPRT_IDENTIMAGEN"));
				imagenes.add(reporte);
			}

			resultSet.close();
			consulta.close();
			conn.close();
			log.info("[consultarImagenes] Termino");
		} catch (Exception e) {
			log.error("[consultarImagenes] Fallo");
			e.printStackTrace();
		}

		return imagenes;
	}
	
	public static String consultarTipoReporte(Integer idTipo) { 
		Logger log = null;
		Connection conn = ConexionBD.establecerConexion();

		String resultado = "";
		
		String query = "select tprp_nombre from red_tiporeporte where tprp_consecutivo="+idTipo;
		
		try {
			log = SMBC_Log.Log(ConsultaReportes.class);
			Statement consulta = conn.createStatement();
			ResultSet resultSet = consulta.executeQuery(query);

			while (resultSet.next()) {
				resultado=resultSet.getString("tprp_nombre");	
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
	
	public static ArrayList<TipoReporte> consultarTipoReportes() { 
		Logger log = null;
		Connection conn = ConexionBD.establecerConexion();
		ArrayList<TipoReporte> tipoReportes = new ArrayList<TipoReporte>();
		
		String query = "select * from red_tiporeporte";
		
		try {
			log = SMBC_Log.Log(ConsultaReportes.class);
			Statement consulta = conn.createStatement();
			ResultSet resultSet = consulta.executeQuery(query);

			while (resultSet.next()) {
				TipoReporte trep = new TipoReporte();
				trep.setId(resultSet.getInt("tprp_consecutivo"));
				trep.setNombre(resultSet.getString("tprp_nombre"));
				System.out.println("Filtrando tipos de reporte");
				System.out.println("Tipo de reporte filtrado: " + trep.getNombre());
				tipoReportes.add(trep);	
			}
			
			resultSet.close();
			consulta.close();
			conn.close();
			log.info("[consultarImagenes] Termino");
		} catch (Exception e) {
			log.error("[consultarImagenes] Fallo");
			e.printStackTrace();
		}

		return tipoReportes;
	}

	/** 
	 * Permite saber si un valor esta en un arreglo
	 * @param v
	 * @param a
	 * @return
	 */
	public static boolean inArray(String v, String[] a) {
		if (a == null) return false;
		if (v == null) return false;
		
		for (int i=0; i<a.length; i++) {
			if (a[i] != null) {
				if (v.equals(a[i])) {
					return true;
				}
			}
		}
		
		return false;
	}

	
	public static ArrayList<DivisionTerritorio> consultarDivisionesTerritorio() { 
		Logger log = null;
		Connection conn = ConexionBD.establecerConexion();

		ArrayList<DivisionTerritorio> tipoReportes = new ArrayList<DivisionTerritorio>();
		
		String query = "select * from red_divisionterritorio";
		
		try {
			log = SMBC_Log.Log(ConsultaReportes.class);
			Statement consulta = conn.createStatement();
			ResultSet resultSet = consulta.executeQuery(query);

			while (resultSet.next()) {
				DivisionTerritorio trep = new DivisionTerritorio();
				trep.setId(resultSet.getInt("dvtr_consecutivo"));
				trep.setNombre(resultSet.getString("dvtr_nombre"));
				tipoReportes.add(trep);	
			}

			resultSet.close();
			consulta.close();
			conn.close();
			log.info("[consultarImagenes] Termino");
		} catch (Exception e) {
			log.error("[consultarImagenes] Fallo");
			e.printStackTrace();
		}

		return tipoReportes;
	}
	
	public static ArrayList<String> consultarNombresDivisionesTerritoriales(String id_reporte) { 
		Logger log = null;
		Connection conn = ConexionBD.establecerConexion();

		ArrayList<String> nombres = new ArrayList<String>();
		
		String query = "select distinct division_territorial from red_inforeportes where id_reporte=" + id_reporte + " order by division_territorial";
		
		try {
			log = SMBC_Log.Log(ConsultaReportes.class);
			Statement consulta = conn.createStatement();
			ResultSet resultSet = consulta.executeQuery(query);

			while (resultSet.next()) {
				nombres.add(resultSet.getString("division_territorial"));	
			}

			resultSet.close();
			consulta.close();
			conn.close();
			log.info("[consultarNombresDivisionesTerritoriales] Termino");
		} catch (Exception e) {
			log.error("[consultarNombresDivisionesTerritoriales] Fallo");
			e.printStackTrace();
		}

		return nombres;
	}
	
	/**
	 * Metodo consultarReportesGeoproceso
	 * 
	 * Retorna una lista de arreglo de los reportes encontrados según los parámetros de filtro
	 * 
	 * @param divisionTerritorio
	 * @param tipoReporte
	 * @return
	 */
	public static ArrayList<Reporte> consultarReportesGeoproceso(Integer divisionTerritorio, Integer tipoReporte, String nombreDivisionTerritorial) {
		Connection conn = ConexionBD.establecerConexion();

		ArrayList<Reporte> reportes = new ArrayList<Reporte>();
		try {
			CallableStatement stmt_consultaReportes = conn.prepareCall("{call RED_PK_REPORTES.ConsultarReporte(?,?,?,?,?,?,?,?,?,?)}");
			stmt_consultaReportes.setNull("un_reporte", OracleTypes.NULL);
			stmt_consultaReportes.setInt("un_tipoReporte", tipoReporte);
			stmt_consultaReportes.setInt("una_division", divisionTerritorio);
			stmt_consultaReportes.setNull("un_periodouno", OracleTypes.NULL);
			stmt_consultaReportes.setNull("un_periododos", OracleTypes.NULL);
			stmt_consultaReportes.setInt("un_publicado", 1);
			stmt_consultaReportes.setString("una_identimagen", "");
			stmt_consultaReportes.setString("un_nombreDivT", nombreDivisionTerritorial);
			stmt_consultaReportes.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			stmt_consultaReportes.registerOutParameter("una_sentencia", OracleTypes.VARCHAR);
			stmt_consultaReportes.execute();
			
			ResultSet resultSet = (ResultSet) stmt_consultaReportes.getObject("un_Resultado");
			while (resultSet.next()) {
				Reporte reporte = new Reporte();
				reporte.setId(Integer.valueOf(resultSet.getObject("RPRT_CONSECUTIVO").toString()));
				reporte.setFechaGeneracion(resultSet.getDate("MAX_RPRT_FECHAGENERACION"));
				reporte.setIdDivision(resultSet.getInt("RPRT_DIVISIONTERRITORIO"));
				reporte.setPeriodoUno(resultSet.getInt("RPRT_PERIODOUNO"));
				reporte.setPeriodoDos(resultSet.getInt("RPRT_PERIODODOS"));
				reporte.setIdTipoReporte(resultSet.getInt("RPRT_TIPOREPORTE"));
				reporte.setIdentImagen(resultSet.getString("RPRT_IDENTIMAGEN"));
				reportes.add(reporte);
			}
			
			String sql = stmt_consultaReportes.getString("una_sentencia");
			System.out.println(sql);
			
			resultSet.close();
			stmt_consultaReportes.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return reportes;
	}

	/**
	 * Metodo consultarReportesGeoproceso Admin
	 * 
	 * Retorna una lista de arreglo de los reportes encontrados según los parámetros de filtro
	 * 
	 * @param divisionTerritorio
	 * @param tipoReporte
	 * @return
	 */
	public static ArrayList<Reporte> consultarReportesGeoprocesoAdmin(
			Integer id_reporte, 
			Integer tipoReporte, 
			Integer divisionTerritorio, 
			String nombreDivisionTerritorial, 
			String fechaInicial, 
			String fechaFinal, 
			String identImagen, 
			Integer periodoUno, 
			Integer periodoDos, 
			Integer publicado) {
		Connection conn = ConexionBD.establecerConexion();
		SimpleDateFormat isodateformat = new SimpleDateFormat("yyyy-MM-dd");
		
		ArrayList<Reporte> reportes = new ArrayList<Reporte>();
		try {
			CallableStatement stmt_consultaReportes = conn.prepareCall("{call RED_PK_REPORTES.ConsultarReporteAdmin(?,?,?,?,?,?,?,?,?,?,?,?)}");
			if (id_reporte != null) stmt_consultaReportes.setInt("un_reporte", id_reporte); else stmt_consultaReportes.setNull("un_reporte", OracleTypes.NULL);
			if (tipoReporte != null) stmt_consultaReportes.setInt("un_tipoReporte", tipoReporte); else stmt_consultaReportes.setNull("un_tipoReporte", OracleTypes.NULL); 
			if (divisionTerritorio != null) stmt_consultaReportes.setInt("una_division", divisionTerritorio); else  stmt_consultaReportes.setNull("una_division", OracleTypes.NULL);
			if (nombreDivisionTerritorial != null) stmt_consultaReportes.setString("un_nombreDivT", nombreDivisionTerritorial); else stmt_consultaReportes.setNull("un_nombreDivT", OracleTypes.NULL);
			
			if (fechaInicial != null) {
				stmt_consultaReportes.setDate("un_fechaInicial", new java.sql.Date(isodateformat.parse(fechaInicial).getTime())); 
			} 
			else {
				stmt_consultaReportes.setNull("un_fechaInicial", OracleTypes.NULL);
			}
			
			if (fechaFinal != null) {
				stmt_consultaReportes.setDate("un_fechaFinal", new java.sql.Date(isodateformat.parse(fechaFinal).getTime()));
			}
			else {
				stmt_consultaReportes.setNull("un_fechaFinal", OracleTypes.NULL);
			}
			
			if (identImagen != null) stmt_consultaReportes.setString("una_identimagen", identImagen); else stmt_consultaReportes.setNull("una_identimagen", OracleTypes.NULL);
			if (periodoUno != null) stmt_consultaReportes.setInt("un_periodouno", periodoUno); else stmt_consultaReportes.setNull("un_periodouno", OracleTypes.NULL);
			if (periodoDos != null) stmt_consultaReportes.setInt("un_periododos", periodoDos); else stmt_consultaReportes.setNull("un_periododos", OracleTypes.NULL);
			if (publicado != null) stmt_consultaReportes.setInt("un_publicado", publicado); else stmt_consultaReportes.setNull("un_publicado", OracleTypes.NULL);
			stmt_consultaReportes.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			stmt_consultaReportes.registerOutParameter("una_sentencia", OracleTypes.VARCHAR);
			stmt_consultaReportes.execute();
			
			ResultSet resultSet = (ResultSet) stmt_consultaReportes.getObject("un_Resultado");
			while (resultSet.next()) {
				Reporte reporte = new Reporte();
				reporte.setId(resultSet.getInt("RPRT_CONSECUTIVO"));	
				reporte.setFechaGeneracion(resultSet.getDate("RPRT_FECHAGENERACION"));
				reporte.setIdDivision(resultSet.getInt("RPRT_DIVISIONTERRITORIO"));
				reporte.setPeriodoUno(resultSet.getInt("RPRT_PERIODOUNO"));
				reporte.setPeriodoDos(resultSet.getInt("RPRT_PERIODODOS"));
				reporte.setIdTipoReporte(resultSet.getInt("RPRT_TIPOREPORTE"));
				reporte.setIdentImagen(resultSet.getString("RPRT_IDENTIMAGEN"));
				reporte.setPublicado(resultSet.getBoolean("RPRT_PUBLICADO"));
				reportes.add(reporte);
			}
			
			String sql = stmt_consultaReportes.getString("una_sentencia");
			System.out.println(sql);
			
			resultSet.close();
			stmt_consultaReportes.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return reportes;
	}

	
	/**
	 * Metodo consultarDivisionesTerritoriales
	 * 
	 * Retorna una lista de arreglo de las divisiones territoriales para un tipo de division territorial y un tipo de reporte
	 * 
	 * @param divisionTerritorio
	 * @param tipoReporte
	 * @return
	 */
	public static ArrayList<String> consultarDivisionesTerritoriales(Integer divisionTerritorio, Integer tipoReporte) {
		Connection conn = ConexionBD.establecerConexion();
		
		ArrayList<String> nombresDivisionesTerritoriales = new ArrayList<String>();
		try {
			CallableStatement stmt = conn.prepareCall("{call RED_PK_REPORTES.divisionesTerritoriales(?,?,?,?)}");
			stmt.setInt("un_tipoReporte", tipoReporte);
			stmt.setInt("una_division", divisionTerritorio);
			stmt.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			stmt.registerOutParameter("una_sentencia", OracleTypes.VARCHAR);
			stmt.execute();
			
			ResultSet resultSet = (ResultSet) stmt.getObject("un_Resultado");
			while (resultSet.next()) {
				String nombreDivisionTerritorial = resultSet.getString("NOMBRE");
				nombresDivisionesTerritoriales.add(nombreDivisionTerritorial);
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
		
		return nombresDivisionesTerritoriales;
	}
	
	
	public static ArrayList<AgregadoReporte> consolidarReporte(String id_reporte) {
		Connection conn = ConexionBD.establecerConexion();
		ArrayList<AgregadoReporte> agregadosReporte = new ArrayList<AgregadoReporte>();
		try {
			CallableStatement stmt = conn.prepareCall("{call RED_PK_REPORTES.consolidarReporte(?,?,?)}");
			stmt.setInt("id_reporte", Integer.parseInt(id_reporte));
			stmt.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			stmt.registerOutParameter("una_sentencia", OracleTypes.VARCHAR);
			stmt.execute();
			
			ResultSet resultSet = (ResultSet) stmt.getObject("un_Resultado");
			while (resultSet.next()) {
				AgregadoReporte agregadoReporte = new AgregadoReporte();
				agregadoReporte.setId_reporte(Integer.parseInt(id_reporte));
				agregadoReporte.setClasificacion(resultSet.getString("CLASIFICACION"));
				agregadoReporte.setDivisionTerritorial(resultSet.getString("DIVISION_TERRITORIAL"));
				agregadoReporte.setArea(resultSet.getDouble("SUMA_AREA"));
				agregadoReporte.setIncertidumbre(resultSet.getDouble("PROM_INCERTIDUMBRE"));
				agregadoReporte.setError(resultSet.getDouble("PROM_ERROR"));
				agregadoReporte.setBaj(resultSet.getDouble("SUMA_BAJ"));
				agregadoReporte.setBat(resultSet.getDouble("SUMA_BAT"));
				agregadoReporte.setCaj(resultSet.getDouble("SUMA_CAJ"));
				agregadoReporte.setCat(resultSet.getDouble("SUMA_CAT"));
				agregadoReporte.setCoe(resultSet.getDouble("SUMA_COE"));
				agregadoReporte.setCoet(resultSet.getDouble("SUMA_COET"));
				agregadosReporte.add(agregadoReporte);
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
		return agregadosReporte;
	}
	
	/**
	 * Método idTipoReporte
	 * 
	 * devuelve el id del tipo de reporte para un reporte dado
	 * 
	 * @param id_reporte
	 * @return
	 */
	public static String idTipoReporte(String id_reporte) {
		String r = "";

		r = ConexionBD.obtenerDato("SELECT RPRT_TIPOREPORTE FROM RED_REPORTES WHERE RPRT_CONSECUTIVO=" + id_reporte, "", null);
		
		return r;
	}

	/**
	 * Método idDivisionTerritorial
	 * 
	 * devuelve el id del tipo de división territorial de un reporte
	 * 
	 * @param id_reporte
	 * @return
	 */
	public static String idDivisionTerritorial(String id_reporte) {
		String r = "";
		
		r = ConexionBD.obtenerDato("SELECT RPRT_DIVISIONTERRITORIO FROM RED_REPORTES WHERE RPRT_CONSECUTIVO=" + id_reporte, "", null);
		
		return r;
	}
	
	/**
	 * Método identImagen
	 * 
	 * devuelve el codigo de la imagen WMS
	 * 
	 * @param id_reporte
	 * @return
	 */
	public static String identImagen(String id_reporte) {
		String r = "";
		
		r = ConexionBD.obtenerDato("SELECT RPRT_IDENTIMAGEN FROM RED_REPORTES WHERE RPRT_CONSECUTIVO=" + id_reporte, "", null);
		
		return r;
	}

	/**
	 * Método periodos
	 * 
	 * devuelve un string con la etiqueta de los periodos del reporte
	 * 
	 * @param id_reporte
	 * @return
	 */
	public static String periodos(String id_reporte) {
		String r = "";
		
		String p1 = ConexionBD.obtenerDato("SELECT RPRT_PERIODOUNO FROM RED_REPORTES WHERE RPRT_CONSECUTIVO=" + id_reporte, "", null);
		String p2 = ConexionBD.obtenerDato("SELECT RPRT_PERIODODOS FROM RED_REPORTES WHERE RPRT_CONSECUTIVO=" + id_reporte, "", null);
		
		r = p1;
		
		if (Auxiliar.tieneAlgo(p2)) {
			r += "-" + p2;
		}
		
		return r;
	}
	
	/**
	 * Método nombreTipoReporte
	 * 
	 * devuelve el nombre del tipo de reporte
	 * 
	 * @param id_reporte
	 * @param idioma
	 * @return
	 */
	public static String nombreTipoReporte(String id, String idioma) {
		String r = "";
		
		String nombre_columna = "TPRP_NOMBRE";
		
		if (idioma.equals("en")) {
			nombre_columna += "_EN";
		}
		
		r = ConexionBD.obtenerDato("SELECT "+nombre_columna+" FROM RED_TIPOREPORTE WHERE TPRP_CONSECUTIVO=" + id, "", null);
		
		return r;
	}

	/**
	 * Método nombreDivisionTerritorial
	 * 
	 * devuelve el nombre de la división territorial
	 * 
	 * @param id
	 * @param idioma
	 * @return
	 */
	public static String nombreDivisionTerritorial(String id, String idioma) {
		String r = "";
		
		if (id.equals("4")) {
			if (idioma.equals("es")) {
				return "Consolidado Nacional";
			}
			else {
				return "Consolidated National";
			}
		}
		
		String nombre_columna = "DVTR_NOMBRE";
		
		if (idioma.equals("en")) {
			nombre_columna += "_EN";
		}
		
		r = ConexionBD.obtenerDato("SELECT "+nombre_columna+" FROM RED_DIVISIONTERRITORIO WHERE DVTR_CONSECUTIVO=" + id, "", null);
		
		return r;
	}
	
}
