// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.entities;

import java.util.Date;

/**
 * Clase que representa una noticia
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class Noticias {

	private Integer		consecutivo;
	private Integer		tipo;
	private Date		fecha;
	private String		nombre;
	private String		descripcion;
	private String		lugar;
	private String		pathImagen;
	private String[]	coordenadas;
	private String		hora;

	/**
	 * Retorna el consecutivo
	 * 
	 * @return
	 */
	public Integer getConsecutivo() {
		return consecutivo;
	}

	/**
	 * Establece el consecutivo
	 * 
	 * @param consecutivo
	 */
	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * Retorna el tipo
	 * 
	 * @return
	 */
	public Integer getTipo() {
		return tipo;
	}

	/**
	 * Establece el tipo
	 * 
	 * @param tipo
	 */
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	/**
	 * Retorna la fecha
	 * 
	 * @return
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Establece la fecha
	 * 
	 * @param fecha
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Retorna el nombre
	 * 
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Retorna la descripción
	 * 
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Establece la descripción
	 * 
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Retorna el lugar
	 * 
	 * @return
	 */
	public String getLugar() {
		return lugar;
	}

	/**
	 * Establece el lugar
	 * 
	 * @param lugar
	 */
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	/**
	 * Retorna la ruta de la imagen
	 * 
	 * @return
	 */
	public String getPathImagen() {
		return pathImagen;
	}

	/**
	 * Establece la ruta a la imagen
	 * 
	 * @param pathImagen
	 */
	public void setPathImagen(String pathImagen) {
		this.pathImagen = pathImagen;
	}

	/**
	 * Retorna las coordenadas
	 * 
	 * @return
	 */
	public String[] getCoordenadas() {
		return coordenadas;
	}

	/**
	 * Establece las coordenadas
	 * 
	 * @param coordenadas
	 */
	public void setCoordenadas(String[] coordenadas) {
		this.coordenadas = coordenadas;
	}

	/**
	 * Retorna la hora
	 * 
	 * @return
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * Establece la hora
	 * 
	 * @param hora
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

}
