package co.gov.ideamredd.entities;

import java.sql.Date;

public class Individuo {

	private Integer consecutivo;
	private Date fechaTomaDatos;
	private Integer consAutor;
	private String codigoCampoParcela;
	private String faja;
	private String cuadrante;
	private String subCuadrante;
	private String placa;
	private Double cap;
	private Double dap1;
	private Double dap2;
	private String talloPrincipal;
	private Integer alturaMedida;
	private Double vmenos;
	private Double vmas;
	private Double vdist;
	private String nombreComun;
	private String especie;
	private String ubicacionArchivo;
	private String nombreArchivo;

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Date getFechaTomaDatos() {
		return fechaTomaDatos;
	}

	public void setFechaTomaDatos(Date fechaTomaDatos) {
		this.fechaTomaDatos = fechaTomaDatos;
	}

	public Integer getConsAutor() {
		return consAutor;
	}

	public void setConsAutor(Integer consAutor) {
		this.consAutor = consAutor;
	}

	public String getCodigoCampoParcela() {
		return codigoCampoParcela;
	}

	public void setCodigoCampoParcela(String codigoCampoParcela) {
		this.codigoCampoParcela = codigoCampoParcela;
	}

	public String getFaja() {
		return faja;
	}

	public void setFaja(String faja) {
		this.faja = faja;
	}

	public String getCuadrante() {
		return cuadrante;
	}

	public void setCuadrante(String cuadrante) {
		this.cuadrante = cuadrante;
	}

	public String getSubCuadrante() {
		return subCuadrante;
	}

	public void setSubCuadrante(String subCuadrante) {
		this.subCuadrante = subCuadrante;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Double getCap() {
		return cap;
	}

	public void setCap(Double cap) {
		this.cap = cap;
	}

	public Double getDap1() {
		return dap1;
	}

	public void setDap1(Double dap1) {
		this.dap1 = dap1;
	}

	public Double getDap2() {
		return dap2;
	}

	public void setDap2(Double dap2) {
		this.dap2 = dap2;
	}

	public String getTalloPrincipal() {
		return talloPrincipal;
	}

	public void setTalloPrincipal(String talloPrincipal) {
		this.talloPrincipal = talloPrincipal;
	}

	public Integer getAlturaMedida() {
		return alturaMedida;
	}

	public void setAlturaMedida(Integer alturaMedida) {
		this.alturaMedida = alturaMedida;
	}

	public Double getVmenos() {
		return vmenos;
	}

	public void setVmenos(Double vmenos) {
		this.vmenos = vmenos;
	}

	public Double getVmas() {
		return vmas;
	}

	public void setVmas(Double vmas) {
		this.vmas = vmas;
	}

	public Double getVdist() {
		return vdist;
	}

	public void setVdist(Double vdist) {
		this.vdist = vdist;
	}

	public String getNombreComun() {
		return nombreComun;
	}

	public void setNombreComun(String nombreComun) {
		this.nombreComun = nombreComun;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getUbicacionArchivo() {
		return ubicacionArchivo;
	}

	public void setUbicacionArchivo(String ubicacionArchivo) {
		this.ubicacionArchivo = ubicacionArchivo;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

}
