package co.gov.ideamredd.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.Organizacion;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.TipoBosque;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.util.Util;

@Stateless
public class InsertarAsociadosParcela {

	@Resource(mappedName = "java:/SMBC_DS")
	private DataSource dataSource;

	private ArrayList<Integer> un_Departamento = new ArrayList<Integer>();
	private ArrayList<Integer> un_Municipio = new ArrayList<Integer>();
	private ArrayList<TipoBosque> un_TipoBosque = new ArrayList<TipoBosque>();
	private ArrayList<Integer> una_Fisiografia = new ArrayList<Integer>();
	private ArrayList<Integer> una_corporacion = new ArrayList<Integer>();
	private Integer idPais;
	private Connection conn;
	private static Logger log;

	public void insertaAsociados(Integer idParcela) throws Exception {
		consultaBosqueParcela(idParcela);
		consultaDeptoParcela(idParcela);
		consultaMunicipioParcela(idParcela);
		consultaCarParcela(idParcela);
		consultaSMParcela(idParcela);
		consultaPaisParcela(idParcela);
		insertaDeptoParcela(idParcela);
		insertaMunicipioParcela(idParcela);
		insertaPaisParcela(idParcela, idPais);
		insertaCarParcela(idParcela);
		insertaTipoBosqueParcela(idParcela);
		insertaFisiografiaParcela(idParcela);
	}

	public void insertarAreaParcela(Integer idParcela) throws Exception {
		conn = dataSource.getConnection();
		CallableStatement insertarAreaParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.Insertar_AreaParcela(?,?,?)}");
		insertarAreaParcela.setInt("un_consecutivo", idParcela);
		insertarAreaParcela.setInt("un_area", obtenerArea(idParcela));
		insertarAreaParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		insertarAreaParcela.execute();
		System.out.println(insertarAreaParcela.getObject("un_Resultado"));
		insertarAreaParcela.close();
		conn.close();
	}

	private Integer obtenerArea(Integer idParcela) throws Exception {
		conn = dataSource.getConnection();
		Integer a;
		CallableStatement obtenerArea = conn
				.prepareCall("{call RED_PK_GEOMETRIA.parcela_consultaArea(?,?)}");
		obtenerArea.setInt("un_consecutivo", idParcela);
		obtenerArea.registerOutParameter("un_Resultado", OracleTypes.NUMBER);
		obtenerArea.execute();
		a = ((BigDecimal) obtenerArea.getObject("un_Resultado")).intValue();
		obtenerArea.close();
		conn.close();
		return a;
	}

	public ArrayList<TipoBosque> consultaBosqueParcela(Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del cruce geografico de tipos de bosque con la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			CallableStatement consultarBosqueParcela = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta_Bosque(?,?,?)}");
			consultarBosqueParcela.setInt("un_Consecutivo", idParcela);
			consultarBosqueParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarBosqueParcela.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarBosqueParcela.execute();
			OracleResultSet r = (OracleResultSet) consultarBosqueParcela
					.getObject("un_Resultado");
			System.out.println(consultarBosqueParcela.getObject("un_Mensaje"));
			while (r.next()) {
				TipoBosque bosque = new TipoBosque();
				bosque.setTipoBosque((String) r.getObject(4));
				bosque.setTemperatura((String) r.getObject(2));
				bosque.setPrecipitacion((String) r.getObject(3));
				bosque.setAltitud((String) r.getObject(1));
				bosque.setIdBosque(Integer.valueOf(r.getObject(5).toString()));
				un_TipoBosque.add(bosque);
			}
			r.close();
			consultarBosqueParcela.close();
			conn.close();
//			log.info("Cruce geografico exitoso de los tipos de bosque con la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el cruce geografico de los tipos de bosque con la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
		return un_TipoBosque;
	}

	public void consultaSMParcela(Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del cruce geografico de la fisiografia con la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			CallableStatement consultarSMProyecto = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta_SM(?,?,?)}");
			consultarSMProyecto.setInt("un_Consecutivo", idParcela);
			consultarSMProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarSMProyecto.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarSMProyecto.execute();
			OracleResultSet r = (OracleResultSet) consultarSMProyecto
					.getObject("un_Resultado");
			System.out.println(consultarSMProyecto.getObject("un_Mensaje"));
			while (r.next()) {
				una_Fisiografia
						.add(Integer.valueOf(r.getObject(12).toString()));
			}
			r.close();
			consultarSMProyecto.close();
			conn.close();
