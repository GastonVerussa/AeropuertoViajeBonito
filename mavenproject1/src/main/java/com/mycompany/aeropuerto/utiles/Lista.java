package com.mycompany.aeropuerto.utiles;

public class Lista {
    
    //  Variable Nodo que apunta simepre a la cabecera
    private Nodo cabecera;
    private int longitud;
    
    //  Crea y devuelve una lista vacía.
    public Lista(){
        cabecera = null;
        longitud = 0;
    }

    //  Agrega el elemento pasado por parámetro en la posición pos, de manera que la cantidad de elementos
    //      de la lista se incrementa en 1. Para una inserción exitosa, la posición recibida debe ser 1 ≤ pos ≤
    //      longitud(lista) + 1. Devuelve verdadero si se puede insertar correctamente y falso en caso contrario.
    public boolean insertar(Object elem, int pos){
        
        //  Variable de control para saber si hubo un fallo
        boolean exito = true;
        
        //  Checkea que la posicion sea valida
        if(pos < 1 || pos > this.longitud + 1){
            exito = false;
        } else {
            //  Caso especial, si es la primera posicion y es valida
            if(pos == 1){
                //  Simplemente crea un nuevo nodo enalazado a la actual cabecera y lo asigna como nueva cabecera.
                cabecera = new Nodo(elem, cabecera);
            } else {
                //  Nodo auxiliar para recorrer la estructura en busca de la posicion
                Nodo aux = cabecera;
                //  Recorre la estructura hasta encontrar la posicion anterior a la buscada
                for(int i = 1; i < pos - 1; i++){
                    aux = aux.getEnlace();
                }
                
                //  Una vez encontrada, crea un nuevo nodo con el elemento y enlazado al nodo que antes ocupaba esa posición
                Nodo nuevo = new Nodo(elem, aux.getEnlace());
                
                //      Y lo setea como el nuevo enlace a la posicion anterior.
                aux.setEnlace(nuevo);
                
            }
        }
        
        if(exito)longitud++;
        
        return exito;
    }

    //  Borra el elemento de la posición pos, por lo que la cantidad de elementos de la lista disminuye
    //      en uno. Para una eliminación exitosa, la lista no debe estar vacía y la posición recibida debe ser
    //      1 ≤ pos ≤ longitud(lista). Devuelve verdadero si se pudo eliminar correctamente y falso en caso contrario.
    public boolean eliminar(int pos){
        
        //  Variable de control para saber si hubo exito
        boolean exito = true;
        
        //  Si la lista esta vacía, o la posicion no es valida
        if(this.esVacia() || pos < 1 || pos > this.longitud()){
            exito = false;
        } else {
            //  Caso especial, si se quiere sacar el elemento de la primera posicion
            if(pos == 1){
                //  Simplemente se asigna la nueva cabecera al enlace de la vieja cabecera,
                //      el Garbage Collector de Java se encarga de borrar la vieja cabecera.
                cabecera = cabecera.getEnlace();
            } else {
                //  Variable Nodo auxiliar para recorrer la estructura
                Nodo aux = cabecera;
                //  Busca el nodo en la posicion anterior a la buscada
                for(int i = 1; i < pos - 1; i++){
                    aux = aux.getEnlace();
                }
                
                //  Setea el enlace del Nodo de la anterior posicion al enlace del Nodo
                //      que se desea borrar, al perder el enlace el Garbage Collector lo borra.  
                aux.setEnlace(aux.getEnlace().getEnlace());
            }
        }
        
        if(exito)longitud--;
        
        return exito;
    }

    //  Devuelve el elemento de la posición pos. La precondición es que la posición sea válida.
    public Object recuperar(int pos){
        
        Object resultado;

            //  Nodo auxiliar para recorrer la estructura
            Nodo aux = cabecera;

            //  Recorre hasta encontrar el Nodo en la posicion deseada
            for(int i = 1; i < pos; i++){
                aux = aux.getEnlace();
            }
            
            resultado = aux.getElem();
        
        //  Y devuelve su elemento
        return resultado;
    }

    //  Devuelve la posición en la que se encuentra la primera ocurrencia de elem dentro de la lista. En caso
    //      de no encontrarlo devuelve -1.
    public int localizar(Object elem){
        
        //  Variable resultado que se retornara al final
        int resultado = -1;
        
        //  Nodo auxiliar para recorrer la estructura
        Nodo aux = cabecera;
        //  Variable para saber en que posicion estamos
        int posAux = 1;
        
        //  Recorre la estructura hasta llegar al final o encontrar el elemento buscado
        while(posAux <= this.longitud && resultado == -1){
            //  Si es el elemento buscado
            if(aux.getElem().equals(elem)){
                //  Asigna su posicion a resultado
                resultado = posAux;
            } else {
                //  Si no es, avanza al siguiente Nodo y aumenta la posicion
                aux = aux.getEnlace();
                posAux++;
            }
        }
            
        return resultado;
    }

    //  Quita todos los elementos de la lista. El manejo de memoria es similar al explicado anteriormente para
    //      Cola y Pila dinámicas.
    public void vaciar(){
        //  Setear la cabecera a null es suficiente para vaciarla, no se necesitan borrar
        //      los nodos gracias al Garbage Collector de Java.
        this.cabecera = null;
    }

    //  Devuelve verdadero si la lista no tiene elementos y falso en caso contrario.
    public boolean esVacia(){
        //  Si la cabecera es null, entonces está vacía
        return this.cabecera == null;
    }
    
    //  Devuelve la cantidad de elementos de la lista.
    public int longitud(){
        return this.longitud;
    }
    
    //  Devuelve una copia exacta de los datos en la estructura original, y respetando el orden de los mismos,
    //      en otra estructura del mismo tipo
    @Override
    public Lista clone(){
        
        //  Nueva Lista que se retorna al final
        Lista resultado = new Lista();
        
        //  Variables para recorrer la estructura
        Nodo aux = cabecera;
        int posAux = 1;
        
        //  Recorre toda la estructura
        while(aux != null){
            //  Agrega el elemento del nodo a la lista en la posicion correspondiente
            resultado.insertar(aux.getElem(), posAux);
            //  Aumenta su posicion y pasa al siguiente
            posAux++;
            aux = aux.getEnlace();
        }
        
        return resultado;
    }

    //  Crea y devuelve una cadena de caracteres formada por todos los elementos de la lista para poder
    //      mostrarla por pantalla. Es recomendable utilizar este método únicamente en la etapa de prueba y luego
    //      comentar el código.
    @Override
    public String toString(){
        
        //  Variable que guarda el resultado
        String resultado;
        
        //  Caso especia: Si la lista esta vacía
        if(this.esVacia()){
            resultado = "Lista vacia";
        } else {
            //  Si no esta vacía, abre el corchete donde iran los elementos
            resultado = "[";
            
            //  Nodo auxiliar para recorrer la estructura
            Nodo aux = cabecera;
            //  Recorre toda la estructura
            while(aux != null){
                //  Agrega el elemento al String
                resultado += aux.getElem().toString();
                //  Avanza al siguiente nodo
                aux = aux.getEnlace();
                //  Si no era el ultimo, agrega una coma.
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
