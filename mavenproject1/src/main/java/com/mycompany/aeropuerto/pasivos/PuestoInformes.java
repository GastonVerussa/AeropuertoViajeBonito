package com.mycompany.aeropuerto.pasivos;

import com.mycompany.aeropuerto.utiles.DiccionarioAVL;

public class PuestoInformes {
    
    private final DiccionarioAVL mapaTerminales;
    
    public PuestoInformes(){
        mapaTerminales = new DiccionarioAVL();
    }
    
    public boolean agregarAerolinea(String nombreAerolinea, PuestoAtencion puestoAtencion){
        return mapaTerminales.insertar(nombreAerolinea, puestoAtencion);
    }
    
    public synchronized PuestoAtencion recuperarPuesto(String nombreAerolinea){
        return (PuestoAtencion) mapaTerminales.obtenerInformacion(nombreAerolinea);
    }
}
