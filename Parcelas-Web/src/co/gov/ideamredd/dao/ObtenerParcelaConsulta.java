package co.gov.ideamredd.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import oracle.sql.CLOB;
import co.gov.ideamredd.conexion.ConexionBD;
import co.gov.ideamredd.entities.CAR;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.ContactoParcela;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.MetadataView;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Organizacion;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.Parcela;
import co.gov.ideamredd.entities.Proposito;
import co.gov.ideamredd.entities.Sector;
import co.gov.ideamredd.entities.Temporalidad;
import co.gov.ideamredd.entities.TipoBosque;
import co.gov.ideamredd.entities.TipoInventario;
import co.gov.ideamredd.util.Util;

public class ObtenerParcelaConsulta {

    private static Connection conn;
    private static DataSource dataSource = ConexionBD.getConnection();

    private static ArrayList<TipoBosque> bosque;
    private static ArrayList<Depto> depto;
    private static ArrayList<Municipios> municipio;
    private static ArrayList<CAR> car;
    private static ArrayList<String> fisiografia;
    private static Proposito proposito;
    private static TipoInventario inventario;
    private static ArrayList<ContactoParcela> contactosParcela;

    public static Parcela obtenerParcela(Integer idParcela) {
	Parcela parcela = new Parcela();
	try {
	    conn = dataSource.getConnection();
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

    public static ArrayList<TipoBosque> consultaTipoBosqueParcela(
	    Integer idParcela) {
	try {
	    conn = dataSource.getConnection();
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
		tipoBosque.setConsecutivo(resultSet.getInt(1));
		tipoBosque.setIdBosque(resultSet.getInt(2));
		tipoBosque.setAltitud(resultSet.getString(3));
		tipoBosque.setTemperatura(resultSet.getString(4));
		tipoBosque.setPrecipitacion(resultSet.getString(5));
		tipoBosque.setTipoBosque(resultSet.getString(6));
		bosque.add(tipoBosque);
	    }
	    resultSet.close();
	    consultarBosqueParcela.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return bosque;
    }

    public static ArrayList<Depto> consultaDeptoParcela(Integer idParcela) {
	try {
	    conn = dataSource.getConnection();
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
		departamento.setNombre(resultSet.getString(2));
		departamento.setPais(resultSet.getInt(3));
		depto.add(departamento);
	    }
	    resultSet.close();
	    consultarDeptoParcela.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return depto;
    }

    public static ArrayList<Municipios> consultaMunicipioParcela(
	    Integer idParcela) {
	try {
	    conn = dataSource.getConnection();
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
		// if (!resultSet.getString(2).contains("LOCALIDAD")) {
		Municipios municipios = new Municipios();
		municipios.setConsecutivo(resultSet.getInt(1));
		municipios.setNombre(resultSet.getString(2));
		municipios.setDepartamento(resultSet.getInt(3));
		municipio.add(municipios);
		// }
	    }
	    resultSet.close();
	    consultarMunicipioParcela.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return municipio;
    }

    public static ArrayList<CAR> consultaCarParcela(Integer idParcela) {
	try {
	    conn = dataSource.getConnection();
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
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return car;
    }

    public static ArrayList<ContactoParcela> consultaContactosParcelaId(
	    Integer idParcela) {
	try {
	    conn = dataSource.getConnection();
	    contactosParcela = new ArrayList<ContactoParcela>();
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

    public static Pais consultarPaisParcela(Integer idParcela) {
	Pais pais = new Pais();
	try {
	    conn = dataSource.getConnection();
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
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return pais;
    }

    public static Temporalidad consultarTemporalidadParcela(
	    Integer idTemporalidad) {
	Temporalidad temporalidad = new Temporalidad();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultarPaisParcela = conn
		    .prepareCall("{call RED_PK_PARCELAS.TemporalidadParcela_Consulta(?,?)}");
	    consultarPaisParcela.setInt("una_temporalidad", idTemporalidad);
	    consultarPaisParcela.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultarPaisParcela.execute();
	    OracleResultSet resultSet = (OracleResultSet) consultarPaisParcela
		    .getObject("un_Resultado");

	    while (resultSet.next()) {
		temporalidad.setConsecutivo(Integer.valueOf(resultSet
			.getObject(1).toString()));
		temporalidad.setNombre((String) resultSet.getObject(2));
	    }
	    resultSet.close();
	    consultarPaisParcela.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return temporalidad;
    }

    public static ArrayList<String> consultaFisiografiaParcela(Integer idParcela) {
	try {
	    conn = dataSource.getConnection();
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
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return fisiografia;
    }

    public static Proposito consultarPropositoParcela(Integer idProposito) {
	try {
	    conn = dataSource.getConnection();
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
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return proposito;
    }

    public static TipoInventario consultarTipoInventarioParcela(Integer idTipo) {
	try {
	    conn = dataSource.getConnection();
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
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return inventario;
    }

    public static/* ArrayList<String> */String[] consultarGeoParcela(
	    Integer idParcela) {
	// Clob geometria = null;
	// ArrayList<String> coordenadas = new ArrayList<String>();
	// try {
	// conn = dataSource.getConnection();
	// CallableStatement consultarGeoProyecto = conn
	// .prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta(?,?)}");
	// consultarGeoProyecto.setInt("un_Consecutivo", idParcela);
	// consultarGeoProyecto.registerOutParameter("un_Resultado",
	// OracleTypes.CURSOR);
	// consultarGeoProyecto.execute();
	// OracleResultSet resultSet = (OracleResultSet) consultarGeoProyecto
	// .getObject("un_Resultado");
	// while (resultSet.next()) {
	// coordenadas.add(resultSet.getObject(1).toString() + ","
	// + resultSet.getObject(2).toString());
	// }

	// geometria = (CLOB)
	// consultarGeoProyecto.getObject("un_Resultado");
	// resultSet.close();
	// consultarGeoProyecto.close();
	// coordenadas = Util.obtenerDatosGeometria(Util
	// .clobStringConversion(geometria));
	// conn.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return coordenadas;
	CLOB geometria = null;
	String[] coordenadas = null;
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultarPaisProyecto = conn
		    .prepareCall("{call RED_PK_GEOMETRIA.Parcela_Consulta(?,?)}");
	    consultarPaisProyecto.setInt("un_Consecutivo", idParcela);
	    consultarPaisProyecto.registerOutParameter("un_Resultado",
		    OracleTypes.CLOB);
	    consultarPaisProyecto.execute();
	    geometria = (CLOB) consultarPaisProyecto.getObject("un_Resultado");
	    consultarPaisProyecto.close();
	    coordenadas = Util.obtenerDatosGeometria(Util
		    .clobStringConversion(geometria));
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return coordenadas;
    }

    public static Organizacion consultarOrganizacionId(Integer idOrganizacion) {
	Organizacion org = null;
	try {
	    // log.info("Inicio de la consulta de la organizacion id: "
	    // + idOrganizacion);
	    conn = dataSource.getConnection();
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

    public static Contacto consultarContactoId(Integer idContacto) {
	Contacto contacto = null;
	try {
	    // log.info("Inicio de la consulta del contacto id: " + idContacto);
	    conn = dataSource.getConnection();
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

    public static ArrayList<Object> obtenerContactosParcela(
	    ArrayList<ContactoParcela> contactos) {
	ArrayList<Object> cont = new ArrayList<Object>();
	for (int i = 0; i < contactos.size(); i++) {
	    if (contactos.get(i).getIdContacto() != 0)
		cont.add(consultarContactoId(contactos.get(i).getIdContacto()));
	    else
		cont.add(consultarOrganizacionId(contactos.get(i)
			.getIdOrganizacion()));
	}
	return cont;
    }

    public static Pais consultarPaisPersona(Integer idPais) {
	Pais pais = null;
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultarPaisPersona = conn
		    .prepareCall("{call RED_PK_USUARIOS.PaisPersona_Consulta(?,?)}");
	    consultarPaisPersona.setInt("un_Pais", idPais);
	    consultarPaisPersona.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultarPaisPersona.execute();
	    OracleResultSet resultSet = (OracleResultSet) consultarPaisPersona
		    .getObject("un_Resultado");

	    while (resultSet.next()) {
		pais = new Pais();
		pais.setConsecutivo(Integer.valueOf(resultSet.getObject(1)
			.toString()));
		pais.setNombre((String) resultSet.getObject(2));
	    }
	    resultSet.close();
	    consultarPaisPersona.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return pais;
    }

    public static Sector consultarSectorOrganizacion(Integer idSector) {
	Sector sector = null;
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaSector = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.ConsultaSector(?,?)}");
	    consultaSector.setInt("un_consecutivo", idSector);
	    consultaSector.registerOutParameter("un_resultado",
		    OracleTypes.CURSOR);
	    consultaSector.execute();
	    OracleResultSet resultSet = (OracleResultSet) consultaSector
		    .getObject("un_resultado");
	    while (resultSet.next()) {
		sector = new Sector();
		sector.setConsecutivo(resultSet.getInt(1));
		sector.setDescripcion(resultSet.getString(2));
		sector.setClave(resultSet.getString(3));
	    }
	    resultSet.close();
	    consultaSector.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return sector;
    }

    public static MetadataView consultaInfoMetadata(Integer idParcela) {
	MetadataView meta = new MetadataView();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultarMetadataview = conn
		    .prepareCall("{call RED_PK_PARCELAS.consultarMetadataview(?,?)}");
	    consultarMetadataview.setInt("una_parcela", idParcela);
	    consultarMetadataview.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultarMetadataview.execute();
	    OracleResultSet resultSet = (OracleResultSet) consultarMetadataview
		    .getObject("un_Resultado");

	    while (resultSet.next()) {
		meta.setInfoParcela(resultSet.getString(2));
		meta.setInfoCoord(resultSet.getString(3));
		meta.setInfoCont1(resultSet.getString(4));
		meta.setInfoCont2(resultSet.getString(5));
		meta.setInfoCont3(resultSet.getString(6));
		meta.setInfoFecha(resultSet.getString(7));
		meta.setInfoKey(resultSet.getString(8));
	    }
	    resultSet.close();
	    consultarMetadataview.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return meta;
    }

    public static ArrayList<String> consultarDatosMetadatos() {
	ArrayList<String> metadato = new ArrayList<String>();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultarMetadatoInfo = conn
		    .prepareCall("{call RED_PK_PARCELAS.ConsultaMetadatoInfo(?)}");
	    consultarMetadatoInfo.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultarMetadatoInfo.execute();
	    OracleResultSet resultSet = (OracleResultSet) consultarMetadatoInfo
		    .getObject("un_Resultado");
	    while (resultSet.next()) {
		metadato.add(resultSet.getString(2));
		metadato.add(resultSet.getString(3));
		metadato.add(resultSet.getString(4));
		metadato.add(resultSet.getString(5));
		metadato.add(resultSet.getString(6));
		metadato.add(resultSet.getString(7));
		metadato.add(resultSet.getString(8));
		metadato.add(resultSet.getString(9));
		metadato.add(resultSet.getString(10));
		metadato.add(resultSet.getString(11));
		metadato.add(resultSet.getString(12));
		metadato.add(resultSet.getString(13));
		metadato.add(resultSet.getString(14));
		metadato.add(resultSet.getString(15));
		metadato.add(resultSet.getString(16));
		metadato.add(resultSet.getString(17));
	    }
	    resultSet.close();
	    conn.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return metadato;
    }

    public static String getRutaBD(String clave) {
	String ruta = "";
	try {
	    // log.info(ActualizarParametro.class+" Inicio get ruta providers");
	    conn = dataSource.getConnection();
	    CallableStatement consultaRutaProvider = conn
		    .prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
	    consultaRutaProvider.setString("un_Nombre", clave);
	    consultaRutaProvider.registerOutParameter("una_Ruta",
		    OracleTypes.CURSOR);
	    consultaRutaProvider.registerOutParameter("sentencia",
		    OracleTypes.VARCHAR);
	    consultaRutaProvider.execute();
	    OracleResultSet r = (OracleResultSet) consultaRutaProvider
		    .getObject("una_Ruta");
	    while (r.next()) {
		ruta = r.getString(1);
	    }
	    r.close();
	    consultaRutaProvider.close();
	    conn.close();
	    // log.info(ActualizarParametro.class+
	    // "[modificarParametros] Termino");
	} catch (Exception e) {
	    // log.error(ActualizarParametro.class+
	    // "[modificarParametros] Fallo, no se puede obtener la ruta PROVIDERS de la tabpa RED_PARAMETROS",e);
	    e.printStackTrace();
	}
	return ruta;
    }

    public static String[] copiarImagenCarrusel(String rutaImagenes) {
	String[] listaDirectorio = null;
	try {
	    File directorio = new File(rutaImagenes);
	    File outFile = new File(System.getProperty("user.dir").substring(0,
		    System.getProperty("user.dir").length() - 3)
		    + getRutaBD("servidor_imagenes"));

	    listaDirectorio = directorio.list();
	    for (int x = 0; x < listaDirectorio.length; x++) {

		FileInputStream in = new FileInputStream(new File(directorio.getPath()+"/"+
			listaDirectorio[x]));
		FileOutputStream out = new FileOutputStream(outFile+"/"+listaDirectorio[x]);
		int c;
		try {
		    while ((c = in.read()) != -1)
			out.write(c);
		    in.close();
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
	return listaDirectorio;
    }

    public static Connection getConn() {
	return conn;
    }

    public static void setConn(Connection conn) {
	ObtenerParcelaConsulta.conn = conn;
    }

}
