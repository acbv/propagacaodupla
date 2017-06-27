/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.entidades;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author arthur
 */
public class Corpus {
    
    public List<Resenha> resenhas;
    public HashMap<String, Integer> bagOfWords;
    public HashMap<String, Integer> bagOf2Grams;
    public HashMap<String, Integer> bagOf3Grams;
    public HashMap<String, Integer> bagOf4Grams;
    public HashMap<String, Integer> bagOf5Grams;

    public Corpus(List<Resenha> resenhas) {
        this.resenhas = resenhas;
    }
    
}
