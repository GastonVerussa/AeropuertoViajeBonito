package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivos.Tren;

public class Maquinista extends Thread{
    
    private final Tren tren;
    private final String nombre;
    private final String[] terminalesOrden;
    
    public Maquinista(String nombre, Tren tren, String[] terminales){
        super(ManejadorTiempo.getThreadGroup(), "Maquinista " + nombre);
        this.nombre = "Maquinista " + nombre;
        this.tren = tren;
        this.terminalesOrden = terminales;
    }
    
    @Override
    public void run(){
        while(true){
            while(true){
                try{
                    ManejadorTiempo.esperarApertura();
                    break;
                } catch(InterruptedException e){
                    imprimir("Tuve un problema esperando que se abra el aeropuerto");
                }
            }
            imprimir("Llegue al aeropuerto, otro dia de trabajo");
            try{
                while(true){
                    imprimir("Hora de esperar que la gente se suba");
                    boolean arrancar = false;
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
                    if(arrancar){
                        imprimir("Muy bien, listos para partir");
                        for(String terminal : terminalesOrden){
                            imprimir("Yendo a la terminal " + terminal);
                            //  Simula la espera del viaje entre terminales. Duracion: 2 a 5 min
                            Thread.sleep(((int) (Math.random() * ManejadorTiempo.duracionMinuto() * 3)) + ManejadorTiempo.duracionMinuto() * 2);
                            imprimir("A ver si alguien quiere bajarse en la terminal " + terminal);
                            //  Checkea si alguien pidio bajarse en esa parada
                            if(tren.checkearParada(terminal)){
                                imprimir("Si. Bienvenidos a la terminal " + terminal + ". Espero que se bajen, buen viaje.");
                                //  Si alguien pidio, se frena y espera a que se bajen
                                tren.arribarParada(terminal);
                                imprimir("Muy bien, ya estan todos, podemos seguir camino");
                            } else {
                                imprimir("Parece que nadie se quiere bajar en la terminal " + terminal + ". Pasamos de largo a la siguiente");
                            }
                        }
                        imprimir("Ya terminamos con todas las terminales, volviendo a la estacion base");
                        Thread.sleep(ManejadorTiempo.duracionMinuto());
                        imprimir("Llegué a la estacion base");
                        tren.volverEstacionBase();
                    }
                }
            } catch(InterruptedException e){
                imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
            }
        }
    }
    
    public void imprimir(String cadena){
        System.out.println(nombre + ": " + cadena);
    }
}
