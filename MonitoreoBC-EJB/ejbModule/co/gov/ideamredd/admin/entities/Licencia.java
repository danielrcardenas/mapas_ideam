// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.admin.entities;

/**
 * Clase que representa una Licencia de uso
 */
public class Licencia {
	
	private Integer	consecutivo;
	private String nombre;
	private String	descripcion;
	private Integer tipo;
	private Integer autorizacion;
	
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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	
	public Integer getAutorizacion() {
		return autorizacion;
	}
	public void setAutorizacion(Integer autorizacion) {
		this.autorizacion = autorizacion;
	}
}
