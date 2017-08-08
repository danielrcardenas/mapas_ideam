package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.CLOB;
import co.gov.ideamredd.conexionBD.ConexionBDParcelas;
import co.gov.ideamredd.entities.BiomasaYCarbono;
import co.gov.ideamredd.entities.CAR;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.ContactoParcela;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.Individuo;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Organizacion;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.Parcela;
import co.gov.ideamredd.entities.Proposito;
import co.gov.ideamredd.entities.SioEstaciones;
import co.gov.ideamredd.entities.SioFuenteGeneradora;
import co.gov.ideamredd.entities.SioPuntosMonitoreo;
import co.gov.ideamredd.entities.Temporalidad;
import co.gov.ideamredd.entities.TipoBosque;
import co.gov.ideamredd.entities.TipoInventario;
import co.gov.ideamredd.entities.TmpFamilia;
import co.gov.ideamredd.entities.Usuario;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.util.Util;

@Stateless
public class ConsultarAsociadosParcela {

	@EJB
	private ConexionBDParcelas conexionBD;

	private Connection conn;
	private ArrayList<TipoBosque> bosque;
	private ArrayList<Depto> depto;
	private ArrayList<Municipios> municipio;
	private ArrayList<CAR> car;
	private ArrayList<String> fisiografia;
	// private ArrayList<Contacto> contactos;
	// private Proyecto proyecto;
	private Contacto contacto;
	private Proposito proposito;
	private TipoInventario inventario;
	private ArrayList<Individuo> individuos;
	private TmpFamilia familia;

	// private static Logger log;

	public ConsultarAsociadosParcela() {
		// log = SMBC_Log.log(ConsultarAsociadosParcela.class);
	}

