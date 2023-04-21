package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class Tren {
    
    private final int capacidad;
    //  CountDownLatch utilizado para que el tren espere que todos se suban
    private CountDownLatch contador;
    //  Un mapa que contiene los semaforos usados para cada parada
    private final HashMap<String, Semaphore> conteoParadas;
    //  Boolean para saber si el tren esta en base
    private boolean estaEnBase;
    
    //  Constructor de parada
    public Tren(int capacidad, String[] terminales){
        this.capacidad = capacidad;
        contador = new CountDownLatch(capacidad);
        conteoParadas = new HashMap(terminales.length);
        for (String terminal : terminales) {
            conteoParadas.put(terminal, new Semaphore(0));
        }
        this.estaEnBase = true;
    }
    
    public void limpiar() throws InterruptedException{
        contador = new CountDownLatch(capacidad);
        this.estaEnBase = true;
        for(Semaphore semaforoTerminal : conteoParadas.values()){
            semaforoTerminal.acquire(semaforoTerminal.availablePermits());
        }
    }
    
    //  Metodos para el Pasajero
    
    public synchronized boolean intentarSubirse(String terminal){
        boolean exito;
                                //System.out.println(Thread.currentThread().getName() + ": Voy a intentar subirme.");
        //  Checkea que haya espacio en el tren
        if(contador.getCount() == 0 || !estaEnBase){
            if(!estaEnBase){
                                //System.out.println(Thread.currentThread().getName() + ": Esperando el tren. No esta en base.");
            } else {
                                //System.out.println(Thread.currentThread().getName() + ": Esperando el tren. Esta lleno.");
            }
            exito = false;
            //barrera.isBroken()
            //  Si no hay, espera a que vuelva el tren y checkea de nuevo
        } else {
            exito = true;
                                //System.out.println(Thread.currentThread().getName() + ": Parece que esta todo bien, libero semaforo.");
            //  Avisa que se va a bajar en la parada correspondiente
            conteoParadas.get(terminal).release();
            //  Se sube al tren
                                //System.out.println(Thread.currentThread().getName() + ": Bajo contador.");
            contador.countDown();
        }
        return exito;
    }
    
    public synchronized void esperarTren() throws InterruptedException{
        this.wait();
    }
    
    public void bajarse(String terminal) throws InterruptedException{
        Semaphore semaforoTerminal = conteoParadas.get(terminal);
        synchronized (semaforoTerminal) {
            //  Espera que se llegue a la parada
            semaforoTerminal.wait();
        }
        //  Se baja
        conteoParadas.get(terminal).acquire();
    }
    
    //  Metodos para el Maquinista
    
    public boolean esperarCapacidad() throws InterruptedException{
        //  Espera que se vacie el contador (se llene el tren) o pase el tiempo designado (media hora)
        boolean trenLleno = contador.await(ManejadorTiempo.duracionHora() / 2, TimeUnit.MILLISECONDS);
        //  Si no esta vacio, avisa que partio el tren
        if(!this.estaVacio())this.estaEnBase = false;
        //  Devuelve el boolean para decirle al maquinista si el tren esta lleno o pasó el tiempo
        return trenLleno;
    }
    
    //  Variable para saber si esta vacio
    public boolean estaVacio(){
        return contador.getCount() == capacidad;
    }
    
    //  Se fija si alguien pidio bajarse en esa terminal
    public boolean checkearParada(String terminal){
        return conteoParadas.get(terminal).availablePermits() != 0;
    }
    
    public void arribarParada(String terminal) throws InterruptedException{
        Semaphore semaforoTerminal = conteoParadas.get(terminal);
        synchronized (semaforoTerminal) {
            //  Avisa que se llego a la parada
            semaforoTerminal.notifyAll();
        }
        //  Espera que se bajen todos los pasajeros de la parada
        while(semaforoTerminal.availablePermits() != 0){
            Thread.sleep(ManejadorTiempo.duracionMinuto());
        }
    }
    
    public synchronized void volverEstacionBase() throws InterruptedException{
        //  Recrea el contador para permitir que se suban de nuvo
        contador = new CountDownLatch(capacidad);
        this.estaEnBase = true;
        //  Les avisa que llego a la estacion a los que esten esperando
        this.notifyAll();
    }
}
