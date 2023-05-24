package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.pasivosSinSincronizacion.Horario;
import com.mycompany.aeropuerto.pasivosSinSincronizacion.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivos.PuertasAeropuerto;
import java.util.Random;

public class GeneradorPasajeros extends Thread{
    
    private final Random random;
    private final int HORA_FIN_PASAJEROS = 18;
    private final int DURACION_MINIMA = ManejadorTiempo.duracionMinuto() * 2;
    private final int DURACION_MAXIMA = ManejadorTiempo.duracionMinuto() * 10;
    
    
    public GeneradorPasajeros(){
        super("GeneradorPasajeros");
        random = new Random(System.currentTimeMillis());
    }
    
    @Override
    public void run(){
        int diaActual = 1;
        //  Bucle en el que cada ciclo representa un dia
        while(true){
            //  Primero espera que abran el aeropuerto
            PuertasAeropuerto.esperarApertura();
            //  Reinicia el numero de pasajero cada dia
            int numPasajero = 1;
            //  Horario hasta que llegan los pasajeros (Ya que si llegan mas tarde es muy probable que no les de el tiempo
            Horario horarioFinPasajeros = new Horario(diaActual, HORA_FIN_PASAJEROS, 0);
            //  Mientras no se llegue al horario de fin
            try{
                while(true){
                        //  Crea un nuevo hilo pasajero
                        Pasajero pasajero = new Pasajero(String.valueOf(numPasajero));
                        pasajero.start();
                        numPasajero++;
                        //  Cada 2 a 10 minutos crea un pasajero nuevo
                        Thread.sleep(random.nextInt(DURACION_MINIMA, DURACION_MAXIMA + 1));
                }
            } catch (InterruptedException e){
                //  Fue interrumpido, avisando el cierre al ingreso de pasajeros
            }
            try{
                //  Espera que cierre el aeropuerto
                while(true){
                    Thread.sleep(ManejadorTiempo.duracionHora() * 2);
                }
            } catch (InterruptedException e){
                //  Fue interrumpido, avisando el cierre del aeropuerto
            }
            diaActual++;
        }
    }
}
