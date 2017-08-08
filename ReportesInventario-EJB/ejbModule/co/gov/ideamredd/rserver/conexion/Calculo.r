##########################################################
# Rutina para realizar la estimación de la biomasa aérea #
# Autor: Kenneth Cabrera                                 #
#        Juan Phillips                                   #
# Fecha de creación: Miércoles, 22 de Junio de 2011      #
##########################################################
library(lattice)
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
  return(out)
}

############
# Nacional #
############

Data01<-read.csv2("DatosEstimacion.csv",row.names=1,enc="latin1")
str(Data01)
summary(Data01)
varzas<-with(Data01,tapply(BA,list(Bosque,Área),var))
desv.estand<-with(Data01,tapply(BA,list(Bosque),sd))
ns1<-with(Data01,tapply(BA,list(Bosque),length))
ponderaciones<-(1/varzas)/apply((1/varzas),1,sum,na.rm=TRUE)
apply(ponderaciones,1,sum,na.rm=TRUE)
medias1<-with(Data01,tapply(BA,list(Bosque,Área),mean))
medias2<-apply(medias1*ponderaciones,1,sum,na.rm=TRUE)
medias3<-with(Data01,tapply(BA,Bosque,mean))
sec1<-1:nrow(medias1)
var.wtd.media<-function(i,medias,varianzas,na.rm=TRUE){
   var.wtd.mean.cochran(medias1[i,],1/varianzas[i,],na.rm=TRUE)
   }
r1<-sapply(sec1,var.wtd.media,medias1,varzas)
Nacional<-data.frame(ns=ns1,media=medias3,desv.estand=desv.estand,
                     CV=desv.estand/medias3*100,
                     IC=1.96*desv.estand/sqrt(ns1),
                     media_w=medias2,desv.estand_w=sqrt(r1),
                     CV_w=sqrt(r1)/medias2*100,
                     IC_w=1.96*sqrt(r1))
Nacional
write.table(Nacional,file="Nacional.csv",row.names=TRUE,sep=";",dec=",")

############
# Amazonia #
############

Data02<-subset(Data01,Región=="Amazonia")
varzas<-with(Data02,tapply(BA,list(Bosque,Área),var))
desv.estand<-with(Data02,tapply(BA,list(Bosque),sd))
ns1<-with(Data02,tapply(BA,list(Bosque),length))
ponderaciones<-(1/varzas)/apply((1/varzas),1,sum,na.rm=TRUE)
apply(ponderaciones,1,sum,na.rm=TRUE)
medias1<-with(Data02,tapply(BA,list(Bosque,Área),mean))
medias2<-apply(medias1*ponderaciones,1,sum,na.rm=TRUE)
medias3<-with(Data02,tapply(BA,Bosque,mean))
sec1<-1:nrow(medias1)
var.wtd.media<-function(i,medias,varianzas,na.rm=TRUE){
  var.wtd.mean.cochran(medias1[i,],1/varianzas[i,],na.rm=TRUE)
}
r1<-sapply(sec1,var.wtd.media,medias1,varzas)
Amazonia<-data.frame(ns=ns1,media=medias3,desv.estand=desv.estand,
                     CV=desv.estand/medias3*100,
                     IC=1.96*desv.estand/sqrt(ns1),
                     media_w=medias2,desv.estand_w=sqrt(r1),
                     CV_w=sqrt(r1)/medias2*100,
                     IC_w=1.96*sqrt(r1))
Amazonia
write.table(Amazonia,file="Amazonia.csv",row.names=TRUE,sep=";",dec=",")

#########
# Andes #
#########

Data02<-subset(Data01,Región=="Andes")
varzas<-with(Data02,tapply(BA,list(Bosque,Área),var))
desv.estand<-with(Data02,tapply(BA,list(Bosque),sd))
ns1<-with(Data02,tapply(BA,list(Bosque),length))
ponderaciones<-(1/varzas)/apply((1/varzas),1,sum,na.rm=TRUE)
apply(ponderaciones,1,sum,na.rm=TRUE)
medias1<-with(Data02,tapply(BA,list(Bosque,Área),mean))
medias2<-apply(medias1*ponderaciones,1,sum,na.rm=TRUE)
medias3<-with(Data02,tapply(BA,Bosque,mean))
sec1<-1:nrow(medias1)
var.wtd.media<-function(i,medias,varianzas,na.rm=TRUE){
  var.wtd.mean.cochran(medias1[i,],1/varianzas[i,],na.rm=TRUE)
}
r1<-sapply(sec1,var.wtd.media,medias1,varzas)
Andes<-data.frame(ns=ns1,media=medias3,desv.estand=desv.estand,
                  CV=desv.estand/medias3*100,
                  IC=1.96*desv.estand/sqrt(ns1),
                  media_w=medias2,desv.estand_w=sqrt(r1),
                  CV_w=sqrt(r1)/medias2*100,
                  IC_w=1.96*sqrt(r1))
