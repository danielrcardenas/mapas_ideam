package co.gov.ideamredd.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadException;
import javazoom.upload.UploadFile;
import co.gov.ideamredd.dao.ConsultarAsociadosParcela;
import co.gov.ideamredd.dao.ModificarParcela;
import co.gov.ideamredd.entities.Contacto;
import co.gov.ideamredd.entities.Organizacion;
import co.gov.ideamredd.util.Util;

public class ModificarParcelaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private ModificarParcela parcela;
    @EJB
    private ConsultarAsociadosParcela asociadosParcela;

    private Contacto contactoPropietario;
    private Contacto contactoCustodio;
    private Contacto contactoFGDA;
    private Contacto contacto;
    private Organizacion organizacionFGDA;
    private Organizacion organizacionPropietario;
    private Organizacion organizacionCustodio;
    private Organizacion organizacion;
    private static final String archivoParcela = "co/gov/ideamredd/dao/parcela";
    private static final ResourceBundle par = ResourceBundle
	    .getBundle(archivoParcela);

    MultipartFormDataRequest mrequest = null;
    UploadBean upBean = null;
    String ubicacionArchivo = Util.obtenerClave("parcela.ruta.archivo.fotos",
	    par);
    UploadFile file;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	try {
//	    SMBC_Log.Log(this.getClass());
	    mrequest = new MultipartFormDataRequest(request);
	    @SuppressWarnings("rawtypes")
	    Hashtable files = mrequest.getFiles();
	    file = (UploadFile) files.get("archivo");
	    upBean = new UploadBean();
	    try {
		upBean.setFolderstore(ubicacionArchivo);
		upBean.store(mrequest, "archivo");
	    } catch (UploadException e) {
		e.printStackTrace();
	    }
	    registraParcela();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void registraParcela() {
	try {
	    parcela.setIdParcela(Integer.valueOf(mrequest.getParameter("consecutivoParcela")));
	    parcela.setUn_Nombre(mrequest.getParameter("nombreParcela"));
	    String fechaGeneracion = mrequest.getParameter("fInicial");
	    String[] fg = fechaGeneracion.split("/");
	    fechaGeneracion = fg[1]+"/"+fg[0]+"/"+fg[2];
	    parcela.setUna_Fecha(fechaGeneracion);
	    parcela.setUn_Aprovechamiento(mrequest.getParameter("aprov"));
	    parcela.setUna_Temporalidad(Integer.valueOf(mrequest
		    .getParameter("temporalidad")));
	    parcela.setUn_TipoInventario(Integer.valueOf(mrequest
		    .getParameter("tipoinv")));
	    parcela.setUna_Descripcion(mrequest.getParameter("descparcela"));
	    parcela.setUn_Proposito(Integer.valueOf(mrequest
		    .getParameter("proposito")));
	    parcela.setUnas_Observaciones(mrequest
		    .getParameter("observaciones"));
	    if (!mrequest.getParameter("tieneImagen").equalsIgnoreCase("0")) {
		parcela.setUn_NombreArchivo(file.getFileName());
		parcela.setUna_Ubicacion(Util.obtenerClave(
			"parcela.ruta.archivo.fotos", par));
	    }
	    parcela.setForma(Integer.valueOf(mrequest.getParameter("geometria")));
	    parcela.setCoor(mrequest.getParameter("valor"));
	    parcela.setGeometria(Util.crearCadenaGeometria(
		    Integer.valueOf(mrequest.getParameter("geometria")),
		    mrequest.getParameter("valor")));
	    if (mrequest.getParameter("geometria").equals("1")) {
		parcela.setUn_Radio(BigDecimal.valueOf(Double.valueOf(mrequest
			.getParameter("area"))));
		parcela.setUn_Ancho(new BigDecimal(0));
		parcela.setUn_Largo(new BigDecimal(0));
	    } else {
		parcela.setUn_Radio(new BigDecimal(0));
		parcela.setUn_Ancho(BigDecimal.valueOf(Double.valueOf(/*
								       * mrequest.
								       * getParameter
								       * (
								       * "ancho"
								       * )
								       */1)));
		parcela.setUn_Largo(BigDecimal.valueOf(Double.valueOf(/*
								       * mrequest.
								       * getParameter
								       * (
								       * "largo"
								       * )
								       */1)));
	    }
	    parcela.setUn_InventarioP(mrequest.getParameter("usoinfo"));
	    parcela.setPublica(mrequest.getParameter("tipolicencia"));

	    registrarFGDAParcela();
	    registrarPropietarioParcela();
	    registrarCustodioParcela();

	    if (mrequest.getParameter("isInv").equals(
		    Constantes.registraOtroContacto))
		registrarOtrosContactosParcela(Constantes.claveInvestigador,
			Constantes.opcionInv);
	    if (mrequest.getParameter("isCol").equals(
		    Constantes.registraOtroContacto) || mrequest.getParameter("isEnc").equals(
			    Constantes.registraOtroContacto)) {
		registrarOtrosContactosParcela(Constantes.claveColeccion,
			Constantes.opcionCol);
		registrarOtrosContactosParcela(Constantes.claveEncargado,
			Constantes.opcionEnc);
	    }
	    if (mrequest.getParameter("isBri").equals(
		    Constantes.registraOtroContacto))
		registrarOtrosContactosParcela(Constantes.claveBrigadista,
			Constantes.opcionBri);
	    if (mrequest.getParameter("isSup").equals(
		    Constantes.registraOtroContacto))
		registrarOtrosContactosParcela(Constantes.claveSupervisor,
			Constantes.opcionSup);

	    parcela.setGeometria(Util.crearCadenaGeometria(
		    Integer.valueOf(mrequest.getParameter("geometria")),
		    mrequest.getParameter("valor")));

	    parcela.actualizaParcela();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void registrarFGDAParcela() {
	parcela.getContactos().put("fgda", Constantes.opcionFGDA);
	if (mrequest.getParameter("selfgda").equals("0")) {
	    parcela.getContactos().put("selfgda", Constantes.consultarContacto);
	    if (mrequest.getParameter("claseFGDA").equals(
		    Constantes.personaNatural)) {
		parcela.getContactos().put("tipoContacto",
			Constantes.personaNatural);
		parcela.getContactos().put(
			"contactoFGDA",
			asociadosParcela.ConsultarContactoId(Integer
				.valueOf(mrequest.getParameter("consFGDA"))));
	    } else {
		parcela.getContactos().put("tipoContacto",
			Constantes.personaJuridica);
		parcela.getContactos().put(
			"contactoFGDA",
			asociadosParcela.ConsultarOrganizacionId(Integer
				.valueOf(mrequest.getParameter("consFGDA"))));
	    }
	} else {
	    parcela.getContactos().put("selfgda",
		    mrequest.getParameter("selfgda"));
	    if (mrequest.getParameter("selfgda").equals(
		    Constantes.usuarioSession)) {
		parcela.getContactos().put("tipoContact",
			Constantes.personaNatural);
	    } else if (mrequest.getParameter("selfgda")
		    .equals(Constantes.ideam)) {
		parcela.getContactos().put("tipoContact",
			Constantes.personaJuridica);
		parcela.getContactos().put(
			"contactoFGDA",
			asociadosParcela
				.ConsultarOrganizacionId(Constantes.id_IDEAM));
	    } else if (mrequest.getParameter("selfgda").equals(
		    Constantes.registrarContacto)) {
		if (mrequest.getParameter("claseFGDA").equals(
			Constantes.personaNatural)) {
		    contactoFGDA = new Contacto();
		    contactoFGDA.setNombre(mrequest.getParameter("nomConFGDA"));
		    contactoFGDA.setTelefono(mrequest
			    .getParameter("fijConFGDA"));
		    contactoFGDA.setMovil(mrequest.getParameter("celConFGDA"));
		    contactoFGDA.setCorreo(mrequest.getParameter("corConFGDA"));
		    contactoFGDA.setPais(Integer.valueOf(mrequest
			    .getParameter("paiConFGDA")));
		    parcela.getContactos().put("contactoFGDA", contactoFGDA);
		} else if (mrequest.getParameter("tipoContact").equals(
			Constantes.personaJuridica)) {
		    organizacionFGDA = new Organizacion();
		    organizacionFGDA.setNombre(mrequest
			    .getParameter("nomOrgFGDA"));
		    organizacionFGDA.setSector(Integer.valueOf(mrequest
			    .getParameter("secOrgFGDA")));
		    organizacionFGDA.setTelefono(Integer.valueOf(mrequest
			    .getParameter("telOrgFGDA")));
		    organizacionFGDA.setDireccion(mrequest
			    .getParameter("dirOrgFGDA"));
		    organizacionFGDA.setCorreo(mrequest
			    .getParameter("corOrgFGDA"));
		    organizacionFGDA.setPais(Integer.valueOf(mrequest
			    .getParameter("paiOrgFGDA")));
		    parcela.getContactos()
			    .put("contactoFGDA", organizacionFGDA);
		}
	    } else if (mrequest.getParameter("selfgda").equals(
		    Constantes.consultarContacto)) {
		if (mrequest.getParameter("claseFGDA").equals(
			Constantes.personaNatural)) {
		    parcela.getContactos().put("tipoContacto",
			    Constantes.personaNatural);
		    parcela.getContactos()
			    .put("contactoFGDA",
				    asociadosParcela.ConsultarContactoId(Integer
					    .valueOf(mrequest
						    .getParameter("consFGDA"))));
		} else {
		    parcela.getContactos().put("tipoContacto",
			    Constantes.personaJuridica);
		    parcela.getContactos()
			    .put("contactoFGDA",
				    asociadosParcela.ConsultarOrganizacionId(Integer
					    .valueOf(mrequest
						    .getParameter("consFGDA"))));
		}
	    }
	}
    }

    private void registrarPropietarioParcela() {
	parcela.getContactos().put("propietario", Constantes.opcionPropietario);
	if (mrequest.getParameter("selContPro").equals("0")) {
	    parcela.getContactos().put("selContPro",
		    Constantes.consultarContacto);
	    if (mrequest.getParameter("claseprop").equals(
		    Constantes.personaNatural)) {
		parcela.getContactos().put("tipoProp",
			Constantes.personaNatural);
		parcela.getContactos().put(
			"contactoPropietario",
			asociadosParcela.ConsultarContactoId(Integer
				.valueOf(mrequest.getParameter("consprop"))));
	    } else {
		parcela.getContactos().put("tipoProp",
			Constantes.personaJuridica);
		parcela.getContactos().put(
			"contactoPropietario",
			asociadosParcela.ConsultarOrganizacionId(Integer
				.valueOf(mrequest.getParameter("consprop"))));
	    }
	} else {
	    parcela.getContactos().put("selContPro",
		    mrequest.getParameter("selContPro"));
	    if (mrequest.getParameter("selContPro").equals(
		    Constantes.usuarioSession)) {
		parcela.getContactos().put("tipoConPro",
			Constantes.personaNatural);
	    } else if (mrequest.getParameter("selContPro").equals(
		    Constantes.ideam)) {
		parcela.getContactos().put("tipoConPro",
			Constantes.personaJuridica);
		parcela.getContactos().put(
			"contactoPropietario",
			asociadosParcela
				.ConsultarContactoId(Constantes.id_IDEAM));
	    } else if (mrequest.getParameter("selContPro").equals(
		    Constantes.registrarContacto)) {
		if (mrequest.getParameter("claseprop").equals(
			Constantes.personaNatural)) {
		    contactoPropietario = new Contacto();
		    contactoPropietario.setNombre(mrequest
			    .getParameter("nomConprop"));
		    contactoPropietario.setTelefono(mrequest
			    .getParameter("fijConprop"));
		    contactoPropietario.setMovil(mrequest
			    .getParameter("celConprop"));
		    contactoPropietario.setCorreo(mrequest
			    .getParameter("corConprop"));
		    contactoPropietario.setPais(Integer.valueOf(mrequest
			    .getParameter("paiConprop")));
		    parcela.getContactos().put("contactoPropietario",
			    contactoPropietario);
		} else if (mrequest.getParameter("claseprop").equals(
			Constantes.personaJuridica)) {
		    organizacionPropietario = new Organizacion();
		    organizacionPropietario.setNombre(mrequest
			    .getParameter("nomOrgprop"));
		    organizacionPropietario.setSector(Integer.valueOf(mrequest
			    .getParameter("secOrgprop")));
		    organizacionPropietario.setTelefono(Integer
			    .valueOf(mrequest.getParameter("telOrgprop")));
		    organizacionPropietario.setDireccion(mrequest
			    .getParameter("dirOrgprop"));
		    organizacionPropietario.setCorreo(mrequest
			    .getParameter("corOrgprop"));
		    organizacionPropietario.setPais(Integer.valueOf(mrequest
			    .getParameter("paiOrgprop")));
		    parcela.getContactos().put("contactoPropietario",
			    organizacionPropietario);
		}
	    } else if (mrequest.getParameter("selContPro").equals(
		    Constantes.consultarContacto)) {
		if (mrequest.getParameter("claseprop").equals(
			Constantes.personaNatural)) {
		    parcela.getContactos().put("tipoProp",
			    Constantes.personaNatural);
		    parcela.getContactos()
			    .put("contactoPropietario",
				    asociadosParcela.ConsultarContactoId(Integer
					    .valueOf(mrequest
						    .getParameter("consprop"))));
		} else {
		    parcela.getContactos().put("tipoProp",
			    Constantes.personaJuridica);
		    parcela.getContactos()
			    .put("contactoPropietario",
				    asociadosParcela.ConsultarOrganizacionId(Integer
					    .valueOf(mrequest
						    .getParameter("consprop"))));
		}
	    }
	}
    }

    private void registrarCustodioParcela() {
	parcela.getContactos().put("custodio", Constantes.opcionCustodio);
	if (mrequest.getParameter("selCustodio").equals("0")) {
	    parcela.getContactos().put("selCustodio",
		    Constantes.consultarContacto);
	    if (mrequest.getParameter("clasecus").equals(
		    Constantes.personaNatural)) {
		parcela.getContactos().put("tipoConCus",
			Constantes.personaNatural);
		parcela.getContactos().put(
			"contactoCustodio",
			asociadosParcela.ConsultarContactoId(Integer
				.valueOf(mrequest.getParameter("conscus"))));
	    } else {
		parcela.getContactos().put("tipoConCus",
			Constantes.personaJuridica);
		parcela.getContactos().put(
			"contactoCustodio",
			asociadosParcela.ConsultarOrganizacionId(Integer
				.valueOf(mrequest.getParameter("conscus"))));
	    }
	} else {
	    parcela.getContactos().put("selCustodio",
		    mrequest.getParameter("selCustodio"));
	    if (mrequest.getParameter("selCustodio").equals(
		    Constantes.usuarioSession)) {
		parcela.getContactos().put("tipoConCustodio",
			Constantes.personaNatural);
	    } else if (mrequest.getParameter("selCustodio").equals(
		    Constantes.ideam)) {
		parcela.getContactos().put("tipoConCustodio",
			Constantes.personaJuridica);
		parcela.getContactos().put(
			"contactoCustodio",
			asociadosParcela
				.ConsultarContactoId(Constantes.id_IDEAM));
	    } else if (mrequest.getParameter("selCustodio").equals(
		    Constantes.registrarContacto)) {
		if (mrequest.getParameter("clasecus").equals(
			Constantes.personaNatural)) {
		    contactoCustodio = new Contacto();
		    contactoCustodio.setNombre(mrequest
			    .getParameter("nomConcus"));
		    contactoCustodio.setTelefono(mrequest
			    .getParameter("fijConcus"));
		    contactoCustodio.setMovil(mrequest
			    .getParameter("celConcus"));
		    contactoCustodio.setCorreo(mrequest
			    .getParameter("corConcus"));
		    contactoCustodio.setPais(Integer.valueOf(mrequest
			    .getParameter("paiConcus")));
		    parcela.getContactos().put("contactoCustodio",
			    contactoCustodio);
		} else {
		    organizacionCustodio = new Organizacion();
		    organizacionCustodio.setNombre(mrequest
			    .getParameter("nomOrgcus"));
		    organizacionCustodio.setSector(Integer.valueOf(mrequest
			    .getParameter("secOrgcus")));
		    organizacionCustodio.setTelefono(Integer.valueOf(mrequest
			    .getParameter("telOrgcus")));
		    organizacionCustodio.setDireccion(mrequest
			    .getParameter("dirOrgcus"));
		    organizacionCustodio.setCorreo(mrequest
			    .getParameter("corOrgcus"));
		    organizacionCustodio.setPais(Integer.valueOf(mrequest
			    .getParameter("paiOrgcus")));
		    parcela.getContactos().put("contactoCustodio",
			    organizacionCustodio);
		}
	    } else if (mrequest.getParameter("selCustodio").equals(
		    Constantes.consultarContacto)) {
		if (mrequest.getParameter("clasecus").equals(
			Constantes.personaNatural)) {
		    parcela.getContactos().put("tipoConCus",
			    Constantes.personaNatural);
		    parcela.getContactos()
			    .put("contactoCustodio",
				    asociadosParcela.ConsultarContactoId(Integer
					    .valueOf(mrequest
						    .getParameter("conscus"))));
		} else {
		    parcela.getContactos().put("tipoConCus",
			    Constantes.personaJuridica);
		    parcela.getContactos()
			    .put("contactoCustodio",
				    asociadosParcela.ConsultarOrganizacionId(Integer
					    .valueOf(mrequest
						    .getParameter("conscus"))));
		}
	    }
	}
    }

    private void registrarOtrosContactosParcela(String clave, String tipo) {
	parcela.getContactos().put(clave, tipo);
	if (mrequest.getParameter("sel" + clave).equals("0")) {
	    parcela.getContactos().put("sel" + clave,
		    Constantes.consultarContacto);
	    if (mrequest.getParameter("clase" + clave).equals(
		    Constantes.personaNatural)) {
		parcela.getContactos().put("tipoCon" + clave,
			Constantes.personaNatural);
		parcela.getContactos()
			.put("contacto" + clave,
				asociadosParcela.ConsultarContactoId(Integer
					.valueOf(mrequest.getParameter("cons"
						+ clave))));
	    } else {
		parcela.getContactos().put("tipoCon" + clave,
			Constantes.personaJuridica);
		parcela.getContactos()
			.put("contacto" + clave,
				asociadosParcela
					.ConsultarOrganizacionId(Integer
						.valueOf(mrequest
							.getParameter("cons"
								+ clave))));
	    }
	} else {
	    parcela.getContactos().put("sel" + clave,
		    mrequest.getParameter("sel" + clave));
	    if (mrequest.getParameter("sel" + clave).equals(
		    Constantes.usuarioSession)) {
		parcela.getContactos().put("tipoCont" + clave,
			Constantes.personaNatural);
	    } else if (mrequest.getParameter("sel" + clave).equals(
		    Constantes.ideam)) {
		parcela.getContactos().put("tipoCont" + clave,
			Constantes.personaJuridica);
		parcela.getContactos().put(
			"contacto" + clave,
			asociadosParcela
				.ConsultarContactoId(Constantes.id_IDEAM));
	    } else if (mrequest.getParameter("sel" + clave).equals(
		    Constantes.registrarContacto)) {
		if (mrequest.getParameter("clase" + clave).equals(
			Constantes.personaNatural)) {
		    contacto = new Contacto();
		    contacto.setNombre(mrequest.getParameter("nomCon" + clave));
		    contacto.setTelefono(mrequest.getParameter("fijCon"
			    + clave));
		    contacto.setMovil(mrequest.getParameter("celCon" + clave));
		    contacto.setCorreo(mrequest.getParameter("corCon" + clave));
		    contacto.setPais(Integer.valueOf(mrequest
			    .getParameter("paiCon" + clave)));
		    parcela.getContactos().put("contacto" + clave, contacto);
		} else {
		    organizacion = new Organizacion();
		    organizacion.setNombre(mrequest.getParameter("nomOrg"
			    + clave));
		    organizacion.setSector(Integer.valueOf(mrequest
			    .getParameter("secOrg" + clave)));
		    organizacion.setTelefono(Integer.valueOf(mrequest
			    .getParameter("telOrg" + clave)));
		    organizacion.setDireccion(mrequest.getParameter("dirOrg"
			    + clave));
		    organizacion.setCorreo(mrequest.getParameter("corOrg"
			    + clave));
		    organizacion.setPais(Integer.valueOf(mrequest
			    .getParameter("paiOrg" + clave)));
		    parcela.getContactos()
			    .put("contacto" + clave, organizacion);
		}
	    } else if (mrequest.getParameter("sel" + clave).equals(
		    Constantes.consultarContacto)) {
		if (mrequest.getParameter("clase" + clave).equals(
			Constantes.personaNatural)) {
		    parcela.getContactos().put("tipoCont" + clave,
			    Constantes.personaNatural);
		    parcela.getContactos().put(
			    "contacto" + clave,
			    asociadosParcela.ConsultarContactoId(Integer
				    .valueOf(mrequest.getParameter("cons"
					    + clave))));
		} else {
		    parcela.getContactos().put("tipoCon" + clave,
			    Constantes.personaJuridica);
		    parcela.getContactos().put(
			    "contacto" + clave,
			    asociadosParcela.ConsultarOrganizacionId(Integer
				    .valueOf(mrequest.getParameter("cons"
					    + clave))));
		}
	    }
	}
    }
}
