package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.Individuo;
import co.gov.ideamredd.entities.TipoBosque;

@Stateless
public class ConsultarAsociadosParcela {

	@EJB
	private ConexionBD conexionBD;

	private Connection conn;
	private ArrayList<TipoBosque> bosque;

	public ArrayList<TipoBosque> consultaTipoBosqueParcela(Integer idParcela) {
		try {
			// log.info("Inicio de la consulta de tipos de bosque para la parcela id:"
			// + idParcela);
			conn = conexionBD.establecerConexion();
			bosque = new ArrayList<TipoBosque>();
			CallableStatement consultarBosqueParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.BosqueParcela_Consulta(?,?)}");
			consultarBosqueParcela.setInt("una_parcela", idParcela);
			consultarBosqueParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarBosqueParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarBosqueParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				TipoBosque tipoBosque = new TipoBosque();
				tipoBosque.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				tipoBosque.setTipoBosque((String) resultSet.getObject(2));
				tipoBosque.setIdParcela(Integer.valueOf(resultSet.getObject(3)
						.toString()));
				tipoBosque.setPrecipitacion((String) resultSet.getObject(4));
				tipoBosque.setTemperatura((String) resultSet.getObject(5));
				tipoBosque.setAltitud((String) resultSet.getObject(6));
				tipoBosque.setIdBosque(Integer.valueOf(resultSet.getObject(7)
						.toString()));
				bosque.add(tipoBosque);
			}
			resultSet.close();
			consultarBosqueParcela.close();
			conn.close();
			// log.info("consulta exitosa de los tipos de bosque para la parcela id:"
			// + idParcela);
		} catch (Exception e) {
			// log.error("Error en la consulta de los tipos de bosque de la parcela id:"
			// + idParcela);
			e.printStackTrace();
		}
		return bosque;
	}
	
	public ArrayList<Individuo> ConsultarIndividuoParcela(
			String idParcela) {
		ArrayList<Individuo> individuos = new ArrayList<Individuo>();
		try {
			CallableStatement consultarIndividuoParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.consultarIndividuoParcela(?,?)}");
			consultarIndividuoParcela.setString("una_parcela", idParcela);
			consultarIndividuoParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarIndividuoParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarIndividuoParcela
					.getObject("un_Resultado");
			while (resultSet.next()) {
				if (resultSet.getObject(10) != null) {
					Individuo i = new Individuo();
					i.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
							.toString()));
					i.setDap1(Double
							.valueOf(resultSet.getObject(10).toString()));
					i.setEspecie(resultSet.getObject(17) != null ? resultSet
							.getObject(17).toString() : "NO DATA");
					individuos.add(i);
				}
			}
			resultSet.close();
			consultarIndividuoParcela.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return individuos;
	}
	
	public Double ConsultarAreaParcela(Integer idParcela) {
		Double area = new Double(0);
		try {
			CallableStatement consultarAreaParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.ConsultarAreaParcela(?,?)}");
			consultarAreaParcela.setInt("un_consecutivo", idParcela);
			consultarAreaParcela.registerOutParameter("un_Resultado", OracleTypes.NUMBER);
			consultarAreaParcela.execute();
			area = consultarAreaParcela.getDouble("un_Resultado");
			if (area == 0)
				area = 1.0;
			consultarAreaParcela.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return area;
	}

}
