package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivos.PuertasAeropuerto;
import com.mycompany.aeropuerto.pasivos.Tren;
import java.util.Random;

public class Maquinista extends Thread{
    
    private final Tren tren;
    private final String nombre;
    private final String[] terminalesOrden;
    private final int DURACION_TERMINAL_MINIMA = ManejadorTiempo.duracionMinuto() * 2;
    private final int DURACION_TERMINAL_MAXIMA = ManejadorTiempo.duracionMinuto() * 5;
    private final int DURACION_VUELTA_BASE = ManejadorTiempo.duracionMinuto() * 3;
    private final Random random = new Random(System.currentTimeMillis());
    
    public Maquinista(String nombre, Tren tren, String[] terminales){
        super(PuertasAeropuerto.getThreadGroup(), "Maquinista " + nombre);
        this.nombre = "Maquinista " + nombre;
        this.tren = tren;
        this.terminalesOrden = terminales;
    }
    
    @Override
    public void run(){
        while(true){
            PuertasAeropuerto.esperarApertura();
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
                            Thread.sleep(random.nextInt(DURACION_TERMINAL_MINIMA, DURACION_TERMINAL_MAXIMA + 1));
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
                        Thread.sleep(DURACION_VUELTA_BASE + 1);
                        imprimir("Llegué a la estacion base");
                        tren.volverEstacionBase();
                    }
                }
            } catch(InterruptedException e){
                imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
                try{
                    tren.limpiar();
                } catch(InterruptedException ex){
                    System.out.println(" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
                    System.out.println(" ----------------------------------------------------------- ");
                    imprimir("Me interrumpieron limpiando");
                    System.out.println(" ----------------------------------------------------------- ");
                    System.out.println(" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
                }
            }
        }
    }
    
    public void imprimir(String cadena){
        int identacion = 3;
        String cadenaFinal = "";
        for(int i = 1; i <= identacion; i++){
            cadenaFinal += "---";
        }
        cadenaFinal += nombre + ": " + cadena;
        System.out.println(cadenaFinal);
    }
}
