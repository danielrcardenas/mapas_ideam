package co.gov.ideamredd.entities;

import java.util.ArrayList;

import co.gov.ideamredd.security.Encript;

public class Usuario {

	private Integer idUsuario;
	private String nombre;
	private String apellidoUno;
	private String apellidoDos;
	private Integer tipoIdentificacion;
	private String identificacion;
	private Integer tipoPersona;
	private Integer pais;
	private String direccion;
	private String telefonoOficina;
	private String celular;
	private String correoElectronico;
	private String clave;
	private String activo;
	private String fechaCreacion;
	private Integer depto;
	private Integer municipio;
	private String[] licencias;
	private String organizacion;
	private String cargo;
	private Integer rolId;
	private ArrayList<String> rolNombre;
	private String tipoPersonaNombre;
	private String tipoIdentNombre;
	private String paisNombre;
	private String departamentoNombre;
	private String ciudadNombre;
	private String licenciasNombres;
	
	public Usuario(){
		
	}
	
	public Usuario(Integer consecutivo, String Nombre, Integer Tipo_identi, 
			String identificacion, Integer tipo_Persona, Integer pais,
			String direccion, String telefono, String celular, String correoE,
			String activo, Integer departamento, Integer Municipio)
	{
		this.idUsuario=consecutivo;
		this.nombre=Nombre;
		this.tipoIdentificacion=Tipo_identi;
		this.identificacion=identificacion;
		this.tipoPersona=tipo_Persona;
		this.pais= pais;
		this.direccion=direccion;
		this.telefonoOficina=telefono;
		this.celular=celular;
		this.correoElectronico=correoE;
		this.activo=activo;
		this.depto=departamento;
		this.municipio=Municipio;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidoUno() {
		return apellidoUno;
	}

	public void setApellidoUno(String apellidoUno) {
		this.apellidoUno = apellidoUno;
	}

	public String getApellidoDos() {
		return apellidoDos;
	}

	public void setApellidoDos(String apellidoDos) {
		this.apellidoDos = apellidoDos;
	}

	public Integer getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(Integer tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public Integer getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(Integer tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public Integer getPais() {
		return pais;
	}

	public void setPais(Integer pais) {
		this.pais = pais;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefonoOficina() {
		return telefonoOficina;
	}

	public void setTelefonoOficina(String telefonoOficina) {
		this.telefonoOficina = telefonoOficina;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Integer getDepto() {
		return depto;
	}

	public void setDepto(Integer depto) {
		this.depto = depto;
	}

	public Integer getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Integer municipio) {
		this.municipio = municipio;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String[] getLicencias() {
		return licencias;
	}

	public void setLicencias(String[] licencias) {
		this.licencias = licencias;
	}

	public String getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(String organizacion) {
		this.organizacion = organizacion;
	}

	public String getCargo() {
		return cargo;
	}

	public void setRolId(Integer rolid) {
		this.rolId = rolid;
	}
	
	public Integer getRolId() {
		return rolId;
	}

	public void setRolNombre(ArrayList<String> rolnombre) {
		this.rolNombre = rolnombre;
	}

	public ArrayList<String> getRolNombre() {
		return rolNombre;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	public String getNombreTipoPersona() {
		return tipoPersonaNombre;
	}

	public void setNombreTipoPersona(String nombreTipoPers) {
		this.tipoPersonaNombre = nombreTipoPers;
	}

	public String getNombreTipoIdenti() {
		return tipoIdentNombre;
	}

	public void setNombreTipoIdenti(String nombreTipoIdent) {
		this.tipoIdentNombre = nombreTipoIdent;
	}

	public String getNombrePais() {
		return paisNombre;
	}

	public void setNombrePais(String pais) {
		this.paisNombre = pais;
	}

	public String getNombreDepto() {
		return departamentoNombre;
	}

	public void setNombreDepto(String depto) {
		this.departamentoNombre = depto;
	}
	
	public String getNombreCiudad() {
		return ciudadNombre;
	}

	public void setNombreCiudad(String ciudad) {
		this.ciudadNombre = ciudad;
	}

	public String getNombresLicencias() {
		return licenciasNombres;
	}

	public void setNombresLicencias(String lic) {
		this.licenciasNombres = lic;
	}

}
