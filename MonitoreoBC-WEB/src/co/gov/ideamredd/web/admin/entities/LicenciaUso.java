// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.entities;

/**
 * Clase que representa una Licencia de uso.
 */
public class LicenciaUso {
	
	private Integer consecutivo;
	private String nombre;
	private Integer licencia;
	private String descripcion;
	private Integer activa;
	
	public Integer getConsecutivo() {
		return consecutivo;
	}
	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Integer getLicencia() {
		return licencia;
	}
	public void setLicencia(Integer licencia) {
		this.licencia = licencia;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Integer getActiva() {
		return activa;
	}
	public void setActiva(Integer activa) {
		this.activa = activa;
	}

}
