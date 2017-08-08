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
 * Clase que representa la lita de tipos de bosque 
 * que existe entre los individuos
 * @author Daniel Rodríguez Cárdenas
 *
 */
public class TipoBosqueDAO {

	private DataSource dataSource = (DataSource) ConexionBD.getConnection();

	Connection connection;
	PreparedStatement statement;
	ResultSet result;
	CallableStatement consultaTipoBosque;
	
	

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
	 * Listado en HTML para combobox de los tipos de bosque
	 * @return
	 */
	
	public  String getTipoBosque() {
		String tipoBosque = "";
		try {
			
			connection = dataSource.getConnection();
			consultaTipoBosque = connection
					.prepareCall("{call RED_PK_TABLASTIPO.TipoBosque_Consulta(?, ?)}");
			consultaTipoBosque.setString("un_Nombre", "");
			consultaTipoBosque.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTipoBosque.execute();
			result = (ResultSet) consultaTipoBosque
					.getObject("un_Resultado");
			while (result.next()) {
				tipoBosque += "<option value=\"" + result.getObject(1)
						+ "\">"+ result.getObject(3)+ " - "+ result.getObject(2) + "</option>\n";
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			closeConection();
		}
		return tipoBosque;
	}

	

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
			consultaTipoBosque.close();
		} catch (Exception e) {
		}
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}



}
