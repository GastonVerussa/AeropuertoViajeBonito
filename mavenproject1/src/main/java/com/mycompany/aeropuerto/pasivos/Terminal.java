package com.mycompany.aeropuerto.pasivos;

public class Terminal {
    
    private final String nombre;
    private final FreeShop freeShop;
    private final Object[] monitoresEmbarque;
    private final int numPuertoInicial;
    
    public Terminal(String nombre, int puertoInicial, int puertoFinal, int capacidadFreeShop){
        this.nombre = nombre;
        numPuertoInicial = puertoInicial;
        monitoresEmbarque = new Object[puertoFinal - puertoInicial + 1];
        freeShop = new FreeShop(capacidadFreeShop);
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void esperarAvion(int puertoEmbarque) throws InterruptedException{
        synchronized (monitoresEmbarque[puertoEmbarque - numPuertoInicial]) {
            monitoresEmbarque[puertoEmbarque - numPuertoInicial].wait();
        }
    }
    
    public void llamarPasajeros(int numPuerto){
        monitoresEmbarque[numPuerto - numPuertoInicial].notifyAll();
    }
    
    public boolean entrarFreeShop(){
        return freeShop.entrar();
    }
    
    public void comprarFreeShop(){
        freeShop.comprar();
    }
    
    public void salirFreeShop(){
        freeShop.salir();
    }
}
