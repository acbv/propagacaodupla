/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.entidades;

/**
 *
 * @author Arthur
 */
public class Relacao_Dependencia {
    
    public Nodo governante, dependente;
    public Tipo_Relacao relacao;

    public Relacao_Dependencia(Nodo governante, Nodo dependente, Tipo_Relacao relacao) {
        this.governante = governante;
        this.dependente = dependente;
        this.relacao = relacao;
    }

    @Override
    public String toString() {
        return "Relacao_Dependencia{" + "governante=" + governante + ", dependente=" + dependente + ", relacao=" + relacao + '}';
    }
    
}
