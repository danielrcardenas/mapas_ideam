package co.gov.ideamredd.inventarioSuelo.dao;

import java.sql.CallableStatement;

import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.conexionBD.ConexionBDParcelas;
import co.gov.ideamredd.logs.Transaccion;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.util.TransaccionUtil;

/**
 * RegistrarInventarioSuelo.java Clase encargada de realizar los registros del
 * inventario de suelo a la base de datos de el inventario de suelos.
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Oct 25 de 2013
 */
@Stateless 
public class RegistrarInventarioSuelo {

	@EJB
	ConexionBDParcelas conexion;
	@EJB
	ModificarInventarioSuelo mis;
	@EJB
	Transaccion transaccion;

	private Connection conn;
	private Logger log;
	private CallableStatement registraISuelo;

	/**
	 * metodo que registra un invenatrio de suelo.
	 * 
	 * @param parcela
	 *            : id del inventario de suelo de la parcela a modificar
	 * @param metodologia
	 *            : id de la metodologia seleccionada
	 * @param profundidad
	 *            : profundidad de toma a actualizar
	 * @param textura
	 *            : textura a actualizar
	 * @param densidad
	 *            : densidad aparente a actualizar
	 * @param flujo
	 *            : flujo CO2 a actualizar
	 * @param fecha
	 *            : fecha de toma de datos a actualizar
	 */
	public void registraInventarioSuelo(Integer parcela, Integer metodologia,
			Integer profundidad, String textura, Integer densidad,
			Integer flujo, String fecha) {

		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			// Se obtiene el nombre de la parcela
			registraISuelo = conn
					.prepareCall("{call RED_PK_PARCELAS.REGISTRA_INVENTSUELO(?,?,?,?,?,?,?,?,?)}");

			registraISuelo.setInt("UNA_PARCELA", parcela);
			registraISuelo.setInt("UN_CONTACTO", 1);
			registraISuelo.setInt("UNA_METODOLOGIA", metodologia);
			registraISuelo.setInt("UNA_PROFUNDIDAD", profundidad);
			registraISuelo.setString("UNA_TEXTURA", textura);
			registraISuelo.setInt("UNA_DENSIDAD", densidad);
			registraISuelo.setInt("UN_FLUJO", flujo);
			registraISuelo.setDate("UNA_fecha", java.sql.Date.valueOf(fecha));
			registraISuelo.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			registraISuelo.execute();

//			log.info("[registraInventarioSuelo] Termino");
			registraISuelo.close();
			conn.close();
			transaccion.logTransaccion(parcela, TransaccionUtil.Tipo.CREA, 4,
					"Se inserto un inventario de suelo", RegistrarInventarioSuelo.class);
		} catch (Exception e) {
//			log.error("[registraInventarioSuelo] Fallo");
			e.printStackTrace();
		}

	}

	/**
	 * Metodo que registra un inventario de suelo con nueva metodologia.
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
	public void registrarInventarioSueloConMetodologia(Integer parcela,
			String nombreMetodologia, String ecuacionMetodologia,
			String archivoMetodologia, Integer profundidad, String textura,
			Integer densidad, Integer flujo, String fecha) {

		try {
//			log = SMBC_Log.log(ConsultaInventarioSuelo.class);
			// conn = ConexionBD.establecerConexion();
			mis.insertaMetodologia(nombreMetodologia, ecuacionMetodologia,
					archivoMetodologia, fecha);
			int indice = mis.consultaUltimaMetodologia();

			conn = conexion.establecerConexion();

			registraISuelo = conn
					.prepareCall("{call RED_PK_PARCELAS.REGISTRA_INVENTSUELO(?,?,?,?,?,?,?,?,?)}");

			registraISuelo.setInt("UNA_PARCELA", parcela);
			registraISuelo.setInt("UN_CONTACTO", 1);
			registraISuelo.setInt("UNA_METODOLOGIA", indice);
			registraISuelo.setInt("UNA_PROFUNDIDAD", profundidad);
			registraISuelo.setString("UNA_TEXTURA", textura);
			registraISuelo.setInt("UNA_DENSIDAD", densidad);
			registraISuelo.setInt("UN_FLUJO", flujo);
			registraISuelo.setDate("UNA_fecha", java.sql.Date.valueOf(fecha));
			registraISuelo.registerOutParameter("UN_RESULTADO",
					OracleTypes.VARCHAR);
			registraISuelo.execute();

//			log.info("[registrarInventarioSueloConMetodologia] Termino");
			registraISuelo.close();
			conn.close();
			transaccion.logTransaccion(parcela, TransaccionUtil.Tipo.CREA, 4,
					"Se inserto un inventario de suelo", RegistrarInventarioSuelo.class);
		} catch (Exception e) {
//			log.error("[registrarInventarioSueloConMetodologia] Fallo");
			e.printStackTrace();
		}

	}

}
