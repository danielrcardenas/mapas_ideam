var accordion=function(){
	var tm=10; var sp=10;
	function slider(n){
		this.nm=n; this.arr=[]; this.sel=''
	}
	slider.prototype.init=function(t,c,k){
		var a,h,s,l,i; a=document.getElementById(t);
		h=a.getElementsByTagName('dt'); s=a.getElementsByTagName('dd');
		l=h.length;
		for(i=0;i<l;i++){
			var d=h[i]; this.arr[i]=d; d.onclick=new Function(this.nm+".process(this)");
			if(k!=null&&c==i){this.sel=d.className=k}
		}
		l=s.length;
		for(i=0;i<l;i++){
			var d=s[i]; d.maxh=d.offsetHeight;
			if(c!=i){d.style.height='0'; d.style.display='none'}
		}
	}
	slider.prototype.process=function(d){
		var i,l; l=this.arr.length;
		for(i=0;i<l;i++){
			var h=this.arr[i]; var s=h.nextSibling;
			if(s.nodeType!=1){s=s.nextSibling}
			clearInterval(s.timer);
			if(h==d&&s.style.display=='none'){
				s.style.display=''; setup(s,1); h.className=this.sel}
			else if(s.style.display==''){setup(s,-1); h.className=''}
		}
	}
	function setup(c,f){c.timer=setInterval(function(){slide(c,f)},tm)}
	function slide(c,f){
		var h,m,d; h=c.offsetHeight; m=c.maxh; d=(f==1)?Math.ceil((m-h)/sp):Math.ceil(h/sp);
		c.style.height=h+(d*f)+'px'; c.style.opacity=h/m; c.style.filter='alpha(opacity='+h*100/m+')';
		if(f==1&&h>=m){clearInterval(c.timer)}
		else if(f!=1&&h==1){c.style.display='none'; clearInterval(c.timer)}
	}
	return{slider:slider}
}();