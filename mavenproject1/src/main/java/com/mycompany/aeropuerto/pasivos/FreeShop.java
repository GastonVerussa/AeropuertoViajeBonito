package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class FreeShop {

    private final Semaphore capacidad;
    private final ReentrantLock[] mutexCajas;
    private final Semaphore[] semaforosCajeros;
    private final Semaphore[] semaforosClientes;
    
    public FreeShop(int capacidad, int cantidadCajeros){
        this.capacidad = new Semaphore(capacidad);
        this.semaforosCajeros = new Semaphore[cantidadCajeros];
        for(int i = 0; i < semaforosCajeros.length; i++){
            semaforosCajeros[i] = new Semaphore(0);
        }
        this.semaforosClientes = new Semaphore[cantidadCajeros];
        for(int i = 0; i < semaforosClientes.length; i++){
            semaforosClientes[i] = new Semaphore(1);
        }
        this.mutexCajas = new ReentrantLock[cantidadCajeros];
        for(int i = 0; i < mutexCajas.length; i++){
            mutexCajas[i] = new ReentrantLock();
        }
    }
    
    //  Metodos para pasajero
    
    //  Entra free shop
    public boolean entrar(){
        return capacidad.tryAcquire();
    }
    
    //  Va a una de las cajas
    public synchronized int irCaja() {
        //  Espera que haya al menos una caja libre
        int cajaLibre = -1;
        while(cajaLibre == -1){
            for(int i = 0; i <= mutexCajas.length; i++){
                if(mutexCajas[i].tryLock()){
                    cajaLibre = i;
                    break;
                }
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
        mutexCajas[cajaLibre].unlock();
    }
    
    //  Sale del free shop
    public void salir(){
        capacidad.release();
    }
    
    //  Metodos para Cajero
    
    public boolean atenderCliente(int numCajero) throws InterruptedException {
        return semaforosCajeros[numCajero].tryAcquire(ManejadorTiempo.duracionMinuto() * 10, TimeUnit.MILLISECONDS);
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
