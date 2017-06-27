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
public class Frase {

    public String texto, texto_pre_processado;
    public Relacao_Dependencia[] relacoes;
    public Nodo[] nodos;
    public Oracao[] oracoes;

    public Frase(String texto, Relacao_Dependencia[] relacoes) {
        this.texto = texto;
        this.relacoes = relacoes;
    }

    @Override
    public String toString() {
        return "Frase{" + "texto=" + texto + ", relacoes=" + Arrays.toString(relacoes) + ", nodos=" + Arrays.toString(nodos) + ", oracoes=" + Arrays.toString(oracoes) + '}';
    }

    
}
