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

CargarDatos<-function(y){
tryCatch({
datos<-read.csv(y,enc="latin1")
return (list(FALSE,datos))
}, error = function(ex) {
print("No se pudo cargar el archivo de individuos")
return (TRUE)
})
}


IdentificarDistribucion<-function(x){
tryCatch({
Limit <- seq(0.15,0.95,0.01)
R2 <- numeric(length(Limit))
for ( i in 1:length(Limit) )
    R2[i] <- getOutliersII(datos$Biomasa, distribution=x,
FLim=c(0.1, Limit[i]) )$R2
A<-Limit
D1<-R2
return (list(FALSE,D1,A))
},error=function(ex){
print("Error identificacion la distribucion")
return(TRUE)
})
}

graficar<-function(){
tryCatch({
min_y<-min(c(D1,D2,D3,D4,D5))
max_y<-max(c(D1,D2,D3,D4,D5))
colores<-c("magenta","blue","red","brown","green")
png("C:/grafica.png")
plot(A,D1,type="l",col=colores[1],ylim=c(min_y,max_y),ann=FALSE)
lines(A,D2,type="l",pch=23,lty=1,col=colores[2])
lines(A,D3,type="l",pch=23,lty=1,col=colores[3])
lines(A,D4,type="l",pch=23,lty=1,col=colores[4])
lines(A,D5,type="l",pch=23,lty=1,col=colores[5])
title(main="Valores R2 y Fmax",col.main="black")
title(xlab= "Fmax",col.main="black")
title(ylab= "R2",col.main="black")
legend("bottomright",title="Distribucion",legend=c("Normal","Lognormal","Pareto","Weibull","Exponencial"),col=colores,lty=1)
dev.off()
})
}