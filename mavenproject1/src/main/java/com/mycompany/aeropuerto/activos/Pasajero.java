package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.pasivosSinSincronizacion.PuestoInformes;
import com.mycompany.aeropuerto.pasivosSinSincronizacion.GeneradorPasajes;
import com.mycompany.aeropuerto.pasivosSinSincronizacion.ManejadorTiempo;
import com.mycompany.aeropuerto.pasivosSinSincronizacion.Pasaje;
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
    private static Random random;
    private static GeneradorPasajes generadorPasajes;
    private final int DURACION_PASEO_FREESHOP_MINIMA = ManejadorTiempo.duracionMinuto() * 5;
    private final int DURACION_PASEO_FREESHOP_MAXIMA = ManejadorTiempo.duracionMinuto() * 15;
    
    public Pasajero(String nombre){
        super(PuertasAeropuerto.getThreadGroup(), "Pasajero " + nombre);
        this.nombre = "Pasajero " + nombre;
    }   
    
    public static void setDatos(PuestoInformes puesto, Tren tren, GeneradorPasajes generadorPasajes){
        Pasajero.puestoInformes = puesto;
        Pasajero.tren = tren;
        Pasajero.random = new Random(System.currentTimeMillis());
        Pasajero.generadorPasajes = generadorPasajes;
    }
    
    @Override
    public void run(){
        imprimir("Llegue al aeropuerto", 0);
                            //imprimir("Bandera 1");
        try{
            pasaje = generadorPasajes.crearPasajeRandom();
        } catch (Exception ex) {
            imprimir("No pude conseguir pasaje, me voy del aeropuerto. Exception: " + ex.getMessage(), 0);
        }
        //  Si consiguió pasaje, entonces avanza
        if(pasaje != null){
            try{
                            //imprimir("Bandera 2");
                imprimir("Traje mi pasaje, tengo el vuelo " + pasaje.getNumVuelo(), 0);
                puestoAtencion = puestoInformes.recuperarPuesto(pasaje.getAerolinea());
                imprimir("Muchas gracias, ire al puesto de atencion de " + pasaje.getAerolinea(), 1);

                            //imprimir("Bandera 3");
                while(!puestoAtencion.entrarCola(this)){
                    //  Va al hall central a esperar
                    imprimir("La cola esta llena, a esperar al hall", 2);
                    HallCentral.esperarHall(puestoAtencion);
                    imprimir("Oh bueno, a ver si puedo entrar.", 2);
                }
                        //imprimir("Logre entrar a la cola, ahora a esperar mi turno.");
                //  Realiza comunicacion con recepcionista
                imprimir("Buen dia, si, este es mi pasaje.", 2);
                puestoAtencion.mostrarPasaje(pasaje);
                terminal = puestoAtencion.recuperarTerminal();
                puertoEmbarque = puestoAtencion.recuperarPuertoEmbarque();
                imprimir("Muchas gracias, voy para alli", 2);
                puestoAtencion.despedirse();

                            //imprimir("Bandera 4");
                //  Va al tren
                            //imprimir("Yendo al tren");
                //  Espera a subirse
                while(!tren.intentarSubirse(terminal.getNombre())){
                    imprimir("No pude subirme. A esperar", 3);
                    tren.esperarTren();
                }
                imprimir("Logre subirme al tren... A la terminal " + terminal.getNombre() + " por favor!", 3);
                //  Espera a su parada para bajarse
                tren.esperarBajarse(terminal.getNombre());
                imprimir("Muchas gracias, me bajo aca", 3);

                            //imprimir("Bandera 5");
                //  Llegó a la temrinal.
                //  Se fija si tiene tiempo para el free-shop
                if(this.comprobarTiempo()){
                    //  Si tiene tiempo, se fija si tiene ganas de ir
                    if(this.decidirRandom()){
                        //  Si tiene tiempo y decide ir, se fija si hay espacio
                        if(terminal.entrarFreeShop()){
                            //  Hay espacio, entro
                            imprimir("Entre al free-shop, vamos a ver que hay...", 4);
                            Thread.sleep(random.nextInt(DURACION_PASEO_FREESHOP_MINIMA, DURACION_PASEO_FREESHOP_MAXIMA + 1));
                            //  Decide si quiere comprar algo
                            if(this.decidirRandom()){
                                imprimir("Bueno, voy a comprar", 4);
                                int numCaja = terminal.irCajaFreeShop();
                                imprimir("Buen dia deseo comprar algo", 4);
                                terminal.comprarFreeShop(numCaja);
                                imprimir("Este producto quiero comprar", 4);
                                terminal.pagarFreeShop(numCaja);
                                imprimir("Muy bien, tome el pago, adios!", 4);
                            } else {
                                imprimir("Mejor no compro nada", 4);
                            }
                            terminal.salirFreeShop();
                            imprimir("Sali del free-shop", 4);
                        } else {
                            imprimir("No hay espacio en el free-shop, una lastima", 4);
                        }
                    } else {
                        imprimir("Hmm no tengo ganas de ir al free-shop", 4);
                    }
                                //imprimir("Bandera 6");
                } else {
                    imprimir("No tengo mucho tiempo, no voy al free-shop esta vez", 4);
                }
                imprimir("Ahora a esperar el avion", 5);
                boolean limiteEsperaSuperado = false;
                while(pasaje.getNumVuelo() != terminal.recuperarNumVueloPuerto(puertoEmbarque) && !limiteEsperaSuperado){
                    if(ManejadorTiempo.milisRestantesParaHorario(pasaje.getHorario()) < ( - ManejadorTiempo.duracionHora())){
                        //  Si no es su vuelo y pasó más de 1 hora de su vuelo
                        limiteEsperaSuperado = true;
                    } else {
                        //  Espera que llegue su avion
                        terminal.esperarAvion(puertoEmbarque);
                    }
                }
                //  Revisa por que razon dejó de esperar
                if(limiteEsperaSuperado){
                    imprimir("Ya esperé mucho, pasó más de una hora de mi vuelo, me voy", 5);
                } else {
                    // Si limite de espera es falso, entonces salio del while porque llego su avion
                    imprimir("Mi avión ya llegó, me retiro", 5);
                }
            } catch (InterruptedException e){
                imprimir("El aeropuerto esta cerrando, mejor me voy", 6);
            }
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
    
    public void imprimir(String cadena, int identacion){
        String cadenaFinal = "";
        for(int i = 1; i <= identacion; i++){
            cadenaFinal += "---";
        }
        cadenaFinal += nombre + ": " + cadena;
        System.out.println(cadenaFinal);
    }
}
