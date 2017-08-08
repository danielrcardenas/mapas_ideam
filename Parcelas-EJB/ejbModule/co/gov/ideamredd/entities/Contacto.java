package co.gov.ideamredd.entities;

public class Contacto {

	private Integer consecutivo;
	private String nombre;
	private Integer pais;
//	private String direccion;
	private String telefono;
	private String movil;
	private String correo; 
	private Integer municipio;
//	private String tipo;
//	private String organizacion;
//	private String cargo;
//	private String sector;

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getPais() {
		return pais;
	}

	public void setPais(Integer pais) {
		this.pais = pais;
	}

//	public String getDireccion() {
//		return direccion;
//	}
//
//	public void setDireccion(String direccion) {
//		this.direccion = direccion;
//	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMovil() {
		return movil;
	}

	public void setMovil(String movil) {
		this.movil = movil;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

//	public String getTipo() {
//		return tipo;
//	}
//
//	public void setTipo(String tipo) {
//		this.tipo = tipo;
//	}
//
//	public String getOrganizacion() {
//		return organizacion;
//	}
//
//	public void setOrganizacion(String organizacion) {
//		this.organizacion = organizacion;
//	}
//
//	public String getCargo() {
//		return cargo;
//	}
//
//	public void setCargo(String cargo) {
//		this.cargo = cargo;
//	}
//
//	/**
//	 * @return the sector
//	 */
//	public String getSector() {
//		return sector;
//	}
//
//	/**
//	 * @param sector the sector to set
//	 */
//	public void setSector(String sector) {
//		this.sector = sector;
//	}

	public Integer getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Integer municipio) {
		this.municipio = municipio;
	}

}
