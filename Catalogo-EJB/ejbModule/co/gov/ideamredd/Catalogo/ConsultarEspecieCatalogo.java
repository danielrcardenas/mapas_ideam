package co.gov.ideamredd.Catalogo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import co.gov.ideamredd.conexionBD.ConexionBD;
import co.gov.ideamredd.entities.Especie;
import co.gov.ideamredd.util.SMBC_Log;


/**
 * ConsultarEspecieCatalogo.java Clase que consulta los
 * datos de las especies que estan en el catalogo.
 * 
 * SMBC Proyecto: Sistema de Monitoreo de Bosques y Carbono
 * 
 * @author Julio Cesar Sanchez Torres Nov 18 de 2013
 */
@Stateless
public class ConsultarEspecieCatalogo {

	@EJB
	ConexionBD conexion;
	private Connection conn;
	private Logger log;
	private CallableStatement consultarCatalogo;
	private ResultSet resultSet;

	/**
	 * Metodo que hace una consulta basica del catalogo
	 * 
	 * @param cadena
	 *            : cadena de texto a buscar.
	 */
	public void consultaBasicaEspecie(String cadena, ArrayList<Especie> listaEspecies) {
		try {
//			log = SMBC_Log.Log(ConsultarEspecieCatalogo.class);
			// conn = ConexionBD.establecerConexion();
			conn = conexion.establecerConexion();

			// Se obtiene el nombre de la parcela
			consultarCatalogo = conn
					.prepareCall("{call RED_PK_CATALOGO.Consulta_EspeciesBasico(?,?,?)}");
			consultarCatalogo.setString("una_cadena", cadena);
			consultarCatalogo.registerOutParameter("un_Resultado",
					OracleTypes.CURSOR);
			consultarCatalogo.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultarCatalogo.execute();

			System.out.println(consultarCatalogo.getObject("sentencia"));

			resultSet = (ResultSet) consultarCatalogo.getObject("un_Resultado");

			while (resultSet.next()) {
				Especie especie = new Especie();
				especie.setEspecieId(resultSet.getInt(1));
				especie.setReino(resultSet.getString(2));
				especie.setDivision(resultSet.getString(3));
				especie.setClase(resultSet.getString(4));
				especie.setOrden(resultSet.getString(5));
				especie.setFamilia(resultSet.getString(6));
				especie.setGenero(resultSet.getString(7));
				especie.setEspecie(resultSet.getString(8));
				especie.setEspecieNme(resultSet.getString(9));
				especie.setUICN(resultSet.getString(10));
				especie.setCITES(resultSet.getString(11));
				especie.setFmlFmtpCode(resultSet.getString(12));
				especie.setfmlCode(resultSet.getString(13));
				especie.setTimestamp(resultSet.getString(14));
				especie.setUserId(resultSet.getInt(15));
				especie.setCodigoForestal(resultSet.getInt(16));

				listaEspecies.add(especie);
			}

			log.info("[consultaBasicaEspecie] Termino");
			resultSet.close();
			consultarCatalogo.close();
			conn.close();
		} catch (Exception e) {
			log.error("[consultaBasicaEspecie] Fallo");
			e.printStackTrace();
		}
	}

}
