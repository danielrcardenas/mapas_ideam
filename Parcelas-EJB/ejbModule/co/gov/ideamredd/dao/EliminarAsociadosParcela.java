package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import co.gov.ideamredd.conexionBD.ConexionBDParcelas;

import oracle.jdbc.OracleTypes;

@Stateless
public class EliminarAsociadosParcela {

	@EJB
	private ConexionBDParcelas conexionBD;

	private Connection conn;

	public void eliminarAsociados(Integer idParcela) {
		try {
			eliminaBosqueParcela(idParcela);
			eliminaDeptoParcela(idParcela);
			eliminaMunicipioParcela(idParcela);
			eliminaCarParcela(idParcela);
			eliminaFisiografiaParcela(idParcela);
			eliminaProyectoParcela(idParcela);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void eliminaBosqueParcela(Integer idParcela) throws SQLException {
		conn = conexionBD.establecerConexion();
		CallableStatement eliminaBosqueParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.Elimina_TipoBosque_Parcela(?,?)}");
		eliminaBosqueParcela.setInt("una_parcela", idParcela);
		eliminaBosqueParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		eliminaBosqueParcela.execute();
		System.out.println(eliminaBosqueParcela.getObject("un_Resultado"));
		eliminaBosqueParcela.close();
		conn.close();
	}

	private void eliminaDeptoParcela(Integer idParcela) throws SQLException {
		conn = conexionBD.establecerConexion();
		CallableStatement eliminaDeptoParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.Elimina_Depto_Parcela(?,?)}");
		eliminaDeptoParcela.setInt("una_parcela", idParcela);
		eliminaDeptoParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		eliminaDeptoParcela.execute();
		System.out.println(eliminaDeptoParcela.getObject("un_Resultado"));
		eliminaDeptoParcela.close();
		conn.close();
	}

	private void eliminaMunicipioParcela(Integer idParcela) throws SQLException {
		conn = conexionBD.establecerConexion();
		CallableStatement eliminaMunicipioParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.Elimina_Municipio_Parcela(?,?)}");
		eliminaMunicipioParcela.setInt("una_parcela", idParcela);
		eliminaMunicipioParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		eliminaMunicipioParcela.execute();
		System.out.println(eliminaMunicipioParcela.getObject("un_Resultado"));
		eliminaMunicipioParcela.close();
		conn.close();
	}

	private void eliminaCarParcela(Integer idParcela) throws SQLException {
		conn = conexionBD.establecerConexion();
		CallableStatement eliminaCarParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.Elimina_Car_Parcela(?,?)}");
		eliminaCarParcela.setInt("una_parcela", idParcela);
		eliminaCarParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		eliminaCarParcela.execute();
		System.out.println(eliminaCarParcela.getObject("un_Resultado"));
		eliminaCarParcela.close();
		conn.close();
	}

	private void eliminaFisiografiaParcela(Integer idParcela)
			throws SQLException {
		conn = conexionBD.establecerConexion();
		CallableStatement eliminaFisiografiaParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.Elimina_Fisiografia_Parcela(?,?)}");
		eliminaFisiografiaParcela.setInt("una_parcela", idParcela);
		eliminaFisiografiaParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		eliminaFisiografiaParcela.execute();
		System.out.println(eliminaFisiografiaParcela.getObject("un_Resultado"));
		eliminaFisiografiaParcela.close();
		conn.close();
	}

	private void eliminaProyectoParcela(Integer idParcela) throws SQLException {
		conn = conexionBD.establecerConexion();
		CallableStatement eliminaParcelaProyecto = conn
				.prepareCall("{call RED_PK_PARCELAS.Elimina_Proyecto_Parcela(?,?)}");
		eliminaParcelaProyecto.setInt("una_parcela", idParcela);
		eliminaParcelaProyecto.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		eliminaParcelaProyecto.execute();
		System.out.println(eliminaParcelaProyecto.getObject("un_Resultado"));
		eliminaParcelaProyecto.close();
		conn.close();
	}
	
	public void eliminarContactoParcelaId(Integer idParcela) throws SQLException{
		conn = conexionBD.establecerConexion();
		CallableStatement eliminaContactoParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.eliminarContactoParcelaId(?,?)}");
		eliminaContactoParcela.setInt("una_parcela", idParcela);
		eliminaContactoParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		eliminaContactoParcela.execute();
		System.out.println(eliminaContactoParcela.getObject("un_Resultado"));
		eliminaContactoParcela.close();
		conn.close();
	}
 
}
