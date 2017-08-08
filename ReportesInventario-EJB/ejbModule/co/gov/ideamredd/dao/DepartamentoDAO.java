package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;
/**
 * Clase que representa al objeto departamentos de Colombia 
 * al extraer la información desde la base de datos
 * @author Daniel Rodríguez Cárdenas
 *
 */
public class DepartamentoDAO {

	private DataSource dataSource = (DataSource) ConexionBD.getConnection();

	Connection connection;
	PreparedStatement statement;
	ResultSet result;
	CallableStatement consultaDepartamento;
	
	

	/**
	 * Obtiene una lista de parcelas 
	 * @param parcela nombre o parte del nombre de la parcela
	 * @return Listado de nombres de parcelas
	 */
	public List<String> consultarParcela(String parcela) {
		List<String> listado = new ArrayList<String>();

		try {
			connection = dataSource.getConnection();
			String consulta = "SELECT DISTINCT PRCL_NOMBRE FROM RED_PARCELA WHERE UPPER(PRCL_NOMBRE) LIKE UPPER(?)";
			statement = connection.prepareStatement(consulta);
			statement.setString(1,  parcela + "%");
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
	 * Obtiene un listado de departamentos en formato html para selección en un 
	 * combobox
	 * @return String html
	 */
	public  String getDepartamentos() {
		String departamentos = "";
		try {
			
			connection = dataSource.getConnection();
			consultaDepartamento = connection
					.prepareCall("{call RED_PK_TABLASTIPO.Departamento_Consulta(?, ?)}");
			consultaDepartamento.setString("un_Nombre", "");
			consultaDepartamento.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaDepartamento.execute();
			result = (ResultSet) consultaDepartamento
					.getObject("un_Resultado");
			while (result.next()) {
				departamentos += "<option value=\"" + result.getObject(1)
						+ "\">" + result.getString(2) + "</option>\n";
			}
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		finally {
			closeConection();
		}
		return departamentos;
	}


	/**
	 * Cierra conexión a la base de datos
	 */
	private void closeConection() {
		try {
			result.close();
		} catch (SQLException e) {
		}
		try {
			statement.close();
		} catch (Exception e) {
		}
		try {
			consultaDepartamento.close();
		} catch (Exception e) {
		}
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}



}
