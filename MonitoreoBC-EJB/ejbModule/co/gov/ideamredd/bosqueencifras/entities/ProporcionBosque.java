// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.bosqueencifras.entities;

/**
 * Clase que representa un consolidado de proporción de bosque para un período
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class ProporcionBosque {

	private String	id_reporte;
	private String	periodo;
	private Double	areaBosque;
	private Double	areaTotal;
	private Double	porcentaje;

	/**
	 * Constructor
	 */
	public ProporcionBosque() {
		super();
	}

	/**
	 * Retorna el id del reporte
	 * 
	 * @return String id del reporte
	 */
	public String getId_reporte() {
		return id_reporte;
	}

	/**
	 * Establece el id del reporte
	 * 
	 * @param id_reporte
	 */
	public void setId_reporte(String id_reporte) {
		this.id_reporte = id_reporte;
	}

	/**
	 * Retorna el período
	 * 
	 * @return String del período
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
	 * Retorna el área del bosque natural
	 * 
	 * @return Double del área del bosque
	 */
	public Double getAreaBosque() {
		return areaBosque;
	}

	/**
	 * Establece el área de bosque
	 * 
	 * @param areaBosque
	 */
	public void setAreaBosque(Double areaBosque) {
		this.areaBosque = areaBosque;
	}

	/**
	 * Retorna el área total
	 * 
	 * @return Double del área total
	 */
	public Double getAreaTotal() {
		return areaTotal;
	}

	/**
	 * Establece el área total
	 * 
	 * @param areaTotal
	 */
	public void setAreaTotal(Double areaTotal) {
		this.areaTotal = areaTotal;
	}

	/**
	 * Retorna el porcentaje de área de bosque sobre el total
	 * 
	 * @return Double porcentaje de área
	 */
	public Double getPorcentaje() {
		return porcentaje;
	}

	/**
	 * Establece el porcentaje de área
	 * 
	 * @param porcentaje
	 */
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

}
