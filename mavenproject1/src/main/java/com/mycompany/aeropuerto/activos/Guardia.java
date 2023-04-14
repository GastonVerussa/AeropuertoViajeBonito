package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.Vuelo;
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
            try{
                ManejadorTiempo.esperarApertura();
            } catch(InterruptedException e){
                imprimir("Tuve un problema esperando que se abra el aeropuerto");
            }
            imprimir("Llegue al aeropuerto, otro dia de trabajo");
            while(ManejadorTiempo.estaAbierto()){
                try {
                    imprimir("Esperando que se desocupe un espacio.");
                    if(!puesto.esperarDesocupe()){
                        imprimir("Buenas, hay un lugar disponible para " + puesto.getAerolinea());
                        hall.avisarHall(puesto);
                    }
                    imprimir("A ver que hora es");
                } catch (InterruptedException ex) {
                    imprimir("Tuve un problema");
                }
            }
            imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
        }
    }
    
    public void imprimir(String cadena){
        System.out.println("Guardia " + nombre + ": " + cadena);
    }
}
