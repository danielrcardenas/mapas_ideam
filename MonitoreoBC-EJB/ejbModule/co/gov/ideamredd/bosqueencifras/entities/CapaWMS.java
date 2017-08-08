// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.bosqueencifras.entities;

/**
 * Clase que representa una capa WMS de Apollo
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class CapaWMS {

	private String	periodo;
	private String	identimagen;

	/**
	 * Constructor
	 */
	public CapaWMS() {
		super();
	}

	/**
	 * Retorna el período
	 * 
	 * @return
	 */
	public String getPeriodo() {
		return periodo;
	}

	/**
	 * Establece el período
	 * 
	 * @param periodo
	 */
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	/**
	 * Retorna el código de la capa WMS en Apollo
	 * 
	 * @return
	 */
	public String getIdentimagen() {
		return identimagen;
	}

	/**
	 * Establece el código de la capa WMS en Apollo
	 * 
	 * @param identimagen
	 */
	public void setIdentimagen(String identimagen) {
		this.identimagen = identimagen;
	}

}
