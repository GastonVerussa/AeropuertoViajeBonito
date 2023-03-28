package com.mycompany.aeropuerto;

import com.mycompany.aeropuerto.pasivos.Terminal;

public class Vuelo{
    
    private final String aerolinea;
    private final int numVuelo;
    private final int horario;
    private final Terminal terminal;
    private final int puertoEmbarque;

    public Vuelo(String aerolinea, int numVuelo, int horario, Terminal terminal, int puertoEmbarque) {
        this.aerolinea = aerolinea;
        this.numVuelo = numVuelo;
        this.horario = horario;
        this.terminal = terminal;
        this.puertoEmbarque = puertoEmbarque;
    }

    public String getAerolinea() {
        return aerolinea;
    }

    public int getNumVuelo() {
        return numVuelo;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public int getPuertoEmbarque() {
        return puertoEmbarque;
    }
    
    public int getHorario(){
        return horario;
    }
}
