package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivos.FreeShop;
import java.util.Random;

public class Cajero extends Thread{
    
    private final String nombre;
    private final FreeShop freeShop;
    private final int numCaja;
    private final int DURACION_MINIMA = ManejadorTiempo.duracionMinuto() * 1;
    private final int DURACION_MAXIMA = ManejadorTiempo.duracionMinuto() * 2;
    private final Random random = new Random(System.currentTimeMillis());

    public Cajero(String nombre, FreeShop freeShop, int numCaja) {
        super(ManejadorTiempo.getThreadGroup(), "Cajero " + nombre);
        this.nombre = "Cajero " + nombre;
        this.freeShop = freeShop;
        this.numCaja = numCaja;
    }
    
    @Override
    public void run(){
        while(true){
            while(true){
                try{
                    ManejadorTiempo.esperarApertura();
                    break;
                } catch(InterruptedException e){
                    System.out.println(" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
                    System.out.println(" ----------------------------------------------------------- ");
                    imprimir("Tuve un problema esperando que se abra el aeropuerto");
                    System.out.println(" ----------------------------------------------------------- ");
                    System.out.println(" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
                }
            } 
            imprimir("Llegue al aeropuerto, otro dia de trabajo");
            try{
                while(true){
                    imprimir("Esperando un cliente");
                    freeShop.atenderCliente(numCaja);
                    imprimir("Buenas, que desea comprar?");
                    freeShop.esperarRespuestaCliente(numCaja);
                    imprimir("Muy bien, dejeme ver el precio");
                    //  Simula tardanza, entre 1 a 2 minutos
                    Thread.sleep(random.nextInt(DURACION_MINIMA, DURACION_MAXIMA + 1));
                    imprimir("Serían " + random.nextInt(500, 4501) + " pesos.");
                    freeShop.cobrarCliente(numCaja);
                    imprimir("Muchas gracias, disfrute su vuelo");
                }
                
                //  While si debe checkear cada tanto el tiempo
                /*
                while(true){
                    imprimir("Esperando un cliente");
                    if(freeShop.atenderCliente(numCaja)){
                        imprimir("Buenas, que desea comprar?");
                        freeShop.esperarRespuestaCliente(numCaja);
                        imprimir("Muy bien, dejeme ver el precio");
                        //  Simula tardanza, entre 1 a 2 minutos
                        Thread.sleep((int) (Math.random() * ManejadorTiempo.duracionMinuto() + ManejadorTiempo.duracionMinuto()));
                        imprimir("Serían " + ((int) (Math.random() * 4000 + 500)) + " pesos.");
                        freeShop.cobrarCliente(numCaja);
                        imprimir("Muchas gracias, disfrute su vuelo");
                    }
                    imprimir("A ver que hora es");
                }
                */
            } catch (InterruptedException e){
                imprimir("Bueno, hora de cerrar, a descansar a mi casa, vuelvo mañana.");
                try {
                    freeShop.limpiar();
                } catch (InterruptedException ex) {
                    System.out.println(" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
                    System.out.println(" ----------------------------------------------------------- ");
                    imprimir("Me interrumpieron limpiando");
                    System.out.println(" ----------------------------------------------------------- ");
                    System.out.println(" +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
                }
            }
        }
    }
    
    public void imprimir(String cadena){
        int identacion = 4;
        String cadenaFinal = "";
        for(int i = 1; i <= identacion; i++){
            cadenaFinal += "---";
        }
        cadenaFinal += nombre + ": " + cadena;
        System.out.println(cadenaFinal);
    }
}
