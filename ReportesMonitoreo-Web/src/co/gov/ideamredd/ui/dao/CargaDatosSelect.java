package co.gov.ideamredd.ui.dao;

import java.sql.CallableStatement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.CAR;
import co.gov.ideamredd.entities.CarDepto;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.MetodologiaBiomasa;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Parcela;
import co.gov.ideamredd.entities.Reportes;
import co.gov.ideamredd.entities.Temporalidad;
import co.gov.ideamredd.util.SMBC_Log;

public class CargaDatosSelect {

	private static ArrayList<Municipios> listaMunicipios;
	private static ArrayList<Depto> listaDeptos;
	private static DataSource dataSource = (DataSource) ConexionBD.getConnection();
	private static Connection conn;
	private static Temporalidad temp;
	private static Integer idUsuario;
	private static Integer usuario;
	private static Logger log;

	public static String getPaises() {
		String pais = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaPais = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Pais_Consulta(?, ?)}");
			consultaPais.setString("un_Nombre", "");
			consultaPais.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaPais.execute();
			ResultSet resultSet = (ResultSet) consultaPais
					.getObject("un_Resultado");
			while (resultSet.next()) {
				pais += "<option value=\"" + resultSet.getObject(1) + "\">"
						+ resultSet.getObject(2) + "</option>\n";
			}

