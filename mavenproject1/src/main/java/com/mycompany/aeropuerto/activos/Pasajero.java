package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.Pasaje;
import com.mycompany.aeropuerto.pasivos.*;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pasajero extends Thread{
    
    private final Pasaje pasaje;
    private final String nombre;
    private Terminal terminal;
    private int puertoEmbarque;
    private PuestoAtencion puestoAtencion;
    private static PuestoInformes puestoInformes;
    private static Tren tren;
    private static HallCentral hall;
    private static Random random;
    private final Semaphore semaforoPersonal;
    
    public Pasajero(Pasaje pasaje, String nombre){
        super(nombre);
        this.pasaje = pasaje;
        this.nombre = nombre;
        Pasajero.random = new Random();
        semaforoPersonal = new Semaphore(0);
    }
    
    public static void setDatos(PuestoInformes puesto, Tren tren, HallCentral hall){
        Pasajero.puestoInformes = puesto;
        Pasajero.tren = tren;
        Pasajero.hall = hall;
    }
    
    @Override
    public void run(){
        imprimir("Llegue al aeropuerto");
        puestoAtencion = puestoInformes.recuperarPuesto(pasaje.getAerolinea());
        imprimir("Muchas gracias, ire al puesto de atencion de " + pasaje.getAerolinea());
        try {
            while(!puestoAtencion.entrarCola(semaforoPersonal)){
                //  Va al hall central a esperar
            }
            imprimir("Logre entrar a la cola, ahora a esperar mi turno.");
            puestoAtencion.esperarAtencion(semaforoPersonal);
            //  Realiza comunicacion con recepcionista
            imprimir("Buen dia, si, este es mi pasaje.");
            puestoAtencion.mostrarPasaje(pasaje);
            terminal = puestoAtencion.recuperarTerminal();
            puertoEmbarque = puestoAtencion.recuperarPuertoEmbarque();
            imprimir("Muchas gracias, voy para alli");
            puestoAtencion.despedirse();
        } catch (InterruptedException ex) {
            imprimir("Hubo un error en el puesto de atencion");
        }
        //  Va al tren
        try {
            //  Espera a subirse
            tren.subirse(terminal.getNombre());
            imprimir("Logre subirme al tren... A la terminal " + terminal + " por favor!");
            //  Espera a su parada para bajarse
            tren.bajarse(terminal.getNombre());
            imprimir("Muchas gracias, me bajo aca");
        } catch (InterruptedException ex) {
            imprimir("Error al subirse o bajarse del tren, fui interrumpido");
        }
        //  Llegó a la temrinal.
        //  Se fija si tiene tiempo para el free-shop
        if(this.comprobarTiempo()){
            //  Si tiene tiempo, se fija si tiene ganas de ir
            if(this.decidirRandom()){
                //  Si tiene tiempo y decide ir, se fija si hay espacio
                if(terminal.entrarFreeShop()){
                    //  Hay espacio, entro
                    imprimir("Entre al free-shop, vamos a ver que hay...");
                    try{
                        Thread.sleep(random.nextInt(1000, 2000));
                    } catch (InterruptedException e){
                        imprimir("Interrupted Exception");
                    }
                    //  Decide si quiere comprar algo
                    if(this.decidirRandom()){
                        terminal.comprarFreeShop();
                        imprimir("Compre algo");
                    } else {
                        imprimir("Mejor no compro nada");
                    }
                    terminal.salirFreeShop();
                    imprimir("Sali del free-shop");
                } else {
                    imprimir("No hay espacio en el free-shop, una lastima");
                }
            } else {
                imprimir("Hmm no tengo ganas de ir al free-shop");
            }
        } else {
            imprimir("No tengo mucho tiempo, no voy al free-shop esta vez");
        }
        imprimir("Ahora a esperar el avion");
        try {
            //  Espera que llegue su avion
            terminal.esperarAvion(puertoEmbarque);
        } catch (InterruptedException ex) {
            imprimir("Tuve un problema esperando el avion");
        }
        imprimir("Llegó mi avion, Adios!");
    }
    
    public boolean comprobarTiempo(){
        return true;
    }
    
    public boolean decidirRandom(){
        //  Devuelve un boolean al azar. 
        return Math.random() < 0.5;
    }
    
    public void darInformacion(Terminal terminal, int puertoEmbarque){
        this.terminal = terminal;
        this.puertoEmbarque = puertoEmbarque;
    }
    
    private void imprimir(String cadena){
        System.out.println("Pasajero " + nombre + ": " + cadena);
    }
}
