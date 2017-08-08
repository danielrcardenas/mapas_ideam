package co.gov.ideamredd.ui.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.dao.ConsultaUsuario;
import co.gov.ideamredd.entities.Usuario;
import co.gov.ideamredd.util.SMBC_Log;

public class ConsultaWebUsuario {
	
	private static DataSource dataSource = ConexionBD.getConnection();
	private static Connection conn;
	private static Logger log;
	
	public static Usuario consultarUsuarioPorDoc(int docPersona) {
		Usuario usuario = new Usuario();
		try {
			log = SMBC_Log.Log(ConsultaWebUsuario.class);
			conn =  dataSource.getConnection();
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
				if(resultSet.getString(12)!=null)
				{
				usuario.setCelular(resultSet.getString(12));
				}
				usuario.setClave(resultSet.getString(13));
				if(resultSet.getString(14)!=null)
				{
				usuario.setApellidoUno(resultSet.getString(14));
				}
				if(resultSet.getString(15)!=null)
				{
				usuario.setApellidoDos(resultSet.getString(15));
				}
				if(resultSet.getString(16)!=null)
				{
				usuario.setDepto(resultSet.getInt(16));
				}
				if(resultSet.getString(17)!=null)
				{
				usuario.setMunicipio(resultSet.getInt(17));
				}
				if(resultSet.getString(18)!=null)
				{
				usuario.setOrganizacion(resultSet.getString(18));
				}
				if(resultSet.getString(19)!=null)
				{
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
	
	public static String consultarUltimoIngreso(Integer idPersona) {
		String ultimoRegistro = null;
		try {
			log = SMBC_Log.Log(ConsultaUsuario.class);
			conn = dataSource.getConnection();
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
	
	public static ArrayList<Integer> consultarLicenciasUsuario(int idPersona) {
		ArrayList<Integer> licencias = new ArrayList<Integer>();
		try {
			log = SMBC_Log.Log(ConsultaWebUsuario.class);
			conn =  dataSource.getConnection();
			CallableStatement consultarUsuario = conn
					.prepareCall("{call RED_PK_USUARIOS.CONSULTA_LICENCIAS(?,?)}");
			consultarUsuario.setInt("un_Id", idPersona);
			consultarUsuario.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarUsuario.execute();
			ResultSet resultSet = (ResultSet) consultarUsuario
					.getObject("un_Resultado");

			while (resultSet.next()) {
				licencias.add(resultSet.getInt(3));
			}

			resultSet.close();
			consultarUsuario.close();
			conn.close();
			log.info("[consultarLicenciasUsuario] Termino");
		} catch (Exception e) {
			log.error("[consultarLicenciasUsuario] Fallo");
			e.printStackTrace();
		}
		return licencias;
	}

}
