package r.ejemplo;

import java.io.File;

import org.rosuda.REngine.Rserve.RConnection;

public class EjemploR {

	public String ProbandoR(){
		String resultado="";

		try {

			resultado=resultado+"INFO : Trying to Connect to R<br>";

			// making connection object
			RConnection c = new RConnection();

			/*
			 * If u have remote server running at 192.168.1.21:6411,then
			 * RConnection c = RConnection(“192.168.1.21″, 6411)
			 */

			resultado=resultado+"INFO : The Server version is :-- "
					+ c.getServerVersion()+"<br>";

			// getting working directory
			resultado=resultado+c.eval("getwd()").asString()+"<br>";
			resultado=resultado+c.eval("library(lattice)").toString()+"<br>";
			c.eval("dato = 55");
			resultado=resultado+c.eval("dato").asInteger()+"<br>";
			c.eval("source(\""+"/home/harry.sanchez/Escritorio/Calculo.r"+"\")");
			resultado=resultado+c.eval("mean(datos)").asString();
			//resultado=resultado+c.eval("DT1$BAKg").asString();

			// random 20 numbers
//			double prem[] = c.eval("rnorm(20)").asDoubles();
//
//			for (int i = 0; i < prem.length; i++) {
//				resultado=resultado+prem[i]+"<br>";
//			}

			// closing connection
			c.close();

		} catch (Exception e) {

			resultado=resultado+"ERROR : In Connection to R<br>";
			resultado=resultado+"The Exception is " + e.getMessage()+"<br>";
			e.printStackTrace();
		}
		
		return resultado;

	}

}
