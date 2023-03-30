package com.mycompany.aeropuerto;

public class Pasaje {
    
    private final String aerolinea;
    private final int numVuelo;
    private final Horario horario;
    
    public Pasaje(String aerolinea, int numVuelo, Horario horario){
        this.aerolinea = aerolinea;
        this.numVuelo = numVuelo;
        this.horario = horario;
    }

    public String getAerolinea() {
        return aerolinea;
    }

    public int getNumVuelo() {
        return numVuelo;
    }

    public Horario getHorario() {
        return horario;
    }
}
