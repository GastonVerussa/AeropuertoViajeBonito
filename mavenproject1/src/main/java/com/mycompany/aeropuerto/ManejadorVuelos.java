package com.mycompany.aeropuerto;

import com.mycompany.aeropuerto.pasivos.PuestoInformes;
import com.mycompany.aeropuerto.pasivos.Terminal;
import java.util.Random;

public class ManejadorVuelos {

    private int ultimoNumVuelo;
    private final String[] aerolineas;
    private final Terminal[] terminales;
    private final int ultimoPuertoEmbarque;
    private final GeneradorPasajes generadorPasajes;
    private final PuestoInformes puestoInformes;
    private final int CAPACIDAD_PUESTOS = 5;
    private final Random random = new Random(System.currentTimeMillis());

    public ManejadorVuelos(String[] aerolineas, Terminal[] terminales, int ultimoPuertoEmbarque, GeneradorPasajes generadorPasajes, PuestoInformes puestoInformes) {
        this.aerolineas = aerolineas;
        this.terminales = terminales;
        this.ultimoPuertoEmbarque = ultimoPuertoEmbarque;
        this.generadorPasajes = generadorPasajes;
        this.puestoInformes = puestoInformes;
        for (String aerolinea : aerolineas){
            this.puestoInformes.agregarAerolinea(aerolinea, CAPACIDAD_PUESTOS);
        }
    }   
    
    public void crearVueloRandom(int diaActual) throws Exception{
        //  Selecciona aerolinea al azar
        String aerolinea = aerolineas[(int) (Math.random() * (aerolineas.length - 1))];
        Terminal terminal = terminales[(int) (Math.random() * (terminales.length - 1))];
        int puertoEmbarque = 1 + (int) (Math.random() * (ultimoPuertoEmbarque - 1));
        Horario horario = new Horario(diaActual, random.nextInt(6, 22), random.nextInt(60));
        Vuelo vuelo = new Vuelo(aerolinea, ultimoNumVuelo + 1, horario, terminal, puertoEmbarque);
        generadorPasajes.agregarVuelo(vuelo);
        puestoInformes.agregarVuelo(vuelo);
    }
    
    //  Si no fue borrado exitosamente de ambos lados lo deja como estaba
    public boolean eliminarVuelo(Vuelo vuelo){
        boolean exito = generadorPasajes.eliminarVuelo(vuelo);
        if(exito){
            exito = puestoInformes.eliminarVuelo(vuelo);
            if(!exito){
                generadorPasajes.agregarVuelo(vuelo);
            }
        }
        return exito;
    }
    
    public void vaciarVuelos(){
        generadorPasajes.vaciarVuelos();
    }
}
