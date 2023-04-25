package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.Horario;
import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivos.HallCentral;

public class PorteroHorario extends Thread{
    
    private final int HORA_APERTURA = 6;
    private final int HORA_FIN_INGRESO_PASAJEROS = 18;
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
            long tiempoRestante;
            while(true){
                imprimir("Bueno, son las 6AM, hora de abrir el aeropuerto");
                ManejadorTiempo.abrir();
                tiempoRestante = ManejadorTiempo.milisRestantesParaHorario(new Horario(diaActual, HORA_FIN_INGRESO_PASAJEROS, 0));
                Thread.sleep(tiempoRestante);
                imprimir("Okay, son las 18hs, ya no ingresan mas pasajeros, no les da el tiempo");
                ManejadorTiempo.cerrarIngreso();
                tiempoRestante = ManejadorTiempo.milisRestantesParaHorario(new Horario(diaActual, HORA_CIERRE, 0));
                Thread.sleep(tiempoRestante);
                imprimir("Okay, son las 22hs, hora de cerrar el aeropuerto, vuelvan todos a sus casas");
                ManejadorTiempo.cerrar();
                //  Limpia el hall, ver si hay un lugar mas apropiado
                HallCentral.limpiarHall();
                tiempoRestante = ManejadorTiempo.milisRestantesParaHorario(new Horario(diaActual + 1, HORA_APERTURA, 0));
                Thread.sleep(tiempoRestante);
                diaActual++;
            }
        } catch (InterruptedException e) {
            System.out.println(" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
            System.out.println(" ----------------------------------------------------------- ");
            imprimir("Tuve un problema de interrupcion");
            System.out.println(" ----------------------------------------------------------- ");
            System.out.println(" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
        }
    }
    
    public void imprimir(String cadena){
        System.out.println(" ----------------------------------------------------------- ");
        System.out.println("Portero: " + cadena);
        System.out.println(" ----------------------------------------------------------- ");
    }
}
