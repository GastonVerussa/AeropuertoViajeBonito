package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.Vuelo;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import com.mycompany.aeropuerto.Pasaje;

public class PuestoAtencion {

    private final String aerolinea;
    private final HashMap<Integer, Vuelo> mapaVuelos;
    private final ArrayBlockingQueue<Semaphore> colaEspera;
    private final Semaphore semaforoRecepcionista;
    private final Semaphore semaforoGuardia;
    private Semaphore semaforoClienteActual;
    private Pasaje pasajeActual;
    private Terminal terminalPasajeActual;
    private int puertoEmbarqueActual;
    
    public PuestoAtencion(String aerolinea, int capacidad){
        this.aerolinea = aerolinea;
        mapaVuelos = new HashMap<>();
        colaEspera = new ArrayBlockingQueue(capacidad);
        semaforoRecepcionista = new Semaphore(0);
        semaforoGuardia = new Semaphore(0);
    }
    
    public String getAerolinea(){
        return aerolinea;
    }
    
    //  Metodo para agregar los vuelos de la aerolinea
    public synchronized void agregarVuelo(Vuelo vuelo){
        mapaVuelos.put(vuelo.getNumVuelo(), vuelo);
    }
    
    public synchronized boolean eliminarVuelo(int numeroVuelo){
        return (mapaVuelos.remove(numeroVuelo) != null);
    }
    
    public synchronized void vaciarVuelos(){
        mapaVuelos.clear();
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
        semaforoGuardia.release();
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
    
    //  Metodo para el guardia

    public void esperarDesocupe() throws InterruptedException{
        semaforoGuardia.acquire();
    }
}
