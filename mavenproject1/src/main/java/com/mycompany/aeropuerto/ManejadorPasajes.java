package com.mycompany.aeropuerto;

import java.util.ArrayList;

public class ManejadorPasajes {

//  Clase que mantendrá los 
    
    
    private final String[] aerolineas;
    private final ArrayList<Vuelo>[] vuelos;
    private final int cantidadAerolines;
    
    public ManejadorPasajes(int cantidadAerolineas){
        aerolineas = new String[cantidadAerolineas];
        vuelos = new ArrayList[cantidadAerolineas];
        for(int i = 0; i < cantidadAerolineas; i++){
            vuelos[i] = new ArrayList<Vuelo>();
        }
        this.cantidadAerolines = cantidadAerolineas;
    }
    
    public Pasaje crearPasajeRandom(){
        int numAerolinea = (int)(Math.random() * cantidadAerolines);
        Vuelo vueloElegido = vuelos[numAerolinea].get((int) (Math.random() * (vuelos[numAerolinea].size() - 1)));
        return new Pasaje(vueloElegido.getAerolinea(), vueloElegido.getNumVuelo(), vueloElegido.getHorario());
    }
}