//			log.info("Cruce geografico exitoso de la fisiografia con la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el cruce geografico de la fisiografia con la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}
	
	public void consultaPaisParcela(Integer idParcela) {
		Pais pais;
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del cruce geografico del pais con la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			CallableStatement consultarPaisProyecto = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta_Pais(?,?,?)}");
			consultarPaisProyecto.setInt("un_Consecutivo", idParcela);
			consultarPaisProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarPaisProyecto.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarPaisProyecto.execute();
			OracleResultSet r = (OracleResultSet) consultarPaisProyecto
					.getObject("un_Resultado");
			System.out.println(consultarPaisProyecto.getObject("un_Mensaje"));
			while (r.next()) {
				pais = new Pais();
				pais.setConsecutivo(r.getInt(1));
				pais.setNombre(r.getString(2));
				idPais = pais.getConsecutivo();
			}
			r.close();
			consultarPaisProyecto.close();
			conn.close();
//			log.info("Cruce geografico exitoso del pais con la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el cruce geografico del pais con la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}

	public ArrayList<Integer> consultaDeptoParcela(Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del cruce geografico de departamento(s) con la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			CallableStatement consultarDeptoProyecto = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta_Depto(?,?,?)}");
			consultarDeptoProyecto.setInt("un_Consecutivo", idParcela);
			consultarDeptoProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarDeptoProyecto.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarDeptoProyecto.execute();
			OracleResultSet r = (OracleResultSet) consultarDeptoProyecto
					.getObject("un_Resultado");
			System.out.println(consultarDeptoProyecto.getObject("un_Mensaje"));
			while (r.next()) {
				un_Departamento.add(Integer.valueOf(r.getObject(1).toString()));
			}
			r.close();
			consultarDeptoProyecto.close();
			conn.close();
//			log.info("Cruce geografico exitoso de departamento(s) con la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el cruce geografico de departamento(s) con la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
		return un_Departamento;
	}

	public ArrayList<Integer> consultaMunicipioParcela(Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del cruce geografico de municipio(s) con la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			CallableStatement consultarMuniciopioParcela = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta_Municipio(?,?,?)}");
			consultarMuniciopioParcela.setInt("un_Consecutivo", idParcela);
			consultarMuniciopioParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarMuniciopioParcela.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarMuniciopioParcela.execute();
			OracleResultSet r = (OracleResultSet) consultarMuniciopioParcela
					.getObject("un_Resultado");
			System.out.println(consultarMuniciopioParcela
					.getObject("un_Mensaje"));
			while (r.next()) {
				un_Municipio.add(Integer.valueOf(r.getObject(1).toString()));
			}
			r.close();
			consultarMuniciopioParcela.close();
			conn.close();
//			log.info("Cruce geografico exitoso de municipio(s) con la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el cruce geografico de municipio(s) con la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
		return un_Municipio;
	}

	public ArrayList<Integer> consultaCarParcela(Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del cruce geografico de CAR con la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			CallableStatement consultarBosqueParcela = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta_Corporaciones(?,?,?)}");
			consultarBosqueParcela.setInt("un_Consecutivo", idParcela);
			consultarBosqueParcela.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarBosqueParcela.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarBosqueParcela.execute();
			OracleResultSet r = (OracleResultSet) consultarBosqueParcela
					.getObject("un_Resultado");
			System.out.println(consultarBosqueParcela.getObject("un_Mensaje"));
			while (r.next()) {
				una_corporacion.add(Integer.valueOf(r.getObject(1).toString()));
			}
			r.close();
			consultarBosqueParcela.close();
			conn.close();
