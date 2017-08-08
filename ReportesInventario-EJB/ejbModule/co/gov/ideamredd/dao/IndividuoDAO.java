package co.gov.ideamredd.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.InformacionReporteInventarios;

/**
 * Clase que representa a un individuo o tallo
 * para calculo de la biomasa desde la base de datos
 * @author Daniel Rodríguez Cárdenas
 * 
 *
 */
public class IndividuoDAO {

	private DataSource dataSource = (DataSource) ConexionBD.getConnection();

	Connection connection;
	PreparedStatement statement;
	ResultSet result;

	/**
	 * Este metodo permite sugerir al usuario el nombre de la especie ingresando
	 * el nombre del departamento, tipo de bosques, parcela o parte del nombre
	 * de la especie. Los parametros no deben estar en null pero si se aceptan
	 * vacios
	 * 
	 * @param parametros
	 *            se reciben parametros en el orden 1. especie 2. parcela 3.
	 *            departamento y 4. tipode Bosque los dos ultimos corresponden
	 *            al ID
	 * @return Listado de especie sugeridas de acuerdo a los par�metros.
	 */
	/**
	 * @param parametros
	 * @return
	 */
	public List<String> consultarEspecies(ArrayList<String> parametros) {

		String especie = "";
		String parcela = "";
		String departamento = "";
		String tipoBosque = "";

		try {
			especie = parametros.get(0);
			parcela = parametros.get(1);
			departamento = parametros.get(2);
			tipoBosque = parametros.get(3);
			String[] parcelas = parcela.split(",");

			if (!especie.isEmpty()) {
				ArrayList<String> nParametros = new ArrayList<String>();
				nParametros.add("%" + especie + "%");

				String consulta = "SELECT * FROM (SELECT DISTINCT I.INDV_ESPECIE FROM VRED_INDIVIDUO I, RED_PARCELA P WHERE UPPER(INDV_ESPECIE) LIKE UPPER(?)"
						+ " AND  P.PRCL_CONSECUTIVO = I.INDV_PRCL_CONSECUTIVO";
				if (!departamento.isEmpty()) {

					nParametros.add(departamento);
					consulta += " AND P.PRCL_DEPARTAMENTO= (?)";

				}
				if (!tipoBosque.isEmpty()) {

					nParametros.add(tipoBosque);
					consulta += " AND P.PRCL_TIPOBOSQUE= (?)";

				}

				if (!parcela.isEmpty()) {

					for (String par : parcelas) {
						nParametros.add(par);
					}

					consulta += " AND UPPER(P.PRCL_NOMBRE) IN ("
							+ crearClausulaIn(parcelas) + ")";
				}
				consulta += " ORDER BY I.INDV_ESPECIE) WHERE ROWNUM <=10";
				return consultarEspecies(consulta, nParametros);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		return null;

	}

	/**
	 * Consulta la lista de especies asociados con los individuos
	 * @param consulta Parámetros de la consulta SQL
	 * @param parametros parámetros de la sentencia
	 * @return Lista de nombres de especies
	 */
	public List<String> consultarEspecies(String consulta,
			ArrayList<String> parametros) {
		List<String> listado = new ArrayList<String>();
		try {
			connection = dataSource.getConnection();

			statement = connection.prepareStatement(consulta);
			for (int i = 0; i < parametros.size(); i++) {
				statement.setString(i + 1, parametros.get(i));
			}

			result = statement.executeQuery();

			while (result.next()) {
				listado.add(result.getString(1));
			}

		} catch (Exception e) {
			return Collections.emptyList();
		} finally {
			closeConection();
		}

		return listado;
	}
	
	/**
	 * Lista con la información de los individuos luego de obtener el resultado
	 * de la consulta.
	 * @param re resultado desde la BD
	 * @return Lista de individuos con sus propiedades
	 */

	public List<InformacionReporteInventarios> translateResult(ResultSet re) {
		List<InformacionReporteInventarios> listado = new ArrayList<InformacionReporteInventarios>();

		try {

			while (result.next()) {
				String numeroArbol = result.getString("INDV_NUMERO_ARBOL");
				String especie = result.getString("INDV_ESPECIE");
				String genero = result.getString("INDV_GENERO");
				String familia = result.getString("INDV_FAMILIA");
				BigDecimal biomasa = new BigDecimal(
						result.getFloat("INDV_BIOMASA_AEREA"));
				BigDecimal carbono = new BigDecimal(
						result.getFloat("INDV_CARBONO"));
				BigDecimal dap = new BigDecimal(result.getFloat("INDV_DAP"));
				BigDecimal densidad = new BigDecimal(
						result.getFloat("INDV_DENSIDAD"));
				BigDecimal areaBasal = calcularAreaBasal(dap, densidad);
				InformacionReporteInventarios reporte = new InformacionReporteInventarios(
						numeroArbol, genero, especie, familia, biomasa,
						carbono, areaBasal, BigDecimal.ZERO);
				reporte.setDAP(dap);
				listado.add(reporte);
			}
			return listado;
		} catch (Exception e) {
		}
		return Collections.emptyList();
	}

	/**
	 * Consulta todos los indiciduos para generar el reportes de estructura y composición
	 * La consulta realiza el calculo del DAP= (DAP individuo 1 + DAP indivduo 2)/2
	 * dentro de un rango de fechas, un departamento y tipo de bosque
	 * @param filtro comprende la lista de propiedades del individuo o conjunto a buscar
	 * @param save si es verdadero entonces convierte el resultado a archivo en formato CSV
	 * en la ruta donde se encuentra el SCRIPT de R
	 * @return Lista de individuos que conforman el inventario
	 */
	public List<InformacionReporteInventarios> consultarReporteEstructuraComposicion(
			InformacionReporteInventarios filtro, boolean save) {

		int depto = filtro.getDepartamento();
		int tBosque = filtro.getTipoBosque();
		List<InformacionReporteInventarios> listado;
		try {

			connection = dataSource.getConnection();
			String consulta = "SELECT "
					+ "I.INDV_NUMERO_ARBOL, "
					+ "I.INDV_ESPECIE, "
					+ "I.INDV_GENERO, "
					+ "I.INDV_FAMILIA, "
					+ "I.INDV_BIOMASA_AEREA, "
					+ "I.INDV_CARBONO, "
					+ "((I.INDV_TAYO_DAP1 + I.INDV_TAYO_DAP2) / 2) as INDV_DAP, "
					+ "I.INDV_DENSIDAD "
					+ "FROM VRED_INDIVIDUO I, RED_PARCELA P "
					+ "WHERE " + "I.INDV_ACTUALIZACION BETWEEN ? AND ? "
					+ "AND  P.PRCL_CONSECUTIVO = I.INDV_PRCL_CONSECUTIVO "; // 3
																			// fecha
			// inicial ;
			// 4 fecha
			// final

			if (depto > -1)
				consulta += "AND P.PRCL_DEPARTAMENTO= ? ";
			if (tBosque > -1)
				consulta += "AND P.PRCL_TIPOBOSQUE= ? ";

			int index = 1;
			statement = connection.prepareStatement(consulta);
			statement.setDate(index++, filtro.getFechaInicialFiltro());
			statement.setDate(index++, filtro.getFechaFinalFiltro());
			if (depto > -1)
				statement.setInt(index++, depto);
			if (tBosque > -1)
				statement.setInt(index++, tBosque);
			result = statement.executeQuery();
			listado = translateResult(result);
			if (save) {
				result = statement.executeQuery();
				convertToCsv(result, filtro.getrScriptPath(),
						filtro.getResultName());
			}

		} catch (Exception e) {
			return null;
		} finally {
			closeConection();
		}

		return listado;
	}

	/**
	 * Método para convertir los objetos resultado de la consulta en un archivo en formato CSV
	 * @param listado Lista de individuos
	 * @param path Ruta de almacenamiento del archivo
	 * @param name Nombre del archivo
	 * @throws FileNotFoundException No se puede generar el archivo
	 */
	public void convertToCsv(List<InformacionReporteInventarios> listado,
			String path, String name) throws FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter(new File(path + File.separator
				+ name));
		String dataHeaders = "INDV_NUMERO_ARBOL;INDV_ESPECIE;INDV_GENERO;INDV_FAMILIA;INDV_BIOMASA_AEREA;INDV_CARBONO;INDV_DAP";
		csvWriter.println(dataHeaders);
		for (InformacionReporteInventarios reporte : listado) {
			String row = reporte.getNumeroArbol() + ";" + reporte.getEspecie()
					+ ";" + reporte.getGenero() + ";" + reporte.getFamilia()
					+ ";" + reporte.getBiomasa().toPlainString() + ";"
					+ reporte.getCarbono().toPlainString() + ";"
					+ reporte.getDAP().toPlainString();
			csvWriter.println(row);

		}
		csvWriter.close();
	}

	/**
	 * Metodo para convertir el resulset a CSV
	 * @param rs
	 * @param path
	 * @param name
	 * @throws SQLException
	 * @throws FileNotFoundException
	 */
	public void convertToCsv(ResultSet rs, String path, String name)
			throws SQLException, FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter(new File(path + File.separator
				+ name));
		ResultSetMetaData meta = rs.getMetaData();
		int numberOfColumns = meta.getColumnCount();
		String dataHeaders = meta.getColumnName(1);
		for (int i = 2; i < numberOfColumns + 1; i++) {
			dataHeaders += ";" + meta.getColumnName(i);
		}
		csvWriter.println(dataHeaders);
		while (rs.next()) {
			String row = rs.getString(1);
			for (int i = 2; i < numberOfColumns + 1; i++) {
				row += ";" + rs.getString(i);
			}
			csvWriter.println(row);
		}
		csvWriter.close();
	}

