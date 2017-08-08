// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexion.Conexion;

/**
 * Clase que representa un buffer
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 * 
 */
public class Buffer {

	private String				puntos	= "";
	private static Connection	conn;
	private DecimalFormat		df		= new DecimalFormat("###.####");

	/**
	 * Genera el buffer de un punto
	 * 
	 * @param x
	 * @param y
	 * @param distancia
	 * @return
	 */
	public String generaBufferPunto(Double x, Double y, Double distancia) {

		try {
			conn = Conexion.establecerConexion();
			CallableStatement buffer = conn.prepareCall("{call RED_PK_GEOMETRIA.ConsultaBuffer(?,?,?,?,?)}");
			buffer.setDouble("una_CoordenadaX", x);
			buffer.setDouble("una_CoordenadaY", y);
			buffer.setDouble("una_Distancia", distancia);
			buffer.registerOutParameter("un_Resultado", OracleTypes.CURSOR);
			buffer.registerOutParameter("un_Mensaje", OracleTypes.VARCHAR);

			buffer.execute();
			System.out.println(buffer.getObject("un_Mensaje"));
			ResultSet resultSet = (ResultSet) buffer.getObject("un_Resultado");
			// puntos=x.toString()+","+y.toString();
			while (resultSet.next()) {

				System.out.println(df.format(resultSet.getObject(2)).replace(",", ".") + "," + df.format(resultSet.getObject(3)).replace(",", ".") + "\n");
				if (puntos == "")
					puntos = df.format(resultSet.getObject(2)).replace(",", ".") + "," + df.format(resultSet.getObject(3)).replace(",", ".");
				else
					puntos += "," + df.format(resultSet.getObject(2)).replace(",", ".") + "," + df.format(resultSet.getObject(3)).replace(",", ".");
			}
			resultSet.close();
			buffer.close();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return puntos;
	}

}
