// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import gnu.jel.Evaluator;
import gnu.jel.CompiledExpression;
import gnu.jel.Library;
import gnu.jel.CompilationException;

 
/**
 * Clase BMC
 * 
 * Realiza cálculos de biomasa y carbono de forma parametrizada
 * 
 * @author © 2015 Santiago Hernández Plazas, santiago.h.plazas@gmail.com
 *
 */
public class BMC{

	public static String yo = "BMC.";
	boolean siga = false;
	
	/**
	 * Constructor
	 * 
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public BMC() 
	throws ClassNotFoundException, Exception {
	}

	/**
	 * Calcula la biomasa y el carbono de una parcela usando R
	 * 
	 * @param PRCL_CONSECUTIVO
	 * @param MTDL_CONSECUTIVO
	 * @param ruta_parcela
	 * @param BMCR_CONSECUTIVO
	 * @return Arreglo de biomasa y carbono
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String [] bmcParcelaBD_R(String PRCL_CONSECUTIVO, String MTDL_CONSECUTIVO, String ruta_parcela, String BMCR_CONSECUTIVO) 
			throws ClassNotFoundException, Exception {
		
		//Auxiliar aux = new Auxiliar();

		String [] a_bmc = new String[3];
		a_bmc = new String[]{"", "", "Debe especificar el ID de la ecuación alométrica."};

		if (!Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			return a_bmc; 
		}
		
		BD dbREDD = new BD();
		String s = ";";
		String nl = "\n";
		
		Archivo archie = new Archivo();

		if (!Auxiliar.tieneAlgo(ruta_parcela)) {
			a_bmc = new String[]{"", "", "Debe especificar la carpeta de la parcela para poder realizar los cálculos."};
			return a_bmc; 
		}
		
		if (!archie.crearCarpeta(ruta_parcela)) {
			a_bmc = new String[]{"", "", "No se pudo crear la carpeta de cálculos de la parcela " + PRCL_CONSECUTIVO};		
			return a_bmc; 
		}
		
		// CREAR ARCHIVO DE INDIVIDUOS QUE SOLO CONTENGA AQUELLOS CON SUMA DE TALLOS MAYOR A 10 Y QUE SE INCLUYAN EN CÁLCULOS
		
		String ruta_csv_individuos = "";
		
		String sql_individuos = "" +
				"SELECT " +
				"SUM((T.TAYO_DAP1+T.TAYO_DAP2)/2) AS DAP, " +
				"AVG(T.TAYO_ALTURATOTAL) AS ALT, " +
				"P.PRCL_CONSECUTIVO, " +
				"P.PRCL_AREA, " +
				"COALESCE(I.INDV_DENSIDAD, E.TXCT_DENSIDAD) AS DENS, " +
				"I.INDV_DENSIDAD, " +
				"E.TXCT_DENSIDAD, " +
				"P.PRCL_EQ, " +
				"I.INDV_CONSECUTIVO, " +
				"H.HABI_NOMBRE AS HABITO, " +
				"E.TXCT_ID AS TAXONOMIA, " +
				"E.TXCT_DVS, " +
				"E.TXCT_CLS, " +
				"E.TXCT_ORD, " +
				"E.TXCT_FML, " +
				"E.TXCT_GNS, " +
				"E.TXCT_SPC " +
				"FROM " +
				"REDD.RED_PARCELA P " +
				"INNER JOIN REDD.RED_INDIVIDUO I ON P.PRCL_CONSECUTIVO=I.INDV_PRCL_CONSECUTIVO " +
				"INNER JOIN REDD.RED_TALLO T ON I.INDV_CONSECUTIVO=T.TAYO_INDV_CONSECUTIVO " +
				"INNER JOIN REDD.RED_HABITO H ON I.INDV_HABITO=H.HABI_ID " +
				"LEFT OUTER JOIN REDD.IDT_TAXONOMY_CATALOGUE E ON I.INDV_TXCT_ID=E.TXCT_ID " +
				"WHERE P.PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO + " " + 
				//"AND (I.INDV_DENSIDAD IS NOT NULL OR E.TXCT_DENSIDAD IS NOT NULL) " +
				"GROUP BY " +
				"P.PRCL_CONSECUTIVO, " +
				"P.PRCL_AREA, " +
				"P.PRCL_EQ, " +
				"I.INDV_CONSECUTIVO, " +
				"I.INDV_DENSIDAD, " +
				"E.TXCT_DENSIDAD, " +
				"H.HABI_NOMBRE, " +
				"E.TXCT_ID, " +
				"E.TXCT_DVS, " +
				"E.TXCT_CLS, " +
				"E.TXCT_ORD, " +
				"E.TXCT_FML, " +
				"E.TXCT_GNS, " +
				"E.TXCT_SPC ";
		
		/*
		sql_individuos = "" +
				"SELECT P.PRCL_CONSECUTIVO, " +
				"P.PRCL_AREA, " +
				"P.PRCL_EQ, " +
				"I.INDV_CONSECUTIVO, " +
				"SUM((T.TAYO_DAP1+T.TAYO_DAP2)/2) AS DAP, " +
				"AVG(T.TAYO_ALTURATOTAL) AS ALT, " +
				"I.INDV_DENSIDAD AS DENS, " +
				"H.HABI_NOMBRE AS HABITO " +
				"FROM " +
				"REDD.RED_PARCELA P " +
				"INNER JOIN REDD.RED_INDIVIDUO I ON P.PRCL_CONSECUTIVO=I.INDV_PRCL_CONSECUTIVO " +
				"INNER JOIN REDD.RED_TALLO T ON I.INDV_CONSECUTIVO=T.TAYO_INDV_CONSECUTIVO " +
				"INNER JOIN REDD.RED_HABITO H ON I.INDV_HABITO=H.HABI_ID " +
				"WHERE P.PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO + " " + 
				"GROUP BY " +
				"P.PRCL_CONSECUTIVO, " +
				"P.PRCL_AREA, " +
				"P.PRCL_EQ, " +
				"I.INDV_CONSECUTIVO, " +
				"I.INDV_DENSIDAD, " +
				"H.HABI_NOMBRE ";
		*/
		
