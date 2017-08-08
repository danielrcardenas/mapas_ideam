// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Clase que representa una Parcela
 */
public class Parcela {

	private Integer consecutivo;
	private String nombre;
	private String perteneceProyecto;
	private Timestamp fechaEstablecimiento;
	private String aprovechamiento;
	private String colecciones;
	private String investigador;
	private BigDecimal area;
	private String verificado;
	private String inventarioPublico;
	private Integer estado;
	private Integer temporalidad;
	private Integer inventario;
	private Integer fgda;
	private Integer tipoAutor;
	private Integer tipoCustodio;
	private Integer metadato;
	private Integer idFgda;
	private Integer idAutor;
	private Integer idCustodio;
	private Integer idInvestigador;
	private Integer idEncargado;
	private Integer tipoColeccion;
	private Integer idColeccion;
	private BigDecimal largoParcela;
	private BigDecimal anchoParcela;
	private BigDecimal radioParcela;
	private String observaciones;
	private String rutaImagen;
	private String nombreImagen;
	private Integer pais;
	private Integer proposito;
	private String codigoCampo;
	private Integer forma;
	private String descripcion;

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

	public String getAprovechamiento() {
		return aprovechamiento;
	}

	public void setAprovechamiento(String aprovechamiento) {
		this.aprovechamiento = aprovechamiento;
	}

	public String getColecciones() {
		return colecciones;
	}

	public void setColecciones(String colecciones) {
		this.colecciones = colecciones;
	}

	public String getInvestigador() {
		return investigador;
	}

	public void setInvestigador(String investigador) {
		this.investigador = investigador;
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public String getVerificado() {
		return verificado;
	}

	public void setVerificado(String verificado) {
		this.verificado = verificado;
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

	public Integer getInventario() {
		return inventario;
	}

	public void setInventario(Integer inventario) {
		this.inventario = inventario;
	}

	public Integer getFgda() {
		return fgda;
	}

	public void setFgda(Integer fgda) {
		this.fgda = fgda;
	}

	public Integer getTipoAutor() {
		return tipoAutor;
	}

	public void setTipoAutor(Integer tipoAutor) {
		this.tipoAutor = tipoAutor;
	}

	public Integer getTipoCustodio() {
		return tipoCustodio;
	}

	public void setTipoCustodio(Integer tipoCustodio) {
		this.tipoCustodio = tipoCustodio;
	}

	public Integer getMetadato() {
		return metadato;
	}

	public void setMetadato(Integer metadato) {
		this.metadato = metadato;
	}

	public Integer getIdFgda() {
		return idFgda;
	}

	public void setIdFgda(Integer idFgda) {
		this.idFgda = idFgda;
	}

	public Integer getIdAutor() {
		return idAutor;
	}

	public void setIdAutor(Integer idAutor) {
		this.idAutor = idAutor;
	}

	public Integer getIdCustodio() {
		return idCustodio;
	}

	public void setIdCustodio(Integer idCustodio) {
		this.idCustodio = idCustodio;
	}

	public Integer getIdInvetigador() {
		return idInvestigador;
	}

	public void setIdInvetigador(Integer idInvetigador) {
		this.idInvestigador = idInvetigador;
	}

	public Integer getIdEncargado() {
		return idEncargado;
	}

	public void setIdEncargado(Integer idEncargado) {
		this.idEncargado = idEncargado;
	}

	public Integer getTipoColeccion() {
		return tipoColeccion;
	}

	public void setTipoColeccion(Integer tipoColeccion) {
		this.tipoColeccion = tipoColeccion;
	}

	public Integer getIdColeccion() {
		return idColeccion;
	}

	public void setIdColeccion(Integer idColeccion) {
		this.idColeccion = idColeccion;
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

	public Integer getPais() {
		return pais;
	}

	public void setPais(Integer pais) {
		this.pais = pais;
	}

	public Timestamp getFechaEstablecimiento() {
		return fechaEstablecimiento;
	}

	public void setFechaEstablecimiento(Timestamp fechaEstablecimiento) {
		this.fechaEstablecimiento = fechaEstablecimiento;
	}

	public Integer getProposito() {
		return proposito;
	}

	public void setProposito(Integer proposito) {
		this.proposito = proposito;
	}

	public String getCodigoCampo() {
		return codigoCampo;
	}

	public void setCodigoCampo(String codigoCampo) {
		this.codigoCampo = codigoCampo;
	}

	public Integer getForma() {
		return forma;
	}

	public void setForma(Integer forma) {
		this.forma = forma;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
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

}