	public ArrayList<TipoBosque> consultaTipoBosqueParcela(Integer idParcela) {
		try {
			// log.info("Inicio de la consulta de tipos de bosque para la parcela id:"
			// + idParcela);
			conn = conexionBD.establecerConexion();
			bosque = new ArrayList<TipoBosque>();
			CallableStatement consultarBosqueParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.BosqueParcela_Consulta(?,?)}");
			consultarBosqueParcela.setInt("una_parcela", idParcela);
			consultarBosqueParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarBosqueParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarBosqueParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				TipoBosque tipoBosque = new TipoBosque();
				tipoBosque.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				tipoBosque.setTipoBosque((String) resultSet.getObject(2));
				tipoBosque.setIdParcela(Integer.valueOf(resultSet.getObject(3)
						.toString()));
				tipoBosque.setPrecipitacion((String) resultSet.getObject(4));
				tipoBosque.setTemperatura((String) resultSet.getObject(5));
				tipoBosque.setAltitud((String) resultSet.getObject(6));
				tipoBosque.setIdBosque(Integer.valueOf(resultSet.getObject(7)
						.toString()));
				bosque.add(tipoBosque);
			}
			resultSet.close();
			consultarBosqueParcela.close();
			conn.close();
			// log.info("consulta exitosa de los tipos de bosque para la parcela id:"
			// + idParcela);
		} catch (Exception e) {
			// log.error("Error en la consulta de los tipos de bosque de la parcela id:"
			// + idParcela);
			e.printStackTrace();
		}
		return bosque;
	}

	public ArrayList<Depto> consultaDeptoParcela(Integer idParcela) {
		try {
			// log.info("Inicio de la consulta de departamento(s) de la parcela id:"
			// + idParcela);
			conn = conexionBD.establecerConexion();
			depto = new ArrayList<Depto>();
			CallableStatement consultarDeptoParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.DeptoParcela_Consulta(?,?)}");
			consultarDeptoParcela.setInt("una_parcela", idParcela);
			consultarDeptoParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarDeptoParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarDeptoParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				Depto departamento = new Depto();
				departamento.setConsecutivo(resultSet.getInt(1));
				departamento.setNombre((String) resultSet.getObject(2));
				depto.add(departamento);
			}
			resultSet.close();
			consultarDeptoParcela.close();
			conn.close();
			// log.info("consulta exitosa de departamento(s) para la parcela id:"
			// + idParcela);
		} catch (Exception e) {
			// log.error("Error en la consulta de departamento(s) de la parcela id:"
			// + idParcela);
			e.printStackTrace();
		}
		return depto;
	}

	public ArrayList<Municipios> ConsultaMunicipioParcela(Integer idParcela) {
		try {
			// log.info("Inicio de la consulta de municipio(s) para la parcela id:"
			// + idParcela);
			conn = conexionBD.establecerConexion();
			municipio = new ArrayList<Municipios>();
			CallableStatement consultarMunicipioParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.MunicipioParcela_Consulta(?,?)}");
			consultarMunicipioParcela.setInt("una_parcela", idParcela);
			consultarMunicipioParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarMunicipioParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarMunicipioParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				if (!resultSet.getString(2).contains("LOCALIDAD")) {
					Municipios municipios = new Municipios();
					municipios.setConsecutivo(resultSet.getInt(1));
					municipios.setNombre(resultSet.getString(2));
					municipios.setDepartamento(resultSet.getInt(3));
					municipio.add(municipios);
				}
			}
			resultSet.close();
			consultarMunicipioParcela.close();
			conn.close();
			// log.info("consulta exitosa de municipio(s) para la parcela id:"
			// + idParcela);
		} catch (Exception e) {
			// log.error("Error en la consulta de municipio(s) de la parcela id:"
			// + idParcela);
			e.printStackTrace();
		}
		return municipio;
	}

	public ArrayList<CAR> ConsultaCarParcela(Integer idParcela) {
		try {
			// log.info("Inicio de la consulta de CAR para la parcela id:"
			// + idParcela);
			conn = conexionBD.establecerConexion();
			car = new ArrayList<CAR>();
			CallableStatement consultarCarParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.CarParcela_Consulta(?,?)}");
			consultarCarParcela.setInt("una_parcela", idParcela);
			consultarCarParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarCarParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarCarParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				CAR c = new CAR();
				c.setConsecutivo(resultSet.getInt(1));
				c.setNombre(resultSet.getString(2));
				car.add(c);
			}
			resultSet.close();
			consultarCarParcela.close();
			conn.close();
			// log.info("consulta exitosa de CAR para la parcela id:" +
			// idParcela);
		} catch (Exception e) {
			// log.error("Error en la consulta de CAR de la parcela id:"
			// + idParcela);
			e.printStackTrace();
		}
		return car;
	}

	public Pais ConsultarPaisParcela(Integer idParcela) {
		Pais pais = new Pais();
		try {
			// log.info("Inicio de la consulta del pais de la parcela id:"
			// + idParcela);
			conn = conexionBD.establecerConexion();
			CallableStatement consultarPaisParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.PaisParcela_Consulta(?,?)}");
			consultarPaisParcela.setInt("una_parcela", idParcela);
			consultarPaisParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPaisParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarPaisParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				pais.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				pais.setNombre((String) resultSet.getObject(2));
			}
			resultSet.close();
			consultarPaisParcela.close();
			conn.close();
			// log.info("consulta exitosa del pais de la parcela id:" +
			// idParcela);
		} catch (Exception e) {
			// log.error("Error en la consulta del pais de la parcela id:"
			// + idParcela);
			e.printStackTrace();
		}
		return pais;
	}

	public Temporalidad ConsultarTemporalidadParcela(Integer idTemporalidad) {
		Temporalidad temporalidad = new Temporalidad();
		try {
			// log.info("Inicio de la consulta de la temporalidad de una parcela");
			conn = conexionBD.establecerConexion();
			CallableStatement consultarTempParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.TemporalidadParcela_Consulta(?,?)}");
			consultarTempParcela.setInt("una_temporalidad", idTemporalidad);
			consultarTempParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarTempParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarTempParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				temporalidad.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				temporalidad.setNombre((String) resultSet.getObject(2));
			}
			resultSet.close();
			consultarTempParcela.close();
			conn.close();
			// log.info("consulta exitosa de la temporalidad de la parcela");
		} catch (Exception e) {
			// log.error("Error en la consulta de la temporalidad de la parcela");
			e.printStackTrace();
		}
		return temporalidad;
	}

	public ArrayList<String> ConsultaFisiografiaParcela(Integer idParcela) {
		try {
			// log.info("Inicio de la consulta de la fisiografia de la parcela id:"
			// + idParcela);
			conn = conexionBD.establecerConexion();
			fisiografia = new ArrayList<String>();
			CallableStatement consultarFisiografiaParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.Consulta_Fisiografia_Parcela(?,?)}");
			consultarFisiografiaParcela.setInt("una_parcela", idParcela);
			consultarFisiografiaParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarFisiografiaParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarFisiografiaParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				fisiografia.add(resultSet.getObject(1).toString());
			}
			resultSet.close();
			consultarFisiografiaParcela.close();
			conn.close();
			// log.info("consulta exitosa de la fisiografia para la parcela id:"
			// + idParcela);
		} catch (Exception e) {
			// log.error("Error en la consulta de la fisiografia de la parcela id:"
			// + idParcela);
			e.printStackTrace();
		}
		return fisiografia;
	}

	// public Proyecto ConsultaProyectoParcela(Integer idParcela) {
	// try {
	// conn = dataSource.getConnection();
	// proyecto = new Proyecto();
	// CallableStatement consultarParcelaProyecto = conn
	// .prepareCall("{call RED_PK_PARCELAS.ProyectoParcela_Consulta(?,?)}");
	// consultarParcelaProyecto.setInt("una_parcela", idParcela);
	// consultarParcelaProyecto.registerOutParameter("un_Resultado",
	// OracleTypes.CURSOR);
	// consultarParcelaProyecto.execute();
	// OracleResultSet resultSet = (OracleResultSet) consultarParcelaProyecto
	// .getObject("un_Resultado");
	//
	// while (resultSet.next()) {
	// proyecto.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
	// .toString()));
	// proyecto.setNombre((String) resultSet.getObject(2));
	// proyecto.setDescripcionArea((String) resultSet.getObject(3));
	// proyecto.setTipoBosques((String) resultSet.getObject(4));
	// proyecto.setFechaInicio((Timestamp) resultSet.getObject(5));
	// proyecto.setFechaFin((Timestamp) resultSet.getObject(6));
	// proyecto.setPais(Integer.valueOf(resultSet.getObject(7)
	// .toString()));
	// proyecto.setEstado(Integer.valueOf(resultSet.getObject(8)
	// .toString()));
	// proyecto.setArea((BigDecimal) resultSet.getObject(9));
	// proyecto.setCo2Reducir((BigDecimal) resultSet.getObject(10));
	// proyecto
	// .setTasaDeforestar((BigDecimal) resultSet.getObject(11));
	// proyecto.setDuracionProyecto(Integer.valueOf(resultSet
	// .getObject(12).toString()));
	// proyecto.setPropietario(Integer.valueOf(resultSet.getObject(13)
	// .toString()));
	// proyecto.setPublico(Integer.valueOf(resultSet.getObject(14)
	// .toString()));
	// }
	// resultSet.close();
	// consultarParcelaProyecto.close();
	// } catch (Exception e) {
	// try {
	// conn.rollback();
	// } catch (SQLException e1) {
	// e1.printStackTrace();
	// }
	// e.printStackTrace();
	// }
	// return proyecto;
	// }

	// public Contacto ConsultarContactoParcela(Integer idContacto,
	// Integer idUsuario) {
	// try {
	// conn = conexionBD.establecerConexion();
	// contacto = new Contacto();
	// CallableStatement consultarContactoParcela = conn
	// .prepareCall("{call RED_PK_PARCELAS.ConsultarContactoParcela(?,?,?,?)}");
	// consultarContactoParcela.setInt("un_contacto", idContacto);
	// consultarContactoParcela.setInt("un_usuario", idUsuario);
	// consultarContactoParcela.registerOutParameter("un_Resultado",
	// OracleTypes.CURSOR);
	// consultarContactoParcela.registerOutParameter("sentencia",
	// OracleTypes.VARCHAR);
	// consultarContactoParcela.execute();
	// OracleResultSet resultSet = (OracleResultSet) consultarContactoParcela
	// .getObject("un_Resultado");
	//
	// System.out.println(consultarContactoParcela.getObject("sentencia"));
	//
	// while (resultSet.next()) {
	// contacto.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
	// .toString()));
	// contacto.setNombre(resultSet.getObject(2).toString());
	// contacto.setPais(Integer.valueOf(resultSet.getObject(3)
	// .toString()));
	// // contacto.setDireccion(resultSet.getObject(4) == null ? "":
	// // resultSet.getObject(4).toString());
	// contacto.setTelefono(resultSet.getObject(5) == null ? ""
	// : resultSet.getObject(5).toString());
	// contacto.setMovil(resultSet.getObject(6) == null ? ""
	// : resultSet.getObject(6).toString());
	// contacto.setCorreo(resultSet.getObject(7) == null ? ""
	// : resultSet.getObject(7).toString());
	// // contacto.setTipo(resultSet.getObject(10) == null ? "":
	// // resultSet.getObject(10).toString());
	// // contacto.setOrganizacion(resultSet.getObject(8) == null ? "":
	// // resultSet.getObject(8).toString());
	// // contacto.setCargo(resultSet.getObject(9) == null ? "":
	// // resultSet.getObject(9).toString());
	// // contacto.setSector(resultSet.getObject(11) == null ? "":
	// // resultSet.getObject(11).toString());
	// }
	// resultSet.close();
	// consultarContactoParcela.close();
	// conn.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return contacto;
	// }

	public Contacto ConsultarContactoId(Integer idContacto) {
		try {
			// log.info("Inicio de la consulta del contacto id: " + idContacto);
			conn = conexionBD.establecerConexion();
			CallableStatement consultarContacto = conn
					.prepareCall("{call RED_PK_PARCELAS.consulta_ContactoId(?,?)}");
			consultarContacto.setInt("un_consecutivo", idContacto);
			consultarContacto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarContacto.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarContacto
					.getObject("un_Resultado");
			while (resultSet.next()) {
				contacto = new Contacto();
				contacto.setConsecutivo(resultSet.getInt(1));
				contacto.setNombre(resultSet.getString(2));
				contacto.setPais(resultSet.getInt(3));
				contacto.setTelefono(resultSet.getString(4) == null ? ""
						: resultSet.getString(4));
				contacto.setMovil(resultSet.getString(5) == null ? ""
						: resultSet.getString(5));
				contacto.setCorreo(resultSet.getString(6));
				contacto.setMunicipio(((Integer) resultSet.getInt(7)) == null ? 0
						: resultSet.getInt(7));
			}
			resultSet.close();
			consultarContacto.close();
			conn.close();
			// log.info("Consulta exitosa del contacto id: " + idContacto);
		} catch (Exception e) {
			// log.error("Error en la consulta del contacto id: " + idContacto);
			e.printStackTrace();
		}
		return contacto;
	}

	public Organizacion ConsultarOrganizacionId(Integer idOrganizacion) {
		Organizacion org = null;
		try {
			// log.info("Inicio de la consulta de la organizacion id: "
			// + idOrganizacion);
			conn = conexionBD.establecerConexion();
			CallableStatement consultarOrganizacion = conn
					.prepareCall("{call RED_PK_PARCELAS.consulta_OrganizacionId(?,?)}");
			consultarOrganizacion.setInt("un_consecutivo", idOrganizacion);
			consultarOrganizacion.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarOrganizacion.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarOrganizacion
					.getObject("un_Resultado");
			while (resultSet.next()) {
				org = new Organizacion();
				org.setConsecutivo(resultSet.getInt(1));
				org.setNombre(resultSet.getString(2));
				org.setDireccion(resultSet.getString(3));
				org.setTelefono(resultSet.getInt(4));
				org.setCorreo(resultSet.getString(5));
				org.setSector(resultSet.getInt(6));
				org.setPais(resultSet.getInt(7));
				org.setDepto(resultSet.getInt(8));
				org.setMunicipio(resultSet.getInt(9));
			}
			resultSet.close();
			consultarOrganizacion.close();
			conn.close();
			// log.info("Consulta exitosa de la organizacion id: "
			// + idOrganizacion);
		} catch (Exception e) {
			// log.error("Error en la consulta de la organizacion id: "
			// + idOrganizacion);
			e.printStackTrace();
		}
		return org;
	}

	public ContactoParcela consultaContactoPorParcelaClase(Integer idParcela,
			Integer idClase) {
		ContactoParcela contactoParcela = null;
		try {
			// log.info("Inicio de la consulta del contacto de la parcela: "
			// + idParcela + " y clase de contacto: " + idClase);
			conn = conexionBD.establecerConexion();
			CallableStatement consultarContactoPorParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.ConsContXParcelaClase(?,?,?)}");
			consultarContactoPorParcela.setInt("un_consecutivo", idParcela);
			consultarContactoPorParcela.setInt("una_clase", idClase);
			consultarContactoPorParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarContactoPorParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarContactoPorParcela
					.getObject("un_Resultado");
			while (resultSet.next()) {
				contactoParcela = new ContactoParcela();
				contactoParcela.setIdParcela(resultSet.getInt(1));
				contactoParcela.setIdContacto(resultSet.getInt(2));
				contactoParcela.setIdClase(resultSet.getInt(3));
				contactoParcela.setIdOrganizacion(resultSet.getInt(4));
			}
			resultSet.close();
			conn.close();
			// log.info("Consulta exitosa del contacto de la parcela: "
			// + idParcela + " y clase de contacto: " + idClase);
		} catch (Exception e) {
			// log.error("Error en la consulta del contacto de la parcela: "
			// + idParcela + " y clase de contacto: " + idClase);
			e.printStackTrace();
		}
		return contactoParcela;
	}

	public Proposito ConsultarPropositoParcela(Integer idProposito) {
		try {
			// log.info("Inicio de la consulta del proposito de la parcela");
			conn = conexionBD.establecerConexion();
			proposito = new Proposito();
			CallableStatement consultarPropositoParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.PropositoParcela_Consulta(?,?)}");
			consultarPropositoParcela.setInt("un_proposito", idProposito);
			consultarPropositoParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPropositoParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarPropositoParcela
					.getObject("un_Resultado");
			while (resultSet.next()) {
				proposito.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				proposito.setNombre((String) resultSet.getObject(2));
			}
			resultSet.close();
			consultarPropositoParcela.close();
			conn.close();
			// log.info("Consulta exitosa del proposito de la parcela");
		} catch (Exception e) {
			// log.error("Error en la consulta del proposiro de la parcela");
			e.printStackTrace();
		}
		return proposito;
	}

	public TipoInventario ConsultarTipoInventarioParcela(Integer idTipo) {
		try {
			// log.info("Inicio de la consulta del tipo de inventario de la parcela");
			conn = conexionBD.establecerConexion();
			inventario = new TipoInventario();
			CallableStatement consultarTipoInventarioParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.TipoInventarioParcela_Consulta(?,?)}");
			consultarTipoInventarioParcela.setInt("un_tipo", idTipo);
			consultarTipoInventarioParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarTipoInventarioParcela.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarTipoInventarioParcela
					.getObject("un_Resultado");
			while (resultSet.next()) {
				inventario.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				inventario.setNombre((String) resultSet.getObject(2));
			}
			resultSet.close();
			consultarTipoInventarioParcela.close();
			conn.close();
			// log.info("Consulta exitosa del tipo de inventario de la parcela");
		} catch (Exception e) {
			// log.error("Error en la consulta del tipo de inventario de la parcela");
			e.printStackTrace();
		}
		return inventario;
	}

	public String[] ConsultarGeoParcela(Integer idParcela) {
		Clob geometria = null;
		String[] coordenadas = null;
		try {
			// log.info("Inicio de la consulta de la geometria de la parcela "
			// + idParcela);
			conn = conexionBD.establecerConexion();
			CallableStatement consultarGeoProyecto = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta(?,?)}");
			consultarGeoProyecto.setInt("un_Consecutivo", idParcela);
			consultarGeoProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CLOB);
			consultarGeoProyecto.execute();
			geometria = (CLOB) consultarGeoProyecto.getObject("un_Resultado");
			consultarGeoProyecto.close();
			conn.close();
			coordenadas = Util.obtenerDatosGeometria(Util
					.clobStringConversion(geometria));
			// log.info("Consulta exitosa de la geometria de la parcela "
			// + idParcela);
		} catch (Exception e) {
			// log.error("Error en la consulta de la geometria de la parcela "
			// + idParcela);
			e.printStackTrace();
		}
		return coordenadas;
	}

	public BiomasaYCarbono ConsultarUltimaBiomasaParcela(Integer idParcela) {
		BiomasaYCarbono bc = null;
		try {
			// log.info("Inicio de la consulta de biomasa y carbono de la parcela "
			// + idParcela);
			conn = conexionBD.establecerConexion();
			CallableStatement consultarUltimaBiomasa = conn
					.prepareCall("{call RED_PK_PARCELAS.consultar_UltimaBiomasa(?,?)}");
			consultarUltimaBiomasa.setInt("una_parcela", idParcela);
			consultarUltimaBiomasa.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarUltimaBiomasa.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarUltimaBiomasa
					.getObject("un_Resultado");
			while (resultSet.next()) {
				bc = new BiomasaYCarbono();
				bc.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				bc.setBiomasa(Double.valueOf(resultSet.getObject(2).toString()));
				bc.setCarbono(Double.valueOf(resultSet.getObject(3).toString()));
				bc.setFechaInicio((Timestamp) resultSet.getObject(7));
				bc.setMetodologia(Integer.valueOf(resultSet.getObject(4)
						.toString()));
			}
			resultSet.close();
			consultarUltimaBiomasa.close();
			conn.close();
			// log.info("Consulta exitosa de la biomasa y carbono de la parcela "
			// + idParcela);
		} catch (Exception e) {
			// log.error("Error en la consulta de la biomasa y carbono de la parcela "
			// + idParcela);
			e.printStackTrace();
		}
		return bc;
	}

	// TODO: Activa cuando se implementen los usuarios de la aplicacion.
	// public Usuario ConsultarContactoUsuario(Integer idUsuario) {
	// Usuario u = null;
	// try {
	// conn = conexionBD.establecerConexion();
	// CallableStatement consultarUltimaBiomasa = conn
	// .prepareCall("{call RED_PK_PARCELAS.consultar_contactoUsuario(?,?)}");
	// consultarUltimaBiomasa.setInt("un_consecutivo", idUsuario);
	// consultarUltimaBiomasa.registerOutParameter("un_Resultado",
	// OracleTypes.CURSOR);
	// consultarUltimaBiomasa.execute();
	// OracleResultSet resultSet = (OracleResultSet) consultarUltimaBiomasa
	// .getObject("un_Resultado");
	// while (resultSet.next()) {
	// u = new Usuario();
	// u.setIdUsuario(resultSet.getInt(1));
	// u.setNombre((String) resultSet.getObject(2));
	// u.setIdentificacion(resultSet.getString(3));
	// u.setDireccion(resultSet.getString(4) != null ? resultSet
	// .getString(4) : "");
	// u.setTelefonoOficina(resultSet.getString(5) != null ? resultSet
	// .getString(5) : "");
	// u.setCelular(resultSet.getString(6) != null ? resultSet
	// .getString(6) : "");
	// u.setCorreoElectronico((String) resultSet.getObject(7));
	// u.setPais(resultSet.getInt(12));
	// u.setApellidoUno((String) resultSet.getObject(14));
	// u.setApellidoDos((String) resultSet.getObject(15));
	// u.setOrganizacion(resultSet.getString(18) != null ? resultSet
	// .getString(18) : "");
	// u.setCargo(resultSet.getString(19) != null ? resultSet
	// .getString(19) : "");
	//
	// }
	// resultSet.close();
	// consultarUltimaBiomasa.close();
	// conn.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return u;
	// }

	// public String ConsultarProyectos(Integer idusuario) {
	// String opcion = "";
	// ConsultarProyecto cp = new ConsultarProyecto();
	// cp.setUsuario(1);
	// cp.setIdUsuario(idusuario);
	// ArrayList<Proyecto> proyectos = new ArrayList<Proyecto>();
	// proyectos = cp.consultarProyecto();
	// for(Proyecto p:proyectos){
	// opcion += "<option value=\"" + p.getConsecutivo() + "\">"
	// + p.getNombre() + "</option>\n";
	// }
	// return opcion;
	// }

	// public Integer ConsultarPropietarioProyecto(Integer idProyecto) {
	// Integer opcion = -1;
	// try {
	// conn = conexionBD.establecerConexion();
	// CallableStatement ConsultarPropietarioProyecto = conn
	// .prepareCall("{call RED_PK_PARCELAS.ConsultarPropietarioProyecto(?,?)}");
	// ConsultarPropietarioProyecto.setInt("un_idproyecto", idProyecto);
	// ConsultarPropietarioProyecto.registerOutParameter("un_Resultado",
	// OracleTypes.CURSOR);
	// ConsultarPropietarioProyecto.execute();
	// OracleResultSet resultSet = (OracleResultSet)
	// ConsultarPropietarioProyecto
	// .getObject("un_Resultado");
	// while (resultSet.next())
	// opcion = Integer.valueOf(resultSet.getObject(1).toString());
	// resultSet.close();
	// ConsultarPropietarioProyecto.close();
	// conn.close();
	// } catch (Exception e) {
	// try {
	// conn.rollback();
	// } catch (SQLException e1) {
	// e1.printStackTrace();
	// }
	// e.printStackTrace();
	// }
	// return opcion;
	// }
	//
	// public ArrayList<Individuo> ConsultarIndividuoParcela(String idParcela) {
	// try {
	// conn = conexionBD.establecerConexion();
	// individuos = new ArrayList<Individuo>();
	// CallableStatement consultarIndividuoParcela = conn
	// .prepareCall("{call RED_PK_PARCELAS.consultarIndividuoParcela(?,?)}");
	// consultarIndividuoParcela.setString("una_parcela", idParcela);
	// consultarIndividuoParcela.registerOutParameter("un_Resultado",
	// OracleTypes.CURSOR);
	// consultarIndividuoParcela.execute();
	// OracleResultSet resultSet = (OracleResultSet) consultarIndividuoParcela
	// .getObject("un_Resultado");
	// while (resultSet.next()) {
	// if (resultSet.getObject(10) != null) {
	// Individuo i = new Individuo();
	// i.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
	// .toString()));
	// i.setDap1(Double
	// .valueOf(resultSet.getObject(10).toString()));
	// i.setEspecie(resultSet.getObject(17) != null ? resultSet
	// .getObject(17).toString() : "NO DATA");
	// individuos.add(i);
	// }
	// }
	// resultSet.close();
	// consultarIndividuoParcela.close();
	// conn.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return individuos;
	// }
	//
	// public TmpFamilia ConsultarFamilia(String especie) {
	// try {
	// conn = conexionBD.establecerConexion();
	// familia = new TmpFamilia();
	// CallableStatement consultarFamilia = conn
	// .prepareCall("{call RED_PK_PARCELAS.consultarFamilia(?,?)}");
	// consultarFamilia.setString("una_especie", especie);
	// consultarFamilia.registerOutParameter("un_Resultado",
	// OracleTypes.CURSOR);
	// consultarFamilia.execute();
	// OracleResultSet resultSet = (OracleResultSet) consultarFamilia
	// .getObject("un_Resultado");
	// while (resultSet.next()) {
	// familia.setId(Integer
	// .valueOf(resultSet.getObject(1).toString()));
	// familia.setBinomial(resultSet.getObject(2).toString());
	// familia.setFamilia(resultSet.getObject(3).toString());
	// familia.setGenero(resultSet.getObject(4).toString());
	// familia.setEspecie(resultSet.getObject(5).toString());
	// familia.setDensidadFamilia(Double.valueOf(resultSet
	// .getObject(6).toString()));
	// familia.setDensidadGenero(Double.valueOf(resultSet.getObject(7)
	// .toString()));
	// familia.setDensidadEspecie(Double.valueOf(resultSet
	// .getObject(8).toString()));
	// }
	// resultSet.close();
	// consultarFamilia.close();
	// conn.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return familia;
	// }

	public Double ConsultarAreaParcela(Integer idParcela) {
		Double area = new Double(0);
		try {
			// log.info("Inicio de la consulta del area de la parcela "+idParcela);
			conn = conexionBD.establecerConexion();
			CallableStatement consultarAreaParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.ConsultarAreaParcela(?,?)}");
			consultarAreaParcela.setInt("un_consecutivo", idParcela);
			consultarAreaParcela.registerOutParameter("un_Resultado",
					OracleTypes.NUMBER);
			consultarAreaParcela.execute();
			area = consultarAreaParcela.getDouble("un_Resultado");
			if (area == 0)
				area = 1.0;
			consultarAreaParcela.close();
			conn.close();
			// log.info("Consulta exitosa del area de la parcela "+idParcela);
		} catch (Exception e) {
			// log.error("Error en la consulta del area de la parcela "+idParcela);
			e.printStackTrace();
		}
		return area;
	}

	public SioFuenteGeneradora ConsultarSIOFGDA(String nombre) {
		SioFuenteGeneradora fgda = new SioFuenteGeneradora();
		try {
			// log.info("Inicio de la consulta por nombre de la FGDA de SIO_FUENTE_GENERADORA");
			conn = conexionBD.establecerConexion();
			CallableStatement consultarSioFgda = conn
					.prepareCall("{call RED_PK_PARCELAS.Consultar_SIOFUENTE(?,?)}");
			consultarSioFgda.setString("un_nombre", nombre);
			consultarSioFgda.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarSioFgda.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarSioFgda
					.getObject("un_Resultado");
			while (resultSet.next()) {
				fgda.setFechaModificacion(resultSet.getDate(1));
				fgda.setFechaAplicacion(resultSet.getDate(2));
				fgda.setSector(resultSet.getString(3));
				fgda.setFechaCreacion(resultSet.getDate(4));
				fgda.setEstado(resultSet.getString(5));
				fgda.setNombre(resultSet.getString(6));
				fgda.setFechaRegistro(resultSet.getDate(7));
				fgda.setFgdaId(resultSet.getInt(8));
				fgda.setTipo(resultSet.getString(9));
			}
			resultSet.close();
			consultarSioFgda.close();
			conn.close();
			// log.info("Consulta exitosa por nombre de la FGDA de SIO_FUENTE_GENERADORA");
		} catch (Exception e) {
			// log.error("Error en la consulta por nombre de la FGDA de SIO_FUENTE_GENERADORA");
			e.printStackTrace();
		}
		return fgda;
	}

	public SioPuntosMonitoreo ConsultarSioPuntos(String nombre, Integer idFgda) {
		SioPuntosMonitoreo puntos = new SioPuntosMonitoreo();
		try {
			// log.info("Inicio de la consulta por nombre y id FGDA del  punto de monitoreo de SIO_PUNTOS_MONITOREO");
			conn = conexionBD.establecerConexion();
			CallableStatement consultarSioPuntos = conn
					.prepareCall("{call RED_PK_PARCELAS.Consultar_SIOPUNTO(?,?,?)}");
			consultarSioPuntos.setString("un_nombre", nombre);
			consultarSioPuntos.setInt("un_fgda_id", idFgda);
			consultarSioPuntos.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarSioPuntos.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarSioPuntos
					.getObject("un_Resultado");
			while (resultSet.next()) {
				puntos.setEstado(resultSet.getString(1));
				puntos.setPuntoMonitoreoId(resultSet.getInt(2));
				puntos.setFechaCreacion(resultSet.getDate(3));
				puntos.setTipo(resultSet.getString(4));
				puntos.setNombre(resultSet.getString(5));
				puntos.setFechaModificacion(resultSet.getDate(6));
				puntos.setDescripcion(resultSet.getString(7));
				puntos.setFgdaId(resultSet.getInt(8));
				puntos.setFechaAplicacion(resultSet.getDate(9));
				puntos.setLatitud(resultSet.getDouble(10));
				puntos.setLongitud(resultSet.getDouble(11));
				puntos.setGradosLatitud(resultSet.getDouble(12));
				puntos.setMinutosLatitud(resultSet.getDouble(13));
				puntos.setSegundosLatitud(resultSet.getDouble(14));
				puntos.setDireccionLatitud(resultSet.getString(15));
				puntos.setGradosLongitud(resultSet.getDouble(16));
				puntos.setMinutosLongitud(resultSet.getDouble(17));
				puntos.setSegundosLongitud(resultSet.getDouble(18));
				puntos.setDireccionLongitud(resultSet.getString(19));
				puntos.setAltitud(resultSet.getDouble(20));
			}
			resultSet.close();
			consultarSioPuntos.close();
			conn.close();
			// log.info("Consulta exitosa por nombre y id FGDA del punto de monitoreo de SIO_PUNTOS_MONITOREO");
		} catch (Exception e) {
			// log.error("Error en la consulta por nombre y id FGDA del punto de monitoreo de SIO_PUNTOS_MONITOREO");
			e.printStackTrace();
		}
		return puntos;
	}

	public SioEstaciones ConsultarSioEstaciones(String nombre, Integer idPunto) {
		SioEstaciones estaciones = new SioEstaciones();
		try {
			// log.info("Inicio de la consulta por nombre e id punto de monitoreo de la estacion de SIO_ESTACIONES");
			conn = conexionBD.establecerConexion();
			CallableStatement consultarSioEstaciones = conn
					.prepareCall("{call RED_PK_PARCELAS.Consultar_SIOESTACIONES(?,?,?)}");
			consultarSioEstaciones.setString("un_nombre", nombre);
			consultarSioEstaciones.setInt("un_punto_monitoreo_id", idPunto);
			consultarSioEstaciones.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarSioEstaciones.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarSioEstaciones
					.getObject("un_Resultado");
			while (resultSet.next()) {
				estaciones.setTipo(resultSet.getString(1));
				estaciones.setFechaCreacion(resultSet.getDate(2));
				estaciones.setFechaModificacion(resultSet.getDate(3));
				estaciones.setAlturaReferencia(resultSet.getDouble(4));
				estaciones.setIdEstacion(resultSet.getInt(5));
				estaciones.setClase(resultSet.getString(6));
				estaciones.setCategoria(resultSet.getString(7));
				estaciones.setNombre(resultSet.getString(8));
				estaciones.setObjetoInstalacion(resultSet.getString(9));
				estaciones.setPuntoMonitoreoId(resultSet.getInt(10));
				estaciones.setEstado(resultSet.getString(11));
				estaciones.setFechaAplicacion(resultSet.getDate(12));
			}
			resultSet.close();
			consultarSioEstaciones.close();
			conn.close();
			// log.info("Consulta exitosa por nombre e id punto de monitoreo de la estacion de SIO_ESTACIONES");
		} catch (Exception e) {
			// log.error("Error en la consulta por nombre e id punto de monitoreo de la estacion de SIO_ESTACIONES");
			e.printStackTrace();
		}
		return estaciones;
	}

	public String ConsultaClaveSector(Integer idSector) {
		String clave = null;
		try {
			// log.info("Inicio de la consulta de la clave del sector por su id");
			conn = conexionBD.establecerConexion();
			CallableStatement consultarAreaParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.Consulta_Clave_Sector(?,?)}");
			consultarAreaParcela.setInt("un_Consecutivo", idSector);
			consultarAreaParcela.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			consultarAreaParcela.execute();
			clave = consultarAreaParcela.getString("un_Resultado");
			consultarAreaParcela.close();
			conn.close();
			// log.info("Consulta exitosa de la clave del sector por su id");
		} catch (Exception e) {
			// log.error("Error en la consulta de la clave del sector por su id");
			e.printStackTrace();
		}
		return clave;

	}

	public ArrayList<ContactoParcela> consultaContactosParcelaId(
			Integer idParcela) {
		ArrayList<ContactoParcela> contactosParcela = new ArrayList<ContactoParcela>();
		try {
			conn = conexionBD.establecerConexion();
			CallableStatement consultarContactosParcelaId = conn
					.prepareCall("{call RED_PK_PARCELAS.consultarContactosParcelaId(?,?)}");
			consultarContactosParcelaId.setInt("una_parcela", idParcela);
			consultarContactosParcelaId.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarContactosParcelaId.execute();
			OracleResultSet resultSet = (OracleResultSet) consultarContactosParcelaId
					.getObject("un_Resultado");

			while (resultSet.next()) {
				ContactoParcela contactoParcela = new ContactoParcela();
				contactoParcela.setIdParcela(resultSet.getInt(1));
				contactoParcela.setIdContacto(resultSet.getInt(2));
				contactoParcela.setIdClase(resultSet.getInt(3));
				contactoParcela.setIdOrganizacion(resultSet.getInt(4));
				contactosParcela.add(contactoParcela);
			}
			resultSet.close();
			consultarContactosParcelaId.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contactosParcela;
	}

	 public Parcela obtenerParcela(Integer idParcela) {
			Parcela parcela = new Parcela();
			try {
				conn = conexionBD.establecerConexion();
			    CallableStatement consultaParcela = conn
				    .prepareCall("{call RED_PK_PARCELAS.Consultar_ParcelaId(?,?)}");
			    consultaParcela.setInt("una_parcela", idParcela);
			    consultaParcela.registerOutParameter("un_Resultado",
				    OracleTypes.CURSOR);
			    consultaParcela.execute();
			    OracleResultSet r = (OracleResultSet) consultaParcela
				    .getObject("un_Resultado");
			    while (r.next()) {
				parcela.setConsecutivo(r.getInt(1));
				parcela.setNombre(r.getString(2));
				parcela.setPerteneceProyecto(r.getString(3));
				parcela.setFechaEstablecimiento(r.getTimestamp(4));
				parcela.setAprovechamiento(r.getString(5));
				parcela.setArea(r.getBigDecimal(6));
				parcela.setInventarioPublico(r.getString(7));
				parcela.setTemporalidad(r.getInt(8));
				parcela.setLargoParcela(r.getBigDecimal(9));
				parcela.setAnchoParcela(r.getBigDecimal(10));
				parcela.setRadioParcela(r.getBigDecimal(11));
				parcela.setObservaciones(r.getString(12));
				parcela.setRutaImagen(r.getString(13) == null ? "" : r
					.getString(13));
				parcela.setNombreImagen(r.getString(14) == null ? "" : r
					.getString(14));
				parcela.setCodigoCampo(r.getString(15) == null ? "" : r
					.getString(15));
				parcela.setDescripcion(r.getString(16));
				parcela.setPublica(r.getString(17));
				parcela.setInventario(r.getInt(18));
				parcela.setEstado(r.getInt(19));
				parcela.setMetadato(r.getInt(20));
				parcela.setProposito(r.getInt(21));
				parcela.setForma(r.getInt(22));
				parcela.setPais(r.getInt(23));
			    }
			    r.close();
			    consultaParcela.close();
			    conn.close();
			} catch (SQLException e) {
			    e.printStackTrace();
			}
			return parcela;
		    }

}
