package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import co.gov.ideamredd.conexionBD.ConexionBD;

@Stateless
public class CambiarEstadoBiomasa {

	@EJB
	private ConexionBD conexionBD;

	private Connection conn;
	private String verificado;
	private String atipico;
	private String incluido;
	private Integer idBiomasa;

	public void actualizarEstado() {
		try {
			conn = conexionBD.establecerConexion();
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			CallableStatement actualizarBiomasa = conn
					.prepareCall("{call RED_PK_PARCELAS.actualizarEstadoBiomasa(?,?,?,?,?)}");
			actualizarBiomasa.setString("un_verificado", verificado);
			actualizarBiomasa.setString("un_atipico", atipico);
			actualizarBiomasa.setString("un_incluido", incluido);
			actualizarBiomasa.setInt("una_biomasa", idBiomasa);
			actualizarBiomasa.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			actualizarBiomasa.execute();
			System.out.println(actualizarBiomasa.getObject("un_resultado"));
			actualizarBiomasa.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getVerificado() {
		return verificado;
	}

	public void setVerificado(String verificado) {
		this.verificado = verificado;
	}

	public String getAtipico() {
		return atipico;
	}

	public void setAtipico(String atipico) {
		this.atipico = atipico;
	}

	public String getIncluido() {
		return incluido;
	}

	public void setIncluido(String incluido) {
		this.incluido = incluido;
	}

	public Integer getIdBiomasa() {
		return idBiomasa;
	}

	public void setIdBiomasa(Integer idBiomasa) {
		this.idBiomasa = idBiomasa;
	}
}
