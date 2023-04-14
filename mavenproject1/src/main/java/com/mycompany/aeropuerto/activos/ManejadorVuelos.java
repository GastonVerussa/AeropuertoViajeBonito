package com.mycompany.aeropuerto.activos;

import com.mycompany.aeropuerto.GeneradorPasajes;
import com.mycompany.aeropuerto.Horario;
import com.mycompany.aeropuerto.ManejadorTiempo;
import com.mycompany.aeropuerto.Vuelo;
import com.mycompany.aeropuerto.pasivos.PuestoInformes;
import com.mycompany.aeropuerto.pasivos.Terminal;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManejadorVuelos extends Thread{

    private int ultimoNumVuelo;
    private final String[] aerolineas;
    private final Terminal[] terminales;
    private final int ultimoPuertoEmbarque;
    private final GeneradorPasajes generadorPasajes;
    private final PuestoInformes puestoInformes;
    private final int CAPACIDAD_PUESTOS = 5;
    private final Random random = new Random(System.currentTimeMillis());
    private final int CANT_VUELOS_DIA;

    public ManejadorVuelos(String[] aerolineas, Terminal[] terminales, int ultimoPuertoEmbarque, GeneradorPasajes generadorPasajes, PuestoInformes puestoInformes, int cantidadVuelosDia) {
        this.aerolineas = aerolineas;
        this.terminales = terminales;
        this.ultimoPuertoEmbarque = ultimoPuertoEmbarque;
        this.generadorPasajes = generadorPasajes;
        this.puestoInformes = puestoInformes;
        for (String aerolinea : aerolineas){
            this.puestoInformes.agregarAerolinea(aerolinea, CAPACIDAD_PUESTOS);
        }
        this.CANT_VUELOS_DIA = cantidadVuelosDia;
    }   
    
    public void run(){
        int diaActual = 1;
        while(true){
            //  Crea una cantidad de vuelos igual al valor de la constante
            for(int i = 1; i <= CANT_VUELOS_DIA; i++){
                try {
                    this.crearVueloRandom(diaActual);
                    System.out.println("Vuelo " + ultimoNumVuelo + " generado con exito");
                } catch (Exception ex) {
                    Logger.getLogger(ManejadorVuelos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                //  Una vez que se crearon todos los vuelos espera a que abra
                while(!ManejadorTiempo.estaAbierto()){
                    Thread.sleep(ManejadorTiempo.duracionHora() * 2);
                }
                //  Y vuelva a cerrarse el aeropuerto. Esto es porque crea los vuelos
                //      mientras esta cerrado para que esten listos en el dia
                while(ManejadorTiempo.estaAbierto()){
                    Thread.sleep(ManejadorTiempo.duracionHora() * 2);
                }
            } catch (InterruptedException ex) {
                System.out.println("El generador de vuelos tuvo problema durmiendo");;
            }
            //  Una vez que sabe que no se utilizaran mas estos vuelos, los vacia
            this.vaciarVuelos();
            //  Pasa el dia actual y vuelve al incio del loop para crear vuelos nuevos
            diaActual++;
        }
    }
    
    private void crearVueloRandom(int diaActual) throws Exception{
        boolean exito;
        //  Selecciona aerolinea al azar
        String aerolinea = aerolineas[random.nextInt(aerolineas.length)];
        Terminal terminal = terminales[random.nextInt(terminales.length)];
        int numVuelo = ultimoNumVuelo++;
        do{
            //  Consigue un puerto al azar entre los posibles para la terminal
            int puertoEmbarque = random.nextInt(terminal.getPrimerPuerto(), terminal.getUltimoPuerto() + 1);
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
    private boolean eliminarVuelo(Vuelo vuelo){
        boolean exito = generadorPasajes.eliminarVuelo(vuelo);
        if(exito){
            exito = puestoInformes.eliminarVuelo(vuelo);
            if(!exito){
                generadorPasajes.agregarVuelo(vuelo);
            }
        }
        return exito;
    }
    
    private void vaciarVuelos(){
        generadorPasajes.vaciarVuelos();
    }
}
