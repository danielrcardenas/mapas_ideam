// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.entities;

public class TipoBosque {

	private Integer consecutivo;
	private Integer idBosque;
	private String tipoBosque;
	private Integer idParcela;
	private String precipitacion;
	private String temperatura;
	private String altitud;

	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getTipoBosque() {
		return tipoBosque;
	}

	public void setTipoBosque(String tipoBosque) {
		this.tipoBosque = tipoBosque;
	}

	public Integer getIdParcela() {
		return idParcela;
	}

	public void setIdParcela(Integer idParcela) {
		this.idParcela = idParcela;
	}

	public String getPrecipitacion() {
		return precipitacion;
	}

	public void setPrecipitacion(String precipitacion) {
		this.precipitacion = precipitacion;
	}

	public String getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}

	public String getAltitud() {
		return altitud;
	}

	public void setAltitud(String altitud) {
		this.altitud = altitud;
	}

	public Integer getIdBosque() {
		return idBosque;
	}

	public void setIdBosque(Integer idBosque) {
		this.idBosque = idBosque;
	}

}
