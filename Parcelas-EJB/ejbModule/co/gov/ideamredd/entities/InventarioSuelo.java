package co.gov.ideamredd.entities;

public class InventarioSuelo {

	private int parcelaId;
	private String Parcela;
	private String departamentoParcela;
	private String municipioParcela;
	private int contactoId;
	private String contactoNombre;
	private String contactoPais;
	private String contactoTelefono;
	private String contactoMovil;
	private String contactoCorreo;
	private String contactoMunicipio;
	private int metodologiaId;
	private String metodologiaNombre;
	private String metodologiaEcuacion;
	private String metodologiaArchivo;
	private String profundidadToma;
	private String textura;
	private String densidadAparente;
	private String flujoCO2;
	private String fechaTomaDatos;

	public void setParcelaId(int parcelaId) {
		this.parcelaId = parcelaId;
	}

	public int getParcelaId() {
		return this.parcelaId;
	}

	public void setParcela(String nomParcela) {
		this.Parcela = nomParcela;
	}

	public String getParcela() {
		return this.Parcela;
	}

	public void setDepartamentoP(String depParcela) {
		this.departamentoParcela = depParcela;
	}

	public String getDepartamentoP() {
		return this.departamentoParcela;
	}

	public void setMunicipioP(String munParcela) {
		this.municipioParcela = munParcela;
	}

	public String getMunicipioP() {
		return this.municipioParcela;
	}
	
	public void setContactoId(int contactoId) {
		this.contactoId = contactoId;
	}

	public int getContactoId() {
		return this.contactoId;
	}
	
	public void setContactoNombre(String nombre) {
		this.contactoNombre = nombre;
	}

	public String getContactoNombre() {
		return this.contactoNombre;
	}
	
	public void setContactoPais(String pais) {
		this.contactoPais = pais;
	}

	public String getContactoPais() {
		return this.contactoPais;
	}
	
	public void setContactoTelefono(String telefono) {
		this.contactoTelefono = telefono;
	}

	public String getContactoTelefono() {
		return this.contactoTelefono;
	}
	
	public void setContactoMovil(String movil) {
		this.contactoMovil = movil;
	}

	public String getContactoMovil() {
		return this.contactoMovil;
	}
	
	public void setContactoCorreo(String correo) {
		this.contactoCorreo = correo;
	}

	public String getContactoCorreo() {
		return this.contactoCorreo;
	}
	
	public void setContactoMunicipio(String municipio) {
		this.contactoMunicipio = municipio;
	}

	public String getContactoMunicipio() {
		return this.contactoMunicipio;
	}
	
	public void setMetodologiaId(int id) {
		this.metodologiaId = id;
	}

	public int getMetodologiaId() {
		return this.metodologiaId;
	}
	
	public void setMetodologiaNombre(String nombre) {
		this.metodologiaNombre = nombre;
	}

	public String getMetodologiaNombre() {
		return this.metodologiaNombre;
	}
	
	public void setMetodologiaEcuacion(String ecuacion) {
		this.metodologiaEcuacion = ecuacion;
	}

	public String getMetodologiaEcuacion() {
		return this.metodologiaEcuacion;
	}
	
	public void setMetodologiaArchivo(String archivo) {
		this.metodologiaArchivo = archivo;
	}

	public String getMetodologiaArchivo() {
		return this.metodologiaArchivo;
	}

	public void setProfundidadToma(String profToma) {
		this.profundidadToma = profToma;
	}

	public String getProfundidadToma() {
		return this.profundidadToma;
	}

	public void setTextura(String textura) {
		this.textura = textura;
	}

	public String getTextura() {
		return this.textura;
	}

	public void setDensidadAparente(String densAparente) {
		this.densidadAparente = densAparente;
	}

	public String getDensidadAparente() {
		return this.densidadAparente;
	}

	public void setFlujoCO2(String textura) {
		this.flujoCO2 = textura;
	}

	public String getFlujoCO2() {
		return this.flujoCO2;
	}

	public void setFechaTomaDatos(String fecha) {
		this.fechaTomaDatos = fecha;
	}

	public String getFechaTomaDatos() {
		return this.fechaTomaDatos;
	}

}
