// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.entities;

import java.sql.Date;

/**
 * Clase que representa una lista de Reportes
 */
public class Reportes {

	private Integer consecutivo;
	private Date fechaGeneracion;
	private Integer division;
	private Integer periodoUno;
	private Integer periodoDos;
	private Integer tipoReporte;
	private String identImagen;

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public Integer getDivision() {
		return division;
	}

	public void setDivision(Integer division) {
		this.division = division;
	}

	public Integer getPeriodoUno() {
		return periodoUno;
	}

	public void setPeriodoUno(Integer periodoUno) {
		this.periodoUno = periodoUno;
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

	public String getIdentImagen() {
		return identImagen;
	}

	public void setIdentImagen(String identImagen) {
		this.identImagen = identImagen;
	}

	
	
}
