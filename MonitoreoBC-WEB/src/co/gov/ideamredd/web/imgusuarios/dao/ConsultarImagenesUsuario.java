// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.imgusuarios.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.imgusuarios.entities.ImagenUsuario;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;


public class ConsultarImagenesUsuario {

	private static Logger log;
	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();

	/**
	 * Metodo para obtener las imagenes registradas de usuarios.
	 */
	public static ArrayList<ImagenUsuario> consultarImagenes() {
		ArrayList<ImagenUsuario> imagenes = new ArrayList<ImagenUsuario>();
		ImagenUsuario imaUs=null;
		
		try {
			log = SMBC_Log.Log(ConsultarImagenesUsuario.class);
			conn = dataSource.getConnection();
			CallableStatement consultarImagen = conn
					.prepareCall("{call RED_PK_USUARIOS.consultar_TodasImagenes(?)}");
			consultarImagen.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarImagen.execute();
			ResultSet resultSet = (ResultSet) consultarImagen
					.getObject("un_Resultado");

			while (resultSet.next()) {
				imaUs = new ImagenUsuario();
				imaUs.setIdImagen(resultSet.getInt("IMUS_CONS"));
				imaUs.setIdUsuario(resultSet.getInt("IMUS_CONS_USUARIO"));
				imaUs.setPath(resultSet.getString("IMUS_PATH"));
				imaUs.setNombre(resultSet.getString("IMUS_NOMBRE"));
				imaUs.setFechaSubida(resultSet.getDate("IMUS_FECHA_CARGA"));
				imaUs.setEstado(resultSet.getInt("IMUS_CONS_ESTADOIMAGEN"));
				imaUs.setLicencia(resultSet.getInt("IMUS_CONS_LICENCIA"));
				imaUs.setDescripcion(resultSet.getString("IMUS_DESCRIPCION"));
				imaUs.setAutor(resultSet.getString("IMUS_AUTOR"));
				imaUs.setLatitud(resultSet.getDouble("IMUS_LOCALIZACION_X"));
				imaUs.setLongitud(resultSet.getDouble("IMUS_LOCALIZACION_Y"));
				
				imagenes.add(imaUs);

			}

			resultSet.close();
			consultarImagen.close();
			conn.close();
			log.info("[consultarImagenes] Termino");
		} catch (Exception e) {
			log.error("[consultarImagenes] Fallo");
			e.printStackTrace();
		}

		return imagenes;
	}

}
