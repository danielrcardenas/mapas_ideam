// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.usuario.entities;

public class Perfil {
	
	private Integer	consecutivo;
	private String nombre;
	private Integer idperfilusuario;

	/**
	 * @return the consecutivo
	 */
	public Integer getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return the idperfilusuario
	 */
	public Integer getIdperfilusuario() {
		return idperfilusuario;
	}
	/**
	 * @param idperfilusuario the idperfilusuario to set
	 */
	public void setIdperfilusuario(Integer idperfilusuario) {
		this.idperfilusuario = idperfilusuario;
	}
	
	
}