//			log.info("Cruce geografico exitoso de CAR con la parcela id:" + idParcela);
		} catch (Exception e) {
//			log.error("Error en el cruce geografico de CAR con la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
		return una_corporacion;
	}

	private void insertaDeptoParcela(Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del registro de departamento(s) para la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			for (int i = 0; i < un_Departamento.size(); i++) {
				CallableStatement deptoParcela = conn
						.prepareCall("{call RED_PK_PARCELAS.Inserta_Depto_Parcela(?,?,?)}");
				deptoParcela.setInt("un_consecutivo", idParcela);
				deptoParcela.setInt("un_depto", un_Departamento.get(i));
				deptoParcela.registerOutParameter("un_Resultado",
						OracleTypes.VARCHAR);
				deptoParcela.execute();
				System.out.println(deptoParcela.getObject("un_Resultado"));
				deptoParcela.close();
			}
			conn.close();
//			log.info("Registro exitoso de departamento(s) para la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el registro de departamento(s) de la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}

	private void insertaMunicipioParcela(Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio de registro de municipio(s) de la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			for (int i = 0; i < un_Municipio.size(); i++) {
				CallableStatement municipioParcela = conn
						.prepareCall("{call RED_PK_PARCELAS.Inserta_Municipio_Parcela(?,?,?)}");
				municipioParcela.setInt("un_consecutivo", idParcela);
				municipioParcela.setInt("un_municipio", un_Municipio.get(i));
				municipioParcela.registerOutParameter("un_Resultado",
						OracleTypes.VARCHAR);
				municipioParcela.execute();
				System.out.println(municipioParcela.getObject("un_Resultado"));
				municipioParcela.close();
			}
			conn.close();
//			log.info("Registro exitoso de municipio(s) para la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el registro de municipio(s) de la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}

	private void insertaCarParcela(Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del registro de CAR para la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			for (int i = 0; i < una_corporacion.size(); i++) {
				CallableStatement carParcela = conn
						.prepareCall("{call RED_PK_PARCELAS.Inserta_Car_Parcela(?,?,?)}");
				carParcela.setInt("un_consecutivo", idParcela);
				carParcela.setInt("una_car", una_corporacion.get(i));
				carParcela.registerOutParameter("un_Resultado",
						OracleTypes.VARCHAR);
				carParcela.execute();
				System.out.println(carParcela.getObject("un_Resultado"));
				carParcela.close();
			}
			conn.close();
//			log.info("Registro exitoso de CAR para la parcela id:" + idParcela);
		} catch (Exception e) {
//			log.error("Error en el registro de CAR de la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}

	private void insertaTipoBosqueParcela(Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del registro de los tipos de bosque de la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			for (int i = 0; i < un_TipoBosque.size(); i++) {
				TipoBosque bosque = un_TipoBosque.get(i);
				CallableStatement tipoBosqueParcela = conn
						.prepareCall("{call RED_PK_PARCELAS.Inserta_TipoBosque_Parcela(?,?,?)}");
				tipoBosqueParcela.setInt("un_consecutivo", idParcela);
				tipoBosqueParcela.setString("un_TipoBosque", bosque
						.getIdBosque().toString());
				// tipoBosqueParcela.setString("una_Precipitaicon",
				// bosque.getPrecipitacion());
				// tipoBosqueParcela.setString("una_Temperatura",
				// bosque.getTemperatura());
				// tipoBosqueParcela.setString("una_Altitud",
				// bosque.getAltitud());
				// tipoBosqueParcela.setInt("un_IdBosque",
				// bosque.getIdBosque());
				tipoBosqueParcela.registerOutParameter("un_Resultado",
						OracleTypes.VARCHAR);
				tipoBosqueParcela.execute();
				System.out.println(tipoBosqueParcela.getObject("un_Resultado"));
				tipoBosqueParcela.close();
			}
			conn.close();
//			log.info("Registro exitoso de tipos de bosque para la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el registro de los tipos de bosque de la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}

	private void insertaFisiografiaParcela(Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del registro de la fisiografia de la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			for (int i = 0; i < una_Fisiografia.size(); i++) {
				CallableStatement fisiografiaParcela = conn
						.prepareCall("{call RED_PK_PARCELAS.Inserta_Fisiografia_Parcela(?,?,?)}");
				fisiografiaParcela.setInt("un_consecutivo", idParcela);
				fisiografiaParcela.setString("una_fisiografia", una_Fisiografia
						.get(i).toString());
				fisiografiaParcela.registerOutParameter("un_Resultado",
						OracleTypes.VARCHAR);
				fisiografiaParcela.execute();
				System.out
						.println(fisiografiaParcela.getObject("un_Resultado"));
				fisiografiaParcela.close();
			}
			conn.close();
//			log.info("Registro exitoso de la fisiografia para la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el registro de la fisiografia de la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}

	public void insertaProyectoParcela(Integer idParcela, Integer idProyecto) {
		try {
//			log = SMBC_Log.log(this.getClass());
			if (idParcela != -1) {
				log.info("Inicio del registro del proyecto al que pertenece la parcela id:"
						+ idParcela);
				conn = dataSource.getConnection();
				CallableStatement proyectoParcela = conn
						.prepareCall("{call RED_PK_PARCELAS.Inserta_Proyecto_Parcela(?,?,?)}");
				proyectoParcela.setInt("un_consecutivo", idParcela);
				proyectoParcela.setInt("un_proyecto", idProyecto);
				proyectoParcela.registerOutParameter("un_Resultado",
						OracleTypes.VARCHAR);
				proyectoParcela.execute();
				System.out.println(proyectoParcela.getObject("un_Resultado"));
				proyectoParcela.close();
				conn.close();

//				log.info("Registro exitoso del proyecto al que pertenece la parcela id:"
//						+ idParcela);
			}
		} catch (Exception e) {
//			log.error("Error en el registro del proyecto al que pertenece la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}

	}

	public Integer insertaContacto(Contacto contacto) {
		Integer idContacto = null;
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio el registro del contacto a la base de datos");
			conn = dataSource.getConnection();
			CallableStatement registrarContacto = conn
					.prepareCall("{call RED_PK_PARCELAS.Inserta_Contacto(?,?,?,?,?,?,?,?)}");
			registrarContacto.setString("un_nombre", contacto.getNombre()
					.toUpperCase());
			registrarContacto.setInt("un_pais", contacto.getPais());
			registrarContacto.setString("un_telefono", contacto.getTelefono());
			registrarContacto.setString("un_movil", contacto.getMovil());
			registrarContacto.setString("un_correo", contacto.getCorreo()
					.toLowerCase());
			registrarContacto.setInt("un_municipio", 11001);// TODO:Cambiar con
															// el
															// id del municipio
			registrarContacto.registerOutParameter("un_Consecutivo",
					OracleTypes.NUMBER);
			registrarContacto.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			registrarContacto.execute();
			System.out.println(registrarContacto.getObject("un_Resultado"));
			idContacto = registrarContacto.getInt("un_Consecutivo");
			registrarContacto.close();
			conn.close();
//			log.info("Registro exitoso del contacto a la base de datos");
		} catch (Exception e) {
//			log.error("Error en el registro del contacto a la base de datos");
			e.printStackTrace();
		}
		return idContacto;
	}

	public Integer insertaOrganizacion(Organizacion organizacion) {
		Integer idOrganizacion = null;
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio el registro de la organizacion a la base de datos");
			conn = dataSource.getConnection();
			CallableStatement registrarOrganizacion = conn
					.prepareCall("{call RED_PK_PARCELAS.Inserta_Organizacion(?,?,?,?,?,?,?,?,?,?)}");
			registrarOrganizacion.setString("un_nombre", organizacion
					.getNombre().toUpperCase());
			registrarOrganizacion.setString("una_direccion",
					organizacion.getDireccion());
			registrarOrganizacion.setInt("un_telefono",
					organizacion.getTelefono());
			registrarOrganizacion.setString("un_correo", organizacion
					.getCorreo().toLowerCase());
			registrarOrganizacion.setInt("un_sector", organizacion.getSector());
			registrarOrganizacion.setInt("un_pais", organizacion.getPais());
			registrarOrganizacion.setInt("un_depto", 11);// TODO:Cambiar con el
															// id
															// del depto
			registrarOrganizacion.setInt("un_municipio", 11001);// TODO:Cambiar
																// con
																// el id del
																// municipio
			registrarOrganizacion.registerOutParameter("un_Consecutivo",
					OracleTypes.NUMBER);
			registrarOrganizacion.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			registrarOrganizacion.execute();
			System.out.println(registrarOrganizacion.getObject("un_Resultado"));
			idOrganizacion = registrarOrganizacion.getInt("un_Consecutivo");
			registrarOrganizacion.close();
			conn.close();
//			log.info("Registro exitoso de la organizacion a la base de datos");
		} catch (Exception e) {
//			log.error("Error en el registro de la organizacion a la base de datos");
			e.printStackTrace();
		}
		return idOrganizacion;
	}

	public void insertaContactoParcela(Integer idParcela, Integer idContacto,
			Integer idOrganizacion, Integer claseContacto) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio el registro del contacto de la parcela id:"
//					+ idParcela + ", clase de contacto id:" + claseContacto);
			conn = dataSource.getConnection();
			CallableStatement contactoParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.Inserta_Contacto_Parcela(?,?,?,?,?)}");
			contactoParcela.setInt("una_parcela", idParcela);
			contactoParcela.setInt("un_contacto", idContacto);
			contactoParcela.setInt("una_organizacion", idOrganizacion);
			contactoParcela.setInt("una_claseContacto", claseContacto);
			contactoParcela.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			contactoParcela.execute();
			System.out.println(contactoParcela.getObject("un_Resultado"));
			contactoParcela.close();
			conn.close();
//			log.info("Registro exitoso del contacto de la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el registro del contacto de la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}

	public Integer modificarContactoParcela(Contacto contacto) throws Exception {
		conn = dataSource.getConnection();
		Integer exito = -1;
		CallableStatement contactoParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.Actualiza_Contacto_Parcela(?,?,?,?,?,?,?,?,?,?,?)}");
		contactoParcela.setString("un_nombre", contacto.getNombre()
				.toUpperCase());
		contactoParcela.setInt("un_consecutivo", contacto.getConsecutivo());
		contactoParcela.setInt("un_pais", contacto.getPais());
		// contactoParcela.setString("una_direccion", contacto.getDireccion());
		contactoParcela.setString("un_telefono", contacto.getTelefono());
		contactoParcela.setString("un_movil", contacto.getMovil());
		// contactoParcela.setString("un_tipo", contacto.getTipo());
		// contactoParcela.setString("una_organizacion", contacto
		// .getOrganizacion());
		// contactoParcela.setString("un_cargo", contacto.getCargo());
		// contactoParcela.setString("un_sector", contacto.getSector());
		contactoParcela.registerOutParameter("un_Consecutivo",
				OracleTypes.NUMBER);
		contactoParcela.registerOutParameter("un_Resultado",
				OracleTypes.VARCHAR);
		contactoParcela.execute();
		System.out.println(contactoParcela.getObject("un_Resultado"));
		if (contactoParcela.getObject("un_Resultado").toString()
				.contains("Insertado"))
			exito = 1;
		contactoParcela.close();
		conn.close();
		return exito;
	}

	public Integer insertaMetadato(String nombre) {
		Integer idMetadato = null;
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del registro del metadato " + nombre);
			conn = dataSource.getConnection();
			CallableStatement insertarMetadato = conn
					.prepareCall("{call RED_PK_PARCELAS.InsertarMetadato(?,?,?)}");
			insertarMetadato.setString("un_nombre", nombre);
			insertarMetadato.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			insertarMetadato.registerOutParameter("un_idMetadato",
					OracleTypes.NUMBER);
			insertarMetadato.execute();
			System.out.println(insertarMetadato.getObject("un_Resultado"));
			idMetadato = Integer.valueOf(insertarMetadato.getObject(
					"un_idMetadato").toString());
			insertarMetadato.close();
			conn.close();
//			log.info("Registro exitoso del metadato " + nombre);
		} catch (Exception e) {
//			log.error("Error en el registro del metadato " + nombre);
			e.printStackTrace();
		}
		return idMetadato;
	}

	public void insertaMetadatoParcela(Integer idParcela, Integer idMetadato) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del registro de metadato de la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			CallableStatement metadatoParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.InsertarMetadatoParcela(?,?,?)}");
			metadatoParcela.setInt("un_consecutivo", idParcela);
			metadatoParcela.setInt("un_metadato", idMetadato);
			metadatoParcela.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			metadatoParcela.execute();
			System.out.println(metadatoParcela.getObject("un_Resultado"));
			metadatoParcela.close();
			conn.close();
