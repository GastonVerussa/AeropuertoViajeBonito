package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivos.HallCentral;
import com.mycompany.aeropuerto.pasivos.PuestoAtencion;

public class Guardia extends Thread{

    private final String nombre;
    private final PuestoAtencion puesto;
    
    public Guardia(String nombre, PuestoAtencion puesto) {
        super(ManejadorTiempo.getThreadGroup(), "Guardia " + nombre);
        this.nombre = "Guardia " + nombre;
        this.puesto = puesto;
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
                    imprimir("Esperando que se desocupe un espacio.");
                    puesto.esperarDesocupe();
                    //  Si no hay nadie en el hall, entonces no avisa
                    if(HallCentral.hayAlguien()){
                        //  Si hay alguna persona (No importa para que puesto), avisa
                        imprimir("Buenas, hay un lugar disponible para " + puesto.getAerolinea());
                        HallCentral.avisarHall(puesto);
                    } else {
                        imprimir("Hmm, se libero un espacio, pero no hay nadie en el hall");
                    }
                }
                
                //  While si debe checkear tiempo cada tanto
                /*
                while(true){
                    imprimir("Esperando que se desocupe un espacio.");
                    if(puesto.esperarDesocupe()){
                        //  Si no hay nadie en el hall, entonces no avisa
                        if(HallCentral.hayAlguien()){
                            //  Si hay alguna persona (No importa para que puesto), avisa
                            imprimir("Buenas, hay un lugar disponible para " + puesto.getAerolinea());
                            HallCentral.avisarHall(puesto);
                        }
                    }
                    imprimir("A ver que hora es");
                }
                */
            } catch(InterruptedException e){
                imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
            }
        }
    }
    
    public void imprimir(String cadena){
        System.out.println(nombre + ": " + cadena);
    }
}
