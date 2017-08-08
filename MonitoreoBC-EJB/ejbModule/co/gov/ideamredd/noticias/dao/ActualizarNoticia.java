// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.noticias.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;

@Stateless
public class ActualizarNoticia {
	
	@EJB
	ConexionBD conexion;

	private Connection conn;
	private Logger log;

    /**
     * Método usado para registrar una noticia o evento en el sistema.
     */
	public void actualizarDatos(int idUsuario, String rutaImagen,
			String nombreNoticia, Integer tipoNoticia, String descripcion, Integer consecutivo) {
		
		System.out.println("[actualizarDatos]: idUsuario: "+idUsuario+ " rutaImagen: "+rutaImagen+" nombreNoticia: "+nombreNoticia+" tipoNoticia: "+tipoNoticia+" descripcion: "+descripcion+" consecutivo: "+consecutivo);

		try {
			log = SMBC_Log.Log(ActualizarNoticia.class);
			conn = conexion.establecerConexion();
			CallableStatement registrarNoticia = conn
					.prepareCall("{call RED_PK_TABLASTIPO.NoticiaEvento_Actualizar(?,?,?,?,?,?,?)}");
			
	        registrarNoticia.setInt("un_idUsuario", idUsuario);
	        registrarNoticia.setString("un_nombreNoticia", nombreNoticia);
	        registrarNoticia.setInt("un_tipo", tipoNoticia);
	        registrarNoticia.setString("una_descripcion", descripcion);
	        if(rutaImagen.equals("")){rutaImagen="nulo";}
	        registrarNoticia.setString("un_path", rutaImagen);
	        registrarNoticia.setInt("consecutivo", consecutivo);
	        registrarNoticia.registerOutParameter("un_Resultado",OracleTypes.VARCHAR);
	        registrarNoticia.execute();


			System.out.println("[actualizarDatos]: "+registrarNoticia.getObject("un_Resultado"));

			log.info("[actualizarDatos] Termino Exitosamente");
			registrarNoticia.close();
			conn.close();
		} catch (Exception e) {
			log.info("[actualizarDatos] Fallo: "+ e.getMessage());
			e.printStackTrace();
		}

	}

	
}
