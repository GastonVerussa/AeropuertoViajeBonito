package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.pasivosSinSincronizacion.ManejadorTiempo;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Tren {
    
    private class Contador{
     
        private int conteo;
        
        public Contador(){
            conteo = 0;
        }
        
        public synchronized void sumarUno(){
            conteo++;
        }
        
        public synchronized int get(){
            return conteo;
        }
        
        public synchronized void reiniciar(){
            conteo = 0;
        }
    }
    
    private final int capacidad;
    //  CountDownLatch utilizado para que el tren espere que todos se suban
    private CountDownLatch contador;
    //  Un mapa que contiene un contador protegido para cada parada
    private final HashMap<String, Contador> contadoresParada;
    //  Un mapa que contiene los semaforos usados para cada parada
    private final HashMap<String, Semaphore> semaforosParada;
    //  Boolean para saber si el tren esta en base
    private boolean estaEnBase;
    
    private final int LIMITE_TIEMPO_ESPERA = ManejadorTiempo.duracionHora() / 2;
    
    //  Constructor de parada
    public Tren(int capacidad, String[] terminales){
        this.capacidad = capacidad;
        contador = new CountDownLatch(capacidad);
        semaforosParada = new HashMap(terminales.length);
        for (String terminal : terminales) {
            semaforosParada.put(terminal, new Semaphore(0));
        }
        contadoresParada = new HashMap(terminales.length);
        for (String terminal : terminales) {
            contadoresParada.put(terminal, new Contador());
        }
        this.estaEnBase = true;
    }
    
    public void limpiar() throws InterruptedException{
        contador = new CountDownLatch(capacidad);
        this.estaEnBase = true;
        for(Semaphore semaforoTerminal : semaforosParada.values()){
            while(semaforoTerminal.tryAcquire()){
                // Vuelve a 0 los semaforos
            }
        }
        //  Reinicia todos los contadores
        for(Contador conteo : contadoresParada.values()){
            conteo.reiniciar();
        }
    }
    
    //  Metodos para el Pasajero
    
    public synchronized boolean intentarSubirse(String terminal){
        boolean exito;
        //  Checkea que haya espacio en el tren y este en la base
        if(contador.getCount() == 0 || !estaEnBase){
            exito = false;
            //  Si no hay o no esta, espera a que vuelva el tren y checkea de nuevo
        } else {
            exito = true;
            //  Avisa que se va a bajar en la parada correspondiente
            contadoresParada.get(terminal).sumarUno();
            //  Se sube al tren
            contador.countDown();
        }
        return exito;
    }
    
    public synchronized void esperarTren() throws InterruptedException{
        this.wait();
    }
    
    public void esperarBajarse(String terminal) throws InterruptedException{
        //  Espera para bajarse
        semaforosParada.get(terminal).acquire();
    }
    
    //  Metodos para el Maquinista
    
    public synchronized boolean esperarCapacidad() throws InterruptedException{
        //  Espera que se vacie el contador (se llene el tren) o pase el tiempo designado (media hora)
        boolean trenLleno = contador.await(LIMITE_TIEMPO_ESPERA, TimeUnit.MILLISECONDS);
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
        return contadoresParada.get(terminal).get() != 0;
    }
    
    public void arribarParada(String terminal) throws InterruptedException{
        //  Consigue el semaforo
        Semaphore semaforoTerminal = semaforosParada.get(terminal);
        //  Libera una cantidad de permisos igual a los que pidieron bajarse
        semaforoTerminal.release(contadoresParada.get(terminal).get());
        //  Reinicia el contador
        contadoresParada.get(terminal).reiniciar();
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
