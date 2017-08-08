// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.proyecto.entities;

/**
 * Clase que representa un Metodologia de proyecto.
 */
public class Metodologia {

	private int Id;
	private String nombre;
	private String descripcion;
	private String ecuacion;
	private String dirArchivo;

	public void setMetodologiaId(int metodId) {
		this.Id = metodId;
	}

	public int getMetodologiaId() {
		return this.Id;
	}

	public void setMetodologiaNombre(String nomMetod) {
		this.nombre = nomMetod;
	}

	public String getMetodologiaNombre() {
		return this.nombre;
	}

	public void setMetodologiaEcuacion(String ecuacion) {
		this.ecuacion = ecuacion;
	}

	public String getMetodologiaEcuacion() {
		return this.ecuacion;
	}

	public void setMetodologiaDirArchivo(String dirArchivo) {
		this.dirArchivo = dirArchivo;
	}

	public String getMetodologiaDirArchivo() {
		return this.dirArchivo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
