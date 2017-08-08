package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sql.DataSource;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.BiomasaYCarbono;
import co.gov.ideamredd.entities.EstadoBiomasa;
import co.gov.ideamredd.entities.Noticias;

public class CargaDatosInicialBiomasa {

	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();
	private static final String cero = "0";
	private static final String verificado = "1";
	private static final String atipico = "1";
	private static final String incluido = "1";

	public static ArrayList<Noticias> getNoticiasHome() {
		ArrayList<Noticias> noticias = new ArrayList<Noticias>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaNoticias = conn
					.prepareCall("{call RED_PK_TABLASTIPO.consultaNoticiaHome(?)}");
			consultaNoticias.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaNoticias.execute();
			OracleResultSet r = (OracleResultSet) consultaNoticias
					.getObject("un_Resultado");
			while (r.next()) {
				Noticias n = new Noticias();
				n.setConsecutivo(r.getInt(1));
				n.setTipo(r.getInt(2));
				n.setFecha(r.getDate(3));
				n.setNombre(r.getString(4));
				n.setDescripcion(r.getString(5));
				n.setPathImagen(r.getString(7));
				noticias.add(n);
			}
			r.close();
			consultaNoticias.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noticias;
	}

	public static ArrayList<Noticias> getEventosHome() {
		ArrayList<Noticias> eventos = new ArrayList<Noticias>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaEventos = conn
					.prepareCall("{call RED_PK_TABLASTIPO.consultaEventoHome(?)}");
			consultaEventos.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaEventos.execute();
			OracleResultSet r = (OracleResultSet) consultaEventos
					.getObject("un_Resultado");
			while (r.next()) {
				Noticias n = new Noticias();
				n.setConsecutivo(r.getInt(1));
				n.setTipo(r.getInt(2));
				n.setFecha(r.getDate(3));
				n.setNombre(r.getString(4));
				n.setDescripcion(r.getString(5));
				n.setLugar(r.getString(6));
				n.setPathImagen(r.getString(7));
				// n.setCoordenadas(r.getString(8));
				n.setHora(r.getString(9));
				eventos.add(n);
			}
			r.close();
			consultaEventos.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventos;
	}

	public static ArrayList<Noticias> getNoticias() {
		ArrayList<Noticias> noticias = new ArrayList<Noticias>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaNoticias = conn
					.prepareCall("{call RED_PK_TABLASTIPO.consultaTodasNoticias(?)}");
			consultaNoticias.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaNoticias.execute();
			OracleResultSet r = (OracleResultSet) consultaNoticias
					.getObject("un_Resultado");
			while (r.next()) {
				Noticias n = new Noticias();
				n.setConsecutivo(r.getInt(1));
				n.setTipo(r.getInt(2));
				n.setFecha(r.getDate(3));
				n.setNombre(r.getString(4));
				n.setDescripcion(r.getString(5));
				n.setPathImagen(r.getString(7));
				noticias.add(n);
			}
			r.close();
			consultaNoticias.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return noticias;
	}

	public static ArrayList<Noticias> getEventos() {
		ArrayList<Noticias> eventos = new ArrayList<Noticias>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaEventos = conn
					.prepareCall("{call RED_PK_TABLASTIPO.consultaTodosEventos(?)}");
			consultaEventos.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaEventos.execute();
			OracleResultSet r = (OracleResultSet) consultaEventos
					.getObject("un_Resultado");
			while (r.next()) {
				Noticias n = new Noticias();
				n.setConsecutivo(r.getInt(1));
				n.setTipo(r.getInt(2));
				n.setFecha(r.getDate(3));
				n.setNombre(r.getString(4));
				n.setDescripcion(r.getString(5));
				n.setLugar(r.getString(6));
				n.setPathImagen(r.getString(7));
				// n.setCoordenadas(r.getString(8));
				n.setHora(r.getString(9));
				eventos.add(n);
			}
			r.close();
			consultaEventos.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventos;
	}

