package com.mycompany.aeropuerto.utiles;

public class NodoAVLDicc {
    
    private Comparable clave;
    private Object dato;
    private NodoAVLDicc izquierdo, derecho;
    private int altura;
    
    public NodoAVLDicc(Comparable clave, Object dato){
        this.clave = clave;
        this.dato = dato;
        this.izquierdo = null;
        this.derecho = null;
        this.altura = 0;
    }
    
    public Comparable getClave(){
        return this.clave;
    }
    
    public Object getDato(){
        return this.dato;
    }
    
    public NodoAVLDicc getIzquierdo(){
        return izquierdo;
    }
    
    public NodoAVLDicc getDerecho(){
        return derecho;
    }
    
    public int getAltura(){
        return altura;
    }
    
    public void setClave(Comparable clave){
        this.clave = clave;
    }
    
    public void setDato(Object dato){
        this.dato = dato;
    }
    
    public void setIzquierdo(NodoAVLDicc izquierdo){
        this.izquierdo = izquierdo;
    }
    
    public void setDerecho(NodoAVLDicc derecho){
        this.derecho = derecho;
    }
    
    public void recalcularAltura(){
        
        this.altura = 0;
        
        if(this.izquierdo != null){
            this.altura = this.izquierdo.getAltura() + 1;
        }
        
        if(this.derecho != null){
            if(this.derecho.getAltura() >= this.altura)
                this.altura = this.derecho.getAltura() + 1;
        }
    }
}
