// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;


/**
 * Métodos para desplegar el menú de enlaces a los socios del proyecto
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 */
public class UI {

	/**
	 * Retorna el encabezado con los enlaces a las páginas de los socios del proyecto
	 * @return String html de encabezado
	 */
	public static String getHeader() {

		String html = "";
		
		html += "<div class='section'>";
		html += "	<div class='logo'>";
		html += "		<a href='/MonitoreoBC-WEB'>";
		html += "			<img src='/AdmIF/img/logo.png' alt='' class='logo'>";
		html += "		</a>";
		html += "	</div>";
		html += "	<div class='logo'>";
		html += "		<a href='http://www.minambiente.gov.co/'>";
		html += "			<img src='/AdmIF/img/img-min.png' alt='' class='logo'>";
		html += "		</a>";
		html += "	</div>";
		html += "	<div class='logo'>";
		html += "		<a href='http://wsp.presidencia.gov.co/portal/Paginas/default.aspx'>";
		html += "			<img src='/AdmIF/img/img-prosperidad.png' alt='' class='logo'>";
		html += "		</a>";
		html += "	</div>";
		html += "	<div class='logo'>";
		html += "		<a href='http://www.moore.org/'>";
		html += "			<img src='/AdmIF/img/img-moore.png' alt='' class='logo'>";
		html += "		</a>"; 
		html += "	</div>";
		html += "	<div class='logo'>";
		html += "		<a href='http://www.patrimonionatural.org.co/'>";
		html += "			<img src='/AdmIF/img/img-patrimonio.png' alt='' class='logo'>";
		html += "		</a>";
		html += "	</div>";
		html += "</div>";

		return html;
		
	}
	
}
