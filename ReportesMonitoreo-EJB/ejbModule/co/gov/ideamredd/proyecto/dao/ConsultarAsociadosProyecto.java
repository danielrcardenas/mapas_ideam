package co.gov.ideamredd.proyecto.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import oracle.sql.CLOB;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.ActividadRedd;
import co.gov.ideamredd.entities.CAR;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.DocumentosAsociados;
import co.gov.ideamredd.entities.Metodologia;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.Parcela;
import co.gov.ideamredd.entities.Tenencia;
import co.gov.ideamredd.entities.TipoBosque;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.util.Util;

@Stateless
public class ConsultarAsociadosProyecto {

	@EJB
	ConexionBD conexion;

	private ArrayList<ActividadRedd> actividades;
	private ArrayList<CAR> cars;
	private ArrayList<Depto> departamentos;
	private ArrayList<Municipios> municipio;
	private ArrayList<Tenencia> tenencias;
	private ArrayList<DocumentosAsociados> documentos;
	private ArrayList<Parcela> parcelas;
	private ArrayList<TipoBosque> tipoBosques;
	private Connection conn;
	private ResultSet resultSet;
	private Logger log;

	public ArrayList<ActividadRedd> ConsultarActividadProyecto(
			Integer idProyecto) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			conn = conexion.establecerConexion();
			actividades = new ArrayList<ActividadRedd>();
			CallableStatement consultarActividadProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.ActividadProyecto_Consulta(?,?)}");
			consultarActividadProyecto.setInt("un_Proyecto", idProyecto);
			consultarActividadProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarActividadProyecto.execute();
			resultSet = (ResultSet) consultarActividadProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				ActividadRedd actividadRedd = new ActividadRedd();
				actividadRedd.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				actividadRedd.setNombre((String) resultSet.getObject(2));
				actividadRedd.setDescripcion((String) resultSet.getObject(3));
				actividades.add(actividadRedd);
			}

			log.info("[ConsultarActividadProyecto] Termino");
			resultSet.close();
			consultarActividadProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarActividadProyecto] Fallo");
			e.printStackTrace();
		}
		return actividades;
	}

	public ArrayList<Depto> ConsultarDeptoProyecto(Integer idProyecto) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			departamentos = new ArrayList<Depto>();
			CallableStatement consultarDeptoProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.DeptoProyecto_Consulta(?,?)}");
			consultarDeptoProyecto.setInt("un_Proyecto", idProyecto);
			consultarDeptoProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarDeptoProyecto.execute();
			resultSet = (ResultSet) consultarDeptoProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				Depto depto = new Depto();
				depto.setConsecutivo(Integer.valueOf(resultSet.getObject(2)
						.toString()));
				depto.setNombre((String) resultSet.getObject(3));
				departamentos.add(depto);
			}

			log.info("[ConsultarDeptoProyecto] Termino");
			resultSet.close();
			consultarDeptoProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarDeptoProyecto] Fallo");
			e.printStackTrace();
		}
		return departamentos;
	}

	public ArrayList<Municipios> ConsultarMunicipioProyecto(Integer idProyecto) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			municipio = new ArrayList<Municipios>();
			CallableStatement consultarMunicipioProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.MunicipioProyecto_Consulta(?,?)}");
			consultarMunicipioProyecto.setInt("un_Proyecto", idProyecto);
			consultarMunicipioProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarMunicipioProyecto.execute();
			resultSet = (ResultSet) consultarMunicipioProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				if (!resultSet.getString(2).contains("LOCALIDAD")) {
					Municipios municipios = new Municipios();
					municipios.setConsecutivo(Integer.valueOf(resultSet
							.getObject(5).toString()));
					municipios.setNombre(resultSet.getObject(6).toString());
					municipios.setDepartamento(Integer.valueOf(resultSet
							.getObject(3).toString()));
					municipio.add(municipios);
				}
			}

			log.info("[ConsultarMunicipioProyecto] Termino");
			resultSet.close();
			consultarMunicipioProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarMunicipioProyecto] Fallo");
			e.printStackTrace();
		}
		return municipio;
	}

	public Pais ConsultarPaisProyecto(Integer idProyecto) {
		Pais pais = new Pais();
		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			CallableStatement consultarPaisProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.PaisProyecto_Consulta(?,?)}");
			consultarPaisProyecto.setInt("un_Proyecto", idProyecto);
			consultarPaisProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPaisProyecto.execute();
			resultSet = (ResultSet) consultarPaisProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				pais.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				pais.setNombre((String) resultSet.getObject(2));
			}

			log.info("[ConsultarPaisProyecto] Termino");
			resultSet.close();
			consultarPaisProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarPaisProyecto] Fallo");
			e.printStackTrace();
		}
		return pais;
	}

	public ArrayList<Tenencia> ConsultarTenenciaProyecto(Integer idProyecto) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			tenencias = new ArrayList<Tenencia>();
			CallableStatement consultarTenenciaProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.TenenciaProyecto_Consulta(?,?)}");
			consultarTenenciaProyecto.setInt("un_Proyecto", idProyecto);
			consultarTenenciaProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarTenenciaProyecto.execute();
			resultSet = (ResultSet) consultarTenenciaProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				Tenencia tenencia = new Tenencia();
				tenencia.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				tenencia.setDescripcion((String) resultSet.getObject(2));
				tenencias.add(tenencia);
			}

			log.info("[ConsultarTenenciaProyecto] Termino");
			resultSet.close();
			consultarTenenciaProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarTenenciaProyecto] Fallo");
			e.printStackTrace();
		}
		return tenencias;
	}

	public ArrayList<DocumentosAsociados> ConsultarDocumentosProyecto(
			Integer idProyecto) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			documentos = new ArrayList<DocumentosAsociados>();
			CallableStatement consultarDocumentosProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.DocumentoAsociado_Consultar(?,?)}");
			consultarDocumentosProyecto.setInt("un_Proyecto", idProyecto);
			consultarDocumentosProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarDocumentosProyecto.execute();
			resultSet = (ResultSet) consultarDocumentosProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				DocumentosAsociados documentosAsociados = new DocumentosAsociados();
				documentosAsociados.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				documentosAsociados.setUn_NombreDocumento((String) resultSet
						.getObject(2));
				documentosAsociados
						.setUna_Ruta((String) resultSet.getObject(3));
				documentosAsociados.setUna_FechaIngresoDocumento(new Date(
						((Timestamp) resultSet.getObject(4)).getTime()));
				documentosAsociados.setEs_Publico((String) resultSet
						.getObject(5));
				documentosAsociados.setUn_TipoDocumento(Integer
						.valueOf(resultSet.getObject(6).toString()));
				documentosAsociados.setUna_Licencia(Integer.valueOf(resultSet
						.getObject(7).toString()));
				documentosAsociados.setUn_Proyecto(Integer.valueOf(resultSet
						.getObject(8).toString()));
				documentosAsociados.setUn_NombreTipoDoc(resultSet.getString(9));
				documentos.add(documentosAsociados);
			}

			log.info("[ConsultarDocumentosProyecto] Termino");
			resultSet.close();
			consultarDocumentosProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarDocumentosProyecto] Fallo");
			e.printStackTrace();
		}
		return documentos;
	}

	public ArrayList<CAR> ConsultarCarProyecto(Integer idProyecto) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			cars = new ArrayList<CAR>();
			CallableStatement consultarCarProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.CarProyecto_Consulta(?,?)}");
			consultarCarProyecto.setInt("un_Proyecto", idProyecto);
			consultarCarProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarCarProyecto.execute();
			resultSet = (ResultSet) consultarCarProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				CAR car = new CAR();
				car.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				car.setNombre((String) resultSet.getObject(2));
				cars.add(car);
			}
			log.info("[ConsultarCarProyecto] Termino");
			resultSet.close();
			consultarCarProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarCarProyecto] Fallo");
			e.printStackTrace();
		}
		return cars;
	}

	public Metodologia ConsultarMetodologiaProyecto(Integer idProyecto) {
		Metodologia metodologia = new Metodologia();
		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			CallableStatement consultarMetodologiaProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.MetodologiaProyecto_Consulta(?,?)}");
			consultarMetodologiaProyecto.setInt("un_Proyecto", idProyecto);
			consultarMetodologiaProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarMetodologiaProyecto.execute();
			resultSet = (ResultSet) consultarMetodologiaProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				metodologia.setConsecutivo(Integer.valueOf(resultSet.getObject(
						1).toString()));
				metodologia.setDescripcion((String) resultSet.getObject(2));
				metodologia.setNombre((String) resultSet.getObject(3));
			}
			log.info("[ConsultarMetodologiaProyecto] Termino");
			resultSet.close();
			consultarMetodologiaProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarMetodologiaProyecto] Fallo");
			e.printStackTrace();
		}
		return metodologia;
	}

	public ArrayList<Parcela> ConsultarParcelaProyecto(Integer idProyecto) {
		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			parcelas = new ArrayList<Parcela>();
			CallableStatement consultarParcelaProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.ProyectoParcela_Consulta(?,?)}");
			consultarParcelaProyecto.setInt("un_Proyecto", idProyecto);
			consultarParcelaProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarParcelaProyecto.execute();
			resultSet = (ResultSet) consultarParcelaProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				Parcela parcela = new Parcela();
				parcela.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				parcela.setNombre((String) resultSet.getObject(2));
				parcela.setPerteneceProyecto((String) resultSet.getObject(3));
				parcela.setFechaEstablecimiento((Timestamp) resultSet
						.getObject(4));
				parcela.setAprovechamiento((String) resultSet.getObject(5));
				parcela.setColecciones((String) resultSet.getObject(6));
				parcela.setInvestigador(resultSet.getObject(7).toString());
				parcela.setArea(new BigDecimal(resultSet.getObject(8)
						.toString()));
				parcela.setVerificado(resultSet.getObject(9).toString());
				parcela.setInventarioPublico(resultSet.getObject(10).toString());
				parcela.setEstado(Integer.valueOf(resultSet.getObject(11)
						.toString()));
				parcela.setTemporalidad(Integer.valueOf(resultSet.getObject(12)
						.toString()));
				parcela.setInventario(Integer.valueOf(resultSet.getObject(13)
						.toString()));
				parcela.setFgda(Integer.valueOf(resultSet.getObject(14)
						.toString()));
				parcela.setTipoAutor(Integer.valueOf(resultSet.getObject(15)
						.toString()));
				parcela.setTipoCustodio(Integer.valueOf(resultSet.getObject(16)
						.toString()));
				parcela.setMetadato(Integer.valueOf(resultSet.getObject(17)
						.toString()));
				parcela.setIdFgda(Integer.valueOf(resultSet.getObject(18)
						.toString()));
				parcela.setIdAutor(Integer.valueOf(resultSet.getObject(19)
						.toString()));
				parcela.setIdCustodio(Integer.valueOf(resultSet.getObject(20)
						.toString()));
				parcela.setIdInvetigador(Integer.valueOf(resultSet
						.getObject(21).toString()));
				parcela.setIdEncargado(Integer.valueOf(resultSet.getObject(22)
						.toString()));
				parcela.setIdColeccion(Integer.valueOf(resultSet.getObject(23)
						.toString()));
				parcela.setLargoParcela((BigDecimal) resultSet.getObject(24));
				parcela.setAnchoParcela((BigDecimal) resultSet.getObject(25));
				parcela.setRadioParcela((BigDecimal) resultSet.getObject(26));
				parcela.setObservaciones(resultSet.getObject(27).toString());
				parcela.setRutaImagen(resultSet.getObject(28).toString());
				parcela.setNombreImagen(resultSet.getObject(29).toString());
				parcela.setPais(Integer.valueOf(resultSet.getObject(30)
						.toString()));
				parcela.setProposito(Integer.valueOf(resultSet.getObject(31)
						.toString()));
				parcela.setCodigoCampo(resultSet.getObject(32).toString());
				parcela.setForma(Integer.valueOf(resultSet.getObject(33)
						.toString()));
				parcela.setDescripcion(resultSet.getObject(34).toString());
			}

			log.info("[ConsultarParcelaProyecto] Termino");
			resultSet.close();
			consultarParcelaProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarParcelaProyecto] Fallo");
			e.printStackTrace();
		}
		return parcelas;
	}

	public String[] ConsultarGeoProyecto(Integer idProyecto) {
		CLOB geometria = null;
		String[] coordenadas = null;
		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			CallableStatement consultarPaisProyecto = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Proyecto_Consulta(?,?)}");
			consultarPaisProyecto.setInt("un_Consecutivo", idProyecto);
			consultarPaisProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CLOB);
			consultarPaisProyecto.execute();
			geometria = (CLOB) consultarPaisProyecto.getObject("un_Resultado");
			coordenadas = Util.obtenerDatosGeometria(Util
					.clobStringConversion(geometria));

			log.info("[ConsultarGeoProyecto] Termino");
			consultarPaisProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarGeoProyecto] Fallo");
			e.printStackTrace();
		}
		return coordenadas;
	}

	public BigDecimal consultarAreaProyecto(Integer idProyecto) {
		BigDecimal area = null;
		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			CallableStatement areaProyecto = conn
					.prepareCall("{call RED_PK_GEOMETRIA.proyecto_consultaArea(?,?)}");
			areaProyecto.setInt("un_consecutivo", idProyecto);
			areaProyecto.registerOutParameter("un_Resultado",
					OracleTypes.NUMBER);
			areaProyecto.execute();
			area = (BigDecimal) areaProyecto.getObject("un_Resultado");

			log.info("[consultarAreaProyecto] Termino");
			areaProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultarAreaProyecto] Fallo");
			e.printStackTrace();
		}
		return area;
	}

	public ArrayList<TipoBosque> ConsultarTipoBosqueProyecto(Integer idProyecto) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosProyecto.class);
			tipoBosques = new ArrayList<TipoBosque>();
			CallableStatement consultarMunicipioProyecto = conn
					.prepareCall("{call RED_PK_PROYECTOS.tipoBosqueProyecto_Consulta(?,?)}");
			consultarMunicipioProyecto.setInt("un_proyecto", idProyecto);
			consultarMunicipioProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarMunicipioProyecto.execute();
			resultSet = (ResultSet) consultarMunicipioProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				TipoBosque tipoBosque = new TipoBosque();
				tipoBosque.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				tipoBosque.setTipoBosque((String) resultSet.getObject(2));
				tipoBosque.setPrecipitacion(resultSet.getObject(4).toString());
				tipoBosque.setTemperatura(resultSet.getObject(5).toString());
				tipoBosque.setAltitud(resultSet.getObject(6).toString());
				tipoBosques.add(tipoBosque);
			}

			log.info("[ConsultarTipoBosqueProyecto] Termino");
			resultSet.close();
			consultarMunicipioProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarTipoBosqueProyecto] Fallo");
			e.printStackTrace();
		}
		return tipoBosques;
	}

}
