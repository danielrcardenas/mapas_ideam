// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.admin.entities.Depto;
import co.gov.ideamredd.admin.entities.Municipios;
import co.gov.ideamredd.admin.entities.Pais;
import co.gov.ideamredd.mbc.conexion.ConexionBD;

/**
 * Clase que carga datos iniciales de los formularios.
 */
public class ConsultarAsociadosUsuarios {

	private static Pais pais = new Pais();
	private static Depto depto = new Depto();
	private static Municipios municipios = new Municipios();
	private static DataSource dataSource = ConexionBD.getConnection();

	private static Connection conn;

	public static Depto ConsultarDeptoPersona(Integer idDepto) {

		try {
			conn = dataSource.getConnection();
			CallableStatement consultarDeptoPersona = conn
					.prepareCall("{call RED_PK_USUARIOS.DeptoPersona_Consulta(?,?)}");
			consultarDeptoPersona.setInt("un_depto", idDepto);
			consultarDeptoPersona.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarDeptoPersona.execute();
			ResultSet resultSet = (ResultSet) consultarDeptoPersona
					.getObject("un_Resultado");

			while (resultSet.next()) {
				depto.setConsecutivo(Integer.valueOf(resultSet.getObject(1).toString()));
				depto.setNombre((String) resultSet.getObject(2));
			}
			resultSet.close();
			consultarDeptoPersona.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return depto;
	}

	public static Municipios ConsultarMunicipio(Integer idMunicipio) {

		try {
			conn = dataSource.getConnection();
			CallableStatement consultarMunicipioPersona = conn
					.prepareCall("{call RED_PK_USUARIOS.MunicipioPersona_Consulta(?,?)}");
			consultarMunicipioPersona.setInt("un_municipio", idMunicipio);
			consultarMunicipioPersona.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarMunicipioPersona.execute();
			ResultSet resultSet = (ResultSet) consultarMunicipioPersona
					.getObject("un_Resultado");

			while (resultSet.next()) {
				municipios.setConsecutivo(Integer.valueOf(resultSet.getObject(1).toString()));
				municipios.setNombre((String) resultSet.getObject(2));
			}
			resultSet.close();
			consultarMunicipioPersona.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return municipios;
	}
	

	public static Pais ConsultarPaisPersona(
			Integer idPais) {

		try {
			conn = dataSource.getConnection();
			CallableStatement consultarPaisPersona = conn
					.prepareCall("{call RED_PK_USUARIOS.PaisPersona_Consulta(?,?)}");
			consultarPaisPersona.setInt("un_Pais", idPais!=null?idPais:-1);
			consultarPaisPersona.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPaisPersona.execute();
			ResultSet resultSet = (ResultSet) consultarPaisPersona
					.getObject("un_Resultado");

			while (resultSet.next()) {
				pais.setConsecutivo(Integer.valueOf(resultSet.getObject(1).toString()));
				pais.setNombre((String) resultSet.getObject(2));
			}
			resultSet.close();
			consultarPaisPersona.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pais;
	}
	
	public static String getTipoDocumento(Integer idTipo) {
		String tipoDocumento = "";
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaTipoDocumento = conn
					.prepareCall("{call RED_PK_TABLASTIPO.TipoIdentificacion_IdConsulta(?, ?)}");
			consultaTipoDocumento.setInt("un_Tipo", idTipo);
			consultaTipoDocumento.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTipoDocumento.execute();
			ResultSet r = (ResultSet) consultaTipoDocumento
					.getObject("un_Resultado");
			r.next();
			tipoDocumento = (String) r.getObject(2);
			r.close();
			consultaTipoDocumento.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tipoDocumento;
	}
	
	public static String getTipoPersona(Integer idTipo) {
		String tipoPersona = "";
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaTipoPersona = conn
					.prepareCall("{call RED_PK_TABLASTIPO.TipoPersona_idConsulta(?, ?)}");
			consultaTipoPersona.setInt("un_Tipo", idTipo);
			consultaTipoPersona.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTipoPersona.execute();
			ResultSet r = (ResultSet) consultaTipoPersona
					.getObject("un_Resultado");
			r.next();
			tipoPersona = (String) r.getObject(2);
			r.close();
			consultaTipoPersona.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tipoPersona;
	}

}
