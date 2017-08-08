package co.gov.ideamredd.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import co.gov.ideamredd.conexionBD.ConexionBDParcelas;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.ContactoParcela;
import co.gov.ideamredd.entities.Depto;
import co.gov.ideamredd.entities.Municipios;
import co.gov.ideamredd.entities.Organizacion;
import co.gov.ideamredd.entities.Pais;
import co.gov.ideamredd.entities.Proposito;
import co.gov.ideamredd.entities.TipoInventario;
import co.gov.ideamredd.util.Util;

@Stateless
public class MetadatoParcela {

	@EJB
	private ConsultarAsociadosParcela consultarAsociadosParcela;

	@EJB
	private ConexionBDParcelas conexionBD;

	@EJB
	private consultarAsociadosContactos consultarAsociadosContactos;

	@EJB
	private ConsultarCrucesMetadato consultarCrucesMetadato;

	private Connection conn;
	private Integer idParcela;
	private Integer idTipoInventario;
	private String coor;
	private String[] coordenadas;
	private ArrayList<String> metadato = new ArrayList<String>();
	private ArrayList<String> custodio = new ArrayList<String>();
	private ArrayList<String> investigador = new ArrayList<String>();
	private ArrayList<String> propietario = new ArrayList<String>();
	private ArrayList<String> parcela = new ArrayList<String>();
	private Depto depto;
	private Municipios municipio;
	private Proposito proposito = new Proposito();
	private ArrayList<Municipios> municipios = new ArrayList<Municipios>();
	private ArrayList<Depto> departamentos = new ArrayList<Depto>();
	private TipoInventario inventario;
	private ArrayList<String> comunidad = new ArrayList<String>();
	private ArrayList<String> resguardo = new ArrayList<String>();
	private ArrayList<String> parques = new ArrayList<String>();
	private ArrayList<String> infoMeta = new ArrayList<String>();
	private String nombre = "";
	private String kw = "";
	private String inv;
	private boolean isModificacion=false;