			log.info("[getPaises] Termino");
			resultSet.close();
			consultaPais.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getPaises] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return pais;
	}

	public static String getDepartamentos() {
		String departamentos = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaDepartamento = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Departamento_Consulta(?, ?)}");
			consultaDepartamento.setString("un_Nombre", "");
			consultaDepartamento.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaDepartamento.execute();
			ResultSet resultSet = (ResultSet) consultaDepartamento
					.getObject("un_Resultado");
			while (resultSet.next()) {
				departamentos += "<option value=\"" + resultSet.getObject(2)
						+ "\">" + resultSet.getString(3) + "</option>\n";
				// System.out.println("<option value=\""+resultSet.getObject(1)+"\">"+resultSet.getObject(2)+"</option>");
			}
			log.info("[getDepartamentos] Termino");
			resultSet.close();
			consultaDepartamento.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getDepartamentos] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return departamentos;
	}

	public static String getMunicipios() {
		String departamentos = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaDepartamento = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Municipio_Consulta(?, ?)}");
			consultaDepartamento.setString("un_Nombre", "");
			consultaDepartamento.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaDepartamento.execute();
			ResultSet resultSet = (ResultSet) consultaDepartamento
					.getObject("un_Resultado");
			while (resultSet.next()) {
				departamentos += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
				// System.out.println("<option value=\""+resultSet.getObject(1)+"\">"+resultSet.getObject(2)+"</option>");
			}
			log.info("[getMunicipios] Termino");
			resultSet.close();
			consultaDepartamento.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getMunicipios] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return departamentos;
	}

	public static ArrayList<Municipios> getArrayMunicipios() {
		listaMunicipios = new ArrayList<Municipios>();
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaMunicipio = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Municipio_Consulta(?, ?)}");
			consultaMunicipio.setString("un_Nombre", "");
			consultaMunicipio.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaMunicipio.execute();
			ResultSet resultSet = (ResultSet) consultaMunicipio
					.getObject("un_Resultado");
			while (resultSet.next()) {
				if (!resultSet.getString(2).contains("LOCALIDAD")) {
					Municipios municipios = new Municipios();
					municipios.setConsecutivo(Integer.valueOf(resultSet
							.getObject(5).toString()));
					municipios.setNombre(resultSet.getObject(6).toString());
					municipios.setDepartamento(Integer.valueOf(resultSet
							.getObject(3).toString()));
					listaMunicipios.add(municipios);
				}
			}
			log.info("[getArrayMunicipios] Termino");
			resultSet.close();
			consultaMunicipio.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getArrayMunicipios] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return listaMunicipios;
	}

	public static ArrayList<Depto> getArrayDeptos() {
		listaDeptos = new ArrayList<Depto>();
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaDepartamento = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Departamento_Consulta(?, ?)}");
			consultaDepartamento.setString("un_Nombre", "");
			consultaDepartamento.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaDepartamento.execute();
			ResultSet resultSet = (ResultSet) consultaDepartamento
					.getObject("un_Resultado");
			while (resultSet.next()) {
				Depto departamento = new Depto();
				departamento.setConsecutivo(resultSet.getInt(2));
				departamento.setNombre(resultSet.getString(3));
				listaDeptos.add(departamento);
			}
			log.info("[getArrayDeptos] Termino");
			resultSet.close();
			consultaDepartamento.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getArrayDeptos] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return listaDeptos;
	}

	public static String getTipoDocumento() {
		String tipoDocumento = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaTipoDocumento = conn
					.prepareCall("{call RED_PK_TABLASTIPO.TipoIdentificacion_Consulta(?, ?)}");
			consultaTipoDocumento.setString("un_Nombre", "");
			consultaTipoDocumento.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTipoDocumento.execute();
			ResultSet resultSet = (ResultSet) consultaTipoDocumento
					.getObject("un_Resultado");
			while (resultSet.next()) {
				tipoDocumento += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getTipoDocumento] Termino");
			resultSet.close();
			consultaTipoDocumento.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getTipoDocumento] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return tipoDocumento;
	}

	public static String getTipoPersona() {
		String tipoPersona = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaTipoPersona = conn
					.prepareCall("{call RED_PK_TABLASTIPO.TipoPersona_Consulta(?, ?)}");
			consultaTipoPersona.setString("una_Descripcion", "");
			consultaTipoPersona.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTipoPersona.execute();
			ResultSet resultSet = (ResultSet) consultaTipoPersona
					.getObject("un_Resultado");
			while (resultSet.next()) {
				tipoPersona += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[consultarMetodologiaBiomasa] Termino");
			resultSet.close();
			consultaTipoPersona.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultarMetodologiaBiomasa] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return tipoPersona;
	}

	public static String getTipoInventario() {
		String tipoInventario = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaTipoInventario = conn
					.prepareCall("{call RED_PK_TABLASTIPO.TipoInventario_Consulta(?, ?)}");
			consultaTipoInventario.setString("un_Nombre", "");
			consultaTipoInventario.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTipoInventario.execute();
			ResultSet resultSet = (ResultSet) consultaTipoInventario
					.getObject("un_Resultado");
			while (resultSet.next()) {
				tipoInventario += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getTipoInventario] Termino");
			resultSet.close();
			consultaTipoInventario.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getTipoInventario] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return tipoInventario;
	}

	public static String getTipoBosque() {
		String tipoBosque = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaTipoBosque = conn
					.prepareCall("{call RED_PK_TABLASTIPO.TipoBosque_Consulta(?, ?)}");
			consultaTipoBosque.setString("un_Nombre", "");
			consultaTipoBosque.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTipoBosque.execute();
			ResultSet resultSet = (ResultSet) consultaTipoBosque
					.getObject("un_Resultado");
			while (resultSet.next()) {
				tipoBosque += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getTipoBosque] Termino");
			resultSet.close();
			consultaTipoBosque.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getTipoBosque] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return tipoBosque;
	}

	public static String getTenencia() {
		String tenencia = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaTenencia = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Tenencia_Consulta(?, ?)}");
			consultaTenencia.setString("una_Descripcion", "");
			consultaTenencia.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTenencia.execute();
			ResultSet resultSet = (ResultSet) consultaTenencia
					.getObject("un_Resultado");
			while (resultSet.next()) {
				tenencia += "<option value=\"" + resultSet.getObject(1) + "\">"
						+ resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getTenencia] Termino");
			resultSet.close();
			consultaTenencia.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getTenencia] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return tenencia;
	}

	public static String getTemporalidad() {
		String temporalidad = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaTemporalidad = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Temporalidad_Consulta(?, ?)}");
			consultaTemporalidad.setString("un_Nombre", "");
			consultaTemporalidad.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTemporalidad.execute();
			ResultSet resultSet = (ResultSet) consultaTemporalidad
					.getObject("un_Resultado");
			while (resultSet.next()) {
				temporalidad += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getTemporalidad] Termino");
			resultSet.close();
			consultaTemporalidad.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getTemporalidad] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return temporalidad;
	}

	public static String getMetodologiaGeneracion() {
		String metodologiaGeneracion = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaMetodologiaGeneracion = conn
					.prepareCall("{call RED_PK_TABLASTIPO.MetodologiaGeneracion_Consulta(?, ?)}");
			consultaMetodologiaGeneracion.setString("una_Descripcion", "");
			consultaMetodologiaGeneracion.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaMetodologiaGeneracion.execute();
			ResultSet resultSet = (ResultSet) consultaMetodologiaGeneracion
					.getObject("un_Resultado");
			while (resultSet.next()) {
				metodologiaGeneracion += "<option value=\""
						+ resultSet.getObject(1) + "\">"
						+ resultSet.getObject(2) + "</option>\n";
			}

			log.info("[getMetodologiaGeneracion] Termino");
			resultSet.close();
			consultaMetodologiaGeneracion.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getMetodologiaGeneracion] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return metodologiaGeneracion;
	}

	public static String getMetodologia() {
		String metodologia = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaMetodologia = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Metodologia_Consulta(?, ?)}");
			consultaMetodologia.setString("un_Nombre", "");
			consultaMetodologia.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaMetodologia.execute();
			ResultSet resultSet = (ResultSet) consultaMetodologia
					.getObject("un_Resultado");
			while (resultSet.next()) {
				metodologia += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}

			log.info("[getMetodologia] Termino");
			resultSet.close();
			consultaMetodologia.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getMetodologia] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return metodologia;
	}

	public static String getLicencia() {
		String licencia = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaLicencia = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Licencias_Consulta(?, ?)}");
			consultaLicencia.setString("un_Nombre", "");
			consultaLicencia.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaLicencia.execute();
			ResultSet resultSet = (ResultSet) consultaLicencia
					.getObject("un_Resultado");
			while (resultSet.next()) {
				if (resultSet.getInt(1) > 3) {
					licencia += "<option value=\"" + resultSet.getObject(1)
							+ "\">" + resultSet.getObject(2) + "</option>\n";
				}
			}

			log.info("[getLicencia] Termino");
			resultSet.close();
			consultaLicencia.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getLicencia] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return licencia;
	}

	public static String getEstado() {
		String estado = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaEstado = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Estado_Consulta(?, ?)}");
			consultaEstado.setString("un_Nombre", "");
			consultaEstado.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaEstado.execute();
			ResultSet resultSet = (ResultSet) consultaEstado
					.getObject("un_Resultado");
			while (resultSet.next()) {
				estado += "<option value=\"" + resultSet.getObject(1) + "\">"
						+ resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getEstado] Termino");
			resultSet.close();
			consultaEstado.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getEstado] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return estado;
	}

	public static String getDocumento() {
		String documento = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaDocumento = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Documento_Consulta(?, ?)}");
			consultaDocumento.setString("un_Nombre", "");
			consultaDocumento.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaDocumento.execute();
			ResultSet resultSet = (ResultSet) consultaDocumento
					.getObject("un_Resultado");
			while (resultSet.next()) {
				documento += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}

			log.info("[consultarMetodologiaBiomasa] Termino");
			resultSet.close();
			consultaDocumento.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultarMetodologiaBiomasa] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return documento;
	}

	public static String getCredencial() {
		String credencial = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaCredencial = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Credencial_Consulta(?, ?)}");
			consultaCredencial.setString("un_Nombre", "");
			consultaCredencial.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaCredencial.execute();
			ResultSet resultSet = (ResultSet) consultaCredencial
					.getObject("un_Resultado");
			while (resultSet.next()) {
				credencial += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getCredencial] Termino");
			resultSet.close();
			consultaCredencial.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getCredencial] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return credencial;
	}

	public static String getCAR() {
		String car = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaCar = conn
					.prepareCall("{call RED_PK_TABLASTIPO.CAR_Consulta(?, ?)}");
			consultaCar.setString("un_Nombre", "");
			consultaCar
					.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaCar.execute();
			ResultSet resultSet = (ResultSet) consultaCar
					.getObject("un_Resultado");
			while (resultSet.next()) {
				car += "<option value=\"" + resultSet.getObject(1) + "\">"
						+ resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getCAR] Termino");
			resultSet.close();
			consultaCar.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getCAR] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return car;
	}

	public static ArrayList<CarDepto> getCarDepto() {
		ArrayList<CarDepto> carDepto = new ArrayList<CarDepto>();
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaCarDepto = conn
					.prepareCall("{call RED_PK_TABLASTIPO.CAR_DEPTO_Consulta(?, ?)}");
			consultaCarDepto.setString("un_depto", "");
			consultaCarDepto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaCarDepto.execute();
			ResultSet resultSet = (ResultSet) consultaCarDepto
					.getObject("un_Resultado");
			while (resultSet.next()) {
				CarDepto cd = new CarDepto();
				cd.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				cd.setCar(Integer.valueOf(resultSet.getObject(2).toString()));
				cd.setDepto(Integer.valueOf(resultSet.getObject(3).toString()));
				carDepto.add(cd);
			}
			log.info("[getCarDepto] Termino");
			resultSet.close();
			consultaCarDepto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getCarDepto] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return carDepto;
	}

	public static ArrayList<CAR> getCARs() {
		ArrayList<CAR> car = new ArrayList<CAR>();
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaCar = conn
					.prepareCall("{call RED_PK_TABLASTIPO.CAR_Consulta(?, ?)}");
			consultaCar.setString("un_Nombre", "");
			consultaCar
					.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaCar.execute();
			ResultSet resultSet = (ResultSet) consultaCar
					.getObject("un_Resultado");
			while (resultSet.next()) {
				CAR car2 = new CAR();
				car2.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				car2.setNombre(resultSet.getObject(2).toString());
				car.add(car2);
			}
			log.info("[getCARs] Termino");
			resultSet.close();
			consultaCar.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getCARs] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return car;
	}

	public static String getAlturaIndividuo() {
		String alturaIndividuo = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaAlturaIndividuo = conn
					.prepareCall("{call RED_PK_TABLASTIPO.AlturaIndividuo_Consulta(?, ?)}");
			consultaAlturaIndividuo.setString("un_Nombre", "");
			consultaAlturaIndividuo.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaAlturaIndividuo.execute();
			ResultSet resultSet = (ResultSet) consultaAlturaIndividuo
					.getObject("un_Resultado");
			while (resultSet.next()) {
				alturaIndividuo += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getAlturaIndividuo] Termino");
			resultSet.close();
			consultaAlturaIndividuo.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getAlturaIndividuo] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return alturaIndividuo;
	}

	public static String getActividad() {
		String actividad = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaActividad = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Actividad_Consulta(?, ?)}");
			consultaActividad.setString("un_Nombre", "");
			consultaActividad.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaActividad.execute();
			ResultSet resultSet = (ResultSet) consultaActividad
					.getObject("un_Resultado");
			while (resultSet.next()) {
				actividad += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getActividad] Termino");
			resultSet.close();
			consultaActividad.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getActividad] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return actividad;
	}

	public static String getActividadUsuario() {
		String actividadUsuario = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaActividadUsuario = conn
					.prepareCall("{call RED_PK_TABLASTIPO.ActividadUsuario_Consulta(?, ?)}");
			consultaActividadUsuario.setString("una_Descripcion", "");
			consultaActividadUsuario.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaActividadUsuario.execute();
			ResultSet resultSet = (ResultSet) consultaActividadUsuario
					.getObject("un_Resultado");
			while (resultSet.next()) {
				actividadUsuario += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getActividadUsuario] Termino");
			resultSet.close();
			consultaActividadUsuario.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getActividadUsuario] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return actividadUsuario;
	}

	public static String getProposito() {
		String propositoParcela = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultarPropuesta = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Propuesta_Consulta(?, ?)}");
			consultarPropuesta.setString("una_clave", "");
			consultarPropuesta.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPropuesta.execute();
			ResultSet resultSet = (ResultSet) consultarPropuesta
					.getObject("un_Resultado");
			while (resultSet.next()) {
				propositoParcela += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getProposito] Termino");
			resultSet.close();
			consultarPropuesta.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getProposito] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return propositoParcela;
	}

	public static String getPertenencia() {
		String pertenencia = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultarPertenencia = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Pertenencia_Consulta(?, ?)}");
			consultarPertenencia.setString("una_clave", "");
			consultarPertenencia.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPertenencia.execute();
			ResultSet resultSet = (ResultSet) consultarPertenencia
					.getObject("un_Resultado");
			while (resultSet.next()) {
				pertenencia += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getPertenencia] Termino");
			resultSet.close();
			consultarPertenencia.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getPertenencia] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return pertenencia;
	}

	public static String consultarTemporalidad() {
		String temporalidad = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaTemporalidad = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Temporalidad_Consulta(?, ?)}");
			consultaTemporalidad.setString("un_Nombre", "");
			consultaTemporalidad.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTemporalidad.execute();
			ResultSet resultSet = (ResultSet) consultaTemporalidad
					.getObject("un_Resultado");
			while (resultSet.next()) {
				temporalidad += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[consultarTemporalidad] Termino");
			resultSet.close();
			consultaTemporalidad.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultarTemporalidad] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return temporalidad;
	}

	public static Temporalidad consultarTemporalidadId(Integer idTemporalidad) {
		temp = new Temporalidad();
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaTemporalidad = conn
					.prepareCall("{call RED_PK_TABLASTIPO.TemporalidadId_Consulta(?, ?)}");
			consultaTemporalidad.setInt("un_consecutivo", idTemporalidad);
			consultaTemporalidad.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTemporalidad.execute();
			ResultSet resultSet = (ResultSet) consultaTemporalidad
					.getObject("un_Resultado");
			while (resultSet.next()) {
				temp.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				temp.setNombre(resultSet.getObject(2).toString());
			}
			log.info("[consultarTemporalidadId] Termino");
			resultSet.close();
			consultaTemporalidad.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultarTemporalidadId] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return temp;
	}

	public static String getPropositoParcela() {
		String propositoParcela = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaPropositoParcela = conn
					.prepareCall("{call RED_PK_TABLASTIPO.PropositoParcela_Consulta(?, ?)}");
			consultaPropositoParcela.setString("un_Nombre", "");
			consultaPropositoParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaPropositoParcela.execute();
			ResultSet resultSet = (ResultSet) consultaPropositoParcela
					.getObject("un_Resultado");
			while (resultSet.next()) {
				propositoParcela += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getPropositoParcela] Termino");
			resultSet.close();
			consultaPropositoParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getPropositoParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return propositoParcela;
	}

	public static String getTipoReporte(String idReportes) {
		String tipoReporte = "";
		CallableStatement consultaTipoReporte=null;
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			
			if(idReportes.equals("bosques"))
			{
			    consultaTipoReporte = conn
					.prepareCall("{call RED_PK_TABLASTIPO.TipoReporte_ConsultaBosques(?, ?)}");
			}else{
				consultaTipoReporte = conn
						.prepareCall("{call RED_PK_TABLASTIPO.TipoReporte_ConsultaCarbono(?, ?)}");
			}
			
			consultaTipoReporte.setString("un_Nombre", null);
			consultaTipoReporte.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTipoReporte.execute();
			ResultSet resultSet = (ResultSet) consultaTipoReporte
					.getObject("un_Resultado");
			while (resultSet.next()) {
				tipoReporte += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getTipoReporte] Termino");
			resultSet.close();
			consultaTipoReporte.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getTipoReporte] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return tipoReporte;
	}

	public static String getDivisionTerritorio() {
		String divTerritorio = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaDivTerritorio = conn
					.prepareCall("{call RED_PK_TABLASTIPO.DivTerritorio_Consulta(?, ?)}");
			consultaDivTerritorio.setString("un_Nombre", null);
			consultaDivTerritorio.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaDivTerritorio.execute();
			ResultSet resultSet = (ResultSet) consultaDivTerritorio
					.getObject("un_Resultado");
			while (resultSet.next()) {
				divTerritorio += "<option value=\"" + resultSet.getObject(1)
						+ "\">" + resultSet.getObject(2) + "</option>\n";
			}
			log.info("[getDivisionTerritorio] Termino");
			resultSet.close();
			consultaDivTerritorio.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getDivisionTerritorio] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return divTerritorio;
	}

	public static ArrayList<Reportes> getPeriodos() {
		ArrayList<Reportes> listaReportes = new ArrayList<Reportes>();
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaPeriodosReportes = conn
					.prepareCall("{call RED_PK_TABLASTIPO.PeriodosReportes_Consulta(?)}");
			consultaPeriodosReportes.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaPeriodosReportes.execute();
			ResultSet resultSet = (ResultSet) consultaPeriodosReportes
					.getObject("un_Resultado");
			while (resultSet.next()) {
				Reportes reportes = new Reportes();
				reportes.setConsecutivo(resultSet.getInt(1));
				reportes.setFechaGeneracion(resultSet.getDate(2));
				reportes.setDivision(resultSet.getInt(3));
				reportes.setPeriodoUno(resultSet.getInt(4));
				reportes.setPeriodoDos(resultSet.getInt(7));
				reportes.setTipoReporte(resultSet.getInt(5));
				listaReportes.add(reportes);
			}
			log.info("[getPeriodos] Termino");
			resultSet.close();
			consultaPeriodosReportes.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getPeriodos] Fallo");
			e.printStackTrace();
		}
		return listaReportes;
	}

	public static String getPeriodos2() {
		String periodo = "";
		Calendar cal = Calendar.getInstance();
		int actual = cal.get(Calendar.YEAR);
		for (int x = 1970; x <= actual; x++) {
			periodo += "<option value=\"" + x + "\">" + x + "</option>\n";
		}
		return periodo;
	}

	public static String getParcelas() {
		String parcelas = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaParcela = conn
					.prepareCall("{call RED_PK_TABLASTIPO.Parcelas_Consulta(?,?,?,?)}");
			consultaParcela.setInt("un_usuario", usuario);
			consultaParcela.setInt("un_idusuario", idUsuario);
			consultaParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaParcela.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultaParcela.execute();
			System.out.println(consultaParcela.getObject("sentencia"));
			ResultSet resultSet = (ResultSet) consultaParcela
					.getObject("un_Resultado");
			while (resultSet.next()) {
				if (resultSet.getObject(2) != null)
					parcelas += "<option value=\"" + resultSet.getObject(1)
							+ "\">" + resultSet.getObject(2) + "</option>\n";
				else
					parcelas += "<option value=\"" + resultSet.getObject(1)
							+ "\">" + resultSet.getObject(32) + "</option>\n";
			}
			log.info("[getParcelas] Termino");
			resultSet.close();
			consultaParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getParcelas] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return parcelas;
	}

	public static ArrayList<String> getCodigoCampoParcelas(
			ArrayList<Integer> idParcela) {
		ArrayList<String> codigosCampos = new ArrayList<String>();
		Integer idP;
		for (int i = 0; i < idParcela.size(); i++) {
			idP = idParcela.get(i);
			try {
				log = SMBC_Log.Log(CargaDatosSelect.class);
				conn = dataSource.getConnection();
				CallableStatement consultaParcela = conn
						.prepareCall("{call RED_PK_TABLASTIPO.ConsultaCodigoCampo(?,?)}");
				consultaParcela.setInt("un_consecutivo", idP);
				consultaParcela.registerOutParameter("un_Resultado",
						OracleTypes.CURSOR);
				consultaParcela.execute();
				ResultSet resultSet = (ResultSet) consultaParcela
						.getObject("un_Resultado");
				while (resultSet.next()) {
					codigosCampos.add(resultSet.getString(1));
				}
				log.info("[getCodigoCampoParcelas] Termino");
				resultSet.close();
				consultaParcela.close();
				conn.close();

			} catch (Exception e) {
				log.error("[getCodigoCampoParcelas] Fallo");
				e.printStackTrace();
				/*
				 * try { conn.rollback(); } catch (SQLException e1) {
				 * e1.printStackTrace(); } e.printStackTrace();
				 */
			}
		}
		return codigosCampos;
	}

	public static Parcela consultaParcelas(Integer idParcela) {
		Parcela p = new Parcela();
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaParcelaId = conn
					.prepareCall("{call RED_PK_TABLASTIPO.ConsultaParcela(?,?)}");
			consultaParcelaId.setInt("un_consecutivo", idParcela);
			consultaParcelaId.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaParcelaId.execute();
			ResultSet resultSet = (ResultSet) consultaParcelaId
					.getObject("un_Resultado");
			while (resultSet.next()) {
				p.setConsecutivo(resultSet.getInt(1));
				p.setNombre(resultSet.getString(2));
				p.setCodigoCampo(resultSet.getString(32));
			}
			log.info("[consultaParcelas] Termino");
			resultSet.close();
			consultaParcelaId.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultaParcelas] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return p;
	}

	public static MetodologiaBiomasa consultaMetodologiaBiomasa(
			Integer idMetodologia) {
		MetodologiaBiomasa mb = new MetodologiaBiomasa();
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaParcelaId = conn
					.prepareCall("{call RED_PK_TABLASTIPO.ConsultaMetodologia(?,?)}");
			consultaParcelaId.setInt("un_consecutivo", idMetodologia);
			consultaParcelaId.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaParcelaId.execute();
			ResultSet resultSet = (ResultSet) consultaParcelaId
					.getObject("un_Resultado");
			while (resultSet.next()) {
				mb.setNombre(resultSet.getString(2));
				mb.setDescripcion(resultSet.getString(3));
				mb.setEcuacion(resultSet.getString(4));
			}
			log.info("[consultaMetodologiaBiomasa] Termino");
			resultSet.close();
			consultaParcelaId.close();
			conn.close();

		} catch (Exception e) {
			log.error("[consultaMetodologiaBiomasa] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return mb;
	}

	public static Integer getIdUsuario() {
		return idUsuario;
	}

	public static void setIdUsuario(Integer idus) {
		idUsuario = idus;
	}

	public Integer getUsuario() {
		return usuario;
	}

	public static void setUsuario(Integer us) {
		usuario = us;
	}

	public static String getRoles() {
		String pais = "";
		try {
			log = SMBC_Log.Log(CargaDatosSelect.class);
			conn = dataSource.getConnection();
			CallableStatement consultaRoles = conn
					.prepareCall("{call RED_PK_USUARIOS.consultarRoles(?)}");
			consultaRoles.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaRoles.execute();
			ResultSet resultSet = (ResultSet) consultaRoles
					.getObject("un_Resultado");
			while (resultSet.next()) {
				if (resultSet.getInt(1) > 1 && resultSet.getInt(1) < 12)
					pais += "<option value=\"" + resultSet.getObject(1) + "\">"
							+ resultSet.getString(2).replace('_', ' ')
							+ "</option>\n";
			}
			log.info("[getRoles] Termino");
			resultSet.close();
			consultaRoles.close();
			conn.close();
		} catch (Exception e) {
			log.error("[getRoles] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return pais;
	}

}
