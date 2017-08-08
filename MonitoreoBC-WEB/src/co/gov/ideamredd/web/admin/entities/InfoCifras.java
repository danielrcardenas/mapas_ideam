// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.entities;

/**
 * Clase que representa datos de bosque no bosque
 */
public class InfoCifras {
	
	private Double Bosque;
	private Double NoBosque;
	private Double SinInfo;
	private String Nombre;
	
	public void setBosque(Double dat)
	{
		Bosque=dat;
	}
	public Double getBosque()
	{
		return Bosque;
	}
	
	public void setNoBosque(Double dat)
	{
		NoBosque = dat;
	}
	public Double getNoBosque()
	{
		return NoBosque;
	}
	
	public void setSinInfo(Double dat)
	{
		SinInfo=dat;
	}
	public Double getSinInfo()
	{
		return SinInfo;
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
