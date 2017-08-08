/**
	Validadores de formularios
*/

function valideNum(e){
 
	var evt = e ? e : event;
	var key = window.Event ? evt.which : evt.keyCode;

	if (key > 47 && key < 58 || key ==45 || key==46 || key==8) {
		return true;
	} else {
		return false;
}
}

function valideMail(mail){
	var txt = mail;
	//expresion regular
    var b=/^[^@\s]+@[^@\.\s]+(\.[^@\.\s]+)+$/;
    
    //devuelve verdadero si validacion OK, y falso en caso contrario
    return b.test(txt);
}

function valideText(e){
	var evt = e ? e : event;
	var key = window.Event ? evt.which : evt.keyCode;

	if (key > 47 && key < 58|| key==8) {
		return true;
	} else {
		return false;
	}
}

function valideValNum(e){
	var evt = e ? e : event;
	var key = window.Event ? evt.which : evt.keyCode;

	if (key > 47 && key < 58|| key==8) {
		return true;
	} else {
		return false;
	}
}

function valideValDec(e,o){
	var evt = e ? e : event;
	var key = window.Event ? evt.which : evt.keyCode;

	
	if (key > 47 && key < 58 || key==8) {
		return true;
	}else if(key==46&&o.value.toString()!=""){
		return true;
	}else {
		return false;
	}
}

//busca caracteres que no sean espacio en blanco en una cadena
function valideEmpty(q) {
    for ( i = 0; i < q.length; i++ ) {
            if ( q.charAt(i) != " " ) {
                    return true;
            }
    }
    return false;
}

//valida que el campo no este vacio y no tenga solo espacios en blanco
function valideBlank(F) {        
    if( vacio(F.campo.value) == false ) {
            alert("Introduzca un cadena de texto.");
            return false;
    } else {
            alert("OK");
            //cambiar la linea siguiente por return true para que ejecute la accion del formulario
            return false;
    }        
}

//valida que el campo no este vacio y no tenga solo espacios en blanco
function noKeyData(e) {
        
    var evt = e ? e : event;
	var key = window.Event ? evt.which : evt.keyCode;

	if (key > 256) {
		return true;
	} else {
		return false;
	}
        
}

function validePass(cadena) {  
      
    var minuscula = false;
	var mayuscula = false;
	var numero = false;
	var caracter = false;
	
	//recorre cada caracter de la cadena
	for(i=0;i<cadena.length;i++) {
		//si el codigo ASCII es el de las minusculas, pone a true el flag de minusculas
		if(cadena.charCodeAt(i)>=97 && cadena.charCodeAt(i)<=122) {
			minuscula=true;
		//si el codigo ASCII es el de las mayusculas, pone a true el flag de mayusculas
		} else if(cadena.charCodeAt(i)>=65 && cadena.charCodeAt(i)<=90) {
			mayuscula=true;
		//si el codigo ASCII es el de loss numeros, pone a true el flag de numeros
		} else if(cadena.charCodeAt(i)>=48 && cadena.charCodeAt(i)<=57) {
			numero=true;
		//si no es ninguno de los anteriores, a true el flag de caracter simbolico
		} else 
			caracter=true;
	}

	if(caracter==true && numero==true && minuscula==true && mayuscula==true) {
		alert("La password elegida contiene todos los caracteres requeridos.");
		return false;	//cambiar false por true para hacer el submit
	} else {
		alert("La password elegida no es segura. Introduzca al menos una mayúscula, una minúscula, un número y un carácter simbólico.");
		return false;
	}
} 

function ConvertDMSToDD(days, minutes, seconds, direction) {//para longitud -1
    var dd = days + minutes/60 + seconds/(60*60);

    if (direction == "S" || direction == "W") {
        dd = dd * -1;
    } // Don't do anything for N or E
    return dd;
}


