package co.gov.ideamredd.inventarioSuelo.dao;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.conexionBD.ConexionBDParcelas;
import co.gov.ideamredd.entities.InventarioSuelo;
import co.gov.ideamredd.entities.InventarioSueloHistorico;
import co.gov.ideamredd.util.SMBC_Log;

/**
 * ConsultaInventarioSuelo.java Clase encargada de realizar las consultas a la
 * base de datos de el inventario de suelos.
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Oct 18 de 2013
 */
@Stateless
public class ConsultaInventarioSuelo {

	@EJB
	ConexionBDParcelas conexion;

	private static final String configNewMetodologia = "co/gov/ideamredd/recursos/newMetodologia";
	private static final ResourceBundle prop = ResourceBundle
			.getBundle(configNewMetodologia);
	private Connection conn;
	private Logger log;
	private ArrayList<InventarioSuelo> invSueloLista;
	private CallableStatement consultarISuelo;
	private CallableStatement consultaMetodologia;
	private CallableStatement consultaContacto;
	private CallableStatement consultarISueloHistorico;
	private ResultSet resultSet;

	public ConsultaInventarioSuelo() {
		invSueloLista = new ArrayList<InventarioSuelo>();
	}

	/**
	 * Metodo que consulta el nombre de una parcela
	 * 
	 * @param consecutivo
	 *            : id de la parcela a consultar
	 * @param invSuelo
	 *            : Inventario de suelo donde se guardaran los datos
	 *            consultados.
	 */
	public void consultaNombreParcela(int consecutivo, InventarioSuelo invSuelo) {
		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			// Se obtiene el nombre de la parcela
			consultarISuelo = conn
					.prepareCall("{call RED_PK_PARCELAS.Consulta_ParcelaPorConsecutivo(?,?,?)}");
			consultarISuelo.setInt("un_Consecutivo", consecutivo);
			consultarISuelo.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarISuelo.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultarISuelo.execute();
			System.out.println(consultarISuelo.getObject("sentencia"));

			resultSet = (ResultSet) consultarISuelo.getObject("un_Resultado");

			while (resultSet.next()) {
				invSuelo.setParcelaId(resultSet.getInt(1));
				invSuelo.setParcela(resultSet.getString(2));
			}

			log.info("[consultaNombreParcela] Termino");
			resultSet.close();
			consultarISuelo.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultaNombreParcela] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que consulta el departamento de una parcela
	 * 
	 * @param consecutivo
	 *            : id de la parcela a consultar
	 * @param invSuelo
	 *            : Inventario de suelo donde se guardaran los datos
	 *            consultados.
	 */
	public void consultaDepartamentoP(int consecutivo, InventarioSuelo invSuelo) {
		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			consultarISuelo = conn
					.prepareCall("{call RED_PK_PARCELAS.DeptoParcela_Consulta(?,?)}");
			consultarISuelo.setInt("una_parcela", consecutivo);
			consultarISuelo.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarISuelo.execute();

			resultSet = (ResultSet) consultarISuelo.getObject("un_Resultado");

			while (resultSet.next()) {
				invSuelo.setDepartamentoP(resultSet.getString(2));
			}

			log.info("[consultaDepartamentoP] Termino");
			resultSet.close();
			consultarISuelo.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultaDepartamentoP] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que consulta el municipio de una parcela
	 * 
	 * @param consecutivo
	 *            : id de la parcela a consultar
	 * @param invSuelo
	 *            : Inventario de suelo donde se guardaran los datos
	 *            consultados.
	 */
	public void consultaMunicipioP(int consecutivo, InventarioSuelo invSuelo) {
		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			consultarISuelo = conn
					.prepareCall("{call RED_PK_PARCELAS.MunicipioParcela_Consulta(?,?)}");
			consultarISuelo.setInt("una_parcela", consecutivo);
			consultarISuelo.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarISuelo.execute();

			resultSet = (ResultSet) consultarISuelo.getObject("un_Resultado");

			while (resultSet.next()) {
				invSuelo.setMunicipioP(resultSet.getString(2));
			}

			log.info("[consultaMunicipioP] Termino");
			resultSet.close();
			consultarISuelo.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultaMunicipioP] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que consulta los datos de la metodologia de un inventario de suelo
	 * 
	 * @param consecutivo
	 *            : id del inventario de suelo a consultar
	 * @param invSuelo
	 *            : Inventario de suelo donde se guardaran los datos
	 *            consultados.
	 */
	public void consultaDatosMetodologia(int consecutivo,
			InventarioSuelo invSuelo) {
		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
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
				if (resultSet.getString(4) != null) {
					invSuelo.setMetodologiaArchivo(resultSet.getString(4));
				} else {
					invSuelo.setMetodologiaArchivo(null);
				}
			}

