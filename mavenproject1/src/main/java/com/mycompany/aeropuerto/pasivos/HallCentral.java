package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.pasivosSinSincronizacion.ManejadorTiempo;

public class HallCentral {
    
    private static int cantidadPersonas = 0;
    
    public HallCentral(){
    }
    
    public static void limpiarHall(){
        cantidadPersonas = 0;
    }
    
    //  Metodo para que el pasajero espere en el hall cuando la cola este llena
    //      es synchronized por la variable cantidadPersonas.
    public synchronized static void esperarHall(PuestoAtencion puestoAtencion) throws InterruptedException{
        synchronized (puestoAtencion) {
            cantidadPersonas++;
            //  Timeout por si justo entre que no pudo entrar a la cola y vino a esperar ya se liberaron todos los espacios.
            puestoAtencion.wait(ManejadorTiempo.duracionMinuto() * 10);
            cantidadPersonas--;
        }
    }
    
    //  Metodo para que el guardia avise que se libero un lugar para el puesto de atencion. 
    public static void avisarHall(PuestoAtencion puestoAtencion){
        synchronized (puestoAtencion) {
            puestoAtencion.notify();
        }
    }
    
    //  Devuelve si hay alguien esperando en todo el hall
    public synchronized static boolean hayAlguien(){
        return cantidadPersonas != 0;
    }
}
