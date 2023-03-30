package com.mycompany.aeropuerto;

public class ManejadorTiempo {
    
    private final long tiempoInicial;
    private final int horaInicial = 5;
    
    private final int duracionHora = 60000;
    private final int duracionMinuto = 1000;

    public ManejadorTiempo(long tiempoInicial) {
        this.tiempoInicial = tiempoInicial;
    }
    
    public long getMilisPasadosActual(){
        return System.currentTimeMillis() - tiempoInicial;
    }
    
    public Horario getHorarioActual(){
        //  Consigue la cantidad de milisegundos pasados desde el inicio
        long diferenciaTiempo = getMilisPasadosActual();
        //  Consigue las horas totales pasadas de las 00:00hs del dia 1. Le suma la hora inicial.
        int horasTotales = ((int) diferenciaTiempo / duracionHora) + horaInicial;
        //  Calcula el dia que es, dividiendo las horas por 24 para saber cuantos dias pasaron del inicial.
        int diaActual = (horasTotales / 24) + 1;
        //  Calcula la hora actual, viendo el resto de dividir las totales por 24
        int horaActual = (horasTotales) % 24;
        //  Calcula los minutos actuales, primero viendo el resto de dividir por 
        //      la duracion de las horas, luego divide por la duracion de los minutos
        int minutosActuales = (int) (diferenciaTiempo % duracionHora) / duracionMinuto;
        //  Ya calculado todo, devuelve el horario actual
        return new Horario(diaActual, horaActual, minutosActuales);
    }
    
    //  Devuelve el tiempo que falta para el horario enviado como parametro
    public long milisRestantesParaHorario(Horario horario){
        //  Consigue el total de horas pasadas para el horario. Primero multiplicando los dias
        //      pasados (diaHorario - 1) por 24 y luego le suma las horas de ese dia.
        //      por ultimo resta la hora donde se inicio el conteo para tener las horas
        //      pasadas desde ese momento.
        int horasPasadas = ((horario.getDia() - 1) * 24) + horario.getHora() - horaInicial;
        //  Empieza a calcular los milisegundos, multiplicando las horas pasadas por su
        //      duracion en milis
        long resultado = horasPasadas * duracionHora;
        //  Luego suma la duracion de los minutos pasados en milis
        resultado += horario.getMinutos() * duracionMinuto;
        //  Luego, teniendo ya todos los milis que deben pasar desde el tiempo 
        //      inicial hasta ese horario, les suma el timepo inicial para saber
        //      cuantos milis totales debe ser.
        resultado += tiempoInicial;
        //  Al total le resta los milis actuales y lo devuelve.
        resultado -= getMilisPasadosActual();
        return resultado;
    }
    
    public int duracionHora(){
        return duracionHora;
    }
    
    public int duracionMinuto(){
        return duracionMinuto;
    }
}