	/**
	 * Consulta el conjunto de individuos que pertenecen a una especie
	 * Calcula el DAP promediando el DAP de los tallos
	 * @param filtro Criterio o parámetros de consulta de los individuos
	 * @param save Si es verdadero almecena el resultado en archivo CSV
	 * @return Lista de objetos individuo
	 */
	public List<InformacionReporteInventarios> consultarEspecieReporte(
			InformacionReporteInventarios filtro, boolean save) {

		if (filtro.getParcela() != null && !filtro.getParcela().isEmpty()) {
			System.out.println("Filtro por parcela");
			return consultarEspecieReporteParcela(filtro, save);
		}

		List<InformacionReporteInventarios> listado = new ArrayList<InformacionReporteInventarios>();

		String[] especies = filtro.getEspecie().split(",");

		try {

			connection = dataSource.getConnection();
			String consulta = "SELECT " + "INDV_NUMERO_ARBOL, "
					+ "INDV_ESPECIE, " + "INDV_GENERO, " + "INDV_FAMILIA, "
					+ "INDV_BIOMASA_AEREA, " + "INDV_CARBONO, "
					+ "((INDV_TAYO_DAP1 + INDV_TAYO_DAP2) / 2) as INDV_DAP, "
					+ "INDV_DENSIDAD " + "FROM VRED_INDIVIDUO " + "WHERE "
					+ "UPPER(INDV_ESPECIE) IN (" + crearClausulaIn(especies)
					+ ") "// 1 especie
					+ "AND INDV_ACTUALIZACION BETWEEN ? AND ?"; // 2 fecha
																// inicial ; 3
																// fecha final
			//System.out.println(consulta);
			statement = connection.prepareStatement(consulta);
			int index = 1;
			for (String especie : especies) {
				statement.setString(index++, especie.toUpperCase());
			}
			statement.setDate(index++, filtro.getFechaInicialFiltro());
			statement.setDate(index, filtro.getFechaFinalFiltro());

			result = statement.executeQuery();
			String executedQuery = result.getStatement().toString();
			System.out.println(executedQuery);
			while (result.next()) {
				String numeroArbol = result.getString("INDV_NUMERO_ARBOL");
				String especie = result.getString("INDV_ESPECIE");
				String genero = result.getString("INDV_GENERO");
				String familia = result.getString("INDV_FAMILIA");
				BigDecimal biomasa = new BigDecimal(
						result.getFloat("INDV_BIOMASA_AEREA"));
				BigDecimal carbono = new BigDecimal(
						result.getFloat("INDV_CARBONO"));
				BigDecimal dap = new BigDecimal(result.getFloat("INDV_DAP"));
				BigDecimal densidad = new BigDecimal(
						result.getFloat("INDV_DENSIDAD"));
				BigDecimal areaBasal = calcularAreaBasal(dap, densidad);

				listado.add(new InformacionReporteInventarios(numeroArbol,
						genero, especie, familia, biomasa, carbono, areaBasal,
						BigDecimal.ZERO));
			}
			if (save) {
				result = statement.executeQuery();
				convertToCsv(result, filtro.getrScriptPath(),
						filtro.getResultName());
			}

		} catch (Exception e) {
			return Collections.emptyList();
		} finally {
			closeConection();
		}

		return listado;
	}

