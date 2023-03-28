package com.mycompany.aeropuerto.utiles;

public class ArbolAVL {
    
    private NodoAVL raiz;
    
    //  Crea un árbol sin elementos.
    public ArbolAVL(){
        raiz = null;
    }
    
    //  Recibe un elemento y lo agrega en el árbol de manera ordenada. Si el elemento ya se encuentra
    //      en el árbol no se realiza la inserción. Devuelve verdadero si el elemento se agrega a la estructura y
    //      falso en caso contrario.
    public boolean insertar(Comparable elemento){
        
        boolean exito = true;
        
        //  Si el arbol esta vacio
        if(this.esVacio()){
            //  Pone el nuevo elemento en la nueva raiz
            raiz = new NodoAVL(elemento);
        } else {
            //  Variable para salir del while
            boolean salir = false;
            //  Variable para recorrer la estructura
            NodoAVL aux = raiz;
            //  Pila donde se guardaran los nodos para verificar el balance y altura a la vuelta
            Pila pilaAuxiliar = new Pila();
            //  En caso de que no este vacio, se ingresa a un while para buscar la posicion adecuada
            while(!salir){
                //  Si el elemento del nodo actual es igual al que se desea ingresar
                if(aux.getElem().compareTo(elemento) == 0){
                    //  Da error, no acepta valores duplicados
                    exito = false;
                    salir = true;
                } else {
                    //  Si es menor el buscado al del nodo actual
                    if(elemento.compareTo(aux.getElem()) < 0){
                        if(aux.getIzquierdo() != null){
                            //  Si existe, se apila el nodo y se busca por su hijo izquierdo
                            pilaAuxiliar.apilar(aux);
                            aux = aux.getIzquierdo();
                        } else {
                            //  Si no existe, se crea un nuevo nodo con el elemento como hijo derecho
                            aux.setIzquierdo(new NodoAVL(elemento));
                            //  Recalcula la altura con el nuevo hijo
                            aux.recalcularAltura();
                            salir = true;
                        }
                    } else {
                        if(aux.getDerecho() != null){
                            //  Si existe, se apila el nodo y se busca por su hijo derecho
                            pilaAuxiliar.apilar(aux);
                            aux = aux.getDerecho();
                        } else {
                            //  Si no existe, se crea un nuevo nodo con el elemento como hijo derecho
                            aux.setDerecho(new NodoAVL(elemento));
                            //  Recalcula la altura con el nuevo hijo
                            aux.recalcularAltura();
                            salir = true;
                        }
                    }
                }
            }
            if(exito){
                NodoAVL padreAux;
                NodoAVL nuevaRaizSubarbol;
                while(!pilaAuxiliar.esVacia()){
                    padreAux = (NodoAVL) pilaAuxiliar.obtenerTope();
                    pilaAuxiliar.desapilar();
                    nuevaRaizSubarbol = verificarBalance(aux);
                    if(nuevaRaizSubarbol != null){
                        if(nuevaRaizSubarbol.getElem().compareTo(padreAux.getElem()) > 0){
                            padreAux.setDerecho(nuevaRaizSubarbol);
                        } else {
                            padreAux.setIzquierdo(nuevaRaizSubarbol);
                        }
                    }
                    padreAux.recalcularAltura();
                    aux = padreAux;
                }
                
                nuevaRaizSubarbol = verificarBalance(raiz);
                if(nuevaRaizSubarbol != null){
                    raiz = nuevaRaizSubarbol;
                }
            }
        }
        
        
        
        return exito;
    }

    //  Recibe el elemento que se desea eliminar y se procede a removerlo del árbol. Si no se encuentra
    //      el elemento no se puede realizar la eliminación. Devuelve verdadero si el elemento se elimina de la
    //      estructura y falso en caso contrario.
    public boolean eliminar(Comparable elemento){
        
        boolean exito = true;
        
        //  Si esta vacio, no hay nada que eliminar, se devuelve que no hubo exito
        if(this.esVacio()){
            exito = false;
        } else {
            //  Si la raiz no tiene el elemento que buscamos
            if(raiz.getElem().compareTo(elemento) != 0){
                //  Llamamos a la funcion privada auxiliar elminarAux
                exito = eliminarAux(elemento, raiz);
                if(exito == true){
                    NodoAVL nuevaRaiz = verificarBalance(raiz);
                    if(nuevaRaiz != null){
                        raiz = nuevaRaiz;
                    }
                }
            } else {
                //  Si la raiz tiene el elemento que buscamos, se fija que caso es
                if(raiz.getIzquierdo() != null){
                    if(raiz.getDerecho() != null){
                        //  Caso 3, tiene ambos hijos
                        //  Para este caso existe una funcion privada llamada elminarCaso3
                        eliminarCaso3(raiz);
                        NodoAVL nuevaRaiz = verificarBalance(raiz);
                        if(nuevaRaiz != null)raiz = nuevaRaiz;
                    } else {
                        //  Caso 2, tiene un hijo izquierdo nomas
                        raiz = raiz.getIzquierdo();
                    }
                } else {
                    if(raiz.getDerecho() != null){
                        //  Caso 2, tiene un hijo derecho nomas
                        raiz = raiz.getDerecho();
                    } else {
                        //  Caso 1, no tiene hijos, arbol de un solo elemento, lo vacía
                        raiz = null;
                    }
                }
            } 
        }
        
        return exito;
    }
    
