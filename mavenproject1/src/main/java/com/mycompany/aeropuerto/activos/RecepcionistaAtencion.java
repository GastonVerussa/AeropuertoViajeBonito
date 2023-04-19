package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.Vuelo;
import com.mycompany.aeropuerto.pasivos.PuestoAtencion;
public class RecepcionistaAtencion extends Thread{
 
    private final PuestoAtencion puesto;
    private final String nombre;
    
    public RecepcionistaAtencion(String nombre, PuestoAtencion puestoAtencion){
        super(ManejadorTiempo.getThreadGroup(), "Recepcionista " + nombre);
        this.nombre = "Recepcionista " + nombre;
        this.puesto = puestoAtencion;
        System.out.println("Recepcionista " + nombre + ": Creado correctamente");
    }
    
    @Override
    public void run(){
        while(true){
            while(true){
                try {
                    ManejadorTiempo.esperarApertura();
                    break;
                } catch(InterruptedException e){
                    imprimir("Error esperando que abra");
                }
            }
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
                    Thread.sleep((int) (Math.random() * ManejadorTiempo.duracionMinuto()) + ManejadorTiempo.duracionMinuto());
                    imprimir("Su vuelo es el numero " + vueloPasajero.getNumVuelo() + ", debe ir al puesto de embarque "
                            + vueloPasajero.getPuertoEmbarque() + " en la terminal " + vueloPasajero.getTerminal().getNombre());
                    puesto.darInformacionCliente(vueloPasajero.getTerminal(), vueloPasajero.getPuertoEmbarque());
                    puesto.esperarclienteInformacion();
                    
                    //  Bucle si debe checkear tiempo
                    /*
                    if(puesto.esperarCliente()){
                        imprimir("Buenas, bienvenido al puesto de " + puesto.getAerolinea() + ", ¿me permite su pasaje por favor?");
                        puesto.avisarCliente();
                        Vuelo vueloPasajero = puesto.recuperarTerminalPuesto();
                        imprimir("Dejeme revisar...");
                        //  Simula la espera, tarda entre 1 a 2 minutos
                        Thread.sleep((int) (Math.random() * ManejadorTiempo.duracionMinuto()) + ManejadorTiempo.duracionMinuto());
                        imprimir("Su vuelo es el numero " + vueloPasajero.getNumVuelo() + ", debe ir al puesto de embarque "
                                + vueloPasajero.getPuertoEmbarque() + " en la terminal " + vueloPasajero.getTerminal().getNombre());
                        puesto.darInformacionCliente(vueloPasajero.getTerminal(), vueloPasajero.getPuertoEmbarque());
                        puesto.esperarclienteInformacion();
                    }
                    */
                }
            } catch (InterruptedException ex) {
                imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
            }
        }
    }
    
    public void imprimir(String cadena){
        System.out.println(nombre + ": " + cadena);
    }
}
