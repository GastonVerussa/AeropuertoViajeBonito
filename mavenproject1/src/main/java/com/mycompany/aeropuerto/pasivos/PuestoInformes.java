package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.Vuelo;
import com.mycompany.aeropuerto.activos.Guardia;
import com.mycompany.aeropuerto.activos.RecepcionistaAtencion;
import java.util.Collection;
import java.util.TreeMap;

public class PuestoInformes {
    
    private final TreeMap<String, PuestoAtencion> mapaPuestosAtencion;
    
    public PuestoInformes(){
        mapaPuestosAtencion = new TreeMap<>();
    }
    
    //  Metodos para manejar los vuelos
    
    public void agregarAerolinea(String nombreAerolinea, int capacidadPuesto){
        PuestoAtencion nuevoPuesto = new PuestoAtencion(nombreAerolinea, capacidadPuesto);
        mapaPuestosAtencion.put(nombreAerolinea, nuevoPuesto);
        RecepcionistaAtencion recepcionsita = new RecepcionistaAtencion(nombreAerolinea, nuevoPuesto);
        recepcionsita.start();
        Guardia guardia = new Guardia(nombreAerolinea, nuevoPuesto);
        guardia.start();
    }
    
    public boolean agregarVuelo(Vuelo vuelo){
        PuestoAtencion puesto =  mapaPuestosAtencion.get(vuelo.getAerolinea());
        return puesto.agregarVuelo(vuelo);
    }
    
    public boolean eliminarVuelo(Vuelo vuelo){
        PuestoAtencion puesto = (PuestoAtencion) mapaPuestosAtencion.get(vuelo.getAerolinea());
        return puesto.eliminarVuelo(vuelo.getNumVuelo());
    }

    public void vaciarVuelos(){
        Collection<PuestoAtencion> puestos = mapaPuestosAtencion.values();
        for(PuestoAtencion puesto : puestos){
            puesto.vaciarVuelos();
        }
    }
    
    //  Metodos para los pasajeros
    
    public synchronized PuestoAtencion recuperarPuesto(String nombreAerolinea){
        return (PuestoAtencion) mapaPuestosAtencion.get(nombreAerolinea);
    }
}
