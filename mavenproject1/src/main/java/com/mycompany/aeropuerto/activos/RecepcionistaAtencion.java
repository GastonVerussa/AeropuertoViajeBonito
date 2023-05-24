package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.pasivosSinSincronizacion.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivosSinSincronizacion.Vuelo;
import com.mycompany.aeropuerto.pasivos.PuertasAeropuerto;
import com.mycompany.aeropuerto.pasivos.PuestoAtencion;
import java.util.Random;
public class RecepcionistaAtencion extends Thread{
 
    private final PuestoAtencion puesto;
    private final String nombre;
    private final Random random = new Random(System.currentTimeMillis());
    private final int DURACION_ATENCION_MINIMA = ManejadorTiempo.duracionMinuto() * 1;
    private final int DURACION_ATENCION_MAXIMA = ManejadorTiempo.duracionMinuto() * 2;
    
    
    public RecepcionistaAtencion(String nombre, PuestoAtencion puestoAtencion){
        super(PuertasAeropuerto.getThreadGroup(), "Recepcionista " + nombre);
        this.nombre = "Recepcionista " + nombre;
        this.puesto = puestoAtencion;
    }
    
    @Override
    public void run(){
        while(true){
            PuertasAeropuerto.esperarApertura();
            imprimir("Llegue al aeropuerto, otro dia de trabajo");
            try{
                while(true){
                    //  Espera un cliente
                    puesto.esperarCliente();
                    imprimir("Buenas, bienvenido al puesto de " + puesto.getAerolinea() + ", ¿me permite su pasaje por favor?");
                    puesto.avisarCliente();
                    Vuelo vueloPasajero = puesto.recuperarTerminalPuesto();
                    imprimir("Dejeme revisar...");
                    //  Simula la espera, tarda entre 1 a 2 minutos
                    Thread.sleep(random.nextInt(DURACION_ATENCION_MINIMA, DURACION_ATENCION_MAXIMA + 1));
                    imprimir("Su vuelo es el numero " + vueloPasajero.getNumVuelo() + ", debe ir al puesto de embarque "
                            + vueloPasajero.getPuertoEmbarque() + " en la terminal " + vueloPasajero.getTerminal().getNombre());
                    puesto.darInformacionCliente(vueloPasajero.getTerminal(), vueloPasajero.getPuertoEmbarque());
                    imprimir("De nada, que tenga un agradable vuelo.");
                }
            } catch (InterruptedException ex) {
                imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
                puesto.limpiar();
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
