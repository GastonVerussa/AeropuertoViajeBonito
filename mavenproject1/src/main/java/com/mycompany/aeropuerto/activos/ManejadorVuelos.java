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
    private final GeneradorPasajes generadorPasajes;
    private final PuestoInformes puestoInformes;
    private final Random random = new Random(System.currentTimeMillis());
    private final int CANT_VUELOS_DIA;
    private final int DURACION_SLEEP = ManejadorTiempo.duracionHora() * 2;

    public ManejadorVuelos(String[] aerolineas, Terminal[] terminales, GeneradorPasajes generadorPasajes, PuestoInformes puestoInformes, int cantidadVuelosDia) {
        super(ManejadorTiempo.getThreadGroup(), "Manejador Vuelos");
        this.aerolineas = aerolineas;
        this.terminales = terminales;
        this.generadorPasajes = generadorPasajes;
        this.puestoInformes = puestoInformes;
        this.CANT_VUELOS_DIA = cantidadVuelosDia;
        this.ultimoNumVuelo = 1;
    }   
    
    public void run(){
        int diaActual = 1;
        while(true){
            //  Crea una cantidad de vuelos igual al valor de la constante
            for(int i = 1; i <= CANT_VUELOS_DIA; i++){
                try {
                    this.crearVueloRandom(diaActual);
                } catch (Exception ex) {
                    Logger.getLogger(ManejadorVuelos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            generadorPasajes.ordenarVuelos();
            try {
                //  Una vez que se crearon todos los vuelos espera que le avisen que cerro
                while(true){
                    Thread.sleep(DURACION_SLEEP);
                }
            } catch (InterruptedException ex) {
                System.out.println("ManejadorVuelos: Cerro el aeropuerto, voy preparando los vuelos de mañana");;
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
            if(exito){
                generadorPasajes.agregarVuelo(vuelo);
            System.out.println("Vuelo " + numVuelo + " creado: aerolinea " + aerolinea 
                    + " Terminal y puerto: " + terminal.getNombre() + ", " + puertoEmbarque 
                    + ". Horario :" + diaActual + "/" + horaRandom + ":" + minutoRandom);
            }
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
