package com.mycompany.aeropuerto;

//  Una clase para manejar el tiempo, abstracta ya que no se necesitan crear instancia.
//      Todos sus metodos son estaticos
public abstract class ManejadorTiempo {
    
    private static long tiempoInicial;
    private static boolean estaAbierto = false;
    private static final Object monitorAbierto = new Object();
    
    private static final int HORA_INICIAL = 5;
    
    private static final int DURACION_MINUTO = 1000;
    private static final int DURACION_HORA = DURACION_MINUTO * 60;
    
    //  Se debe guardar cual fue el tiempo inicial antes de utilizar cualquier otro metodo
    public static void setTiempoInicial(long tiempo){
        ManejadorTiempo.tiempoInicial = tiempo;
    }
    
    //  Devuelve cuantos milisegundos pasaron
    public static long getMilisPasadosActual(){
        return System.currentTimeMillis() - tiempoInicial;
    }
    
    //  Devuelve el Horario actual (Dia, Hora, Minutos)
    public static Horario getHorarioActual(){
        //  Consigue la cantidad de milisegundos pasados desde el inicio
        long diferenciaTiempo = getMilisPasadosActual();
        //  Consigue las horas totales pasadas de las 00:00hs del dia 1. Le suma la hora inicial.
        int horasTotales = ((int) diferenciaTiempo / DURACION_HORA) + HORA_INICIAL;
        //  Calcula el dia que es, dividiendo las horas por 24 para saber cuantos dias pasaron del inicial.
        int diaActual = (horasTotales / 24) + 1;
        //  Calcula la hora actual, viendo el resto de dividir las totales por 24
        int horaActual = (horasTotales) % 24;
        //  Calcula los minutos actuales, primero viendo el resto de dividir por 
        //      la duracion de las horas, luego divide por la duracion de los minutos
        int minutosActuales = (int) (diferenciaTiempo % DURACION_HORA) / DURACION_MINUTO;
        //  Ya calculado todo, devuelve el horario actual
        return new Horario(diaActual, horaActual, minutosActuales);
    }
    
    //  Devuelve el tiempo que falta para el horario enviado como parametro
    public static long milisRestantesParaHorario(Horario horario){
        //  Consigue el total de horas faltantes para el horario. Primero multiplicando los dias
        //      pasados (diaHorario - 1) por 24 y luego le suma las horas de ese dia.
        //      por ultimo resta la hora donde se inicio el conteo para tener las horas
        //      pasadas desde ese momento.
        int horasPasadas = ((horario.getDia() - 1) * 24) + horario.getHora() - HORA_INICIAL;
        //  Empieza a calcular los milisegundos, multiplicando las horas pasadas por su
        //      duracion en milis
        long resultado = horasPasadas * DURACION_HORA;
        //  Luego suma la duracion de los minutos pasados en milis
        resultado += horario.getMinutos() * DURACION_MINUTO;
        //  Al total le resta los milis actuales y lo devuelve.
        resultado -= getMilisPasadosActual();
        return resultado;
    }
    
    //  Devuelve el valor de la duracion en milisegundos para una hora
    public static int duracionHora(){
        return DURACION_HORA;
    }
    
    //  Devuelve el valor de la duracion en milisegundos para un Minuto
    public static int duracionMinuto(){
        return DURACION_MINUTO;
    }
    
    public static void abrir(){
        synchronized (monitorAbierto) {
            ManejadorTiempo.estaAbierto = true;
            monitorAbierto.notifyAll();
        }
    }
    
    public static void cerrar(){
        ManejadorTiempo.estaAbierto = false;
    }
    
    public static boolean estaAbierto(){
        return ManejadorTiempo.estaAbierto;
    }
    
    public static void esperarApertura() throws InterruptedException{
        synchronized (monitorAbierto) {
            if(!estaAbierto)monitorAbierto.wait();
        }
    }
}
