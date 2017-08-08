// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.entities;

/**
 * Clase que representa una Entidad de alerta temprana
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class EntidadAlertas {

	private String	nombre;
	private Double	porcentaje;
	private Double	porcentajeAcumulado;

	/**
	 * Retorna nombre
	 * 
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece nombre
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Retorna porcentaje
	 * 
	 * @return
	 */
	public Double getPorcentaje() {
		return porcentaje;
	}

	/**
	 * Establece porcentaje
	 * 
	 * @param porcentaje
	 */
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * Retorna porcentaje acumulado
	 * 
	 * @return
	 */
	public Double getPorcentajeAcumulado() {
		return porcentajeAcumulado;
	}

	/**
	 * Establece porcentaje acumulado
	 * 
	 * @param porcentajeAcumulado
	 */
	public void setPorcentajeAcumulado(Double porcentajeAcumulado) {
		this.porcentajeAcumulado = porcentajeAcumulado;
	}

}
