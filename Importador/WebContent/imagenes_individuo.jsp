<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="co.gov.ideamredd.lenguaje.LenguajeI18N" %>
<%@ page import="java.util.MissingResourceException" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="co.gov.ideamredd.admif.BD" %>
<%@ page import="co.gov.ideamredd.admif.Auxiliar" %>
<%
//co.gov.ideamredd.admif.Auxiliar aux = new co.gov.ideamredd.admif.Auxiliar();
LenguajeI18N L = new LenguajeI18N();
ResourceBundle msj = null;
String yo = "detalle_individuo.";
String idioma = Auxiliar.nzObjStr(session.getAttribute("idioma"), "es");

if (idioma.equals("es")) {
	L.setLenguaje("es");
	L.setPais("CO");
}
else {
	L.setLenguaje("en");
	L.setPais("US");
}
msj = L.obtenerMensajeIdioma();
%>

<%
String ua=request.getHeader("User-Agent").toLowerCase();
boolean es_movil = ua.matches("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")||ua.substring(0,4).matches("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-");
String doctype = "";
String estilo = "estilos.css";
String estilo_galeria = "style.css";

if (es_movil) { 
	doctype = " <!DOCTYPE html PUBLIC '-//WAPFORUM//DTD XHTML Mobile 1.0//EN' 'http://www.wapforum.org/DTD/xhtml-mobile10.dtd' >"; 
	estilo = "estilos_movil.css";
	estilo_galeria = "style_mobil.css";
} 
else {
	doctype = " <!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN' 'http://www.w3.org/TR/html4/loose.dtd' >";
	estilo = "estilos_pc.css";
	estilo_galeria = "style.css";
} 
out.print(doctype); 
%>
<html>
<!-- Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com). -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link type="text/css" rel="stylesheet" href="css/estilos.css" media="all" />
<link type="text/css" rel="stylesheet" href="css/<% out.print(estilo); %>" media="all" />

<link rel="stylesheet" href="js/FullPageImageGallery/css/<% out.print(estilo_galeria); %>" type="text/css" media="screen"/>

<title>
<% try { out.print(msj.getString(yo+"Imagenes_de_Individuo")); } catch (MissingResourceException e) { out.print("Imágenes del Individuo" + ".."); } %>
</title>

<style>
span.reference{
	font-family:Arial;
	position:fixed;
	right:10px;
	top:10px;
	font-size:10px;
}
span.reference a{
	color:#fff;
	text-transform:uppercase;
	text-decoration:none;
	text-shadow:1px 1px 1px #000;
	margin-left:20px;
}
span.reference a:hover{
	color:#ddd;
}
h1.title{
	width:919px;
	height:148px;
	position:fixed;
	top:10px;
	left:10px;
	text-indent:-9000px;
	background:transparent url(images/icons/title.png) no-repeat top left;
	z-index:2;
}
</style>

<script type='text/javascript' src='js/ajax.js'></script>
<script type='text/javascript' src='js/ajax_opciones.js'></script>
<script type='text/javascript' src='js/jquery.min.js'></script>
<!-- <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>-->
<script type="text/javascript" src="js/FullPageImageGallery/jquery.easing.1.3.js"></script>
<script type='text/javascript' src='js/auxiliares.js'></script>

<%
String INDV_CONSECUTIVO = Auxiliar.nzObjStr(request.getAttribute("INDV_CONSECUTIVO"), "").toString();
%>

<script type='text/javascript'>

$(document).ready(function(){
	<% if (es_movil) { %>
    document.body.style.width = (getWidth()-30) + 'px';
    <% } %>
});

