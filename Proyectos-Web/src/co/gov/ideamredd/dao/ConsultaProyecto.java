package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes; 
import java.sql.Clob;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.ActividadRedd;
import co.gov.ideamredd.entities.CAR;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.DocumentosAsociados;
import co.gov.ideamredd.entities.Metodologia;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.Proyecto;
import co.gov.ideamredd.entities.Tenencia;
import co.gov.ideamredd.entities.TipoBosque;
import co.gov.ideamredd.util.Util;

public class ConsultaProyecto {

	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();

	public static Proyecto consultarProyectoId(Integer idProyecto) {
		Proyecto proyecto = new Proyecto();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.consultarProyectoId(?,?)}");
			consultaProyecto.setInt("un_proyecto", idProyecto);
			consultaProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaProyecto.execute();
			ResultSet r = (ResultSet) consultaProyecto
					.getObject("un_Resultado");
			while (r.next()) {
				proyecto.setConsecutivo(r.getInt(1));
				proyecto.setNombre(r.getString(2));
				proyecto.setDescripcionArea(r.getString(3));
				proyecto.setTipoBosques(r.getString(4));
				proyecto.setFechaInicio(r.getTimestamp(5));
				proyecto.setFechaFin(r.getTimestamp(6));
				proyecto.setPais(r.getInt(7));
				proyecto.setEstado(r.getInt(8));
				proyecto.setArea(r.getBigDecimal(9));
				proyecto.setCo2Reducir(r.getBigDecimal(10));
				proyecto.setTasaDeforestar(r.getBigDecimal(11));
				proyecto.setDuracionProyecto(r.getInt(12));
				proyecto.setPropietario(r.getInt(13));
				proyecto.setMetodologia(r.getInt(15));
			}
			r.close();
			consultaProyecto.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return proyecto;
	}

	public static Depto consultarDepto(Integer idProyecto) {
		Depto depto = new Depto();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaDepto = conn
					.prepareCall("{call RED_PK_PROYECTOS.DeptoProyecto_Consulta(?,?)}");
			consultaDepto.setInt("un_Proyecto", idProyecto);
			consultaDepto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaDepto.execute();
			ResultSet r = (ResultSet) consultaDepto
					.getObject("un_Resultado");
			while (r.next()) {
				depto.setConsecutivo(r.getInt(1));
				depto.setNombre(r.getString(2));
			}
			r.close();
			consultaDepto.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return depto;
	}

	public static Municipios consultarMcipio(Integer idProyecto) {
		Municipios municipios = new Municipios();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaMunicipio = conn
					.prepareCall("{call RED_PK_PROYECTOS.MunicipioProyecto_Consulta(?,?)}");
			consultaMunicipio.setInt("un_Proyecto", idProyecto);
			consultaMunicipio.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaMunicipio.execute();
			ResultSet r = (ResultSet) consultaMunicipio
					.getObject("un_Resultado");
			while (r.next()) {
				municipios.setConsecutivo(r.getInt(1));
				municipios.setNombre(r.getString(2));
			}
			r.close();
			consultaMunicipio.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return municipios;
	}

	public static CAR consultarCAR(Integer idProyecto) {
		CAR car = new CAR();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaCar = conn
					.prepareCall("{call RED_PK_PROYECTOS.CarProyecto_Consulta(?,?)}");
			consultaCar.setInt("un_Proyecto", idProyecto);
			consultaCar
					.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaCar.execute();
			ResultSet r = (ResultSet) consultaCar
					.getObject("un_Resultado");
			while (r.next()) {
				car.setConsecutivo(r.getInt(1));
				car.setNombre(r.getString(2));
			}
			r.close();
			consultaCar.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return car;
	}

	public static Pais consultarPais(Integer idProyecto) {
		Pais pais = new Pais();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaPais = conn
					.prepareCall("{call RED_PK_PROYECTOS.PaisProyecto_Consulta(?,?)}");
			consultaPais.setInt("un_Proyecto", idProyecto);
			consultaPais.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaPais.execute();
			ResultSet r = (ResultSet) consultaPais
					.getObject("un_Resultado");
			while (r.next()) {
				pais.setConsecutivo(r.getInt(1));
				pais.setNombre(r.getString(2));
			}
			r.close();
			consultaPais.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pais;
	}

	public static TipoBosque consultarTipoBosque(Integer idProyecto) {
		TipoBosque bosque = new TipoBosque();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaBosque = conn
					.prepareCall("{call RED_PK_PROYECTOS.tipoBosqueProyecto_Consulta(?,?)}");
			consultaBosque.setInt("un_proyecto", idProyecto);
			consultaBosque.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaBosque.execute();
			ResultSet r = (ResultSet) consultaBosque
					.getObject("un_Resultado");
			while (r.next()) {
				bosque.setConsecutivo(r.getInt(1));
				bosque.setTipoBosque(r.getString(2));
				bosque.setPrecipitacion(r.getString(3));
				bosque.setTemperatura(r.getString(4));
				bosque.setAltitud(r.getString(5));
			}
			r.close();
			consultaBosque.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bosque;
	}

	public static Metodologia consultarMetodologia(Integer idProyecto) {
		Metodologia metodologia = new Metodologia();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaMetologia = conn
					.prepareCall("{call RED_PK_PROYECTOS.MetodologiaProyecto_Consulta(?,?)}");
			consultaMetologia.setInt("un_Proyecto", idProyecto);
			consultaMetologia.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaMetologia.execute();
			ResultSet r = (ResultSet) consultaMetologia
					.getObject("un_Resultado");
			while (r.next()) {
				metodologia.setMetodologiaId(r.getInt(1));
				metodologia.setMetodologiaNombre(r.getString(2));
				metodologia.setMetodologiaEcuacion(r.getString(3) == null ? ""
						: r.getString(3));
				metodologia
						.setMetodologiaDirArchivo(r.getString(4) == null ? ""
								: r.getString(4));
				metodologia.setDescripcion(r.getString(5));
			}
			r.close();
			consultaMetologia.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return metodologia;
	}
	
	public static Tenencia consultarTenencia(Integer idProyecto) {
		Tenencia tenencia = new Tenencia();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultarTenenciaProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.TenenciaProyecto_Consulta(?,?)}");
			consultarTenenciaProyecto.setInt("un_Proyecto", idProyecto);
			consultarTenenciaProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarTenenciaProyecto.execute();
			ResultSet resultSet = (ResultSet) consultarTenenciaProyecto
					.getObject("un_Resultado");
			while (resultSet.next()) {
				tenencia.setConsecutivo(resultSet.getInt(1));
				tenencia.setDescripcion(resultSet.getString(2));
			}
			resultSet.close();
			consultarTenenciaProyecto.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tenencia;
	}
	
	public static ArrayList<ActividadRedd> consultarActividadProyecto(
			Integer idProyecto) {
		ArrayList<ActividadRedd> actividades = new ArrayList<ActividadRedd>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultarActividadProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.ActividadProyecto_Consulta(?,?)}");
			consultarActividadProyecto.setInt("un_Proyecto", idProyecto);
			consultarActividadProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarActividadProyecto.execute();
			ResultSet resultSet = (ResultSet) consultarActividadProyecto
					.getObject("un_Resultado");
			while (resultSet.next()) {
				ActividadRedd actividadRedd = new ActividadRedd();
				actividadRedd.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				actividadRedd.setNombre((String) resultSet.getObject(2));
				actividades.add(actividadRedd);
			}
			resultSet.close();
			consultarActividadProyecto.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actividades;
	}
	
	public static String[] consultarGeoProyecto(Integer idProyecto) {
		Clob geometria = null;
		String[] coordenadas =null;
		try {
			conn = dataSource.getConnection();
			CallableStatement consultarPaisProyecto = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Proyecto_Consulta(?,?)}");
			consultarPaisProyecto.setInt("un_Consecutivo", idProyecto);
			consultarPaisProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CLOB);
			consultarPaisProyecto.execute();
			geometria = (Clob) consultarPaisProyecto.getObject("un_Resultado");
			consultarPaisProyecto.close();
			coordenadas=Util.obtenerDatosGeometria(Util.clobStringConversion(geometria));
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return coordenadas;
	}
	
	public static ArrayList<DocumentosAsociados> consultarDocumentosProyecto(
			Integer idProyecto) {
		ArrayList<DocumentosAsociados> documentos = new ArrayList<DocumentosAsociados>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultarDocumentosProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.DocumentoAsociado_Consultar(?,?)}");
			consultarDocumentosProyecto.setInt("un_Proyecto", idProyecto);
			consultarDocumentosProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarDocumentosProyecto.execute();
			ResultSet resultSet = (ResultSet) consultarDocumentosProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				DocumentosAsociados documentosAsociados = new DocumentosAsociados();
				documentosAsociados.setConsecutivo(resultSet.getInt(1));
				documentosAsociados.setUn_NombreDocumento(resultSet.getString(2));
				documentosAsociados
						.setUna_Ruta(resultSet.getString(3));
				documentosAsociados.setUna_FechaIngresoDocumento(resultSet.getTimestamp(4));
				documentosAsociados.setEs_Publico(resultSet.getString(5));
				documentosAsociados.setUn_TipoDocumento(resultSet.getInt(6));
				documentosAsociados.setUna_Licencia(resultSet.getInt(7));
				documentosAsociados.setUn_Proyecto(resultSet.getInt(8));	
				documentosAsociados.setUn_NombreTipoDoc(resultSet.getString(9));
				documentos.add(documentosAsociados);
			}
			resultSet.close();
			consultarDocumentosProyecto.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documentos;
	}
}
