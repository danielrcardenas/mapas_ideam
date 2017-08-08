// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.imgusuarios.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.usuario.dao.ActualizarUsuario;
import co.gov.ideamredd.util.SMBC_Log;

@Stateless public class ActualizarImagenUsuario {

	@EJB
	ConexionBD			conexion;

	private Connection	conn;
	private Logger		log;

	/**
	 * Método usado para procesar una imagen de usuario. definiendo la accion (aceptada, rechazada).
	 */
	public void procesarImagen(Integer idImagen, String accion) {
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement registrarImagen = conn.prepareCall("{call RED_PK_USUARIOS.actualizarEstadoImagenUs(?,?,?)}");
			registrarImagen.setInt("un_idImagen", idImagen);
			if (accion.equals("acepta")) {
				registrarImagen.setInt("un_Estado", 2);
			}
			else {
				registrarImagen.setInt("un_Estado", 3);
			}
			registrarImagen.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			registrarImagen.execute();

			System.out.println(registrarImagen.getObject("un_Resultado"));

			log.info("[procesarImagen] Termino");
			registrarImagen.close();
			conn.close();
		}
		catch (Exception e) {
			log.info("[procesarImagen] Fallo");
			e.printStackTrace();
		}

	}

}
