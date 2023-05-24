package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.pasivosSinSincronizacion.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivos.HallCentral;
import com.mycompany.aeropuerto.pasivos.PuertasAeropuerto;
import com.mycompany.aeropuerto.pasivos.PuestoAtencion;

public class Guardia extends Thread{

    private final String nombre;
    private final PuestoAtencion puesto;
    
    public Guardia(String nombre, PuestoAtencion puesto) {
        super(PuertasAeropuerto.getThreadGroup(), "Guardia " + nombre);
        this.nombre = "Guardia " + nombre;
        this.puesto = puesto;
    }
    
    @Override
    public void run(){
        while(true){
            PuertasAeropuerto.esperarApertura();
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
            } catch(InterruptedException e){
                imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
            }
        }
    }
    
    public void imprimir(String cadena){
        int identacion = 2;
        String cadenaFinal = "";
        for(int i = 1; i <= identacion; i++){
            cadenaFinal += "---";
        }
        cadenaFinal += nombre + ": " + cadena;
        System.out.println(cadenaFinal);
    }
}
