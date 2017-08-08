// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.ejb.mbc.entities;

/**
 * Clase que representa un Area de Bosque
 */
public class BosqueArea {
	
	private Double Area;
	private String Nombre;
	
	public BosqueArea(String nombre, Double area){
		this.Nombre=nombre;
		this.Area=area;
	}
	
	public void setArea(Double dat)
	{
		Area=dat;
	}
	public Double getArea()
	{
		return Area;
	}
	
	public void setNombre(String dat)
	{
		Nombre = dat;
	}
	public String getNombre()
	{
		return Nombre;
	}

}