		/*
		sql_individuos = "SELECT P.PRCL_CONSECUTIVO, P.PRCL_AREA, P.PRCL_EQ, I.INDV_CONSECUTIVO, I.INDV_DENSIDAD AS DENS, H.HABI_NOMBRE AS HABITO FROM REDD.RED_PARCELA P INNER JOIN REDD.RED_INDIVIDUO I ON P.PRCL_CONSECUTIVO=I.INDV_PRCL_CONSECUTIVO INNER JOIN REDD.RED_TALLO T ON I.INDV_CONSECUTIVO=T.TAYO_INDV_CONSECUTIVO INNER JOIN REDD.RED_HABITO H ON I.INDV_HABITO=H.HABI_ID WHERE P.PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO + " AND (I.INDV_DENSIDAD IS NOT NULL) ";
		sql_individuos = "SELECT P.PRCL_CONSECUTIVO, P.PRCL_AREA, P.PRCL_EQ, I.INDV_CONSECUTIVO, I.INDV_DENSIDAD AS DENS, H.HABI_NOMBRE AS HABITO FROM REDD.RED_PARCELA P INNER JOIN REDD.RED_INDIVIDUO I ON P.PRCL_CONSECUTIVO=I.INDV_PRCL_CONSECUTIVO INNER JOIN REDD.RED_TALLO T ON I.INDV_CONSECUTIVO=T.TAYO_INDV_CONSECUTIVO INNER JOIN REDD.RED_HABITO H ON I.INDV_HABITO=H.HABI_ID WHERE P.PRCL_CONSECUTIVO=" + PRCL_CONSECUTIVO;
		*/
		
		String csv_individuos = "PID" + s + "AREA" + s + "ECUACION" + s + "FID" + s + "DAP" + s + "ALT" + s + "DENS" + s + "DENS_INDIVIDUO" + s + "DENS_ESPECIE" + s + "HABITO" + s + "TAXONOMIA" + s + "DIVISION" + s + "CLASE" + s + "ORDEN" + s + "FAMILIA" + s + "GENERO" + s + "ESPECIE" + nl;
		
