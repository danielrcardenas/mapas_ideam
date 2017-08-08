// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.admin.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.imgusuarios.entities.ImagenUsuario;
import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.util.SMBC_Log;
import co.gov.ideamredd.web.admin.entities.LicenciaUso;

/**
 * clase que carga datos iniciales de formularios de licencias.
 */
public class ConsultarLicenciasUso {

	private static Logger log;
	private static Connection conn;
	private static DataSource dataSource = ConexionBD.getConnection();

	public static ArrayList<LicenciaUso> consultarLicencias() {
		ArrayList<LicenciaUso> licenciasUso = new ArrayList<LicenciaUso>();
		LicenciaUso licenciaUso=null;
		
		try {
			log = SMBC_Log.Log(ConsultarLicenciasUso.class);
			conn = dataSource.getConnection();
			CallableStatement consultarImagen = conn
					.prepareCall("{call RED_PK_USUARIOS.consultar_TodasLicencias(?)}");
			consultarImagen.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarImagen.execute();
			ResultSet resultSet = (ResultSet) consultarImagen
					.getObject("un_Resultado");

			while (resultSet.next()) {
				licenciaUso = new LicenciaUso();
				licenciaUso.setConsecutivo(resultSet.getInt("LCNC_CONSECUTIVO"));
				licenciaUso.setNombre(resultSet.getString("LCNC_NOMBRE"));
				licenciaUso.setLicencia(resultSet.getInt("LCNC_CONS_TIPOLICENCIA"));
				licenciaUso.setDescripcion(resultSet.getString("LCNC_DESCRIPCION").replace("<br />", ""));
				licenciaUso.setActiva(resultSet.getInt("LCNC_ACTIVA"));
				
				licenciasUso.add(licenciaUso);
			}

			resultSet.close();
			consultarImagen.close();
			conn.close();
			log.info("[consultarImagenes] Termino");
		} catch (Exception e) {
			log.error("[consultarImagenes] Fallo");
			e.printStackTrace();
		}

		return licenciasUso;
	}

}
