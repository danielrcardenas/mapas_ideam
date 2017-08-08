/**
 * 
 */
// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.descarga.entities;

import java.sql.Timestamp;

/**
 * @author Usuario
 *
 */
public class Dataset {
	
	private String id;
	private String nombre;
	private String tipoImagenMapa;
	private Timestamp fechaAdquisicion;
	private Timestamp fechaRegistro;
	private float tamanio;
	private String formato;
	private String resolucion;
	private String proyeccion;
	private String georeferenciacion;
	private String numBandas;
	private String tipoDato;
	private String linkMetadato;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipoImagenMapa() {
		return tipoImagenMapa;
	}
	public void setTipoImagenMapa(String tipoImagenMapa) {
		this.tipoImagenMapa = tipoImagenMapa;
	}
	public Timestamp getFechaAdquisicion() {
		return fechaAdquisicion;
	}
	public void setFechaAdquisicion(Timestamp fechaAdquisicion) {
		this.fechaAdquisicion = fechaAdquisicion;
	}
	public Timestamp getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Timestamp fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public float getTamanio() {
		return tamanio;
	}
	public void setTamanio(float tamanio) {
		this.tamanio = tamanio;
	}
	public String getFormato() {
		return formato;
	}
	public void setFormato(String formato) {
		this.formato = formato;
	}
	public String getResolucion() {
		return resolucion;
	}
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}
	public String getProyeccion() {
		return proyeccion;
	}
	public void setProyeccion(String proyeccion) {
		this.proyeccion = proyeccion;
	}
	public String getGeoreferenciacion() {
		return georeferenciacion;
	}
	public void setGeoreferenciacion(String georeferenciacion) {
		this.georeferenciacion = georeferenciacion;
	}
	public String getNumBandas() {
		return numBandas;
	}
	public void setNumBandas(String numBandas) {
		this.numBandas = numBandas;
	}
	public String getTipoDato() {
		return tipoDato;
	}
	public void setTipoDato(String tipoDato) {
		this.tipoDato = tipoDato;
	}
	public String getLinkMetadato() {
		return linkMetadato;
	}
	public void setLinkMetadato(String linkMetadato) {
		this.linkMetadato = linkMetadato;
	}	
}
