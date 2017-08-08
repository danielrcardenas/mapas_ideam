// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.imgusuarios.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.usuario.dao.ActualizarUsuario;
import co.gov.ideamredd.util.SMBC_Log;

@Stateless
public class GuardarImagenUsuario {

	@EJB
	ConexionBD conexion;

	private Connection conn;
	private Logger log;

    /**
     * Método usado para registrar una imagen de usuario.
     */
	public void guardarDatos(Integer idUsuario, String rutaImagen,
			String nombreImagen, String tipoLicencia, String descripcion,
			String autor, Double latitud, Double longitud) {
		try {
			log = SMBC_Log.Log(ActualizarUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement registrarImagen = conn
					.prepareCall("{call RED_PK_USUARIOS.registrarImagenUsuario(?,?,?,?,?,?,?,?,?,?)}");
			
	        registrarImagen.setInt("un_idUsuario", idUsuario);
	        registrarImagen.setString("un_path", rutaImagen);
	        registrarImagen.setString("un_nombreImagen", nombreImagen);
	        registrarImagen.setInt("un_estadoImagen", 1);
	        registrarImagen.setString("una_licenciaImagen", tipoLicencia);
	        registrarImagen.setString("una_descripcion", descripcion);
	        if(autor.equals("")){autor="No registrado";}
	        registrarImagen.setString("un_autor", autor);
	        if(latitud == null){latitud=0.0;}
	        registrarImagen.setDouble("una_latitud", latitud);
	        if(longitud == null){longitud=0.0;}
	        registrarImagen.setDouble("una_longitud", longitud);
	        registrarImagen.registerOutParameter("un_Resultado",OracleTypes.VARCHAR);
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
