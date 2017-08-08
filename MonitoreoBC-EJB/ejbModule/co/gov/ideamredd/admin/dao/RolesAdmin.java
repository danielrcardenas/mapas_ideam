// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.admin.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.admin.entities.Rol;

@Stateless
public class RolesAdmin {

	@EJB
	ConexionBD			conexion;

	private Logger		log;
	private Connection	conn;

	/**
	 * Metodo usado para consultar roles registrados.
	 */
	public ArrayList<Rol> rolesUsuarioBusqueda(String un_Texto) {
		ArrayList<Rol> roles = new ArrayList<Rol>();
		Rol rol;
		try {
			log = SMBC_Log.Log(RolesAdmin.class);
			conn = conexion.establecerConexion();
			CallableStatement consultaRoles = conn.prepareCall("{call RED_PK_USUARIOS.buscarRoles(?,?,?)}");
			consultaRoles.setString("un_Texto", un_Texto);
			consultaRoles.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaRoles.registerOutParameter("un_Mensaje", OracleTypes.VARCHAR, 250);
			consultaRoles.execute();
			System.out.println(consultaRoles.getObject("un_Mensaje"));
			ResultSet resultSet = (ResultSet) consultaRoles.getObject("un_Resultado");
			while (resultSet.next()) {
				rol = new Rol();
				rol.setConsecutivo(resultSet.getInt(1));
				rol.setDescripcion(resultSet.getString(3));
				rol.setNombre(resultSet.getString(4));
				rol.setActivo(resultSet.getInt(5));
				roles.add(rol);
			}
			log.info("[rolesUsuarioBusqueda] Termino");
			resultSet.close();
			consultaRoles.close();
			conn.close();
		}
		catch (SQLException e) {
			log.error("[rolesUsuarioBusqueda] Fallo SQL");
			e.printStackTrace();
		}
		catch (Exception e) {
			log.error("[rolesUsuarioBusqueda] Fallo");
			e.printStackTrace();
		}
		return roles;
	}

}
