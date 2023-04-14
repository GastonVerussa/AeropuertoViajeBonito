package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.Horario;
import com.mycompany.aeropuerto.ManejadorTiempo;

public class PorteroHorario extends Thread{
    
    private final int HORA_APERTURA = 6;
    private final int HORA_CIERRE = 22;
    
    public PorteroHorario(){
        
    }
    
    @Override
    public void run(){
        int diaActual = 1;
        //  Son las 5 AM
        ManejadorTiempo.setTiempoInicial(System.currentTimeMillis());
        try {
            Thread.sleep(ManejadorTiempo.duracionHora());
            while(true){
                imprimir("Bueno, son las 6AM, hora de abrir el aeropuerto");
                ManejadorTiempo.abrir();
                long tiempoRestante = ManejadorTiempo.milisRestantesParaHorario(new Horario(diaActual, HORA_CIERRE, 0));
                Thread.sleep(tiempoRestante);
                imprimir("Okay, son las 22AM, hora de cerrar el aeropuerto, vuelvan todos a sus casas");
                ManejadorTiempo.cerrar();
                tiempoRestante = ManejadorTiempo.milisRestantesParaHorario(new Horario(diaActual + 1, HORA_APERTURA, 0));
                Thread.sleep(tiempoRestante);
                diaActual++;
            }
        } catch (InterruptedException e) {
            imprimir("Tuve un problema de interrupcion");
        }
    }
    
    public void imprimir(String cadena){
        System.out.println("Portero: " + cadena);
    }
}
