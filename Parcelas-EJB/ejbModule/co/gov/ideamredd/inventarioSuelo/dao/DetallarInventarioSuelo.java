package co.gov.ideamredd.inventarioSuelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBDParcelas;
import co.gov.ideamredd.entities.InventarioSuelo;
import co.gov.ideamredd.util.SMBC_Log;

/**
 * DetallarInventarioSuelo.java Clase encargada de detallar los datos
 * de los inventarios de suelos seleccionados.
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Nov 7 de 2013
 */
@Stateless
public class DetallarInventarioSuelo {

	@EJB
	ConexionBDParcelas conexion;

	private Connection conn;
	private Logger log;
	private CallableStatement consultaMetodologia;
	private CallableStatement consultaContacto;
	private ResultSet resultSet;

	/**
	 * Metodo que consulta los datos de una metodologia
	 * 
	 * @param consecutivo
	 *            : id del inv de suelo
	 * @param invSuelo
	 *            : Inventario de suelo donde se guardaran los datos
	 *            consultados.
	 */
	public void consultaDatosMetodologia(int consecutivo,
			InventarioSuelo invSuelo) {
		try {
//			log = SMBC_Log.setLog(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			consultaMetodologia = conn
					.prepareCall("{call RED_PK_PARCELAS.CONSULTA_METOD_PARCELA(?,?,?)}");
			consultaMetodologia.setInt("un_Consecutivo", consecutivo);
			consultaMetodologia.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaMetodologia.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultaMetodologia.execute();

			resultSet = (ResultSet) consultaMetodologia
					.getObject("un_Resultado");

			while (resultSet.next()) {
				invSuelo.setMetodologiaId(resultSet.getInt(1));
				invSuelo.setMetodologiaNombre(resultSet.getString(2));
				invSuelo.setMetodologiaEcuacion(resultSet.getString(3));
				invSuelo.setMetodologiaArchivo(resultSet.getString(4));
			}

//			log.info("[consultaMetodologiaP] Termino");
			resultSet.close();
			consultaMetodologia.close();
			conn.close();
		} catch (Exception e) {
//			log.error("[consultaMetodologiaP] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que consulta los datos de un contacto
	 * 
	 * @param consecutivo
	 *            : id del inv. de suelo
	 * @param invSuelo
	 *            : Inventario de suelo donde se guardaran los datos
	 *            consultados.
	 */
	public void consultaDatosContacto(int consecutivo, InventarioSuelo invSuelo) {
		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			consultaContacto = conn
					.prepareCall("{call RED_PK_PARCELAS.CONSULTA_CONTAC_PARCELA(?,?,?)}");
			consultaContacto.setInt("un_Consecutivo", consecutivo);
			consultaContacto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaContacto.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultaContacto.execute();

			resultSet = (ResultSet) consultaContacto.getObject("un_Resultado");

			while (resultSet.next()) {
				invSuelo.setContactoId(resultSet.getInt(1));
				invSuelo.setContactoNombre(resultSet.getString(2));
				invSuelo.setContactoPais(resultSet.getString(3));
				invSuelo.setContactoTelefono(resultSet.getString(4));
				invSuelo.setContactoMovil(resultSet.getString(5));
				invSuelo.setContactoCorreo(resultSet.getString(6));
				invSuelo.setContactoMunicipio(resultSet.getString(7));
			}

//			log.info("[consultaContactoP] Termino");
			resultSet.close();
			consultaContacto.close();
			conn.close();

			consultaPaisContacto(Integer.parseInt(invSuelo.getContactoPais()),
					invSuelo);
			consultaMunicipioContacto(
					Integer.parseInt(invSuelo.getContactoMunicipio()), invSuelo);

		} catch (Exception e) {
//			log.error("[consultaContactoP] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que consulta el pais de un contacto
	 * 
	 * @param consecutivo
	 *            : id del pais.
	 * @param invSuelo
	 *            : Inventario de suelo donde se guardaran los datos
	 *            consultados.
	 */
	public void consultaPaisContacto(int consecutivo, InventarioSuelo invSuelo) {
		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			consultaContacto = conn
					.prepareCall("{call RED_PK_PARCELAS.CONSULTA_PAIS(?,?,?)}");
			consultaContacto.setInt("UN_PAISID", consecutivo);
			consultaContacto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaContacto.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultaContacto.execute();

			resultSet = (ResultSet) consultaContacto.getObject("un_Resultado");

			while (resultSet.next()) {
				invSuelo.setContactoPais(resultSet.getString(2));
			}

//			log.info("[consultaContactoP] Termino");
			resultSet.close();
			consultaContacto.close();
			conn.close();
		} catch (Exception e) {
//			log.error("[consultaContactoP] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que consulta el municipio de un contacto
	 * 
	 * @param consecutivo
	 *            : id del municipio.
	 * @param invSuelo
	 *            : Inventario de suelo donde se guardaran los datos
	 *            consultados.
	 */
	public void consultaMunicipioContacto(int consecutivo,
			InventarioSuelo invSuelo) {
		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			consultaContacto = conn
					.prepareCall("{call RED_PK_PARCELAS.CONSULTA_MUNICIPIO(?,?,?)}");
			consultaContacto.setInt("UN_MUNICIPIOID", consecutivo);
			consultaContacto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaContacto.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultaContacto.execute();

			resultSet = (ResultSet) consultaContacto.getObject("un_Resultado");

			while (resultSet.next()) {
				invSuelo.setContactoMunicipio(resultSet.getString(2));
			}

//			log.info("[consultaContactoP] Termino");
			resultSet.close();
			consultaContacto.close();
			conn.close();
		} catch (Exception e) {
//			log.error("[consultaContactoP] Fallo");
			e.printStackTrace();
		}
	}

}
