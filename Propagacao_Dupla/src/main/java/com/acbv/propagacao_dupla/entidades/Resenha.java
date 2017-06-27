/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.entidades;

import java.util.Arrays;

/**
 *
 * @author Arthur
 */
public class Resenha {
    
    public Frase[] frases;
    public String texto, texto_pre_processado;

    public Resenha(Frase[] frases, String texto) {
        this.frases = frases;
        this.texto = texto;
    }

    @Override
    public String toString() {
        return "Resenha{" + "frases=" + Arrays.toString(frases) + ", texto=" + texto + '}';
    }
    
    
}
