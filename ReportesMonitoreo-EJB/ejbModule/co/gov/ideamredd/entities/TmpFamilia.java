package co.gov.ideamredd.entities;

public class TmpFamilia {
	
	private Integer id;
	private String binomial;
	private String familia;
	private String genero;
	private String especie;
	private Double densidadFamilia;
	private Double densidadGenero;
	private Double densidadEspecie;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBinomial() {
		return binomial;
	}

	public void setBinomial(String binomial) {
		this.binomial = binomial;
	}

	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public Double getDensidadFamilia() {
		return densidadFamilia;
	}

	public void setDensidadFamilia(Double densidadFamilia) {
		this.densidadFamilia = densidadFamilia;
	}

	public Double getDensidadGenero() {
		return densidadGenero;
	}

	public void setDensidadGenero(Double densidadGenero) {
		this.densidadGenero = densidadGenero;
	}

	public Double getDensidadEspecie() {
		return densidadEspecie;
	}

	public void setDensidadEspecie(Double densidadEspecie) {
		this.densidadEspecie = densidadEspecie;
	}

}
