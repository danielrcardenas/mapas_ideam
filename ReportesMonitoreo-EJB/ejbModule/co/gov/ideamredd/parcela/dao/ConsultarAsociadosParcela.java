package co.gov.ideamredd.parcela.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import oracle.jdbc.OracleTypes;
import oracle.sql.CLOB;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.BiomasaYCarbono;
import co.gov.ideamredd.entities.CAR;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.Individuo;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.Proposito;
import co.gov.ideamredd.entities.Proyecto;
import co.gov.ideamredd.entities.SioEstaciones;
import co.gov.ideamredd.entities.SioFuenteGeneradora;
import co.gov.ideamredd.entities.SioPuntosMonitoreo;
import co.gov.ideamredd.entities.Temporalidad;
import co.gov.ideamredd.entities.TipoBosque;
import co.gov.ideamredd.entities.TipoInventario;
import co.gov.ideamredd.entities.TmpFamilia;
import co.gov.ideamredd.entities.Usuario;
import co.gov.ideamredd.proyecto.dao.ConsultarProyecto;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.util.Util;

@Stateless
public class ConsultarAsociadosParcela {

	@EJB
	ConexionBD conexion;

	@EJB
	ConsultarProyecto cp;

	private Connection conn;
	private ArrayList<TipoBosque> bosque;
	private ArrayList<Depto> depto;
	private ArrayList<Municipios> municipio;
	private ArrayList<CAR> car;
	private ArrayList<String> fisiografia;
	private ArrayList<Contacto> contactos;
	private Proyecto proyecto;
	private Contacto contacto;
	private Proposito proposito;
	private TipoInventario inventario;
	private ArrayList<Individuo> individuos;
	private TmpFamilia familia;
	private Logger log;
	private CallableStatement consultarBosqueParcela;
	private ResultSet resultSet;
	private CallableStatement consultarDeptoParcela;
	private CallableStatement consultarMunicipioParcela;
	private CallableStatement consultarCarParcela;
	private CallableStatement consultarPaisParcela;
	private CallableStatement consultarFisiografiaParcela;
	private CallableStatement consultarParcelaProyecto;
	private CallableStatement consultarContactoParcela;
	private CallableStatement consultarContactosParcela;
	private CallableStatement consultarPropositoParcela;
	private CallableStatement consultarTipoInventarioParcela;
	private CallableStatement consultarGeoProyecto;
	private CallableStatement consultarUltimaBiomasa;
	private CallableStatement ConsultarPropietarioProyecto;
	private CallableStatement consultarIndividuoParcela;
	private CallableStatement consultarFamilia;
	private CallableStatement consultarAreaParcela;
	private CallableStatement consultarSioFgda;
	private CallableStatement consultarSioPuntos;
	private CallableStatement consultarSioEstaciones;

	public ArrayList<TipoBosque> consultaTipoBosqueParcela(Integer idParcela) {
		try {
			conn = conexion.establecerConexion();
			bosque = new ArrayList<TipoBosque>();

			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);

			consultarBosqueParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.BosqueParcela_Consulta(?,?)}");
			consultarBosqueParcela.setInt("una_parcela", idParcela);
			consultarBosqueParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarBosqueParcela.execute();

			resultSet = (ResultSet) consultarBosqueParcela
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

