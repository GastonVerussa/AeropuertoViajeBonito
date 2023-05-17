package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.activos.GeneradorPasajeros;

public abstract class PuertasAeropuerto {
    
    private static final Object monitorAbierto = new Object();
    private static boolean estaAbierto = false;
    private static final ThreadGroup hilosPersonas = new ThreadGroup("personas");
    private static GeneradorPasajeros generadorPasajeros;
    
    public static ThreadGroup getThreadGroup(){
        return hilosPersonas;
    }
    
    public static void setGenerador(GeneradorPasajeros generadorPasajeros){
        PuertasAeropuerto.generadorPasajeros = generadorPasajeros;
    }
    
    public static void abrir(){
        synchronized (monitorAbierto) {
            estaAbierto = true;
            monitorAbierto.notifyAll();
        }
    }
    
    public static void cerrarIngreso(){
        //  Lo interrumpe para avisarle el cierre de ingreso de pasajeros
        generadorPasajeros.interrupt();
    }
    
    public static void cerrar(){
        //  Synchronized por el boolean "estaAbierto", para que no sea revisado antes de ser interrumpido
        synchronized (monitorAbierto) {
            estaAbierto = false;
            //  Interrumpe a todas las "personas" para avisarles el cierre de aeropuerto
            hilosPersonas.interrupt();
            //  Interrumpe al generador de pasajeros para avisarle que cerro el aeropuerto
            generadorPasajeros.interrupt();
        }
    }
    
    public static void esperarApertura(){
        synchronized (monitorAbierto) {
            while(!estaAbierto){
                try{
                    monitorAbierto.wait();
                }catch(InterruptedException e){
                    System.out.println("Error, hilo " + Thread.currentThread().getName() + " interrumpido mientras esepra apertura. Vuelve a esperar");
                }
            }
        }
    }
}
