package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.pasivos.FreeShop;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cajero extends Thread{
    
    private final String nombre;
    private final FreeShop freeShop;

    public Cajero(String nombre, FreeShop freeShop) {
        super(nombre);
        this.nombre = nombre;
        this.freeShop = freeShop;
    }
    
    @Override
    public void run(){
        imprimir("Empieza el trabajo");
        while(true){
            try {
                imprimir("Esperando un cliente");
                freeShop.atenderCliente();
                imprimir("Buenas, que desea comprar?");
                freeShop.esperarRespuestaCliente();
                imprimir("Muy bien, dejeme ver el precio");
                Thread.sleep((int) (Math.random() * 1000 + 500));
                imprimir("Serían " + ((int) (Math.random() * 4000 + 500)) + " pesos.");
                freeShop.cobrarCliente();
                imprimir("Muchas gracias, disfrute su vuelo");
            } catch (InterruptedException ex) {
                imprimir("Tuve un problema.");
            }
        }
    }
    
    public void imprimir(String cadena){
        System.out.println("Cajero " + nombre + ": " + cadena);
    }
}
