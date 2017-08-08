// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.bosqueencifras.entities;

/**
 * Clase que representa un consolidado de tása de deforestación de un período
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class TasaDeforestacion {

	private String	id_reporte;
	private String	periodo;
	private Double	areaBosqueInicial;
	private Double	areaBosqueFinal;
	private Double	areaDeforestada;
	private Double	tasaDeforestacion;

	/**
	 * Constructor
	 */
	public TasaDeforestacion() {
		super();
	}

	/**
	 * Retorna el id del reporte
	 * 
	 * @return String del id de reporte
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
	 * Retorna el área inicial de bosque
	 * 
	 * @return Double del área inicial
	 */
	public Double getAreaBosqueInicial() {
		return areaBosqueInicial;
	}

	/**
	 * Establece el área inicial del bosque
	 * 
	 * @param areaBosqueInicial
	 */
	public void setAreaBosqueInicial(Double areaBosqueInicial) {
		this.areaBosqueInicial = areaBosqueInicial;
	}

	/**
	 * Retorna el área final de bosque
	 * 
	 * @return Double del área final
	 */
	public Double getAreaBosqueFinal() {
		return areaBosqueFinal;
	}

	/**
	 * Establece el área final de bosque
	 * 
	 * @param areaBosqueFinal
	 */
	public void setAreaBosqueFinal(Double areaBosqueFinal) {
		this.areaBosqueFinal = areaBosqueFinal;
	}

	/**
	 * Retorna el área deforestada
	 * 
	 * @return Double del área deforestada
	 */
	public Double getAreaDeforestada() {
		return areaDeforestada;
	}

	/**
	 * Establece el área deforestada
	 * 
	 * @param areaDeforestada
	 */
	public void setAreaDeforestada(Double areaDeforestada) {
		this.areaDeforestada = areaDeforestada;
	}

	/**
	 * Retorna la tasa de deforestación
	 * 
	 * @return Double de la tasa de deforestación
	 */
	public Double getTasaDeforestacion() {
		return tasaDeforestacion;
	}

	/**
	 * Establece la tasa de deforestación
	 * 
	 * @param tasaDeforestacion
	 */
	public void setTasaDeforestacion(Double tasaDeforestacion) {
		this.tasaDeforestacion = tasaDeforestacion;
	}

}
