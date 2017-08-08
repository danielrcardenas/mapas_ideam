package co.gov.ideamredd.dao;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.ejb.Stateless;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import co.gov.ideamredd.R.ConexionR;
import co.gov.ideamredd.util.Util;

/**
 * 
 * Clase en la cual se genera el dato de Biomasa y Carbono calculado por medio
 * del software estadistico R.
 * 
 * @author Harry S�nchez - Datum Ingenieria Ltda.
 * @version 1.0
 * @since 15 de Septiembre de 2011
 * 
 */

public class CalculoBiomasa {

	private ArrayList<Double> biomasa = new ArrayList<Double>();
	private ArrayList<Double> carbono = new ArrayList<Double>();
	private Rengine rengine;// = ConexionR.getConexionR();
	private boolean errorParcela = false;
	private boolean errorInventario = false;
	private boolean errorBosque = false;
	private Integer opcionAtipicos = 0;
	// private static final String archivoBiomasa =
	// "co/gov/ideamredd/dao/biomasa";
	// private static final ResourceBundle bio = ResourceBundle
	// .getBundle(archivoBiomasa);
	private String nombreAtipicos;
	private String nombreSinAtipicos;

	/**
	 * Metodo en el cual se carga el codigo de R para realizar el calculo de la
	 * biomasa y el carbono, a la vez, de identificar los valores atipicos y
	 * generar un archivo con o sin estos valores dependiendo de la seleccion
	 * del usuario.
	 * 
	 * @param codigoCampo
	 *            Arreglo del codigo de campo de las parcelas a las cuales se
	 *            les va a calcular la Biomasa y el Carbono. Por medio de este
	 *            parametro se obtiene la informacion de los individuos de la
	 *            parcela.
	 * 
	 * @param idParcela
	 *            Arreglo que tiene los id de las parcelas a la cuales se les va
	 *            a calcular la Biomasa y el Carbono. Este parametro se utiliza
	 *            para obtener la informaicon de las parcelas y armar el archivo
	 *            de parcelas.
	 * 
	 * 
	 */

