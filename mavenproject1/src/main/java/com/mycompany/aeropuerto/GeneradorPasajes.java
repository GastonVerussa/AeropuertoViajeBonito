package com.mycompany.aeropuerto;

import java.util.ArrayList;

public class GeneradorPasajes {

//  Clase que mantendrá los vuelos en un ArrayList con el proposito de generar vuelos al azar
    
    private final ArrayList<Vuelo> vuelos;
    
    public GeneradorPasajes(int capacidadInicial){
        vuelos = new ArrayList(capacidadInicial);
    }
    
    public Pasaje crearPasajeRandom() throws Exception{
        Pasaje resultado;
        if(vuelos.isEmpty()){
            throw new Exception("No hay vuelos disponibles");
        } else {
            int indice = (int) (Math.random() * (vuelos.size() - 1));  
            Vuelo vueloDevuelto = vuelos.get(indice);
            resultado = new Pasaje(vueloDevuelto.getAerolinea(), vueloDevuelto.getNumVuelo(), vueloDevuelto.getHorario());
        }
        return resultado;
    }
    
    public void agregarVuelo(Vuelo vuelo){
        vuelos.add(vuelo);
    }
    
    public boolean eliminarVuelo(Vuelo vuelo){
        return vuelos.remove(vuelo);
    }
    
    public void vaciarVuelos(){
        vuelos.clear();
    }
}
