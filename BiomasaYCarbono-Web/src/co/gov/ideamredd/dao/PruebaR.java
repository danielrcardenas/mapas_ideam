package co.gov.ideamredd.dao;

import org.rosuda.JRI.Rengine;

import co.gov.ideamredd.R.ConexionR;

public class PruebaR {
	private static Rengine rengine;

	public static Rengine prueba() {
		rengine = ConexionR.getConexionR();
		return rengine;
	}

}
