function ajax_opciones(escript,selecta_id,feedback_id,params,funcioncallback,mensajeno,div_resultados,div_elegido, input_etiqueta)
{
	if (mensajeno == undefined) mensajeno = 'No records found.';

	var feedback = document.getElementById(feedback_id);
	
	var xmlHttp = GetXmlHttpObject();
	if (xmlHttp == null)
	{
		alert ('Su navegador no soporta AJAX');
		return;
	}
	
	var url=escript + params;
	
	feedback.innerHTML = '';
	//feedback.innerHTML = url;
	//alert(url);
	
	if (div_resultados == undefined) div_resultados = '';
	if (div_elegido == undefined) div_elegido = '';
	if (input_etiqueta == undefined) input_etiqueta = '';
	
	xmlHttp.onreadystatechange = function() 
	{
		if (xmlHttp.readyState==4 || xmlHttp.readyState=="complete") 
		{
			r(xmlHttp,selecta_id,feedback_id,funcioncallback,mensajeno,div_resultados,div_elegido, input_etiqueta);
		}
	};
	xmlHttp.open('GET',url,true);
	if (feedback.innerHTML == '') feedback.innerHTML = '...';    
	xmlHttp.send(null);
}

function r(ajaxy,selecta_id,feedback_id,funcioncallback,mensajeno,div_resultados,div_elegido, input_etiqueta) 
{ 
	if (ajaxy.readyState==4 || ajaxy.readyState=='complete')
	{
		respuesta = new String(ajaxy.responseText);
		
		var s = document.getElementById(selecta_id);
		var feedback = document.getElementById(feedback_id);
		
		var arreglo = respuesta.split("\n");
		var id = "";
		var info = "";
		var valor = "";
		var arrint = [];
		
		if (div_resultados == '') {
			s.options.length = 0;
		}
		else {
			var divr = document.getElementById(div_resultados);
			divr.innerHTML = "";
			var divrHTML = "";
			var jsDivR = "";
		}
			

		for (var i=0; i<arreglo.length-1; i++)
		{
			valor = arreglo[i];
			arrint = valor.split('@@');
			id = arrint[0];
			info = arrint[1];

			if (div_resultados == '') {
				if (info && info != '') {
					s.options.length = s.options.length + 1;
					s.options[i].value = id;
					s.options[i].text = info;
				}
			}
			else {
				//document.getElementById(div_resultados).innerHTML += "<div><div id='r_"+i+"' style='display: none; height:0px;'>"+id+"</div><div onclick='document.getElementById(\'"+selecta_id+"\').value=document.getElementById(\'r_"+i+"\').innerHTML;document.getElementById(\'"+div_elegido+"\').innerHTML=this.innerHTML;'>"+info+"</div></div>";
				divrHTML = '<div>';
				divrHTML += '<div id="r_'+i+'" style="display:none; height:0px;">';
				divrHTML += id;
				divrHTML += '</div>';
				
				if (input_etiqueta != '') js_input_etiqueta = ' document.getElementById(\''+input_etiqueta+'\').value=this.innerHTML; ';
				else js_input_etiqueta = '';
				
				jsDivR = ' onclick="javascript: document.getElementById(\''+selecta_id+'\').value=document.getElementById(\'r_'+i+'\').innerHTML; document.getElementById(\''+div_elegido+'\').innerHTML=\'[\' + document.getElementById(\'r_'+i+'\').innerHTML + \']\' + this.innerHTML; document.getElementById(\''+div_resultados+'\').innerHTML=\'\'; '+js_input_etiqueta+' " ';
				
				divrHTML += '<div ';
				
				divrHTML += jsDivR;
				
				divrHTML += ' style="padding-top:1px; padding-bottom:1px; border: 1px solid gray; margin:1px;" >';
				divrHTML += info;
				divrHTML += '</div>';
				
				divrHTML += '</div>';
				
				divr.innerHTML += divrHTML;
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

