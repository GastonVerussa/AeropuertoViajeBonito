package com.mycompany.aeropuerto.pasivos;

public class HallCentral {
    
    private static int cantidadPersonas = 0;
    
    public HallCentral(){
    }
    
    public static void esperarHall(PuestoAtencion puestoAtencion) throws InterruptedException{
        synchronized (puestoAtencion) {
            cantidadPersonas++;
            puestoAtencion.wait();
            cantidadPersonas--;
        }
    }
    
    public static void avisarHall(PuestoAtencion puestoAtencion){
        synchronized (puestoAtencion) {
            puestoAtencion.notify();
        }
    }
    
    //  Devuelve si hay alguien esperando en todo el hall
    public static boolean hayAlguien(){
        return cantidadPersonas != 0;
    }
}
