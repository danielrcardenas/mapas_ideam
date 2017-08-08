// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.entities;

import java.util.Date;

/**
 * Clase que representa un Reporte
 */
public class Reporte {
	
	private Integer id;
	private Date fechaGeneracion;
	private Integer idDivision;
	private Integer periodoUno;
	private Integer periodoDos;
	private Integer idTipoReporte;
	private Boolean publicado;
	private String identImagen;
	
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
	public Integer getIdDivision() {
		return idDivision;
	}
	public void setIdDivision(Integer idDivision) {
		this.idDivision = idDivision;
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
	public Integer getIdTipoReporte() {
		return idTipoReporte;
	}
	public void setIdTipoReporte(Integer idTipoReporte) {
		this.idTipoReporte = idTipoReporte;
	}
	public Boolean getPublicado() {
		return publicado;
	}
	public void setPublicado(Boolean publicado) {
		this.publicado = publicado;
	}
	public String getIdentImagen() {
		return identImagen;
	}
	public void setIdentImagen(String identImagen) {
		this.identImagen = identImagen;
	}

	

}
