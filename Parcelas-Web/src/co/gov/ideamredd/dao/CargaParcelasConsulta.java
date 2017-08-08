package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.conexion.ConexionBD;
import co.gov.ideamredd.entities.CAR;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.ContactoParcela;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Organizacion;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.TipoBosque;
import co.gov.ideamredd.entities.TipoInventario;
import co.gov.ideamredd.servlets.Constantes;

public class CargaParcelasConsulta {

    private static Connection conn;
    private static DataSource dataSource = ConexionBD.getConnection();
    private static ArrayList<String> parcelas;
    private static ArrayList<TipoBosque> bosque;
    private static ArrayList<Depto> depto;
    private static ArrayList<Municipios> municipio;
    private static Logger log;
    private static TipoInventario inventario;
    private static Contacto contacto;
    private static ArrayList<CAR> car;
    public static ArrayList<String> codigos;

    public static ArrayList<String> cargarResumenParcelaConsulta() {

	parcelas = new ArrayList<String>();
	codigos = new ArrayList<String>();
	// ArrayList<String> par = consultaParcelas();
	ArrayList<String> par = new ArrayList<String>();
	try {
	    Integer idTBosque;
	    ArrayList<String> tipoBosque;
	    Contacto cont;
	    Organizacion org;
	    String fgda;
	    conn = dataSource.getConnection();
	    CallableStatement consultaParcelaBusqueda = conn.prepareCall("{call RED_PK_TABLASTIPO.ConsultarParcelas(?)}");
	    consultaParcelaBusqueda.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
	    consultaParcelaBusqueda.execute();
	    OracleResultSet r = (OracleResultSet) consultaParcelaBusqueda.getObject("un_Resultado");
	    
	    while (r.next()) {
		ArrayList<TipoBosque> tipoBosquePar = consultaTipoBosqueParcela(Integer
			.valueOf(r.getInt(1)));
		if (tipoBosquePar.size() > 0) {
		    tipoBosque = getTipoBosque(tipoBosquePar.get(0)
			    .getIdBosque());
		    idTBosque = tipoBosquePar.get(0).getIdBosque();
		} else {
		    tipoBosque = new ArrayList<String>();
		    tipoBosque.add("N/S");
		    idTBosque = 0;
		}
		ContactoParcela cp = consultaContactoPorParcelaClase(
			Integer.valueOf(r.getString(1)),
			Integer.valueOf(Constantes.opcionFGDA));
		if (cp.getIdContacto() != 0) {
		    cont = consultarContactoId(cp.getIdContacto());
		    fgda = cont.getNombre();
		} else {
		    org = consultarOrganizacionId(cp.getIdOrganizacion());
		    fgda = org.getNombre();
		}
		parcelas.add(r.getString(1)+","+r.getString(2)+","+r.getString(3).substring(0,11)+","+r.getString(5)+","+r.getString(7)+","+r.getString(10)+","+r.getString(14)+","+fgda+","+tipoBosque.get(0)+","+r.getString(12));
		codigos.add(r.getString(1)+","+r.getString(2)+","+r.getString(3).substring(0,11)+","+r.getString(6)+","+r.getString(8)+","+r.getString(9)+","+r.getString(13)+","+fgda+","+idTBosque+","+r.getString(11));
	    }
	    r.close();
	    consultaParcelaBusqueda.close();
	    conn.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return parcelas;
    }

    private static ArrayList<String> consultaParcelas() {
	ArrayList<String> p = new ArrayList<String>();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaParcelaBusqueda = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.ConsultarParcelas(?)}");
	    consultaParcelaBusqueda.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaParcelaBusqueda.execute();
	    OracleResultSet r = (OracleResultSet) consultaParcelaBusqueda
		    .getObject("un_Resultado");
	    while (r.next()) {
		p.add(r.getInt(1) + "," + r.getString(2) + "," + r.getString(3)
			+ "," + r.getInt(4));
	    }
	    r.close();
	    consultaParcelaBusqueda.close();
	    conn.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return p;
    }

    public static Pais consultarPaisParcela(Integer idParcela) {
	Pais pais = new Pais();
	try {
//	    log = SMBC_Log.Log(CargaParcelasConsulta.class);
//	    log.info("Inicio de la consulta del pais de la parcela id:"
//		    + idParcela);
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
//	    log.info("consulta exitosa del pais de la parcela id:" + idParcela);
	} catch (Exception e) {
//	    log.error("Error en la consulta del pais de la parcela id:"
//		    + idParcela);
	    e.printStackTrace();
	}
	return pais;
    }

