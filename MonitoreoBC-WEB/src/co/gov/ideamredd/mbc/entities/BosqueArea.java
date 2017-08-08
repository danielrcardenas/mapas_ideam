// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.entities;

/**
 * Clase que representa un Area de bosque
 */
public class BosqueArea {

	private Double	Area;
	private String	Nombre;

	/**
	 * Constructor
	 * 
	 * @param nombre
	 * @param area
	 */
	public BosqueArea(String nombre, Double area) {
		this.Nombre = nombre;
		this.Area = area;
	}

	/**
	 * Establece el área
	 * 
	 * @param dat
	 */
	public void setArea(Double dat) {
		Area = dat;
	}

	/**
	 * Retorna el área
	 * 
	 * @return
	 */
	public Double getArea() {
		return Area;
	}

	/**
	 * Establece el nombre
	 * 
	 * @param dat
	 */
	public void setNombre(String dat) {
		Nombre = dat;
	}

	/**
	 * Retorna el nombre
	 * 
	 * @return
	 */
	public String getNombre() {
		return Nombre;
	}

}
