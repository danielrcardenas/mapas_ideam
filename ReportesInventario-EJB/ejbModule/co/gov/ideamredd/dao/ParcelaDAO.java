package co.gov.ideamredd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import co.gov.ideamredd.conexionBD.ConexionBD;
/**
 * CLase que representa al conjunto de parcelas en la base de datos
 * para general el listado de parcelas asociadas a los individuos
 * @author Daniel Rodríguez Cárdenas
 *
 */
public class ParcelaDAO {

	private DataSource dataSource = (DataSource) ConexionBD.getConnection();

	Connection connection;
	PreparedStatement statement;
	ResultSet result;

	/**
	 * Método para realizar la consulta de acuerdo a los parámetros de busqueda
	 * @param parcela Nombre total o parcial de la parcela
	 * @param depto Nombre total o iniciales del departamento al que pertenece la pacela
	 * @param tbosque Tipo de bosque que se espera que contenga la parcela
	 * @return
	 */
	public List<String> consultarParcela(String parcela, String depto,
			String tbosque) {

		try {
			if (!parcela.isEmpty()) {
				String consulta = "SELECT * FROM (SELECT DISTINCT PRCL_NOMBRE FROM RED_PARCELA WHERE UPPER(PRCL_NOMBRE) LIKE UPPER(?)";
				List<String> parametros = new ArrayList<String>();
				parametros.add(parcela + "%");
				if (!depto.isEmpty()) {
					parametros.add(depto);
					consulta += " AND PRCL_DEPARTAMENTO= (?)";
				}
				if (!tbosque.isEmpty()) {
					parametros.add(tbosque);
					consulta += " AND PRCL_TIPOBOSQUE= (?)";
				}
				consulta += " ORDER BY PRCL_NOMBRE) WHERE ROWNUM<=10 ";
				return consultarParcelas(consulta, parametros);
			}

		} catch (Exception e) {
			return Collections.emptyList();
		}
		return Collections.emptyList();

	}
	/**
	 * Metodo genérico para filtrar las parcelas
	 * @param consulta
	 * @param parametros
	 * @return
	 */

	public List<String> consultarParcelas(String consulta,
			List<String> parametros) {
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

	private void closeConection() {
		try {
			result.close();
		} catch (SQLException e) {
		}
		try {
			statement.close();
		} catch (SQLException e) {
		}
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}

}
