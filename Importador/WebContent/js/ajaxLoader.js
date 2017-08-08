function ajaxLoader(escript,selecta_id,feedback_id,params,multiplex,funcioncallback,mensajeno)
{
	if (mensajeno == undefined) mensajeno = 'No records found.';

	var feedback = document.getElementById(feedback_id);

	var xmlHttp=null;
	if (window.XMLHttpRequest)
	{
		xmlHttp=new XMLHttpRequest();
	}
	else if (window.ActiveXObject)
	{
		xmlHttp=new ActiveXObject('Microsoft.XMLHTTP');
	}
	
	if (xmlHttp == null) {
		alert ('Su navegador no soporta AJAX');
		return;
	}
	
	var parametro_multiplex = "";
	
	if (document.getElementById(multiplex)) {
		var s = document.getElementById(multiplex); 
		var str_multiplex = '';
		var i=0;

		for (i=0; i < s.options.length; i++)
		{
			if (s.options[i].selected)
			{
				str_multiplex += s.options[i].value + ','; 
			}
		}
		
		if (i>0)
		{
			str_multiplex = str_multiplex.slice(0, -1);
		}
		
		if (params.length > 0 || escript.indexOf('?') != -1) {
			parametro_multiplex = "&";
		}
		else {
			parametro_multiplex = "?";
		}
		
		parametro_multiplex += multiplex + "=" + str_multiplex;
	}
	
	var url=escript + params + parametro_multiplex;
	
	feedback.innerHTML = '';
	//feedback.innerHTML = url;
	
	xmlHttp.onreadystatechange = function() 
	{
		if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete") 
		{
			r_ajaxLoader(xmlHttp,selecta_id,feedback_id,funcioncallback,mensajeno);
		}
	};
	xmlHttp.open('GET',url,true);
	if (feedback.innerHTML == '') feedback.innerHTML = '...';    
	xmlHttp.send(null);
}

function r_ajaxLoader(ajaxy,selecta_id,feedback_id,funcioncallback,mensajeno) 
{ 
	if (ajaxy.readyState==4 || ajaxy.readyState=='complete')
	{
		respuesta = new String(ajaxy.responseText);
		
		var s = document.getElementById(selecta_id);
		var feedback = document.getElementById(feedback_id);
		
		var arreglo = respuesta.split("\n");
		var id = "";
		var selected = "";
		var info = "";
		var valor = "";
		var arrint = [];
		
		s.options.length = 0;

		for (var i=0; i<arreglo.length-1; i++)
		{
			valor = arreglo[i];
			arrint = valor.split('@@');
			id = arrint[0];
			selected = arrint[1];
			info = arrint[2];

			s.options.length = s.options.length + 1;
			s.options[i].value = id;
			s.options[i].text = info;
			if (selected.length > 0) {
				s.options[i].selected = true;
			}
		}
		
		if (respuesta == '' || respuesta == '\n')
		{
			if (feedback.innerHTML == '...') feedback.innerHTML = mensajeno;    
		}
		else
		{
			if (feedback.innerHTML == '...') feedback.innerHTML = '';
			
			if (typeof funcioncallback == 'function')
			{
				funcioncallback();
			}
			
			/* PARA SABER COMO SE HACE CON UNA VARIABLE STRING CON EL NOMBRE DE LA FUNCION
			if (typeof funcion == 'string' && eval('typeof ' + funcion) == 'function') 
			{
				eval(funcion+'()');
			}
			*/
		}
	}
	else
	{
		feedback.innerHTML += '...';    
	}
} 

