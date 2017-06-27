/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.utils;

import ptstemmer.Stemmer;
import ptstemmer.implementations.PorterStemmer;

/**
 *
 * @author arthur
 */
public class StemmerUtils {

    private final Stemmer stemm;

    public StemmerUtils() {
        this.stemm = new PorterStemmer();
    }

    public String stemmFrase(String frase) throws Exception {
        String retorno = "";
        String[] phraseStems = this.stemm.getPhraseStems(frase);
        for (String phraseStem : phraseStems) {
            if (retorno.isEmpty()) {
                retorno = phraseStem;
            } else {
                retorno = retorno.concat(" " + phraseStem);
            }
        }
        return retorno;
    }
    
    public String stemmPalavra(String palavra) throws Exception {
        String word = this.stemm.getWordStem(palavra);
        return word;
    }
}
