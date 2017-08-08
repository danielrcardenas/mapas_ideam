/**
 * 
 */
// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.proyecto.entities;


/**
 * @author santiago
 *
 */
public class ConteoProyecto {
	private int conteo;
	private String dimension1;
	private String dimension2;
	
	public ConteoProyecto() {
		super();
	}
	
	public ConteoProyecto(int conteo, String dimension1, String dimension2) {
		super();
		this.conteo = conteo;
		this.dimension1 = dimension1;
		this.dimension2 = dimension2;
	}

	
	public int getConteo() {
		return conteo;
	}

	
	public void setConteo(int conteo) {
		this.conteo = conteo; 
	}

	
	public String getDimension1() {
		return dimension1;
	}

	
	public void setDimension1(String dimension1) {
		this.dimension1 = dimension1;
	}

	
	public String getDimension2() {
		return dimension2;
	}

	
	public void setDimension2(String dimension2) {
		this.dimension2 = dimension2;
	}
	
	
}
