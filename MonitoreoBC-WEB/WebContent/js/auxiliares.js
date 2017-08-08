// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
function seleccionarTodasLasOpciones(s)
{
	var s = document.getElementById(s);

	for (loop=0; loop < s.options.length; loop++)
	{
		s.options[loop].selected = true;
	}
}

function deseleccionarTodasLasOpciones(s)
{
	var s = document.getElementById(s);

	for (loop=0; loop < s.options.length; loop++)
	{
		s.options[loop].selected = false;
	}
}

function copiarOpcionesSeleccionadas(origen, destino)
{
	var origen = document.getElementById(origen);
	var destino = document.getElementById(destino);

	var loop_origen=0;
	var loop_destino=0;
	var yaesta=0;

	for (loop_origen=0; loop_origen < origen.options.length; loop_origen++)
	{
		if (origen.options[loop_origen].selected)
		{
			yaesta=0;
			origen.options[loop_origen].selected = false;
			for (loop_destino=0; loop_destino < destino.options.length; loop_destino++)
			{
				if (destino.options[loop_destino].value == origen.options[loop_origen].value)
				{
					yaesta=1;
					continue;
				}
			}
			if (yaesta==0)
			{
				var opcion = new Option(origen.options[loop_origen].text,origen.options[loop_origen].value);
				destino.options[destino.options.length] = opcion;
			}
		}
	}
}

function quitarOpcionesSeleccionadas(s)
{
	var s = document.getElementById(s);

	var loop=0;
	for (loop=0; loop < s.options.length; loop++)
	{
		if (s.options[loop].selected)
		{
			s.options[loop] = null;
			loop--;
		}
	}
}



function getWidth() {
	var w = 400;
	var w_final = 400;

	if (self.innerHeight) {
		w = self.innerWidth;
	}

	if (document.documentElement && document.documentElement.clientHeight) {
		w = document.documentElement.clientWidth;
	}

	if (document.body) {
		w = document.body.clientWidth;
	}

	var ratio = window.devicePixelRatio || 1;
	var w_screen = screen.width * ratio;
	//var h = screen.height * ratio;
	
	if (w <= w_screen) {
		w_final = w;
	}
	else {
		w_final = w_screen;
	}
		
	return w_final;
}

function getHeight() {
	  if (self.innerHeight) {
	    return self.innerHeight;
	  }

	  if (document.documentElement && document.documentElement.clientHeight) {
	    return document.documentElement.clientHeight;
	  }

	  if (document.body) {
	    return document.body.clientHeight;
	  }
}

function DIVisibilidad(divId) {
	var div = document.getElementById(divId);
	
	if (div) {
		if (div.style.display == 'block') {
			div.style.display = 'none';
		}
		else {
			div.style.display = 'block';
		}
	}
}

function DIVer(divId, ver) {
	var div = document.getElementById(divId);
	
	if (div) {
		if (!ver) {
			div.style.display = 'none';
		}
		else {
			div.style.display = 'block';
		}
	}
}


function esNumero(num){
    return (!isNaN(num) && num != null && num != '');
}

function esPorcentaje(num) {
	var r = false;
	if (esNumero(num)) {
		if (num >= 0 && num <= 1) {
			r = true;
		}
	}
	return r;
}

function esDensidad(num) {
	var r = false;
	if (esNumero(num)) {
		if (num > 0 && num <= 1) {
			r = true;
		}
	}
	return r;
}

function esAzimut(num) {
	var r = false;
	if (esNumero(num)) {
		if (num >= 0 && num < 360) {
			r = true;
		}
	}
	return r;
}

function esSubparcela(num) {
	var r = false;
	if (esNumero(num)) {
		if (num >= 1 && num <= 5) {
			if (num == Math.round(num)) {
				r = true;
			}
		}
	}
	return r;
}

function esDistancia(num) {
	var r = false;
	if (esNumero(num)) {
		if (num >= 0) {
			r = true;
		}
	}
	return r;
}

function esArea(num) {
	var r = false;
	if (esNumero(num)) {
		if (num > 0) {
			r = true;
		}
	}
	return r;
}