	public void calcularBiomasaYCarbono(/*
										 * ArrayList<String> codigoCampo,
										 * ArrayList<Integer> idParcela
										 */) {
		rengine = ConexionR.getConexionR();
		// GenerarArchivosCSV g = new GenerarArchivosCSV();
		// g.generarArchivos(codigoCampo, idParcela);
		// URL funcionesR = this.getClass().getResource("EcuacionesBiomasa.r");
		 URL calR = this.getClass().getResource("Calculo.r");
		// String ruta = funcionesR.getFile().replaceFirst("/", "")
		// .replace("%20", " ");
		 String rutaCal = calR.getFile().replaceFirst("/", "")
		 .replace("%20", " ");
//		 rengine.eval("source('/" + rutaCal + "')");
//		 rengine.eval("source('/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Estimación.r')");
		// rengine.eval("source('/" + rutaCal + "')");
		// rengine.eval("source('/" + ruta + "')");
		// if (!cargarLibrerias()) {
		// if (cargarArchivos(
		// "/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Parcelas.csv",
		// "/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Individuos.csv",
		// "/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Bosque.csv"))
		// {
		// if(!calcularBiomasaCarbono()){
		// // if(!calcularAtipicos())//rengine.eval("A<-ATP(BC[[2]])");
		// System.out.println("a");
		// //
		// rengine.eval("\"/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/ResultadoR.csv\",A[[2]],B[[2]]");
		// }
		// } else
		// System.out.println("Error cargando archivos");
		// }
		// rengine.end();
		// obtenerBiomasaYCArbono(idParcela);

		rengine.eval("library(lattice)");
		rengine.eval("library(extremevalues)");

		rengine.eval("ID1<-read.csv2(\"/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Individuos.csv\",enc=\"latin1\")");
		rengine.eval("ID2<-subset(ID1,D...cm.>=10)");
		rengine.eval("ID3<-subset(ID2,Entra!=\"No\")");
		rengine.eval("Bio1<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(3.652+(-1.697*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(1.285*log(Dens..g.cm3.))) # Tropical Dry (Álvarez et al. 2012)");
		rengine.eval("Bio2<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(2.406+(-1.289*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(0.445*log(Dens..g.cm3.))) # Tropical Moist (Álvarez et al. 2012)");
		rengine.eval("Bio3<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(1.662+(-1.114*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(0.331*log(Dens..g.cm3.))) # Tropical Rain (Álvarez et al. 2012)");
		rengine.eval("Bio4<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(1.960+(-1.098*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(1.061*log(Dens..g.cm3.))) # Premontane Moist (Álvarez et al. 2012)");
		rengine.eval("Bio5<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(1.836+(-1.255*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(-0.222*log(Dens..g.cm3.))) # Lower Montane Wet (Álvarez et al. 2012)");
		rengine.eval("Bio6<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(3.130+(-1.536*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(1.767*log(Dens..g.cm3.))) # Montane Wet (Álvarez et al. 2012)");
		rengine.eval("EQ1<-c(\"Bio1\",\"Bio2\",\"Bio3\",\"Bio4\",\"Bio5\",\"Bio6\")");
		rengine.eval("PL1<-read.csv2(\"/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Parcelas.csv\",enc=\"latin1\")");
		rengine.eval("BQ1<-read.csv2(\"/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Bosque.csv\",enc=\"latin1\")");
		rengine.eval("DT1<-merge(ID3,PL1)");
		
		rengine.eval("source('/" + rutaCal + "')");
		
//		rengine.eval("CB<-function(x){BA<-get(EQ1[as.integer(x[[\"EQ\"]])])(as.numeric(x[[\"Dens..g.cm3.\"]]), as.numeric(x[[\"D...cm.\"]]), as.numeric(x[[\"Alt\"]]), as.numeric(x[[\"AB\"]]))  return(BA)}");
		rengine.eval("DT1$BAKg<-apply(DT1,1,CB)");
		rengine.eval("print(DT1$BAKg)");
		rengine.eval("DT2<-aggregate(DT1$BAKg/((DT1$Área*10000)/10),list(Plot=DT1[,\"Plot\"]),sum,na.rm=TRUE)");
		rengine.eval("print(DT2)");
		rengine.eval("DT2$BA<-DT2$x");
		rengine.eval("print(DT2$BA)");
		rengine.eval("MG1<-merge(PL1,DT2)");
		rengine.eval("print(MG1)");
		rengine.eval("DT3<-subset(MG1,select=c(Plot,Área,Año,Bosque,BA))");
		rengine.eval("print(DT3$BA)");
		rengine.eval("OUT<-getOutliers(DT3$BA,method=\"II\",distribution=\"lognormal\")");
		rengine.eval("DT4<-DT3[-c(OUT$iRight,OUT$iLeft),]");

		rengine.eval("NS1<-with(DT4,tapply(BA,list(Bosque),length))");
		rengine.eval("MN1<-with(DT4,tapply(BA,Bosque,mean))");
		rengine.eval("DS1<-with(DT4,tapply(BA,list(Bosque),sd))");
		rengine.eval("BQ2<-BQ1[order(BQ1$Bosque),]");
		rengine.eval("EST<-data.frame(Ai=BQ2$Ai, # Área que ocupa cada tipo de bosque en el país (expresado en ha) #		                n=NS1, # Número de parcelas por tipo de bosque #		                BAj=MN1, # Biomasa aérea por tipo de bosque (expresada en Mg/ha) #		                DS=DS1, # Desviación estándar asociada al promedio de biomasa aérea por tipo de bosque (expresada en Mg/ha) #		                CV=DS1/MN1*100, # Coeficiente de variación por tipo de bosque (expresado en %) #		                IC=1.96*DS1/sqrt(NS1), # Intervalo de confianza asociado al promedio de biomasa aérea por tipo de bosque (expresado en Mg/ha) #		                BAi=(MN1*BQ2$Ai)/1000000000, # Biomasa aérea total potencial por tipo de bosque (expresada en Pg) #		                Ci=((MN1*0.5)*BQ2$Ai)/1000000000, # Potencial de Carbono almacenado en la biomasa aérea por tipo de bosque (expresado en Pg) #		                CO2e=((MN1*0.5*3.67)*BQ2$Ai)/1000000000) # Dióxido de Carbono que aún no ha sido emitido a la atmósfera por tipo de bosque (expresado en Pg) #");
		rengine.eval("write.table(EST,file=\"/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Estimación.csv\",row.names=TRUE,sep=\";\",dec=\",\")");
	}

