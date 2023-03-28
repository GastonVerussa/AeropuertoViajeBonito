package com.mycompany.aeropuerto.utiles;

public class DiccionarioAVL {
    
    private NodoAVLDicc raiz;
    
    // crea una estructura sin elementos.
    public DiccionarioAVL(){
        this.raiz = null;
    }
    
    //  Recibe la clave que es única y el dato (información asociada a ella). Si no existe en la estructura
    //      un elemento con igual clave, agrega el par (clave, dato) a la estructura. Si la operación termina con
    //      éxito devuelve verdadero y falso en caso contrario.
    public boolean insertar(Comparable clave,Object dato){
        boolean exito = true;
        
        //  Si el diccionario esta vacio
        if(this.esVacio()){
            //  Pone el nuevo elemento en la nueva raiz
            raiz = new NodoAVLDicc(clave, dato);
        } else {
            //  Variable para salir del while
            boolean salir = false;
            //  Variable para recorrer la estructura
            NodoAVLDicc aux = raiz;
            //  Pila donde se guardaran los nodos para verificar el balance y altura a la vuelta
            Pila pilaAuxiliar = new Pila();
            //  En caso de que no este vacio, se ingresa a un while para buscar la posicion adecuada
            while(!salir){
                //  Si la clave del nodo actual es igual al que se desea ingresar
                if(aux.getClave().compareTo(clave) == 0){
                    //  Da error, las claves deben ser unicas
                    exito = false;
                    salir = true;
                } else {
                    //  Si es menor el buscado al del nodo actual
                    if(clave.compareTo(aux.getClave()) < 0){
                        if(aux.getIzquierdo() != null){
                            //  Si existe, se apila el nodo y se busca por su hijo izquierdo
                            pilaAuxiliar.apilar(aux);
                            aux = aux.getIzquierdo();
                        } else {
                            //  Si no existe, se crea un nuevo nodo con la clave y dato como hijo derecho
                            aux.setIzquierdo(new NodoAVLDicc(clave, dato));
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
                            //  Si no existe, se crea un nuevo nodo con la clave y el dato como hijo derecho
                            aux.setDerecho(new NodoAVLDicc(clave, dato));
                            //  Recalcula la altura con el nuevo hijo
                            aux.recalcularAltura();
                            salir = true;
                        }
                    }
                }
            }
            if(exito){
                NodoAVLDicc padreAux;
                NodoAVLDicc nuevaRaizSubarbol;
                while(!pilaAuxiliar.esVacia()){
                    padreAux = (NodoAVLDicc) pilaAuxiliar.obtenerTope();
                    pilaAuxiliar.desapilar();
                    nuevaRaizSubarbol = verificarBalance(aux);
                    if(nuevaRaizSubarbol != null){
                        if(nuevaRaizSubarbol.getClave().compareTo(padreAux.getClave()) > 0){
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
    
    //  Elimina el elemento cuya clave sea la recibida por parámetro. Si lo encuentra y la operación de
    //      eliminación termina con éxito devuelve verdadero y falso en caso contrario.
    public boolean eliminar(Comparable clave){
        
        boolean exito = true;
        
        //  Si esta vacio, no hay nada que eliminar, se devuelve que no hubo exito
        if(this.esVacio()){
            exito = false;
        } else {
            //  Si la raiz no tiene la clave que buscamos
            if(raiz.getClave().compareTo(clave) != 0){
                //  Llamamos a la funcion privada auxiliar elminarAux
                exito = eliminarAux(clave, raiz);
                if(exito == true){
                    NodoAVLDicc nuevaRaiz = verificarBalance(raiz);
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
                        NodoAVLDicc nuevaRaiz = verificarBalance(raiz);
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
    //  Precondicion: Que el nodo pasado como paramentro no tenga la clave buscado, ni sea nulo
    private boolean eliminarAux(Comparable clave, NodoAVLDicc nodo){
        
        boolean exito = true;
        
        //  Si el valor de la clave que buscamos es menor la clave del nodo por parametro
        if(clave.compareTo(nodo.getClave()) < 0){
            //  Significa que debemos buscar por la izquierda
            NodoAVLDicc aux = nodo.getIzquierdo();
            //  Si no hay nada a la izquierda, no existe el elemento con la clave en el arbol, se devuelve que no hubo exito
            if(aux == null){
                exito = false;
            } else {
                //  Si hay algo a la izquierda, verifica si es la clave buscado
                if(aux.getClave().compareTo(clave) != 0){
                    //  De no serlo, busca por izquierda con el mismo metodo recursivo
                    exito = eliminarAux(clave, aux);
                    aux.recalcularAltura();
                    if(exito == true){
                        NodoAVLDicc nuevaRaizSubarbol = verificarBalance(aux);
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
                            NodoAVLDicc nuevaRaizSubarbol = verificarBalance(aux);
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
            NodoAVLDicc aux = nodo.getDerecho();
            if(aux == null){
                exito = false;
            } else {
                if(aux.getClave().compareTo(clave) != 0){
                    exito = eliminarAux(clave, aux); 
                    if(exito == true){
                        NodoAVLDicc nuevaRaizSubarbol = verificarBalance(aux);
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
                            NodoAVLDicc nuevaRaizSubarbol = verificarBalance(aux);
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
    private void eliminarCaso3(NodoAVLDicc nodo){
        //  Caso 3, tiene ambos hijos, consigue candidato izquierdo, el mayor de los menores
        NodoAVLDicc aux = nodo.getIzquierdo();
        //  Si es el primero a la izquierda
        if(aux.getDerecho() == null){
            //  Copia su clave y dato
            nodo.setClave(aux.getClave());
            nodo.setDato(aux.getDato());
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
            //  Copia la clave y dato del mayor
            nodo.setClave(aux.getDerecho().getClave());
            //  En caso de tener hijo izquierdo
            if(aux.getDerecho().getIzquierdo() != null){
                //  Caso 2, hijo izquierdo
                aux.setDerecho(aux.getDerecho().getIzquierdo());
            } else {
                //  Caso 1, sin hijos
                aux.setDerecho(null);
            }
            aux.recalcularAltura();
            NodoAVLDicc padreAuxiliar;
            NodoAVLDicc nuevaRaizSubarbol;
            while(!pilaAuxiliar.esVacia()){
                padreAuxiliar = (NodoAVLDicc) pilaAuxiliar.obtenerTope();
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
    
    private NodoAVLDicc verificarBalance(NodoAVLDicc nodo){
        
        NodoAVLDicc nuevaRaiz = null;
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
    
    private int getBalance(NodoAVLDicc nodo){
        
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
    
    private NodoAVLDicc rotarDerecha(NodoAVLDicc pivot){
        
        NodoAVLDicc hijo = pivot.getIzquierdo();
        NodoAVLDicc aux = hijo.getDerecho();
        hijo.setDerecho(pivot);
        pivot.setIzquierdo(aux);
        pivot.recalcularAltura();
        hijo.recalcularAltura();
        return hijo;
    }
    
    private NodoAVLDicc rotarIzquierda(NodoAVLDicc pivot){
        
        NodoAVLDicc hijo = pivot.getDerecho();
        NodoAVLDicc aux = hijo.getIzquierdo();
        hijo.setIzquierdo(pivot);
        pivot.setDerecho(aux);
        pivot.recalcularAltura();
        hijo.recalcularAltura();
        return hijo;
    }
    
    //  Devuelve verdadero si en la estructura se encuentra almacenado un elemento con la clave recibida
    //      por parámetro, caso contrario devuelve falso
    public boolean existeClave(Comparable clave){
        return existeClaveAux(clave, raiz);
    }
    
    //  Funcion privada para pertenece()
    private boolean existeClaveAux(Comparable clave, NodoAVLDicc nodo){
        
        boolean esta;
        
        //  Si el nodo no existe
        if(nodo == null){
            //  Significa que el elemento no esta en el arbol.
            esta = false;
        } else {
            //  Si el nodo contiene el elemento buscado
            if(nodo.getClave().compareTo(clave) == 0){
                //  Devolvemos que existe
                esta = true;
            } else {
                //  En caso que no lo contenga, revisamos el nodo izquierdo o derecho,
                //      dependiendo de si es mayor o menor.
                if(nodo.getClave().compareTo(clave) < 0){
                    esta = existeClaveAux(clave, nodo.getDerecho());
                } else {
                    esta = existeClaveAux(clave, nodo.getIzquierdo());
                }
            }
        }
        
        return esta;
    }
    
    //  Si en la estructura se encuentra almacenado un elemento con la clave recibida por parámetro,
    //      devuelve la información asociada a ella. Precondición: si no existe un elemento con esa clave no se
    //      puede asegurar el funcionamiento de la operación.
    public Object obtenerInformacion(Comparable clave){
        return obtenerInformacionAux(clave, raiz);
    }
    
    private Object obtenerInformacionAux(Comparable clave, NodoAVLDicc nodo){
        
        Object resultado = null;
        
        //  Si el nodo no existe
        if(nodo != null){
            //  Si el nodo contiene el elemento buscado
            if(nodo.getClave().compareTo(clave) == 0){
                //  Devolvemos que existe
                resultado = nodo.getDato();
            } else {
                //  En caso que no lo contenga, revisamos el nodo izquierdo o derecho,
                //      dependiendo de si es mayor o menor.
                if(nodo.getClave().compareTo(clave) < 0){
                    resultado = obtenerInformacionAux(clave, nodo.getDerecho());
                } else {
                    resultado = obtenerInformacionAux(clave, nodo.getIzquierdo());
                }
            }
        }
        
        return resultado;
    }
    
    //  Recorre la estructura completa y devuelve una lista ordenada con las claves de los elementos que
    //      se encuentran almacenados en ella.
    public Lista listarClaves(){
        Lista resultado = new Lista();
        listarClavesAux(raiz, resultado);
        return resultado;
    }
    

    //  Funciona privada para llenar la lista, la llena en Inorden, ya que de esta forma quedan de menor a mayor.
    private void listarClavesAux(NodoAVLDicc nodo, Lista list){
        
        //  Si el nodo existe
        if(nodo != null){
            //  Llena la lista en Inorden
            listarClavesAux(nodo.getIzquierdo(), list);
            list.insertar(nodo.getClave(), list.longitud() + 1);
            listarClavesAux(nodo.getDerecho(), list);
        } 
    }
    
    //  Recorre la estructura completa y devuelve una lista ordenada con la información asociada de los
    //      elementos que se encuentran almacenados en ella.
    public Lista listarDatos(){
        Lista resultado = new Lista();
        listarDatosAux(raiz, resultado);
        return resultado;
    }
    
    
    //  Funciona privada para llenar la lista, la llena en Inorden, ya que de esta forma quedan de menor a mayor.
    private void listarDatosAux(NodoAVLDicc nodo, Lista list){
        
        //  Si el nodo existe
        if(nodo != null){
            //  Llena la lista en Inorden
            listarDatosAux(nodo.getIzquierdo(), list);
            list.insertar(nodo.getDato(), list.longitud() + 1);
            listarDatosAux(nodo.getDerecho(), list);
        } 
    }
    
    // devuelve falso si hay al menos un elemento cargado en la estructura y verdadero en caso contrario
    public boolean esVacio(){
        return this.raiz == null;
    }
    
    public void vaciar(){
        this.raiz = null;
    }
    
    //  Devuelve una copia del arbol, para clonarlo recorre la estructura en orden 
    //      por niveles, de esta forma, al ir insertando nodos en el clon, no será
    //      necesario hacer un rebalance, siendo la manera mas efectiva.
    @Override
    public DiccionarioAVL clone(){
        
        DiccionarioAVL resultado = new DiccionarioAVL();
        
        //  Si no esta vacía
        if(!this.esVacio()){
            
            //  Cola para ir almacenando los nodos
            Cola colaAuxiliar = new Cola();
            //  auxiliar para recorrer la estructura
            NodoAVLDicc nodoAux;

            //  Empiezo poniendo la raiz en la cola
            colaAuxiliar.poner(raiz);

            //  Mientras la cola no este vacía
            while(!colaAuxiliar.esVacia()){
                //  Consigo el frente de la cola y lo saco
                nodoAux = (NodoAVLDicc) colaAuxiliar.obtenerFrente();
                colaAuxiliar.sacar();
                //  Luego inserto ese elemento al final de la lista.
                resultado.insertar(nodoAux.getClave(), nodoAux.getDato());
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
            NodoAVLDicc nodoAux;

            //  Empiezo poniendo la raiz en la cola
            colaAuxiliar.poner(raiz);

            //  Mientras la cola no este vacía
            while(!colaAuxiliar.esVacia()){
                //  Consigo el frente de la cola y lo saco
                nodoAux = (NodoAVLDicc) colaAuxiliar.obtenerFrente();
                colaAuxiliar.sacar();
                
                //  Variables para referencias mas facilmente a los hijos, ademas de no tener que llamar a la funcion multiples veces
                NodoAVLDicc izquierdo = nodoAux.getIzquierdo();
                NodoAVLDicc derecho = nodoAux.getDerecho();
                
                //  Luego agrego el elemento al String
                resultado += "\n {" + nodoAux.getClave().toString() + ", "+ nodoAux.getDato().toString() + "}" + " --> ";
            
                //  Si el izquierdo existe
                if(izquierdo != null){
                    //  Agrega su elemento al String
                    resultado += "{" + izquierdo.getClave().toString() + ", "+ izquierdo.getDato().toString() + "}";
                }
                
                //  Coma para separar elementos o saber cual es hijo izquierdo y derecho
                resultado += " , ";

                //  Si el derecho existe, lo agrega
                if(derecho != null){
                    resultado += "{" + derecho.getClave().toString() + ", "+ derecho.getDato().toString()+ "}";
                }
                
                //  Y en caso de tener hijos los pongo en la cola
                if(izquierdo != null) colaAuxiliar.poner(nodoAux.getIzquierdo());
                if(derecho != null) colaAuxiliar.poner(nodoAux.getDerecho());
            }
        }
        
        return resultado;
    }
}