			log.info("[consultaMetodologiaP] Termino");
			resultSet.close();
			consultaMetodologia.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultaMetodologiaP] Fallo");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que consulta los datos del contacto de un inv. de suelo.
	 * 
	 * @param consecutivo
	 *            : id de la parcela a consultar
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

			if (invSuelo.getContactoPais() != null) {
				consultaPaisContacto(
						Integer.parseInt(invSuelo.getContactoPais()), invSuelo);
				consultaMunicipioContacto(
						Integer.parseInt(invSuelo.getContactoMunicipio()),
						invSuelo);
			}

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
	 *            : id del municipio
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

	/**
	 * Metodo que consulta los datos del inventario de suelo
	 * 
	 * @param consecutivo
	 *            : id del inventario de suelo.
	 * @param invSuelo
	 *            : Inventario de suelo donde se guardaran los datos
	 *            consultados.
	 */
	public void consultaInventSuelosP(int consecutivo, InventarioSuelo invSuelo) {
		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			consultarISuelo = conn
					.prepareCall("{call RED_PK_PARCELAS.Consulta_consecInventSuelo(?,?,?)}");
			consultarISuelo.setInt("un_Consecutivo", consecutivo);
			consultarISuelo.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarISuelo.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultarISuelo.execute();

			resultSet = (ResultSet) consultarISuelo.getObject("un_Resultado");

			while (resultSet.next()) {
				invSuelo.setProfundidadToma(resultSet.getString(4));
				invSuelo.setTextura(resultSet.getString(5));
				invSuelo.setDensidadAparente(resultSet.getString(6));
				invSuelo.setFlujoCO2(resultSet.getString(7));
				invSuelo.setFechaTomaDatos(resultSet.getString(8));
			}

			if (invSuelo.getParcela() != null) {
				invSueloLista.add(invSuelo);
			} else {
				invSueloLista.clear();
			}

//			log.info("[consultaInventSuelosP] Termino");
			resultSet.close();
			consultarISuelo.close();
			conn.close();
		} catch (Exception e) {
//			log.error("[consultaInventSuelosP] Fallo");
			e.printStackTrace();
		}
	}

	public ArrayList<InventarioSuelo> getListaInvSuelos() {
		return invSueloLista;
	}

	public void clearinvSueloLista() {
		this.invSueloLista.clear();
	}
	
	/**
	 * Metodo que consulta el nombre de una parcela
	 * 
	 * @param consecutivo
	 *            : id de la parcela a consultar
	 * @param invSuelo
	 *            : Inventario de suelo donde se guardaran los datos
	 *            consultados.
	 */
	public void consultaHistoricoInvSuelo(int consecutivo, InventarioSueloHistorico invSueloHistorico) {
		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			// Se obtiene el nombre de la parcela
			consultarISueloHistorico = conn
					.prepareCall("{call RED_PK_PARCELAS.CONSULTA_HISTORI_INVSUELO(?,?,?)}");
			consultarISueloHistorico.setInt("un_Consecutivo", consecutivo);
			consultarISueloHistorico.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarISueloHistorico.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultarISueloHistorico.execute();

			resultSet = (ResultSet) consultarISuelo.getObject("un_Resultado");

			while (resultSet.next()) {
				invSueloHistorico.setParcelaId(resultSet.getInt(2));
				invSueloHistorico.setContactoId(resultSet.getInt(3));
				invSueloHistorico.setMetodologiaId(resultSet.getInt(4));
				/*invSueloHistorico.setParcela(resultSet.getString(5));
				invSueloHistorico.setParcelaId(resultSet.getInt(6));
				invSueloHistorico.setParcela(resultSet.getString(7));
				invSueloHistorico.setParcelaId(resultSet.getInt(8));
				invSueloHistorico.setParcela(resultSet.getString(9));
				invSueloHistorico.setParcelaId(resultSet.getInt(10));*/
			}

//			log.info("[consultaHistoricoInvSuelo] Termino");
			resultSet.close();
			consultarISueloHistorico.close();
			conn.close();
		} catch (Exception e) {
//			log.error("[consultaHistoricoInvSuelo] Fallo");
			e.printStackTrace();
		}
	}

}
