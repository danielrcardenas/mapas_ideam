// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.proyecto.entities;

import java.util.Date;

/**
 * Clase que representa un Reporte de proyecto
 */
public class ReporteProyecto {
	
	private Integer Codigo_Proyecto;
	private String Nombre_Proyecto;
	private Double Area_Proyecto;
	private String Descripcion_Area;
	private String Nombre_Pais;
	private String Nombre_Departamento;
	private String Nombre_Municipio;
	private String Tipo_De_Bosque;
	private String CAR;
	private String Tipo_De_Proyecto;
	private Date Fecha_De_Inicio;
	private Date Fecha_De_Finalizacion;
	private String Tenencia_De_La_Tierra;
	private String Actividad_REDD;
	private Double Cantidad_De_CO2_A_Reducir;
	private Double Tasa_Deforestacion_A_Reducir;
	private String Nombre_Metodologia;
	private String Descripcion_Metodologia;
	private String Ecuacion_Metodologia;
	
	public Integer getCodigo_Proyecto() {
		return Codigo_Proyecto;
	}
	public void setCodigo_Proyecto(Integer codigo_Proyecto) {
		Codigo_Proyecto = codigo_Proyecto;
	}
	public String getNombre_Proyecto() {
		return Nombre_Proyecto;
	}
	public void setNombre_Proyecto(String nombre_Proyecto) {
		Nombre_Proyecto = nombre_Proyecto;
	}
	public Double getArea_Proyecto() {
		return Area_Proyecto;
	}
	public void setArea_Proyecto(Double area_Proyecto) {
		Area_Proyecto = area_Proyecto;
	}
	public String getDescripcion_Area() {
		return Descripcion_Area;
	}
	public void setDescripcion_Area(String descripcion_Area) {
		Descripcion_Area = descripcion_Area;
	}
	public String getNombre_Pais() {
		return Nombre_Pais;
	}
	public void setNombre_Pais(String nombre_Pais) {
		Nombre_Pais = nombre_Pais;
	}
	public String getNombre_Departamento() {
		return Nombre_Departamento;
	}
	public void setNombre_Departamento(String nombre_Departamento) {
		Nombre_Departamento = nombre_Departamento;
	}
	public String getNombre_Municipio() {
		return Nombre_Municipio;
	}
	public void setNombre_Municipio(String nombre_Municipio) {
		Nombre_Municipio = nombre_Municipio;
	}
	public String getTipo_De_Bosque() {
		return Tipo_De_Bosque;
	}
	public void setTipo_De_Bosque(String tipo_De_Bosque) {
		Tipo_De_Bosque = tipo_De_Bosque;
	}
	public String getCAR() {
		return CAR;
	}
	public void setCAR(String cAR) {
		CAR = cAR;
	}
	public String getTipo_De_Proyecto() {
		return Tipo_De_Proyecto;
	}
	public void setTipo_De_Proyecto(String tipo_De_Proyecto) {
		Tipo_De_Proyecto = tipo_De_Proyecto;
	}
	public Date getFecha_De_Inicio() {
		return Fecha_De_Inicio;
	}
	public void setFecha_De_Inicio(Date fecha_De_Inicio) {
		Fecha_De_Inicio = fecha_De_Inicio;
	}
	public Date getFecha_De_Finalizacion() {
		return Fecha_De_Finalizacion;
	}
	public void setFecha_De_Finalizacion(Date fecha_De_Finalizacion) {
		Fecha_De_Finalizacion = fecha_De_Finalizacion;
	}
	public String getTenencia_De_La_Tierra() {
		return Tenencia_De_La_Tierra;
	}
	public void setTenencia_De_La_Tierra(String tenencia_De_La_Tierra) {
		Tenencia_De_La_Tierra = tenencia_De_La_Tierra;
	}
	public String getActividad_REDD() {
		return Actividad_REDD;
	}
	public void setActividad_REDD(String actividad_REDD) {
		Actividad_REDD = actividad_REDD;
	}
	public Double getCantidad_De_CO2_A_Reducir() {
		return Cantidad_De_CO2_A_Reducir;
	}
	public void setCantidad_De_CO2_A_Reducir(Double cantidad_De_CO2_A_Reducir) {
		Cantidad_De_CO2_A_Reducir = cantidad_De_CO2_A_Reducir;
	}
	public Double getTasa_Deforestacion_A_Reducir() {
		return Tasa_Deforestacion_A_Reducir;
	}
	public void setTasa_Deforestacion_A_Reducir(
			Double tasa_Deforestacion_A_Reducir) {
		Tasa_Deforestacion_A_Reducir = tasa_Deforestacion_A_Reducir;
	}
	public String getNombre_Metodologia() {
		return Nombre_Metodologia;
	}
	public void setNombre_Metodologia(String nombre_Metodologia) {
		Nombre_Metodologia = nombre_Metodologia;
	}
	public String getDescripcion_Metodologia() {
		return Descripcion_Metodologia;
	}
	public void setDescripcion_Metodologia(String descripcion_Metodologia) {
		Descripcion_Metodologia = descripcion_Metodologia;
	}
	public String getEcuacion_Metodologia() {
		return Ecuacion_Metodologia;
	}
	public void setEcuacion_Metodologia(String ecuacion_Metodologia) {
		Ecuacion_Metodologia = ecuacion_Metodologia;
	}

}
