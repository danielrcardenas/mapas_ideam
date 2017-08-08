package co.gov.ideamredd.mbc.entities;

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