    //  Funcion privada auxiliar para el metodo eliminar()
    //  Precondicion: Que el nodo pasado como paramentro no tenga el elemento buscado, ni sea nulo
    private boolean eliminarAux(Comparable elemento, NodoAVL nodo){
        
        boolean exito = true;
        
        //  Si el valor del elemento que buscamos es menor al elemenoto del nodo por parametro
        if(elemento.compareTo(nodo.getElem()) < 0){
            //  Significa que debemos buscar por la izquierda
            NodoAVL aux = nodo.getIzquierdo();
            //  Si no hay nada a la izquierda, no existe el elemento en el arbol, se devuelve que no hubo exito
            if(aux == null){
                exito = false;
            } else {
                //  Si hay algo a la izquierda, verifica si es el elemento buscado
                if(aux.getElem().compareTo(elemento) != 0){
                    //  De no serlo, busca por izquierda con el mismo metodo recursivo
                    exito = eliminarAux(elemento, aux);
                    aux.recalcularAltura();
                    if(exito == true){
                        NodoAVL nuevaRaizSubarbol = verificarBalance(aux);
                        if(nuevaRaizSubarbol != null){
                            nodo.setIzquierdo(nuevaRaizSubarbol);
                        }
                        nodo.recalcularAltura();
                    }
                } else {
                    //  Si lo es, verifica en que caso de eliminacion se encuentra y actua acorde
                    if(aux.getIzquierdo() != null){
                        if(aux.getDerecho() != null){
                            //  Caso 3, tiene ambos hijos, consigue candidato izquierdo, el mayor de los menores
                            eliminarCaso3(aux);
                            NodoAVL nuevaRaizSubarbol = verificarBalance(aux);
                            if(nuevaRaizSubarbol != null){
                                nodo.setIzquierdo(nuevaRaizSubarbol);
                            }
                            nodo.recalcularAltura();
                        } else {
                            //  Caso 2, hijo izquierdo
                            nodo.setIzquierdo(aux.getIzquierdo());
                            aux.getIzquierdo().recalcularAltura();
                        }
                    } else {
                        if(aux.getDerecho() != null){
                            //  Caso 2, hijo derecho
                            nodo.setIzquierdo(aux.getDerecho());
                            aux.getDerecho().recalcularAltura();
                        } else {
                            //  Caso 1, sin hijos
                            nodo.setIzquierdo(null);
                        }
                    }
                }
            } 
        } else {
            //  Si no es menor, sabemos que no es mayor (Ya que la precondicion es que no sea igual), usa la misma logica 
            //      que para revisar por izquierda
            NodoAVL aux = nodo.getDerecho();
            if(aux == null){
                exito = false;
            } else {
                if(aux.getElem().compareTo(elemento) != 0){
                    exito = eliminarAux(elemento, aux); 
                    if(exito == true){
                        NodoAVL nuevaRaizSubarbol = verificarBalance(aux);
                        if(nuevaRaizSubarbol != null){
                            nodo.setDerecho(nuevaRaizSubarbol);
                        }
                        nodo.recalcularAltura();
                    }
                } else {
                    if(aux.getIzquierdo() != null){
                        if(aux.getDerecho() != null){
                            //  Caso 3, tiene ambos hijos, consigue candidato izquierdo, el mayor de los menores
                            eliminarCaso3(aux);
                            NodoAVL nuevaRaizSubarbol = verificarBalance(aux);
                            if(nuevaRaizSubarbol != null){
                                nodo.setDerecho(nuevaRaizSubarbol);
                            }
                            nodo.recalcularAltura();
                        } else {
                            //  Caso 2, hijo izquierdo
                            nodo.setDerecho(aux.getIzquierdo());
                        }
                    } else {
                        if(aux.getDerecho() != null){
                            //  Caso 2, hijo derecho
                            nodo.setDerecho(aux.getDerecho());
                        } else {
                            //  Caso 1, sin hijos
                            nodo.setDerecho(null);
                        }
                    }
                }
            } 
        }
        
        return exito;
    }
    
