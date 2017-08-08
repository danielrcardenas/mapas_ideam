package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.security.Encript;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.entities.Usuario;

@Stateless
public class ConsultaUsuario {

	@EJB
	ConexionBD conexion;

	private Logger log;
	private Connection conn;
	private String un_Nombre;
	private String un_ApellidoUno;
	private String fechaUltimoI;
	private Integer un_TipoDocumento = -1;
	private Double una_Identificacion = new Double(-1);
	private String un_Rol = "";
	private ArrayList<Usuario> usuarios;
	private String un_CorreoElectronico;

	public ArrayList<Usuario> consultarUsuario() {
		usuarios = new ArrayList<Usuario>();
		Usuario usuario = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarUsuario = conn
					.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta(?,?,?,?,?,?)}");
			consultarUsuario.setInt("una_Identificacion",
					una_Identificacion.intValue());
			consultarUsuario.setString("un_Nombre", un_Nombre.toUpperCase());
			consultarUsuario.setInt("un_TipoDocumento", un_TipoDocumento);
			consultarUsuario.setString("una_fecha", null);
			consultarUsuario.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarUsuario.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR, 250);
			consultarUsuario.execute();
			System.out.println(consultarUsuario.getObject("un_Mensaje"));
			ResultSet resultSet = (ResultSet) consultarUsuario
					.getObject("un_Resultado");

			while (resultSet.next()) {
				usuario = new Usuario();
				usuario.setIdUsuario(resultSet.getInt(1));
				usuario.setNombre(resultSet.getString(2));
				usuario.setIdentificacion(resultSet.getObject(3).toString());
				usuario.setCorreoElectronico(resultSet.getString(4));
				usuario.setFechaCreacion(resultSet.getString(5));
				usuario.setActivo(resultSet.getString(6));
				if (resultSet.getString(7) != null) {
					usuario.setPais(resultSet.getInt(7));
					usuario.setNombrePais(consultaNombrePais(resultSet
							.getInt(7)));
				}
				usuario.setTipoPersona(resultSet.getInt(8));
				usuario.setNombreTipoPersona(consultaNombreTipoPersona(resultSet
						.getInt(8)));
				usuario.setTipoIdentificacion(resultSet.getInt(9));
				usuario.setNombreTipoIdenti(consultaNombreTipoIdent(resultSet
						.getInt(9)));
				usuario.setDireccion(resultSet.getString(10));
				usuario.setTelefonoOficina(resultSet.getObject(11).toString());
				if (resultSet.getString(12) != null) {
					usuario.setCelular(resultSet.getString(12));
				}
				usuario.setClave(resultSet.getString(13));
				if (resultSet.getString(14) != null) {
					usuario.setApellidoUno(resultSet.getString(14));
				}
				if (resultSet.getString(15) != null) {
					usuario.setApellidoDos(resultSet.getString(15));
				}
				if (resultSet.getString(16) != null) {
					usuario.setDepto(resultSet.getInt(16));
					usuario.setNombreDepto(consultaNombreDepartamento(resultSet
							.getInt(16)));
				}
				if (resultSet.getString(17) != null) {
					usuario.setMunicipio(resultSet.getInt(17));
					usuario.setNombreCiudad(consultaNombreMunicipio(resultSet
							.getInt(17)));
				}
				if (resultSet.getString(18) != null) {
					usuario.setOrganizacion(resultSet.getString(18));
				}
				if (resultSet.getString(19) != null) {
					usuario.setCargo(resultSet.getString(19));
				}
				usuario.setRolId(consultarIdRol(resultSet.getInt(1)));
				usuario.setNombresLicencias(consultarNombresLicencias(resultSet
						.getInt(1)));

				usuarios.add(usuario);
			}

			resultSet.close();
			consultarUsuario.close();
			conn.close();
			log.info("[consultarUsuario] Termino");
		} catch (Exception e) {
			log.error("[consultarUsuario] Fallo");
			e.printStackTrace();
		}
		return usuarios;

	}

	public Usuario consultarUsuarioRecordatorio() {
		Usuario usuario = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarUsuario = conn
					.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta_Recordatorio(?,?,?)}");
			consultarUsuario.setString("un_CorreoElectronico",
					un_CorreoElectronico);
			consultarUsuario.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarUsuario.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarUsuario.execute();
			System.out.println(consultarUsuario.getObject("un_Mensaje"));
			ResultSet resultSet = (ResultSet) consultarUsuario
					.getObject("un_Resultado");

			while (resultSet.next()) {
				usuario = new Usuario();
				usuario.setIdUsuario(resultSet.getInt(1));
				usuario.setNombre(resultSet.getString(2));
				usuario.setIdentificacion(resultSet.getObject(3).toString());
				usuario.setCorreoElectronico(resultSet.getString(4));
				usuario.setFechaCreacion(resultSet.getString(5));
				usuario.setActivo(resultSet.getString(6));
				usuario.setPais(resultSet.getInt(7));
				usuario.setTipoPersona(resultSet.getInt(8));
				usuario.setTipoIdentificacion(resultSet.getInt(9));
				usuario.setDireccion(resultSet.getString(10));
				usuario.setTelefonoOficina(resultSet.getObject(11).toString());
				if (resultSet.getString(12) != null) {
					usuario.setCelular(resultSet.getString(12));
				}
				usuario.setClave(resultSet.getString(13));
				if (resultSet.getString(14) != null) {
					usuario.setApellidoUno(resultSet.getString(14));
				}
				if (resultSet.getString(15) != null) {
					usuario.setApellidoDos(resultSet.getString(15));
				}
				if (resultSet.getString(16) != null) {
					usuario.setDepto(resultSet.getInt(16));
				}
				if (resultSet.getString(17) != null) {
					usuario.setMunicipio(resultSet.getInt(17));
				}
				if (resultSet.getString(18) != null) {
					usuario.setOrganizacion(resultSet.getString(18));
				}
				if (resultSet.getString(19) != null) {
					usuario.setCargo(resultSet.getString(19));
				}
			}
			resultSet.close();
			consultarUsuario.close();
			conn.close();
			log.info("[consultarUsuarioRecordatorio] Termino");
		} catch (Exception e) {
			log.error("[consultarUsuarioRecordatorio] Fallo");
			e.printStackTrace();
		}
		return usuario;
	}

	public Usuario consultarUsuarioPorDoc(int docPersona) {
		Usuario usuario = new Usuario();
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarUsuario = conn
					.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta_Doc(?,?,?)}");
			consultarUsuario.setInt("un_Id", docPersona);
			consultarUsuario.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarUsuario.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarUsuario.execute();
			System.out.println(consultarUsuario.getObject("un_Mensaje"));
			ResultSet resultSet = (ResultSet) consultarUsuario
					.getObject("un_Resultado");

			while (resultSet.next()) {
				usuario.setIdUsuario(resultSet.getInt(1));
				usuario.setNombre(resultSet.getString(2));
				usuario.setIdentificacion(resultSet.getObject(3).toString());
				usuario.setCorreoElectronico(resultSet.getString(4));
				usuario.setFechaCreacion(resultSet.getString(5));
				usuario.setActivo(resultSet.getString(6));
				usuario.setPais(resultSet.getInt(7));
				usuario.setTipoPersona(resultSet.getInt(8));
				usuario.setTipoIdentificacion(resultSet.getInt(9));
				usuario.setDireccion(resultSet.getString(10));
				usuario.setTelefonoOficina(resultSet.getObject(11).toString());
				if (resultSet.getString(12) != null) {
					usuario.setCelular(resultSet.getString(12));
				}
				usuario.setClave(resultSet.getString(13));
				if (resultSet.getString(14) != null) {
					usuario.setApellidoUno(resultSet.getString(14));
				}
				if (resultSet.getString(15) != null) {
					usuario.setApellidoDos(resultSet.getString(15));
				}
				if (resultSet.getString(16) != null) {
					usuario.setDepto(resultSet.getInt(16));
				}
				if (resultSet.getString(17) != null) {
					usuario.setMunicipio(resultSet.getInt(17));
				}
				if (resultSet.getString(18) != null) {
					usuario.setOrganizacion(resultSet.getString(18));
				}
				if (resultSet.getString(19) != null) {
					usuario.setCargo(resultSet.getString(19));
				}
			}

			resultSet.close();
			consultarUsuario.close();
			conn.close();
			log.info("[consultarUsuarioPorDoc] Termino");
		} catch (Exception e) {
			log.error("[consultarUsuarioPorDoc] Fallo");
			e.printStackTrace();
		}
		return usuario;
	}

	public Usuario usuarioEnLogOnCorrecto(int docPersona, String password) {
		Usuario usuario = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarUsuario = conn
					.prepareCall("{call RED_PK_USUARIOS.USUARIOLOGON(?,?,?)}");
			consultarUsuario.setInt("un_Doc", docPersona);
			consultarUsuario.setString("un_Pass",
					Encript.getEncodedPassword(password));
			consultarUsuario.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarUsuario.execute();
			ResultSet resultSet = (ResultSet) consultarUsuario
					.getObject("un_Resultado");

			while (resultSet.next()) {
				usuario = new Usuario();
				usuario.setIdUsuario(resultSet.getInt(1));
				usuario.setNombre(resultSet.getString(2));
				usuario.setIdentificacion(resultSet.getObject(3).toString());
				usuario.setCorreoElectronico(resultSet.getString(4));
				usuario.setFechaCreacion(resultSet.getString(5));
				usuario.setActivo(resultSet.getString(6));
				usuario.setPais(resultSet.getInt(7));
				usuario.setTipoPersona(resultSet.getInt(8));
				usuario.setTipoIdentificacion(resultSet.getInt(9));
				usuario.setDireccion(resultSet.getString(10));
				usuario.setTelefonoOficina(resultSet.getObject(11).toString());
				if (resultSet.getString(12) != null) {
					usuario.setCelular(resultSet.getString(12));
				}
				usuario.setClave(resultSet.getString(13));
				if (resultSet.getString(14) != null) {
					usuario.setApellidoUno(resultSet.getString(14));
				}
				if (resultSet.getString(15) != null) {
					usuario.setApellidoDos(resultSet.getString(15));
				}
				if (resultSet.getString(16) != null) {
					usuario.setDepto(resultSet.getInt(16));
				}
				if (resultSet.getString(17) != null) {
					usuario.setMunicipio(resultSet.getInt(17));
				}
				if (resultSet.getString(18) != null) {
					usuario.setOrganizacion(resultSet.getString(18));
				}
				if (resultSet.getString(19) != null) {
					usuario.setCargo(resultSet.getString(19));
				}
			}

			resultSet.close();
			consultarUsuario.close();
			conn.close();
			log.info("[usuarioEnLogOnCorrecto] Termino");
		} catch (Exception e) {
			log.error("[usuarioEnLogOnCorrecto] Fallo");
			e.printStackTrace();
		}
		return usuario;
	}

	public Usuario consultarUsuario(int idPersona) {
		Usuario usuario = new Usuario();
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarUsuario = conn
					.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta_Id(?,?,?)}");
			consultarUsuario.setInt("un_Id", idPersona);
			consultarUsuario.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarUsuario.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarUsuario.execute();
			System.out.println(consultarUsuario.getObject("un_Mensaje"));
			ResultSet resultSet = (ResultSet) consultarUsuario
					.getObject("un_Resultado");

			while (resultSet.next()) {
				usuario.setIdUsuario(resultSet.getInt(1));
				usuario.setNombre(resultSet.getString(2));
				usuario.setIdentificacion(resultSet.getObject(3).toString());
				usuario.setDireccion(resultSet.getString(4));
				usuario.setTelefonoOficina(resultSet.getObject(5).toString());
				usuario.setCelular(resultSet.getObject(6).toString());
				usuario.setCorreoElectronico(resultSet.getString(7));
				// usuario.setFechaCreacion(new Date(((Timestamp) resultSet
				// .getObject(8)).getTime()));
				usuario.setActivo(resultSet.getString(9));
				usuario.setTipoIdentificacion(Integer.valueOf(resultSet
						.getObject(10).toString()));
				usuario.setTipoPersona(resultSet.getInt(11));
				usuario.setPais(resultSet.getInt(12));
				usuario.setClave(resultSet.getString(13));
				usuario.setApellidoUno(resultSet.getString(14));
				usuario.setApellidoDos(resultSet.getString(15));
				usuario.setDepto(resultSet.getInt(16));
				usuario.setMunicipio(resultSet.getInt(17));
				usuario.setOrganizacion(resultSet.getString(18));
				usuario.setCargo(resultSet.getString(19));
			}

			resultSet.close();
			consultarUsuario.close();
			conn.close();
			log.info("[consultarUsuario] Termino");
		} catch (Exception e) {
			log.error("[consultarUsuario] Fallo");
			e.printStackTrace();
		}
		return usuario;
	}

	public String consultarUltimoIngreso(Integer idPersona) {
		String ultimoRegistro = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarUltimoIngreso = conn
					.prepareCall("{call RED_PK_USUARIOS.UsuarioUltimoIngreso_Consulta(?,?)}");
			consultarUltimoIngreso.setInt("un_Id", idPersona);
			consultarUltimoIngreso.registerOutParameter("un_Resultado",
					OracleTypes.TIMESTAMP);
			consultarUltimoIngreso.execute();
			ultimoRegistro = consultarUltimoIngreso.getObject("un_Resultado") != null ? consultarUltimoIngreso
					.getObject("un_Resultado").toString() : "";
			ultimoRegistro = ultimoRegistro.length() > 0 ? ultimoRegistro
					.substring(0, ultimoRegistro.lastIndexOf(".")) : "";

			consultarUltimoIngreso.close();
			conn.close();
			log.info("[consultarUltimoIngreso] Termino");
		} catch (Exception e) {
			log.error("[consultarUltimoIngreso] Fallo");
			e.printStackTrace();
		}
		return ultimoRegistro;
	}

	public ArrayList<Object> consultarEstadisticasTiposPersona(String fechaIni,
			String fechaFin) {
		ArrayList<Object> estadistica = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarEstadisticas2 = conn
					.prepareCall("{call RED_PK_USUARIOS.UsuarioEstadistica2_Consulta(?,?,?,?)}");
			consultarEstadisticas2.setString("una_fechaini", fechaIni);
			consultarEstadisticas2.setString("una_fechafin", fechaFin);
			consultarEstadisticas2.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarEstadisticas2.registerOutParameter("sentencia1",
					OracleTypes.VARCHAR);
			consultarEstadisticas2.execute();

			ResultSet resultSet2 = (ResultSet) consultarEstadisticas2
					.getObject("un_Resultado");
			estadistica = new ArrayList<Object>();

			while (resultSet2.next()) {
				String[] datos = { resultSet2.getString(1),
						resultSet2.getString(2) };
				estadistica.add(datos);
			}
			resultSet2.close();
			consultarEstadisticas2.close();
			conn.close();
			log.info("[consultarEstadisticasTiposPersona] Termino");
		} catch (Exception e) {
			log.error("[consultarEstadisticasTiposPersona] Fallo");
			e.printStackTrace();
		}

		return estadistica;
	}

	public ArrayList<Object> consultarEstadisticasDepartamentos(
			String fechaIni, String fechaFin) {
		ArrayList<Object> estadistica = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarEstadisticas3 = conn
					.prepareCall("{call RED_PK_USUARIOS.UsuarioEstadisticaDeptos(?,?,?,?)}");
			consultarEstadisticas3.setString("una_fechaini", fechaIni);
			consultarEstadisticas3.setString("una_fechafin", fechaFin);
			consultarEstadisticas3.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarEstadisticas3.registerOutParameter("un_Mensaje",
					OracleTypes.VARCHAR);
			consultarEstadisticas3.execute();
			System.out.println(consultarEstadisticas3.getObject("un_Mensaje"));
			ResultSet resultSet3 = (ResultSet) consultarEstadisticas3
					.getObject("un_Resultado");
			estadistica = new ArrayList<Object>();

			while (resultSet3.next()) {
				String depto="";
				
				if(resultSet3.getString(2) != null && !resultSet3.getString(2).equals("0"))
				{
					depto = consultaNombreDepartamento(resultSet3.getInt(2));
				}else{
					depto = "No registrado";
				}
				
				String[] datos = { resultSet3.getString(1),depto };
				estadistica.add(datos);
			}
			resultSet3.close();
			consultarEstadisticas3.close();
			conn.close();
			
			log.info("[consultarEstadisticasDepartamentos] Termino");

		} catch (Exception e) {
			log.error("[consultarEstadisticasDepartamentos] Fallo");
			e.printStackTrace();
		}

		return estadistica;
	}
	
	public ArrayList<Integer> consultarEstadisticasTotales(
			String fechaIni, String fechaFin) {
		ArrayList<Integer> estadistica = new ArrayList<Integer>();
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarEstadisticas3 = conn
					.prepareCall("{call RED_PK_USUARIOS.UsuarioEstadisticaTotales(?,?,?,?)}");
			consultarEstadisticas3.setString("una_fechaini", fechaIni);
			consultarEstadisticas3.setString("una_fechafin", fechaFin);
			consultarEstadisticas3.registerOutParameter("UN_TOLTAL",
					OracleTypes.CURSOR);
			consultarEstadisticas3.registerOutParameter("UN_ToltalPub",
					OracleTypes.CURSOR);
			consultarEstadisticas3.execute();
			ResultSet resultSet3 = (ResultSet) consultarEstadisticas3
					.getObject("UN_TOLTAL");
			
			resultSet3.next();
			estadistica.add(resultSet3.getInt(1));
			
			resultSet3 = (ResultSet) consultarEstadisticas3
					.getObject("UN_ToltalPub");
			
			resultSet3.next();
			estadistica.add(resultSet3.getInt(1));

			resultSet3.close();
			consultarEstadisticas3.close();
			conn.close();
			log.info("[consultarEstadisticasTotales] Termino");

		} catch (Exception e) {
			log.error("[consultarEstadisticasTotales] Fallo");
			e.printStackTrace();
		}

		return estadistica;
	}

	public ArrayList<Object> consultarEstadisticaUsuariosXTipoPersona(
			String fechaIni, String fechaFin) {
		ArrayList<Object> estadistica = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultaXTipoPersona = conn
					.prepareCall("{call RED_PK_USUARIOS.UsuariosXEstadistica2_Consulta(?,?,?)}");
			consultaXTipoPersona.setString("una_fechaini", fechaIni);
			consultaXTipoPersona.setString("una_fechafin", fechaFin);
			consultaXTipoPersona.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaXTipoPersona.execute();

			ResultSet resultSet = (ResultSet) consultaXTipoPersona
					.getObject("un_Resultado");
			estadistica = new ArrayList<Object>();

			while (resultSet.next()) {
				String[] datos = { resultSet.getString(1),
						resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5),
						resultSet.getString(6) };
				estadistica.add(datos);
			}
			resultSet.close();
			consultaXTipoPersona.close();
			conn.close();
			log.info("[consultarEstadisticaUsuariosXTipoPersona] Termino");
		} catch (Exception e) {
			log.error("[consultarEstadisticaUsuariosXTipoPersona] Fallo");
			e.printStackTrace();
		}

		return estadistica;
	}

	public ArrayList<Object> consultarEstadisticaUsuariosXDepartamentos(
			String fechaIni, String fechaFin) {
		ArrayList<Object> estadistica = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultaXDepartamentos = conn
					.prepareCall("{call RED_PK_USUARIOS.UsuarioEstadisticaDeptos(?,?,?)}");
			consultaXDepartamentos.setString("una_fechaini", fechaIni);
			consultaXDepartamentos.setString("una_fechafin", fechaFin);
			consultaXDepartamentos.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaXDepartamentos.execute();

			ResultSet resultSet = (ResultSet) consultaXDepartamentos
					.getObject("un_Resultado");
			estadistica = new ArrayList<Object>();

			while (resultSet.next()) {
				String[] datos = { resultSet.getString(1),
						resultSet.getString(2), resultSet.getString(3),
						resultSet.getString(4), resultSet.getString(5),
						resultSet.getString(6) };
				estadistica.add(datos);
			}
			resultSet.close();
			consultaXDepartamentos.close();
			conn.close();
			log.info("[consultarEstadisticaUsuariosXDepartamentos] Termino");
		} catch (Exception e) {
			log.error("[consultarEstadisticaUsuariosXDepartamentos] Fallo");
			// try {
			// conn.rollback();
			// } catch (SQLException e1) {
			// e1.printStackTrace();
			// }
			e.printStackTrace();
		}

		return estadistica;
	}

	public Integer consultarIdRol(int idUsuario) {
		Integer result = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarIdRol = conn
					.prepareCall("{call RED_PK_USUARIOS.Consulta_IDRol(?,?)}");
			consultarIdRol.setInt("un_IdUsuario", idUsuario);
			consultarIdRol.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarIdRol.execute();

			ResultSet resultSet = (ResultSet) consultarIdRol
					.getObject("un_Resultado");

			while (resultSet.next()) {
				result = resultSet.getInt(1);
			}

			resultSet.close();
			consultarIdRol.close();
			conn.close();
			log.info("[consultarIdRol] Termino");
		} catch (Exception e) {
			log.error("[consultarIdRol] Fallo");
			e.printStackTrace();
		}
		return result;
	}

	public String consultaNombreTipoPersona(int idTipoPers) {
		String dato = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultaNombre = conn
					.prepareCall("{call RED_PK_TABLASTIPO.TipoPersona_idConsulta(?,?)}");
			consultaNombre.setInt("un_Tipo", idTipoPers);
			consultaNombre.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaNombre.execute();

			ResultSet resultSet = (ResultSet) consultaNombre
					.getObject("un_Resultado");

			while (resultSet.next()) {
				dato = resultSet.getString(2);
			}

			resultSet.close();
			consultaNombre.close();
			conn.close();
			log.info("[consultaNombreTipoPersona] Termino");
		} catch (Exception e) {
			log.error("[consultaNombreTipoPersona] Fallo");
			e.printStackTrace();
		}
		return dato;
	}

	public String consultaNombreTipoIdent(int idTipoIdent) {
		String dato = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultaNombre = conn
					.prepareCall("{call RED_PK_TABLASTIPO.TipoIdentificacion_IdConsulta(?,?)}");
			consultaNombre.setInt("un_Tipo", idTipoIdent);
			consultaNombre.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaNombre.execute();

			ResultSet resultSet = (ResultSet) consultaNombre
					.getObject("un_Resultado");

			while (resultSet.next()) {
				dato = resultSet.getString(2);
			}

			resultSet.close();
			consultaNombre.close();
			conn.close();
			log.info("[consultaNombreTipoIdent] Termino");
		} catch (Exception e) {
			log.error("[consultaNombreTipoIdent] Fallo");
			e.printStackTrace();
		}
		return dato;
	}

	public String consultaNombrePais(int idPais) {
		String dato = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultaNombre = conn
					.prepareCall("{call RED_PK_USUARIOS.PaisPersona_Consulta(?,?)}");
			consultaNombre.setInt("un_Pais", idPais);
			consultaNombre.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaNombre.execute();

			ResultSet resultSet = (ResultSet) consultaNombre
					.getObject("un_Resultado");

			while (resultSet.next()) {
				dato = resultSet.getString(2);
			}

			resultSet.close();
			consultaNombre.close();
			conn.close();
			log.info("[consultaNombrePais] Termino");
		} catch (Exception e) {
			log.error("[consultaNombrePais] Fallo");
			e.printStackTrace();
		}
		return dato;
	}

	public String consultaNombreDepartamento(int idDepto) {
		String dato = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultaNombre = conn
					.prepareCall("{call RED_PK_USUARIOS.DeptoPersona_Consulta(?,?)}");
			consultaNombre.setInt("un_depto", idDepto);
			consultaNombre.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaNombre.execute();

			ResultSet resultSet = (ResultSet) consultaNombre
					.getObject("un_Resultado");

			while (resultSet.next()) {
				dato = resultSet.getString(2);
			}

			resultSet.close();
			consultaNombre.close();
			conn.close();
			log.info("[consultaNombreDepartamento] Termino");
		} catch (Exception e) {
			log.error("[consultaNombreDepartamento] Fallo");
			e.printStackTrace();
		}
		return dato;
	}

	public String consultaNombreMunicipio(int idMuni) {
		String dato = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultaNombre = conn
					.prepareCall("{call RED_PK_USUARIOS.MunicipioPersona_Consulta(?,?)}");
			consultaNombre.setInt("un_municipio", idMuni);
			consultaNombre.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultaNombre.execute();

			ResultSet resultSet = (ResultSet) consultaNombre
					.getObject("un_Resultado");

			while (resultSet.next()) {
				dato = resultSet.getString(2);
			}

			resultSet.close();
			consultaNombre.close();
			conn.close();
			log.info("[consultaNombreMunicipio] Termino");
		} catch (Exception e) {
			log.error("[consultaNombreMunicipio] Fallo");
			e.printStackTrace();
		}
		return dato;
	}

	public String consultarNombresLicencias(int idPersona) {
		String dato = "";
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = conexion.establecerConexion();
			CallableStatement consultarUsuario = conn
					.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta_Licencias(?,?)}");
			consultarUsuario.setInt("un_Id", idPersona);
			consultarUsuario.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarUsuario.execute();
			ResultSet resultSet = (ResultSet) consultarUsuario
					.getObject("un_Resultado");

			while (resultSet.next()) {
				dato = dato + resultSet.getString(2);
				dato = dato + "\t\t";
			}

			resultSet.close();
			consultarUsuario.close();
			conn.close();
			log.info("[consultarLicenciasUsuario] Termino");
		} catch (Exception e) {
			log.error("[consultarLicenciasUsuario] Fallo");
			e.printStackTrace();
		}
		return dato;
	}

	public String getUn_Nombre() {
		return un_Nombre;
	}

	public void setUn_Nombre(String unNombre) {
		un_Nombre = unNombre;
	}

	public String getUn_ApellidoUno() {
		return un_ApellidoUno;
	}

	public void setUn_ApellidoUno(String unApellidoUno) {
		un_ApellidoUno = unApellidoUno;
	}

	public String getUna_FechaUltimoI() {
		return fechaUltimoI;
	}

	public void setUna_FechaUltimoI(String fecha) {
		fechaUltimoI = fecha;
	}

	public Integer getUn_TipoDocumento() {
		return un_TipoDocumento;
	}

	public void setUn_TipoDocumento(Integer unTipoDocumento) {
		un_TipoDocumento = unTipoDocumento;
	}

	public String getUn_Rol() {
		return un_Rol;
	}

	public void setUn_Rol(String unRol) {
		un_Rol = unRol;
	}

	public String getUn_CorreoElectronico() {
		return un_CorreoElectronico;
	}

	public void setUn_CorreoElectronico(String unCorreoElectronico) {
		un_CorreoElectronico = unCorreoElectronico;
	}

	public Double getUna_Identificacion() {
		return una_Identificacion;
	}

	public void setUna_Identificacion(Double unaIdentificacion) {
		una_Identificacion = unaIdentificacion;
	}

}