	@SuppressWarnings("resource")
	public void leerPlantilla() {
		try {
			consultarDatosMetadatos();
			consultarParcela();
			consultarGeoParcela();
			coordenadas = Util.obtenerMaxMinCoordenadas(coor);
			infoMeta.add(coordenadas[0] + ";" + coordenadas[2] + ";"
					+ coordenadas[1] + ";" + coordenadas[3]);
			custodio = consultarContactos(consultarAsociadosParcela
					.consultaContactoPorParcelaClase(idParcela,
							Integer.valueOf(Constantes.opcionCustodio)));
			propietario = consultarContactos(consultarAsociadosParcela
					.consultaContactoPorParcelaClase(idParcela,
							Integer.valueOf(Constantes.opcionPropietario)));
			investigador = consultarContactos(consultarAsociadosParcela
					.consultaContactoPorParcelaClase(idParcela,
							Integer.valueOf(Constantes.opcionInv)));
			BufferedReader entrada = new BufferedReader(
					new FileReader(
							getRutaMetadatos("plantilla_metadato")
									+ "Plantilla_Metadatos_parcelas_Inventario_Forestal.txt"));
			FileWriter fw = new FileWriter(getRutaMetadatos("ruta_metadatos")
					+ nombre + ".xml");
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter salida = new PrintWriter(bw);
			String renglon = "";
			int indiceMetadato = 0;
			int indiceCustodio = 0;
			int indicePropietario = 0;
			int indiceParcela = 0;
			int indicePalabras = 0;
			int indiceInvestigador = 0;
			Calendar c = Calendar.getInstance();
			while ((renglon = entrada.readLine()) != null) {
				if (renglon.contains("@@@")) {
					String[] s = renglon.split("@@@");
					renglon = s[0] + metadato.get(indiceMetadato) + s[1];
					indiceMetadato++;
				} else if (renglon.contains("tiempoGeneracion")) {
					String[] s = renglon.split("tiempoGeneracion");
					String f = c.get(Calendar.YEAR) + "-"
							+ (c.get(Calendar.MONTH) + 1) + "-"
							+ c.get(Calendar.DAY_OF_MONTH) + "T"
							+ c.get(Calendar.HOUR) + ":"
							+ c.get(Calendar.MINUTE) + ":"
							+ c.get(Calendar.SECOND);
					renglon = s[0] + f + s[1];
					infoMeta.add(f);
				} else if (renglon.contains("ccc")) {
					String[] s = renglon.split("ccc");
					if (s.length > 1)
						renglon = s[0] + custodio.get(indiceCustodio) + s[1];
					else
						renglon = custodio.get(indiceCustodio);
					indiceCustodio++;
				} else if (renglon.contains("ppp")) {
					String[] s = renglon.split("ppp");
					renglon = s[0] + parcela.get(indiceParcela) + s[1];
					indiceParcela++;
				} else if (renglon.contains("iii")) {
					if (investigador.size() != 0) {
						inv = Util.infoMetadato("investigador");
						String h[] = inv.split(",");
						inv = "";
						for (int i = 0; i < h.length - 1; i++) {
							if (h[i].equals("iii")) {
								h[i] = investigador.get(indiceInvestigador);
								inv += h[i];
								indiceInvestigador++;
							} else
								inv += h[i];
						}
						inv += h[h.length - 1];
					} else
						inv = Util.infoMetadato("investigadorVacio");
					renglon = inv;
				} else if (renglon.contains("fff")) {
					String[] s = renglon.split("fff");
					if (s.length > 1)
						renglon = s[0] + propietario.get(indicePropietario)
								+ s[1];
					else
						renglon = propietario.get(indicePropietario);
					indicePropietario++;
				} else if (renglon.contains("kkk")) {
					renglon = "";
					if (indicePalabras == 0) {
						inventario = consultarAsociadosParcela
								.ConsultarTipoInventarioParcela(idTipoInventario);

						renglon += crearKeyword("Parcela");
						renglon += crearKeyword("Biomasa");
						kw = "Parcela;Biomasa";
						if (inventario != null) {
							renglon += crearKeyword(inventario.getNombre());
							kw = kw + ";" + inventario.getNombre();
						}

						indicePalabras++;
					} else if (indicePalabras == 1) {
						comunidad = consultarCrucesMetadato
								.consultaComunidadParcela(idParcela);
						parques = consultarCrucesMetadato
								.consultaParqueParcela(idParcela);
						resguardo = consultarCrucesMetadato
								.consultaResguardoParcela(idParcela);
						renglon += crearKeyword("Colombia");
						kw = kw + ";Colombia";
						for (int i = 0; i < comunidad.size(); i++) {
							renglon += crearKeyword(comunidad.get(i));
							kw = kw + ";" + comunidad.get(i);
						}
						for (int j = 0; j < parques.size(); j++) {
							renglon += crearKeyword(parques.get(j));
							kw = kw + ";" + parques.get(j);
						}
						for (int k = 0; k < resguardo.size(); k++) {
							renglon += crearKeyword(resguardo.get(k));
							kw = kw + ";" + resguardo.get(k);
						}
						indicePalabras++;
					} else if (indicePalabras == 2) {
						departamentos = consultarAsociadosParcela
								.consultaDeptoParcela(idParcela);
						municipios = consultarAsociadosParcela
								.ConsultaMunicipioParcela(idParcela);
						for (int i = 0; i < departamentos.size(); i++) {
							depto = departamentos.get(i);
							renglon += crearKeyword(depto.getNombre());
							kw = kw + ";" + depto.getNombre();
						}
						for (int j = 0; j < municipios.size(); j++) {
							municipio = municipios.get(j);
							renglon += crearKeyword(municipio.getNombre());
							kw = kw + ";" + municipio.getNombre();
						}
					}

				} else if (renglon.contains("minX")) {
					String[] s = renglon.split("minX");
					renglon = s[0] + coordenadas[2] + s[1];
				} else if (renglon.contains("minY")) {
					String[] s = renglon.split("minY");
					renglon = s[0] + coordenadas[3] + s[1];
				} else if (renglon.contains("maxX")) {
					String[] s = renglon.split("maxX");
					renglon = s[0] + coordenadas[0] + s[1];
				} else if (renglon.contains("maxY")) {
					String[] s = renglon.split("maxY");
					renglon = s[0] + coordenadas[1] + s[1];
				}
				salida.println(renglon);
			}
			infoMeta.add(kw);
			if(isModificacion()){
				eliminarMetada();
				registrarMetadata();
			}else{
				registrarMetadata();
			}
			salida.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void eliminarMetada() {
		try{
			conn = conexionBD.establecerConexion();
			CallableStatement eliminarMetadatoView = conn
					.prepareCall("{call RED_PK_PARCELAS.eliminarMetadataview(?,?)}");
			eliminarMetadatoView.setInt("una_parcela", idParcela);
			eliminarMetadatoView.registerOutParameter("un_resultado", OracleTypes.VARCHAR);
			eliminarMetadatoView.execute();
			System.out.println(eliminarMetadatoView.getObject("un_Resultado"));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private void registrarMetadata() {
		try {
			String infoParcela, infoCoord, infoCont1, infoCont2, infoCont3, infoFecha, infoKey;    
			if(infoMeta.size()==6){
				infoParcela=infoMeta.get(0);
				infoCoord=infoMeta.get(1);
				infoCont1=infoMeta.get(2);
				infoCont2=infoMeta.get(3);
				infoFecha=infoMeta.get(4);
				infoKey=infoMeta.get(5);
				infoCont3="";
			}else{
				infoParcela=infoMeta.get(0);
				infoCoord=infoMeta.get(1);
				infoCont1=infoMeta.get(2);
				infoCont2=infoMeta.get(3);
				infoCont3=infoMeta.get(4);
				infoFecha=infoMeta.get(5);
				infoKey=infoMeta.get(6);
			}
			conn = conexionBD.establecerConexion();
			CallableStatement registrarMetadatoView = conn
					.prepareCall("{call RED_PK_PARCELAS.insertarMetadataview(?,?,?,?,?,?,?,?,?)}");
			registrarMetadatoView.setInt("una_parcela", idParcela);
			registrarMetadatoView.setString("una_infoParcela", infoParcela);
			registrarMetadatoView.setString("una_infoCoord", infoCoord);
			registrarMetadatoView.setString("ina_infoCont1", infoCont1);
			registrarMetadatoView.setString("una_infoCont2", infoCont2);
			registrarMetadatoView.setString("una_infoCont3", infoCont3);
			registrarMetadatoView.setString("una_infoFecha", infoFecha);
			registrarMetadatoView.setString("una_infokey", infoKey);
			registrarMetadatoView.registerOutParameter("un_resultado", OracleTypes.VARCHAR);
			registrarMetadatoView.execute();
			System.out.println(registrarMetadatoView.getObject("un_Resultado"));
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void consultarDatosMetadatos() throws Exception {
		conn = conexionBD.establecerConexion();
		CallableStatement consultarMetadatoInfo = conn
				.prepareCall("{call RED_PK_PARCELAS.ConsultaMetadatoInfo(?)}");
		consultarMetadatoInfo.registerOutParameter("un_Resultado",
				OracleTypes.CURSOR);
		consultarMetadatoInfo.execute();
		OracleResultSet resultSet = (OracleResultSet) consultarMetadatoInfo
				.getObject("un_Resultado");
		while (resultSet.next()) {
			metadato.add(resultSet.getString(2));
			metadato.add(resultSet.getString(3));
			metadato.add(resultSet.getString(4));
			metadato.add(resultSet.getString(5));
			metadato.add(resultSet.getString(6));
			metadato.add(resultSet.getString(7));
			metadato.add(resultSet.getString(8));
			metadato.add(resultSet.getString(9));
			metadato.add(resultSet.getString(10));
			metadato.add(resultSet.getString(11));
			metadato.add(resultSet.getString(12));
			metadato.add(resultSet.getString(13));
			metadato.add(resultSet.getString(14));
			metadato.add(resultSet.getString(15));
			metadato.add(resultSet.getString(16));
			metadato.add(resultSet.getString(17));
		}
		resultSet.close();
		conn.close();
	}

	/**
	 * Obtiene la ruta del archivo providers.fac
	 * 
	 * @return rutaProviders ruta en la cual se encuentra el archivo
	 *         providers.fac, la obtiene de la base de datos
	 */
	public String getRutaMetadatos(String clave) {
		String rutaProviders = "";
		try {
			// log.info(ActualizarParametro.class+" Inicio get ruta providers");
			conn = conexionBD.establecerConexion();
			CallableStatement consultaRutaProvider = conn
					.prepareCall("{call RED_PK_PARAMETROS.Parametro_Consulta_RP(?, ?, ?)}");
			consultaRutaProvider.setString("un_Nombre", clave);
			consultaRutaProvider.registerOutParameter("una_Ruta",
					OracleTypes.CURSOR);
			consultaRutaProvider.registerOutParameter("sentencia",
					OracleTypes.VARCHAR);
			consultaRutaProvider.execute();
			OracleResultSet r = (OracleResultSet) consultaRutaProvider
					.getObject("una_Ruta");

			while (r.next()) {
				rutaProviders = r.getString(1);
			}

			r.close();
			consultaRutaProvider.close();
			conn.close();
			// log.info(ActualizarParametro.class+
			// "[modificarParametros] Termino");
		} catch (Exception e) {
			// log.error(ActualizarParametro.class+
			// "[modificarParametros] Fallo, no se puede obtener la ruta PROVIDERS de la tabpa RED_PARAMETROS",e);
			e.printStackTrace();
		}
		return rutaProviders;
	}

	private ArrayList<String> consultarContactos(ContactoParcela contactoParcela)
			throws SQLException {
		ArrayList<String> contacto = new ArrayList<String>();
		Contacto c;
		Organizacion o;
		Municipios m = null;
		Pais p = null;
		if (contactoParcela != null)
			if (contactoParcela.getIdContacto() != 0) {
				c = consultarAsociadosParcela
						.ConsultarContactoId(contactoParcela.getIdContacto());
				m = consultarAsociadosContactos.ConsultarMunicipio(c
						.getMunicipio());
				p = consultarAsociadosContactos.ConsultarPais(c.getPais());
				contacto.add(c.getNombre());
				contacto.add(organizarInfoContacto("organisationName", null));
				contacto.add(organizarInfoContacto("positionName", null));
				contacto.add(organizarInfoContacto("voice", c.getMovil()));
				contacto.add(organizarInfoContacto("deliveryPoint", null));
				contacto.add(organizarInfoContacto("city", m.getNombre()));
				contacto.add(organizarInfoContacto("country", p.getNombre()));
				contacto.add(c.getCorreo());
				infoMeta.add(c.getNombre() + ";" + c.getMovil() + ";"
						+ m.getNombre() + ";" + p.getNombre() + ";"
						+ c.getCorreo());
			} else {
				o = consultarAsociadosParcela
						.ConsultarOrganizacionId(contactoParcela
								.getIdOrganizacion());
				m = consultarAsociadosContactos.ConsultarMunicipio(o
						.getMunicipio());
				p = consultarAsociadosContactos.ConsultarPais(o.getPais());
				contacto.add(o.getNombre());
				contacto.add(organizarInfoContacto("organisationName",
						o.getNombre()));
				contacto.add(organizarInfoContacto("positionName", null));
				contacto.add(organizarInfoContacto("voice", o.getTelefono()));
				contacto.add(organizarInfoContacto("deliveryPoint",
						o.getDireccion()));
				contacto.add(organizarInfoContacto("city", m.getNombre()));
				contacto.add(organizarInfoContacto("country", p.getNombre()));
				contacto.add(o.getCorreo());
				infoMeta.add(o.getNombre() + ";" + o.getTelefono() + ";"
						+ m.getNombre() + ";" + p.getNombre() + ";"
						+ o.getCorreo());
			}
		return contacto;
	}

	private String organizarInfoContacto(String propiedad, Object info) {
		String p = "";
		if (info == null)
			p = "<gmd:"
					+ propiedad
					+ " gco:nilReason=\"missing\">\n</gco:CharacterString>\n</gmd:"
					+ propiedad + ">";
		else
			p = "<gmd:" + propiedad + ">\n<gco:CharacterString>"
					+ info.toString() + "</gco:CharacterString>\n</gmd:"
					+ propiedad + ">";
		return p;
	}

	private ArrayList<String> consultarParcela() throws SQLException {
		conn = conexionBD.establecerConexion();
		String titulo = "";
		CallableStatement consultarParcela = conn
				.prepareCall("{call RED_PK_PARCELAS.Consultar_ParcelaId(?,?)}");
		consultarParcela.setInt("una_parcela", idParcela);
		consultarParcela.registerOutParameter("un_Resultado",
				OracleTypes.CURSOR);
		consultarParcela.execute();
		OracleResultSet resultSet = (OracleResultSet) consultarParcela
				.getObject("un_Resultado");
		while (resultSet.next()) {
			if (resultSet.getString(2) == null
					|| resultSet.getString(2).equals(""))
				titulo = resultSet.getObject(1).toString() + "/"
						+ resultSet.getString(32);
			else
				titulo = resultSet.getObject(1).toString() + "/"
						+ resultSet.getString(2);
			proposito = consultarAsociadosParcela
					.ConsultarPropositoParcela(Integer.valueOf(resultSet
							.getInt(21)));
			parcela.add(titulo);
			parcela.add(resultSet.getString(4));
			parcela.add(resultSet.getString(16));
			parcela.add(proposito.getNombre());
			parcela.add(resultSet.getString(12) != null ? resultSet
					.getString(12) : "SIN OBSERVACIONES");
			idTipoInventario = resultSet.getInt(18);
			String obs=resultSet.getString(12) != null ? resultSet
					.getString(12) : "SIN OBSERVACIONES";
			String des=resultSet.getString(16);
			String prop=proposito.getNombre();
			infoMeta.add(titulo + ";"
					+ prop + ";" + des
					+ ";" + obs);
		}
		resultSet.close();
		consultarParcela.close();
		conn.close();
		return parcela;
	}

	public String crearKeyword(String key) {
		String linea = "<gmd:keyword>\n" + "<gco:CharacterString>" + key
				+ "</gco:CharacterString>\n" + "</gmd:keyword>\n";
		return linea;
	}

	private void consultarGeoParcela() {
		String[] g = consultarAsociadosParcela.ConsultarGeoParcela(idParcela);
		coor = g[1];
	}

	public Integer getIdParcela() {
		return idParcela;
	}

	public void setIdParcela(Integer idParcela) {
		this.idParcela = idParcela;
	}

	public String getCoor() {
		return coor;
	}

	public void setCoor(String coor) {
		this.coor = coor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isModificacion() {
		return isModificacion;
	}

	public void setModificacion(boolean isModificacion) {
		this.isModificacion = isModificacion;
	}
}
