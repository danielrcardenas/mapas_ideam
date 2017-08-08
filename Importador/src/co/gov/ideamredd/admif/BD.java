// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
/**
 * Paquete admif
 * Función: administración de inventarios forestales
 */
package co.gov.ideamredd.admif;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

 
/**
 * Métodos para interactuar con la base de datos
 * 
 * @author Santiago Hernández Plazas (santiago.h.plazas@gmail.com)
 *
 */
public class BD{

	String username = "";
	String password = "";
	String host = "";
	String port = "";
	String sid = "";
	String conexion = "";

	String encriptar_usuario = "";
	String llave_encripcion = "";


	public String excepcion = "";
	public String comentario = "";

	static String debugstr = "";
	
	String ultimoError = "";

	Connection conn = null;
	ResultSetMetaData meta = null;

	//private Auxiliar aux = new Auxiliar();

	
	/**
	 * Instancia clase
	 * @param p_username
	 * @param p_password
	 * @param p_host
	 * @param p_port
	 * @param p_sid
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public BD() 
	throws ClassNotFoundException, Exception {		
		
		Archivo archie = new Archivo();
		
		String [] a_parametros_bd = archie.aParametrosConexionBD();
		
		this.username = a_parametros_bd[0];		
		this.password = a_parametros_bd[1];		
		this.host = a_parametros_bd[2];		
		this.port = a_parametros_bd[3];		
		this.sid = a_parametros_bd[4];		
		
		if (Auxiliar.tieneAlgo(this.username) && Auxiliar.tieneAlgo(this.password) && Auxiliar.tieneAlgo(this.host) && Auxiliar.tieneAlgo(this.port) && Auxiliar.tieneAlgo(this.sid)) {
			setConnectionString(this.host, this.port, this.sid);
		}
	}

	
	/**
	 * Establece host para la conexión
	 * @param server
	 * @throws ClassNotFoundException
	 */
	public void setHost(String pHost)
	throws ClassNotFoundException
	{
		if (pHost.equals(""))
			return;
		
		this.host = pHost;
	}
	
	/**
	 * Establece username para la conexión
	 * @param server
	 * @throws ClassNotFoundException
	 */
	public void setUsername(String pUsername)
	throws ClassNotFoundException
	{
		if (pUsername.equals(""))
			return;
		
		this.username = pUsername;
	}
	
	/**
	 * Establece password para la conexión
	 * @param server
	 * @throws ClassNotFoundException
	 */
	public void setPassword(String pPassword)
	throws ClassNotFoundException
	{
		if (pPassword.equals(""))
			return;
		
		this.password = pPassword;
	}

	/**
	 * Establece string de conexión
	 * @param pHost
	 * @param pPort
	 * @param pSID
	 * @throws ClassNotFoundException
	 */
	public String setConnectionString(String pHost, String pPort, String pSID)
	throws ClassNotFoundException
	{
		if (pHost.equals("")) pHost = this.host;
		if (pHost.equals("")) return "Falto el host";
		if (pPort.equals("")) pPort = "1521";
		if (pSID.equals("")) pSID = "ORCL";
		
		this.conexion = "jdbc:oracle:thin:@//"+pHost+":"+pPort+"/"+pSID;
		return "1";
	}
	
	
	
