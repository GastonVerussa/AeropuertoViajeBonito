package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.pasivosSinSincronizacion.ManejadorTiempo;
import java.util.concurrent.Semaphore;

public class FreeShop {

    private final int capacidadTotal;
    private final Semaphore capacidad;
    private final Semaphore[] mutexCajas;
    private final Semaphore[] semaforosCajeros;
    private final Semaphore[] semaforosClientes;
    
    public FreeShop(int capacidad, int cantidadCajeros){
        capacidadTotal = capacidad;
        this.capacidad = new Semaphore(capacidad);
        this.semaforosCajeros = new Semaphore[cantidadCajeros];
        for(int i = 0; i < semaforosCajeros.length; i++){
            semaforosCajeros[i] = new Semaphore(0);
        }
        this.semaforosClientes = new Semaphore[cantidadCajeros];
        for(int i = 0; i < semaforosClientes.length; i++){
            semaforosClientes[i] = new Semaphore(1);
        }
        this.mutexCajas = new Semaphore[cantidadCajeros];
        for(int i = 0; i < mutexCajas.length; i++){
            mutexCajas[i] = new Semaphore(1);
        }
    }
    
    //  Reinicia los datos del free-shop para el siguiente dia
    public void limpiar() throws InterruptedException{
        while(capacidad.tryAcquire()){
            //  Primero lleva a 0 los permisos
        }
        //  Y los vuelve a la total
        capacidad.release(capacidadTotal);
        //  Vuelve a 0
        for(Semaphore semaforo : semaforosCajeros){
            semaforo.tryAcquire();
        }        
        //  Los setea en 1, primero volviendo a 0 de ser necesario
        for(Semaphore semaforo : semaforosClientes){
            semaforo.tryAcquire();
            semaforo.release();
        }
        //  Los setea en 1, primero volviendo a 0 de ser necesario
        for(Semaphore semaforo : mutexCajas){
            semaforo.tryAcquire();
            semaforo.release();
        }
    }
    
    //  Metodos para pasajero
    
    //  Entra free shop
    public boolean entrar(){
        return capacidad.tryAcquire();
    }
    
    //  Va a una de las cajas
    public synchronized int irCaja() throws InterruptedException{
        //  Espera que haya al menos una caja libre
        int cajaLibre = -1;
        while(cajaLibre == -1){
            //  Revisa las cajas hasta revisar todas, o encontrar una libre
            for(int i = 0; i < mutexCajas.length && cajaLibre == -1; i++){
                if(mutexCajas[i].tryAcquire()){
                    cajaLibre = i;
                }
            }
            //  Si todas las cajas estan ocupadas actualmente
            if(cajaLibre == -1){
                //  Espera 5 min antes de revisar de nuevo
                Thread.sleep(ManejadorTiempo.duracionMinuto() * 5);
            }
        }
        semaforosCajeros[cajaLibre].release();
        return cajaLibre;
    }
    
    //  Compra
    public void comprar(int cajaLibre) throws InterruptedException{
        semaforosClientes[cajaLibre].acquire();
        semaforosCajeros[cajaLibre].release();
    }
    
    //  Paga y se retira
    public void pagar(int cajaLibre) throws InterruptedException{
        semaforosClientes[cajaLibre].acquire();
        semaforosCajeros[cajaLibre].release();
        mutexCajas[cajaLibre].release();
    }
    
    //  Sale del free shop
    public void salir(){
        capacidad.release();
    }
    
    //  Metodos para Cajero
    
    public void atenderCliente(int numCajero) throws InterruptedException {
        semaforosCajeros[numCajero].acquire();
    }
    
    public void esperarRespuestaCliente(int numCajero) throws InterruptedException {
        semaforosClientes[numCajero].release();
        semaforosCajeros[numCajero].acquire();
    }
    
    public void cobrarCliente(int numCajero) throws InterruptedException {
        semaforosClientes[numCajero].release();
        semaforosCajeros[numCajero].acquire();
    }
}
