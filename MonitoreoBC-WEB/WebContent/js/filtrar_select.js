// Autor y desarrollador parcial o total: Santiago Hernández Plazas (santiago.h.plazas@gmail.com).
function filtrar_select(s) 
{
	if (s == undefined) {
		return;
	}
	
	this.s = s;

	this.init = function() {
		this.clon_opciones = new Array();
		if (this.s && this.s.options) {
			for (var i=0; i < this.s.options.length; i++) {
				this.clon_opciones[i] = new Option();
				this.clon_opciones[i].id = s.options[i].id;
				this.clon_opciones[i].text = s.options[i].text;
				if (s.options[i].value) {
					this.clon_opciones[i].value = s.options[i].value;
				} 
				else {
					this.clon_opciones[i].value = s.options[i].text;
				}
				this.clon_opciones[i].title = s.options[i].title;
			}
		}
	};
	
	this.add = function(id, text, value, title) {
		var opcion = new Option();
		opcion.id = id;
		opcion.text = text;
		opcion.value = value;
		opcion.title = title;
		this.clon_opciones.push(opcion);
	};
	
	this.remove = function(id) {
		for (var i=0; i<this.clon_opciones.length; i++) {
			if (this.clon_opciones[i].id == id) {
				this.clon_opciones.splice(i, 1);
				break;
			}
		}
	};
	
	this.update = function() {
		this.clon_opciones = new Array();
		if (this.s && this.s.options) {
			for (var i=0; i < this.s.options.length; i++) {
				this.clon_opciones[i] = new Option();
				this.clon_opciones[i].id = s.options[i].id;
				this.clon_opciones[i].text = s.options[i].text;
				if (s.options[i].value) {
					this.clon_opciones[i].value = s.options[i].value;
				} 
				else {
					this.clon_opciones[i].value = s.options[i].text;
				}
				this.clon_opciones[i].title = s.options[i].title;
			}
		}
	};

	this.filtre = function(id, value, text, title) {
		if (this.s && this.s.options) {
			var i=0;
			var j=0;
	
			var re_id = new RegExp(id, 'i');
			var re_value = new RegExp(value, 'i');
			var re_text = new RegExp(text, 'i');
			var re_title = new RegExp(title, 'i');
	
			this.s.options.length = 0;
	
			for (i=0; i < this.clon_opciones.length; i++) {
				var opcion = this.clon_opciones[i];
	
				var opcion_id = opcion.id;
				var opcion_value = opcion.value;
				var opcion_text = opcion.text;
				var opcion_title = opcion.title;
				
				var test_id = re_id.test(opcion_id);
				var test_value = re_value.test(opcion_value);
				var test_text = re_text.test(opcion_text);
				var test_title = re_title.test(opcion_title);
				
				if (test_id || test_value || test_text || test_title) {
					j++;
					this.s.options[j] = new Option(opcion.text, opcion.value, false);
					this.s.options[j].id = opcion.id;
					this.s.options[j].title = opcion.title;
				}
			}
		}
	};
	
	this.filtre_id = function(id) {
		if (this.s && this.s.options) {
			var i=0;
			var j=0;
			
			var re_id = new RegExp(id, 'i');
			
			this.s.options.length = 0;
			
			for (i=0; i < this.clon_opciones.length; i++) {
				var opcion = this.clon_opciones[i];
				
				var opcion_id = opcion.id;
				
				var test_id = re_id.test(opcion_id);
				
				if (test_id) {
					j++;
					this.s.options[j] = new Option(opcion.text, opcion.value, false);
					this.s.options[j].id = opcion.id;
					this.s.options[j].title = opcion.title;
				}
			}
		}
	};
	
	this.filtre_title = function(title, regexp) {
		if (this.s && this.s.options) {
			var i=0;
			var j=0;
			
			var re_title = new RegExp(title, 'i');
			
			this.s.options.length = 0;
			
			for (i=0; i < this.clon_opciones.length; i++) {
				var opcion = this.clon_opciones[i];
				
				var opcion_title = opcion.title;
				
				var test_title = regexp ? re_title.test(opcion_title) : (title == opcion_title || title == '');
				
				if (test_title) {
					j++;
					this.s.options[j] = new Option(opcion.text, opcion.value, false);
					this.s.options[j].id = opcion.id;
					this.s.options[j].title = opcion.title;
				}
			}
		}
	};
	
	this.init();
}