	/**
	 * Consulta conjunto de individuos que pertenecen a una especie y una parcela
	 * @param filtro
	 * @param save
	 * @return
	 */
	public List<InformacionReporteInventarios> consultarEspecieReporteParcela(
			InformacionReporteInventarios filtro, boolean save) {

		List<InformacionReporteInventarios> listado = new ArrayList<InformacionReporteInventarios>();

		String[] especies = filtro.getEspecie().split(",");
		String[] parcelas = filtro.getParcela().split(",");

		try {

			connection = dataSource.getConnection();
			String consulta = "SELECT "
					+ "I.INDV_NUMERO_ARBOL, "
					+ "I.INDV_ESPECIE, "
					+ "I.INDV_GENERO, "
					+ "I.INDV_FAMILIA, "
					+ "I.INDV_BIOMASA_AEREA, "
					+ "I.INDV_CARBONO, "
					+ "((I.INDV_TAYO_DAP1 + I.INDV_TAYO_DAP2) / 2) as INDV_DAP, "
					+ "I.INDV_DENSIDAD "
					+ "FROM VRED_INDIVIDUO I, RED_PARCELA  P "
					+ "WHERE "
					+ "UPPER(I.INDV_ESPECIE) IN ("
					+ crearClausulaIn(especies)
					+ ") "// 1 especie
					+ "AND UPPER(P.PRCL_NOMBRE) IN ("
					+ crearClausulaIn(parcelas)
					+ ") "// 2 parcela
					+ "AND P.PRCL_PLOT=I.INDV_PRCL_PLOT "
					+ "AND I.INDV_ACTUALIZACION BETWEEN ? AND ?"; // 3 fecha
																	// inicial ;
																	// 4 fecha
																	// final

			statement = connection.prepareStatement(consulta);
			int index = 1;
			for (String especie : especies) {
				statement.setString(index++, especie.toUpperCase());
			}
			for (String parcela : parcelas) {
				statement.setString(index++, parcela.toUpperCase());
			}
			statement.setDate(index++, filtro.getFechaInicialFiltro());
			statement.setDate(index, filtro.getFechaFinalFiltro());

			result = statement.executeQuery();

			while (result.next()) {
				String numeroArbol = result.getString("INDV_NUMERO_ARBOL");
				String especie = result.getString("INDV_ESPECIE");
				String genero = result.getString("INDV_GENERO");
				String familia = result.getString("INDV_FAMILIA");
				BigDecimal biomasa = new BigDecimal(
						result.getFloat("INDV_BIOMASA_AEREA"));
				BigDecimal carbono = new BigDecimal(
						result.getFloat("INDV_CARBONO"));
				BigDecimal dap = new BigDecimal(result.getFloat("INDV_DAP"));
				BigDecimal densidad = new BigDecimal(
						result.getFloat("INDV_DENSIDAD"));
				BigDecimal areaBasal = calcularAreaBasal(dap, densidad);

				listado.add(new InformacionReporteInventarios(numeroArbol,
						genero, especie, familia, biomasa, carbono, areaBasal,
						BigDecimal.ZERO));
			}
			if (save) {
				result = statement.executeQuery();
				convertToCsv(result, filtro.getrScriptPath(),
						filtro.getResultName());
			}

		} catch (Exception e) {
			return Collections.emptyList();
		} finally {
			closeConection();
		}

		return listado;
	}

