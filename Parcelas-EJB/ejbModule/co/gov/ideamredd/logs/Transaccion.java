package co.gov.ideamredd.logs;

import java.sql.CallableStatement;

import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import co.gov.ideamredd.conexionBD.ConexionBDParcelas;
//import co.gov.ideamredd.inventarioSuelo.dao.ConsultaInventarioSuelo;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.util.TransaccionUtil;

/**
 * Transaccion.java Clase encargada de registrar todas las transacciones
 * realizadas.
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Nov 7 de 2013
 */
@Stateless
public class Transaccion {

	@EJB
	ConexionBDParcelas conexion;

	private static Connection conn;
	private static Logger log;
	private static CallableStatement regTransacccion;

	/**
	 * Metodo que registra una transaccion realizada
	 * 
	 * @param idObjeto
	 *            : id del objeto procesado
	 * @param tipoTransaccion
	 *            : el tipo de transaccion
	 * @param idUsuario
	 *            : el id del usuario que realiza la transaccion.
	 * @param descripcion
	 *            : una descripcion de la transaccion.
	 */
	@SuppressWarnings("rawtypes")
	public void logTransaccion(int idObjeto,
			TransaccionUtil.Tipo tipoTransaccion, int idUsuario,
			String descripcion, Class clazz) {
		try {
//			log = SMBC_Log.log(clazz);   
			// conn = ConexionBD.establecerConexion() ;
			conn = conexion.establecerConexion();

			// Se obtiene el nombre de la parcela
			regTransacccion = conn 
					.prepareCall("{call RED_PK_LOGS.INSERTA_TRANSACCION(?,?,?,?)}");

			regTransacccion.setInt("UN_IDOBJETO", idObjeto);
			regTransacccion.setInt("UN_CONS_TIPOTRANSAC",
					tipoTransaccion.ordinal() + 1);
			regTransacccion.setInt("UN_CONS_USUARIO", idUsuario);
			regTransacccion.setString("UNA_DESCRIPCION", descripcion);
			regTransacccion.execute();

			log.info("[logTransaccion] Termino");
			regTransacccion.close();
			conn.close();
		} catch (Exception e) {
			log.error("[logTransaccion] Fallo");
			e.printStackTrace();
		}
	}

}
