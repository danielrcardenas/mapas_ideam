package co.gov.ideamredd.rserver.conexion;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import co.gov.ideamredd.conexionBD.Parametro;

public class RserverConexion {

	static private LinkedList<String> procesos;
	// static private String serialActual=null;
	private RConnection rc;
	private static RserverConexion yo;

	protected String createConnection() {

		// making connection object
		try {
			if (rc == null) {
				String host = Parametro.getParametro("Rhost");
				int port = Integer.parseInt(Parametro.getParametro("Rport"));
				rc = new RConnection(host, port);

			}
			if (!rc.isConnected()) {
				String host = Parametro.getParametro("Rhost");
				int port = Integer.parseInt(Parametro.getParametro("Rport"));
				rc = new RConnection(host, port);

			}
			String serial = UUID.randomUUID().toString();
			procesos.add(serial);
			return serial;

		} catch (RserveException e) {
			// log.error("It can not connect to Rserver, check conection parameters for R server");
			e.printStackTrace();
			return "";
		}
	}

	// Ruta hacia la carpeta donde se encuentran los datos y el script
	private RserverConexion() {
		procesos = new LinkedList<String>();
	}

	public static RserverConexion getConnector() {
		if (yo == null) {
			yo = new RserverConexion();

		}
		return yo;
	}

	public String connect() {
		return createConnection();
	}

	public void setWorkspace(String token, String path) {
		if (procesos.peek() == token) {
			try {
				String ruta = path.replace("\\", "/");
				rc.eval("setwd(\"" + ruta + "\")");
			} catch (RserveException e) {

				e.printStackTrace();
			}
		}
	}

	public String currentToken() {
		return procesos.peek();
	}

	public boolean closeConnection(String token) {
		try {
			procesos.remove(procesos.indexOf(token));
			if (procesos.isEmpty())
				return rc.close();
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public REXP ejecutar(String token, String command) {

		try {
			if (procesos.peek() == token) {
				return rc.eval(command);

			}

		} catch (Exception e) {
		}
		return null;
	}

	// Ruta hacia un script R espec�fico
	public void ejecutarScript(String ruta, String token) {
		try {
			if (procesos.peek() == token) {
				ruta = ruta.replace("\\", "/");
				rc.eval("source(\"" + ruta + "\")");
			}
		} catch (RserveException e) {

			e.printStackTrace();
		}
	}

}
