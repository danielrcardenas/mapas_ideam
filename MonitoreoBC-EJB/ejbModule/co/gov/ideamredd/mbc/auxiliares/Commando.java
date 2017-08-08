// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
package co.gov.ideamredd.mbc.auxiliares;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import co.gov.ideamredd.mbc.auxiliares.*;

public class Commando extends Thread {

	InputStream	is;
	String		type;

	public Commando(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				System.out.println(type + "> " + line);
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}