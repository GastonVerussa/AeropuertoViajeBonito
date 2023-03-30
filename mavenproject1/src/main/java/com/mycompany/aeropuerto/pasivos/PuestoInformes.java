package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.Vuelo;
import java.util.Collection;
import java.util.TreeMap;

public class PuestoInformes {
    
    private final TreeMap<String, PuestoAtencion> mapaTerminales;
    
    public PuestoInformes(){
        mapaTerminales = new TreeMap<>();
    }
    
    //  Metodos para manejar los vuelos
    
    public void agregarAerolinea(String nombreAerolinea, int capacidadPuesto){
        mapaTerminales.put(nombreAerolinea, new PuestoAtencion(nombreAerolinea, capacidadPuesto));
    }
    
    public void agregarVuelo(Vuelo vuelo){
        PuestoAtencion puesto =  mapaTerminales.get(vuelo.getAerolinea());
        puesto.agregarVuelo(vuelo);
    }
    
    public boolean eliminarVuelo(Vuelo vuelo){
        PuestoAtencion puesto = (PuestoAtencion) mapaTerminales.get(vuelo.getAerolinea());
        return puesto.eliminarVuelo(vuelo.getNumVuelo());
    }

    public void vaciarVuelos(){
        Collection<PuestoAtencion> puestos = mapaTerminales.values();
        for(PuestoAtencion puesto : puestos){
            puesto.vaciarVuelos();
        }
    }
    
    //  Metodos para los pasajeros
    
    public synchronized PuestoAtencion recuperarPuesto(String nombreAerolinea){
        return (PuestoAtencion) mapaTerminales.get(nombreAerolinea);
    }
}
