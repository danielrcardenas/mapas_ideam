// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.admin.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.gov.ideamredd.mbc.conexion.ConexionBD;

import oracle.jdbc.OracleTypes;

@Stateless
public class ActualizarRol {

	@EJB
	private ConexionBD conexionBD;

	private Connection conn;

	private String nombre;
	private String descripcion;
	
    /**
     * Método usado para registrar un rol.
     */
	public void registrar(Integer consecutivo,String nombre, String descripcion,Integer activo){
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement registrarRol = conn
					.prepareCall("{call RED_PK_USUARIOS.actualizarRol(?,?,?,?,?)}");
			registrarRol.setInt("un_consecutivo", consecutivo);
			registrarRol.setString("un_nombre", nombre.replace(" ", "_").toUpperCase());
			registrarRol.setString("una_descripcion", descripcion);
			registrarRol.setInt("es_activo", activo);
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
