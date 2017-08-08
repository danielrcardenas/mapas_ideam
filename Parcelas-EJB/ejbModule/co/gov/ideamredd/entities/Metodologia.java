package co.gov.ideamredd.entities;

public class Metodologia {

	private int Id;
	private String nombre;
	private String ecuacion;
	private String dirArchivo;

	public Metodologia(int id, String nombre) {
		this.Id = id;
		this.nombre = nombre;
	}

	public Metodologia(int id, String nombre, String ecuacion, String dirArchivo) {
		this.Id = id;
		this.nombre = nombre;
		this.ecuacion = ecuacion;
		this.dirArchivo = dirArchivo;
	}

	public void setMetodologiaId(int metodId) {
		this.Id = metodId;
	}

	public int getMetodologiaId() {
		return this.Id;
	}

	public void setMetodologiaNombre(String nomMetod) {
		this.nombre = nomMetod;
	}

	public String getMetodologiaNombre() {
		return this.nombre;
	}

	public void setMetodologiaEcuacion(String ecuacion) {
		this.ecuacion = ecuacion;
	}

	public String getMetodologiaEcuacion() {
		return this.ecuacion;
	}

	public void setMetodologiaDirArchivo(String dirArchivo) {
		this.dirArchivo = dirArchivo;
	}

	public String getMetodologiaDirArchivo() {
		return this.dirArchivo;
	}

}
