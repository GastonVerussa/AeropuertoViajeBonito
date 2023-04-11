package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.Vuelo;
import com.mycompany.aeropuerto.pasivos.PuestoAtencion;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecepcionistaAtencion extends Thread{
 
    private final String aerolinea;
    private final PuestoAtencion puesto;
    private final String nombre;
    
    public RecepcionistaAtencion(String nombre, String aerolinea, PuestoAtencion puestoAtencion){
        super(nombre);
        this.nombre = nombre;
        this.aerolinea = aerolinea;
        this.puesto = puestoAtencion;
    }
    
    @Override
    public void run(){
        try {
            while(true){
                imprimir("Llegue al aeropuerto, otro dia de trabajo");
                while(ManejadorTiempo.estaAbierto()){
                    //  Espera un cliente
                    if(!puesto.esperarCliente()){
                        imprimir("Buenas, bienvenido al puesto de " + puesto.getAerolinea() + ", ¿me permite su pasaje por favor?");
                        puesto.avisarCliente();
                        Vuelo vueloPasajero = puesto.recuperarTerminalPuesto();
                        imprimir("Dejeme revisar...");
                        Thread.sleep((int) (Math.random() * 500) + 500);
                        imprimir("Su vuelo es el numero " + vueloPasajero.getNumVuelo() + ", debe ir al puesto de embarque "
                            + vueloPasajero.getPuertoEmbarque() + " en la terminal " + vueloPasajero.getTerminal().getNombre());
                        puesto.darInformacionCliente(vueloPasajero.getTerminal(), vueloPasajero.getPuertoEmbarque());
                        puesto.esperarclienteInformacion();
                    }
                }
                imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
                ManejadorTiempo.esperarApertura();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RecepcionistaAtencion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void imprimir(String cadena){
        System.out.println("Recepcionista " + nombre + ": " + cadena);
    }
}
