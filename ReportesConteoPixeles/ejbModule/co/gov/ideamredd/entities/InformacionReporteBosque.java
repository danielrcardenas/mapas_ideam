package co.gov.ideamredd.entities;

public class InformacionReporteBosque {

	private Integer consecutivo;
	private Integer idReporte;
	private String nombre;
	private Double bosque;
	private Double noBosque;
	private Double sinInformacion;
	private Double porcentaje;
	private Double porcentajeBosque;
	private Double porcentajeNoBosque;
	private Double porcentajeSinInfo;

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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getBosque() {
		return bosque;
	}

	public void setBosque(Double bosque) {
		this.bosque = bosque;
	}

	public Double getNoBosque() {
		return noBosque;
	}

	public void setNoBosque(Double noBosque) {
		this.noBosque = noBosque;
	}

	public Double getSinInformacion() {
		return sinInformacion;
	}

	public void setSinInformacion(Double sinInformacion) {
		this.sinInformacion = sinInformacion;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public Double getPorcentajeBosque() {
		return porcentajeBosque;
	}

	public void setPorcentajeBosque(Double porcentajeBosque) {
		this.porcentajeBosque = porcentajeBosque;
	}

	public Double getPorcentajeNoBosque() {
		return porcentajeNoBosque;
	}

	public void setPorcentajeNoBosque(Double porcentajeNoBosque) {
		this.porcentajeNoBosque = porcentajeNoBosque;
	}

	public Double getPorcentajeSinInfo() {
		return porcentajeSinInfo;
	}

	public void setPorcentajeSinInfo(Double porcentajeSinInfo) {
		this.porcentajeSinInfo = porcentajeSinInfo;
	}

}
