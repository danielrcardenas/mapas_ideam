// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.admin.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;

@Stateless
public class ModificarLicencia {

	@EJB
	ConexionBD conexion;

	private Connection conn;
	private Logger log;

    /**
     * Método usado para actualizar una licencia.
     */
	public void guardarDatos(Integer idLicencia, String nombreLicencia, String descripcion,
			Integer activa) {
		try {
			log = SMBC_Log.Log(ModificarLicencia.class);
			conn = conexion.establecerConexion();
			CallableStatement registrarImagen = conn
					.prepareCall("{call RED_PK_USUARIOS.modificarLicenciaUsuario(?,?,?,?,?)}");
			
			registrarImagen.setInt("un_idLicencia", idLicencia);
			registrarImagen.setString("un_nombreLic", nombreLicencia);
			registrarImagen.setInt("es_Activa", activa);
			registrarImagen.setString("una_descripcion", descripcion);
			registrarImagen.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			registrarImagen.execute();

			System.out.println(registrarImagen.getObject("un_Resultado"));

			log.info("[guardarDatos] Termino");
			registrarImagen.close();
			conn.close();
		} catch (Exception e) {
			log.info("[guardarDatos] Fallo");
			e.printStackTrace();
		}

	}

}
