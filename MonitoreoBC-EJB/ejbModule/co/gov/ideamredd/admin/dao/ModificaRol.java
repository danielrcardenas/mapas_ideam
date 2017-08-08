// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.admin.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.mbc.conexion.ConexionBD;

import oracle.jdbc.OracleTypes;


@Stateless
public class ModificaRol {

	@EJB
	private ConexionBD conexionBD;

	private Connection conn;

	private Integer consecutivo;
	private String nombre;
	private String descripcion;

    /**
     * Método usado para actualizar un rol.
     */
	public void modicarRol() {
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement modificarRol = conn
					.prepareCall("{call RED_PK_USUARIOS.modificarRol(?,?,?,?)}");
			modificarRol.setInt("un_consecutivo", consecutivo);
			modificarRol.setString("un_nombre", nombre);
			modificarRol.setString("una_descripcion", descripcion);
			modificarRol.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			modificarRol.execute();
			System.out.println(modificarRol.getObject("un_resultado"));
			modificarRol.close();
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}