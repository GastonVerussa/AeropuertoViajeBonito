package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.Horario;
import com.mycompany.aeropuerto.ManejadorTiempo;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneradorPasajeros extends Thread{
    
    private final Random random;
    private final int HORA_FIN_PASAJEROS = 18;
    
    
    public GeneradorPasajeros(){
        super("GeneradorPasajeros");
        random = new Random(System.currentTimeMillis());
    }
    
    @Override
    public void run(){
        int diaActual = 1;
        //  Bucle en el que cada ciclo representa un dia
        while(true){
            try {
                //  Primero espera que abran el aeropuerto
                ManejadorTiempo.esperarApertura();
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
                            Thread.sleep(random.nextInt(ManejadorTiempo.duracionMinuto() * 2, ManejadorTiempo.duracionMinuto() * 10));
                    }
                } catch (InterruptedException e){
                }
                diaActual++;
            } catch (InterruptedException ex) {
                Logger.getLogger(GeneradorPasajeros.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
