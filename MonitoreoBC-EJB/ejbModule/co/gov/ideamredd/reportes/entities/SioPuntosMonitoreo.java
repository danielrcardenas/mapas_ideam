// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.entities;

import java.sql.Date;

public class SioPuntosMonitoreo {

	private String estado;
	private Integer puntoMonitoreoId;
	private Date fechaCreacion;
	private String tipo;
	private String nombre;
	private Date fechaModificacion;
	private String descripcion;
	private Integer fgdaId;
	private Date fechaAplicacion;
	private Double latitud;
	private Double longitud;
	private Double gradosLatitud;
	private Double minutosLatitud;
	private Double segundosLatitud;
	private String direccionLatitud;
	private Double gradosLongitud;
	private Double minutosLongitud;
	private Double segundosLongitud;
	private String direccionLongitud;
	private Double altitud;

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getFgdaId() {
		return fgdaId;
	}

	public void setFgdaId(Integer fgdaId) {
		this.fgdaId = fgdaId;
	}

	public Date getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Double getGradosLatitud() {
		return gradosLatitud;
	}

	public void setGradosLatitud(Double gradosLatitud) {
		this.gradosLatitud = gradosLatitud;
	}

	public Double getMinutosLatitud() {
		return minutosLatitud;
	}

	public void setMinutosLatitud(Double minutosLatitud) {
		this.minutosLatitud = minutosLatitud;
	}

	public Double getSegundosLatitud() {
		return segundosLatitud;
	}

	public void setSegundosLatitud(Double segundosLatitud) {
		this.segundosLatitud = segundosLatitud;
	}

	public String getDireccionLatitud() {
		return direccionLatitud;
	}

	public void setDireccionLatitud(String direccionLatitud) {
		this.direccionLatitud = direccionLatitud;
	}

	public Double getGradosLongitud() {
		return gradosLongitud;
	}

	public void setGradosLongitud(Double gradosLongitud) {
		this.gradosLongitud = gradosLongitud;
	}

	public Double getMinutosLongitud() {
		return minutosLongitud;
	}

	public void setMinutosLongitud(Double minutosLongitud) {
		this.minutosLongitud = minutosLongitud;
	}

	public Double getSegundosLongitud() {
		return segundosLongitud;
	}

	public void setSegundosLongitud(Double segundosLongitud) {
		this.segundosLongitud = segundosLongitud;
	}

	public String getDireccionLongitud() {
		return direccionLongitud;
	}

	public void setDireccionLongitud(String direccionLongitud) {
		this.direccionLongitud = direccionLongitud;
	}

	public Double getAltitud() {
		return altitud;
	}

	public void setAltitud(Double altitud) {
		this.altitud = altitud;
	}

	public Integer getPuntoMonitoreoId() {
		return puntoMonitoreoId;
	}

	public void setPuntoMonitoreoId(Integer puntoMonitoreoId) {
		this.puntoMonitoreoId = puntoMonitoreoId;
	}

}
