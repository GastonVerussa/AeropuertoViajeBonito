package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.Vuelo;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import com.mycompany.aeropuerto.Pasaje;
import com.mycompany.aeropuerto.activos.Guardia;
import com.mycompany.aeropuerto.activos.Pasajero;
import com.mycompany.aeropuerto.activos.RecepcionistaAtencion;
import java.util.concurrent.TimeUnit;

public class PuestoAtencion {

    private final String aerolinea;
    private final HashMap<Integer, Vuelo> mapaVuelos;
    private final ArrayBlockingQueue<Pasajero> colaEspera;
    private final Semaphore semaforoRecepcionista;
    private final Semaphore semaforoGuardia;
    private Pasajero clienteActual;
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
            if(exito) pasajero.wait();
        }
        return exito;
    }
    
    public void mostrarPasaje(Pasaje pasaje) throws InterruptedException{
        synchronized (clienteActual) {
            this.pasajeActual = pasaje;
            semaforoRecepcionista.release();
            clienteActual.wait();   
        }
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
    
    public boolean esperarCliente() throws InterruptedException{
        clienteActual = colaEspera.poll(ManejadorTiempo.duracionMinuto() * 5, TimeUnit.MILLISECONDS);
        boolean exito = clienteActual != null;
        if(exito){
            semaforoGuardia.release();
        }
        return exito;
    }
    
    public void avisarCliente() throws InterruptedException{
        synchronized (clienteActual) {
            clienteActual.notify();
        }
        semaforoRecepcionista.acquire();
    }
    
    public Vuelo recuperarTerminalPuesto(){
        return mapaVuelos.get(pasajeActual.getNumVuelo());
    }
    
    public synchronized void darInformacionCliente(Terminal terminal, int puestoEmbarque){
        terminalPasajeActual = terminal;
        puertoEmbarqueActual = puestoEmbarque;
        synchronized (clienteActual) {
            clienteActual.notify();   
        }
    }
    
    public void esperarclienteInformacion() throws InterruptedException{
        semaforoRecepcionista.acquire();
    }
    
    //  Metodo para el guardia

    public boolean esperarDesocupe() throws InterruptedException{
        return semaforoGuardia.tryAcquire(ManejadorTiempo.duracionMinuto() * 10, TimeUnit.MILLISECONDS);
    }
}
