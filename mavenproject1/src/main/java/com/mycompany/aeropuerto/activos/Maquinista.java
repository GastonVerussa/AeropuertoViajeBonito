package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivos.Tren;

public class Maquinista extends Thread{
    
    private final Tren tren;
    private final String nombre;
    private final String[] terminalesOrden;
    
    public Maquinista(String nombre, Tren tren, String[] terminales){
        super(nombre);
        this.nombre = nombre;
        this.tren = tren;
        this.terminalesOrden = terminales;
    }
    
    @Override
    public void run(){
        while(true){
            try{
                ManejadorTiempo.esperarApertura();
            } catch(InterruptedException e){
                imprimir("Tuve un problema esperando que se abra el aeropuerto");
            }
            imprimir("Llegue al aeropuerto, otro dia de trabajo");
            while(ManejadorTiempo.estaAbierto()){
                imprimir("Hora de esperar que la gente se suba");
                boolean arrancar = false;
                try{
                    if(tren.esperarCapacidad()){
                        imprimir("Veo que se llenó el tren");
                        arrancar = true;
                    } else { 
                        if(tren.estaVacio()){
                            imprimir("Veo que nadie se subio al tren");
                            arrancar = false;
                        } else {
                            imprimir("Veo que hay personas en el tren");
                            arrancar = true;
                        }
                    }
                } catch (InterruptedException e){
                    imprimir("Tuve un problema esperando que se llene el tren");
                }
                if(arrancar){
                    imprimir("Muy bien, listos para partir");
                    for(String terminal : terminalesOrden){
                        imprimir("Yendo a la terminal " + terminal);
                        try {
                            //  Simula la espera del viaje entre terminales
                            Thread.sleep(((int) (Math.random() * 2000)) + 1000);
                        } catch (InterruptedException ex) {
                            imprimir("Tuve un error viajando de terminal a terminal");
                        }
                        imprimir("A ver si alguien quiere bajarse en la terminal " + terminal);
                        //  Checkea si alguien pidio bajarse en esa parada
                        if(tren.checkearParada(terminal)){
                            imprimir("Si. Bienvenidos a la terminal " + terminal + ". Espero que se bajen, buen viaje.");
                            try {
                                //  Si alguien pidio, se frena y espera a que se bajen
                                tren.arribarParada(terminal);
                            } catch (InterruptedException ex) {
                                imprimir("Tuve un problema frenando en la terminal " + terminal);
                            }
                            imprimir("Muy bien, ya estan todos, podemos seguir camino");
                        } else {
                            imprimir("Parece que nadie se quiere bajar en la terminal " + terminal + ". Pasamos de largo a la siguiente");
                        }
                    }
                    imprimir("Ya terminamos con todas las terminales, volviendo a la estacion base");
                    try {
                        Thread.sleep(ManejadorTiempo.duracionMinuto());
                        tren.volverEstacionBase();
                    } catch (InterruptedException ex) {
                        imprimir("Tuve un problema volviendo a la estacion base.");
                    }
                }
                imprimir("Bien, hora de ver que hora es");
            }
            imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
        }
    }
    
    public void imprimir(String cadena){
        System.out.println("Maquinista " + nombre + ": " + cadena);
    }
}
