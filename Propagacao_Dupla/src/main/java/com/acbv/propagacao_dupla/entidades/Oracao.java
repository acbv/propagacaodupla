/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.entidades;

/**
 *
 * @author arthur
 */
public class Oracao {

    public String texto;
    public Relacao_Dependencia[] relacoes;
    public Nodo[] nodos;

    public Oracao(String texto, Relacao_Dependencia[] relacoes) {
        this.texto = texto;
        this.relacoes = relacoes;
    }
}
