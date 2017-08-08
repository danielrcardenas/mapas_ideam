// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.admin.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.mbc.conexion.ConexionBD;

import oracle.jdbc.OracleTypes;

@Stateless
public class ActualizaPermisos {

	@EJB
	private ConexionBD conexionBD;

	private Connection conn;

    /**
     * Método usado para actualizar un rol y sus permisos.
     */
	public void actualizar(Integer idRol, String[] permisos) {
		CallableStatement registrarRol = null;
		borrarPermisos(idRol);
		try {
			conn = conexionBD.establecerConexion();

			for (int i = 0; i < permisos.length; i++) {
				if (!permisos[i].equals("") && permisos[i] != null) {
					registrarRol = conn
							.prepareCall("{call RED_PK_USUARIOS.insertarPermisosRol(?,?,?)}");
					registrarRol.setInt("un_idRol", idRol);
					registrarRol.setInt("un_idPermiso",
							Integer.parseInt(permisos[i]));
					registrarRol.registerOutParameter("un_resultado",
							OracleTypes.VARCHAR);
					registrarRol.execute();
					System.out.println(registrarRol.getObject("un_resultado"));
				}
			}

			registrarRol.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Método usado para borrar los permisos de un rol.
     */
	public void borrarPermisos(Integer idRol) {
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement registrarRol = conn
					.prepareCall("{call RED_PK_USUARIOS.borrarPermisosRol(?,?)}");
			registrarRol.setInt("un_idRol", idRol);
			registrarRol.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			registrarRol.execute();
			System.out.println(registrarRol.getObject("un_resultado"));
			registrarRol.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
