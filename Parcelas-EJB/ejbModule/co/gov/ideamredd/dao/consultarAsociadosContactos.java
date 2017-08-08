package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBDParcelas;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Pais;

@Stateless
public class consultarAsociadosContactos {

	@EJB
	private ConexionBDParcelas conexionBD;
	private Connection conn;
	private Pais pais = new Pais();
	private Depto depto = new Depto();
	private Municipios municipios = new Municipios();

	public Depto ConsultarDepto(Integer idDepto) {
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement consultarDeptoPersona = conn
					.prepareCall("{call RED_PK_USUARIOS.DeptoPersona_Consulta(?,?)}");
			consultarDeptoPersona.setInt("un_depto", idDepto);
			consultarDeptoPersona.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarDeptoPersona.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarDeptoPersona
					.getObject("un_Resultado");
			while (resultSet.next()) {
				depto.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
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

	public Municipios ConsultarMunicipio(Integer idMunicipio) {
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement consultarMunicipioPersona = conn
					.prepareCall("{call RED_PK_USUARIOS.MunicipioPersona_Consulta(?,?)}");
			consultarMunicipioPersona.setInt("un_municipio", idMunicipio);
			consultarMunicipioPersona.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarMunicipioPersona.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarMunicipioPersona
					.getObject("un_Resultado");
			while (resultSet.next()) {
				municipios.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
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

	public Pais ConsultarPais(Integer idPais) {
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement consultarPaisPersona = conn
					.prepareCall("{call RED_PK_USUARIOS.PaisPersona_Consulta(?,?)}");
			consultarPaisPersona
					.setInt("un_Pais", idPais != null ? idPais : -1);
			consultarPaisPersona.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPaisPersona.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarPaisPersona
					.getObject("un_Resultado");
			while (resultSet.next()) {
				pais.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
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

}
