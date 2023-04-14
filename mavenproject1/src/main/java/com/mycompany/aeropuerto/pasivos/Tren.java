package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.activos.Maquinista;
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
    
    //  Metodos para el Pasajero
    
    public synchronized void subirse(String terminal) throws InterruptedException{
        //  Checkea que haya espacio en el tren
        while(contador.getCount() == 0 || !estaEnBase){
            //barrera.isBroken()
            //  Si no hay, espera a que vuelva el tren y checkea de nuevo
            this.wait();
        }
        //  Avisa que se va a bajar en la parada correspondiente
        conteoParadas.get(terminal).release();
        //  Se sube al tren
        contador.countDown();
    }
    
    public void bajarse(String terminal) throws InterruptedException{
        //  Espera que se llegue a la parada
        conteoParadas.get(terminal).wait();
        //  Se baja
        conteoParadas.get(terminal).acquire();
    }
    
    //  Metodos para el Maquinista
    
    public synchronized boolean esperarCapacidad() throws InterruptedException{
        //  Espera que se vacie el contador (se llene el tren) o pase el tiempo designado (media hora)
        boolean trenLleno = contador.await(ManejadorTiempo.duracionHora() / 2, TimeUnit.MILLISECONDS);
        //  Avisa que partio el tren
        this.estaEnBase = false;
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
        //  Avisa que se llego a la parada
        conteoParadas.get(terminal).notifyAll();
        //  Espera que se bajen todos los pasajeros de la parada
        while(conteoParadas.get(terminal).availablePermits() != 0){
            Thread.sleep(1000);
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
