package co.gov.ideamredd.inventarioSuelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.conexionBD.ConexionBDParcelas;
import co.gov.ideamredd.entities.InventarioSuelo;
import co.gov.ideamredd.entities.Metodologia;
import co.gov.ideamredd.logs.Transaccion;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.util.TransaccionUtil;

/**
 * ModificarInventarioSuelo.java Clase encargada de realizar las actualizaciones
 * a la base de datos de el inventario de suelos.
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Nov 7 de 2013
 */

@Stateless
public class ModificarInventarioSuelo {

	@EJB
	ConexionBDParcelas conexion;
	@EJB
	Transaccion transaccion;
	@EJB
	ConsultaInventarioSuelo cis;

	private Connection conn;
	private Logger log;
	private CallableStatement modificaISuelo;
	private CallableStatement consultaMetodologia;
	private CallableStatement insertaMetodologia;
	private String metodologiasLista;
	private ArrayList<Metodologia> metodologias;
	private ResultSet resultSet;

	public ModificarInventarioSuelo() {
		metodologias = new ArrayList<Metodologia>();
	}

	/**
	 * Metodo que actualiza un inventario de suelo.
	 * 
	 * @param parcela
	 *            : id de la parcela a consultar.
	 * @param metodologia
	 *            : id de la metodologia
	 * @param profundidad
	 *            : profundidad de toma.
	 * @param textura
	 *            : textura del inventario de suelo.
	 * @param densidad
	 *            : Densidad aparente.
	 * @param flujo
	 *            : Flujo CO2
	 * @param fecha
	 *            : Fecha de toma de datos.
	 */
	public void actualizaInventarioSuelo(Integer parcela, Integer metodologia,
			Integer profundidad, String textura, Integer densidad,
			Integer flujo, String fecha) {

		insertaHistoricoInventarioSuelo(parcela);

		try {
//			log = SMBC_Log.log(ModificarInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			modificaISuelo = conn
					.prepareCall("{call RED_PK_PARCELAS.ACTUALIZA_INVENTSUELO(?,?,?,?,?,?,?,?)}");

			modificaISuelo.setInt("UNA_PARCELA", parcela);
			modificaISuelo.setInt("UNA_METODOLOGIA", metodologia);
			modificaISuelo.setInt("UNA_PROFUNDIDAD", profundidad);
			modificaISuelo.setString("UNA_TEXTURA", textura);
			modificaISuelo.setInt("UNA_DENSIDAD", densidad);
			modificaISuelo.setInt("UN_FLUJO", flujo);
			modificaISuelo.setDate("UNA_fecha", java.sql.Date.valueOf(fecha));
			modificaISuelo.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			modificaISuelo.execute();

//			log.info("[actualizaInventarioSuelo] Termino");
			modificaISuelo.close();
			conn.close();
			transaccion.logTransaccion(parcela, TransaccionUtil.Tipo.ACTUALIZA,
					4, "Se actualizo un inventario de suelo de parcela",
					ModificarInventarioSuelo.class);
		} catch (Exception e) {
//			log.error("[actualizaInventarioSuelo] Fallo");
			e.printStackTrace();
		}

	}

