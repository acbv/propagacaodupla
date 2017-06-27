/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.entidades;

import java.util.Objects;

/**
 *
 * @author Arthur
 */
public class Nodo {
    
    public int indice;
    public String texto;
    public Categoria_Sintatica categoria;
    public String lemma;

    public Nodo(String texto, Categoria_Sintatica categoria) {
        this.texto = texto;
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Nodo{" + "texto=" + texto + ", categoria=" + categoria + ", lemma=" + lemma + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Nodo other = (Nodo) obj;
        if (this.indice != other.indice) {
            return false;
        }
        if (!Objects.equals(this.texto, other.texto)) {
            return false;
        }
        if (!Objects.equals(this.lemma, other.lemma)) {
            return false;
        }
        if (this.categoria != other.categoria) {
            return false;
        }
        return true;
    }

    
    
}
