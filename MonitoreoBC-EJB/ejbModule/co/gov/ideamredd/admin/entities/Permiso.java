// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.admin.entities;

/**
 * Clase que representa un Permiso de usuario
 */
public class Permiso {
	
	private Integer idPermiso;
	private String descripcion;
	private Integer idModulo;
	private String nombreModulo;

	public Integer getIdPermiso() {
		return idPermiso;
	}

	public void setIdPermiso(Integer idPermiso) {
		this.idPermiso = idPermiso;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getIdModulo() {
		return idModulo;
	}

	public void setIdModulo(Integer idModulo) {
		this.idModulo = idModulo;
	}

	
	public String getNombreModulo() {
		return nombreModulo;
	}

	
	public void setNombreModulo(String nombreModulo) {
		this.nombreModulo = nombreModulo;
	}
	
	

}