//			log.info("Registro exitoso del metadato de la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el registro del metadato de la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}
	
	public void insertaPaisParcela(Integer idParcela, Integer idPais) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del registro del pais de la parcela id:"
//					+ idParcela);
			conn = dataSource.getConnection();
			CallableStatement paisParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.IngresaPaisParcela(?,?,?)}");
			paisParcela.setInt("una_parcela", idParcela);
			paisParcela.setInt("un_pais", idPais);
			paisParcela.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			paisParcela.execute();
			System.out.println(paisParcela.getObject("un_Resultado"));
			paisParcela.close();
			conn.close();
//			log.info("Registro exitoso del pais de la parcela id:"
//					+ idParcela);
		} catch (Exception e) {
//			log.error("Error en el registro del pais de la parcela id:"
//					+ idParcela);
			e.printStackTrace();
		}
	}

	public void ingresarFuenteSiopera(String sector, String nombre) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del registro de la FGDA en SIO_FUENTE_GENERADORA");
			conn = dataSource.getConnection();
			CallableStatement FGDASiopera = conn
					.prepareCall("{call RED_PK_PARCELAS.InsertarSIO_FGDA(?,?,?,?,?,?,?,?,?,?)}");
			FGDASiopera.setDate("una_fecha_modificacion",
					new Date(System.currentTimeMillis()));
			FGDASiopera.setDate("una_fecha_aplicacion",
					new Date(System.currentTimeMillis()));
			FGDASiopera.setString("un_sector", sector);
			FGDASiopera.setDate("una_fecha_creacion",
					new Date(System.currentTimeMillis()));
			FGDASiopera.setString("un_estado", "ACT");
			FGDASiopera.setString("un_nombre", nombre);
			FGDASiopera.setDate("una_fecha_registro",
					new Date(System.currentTimeMillis()));
			FGDASiopera.setInt("un_fgda_id", consultarIdFGDA() + 1);
			FGDASiopera.setString("un_tipo", "RED");
			FGDASiopera.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			FGDASiopera.execute();
			System.out.println(FGDASiopera.getObject("un_Resultado"));
			FGDASiopera.close();
			conn.close();
