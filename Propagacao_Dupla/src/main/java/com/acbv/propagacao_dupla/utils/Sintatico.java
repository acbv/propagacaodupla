/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.utils;

import com.acbv.propagacao_dupla.entidades.Categoria_Sintatica;

/**
 *
 * @author arthur
 */
public class Sintatico {

    public static boolean isSubstantivo(Categoria_Sintatica categoria) {
        return categoria == Categoria_Sintatica.NN || categoria == Categoria_Sintatica.NNS;
    }

    public static boolean isPreposicao(Categoria_Sintatica categoria) {
        return categoria == Categoria_Sintatica.PREP;
    }

    public static boolean isAdjetivo(Categoria_Sintatica categoria) {
        return categoria == Categoria_Sintatica.JJ || categoria == Categoria_Sintatica.JJR || categoria == Categoria_Sintatica.JJS;
    }
    
}
