package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.Vuelo;
import com.mycompany.aeropuerto.activos.Cajero;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Terminal {
    
    private class PuertoEmbarque{
        
        //  Guarda el vuelo que vaya a salir
        private Vuelo vueloActual;
        private ArrayList<Vuelo> vuelosAsignados;

        public PuertoEmbarque(){
            vuelosAsignados = new ArrayList<>(2);
        }

        public Vuelo getVuelo() {
            return vueloActual;
        }

        public void setVuelo(Vuelo vuelo) {
            this.vueloActual= vuelo;
            //  Como ya fue cargado como vuelo actual, se puede sacar de los
            //      vuelos asignados
            vuelosAsignados.remove(vuelo);
        }
        
        //  Guarda el vuelo entre los vuelos asignados para el dia actual.
        //  Revisa si ya hay un vuelo para el horario del vuelo. En caso de ser 
        //      cierto, no lo guarda y devuelve false
        public boolean asignarVuelo(Vuelo vuelo){
            boolean exito = true;
            //  Revisa todos los vuelos asignados para el dia
            for(Vuelo vueloAsignado : vuelosAsignados){
                //  Si alguno comparte el horario, setea exito en false
                if(vueloAsignado.getHorario().equals(vuelo.getHorario())){
                    exito = false;
                    break;
                }
            }
            //  Si hubo exito, es decir, no hay vuelo con horario repetido,
            //  Lo guarda en los vuelos asignados
            if(exito){
                vuelosAsignados.add(vuelo);
            }
            return exito;
        }
    }
    
    private final String nombre;
    private final FreeShop freeShop;
    private final PuertoEmbarque[] puertosEmbarque;
    private final int numPuertoInicial;
    private final ScheduledThreadPoolExecutor manejadorAvisos;
    
    public Terminal(String nombre, int puertoInicial, int puertoFinal, int capacidadFreeShop){
        this.nombre = nombre;
        numPuertoInicial = puertoInicial;
        puertosEmbarque = new PuertoEmbarque[puertoFinal - puertoInicial + 1];
        for(int i = 0; i < puertosEmbarque.length; i++){
            puertosEmbarque[i] = new PuertoEmbarque();
        }
        int cantidadCajeros = 2;
        freeShop = new FreeShop(capacidadFreeShop, cantidadCajeros);
        for(int i = 0; i < cantidadCajeros; i++){
            Cajero cajero = new Cajero(this.nombre + " " + i, freeShop, i);
            cajero.start();
        }
        manejadorAvisos = new ScheduledThreadPoolExecutor(1);
    }
    
    public String getNombre(){
        return nombre;
    }
    
    //  Metodos para Pasajero
    
    public void esperarAvion(int puertoEmbarque) throws InterruptedException{
        PuertoEmbarque puerto = puertosEmbarque[puertoEmbarque - numPuertoInicial];
        synchronized (puerto) {
            puerto.wait(ManejadorTiempo.duracionHora());
        }
    }
    
    public int recuperarNumVueloPuerto(int numPuerto){
        Vuelo vuelo = puertosEmbarque[numPuerto - numPuertoInicial].getVuelo();
        int respuesta;
        //  Se fija si hay un vuelo actual
        if(vuelo == null){
            //  Si no hay devuelve -1, ya que ningun vuelo podria tener ese valor
            respuesta = -1;
        } else {
            //  Si hay devuelve el numero de vuelo
            respuesta = vuelo.getNumVuelo();
        }
        return respuesta;
    }
 
    public boolean entrarFreeShop(){
        return freeShop.entrar();
    }
    
    public int irCajaFreeShop(){
        return freeShop.irCaja();
    }
    
    public void comprarFreeShop(int numCaja) throws InterruptedException {
        freeShop.comprar(numCaja);
    }
    
    public void pagarFreeShop(int numCaja) throws InterruptedException {
        freeShop.pagar(numCaja);
    }
    
    public void salirFreeShop(){
        freeShop.salir();
    }
    
    //  Metodos para el Manejador Vuelos
    
    public int getPrimerPuerto(){
        return numPuertoInicial;
    }
    
    public int getUltimoPuerto(){
        return numPuertoInicial + puertosEmbarque.length - 1;
    }
    
    public boolean cargarVuelo(Vuelo vuelo){
        //  Prepara el aviso del vuelo
        PuertoEmbarque puerto = puertosEmbarque[vuelo.getPuertoEmbarque() - numPuertoInicial];
        boolean exito = puerto.asignarVuelo(vuelo);
        if(exito) manejadorAvisos.schedule(new AvisoVuelo(vuelo), ManejadorTiempo.milisRestantesParaHorario(vuelo.getHorario()), TimeUnit.MILLISECONDS);
        return exito;
    }

    private class AvisoVuelo implements Runnable{
        
        private final Vuelo vuelo;

        public AvisoVuelo(Vuelo vuelo) {
            this.vuelo = vuelo;
        }
        
        public void run(){
            PuertoEmbarque puerto = puertosEmbarque[vuelo.getPuertoEmbarque() - numPuertoInicial];
            synchronized (puerto) {
                System.out.println("Listo el embarque para el vuelo " + vuelo.getNumVuelo() + 
                        " con la aerolinea " + vuelo.getAerolinea() + " en el puerto " + vuelo.getPuertoEmbarque());
                puerto.setVuelo(vuelo);
                puerto.notifyAll();
            }
        }
    }
}
