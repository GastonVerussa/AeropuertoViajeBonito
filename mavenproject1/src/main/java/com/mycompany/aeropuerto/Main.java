package com.mycompany.aeropuerto;

import com.mycompany.aeropuerto.activos.ManejadorVuelos;
import com.mycompany.aeropuerto.activos.GeneradorPasajeros;
import com.mycompany.aeropuerto.activos.Maquinista;
import com.mycompany.aeropuerto.activos.Pasajero;
import com.mycompany.aeropuerto.activos.PorteroHorario;
import com.mycompany.aeropuerto.pasivos.PuestoInformes;
import com.mycompany.aeropuerto.pasivos.Terminal;
import com.mycompany.aeropuerto.pasivos.Tren;

public class Main {
    
    public static void main(String[] args) {
        
        final int CANTIDAD_VUELOS_DIA = 15;
        final String[] AEROLINEAS = {"Argentina", "FlyBondi", "Air France", "Lufthansa", "Gol"};
        final String[] NOMBRE_TERMINALES = {"A", "B", "C"};
        final int[] CANT_PUERTOS_TERMINAL = {7, 8, 5};
        final int CAPACIDAD_FREE_SHOP = 5;
        final int CAPACIDAD_PUESTOS_ATENCION = 4;
        final int CAPACIDAD_TREN = 15;
        
        
        Terminal[] terminales = new Terminal[NOMBRE_TERMINALES.length];
        
        int ultimoPuerto = 0;
        for(int i = 0; i < NOMBRE_TERMINALES.length; i++){
            terminales[i] = new Terminal(NOMBRE_TERMINALES[i], ultimoPuerto + 1, ultimoPuerto + CANT_PUERTOS_TERMINAL[i], CAPACIDAD_FREE_SHOP);
            ultimoPuerto = ultimoPuerto + CANT_PUERTOS_TERMINAL[i];
        }
        
        GeneradorPasajes generadorPasajes = new GeneradorPasajes(CANTIDAD_VUELOS_DIA);
        PuestoInformes puestoInformes = new PuestoInformes();
        
        for(String aerolinea : AEROLINEAS){
            puestoInformes.agregarAerolinea(aerolinea, CAPACIDAD_PUESTOS_ATENCION);
        }
        
        ManejadorVuelos manejadorVuelos = new ManejadorVuelos(AEROLINEAS, terminales, generadorPasajes, puestoInformes, CANTIDAD_VUELOS_DIA);
        Tren tren = new Tren(CAPACIDAD_TREN, NOMBRE_TERMINALES);
        
        Pasajero.setDatos(puestoInformes, tren, generadorPasajes);
        
        Maquinista maquinista = new Maquinista("Raul", tren, NOMBRE_TERMINALES);
        GeneradorPasajeros generadorPasajeros = new GeneradorPasajeros();
        ManejadorTiempo.setGenerador(generadorPasajeros);
        PorteroHorario portero = new PorteroHorario();
        
        manejadorVuelos.start();
        maquinista.start();
        generadorPasajeros.start();
        portero.start();
    }
}
