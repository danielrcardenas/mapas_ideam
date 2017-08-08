Bio1<-function(Dens=NULL,DAP,Alt=NULL,AB=NULL) exp(-2.235282155+(2.3733*(log(DAP)))) # bs-T de alvarez et al. (en preparacion)
Bio2<-function(Dens=NULL,DAP,Alt=NULL,AB=NULL) exp(-1.544182155+(2.3733*(log(DAP)))) # bh-T de alvarez et al. (en preparacion)
Bio3<-function(Dens=NULL,DAP,Alt=NULL,AB=NULL) exp(-1.908382155+(2.3733*(log(DAP)))) # bp-T de alvarez et al. (en preparacion)
Bio4<-function(Dens=NULL,DAP,Alt=NULL,AB=NULL) exp(-1.865582155+(2.3733*(log(DAP)))) # bh-PM de alvarez et al. (en preparacion)
Bio5<-function(Dens=NULL,DAP,Alt=NULL,AB=NULL) exp(-1.662982155+(2.3733*(log(DAP)))) # bh-MB de alvarez et al. (en preparacion)
Bio6<-function(Dens=NULL,DAP,Alt=NULL,AB=NULL) exp(-2.616382155+(2.3733*(log(DAP)))) # bh-M de alvarez et al. (en preparacion)

CargarIndividuos<-function(y){
tryCatch({
Individuos<-read.csv(y,enc="latin1")
return (list(FALSE,Individuos))
}, error = function(ex) {
print("No se pudo cargar el archivo de individuos")
return (TRUE)
})
}

CargarParcelas<-function(x){
tryCatch({
Parcela<-read.csv(x,enc="latin1")
return (list(FALSE,Parcela))
}, error = function(ex) {
print("No se pudo cargar el archivo de parcelas")
return (TRUE)
})
}


organizarInfo<-function(i,p){
tryCatch({
base1<-merge(i,p)
base1a<-subset(base1,DAP>=10) 
base2<-subset(base1a,Familia!="Arecaceae") 
str(base2)
summary(base2)
return (list(FALSE,base2,base1))
}, error = function(ex) {
print("Error en el analisis de la informacion de los archivos")
return(TRUE)
})
}

CalculaBiomasa<-function(x){
 tryCatch({
  EcuacionesAlometricas<-c('Bio1','Bio2','Bio3','Bio4','Bio5','Bio6')
  Biomasa<-get(EcuacionesAlometricas[as.integer(x[["Ecuacion"]])])(as.numeric(x[["Dens"]]),
  as.numeric(x[["DAP"]]),
  as.numeric(x[["Alt"]]),
  as.numeric(x[["AB"]]))
  return(Biomasa)
  }, error = function(ex) {
print("No se pudo realizar el calculo de la Biomasa")
})
}

calcularBiomasaCarbono<-function(base2){
tryCatch({
base2$BiomasaKg<-apply(base2,1,CalculaBiomasa) 
ParcelasBiomasa<-aggregate(base2$BiomasaKg/((base2$Area*10000)/10),list(Plot=base2[,"Plot"]),sum,na.rm=TRUE)
#base3<-merge(Parcela,ParcelasBiomasa)
base3<-merge(base1,ParcelasBiomasa)
base3$Biomasa<-base3$x
base3$Carbono<-base3$Biomasa*0.5
base4<-subset(base3,select=c(Plot,FID,DAP,Familia,Area,Bosque,Ecuacion,Biomasa,Carbono))
return(list(FALSE,base4,ParcelasBiomasa))
}, error = function(ex) {
print("Error al realizar el calculo de la biomasa y el carbono")
return (TRUE)
})
}

cargarExtremeValues<-function(){
tryCatch({
  library(extremevalues)
  return(FALSE)
}, error = function(ex) {
  return(TRUE)
})
}

