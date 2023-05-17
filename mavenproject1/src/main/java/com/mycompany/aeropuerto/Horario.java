package com.mycompany.aeropuerto;

public class Horario implements Comparable<Horario>{

    private int dia;
    private int hora;
    private int minutos;

    public Horario(int dia, int hora, int minutos) {
        this.dia = dia;
        this.hora = hora;
        this.minutos = minutos;
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
    
    public void setDatos(int dia, int hora, int minutos){
        this.dia = dia;
        this.hora = hora;
        this.minutos = minutos;
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
    
    public boolean equals(Horario horario){
        return this.dia == horario.getDia() && this.hora == horario.getHora()
                &&  this.minutos == horario.getMinutos();
    }
    
    @Override
    public String toString(){
        return "dia " + dia + " a las " + hora + "/" + minutos + "hs";
    }
}