	/**
	 * Método que establece una conexión a la base de datos
	 * (producción o desarrollo).
	 * @return conexión JNDI a oracle
	 * @throws NamingException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unused")
	public Connection conexionBD() 
	throws NamingException, SQLException, IOException, ClassNotFoundException 
	{
		try 
		{
			@SuppressWarnings("rawtypes")
			Class theClass = null;
			try 
			{
			    theClass = Class.forName("oracle.jdbc.driver.OracleDriver", true, Thread.currentThread().getContextClassLoader() );
			}
			catch (ClassNotFoundException e) 
			{
			    theClass = Class.forName("oracle.jdbc.driver.OracleDriver");
			}
			
			if (conexion.equals("")) return null;
			if (username.equals("")) return null;
			if (password.equals("")) return null;
			
			return DriverManager.getConnection(conexion,username,password);
		}
		catch (Exception e)
		{
			this.excepcion = Auxiliar.mensajeImpersonal("error", "Error en getConnection: " + e.toString() + "<br/>comentario: "+comentario);
			return null;
		}
    }

	
	/**
	 * Método que establece una conexión a la base de datos en transacción
	 * (producción o desarrollo).
	 * @return conexión JNDI a oracle
	 * @throws NamingException
	 * @throws SQLException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unused")
	public boolean conectarABDEnTransaccion() 
	throws NamingException, SQLException, IOException, ClassNotFoundException 
	{
		try 
		{
			@SuppressWarnings("rawtypes")
			Class theClass = null;
			try 
			{
				theClass = Class.forName("oracle.jdbc.driver.OracleDriver", true, Thread.currentThread().getContextClassLoader() );
			}
			catch (ClassNotFoundException e) 
			{
				theClass = Class.forName("oracle.jdbc.driver.OracleDriver");
			}
			
			if (conexion.equals("")) return false;
			if (username.equals("")) return false;
			if (password.equals("")) return false;
			
			this.conn = DriverManager.getConnection(conexion,username,password);
			
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.excepcion = Auxiliar.mensajeImpersonal("error", "Error en getConnection: " + e.toString() + "<br/>comentario: "+comentario);
			this.conn = null;
			return false;
		}
	}
	
	/**
	 * Metodo obtenerDato
	 * obtiene un dato de una consulta sql y lo retorna como un string
	 * 
	 * @param sql
	 * @param valorSiEsNulo
	 * @return String dato consultado
	 */
	public String obtenerDato(String sql, String valorSiEsNulo)
	{
		Statement stmt = null;
		ResultSet rset = null;

		if (!Auxiliar.tieneAlgo(sql)) return valorSiEsNulo;
		
		if (valorSiEsNulo == null) valorSiEsNulo = "";
		
		String dato = valorSiEsNulo;

		try
		{
			if (conn == null) {
				//conn = conexionBD();//L
				conectarABDEnTransaccion();//L
			}
			
			if (conn != null) {
				if (conn.isClosed()) {
					conectarABDEnTransaccion();//L
				}
			}
			
			if (conn == null)
			{
				return "No me pude conectar a la base de datos... Conn:" +conexion+"-User:"+username+"-Pass:"+password+"-Excepción:"+excepcion;
			}

			stmt = conn.createStatement();

			if (stmt.execute(sql))
			{
				rset = stmt.getResultSet();
				
				if(rset.next())
				{
					dato = rset.getString(1);
					if (dato == null) dato = valorSiEsNulo;
				}

				rset.close();
			}
			
			stmt.close();
			
			return dato;
		}
		catch (SQLException e)
		{
			ultimoError = "["+ Auxiliar.nz(sql, "").replace("'", "`")+"]:" + e.toString();
		}
		catch (Exception e)
		{
			ultimoError = "["+ Auxiliar.nz(sql, "").replace("'", "`")+"]:" + e.toString();
		}
		
		return dato;		
	}
	
	/**
	 * Convierte un resultset a un map de resultados
	 * 
	 * @param rs
	 * @return mapa de resultados
	 * @throws SQLException
	 */
	private Map<String, List<Object>> convertirResultSetAMapa(ResultSet rs) throws SQLException {
	    ResultSetMetaData md = rs.getMetaData();
	    int columns = md.getColumnCount();
	    Map<String, List<Object>> map = new HashMap<String, List<Object>>(columns);
	    for (int i = 1; i <= columns; ++i) {
	        map.put(md.getColumnName(i), new ArrayList<Object>());
	    }
	    while (rs.next()) {
	        for (int i = 1; i <= columns; ++i) {
	            map.get(md.getColumnName(i)).add(rs.getObject(i));
	        }
	    }

	    return map;
	}
	
