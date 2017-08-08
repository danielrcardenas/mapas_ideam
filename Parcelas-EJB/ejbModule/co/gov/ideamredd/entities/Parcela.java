package co.gov.ideamredd.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Parcela {

	private Integer consecutivo;
	private String nombre;
	private String perteneceProyecto;
	private Timestamp fechaEstablecimiento;
	private String aprovechamiento;
	private BigDecimal area;
	private String inventarioPublico;
	private Integer temporalidad;
	private BigDecimal largoParcela;
	private BigDecimal anchoParcela;
	private BigDecimal radioParcela;
	private String observaciones;
	private String rutaImagen;
	private String nombreImagen;
	private String codigoCampo;
	private String descripcion;
	private String publica;
	private Integer inventario;
	private Integer estado;
	private Integer metadato;
	private Integer proposito;
	private Integer forma;
	private Integer pais;

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

	public String getPerteneceProyecto() {
		return perteneceProyecto;
	}

	public void setPerteneceProyecto(String perteneceProyecto) {
		this.perteneceProyecto = perteneceProyecto;
	}

	public Timestamp getFechaEstablecimiento() {
		return fechaEstablecimiento;
	}

	public void setFechaEstablecimiento(Timestamp fechaEstablecimiento) {
		this.fechaEstablecimiento = fechaEstablecimiento;
	}

	public String getAprovechamiento() {
		return aprovechamiento;
	}

	public void setAprovechamiento(String aprovechamiento) {
		this.aprovechamiento = aprovechamiento;
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public String getInventarioPublico() {
		return inventarioPublico;
	}

	public void setInventarioPublico(String inventarioPublico) {
		this.inventarioPublico = inventarioPublico;
	}

	public Integer getTemporalidad() {
		return temporalidad;
	}

	public void setTemporalidad(Integer temporalidad) {
		this.temporalidad = temporalidad;
	}

	public BigDecimal getLargoParcela() {
		return largoParcela;
	}

	public void setLargoParcela(BigDecimal largoParcela) {
		this.largoParcela = largoParcela;
	}

	public BigDecimal getAnchoParcela() {
		return anchoParcela;
	}

	public void setAnchoParcela(BigDecimal anchoParcela) {
		this.anchoParcela = anchoParcela;
	}

	public BigDecimal getRadioParcela() {
		return radioParcela;
	}

	public void setRadioParcela(BigDecimal radioParcela) {
		this.radioParcela = radioParcela;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public String getNombreImagen() {
		return nombreImagen;
	}

	public void setNombreImagen(String nombreImagen) {
		this.nombreImagen = nombreImagen;
	}

	public String getCodigoCampo() {
		return codigoCampo;
	}

	public void setCodigoCampo(String codigoCampo) {
		this.codigoCampo = codigoCampo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPublica() {
		return publica;
	}

	public void setPublica(String publica) {
		this.publica = publica;
	}

	public Integer getInventario() {
		return inventario;
	}

	public void setInventario(Integer inventario) {
		this.inventario = inventario;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Integer getMetadato() {
		return metadato;
	}

	public void setMetadato(Integer metadato) {
		this.metadato = metadato;
	}

	public Integer getProposito() {
		return proposito;
	}

	public void setProposito(Integer proposito) {
		this.proposito = proposito;
	}

	public Integer getForma() {
		return forma;
	}

	public void setForma(Integer forma) {
		this.forma = forma;
	}

	public Integer getPais() {
		return pais;
	}

	public void setPais(Integer pais) {
		this.pais = pais;
	}

}