		try {
			ResultSet rset = dbREDD.consultarBD(sql_individuos);
			
			if (rset != null)
			{
				String PRCL_AREA = " ";
				String PRCL_EQ = "";
				String INDV_CONSECUTIVO = "";
				String DAP = "";
				String ALT = "";
				String DENS = "";
				String INDV_DENSIDAD = "";
				String TXCT_DENSIDAD = "";
				String HABITO = "";
				String TAXONOMIA = "";
				String TXCT_DVS = "";
				String TXCT_CLS = "";
				String TXCT_ORD = "";
				String TXCT_FML = "";
				String TXCT_GNS = "";
				String TXCT_SPC = "";
				
				while (rset.next())
				{
					PRCL_AREA = Auxiliar.nz(rset.getString("PRCL_AREA"), "");
					PRCL_EQ = Auxiliar.nz(rset.getString("PRCL_EQ"), "");
					INDV_CONSECUTIVO = Auxiliar.nz(rset.getString("INDV_CONSECUTIVO"), "");
					DAP = Auxiliar.nz(rset.getString("DAP"), "");
					ALT = Auxiliar.nz(rset.getString("ALT"), "");
					DENS = Auxiliar.nz(rset.getString("DENS"), "");
					INDV_DENSIDAD = Auxiliar.nz(rset.getString("INDV_DENSIDAD"), "");
					TXCT_DENSIDAD = Auxiliar.nz(rset.getString("TXCT_DENSIDAD"), "");
					HABITO = Auxiliar.nz(rset.getString("HABITO"), "");
					TAXONOMIA = Auxiliar.nz(rset.getString("TAXONOMIA"), "");
					
					TXCT_DVS = Auxiliar.nz(rset.getString("TXCT_DVS"), "");
					TXCT_CLS = Auxiliar.nz(rset.getString("TXCT_CLS"), "");
					TXCT_ORD = Auxiliar.nz(rset.getString("TXCT_ORD"), "");
					TXCT_FML = Auxiliar.nz(rset.getString("TXCT_FML"), "");
					TXCT_GNS = Auxiliar.nz(rset.getString("TXCT_GNS"), "");
					TXCT_SPC = Auxiliar.nz(rset.getString("TXCT_SPC"), "");
					
					/*
					if (Auxiliar.tieneAlgo(TAXONOMIA)) {
						INDV_DENSIDAD = DENS;
						TXCT_DENSIDAD = dbREDD.obtenerDato("SELECT TXCT_DENSIDAD FROM IDT_TAXONOMY_CATALOGUE WHERE TXCT_ID="+TAXONOMIA, "");
						TXCT_DVS = dbREDD.obtenerDato("SELECT TXCT_DVS FROM IDT_TAXONOMY_CATALOGUE WHERE TXCT_ID="+TAXONOMIA, "");
						TXCT_CLS = dbREDD.obtenerDato("SELECT TXCT_CLS FROM IDT_TAXONOMY_CATALOGUE WHERE TXCT_ID="+TAXONOMIA, "");
						TXCT_ORD = dbREDD.obtenerDato("SELECT TXCT_ORD FROM IDT_TAXONOMY_CATALOGUE WHERE TXCT_ID="+TAXONOMIA, "");
						TXCT_FML = dbREDD.obtenerDato("SELECT TXCT_FML FROM IDT_TAXONOMY_CATALOGUE WHERE TXCT_ID="+TAXONOMIA, "");
						TXCT_GNS = dbREDD.obtenerDato("SELECT TXCT_GNS FROM IDT_TAXONOMY_CATALOGUE WHERE TXCT_ID="+TAXONOMIA, "");
						TXCT_SPC = dbREDD.obtenerDato("SELECT TXCT_SPC FROM IDT_TAXONOMY_CATALOGUE WHERE TXCT_ID="+TAXONOMIA, "");
					}
					*/
					
					csv_individuos += PRCL_CONSECUTIVO + s + PRCL_AREA.replace(".", ",") + s + PRCL_EQ + s + INDV_CONSECUTIVO + s + DAP.replace(".", ",") + s + ALT.replace(".", ",") + s + DENS.replace(".", ",") + s + INDV_DENSIDAD.replace(".", ",") + s + TXCT_DENSIDAD.replace(".", ",") + s + HABITO + s + TAXONOMIA + s + TXCT_DVS.replace(";", "_") + s + TXCT_CLS.replace(";", "_") + s + TXCT_ORD.replace(";", "_") + s + TXCT_FML.replace(";", "_") + s + TXCT_GNS.replace(";", "_") + s + TXCT_SPC.replace(";", "_") + nl;
				}
				
				rset.close();
			}
		}
		catch (Exception e) {
			
		}
	
		String ruta_individuos = ruta_parcela + File.separator + BMCR_CONSECUTIVO + "_individuos.csv";
		
		archie.escribirArchivo(ruta_individuos, csv_individuos);
		
		
		// ARMAR STRING DE SCRIPT
		
		List<String[]> commandos = new ArrayList<String[]>();
		
		//commandos.add(new String[] {"library(lattice)", ""});
		//commandos.add(new String[] {"library(extremevalues)", ""});
		commandos.add(new String[] {"INDIVIDUOS<-read.csv2(\""+ruta_individuos+"\",enc=\"UTF-8\")", ""});
		commandos.add(new String[] {"str(INDIVIDUOS)", ""});
		
		/*
		// OBTENER LA ECUACIÓN ALOMÉTRICA
		
		String EQAL_R = dbREDD.obtenerDato("SELECT EQAL_R FROM RED_ECUACIONALOMETRICA WHERE EQAL_ID=(SELECT PRCL_EQ FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+")", "");
		
		if (!Auxiliar.tieneAlgo(EQAL_R)) {
			a_bmc = new String[]{"", "", "La parcela no tiene definida su ecuación alometrica."};
			return a_bmc; 
		}
		
		commandos.add(new String[] {"CalculaBiomasa<-function(x) { Biomasa<-"+EQAL_R+"}", ""});
		*/
		
		// OBTENER SCRIPT DE R DE LA BD
		
		String w_metodologia = "";
		
		if (Auxiliar.tieneAlgo(PRCL_CONSECUTIVO)) {
			w_metodologia = "(SELECT EQAL_METODOLOGIA FROM RED_ECUACIONALOMETRICA WHERE EQAL_ID=(SELECT PRCL_EQ FROM RED_PARCELA WHERE PRCL_CONSECUTIVO="+PRCL_CONSECUTIVO+"))";
		}
		
		if (Auxiliar.tieneAlgo(MTDL_CONSECUTIVO)) {
			w_metodologia = MTDL_CONSECUTIVO;
		}
		
		if (!Auxiliar.tieneAlgo(w_metodologia)) {
			a_bmc = new String[]{"", "", "Cuál es la metodología?"};
			return a_bmc; 
		}
		
		String rscript = dbREDD.obtenerDato("SELECT MTDL_RSCRIPT FROM RED_METODOLOGIA WHERE MTDL_CONSECUTIVO="+w_metodologia, "");
		
		if (!Auxiliar.tieneAlgo(rscript)) {
			a_bmc = new String[]{"", "", "Cuál es el script de R?"};
			return a_bmc; 
		}
		
