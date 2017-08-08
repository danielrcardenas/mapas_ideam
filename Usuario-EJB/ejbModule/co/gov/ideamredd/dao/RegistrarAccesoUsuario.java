package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;

@Stateless
public class RegistrarAccesoUsuario {

	@EJB
	private ConexionBD conexion;

	private Logger log;
	private Connection conn;

	public void registrarIngresoUsuario(Integer un_Id) {
		try {
			conn = conexion.establecerConexion();
			log = SMBC_Log.Log(RegistrarAccesoUsuario.class);
			CallableStatement registrarAcceso = conn
					.prepareCall("{call RED_PK_USUARIOS.RegistroIngresoUsuario_Inserta(?,?)}");
			registrarAcceso.setInt("un_Id", un_Id);
			registrarAcceso.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			registrarAcceso.execute();
			System.out.println(registrarAcceso.getObject("un_Resultado"));
			
			registrarAcceso.close();
			conn.close();
			log.info("[registrarIngresoUsuario] Termino");
		} catch (Exception e) {
			log.error("[registrarIngresoUsuario] Fallo");
			e.printStackTrace();
		}
	}

}
