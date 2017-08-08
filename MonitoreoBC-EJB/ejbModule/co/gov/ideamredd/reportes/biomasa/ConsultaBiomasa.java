// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.reportes.biomasa;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.reportes.entities.BiomasaYCarbono;
import co.gov.ideamredd.reportes.entities.MetodologiaBiomasa;
import co.gov.ideamredd.util.SMBC_Log;

/**
 * ConsultaBiomasa.java.
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Biomasa y Carbono
 */
@Stateless
public class ConsultaBiomasa {

	@EJB
	ConexionBD conexion;

	private String idParcela = "-1";
	private Connection conn;
	private BiomasaYCarbono biomasaYCarbono;
	private MetodologiaBiomasa metodologiaBiomasa;
	private ArrayList<BiomasaYCarbono> biomasas;
	private CallableStatement biomasaParcela;
	private Logger log;
	private ResultSet resultSet;
	private CallableStatement consultaMetodologiaBiomasa;

	public ArrayList<BiomasaYCarbono> consultarBiomasa() {
		try {
			biomasas = new ArrayList<BiomasaYCarbono>();

			log = SMBC_Log.Log(ConsultaBiomasa.class);
			conn = conexion.establecerConexion();
			biomasaParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.Consultar_BiomasaParcela(?,?,?)}");
			biomasaParcela.setString("una_Parcela", idParcela);
			biomasaParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			biomasaParcela.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			biomasaParcela.execute();
			System.out.println(biomasaParcela.getObject("sentencia"));
			resultSet = (ResultSet) biomasaParcela.getObject("un_Resultado");

			while (resultSet.next()) {
				biomasaYCarbono = new BiomasaYCarbono();
				biomasaYCarbono.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				biomasaYCarbono.setBiomasa(Double.valueOf(resultSet
						.getObject(2).toString()));
				biomasaYCarbono.setCarbono(Double.valueOf(resultSet
						.getObject(3).toString()));
				biomasaYCarbono.setFechaInicio((Timestamp) resultSet
						.getObject(4));
				biomasaYCarbono.setEstado(Integer.valueOf(resultSet
						.getObject(5).toString()));
				biomasaYCarbono.setIdParcela(Integer.valueOf(resultSet
						.getObject(6).toString()));
				biomasaYCarbono.setMetodologia(Integer.valueOf(resultSet
						.getObject(7).toString()));
				biomasaYCarbono
						.setAreaBasalPromedio(resultSet.getObject(8) == null ? new Double(
								0) : Double.valueOf(resultSet.getObject(8)
								.toString()));
				biomasaYCarbono
						.setAreaBasaltotal(resultSet.getObject(9) == null ? new Double(
								0) : Double.valueOf(resultSet.getObject(9)
								.toString()));
				biomasaYCarbono
						.setVolumen(resultSet.getObject(10) == null ? new Double(
								0) : Double.valueOf(resultSet.getObject(10)
								.toString()));
				biomasaYCarbono.setTipoGeneracion(Integer.valueOf(resultSet
						.getObject(11).toString()));
				biomasaYCarbono.setFecha_fin((Timestamp) resultSet
						.getObject(12));
				biomasas.add(biomasaYCarbono);
			}

			log.info("[consultarBiomasa] Termino");
			resultSet.close();
			biomasaParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultarBiomasa] Fallo");
			e.printStackTrace();
		}
		return biomasas;
	}

	public MetodologiaBiomasa consultarMetodologiaBiomasa(
			Integer idMetodologia, String nombreMetodologia) {
		try {
			log = SMBC_Log.Log(ConsultaBiomasa.class);
			metodologiaBiomasa = new MetodologiaBiomasa();
			consultaMetodologiaBiomasa = conn
					.prepareCall("{call RED_PK_PARCELAS.consultar_MetodologiaBiomasa(?,?,?)}");
			consultaMetodologiaBiomasa.setInt("un_consecutivo", idMetodologia);
			consultaMetodologiaBiomasa
					.setString("un_nombre", nombreMetodologia);
			consultaMetodologiaBiomasa.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaMetodologiaBiomasa.execute();
			resultSet = (ResultSet) consultaMetodologiaBiomasa
					.getObject("un_Resultado");
			while (resultSet.next()) {
				metodologiaBiomasa.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				metodologiaBiomasa.setNombre(resultSet.getObject(2).toString());
				metodologiaBiomasa.setDescripcion(resultSet.getObject(3)
						.toString());
			}

			log.info("[consultarMetodologiaBiomasa] Termino");
			resultSet.close();
			consultaMetodologiaBiomasa.close();
			conn.close();
		} catch (SQLException e) {
			log.error("[consultarMetodologiaBiomasa] Fallo");
			e.printStackTrace();
		}
		return metodologiaBiomasa;
	}

	/*
	 * public static void main(String[] args) { ConsultaBiomasa c = new
	 * ConsultaBiomasa(); // c.setIdParcela(95); //
	 * c.setNombreParcela("Prueba Parcela"); // c.setCodigoCampo("k4"); try {
	 * c.consultarBiomasa(); } catch (SQLException e) { e.printStackTrace(); } }
	 */

	public String getIdParcela() {
		return idParcela;
	}

	public void setIdParcela(String idParcela) {
		this.idParcela = idParcela;
	}

}
