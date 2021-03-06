package co.gov.ideamredd.mbc.entities;

import java.math.BigDecimal;

public class InformacionReporteCobertura {

	private Integer consecutivo;
	private Integer numero;
	private Integer idReporte;
	private String tipoBosque;
	private String nombreTipoBosque;
	private String nombreAreaHidrografica;
	private Integer areaHidrografica;
	private Integer nivelIncertidumbre;
	private Double area;
	private BigDecimal deforestacion;
	private BigDecimal resforestacion;
	private BigDecimal BA;
	private BigDecimal C;
	private BigDecimal CO2;
	private Double porcentaje;

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Integer getIdReporte() {
		return idReporte;
	}

	public void setIdReporte(Integer idReporte) {
		this.idReporte = idReporte;
	}

	public String getTipoBosque() {
		return tipoBosque;
	}

	public void setTipoBosque(String tipoBosque) {
		this.tipoBosque = tipoBosque;
	}

	public String getNombreTipoBosque() {
		return nombreTipoBosque;
	}

	public void setNombreTipoBosque(String nombreTipoBosque) {
		this.nombreTipoBosque = nombreTipoBosque;
	}

	public String getNombreAreaHidrografica() {
		return nombreAreaHidrografica;
	}

	public void setNombreAreaHidrografica(String nombreAreaHidrografica) {
		this.nombreAreaHidrografica = nombreAreaHidrografica;
	}

	public Integer getAreaHidrografica() {
		return areaHidrografica;
	}

	public void setAreaHidrografica(Integer areaHidrografica) {
		this.areaHidrografica = areaHidrografica;
	}

	public Integer getNivIncertidumbre() {
		return nivelIncertidumbre;
	}

	public void setNivIncertidumbre(Integer nivIncertidumbre) {
		this.nivelIncertidumbre = nivIncertidumbre;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public BigDecimal getDeforestacion() {
		return deforestacion;
	}

	public void setDeforestacion(BigDecimal biomasa) {
		this.deforestacion = biomasa;
	}

	public BigDecimal getReforestacion() {
		return resforestacion;
	}

	public void setReforestacion(BigDecimal carbono) {
		this.resforestacion = carbono;
	}

	public BigDecimal getBA() {
		return BA;
	}

	public void setBA(BigDecimal bA) {
		BA = bA;
	}

	public BigDecimal getC() {
		return C;
	}

	public void setC(BigDecimal c) {
		C = c;
	}

	public BigDecimal getCO2() {
		return CO2;
	}

	public void setCO2(BigDecimal cO2) {
		CO2 = cO2;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

}