    //  Funcion privada para realizar el algoritmo de eliminacion de caso 3, el caso en el que
    //      el nodo a ser eliminado tiene hijo izquierdo y derecho.
    private void eliminarCaso3(NodoAVL nodo){
        //  Caso 3, tiene ambos hijos, consigue candidato izquierdo, el mayor de los menores
        NodoAVL aux = nodo.getIzquierdo();
        //  Si es el primero a la izquierda
        if(aux.getDerecho() == null){
            //  Copia su elemento
            nodo.setElem(aux.getElem());
            //  En caso de tener hijo izquierdo
            if(aux.getIzquierdo() != null){
                //  Caso 2, hijo izquierdo
                nodo.setIzquierdo(aux.getIzquierdo());
            } else {
                //  Caso 1, sin hijos
                nodo.setIzquierdo(null);
            }
            nodo.recalcularAltura();
        } else {
            Pila pilaAuxiliar = new Pila();
            //  Si no es el primero a la izquierda, busca el padre del mayor de los menores
            while(aux.getDerecho().getDerecho() != null){
                pilaAuxiliar.apilar(aux);
                aux = aux.getDerecho();
            }
            //  Copia el elemento del mayor
            nodo.setElem(aux.getDerecho().getElem());
            //  En caso de tener hijo izquierdo
            if(aux.getDerecho().getIzquierdo() != null){
                //  Caso 2, hijo izquierdo
                aux.setDerecho(aux.getDerecho().getIzquierdo());
            } else {
                //  Caso 1, sin hijos
                aux.setDerecho(null);
            }
            aux.recalcularAltura();
            NodoAVL padreAuxiliar;
            NodoAVL nuevaRaizSubarbol;
            while(!pilaAuxiliar.esVacia()){
                padreAuxiliar = (NodoAVL) pilaAuxiliar.obtenerTope();
                nuevaRaizSubarbol = verificarBalance(aux);
                if(nuevaRaizSubarbol != null){
                    padreAuxiliar.setDerecho(nuevaRaizSubarbol);
                }
                padreAuxiliar.recalcularAltura();
                aux = padreAuxiliar;
                pilaAuxiliar.desapilar();
            }
            nuevaRaizSubarbol = verificarBalance(aux);
            if(nuevaRaizSubarbol != null){
                nodo.setDerecho(nuevaRaizSubarbol);
            }
            nodo.recalcularAltura();
        }
    }
    
    private NodoAVL verificarBalance(NodoAVL nodo){
        
        NodoAVL nuevaRaiz = null;
        int balance = getBalance(nodo);
        
        if(balance > 1){
            //  Nodo desbalanceado a izquierda
            if(getBalance(nodo.getIzquierdo()) == -1){
                //  Hijo desbalanceado en sentido contrario, se necesita rotacion a izquierda con hijo
                //      izquierdo de pivot
                nodo.setIzquierdo(rotarIzquierda(nodo.getIzquierdo()));
            }
            //  Compartan o no sentido de desbalance nodo e hijo, se debe rotar a derecha con nodo de pivot
            nuevaRaiz = rotarDerecha(nodo);
        } else {
            if(balance < -1){
                //  Nodo desbalanceado a derecha
                if(getBalance(nodo.getDerecho()) == 1){
                    //  Hijo desbalanceado en sentido contrario, se necesita rotacion a derecha con
                    //      hijo derecho como pivot
                    nodo.setDerecho(rotarDerecha(nodo.getDerecho()));
                }
                //  Compartan o no sentido de desbalance nodo e hijo, se debe rotar a izquierda con nodo de pivot
                nuevaRaiz = rotarIzquierda(nodo);
            }
        }
        //  Si no esta desbalanceado, no se hace nada.
        return nuevaRaiz;
    }
    
