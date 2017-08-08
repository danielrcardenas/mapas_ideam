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
public class CrearNoticia {
	
	@EJB
	ConexionBD conexion;

	private Connection conn;
	private Logger log;

    /**
     * Método usado para registrar una noticia o evento en el sistema.
     */
	public void guardarDatos(int idUsuario, String rutaImagen,
			String nombreNoticia, Integer tipoNoticia, String descripcion) {
		try {
			log = SMBC_Log.Log(CrearNoticia.class);
			conn = conexion.establecerConexion();
			CallableStatement registrarNoticia = conn
					.prepareCall("{call RED_PK_TABLASTIPO.registrarNoticiaEvento(?,?,?,?,?,?)}");
			
	        registrarNoticia.setInt("un_idUsuario", idUsuario);
	        registrarNoticia.setString("un_nombreNoticia", nombreNoticia);
	        registrarNoticia.setInt("un_tipo", tipoNoticia);
	        registrarNoticia.setString("una_descripcion", descripcion);
	        if(rutaImagen.equals("")){rutaImagen="nulo";}
	        registrarNoticia.setString("un_path", rutaImagen);
	        registrarNoticia.registerOutParameter("un_Resultado",OracleTypes.VARCHAR);
	        registrarNoticia.execute();

			System.out.println(registrarNoticia.getObject("un_Resultado"));

			log.info("[guardarDatos] Termino");
			registrarNoticia.close();
			conn.close();
		} catch (Exception e) {
			log.info("[guardarDatos] Fallo");
			e.printStackTrace();
		}

	}

}