	private boolean calcularBiomasaCarbono() {
		// rengine.eval("DT1<-merge(I[[2]],P[[2]])");
		// rengine.eval("DT1$BAKg<-apply(DT1,1,CB)");
		// rengine.eval("DT2<-aggregate(DT1$BAKg/((DT1$Área*10000)/10),list(Plot=DT1[,\"Plot\"]),sum,na.rm=TRUE)");
		// rengine.eval("DT2$BA<-DT2$x");
		// rengine.eval("MG1<-merge(P[[2]],DT2)");
		// rengine.eval("DT3<-subset(MG1,select=c(Plot,Área,Año,Bosque,BA))");
		// return false;
		rengine.eval("BC<-CBC(I[[2]],P[[2]])");
		if (rengine.eval("BC[[1]]").asBool().isTRUE()) {
			System.out
					.println("Error en el calculo de la biomasa y el carbono");
			return true;
		} else
			return false;
	}

	private boolean calcularAtipicos() {
		// rengine.eval("A<-ATP(BC[[2]])");
		rengine.eval("OUT<-getOutliers(BC[[2]]$BA,method=\"II\",distribution= \"lognormal\")");
		rengine.eval("DT4<-BC[[2]][-c(OUT$iRight,OUT$iLeft),]");
		return false;
	}

	private void obtenerBiomasaYCArbono(ArrayList<Integer> idParcela) {
		try {
			int parcela = 0;
			File archivo = new File(/*
									 * bio.getString("ruta") +
									 * bio.getString("nombreArchivoSalida")
									 */"");
			FileReader fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			String linea = br.readLine();
			while ((linea = br.readLine()) != null) {
				String[] datos = linea.split(",");
				if (parcela == 0) {
					parcela = Integer.valueOf(datos[1]);
					if (!datos[datos.length - 2].equals("Inf")) {
						biomasa.add(Double.valueOf(datos[datos.length - 2]));
						carbono.add(Double.valueOf(datos[datos.length - 1]));
					} else {
						System.out
								.println("Valores Infinitos para el calculo de Biomasa");
					}
				} else if (parcela != Integer.valueOf(datos[1])) {
					parcela = Integer.valueOf(datos[1]);
					if (!datos[datos.length - 2].equals("Inf")) {
						biomasa.add(Double.valueOf(datos[datos.length - 2]));
						carbono.add(Double.valueOf(datos[datos.length - 1]));
					} else {
						System.out
								.println("Valores Infinitos para el calculo de Biomasa");
					}
				}
			}
			br.close();
			fr.close();
			archivo.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean cargarArchivos(String archivoParcelas,
			String archivoIndividuos, String archivoBosques) {
		rengine.eval("I<-CargarIndividuos(\"" + archivoIndividuos + "\")");
		errorInventario = rengine.eval("I[[1]]").asBool().isTRUE();
		if (errorInventario)
			System.out.println("Error cargando arhivo de individuos");
		rengine.eval("P<-CargarParcelas(\"" + archivoParcelas + "\")");
		errorParcela = rengine.eval("P[[1]]").asBool().isTRUE();
		if (errorParcela)
			System.out.println("Error cargando archivo de parcelas");
		rengine.eval("B<-CargarBosques(\"" + archivoBosques + "\")");
		errorBosque = rengine.eval("B[[1]]").asBool().isTRUE();
		if (errorBosque)
			System.out.println("Error vargando archivo de bosques");
		if (errorInventario && errorParcela && errorBosque)
			return false;
		else
			return true;
	}

	private boolean cargarLibrerias() {
		rengine.eval("lat<-cargarLattice()");
		rengine.eval("ext<-cargarExtremevalues()");
		if (rengine.eval("lat").asBool().isTRUE()
				|| rengine.eval("ext").asBool().isTRUE()) {
			System.out
					.println("Error cargando librerias extremeValues o Lattice");
			return true;
		} else
			return false;
	}

	// public static void main(String[] args) {
	// CalculoBiomasa c = new CalculoBiomasa();
	// ArrayList<Integer> p = new ArrayList<Integer>();
	// p.add(246);
	// ArrayList<String> cc = new ArrayList<String>();
	// cc.add("P07");
	// c.calcularBiomasaYCarbono(cc, p);
	// }

	public Integer getOpcionAtipicos() {
		return opcionAtipicos;
	}

	public void setOpcionAtipicos(Integer opcionAtipicos) {
		this.opcionAtipicos = opcionAtipicos;
	}

	public ArrayList<Double> getBiomasa() {
		return biomasa;
	}

	public void setBiomasa(ArrayList<Double> biomasa) {
		this.biomasa = biomasa;
	}

	public ArrayList<Double> getCarbono() {
		return carbono;
	}

	public void setCarbono(ArrayList<Double> carbono) {
		this.carbono = carbono;
	}

}