		String [] a_rscript = rscript.split(nl);
		
		if (a_rscript.length == 0) {
			a_bmc = new String[]{"", "", "El script de R está vacío"};
			return a_bmc; 
		}
		
		int i = 0;
		
		for (i=0; i<a_rscript.length; i++) {
			commandos.add(new String[] {Auxiliar.nz(a_rscript[i], "").toString(), ""});
		}
		
		String ruta_biomasa = ruta_parcela + File.separator + BMCR_CONSECUTIVO + "_biomasa.csv";
		commandos.add(new String[] {"write.csv2(RESULTADOS,\""+ruta_biomasa+"\")", ""});

		
		// EJECUTAR SCRIPT EN R
		
		if (archie.existeArchivo(ruta_biomasa)) {
			if (!archie.eliminarArchivo(ruta_biomasa)) {
				a_bmc = new String[]{"", "", "No se pudo eliminar el archivo " + ruta_biomasa};
				return a_bmc; 
			}
		}
		
		RCliente rc = new RCliente();

		List<String[]> respuestas = rc.RCommandos(commandos, "", "", "", "");
		
		int n = respuestas.size();
    	
    	String [] a_respuesta;
    	String commando = "";
    	String respuesta = "";
    	
    	String mensajes = "";
    	
    	int r = 0;
    	for (r=0; r<n; r++) {
		    a_respuesta = respuestas.get(r);
		    commando = a_respuesta[0];
		    respuesta = a_respuesta[1];
            mensajes += Auxiliar.mensajeImpersonal("nota", commando + " ==> " + respuesta);    // prints result
		}

    	
    	// LEER ARCHIVO DE BIOMASAS
    	
    	String contenido_biomasas = "";
    	String [] a_biomasas;
    	String PID = "";
    	String BA = "";
    	double SUMA_BA = 0.0;
    	
    	if (archie.existeArchivo(ruta_biomasa)) {
    		contenido_biomasas = archie.leerArchivoSeparado(ruta_biomasa, nl);
    		
    		if (Auxiliar.tieneAlgo(contenido_biomasas)) {
    			a_biomasas = contenido_biomasas.split(nl);
    			int n_biomasas = a_biomasas.length;
    			
    			if (n_biomasas > 0) {
    				int b=0;
    				for (b=0; b<n_biomasas; b++) {
	    				String biomasa = a_biomasas[b];
	    				String [] a_biomasa = biomasa.split(s); 
		    			if (a_biomasa.length == 3) {
		    				if (Auxiliar.tieneAlgo(Auxiliar.nz(a_biomasa[0],"").replace("\"", ""))) {
			    				PID = Auxiliar.nz(a_biomasa[1], "");
			    				BA = Auxiliar.nz(a_biomasa[2], "").replace(",", ".");
	
			    				if (Auxiliar.tieneAlgo(PID) && Auxiliar.esBiomasaAerea(BA)) {
			    					dbREDD.ejecutarSQL("UPDATE RED_PARCELA SET PRCL_BIOMASA="+BA+" WHERE PRCL_CONSECUTIVO="+PID);
				    				SUMA_BA += Double.parseDouble(BA);
			    				}
		    				}
		    			}
    				}
    			}
    		}
    	}
    	
		a_bmc = new String[]{PRCL_CONSECUTIVO, String.valueOf(SUMA_BA), mensajes};

