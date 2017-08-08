package co.gov.ideamredd.ui.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;

public class RegistraNoticiaEvento {

	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();
	private Integer consecutivo;
	private Integer tipo;
	private String fecha;
	private String nombre;
	private String descripcion;
	private String lugar;
	private String pathImagen;
	private String hora;

	public void crearNoticia() {
		try {
			conn = dataSource.getConnection();
			CallableStatement registrarNoticias = conn
					.prepareCall("{call RED_PK_TABLASTIPO.insertarNoticia(?,?,?,?,?)}");
			registrarNoticias.setString("un_nombre", nombre);
			registrarNoticias.setString("una_fecha", fecha);
			registrarNoticias.setString("una_descripcion", descripcion);
			registrarNoticias.setString("una_imagen", pathImagen);
			registrarNoticias.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			registrarNoticias.execute();
			System.out.println(registrarNoticias.getString("un_resultado"));
			registrarNoticias.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void crearEvento() {
		try {
			conn = dataSource.getConnection();
			CallableStatement registrarEvento = conn
					.prepareCall("{call RED_PK_TABLASTIPO.insertarEvento(?,?,?,?,?,?)}");
			registrarEvento.setString("un_nombre", nombre);
			registrarEvento.setString("una_fecha", fecha);
			registrarEvento.setString("una_descripcion", descripcion);
			registrarEvento.setString("un_lugar", lugar);
			registrarEvento.setString("una_hora", hora);
			registrarEvento.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			registrarEvento.execute();
			System.out.println(registrarEvento.getString("un_resultado"));
			registrarEvento.close();
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

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
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

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public String getPathImagen() {
		return pathImagen;
	}

	public void setPathImagen(String pathImagen) {
		this.pathImagen = pathImagen;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}
}
