package com.mycompany.aeropuerto;

import java.util.ArrayList;
import java.util.Collections;

public class GeneradorPasajes {

//  Clase que mantendrá los vuelos en un ArrayList con el proposito de generar vuelos al azar
    
    private final ArrayList<Vuelo> vuelos;
    
    public GeneradorPasajes(int capacidadInicial){
        vuelos = new ArrayList(capacidadInicial);
    }
    
    //  Crea un pasaje al azar para un vuelo que falten 2 horas o mas (Margen para el pasajero)
    //  Prerrequisito: Vuelos ordenados por horario
    public Pasaje crearPasajeRandom() throws Exception{
        Pasaje resultado;
        if(vuelos.isEmpty()){
            throw new Exception("No hay vuelos disponibles");
        } else {
            Horario horarioActual = ManejadorTiempo.getHorarioActual();
            //  Le suma 2 horas para dar tiempo al pasajero a recorrer el aeropuerto
            Horario horarioInicial = new Horario(horarioActual.getDia(), horarioActual.getHora() + 2, horarioActual.getMinutos());
            int indiceInicial = 0;
            //  Busca el indice del primer vuelo que falten 2 horas o mas 
            for(int i = 0; i < vuelos.size(); i++){
                if(vuelos.get(i).getHorario().compareTo(horarioInicial) >= 0){
                    indiceInicial = i;
                    break;
                }           }
            if(indiceInicial == 0){
                //  Si sigue siendo 0, significa que pasó por todos los vuelos y ninguno esta a tiempo
                throw new Exception("Los vuelos restantes estan a mas de 2 horas de diferencia");
            } else {
                //  Genera un indice al azar entre el inicial y el ultimo
                int indiceElegido = indiceInicial + ((int) (Math.random() * (vuelos.size() - 1 - indiceInicial)));  
                Vuelo vueloDevuelto = vuelos.get(indiceElegido);
                resultado = new Pasaje(vueloDevuelto.getAerolinea(), vueloDevuelto.getNumVuelo(), vueloDevuelto.getHorario());
            }
        }
        return resultado;
    }
    
    public void agregarVuelo(Vuelo vuelo){
        vuelos.add(vuelo);
    }
    
    public void ordenarVuelos(){
        Collections.sort(vuelos);
    }
    
    public boolean eliminarVuelo(Vuelo vuelo){
        return vuelos.remove(vuelo);
    }
    
    public void vaciarVuelos(){
        vuelos.clear();
    }
}
