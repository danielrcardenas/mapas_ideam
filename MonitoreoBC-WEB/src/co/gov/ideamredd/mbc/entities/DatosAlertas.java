// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.entities;

/**
 * Clase que representa los datos de una alerta temprana
 * 
 * @author Julio Sánchez, Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class DatosAlertas {

	private String	nombre;
	private String	periodo;
	private Integer	numero;
	private String	fecha;
	private String	descripcion;
	private String	direccionContacto;
	private String	informacionComplementaria;
	private String	creditos;
	private String	proximasPublicaciones;
	private String	desccripcionDepartamentos;
	private String	descripcionRegiones;
	private String	descripcionAutoridades;
	private String	descripcionPersistencia;
	private String	descripcionConcentracion;
	private String	descripcionNucleos;

	/**
	 * Retorna el nombre
	 * 
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre
	 * 
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	 * Retorna el número
	 * 
	 * @return
	 */
	public Integer getNumero() {
		return numero;
	}

	/**
	 * Establece el número
	 * 
	 * @param numero
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	/**
	 * Retorna la fecha
	 * 
	 * @return
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * Establece la fecha
	 * 
	 * @param fecha
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * Retorna la descripción
	 * 
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Establece la descripción
	 * 
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Retorna los créditos
	 * 
	 * @return
	 */
	public String getCreditos() {
		return creditos;
	}

	/**
	 * Establece los créditos
	 * 
	 * @param creditos
	 */
	public void setCreditos(String creditos) {
		this.creditos = creditos;
	}

	/**
	 * Retorna las próximas publicaciones
	 * 
	 * @return
	 */
	public String getProximasPublicaciones() {
		return proximasPublicaciones;
	}

	/**
	 * Establece las próximas publicaciones
	 * 
	 * @param proximasPublicaciones
	 */
	public void setProximasPublicaciones(String proximasPublicaciones) {
		this.proximasPublicaciones = proximasPublicaciones;
	}

	/**
	 * Retorna la descripción de los departamentos
	 * 
	 * @return
	 */
	public String getDesccripcionDepartamentos() {
		return desccripcionDepartamentos;
	}

	/**
	 * Establece la descripción de los departamentos
	 * 
	 * @param desccripcionDepartamentos
	 */
	public void setDesccripcionDepartamentos(String desccripcionDepartamentos) {
		this.desccripcionDepartamentos = desccripcionDepartamentos;
	}

	/**
	 * Retorna la descripción de las regiones
	 * 
	 * @return
	 */
	public String getDescripcionRegiones() {
		return descripcionRegiones;
	}

	/**
	 * Establece la descripción de las regiones
	 * 
	 * @param descripcionRegiones
	 */
	public void setDescripcionRegiones(String descripcionRegiones) {
		this.descripcionRegiones = descripcionRegiones;
	}

	/**
	 * Retorna la descripción de las autoridades
	 * 
	 * @return
	 */
	public String getDescripcionAutoridades() {
		return descripcionAutoridades;
	}

	/**
	 * Establece
	 * 
	 * @param descripcionAutoridades
	 */
	public void setDescripcionAutoridades(String descripcionAutoridades) {
		this.descripcionAutoridades = descripcionAutoridades;
	}

	/**
	 * Retorna la descripción de la persistencia
	 * 
	 * @return
	 */
	public String getDescripcionPersistencia() {
		return descripcionPersistencia;
	}

	/**
	 * Establece la descripción de la persistencia
	 * 
	 * @param descripcionPersistencia
	 */
	public void setDescripcionPersistencia(String descripcionPersistencia) {
		this.descripcionPersistencia = descripcionPersistencia;
	}

	/**
	 * Retorna la descripción de la concentración
	 * 
	 * @return
	 */
	public String getDescripcionConcentracion() {
		return descripcionConcentracion;
	}

	/**
	 * Establece la descripción de la concentración
	 * 
	 * @param descripcionConcentracion
	 */
	public void setDescripcionConcentracion(String descripcionConcentracion) {
		this.descripcionConcentracion = descripcionConcentracion;
	}

	/**
	 * Retorna la descripción de los núcleos
	 * 
	 * @return
	 */
	public String getDescripcionNucleos() {
		return descripcionNucleos;
	}

	/**
	 * Establece la descripción de los núcleos
	 * 
	 * @param descripcionNucleos
	 */
	public void setDescripcionNucleos(String descripcionNucleos) {
		this.descripcionNucleos = descripcionNucleos;
	}

	/**
	 * Retorna la dirección de contacto
	 * 
	 * @return
	 */
	public String getDireccionContacto() {
		return direccionContacto;
	}

	/**
	 * Establece la dirección de contacto
	 * 
	 * @param direccionContacto
	 */
	public void setDireccionContacto(String direccionContacto) {
		this.direccionContacto = direccionContacto;
	}

	/**
	 * Retorna la información complementaria
	 * 
	 * @return
	 */
	public String getInformacionComplementaria() {
		return informacionComplementaria;
	}

	/**
	 * Establece la información complementaria
	 * 
	 * @param informacionComplementaria
	 */
	public void setInformacionComplementaria(String informacionComplementaria) {
		this.informacionComplementaria = informacionComplementaria;
	}

}
