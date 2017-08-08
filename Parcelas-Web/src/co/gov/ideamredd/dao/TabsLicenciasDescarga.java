package co.gov.ideamredd.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexion.ConexionBD;

public class TabsLicenciasDescarga {
	private static DataSource dataSource = ConexionBD.getConnection();
	private static Connection conn;
	private static final String BUNDLE_NAME = "co/gov/ideamredd/dao/descargas";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String[] getLicenciasDescargaUsuarios(int idPersona,int tipoLicencia) {
		String licenciasDescarga[]={"",""};
		try {
			conn = dataSource.getConnection();
			CallableStatement licencias = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta_Licencias(?,?,?,?)}");
			licencias.setInt("un_Id", idPersona);
			licencias.setInt("un_TipoLicencia", tipoLicencia);
			licencias.registerOutParameter("un_Resultado",OracleTypes.CURSOR);
			licencias.registerOutParameter("un_Mensaje",OracleTypes.VARCHAR);
			licencias.execute();
			OracleResultSet r = (OracleResultSet) licencias.getObject("un_Resultado");
			while (r.next()) {
				if(tipoLicencia==1){
					licenciasDescarga[0]=licenciasDescarga[0]+"<li class=\"nav-two\"><a class=\"current\" href=\"#licencia"+r.getInt(1)+"\">Licencia de Uso "+r.getString(2)+"</a></li>";
					licenciasDescarga[1]=licenciasDescarga[1]+"<ul id=\"licencia"+r.getInt(1)+"\">"+
		        			"<li>"+
		        				"<div align=\"left\">"+
		        					"<fieldset style=\"padding: 10px;\">"+	        					
		        						"<table>"+
											"<tbody>"+
												"<tr>"+
													"<td><label>Permito que los datos de esta</br> parcela sean p&uacute;blicos?: </label></td>"+		
													"<td>" +
														"<select id=\"tipolicencia\" name=\"tipolicencia\">"+
															"<option value=\"0\">No</option>"+
															"<option value=\"1\">Si</option>"+
														"</select>"+
													"</td>"+
												"</tr>"+
												"<tr><td colspan=\"2\">&nbsp;</td></tr>"+
												"<tr>"+
													"<td><label>Permito que los datos de esta </br>parcela sean usados por el IDEAM </br>para el calculo de la biomasa?: </label></td>"+
													"<td>" +
														"<select id=\"usoinfo\" name=\"usoinfo\">"+
															"<option value=\"0\">No</option>"+
															"<option value=\"1\">Si</option>"+
														"</select>"+
													"</td>"+
												"</tr>"+
												"<tr><td colspan=\"2\">&nbsp;</td></tr>"+
												"<tr>"+
													"<td colspan=\"2\"><label>Licencia de uso de la informaci&oacute;n </label></td>"+														
												"</tr>"+											
									    		"<tr>"+
									    			"<td colspan=\"2\">"+
									    				"<div class=\"txtlicencia\">"+
															r.getString(3)+														
														"</div>"+													
													"</td>"+								    			
									    		"</tr>"+
												"<tr>"+
													"<td><label>�Acepta la licencia de uso de<br/> la informaci�n ingresada por usted?</label></td>"+
													"<td>"+
														"<input type=\"checkbox\" name=\"licencias\" id=\"licencias\" value=\""+r.getInt(1)+"\"/>"+
													"</td>"+
												"</tr>"+
								    		"</tbody>"+	
									"</table>"+
	        					"</fieldset>"+
	        				"</div>"+
	        			"</li>"+
	        		"</ul>";
				}else{					
					String[] publicas = getString("licencias").split(",");
					boolean publica = false;
					for(String eval:publicas){
						if(eval.equalsIgnoreCase(r.getString(2)))
							publica = true;
					}
					if(publica)
						licenciasDescarga[0]=licenciasDescarga[0]+"<li class=\"nav-two\">";
					else
						licenciasDescarga[0]=licenciasDescarga[0]+"<li class=\"nav-three\" style=\"display:none;\">";					
					licenciasDescarga[0]=licenciasDescarga[0]+"<a href=\"#licencia"+r.getInt(1)+"\">Licencia de Uso "+r.getString(2)+"</a></li>";
					licenciasDescarga[1]=licenciasDescarga[1]+"<ul id=\"licencia"+r.getInt(1)+"\" class=\"hide\">"+
		        			"<li>"+
		        				"<div align=\"left\">"+
		        					"<fieldset style=\"padding: 10px;\">"+	        					
		        						"<table>"+
											"<tbody>"+
												"<tr>"+
													"<td colspan=\"2\"><label>Licencia de uso de la informaci&oacute;n </label></td>"+														
												"</tr>"+											
									    		"<tr>"+
									    			"<td colspan=\"2\">"+
									    				"<div class=\"txtlicencia\">"+
															r.getString(3)+														
														"</div>"+													
													"</td>"+								    			
									    		"</tr>"+
												"<tr>"+
													"<td><label>�Acepta haber le&iacute;do el acuerdo<br/> de licenciamiento y est&aacute;<br/>de acuerdo con su contenido?</label></td>"+
													"<td>"+
														"<input type=\"checkbox\" name=\"licencias\" id=\"licencias\" value=\""+r.getInt(1)+"\"/>"+
													"</td>"+
												"</tr>"+
								    		"</tbody>"+	
									"</table>"+
	        					"</fieldset>"+
	        				"</div>"+
	        			"</li>"+
	        		"</ul>";
				}
			}
			r.close();
			licencias.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return licenciasDescarga;
	}

	public static String[] getLicenciasUsuario(int idPersona) {
		String licenciasDescarga[]={"",""};
		try {
			conn = dataSource.getConnection();
			CallableStatement licencias = conn.prepareCall("{call RED_PK_USUARIOS.Usuario_Consulta_Licencias(?,?,?)}");
			licencias.setInt("un_Id", idPersona);
			licencias.registerOutParameter("una_Cantidad",OracleTypes.NUMBER);
			licencias.registerOutParameter("un_Resultado",OracleTypes.CURSOR);			
			licencias.execute();
			OracleResultSet r = (OracleResultSet) licencias.getObject("un_Resultado");
			while (r.next()) {
				licenciasDescarga[0]=licenciasDescarga[0]+"<li class=\"nav-two\"><a class=\"current\" href=\"#licencia"+r.getString(1).substring(0,r.getString(1).indexOf("-"))+"\">Licencia de Uso "+r.getString(1).substring(r.getString(1).indexOf("-")+1)+"</a></li>";
				licenciasDescarga[1]=licenciasDescarga[1]+"<ul id=\"licencia"+r.getString(1).substring(0,r.getString(1).indexOf("-"))+"\" class=\"hide\">"+
	        			"<li>"+
	        				"<div align=\"left\">"+
	        					"<fieldset style=\"padding: 10px;\">"+	        					
	        						"<table>"+
										"<tbody>"+											
											"<tr>"+
												"<td colspan=\"2\"><label>Licencia de uso de la informaci&oacute;n </label></td>"+														
											"</tr>"+											
								    		"<tr>"+
								    			"<td colspan=\"2\">"+
								    				"<div class=\"txtlicencia\">"+
														r.getString(2)+														
													"</div>"+													
												"</td>"+								    			
								    		"</tr>"+
							    		"</tbody>"+	
								"</table>"+
        					"</fieldset>"+
        				"</div>"+
        			"</li>"+
        		"</ul>";
				
			}
			r.close();
			licencias.close();
			conn.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		return licenciasDescarga;
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
}
