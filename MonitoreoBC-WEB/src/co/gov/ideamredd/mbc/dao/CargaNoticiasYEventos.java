// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.mbc.entities.Noticias;
import co.gov.ideamredd.noticias.dao.CrearNoticia;
import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;

/**
 * Métodos para consultar noticias y eventos
 *
 */
public class CargaNoticiasYEventos {

	/**
	 * Metodo para consultar noticias y eventos.
	 */
	public static ArrayList<Noticias> cargaNoticias() {
		Connection conn = null;
		ArrayList<Noticias> noticias = new ArrayList<Noticias>();
		try {
			conn = ConexionBD.establecerConexion();
			CallableStatement consultaNoticias = conn.prepareCall("{call RED_PK_TABLASTIPO.consult_allNoticiasEventos(?,?)}");
			consultaNoticias.setInt("un_tipo", 1);
			consultaNoticias.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaNoticias.execute();
			ResultSet r = (ResultSet) consultaNoticias.getObject("un_Resultado");
			while (r.next()) {
				Noticias n = new Noticias();
				n.setConsecutivo(r.getInt(1));
				n.setTipo(r.getInt(2));
				n.setFecha(r.getDate(3));
				n.setNombre(r.getString(4));
				n.setDescripcion(r.getString(5));
				n.setPathImagen(r.getString(6));
				noticias.add(n);
			}
			r.close();
			consultaNoticias.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return noticias;
	}

	/**
	 * Metodo para consultar eventos.
	 */
	public static ArrayList<Noticias> cargaEventos() {
		Connection conn = null;
		ArrayList<Noticias> noticias = new ArrayList<Noticias>();
		try {
			conn = ConexionBD.establecerConexion();
			CallableStatement consultaNoticias = conn.prepareCall("{call RED_PK_TABLASTIPO.consult_allNoticiasEventos(?,?)}");
			consultaNoticias.setInt("un_tipo", 2);
			consultaNoticias.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaNoticias.execute();
			ResultSet r = (ResultSet) consultaNoticias.getObject("un_Resultado");
			while (r.next()) {
				Noticias n = new Noticias();
				n.setConsecutivo(r.getInt(1));
				n.setTipo(r.getInt(2));
				n.setFecha(r.getDate(3));
				n.setNombre(r.getString(4));
				n.setDescripcion(r.getString(5));
				n.setPathImagen(r.getString(6));
				noticias.add(n);
			}
			r.close();
			consultaNoticias.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return noticias;
	}

	/**
	 * Metodo para consultar un noticia o evento.
	 */
	public static Noticias consultarNoticiaEvento(Integer noticiaEventoID) {
		Connection conn = ConexionBD.establecerConexion();
		Noticias n = new Noticias();
		try {
			CallableStatement consultaNoticias = conn.prepareCall("{call RED_PK_TABLASTIPO.consult_unaNoticiaEvento(?,?)}");
			consultaNoticias.setInt("un_id", noticiaEventoID);
			consultaNoticias.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaNoticias.execute();
			ResultSet r = (ResultSet) consultaNoticias.getObject("un_Resultado");
			while (r.next()) {
				n.setConsecutivo(r.getInt(1));
				n.setTipo(r.getInt(2));
				n.setFecha(r.getDate(3));
				n.setNombre(r.getString(4));
				n.setDescripcion(r.getString(5));
				n.setPathImagen(r.getString(6));
			}
			r.close();
			consultaNoticias.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}
	

	

	
}
