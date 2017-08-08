// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.entities;

import java.math.BigDecimal;

/**
 * Clase que representala informacion del reporte de biomasa
 */
public class AgregadoReporte {

	private Integer id_reporte;
	private Double area;
	private String clasificacion;
	private String divisionTerritorial;
	private Double incertidumbre;
	private Double error;
	private Double baj;
	private Double bat;
	private Double caj;
	private Double cat;
	private Double coe;
	private Double coet;
	
	
	/**
	 * 
	 */
	public AgregadoReporte() {
		super();
	}


	/**
	 * @param id_reporte
	 * @param area
	 * @param clasificacion
	 * @param divisionTerritorial
	 * @param incertidumbre
	 * @param error
	 * @param baj
	 * @param bat
	 * @param caj
	 * @param cat
	 * @param coe
	 * @param coet
	 */
	public AgregadoReporte(Integer id_reporte, Double area, String clasificacion, String divisionTerritorial, Double incertidumbre, Double error, Double baj, Double bat, Double caj, Double cat, Double coe, Double coet) {
		super();
		this.id_reporte = id_reporte;
		this.area = area;
		this.clasificacion = clasificacion;
		this.divisionTerritorial = divisionTerritorial;
		this.incertidumbre = incertidumbre;
		this.error = error;
		this.baj = baj;
		this.bat = bat;
		this.caj = caj;
		this.cat = cat;
		this.coe = coe;
		this.coet = coet;
	}


	
	public Integer getId_reporte() {
		return id_reporte;
	}


	
	public void setId_reporte(Integer id_reporte) {
		this.id_reporte = id_reporte;
	}


	
	public Double getArea() {
		return area;
	}


	
	public void setArea(Double area) {
		this.area = area;
	}


	
	public String getClasificacion() {
		return clasificacion;
	}


	
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}


	
	public String getDivisionTerritorial() {
		return divisionTerritorial;
	}


	
	public void setDivisionTerritorial(String divisionTerritorial) {
		this.divisionTerritorial = divisionTerritorial;
	}


	
	public Double getIncertidumbre() {
		return incertidumbre;
	}


	
	public void setIncertidumbre(Double incertidumbre) {
		this.incertidumbre = incertidumbre;
	}


	
	public Double getError() {
		return error;
	}


	
	public void setError(Double error) {
		this.error = error;
	}


	
	public Double getBaj() {
		return baj;
	}


	
	public void setBaj(Double baj) {
		this.baj = baj;
	}


	
	public Double getBat() {
		return bat;
	}


	
	public void setBat(Double bat) {
		this.bat = bat;
	}


	
	public Double getCaj() {
		return caj;
	}


	
	public void setCaj(Double caj) {
		this.caj = caj;
	}


	
	public Double getCat() {
		return cat;
	}


	
	public void setCat(Double cat) {
		this.cat = cat;
	}


	
	public Double getCoe() {
		return coe;
	}


	
	public void setCoe(Double coe) {
		this.coe = coe;
	}


	
	public Double getCoet() {
		return coet;
	}


	
	public void setCoet(Double coet) {
		this.coet = coet;
	}

	

}
