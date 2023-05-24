package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.pasivosSinSincronizacion.Vuelo;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import com.mycompany.aeropuerto.pasivosSinSincronizacion.Pasaje;
import com.mycompany.aeropuerto.activos.Pasajero;

public class PuestoAtencion {

    private final String aerolinea;
    private final HashMap<Integer, Vuelo> mapaVuelos;
    private final ArrayBlockingQueue<Pasajero> colaEspera;
    private final Semaphore semaforoRecepcionista;
    private final Semaphore semaforoGuardia;
    private final Semaphore semaforoPasajero;
    private Pasaje pasajeActual;
    private Terminal terminalPasajeActual;
    private int puertoEmbarqueActual;
    
    public PuestoAtencion(String aerolinea, int capacidad){
        this.aerolinea = aerolinea;
        mapaVuelos = new HashMap<>();
        colaEspera = new ArrayBlockingQueue(capacidad);
        semaforoRecepcionista = new Semaphore(0);
        semaforoGuardia = new Semaphore(0);
        semaforoPasajero = new Semaphore(0);
    }
    
    public void limpiar(){
        colaEspera.clear();
        semaforoGuardia.tryAcquire();
        semaforoRecepcionista.tryAcquire();
        semaforoPasajero.tryAcquire();
        //  Se reseteaban, pero no es necesario
        //pasajeActual = null;
        //terminalPasajeActual = null;
        //puertoEmbarqueActual = 0;
    }
    
    public String getAerolinea(){
        return aerolinea;
    }
    
    //  Metodo para agregar los vuelos de la aerolinea
    public boolean agregarVuelo(Vuelo vuelo){
        //  Debe checkear que no exista un vuelo con el mismo puerto y horario
        boolean exito = vuelo.getTerminal().cargarVuelo(vuelo);
        //  Si no existe un vuelo del mismo horario y puerto, agrega el vuelo
        if(exito) mapaVuelos.put(vuelo.getNumVuelo(), vuelo);
        return exito;
    }
    
    public boolean eliminarVuelo(int numeroVuelo){
        return (mapaVuelos.remove(numeroVuelo) != null);
    }
    
    public void vaciarVuelos(){
        mapaVuelos.clear();
    }
    
    //  Metodos para el hilo Pasajero
    
    public boolean entrarCola(Pasajero pasajero) throws InterruptedException{
        boolean exito;
        synchronized (pasajero) {
            exito = colaEspera.offer(pasajero);
            if(exito){
                pasajero.wait();
                semaforoPasajero.acquire();
            }
        }
        return exito;
    }
    
    public void mostrarPasaje(Pasaje pasaje) throws InterruptedException{
        this.pasajeActual = pasaje;
        semaforoRecepcionista.release();
        semaforoPasajero.acquire();
    }
    
    public Terminal recuperarTerminal(){
        return terminalPasajeActual;
    }
    
    public int recuperarPuertoEmbarque(){
        return puertoEmbarqueActual;
    }
    
    public void despedirse(){
        semaforoRecepcionista.release();
    }
    
    //  Metodos para el hilo RecepcionistaAtencion
    
    public void esperarCliente() throws InterruptedException{
        Pasajero clienteActual = colaEspera.take();
        semaforoGuardia.release();
        synchronized (clienteActual) {
            clienteActual.notify();
        }
    }
    
    public void avisarCliente() throws InterruptedException{
        semaforoPasajero.release();
        semaforoRecepcionista.acquire();
    }
    
    public Vuelo recuperarTerminalPuesto(){
        return mapaVuelos.get(pasajeActual.getNumVuelo());
    }
    
    public synchronized void darInformacionCliente(Terminal terminal, int puestoEmbarque) throws InterruptedException{
        terminalPasajeActual = terminal;
        puertoEmbarqueActual = puestoEmbarque;
        semaforoPasajero.release();
        semaforoRecepcionista.acquire();
    }
    
    //  Metodo para el guardia

    public void esperarDesocupe() throws InterruptedException{
        semaforoGuardia.acquire();
    }
}
