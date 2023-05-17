package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.Horario;
import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivos.HallCentral;
import com.mycompany.aeropuerto.pasivos.PuertasAeropuerto;

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
            Horario siguienteHorario = new Horario(1, 0, 0);
            long tiempoRestante;
            while(true){
                imprimir("Bueno, son las 6AM, hora de abrir el aeropuerto");
                PuertasAeropuerto.abrir();
                siguienteHorario.setDatos(diaActual, HORA_FIN_INGRESO_PASAJEROS, 0);
                tiempoRestante = ManejadorTiempo.milisRestantesParaHorario(siguienteHorario);
                if(tiempoRestante > 0){
                    Thread.sleep(tiempoRestante);
                } else {
                    imprimir("-------------------------------------");
                    imprimir("ERROR, PORTERO ATRASADO. TIEMPO DE ESPERA NEGATIVO");
                    imprimir("Dia actual mio: " + diaActual + ", Dia actual segun manejador: " + ManejadorTiempo.getHorarioActual().getDia());
                    imprimir("Horario al que debo avisar " + siguienteHorario.toString());
                    imprimir("Horario actual segun manejador " + ManejadorTiempo.getHorarioActual().toString());
                    imprimir("Diferencia milis segun manejador " + ManejadorTiempo.milisRestantesParaHorario(siguienteHorario));
                    imprimir("-------------------------------------");
                }
                imprimir("Okay, son las 18hs, ya no ingresan mas pasajeros, no les da el tiempo");
                PuertasAeropuerto.cerrarIngreso();
                siguienteHorario.setDatos(diaActual, HORA_CIERRE, 0);
                tiempoRestante = ManejadorTiempo.milisRestantesParaHorario(siguienteHorario);
                if(tiempoRestante > 0){
                    Thread.sleep(tiempoRestante);
                } else {
                    imprimir("-------------------------------------");
                    imprimir("ERROR, PORTERO ATRASADO. TIEMPO DE ESPERA NEGATIVO");
                    imprimir("Dia actual mio: " + diaActual + ", Dia actual segun manejador: " + ManejadorTiempo.getHorarioActual().getDia());
                    imprimir("Horario al que debo avisar " + siguienteHorario.toString());
                    imprimir("Horario actual segun manejador " + ManejadorTiempo.getHorarioActual().toString());
                    imprimir("Diferencia milis segun manejador " + ManejadorTiempo.milisRestantesParaHorario(siguienteHorario));
                    imprimir("-------------------------------------");
                }
                imprimir("Okay, son las 22hs, hora de cerrar el aeropuerto, vuelvan todos a sus casas");
                PuertasAeropuerto.cerrar();
                //  Limpia el hall, ver si hay un lugar mas apropiado
                HallCentral.limpiarHall();
                siguienteHorario.setDatos(diaActual + 1, HORA_APERTURA, 0);
                tiempoRestante = ManejadorTiempo.milisRestantesParaHorario(siguienteHorario);
                if(tiempoRestante > 0){
                    Thread.sleep(tiempoRestante);
                } else {
                    imprimir("-------------------------------------");
                    imprimir("ERROR, PORTERO ATRASADO. TIEMPO DE ESPERA NEGATIVO");
                    imprimir("Dia actual mio: " + diaActual + ", Dia actual segun manejador: " + ManejadorTiempo.getHorarioActual().getDia());
                    imprimir("Horario al que debo avisar " + siguienteHorario.toString());
                    imprimir("Horario actual segun manejador " + ManejadorTiempo.getHorarioActual().toString());
                    imprimir("Diferencia milis segun manejador " + ManejadorTiempo.milisRestantesParaHorario(siguienteHorario));
                    imprimir("-------------------------------------");
                }
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
