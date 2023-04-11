package com.mycompany.aeropuerto;

public class Horario implements Comparable<Horario>{

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
    
    @Override
    public int compareTo(Horario horario){
        int resultado;
        resultado = dia - horario.getDia();
        if(resultado == 0){
            resultado = hora - horario.getHora();
            if(resultado == 0){
                resultado = minutos - horario.getMinutos();
            }
        }
        return resultado;
    }
    
    public boolean equal(Horario horario){
        return this.dia == horario.getDia() && this.hora == horario.getHora()
                &&  this.minutos == horario.getMinutos();
    }
}
