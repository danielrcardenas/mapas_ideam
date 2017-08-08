// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.sql.DataSource;

import co.gov.ideamredd.mbc.conexion.ConexionBD;

import oracle.jdbc.OracleTypes;

/**
 * Clase usada para administrar noticias y eventos.
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class RegistraNoticiaEvento {

	private static Connection	conn;
	private static DataSource	dataSource	= ConexionBD.getConnection();
	private Integer				consecutivo;
	private Integer				tipo;
	private String				fecha;
	private String				nombre;
	private String				descripcion;
	private String				lugar;
	private String				pathImagen;
	private String				hora;

	/**
	 * Crea una noticia
	 */
	public void crearNoticia() {
		try {
			conn = dataSource.getConnection();
			CallableStatement registrarNoticias = conn.prepareCall("{call RED_PK_TABLASTIPO.insertarNoticia(?,?,?,?,?)}");
			registrarNoticias.setString("un_nombre", nombre);
			registrarNoticias.setString("una_fecha", fecha);
			registrarNoticias.setString("una_descripcion", descripcion);
			registrarNoticias.setString("una_imagen", pathImagen);
			registrarNoticias.registerOutParameter("un_resultado", OracleTypes.VARCHAR);
			registrarNoticias.execute();
			System.out.println(registrarNoticias.getString("un_resultado"));
			registrarNoticias.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crea un evento
	 */
	public void crearEvento() {
		try {
			conn = dataSource.getConnection();
			CallableStatement registrarEvento = conn.prepareCall("{call RED_PK_TABLASTIPO.insertarEvento(?,?,?,?,?,?)}");
			registrarEvento.setString("un_nombre", nombre);
			registrarEvento.setString("una_fecha", fecha);
			registrarEvento.setString("una_descripcion", descripcion);
			registrarEvento.setString("un_lugar", lugar);
			registrarEvento.setString("una_hora", hora);
			registrarEvento.registerOutParameter("un_resultado", OracleTypes.VARCHAR);
			registrarEvento.execute();
			System.out.println(registrarEvento.getString("un_resultado"));
			registrarEvento.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Retorna el consecutivo
	 * @return Integer del consecutivo
	 */
	public Integer getConsecutivo() {
		return consecutivo;
	}

	
	/**
	 * Establece el consecutivo 
	 * @param consecutivo
	 */
	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * Retorna el tipo (evento o noticia)
	 * @return tipo de nota
	 */
	public Integer getTipo() {
		return tipo;
	}

	/**
	 * Establece el tipo (noticia o evento) 
	 * @param tipo
	 */
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	/**
	 * Retorna fecha
	 * @return fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * Establece fecha
	 * @param fecha
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * Retorna nombre
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece nombre
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Retorna descripción
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Establece descripción
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Retorna lugar
	 * @return lugar
	 */
	public String getLugar() {
		return lugar;
	}

	/**
	 * Establece lugar
	 * @param lugar
	 */
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	/**
	 * Retorna ruta de la image
	 * @return ruta
	 */
	public String getPathImagen() {
		return pathImagen;
	}

	/**
	 * Establece ruta de la imagen
	 * @param pathImagen
	 */
	public void setPathImagen(String pathImagen) {
		this.pathImagen = pathImagen;
	}

	/**
	 * Retorna la hora
	 * @return hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * Establece la hora
	 * @param hora
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
}