$(window).load(function() {
	sliderLeft=$('#thumbScroller .container').position().left;
	padding=$('#outer_container').css('paddingRight').replace("px", "");

	sliderWidth=$(window).width()-padding;
	$('#thumbScroller').css('width',sliderWidth);

	var totalContent=0;
	$('#thumbScroller .content').each(function () {
		totalContent+=$(this).innerWidth();
		$('#thumbScroller .container').css('width',totalContent);
	});
	
	<% if (!es_movil) { %>
	$('#thumbScroller').mousemove(function(e){
		if($('#thumbScroller  .container').width()>sliderWidth){
			var mouseCoords=(e.pageX - this.offsetLeft);
			var mousePercentX=mouseCoords/sliderWidth;
			var destX=-(((totalContent-(sliderWidth))-sliderWidth)*(mousePercentX));
			var thePosA=mouseCoords-destX;
			var thePosB=destX-mouseCoords;
			var animSpeed=600; //ease amount
			var easeType='easeOutCirc';
			if(mouseCoords==destX){
				$('#thumbScroller .container').stop();
			}
			else if(mouseCoords>destX){
				//$('#thumbScroller .container').css('left',-thePosA); //without easing
				$('#thumbScroller .container').stop().animate({left: -thePosA}, animSpeed,easeType); //with easing
			}
			else if(mouseCoords<destX){
				//$('#thumbScroller .container').css('left',thePosB); //without easing
				$('#thumbScroller .container').stop().animate({left: thePosB}, animSpeed,easeType); //with easing
			}
		}
	});
	<% } %>
	
	$('#thumbScroller  .thumb').each(function () {
		$(this).fadeTo(fadeSpeed, 0.6);
	});
	var fadeSpeed=200;
	$('#thumbScroller .thumb').hover(
	function(){ //mouse over
		$(this).fadeTo(fadeSpeed, 1);
	},
	function(){ //mouse out
		$(this).fadeTo(fadeSpeed, 0.6);
	}
);
});
$(window).resize(function() {
	//$('#thumbScroller .container').css('left',sliderLeft); //without easing
	$('#thumbScroller .container').stop().animate({left: sliderLeft}, 400,'easeOutCirc'); //with easing
	$('#thumbScroller').css('width',$(window).width()-padding);
	sliderWidth=$(window).width()-padding;
});


function editar(f) {
	var nombrearchivo = f.f_imagen.value;

	if (nombrearchivo == '') {
		return;
	}

	var a_tokens = nombrearchivo.split('.');

	var n_tokens = a_tokens.length;

	if (n_tokens > 0) {
		var extension = a_tokens[n_tokens-1];
		extension = extension.toString().toLowerCase();
		
		if (extension == 'jpg' || extension == 'jpeg' || extension == 'png') {
			f.accion.value = 'registrar';
			f.submit();
		}
		else {
			alert('Formatos soportados/Formats supported: .jpg, .png');
			return;
		}
	}
	f.submit();
}

function eliminar(f) {
	f.accion.value = 'eliminar';
	f.submit();
}

function publicarOcultar(f) {
	if (f.boton_estado.tabindex == '0') {
		f.accion.value = 'publicar';
	}
	else {
		f.accion.value = 'ocultar';
	} 
	f.submit();
}

</script>

</head>
<body style='background: black;'>

<%
String datos_sesion = "";
try {
	String id_usuario = "";
	id_usuario = Auxiliar.nzObjStr(session.getAttribute("usuario"), "");
	String t_usuario = "";
	try { t_usuario = msj.getString("General.USUARIO_EN_SESION"); } catch (MissingResourceException e) { t_usuario = "Usuario en sesión:"; }
	if (Auxiliar.tieneAlgo(id_usuario)) datos_sesion = Auxiliar.mensajeImpersonal("sesion", t_usuario + ": " + id_usuario);
}
catch (Exception e) {
	datos_sesion = "..." + e.toString();
}
%>

<% 
out.println(request.getAttribute("retorno")); 
%>


<%

String str_fotos = Auxiliar.nzObjStr(request.getAttribute("INDV_NOMBREARCHIVO"), "").toString();
String [] a_fotos = str_fotos.split(",");

String primera_foto = "";
String primera_foto_publica = "";
String primera_foto_estado = "";