	public static ArrayList<BiomasaYCarbono> getBiomasaYCarbono(String idParcela) {
		ArrayList<BiomasaYCarbono> biomasas = new ArrayList<BiomasaYCarbono>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaBiomasaYCarbono = conn
					.prepareCall("{call RED_PK_PARCELAS.Consultar_BiomasaParcela(?,?,?)}");
			consultaBiomasaYCarbono.setString("una_Parcela", idParcela);
			consultaBiomasaYCarbono.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaBiomasaYCarbono.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultaBiomasaYCarbono.execute();
			System.out.println(consultaBiomasaYCarbono.getString("sentencia"));
			OracleResultSet r = (OracleResultSet) consultaBiomasaYCarbono
					.getObject("un_Resultado");
			while (r.next()) {
				BiomasaYCarbono bio = new BiomasaYCarbono();
				bio.setConsecutivo(r.getInt(1));
				bio.setBiomasa(r.getDouble(2));
				bio.setCarbono(r.getDouble(3));
				bio.setMetodologia(r.getInt(4));
				bio.setEstado(r.getInt(5));
				bio.setIdParcela(r.getInt(6));
				bio.setFechaInicio((Timestamp) r.getObject(7));
				bio.setAreaBasalPromedio(r.getDouble(8));
				bio.setAreaBasaltotal(r.getDouble(9));
				bio.setVolumen(r.getDouble(10));
				bio.setTipoGeneracion(r.getInt(11));
				bio.setFecha_fin(r.getTimestamp(12));
				biomasas.add(bio);
			}
			r.close();
			consultaBiomasaYCarbono.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return biomasas;
	}

	public static String obtenerNombreParcela(String idParcela) {
		String parcela = "";
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaBiomasaYCarbono = conn
					.prepareCall("{call RED_PK_PARCELAS.ObtenerNombreParcela(?,?)}");
			consultaBiomasaYCarbono.setString("una_parcela", idParcela);
			consultaBiomasaYCarbono.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			consultaBiomasaYCarbono.execute();
			parcela = consultaBiomasaYCarbono.getString("un_resultado");
			consultaBiomasaYCarbono.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parcela;
	}

	public static EstadoBiomasa obtenerEstadoBiomasa(Integer estado) {
		EstadoBiomasa estadoBiomasa = new EstadoBiomasa();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaBiomasaYCarbono = conn
					.prepareCall("{call RED_PK_PARCELAS.ObtenerEstadoBiomasa(?,?)}");
			consultaBiomasaYCarbono.setInt("un_estado", estado);
			consultaBiomasaYCarbono.registerOutParameter("un_resultado",
					OracleTypes.CURSOR);
			consultaBiomasaYCarbono.execute();
			OracleResultSet r = (OracleResultSet) consultaBiomasaYCarbono
					.getObject("un_resultado");
			while (r.next()) {
				estadoBiomasa.setConsecutivo(r.getInt(1));
				estadoBiomasa.setVerificado(r.getString(2));
				estadoBiomasa.setAtipico(r.getString(3));
				estadoBiomasa.setIncluidoCalculos(r.getString(4));
			}
			r.close();
			consultaBiomasaYCarbono.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return estadoBiomasa;
	}

	public static String estadoBiomasa(EstadoBiomasa estadoBiomasa) {
		String estado = "";
		if (estadoBiomasa.getVerificado().equals(verificado))
			estado += "Verificado";
		else
			estado += "No Verificado";
		if (estadoBiomasa.getAtipico().equals(atipico))
			estado += " / Dato Atipico";
		else
			estado += " / Dato Tipico";
		if (estadoBiomasa.getIncluidoCalculos().equals(incluido))
			estado += " / Incluido en calculos";
		else
			estado += " / No incluido en calculos";
		return estado;
	}

	public static String[] configurarEstado(Integer id) {
		String[] valores = new String[3];
		EstadoBiomasa b = obtenerEstadoBiomasa(id);
		if (b.getVerificado().equals(verificado))
			valores[0] = verificado;
		else
			valores[0] = cero;
		if (b.getAtipico().equals(atipico))
			valores[1] = verificado;
		else
			valores[1] = cero;
		if (b.getIncluidoCalculos().equals(incluido))
			valores[2] = verificado;
		else
			valores[2] = cero;
		return valores;
	}

}
