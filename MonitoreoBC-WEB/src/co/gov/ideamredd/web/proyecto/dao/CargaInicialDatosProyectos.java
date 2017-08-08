// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.proyecto.dao;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.proyecto.entities.Depto;
import co.gov.ideamredd.proyecto.entities.Municipios;
import co.gov.ideamredd.proyecto.entities.Noticias;
import co.gov.ideamredd.util.Util;

/**
 * clase usuada para cargar datos iniciales de fromularios de proyecto.
 */
public class CargaInicialDatosProyectos {

	private static Connection				conn;
	private static DataSource				dataSource	= ConexionBD.getConnection();
	private static ArrayList<String>		proyectos;
	private static ArrayList<String>		codigos;
	private static ArrayList<Municipios>	listaMunicipios;
	private static ArrayList<Depto>			listaDeptos;

	public static void registrarGeometria(Integer idProyecto, String geometria) {

		try {
			conn = dataSource.getConnection();
			CallableStatement adicionarGeometriaParcela = conn.prepareCall("{call RED_PK_GEOMETRIA.Proyecto_Adiciona(?,?,?)}");

			adicionarGeometriaParcela.setInt("una_Llave", idProyecto);
			adicionarGeometriaParcela.setString("una_Geometria", geometria);
			adicionarGeometriaParcela.registerOutParameter("un_Resultado", OracleTypes.VARCHAR);
			adicionarGeometriaParcela.execute();
			System.out.println(adicionarGeometriaParcela.getObject("un_Resultado"));
			adicionarGeometriaParcela.close();
		}
		catch (Exception e) {
			try {
				conn.rollback();
			}
			catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public static ArrayList<Noticias> getNoticias() {
		ArrayList<Noticias> noticias = new ArrayList<Noticias>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaNoticias = conn.prepareCall("{call RED_PK_TABLASTIPO.consultaTodasNoticias(?)}");
			consultaNoticias.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaNoticias.execute();
			ResultSet r = (ResultSet) consultaNoticias.getObject("un_Resultado");
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return noticias;
	}

	public static ArrayList<Noticias> getEventos() {
		ArrayList<Noticias> eventos = new ArrayList<Noticias>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaEventos = conn.prepareCall("{call RED_PK_TABLASTIPO.consultaTodosEventos(?)}");
			consultaEventos.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaEventos.execute();
			ResultSet r = (ResultSet) consultaEventos.getObject("un_Resultado");
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return eventos;
	}

	public static void cargarProyectoConsulta() {

		proyectos = new ArrayList<String>();
		codigos = new ArrayList<String>();

		ArrayList<String> par = new ArrayList<String>();
		
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaProyectosBusqueda = conn.prepareCall("{call RED_PK_PROYECTOS.consultarTodosProyectos(?)}");
			consultaProyectosBusqueda.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaProyectosBusqueda.execute();
			ResultSet r = (ResultSet) consultaProyectosBusqueda.getObject("un_Resultado");
			while (r.next()) {
				String pryc_consecutivo = r.getString("PRYC_CONSECUTIVO");
				String pryc_nombre = r.getString("PRYC_NOMBRE");
				String pryc_area = r.getString("PRYC_DES_AREA");
				String forma = r.getString("FRPR_FORMA");
				String pryc_cons_pais = r.getString("PRYC_CONS_PAIS");
				String nombre_pais = r.getString("PAIS");
				String tpbs_descripcion = r.getString("TIPOBOSQUE");
				String id_tipobosque = r.getString("PRYC_TIPO_BOSQUE");
				String espr_nombre = r.getString("ESTADO");
				String pryc_cons_estado = r.getString("PRYC_CONS_ESTADO");
				String usr_nombre = r.getString("USUARIO");
				String usr_consecutivo = r.getString("PRYC_PROPIETARIO");
				String pryc_fecha_inicio = r.getString("PRYC_FECHA_INICIO");
				String pryc_fecha_fin = r.getString("PRYC_FECHA_FIN");

				
				// Obtener los departamentos

				ResultSet r_departamentos = null;
				Statement s_departamentos = null;
				s_departamentos = conn.createStatement();
				String sql_departamentos = "SELECT D.CODIGO AS ID_DEPARTAMENTO, D.NOMBRE AS DEPARTAMENTO FROM RED_DEPTOS_SHAPE D INNER JOIN RED_DEPTO_PROYECTO DP ON D.CODIGO=DP.DPPR_CONS_DEPARTAM WHERE DP.DPPR_CONS_PROYECTO=" + pryc_consecutivo;
				String nombres_departamentos = "";
				String ids_departamentos = "";

				try {
					if (s_departamentos.execute(sql_departamentos)) {
						r_departamentos = s_departamentos.getResultSet();

						String id_DEPARTAMENTO = "";
						String nombre_DEPARTAMENTO = "";
						int a = 0;

						if (r_departamentos != null) {
							while (r_departamentos.next()) {
								id_DEPARTAMENTO = r_departamentos.getString("ID_DEPARTAMENTO");
								nombre_DEPARTAMENTO = r_departamentos.getString("DEPARTAMENTO");

								if (a > 0) {
									ids_departamentos += "-=-";
									nombres_departamentos += ", ";
								}

								ids_departamentos += id_DEPARTAMENTO;
								nombres_departamentos += nombre_DEPARTAMENTO;

								a++;
							}

							r_departamentos.close();
						}
					}

					s_departamentos.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				try {
					if (r_departamentos != null) {
						r_departamentos.close();
					}
					if (s_departamentos != null) {
						s_departamentos.close();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}

				
				// Obtener los municipios
				
				ResultSet r_municipios = null;
				Statement s_municipios = null;
				s_municipios = conn.createStatement();
				String sql_municipios = "SELECT M.CODIGO AS ID_municipio, M.NOMBRE AS municipio FROM RED_municipios_SHAPE M INNER JOIN RED_municipio_PROYECTO MP ON M.CODIGO=MP.MNPR_CONS_MUNICIPIO WHERE MP.MNPR_CONS_PROYECTO=" + pryc_consecutivo;
				String nombres_municipios = "";
				String ids_municipios = "";
				
				try {
					if (s_municipios.execute(sql_municipios)) {
						r_municipios = s_municipios.getResultSet();
						
						String id_municipio = "";
						String nombre_municipio = "";
						int a = 0;
						
						if (r_municipios != null) {
							while (r_municipios.next()) {
								id_municipio = r_municipios.getString("ID_municipio");
								nombre_municipio = r_municipios.getString("municipio");
								
								if (a > 0) {
									ids_municipios += "-=-";
									nombres_municipios += ", ";
								}
								
								ids_municipios += id_municipio;
								nombres_municipios += nombre_municipio;
								
								a++;
							}
							
							r_municipios.close();
						}
					}
					
					s_municipios.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					if (r_municipios != null) {
						r_municipios.close();
					}
					if (s_municipios != null) {
						s_municipios.close();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				
				// Obtener las Cars
				
				ResultSet r_cars = null;
				Statement s_cars = null;
				s_cars = conn.createStatement();
				String sql_cars = "SELECT C.id_car AS ID_car, C.car AS car FROM RED_corporaciones C INNER JOIN RED_car_PROYECTO CP ON C.id_car=CP.crpr_cons_car WHERE CP.CRPR_CONS_PROYECTO=" + pryc_consecutivo;
				String nombres_cars = "";
				String ids_cars = "";
				
				try {
					if (s_cars.execute(sql_cars)) {
						r_cars = s_cars.getResultSet();
						
						String id_car = "";
						String nombre_car = "";
						int a = 0;
						
						if (r_cars != null) {
							while (r_cars.next()) {
								id_car = r_cars.getString("ID_car");
								nombre_car = r_cars.getString("car");
								
								if (a > 0) {
									ids_cars += "-=-";
									nombres_cars += ", ";
								}
								
								ids_cars += id_car;
								nombres_cars += nombre_car;
								
								a++;
							}
							
							r_cars.close();
						}
					}
					
					s_cars.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					if (r_cars != null) {
						r_cars.close();
					}
					if (s_cars != null) {
						s_cars.close();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				
				// Obtener las actividades
				
				ResultSet r_actividades = null;
				Statement s_actividades = null;
				s_actividades = conn.createStatement();
				String sql_actividades = "SELECT A.ACRD_CONSECUTIVO AS ID_ACTIVIDAD, A.ACRD_NOMBRE AS ACTIVIDAD FROM RED_ACTIVIDADREDD A INNER JOIN RED_ACTIVIDAD_PROYECTO AP ON A.ACRD_CONSECUTIVO=AP.ACPR_CONS_ACTIVIDAD WHERE AP.ACPR_CONS_PROYECTO=" + pryc_consecutivo;
				String nombres_actividades = "";
				String ids_actividades = "";
				
				try {
					if (s_actividades.execute(sql_actividades)) {
						r_actividades = s_actividades.getResultSet();
						
						String id_actividad = "";
						String nombre_actividad = "";
						int a = 0;
						
						if (r_actividades != null) {
							while (r_actividades.next()) {
								id_actividad = r_actividades.getString("ID_ACTIVIDAD");
								nombre_actividad = r_actividades.getString("ACTIVIDAD");
								
								if (a > 0) {
									ids_actividades += "-=-";
									nombres_actividades += ", ";
								}
								
								ids_actividades += id_actividad;
								nombres_actividades += nombre_actividad;
								
								a++;
							}
							
							r_actividades.close();
						}
					}
					
					s_actividades.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					if (r_actividades != null) {
						r_actividades.close();
					}
					if (s_actividades != null) {
						s_actividades.close();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				proyectos.add(pryc_consecutivo + ";" + pryc_nombre + ";" + pryc_area + ";" + "" + ";" + nombre_pais    + ";" + nombres_municipios + ";" + nombres_departamentos + ";" + tpbs_descripcion + ";" + nombres_cars + ";" + espr_nombre      + ";" + usr_nombre      + ";" + pryc_fecha_inicio + ";" + pryc_fecha_fin + ";" + nombres_actividades);
				codigos.add(  pryc_consecutivo + ";" + pryc_nombre + ";" + pryc_area + ";" + "" + ";" + pryc_cons_pais + ";" + ids_municipios     + ";" + ids_departamentos     + ";" + id_tipobosque    + ";" + ids_cars     + ";" + pryc_cons_estado + ";" + usr_consecutivo + ";" + pryc_fecha_inicio + ";" + pryc_fecha_fin + ";" + ids_actividades);
			}
			r.close();
			consultaProyectosBusqueda.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void cargarProyectoConsulta_ANTES() {
		
		proyectos = new ArrayList<String>();
		codigos = new ArrayList<String>();
		// ArrayList<String> par = consultaParcelas();
		ArrayList<String> par = new ArrayList<String>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaProyectosBusqueda = conn.prepareCall("{call RED_PK_PROYECTOS.consultarTodosProyectos(?)}");
			consultaProyectosBusqueda.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaProyectosBusqueda.execute();
			ResultSet r = (ResultSet) consultaProyectosBusqueda.getObject("un_Resultado");
			while (r.next()) {
				//				String valor = r.getString(4);
				//				String[] cords = Util.obtenerDatosGeometria(r.getString(4));
				String c = "";
				//				for(int i=1;i<cords.length;i++){
				//					if(c.equals(""))
				//						c=cords[i];
				//					else
				//						c=c+","+cords[i];
				//				}
				if (c.length() > 15)
					c = c.substring(0, 15) + "...";
				/*proyectos.add(r.getString(1) + ";" + r.getString(2) + ";" + r.getString(3) + ";" + c + ";" + r.getString(5) + ";" + r.getString(7) + ";" + r.getString(9) + ";" + r.getString(11) + ";" + r.getString(13) + ";" + r.getString(15) + ";" +
				 * r.getString(17)+";"+r.getString(19).substring(0,11)+";"+r.getString(20).substring(0,11)); codigos.add(r.getString(1) + ";" + r.getString(2) + ";" + r.getString(3) + ";" + r.getString(4) + ";" + r.getString(6) + ";" + r.getString(8) +
				 * ";" + r.getString(10) + ";" + r.getString(11) + ";" + r.getString(14) + ";" + r.getString(16) + ";" + r.getString(18)+";"+r.getString(19)+";"+r.getString(20)); */
				
				String pryc_consecutivo = r.getString("pryc_consecutivo");
				String pryc_nombre = r.getString("pryc_nombre");
				String pryc_area = r.getString("pryc_area");
				String forma = r.getString("frpr_forma");
				String pryc_cons_pais = r.getString("pryc_cons_pais");
				String nombre_pais = r.getString("nombre_pais");
				String nombre_departamento = r.getString("nombre_departamento");
				String codigo_departamento = r.getString("codigo_departamento");
				String codigo_municipio = r.getString("codigo_municipio");
				String nombre_municipio = r.getString("nombre_municipio");
				String tpbs_descripcion = r.getString("tpbs_descripcion");
				String id_tipobosque = r.getString("id_tipobosque");
				String nombre_car = r.getString("nombre_car");
				String id_car = r.getString("id_car");
				String espr_nombre = r.getString("espr_nombre");
				String pryc_cons_estado = r.getString("pryc_cons_estado");
				String usr_nombre = r.getString("usr_nombre");
				String usr_consecutivo = r.getString("usr_consecutivo");
				String pryc_fecha_inicio = r.getString("pryc_fecha_inicio");
				String pryc_fecha_fin = r.getString("pryc_fecha_fin");
				
				// Obtener las actividades
				
				ResultSet r_actividades = null;
				Statement s_actividades = null;
				s_actividades = conn.createStatement();
				String sql_actividades = "SELECT A.ACRD_CONSECUTIVO AS ID_ACTIVIDAD, A.ACRD_NOMBRE AS ACTIVIDAD FROM RED_ACTIVIDADREDD A INNER JOIN RED_ACTIVIDAD_PROYECTO AP ON A.ACRD_CONSECUTIVO=AP.ACPR_CONS_ACTIVIDAD WHERE AP.ACPR_CONS_PROYECTO=" + pryc_consecutivo;
				String nombres_actividades = "";
				String ids_actividades = "";
				
				try {
					if (s_actividades.execute(sql_actividades)) {
						r_actividades = s_actividades.getResultSet();
						
						String id_actividad = "";
						String nombre_actividad = "";
						int a = 0;
						
						if (r_actividades != null) {
							while (r_actividades.next()) {
								id_actividad = r_actividades.getString("ID_ACTIVIDAD");
								nombre_actividad = r_actividades.getString("ACTIVIDAD");
								
								if (a > 0) {
									ids_actividades += "-=-";
									nombres_actividades += ", ";
								}
								
								ids_actividades += id_actividad;
								nombres_actividades += nombre_actividad;
								
								a++;
							}
							
							r_actividades.close();
						}
					}
					
					s_actividades.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					if (r_actividades != null) {
						r_actividades.close();
					}
					if (s_actividades != null) {
						s_actividades.close();
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				proyectos.add(pryc_consecutivo + ";" + pryc_nombre + ";" + pryc_area + ";" + forma + ";País:" + nombre_pais + "; Municipio:" + nombre_municipio + "; Departamento:" + nombre_departamento + ";" + tpbs_descripcion + ";" + nombre_car + ";" + espr_nombre + ";" +      usr_nombre + ";" +      pryc_fecha_inicio.substring(0, 11) + ";" + pryc_fecha_fin.substring(0, 11) + ";" + nombres_actividades);
				codigos.add(  pryc_consecutivo + ";" + pryc_nombre + ";" + pryc_area + ";" + forma + ";" +      pryc_cons_pais + ";" +         codigo_municipio + ";" +               codigo_departamento + ";" + id_tipobosque + ";" +    id_car + ";" +     pryc_cons_estado + ";" + usr_consecutivo + ";" + pryc_fecha_inicio + ";" +                  pryc_fecha_fin + ";" +                  ids_actividades);
			}
			r.close();
			consultaProyectosBusqueda.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getPaises() {
		String pais = "";
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaPais = conn.prepareCall("{call RED_PK_TABLASTIPO.Pais_Consulta(?, ?)}");
			consultaPais.setString("un_Nombre", "");
			consultaPais.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaPais.execute();
			ResultSet r = (ResultSet) consultaPais.getObject("un_Resultado");
			while (r.next()) {
				pais += "<option value=\"" + r.getObject(1) + "\">" + r.getObject(2) + "</option>\n";
			}
			r.close();
			consultaPais.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return pais;
	}

	public static ArrayList<Municipios> getArrayMunicipios() {
		listaMunicipios = new ArrayList<Municipios>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaMunicipio = conn.prepareCall("{call RED_PK_TABLASTIPO.Municipio_Consulta(?, ?)}");
			consultaMunicipio.setString("un_Nombre", "");
			consultaMunicipio.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaMunicipio.execute();
			ResultSet r = (ResultSet) consultaMunicipio.getObject("un_Resultado");
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return listaMunicipios;
	}

	public static ArrayList<Depto> getArrayDeptos() {
		listaDeptos = new ArrayList<Depto>();
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaDepartamento = conn.prepareCall("{call RED_PK_TABLASTIPO.Departamento_Consulta(?, ?)}");
			consultaDepartamento.setString("un_Nombre", "");
			consultaDepartamento.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaDepartamento.execute();
			ResultSet r = (ResultSet) consultaDepartamento.getObject("un_Resultado");
			while (r.next()) {
				Depto departamento = new Depto();
				departamento.setConsecutivo(r.getInt(1));
				departamento.setNombre(r.getString(2));
				listaDeptos.add(departamento);
			}
			r.close();
			consultaDepartamento.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return listaDeptos;
	}

	public static String getTipoBosque() {
		String tipoBosque = "";
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaTipoBosque = conn.prepareCall("{call RED_PK_TABLASTIPO.TipoBosque_Consulta(?, ?)}");
			consultaTipoBosque.setString("un_Nombre", "");
			consultaTipoBosque.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaTipoBosque.execute();
			ResultSet r = (ResultSet) consultaTipoBosque.getObject("un_Resultado");
			while (r.next()) {
				tipoBosque += "<option value=\"" + r.getObject("TPBS_CONSECUTIVO") + "\">" + r.getObject("TPBS_DESCRIPCION") + "</option>\n";
			}
			r.close();
			consultaTipoBosque.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return tipoBosque;
	}

	public static String getEstado() {
		String estado = "";
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaEstado = conn.prepareCall("{call RED_PK_TABLASTIPO.Estado_Consulta(?, ?)}");
			consultaEstado.setString("un_Nombre", "");
			consultaEstado.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaEstado.execute();
			ResultSet r = (ResultSet) consultaEstado.getObject("un_Resultado");
			while (r.next()) {
				estado += "<option value=\"" + r.getObject(1) + "\">" + r.getObject(2) + "</option>\n";
			}
			r.close();
			consultaEstado.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return estado;
	}

	public static String getCAR() {
		String car = "";
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaCar = conn.prepareCall("{call RED_PK_TABLASTIPO.CAR_Consulta(?, ?)}");
			consultaCar.setString("un_Nombre", "");
			consultaCar.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaCar.execute();
			ResultSet r = (ResultSet) consultaCar.getObject("un_Resultado");
			while (r.next()) {
				car += "<option value=\"" + r.getObject(1) + "\">" + r.getObject(2) + "</option>\n";
			}
			r.close();
			consultaCar.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return car;
	}

	public static String getActividad() {
		String actividad = "";
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaActividad = conn.prepareCall("{call RED_PK_TABLASTIPO.Actividad_Consulta(?, ?)}");
			consultaActividad.setString("un_Nombre", "");
			consultaActividad.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaActividad.execute();
			ResultSet r = (ResultSet) consultaActividad.getObject("un_Resultado");
			while (r.next()) {
				actividad += "<option value=\"" + r.getObject(1) + "\">" + r.getObject(2) + "</option>\n";
			}
			r.close();
			consultaActividad.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return actividad;
	}

	public static String getTipoReporteProyecto() {
		String tipo = "";
		try {
			conn = dataSource.getConnection();
			CallableStatement consultaTipoReporteProyecto = conn.prepareCall("{call RED_PK_TABLASTIPO.consultaTipoReporteProyecto(?)}");
			consultaTipoReporteProyecto.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			consultaTipoReporteProyecto.execute();
			ResultSet r = (ResultSet) consultaTipoReporteProyecto.getObject("un_Resultado");
			while (r.next()) {
				tipo += "<option value=\"" + r.getObject(1) + "\">" + r.getObject(2) + "</option>\n";
			}
			r.close();
			consultaTipoReporteProyecto.close();
			conn.close();
		}
		catch (Exception e) {
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
