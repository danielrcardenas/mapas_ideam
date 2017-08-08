package co.gov.ideamredd.entities;

import java.math.BigDecimal;
import java.sql.Date;

public class InformacionReporteInventarios {

	private Integer consecutivo;
	private Integer numero;
	private Integer idReporte;
	private String numeroArbol;
	private String genero;
	private String especie;
	private String familia;
	private String parcela;
	
	private String rScriptPath;
	private String templatePath;
	private String resultName;
	
	private int departamento;
	private int tipoBosque;
	


	private double frecuenciaRelativa;
	private double dominancia;
	private double abundanciaRelativa;
	private double ivi;
	
	private BigDecimal biomasa;
	private BigDecimal carbono;
	private BigDecimal areaBasal;
	private BigDecimal volumen;
	private BigDecimal DAP;
	
	private Date fechaInicialFiltro;
	private Date fechaFinalFiltro;

	private double diversidadGamma;
	private double indiceShannon;
	private double indiceSimpson;

	public void setDiversidadGamma(double value){
		this.diversidadGamma=value;
	}
	public double getDiversidadGamma(){
		return diversidadGamma;
	}

	public void setIndiceShannon(double value){
		this.indiceShannon = value;
	}

	public double getIndiceShannon(){
		return indiceShannon;
	}

	public void setIndiceSimpson(double value){
		this.indiceSimpson = value;
	}
	public double getIndiceSimpson(){
		return indiceSimpson;
	}

	public InformacionReporteInventarios(){
		
	}
	public int getDepartamento() {
		return departamento;
	}

	public void setDepartamento(int departamento) {
		this.departamento = departamento;
	}

	public int getTipoBosque() {
		return tipoBosque;
	}

	public void setTipoBosque(int tipoBosque) {
		this.tipoBosque = tipoBosque;
	}

	public InformacionReporteInventarios(String numeroArbol, String genero, String especie, String familia,
			BigDecimal biomasa, BigDecimal carbono, BigDecimal areaBasal, BigDecimal volumen) {
		super();
		this.numeroArbol = numeroArbol;
		this.genero = genero;
		this.especie = especie;
		this.familia = familia;
		this.biomasa = biomasa;
		this.carbono = carbono;
		this.areaBasal = areaBasal;
		this.volumen = volumen;
	}

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Integer getIdReporte() {
		return idReporte;
	}

	public void setIdReporte(Integer idReporte) {
		this.idReporte = idReporte;
	}

	public String getNumeroArbol() {
		return numeroArbol;
	}

	public void setNumeroArbol(String numeroArbol) {
		this.numeroArbol = numeroArbol;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public BigDecimal getBiomasa() {
		return biomasa;
	}

	public void setBiomasa(BigDecimal biomasa) {
		this.biomasa = biomasa;
	}

	public BigDecimal getCarbono() {
		return carbono;
	}

	public void setCarbono(BigDecimal carbono) {
		this.carbono = carbono;
	}

	public BigDecimal getAreaBasal() {
		return areaBasal;
	}

	public void setAreaBasal(BigDecimal areaBasal) {
		this.areaBasal = areaBasal;
	}

	public BigDecimal getVolumen() {
		return volumen;
	}

	public void setVolumen(BigDecimal volumen) {
		this.volumen = volumen;
	}

	public Date getFechaInicialFiltro() {
		return fechaInicialFiltro;
	}

	public void setFechaInicialFiltro(Date fechaInicialFiltro) {
		this.fechaInicialFiltro = fechaInicialFiltro;
	}

	public Date getFechaFinalFiltro() {
		return fechaFinalFiltro;
	}

	public void setFechaFinalFiltro(Date fechaFinalFiltro) {
		this.fechaFinalFiltro = fechaFinalFiltro;
	}
	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}
	public String getrScriptPath() {
		return rScriptPath;
	}
	public void setrScriptPath(String rScriptPath) {
		this.rScriptPath = rScriptPath;
	}
	public String getTemplatePath() {
		return templatePath;
	}
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	public String getResultName() {
		return resultName;
	}
	public void setResultName(String resultName) {
		this.resultName = resultName;
	}
	public BigDecimal getDAP() {
		return DAP;
	}
	public void setDAP(BigDecimal dAP) {
		DAP = dAP;
	}
	public double getFrecuenciaRelativa() {
		return frecuenciaRelativa;
	}
	public void setFrecuenciaRelativa(double frecuenciaRelativa) {
		this.frecuenciaRelativa = frecuenciaRelativa;
	}
	public double getDominancia() {
		return dominancia;
	}
	public void setDominancia(double dominancia) {
		this.dominancia = dominancia;
	}
	public double getAbundanciaRelativa() {
		return abundanciaRelativa;
	}
	public void setAbundanciaRelativa(double abundanciaRelativa) {
		this.abundanciaRelativa = abundanciaRelativa;
	}
	public double getIvi() {
		return ivi;
	}
	public void setIvi(double ivi) {
		this.ivi = ivi;
	}
}
