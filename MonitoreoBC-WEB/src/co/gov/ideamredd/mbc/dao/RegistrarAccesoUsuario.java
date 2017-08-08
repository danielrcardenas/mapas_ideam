// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;

/**
 * Métodos para registrar el acceso de un usuario
 * 
 * @author Julio Sánchez y Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
@Stateless public class RegistrarAccesoUsuario {

	@EJB
	private ConexionBD	conexion;

	private Logger		log;

	/**
	 * Metodo para registrar el acceso de un usuario.
	 */
	public void registrarIngresoUsuario(String un_Id) {
		CallableStatement registrarAcceso = null;
		Connection conn = null;

		try {
			conn = conexion.establecerConexion();
			log = SMBC_Log.Log(RegistrarAccesoUsuario.class);
			registrarAcceso = conn.prepareCall("{call RED_PK_USUARIOS.RegistroIngresoUsuario_Inserta(?,?)}");
			registrarAcceso.setString("un_Id", un_Id);
			registrarAcceso.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			registrarAcceso.execute();
			System.out.println(registrarAcceso.getObject("un_Resultado"));

			log.info("[registrarIngresoUsuario] Termino");
		}
		catch (Exception e) {
			log.error("[registrarIngresoUsuario] Fallo");
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					if (!conn.isClosed()) conn.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (registrarAcceso != null) {
				try {
					registrarAcceso.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