function esLatitud(num) {
	var r = false;
	if (esNumero(num)) {
		if (num >= -90 && num <= 90) {
			r = true;
		}
	}
	return r;
}

function esLongitud(num) {
	var r = false;
	if (esNumero(num)) {
		if (num >= -180 && num <= 180) {
			r = true;
		}
	}
	return r;
}

function esAltitud(num) {
	var r = false;
	if (esNumero(num)) {
		if (num >= 0 && num <= 6000) {
			r = true;
		}
	}
	return r;
}

/**
 * Método para verificar la validez de una fecha con formato yyyy-MM-dd
 * @param text
 * @return
 */
function fechaEsValida(text) {
	/*
	if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
		return false;
	else 
		return true;
	*/
	
	var fecha = new Date(text);
	if (isNaN(fecha)) {
		return false;
	}
	
}

/*
function isGregorianLeapYear(year) {
    return year % 400 === 0 || year % 100 !== 0 && year % 4 === 0;
}

function daysInGregorianMonth(year, month) {
    var days;

    if (month == 2) {
        days = 28;
        if (isGregorianLeapYear(year)) {
            days += 1;
        }
    } else {
        days = 31 - ((month - 1) % 7 % 2);
    }

    return days;
}

if (typeof iso8601 !== "string") {
    alert("not an iso8601 string");
} else {
    dateTime = iso8601.split(" ");
    if (dateTime.length !== 2) {
        alert("missing date or time element");
    } else {
        date = dateTime[0].split("-");
        if (date.length !== 3) {
            alert("incorrect number of date elements");
        } else {
            value = +date[0];
            if (date[0].length !== 4 || value < -9999 || value > 9999) {
                alert("year value is incorrect");
            }

            value = +date[1];
            if (date[1].length !== 2 || value < 1 || value > 12) {
                alert("month value is incorrect");
            }

            value = +date[2];
            if (date[2].length !== 2 || value < 1 || value > daysInGregorianMonth(+date[0], +date[1])) {
                alert("day value is incorrect");
            }
        }

        time = dateTime[1].split(":");
        if (time.length !== 3) {
            alert("incorrect number of time elements");
        } else {
            value = +time[0];
            if (time[0].length !== 2 || value < 0 || value > 23) {
                alert("hour value is incorrect");
            }

            value = +time[1];
            if (time[1].length !== 2 || value < 0 || value > 59) {
                alert("minute value is incorrect");
            }

            value = +time[2];
            if (time[2].length !== 2 || value < 0 || value > 59) {
                alert("second value is incorrect");
            }
        }
    }
}
*/

function esEntero(v) {
	  var x;
	  if (isNaN(v)) {
	    return false;
	  }
	  x = parseFloat(v);
	  return (x | 0) === x;
}

function esEnteroPositivo(v) {
	var x;
	if (isNaN(v)) {
		return false;
	}
	x = parseFloat(v);
	if (x <= 0) {
		return false;
	}
	return (x | 0) === x;
}

function esEnteroMayorOIgualACero(v) {
	var x;
	if (isNaN(v)) {
		return false;
	}
	x = parseFloat(v);
	if (x < 0) {
		return false;
	}
	return (x | 0) === x;
}

function esPeriodo(v) {
	if (!esEnteroPositivo(v)) {
		return false;
	}
	
	if (v < 1900 || v > 2100) {
		return false;
	}
	
	return true;
}

function esHora(v) {
	var r = false;
	
	if (esEntero(v)) {
		if (v*1.0 >= 0) {
			if (v*1.0 <= 23) {
				r = true;
			}
		}
	}
	
	return r;
	//return (esEntero(v) && v>=0 && v<=23);
}

function esMinuto(v) {
	var r = false;
	
	if (esEntero(v)) {
		if (v*1.0 >= 0) {
			if (v*1.0 <= 59) {
				r = true;
			}
		}
	}
	
	return r;
//	return (esEntero(v) && v>=0 && v<=59);
}

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.split(search).join(replacement);
};

function isNumberKey(evt) {
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	
	console.log(charCode);	
	
	if (charCode > 31  && (charCode < 48 || charCode > 57)) {
		return false;
	}

	return true;
}
