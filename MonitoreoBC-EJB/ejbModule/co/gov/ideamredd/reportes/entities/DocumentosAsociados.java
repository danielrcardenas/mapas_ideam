// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.entities;

import java.sql.Date;

/**
 * Clase que representa un documento asociado a un reporte
 */
public class DocumentosAsociados {

	private Integer consecutivo;
	private String un_NombreDocumento;
	private String una_Ruta;
	private Date una_FechaIngresoDocumento;
	private String es_Publico;
	private Integer un_TipoDocumento;
	private Integer una_Licencia;
	private Integer un_Proyecto;
	private Integer propietario;
	private String un_NombreTipoDoc;

	public String getUn_NombreDocumento() {
		return un_NombreDocumento;
	}

	public void setUn_NombreDocumento(String unNombreDocumento) {
		un_NombreDocumento = unNombreDocumento;
	}

	public String getUna_Ruta() {
		return una_Ruta;
	}

	public void setUna_Ruta(String unaRuta) {
		una_Ruta = unaRuta;
	}

	public Date getUna_FechaIngresoDocumento() {
		return una_FechaIngresoDocumento;
	}

	public void setUna_FechaIngresoDocumento(Date unaFechaIngresoDocumento) {
		una_FechaIngresoDocumento = unaFechaIngresoDocumento;
	}

	public String getEs_Publico() {
		return es_Publico;
	}

	public void setEs_Publico(String esPublico) {
		es_Publico = esPublico;
	}
	
	public Integer getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Integer consecutivo) {
		this.consecutivo = consecutivo;
	}

	public Integer getUn_Proyecto() {
		return un_Proyecto;
	}

	public void setUn_Proyecto(Integer unProyecto) {
		un_Proyecto = unProyecto;
	}

	public void setUn_TipoDocumento(Integer unTipoDocumento) {
		un_TipoDocumento = unTipoDocumento;
	}
	
	public Integer getUn_TipoDocumento() {
		return un_TipoDocumento;
	}

	public void setUna_Licencia(Integer unaLicencia) {
		una_Licencia = unaLicencia;
	}
	
	public Integer getUna_Licencia() {
		return una_Licencia;
	}

	public Integer getPropietario() {
		return propietario;
	}

	public void setPropietario(Integer propietario) {
		this.propietario = propietario;
	}

	public String getUn_NombreTipoDoc() {
		return un_NombreTipoDoc;
	}

	public void setUn_NombreTipoDoc(String unNombreTipoDoc) {
		un_NombreTipoDoc = unNombreTipoDoc;
	}

}