if (a_fotos.length > 0) {
	if (!a_fotos[0].equals("")) {
		String [] a_foto = a_fotos[0].split("_-_");
		if (a_foto.length == 2) {
			primera_foto = a_foto[0];
			primera_foto_publica = a_foto[1];
			primera_foto_estado = "";
			if (primera_foto_publica.equals("1")) {
				try { primera_foto_estado = msj.getString("General.imagen_publica"); } catch (MissingResourceException e) { primera_foto_estado = "Publica" + ".."; }
			}
			else {
				try { primera_foto_estado = msj.getString("General.imagen_oculta"); } catch (MissingResourceException e) { primera_foto_estado = "Oculta" + ".."; }
			}
		}
	}
}
else {
	primera_foto = "";
}

%>	
 
<div id="div_form" class="form" style="position: fixed; z-index: 2; display: block;">
<form name="f"  action='ImagenesIndividuo' method='post' enctype='multipart/form-data'>
<div class="campo" >
<div class="etiqueta">
<% 
try { out.print(msj.getString(yo+"Imagen_individuo_a_subir")); } catch (MissingResourceException e) { out.print("Imagen a subir (sólo formatos .png o .jpg)" + ".."); } 
%> 
</div>
<input type='file' name='f_imagen' />
<input type="button" onclick='javascript:editar(this.form);' value='<% try { out.print(msj.getString("General.Subir")); } catch (MissingResourceException e) { out.print("Registrar" + ".."); } %>' />
<%
String puedePublicar = Auxiliar.nzObjStr(request.getAttribute("puedePublicar"), "0").toString();

if (puedePublicar.equals("1")) {
%>
	<input id='boton_estado' type="button" onclick='javascript:publicarOcultar(this.form);' value='<% try { out.print(msj.getString("General.publicar_ocultar")); } catch (MissingResourceException e) { out.print("Publicar/ocultar" + ".."); } %>' style='display: none;' tabindex='' />
<%
}
%>
<input type="button" onclick='javascript:eliminar(this.form);' value='<% try { out.print(msj.getString("General.Eliminar")); } catch (MissingResourceException e) { out.print("Eliminar" + ".."); } %>' />
<div id="div_datos_sesion"><%=datos_sesion %></div>
</div>
<input type="hidden" name="accion" id="accion" value="" />
<input type="hidden" name="INDV_CONSECUTIVO" id="INDV_CONSECUTIVO" value="<%=INDV_CONSECUTIVO %>" />

<input type="hidden" id="nombre_imagen" name="nombre_imagen" />

</form>
</div>

<%
BD dbREDD = new BD();

String carpeta = dbREDD.obtenerDato("SELECT PRTR_RUTA FROM RED_PARAMETRO WHERE PRTR_NOMBRE='carpeta_imagenes_individuos'", "imagenes_individuos");
%>

<div id="fp_gallery" class="fp_gallery">
<img src='<% out.print(carpeta + "/" + INDV_CONSECUTIVO + "/" + primera_foto); %>' alt="" class="fp_preview" style="display:none;" title='<% out.print(primera_foto_estado); %>' tabindex='<% out.print(primera_foto_publica); %>' />
<div class="fp_overlay"></div>
<div id="fp_loading" class="fp_loading"></div>
<div id="fp_next" class="fp_next"></div>
<div id="fp_prev" class="fp_prev"></div>
<div id="outer_container">
<div id="thumbScroller">

<div class="container" style='width: initial !important;' >


