// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;
import oracle.jdbc.OracleTypes;

import co.gov.ideamredd.admin.entities.Depto;
import co.gov.ideamredd.admin.entities.Municipios;
import co.gov.ideamredd.web.admin.entities.Noticias;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.Util;

/**
 * Clase para cargar los datos iniciales de los formularios.
 */
public class CargaInicialDatosProyectos {

	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();
	private static ArrayList<String> proyectos;
	private static ArrayList<String> codigos;
	private static ArrayList<Municipios> listaMunicipios;
	private static ArrayList<Depto> listaDeptos;

	public static void registrarGeometria(Integer idProyecto, String geometria) {
		
		try {
			conn = dataSource.getConnection();
			CallableStatement adicionarGeometriaParcela = conn
					.prepareCall("{call RED_PK_GEOMETRIA.Proyecto_Adiciona(?,?,?)}");

			adicionarGeometriaParcela.setInt("una_Llave", idProyecto);
			adicionarGeometriaParcela.setString("una_Geometria", geometria);
			adicionarGeometriaParcela.registerOutParameter("un_Resultado",
					OracleTypes.VARCHAR);
			adicionarGeometriaParcela.execute();
			System.out.println(adicionarGeometriaParcela
					.getObject("un_Resultado"));
			adicionarGeometriaParcela.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
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
			ResultSet r = (ResultSet) consultaNoticias
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
			ResultSet r = (ResultSet) consultaEventos
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

	public static void cargarProyectoConsulta() {

		proyectos = new ArrayList<String>();
		codigos = new ArrayList<String>();
		// ArrayList<String> par = consultaParcelas();
		ArrayList<String> par = new ArrayList<String>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaProyectosBusqueda = conn
					.prepareCall("{call RED_PK_PROYECTOS.consultarTodosProyectos(?)}");
			consultaProyectosBusqueda.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaProyectosBusqueda.execute();
			ResultSet r = (ResultSet) consultaProyectosBusqueda
					.getObject("un_Resultado");
			while (r.next()) {
				String[] cords = Util.obtenerDatosGeometria(r.getString(4));
				String c="";
				for(int i=1;i<cords.length;i++){
					if(c.equals(""))
						c=cords[i];
					else
						c=c+","+cords[i];
				}
				if(c.length()>15)
					c=c.substring(0,15)+"...";
				proyectos.add(r.getString(1) + ";" + r.getString(2) + ";"
						+ r.getString(3) + ";" +  c + ";"
						+ r.getString(5) + ";" + r.getString(7) + ";"
						+ r.getString(9) + ";" + r.getString(11) + ";"
						+ r.getString(13) + ";" + r.getString(15) + ";"
						+ r.getString(17)+";"+r.getString(19).substring(0,11)+";"+r.getString(20).substring(0,11));
				codigos.add(r.getString(1) + ";" + r.getString(2) + ";"
						+ r.getString(3) + ";" + r.getString(4) + ";"
						+ r.getString(6) + ";" + r.getString(8) + ";"
						+ r.getString(10) + ";" + r.getString(11) + ";"
						+ r.getString(14) + ";" + r.getString(16) + ";"
						+ r.getString(18)+";"+r.getString(19)+";"+r.getString(20));
			}
			r.close();
			consultaProyectosBusqueda.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
			ResultSet r = (ResultSet) consultaPais
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
			ResultSet r = (ResultSet) consultaMunicipio
					.getObject("un_Resultado");
			while (r.next()) {
//				if (!r.getString(2).contains("LOCALIDAD")) {
					Municipios municipios = new Municipios();
					municipios.setConsecutivo(r.getInt(1));
					municipios.setNombre(r.getString(2));
					municipios.setDepartamento(r.getInt(3));
					listaMunicipios.add(municipios);
//				}
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
			ResultSet r = (ResultSet) consultaDepartamento
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
			ResultSet r = (ResultSet) consultaTipoBosque
					.getObject("un_Resultado");
			while (r.next()) {
				tipoBosque += "<option value=\"" + r.getObject(2) + "\">"
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
			ResultSet r = (ResultSet) consultaEstado
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
			ResultSet r = (ResultSet) consultaCar
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
			ResultSet r = (ResultSet) consultaActividad
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
	
	public static String getTipoReporteProyecto() {
		String tipo = "";
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaTipoReporteProyecto = conn
					.prepareCall("{call RED_PK_TABLASTIPO.consultaTipoReporteProyecto(?)}");
			consultaTipoReporteProyecto.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaTipoReporteProyecto.execute();
			ResultSet r = (ResultSet) consultaTipoReporteProyecto
					.getObject("un_Resultado");
			while (r.next()) {
				tipo += "<option value=\"" + r.getObject(1) + "\">"
						+ r.getObject(2) + "</option>\n";
			}
			r.close();
			consultaTipoReporteProyecto.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tipo;
	}

	public static ArrayList<String> getProyectos() {
		return proyectos;
	}

	public static void setProyectos(ArrayList<String> proyectos) {
		CargaInicialDatosProyectos.proyectos = proyectos;
	}

	public static ArrayList<String> getCodigos() {
		return codigos;
	}

	public static void setCodigos(ArrayList<String> codigos) {
		CargaInicialDatosProyectos.codigos = codigos;
	}

}
