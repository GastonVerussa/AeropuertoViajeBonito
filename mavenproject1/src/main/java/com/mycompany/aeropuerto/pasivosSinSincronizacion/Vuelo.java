package com.mycompany.aeropuerto.pasivosSinSincronizacion;

import com.mycompany.aeropuerto.pasivosSinSincronizacion.Horario;
import com.mycompany.aeropuerto.pasivos.Terminal;

public class Vuelo implements Comparable<Vuelo>{
    
    private final String aerolinea;
    private final int numVuelo;
    private final Horario horario;
    private final Terminal terminal;
    private final int puertoEmbarque;

    public Vuelo(String aerolinea, int numVuelo, Horario horario, Terminal terminal, int puertoEmbarque) {
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
    
    public Horario getHorario(){
        return horario;
    }
    
    public int compareTo(Vuelo vuelo){
        return horario.compareTo(vuelo.getHorario());
    }
}
