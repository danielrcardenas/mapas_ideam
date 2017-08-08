// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Clase que representa un Proyecto
 */
public class Proyecto {

	private Integer consecutivo;
	private String nombre;
	private String descripcionArea;
	private String tipoBosques;
	private Timestamp fechaInicio;
	private Timestamp fechaFin;
	private Integer pais;
	private Integer estado;
	private BigDecimal area;
	private BigDecimal co2Reducir;
	private BigDecimal tasaDeforestar;
	private Integer duracionProyecto;
	private Integer propietario;
	private Integer publico;

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

	public String getDescripcionArea() {
		return descripcionArea;
	}

	public void setDescripcionArea(String descripcionArea) {
		this.descripcionArea = descripcionArea;
	}

	public String getTipoBosques() {
		return tipoBosques;
	}

	public void setTipoBosques(String tipoBosques) {
		this.tipoBosques = tipoBosques;
	}

	public Integer getPais() {
		return pais;
	}

	public void setPais(Integer pais) {
		this.pais = pais;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public Integer getDuracionProyecto() {
		return duracionProyecto;
	}

	public void setDuracionProyecto(Integer duracionProyecto) {
		this.duracionProyecto = duracionProyecto;
	}

	public Timestamp getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Timestamp fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Timestamp getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Timestamp fechaFin) {
		this.fechaFin = fechaFin;
	}

	public BigDecimal getCo2Reducir() {
		return co2Reducir;
	}

	public void setCo2Reducir(BigDecimal co2Reducir) {
		this.co2Reducir = co2Reducir;
	}

	public BigDecimal getTasaDeforestar() {
		return tasaDeforestar;
	}

	public void setTasaDeforestar(BigDecimal tasaDeforestar) {
		this.tasaDeforestar = tasaDeforestar;
	}

	public Integer getPropietario() {
		return propietario;
	}

	public void setPropietario(Integer propietario) {
		this.propietario = propietario;
	}

	public Integer getPublico() {
		return publico;
	}

	public void setPublico(Integer publico) {
		this.publico = publico;
	}

}