    public static ArrayList<Depto> consultaDeptoParcela(Integer idParcela) {
	try {
//	    log = SMBC_Log.Log(CargaParcelasConsulta.class);
//	    log.info("Inicio de la consulta de departamento(s) de la parcela id:"
//		    + idParcela);
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
		departamento.setNombre((String) resultSet.getObject(2));
		depto.add(departamento);
	    }
	    resultSet.close();
	    consultarDeptoParcela.close();
	    conn.close();
//	    log.info("consulta exitosa de departamento(s) para la parcela id:"
//		    + idParcela);
	} catch (Exception e) {
//	    log.error("Error en la consulta de departamento(s) de la parcela id:"
//		    + idParcela);
	    e.printStackTrace();
	}
	return depto;
    }

    public static ArrayList<Municipios> consultaMunicipioParcela(
	    Integer idParcela) {
	try {
//	    log = SMBC_Log.Log(CargaParcelasConsulta.class);
//	    log.info("Inicio de la consulta de municipio(s) para la parcela id:"
//		    + idParcela);
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
//	    log.info("consulta exitosa de municipio(s) para la parcela id:"
//		    + idParcela);
	} catch (Exception e) {
//	    log.error("Error en la consulta de municipio(s) de la parcela id:"
//		    + idParcela);
	    e.printStackTrace();
	}
	return municipio;
    }

    public static TipoInventario consultarTipoInventarioParcela(Integer idTipo) {
	try {
//	    log.info("Inicio de la consulta del tipo de inventario de la parcela");
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
//	    log.info("Consulta exitosa del tipo de inventario de la parcela");
	} catch (Exception e) {
//	    log.error("Error en la consulta del tipo de inventario de la parcela");
	    e.printStackTrace();
	}
	return inventario;
    }

    public static ArrayList<TipoBosque> consultaTipoBosqueParcela(
	    Integer idParcela) {
	try {
//	    log = SMBC_Log.Log(CargaParcelasConsulta.class);
//	    log.info("Inicio de la consulta de tipos de bosque para la parcela id:"
//		    + idParcela);
	    conn = dataSource.getConnection();
	    bosque = new ArrayList<TipoBosque>();
	    CallableStatement consultarBosqueParcela = conn
		    .prepareCall("{call RED_PK_PARCELAS.BosqueParcela_tipoBosque(?,?)}");
	    consultarBosqueParcela.setInt("una_parcela", idParcela);
	    consultarBosqueParcela.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultarBosqueParcela.execute();
	    OracleResultSet resultSet = (OracleResultSet) consultarBosqueParcela
		    .getObject("un_Resultado");

	    while (resultSet.next()) {
		TipoBosque tipoBosque = new TipoBosque();
		tipoBosque.setConsecutivo(resultSet.getInt(1));
		// tipoBosque.setTipoBosque((String) resultSet.getObject(2));
		// tipoBosque.setIdParcela(Integer.valueOf(resultSet.getObject(3)
		// .toString()));
		// tipoBosque.setPrecipitacion((String) resultSet.getObject(4));
		// tipoBosque.setTemperatura((String) resultSet.getObject(5));
		// tipoBosque.setAltitud((String) resultSet.getObject(6));
		tipoBosque.setIdBosque(resultSet.getInt(2));
		bosque.add(tipoBosque);
	    }
	    resultSet.close();
	    consultarBosqueParcela.close();
	    conn.close();
//	    log.info("consulta exitosa de los tipos de bosque para la parcela id:"
//		    + idParcela);
	} catch (Exception e) {
//	    log.error("Error en la consulta de los tipos de bosque de la parcela id:"
//		    + idParcela);
	    e.printStackTrace();
	}
	return bosque;
    }

    public static ArrayList<String> getTipoBosque(Integer tb) {
	ArrayList<String> tipoBosque = new ArrayList<String>();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaTipoBosque = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.consultaTipoBosque_Id(?, ?)}");
	    consultaTipoBosque.setInt("consecutivo", tb);
	    consultaTipoBosque.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaTipoBosque.execute();
	    OracleResultSet r = (OracleResultSet) consultaTipoBosque
		    .getObject("un_Resultado");
	    while (r.next()) {
		tipoBosque.add(r.getString(1));
	    }
	    r.close();
	    consultaTipoBosque.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return tipoBosque;
    }

    public static Contacto consultarContactoId(Integer idContacto) {
	try {
//	    log.info("Inicio de la consulta del contacto id: " + idContacto);
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
//	    log.info("Consulta exitosa del contacto id: " + idContacto);
	} catch (Exception e) {
//	    log.error("Error en la consulta del contacto id: " + idContacto);
	    e.printStackTrace();
	}
	return contacto;
    }

    public static Organizacion consultarOrganizacionId(Integer idOrganizacion) {
	Organizacion org = null;
	try {
//	    log.info("Inicio de la consulta de la organizacion id: "
//		    + idOrganizacion);
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
//	    log.info("Consulta exitosa de la organizacion id: "
//		    + idOrganizacion);
	} catch (Exception e) {
//	    log.error("Error en la consulta de la organizacion id: "
//		    + idOrganizacion);
	    e.printStackTrace();
	}
	return org;
    }

    public static ContactoParcela consultaContactoPorParcelaClase(
	    Integer idParcela, Integer idClase) {
	ContactoParcela contactoParcela = null;
	try {
//	    log.info("Inicio de la consulta del contacto de la parcela: "
//		    + idParcela + " y clase de contacto: " + idClase);
	    conn = dataSource.getConnection();
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
//	    log.info("Consulta exitosa del contacto de la parcela: "
//		    + idParcela + " y clase de contacto: " + idClase);
	} catch (Exception e) {
//	    log.error("Error en la consulta del contacto de la parcela: "
//		    + idParcela + " y clase de contacto: " + idClase);
	    e.printStackTrace();
	}
	return contactoParcela;
    }

    public static ArrayList<CAR> consultaCarParcela(Integer idParcela) {
	try {
//	    log.info("Inicio de la consulta de CAR para la parcela id:"
//		    + idParcela);
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
//	    log.info("consulta exitosa de CAR para la parcela id:" + idParcela);
	} catch (Exception e) {
//	    log.error("Error en la consulta de CAR de la parcela id:"
//		    + idParcela);
	    e.printStackTrace();
	}
	return car;
    }

}