Atipicos<-function(p,x,y)
{
tryCatch({
base3<-merge(p,x)
ParcelaBiomasa<-subset(y,select=c(Plot,Area,Bosque,Biomasa))
lognormal<-getOutliersII(ParcelaBiomasa$Biomasa,distribution="lognormal")
ParcelaSinOuts<-ParcelaBiomasa[-c(lognormal$iRight,lognormal$iLeft),]
return(list(FALSE,ParcelaBiomasa,ParcelaSinOuts))
	}, error = function(ex) {
print("Error realizando el calculo de los datos Atipicos");
return (TRUE)
})
}

var.wtd.mean.cochran <- function(x,w,na.rm=TRUE)
{
  if (na.rm){
  x<-x[!is.na(w)]
  w<-w[!is.na(w)]
  }
	n = length(w)
	xWbar = weighted.mean(x,w)
	wbar = mean(w)
	out = n/((n-1)*sum(w)^2)*(sum((w*x-wbar*xWbar)^2)-2*xWbar*sum((w- wbar)*(w*x-wbar*xWbar))+xWbar^2*sum((w-wbar)^2))
	return (out)
}

AtipicosTodos<-function(ParcelaBiomasa){
tryCatch({
varzas<-with(ParcelaBiomasa,tapply(Biomasa,list(Bosque,Area),var))
desv.estand<-with(ParcelaBiomasa,tapply(Biomasa,list(Bosque),sd))
ns1<-with(ParcelaBiomasa,tapply(Biomasa,list(Bosque),length))
ponderaciones<-(1/varzas)/apply((1/varzas),1,sum,na.rm=TRUE)
apply(ponderaciones,1,sum,na.rm=TRUE)
medias1<-with(ParcelaBiomasa,tapply(Biomasa,list(Bosque,Area),mean))
medias2<-apply(medias1*ponderaciones,1,sum,na.rm=TRUE)
medias3<-with(ParcelaBiomasa,tapply(Biomasa,Bosque,mean))
sec1<-1:nrow(medias1)
var.wtd.media<-function(i,medias,varianzas,na.rm=TRUE){
   var.wtd.mean.cochran(medias1[i,],1/varianzas[i,],na.rm=TRUE)
   }
r1<-sapply(sec1,var.wtd.media,medias1,varzas)

SalidaTodos<-data.frame(ns=ns1,
                  media_w=medias2,desv.estand_w=sqrt(r1),
                  CV_w=sqrt(r1)/medias2*100,
                  IC_w=1.96*sqrt(r1))
return (list(FALSE,SalidaTodos))
}, error = function(ex) {
print("No se pudo realizar el analisis de los Valores Atipicos")
return (TRUE)
})				  
}

AtipicosSin<-function(ParcelaSinOuts){
tryCatch({
varzas<-with(ParcelaSinOuts,tapply(Biomasa,list(Bosque,Area),var))
desv.estand<-with(ParcelaSinOuts,tapply(Biomasa,list(Bosque),sd))
ns1<-with(ParcelaSinOuts,tapply(Biomasa,list(Bosque),length))
ponderaciones<-(1/varzas)/apply((1/varzas),1,sum,na.rm=TRUE)
apply(ponderaciones,1,sum,na.rm=TRUE)
medias1<-with(ParcelaSinOuts,tapply(Biomasa,list(Bosque,Area),mean))
medias2<-apply(medias1*ponderaciones,1,sum,na.rm=TRUE)
medias3<-with(ParcelaSinOuts,tapply(Biomasa,Bosque,mean))
sec1<-1:nrow(medias1)
var.wtd.media<-function(i,medias,varianzas,na.rm=TRUE){
   var.wtd.mean.cochran(medias1[i,],1/varianzas[i,],na.rm=TRUE)
   }
r1<-sapply(sec1,var.wtd.media,medias1,varzas)

SalidaSinOuts<-data.frame(ns=ns1,
                  media_w=medias2,desv.estand_w=sqrt(r1),
                  CV_w=sqrt(r1)/medias2*100,
                  IC_w=1.96*sqrt(r1))
return (list(FALSE,SalidaSinOuts))
}, error = function(ex) {
print("No se pudo realizar el analisis de los Valores Atipicos")
return (TRUE)
})
}