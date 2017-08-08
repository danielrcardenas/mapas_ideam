// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import gnu.jel.Evaluator;
import gnu.jel.CompiledExpression;
import gnu.jel.Library;
import gnu.jel.CompilationException;

 
/**
 * Clase Evaluador
 * 
 * Evalua expresiones matemáticas usando GNU/JEL
 * 
 * @author Santiago Hernández (santiago.h.plazas@gmail.com)
 *
 */
public class Evaluador{

	public static String yo = "BMC.";
	boolean siga = false;
	
	/**
	 * Constructor
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public Evaluador() 
	throws ClassNotFoundException, Exception {
	}

	/**
	 * Evalua String de java usando las clases de java.lang.Math
	 * para el cálculo de biomasa
	 * 
	 * @param expresion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public double evaluar(String expresion) {
		//Auxiliar aux = new Auxiliar();
		
		double o = 0.0;
		
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
			expr_c=Evaluator.compile(expresion, lib);
		} catch (CompilationException ce) {
			Auxiliar.mensajeImpersonal("advertencia", "--- COMPILATION ERROR :");
			Auxiliar.mensajeImpersonal("advertencia", ce.getMessage());
			//System.err.print("                       ");
			Auxiliar.mensajeImpersonal("advertencia", expresion);
			int column=ce.getColumn(); // Column, where error was found
			Auxiliar.mensajeImpersonal("advertencia", "Error encontrado en el caracter " + column);
			//for(int i=0;i<column+23-1;i++) System.err.print(' ');
			//System.err.println('^');
		};
		
	    if (expr_c !=null) {
	    	  
			// Evaluate (Can do it now any number of times FAST !!!)
			try {
				//result=expr_c.evaluate(null);
				o = expr_c.evaluate_double(null);
			} catch (Throwable e) {
				Auxiliar.mensajeImpersonal("error", "Exception emerged from JEL compiled code (IT'S OK) :");
				Auxiliar.mensajeImpersonal("error", e.toString());
				o = 0.0;
			};
	    };

	    return o;
	}
	
}
