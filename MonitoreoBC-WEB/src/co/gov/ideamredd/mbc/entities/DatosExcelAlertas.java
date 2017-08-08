// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.entities;

import java.util.ArrayList;

/**
 * Clase que representa un excel con datos de alertas tempranas
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class DatosExcelAlertas {

	private ArrayList<EntidadAlertas>	departamentos;
	private ArrayList<EntidadAlertas>	regiones;
	private ArrayList<EntidadAlertas>	autoridades;
	private ArrayList<Nucleo>			nucleos;

	/**
	 * Constructor
	 */
	public DatosExcelAlertas() {
		departamentos = new ArrayList<EntidadAlertas>();
		regiones = new ArrayList<EntidadAlertas>();
		autoridades = new ArrayList<EntidadAlertas>();
		nucleos = new ArrayList<Nucleo>();
	}

	/**
	 * Retorna departamentos
	 * 
	 * @return
	 */
	public ArrayList<EntidadAlertas> getDepartamentos() {
		return departamentos;
	}

	/**
	 * Establece departamentos
	 * 
	 * @param departamentos
	 */
	public void setDepartamentos(ArrayList<EntidadAlertas> departamentos) {
		this.departamentos = departamentos;
	}

	/**
	 * Retorna regiones
	 * 
	 * @return
	 */
	public ArrayList<EntidadAlertas> getRegiones() {
		return regiones;
	}

	/**
	 * Establece regiones
	 * 
	 * @param regiones
	 */
	public void setRegiones(ArrayList<EntidadAlertas> regiones) {
		this.regiones = regiones;
	}

	/**
	 * Retorna autoridades
	 * 
	 * @return
	 */
	public ArrayList<EntidadAlertas> getAutoridades() {
		return autoridades;
	}

	/**
	 * Establece autoridades
	 * 
	 * @param autoridades
	 */
	public void setAutoridades(ArrayList<EntidadAlertas> autoridades) {
		this.autoridades = autoridades;
	}

	/**
	 * Retorna núcleos
	 * 
	 * @return
	 */
	public ArrayList<Nucleo> getNucleos() {
		return nucleos;
	}

	/**
	 * Establece núcleos
	 * 
	 * @param nucleos
	 */
	public void setNucleos(ArrayList<Nucleo> nucleos) {
		this.nucleos = nucleos;
	}

}
