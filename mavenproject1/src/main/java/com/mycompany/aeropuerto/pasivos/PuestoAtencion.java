package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.Vuelo;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import com.mycompany.aeropuerto.Pasaje;

public class PuestoAtencion {

    private final HashMap<Integer, Vuelo> mapaVuelos;
    private final ArrayBlockingQueue<Semaphore> colaEspera;
    private final Semaphore semaforoRecepcionista;
    private Semaphore semaforoClienteActual;
    private Pasaje pasajeActual;
    private Terminal terminalPasajeActual;
    private int puertoEmbarqueActual;
    
    public PuestoAtencion(int capacidad){
        mapaVuelos = new HashMap<>();
        colaEspera = new ArrayBlockingQueue(capacidad);
        semaforoRecepcionista = new Semaphore(0);
    }
    
    //  Metodo para agregar los vuelos de la aerolinea
    public synchronized void agregarVuelo(Vuelo vuelo){
        mapaVuelos.put(vuelo.getNumVuelo(), vuelo);
    }
    
    //  Metodos para el hilo Pasajero
    
    public boolean entrarCola(Semaphore semaforoPasajero){
        return colaEspera.offer(semaforoPasajero);
    }
    
    public void esperarAtencion(Semaphore semaforoPasajero) throws InterruptedException{
        semaforoPasajero.acquire();
    }
    
    public void mostrarPasaje(Pasaje pasaje) throws InterruptedException{
        this.pasajeActual = pasaje;
        semaforoRecepcionista.release();
        semaforoClienteActual.acquire();
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
    
    //  Metodos para el hilo RecepcionistaCheckIn
    
    public synchronized void esperarCliente() throws InterruptedException{
        semaforoClienteActual = colaEspera.take();
    }
    
    public void avisarCliente() throws InterruptedException{
        semaforoClienteActual.release();
        semaforoRecepcionista.acquire();
    }
    
    public Vuelo recuperarTerminalPuesto(){
        return mapaVuelos.get(pasajeActual.getNumVuelo());
    }
    
    public void darInformacionCliente(Terminal terminal, int puestoEmbarque){
        terminalPasajeActual = terminal;
        puertoEmbarqueActual = puestoEmbarque;
        semaforoClienteActual.release();
    }
    
    public void esperarclienteInformacion() throws InterruptedException{
        semaforoRecepcionista.acquire();
    }
}