	/**
	 * Funcion que retorna un mapa de resultados de una consulta a la BD
	 * 
	 * @param sql
	 * @return
	 */
	public Map MapBD(String sql) {
		Map mapa = null;
		
		Statement stmt = null;
		ResultSet rset = null;

		if (!Auxiliar.tieneAlgo(sql)) return null;
		
		try
		{
			if (conn == null) {
				conectarABDEnTransaccion();//L
			}
			
			if (conn != null) {
				if (conn.isClosed()) {
					conectarABDEnTransaccion();//L
				}
			}
			
			if (conn == null)
			{
				ultimoError = "No hay conexión con la BD en el servidor:" + conexion;
				return null;
			}

			
			try {
				stmt = conn.createStatement();
	
				if (stmt.execute(sql))
				{
					rset = stmt.getResultSet();
					if (rset != null) {
						mapa = convertirResultSetAMapa(rset); 
					}
					rset.close();
				}
				
				stmt.close();
			}
			finally {
				try {
					stmt.close();
				}
				catch (Exception ignore) {}
			}
			
			return null;
		}
		catch (SQLException e)
		{
			try {
				rset.close();
				stmt.close();
			}
			catch (Exception ignore) {}
			ultimoError = "["+ Auxiliar.nz(sql, "").replace("'", "`")+"]:" + e.toString();
			System.out.println(ultimoError + "["+sql+"]");
		}
		catch (Exception e)
		{
			try {
				rset.close();
				stmt.close();
			}
			catch (Exception ignore) {}
			ultimoError = e.toString();
			System.out.println(ultimoError + "["+sql+"]");
		}
		
		return mapa;
	}
	
	/**
	 * Convierte un resultset a una lista de resultados
	 * 
	 * @param rs
	 * @return lista de resultados
	 * @throws SQLException
	 */
	public List<HashMap<String,Object>> convertirResultSetALista(ResultSet rs) throws SQLException {
	    ResultSetMetaData md = rs.getMetaData();
	    int columns = md.getColumnCount();
	    List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

	    while (rs.next()) {
	        HashMap<String,Object> row = new HashMap<String, Object>(columns);
	        for(int i=1; i<=columns; ++i) {
	            row.put(md.getColumnName(i),rs.getObject(i));
	        }
	        list.add(row);
	    }

	    return list;
	}
	
	/**
	 * Funcion que retorna una lista de resultados de una consulta a la BD
	 * 
	 * @param sql
	 * @return lista de resultados de consulta
	 */
	public List<HashMap<String,Object>> ListaBD(String sql) {
		List<HashMap<String,Object>> lista = null;
		
		Statement stmt = null;
		ResultSet rset = null;
		
		if (!Auxiliar.tieneAlgo(sql)) return null;
		
		try
		{
			if (conn == null) {
				conectarABDEnTransaccion();//L
			}
			
			if (conn != null) {
				if (conn.isClosed()) {
					conectarABDEnTransaccion();//L
				}
			}
			
			if (conn == null)
			{
				ultimoError = "No hay conexión con la BD en el servidor: " + conexion;
				return null;
			}
			
			
			try {
				stmt = conn.createStatement();
				
				if (stmt.execute(sql))
				{
					rset = stmt.getResultSet();
					if (rset != null) {
						lista = convertirResultSetALista(rset); 
						rset.close();
						stmt.close();

						return lista;
					}
				}
			}
			finally {
				try {
					stmt.close();
				}
				catch (Exception ignore) {}
			}
			
			return null;
		}
		catch (SQLException e)
		{
			try {
				rset.close();
				stmt.close();
			}
			catch (Exception ignore) {}
			ultimoError = "["+ Auxiliar.nz(sql, "").replace("'", "`")+"]:" + e.toString();
			System.out.println(ultimoError + "["+sql+"]");
		}
		catch (Exception e)
		{
			try {
				rset.close();
				stmt.close();
			}
			catch (Exception ignore) {}
			ultimoError = e.toString();
			System.out.println(ultimoError + "["+sql+"]");
		}
		
		return null;

	}
	