Andes
write.table(Andes,file="Andes.csv",row.names=TRUE,sep=";",dec=",")

##########
# Caribe #
##########

Data02<-subset(Data01,Región=="Caribe")
varzas<-with(Data02,tapply(BA,list(Bosque,Área),var))
desv.estand<-with(Data02,tapply(BA,list(Bosque),sd))
ns1<-with(Data02,tapply(BA,list(Bosque),length))
ponderaciones<-(1/varzas)/apply((1/varzas),1,sum,na.rm=TRUE)
apply(ponderaciones,1,sum,na.rm=TRUE)
medias1<-with(Data02,tapply(BA,list(Bosque,Área),mean))
medias2<-apply(medias1*ponderaciones,1,sum,na.rm=TRUE)
medias3<-with(Data02,tapply(BA,Bosque,mean))
sec1<-1:nrow(medias1)
var.wtd.media<-function(i,medias,varianzas,na.rm=TRUE){
  var.wtd.mean.cochran(medias1[i,],1/varianzas[i,],na.rm=TRUE)
}
r1<-sapply(sec1,var.wtd.media,medias1,varzas)
Caribe<-data.frame(ns=ns1,media=medias3,desv.estand=desv.estand,
                   CV=desv.estand/medias3*100,
                   IC=1.96*desv.estand/sqrt(ns1),
                   media_w=medias2,desv.estand_w=sqrt(r1),
                   CV_w=sqrt(r1)/medias2*100,
                   IC_w=1.96*sqrt(r1))
Caribe
write.table(Caribe,file="Caribe.csv",row.names=TRUE,sep=";",dec=",")

#############
# Orinoquia #
#############

Data02<-subset(Data01,Región=="Orinoquia")
varzas<-with(Data02,tapply(BA,list(Bosque,Área),var))
desv.estand<-with(Data02,tapply(BA,list(Bosque),sd))
ns1<-with(Data02,tapply(BA,list(Bosque),length))
ponderaciones<-(1/varzas)/apply((1/varzas),1,sum,na.rm=TRUE)
apply(ponderaciones,1,sum,na.rm=TRUE)
medias1<-with(Data02,tapply(BA,list(Bosque,Área),mean))
medias2<-apply(medias1*ponderaciones,1,sum,na.rm=TRUE)
medias3<-with(Data02,tapply(BA,Bosque,mean))
sec1<-1:nrow(medias1)
var.wtd.media<-function(i,medias,varianzas,na.rm=TRUE){
  var.wtd.mean.cochran(medias1[i,],1/varianzas[i,],na.rm=TRUE)
}
r1<-sapply(sec1,var.wtd.media,medias1,varzas)
Orinoquia<-data.frame(ns=ns1,media=medias3,desv.estand=desv.estand,
                      CV=desv.estand/medias3*100,
                      IC=1.96*desv.estand/sqrt(ns1),
                      media_w=medias2,desv.estand_w=sqrt(r1),
                      CV_w=sqrt(r1)/medias2*100,
                      IC_w=1.96*sqrt(r1))
Orinoquia
write.table(Orinoquia,file="Orinoquia.csv",row.names=TRUE,sep=";",dec=",")

############
# Pacifico #
############

Data02<-subset(Data01,Región=="Pacifico")
varzas<-with(Data02,tapply(BA,list(Bosque,Área),var))
desv.estand<-with(Data02,tapply(BA,list(Bosque),sd))
ns1<-with(Data02,tapply(BA,list(Bosque),length))
ponderaciones<-(1/varzas)/apply((1/varzas),1,sum,na.rm=TRUE)
apply(ponderaciones,1,sum,na.rm=TRUE)
medias1<-with(Data02,tapply(BA,list(Bosque,Área),mean))
medias2<-apply(medias1*ponderaciones,1,sum,na.rm=TRUE)
medias3<-with(Data02,tapply(BA,Bosque,mean))
sec1<-1:nrow(medias1)
var.wtd.media<-function(i,medias,varianzas,na.rm=TRUE){
  var.wtd.mean.cochran(medias1[i,],1/varianzas[i,],na.rm=TRUE)
}
r1<-sapply(sec1,var.wtd.media,medias1,varzas)
Pacifico<-data.frame(ns=ns1,media=medias3,desv.estand=desv.estand,
                     CV=desv.estand/medias3*100,
                     IC=1.96*desv.estand/sqrt(ns1),
                     media_w=medias2,desv.estand_w=sqrt(r1),
                     CV_w=sqrt(r1)/medias2*100,
                     IC_w=1.96*sqrt(r1))
Pacifico
write.table(Pacifico,file="Pacifico.csv",row.names=TRUE,sep=";",dec=",")