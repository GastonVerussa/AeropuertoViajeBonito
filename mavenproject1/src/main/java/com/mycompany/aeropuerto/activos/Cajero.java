package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivos.FreeShop;

public class Cajero extends Thread{
    
    private final String nombre;
    private final FreeShop freeShop;
    private final int numCaja;

    public Cajero(String nombre, FreeShop freeShop, int numCaja) {
        super(nombre);
        this.nombre = nombre;
        this.freeShop = freeShop;
        this.numCaja = numCaja;
    }
    
    @Override
    public void run(){
        while(true){
            try{
                ManejadorTiempo.esperarApertura();
            } catch(InterruptedException e){
                imprimir("Tuve un problema esperando que se abra el aeropuerto");
            }
            imprimir("Llegue al aeropuerto, otro dia de trabajo");
            while(ManejadorTiempo.estaAbierto()){
                try {
                    imprimir("Esperando un cliente");
                    if(freeShop.atenderCliente(numCaja)){
                        imprimir("Buenas, que desea comprar?");
                        freeShop.esperarRespuestaCliente(numCaja);
                        imprimir("Muy bien, dejeme ver el precio");
                        Thread.sleep((int) (Math.random() * 1000 + 500));
                        imprimir("Serían " + ((int) (Math.random() * 4000 + 500)) + " pesos.");
                        freeShop.cobrarCliente(numCaja);
                        imprimir("Muchas gracias, disfrute su vuelo");
                    }
                    imprimir("A ver que hora es");
                } catch (InterruptedException ex) {
                    imprimir("Tuve un problema");
                }
            }
            imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
        }
    }
    
    public void imprimir(String cadena){
        System.out.println("Cajero " + nombre + ": " + cadena);
    }
}
