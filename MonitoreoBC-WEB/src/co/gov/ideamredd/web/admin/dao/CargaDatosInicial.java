// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.admin.entities.Modulos;
import co.gov.ideamredd.admin.entities.Permisos;
import co.gov.ideamredd.admin.entities.PermisosRol;
import co.gov.ideamredd.admin.entities.Rol;
import co.gov.ideamredd.mbc.conexion.ConexionBD;

public class CargaDatosInicial {

	private static Connection				conn;
	private static DataSource				dataSource	= ConexionBD.getConnection();
	private static ArrayList<Rol>			roles;
	private static ArrayList<Modulos>		modulos;
	private static ArrayList<Permisos>		permisos;
	private static ArrayList<PermisosRol>	permisosRol;

	/**
	 * Metodo para cargar roles iniciales.
	 */
	public static ArrayList<Rol> cargarRoles() {
		roles = new ArrayList<Rol>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaRoles = conn.prepareCall("{call RED_PK_USUARIOS.consultarTodosRol(?)}");
			consultaRoles.registerOutParameter("un_resultado", OracleTypes.CURSOR);
			consultaRoles.execute();
			ResultSet r = (ResultSet) consultaRoles.getObject("un_resultado");
			while (r.next()) {
				Rol rol = new Rol();
				rol.setConsecutivo(r.getInt(1));
				rol.setNombre(r.getString(2));
				rol.setDescripcion(r.getString(3));
				roles.add(rol);
			}
			r.close();
			consultaRoles.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return roles;
	}

	/**
	 * Metodo para cargar rol.
	 */
	public static Rol consultaRol(Integer idRol) {
		Rol rol = new Rol();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaRoles = conn.prepareCall("{call RED_PK_USUARIOS.consultarRol(?,?)}");
			consultaRoles.setInt("un_consecutivo", idRol);
			consultaRoles.registerOutParameter("un_resultado", OracleTypes.CURSOR);
			consultaRoles.execute();
			ResultSet r = (ResultSet) consultaRoles.getObject("un_resultado");
			while (r.next()) {
				rol.setConsecutivo(r.getInt(1));
				rol.setNombre(r.getString(2));
				rol.setDescripcion(r.getString(3));
			}
			r.close();
			consultaRoles.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rol;
	}

	/**
	 * Metodo para consultar modulos.
	 */
	public static ArrayList<Modulos> consultarModulos() {
		modulos = new ArrayList<Modulos>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaModulos = conn.prepareCall("{call RED_PK_USUARIOS.consultarModulos(?)}");
			consultaModulos.registerOutParameter("un_resultado", OracleTypes.CURSOR);
			consultaModulos.execute();
			ResultSet r = (ResultSet) consultaModulos.getObject("un_resultado");
			while (r.next()) {
				Modulos modulo = new Modulos();
				modulo.setIdModulo(r.getInt(1));
				modulo.setDescripcion(r.getString(2));
				modulos.add(modulo);
			}
			r.close();
			consultaModulos.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return modulos;
	}

	/**
	 * Metodo para consulktar permisos de un rol.
	 */
	public static ArrayList<PermisosRol> consultarPermisosRol(Integer idRol) {
		permisosRol = new ArrayList<PermisosRol>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaPermisosRol = conn.prepareCall("{call RED_PK_USUARIOS.consultarRolPermisos(?,?)}");
			consultaPermisosRol.setInt("un_rol", idRol);
			consultaPermisosRol.registerOutParameter("un_resultado", OracleTypes.CURSOR);
			consultaPermisosRol.execute();
			ResultSet r = (ResultSet) consultaPermisosRol.getObject("un_resultado");
			while (r.next()) {
				PermisosRol permisoRol = new PermisosRol();
				permisoRol.setIdRol(r.getInt(1));
				permisoRol.setIdPermiso(r.getInt(2));
				permisosRol.add(permisoRol);
			}
			r.close();
			consultaPermisosRol.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return permisosRol;
	}

	/**
	 * Metodo para consultar permisos.
	 */
	public static ArrayList<Permisos> consultarPermisos() {
		permisos = new ArrayList<Permisos>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaPermisos = conn.prepareCall("{call RED_PK_USUARIOS.consultarPermisos(?)}");
			consultaPermisos.registerOutParameter("un_resultado", OracleTypes.CURSOR);
			consultaPermisos.execute();
			ResultSet r = (ResultSet) consultaPermisos.getObject("un_resultado");
			while (r.next()) {
				Permisos permiso = new Permisos();
				permiso.setIdPermiso(r.getInt(1));
				permiso.setDescripcion(r.getString(2));
				permiso.setIdModulo(r.getInt(3));
				permisos.add(permiso);
			}
			r.close();
			consultaPermisos.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return permisos;
	}

}
