package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.pasivos.HallCentral;
import com.mycompany.aeropuerto.pasivos.PuestoAtencion;

public class Guardia extends Thread{

    private final String nombre;
    private final PuestoAtencion puesto;
    private static HallCentral hall;
    
    public Guardia(String nombre, PuestoAtencion puesto) {
        super(nombre);
        this.nombre = nombre;
        this.puesto = puesto;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                imprimir("Esperando que se desocupe.");
                puesto.esperarDesocupe();
                imprimir("Buenas, hay un lugar disponible para " + puesto.getAerolinea());
                hall.avisarHall(puesto);
            } catch (InterruptedException ex) {
                imprimir("Tuve un problema");
            }
        }
    }
    
    public void imprimir(String cadena){
        System.out.println("Guardia " + nombre + ": " + cadena);
    }
}
