package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.sql.DataSource;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexion.ConexionBD;
import co.gov.ideamredd.entities.CAR;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Noticias;
import co.gov.ideamredd.entities.Organizacion;
import co.gov.ideamredd.entities.Reportes;
import co.gov.ideamredd.entities.Temporalidad;

public class CargaDatosInicialParcelas {

    private static Connection conn;
    private static DataSource dataSource = ConexionBD.getConnection();
    private static ArrayList<Municipios> listaMunicipios;
    private static ArrayList<Depto> listaDeptos;
    private static ArrayList<Contacto> listaContactos;
    private static ArrayList<Organizacion> listaOrganizacion;
    private static Temporalidad temp;
    private static Integer idUsuario;
    private static Integer usuario;

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

    public static String getPaises() {
	String pais = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaPais = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Pais_Consulta(?, ?)}");
	    consultaPais.setString("un_Nombre", "");
	    consultaPais.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaPais.execute();
	    OracleResultSet r = (OracleResultSet) consultaPais
		    .getObject("un_Resultado");
	    while (r.next()) {
		pais += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaPais.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return pais;
    }

    public static String getDepartamentos() {
	String departamentos = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaDepartamento = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Departamento_Consulta(?, ?)}");
	    consultaDepartamento.setString("un_Nombre", "");
	    consultaDepartamento.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaDepartamento.execute();
	    OracleResultSet r = (OracleResultSet) consultaDepartamento
		    .getObject("un_Resultado");
	    while (r.next()) {
		departamentos += "<option value=\"" + r.getObject(2) + "\">"
			+ r.getString(3) + "</option>\n";
		// System.out.println("<option value=\""+r.getObject(1)+"\">"+r.getObject(2)+"</option>");
	    }
	    r.close();
	    consultaDepartamento.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return departamentos;
    }

    public static String getMunicipios() {
	String departamentos = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaDepartamento = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Municipio_Consulta(?, ?)}");
	    consultaDepartamento.setString("un_Nombre", "");
	    consultaDepartamento.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaDepartamento.execute();
	    OracleResultSet r = (OracleResultSet) consultaDepartamento
		    .getObject("un_Resultado");
	    while (r.next()) {
		departamentos += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
		// System.out.println("<option value=\""+r.getObject(1)+"\">"+r.getObject(2)+"</option>");
	    }
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return departamentos;
    }

    public static ArrayList<Municipios> getArrayMunicipios() {
	listaMunicipios = new ArrayList<Municipios>();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaMunicipio = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Municipio_Consulta(?, ?)}");
	    consultaMunicipio.setString("un_Nombre", "");
	    consultaMunicipio.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaMunicipio.execute();
	    OracleResultSet r = (OracleResultSet) consultaMunicipio
		    .getObject("un_Resultado");
	    while (r.next()) {
		// if (!r.getString(2).contains("LOCALIDAD")) {
		Municipios municipios = new Municipios();
		municipios.setConsecutivo(r.getInt(1));
		municipios.setNombre(r.getString(2));
		municipios.setDepartamento(r.getInt(3));
		listaMunicipios.add(municipios);
		// }
	    }
	    r.close();
	    consultaMunicipio.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return listaMunicipios;
    }

    public static ArrayList<Depto> getArrayDeptos() {
	listaDeptos = new ArrayList<Depto>();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaDepartamento = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Departamento_Consulta(?, ?)}");
	    consultaDepartamento.setString("un_Nombre", "");
	    consultaDepartamento.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaDepartamento.execute();
	    OracleResultSet r = (OracleResultSet) consultaDepartamento
		    .getObject("un_Resultado");
	    while (r.next()) {
		Depto departamento = new Depto();
		departamento.setConsecutivo(r.getInt(1));
		departamento.setNombre(r.getString(2));
		listaDeptos.add(departamento);
	    }
	    r.close();
	    consultaDepartamento.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return listaDeptos;
    }

    public static String getTipoDocumento() {
	String tipoDocumento = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaTipoDocumento = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.TipoIdentificacion_Consulta(?, ?)}");
	    consultaTipoDocumento.setString("un_Nombre", "");
	    consultaTipoDocumento.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaTipoDocumento.execute();
	    OracleResultSet r = (OracleResultSet) consultaTipoDocumento
		    .getObject("un_Resultado");
	    while (r.next()) {
		tipoDocumento += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaTipoDocumento.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return tipoDocumento;
    }

    public static String getTipoPersona() {
	String tipoPersona = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaTipoPersona = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.TipoPersona_Consulta(?, ?)}");
	    consultaTipoPersona.setString("una_Descripcion", "");
	    consultaTipoPersona.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaTipoPersona.execute();
	    OracleResultSet r = (OracleResultSet) consultaTipoPersona
		    .getObject("un_Resultado");
	    while (r.next()) {
		tipoPersona += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaTipoPersona.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return tipoPersona;
    }

    public static String getTipoInventario() {
	String tipoInventario = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaTipoInventario = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.TipoInventario_Consulta(?, ?)}");
	    consultaTipoInventario.setString("un_Nombre", "");
	    consultaTipoInventario.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaTipoInventario.execute();
	    OracleResultSet r = (OracleResultSet) consultaTipoInventario
		    .getObject("un_Resultado");
	    while (r.next()) {
		tipoInventario += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaTipoInventario.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return tipoInventario;
    }

    public static String getTipoBosque() {
	String tipoBosque = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaTipoBosque = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.TipoBosque_Consulta(?, ?)}");
	    consultaTipoBosque.setString("un_Nombre", "");
	    consultaTipoBosque.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaTipoBosque.execute();
	    OracleResultSet r = (OracleResultSet) consultaTipoBosque
		    .getObject("un_Resultado");
	    while (r.next()) {
		tipoBosque += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaTipoBosque.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return tipoBosque;
    }

    public static String getTenencia() {
	String tenencia = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaTenencia = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Tenencia_Consulta(?, ?)}");
	    consultaTenencia.setString("una_Descripcion", "");
	    consultaTenencia.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaTenencia.execute();
	    OracleResultSet r = (OracleResultSet) consultaTenencia
		    .getObject("un_Resultado");
	    while (r.next()) {
		tenencia += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaTenencia.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return tenencia;
    }

    public static String getTemporalidad() {
	String temporalidad = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaTemporalidad = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Temporalidad_Consulta(?, ?)}");
	    consultaTemporalidad.setString("un_Nombre", "");
	    consultaTemporalidad.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaTemporalidad.execute();
	    OracleResultSet r = (OracleResultSet) consultaTemporalidad
		    .getObject("un_Resultado");
	    while (r.next()) {
		temporalidad += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaTemporalidad.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return temporalidad;
    }

    public static String getMetodologiaGeneracion() {
	String metodologiaGeneracion = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaMetodologiaGeneracion = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.MetodologiaGeneracion_Consulta(?, ?)}");
	    consultaMetodologiaGeneracion.setString("una_Descripcion", "");
	    consultaMetodologiaGeneracion.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaMetodologiaGeneracion.execute();
	    OracleResultSet r = (OracleResultSet) consultaMetodologiaGeneracion
		    .getObject("un_Resultado");
	    while (r.next()) {
		metodologiaGeneracion += "<option value=\"" + r.getObject(1)
			+ "\">" + r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaMetodologiaGeneracion.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return metodologiaGeneracion;
    }

    public static String getMetodologia() {
	String metodologia = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaMetodologia = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Metodologia_Consulta(?, ?)}");
	    consultaMetodologia.setString("un_Nombre", "");
	    consultaMetodologia.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaMetodologia.execute();
	    OracleResultSet r = (OracleResultSet) consultaMetodologia
		    .getObject("un_Resultado");
	    while (r.next()) {
		metodologia += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaMetodologia.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return metodologia;
    }

    public static String getLicencia() {
	String licencia = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaLicencia = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Licencias_Consulta(?, ?)}");
	    consultaLicencia.setString("un_Nombre", "");
	    consultaLicencia.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaLicencia.execute();
	    OracleResultSet r = (OracleResultSet) consultaLicencia
		    .getObject("un_Resultado");
	    while (r.next()) {
		if (r.getInt(1) > 3) {
		    licencia += "<option value=\"" + r.getObject(1) + "\">"
			    + r.getObject(2) + "</option>\n";
		}
	    }
	    r.close();
	    consultaLicencia.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return licencia;
    }

    public static String getEstado() {
	String estado = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaEstado = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Estado_Consulta(?, ?)}");
	    consultaEstado.setString("un_Nombre", "");
	    consultaEstado.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaEstado.execute();
	    OracleResultSet r = (OracleResultSet) consultaEstado
		    .getObject("un_Resultado");
	    while (r.next()) {
		estado += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaEstado.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return estado;
    }

    public static String getDocumento() {
	String documento = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaDocumento = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Documento_Consulta(?, ?)}");
	    consultaDocumento.setString("un_Nombre", "");
	    consultaDocumento.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaDocumento.execute();
	    OracleResultSet r = (OracleResultSet) consultaDocumento
		    .getObject("un_Resultado");
	    while (r.next()) {
		documento += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaDocumento.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return documento;
    }

    public static String getCredencial() {
	String credencial = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaCredencial = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Credencial_Consulta(?, ?)}");
	    consultaCredencial.setString("un_Nombre", "");
	    consultaCredencial.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaCredencial.execute();
	    OracleResultSet r = (OracleResultSet) consultaCredencial
		    .getObject("un_Resultado");
	    while (r.next()) {
		credencial += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaCredencial.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return credencial;
    }

    public static String getCAR() {
	String car = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaCar = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.CAR_Consulta(?, ?)}");
	    consultaCar.setString("un_Nombre", "");
	    consultaCar
		    .registerOutParameter("un_Resultado", OracleTypes.CURSOR);
	    consultaCar.execute();
	    OracleResultSet r = (OracleResultSet) consultaCar
		    .getObject("un_Resultado");
	    while (r.next()) {
		car += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaCar.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return car;
    }

    public static ArrayList<CAR> getCARs() {
	ArrayList<CAR> car = new ArrayList<CAR>();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaCar = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.CAR_Consulta(?, ?)}");
	    consultaCar.setString("un_Nombre", "");
	    consultaCar
		    .registerOutParameter("un_Resultado", OracleTypes.CURSOR);
	    consultaCar.execute();
	    OracleResultSet r = (OracleResultSet) consultaCar
		    .getObject("un_Resultado");
	    while (r.next()) {
		CAR car2 = new CAR();
		car2.setConsecutivo(Integer.valueOf(r.getObject(1).toString()));
		car2.setNombre(r.getObject(2).toString());
		car.add(car2);
	    }
	    r.close();
	    consultaCar.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return car;
    }

    public static String getAlturaIndividuo() {
	String alturaIndividuo = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaAlturaIndividuo = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.AlturaIndividuo_Consulta(?, ?)}");
	    consultaAlturaIndividuo.setString("un_Nombre", "");
	    consultaAlturaIndividuo.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaAlturaIndividuo.execute();
	    OracleResultSet r = (OracleResultSet) consultaAlturaIndividuo
		    .getObject("un_Resultado");
	    while (r.next()) {
		alturaIndividuo += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaAlturaIndividuo.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return alturaIndividuo;
    }

    public static String getActividad() {
	String actividad = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaActividad = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Actividad_Consulta(?, ?)}");
	    consultaActividad.setString("un_Nombre", "");
	    consultaActividad.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaActividad.execute();
	    OracleResultSet r = (OracleResultSet) consultaActividad
		    .getObject("un_Resultado");
	    while (r.next()) {
		actividad += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaActividad.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return actividad;
    }

    public static String getActividadUsuario() {
	String actividadUsuario = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaActividadUsuario = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.ActividadUsuario_Consulta(?, ?)}");
	    consultaActividadUsuario.setString("una_Descripcion", "");
	    consultaActividadUsuario.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaActividadUsuario.execute();
	    OracleResultSet r = (OracleResultSet) consultaActividadUsuario
		    .getObject("un_Resultado");
	    while (r.next()) {
		actividadUsuario += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaActividadUsuario.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return actividadUsuario;
    }

    public static String getProposito() {
	String propositoParcela = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultarPropuesta = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Propuesta_Consulta(?, ?)}");
	    consultarPropuesta.setString("una_clave", "");
	    consultarPropuesta.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultarPropuesta.execute();
	    OracleResultSet r = (OracleResultSet) consultarPropuesta
		    .getObject("un_Resultado");
	    while (r.next()) {
		propositoParcela += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultarPropuesta.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return propositoParcela;
    }

    public static String getPertenencia() {
	String pertenencia = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultarPertenencia = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Pertenencia_Consulta(?, ?)}");
	    consultarPertenencia.setString("una_clave", "");
	    consultarPertenencia.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultarPertenencia.execute();
	    OracleResultSet r = (OracleResultSet) consultarPertenencia
		    .getObject("un_Resultado");
	    while (r.next()) {
		pertenencia += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultarPertenencia.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return pertenencia;
    }

    public static String consultarTemporalidad() {
	String temporalidad = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaTemporalidad = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.Temporalidad_Consulta(?, ?)}");
	    consultaTemporalidad.setString("un_Nombre", "");
	    consultaTemporalidad.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaTemporalidad.execute();
	    OracleResultSet r = (OracleResultSet) consultaTemporalidad
		    .getObject("un_Resultado");
	    while (r.next()) {
		temporalidad += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaTemporalidad.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return temporalidad;
    }

    public static Temporalidad consultarTemporalidadId(Integer idTemporalidad) {
	temp = new Temporalidad();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaTemporalidad = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.TemporalidadId_Consulta(?, ?)}");
	    consultaTemporalidad.setInt("un_consecutivo", idTemporalidad);
	    consultaTemporalidad.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaTemporalidad.execute();
	    OracleResultSet r = (OracleResultSet) consultaTemporalidad
		    .getObject("un_Resultado");
	    while (r.next()) {
		temp.setConsecutivo(Integer.valueOf(r.getObject(1).toString()));
		temp.setNombre(r.getObject(2).toString());
	    }
	    r.close();
	    consultaTemporalidad.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return temp;
    }

    public static String getPropositoParcela() {
	String propositoParcela = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaPropositoParcela = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.PropositoParcela_Consulta(?, ?)}");
	    consultaPropositoParcela.setString("un_Nombre", "");
	    consultaPropositoParcela.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaPropositoParcela.execute();
	    OracleResultSet r = (OracleResultSet) consultaPropositoParcela
		    .getObject("un_Resultado");
	    while (r.next()) {
		propositoParcela += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaPropositoParcela.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return propositoParcela;
    }

    public static String getTipoReporte() {
	String tipoReporte = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaTipoReporte = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.TipoReporte_Consulta(?, ?)}");
	    consultaTipoReporte.setString("un_Nombre", null);
	    consultaTipoReporte.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaTipoReporte.execute();
	    OracleResultSet r = (OracleResultSet) consultaTipoReporte
		    .getObject("un_Resultado");
	    while (r.next()) {
		tipoReporte += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaTipoReporte.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return tipoReporte;
    }

    public static String getDivisionTerritorio() {
	String divTerritorio = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaDivTerritorio = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.DivTerritorio_Consulta(?, ?)}");
	    consultaDivTerritorio.setString("un_Nombre", null);
	    consultaDivTerritorio.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaDivTerritorio.execute();
	    OracleResultSet r = (OracleResultSet) consultaDivTerritorio
		    .getObject("un_Resultado");
	    while (r.next()) {
		divTerritorio += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaDivTerritorio.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return divTerritorio;
    }

    public static ArrayList<Reportes> getPeriodos() {
	ArrayList<Reportes> listaReportes = new ArrayList<Reportes>();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaPeriodosReportes = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.PeriodosReportes_Consulta(?)}");
	    consultaPeriodosReportes.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaPeriodosReportes.execute();
	    OracleResultSet r = (OracleResultSet) consultaPeriodosReportes
		    .getObject("un_Resultado");
	    while (r.next()) {
		Reportes reportes = new Reportes();
		reportes.setConsecutivo(r.getInt(1));
		reportes.setFechaGeneracion(r.getDate(2));
		reportes.setDivision(r.getInt(3));
		reportes.setPeriodoUno(r.getInt(4));
		reportes.setPeriodoDos(r.getInt(5));
		reportes.setTipoReporte(r.getInt(6));
		listaReportes.add(reportes);
	    }
	    r.close();
	    consultaPeriodosReportes.close();
	    conn.close();
	} catch (Exception e) {
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
	    OracleResultSet r = (OracleResultSet) consultaParcela
		    .getObject("un_Resultado");
	    while (r.next()) {
		if (r.getObject(2) != null)
		    parcelas += "<option value=\"" + r.getObject(1) + "\">"
			    + r.getObject(2) + "</option>\n";
		else
		    parcelas += "<option value=\"" + r.getObject(1) + "\">"
			    + r.getObject(32) + "</option>\n";
	    }
	    r.close();
	    consultaParcela.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return parcelas;
    }

    public static ArrayList<String> getCodigoCampoParcelas(
	    ArrayList<Integer> idParcela) {
	ArrayList<String> codigosCampos = new ArrayList<String>();
	Integer idP;
	try {
	    conn = dataSource.getConnection();

	    for (int i = 0; i < idParcela.size(); i++) {
		idP = idParcela.get(i);
		CallableStatement consultaParcela = conn
			.prepareCall("{call RED_PK_TABLASTIPO.ConsultaCodigoCampo(?,?)}");
		consultaParcela.setInt("un_consecutivo", idP);
		consultaParcela.registerOutParameter("un_Resultado",
			OracleTypes.CURSOR);
		consultaParcela.execute();
		OracleResultSet r = (OracleResultSet) consultaParcela
			.getObject("un_Resultado");
		while (r.next()) {
		    codigosCampos.add(r.getString(1));
		}
	    }
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return codigosCampos;
    }

    public static Organizacion getDatosIdeam() {
	Organizacion ideam = new Organizacion();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaDatosIdeam = conn
		    .prepareCall("{call RED_PK_PARCELAS.consulta_OrganizacionId(?,?)}");
	    consultaDatosIdeam.setInt("un_consecutivo", 1);
	    consultaDatosIdeam.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaDatosIdeam.execute();
	    OracleResultSet r = (OracleResultSet) consultaDatosIdeam
		    .getObject("un_Resultado");
	    while (r.next()) {
		ideam.setConsecutivo(r.getInt(1));
		ideam.setNombre(r.getString(2));
		ideam.setTelefono(r.getInt(4));
		ideam.setCorreo(r.getString(5));
	    }
	    r.close();
	    consultaDatosIdeam.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return ideam;

    }

    // public static Parcela consultaParcelas(Integer idParcela) {
    // Parcela p = new Parcela();
    // try {
    // CallableStatement consultaParcelaId = conn
    // .prepareCall("{call RED_PK_TABLASTIPO.ConsultaParcela(?,?)}");
    // consultaParcelaId.setInt("un_consecutivo", idParcela);
    // consultaParcelaId.registerOutParameter("un_Resultado",
    // OracleTypes.CURSOR);
    // consultaParcelaId.execute();
    // OracleResultSet r = (OracleResultSet) consultaParcelaId
    // .getObject("un_Resultado");
    // while (r.next()) {
    // p.setConsecutivo(r.getInt(1));
    // p.setNombre(r.getString(2));
    // p.setCodigoCampo(r.getString(32));
    // }
    // consultaParcelaId.close();
    // } catch (Exception e) {
    // try {
    // conn.rollback();
    // } catch (SQLException e1) {
    // e1.printStackTrace();
    // }
    // e.printStackTrace();
    // }
    // return p;
    // }

    // public static MetodologiaBiomasa consultaMetodologiaBiomasa(
    // Integer idMetodologia) {
    // MetodologiaBiomasa mb = new MetodologiaBiomasa();
    // try {
    // CallableStatement consultaParcelaId = conn
    // .prepareCall("{call RED_PK_TABLASTIPO.ConsultaMetodologia(?,?)}");
    // consultaParcelaId.setInt("un_consecutivo", idMetodologia);
    // consultaParcelaId.registerOutParameter("un_Resultado",
    // OracleTypes.CURSOR);
    // consultaParcelaId.execute();
    // OracleResultSet r = (OracleResultSet) consultaParcelaId
    // .getObject("un_Resultado");
    // while (r.next()) {
    // mb.setNombre(r.getString(2));
    // mb.setDescripcion(r.getString(3));
    // mb.setEcuacion(r.getString(4));
    // }
    // } catch (Exception e) {
    // try {
    // conn.rollback();
    // } catch (SQLException e1) {
    // e1.printStackTrace();
    // }
    // e.printStackTrace();
    // }
    // return mb;
    // }

    public static Integer getIdUsuario() {
	return idUsuario;
    }

    // public static void setIdUsuario(Integer idUsuario) {
    // CargaDatosSelect.idUsuario = idUsuario;
    // }

    public static Integer getUsuario() {
	return usuario;
    }

    // public static void setUsuario(Integer usuario) {
    // CargaDatosSelect.usuario = usuario;
    // }

    public static String getRoles() {
	String pais = "";
	try {
	    CallableStatement consultaRoles = conn
		    .prepareCall("{call RED_PK_USUARIOS.consultarRoles(?)}");
	    consultaRoles.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaRoles.execute();
	    OracleResultSet r = (OracleResultSet) consultaRoles
		    .getObject("un_Resultado");
	    while (r.next()) {
		if (r.getInt(1) > 1 && r.getInt(1) < 12)
		    pais += "<option value=\"" + r.getObject(1) + "\">"
			    + r.getString(2).replace('_', ' ') + "</option>\n";
	    }
	    r.close();
	    consultaRoles.close();
	} catch (Exception e) {
	    try {
		conn.rollback();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    }
	    e.printStackTrace();
	}
	return pais;
    }

    public static ArrayList<Contacto> getArrayContactos() {
	listaContactos = new ArrayList<Contacto>();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaContacto = conn
		    .prepareCall("{call RED_PK_PARCELAS.CONSULTA_CONTACTOS(?, ?)}");
	    consultaContacto.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaContacto.registerOutParameter("sentencia",
		    OracleTypes.VARCHAR);
	    consultaContacto.execute();
	    OracleResultSet r = (OracleResultSet) consultaContacto
		    .getObject("un_Resultado");
	    while (r.next()) {
		Contacto contacto = new Contacto();
		contacto.setConsecutivo(r.getInt(1));
		contacto.setNombre(r.getString(2));
		contacto.setPais(r.getInt(3));
		contacto.setTelefono(r.getString(4));
		contacto.setMovil(r.getString(5));
		contacto.setCorreo(r.getString(6));
		contacto.setMunicipio(r.getInt(7));
		listaContactos.add(contacto);
	    }
	    r.close();
	    consultaContacto.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return listaContactos;
    }

    public static ArrayList<Organizacion> getArrayOrganizacion() {
	listaOrganizacion = new ArrayList<Organizacion>();
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaOrganizacion = conn
		    .prepareCall("{call RED_PK_PARCELAS.CONSULTA_ORGANIZACIONES(?, ?)}");
	    consultaOrganizacion.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaOrganizacion.registerOutParameter("sentencia",
		    OracleTypes.VARCHAR);
	    consultaOrganizacion.execute();
	    OracleResultSet r = (OracleResultSet) consultaOrganizacion
		    .getObject("un_Resultado");
	    while (r.next()) {
		Organizacion org = new Organizacion();
		org.setConsecutivo(r.getInt(1));
		org.setNombre(r.getString(2));
		org.setDireccion(r.getString(3));
		org.setTelefono(r.getInt(4));
		org.setCorreo(r.getString(5));
		org.setSector(r.getInt(6));
		org.setPais(r.getInt(7));
		org.setDepto(r.getInt(8));
		org.setMunicipio(r.getInt(9));
		listaOrganizacion.add(org);
	    }
	    r.close();
	    consultaOrganizacion.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return listaOrganizacion;
    }

    public static String consultarSector() {
	String sector = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaSector = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.ConsultaSector(?, ?)}");
	    consultaSector.setInt("un_consecutivo", 0);
	    consultaSector.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaSector.execute();
	    OracleResultSet r = (OracleResultSet) consultaSector
		    .getObject("un_Resultado");
	    while (r.next()) {
		sector += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaSector.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return sector;
    }

    public static String consultarOtrosContactos() {
	String otrosContactos = "";
	try {
	    conn = dataSource.getConnection();
	    CallableStatement consultaOtrosContactos = conn
		    .prepareCall("{call RED_PK_TABLASTIPO.cargarOtrosContactos(?)}");
	    consultaOtrosContactos.registerOutParameter("un_Resultado",
		    OracleTypes.CURSOR);
	    consultaOtrosContactos.execute();
	    OracleResultSet r = (OracleResultSet) consultaOtrosContactos
		    .getObject("un_Resultado");
	    while (r.next()) {
		otrosContactos += "<option value=\"" + r.getObject(1) + "\">"
			+ r.getObject(2) + "</option>\n";
	    }
	    r.close();
	    consultaOtrosContactos.close();
	    conn.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return otrosContactos;
    }

}
