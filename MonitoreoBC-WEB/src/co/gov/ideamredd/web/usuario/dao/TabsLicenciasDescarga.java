// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.web.usuario.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.mbc.conexion.ConexionBD;
import co.gov.ideamredd.mbc.conexion.ParametroNoBean;
import co.gov.ideamredd.usuario.entities.Licencia;
import co.gov.ideamredd.util.SMBC_Log;

public class TabsLicenciasDescarga {

	private static DataSource			dataSource		= ConexionBD.getConnection();
	private static Connection			conn;
	private static Logger				log;
	private static ParametroNoBean		parametro		= new ParametroNoBean();
	private static int					numeroLicencias;
	private static String				nombresLicencias;
	private static String				idsLicencias;
	private static ArrayList<Licencia>	listaLicencias	= new ArrayList<Licencia>();

	public static String getLicenciasDescargaUsuarios(int tipoLicencia, ResourceBundle msj) {
		String licenciasDescarga = "";
		nombresLicencias = "";
		idsLicencias = "";
		try {
			numeroLicencias = 0;
			log = SMBC_Log.Log(TabsLicenciasDescarga.class);
			dataSource = ConexionBD.getConnection();
			conn = dataSource.getConnection();

			ArrayList<String> licenciasPublicas = new ArrayList<String>();

			String[] listaLicenciasPub = parametro.getParametro("licencias.publicas").trim().split(",");

			for (int i = 0; i < listaLicenciasPub.length; i++) {
				licenciasPublicas.add(listaLicenciasPub[i]);
			}

			CallableStatement licencias = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta_Licencias(?,?,?,?)}");
			licencias.setInt("un_Id", 0);
			licencias.setInt("un_TipoLicencia", tipoLicencia);
			licencias.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			licencias.registerOutParameter("un_Mensaje", OracleTypes.VARCHAR);
			licencias.execute();
			ResultSet r = (ResultSet) licencias.getObject("un_Resultado");
			listaLicencias.clear();
			Licencia lic;

			while (r.next()) {
				if (licenciasPublicas.contains(r.getString(2))) {
					numeroLicencias = numeroLicencias + 1;
					lic = new Licencia();
					lic.setConsecutivo(r.getInt(1));
					lic.setNombre(r.getString(2));
					lic.setTipo(r.getInt(3));
					lic.setDescripcion(r.getString(4));

					nombresLicencias = nombresLicencias + r.getString(2) + ",";
					idsLicencias = idsLicencias + r.getString(1) + ",";

					listaLicencias.add(lic);
				}
			}

			for (int i = 0; i < listaLicencias.size(); i++) {

				licenciasDescarga = licenciasDescarga + "<h3>" + msj.getString("registroUsuario.licenciaUso") + ": " + listaLicencias.get(i).getNombre() + "</h3><div>" + msj.getString("registroUsuario.infoLicUso") + "<br>" + "<p>" + listaLicencias.get(i).getDescripcion() + "<p>" + "<p>" + msj.getString("registroUsuario.acuerdoLic") + ":</p><p><input type=\"button\" " + "class=\"btn btn-default btn-descargar\" " + "onclick=\"descargarLic('" + listaLicencias.get(i).getNombre() + "')\" value=\"" + msj
						.getString("registroUsuario.descargar") + "\"></p>" + "<p><input type=\"checkbox\" onclick=\"\" id=\"licCheck" + listaLicencias.get(i).getConsecutivo() + "\"" + "name=\"licCheck" + listaLicencias.get(i).getConsecutivo() + "\" value=\"" + listaLicencias.get(i).getConsecutivo() + "\">" + msj.getString("registroUsuario.mensAcepta") + "</p>" + "</div>";
			}

			log.info("[getLicenciasDescargaUsuarios] Termino");
			r.close();
			licencias.close();
			conn.close();
		}
		catch (Exception e) {
			log.info("[getLicenciasDescargaUsuarios] Fallo");
			e.printStackTrace();
		}

		return licenciasDescarga;
	}

	public static String getFormDescargaLic() {
		String dato = "";
		dato = dato + "<form action=\"/MonitoreoBC-WEB/descargaLicenciaServlet\"" + "method=\"post\" id=\"formDescLicencia\">" + "<input type=\"hidden\" id=\"licenciaDescarga\"" + "name=\"licenciaDescarga\">" + "</form>";

		return dato;
	}

	public static String[] getLicenciasUsuario(int idPersona) {
		String licenciasDescarga[] = { "", "" };
		try {
			log = SMBC_Log.Log(TabsLicenciasDescarga.class);
			dataSource = ConexionBD.getConnection();
			conn = dataSource.getConnection();
			CallableStatement licencias = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta_Licencias(?,?,?)}");
			licencias.setInt("un_Id", idPersona);
			licencias.registerOutParameter("una_Cantidad", OracleTypes.NUMBER);
			licencias.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			licencias.execute();
			ResultSet r = (ResultSet) licencias.getObject("un_Resultado");
			while (r.next()) {
				licenciasDescarga[0] = licenciasDescarga[0] + "<li class=\"nav-two\"><a class=\"current\" href=\"#licencia" + r.getString(1).substring(0, r.getString(1).indexOf("-")) + "\">Licencia de Uso " + r.getString(1).substring(r.getString(1).indexOf("-") + 1) + "</a></li>";
				licenciasDescarga[1] = licenciasDescarga[1] + "<ul id=\"licencia" + r.getString(1).substring(0, r.getString(1).indexOf("-")) + "\" class=\"hide\">" + "<li>" + "<div align=\"left\">" + "<fieldset style=\"padding: 10px;\">" + "<table>" + "<tbody>" + "<tr>" + "<td colspan=\"2\"><label>Informaci&oacute;n </label></td>" + "</tr>" + "<tr>" + "<td colspan=\"2\">" + "<div class=\"txtlicencia\">" + r.getString(2) + "</div>" + "</td>" + "</tr>" + "</tbody>" + "</table>" + "</fieldset>" + "</div>" + "</li>" + "</ul>";
			}
			log.info("[getLicenciasUsuario] Termino");
			r.close();
			licencias.close();
			conn.close();
		}
		catch (Exception e) {
			log.info("[getLicenciasUsuario] Fallo");
			e.printStackTrace();
		}

		return licenciasDescarga;
	}

	public static String getString(String key) {
		try {
			return parametro.getParametro(key);
		}
		catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static int getNumeroLicencias() {
		return numeroLicencias;
	}

	public static String getNombresLicencias() {
		return nombresLicencias;
	}

	public static String getIdsLicencias() {
		return idsLicencias;
	}

	public static String[] getIdsArrayLicencias() {
		return idsLicencias.split(",");
	}

}
