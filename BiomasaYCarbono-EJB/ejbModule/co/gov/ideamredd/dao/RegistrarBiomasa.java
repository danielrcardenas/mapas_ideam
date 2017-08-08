package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;

@Stateless
public class RegistrarBiomasa {

	@EJB
	private ConexionBD conexionBD;
	
	private CalculoBiomasa calculoBiomasa;

	private Connection conn;
	private Integer idParcela;
	// private ArrayList<String> codigoCampo;
	private Integer opcionGenaracion;
	private Double bio;
	private Double car;
	private String fechaInicio;
	private String fechaFin;
	private String nombreMetodologia;
	private Integer metodologia = 1;
	private String descripcionMetodologia;
	private String ecuacion;
	private static final Double factorCarbono = 0.5;
	private Integer estado = 1;
	private Integer atipicos;

	public void registrarBiomasa() {
		if (opcionGenaracion == 1) {
			car = bio * factorCarbono;
			if (metodologia != 1)
				registrarMetodologia();
		} else {
			 calculoBiomasa = new CalculoBiomasa();
			 calculoBiomasa.calcularBiomasaYCarbono(/*codigoCampo,idParcela*/);
			// biomasa = bio.getBiomasa();
			// carbono = bio.getCarbono();
			// fechaInicio = new Date(System.currentTimeMillis());
			// fechaFin = new Date(System.currentTimeMillis());
		}
		// try {
		// for(int i=0;i<idParcela.size();i++){
		registrarBiomasaYCarbono(idParcela, bio, car);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	private void registrarBiomasaYCarbono(Integer idParcela, Double biomasa,
			Double carbono) {
		try {
			conn = conexionBD.establecerConexion();
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			CallableStatement registrarBiomasa = conn
					.prepareCall("{call RED_PK_PARCELAS.registrar_BiomasaCarbono(?,?,?,?,?,?,?,?,?)}");
			registrarBiomasa.setDouble("una_biomasa", biomasa);
			registrarBiomasa.setDouble("un_carbono", carbono);
			registrarBiomasa.setString("una_fecha", fechaInicio.toString());
			registrarBiomasa.setString("una_fecha_fin", fechaFin == null ? null
					: sd.format(fechaFin));
			registrarBiomasa.setInt("un_estado", estado);
			registrarBiomasa.setInt("una_parcela", idParcela);
			registrarBiomasa.setInt("una_metodologia", /* metodologia */43);
			registrarBiomasa.setInt("un_tipo", opcionGenaracion);
			registrarBiomasa.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			registrarBiomasa.execute();
			System.out.println(registrarBiomasa.getObject("un_Resultado"));
			registrarBiomasa.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void registrarMetodologia() {
		conn = conexionBD.establecerConexion();
		CallableStatement registrarMetodologiaBiomasa;
		try {
			registrarMetodologiaBiomasa = conn
					.prepareCall("{call RED_PK_PARCELAS.registrar_MetodologiaBiomasa(?,?,?,?,?)}");
			registrarMetodologiaBiomasa.setString("un_nombre",
					nombreMetodologia);
			registrarMetodologiaBiomasa.setString("una_descripcion",
					descripcionMetodologia);
			registrarMetodologiaBiomasa.setString("una_ecuacion", ecuacion);
			registrarMetodologiaBiomasa.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			registrarMetodologiaBiomasa.registerOutParameter("un_consecutivo",
					OracleTypes.NUMBER);
			registrarMetodologiaBiomasa.execute();
			metodologia = Integer.valueOf(registrarMetodologiaBiomasa
					.getObject("un_consecutivo").toString());
			System.out.println(registrarMetodologiaBiomasa
					.getObject("un_Resultado"));
			registrarMetodologiaBiomasa.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] arg) {
	// RegistroDatosBiomasa r = new RegistroDatosBiomasa();
	// r.setOpcionGenaracion(2);
	// r.setBio(new Double(12.12));
	// ArrayList<Integer> parcela = new ArrayList<Integer>();
	// ArrayList<String> cc = new ArrayList<String>();
	// cc.add("P07");
	// parcela.add(246);
	// r.setIdParcela(parcela);
	// r.setCodigoCampo(cc);
	// r.registrarBiomasa();
	//
	//
	// }

	public Integer getOpcionGenaracion() {
		return opcionGenaracion;
	}

	public void setOpcionGenaracion(Integer opcionGenaracion) {
		this.opcionGenaracion = opcionGenaracion;
	}

	public Integer getMetodologia() {
		return metodologia;
	}

	public void setMetodologia(Integer metodologia) {
		this.metodologia = metodologia;
	}

	public String getDescripcionMetodologia() {
		return descripcionMetodologia;
	}

	public void setDescripcionMetodologia(String descripcionMetodologia) {
		this.descripcionMetodologia = descripcionMetodologia;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getNombreMetodologia() {
		return nombreMetodologia;
	}

	public void setNombreMetodologia(String nombreMetodologia) {
		this.nombreMetodologia = nombreMetodologia;
	}

	public Double getBio() {
		return bio;
	}

	public void setBio(Double bio) {
		this.bio = bio;
	}

	public Double getCar() {
		return car;
	}

	public void setCar(Double car) {
		this.car = car;
	}

	public String getEcuacion() {
		return ecuacion;
	}

	public void setEcuacion(String ecuacion) {
		this.ecuacion = ecuacion;
	}

	public Integer getAtipicos() {
		return atipicos;
	}

	public void setAtipicos(Integer atipicos) {
		this.atipicos = atipicos;
	}

	public Integer getIdParcela() {
		return idParcela;
	}

	public void setIdParcela(Integer idParcela) {
		this.idParcela = idParcela;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

}
