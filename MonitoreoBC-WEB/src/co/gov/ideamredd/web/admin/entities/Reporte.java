// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.entities;

import java.util.Date;

/**
 * Clase que representa un reporte
 */
public class Reporte {
	
	private Integer id;
	private Date fechaGeneracion;
	private Integer divisionTerritorio;
	private Integer periodoUno;
	private Integer periodoDos;
	private Integer tipoReporte;
	private Integer publicado;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	public Integer getPeriodoUno() {
		return periodoUno;
	}
	public void setPeriodoUno(Integer periodoUno) {
		this.periodoUno = periodoUno;
	}
	public Integer getDivisionTerritorio() {
		return divisionTerritorio;
	}
	public void setDivisionTerritorio(Integer divisionTerritorio) {
		this.divisionTerritorio = divisionTerritorio;
	}
	public Integer getPeriodoDos() {
		return periodoDos;
	}
	public void setPeriodoDos(Integer periodoDos) {
		this.periodoDos = periodoDos;
	}
	public Integer getTipoReporte() {
		return tipoReporte;
	}
	public void setTipoReporte(Integer tipoReporte) {
		this.tipoReporte = tipoReporte;
	}
	public Integer getPublicado() {
		return publicado;
	}
	public void setPublicado(Integer publicado) {
		this.publicado = publicado;
	}
	
	

}
