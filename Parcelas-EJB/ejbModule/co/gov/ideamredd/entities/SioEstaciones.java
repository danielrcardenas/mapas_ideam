package co.gov.ideamredd.entities;

import java.sql.Date;

public class SioEstaciones {
	
	private String tipo;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private Double alturaReferencia;
	private Integer idEstacion;
	private String clase;
	private String categoria;
	private String nombre;
	private String objetoInstalacion;
	private Integer puntoMonitoreoId;
	private String estado;
	private Date fechaAplicacion;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public Double getAlturaReferencia() {
		return alturaReferencia;
	}

	public void setAlturaReferencia(Double alturaReferencia) {
		this.alturaReferencia = alturaReferencia;
	}

	public Integer getIdEstacion() {
		return idEstacion;
	}

	public void setIdEstacion(Integer idEstacion) {
		this.idEstacion = idEstacion;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getObjetoInstalacion() {
		return objetoInstalacion;
	}

	public void setObjetoInstalacion(String objetoInstalacion) {
		this.objetoInstalacion = objetoInstalacion;
	}

	public Integer getPuntoMonitoreoId() {
		return puntoMonitoreoId;
	}

	public void setPuntoMonitoreoId(Integer puntoMonitoreoId) {
		this.puntoMonitoreoId = puntoMonitoreoId;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

}
