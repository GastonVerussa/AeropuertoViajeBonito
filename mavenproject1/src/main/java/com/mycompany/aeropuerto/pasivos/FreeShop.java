package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.activos.Cajero;
import java.util.concurrent.Semaphore;

public class FreeShop {

    private Cajero cajero1;
    private Cajero cajero2;
    private Semaphore capacidad;
    
    public FreeShop(int capacidad){
        cajero1 = new Cajero();
        cajero2 = new Cajero();
        this.capacidad = new Semaphore(capacidad);
    }
    
    //  Metodos para pasajero
    
    //  Entra free shop
    public boolean entrar(){
        return capacidad.tryAcquire();
    }
    
    //  Va a una de las cajas
    public void irCaja(){
        
    }
    
    //  Compra
    public void comprar(){
        
    }
    
    //  Sale del free shop
    public void salir(){
        capacidad.release();
    }
    
    //  Metodos para Cajero
    
    public void atenderCliente(){
        
    }
    
    public void cobrarCliente(){
        
    }
}
