package com.mycompany.aeropuerto;

public class Pasaje {
    
    private final String aerolinea;
    private final int numVuelo;
    private final int horario;
    
    public Pasaje(String aerolinea, int numVuelo, int horario){
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

    public int getHorario() {
        return horario;
    }
}
