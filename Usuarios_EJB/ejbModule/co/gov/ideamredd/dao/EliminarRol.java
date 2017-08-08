package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import co.gov.ideamredd.conexionBD.ConexionBD;

@Stateless
public class EliminarRol {

	@EJB
	private ConexionBD conexionBD;

	private Connection conn;

	private Integer consecutivo;

	public void eliminaRol() {
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement eliminarRol = conn
					.prepareCall("{call RED_PK_USUARIOS.eliminarRol(?,?)}");
			eliminarRol.setInt("un_consecutivo", consecutivo);
			eliminarRol.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			eliminarRol.execute();
			System.out.println(eliminarRol.getObject("un_resultado"));
			eliminarRol.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}
}
