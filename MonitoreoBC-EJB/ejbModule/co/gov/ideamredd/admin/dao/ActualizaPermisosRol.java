// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.admin.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.mbc.conexion.ConexionBD;

import oracle.jdbc.OracleTypes;

@Stateless
public class ActualizaPermisosRol {

	@EJB
	private ConexionBD conexionBD;

	private Connection conn;
	private Integer idRol;
	private String[] idPermisos;

	public void actualizarPermisosRol() {
		eliminarPermisoRol();
		insertarPermisoRol();
	}

    /**
     * Método usado para eliminar permisos de un rol, el id del rol debe ser definido antes con el metodo set, respectivo.
     */
	private void eliminarPermisoRol() {
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement eliminarPermisoRol = conn
					.prepareCall("{call RED_PK_USUARIOS.eliminarPermisosRol(?,?)}");
			eliminarPermisoRol.setInt("un_rol", idRol);
			eliminarPermisoRol.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			eliminarPermisoRol.execute();
			System.out.println(eliminarPermisoRol.getObject("un_resultado"));
			eliminarPermisoRol.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Método usado para agregar permisos a un rol, el id del rol debe ser definido antes con el metodo set, respectivo.
     */
	private void insertarPermisoRol() {
		for (int i = 0; i < idPermisos.length; i++) {
			try {
				conn = conexionBD.establecerConexion();
				CallableStatement registrarPermisoRol = conn
						.prepareCall("{call RED_PK_USUARIOS.insertarPermisosRol(?,?,?)}");
				registrarPermisoRol.setInt("un_rol", idRol);
				registrarPermisoRol.setInt("un_permiso",
						Integer.valueOf(idPermisos[i]));
				registrarPermisoRol.registerOutParameter("un_resultado",
						OracleTypes.VARCHAR);
				registrarPermisoRol.execute();
				System.out.println(registrarPermisoRol
						.getObject("un_resultado"));
				registrarPermisoRol.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Integer getIdRol() {
		return idRol;
	}

	public void setIdRol(Integer idRol) {
		this.idRol = idRol;
	}

	public String[] getIdPermisos() {
		return idPermisos;
	}

	public void setIdPermisos(String[] idPermisos) {
		this.idPermisos = idPermisos;
	}
}
