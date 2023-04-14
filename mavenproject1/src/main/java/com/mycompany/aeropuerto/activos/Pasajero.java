package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.GeneradorPasajes;
import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.Pasaje;
import com.mycompany.aeropuerto.pasivos.*;
import java.util.Random;

public class Pasajero extends Thread{
    
    private Pasaje pasaje;
    private final String nombre;
    private Terminal terminal;
    private int puertoEmbarque;
    private PuestoAtencion puestoAtencion;
    private static PuestoInformes puestoInformes;
    private static Tren tren;
    private static HallCentral hall;
    private static Random random;
    private static GeneradorPasajes generadorPasajes;
    
    public Pasajero(String nombre){
        super(nombre);
        this.nombre = nombre;
    }   
    
    public static void setDatos(PuestoInformes puesto, Tren tren, HallCentral hall, GeneradorPasajes generadorPasajes){
        Pasajero.puestoInformes = puesto;
        Pasajero.tren = tren;
        Pasajero.hall = hall;
        Pasajero.random = new Random(System.currentTimeMillis());
        Pasajero.generadorPasajes = generadorPasajes;
    }
    
    @Override
    public void run(){
        imprimir("Llegue al aeropuerto");
        try {
            pasaje = generadorPasajes.crearPasajeRandom();
            imprimir("Traje mi pasaje");
            puestoAtencion = puestoInformes.recuperarPuesto(pasaje.getAerolinea());
            imprimir("Muchas gracias, ire al puesto de atencion de " + pasaje.getAerolinea());
            try {
                while(!puestoAtencion.entrarCola(this)){
                    //  Va al hall central a esperar
                    imprimir("La cola esta llena, a esperar al hall");
                    hall.esperarHall(puestoAtencion);
                    imprimir("Oh gracias guardia, a ver si puedo entrar.");
                }
                        //imprimir("Logre entrar a la cola, ahora a esperar mi turno.");
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
                            try{
                                imprimir("Bueno, voy a comprar");
                                int numCaja = terminal.irCajaFreeShop();
                                imprimir("Buen dia deseo comprar algo");
                                terminal.comprarFreeShop(numCaja);
                                imprimir("Este producto quiero comprar");
                                terminal.pagarFreeShop(numCaja);
                                imprimir("Muy bien, tome el pago, adios!");
                            } catch(InterruptedException e){
                                imprimir("Error comprando");
                            }
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
                do{
                //  Espera que llegue su avion
                terminal.esperarAvion(puertoEmbarque);
                if(pasaje.getNumVuelo() == terminal.recuperarNumVueloPuerto(puertoEmbarque)){
                    //  Si es su vuelo
                    imprimir("Llegó mi avion, Adios!");
                    break;
                } else {
                    if(ManejadorTiempo.milisRestantesParaHorario(pasaje.getHorario()) < ( - ManejadorTiempo.duracionHora())){
                        //  Si no es su vuelo y pasó más de 1 hora de su vuelo
                        imprimir("Ya esperé mucho, pasó más de una hora de mi vuelo, me voy");
                        break;
                    }
                }
                //  Si no era su vuelo, pero todavia no pasó el horario del suyo, sigue esperando
                }while(true);
            } catch (InterruptedException ex) {
                imprimir("Tuve un problema esperando el avion");
            }
        } catch (Exception ex) {
            imprimir("No pude conseguir pasaje, me voy del aeropuerto");
        }
    }
    
    public boolean comprobarTiempo(){
        //  Se fija cuanto tiempo le queda para su vuelo
        long segundosRestantes = ManejadorTiempo.milisRestantesParaHorario(pasaje.getHorario());
        //  Si tiene al menos una hora de tiempo, le parece suficiente
        return segundosRestantes >= ManejadorTiempo.duracionHora();
    }
    
    public boolean decidirRandom(){
        //  Devuelve un boolean al azar. 
        return Math.random() < 0.5;
    }
    
    private void imprimir(String cadena){
        System.out.println("Pasajero " + nombre + ": " + cadena);
    }
}