<%
if (a_fotos.length > 0) {
	int i = 0;
	for (i=0; i<a_fotos.length; i++) {
		if (!a_fotos[i].equals("")) {
			String [] a_foto = a_fotos[i].split("_-_");
			if (a_foto.length == 2) {
				String nombre_archivo = a_foto[0];
				String publica = a_foto[1];
				String estado = "";
				String bordestado = "";
				if (publica.equals("1")) {
					try { estado = msj.getString("General.imagen_publica"); } catch (MissingResourceException e) { estado = "Publica" + ".."; }
					bordestado = "style=' border: 2px solid green !important;' ";
				}
				else {
					try { estado = msj.getString("General.imagen_oculta"); } catch (MissingResourceException e) { estado = "Oculta" + ".."; }
					bordestado = "style=' border: 2px solid red !important;' ";
				}
				
				out.print("<div class='content'><div><a href='#'><img class='imagen' src='" + carpeta + "/" + INDV_CONSECUTIVO + "/" + nombre_archivo + "' alt='" + carpeta + "/" + INDV_CONSECUTIVO + "/" + nombre_archivo + "' class='thumb' title='"+estado+"' tabindex='"+publica+"' "+bordestado+" /></a></div></div>");
			}
		}
	}
}
%>
</div>
</div>
</div>
<div id="fp_thumbtoggle" class="fp_thumbtoggle">Opciones/Options</div>
</div>

        <script type="text/javascript">
            $(function() {
				//current thumb's index being viewed
				var current			= -1;
				//cache some elements
				var $btn_thumbs = $('#fp_thumbtoggle');
				var $loader		= $('#fp_loading');
				var $btn_next		= $('#fp_next');
				var $btn_prev		= $('#fp_prev');
				var $thumbScroller	= $('#thumbScroller');
				
				//total number of thumbs
				var nmb_thumbs		= $thumbScroller.find('.content').length;
				
				//preload thumbs
				var cnt_thumbs 		= 0;
				for(var i=0;i<nmb_thumbs;++i){
					var $thumb = $thumbScroller.find('.content:nth-child('+parseInt(i+1)+')');
					$('<img/>').load(function(){
						++cnt_thumbs;
						if(cnt_thumbs == nmb_thumbs)
				//display the thumbs on the bottom of the page
				showThumbs(2000);
					}).attr('src',$thumb.find('img').attr('src'));
				}
				
				<% if (!es_movil) { %>
				//make the document scrollable
				//when the the mouse is moved up/down
				//the user will be able to see the full image
				makeScrollable();
				<% } %>
				
				//clicking on a thumb...
				$thumbScroller.find('.content').bind('click',function(e){
					var $content= $(this);
					var $elem 	= $content.find('img');
					//keep track of the current clicked thumb
					//it will be used for the navigation arrows
					current 	= $content.index()+1;
					//get the positions of the clicked thumb
					var pos_left 	= $elem.offset().left;
					var pos_top 	= $elem.offset().top;
					//clone the thumb and place
					//the clone on the top of it
					var $clone 	= $elem.clone()
					.addClass('clone')
					.css({
						'position':'fixed',
						'left': pos_left + 'px',
						'top': pos_top + 'px'
					}).insertAfter($('BODY'));
					
					var windowW = $(window).width();
					var windowH = $(window).height();
					
					//animate the clone to the center of the page
					$clone.stop()
					.animate({
						'left': windowW/2 + 'px',
						'top': windowH/2 + 'px',
						'margin-left' :-$clone.width()/2 -5 + 'px',
						'margin-top': -$clone.height()/2 -5 + 'px'
					},500,
					function(){
						var $theClone 	= $(this);
						var ratio		= $clone.width()/120;
						var final_w		= 400*ratio;
						
						$loader.show();
						
						//expand the clone when large image is loaded
						$('<img class="fp_preview"/>').load(function(){
							var $newimg 		= $(this);
							var $currImage 	= $('#fp_gallery').children('img:first');
							$newimg.insertBefore($currImage);
							$loader.hide();
							//expand clone
							$theClone.animate({
								'opacity'		: 0,
								'top'			: windowH/2 + 'px',
								'left'			: windowW/2 + 'px',
								'margin-top'	: '-200px',
								'margin-left'	: -final_w/2 + 'px',
								'width'			: final_w + 'px',
								'height'		: '400px'
							},1000,function(){$(this).remove();});
							//now we have two large images on the page
							//fadeOut the old one so that the new one gets shown
							$currImage.fadeOut(2000,function(){
								$(this).remove();
							});
							//show the navigation arrows
							showNav();
						}).attr('src',$elem.attr('alt'));
						if (document.getElementById('f_imagen')) {
							document.getElementById('f_imagen').value = $elem.attr('alt');
						} 
					});

					if (document.getElementById('nombre_imagen')) {
						document.getElementById('nombre_imagen').value = $elem.attr('alt');
					} 

					document.getElementById('boton_estado').style.display = 'block';
					
					var publica = $elem.attr('tabindex');

					if (publica == '1') {
						document.getElementById('boton_estado').tabindex = '1';
						document.getElementById('boton_estado').value = '<% try { out.print(msj.getString("General.ocultar")); } catch (MissingResourceException e) { out.print("Ocultar" + ".."); } %>';
					} 
					else {
						document.getElementById('boton_estado').tabindex = '0';
						document.getElementById('boton_estado').value = '<% try { out.print(msj.getString("General.publicar")); } catch (MissingResourceException e) { out.print("Publicar" + ".."); } %>';
					}
				
					//hide the thumbs container
					hideThumbs();
					e.preventDefault();
				});
				
				//clicking on the "show thumbs"
				//displays the thumbs container and hides
				//the navigation arrows
				$btn_thumbs.bind('click',function(){
					showThumbs(500);
					hideNav();
				});
				
				function hideThumbs(){
					$('#outer_container').stop().animate({'bottom':'-280px'},500);
					showThumbsBtn();
				}

				function showThumbs(speed){
					$('#outer_container').stop().animate({'bottom':'0px'},speed);
					hideThumbsBtn();
				}
				
				function hideThumbsBtn(){
					$btn_thumbs.stop().animate({'bottom':'-50px'},500);
				}

				function showThumbsBtn(){
					$btn_thumbs.stop().animate({'bottom':'0px'},500);
				}

				function hideNav(){
					$btn_next.stop().animate({'right':'-50px'},500);
					$btn_prev.stop().animate({'left':'-50px'},500);
					document.getElementById('div_form').style.display = 'block';
				}

				function showNav(){
					$btn_next.stop().animate({'right':'0px'},500);
					$btn_prev.stop().animate({'left':'0px'},500);
					document.getElementById('div_form').style.display = 'none'; 
				}

				//events for navigating through the set of images
				$btn_next.bind('click',showNext);
				$btn_prev.bind('click',showPrev);
				
				//the aim is to load the new image,
				//place it before the old one and fadeOut the old one
				//we use the current variable to keep track which
				//image comes next / before
				function showNext(){
					++current;
					var $e_next	= $thumbScroller.find('.content:nth-child('+current+')');
					if($e_next.length == 0){
						current = 1;
						$e_next	= $thumbScroller.find('.content:nth-child('+current+')');
					}
					$loader.show();
					$('<img class="fp_preview"/>').load(function(){
						var $newimg 		= $(this);
						var $currImage 		= $('#fp_gallery').children('img:first');
						$newimg.insertBefore($currImage);
						$loader.hide();
						$currImage.fadeOut(2000,function(){$(this).remove();});
					}).attr('src',$e_next.find('img').attr('alt'));
				}
				
				function showPrev(){
					--current;
					var $e_next	= $thumbScroller.find('.content:nth-child('+current+')');
					if($e_next.length == 0){
						current = nmb_thumbs;
						$e_next	= $thumbScroller.find('.content:nth-child('+current+')');
					}
					$loader.show();
					$('<img class="fp_preview"/>').load(function(){
						var $newimg 		= $(this);
						var $currImage 		= $('#fp_gallery').children('img:first');
						$newimg.insertBefore($currImage);
						$loader.hide();
						$currImage.fadeOut(2000,function(){$(this).remove();});
					}).attr('src',$e_next.find('img').attr('alt'));
				}

				<% if (!es_movil) { %>
                function makeScrollable(){
					$(document).bind('mousemove',function(e){
						var top = (e.pageY - $(document).scrollTop()/2) ;
						$(document).scrollTop(top);
                    });
				}
				<% } %>
				
            });
        </script>
<!-- </div>-->


</body>
</html>