	/**
	 * Funcion que ejecuta una consulta de selección y retorna el conjunto de resultados
	 * 
	 * @param sql String: consulta SQL a la base de datos
	 * @return ResultSet: conjunto de resultados
	 * @throws SQLException 
	 */
	public ResultSet consultarBD(String sql) throws SQLException
	{
		Statement stmt = null;
		ResultSet rset = null;

		if (!Auxiliar.tieneAlgo(sql)) return null;
		
		try
		{
			if (conn == null) {
				//conn = conexionBD();//L
				conectarABDEnTransaccion();//L
			}
			
			if (conn != null) {
				if (conn.isClosed()) {
					conectarABDEnTransaccion();//L
				}
			}
			
			if (conn == null)
			{
				ultimoError = "No hay conexión con la BD en el servidor:" + conexion;
				return null;
			}

			
			try {
				stmt = conn.createStatement();
	
				if (stmt.execute(sql))
				{
					rset = stmt.getResultSet();
					//stmt.close();
	
				    return rset;
				}
				else {
					//stmt.close();
				}
			}
			finally {
				try {
					//stmt.close();
				}
				catch (Exception ignore) {}
			}
			
			return null;
		}
		catch (SQLException e)
		{
			try {
				//rset.close();
				//stmt.close();
			}
			catch (Exception ignore) {}
			ultimoError = "["+ Auxiliar.nz(sql, "").replace("'", "`")+"]:" + e.toString();
			System.out.println(ultimoError + "["+sql+"]");
		}
		catch (Exception e)
		{
			try {
				//rset.close();
				//stmt.close();
			}
			catch (Exception ignore) {}
			ultimoError = e.toString();
			System.out.println(ultimoError + "["+sql+"]");
		}
		
		return null;
	}
	
	
	/**
	 * Funcion que ejecuta una consulta de sql
	 * 
	 * @param sql String: consulta SQL a la base de datos
	 * @return resultado String: "1" si tuvo exito, "0" si no tuvo exito
	 * @throws SQLException 
	 */
	public boolean ejecutarSQL(String sql) throws SQLException
	{
		Statement stmt = null;

		boolean resultado = false;
		
		int r = 0;
		
		if (!Auxiliar.tieneAlgo(sql)) return false;
		
		try
		{
			if (conn == null) {
				//conn = conexionBD();//L
				conectarABDEnTransaccion();//L
			}
			
			if (conn == null) {
				ultimoError = "La conexión a la base de datos es nula.";
				resultado = false;
			}
			else {
				if (conn.isClosed()) {
					ultimoError = "La conexión a la base de datos esta cerrada.";
					resultado = false;
				}
				else {
					stmt = conn.createStatement();

					r = stmt.executeUpdate(sql);
					
					if (r >= 0)
					{
						resultado = true;
					}
					else
					{
						ultimoError = "Error al ejecutar SQL:" + Auxiliar.nz(sql, "").replace("'", "`");
						resultado = false;
					}

					stmt.close();
				}
			}
		}
		catch (SQLException e)
		{
			ultimoError = e.toString() + "--SQL:" + Auxiliar.nz(sql, "").replace("'", "`");
			resultado = false;
		}
		catch (Exception e)
		{
			ultimoError = e.toString() + "--SQL:" + Auxiliar.nz(sql, "").replace("'", "`");
			resultado = false;
		}
		
		return resultado;
	}
	
	/**
	 * Función para cambiar de modo de cometer las transacciones automáticamente o no
	 * 
	 * @param auto_cometer
	 */
	public void establecerAutoCometer(boolean auto_cometer){
		if (conn == null) { 
			return;
		}
		
		try {
			if (conn.isClosed()) return;

			conn.setAutoCommit(auto_cometer);
		} catch (SQLException e) {
			e.printStackTrace();
			this.ultimoError = e.toString();
		}
	}
	
	
	/**
	 * Función para cometer una transacción
	 */
	public void cometerTransaccion() {
		if (conn == null) { 
			return;
		}
		
		try {
			if (!conn.getAutoCommit())
			{
				try {
					conn.commit();
					desconectarse();
				} catch (SQLException e) {
					e.printStackTrace();
					this.ultimoError = e.toString();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			this.ultimoError = e.toString();
		}
	}
	
	/**
	 * Función para deshacer una transacción
	 */
	public void deshacerTransaccion() {
		if (conn == null) { 
			return;
		}
		
		try {
			if (!conn.getAutoCommit())
			{
				try {
					conn.rollback();
					
					desconectarse();
				} catch (SQLException e) {
					e.printStackTrace();
					this.ultimoError = e.toString();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			this.ultimoError = e.toString();
		}
	}
	
	/**
	 * Funcion para desconectarse de la base de datos
	 * @return
	 */
	public boolean desconectarse() {
		try {
			if (conn == null) return true;
			
			if (conn.isClosed()) return true;
			
			conn.rollback();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			this.ultimoError = e.toString();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Destruye la conexión a la base de datos
	 */
	public void destroy()
	{
		Statement stmt = null;
		ResultSet rset = null;

		try {
			if (rset != null && !rset.isClosed()) rset.close();
			if (stmt != null && !stmt.isClosed()) stmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			this.ultimoError = e.toString();
		}
	}
	

}//443