//			log.info("Registro exitoso de la FGDA en SIO_FUENTE_GENERADORA");
		} catch (Exception e) {
//			log.error("Error en el registro de la FGDA en SIO_FUENTE_GENERADORA");
			e.printStackTrace();
		}
	}

	public void ingresarPuntoSiopera(String nombre, String descripcion,
			String X, String Y, Integer idParcela) {
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio del registro de la parcela a SIO_PUNTOS_MONITOREO");
			conn = dataSource.getConnection();
			CallableStatement FGDASiopera = conn
					.prepareCall("{call RED_PK_PARCELAS.InsertaSIO_PUNTOS_MONITOREOS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			FGDASiopera.setString("un_estado", "ACT");
			FGDASiopera.setInt("un_punto_monitoreo_id", consultarIdPunto() + 1);
			FGDASiopera.setDate("una_fecha_creacion",
					new Date(System.currentTimeMillis()));
			FGDASiopera.setString("un_tipo", "BSQ");
			FGDASiopera.setString("un_nombre", nombre);
			FGDASiopera.setDate("un_fecha_modificacion",
					new Date(System.currentTimeMillis()));
			FGDASiopera.setString("una_descripcion", descripcion);
			FGDASiopera.setInt("un_fgda_id", consultarIdFGDA());
			FGDASiopera.setDate("una_fecha_aplicacion",
					new Date(System.currentTimeMillis()));
			FGDASiopera.setDouble("una_latitud", Double.valueOf(X));
			FGDASiopera.setDouble("una_longitud", Double.valueOf(Y));
			String[] latitud = Util.convertirAGrados(X).split(",");
			String[] longitud = Util.convertirAGrados(Y).split(",");
			FGDASiopera.setInt("unos_grados_latitud",
					Integer.valueOf(latitud[0]));
			FGDASiopera.setInt("unos_minutos_latitud",
					Integer.valueOf(latitud[1]));
			FGDASiopera.setInt("unos_segundos_latitud",
					Integer.valueOf(latitud[2]));
			FGDASiopera.setString("una_direccion_latitud", "N");
			FGDASiopera.setInt("unos_grados_longitud",
					Integer.valueOf(longitud[0]));
			FGDASiopera.setInt("unos_minutos_longitud",
					Integer.valueOf(longitud[1]));
			FGDASiopera.setInt("unos_segundos_longitud",
					Integer.valueOf(longitud[2]));
			FGDASiopera.setString("una_direccion_longitud", "E");
			ArrayList<TipoBosque> t = consultaBosqueParcela(idParcela);
			TipoBosque tb = t.get(0);
			String[] tipo = tb.getAltitud().split("-");
			FGDASiopera.setDouble("una_altitud", Double.valueOf(tipo[0]));
			FGDASiopera.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			FGDASiopera.execute();
			System.out.println(FGDASiopera.getObject("un_Resultado"));
			FGDASiopera.close();
			conn.close();
//			log.info("Registro exitoso de la parcela en SIO_PUNTOS_MONITOREO");
		} catch (Exception e) {
//			log.error("Error en el registro de la parcela en SIO_PUNTOS_MONITOREO");
			e.printStackTrace();
		}
	}

	private Integer consultarIdFGDA() {
		Integer id = 0;
		try {
//			log = SMBC_Log.log(this.getClass());
//			log.info("Inicio de la consulta del id de la FGDA de SIO_FUENTE_GENERADORA");
			conn = dataSource.getConnection();
			CallableStatement consultarIdFGDA = conn
					.prepareCall("{call RED_PK_PARCELAS.ConsultarIDFGDA(?)}");
			consultarIdFGDA.registerOutParameter("un_Resultado",
					OracleTypes.NUMBER);
			consultarIdFGDA.execute();
			id = consultarIdFGDA.getInt("un_Resultado");
			consultarIdFGDA.close();
			conn.close();
//			log.info("Consulta exitosa del id de la FGDA de SIO_FUENTE_GENERADORA");
		} catch (Exception e) {
//			log.error("Error en la concsulta del id de la FGDA de SIO_FUENTE_GENERADORA");
			e.printStackTrace();
		}
		return id;
	}

	private Integer consultarIdPunto() {
		Integer id = 0;
		try {
//			log.info("Inicio de la consulta del id del punto de monitoreo de SIO_PUNTOS_MONITOREO");
			conn = dataSource.getConnection();
			CallableStatement consultarPaisParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.ConsultarIDPunto(?)}");
			consultarPaisParcela.registerOutParameter("un_Resultado",
					OracleTypes.NUMBER);
			consultarPaisParcela.execute();
			id = consultarPaisParcela.getInt("un_Resultado");
			consultarPaisParcela.close();
			conn.close();
//			log.info("Consulta exitosa del id del punto de monitoreo de SIO_PUNTOS_MONITOREO");
		} catch (Exception e) {
//			log.error("Error en la consulta del id del punto de monitoreo de SIO_PUNTOS_MONITOREO");
			e.printStackTrace();
		}
		return id;
	}

	private Integer consultarIdEstacion() {
		Integer id = 0;
		try {
//			log.info("Inicio de la consulta del id de la estacion de SIO_ESTACIONES");
			conn = dataSource.getConnection();
			CallableStatement consultarPaisParcela = conn
					.prepareCall("{call RED_PK_PARCELAS.ConsultarIDEstacion(?)}");
			consultarPaisParcela.registerOutParameter("un_Resultado",
					OracleTypes.NUMBER);
			consultarPaisParcela.execute();
			id = consultarPaisParcela.getInt("un_Resultado");
			consultarPaisParcela.close();
			conn.close();
//			log.info("Consulta exitosa del id de la estacion de SIO_ESTACIONES");
		} catch (Exception e) {
//			log.error("Error en la consulta del id de la estacion de SIO_ESTACIONES");
			e.printStackTrace();
		}
		return id;
	}

	public void ingresarEstacionSiopera(String nombre) {
		try {
//			log.info("Inicio del registro de la parcela en SIO_ESTACIONES");
			conn = dataSource.getConnection();
			CallableStatement FGDASiopera = conn
					.prepareCall("{call RED_PK_PARCELAS.InsertarSIO_ESTACIONES(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			FGDASiopera.setString("un_tipo", "MAP");
			FGDASiopera.setDate("una_fecha_creacion",
					new Date(System.currentTimeMillis()));
			FGDASiopera.setDate("una_fecha_modificacion",
					new Date(System.currentTimeMillis()));
			FGDASiopera.setInt("una_altura", 0);
			FGDASiopera.setString("una_clase", "AMB");
			FGDASiopera.setString("una_categoria", "PRD");
			FGDASiopera.setString("un_nombre", nombre);
			FGDASiopera.setString("un_objeto_instalacion", "CCR");
			FGDASiopera.setInt("un_punto_monitoreo_id", consultarIdPunto());
			FGDASiopera.setString("un_estado", "ACT");
			FGDASiopera.setDate("una_fecha_aplicacion",
					new Date(System.currentTimeMillis()));
			FGDASiopera.setInt("un_id", consultarIdEstacion() + 1);
			FGDASiopera.registerOutParameter("un_resultado",
					OracleTypes.VARCHAR);
			FGDASiopera.execute();
			System.out.println(FGDASiopera.getObject("un_Resultado"));
			FGDASiopera.close();
			conn.close();
//			log.info("Registro exitoso de la parcela en SIO_ESTACIONES");
		} catch (Exception e) {
//			log.error("Error en el registro de la parcela en SIO_ESTACIONES");
			e.printStackTrace();
		}
	}

}
