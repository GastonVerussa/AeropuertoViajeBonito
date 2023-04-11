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
        boolean exito;
        //  Selecciona aerolinea al azar
        String aerolinea = aerolineas[(int) (Math.random() * (aerolineas.length - 1))];
        Terminal terminal = terminales[(int) (Math.random() * (terminales.length - 1))];
        int numVuelo = ultimoNumVuelo++;
        do{
            int puertoEmbarque = 1 + (int) (Math.random() * (ultimoPuertoEmbarque - 1));
            //  Consigue una hora al azar entre 9 y 20 (Para dar margen del horario 6 a 21)
            int horaRandom = random.nextInt(9, 21);
            //  Consigue un minuto al azar. Solo entre 0 minutos o 30 minutos. Margen por si 2 vuelos
            //      van al mismo puerto
            int minutoRandom = random.nextInt(2) * 30;
            Horario horario = new Horario(diaActual, horaRandom, minutoRandom); 
            Vuelo vuelo = new Vuelo(aerolinea, numVuelo, horario, terminal, puertoEmbarque);
            exito = puestoInformes.agregarVuelo(vuelo);
            if(exito)generadorPasajes.agregarVuelo(vuelo);
          //    Mientras no tenga exito, sigue intentando con un nuevo puerto y horario
        } while(!exito);
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
