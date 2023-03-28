package com.mycompany.aeropuerto.utiles;

public class Cola {
    
    // Nodos para mantener frente y fin
    private Nodo frente;
    private Nodo fin;
    
    // Crea y devuelve una cola vacía
    public Cola(){
        frente = null;
        fin = null;
    }
    
    // Pone el elemento al final de la cola. Devuelve verdadero si el elemento se pudo agregar en la estructura
    //      y falso en caso contrario.
    public boolean poner(Object nuevoElem){
        
        if(this.esVacia()){
            //  Si esta vacía, crea un nuevo nodo y lo asigna al frente y fin
            frente = new Nodo(nuevoElem, null);
            fin = frente;
        } else {
            //  Si no esta vacía, crea un nuevo nodo, enlaza el fin al mismo y lo asigna como el nuevo fin
            Nodo aux = new Nodo(nuevoElem, null);
            fin.setEnlace(aux);
            fin = aux;
        }
        
        //  En la implementacion dinámica siempre podra agregar un nuevo elemento
        return true;
    }

    
    // Saca el elemento que está en el frente de la cola. Devuelve verdadero si el elemento se pudo sacar 
    //      (la estructura no estaba vacía) y falso en caso contrario.
    public boolean sacar (){
        
        //  Variable para devolver el exito
        boolean exito = true;
        
        if(this.esVacia()){
            //  Si esta vacía
            exito = false;
        } else {
            //  Si no, borra el elemento del frente
            frente = frente.getEnlace();
            
            //  Si solo había un elemento, tambien borra fin
            if(frente == null)fin = null;
        }
        
        return exito;
    }

    // Devuelve el elemento que está en el frente. Precondición: la cola no está vacía.
    public Object obtenerFrente(){
        return frente.getElem();
    }
    
    // Devuelve verdadero si la cola no tiene elementos y falso en caso contrario
    public boolean esVacia(){
        return frente == null;
    }
    
    // Saca todos los elementos de la estructura.
    public void vaciar (){
        
        //  Cambia los punteros a null, los nodos son borrados por el Garbage Collector
        frente = null;
        fin = null;
    }

    // Devuelve una copia exacta de los datos en la estructura original, y respetando el orden de los mismos,
    //      en otra estructura del mismo tipo
    public Cola clone(){
        
        //  Nueva Cola que se devolvera de resultado
        Cola resultado = new Cola();
        
        //  Auxiliar para recorrer la estructura
        Nodo aux = frente;
        
        //  Mientras no haya llegado al ultimo, va poniendo los elementos en el clon
        while(aux != null){
            resultado.poner(aux.getElem());
            aux = aux.getEnlace();
        }
        
        return resultado;
    }
    
    // Crea y devuelve una cadena de caracteres formada por todos los elementos de la cola 
    //      para poder mostrarla por pantalla. Es recomendable utilizar este método únicamente 
    //      en la etapa de prueba y luego comentar el código.
    public String toString(){
        
        //  String para el resultado
        String resultado;
        
        if(this.esVacia()){
            //  Si esta vacia
            resultado = "Cola Vacía";
        } else {
            //  Si no esta vacia, abre el corchete
            resultado = "[";
            //  Auxiliar para recorrer la estructura
            Nodo aux = frente;
            
            //  Mientras no haya llegado
            while(aux != null){
                //  Agrega el elemento al String
                resultado += aux.getElem().toString();
                
                //  Avanza al siguiente Nodo
                aux = aux.getEnlace();
                
                //  Si no es el ultimo, agrega ", "
                if(aux != null){
                    resultado += ", ";
                }
            }
            
            //  Cierra el corchete
            resultado += "]";
        }
        
        return resultado;
    }
}
