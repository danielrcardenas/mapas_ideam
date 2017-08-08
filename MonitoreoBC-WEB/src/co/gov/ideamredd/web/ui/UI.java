// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Maneja contenido de la interfaz de usuario
 */
package co.gov.ideamredd.web.ui;

import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;

import co.gov.ideamredd.lenguaje.LenguajeI18N;
import co.gov.ideamredd.mbc.auxiliares.Auxiliar;
import co.gov.ideamredd.mbc.conexion.ParametroNoBean;
import co.gov.ideamredd.mbc.dao.ControlPermisos;
import co.gov.ideamredd.util.Util;
import co.gov.ideamredd.mbc.entities.Noticias;
import co.gov.ideamredd.mbc.dao.CargaNoticiasYEventos;

/**
 * Métodos para centralizar el contenido de las páginas
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class UI {
	
	/**
	 * Retorna el header
	 * 
	 * @param usuario
	 * @param sesion
	 * @param msj
	 * @param diccionarioPermisos
	 * @param i18n
	 * @param uri
	 * @return html de header
	 */
	public static String getHeader(co.gov.ideamredd.util.entities.Usuario usuario, HttpSession sesion, ResourceBundle msj, Map<Integer, String> diccionarioPermisos, LenguajeI18N i18n, String uri) {
		String html = "";

		html += "\n"+"<div id='header'>";
		html += "\n"+"	<div id='header-first' class='section-wrapper'>";
		html += "\n"+"		<div class='section banner' style='background: url(\"../img/imagen_banner2_"+i18n.getLenguaje()+".png\") repeat-x 0 0 transparent;'>";
		html += "\n"+"		<div class='enlace_index' onclick='location.href=\"/MonitoreoBC-WEB\";'></div>";
		html += "\n"+"			<div class='section-inner clearfix' style='clear:both;'>";
//		html += "\n"+"				<div id='block-logo' class='block logo-smbyc'>";
//		html += "\n"+"					<div class='content'>";
//		html += "\n"+"						<a href='/MonitoreoBC-WEB/index.jsp'><img src='../img/logo_"+i18n.getLenguaje()+".png' alt=''></a>";
//		html += "\n"+"					</div>";
//		html += "\n"+"				</div>";
		html += "\n"+"				<div class='logos-y-login'>";
//		html += "\n"+"					<div id='block-images-header'>";
//		html += "\n"+"						<div class='content'>";
//		html += "\n"+"							<a href='http://www.minambiente.gov.co/'>";
//		html += "\n"+"								<img class='logo-header' src='../img/img-min.png' alt=''>";
//		html += "\n"+"							</a>";
//		html += "\n"+"						</div>";
//		html += "\n"+"					</div>";
		if (usuario != null) {
			html += "\n"+"				<div id='block-top-menu' class='block block-menu'>";
			html += "\n"+"					<div id='content-login-form' class='content'>";
			html += "\n"+"						<div id='form-loguin-header' role='form'>";
			html += "\n"+"							<div class='form-group'>";
			html += "\n"+"								<label for='exampleInputEmail1'>" + msj.getString("home.registrado.bienvenido") + "</label>";
			html += "\n"+"							</div>";
			html += "\n"+"							<div class='form-group'>";
			html += "\n"+"								<label for='exampleInputPassword1'>" + usuario.getNombre() + "</label>";
			html += "\n"+"							</div>";
			html += "\n"+"							<div class='form-group'>";
//			html += "\n"+"								<label for='exampleInputEmail1'><a href='/MonitoreoBC-WEB/limpiarSesionServlet'>" + msj.getString("home.registrado.cerrar") + "</a></label>";
//			html += "\n"+"								<label for='exampleInputEmail1'><a href='/MonitoreoBC-WEB/logout.jsp'>" + msj.getString("home.registrado.cerrar") + "</a></label>";
			html += "\n"+"								<label for='exampleInputEmail1'><a href='#' onclick=\"javascript:window.top.location='/MonitoreoBC-WEB/logout.jsp'\">" + msj.getString("home.registrado.cerrar") + "</a></label>";
			html += "\n"+"							</div>";
			html += "\n"+"						</div>";
			html += "\n"+"							<ul class='social-menu item-list'>";
			html += "\n"+"								<li class='menu-item en'><a onclick=\"lenguaje(2, '"+uri+"');\"></a></li>";
			html += "\n"+"								<li class='menu-item es'><a onclick=\"lenguaje(1, '"+uri+"');\"></a></li>";
			html += "\n"+"							</ul>";
			html += "\n"+"						</div>";
			html += "\n"+"					</div>";
		}
		else {
			html += "\n"+"					<div id='block-top-menu' class='block block-menu'>";
			html += "\n"+"						<div id='content-login-form' class='content'>";
			html += "\n"+"							<div id='form-loguin-header' role='form'>";
			html += "\n"+"								<div class='form-group'>";
			html += "\n"+"									<label for='exampleInputEmail1'>" + msj.getString("home.usuario") + "</label>";
			html += "\n"+"									<input type='text' class='form-control' id='logName' name='logName' placeholder=''>";
			html += "\n"+"								</div>";
			html += "\n"+"								<div class='form-group'>";
			html += "\n"+"									<label for='exampleInputPassword1'>" + msj.getString("home.pass") + "</label>";
			html += "\n"+"									<input type='password' class='form-control' id='logPassword' name='logPassword' placeholder=''>";
			html += "\n"+"								</div>";
			html += "\n"+"								<div class='form-actions'>";
			html += "\n"+"									<input type='button' class='btn btn-default' value='" + msj.getString("home.ir") + "' onclick='enviarForms()'></input>";
			html += "\n"+"								</div>";
			html += "\n"+"							</div>";
			html += "\n"+"							<ul class='social-menu item-list'>";
			html += "\n"+"								<li id='icoAyuda' class='menu-item help first' onclick='popUpAyudaOpen()' style='margin-right: 4px; cursor: pointer;'><a></a></li>";
			html += "\n"+"								<li class='menu-item en' style='cursor: pointer;'><a onclick=\"lenguaje(2, '"+uri+"');\"></a></li>";
			html += "\n"+"								<li class='menu-item es' style='cursor: pointer;'><a onclick=\"lenguaje(1, '"+uri+"');\"></a></li>";
			html += "\n"+"							</ul>";
			html += "\n"+"						</div>";
			html += "\n"+"					</div>";
		}
		html += "\n"+"				</div>";
		html += "\n"+"			</div>";
		html += "\n"+"		</div>";
		html += "\n"+"	</div>";
		html += getMenu(usuario, sesion, msj, diccionarioPermisos, i18n);
		html += "\n"+"</div>";

		return html;
	}
	
	/**
	 * Retorna el header SIN login
	 * 
	 * @param usuario
	 * @param sesion
	 * @param msj
	 * @param diccionarioPermisos
	 * @param i18n
	 * @param uri
	 * @return html de header
	 */
	public static String getHeaderSinLogin(co.gov.ideamredd.util.entities.Usuario usuario, HttpSession sesion, ResourceBundle msj, Map<Integer, String> diccionarioPermisos, LenguajeI18N i18n, String uri) {
		String html = "";
		
		html += "\n"+"<div id='header'>";
		html += "\n"+"	<div id='header-first' class='section-wrapper'>";
		html += "\n"+"		<div class='section banner' style='background: url(\"../img/imagen_banner2_"+i18n.getLenguaje()+".png\") repeat-x 0 0 transparent;'>";
		html += "\n"+"			<div class='enlace_index' onclick='location.href=\"/MonitoreoBC-WEB\";'></div>";
		html += "\n"+"			<div class='section-inner clearfix' style='clear:both;'></div>";
		html += "\n"+"		</div>";
		html += "\n"+"	</div>";
		html += getMenu(usuario, sesion, msj, diccionarioPermisos, i18n);
		html += "\n"+"</div>";
		
		return html;
	}
	
	/**
	 * Retorna el índice principal
	 * 
	 * @param usuario
	 * @param sesion
	 * @param msj
	 * @param diccionarioPermisos
	 * @param i18n
	 * @return html del índice principal
	 */
	public static String getMainIndex(co.gov.ideamredd.util.entities.Usuario usuario, HttpSession sesion, ResourceBundle msj, Map<Integer, String> diccionarioPermisos, LenguajeI18N i18n) {
		ParametroNoBean parametro;
		parametro = new ParametroNoBean();

		String html = "";
		
		html += "\n"+"<div id='main' class='wrapper'>";
		html += "\n"+"<div id='main-inner' class='section-wrapper'>";
		html += "\n"+"	<div class='section'>";
		html += "\n"+"		<div class='section-inner clearfix'>";
		html += "\n"+"			<div id='content'>";
		html += "\n"+"					<div id='block-video' class='block'>";
		html += "\n"+"						<iframe width='50%' height='417px' src='//www.youtube.com/embed/DhhmVENqKv4?version=3&loop=1&playlist=DhhmVENqKv4' frameborder='0' allowfullscreen></iframe>";
		html += "\n"+"					</div>";
		html += "\n"+"					<div id='bloques' class='block-container'>";
		html += "\n"+"						<div id='block-bosques' class='block-vermas'>";
		html += "\n"+"							<div class='content'>";
		html += "\n"+"								<h2 class='titulo-vermas'>" + msj.getString("home.bosque")+ "</h2>";
		html += "\n"+"								<img class='img-vermas' src='/MonitoreoBC-WEB/img/Bosque.jpg'>";
		html += "\n"+"								<p class='p_vermas'>" + Auxiliar.nzObjStr(parametro.getParametro("contenido_nota1_"+i18n.getLenguaje()), msj.getString("index.hecho1")) + "</p>";
		html += "\n"+"								<div class='ver-mas'>";
		html += "\n"+"									<a href='/MonitoreoBC-WEB/pub/consultarReporteGeoproceso.jsp?tipoReporte=1'>" + msj.getString("home.verMas") + "</a>";
		html += "\n"+"								</div>";
		html += "\n"+"							</div>";
		html += "\n"+"						</div>";
		//html += "\n"+"						<div id='block-carbono' class='block-vermas'>";
		//html += "\n"+"							<div class='content'>";
		//html += "\n"+"								<h2 class='titulo-vermas'>" + msj.getString("home.carbono")+ "</h2>";
		//html += "\n"+"								<img class='img-vermas' src='/MonitoreoBC-WEB/img/Carbono.jpg'>";
		//html += "\n"+"								<p class='p_vermas'>" + Auxiliar.nzObjStr(parametro.getParametro("contenido_nota2_"+i18n.getLenguaje()), msj.getString("index.hecho2")) + "</p>";
		//html += "\n"+"								<div class='ver-mas'>";
		//html += "\n"+"									<a href='/MonitoreoBC-WEB/pub/consultarReporteGeoproceso.jsp?tipoReporte=7'>" + msj.getString("home.verMas") + "</a>";
		//html += "\n"+"								</div>";
		//html += "\n"+"							</div>";
		//html += "\n"+"						</div>";
		html += "\n"+"						<div id='block-alertas' class='block-vermas'>";
		html += "\n"+"							<div class='content'>";
		html += "\n"+"								<h2 class='titulo-vermas'>" + msj.getString("home.bosques.alertas") + "</h2>";
		html += "\n"+"								<img class='img-vermas' src='/MonitoreoBC-WEB/img/Alertas.jpg'>";
		html += "\n"+"								<p class='p_vermas'>" + Auxiliar.nzObjStr(parametro.getParametro("contenido_nota3_"+i18n.getLenguaje()), msj.getString("index.hecho3")) + "</p>";
		html += "\n"+"								<div class='ver-mas'>";
		html += "\n"+"									<a href='/MonitoreoBC-WEB/pub/alertasDeforestacion.jsp'>" + msj.getString("home.verMas") + "</a>";
		html += "\n"+"								</div>";
		html += "\n"+"							</div>";
		html += "\n"+"						</div>";
		html += "\n"+"						<div id='block-propuesta' class='block-vermas'>";
		html += "\n"+"							<div class='content'>";
		html += "\n"+"								<h2 class='titulo-vermas'>" + msj.getString("index.titulo_recomendacion") + "</h2>";
		html += "\n"+"								<img class='img-vermas' src='/MonitoreoBC-WEB/img/Visor.png'>";
		html += "\n"+"								<p class='p_vermas'>" + Auxiliar.nzObjStr(parametro.getParametro("contenido_nota4_"+i18n.getLenguaje()), msj.getString("index.nref")) + "</p>";
		html += "\n"+"								<div class='ver-mas'>";
		html += "\n"+"									<a href='/MonitoreoBC-WEB/extra/propReferencia.jsp'>" + msj.getString("home.verMas") + "</a>";
		html += "\n"+"								</div>";
		html += "\n"+"							</div>";
		html += "\n"+"						</div>";
		html += "\n"+"					</div>";
		html += "\n"+"					<div id='bloques-noticias-y-eventos' class='block-container'>";
		html += "\n"+"						" + getEventos(msj, true).replace("../img", "img")+ "";
		html += "\n"+"						" + getNoticias(msj, true).replace("../img", "img")+ "									";
		html += "\n"+"					</div>";
		html += "\n"+"					";
		html += "\n"+"			</div>";
		html += "\n"+"			<!-- /.sidebar-wrapper-->";
		html += "\n"+"		</div>";
		html += "\n"+"		<!-- /.section-inner-->";
		html += "\n"+"	</div>";
		html += "\n"+"	<!--/.section -->";
		html += "\n"+"</div>";
		html += "\n"+"<!-- /.section-wrapper-->";
		html += "\n"+"";
		html += "\n"+"</div>";
		html += "\n"+"<!--/.main -->";
		
		return html;
	}

	/**
	 * Retorna el menú
	 * 
	 * @param usuario
	 * @param sesion
	 * @param msj
	 * @param diccionarioPermisos
	 * @param i18n
	 * @return html del menu
	 */
	public static String getMenu(co.gov.ideamredd.util.entities.Usuario usuario, HttpSession sesion, ResourceBundle msj, Map<Integer, String> diccionarioPermisos, LenguajeI18N i18n) {

		String html = "";

		html += "\n"+"<div id='header-second' class='section-wrapper'>";
//		html += "\n"+"	<div class='section'>";
		html += "\n"+"		<div class='section-inner clearfix'>";
		html += "\n"+"			<div id='block-main-menu' class='block-menu'>";
		html += "\n"+"				<div class='content'>";
		html += "\n"+"					<ul class='main-menu item-list'>";

		html += "\n"+"						<li class='menu-item contact-us'><a href='/MonitoreoBC-WEB/pub/elBosqueEnCifras.jsp'>" + msj.getString("home.bosqueCifras") + "</a></li>";

		html += "\n"+"						<li class='menu-item about-us expanded'><a>" + msj.getString("home.bosque") + "</a>";
		html += "\n"+"							<ul class='menu'>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/pub/consultarReporteBosques.jsp'>" + msj.getString("home.bosques.cuantificacion") + "</a></li>";
		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/pub/consultarReporteGeoproceso.jsp?tipoReporte=1'>" + msj.getString("home.bosques.bosquenobosque") + "</a></li>";
		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/pub/consultarReporteGeoproceso.jsp?tipoReporte=3'>" + msj.getString("home.bosques.cambiocobertura") + "</a></li>";
		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/pub/consultarReporteGeoproceso.jsp?tipoReporte=5'>" + msj.getString("home.bosques.deforestacion") + "</a></li>";
		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/pub/alertasDeforestacion.jsp'>" + msj.getString("home.bosques.alertas") + "</a></li>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/indicadores/reporteInventarios.jsp'>Reporte Inventarios</a></li>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/indicadores/reporteInventariosParcela.jsp'>Reporte Inventarios de Parcela</a></li>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/indicadores/calculoEstructuraComposicion.jsp'>Calculo de estructura y composicion</a></li>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/indicadores/consultaIndicadoresSimilitud.jsp'>Consulta Indicadores de Similitud</a></li>";
//	//	if (usuario != null) {
	//		html += "\n"+"							<li class='leaf'><a href='/AdmIF/Parcela?accion=busqueda_parcelas&usuario=" + Util.encriptar(usuario.getIdentificacion()) + "&idioma=" + i18n.getLenguaje() + "'>Inventarios forestales</a></li>";
	//	}
		html += "\n"+"							</ul>";
		html += "\n"+"						</li>";
		
		
		
//		html += "\n"+"						<li class='menu-item about-us expanded'><a>Inventarios Forestales</a>";
//		html += "\n"+"							<ul class='menu'>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/indicadores/reporteInventarios.jsp'>Reporte Inventarios</a></li>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/indicadores/reporteInventariosParcela.jsp'>Reporte Inventarios de Parcela</a></li>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/indicadores/calculoEstructuraComposicion.jsp'>Calculo de estructura y composicion</a></li>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/indicadores/consultaIndicadoresSimilitud.jsp'>Consulta Indicadores de Similitud</a></li>";
//	
//		if (usuario != null) {
//			html += "\n"+"							<li class='leaf'><a href='/AdmIF/Parcela?accion=busqueda_parcelas&usuario=" + Util.encriptar(usuario.getIdentificacion()) + "&idioma=" + i18n.getLenguaje() + "'>Inventarios forestales</a></li>";
//		}
//
//		html += "\n"+"							</ul>";
//		html += "\n"+"						</li>";
		
		
		
//		html += "\n"+"						<li class='menu-item home expanded'><a>" + msj.getString("home.carbono") + "</a>";
//		html += "\n"+"							<ul class='menu'>";
//		html += "\n"+"								<li class='first leaf'><a href='/MonitoreoBC-WEB/proyectos/consultaProyecto.jsp'>" + msj.getString("home.carbono.actividades") + "</a></li>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/proyectos/reportesProyectos.jsp'>" + msj.getString("reportes.proyectos.link") + "</a></li>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/pub/consultarReporteGeoproceso.jsp?tipoReporte=7'>" + msj.getString("home.carbono.reportes") + "</a></li>";
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/extra/aportesTecnicos.jsp'>" + msj.getString("aportes_tecnicos_smbyc") + "</a></li>";
//		html += "\n"+"							</ul>";
//		html += "\n"+"						</li>";

	

//		html += "\n"+"						<li class='menu-item about-us expanded'><a>" + msj.getString("home.noticiasEventos") + "</a>";
//		html += "\n"+"							<ul class='menu'>";
//		html += "\n"+"								<li class='menu-item noticia'><a href='/MonitoreoBC-WEB/pub/consultaNoticias.jsp'>" + msj.getString("home.noticias") + "</a></li>";
//		html += "\n"+"								<li class='menu-item noticia'><a href='/MonitoreoBC-WEB/pub/consultaEventos.jsp'>" + msj.getString("home.eventos") + "</a></li>";
//
//		if (usuario != null) if (ControlPermisos.tienePermiso(diccionarioPermisos, 141)) {
//			html += "\n"+"							<li class='menu-item noticia'><a href='/MonitoreoBC-WEB/noticias/crearNoticiasEventos.jsp'>Crear Noticias y Eventos</a></li>";
//		}
//
//		html += "\n"+"							</ul>";
//		html += "\n"+"						</li>";
		
		
	

		html += "\n"+"						<li class='menu-item about-us expanded'><a>" + msj.getString("home.links") + "</a>";
		html += "\n"+"							<ul class='menu'>";
		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/extra/documentacion.jsp' >" + msj.getString("home.documentacion") + "</a></li>";
		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/extra/presentacion.jsp'>" + msj.getString("pagina.Presentacion") + "</a></li>";
	//	html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/extra/aspectosGenerales.jsp' >" + msj.getString("pagina.AGenerales") + "</a></li>";
	//	html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/extra/normatividad.jsp' >" + msj.getString("pagina.Normatividad") + "</a></li>";
		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/extra/enlacesRel.jsp' >" + msj.getString("pagina.SitiosInteres") + "</a></li>";
		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/extra/protocolos.jsp' >" + msj.getString("pagina.Protocolos") + "</a></li>";
		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/extra/glosario.jsp' >" + msj.getString("pagina.Glosario") + "</a></li>";
	//  html += "\n"+"			                    <li class='leaf'><a href='/MonitoreoBC-WEB/extra/mapaDelSitio.jsp' >" + msj.getString("pagina.mapaSitio") + "</a></li>";		
//		html += "\n"+"								<li class='leaf'><a href='/MonitoreoBC-WEB/pub/bosqueEnCifras.jsp'>" + msj.getString("home.bosqueCifras") + "</a></li>";
		html += "\n"+"							</ul>";
		html += "\n"+"						</li>";
		
		
		

		
		html += "\n"+"						<li class='menu-item work-oferts expanded'><a>" + msj.getString("home.visor") + "</a>";
		html += "\n"+"							<ul class='menu'>";
		html += "\n"+"								<li class='first leaf'><a href='http://seinekan.ideam.gov.co/SMBYC/ApolloPro.aspx' target='_blank'>"+msj.getString("etiqueta_link_visor_apollo")+"</a></li>";
		html += "\n"+"								<li class='first leaf'><a href='http://visor.ideam.gov.co:8530/geovisor/#!/profiles/5/no' target='_blank'>"+msj.getString("etiqueta_link_visor_ideam")+"</a></li>";
		//html += "\n"+"								<li class='leaf'><a href='/AdmIF' target='_blank'>"+msj.getString("etiqueta_link_visor_parcelas")+"</a></li>";
		// html += "\n"+"								<li class='leaf'><a href='"+basePath2+"MonitoreoBC-WEB/imagenesUsuarios.jsp'>Visor de imagenes de usuario</a></li>";
		html += "\n"+"							</ul>";
		html += "\n"+"						</li>";

		if (usuario != null) {
			html += "\n"+"					<li class='menu-item work-oferts expanded'><a>" + msj.getString("menu_usuario") + "</a>";
			html += "\n"+"						<ul class='menu'>";

			html += "\n"+"							<li class='first leaf'><a href='/MonitoreoBC-WEB/reg/modificarUsuario.jsp'>" + msj.getString("datos_personales") + "</a></li>";

			// if (ControlPermisos.tienePermiso(diccionarioPermisos, 107)) {
			// html += "\n"+"						<li class='leaf'><a href='"+basePath2+"MonitoreoBC-WEB/reg/imagenUsuario.jsp'>Registrar Imagen de Usuario</a></li>";
			// }
			//
			// if (ControlPermisos.tienePermiso(diccionarioPermisos, 108)) {
			// html += "\n"+"						<li class='leaf'><a href='"+basePath2+"MonitoreoBC-WEB/imageus/consultarImagenesUsuarios.jsp'>Consultar Imagenes de Usuarios</a></li>";
			// }
			//
			// if (ControlPermisos.tienePermiso(diccionarioPermisos, 84)) {
			// html += "\n"+"						<li class='leaf'><a href='"+basePath2+"MonitoreoBC-WEB/admin/registraLicenciaUso.jsp'>Registrar Licencia de Uso</a></li>";
			// }

			if (ControlPermisos.tienePermiso(diccionarioPermisos, 85)) {
				html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/admin/consultaLicenciasUso.jsp'>" + msj.getString("consultar_licencias_de_uso") + "</a></li>";
			}

			if (ControlPermisos.tienePermiso(diccionarioPermisos, 80)) {
				html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/admin/registraRol.jsp'>" + msj.getString("registrar_rol") + "</a></li>";
			}

			if (ControlPermisos.tienePermiso(diccionarioPermisos, 79)) {
				html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/admin/consultaRoles.jsp'>" + msj.getString("consultar_roles") + "</a></li>";
			}

			if (ControlPermisos.tienePermiso(diccionarioPermisos, 71)) {
				html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/admin/consultarUsuarios.jsp'>" + msj.getString("Consultar_Usuarios") + "</a></li>";
			}

			if (ControlPermisos.tienePermiso(diccionarioPermisos, 143)) {
				html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/admin/generarReportesUsuarios.jsp'>" + msj.getString("Generar_Reporte_de_Usuarios") + "</a></li>";
			}

			if (ControlPermisos.tienePermiso(diccionarioPermisos, 162)) {
				html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/admin/cargarDatosAlertasTempranas.jsp'>" + msj.getString("Cargar_Datos_Alertas_Tempranas") + "</a></li>";
			}

			if (ControlPermisos.tienePermiso(diccionarioPermisos, 75)) {
				html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/descarga/ConsultarDescargaImagenes.jsp'>" + msj.getString("Descargas_de_Usuarios") + "</a></li>";
			}

			//if (ControlPermisos.tienePermiso(diccionarioPermisos, 144)) {
			//	html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/descarga/ConsultarInventarioImagenes.jsp'>" + msj.getString("Inventario_de_Imagenes") + "</a></li>";
			//}

			if (ControlPermisos.tienePermiso(diccionarioPermisos, 261)) {
				//html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/admin/consultaReportes.jsp'>" + msj.getString("Administrar_Reportes") + "</a></li>";
				html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/admin/reportes.jsp'>" + msj.getString("Administrar_Reportes") + "</a></li>";
			}

			if (ControlPermisos.tienePermiso(diccionarioPermisos, 99)) {
				html += "\n"+"					    <li class='menu-item noticia'><a href='/MonitoreoBC-WEB/pub/consultaNoticias.jsp'>" + msj.getString("home.noticias") + "</a></li>";
				html += "\n"+"						<li class='menu-item noticia'><a href='/MonitoreoBC-WEB/pub/consultaEventos.jsp'>" + msj.getString("home.eventos") + "</a></li>";
				//html += "\n"+"						<li class='menu-item noticia'><a href='/MonitoreoBC-WEB/noticias/crearNoticiasEventos.jsp'>" + msj.getString("home.crear_not_evt") + "</a></li>";
				
				html += "\n"+"						<li class='leaf'><a href='/MonitoreoBC-WEB/parametrizacion/editarRutasConfiguracion.jsp'>" + msj.getString("Parametros_del_sistema") + "</a></li>";
			
			}
			
			

			// if (ControlPermisos.tienePermiso(diccionarioPermisos, 145)) {
			// html += "\n"+"	<li class='leaf'><a href='"+basePath+"borrarArchivosServlet'>Borrar Temporales</a></li>";
			// }

			html += "\n"+"						</ul>";
			html += "\n"+"					</li>";
		}

		
		html += "\n"+"						<li class='menu-item contact-us'><a href='/MonitoreoBC-WEB/extra/socios.jsp'>" + msj.getString("socios") + "</a></li>";
	
		html += "\n"+"						<span class='stretch'></span>";
		
		html += "\n"+"					</ul>";
		html += "\n"+"				</div>";

		html += "\n"+"			</div>";
		html += "\n"+"		</div>";
//		html += "\n"+"	</div>";
		html += "\n"+"</div>";

		return html;
	}

	/**
	 * Retorna sidebar
	 * 
	 * @param usuario
	 * @param sesion
	 * @param msj
	 * @param diccionarioPermisos
	 * @param i18n
	 * @return html del sidebar
	 */
	public static String getSidebar(co.gov.ideamredd.util.entities.Usuario usuario, HttpSession sesion, ResourceBundle msj, Map<Integer, String> diccionarioPermisos, LenguajeI18N i18n) {

		String html = "";

		html += "\n"+"<div id='sidebar-first'>";
		html += "\n"+"<div class='section-inner clearfix'>";

		
		html += "\n"+"<div id='bloques-noticias-y-eventos' class='block-container'>";
//		html += getSidebarMenu(msj);
		html += getEventos(msj, true);
		html += getNoticias(msj, true);
		html += "\n"+"</div>";
		
		html += "\n"+"</div>";
		html += "\n"+"</div>";

		return html;
	}

	/**
	 * Retorna menu del sidebar
	 * 
	 * @param msj
	 * @return html del menu del sidebar
	 */
	public static String getSidebarMenu(ResourceBundle msj) {

		String html = "";

		html += "\n"+"	<div id='menu-sidebar' class='block-gray menu-sidebar block'>";
		html += "\n"+"		<ul>";
		html += "\n"+"			<li class='item-menu first'><a href='/MonitoreoBC-WEB/extra/presentacion.jsp'>" + msj.getString("pagina.Presentacion") + "</a></li>";
		html += "\n"+"			<li class='item-menu'><a href='/MonitoreoBC-WEB/extra/aspectosGenerales.jsp' >" + msj.getString("pagina.AGenerales") + "</a></li>";
		html += "\n"+"			<li class='item-menu'><a href='/MonitoreoBC-WEB/extra/normatividad.jsp' >" + msj.getString("pagina.Normatividad") + "</a></li>";
		html += "\n"+"			<li class='item-menu'><a href='/MonitoreoBC-WEB/extra/enlacesRel.jsp' >" + msj.getString("pagina.SitiosInteres") + "</a></li>";
		html += "\n"+"			<li class='item-menu'><a href='/MonitoreoBC-WEB/extra/documentacion.jsp' >" + msj.getString("pagina.Documentacion") + "</a></li>";
		html += "\n"+"			<li class='item-menu'><a href='/MonitoreoBC-WEB/extra/protocolos.jsp' >" + msj.getString("pagina.Protocolos") + "</a></li>";
		html += "\n"+"			<li class='item-menu'><a href='/MonitoreoBC-WEB/extra/glosario.jsp' >" + msj.getString("pagina.Glosario") + "</a></li>";
		//html += "\n"+"			<li class='item-menu'><a href='/MonitoreoBC-WEB/extra/mapaDelSitio.jsp' >" + msj.getString("pagina.mapaSitio") + "</a></li>";
		html += "\n"+"		</ul>";
		html += "\n"+"	</div>";

		return html;
	}

	/**
	 * Retorna los eventos
	 * 
	 * @param msj
	 * @param esWidget
	 * @return html de los eventos
	 */
	public static String getEventos(ResourceBundle msj, boolean esWidget) {
		ArrayList<Noticias> eventos = CargaNoticiasYEventos.cargaEventos();

		String html = "";

		html += "\n"+"	<div id='block-eventos' class='block-gray block'>";
		if (esWidget) {
			html += "\n"+"			<h2>" + msj.getString("home.eventos") + "</h2>";
		}
		html += "\n"+"			<div class='div_evento'>";

		for (int cont = 0; cont < eventos.size(); cont++) {
			if (cont >= 5) {
				break;
			}

			html += "\n"+"		<div class='noticia-evento'>";
			html += "\n"+"			<div class='titulo-noticia-evento'>";
			html += eventos.get(cont).getNombre();
			html += "				</div>";
			html += "\n"+"			<div class='detalle-noticia-evento'>";
			html += "\n"+"				<div class='miniatura-noticia-evento'>";
			if (eventos.get(cont).getPathImagen().equals("nulo")) {

				html += "			  	  		<img style='width:100px;height: 100px' src='/MonitoreoBC-WEB/img/listaNoticias.jpg'>";
			}
			else {
				html += "			  	       <img style='width: 100px;height: 100px' src='/MonitoreoBC-WEB/imagenNoticiaServlet?nomImagenParam=" + eventos.get(cont).getPathImagen() + "'>";
			}
			html += "					</div>";
			html += "\n"+"				<div class='intro-noticia-evento'>";

			if (eventos.get(cont).getDescripcion().length() > 50) {
				html += eventos.get(cont).getDescripcion().substring(0, 50);
			}
			else {
				html += eventos.get(cont).getDescripcion();
			}
			html += "... <a href='/MonitoreoBC-WEB/pub/verNoticiaEvento.jsp?idNoticia=" + eventos.get(cont).getConsecutivo() + "'> Ver mas.</a>";
			html += "\n"+"				</div>";
			html += "\n"+"			</div>";
			html += "\n"+"		</div>";
		}
		html += "		<br/><a style='font-size:12px;' href='/MonitoreoBC-WEB/pub/consultaEventos.jsp''>"+msj.getString("mas_eventos")+"</a>";
		html += "\n"+"	</div>";
		html += "\n"+"</div>";

		return html;
	}

	/**
	 * Retorna las noticias
	 * 
	 * @param msj
	 * @param esWidget
	 * @return html de las noticias
	 */
	public static String getNoticias(ResourceBundle msj, boolean esWidget) {
		ArrayList<Noticias> noticias = CargaNoticiasYEventos.cargaNoticias();
		String html = "";

		html += "\n"+"<div id='block-noticias' class='block-gray block'>";
		if (esWidget) {
			html += "\n"+"	<h2>" + msj.getString("home.noticias") + "</h2>";
		}
		html += "\n"+"	<div class='div_noticia'>";

		for (int cont = 0; cont < noticias.size(); cont++) {
			if (cont >= 5) {
				break;
			}

			html += "\n"+"		<div class='noticia-evento'>";
			html += "\n"+"			<div class='titulo-noticia-evento'>";
			html += noticias.get(cont).getNombre();
			html += "				</div>";
			html += "\n"+"			<div class='detalle-noticia-evento'>";
			html += "\n"+"				<div class='miniatura-noticia-evento'>";
			if (noticias.get(cont).getPathImagen().equals("nulo")) {
				html += "\n"+"				<img style='width:100px;height: 100px' src='/MonitoreoBC-WEB/img/listaNoticias.jpg'>";
			}
			else {
				html += "\n"+"				<img style='width: 100px;height: 100px' src='/MonitoreoBC-WEB/imagenNoticiaServlet?nomImagenParam=" + noticias.get(cont).getPathImagen() + "'>";
			}
			html += "					</div>";
			html += "\n"+"				<div class='intro-noticia-evento'>";

			if (noticias.get(cont).getDescripcion().length() > 255) {
				html += noticias.get(cont).getDescripcion().substring(0, 255);
			}
			else {
				html += noticias.get(cont).getDescripcion();
			}
			html += "... <a href='/MonitoreoBC-WEB/pub/verNoticiaEvento.jsp?idNoticia=" + noticias.get(cont).getConsecutivo() + "'>"+msj.getString("home.verMas")+"</a>";
			html += "\n"+"				</div>";
			html += "\n"+"			</div>";
			html += "\n"+"		</div>";
		}
		
		html += "\n"+"		<br/><a style='font-size: 12px;' href='/MonitoreoBC-WEB/pub/consultaNoticias.jsp'>"+msj.getString("mas_noticias")+"...</a>";
		html += "\n"+"	</div>";
		html += "\n"+"</div>";

		return html;
	}

	/**
	 * Retorna el footer
	 * 
	 * @param msj
	 * @return html del footer
	 */
	public static String getFooter(ResourceBundle msj) {
		ParametroNoBean parametro;
		parametro = new ParametroNoBean();

		String html = "";

//		html += "<div id='postscript' class='section-wrapper'>";
//		html += "	<div class='section'>";
//		html += "		<div id='links-footer' class='section-inner clearfix'>";
		html += "\n"+"		<div id='links-footer'>";
		html += "\n"+"			<div class='links-interes'>";
		html += "\n"+"				<img src='/MonitoreoBC-WEB/img/gobierno1.png' />";
		html += "\n"+"				<div class='menu-ministerios menu-postscript'>";
		html += "\n"+"					<div class='tripleta_ministerios'>";
		html += "\n"+"						<ul>";
		html += "\n"+"						<li><a class='vicepresidencia' href='" + Auxiliar.nz(parametro.getParametro("url_presidencia"), "http://www.presidencia.gov.co") + "'>" + msj.getString("presidencia") + "</a></li><br>";
		html += "\n"+"						<li><a class='vicepresidencia' href='" + Auxiliar.nz(parametro.getParametro("url_vicepresidencia"), "http://www.vicepresidencia.gov.co") + "'>" + msj.getString("vicepresidencia") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-justicia' href='" + Auxiliar.nz(parametro.getParametro("url_minjusticia"), "http://www.minjusticia.gov.co") + "'>" + msj.getString("minjusticia") + "</a></li>";
		html += "\n"+"						</ul>";
		html += "\n"+"					</div>";
		html += "\n"+"					<div class='tripleta_ministerios'>";
		html += "\n"+"						<ul>";
		html += "\n"+"						<li><a class='min-defensa' href='" + Auxiliar.nz(parametro.getParametro("url_mindefensa"), "http://www.mindefensa.gov.co") + "'>" + msj.getString("mindefensa") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-interior' href='" + Auxiliar.nz(parametro.getParametro("url_mininterior"), "http://www.mininterior.gov.co") + "'>" + msj.getString("mininterior") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-relaciones' href='" + Auxiliar.nz(parametro.getParametro("url_minrelaciones"), "http://www.cancilleria.gov.co") + "'>" + msj.getString("minrelaciones") + "</a></li>";
		html += "\n"+"						</ul>";
		html += "\n"+"					</div>";
		html += "\n"+"					<div class='tripleta_ministerios'>";
		html += "\n"+"						<ul>";
		html += "\n"+"						<li><a class='min-hacienda' href='" + Auxiliar.nz(parametro.getParametro("url_minhacienda"), "http://www.minhacienda.gov.co") + "'>" + msj.getString("minhacienda") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-minas' href='" + Auxiliar.nz(parametro.getParametro("url_minminas"), "https://www.minminas.gov.co") + "'>" + msj.getString("minminas") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-comercio' href='" + Auxiliar.nz(parametro.getParametro("url_mincomercio"), "http://www.mincit.gov.co") + "'>" + msj.getString("mincomercio") + "</a></li>";
		html += "\n"+"						</ul>";
		html += "\n"+"					</div>";
		html += "\n"+"					<div class='tripleta_ministerios'>";
		html += "\n"+"						<ul>";
		html += "\n"+"						<li><a class='min-tic' href='" + Auxiliar.nz(parametro.getParametro("url_mintic"), "http://www.mintic.gov.co") + "'>" + msj.getString("mintic") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-cultura' href='" + Auxiliar.nz(parametro.getParametro("url_mincultura"), "http://www.mincultura.gov.co") + "'>" + msj.getString("mincultura") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-agricultura' href='" + Auxiliar.nz(parametro.getParametro("url_minagricultura"), "https://www.minagricultura.gov.co") + "'>" + msj.getString("minagricultura") + "</a></li>";
		html += "\n"+"						</ul>";
		html += "\n"+"					</div>";
		html += "\n"+"					<div class='tripleta_ministerios'>";
		html += "\n"+"						<ul>";
		html += "\n"+"						<li><a class='min-ambiente' href='" + Auxiliar.nz(parametro.getParametro("url_minambiente"), "http://www.minambiente.gov.co") + "'>" + msj.getString("minambiente") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-transporte' href='" + Auxiliar.nz(parametro.getParametro("url_mintransporte"), "https://www.mintransporte.gov.co") + "'>" + msj.getString("mintransporte") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-vivienda' href='" + Auxiliar.nz(parametro.getParametro("url_minvivienda"), "http://www.minvivienda.gov.co") + "'>" + msj.getString("minvivienda") + "</a></li>";
		html += "\n"+"						</ul>";
		html += "\n"+"					</div>";
		html += "\n"+"					<div class='tripleta_ministerios'>";
		html += "\n"+"						<ul>";
		html += "\n"+"						<li><a class='min-educacion' href='" + Auxiliar.nz(parametro.getParametro("url_mineducacion"), "http://www.mineducacion.gov.co") + "'>" + msj.getString("mineducacion") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-trabajo' href='" + Auxiliar.nz(parametro.getParametro("url_mintrabajo"), "http://www.mintrabajo.gov.co") + "'>" + msj.getString("mintrabajo") + "</a></li><br>";
		html += "\n"+"						<li><a class='min-salud' href='" + Auxiliar.nz(parametro.getParametro("url_minsalud"), "https://www.minsalud.gov.co") + "'>" + msj.getString("minsalud") + "</a></li>";
		html += "\n"+"						</ul>";
		html += "\n"+"					</div>";
		html += "\n"+"				</div>";
		html += "\n"+"			</div>";
		html += "\n"+"		</div>";
//		html += "\n"+"	</div>";
//		html += "\n"+"</div>";

		//dgr
		html += "\n"+"		<div id='links-footer'>";
		html += "\n"+"		   <p style'line-height:26px;'>";
		html += "\n"+"		      <span>";
		html += "\n"+"               <img alt='Logo IDEAM Miniatura' src='/MonitoreoBC-WEB/img/ideamminiatura.png' >";
		html += "\n"+"            </span>";
		html += "\n"+"		      &nbsp;&nbsp;&nbsp;&nbsp;";
		html += "\n"+"		      <a href='http://www.ideam.gov.co/' target='_blank'> IDEAM - Instituto de Hidrología, Meteorología y Estudios Ambientales </a>";
		html += "\n"+"		   </p>";
		html += "\n"+"		   <p style='line-height: 4px; text-align: center;'>";
		html += "\n"+"		      <span style='line-height: 9px;'>";
		html += "\n"+"		          <a class='recuadro_footer' href='http://www.pronosticosyalertas.gov.co' target='_blank' >Pronósticos y Alertas</a>";
		html += "\n"+"		          &nbsp;&nbsp;";
		html += "\n"+"		          <a class='recuadro_footer' href='http://www.cambioclimatico.gov.co' target='_blank' >Cambio Climatico</a>";
		html += "\n"+"		          &nbsp;&nbsp;";
		html += "\n"+"		          <a class='recuadro_footer' href='http://intranet.ideam.gov.co' target='_blank' >Intranet</a>";
		html += "\n"+"		          &nbsp;&nbsp;";
		html += "\n"+"		          <a class='recuadro_footer' href='http://www.meteoaeronautica.gov.co' target='_blank' >Meteorologia Aeronautica</a>";
		html += "\n"+"            </span>";
		html += "\n"+"		   </p>";
		html += "\n"+"		</div>";
		//dgr
		
		html += "\n"+"<div id='footer' class='section-wrapper'>";
		html += "\n"+"	<div class='section'>";
		html += "\n"+"		<div class='section-inner clearfix'>";
//		html += "\n"+"			<div class='menu-footer'>";
//		html += "\n"+"				<ul>";
//		html += "\n"+"					<li><a href='/MonitoreoBC-WEB' >" + msj.getString("home.home") + "</a></li>";
//		html += "\n"+"					<li><a href='/MonitoreoBC-WEB/extra/mapaDelSitio.jsp'>" + msj.getString("home.mapa") + "</a></li>";
//		html += "\n"+"					<li><a href='/MonitoreoBC-WEB/extra/documentacion.jsp'>" + msj.getString("home.documentacion") + "</a></li>";
//		html += "\n"+"					<li><a href='/MonitoreoBC-WEB/extra/protocolos.jsp'>" + msj.getString("home.protocolos") + "</a></li>";
//		html += "\n"+"					<li><a href='/MonitoreoBC-WEB/extra/enlacesRel.jsp'>" + msj.getString("home.links") + "</a></li>";
//		html += "\n"+"				</ul>";
//		html += "\n"+"			</div>";
		html += "\n"+"			<div class='copyriht'>";
		String email_atencion_publico = parametro.getParametro("email_atencion_al_publico");
		html += "\n"+"				<p>" + msj.getString("ideam_adscrito_minambiente") + " <a href='mailto:" + email_atencion_publico + "'>" + email_atencion_publico + "</a></p>";
		html += "\n"+"				<p>" + msj.getString("ideam_direccion") + ":" + parametro.getParametro("direccion_ideam") + "</p>";
		html += "\n"+"				<p>PBX:" + parametro.getParametro("PBX") + " - " + msj.getString("linea_nacional") + ": " + parametro.getParametro("linea_nacional") + " - " + msj.getString("pronostico_y_alertas") + ": " + parametro.getParametro("linea_pronostico_y_alertas") + "</p>";
		html += "\n"+"				<p>" + msj.getString("horario_de_atencion") + ": " + parametro.getParametro("horario_atencion_ideam") + "</p>";
		html += "\n"+"			</div>";
		html += "\n"+"		</div>";
		html += "\n"+"	</div>";
		html += "\n"+"</div>";
		
		html += "\n"+"<div id='popUpAyuda' style='z-index: 50000;'></div>";

		return html;
	}

	/**
	 * Retorna formularios html más comunes
	 * 
	 * @param usuario
	 * @param sesion
	 * @param msj
	 * @param diccionarioPermisos
	 * @param i18n
	 * @return html de formularios comunes
	 */
	public static String getCommonForms(co.gov.ideamredd.util.entities.Usuario usuario, HttpSession sesion, ResourceBundle msj, Map<Integer, String> diccionarioPermisos, LenguajeI18N i18n) {
		String html = "";

//		html += "\n"+"<form action='/MonitoreoBC-WEB/consultarUsuarioServletAdmin' method='post' name='formConsulta' id='formConsulta'>";
//		html += "\n"+"<input type='hidden' name='documento' id='documento'> ";
//		html += "\n"+"<input type='hidden' name='tipodoc' id='tipodoc'> ";
//		html += "\n"+"<input type='hidden' name='nombre' id='nombre'>";
//		html += "\n"+"</form>";
		
		html += "\n"+"<form method='post' action='/MonitoreoBC-WEB/j_security_check' name='j_security_check' id='j_security_check'>";
//		html += "\n"+"<form method='post' action='j_security_check' name='j_security_check' id='j_security_check'>";
		html += "\n"+"	<input type='hidden' name='j_username' id='j_username' />";
		html += "\n"+"	<br>";
		html += "\n"+"	<input type='hidden' name='j_password' id='j_password' />";
		html += "\n"+"</form>";
		
		html += "\n"+"<form method='post' action='/MonitoreoBC-WEB/registrarAccesoServlet' name='formRegistra' id='formRegistra' target='deathFrame'>";
		html += "\n"+"	<input type='hidden' name='hidUsername' id='hidUsername' />";
		html += "\n"+"	<input type='hidden' name='hidPassword' id='hidPassword' />";
		html += "\n"+"</form>";
		
		html += "\n"+"<div id='popUpUsuarios' style='z-index: 3; position: absolute; width: 140px; height: 100px; background: none repeat scroll 0 0 #EEEEEE; display: none; border: 3px solid #D66A10;'>";
		html += "\n"+"	<div style='background: none repeat scroll 0 0 #D66A10;'>";
		html += "\n"+"		<label for='exampleInputEmail1' style='text-align: center; color: white; font-weight: bold;'>"+msj.getString("logOn.admin.Usuarios")+"</label>";
		html += "\n"+"	</div>";
		if (usuario != null) {
			html += "\n"+"	<label for='exampleInputEmail1'><a href='/MonitoreoBC-WEB/admin/consultarUsuarios.jsp?id=" + Util.encriptar(usuario.getIdentificacion()) + "&idiom=" + i18n.getLenguaje() + "&pais=" + i18n.getPais() + "'> *" + msj.getString("logOn.admin.consultarUsuarios") + "</a></label> ";
			html += "\n"+"	<label for='exampleInputEmail1' style='text-align: left;'><a href='/MonitoreoBC-WEB/admin/generarReportesUsuarios.jsp?id=" + Util.encriptar(usuario.getIdentificacion()) + "&idiom=" + i18n.getLenguaje() + "&pais=" + i18n.getPais() + "'> *" + msj.getString("logOn.admin.reporteUsuarios") + "</a></label> ";
			html += "\n"+"	<label for='exampleInputEmail1' style='text-align: left;'><a href='/MonitoreoBC-WEB/borrarArchivosServlet'> *" + msj.getString("logOn.admin.borrarTemporales") + "</a></label>";
			html += "\n"+"	<label for='exampleInputEmail1' onclick='popUpUsuariosClose()' style='text-align: left; cursor: pointer; color: #C36003;'>" + msj.getString("home.popAyuda.cerrar") + "</label>";
		}
		html += "\n"+"</div>";

		html += "\n"+"<div id='popUpAyuda'>";
		html += "\n"+"	<div class='cerrar_popup' onclick='popUpAyudaClose(document)'>X</div>";
		html += "\n"+"	<div class='popup_label'>";
		html += "\n"+"		<label class='popup' for='exampleInputEmail1'>" + msj.getString("home.popAyuda.titulo") + "</label>";
		html += "\n"+"	</div>";
		html += "\n"+"	<div class='botones_popup'>";
		html += "\n"+"		<input class='boton_popup' type='button' onclick=\"location.href='/MonitoreoBC-WEB/pub/registroUsuario.jsp';\" value='" + msj.getString("home.popAyuda.registrarse") + "' />";
		html += "\n"+"		<input class='boton_popup' type='button' onclick=\"location.href='/MonitoreoBC-WEB/pub/recordarClave.jsp';\" value='" + msj.getString("home.popAyuda.recordar") + "' />";
//		html += "\n"+"		<input class='boton_popup' type='button' onclick=\"location.href='/AdmIF/login.jsp';\" value='" + msj.getString("home.popAyuda.registrar_usuario_ldap") + "' />";
		html += "\n"+"	</div>";
		html += "\n"+"</div>";
		
//		html += "\n"+"<form action='/MonitoreoBC-WEB/recordarClaveUsuarioServlet' method='post' onsubmit='return validar();' name='formRecordarClave' id='formRecordarClave'>";
//		html += "\n"+"	<input type='hidden' name='email' id='email' size='30' style='width: 250px'>";
//		html += "\n"+"</form>";
		
		html += "\n"+"<div id='fondoBloqueo' style='z-index: 2; position: fixed; background-color: rgba(100, 100, 100, 0.8); width: 1000%; height: 1000%; top: -20; left: -20; display: none;'></div>";

		html += "\n"+"<form method='post' action='/MonitoreoBC-WEB/descargaDocumentosServletUsuario' name='formDescargaDocs' id='formDescargaDocs'>";
		html += "\n"+"<input type='hidden' name='hidNomDoc' id='hidNomDoc' />";
		html += "\n"+"<input type='hidden' name='hidTipoDocRef' id='hidTipoDocRef' />";
		html += "\n"+"</form>";
		
//		html += "\n"+"<script type='text/javascript'>postEdit();</script>";

		return html;
	}
	
	/**
	 * Retorna html de aviso de página denegada
	 * 
	 * @param msj
	 * @return html de aviso de página denegada
	 */
	public static String getPaginaPermisoDenegado(ResourceBundle msj) {
		String html = "";
		
		html += "\n"+"<style type='text/css'>";
		html += "\n"+"table { ";
		html += "\n"+"width:200px;";
		html += "\n"+"margin-top: -23px;";
		html += "\n"+"margin-left: -163px;";
		html += "\n"+"left: 50%; top: 50%;";
		html += "\n"+"position: absolute; }";
		html += "\n"+"</style>";
		html += "\n"+"</head>";

		html += "\n"+"<body>";
		html += "\n"+"<table border='0'>";
		html += "\n"+"<tr>";
		html += "\n"+"<td>";
		html += "\n"+"<div style='text-align: center;'>";
		html += "\n"+"<img src='../img/permisoDenegado.png' width='70' height='70' frameBorder='0' webkitAllowFullScreen mozallowfullscreen allowFullScreen>";
		html += "\n"+"</div>";
		html += "\n"+"</td>";
		html += "\n"+"</tr>";
		html += "\n"+"<tr>";
		html += "\n"+"<td style='text-align: center;'>";
		html += msj.getString("permiso_denegado");
		html += "\n"+"<a href='/MonitoreoBC-WEB'>Aceptar</a>.";
		html += "\n"+"</td>";
		html += "\n"+"</tr>";
		html += "\n"+"</table>";
		html += "\n"+"</body>";
		html += "\n"+"</html>";

		return html;
	}
}