    private int getBalance(NodoAVL nodo){
        
        int alturaIzq, alturaDer;
        
        if(nodo.getIzquierdo() == null){
            alturaIzq = -1;
        } else {
            alturaIzq = nodo.getIzquierdo().getAltura();
        }
        
        if(nodo.getDerecho() == null){
            alturaDer = -1;
        } else {
            alturaDer = nodo.getDerecho().getAltura();
        }
        
        return alturaIzq - alturaDer;
    }
    
    private NodoAVL rotarDerecha(NodoAVL pivot){
        
        NodoAVL hijo = pivot.getIzquierdo();
        NodoAVL aux = hijo.getDerecho();
        hijo.setDerecho(pivot);
        pivot.setIzquierdo(aux);
        pivot.recalcularAltura();
        hijo.recalcularAltura();
        return hijo;
    }
    
    private NodoAVL rotarIzquierda(NodoAVL pivot){
        
        NodoAVL hijo = pivot.getDerecho();
        NodoAVL aux = hijo.getIzquierdo();
        hijo.setIzquierdo(pivot);
        pivot.setDerecho(aux);
        pivot.recalcularAltura();
        hijo.recalcularAltura();
        return hijo;
    }

    //  Devuelve verdadero si el elemento recibido por parámetro está en el árbol y falso en caso contrario.
    public boolean pertenece(Comparable elemento){
        return perteneceAux(elemento, raiz);
    }
    
    //  Funcion privada para pertenece()
    private boolean perteneceAux(Comparable elemento, NodoAVL nodo){
        
        boolean esta;
        
        //  Si el nodo no existe
        if(nodo == null){
            //  Significa que el elemento no esta en el arbol.
            esta = false;
        } else {
            //  Si el nodo contiene el elemento buscado
            if(nodo.getElem().compareTo(elemento) == 0){
                //  Devolvemos que pertenece
                esta = true;
            } else {
                //  En caso que no lo contenga, revisamos el nodo izquierdo o derecho,
                //      dependiendo de si es mayor o menor.
                if(nodo.getElem().compareTo(elemento) < 0){
                    esta = perteneceAux(elemento, nodo.getDerecho());
                } else {
                    esta = perteneceAux(elemento, nodo.getIzquierdo());
                }
            }
        }
        
        return esta;
    }

    //  Devuelve falso si hay al menos un elemento en el árbol y verdadero en caso contrario.
    public boolean esVacio(){
        return raiz == null;
    }
        
    //  Recorre el árbol completo y devuelve una lista ordenada con los elementos que se encuentran
    //      almacenados en él.
    public Lista listar(){
        Lista resultado = new Lista();
        listarAux(raiz, resultado);
        return resultado;
    }

    //  Funciona privada para llenar la lista, la llena en Inorden, ya que de esta forma quedan de menor a mayor.
    private void listarAux(NodoAVL nodo, Lista list){
        
        //  Si el nodo existe
        if(nodo != null){
            //  Llena la lista en Inorden
            listarAux(nodo.getIzquierdo(), list);
            list.insertar(nodo.getElem(), list.longitud() + 1);
            listarAux(nodo.getDerecho(), list);
        } 
    }
    
    //  Recorre parte del árbol (sólo lo necesario) y devuelve una lista ordenada con los elementos que
    //      se encuentran en el intervalo [elemMinimo, elemMaximo].
    public Lista listarRango(Comparable elemMinimo, Comparable elemMaximo){
        Lista resultado = new Lista();
        listarRangoAux(elemMinimo, elemMaximo, raiz, resultado);
        return resultado;
    }
    
    //  Funcion privada para listarRango(), inserta los elementos que se encuentran en el intervalo en inorden (De menor a mayor)
    private void listarRangoAux(Comparable elemMinimo, Comparable elemMaximo, NodoAVL nodo, Lista list){
        
        if(nodo != null){
            //  Variable para referenciar al elemento del nodo facilmente
            Comparable aux = nodo.getElem();
            //  Si el elemento del nodo es menor o igual al limite inferior del intervalo, no 
            //      revisamos el hijo izquierdo, ya que todos sus elementos serán menores.
            if(aux.compareTo(elemMinimo) > 0){
                //  De no ser menor, revisamos el subarbol del hijo izquierdo
                listarRangoAux(elemMinimo, elemMaximo, nodo.getIzquierdo(), list);
            }
            //  Si el elemento del nodo se encuentra dentro del intervalo
            if(aux.compareTo(elemMinimo) >= 0 && aux.compareTo(elemMaximo) <= 0){
                //  Lo insertamos al final de la lista
                list.insertar(aux, list.longitud() + 1);
            }
            //  Si el elemento del nodo es mayor o igual al limite superior del intervalo, no 
            //      revisamos el hijo derecho, ya que todos sus elementos serán mayores.
            if(aux.compareTo(elemMaximo) < 0){
                //  De no ser mayor, revisamos el subarbol del hijo derecho
                listarRangoAux(elemMinimo, elemMaximo, nodo.getDerecho(), list);
            }
        }
    }

