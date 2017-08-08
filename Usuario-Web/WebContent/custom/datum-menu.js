//var timeout	= 500;
//var closetimer	= 0;
//var ddmenuitem	= 0;
//var navegador = navigator.appName 
////revisar con esto para cambiar hoja de estilo y ajustar
////if (navegador == "Microsoft Internet Explorer")
////alert(navegador);
//
//// open hidden layer
//function mopen(id)
//{	
//	// cancel close timer
//	mcancelclosetime();
//
//	// close old layer
//	if(ddmenuitem) ddmenuitem.style.visibility = 'hidden';
//
//	// get new layer and show it
//	ddmenuitem = document.getElementById(id);
//	ddmenuitem.style.visibility = 'visible';
//
//}
//// close showed layer
//function mclose()
//{
//	if(ddmenuitem) ddmenuitem.style.visibility = 'hidden';
//}
//
//// go close timer
//function mclosetime()
//{
//	closetimer = window.setTimeout(mclose, timeout);
//}
//
//// cancel close timer
//function mcancelclosetime()
//{
//	if(closetimer)
//	{
//		window.clearTimeout(closetimer);
//		closetimer = null;
//	}
//}
//otro menu

// close layer when click-out
//document.onclick = mclose; 

var menuids=["menunav"];

function submenus_horizontal(){
for (var i=0; i<menuids.length; i++){
  var ultags=document.getElementById(menuids[i]).getElementsByTagName("ul")
    for (var t=0; t<ultags.length; t++){
		if (ultags[t].parentNode.parentNode.id==menuids[i]){ 
			ultags[t].style.top="65px";
		}
		else{ 
		    ultags[t].style.left=ultags[t-1].getElementsByTagName("a")[0].offsetWidth+"px";    		
		}
    ultags[t].parentNode.onmouseover=function(){
    this.getElementsByTagName("ul")[0].style.visibility="visible"
    }
    ultags[t].parentNode.onmouseout=function(){
    this.getElementsByTagName("ul")[0].style.visibility="hidden"
    }
    }
  }
}
if (window.addEventListener)
window.addEventListener("load", submenus_horizontal, false)
else if (window.attachEvent)
window.attachEvent("onload", submenus_horizontal)