	/**
	 * Metodo que actualiza un inventario de suelo, e inserta una nueva
	 * metodologia.
	 * 
	 * @param parcela
	 *            : id de la parcela a consultar.
	 * @param nombreMetodologia
	 *            : nombre de la metodologia
	 * @param ecuacionMetodologia
	 *            : ecuacion de la metodologia
	 * @param archivoMetodologia
	 *            : link al archivo de la metodologia
	 * @param profundidad
	 *            : profundidad de toma.
	 * @param textura
	 *            : textura del inventario de suelo.
	 * @param densidad
	 *            : Densidad aparente.
	 * @param flujo
	 *            : Flujo CO2
	 * @param fecha
	 *            : Fecha de toma de datos.
	 */
	public void actualizaInventarioSueloConMetodologia(Integer parcela,
			String nombreMetodologia, String ecuacionMetodologia,
			String archivoMetodologia, Integer profundidad, String textura,
			Integer densidad, Integer flujo, String fecha) {

		insertaHistoricoInventarioSuelo(parcela);

		try {
//			log = SMBC_Log.log(ModificarInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			insertaMetodologia(nombreMetodologia, ecuacionMetodologia,
					archivoMetodologia, fecha);
			int indice = consultaUltimaMetodologia();

			conn = conexion.establecerConexion();

			modificaISuelo = conn
					.prepareCall("{call RED_PK_PARCELAS.ACTUALIZA_INVENTSUELO(?,?,?,?,?,?,?,?)}");

			modificaISuelo.setInt("UNA_PARCELA", parcela);
			modificaISuelo.setInt("UNA_METODOLOGIA", indice);
			modificaISuelo.setInt("UNA_PROFUNDIDAD", profundidad);
			modificaISuelo.setString("UNA_TEXTURA", textura);
			modificaISuelo.setInt("UNA_DENSIDAD", densidad);
			modificaISuelo.setInt("UN_FLUJO", flujo);
			modificaISuelo.setDate("UNA_fecha", java.sql.Date.valueOf(fecha));
			modificaISuelo.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			modificaISuelo.execute();

//			log.info("[actualizaInventarioSueloConMetodologia] Termino");
			modificaISuelo.close();
			conn.close();
			transaccion.logTransaccion(parcela, TransaccionUtil.Tipo.ACTUALIZA,
					4, "Se actualizo un inventario de suelo de parcela",
					ModificarInventarioSuelo.class);
		} catch (Exception e) {
//			log.error("[actualizaInventarioSueloConMetodologia] Fallo");
			e.printStackTrace();
		}

	}

	/**
	 * Metodo que consulta todas las metodologias.
	 */
	public String consultarMetodologias() {
		try {
//			log = SMBC_Log.log(ModificarInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			consultaMetodologia = conn
					.prepareCall("{call RED_PK_PARCELAS.CONSULTA_METODOLOGIA(?,?)}");
			consultaMetodologia.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaMetodologia.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultaMetodologia.execute();

			resultSet = (ResultSet) consultaMetodologia
					.getObject("un_Resultado");

			while (resultSet.next()) {
				metodologias.add(new Metodologia(Integer.parseInt(resultSet
						.getString(1)), resultSet.getString(2)));
			}

//			log.info("[consultarMetodologias] Termino");
			resultSet.close();
			consultaMetodologia.close();
			conn.close();
		} catch (Exception e) {
//			log.error("[consultarMetodologias] Fallo");
			e.printStackTrace();
		}

		metodologiasLista = "";
		for (int i = 0; i < metodologias.size(); i++) {
			metodologiasLista = metodologiasLista + "<option value=\""
					+ metodologias.get(i).getMetodologiaId() + "\" "
					+ "class=\"" + metodologias.get(i).getMetodologiaNombre()
					+ "id=\"" + metodologias.get(i).getMetodologiaNombre()
					+ "\">" + metodologias.get(i).getMetodologiaNombre()
					+ "</option>";
		}

		return metodologiasLista;
	}

	/**
	 * Metodo que inserta una metodologia.
	 * 
	 * @param nombre
	 *            : nombre de la metodologia
	 * @param ecuacion
	 *            : ecuacion de la metodologia
	 * @param archivo
	 *            : link del archivo de la metodologia.
	 * @param fecha
	 *            : fecha
	 */
	public void insertaMetodologia(String nombre, String ecuacion,
			String archivo, String fecha) {

		try {
//			log = SMBC_Log.log(ModificarInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			insertaMetodologia = conn
					.prepareCall("{call RED_PK_PARCELAS.INSERTA_METODOLOGIA(?,?,?,?)}");

			insertaMetodologia.setString("UN_NOMBRE", nombre);
			insertaMetodologia.setString("UNA_ECUACION", ecuacion);
			insertaMetodologia.setString("UN_PATH", archivo);
			insertaMetodologia.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			insertaMetodologia.execute();

//			log.info("[insertaMetodologia] Termino");
			insertaMetodologia.close();
			conn.close();
			transaccion.logTransaccion(consultaUltimaMetodologia(),
					TransaccionUtil.Tipo.CREA, 4, "Se inserto una metodologia",
					ModificarInventarioSuelo.class);
		} catch (Exception e) {
//			log.error("[insertaMetodologia] Fallo");
			e.printStackTrace();
		}

	}

	/**
	 * Metodo que consulta la ultima metodologia insertada.
	 */
	public int consultaUltimaMetodologia() {
		int indice = 0;
		try {
//			log = SMBC_Log.log(ModificarInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			consultaMetodologia = conn
					.prepareCall("{call RED_PK_PARCELAS.CONSULTA_MAXMETODOLOGIA(?,?)}");
			consultaMetodologia.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaMetodologia.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultaMetodologia.execute();

			resultSet = (ResultSet) consultaMetodologia
					.getObject("un_Resultado");

			while (resultSet.next()) {
				indice = resultSet.getInt(1);
			}

//			log.info("[consultaMetodologiaPorNombre] Termino");
			resultSet.close();
			consultaMetodologia.close();
			conn.close();
		} catch (Exception e) {
//			log.error("[consultaMetodologiaPorNombre] Fallo");
			e.printStackTrace();
		}

		return indice;
	}

	public void insertaHistoricoInventarioSuelo(Integer parcela) {

		InventarioSuelo auxIS = new InventarioSuelo();

		cis.consultaNombreParcela(parcela, auxIS);
		cis.consultaMunicipioP(parcela, auxIS);
		cis.consultaDepartamentoP(parcela, auxIS);
		cis.consultaDatosMetodologia(parcela, auxIS);
		cis.consultaDatosContacto(parcela, auxIS);
		cis.consultaInventSuelosP(parcela, auxIS);

		try {
//			log = SMBC_Log.log(ModificarInventarioSuelo.class);
			conn = conexion.establecerConexion();

			modificaISuelo = conn
					.prepareCall("{call RED_PK_PARCELAS.INSERTA_HISTORI_INVSUELO(?,?,?,?,?,?,?,?)}");

			modificaISuelo.setInt("UN_IDPARCELA", auxIS.getParcelaId());
			modificaISuelo.setInt("UN_IDCONTACTO", auxIS.getContactoId());
			modificaISuelo.setInt("UN_IDMETODOLOGIA", auxIS.getMetodologiaId());
			modificaISuelo.setString("UNA_PROFUNDIDAD",
					auxIS.getProfundidadToma());
			modificaISuelo.setString("UNA_TEXTURA", auxIS.getTextura());
			modificaISuelo.setString("UNA_DENSIDAD",
					auxIS.getDensidadAparente());
			modificaISuelo.setString("UN_FLUJO", auxIS.getFlujoCO2());
			modificaISuelo.setDate(
					"UNA_FECHA",
					java.sql.Date.valueOf(auxIS.getFechaTomaDatos().replaceAll(
							" 00:00:00.0", "")));
			modificaISuelo.execute();

//			log.info("[insertaHistoricoInventarioSuelo] Termino");
			modificaISuelo.close();
			conn.close();
			transaccion.logTransaccion(parcela, TransaccionUtil.Tipo.CREA, 4,
					"Se inserto un historico de inventario de suelo",
					ModificarInventarioSuelo.class);
		} catch (Exception e) {
//			log.error("[insertaHistoricoInventarioSuelo] Fallo");
			e.printStackTrace();
		}

	}

	public void clearMetodologias() {
		this.metodologias.clear();
	}

}