    //  Recorre la rama correspondiente y devuelve el elemento más pequeño almacenado en el árbol.
    public Comparable minimoElem(){
        
        Comparable resultado = null;
        
        if(!this.esVacio()){
            NodoAVL aux = raiz;
            while(aux.getIzquierdo() != null){
                aux = aux.getIzquierdo();
            }
            resultado = aux.getElem();
        }
        
        return resultado;
    }

    //  Recorre la rama correspondiente y devuelve el elemento más grande almacenado en el árbol
    public Comparable maximoElem(){
        
        Comparable resultado = null;
        
        if(!this.esVacio()){
            NodoAVL aux = raiz;
            while(aux.getDerecho() != null){
                aux = aux.getDerecho();
            }
            resultado = aux.getElem();
        }
        
        return resultado;
    }
    
    //      EXTRAS
    
    public void vaciar(){
        raiz = null;
    }
    
    //  Devuelve una copia del arbol, para clonarlo recorre la estructura en orden 
    //      por niveles, de esta forma, al ir insertando nodos en el clon, no será
    //      necesario hacer un rebalance, siendo la manera mas efectiva.
    @Override
    public ArbolAVL clone(){
        
        ArbolAVL resultado = new ArbolAVL();
        
        //  Si no esta vacía
        if(!this.esVacio()){
            
            //  Cola para ir almacenando los nodos
            Cola colaAuxiliar = new Cola();
            //  auxiliar para recorrer la estructura
            NodoAVL nodoAux;

            //  Empiezo poniendo la raiz en la cola
            colaAuxiliar.poner(raiz);

            //  Mientras la cola no este vacía
            while(!colaAuxiliar.esVacia()){
                //  Consigo el frente de la cola y lo saco
                nodoAux = (NodoAVL) colaAuxiliar.obtenerFrente();
                colaAuxiliar.sacar();
                //  Luego inserto ese elemento al final de la lista.
                resultado.insertar(nodoAux.getElem());
                //  Y en caso de tener hijos los pongo en la cola
                if(nodoAux.getIzquierdo() != null) colaAuxiliar.poner(nodoAux.getIzquierdo());
                if(nodoAux.getDerecho() != null) colaAuxiliar.poner(nodoAux.getDerecho());
            }
        }
        
        return resultado;
    }
    
    //  Devuelve un String con los elementos del arbol, en orden por niveles para mas facil lectura.
    @Override
    public String toString(){  
        
        String resultado = "Arbol Vacio";
        
        if(!this.esVacio()){
            resultado = "";
            //  Cola para ir almacenando los nodos
            Cola colaAuxiliar = new Cola();
            //  auxiliar para recorrer la estructura
            NodoAVL nodoAux;

            //  Empiezo poniendo la raiz en la cola
            colaAuxiliar.poner(raiz);

            //  Mientras la cola no este vacía
            while(!colaAuxiliar.esVacia()){
                //  Consigo el frente de la cola y lo saco
                nodoAux = (NodoAVL) colaAuxiliar.obtenerFrente();
                colaAuxiliar.sacar();
                
                //  Variables para referencias mas facilmente a los hijos, ademas de no tener que llamar a la funcion multiples veces
                NodoAVL izquierdo = nodoAux.getIzquierdo();
                NodoAVL derecho = nodoAux.getDerecho();
                
                //  Luego agrego el elemento al String
                resultado += "\n" + nodoAux.getElem().toString() + " --> ";
            
                //  Si el izquierdo existe
                if(izquierdo != null){
                    //  Agrega su elemento al String
                    resultado += izquierdo.getElem().toString();
                }
                
                //  Coma para separar elementos o saber cual es hijo izquierdo y derecho
                resultado += " , ";

                //  Si el derecho existe, lo agrega
                if(derecho != null){
                    resultado += derecho.getElem().toString();
                }
                
                //  Y en caso de tener hijos los pongo en la cola
                if(izquierdo != null) colaAuxiliar.poner(nodoAux.getIzquierdo());
                if(derecho != null) colaAuxiliar.poner(nodoAux.getDerecho());
            }
        }
        
        return resultado;
    }
}
