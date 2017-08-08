// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.entities;

import java.sql.Timestamp;

/**
 * Clase que representa Datos de Biomasa y Carbono
 */
public class BiomasaYCarbono {
	
	private Integer consecutivo;
	private Double biomasa;
	private Double carbono;
	private Timestamp fechaInicio;
	private Integer estado;
	private Integer idParcela;
	private Integer Metodologia;
	private Double areaBasalPromedio;
	private Double areaBasaltotal;
	private Double volumen;
	private Integer tipoGeneracion;
	private Timestamp fecha_fin;
	public Integer getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}
	public Double getBiomasa() {
		return biomasa;
	}
	public void setBiomasa(Double biomasa) {
		this.biomasa = biomasa;
	}
	public Double getCarbono() {
		return carbono;
	}
	public void setCarbono(Double carbono) {
		this.carbono = carbono;
	}
	public Timestamp getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Timestamp fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Integer getIdParcela() {
		return idParcela;
	}
	public void setIdParcela(Integer idParcela) {
		this.idParcela = idParcela;
	}
	public Integer getMetodologia() {
		return Metodologia;
	}
	public void setMetodologia(Integer metodologia) {
		Metodologia = metodologia;
	}
	public Double getAreaBasalPromedio() {
		return areaBasalPromedio;
	}
	public void setAreaBasalPromedio(Double areaBasalPromedio) {
		this.areaBasalPromedio = areaBasalPromedio;
	}
	public Double getAreaBasaltotal() {
		return areaBasaltotal;
	}
	public void setAreaBasaltotal(Double areaBasaltotal) {
		this.areaBasaltotal = areaBasaltotal;
	}
	public Double getVolumen() {
		return volumen;
	}
	public void setVolumen(Double volumen) {
		this.volumen = volumen;
	}
	public Integer getTipoGeneracion() {
		return tipoGeneracion;
	}
	public void setTipoGeneracion(Integer tipoGeneracion) {
		this.tipoGeneracion = tipoGeneracion;
	}
	public Timestamp getFecha_fin() {
		return fecha_fin;
	}
	public void setFecha_fin(Timestamp fechaFin) {
		fecha_fin = fechaFin;
	}

}
