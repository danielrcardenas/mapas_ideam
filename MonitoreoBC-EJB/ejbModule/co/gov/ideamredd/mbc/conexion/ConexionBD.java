// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.conexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import co.gov.ideamredd.mbc.auxiliares.*;

/**
 * Clase que realiza la conexion con la base de datos.
 */

@Stateless public class ConexionBD {

	@Resource(mappedName = "java:/SMBC_DS")
	// Para jboss 6.1
	// @Resource(mappedName = "java:jboss/datasources/SMBC_DS") // Para jboss
	// 7.3

	String		ultimoError	= "";

	public ConexionBD() {
		super();
	}

	public static Connection establecerConexion() {

		DataSource	dataSource = null;
		Connection	conn = null;

		try {
			if (dataSource == null) {
				dataSource = getConnection();
			}

			if (dataSource != null) {
				conn = dataSource.getConnection();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

	public static DataSource getConnection() {

		InitialContext initialContext;
		DataSource ds = null;
		
		try {
			initialContext = new InitialContext();
			ds = (DataSource) initialContext.lookup("java:SMBC_DS"); // Para
																		// jboss
																		// 6.1
			// ds = (DataSource)
			// initialContext.lookup("java:jboss/datasources/SMBC_DS"); // Para
			// jboss 7.3
			// ds = (DataSource) initialContext.lookup("java:/SMBC_DS"); // Para
			// jboss 7.3
		}
		catch (NamingException e) {
			e.printStackTrace();
		}
		return ds;
	}

	/**
	 * Método que establece una conexión a la base de datos en transacción (producción o desarrollo).
	 * 
	 * @return conexión JNDI a oracle
	 * @throws NamingException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
//	@SuppressWarnings("unused")
//	public boolean conectarABDEnTransaccion() throws NamingException, SQLException, IOException, ClassNotFoundException {
//		Connection	conn = null;
//
//		try {
//			conn = establecerConexion();
//			return true;
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			conn = null;
//			return false;
//		}
//	}
//
	/**
	 * Metodo obtenerDato obtiene un dato de una consulta sql y lo retorna como un string
	 * 
	 * @param sql
	 * @param valorSiEsNulo
	 * @return String dato consultado
	 */
	public static String obtenerDato(String sql, String valorSiEsNulo, Connection conn) {
		boolean enTransaccion = false;
		if (conn != null) {
			enTransaccion = true;
		}
		
		Statement stmt = null;
		ResultSet rset = null;

		if (valorSiEsNulo == null) valorSiEsNulo = "";

		if (sql == null) return valorSiEsNulo;

		if (sql.equals("")) return valorSiEsNulo;

		String dato = valorSiEsNulo;

		try {
			if (conn == null) {
				conn = establecerConexion();
			}

			if (conn.isClosed()) {
				conn = establecerConexion();
			}

			if (conn == null) return valorSiEsNulo;

			stmt = conn.createStatement();

			if (stmt.execute(sql)) {
				rset = stmt.getResultSet();

				if (rset.next()) {
					dato = rset.getString(1);
					if (dato == null) dato = valorSiEsNulo;
				}

				rset.close();
			}

			stmt.close();

			return dato;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (!enTransaccion) {
				if (conn != null) {
					try {
						if (!conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return dato;
	}

	/**
	 * Funcion que ejecuta una consulta de sql
	 * 
	 * @param sql
	 *            String: consulta SQL a la base de datos
	 * @return resultado String: "1" si tuvo exito, "0" si no tuvo exito
	 * @throws SQLException
	 */
	public static boolean ejecutarSQL(String sql, Connection conn) throws SQLException {
		boolean enTransaccion = false;
		if (conn != null) {
			enTransaccion = true;
		}
		
		Statement stmt = null;

		boolean resultado = false;

		int r = 0;

		if (!Auxiliar.tieneAlgo(sql)) return false;

		try {
			if (conn == null) {
				conn = establecerConexion();
			}

			if (conn.isClosed()) {
				conn = establecerConexion();
			}

			if (conn == null) {
				resultado = false;
			}
			else {
				if (conn.isClosed()) {
					resultado = false;
				}
				else {
					stmt = conn.createStatement();

					r = stmt.executeUpdate(sql);

					if (r >= 0) {
						resultado = true;
					}
					else {
						resultado = false;
						System.out.println("Error al ejecutar SQL:" + Auxiliar.nz(sql, ""));
					}

					stmt.close();
				}
			}
		}
		catch (SQLException e) {
			resultado = false;
		}
		catch (Exception e) {
			resultado = false;
		}
		finally {
			if (!enTransaccion) {
				if (conn != null) {
					try {
						if (!conn.isClosed()) {
							conn.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return resultado;
	}

	/**
	 * Función para cambiar de modo de cometer las transacciones automáticamente o no
	 * 
	 * @param auto_cometer
	 */
	public static boolean establecerAutoCometer(boolean auto_cometer, Connection conn) {
		if (conn == null) {
			return false;
		}
				
		try {
			conn.setAutoCommit(auto_cometer);
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Función para cometer una transacción
	 */
	public static boolean cometerTransaccion(Connection conn) {
		if (conn == null) {
			return false;
		}

		try {
			if (!conn.getAutoCommit()) {
				try {
					conn.commit();
					//return desconectarse(conn);
					return true;
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Función para deshacer una transacción
	 */
	public static boolean deshacerTransaccion(Connection conn) {
		if (conn == null) {
			return false;
		}

		try {
			if (!conn.getAutoCommit()) {
				try {
					conn.rollback();
					return desconectarse(conn);
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Funcion para desconectarse de la base de datos
	 * 
	 * @return
	 */
	public static boolean desconectarse(Connection conn) {
		try {
			if (conn == null) return true;

			if (conn.isClosed()) return true;

			if (!conn.getAutoCommit()) {
				conn.rollback();
			}
			conn.close();
			
			return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}


}
