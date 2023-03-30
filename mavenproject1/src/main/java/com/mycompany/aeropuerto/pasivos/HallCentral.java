package com.mycompany.aeropuerto.pasivos;

public class HallCentral {
    
    public HallCentral(){
    }
    
    public void esperarHall(PuestoAtencion puestoAtencion) throws InterruptedException{
        synchronized (puestoAtencion) {
            puestoAtencion.wait();
        }
    }
    
    public void avisarHall(PuestoAtencion puestoAtencion){
        synchronized (puestoAtencion) {
            puestoAtencion.notify();
        }
    }
}