			log.info("[consultaTipoBosqueParcela] Termino");
			resultSet.close();
			consultarBosqueParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultaTipoBosqueParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return bosque;
	}

	public ArrayList<Depto> consultaDeptoParcela(Integer idParcela) {
		try {
			depto = new ArrayList<Depto>();
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarDeptoParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.DeptoParcela_Consulta(?,?)}");
			consultarDeptoParcela.setInt("una_parcela", idParcela);
			consultarDeptoParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarDeptoParcela.execute();
			resultSet = (ResultSet) consultarDeptoParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				Depto departamento = new Depto();
				departamento.setConsecutivo(Integer.valueOf(resultSet
						.getObject(2).toString()));
				departamento.setNombre((String) resultSet.getObject(3));
				depto.add(departamento);
			}

			log.info("[consultaDeptoParcela] Termino");
			resultSet.close();
			consultarDeptoParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultaDeptoParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return depto;
	}

	public ArrayList<Municipios> ConsultaMunicipioParcela(Integer idParcela) {
		try {
			municipio = new ArrayList<Municipios>();
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarMunicipioParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.MunicipioParcela_Consulta(?,?)}");
			consultarMunicipioParcela.setInt("una_parcela", idParcela);
			consultarMunicipioParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarMunicipioParcela.execute();
			resultSet = (ResultSet) consultarMunicipioParcela
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

			log.info("[ConsultaMunicipioParcela] Termino");
			resultSet.close();
			consultarMunicipioParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultaMunicipioParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return municipio;
	}

	public ArrayList<CAR> ConsultaCarParcela(Integer idParcela) {

		try {
			car = new ArrayList<CAR>();
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarCarParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.CarParcela_Consulta(?,?)}");
			consultarCarParcela.setInt("una_parcela", idParcela);
			consultarCarParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarCarParcela.execute();
			resultSet = (ResultSet) consultarCarParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				CAR c = new CAR();
				c.setConsecutivo(resultSet.getInt(1));
				c.setNombre(resultSet.getString(2));
				car.add(c);
			}

			log.info("[ConsultaCarParcela] Termino");
			resultSet.close();
			consultarCarParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultaCarParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return car;
	}

	public Pais ConsultarPaisParcela(Integer idParcela) {
		Pais pais = new Pais();
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarPaisParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.PaisParcela_Consulta(?,?)}");
			consultarPaisParcela.setInt("una_parcela", idParcela);
			consultarPaisParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPaisParcela.execute();
			resultSet = (ResultSet) consultarPaisParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				pais.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				pais.setNombre((String) resultSet.getObject(2));
			}
			log.info("[ConsultarPaisParcela] Termino");
			resultSet.close();
			consultarPaisParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarPaisParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return pais;
	}

	public Temporalidad ConsultarTemporalidadParcela(Integer idTemporalidad) {
		Temporalidad temporalidad = new Temporalidad();
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarPaisParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.TemporalidadParcela_Consulta(?,?)}");
			consultarPaisParcela.setInt("una_temporalidad", idTemporalidad);
			consultarPaisParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPaisParcela.execute();
			resultSet = (ResultSet) consultarPaisParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				temporalidad.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				temporalidad.setNombre((String) resultSet.getObject(2));
			}

			log.info("[ConsultarTemporalidadParcela] Termino");
			resultSet.close();
			consultarPaisParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarTemporalidadParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return temporalidad;
	}

	public ArrayList<String> ConsultaFisiografiaParcela(Integer idParcela) {
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			fisiografia = new ArrayList<String>();

			consultarFisiografiaParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.Consulta_Fisiografia_Parcela(?,?)}");
			consultarFisiografiaParcela.setInt("una_parcela", idParcela);
			consultarFisiografiaParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarFisiografiaParcela.execute();
			resultSet = (ResultSet) consultarFisiografiaParcela
					.getObject("un_Resultado");

			while (resultSet.next()) {
				fisiografia.add(resultSet.getObject(1).toString());
			}

			log.info("[ConsultaFisiografiaParcela] Termino");
			resultSet.close();
			consultarFisiografiaParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultaFisiografiaParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return fisiografia;
	}

	public Proyecto ConsultaProyectoParcela(Integer idParcela) {
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			proyecto = new Proyecto();
			consultarParcelaProyecto = conn
					.prepareCall("{call RED_PK_PARCELAS.ProyectoParcela_Consulta(?,?)}");
			consultarParcelaProyecto.setInt("una_parcela", idParcela);
			consultarParcelaProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarParcelaProyecto.execute();
			resultSet = (ResultSet) consultarParcelaProyecto
					.getObject("un_Resultado");

			while (resultSet.next()) {
				proyecto.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				proyecto.setNombre((String) resultSet.getObject(2));
				proyecto.setDescripcionArea((String) resultSet.getObject(3));
				proyecto.setTipoBosques((String) resultSet.getObject(4));
				proyecto.setFechaInicio((Timestamp) resultSet.getObject(5));
				proyecto.setFechaFin((Timestamp) resultSet.getObject(6));
				proyecto.setPais(Integer.valueOf(resultSet.getObject(7)
						.toString()));
				proyecto.setEstado(Integer.valueOf(resultSet.getObject(8)
						.toString()));
				proyecto.setArea((BigDecimal) resultSet.getObject(9));
				proyecto.setCo2Reducir((BigDecimal) resultSet.getObject(10));
				proyecto.setTasaDeforestar((BigDecimal) resultSet.getObject(11));
				proyecto.setDuracionProyecto(Integer.valueOf(resultSet
						.getObject(12).toString()));
				proyecto.setPropietario(Integer.valueOf(resultSet.getObject(13)
						.toString()));
				proyecto.setPublico(Integer.valueOf(resultSet.getObject(14)
						.toString()));
			}

			log.info("[ConsultaProyectoParcela] Termino");
			resultSet.close();
			consultarParcelaProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultaProyectoParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return proyecto;
	}

	public Contacto ConsultarContactoParcela(Integer idContacto,
			Integer idUsuario) {
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			contacto = new Contacto();
			consultarContactoParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.ConsultarContactoParcela(?,?,?,?)}");
			consultarContactoParcela.setInt("un_contacto", idContacto);
			consultarContactoParcela.setInt("un_usuario", idUsuario);
			consultarContactoParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarContactoParcela.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultarContactoParcela.execute();
			resultSet = (ResultSet) consultarContactoParcela
					.getObject("un_Resultado");

			System.out.println(consultarContactoParcela.getObject("sentencia"));

			while (resultSet.next()) {
				contacto.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				contacto.setNombre(resultSet.getObject(2).toString());
				contacto.setPais(Integer.valueOf(resultSet.getObject(3)
						.toString()));
				contacto.setDireccion(resultSet.getObject(4) == null ? ""
						: resultSet.getObject(4).toString());
				contacto.setTelefono(resultSet.getObject(5) == null ? ""
						: resultSet.getObject(5).toString());
				contacto.setMovil(resultSet.getObject(6) == null ? ""
						: resultSet.getObject(6).toString());
				contacto.setCorreo(resultSet.getObject(7) == null ? ""
						: resultSet.getObject(7).toString());
				contacto.setTipo(resultSet.getObject(10) == null ? ""
						: resultSet.getObject(10).toString());
				contacto.setOrganizacion(resultSet.getObject(8) == null ? ""
						: resultSet.getObject(8).toString());
				contacto.setCargo(resultSet.getObject(9) == null ? ""
						: resultSet.getObject(9).toString());
				contacto.setSector(resultSet.getObject(11) == null ? ""
						: resultSet.getObject(11).toString());
			}

			log.info("[ConsultarContactoParcela] Termino");
			resultSet.close();
			consultarContactoParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarContactoParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return contacto;
	}

	public ArrayList<Contacto> ConsultarContactoParcela(String nombre,
			String email) {

		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			contactos = new ArrayList<Contacto>();
			consultarContactosParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.ConsultarContactoParcela(?,?,?,?)}");
			consultarContactosParcela.setString("un_nombre", nombre);
			consultarContactosParcela.setString("un_email", email);
			consultarContactosParcela.registerOutParameter("una_consulta",
					OracleTypes.VARCHAR);
			consultarContactosParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarContactosParcela.execute();
			resultSet = (ResultSet) consultarContactosParcela
					.getObject("un_Resultado");

			System.out.println(consultarContactosParcela
					.getObject("una_consulta"));

			while (resultSet.next()) {
				contacto = new Contacto();
				contacto.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				contacto.setNombre(resultSet.getObject(2).toString());
				contacto.setPais(Integer.valueOf(resultSet.getObject(3)
						.toString()));
				contacto.setDireccion(resultSet.getObject(4) == null ? ""
						: resultSet.getObject(4).toString());
				contacto.setTelefono(resultSet.getObject(5) == null ? ""
						: resultSet.getObject(5).toString());
				contacto.setMovil(resultSet.getObject(6) == null ? ""
						: resultSet.getObject(6).toString());
				contacto.setCorreo(resultSet.getObject(7) == null ? ""
						: resultSet.getObject(7).toString());
				contacto.setTipo(resultSet.getObject(8) == null ? ""
						: resultSet.getObject(8).toString());
				contacto.setOrganizacion(resultSet.getObject(9) == null ? ""
						: resultSet.getObject(9).toString());
				contacto.setCargo(resultSet.getObject(10) == null ? ""
						: resultSet.getObject(10).toString());
				contacto.setSector(resultSet.getObject(11) == null ? ""
						: resultSet.getObject(11).toString());
				contactos.add(contacto);
			}

			log.info("[ConsultarContactoParcela] Termino");
			resultSet.close();
			consultarContactosParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarContactoParcela] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return contactos;
	}

	public Proposito ConsultarPropositoParcela(Integer idProposito) {
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			proposito = new Proposito();
			consultarPropositoParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.PropositoParcela_Consulta(?,?)}");
			consultarPropositoParcela.setInt("un_proposito", idProposito);
			consultarPropositoParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPropositoParcela.execute();
			resultSet = (ResultSet) consultarPropositoParcela
					.getObject("un_Resultado");
			while (resultSet.next()) {
				proposito.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				proposito.setNombre((String) resultSet.getObject(2));
			}

			log.info("[ConsultarPropositoParcela] Termino");
			resultSet.close();
			consultarPropositoParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarPropositoParcela] Fallo");
			e.printStackTrace();
		}
		return proposito;
	}

	public TipoInventario ConsultarTipoInventarioParcela(Integer idTipo) {
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			inventario = new TipoInventario();
			consultarTipoInventarioParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.TipoInventarioParcela_Consulta(?,?)}");
			consultarTipoInventarioParcela.setInt("un_tipo", idTipo);
			consultarTipoInventarioParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarTipoInventarioParcela.execute();
			resultSet = (ResultSet) consultarTipoInventarioParcela
					.getObject("un_Resultado");
			while (resultSet.next()) {
				inventario.setConsecutivo(Integer.valueOf(resultSet
						.getObject(1).toString()));
				inventario.setNombre((String) resultSet.getObject(2));
			}

			log.info("[ConsultarTipoInventarioParcela] Termino");
			resultSet.close();
			consultarTipoInventarioParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarTipoInventarioParcela] Fallo");
			e.printStackTrace();
		}
		return inventario;
	}

	public String[] ConsultarGeoParcela(Integer idParcela) {
		Clob geometria = null;
		String[] coordenadas = null;
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarGeoProyecto = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta(?,?)}");
			consultarGeoProyecto.setInt("un_Consecutivo", idParcela);
			consultarGeoProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CLOB);
			consultarGeoProyecto.execute();
			geometria = (CLOB) consultarGeoProyecto.getObject("un_Resultado");
			consultarGeoProyecto.close();
			coordenadas = Util.obtenerDatosGeometria(Util
					.clobStringConversion(geometria));

			log.info("[ConsultarGeoParcela] Termino");
			resultSet.close();
			consultarGeoProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarGeoParcela] Fallo");
			e.printStackTrace();
		}
		return coordenadas;
	}

	public BiomasaYCarbono ConsultarUltimaBiomasaParcela(Integer idParcela) {
		BiomasaYCarbono bc = null;
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarUltimaBiomasa = conn
					.prepareCall("{call RED_PK_PARCELAS.consultar_UltimaBiomasa(?,?)}");
			consultarUltimaBiomasa.setInt("una_parcela", idParcela);
			consultarUltimaBiomasa.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarUltimaBiomasa.execute();
			resultSet = (ResultSet) consultarUltimaBiomasa
					.getObject("un_Resultado");
			while (resultSet.next()) {
				bc = new BiomasaYCarbono();
				bc.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
						.toString()));
				bc.setBiomasa(Double.valueOf(resultSet.getObject(2).toString()));
				bc.setCarbono(Double.valueOf(resultSet.getObject(3).toString()));
				bc.setFechaInicio((Timestamp) resultSet.getObject(4));
				bc.setMetodologia(Integer.valueOf(resultSet.getObject(7)
						.toString()));
			}
			log.info("[ConsultarUltimaBiomasaParcela] Termino");
			resultSet.close();
			consultarUltimaBiomasa.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarUltimaBiomasaParcela] Fallo");
			e.printStackTrace();
		}
		return bc;
	}

	public Usuario ConsultarContactoUsuario(Integer idUsuario) {
		Usuario u = null;
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarUltimaBiomasa = conn
					.prepareCall("{call RED_PK_PARCELAS.consultar_contactoUsuario(?,?)}");
			consultarUltimaBiomasa.setInt("un_consecutivo", idUsuario);
			consultarUltimaBiomasa.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarUltimaBiomasa.execute();
			resultSet = (ResultSet) consultarUltimaBiomasa
					.getObject("un_Resultado");
			while (resultSet.next()) {
				u = new Usuario();
				u.setIdUsuario(resultSet.getInt(1));
				u.setNombre((String) resultSet.getObject(2));
				u.setIdentificacion(resultSet.getString(3));
				u.setDireccion(resultSet.getString(4) != null ? resultSet
						.getString(4) : "");
				u.setTelefonoOficina(resultSet.getString(5) != null ? resultSet
						.getString(5) : "");
				u.setCelular(resultSet.getString(6) != null ? resultSet
						.getString(6) : "");
				u.setCorreoElectronico((String) resultSet.getObject(7));
				u.setPais(resultSet.getInt(12));
				u.setApellidoUno((String) resultSet.getObject(14));
				u.setApellidoDos((String) resultSet.getObject(15));
				u.setOrganizacion(resultSet.getString(18) != null ? resultSet
						.getString(18) : "");
				u.setCargo(resultSet.getString(19) != null ? resultSet
						.getString(19) : "");

			}
			log.info("[ConsultarContactoUsuario] Termino");
			resultSet.close();
			consultarUltimaBiomasa.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarContactoUsuario] Fallo");
			e.printStackTrace();
		}
		return u;
	}

	// public static String ConsultarProyectos() {
	// String opcion = "";
	// try {
	// CallableStatement consultarPaisParcela = conn
	// .prepareCall("{call RED_PK_PROYECTOS.consultarProyectosParcelas(?)}");
	// consultarPaisParcela.registerOutParameter("un_Resultado",
	// OracleTypes.CURSOR);
	// consultarPaisParcela.execute();
	// OracleResultSet resultSet = (OracleResultSet) consultarPaisParcela
	// .getObject("un_Resultado");
	// while (resultSet.next()) {
	// opcion += "<option value=\"" + resultSet.getObject(1) + "\">"
	// + resultSet.getObject(2) + "</option>\n";
	// }
	// resultSet.close();
	// consultarPaisParcela.close();
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

	public String ConsultarProyectos(Integer idusuario) {
		String opcion = "";
		cp.setUsuario(1);
		cp.setIdUsuario(idusuario);
		ArrayList<Proyecto> proyectos = new ArrayList<Proyecto>();
		proyectos = cp.consultarProyecto();
		for (Proyecto p : proyectos) {
			opcion += "<option value=\"" + p.getConsecutivo() + "\">"
					+ p.getNombre() + "</option>\n";
		}
		return opcion;
	}

	public Integer ConsultarPropietarioProyecto(Integer idProyecto) {
		Integer opcion = -1;
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			ConsultarPropietarioProyecto = conn
					.prepareCall("{call RED_PK_PARCELAS.ConsultarPropietarioProyecto(?,?)}");
			ConsultarPropietarioProyecto.setInt("un_idproyecto", idProyecto);
			ConsultarPropietarioProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			ConsultarPropietarioProyecto.execute();
			resultSet = (ResultSet) ConsultarPropietarioProyecto
					.getObject("un_Resultado");
			while (resultSet.next())
				opcion = Integer.valueOf(resultSet.getObject(1).toString());

			log.info("[ConsultarPropietarioProyecto] Termino");
			resultSet.close();
			ConsultarPropietarioProyecto.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarPropietarioProyecto] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return opcion;
	}

	public ArrayList<Individuo> ConsultarIndividuoParcela(String idParcela) {
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			individuos = new ArrayList<Individuo>();
			consultarIndividuoParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.consultarIndividuoParcela(?,?)}");
			consultarIndividuoParcela.setString("una_parcela", idParcela);
			consultarIndividuoParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarIndividuoParcela.execute();
			resultSet = (ResultSet) consultarIndividuoParcela
					.getObject("un_Resultado");
			while (resultSet.next()) {
				if (resultSet.getObject(10) != null) {
					Individuo i = new Individuo();
					i.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
							.toString()));
					i.setDap1(Double
							.valueOf(resultSet.getObject(10).toString()));
					i.setEspecie(resultSet.getObject(17) != null ? resultSet
							.getObject(17).toString() : "NO DATA");
					individuos.add(i);
				}
			}

			log.info("[ConsultarIndividuoParcela] Termino");
			resultSet.close();
			consultarIndividuoParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarIndividuoParcela] Fallo");
			e.printStackTrace();
		}
		return individuos;
	}

	public TmpFamilia ConsultarFamilia(String especie) {
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			familia = new TmpFamilia();
			consultarFamilia = conn
					.prepareCall("{call RED_PK_PARCELAS.consultarFamilia(?,?)}");
			consultarFamilia.setString("una_especie", especie);
			consultarFamilia.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarFamilia.execute();
			resultSet = (ResultSet) consultarFamilia.getObject("un_Resultado");
			while (resultSet.next()) {
				familia.setId(Integer
						.valueOf(resultSet.getObject(1).toString()));
				familia.setBinomial(resultSet.getObject(2).toString());
				familia.setFamilia(resultSet.getObject(3).toString());
				familia.setGenero(resultSet.getObject(4).toString());
				familia.setEspecie(resultSet.getObject(5).toString());
				familia.setDensidadFamilia(Double.valueOf(resultSet
						.getObject(6).toString()));
				familia.setDensidadGenero(Double.valueOf(resultSet.getObject(7)
						.toString()));
				familia.setDensidadEspecie(Double.valueOf(resultSet
						.getObject(8).toString()));
			}

			log.info("[ConsultarFamilia] Termino");
			resultSet.close();
			consultarFamilia.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarFamilia] Fallo");
			e.printStackTrace();
		}
		return familia;
	}

	public Double ConsultarAreaParcela(Integer idParcela) {
		Double area = new Double(0);
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			// CallableStatement consultarAreaParcela = conn
			// .prepareCall("{call RED_PK_PARCELAS.Consultar_ParcelaGeometria(?,?,?,?)}");
			consultarAreaParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.ConsultarAreaParcela(?,?)}");
			consultarAreaParcela.setInt("un_consecutivo", idParcela);
			consultarAreaParcela.registerOutParameter("un_Resultado",
					OracleTypes.NUMBER);
			// consultarAreaParcela.registerOutParameter("un_Area",
			// OracleTypes.NUMBER);
			// consultarAreaParcela.registerOutParameter("un_XCentro",
			// OracleTypes.NUMBER);
			// consultarAreaParcela.registerOutParameter("un_YCentro",
			// OracleTypes.NUMBER);
			consultarAreaParcela.execute();
			area = consultarAreaParcela.getDouble("un_Resultado");
			if (area == 0)
				area = 1.0;

			log.info("[ConsultarAreaParcela] Termino");
			consultarAreaParcela.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarAreaParcela] Fallo");
			e.printStackTrace();
		}
		return area;
	}

	public SioFuenteGeneradora ConsultarSIOFGDA(String nombre) {
		SioFuenteGeneradora fgda = new SioFuenteGeneradora();
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarSioFgda = conn
					.prepareCall("{call RED_PK_PARCELAS.Consultar_SIOFUENTE(?,?)}");
			consultarSioFgda.setString("un_nombre", nombre);
			consultarSioFgda.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarSioFgda.execute();
			resultSet = (ResultSet) consultarSioFgda.getObject("un_Resultado");
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

			log.info("[ConsultarSIOFGDA] Termino");
			resultSet.close();
			consultarSioFgda.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarSIOFGDA] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return fgda;
	}

	public SioPuntosMonitoreo ConsultarSioPuntos(String nombre, Integer idFgda) {
		SioPuntosMonitoreo puntos = new SioPuntosMonitoreo();
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarSioPuntos = conn
					.prepareCall("{call RED_PK_PARCELAS.Consultar_SIOPUNTO(?,?,?)}");
			consultarSioPuntos.setString("un_nombre", nombre);
			consultarSioPuntos.setInt("un_fgda_id", idFgda);
			consultarSioPuntos.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarSioPuntos.execute();
			resultSet = (ResultSet) consultarSioPuntos
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

			log.info("[ConsultarSioPuntos] Termino");
			resultSet.close();
			consultarSioPuntos.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarSioPuntos] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return puntos;
	}

	public SioEstaciones ConsultarSioEstaciones(String nombre, Integer idPunto) {
		SioEstaciones estaciones = new SioEstaciones();
		try {
			log = SMBC_Log.Log(ConsultarAsociadosParcela.class);
			consultarSioEstaciones = conn
					.prepareCall("{call RED_PK_PARCELAS.Consultar_SIOESTACIONES(?,?,?)}");
			consultarSioEstaciones.setString("un_nombre", nombre);
			consultarSioEstaciones.setInt("un_punto_monitoreo_id", idPunto);
			consultarSioEstaciones.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarSioEstaciones.execute();
			resultSet = (ResultSet) consultarSioEstaciones
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

			log.info("[ConsultarSioEstaciones] Termino");
			resultSet.close();
			consultarSioEstaciones.close();
			conn.close();
		} catch (Exception e) {
			log.error("[ConsultarSioEstaciones] Fallo");
			e.printStackTrace();
			/*
			 * try { conn.rollback(); } catch (SQLException e1) {
			 * e1.printStackTrace(); } e.printStackTrace();
			 */
		}
		return estaciones;
	}

	public static void main(String[] args) {
		// ArrayList<TipoBosque> a = ConsultarAsociadosParcela
		// .consultaTipoBosqueParcela(52);
		// ArrayList<Depto> b = ConsultarAsociadosParcela
		// .consultaDeptoParcela(52);
		// ArrayList<Municipios> c = ConsultarAsociadosParcela
		// .ConsultaMunicipioParcela(52);
		// ArrayList<String> d = ConsultarAsociadosParcela
		// .ConsultaFisiografiaParcela(52);
		// Pais p = ConsultarAsociadosParcela.ConsultarPaisParcela(52);
		// Proyecto pr = ConsultarAsociadosParcela.ConsultaProyectoParcela(52);
		// ConsultarAsociadosParcela c = new ConsultarAsociadosParcela();
		// c.obtenerMaxMinCoordenadas("-71.08597,11.83291,-75.28459,12.59028,-81.72015,10.38509,-66.98700,-4.23687,-74.08795,8.79590,-68.09845,0.59260,-66.87045,-1.28695");

	}

}
