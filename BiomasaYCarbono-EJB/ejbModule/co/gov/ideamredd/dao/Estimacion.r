###############################################################################
# Cargar librer�as necesarias para conducir la estimaci�n de la biomasa a�rea #
###############################################################################

library(lattice)
library(extremevalues)

#######################################################
# Cargar datos necesarios para realizar la estimaci�n #
#######################################################

ID1<-read.csv2("/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Individuos.csv",enc="latin1")
ID2<-subset(ID1,D...cm.>=10)
ID3<-subset(ID2,Entra!="No")
source("/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Ecuaciones.r")
EQ1<-c("Bio1","Bio2","Bio3","Bio4","Bio5","Bio6")
PL1<-read.csv2("/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Parcelas.csv",enc="latin1")
BQ1<-read.csv2("/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Bosque.csv",enc="latin1")
DT1<-merge(ID3,PL1)
print(DT1)

############################################
# Estimar la biomasa a�rea de las parcelas #
############################################

CB<-function(x){
  BA<-get(EQ1[as.integer(x[["EQ"]])])(as.numeric(x[["Dens..g.cm3."]]),
  as.numeric(x[["D...cm."]]),
  as.numeric(x[["Alt"]]),
  as.numeric(x[["AB"]]))
  return(BA)
}
DT1$BAKg<-apply(DT1,1,CB)
print(DT1$BAKg)
DT2<-aggregate(DT1$BAKg/((DT1$�rea*10000)/10),list(Plot=DT1[,"Plot"]),sum,na.rm=TRUE)
print(DT2)
DT2$BA<-DT2$x
print(DT2$BA)
MG1<-merge(PL1,DT2)
print(MG1)
DT3<-subset(MG1,select=c(Plot,�rea,A�o,Bosque,BA))
print(DT3)

###########################################################
# Identificar parcelas con valores biomasa a�rea at�picos #
###########################################################
            
OUT<-getOutliers(DT3$BA,method="II",distribution="lognormal")            
DT4<-DT3[-c(OUT$iRight,OUT$iLeft),]

###############################################
# Estimar la biomasa a�rea por tipo de bosque #
###############################################
            
NS1<-with(DT4,tapply(BA,list(Bosque),length))
MN1<-with(DT4,tapply(BA,Bosque,mean))
DS1<-with(DT4,tapply(BA,list(Bosque),sd))
BQ2<-BQ1[order(BQ1$Bosque),]
EST<-data.frame(Ai=BQ2$Ai, # �rea que ocupa cada tipo de bosque en el pa�s (expresado en ha) #
                n=NS1, # N�mero de parcelas por tipo de bosque #
                BAj=MN1, # Biomasa a�rea por tipo de bosque (expresada en Mg/ha) #
                DS=DS1, # Desviaci�n est�ndar asociada al promedio de biomasa a�rea por tipo de bosque (expresada en Mg/ha) #
                CV=DS1/MN1*100, # Coeficiente de variaci�n por tipo de bosque (expresado en %) #
                IC=1.96*DS1/sqrt(NS1), # Intervalo de confianza asociado al promedio de biomasa a�rea por tipo de bosque (expresado en Mg/ha) #
                BAi=(MN1*BQ2$Ai)/1000000000, # Biomasa a�rea total potencial por tipo de bosque (expresada en Pg) #
                Ci=((MN1*0.5)*BQ2$Ai)/1000000000, # Potencial de Carbono almacenado en la biomasa a�rea por tipo de bosque (expresado en Pg) #
                CO2e=((MN1*0.5*3.67)*BQ2$Ai)/1000000000) # Di�xido de Carbono que a�n no ha sido emitido a la atm�sfera por tipo de bosque (expresado en Pg) #

write.table(EST,file="/home/harry.sanchez/Documentos/Desarrollo/Algoritmos_R_marzo6_2014/Rutinas/Estimacion/Estimaci�n.csv",row.names=TRUE,sep=";",dec=",")