	public List<InformacionReporteInventarios> consultarEspeciePorParcela(
			InformacionReporteInventarios filtro, boolean save) {

		List<InformacionReporteInventarios> listado = new ArrayList<InformacionReporteInventarios>();

		String[] parcelas = filtro.getParcela().split(",");

		try {

			connection = dataSource.getConnection();
			String consulta = "SELECT "
					+ "I.INDV_NUMERO_ARBOL, "
					+ "I.INDV_ESPECIE, "
					+ "I.INDV_GENERO, "
					+ "I.INDV_FAMILIA, "
					+ "I.INDV_BIOMASA_AEREA, "
					+ "I.INDV_CARBONO, "
					+ "((I.INDV_TAYO_DAP1 + I.INDV_TAYO_DAP2) / 2) as INDV_DAP, "
					+ "I.INDV_DENSIDAD "
					+ "FROM VRED_INDIVIDUO I, RED_PARCELA  P "
					+ "WHERE " + "UPPER(P.PRCL_NOMBRE) IN ("
					+ crearClausulaIn(parcelas)
					+ ") "// 2 parcela
					+ "AND P.PRCL_PLOT=I.INDV_PRCL_PLOT "
					+ "AND I.INDV_ACTUALIZACION BETWEEN ? AND ?"; // 3 fecha
																	// inicial ;
																	// 4 fecha
																	// final

			statement = connection.prepareStatement(consulta);
			int index = 1;

			for (String parcela : parcelas) {
				statement.setString(index++, parcela.toUpperCase());
			}
			statement.setDate(index++, filtro.getFechaInicialFiltro());
			statement.setDate(index, filtro.getFechaFinalFiltro());

			result = statement.executeQuery();

			while (result.next()) {
				String numeroArbol = result.getString("INDV_NUMERO_ARBOL");
				String especie = result.getString("INDV_ESPECIE");
				String genero = result.getString("INDV_GENERO");
				String familia = result.getString("INDV_FAMILIA");
				BigDecimal biomasa = new BigDecimal(
						result.getFloat("INDV_BIOMASA_AEREA"));
				BigDecimal carbono = new BigDecimal(
						result.getFloat("INDV_CARBONO"));
				BigDecimal dap = new BigDecimal(result.getFloat("INDV_DAP"));
				BigDecimal densidad = new BigDecimal(
						result.getFloat("INDV_DENSIDAD"));
				BigDecimal areaBasal = calcularAreaBasal(dap, densidad);

				listado.add(new InformacionReporteInventarios(numeroArbol,
						genero, especie, familia, biomasa, carbono, areaBasal,
						BigDecimal.ZERO));
			}
			if (save) {
				result = statement.executeQuery();
				convertToCsv(result, filtro.getrScriptPath(),
						filtro.getResultName());
			}

		} catch (Exception e) {
			return Collections.emptyList();
		} finally {
			closeConection();
		}

		return listado;
	}
	
	/**
	 * Metodo para calcular el área basal del total de los individuos
	 * Math.sqrt(Math.PI * dap * dap) / densidad
	 * @param dapMedian
	 * @param densidadBD
	 * @return
	 */

	private BigDecimal calcularAreaBasal(BigDecimal dapMedian,
			BigDecimal densidadBD) {

		double dap = dapMedian.doubleValue();
		double densidad = densidadBD.doubleValue();
		double result = Math.sqrt(Math.PI * dap * dap) / densidad;

		return new BigDecimal(result);
	}

	private void closeConection() {
		try {
			result.close();
		} catch (Exception e) {
		}
		try {
			statement.close();
		} catch (Exception e) {
		}
		try {
			connection.close();
		} catch (Exception e) {
		}
	}

	private String crearClausulaIn(String[] especies) {
		String inClause = "";
		for (int i = 0; i < especies.length; i++) {
			inClause += "?,";
		}

		return inClause.substring(0, inClause.length() - 1);
	}

}
