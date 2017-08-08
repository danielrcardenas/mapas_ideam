package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import co.gov.ideamredd.conexionBD.ConexionBD;

@Stateless
public class CrearRol {

	@EJB
	private ConexionBD conexionBD;

	private Connection conn;

	private String nombre;
	private String descripcion;
	
	public Integer registarRol(){
		Integer idRol=0;
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement registrarRol = conn
					.prepareCall("{call RED_PK_USUARIOS.crearRol(?,?,?,?)}");
			registrarRol.setString("un_nombre", nombre);
			registrarRol.setString("una_descripcion", descripcion);
			registrarRol.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			registrarRol.registerOutParameter("un_consecutivo",
					OracleTypes.INTEGER);
			registrarRol.execute();
			System.out.println(registrarRol.getObject("un_resultado"));
			idRol = registrarRol.getInt("un_consecutivo");
			registrarRol.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idRol;
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
