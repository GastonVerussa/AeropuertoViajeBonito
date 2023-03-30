package com.mycompany.aeropuerto;

public class Horario {

    private final int dia;
    private final int hora;
    private final int minutos;

    public Horario(int dia, int Hora, int Minutos) {
        this.dia = dia;
        this.hora = Hora;
        this.minutos = Minutos;
    }

    public int getDia(){
        return dia;
    }
    
    public int getHora() {
        return hora;
    }

    public int getMinutos() {
        return minutos;
    }
}
