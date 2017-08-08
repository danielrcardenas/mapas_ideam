###############################################################################
# Cargar librerías necesarias para conducir la estimación de la biomasa aérea #
###############################################################################
cargarLattice<-function()
{
tryCatch({
  library(lattice)
  return(FALSE)
}, error = function(ex) {
  print("No se pudo cargar la libreria lattice")
  return(TRUE)
})
}

cargarExtremevalues<-function()
{
tryCatch({
  library(extremevalues)
  return(FALSE)
}, error = function(ex) {
  print("No se pudo cargar la libreria extremevalues")
  return(TRUE)
})
}

#######################################################
# Cargar datos necesarios para realizar la estimación #
#######################################################

CargarIndividuos<-function(y){
tryCatch({
ID1<-read.csv2(y,enc="latin1")
ID2<-subset(ID1,D...cm.>=10)
ID3<-subset(ID2,Entra!="No")
return (list(FALSE,ID3))
}, error = function(ex) {
print("Error en la carga de datos de individuos")
return (TRUE)
})
}

CargarParcelas<-function(x){
tryCatch({
PL1<-read.csv2(x,enc="latin1")
return (list(FALSE,PL1))
}, error = function(ex) {
print("Error en la carga de datos de parcelas")
return (TRUE)
})
}

CargarBosques<-function(b){
tryCatch({
BQ1<-read.csv2(b,enc="latin1")
return (list(FALSE,BQ1))
}, error = function(ex) {
print("Error en la carga de datos de bosques")
return (TRUE)
})
}

Merge<-function(ID3,PL1){
tryCatch({

return (list(FALSE,DT1))
}, error = function(ex) {
print("Error realizando el merge")
return(TRUE)
})
}

############################################
# Estimar la biomasa aérea de las parcelas #
############################################

CB<-function(x){
#tryCatch({
  Bio1<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(3.652+(-1.697*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(1.285*log(Dens..g.cm3.))) # Tropical Dry (Álvarez et al. 2012)
  Bio2<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(2.406+(-1.289*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(0.445*log(Dens..g.cm3.))) # Tropical Moist (Álvarez et al. 2012)
  Bio3<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(1.662+(-1.114*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(0.331*log(Dens..g.cm3.))) # Tropical Rain (Álvarez et al. 2012)
  Bio4<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(1.960+(-1.098*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(1.061*log(Dens..g.cm3.))) # Premontane Moist (Álvarez et al. 2012)
  Bio5<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(1.836+(-1.255*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(-0.222*log(Dens..g.cm3.))) # Lower Montane Wet (Álvarez et al. 2012)
  Bio6<-function(Dens..g.cm3.,D...cm.,Alt=NULL,AB=NULL) exp(3.130+(-1.536*log(D...cm.))+(1.169*((log(D...cm.))^2))+(-0.122*((log(D...cm.))^3))+(1.767*log(Dens..g.cm3.))) # Montane Wet (Álvarez et al. 2012)
  EQ1<-c("Bio1","Bio2","Bio3","Bio4","Bio5","Bio6")
  BA<-get(EQ1[as.integer(x[["EQ"]])])(as.numeric(x[["Dens..g.cm3."]]),
  as.numeric(x[["D...cm."]]),
  as.numeric(x[["Alt"]]),
  as.numeric(x[["AB"]]))
  #return(BA)
#}, error = function(ex) {
#print("Error en el calculo de la Biomasa")
#})
}

CBC<-function(ID3,PL1){
tryCatch({
DT1<-merge(ID3,PL1)
DT1$BAKg<-apply(DT1,1,CB)
DT2<-aggregate(DT1$BAKg/((DT1$Área*10000)/10),list(Plot=DT1[,"Plot"]),sum,na.rm=TRUE)
DT2$BA<-DT2$x
MG1<-merge(PL1,DT2)
DT3<-subset(MG1,select=c(Plot,Área,Año,Bosque,BA))
print(DT3)
print(DT3$BA)
OUT<-getOutliers(DT3$BA,method="II",distribution="lognormal")
print(OUT)            
DT4<-DT3[-c(OUT$iRight,OUT$iLeft),]

return(list(FALSE,DT3))
}, error = function(ex) {
print("Error en el calculo de la biomasa y el carbono")
return (TRUE)
})
}

###########################################################
# Identificar parcelas con valores biomasa aérea atípicos #
###########################################################

ATP<-function(DT3){
tryCatch({
OUT<-getOutliers(DT3$BA,method="II",distribution="lognormal")
print(OUT)            
DT4<-DT3[-c(OUT$iRight,OUT$iLeft),]
return(list(FALSE,DT4))
},error = function(ex){
print("Error en el calculo de los valores atipicos")
return (TRUE)
})
}

###############################################
# Estimar la biomasa aérea por tipo de bosque #
###############################################

ABC<-function(r,DT4,BQ1){
tryCatch({
NS1<-with(DT4,tapply(BA,list(Bosque),length))
MN1<-with(DT4,tapply(BA,Bosque,mean))
DS1<-with(DT4,tapply(BA,list(Bosque),sd))
BQ2<-BQ1[order(BQ1$Bosque),]
EST<-data.frame(Ai=BQ2$Ai, # Área que ocupa cada tipo de bosque en el país (expresado en ha) #
                n=NS1, # Número de parcelas por tipo de bosque #
                BAj=MN1, # Biomasa aérea por tipo de bosque (expresada en Mg/ha) #
                DS=DS1, # Desviación estándar asociada al promedio de biomasa aérea por tipo de bosque (expresada en Mg/ha) #
                CV=DS1/MN1*100, # Coeficiente de variación por tipo de bosque (expresado en %) #
                IC=1.96*DS1/sqrt(NS1), # Intervalo de confianza asociado al promedio de biomasa aérea por tipo de bosque (expresado en Mg/ha) #
                BAi=(MN1*BQ2$Ai)/1000000000, # Biomasa aérea total potencial por tipo de bosque (expresada en Pg) #
                Ci=((MN1*0.5)*BQ2$Ai)/1000000000, # Potencial de Carbono almacenado en la biomasa aérea por tipo de bosque (expresado en Pg) #
                CO2e=((MN1*0.5*3.67)*BQ2$Ai)/1000000000) # Dióxido de Carbono que aún no ha sido emitido a la atmósfera por tipo de bosque (expresado en Pg) #
write.table(EST,file=r,row.names=TRUE,sep=";",dec=",")
},error = function(ex){
print("Error en la escritura de los datos")
return (TRUE)
})
}