		return a_bmc;
	}
	
	/**
	 * Calcula la biomasa y el carbono de un individuo
	 * 
	 * @param INDV_CONSECUTIVO
	 * @param EQAL_METODOLOGIA
	 * @param EQAL_CODIGO
	 * @param EQAL_ID
	 * @param p_DAP
	 * @param p_DENSIDAD
	 * @param herramienta
	 * @return arreglo de biomasa y carbono
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String [] bmcIndividuoBD(String INDV_CONSECUTIVO, String EQAL_METODOLOGIA, String EQAL_CODIGO, String EQAL_ID, String p_DAP, String p_DENSIDAD, String herramienta) 
			throws ClassNotFoundException, Exception {

		Auxiliar aux = new Auxiliar();

		String [] a_bmc = new String[3];
		a_bmc = new String[]{"", "", ""};

		if (!Auxiliar.tieneAlgo(EQAL_ID)) {
			a_bmc = new String[]{"", "", "Debe especificar el ID de la ecuación alométrica."};
			return a_bmc; 
		}
		
		if (!Auxiliar.tieneAlgo(INDV_CONSECUTIVO)) {
			a_bmc = new String[]{"", "", "Debe especificar el consecutivo del individuo."};
			return a_bmc; 
		}


		
		// OBTENER DATOS INDIVIDUO NECESARIOS PARA CALCULAR
		BD dbREDD = new BD();

		// OBTENER VARIABLES  

		// OBTENER DAP
		String sDAP = "";
		
		if (!Auxiliar.tieneAlgo(p_DAP)) {
			sDAP = dbREDD.obtenerDato("SELECT SUM((TAYO_DAP1 + TAYO_DAP2)/2) FROM RED_TALLO WHERE TAYO_INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
		}
		else {
			sDAP = p_DAP;
		}
		
		if (!Auxiliar.esDAP(sDAP)) {
			return new String []{"", "", "DAP "+sDAP+" no es un DAP válido"};
		}
		
		double DAP = Double.parseDouble(sDAP);

		
		// OBTENER DENSIDAD
		
		String sDENSIDAD = "";
		
		if (!Auxiliar.tieneAlgo(Auxiliar.nz(p_DENSIDAD, "")) || p_DENSIDAD.equals("NULL")) {
			sDENSIDAD = dbREDD.obtenerDato("SELECT INDV_DENSIDAD FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
		}
		else {
			sDENSIDAD = p_DENSIDAD;
		}
		
		if (!Auxiliar.tieneAlgo(Auxiliar.nz(sDENSIDAD, ""))) {
			String INDV_TXCT_ID = dbREDD.obtenerDato("SELECT INDV_TXCT_ID FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
			
			if (!Auxiliar.tieneAlgo(INDV_TXCT_ID)) {
				return new String []{"", "", " No encontré la taxonomía del individuo, y por ende, su densidad taxonómica."};
			}
			sDENSIDAD = dbREDD.obtenerDato("SELECT TXCT_DENSIDAD FROM IDT_TAXONOMY_CATALOGUE WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
		}
		
		if (!Auxiliar.tieneAlgo(sDENSIDAD)) {
			return new String []{"", "", "No encontré la densidad de la madera por ningún lado."};
		}
		
		if (!Auxiliar.esDensidad(sDENSIDAD)) {
			return new String []{"", "", "La densidad "+sDENSIDAD+" no es válida."};
		}

	    double DENSIDAD = Double.parseDouble(sDENSIDAD);

		
		double BA = 0.0;
		
		if (EQAL_ID.equals("1")) BA = Math.exp(3.652 - 1.697 * Math.log(DAP) + 1.169 * Math.pow(Math.log(DAP),2) - 0.122 * Math.pow(Math.log(DAP), 3) + 1.285 * Math.log(DENSIDAD) + (Math.pow(0.336, 2)/2));
		if (EQAL_ID.equals("2")) BA = Math.exp(2.406 - 1.289 * Math.log(DAP) + 1.169 * Math.pow(Math.log(DAP),2) - 0.122 * Math.pow(Math.log(DAP), 3) + 0.445 * Math.log(DENSIDAD) + (Math.pow(0.336, 2)/2));
		if (EQAL_ID.equals("3")) BA = Math.exp(1.662 - 1.114 * Math.log(DAP) + 1.169 * Math.pow(Math.log(DAP),2) - 0.122 * Math.pow(Math.log(DAP), 3) + 0.331 * Math.log(DENSIDAD) + (Math.pow(0.336, 2)/2));
		if (EQAL_ID.equals("4")) BA = Math.exp(1.960 - 1.098 * Math.log(DAP) + 1.169 * Math.pow(Math.log(DAP),2) - 0.122 * Math.pow(Math.log(DAP), 3) + 1.061 * Math.log(DENSIDAD) + (Math.pow(0.336, 2)/2));
		if (EQAL_ID.equals("5")) BA = Math.exp(1.836 - 1.255 * Math.log(DAP) + 1.169 * Math.pow(Math.log(DAP),2) - 0.122 * Math.pow(Math.log(DAP), 3) + 0.222 * Math.log(DENSIDAD) + (Math.pow(0.336, 2)/2));
		if (EQAL_ID.equals("6")) BA = Math.exp(3.130 - 1.536 * Math.log(DAP) + 1.169 * Math.pow(Math.log(DAP),2) - 0.122 * Math.pow(Math.log(DAP), 3) + 1.767 * Math.log(DENSIDAD) + (Math.pow(0.336, 2)/2));

		String w_id = "";
		if (Auxiliar.tieneAlgo(EQAL_ID)) {
			w_id = " AND EQAL_ID=" + EQAL_ID;
		}
		
		String w_mc = "";
		if (Auxiliar.tieneAlgo(EQAL_METODOLOGIA) && Auxiliar.tieneAlgo(EQAL_CODIGO)) {
			w_mc = " AND EQAL_METODOLOGIA="+EQAL_METODOLOGIA+" AND EQAL_CODIGO="+EQAL_CODIGO+" ";
		}

		if (herramienta.equals("Evaluar")) {
			Evaluador evalu = new Evaluador();

			// OBTENER LA ECUACION
			String parametrizable = "";
			
				parametrizable = dbREDD.obtenerDato("SELECT EQAL_PARAMETRIZABLE FROM RED_ECUACIONALOMETRICA WHERE 1=1 "+w_id+" "+w_mc, "");
			
				if (!Auxiliar.tieneAlgo(parametrizable)) {
					return new String []{"", "", "No se pudo obtener la ecuación alométrica parametrizable"};
				}
	
			
				// REEMPLAZAR PARÁMETROS POR VARÍABLES
				
				parametrizable = parametrizable.replace("_[D]_", sDAP);
				parametrizable = parametrizable.replace("_[p]_", sDENSIDAD);
				parametrizable = parametrizable.replace("Math.", "");
				
				
				BA = evalu.evaluar(parametrizable);
		}
		else if (herramienta.equals("R")) {
			RCliente rc = new RCliente();
			
			String script = dbREDD.obtenerDato("SELECT EQAL_SCRIPT FROM RED_ECUACIONALOMETRICA WHERE 1=1 "+w_id+" "+w_mc, "");
			String ruta_script = dbREDD.obtenerDato("SELECT EQAL_RUTA_SCRIPT FROM RED_ECUACIONALOMETRICA WHERE 1=1 "+w_id+" "+w_mc, "");

			boolean desde_archivo = false;
			
			if (Auxiliar.tieneAlgo(ruta_script)) {
				script = ruta_script;
				desde_archivo = true;
			}
			else {
				desde_archivo = false;
			}
			
			try {
				String rR = rc.RString(script, desde_archivo, "", "", "", "");
				if (Auxiliar.esBiomasaAerea(rR)) {
					BA = Double.parseDouble(rR);
				}
				else {
					BA = -2.0;
				}
			}
			catch (Exception e) {
				Auxiliar.mensajeImpersonal("error",  "Error al evaluar biomasa en R: " + e.toString());
				BA = -1.0;
			}
		}
		else {
		}
		
		a_bmc = new String []{String.valueOf(BA), String.valueOf(BA/2), "Biomasa Aérea="+String.valueOf(BA) + ", Carbono="+String.valueOf(BA/2)};
	    
		return a_bmc;
	}

	/**
	 * Calcula la biomasa de un individuo
	 * 
	 * @param INDV_CONSECUTIVO
	 * @param EQAL_METODOLOGIA
	 * @param EQAL_CODIGO
	 * @param EQAL_ID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String [] bmcIndividuo(String INDV_CONSECUTIVO, String EQAL_METODOLOGIA, String EQAL_CODIGO, String EQAL_ID) throws ClassNotFoundException, Exception {
		String [] a_bmc = new String[3];
		a_bmc = new String[]{"", "", ""};
		
		BD dbREDD = new BD();
		
		Auxiliar aux = new Auxiliar();
		
		
		// OBTENER DATOS INDIVIDUO NECESARIOS PARA CALCULAR
		
		// OBTENER VARIABLES  
		// Math.exp(3.652 - 1.697 * Math.log(_[D]_) + 1.169 * Math.pow(Math.log(_[D]_),2) - 0.122 * (Math.pow(Math.log(_[D]_), 3) + 1.285 * Math.log(_[p]_) + (Math.pow(0.336, 2)/2))
		
		
		// OBTENER DAP
		String sDAP = dbREDD.obtenerDato("SELECT SUM((TAYO_DAP1 + TAYO_DAP2)/2) FROM RED_TALLO WHERE TAYO_INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
		
		if (!Auxiliar.esDAP(sDAP)) {
			return new String []{"", "", "DAP "+sDAP+" no es un DAP válido"};
		}
		
		double DAP = Double.parseDouble(sDAP);
		
		// OBTENER DENSIDAD
		
		// PRIMERO, DIRECTAMENTE DE LA TABLA DE INDIVIDUOS
		
		String sDENSIDAD = dbREDD.obtenerDato("SELECT INDV_DENSIDAD FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
		
		if (!Auxiliar.tieneAlgo(sDENSIDAD)) {
			String INDV_TXCT_ID = dbREDD.obtenerDato("SELECT INDV_TXCT_ID FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
			
			if (!Auxiliar.tieneAlgo(INDV_TXCT_ID)) {
				return new String []{"", "", "No encontré la taxonomía del individuo, y por ende, su densidad taxonómica."};
			}
			sDENSIDAD = dbREDD.obtenerDato("SELECT TXCT_DENSIDAD FROM IDT_TAXONOMY_CATALOGUE WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
		}
		
		if (!Auxiliar.tieneAlgo(sDENSIDAD)) {
			return new String []{"", "", "No encontré la densidad de la madera por ningún lado."};
		}
		
		if (!Auxiliar.esDensidad(sDENSIDAD)) {
			return new String []{"", "", "La densidad "+sDENSIDAD+" no es válida."};
		}
		
		double DENSIDAD = Double.parseDouble(sDENSIDAD);
		
		
		String w_id = "";
		if (Auxiliar.tieneAlgo(EQAL_ID)) {
			w_id = " AND EQAL_ID=" + EQAL_ID;
		}
		
		String w_mc = "";
		if (Auxiliar.tieneAlgo(EQAL_METODOLOGIA) && Auxiliar.tieneAlgo(EQAL_CODIGO)) {
			w_mc = " AND EQAL_METODOLOGIA="+EQAL_METODOLOGIA+" AND EQAL_CODIGO="+EQAL_CODIGO+" ";
		}
		
		// OBTENER LA ECUACION
		
		String parametrizable = dbREDD.obtenerDato("SELECT EQAL_PARAMETRIZABLE FROM RED_ECUACIONALOMETRICA WHERE 1=1 "+w_id+" "+w_mc, "");
		
		if (!Auxiliar.tieneAlgo(parametrizable)) {
			return new String []{"", "", "No se pudo obtener la ecuación alométrica parametrizable"};
		}
		
		// REEMPLAZAR PARÁMETROS POR VARÍABLES
		
		parametrizable = parametrizable.replace("_[D]_", sDAP);
		parametrizable = parametrizable.replace("_[p]_", sDENSIDAD);
		parametrizable = parametrizable.replace("Math.", "");
		
		
		
		// EVALUACIÓN DE LA ECUACIÓN ALOMÉTRICA
		
		// Set up library
		Class[] staticLib=new Class[1];
		try {
			staticLib[0]=Class.forName("java.lang.Math");
		} catch(ClassNotFoundException e) {
			// Can't be ;)) ...... in java ... ;)
		};
		Library lib=new Library(staticLib,null,null,null,null);
		try {
			lib.markStateDependent("random",null);
		} catch (CompilationException e) {
			// Can't be also
		};
		
		// Compile
		CompiledExpression expr_c=null;
		try {
			expr_c=Evaluator.compile(parametrizable,lib);
		} catch (CompilationException ce) {
			Auxiliar.mensajeImpersonal("advertencia", "--- COMPILATION ERROR :");
			Auxiliar.mensajeImpersonal("advertencia", ce.getMessage());
			//System.err.print("                       ");
			Auxiliar.mensajeImpersonal("advertencia", parametrizable);
			int column=ce.getColumn(); // Column, where error was found
			Auxiliar.mensajeImpersonal("advertencia", "Error encontrado en el caracter " + column);
			//for(int i=0;i<column+23-1;i++) System.err.print(' ');
			//System.err.println('^');
		};
		
		double result = 0.0;
		
	    if (expr_c !=null) {
  
			// Evaluate (Can do it now any number of times FAST !!!)
			try {
				//result=expr_c.evaluate(null);
				result=expr_c.evaluate_double(null);
			} catch (Throwable e) {
				Auxiliar.mensajeImpersonal("error", "Exception emerged from JEL compiled code (IT'S OK) :");
				Auxiliar.mensajeImpersonal("error", e.toString());
				result = 0.0;
			};
				  
				// Print result
			if (result==0.0) 
				Auxiliar.mensajeImpersonal("advertencia", "Resultado es 0.0");
			else
				Auxiliar.mensajeImpersonal("confirmacion", "BA del individuo " + INDV_CONSECUTIVO + "=" +String.valueOf(result));
			
		};
		
		if (!Auxiliar.esBiomasaAerea(String.valueOf(result))) {
			dbREDD.desconectarse();
	    	return new String []{"", "", "El resultado ["+String.valueOf(result)+"] no es válido para el individuo "+INDV_CONSECUTIVO };
		}

		double BA = result;

		if (!Auxiliar.esBiomasaAerea(String.valueOf(BA))) {
			dbREDD.desconectarse();
	    	return new String []{"", "", "Biomasa aerea ["+String.valueOf(BA)+"] no es válida para el individuo "+INDV_CONSECUTIVO };
		}

		a_bmc = new String []{String.valueOf(BA), String.valueOf(BA/2), "Biomasa Aérea="+String.valueOf(BA) + ", Carbono="+String.valueOf(BA/2)};
	    
		dbREDD.desconectarse();
		return a_bmc;
	}
	
	
	/**
	 * Calcula la biomasa y el carbono de un individuo como objeto
	 * 
	 * @param INDV_CONSECUTIVO
	 * @param EQAL_METODOLOGIA
	 * @param EQAL_CODIGO
	 * @param EQAL_ID
	 * @return arreglo de biomasa y carbono
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String [] bmcIndividuoObject(String INDV_CONSECUTIVO, String EQAL_METODOLOGIA, String EQAL_CODIGO, String EQAL_ID) throws ClassNotFoundException, Exception {
		String [] a_bmc = new String[3];
		a_bmc = new String[]{"", "", ""};
		
		BD dbREDD = new BD();
	
	    Auxiliar aux = new Auxiliar();

	    
	    // OBTENER DATOS INDIVIDUO NECESARIOS PARA CALCULAR
	    
	    // OBTENER VARIABLES  
	    // Math.exp(3.652 - 1.697 * Math.log(_[D]_) + 1.169 * Math.pow(Math.log(_[D]_),2) - 0.122 * (Math.pow(Math.log(_[D]_), 3) + 1.285 * Math.log(_[p]_) + (Math.pow(0.336, 2)/2))
	    
	    
	    // OBTENER DAP
	    String sDAP = dbREDD.obtenerDato("SELECT SUM((TAYO_DAP1 + TAYO_DAP2)/2) FROM RED_TALLO WHERE TAYO_INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
	    
	    if (!Auxiliar.esDAP(sDAP)) {
	    	return new String []{"", "", "DAP "+sDAP+" no es un DAP válido"};
	    }
	    
	    double DAP = Double.parseDouble(sDAP);
	    
	    // OBTENER DENSIDAD
	    
	    // PRIMERO, DIRECTAMENTE DE LA TABLA DE INDIVIDUOS
	    
	    String sDENSIDAD = dbREDD.obtenerDato("SELECT INDV_DENSIDAD FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
	    
	    if (!Auxiliar.tieneAlgo(sDENSIDAD)) {
	    	String INDV_TXCT_ID = dbREDD.obtenerDato("SELECT INDV_TXCT_ID FROM RED_INDIVIDUO WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
	    	
	    	if (!Auxiliar.tieneAlgo(INDV_TXCT_ID)) {
		    	return new String []{"", "", "No encontré la taxonomía del individuo, y por ende, su densidad taxonómica."};
	    	}
	    	sDENSIDAD = dbREDD.obtenerDato("SELECT TXCT_DENSIDAD FROM IDT_TAXONOMY_CATALOGUE WHERE INDV_CONSECUTIVO="+INDV_CONSECUTIVO, "");
	    }
	    
	    if (!Auxiliar.tieneAlgo(sDENSIDAD)) {
	    	return new String []{"", "", "No encontré la densidad de la madera por ningún lado."};
	    }
	    
	    if (!Auxiliar.esDensidad(sDENSIDAD)) {
	    	return new String []{"", "", "La densidad "+sDENSIDAD+" no es válida."};
	    }
	    
	    double DENSIDAD = Double.parseDouble(sDENSIDAD);
	    
	    
	    String w_id = "";
	    if (Auxiliar.tieneAlgo(EQAL_ID)) {
	    	w_id = " AND EQAL_ID=" + EQAL_ID;
	    }
	    
		String w_mc = "";
	    if (Auxiliar.tieneAlgo(EQAL_METODOLOGIA) && Auxiliar.tieneAlgo(EQAL_CODIGO)) {
	    	w_mc = " AND EQAL_METODOLOGIA="+EQAL_METODOLOGIA+" AND EQAL_CODIGO="+EQAL_CODIGO+" ";
	    }
	    
	    // OBTENER LA ECUACION
	    
	    String parametrizable = dbREDD.obtenerDato("SELECT EQAL_PARAMETRIZABLE FROM RED_ECUACIONALOMETRICA WHERE 1=1 "+w_id+" "+w_mc, "");
	    
	    if (!Auxiliar.tieneAlgo(parametrizable)) {
	    	return new String []{"", "", "No se pudo obtener la ecuación alométrica parametrizable"};
	    }
	    
	    // REEMPLAZAR PARÁMETROS POR VARÍABLES
	    
	    parametrizable = parametrizable.replace("_[D]_", sDAP);
	    parametrizable = parametrizable.replace("_[p]_", sDENSIDAD);
	    parametrizable = parametrizable.replace("Math.", "");
	    
	    
	    
	    // EVALUACIÓN DE LA ECUACIÓN ALOMÉTRICA
	    
	    // Set up library
	    Class[] staticLib=new Class[1];
	    try {
	      staticLib[0]=Class.forName("java.lang.Math");
	    } catch(ClassNotFoundException e) {
	      // Can't be ;)) ...... in java ... ;)
	    };
	    Library lib=new Library(staticLib,null,null,null,null);
	    try {
	    lib.markStateDependent("random",null);
	    } catch (CompilationException e) {
	      // Can't be also
	    };

	    // Compile
	    CompiledExpression expr_c=null;
	    try {
	      expr_c=Evaluator.compile(parametrizable,lib);
	    } catch (CompilationException ce) {
	      Auxiliar.mensajeImpersonal("advertencia", "--- COMPILATION ERROR :");
	      Auxiliar.mensajeImpersonal("advertencia", ce.getMessage());
	      //System.err.print("                       ");
	      Auxiliar.mensajeImpersonal("advertencia", parametrizable);
	      int column=ce.getColumn(); // Column, where error was found
	      Auxiliar.mensajeImpersonal("advertencia", "Error encontrado en el caracter " + column);
	      //for(int i=0;i<column+23-1;i++) System.err.print(' ');
	      //System.err.println('^');
	    };

		Object result=null;
		
	    if (expr_c !=null) {
  
			// Evaluate (Can do it now any number of times FAST !!!)
			try {
				result=expr_c.evaluate(null);
			} catch (Throwable e) {
				Auxiliar.mensajeImpersonal("error", "Exception emerged from JEL compiled code (IT'S OK) :");
				Auxiliar.mensajeImpersonal("error", e.toString());
			};
				  
				// Print result
			if (result==null) 
				Auxiliar.mensajeImpersonal("advertencia", "Resultado es void");
			else
				Auxiliar.mensajeImpersonal("confirmacion", "BA del individuo " + INDV_CONSECUTIVO + "=" +result.toString());
			
		};
		
		if (result != null) {
			if (!Auxiliar.esBiomasaAerea(result.toString())) {
				dbREDD.desconectarse();
		    	return new String []{"", "", "El resultado ["+result.toString()+"] no es válido para el individuo "+INDV_CONSECUTIVO };
			}
			
			double BA = Double.parseDouble(result.toString());

			if (!Auxiliar.esBiomasaAerea(String.valueOf(BA))) {
				dbREDD.desconectarse();
		    	return new String []{"", "", "Biomasa aerea ["+String.valueOf(BA)+"] no es válida para el individuo "+INDV_CONSECUTIVO };
			}

			a_bmc = new String []{String.valueOf(BA), String.valueOf(BA/2), "Biomasa Aérea="+String.valueOf(BA) + ", Carbono="+String.valueOf(BA/2)};
		}

	    
		dbREDD.desconectarse();
		return a_bmc;
	}
}
