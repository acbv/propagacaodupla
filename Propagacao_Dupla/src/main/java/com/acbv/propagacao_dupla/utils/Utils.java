/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arthur
 */
public class Utils {
    
//    public static Set<OpinionWord> getLexicon(HashMap<String, Boolean> lexicon) {
//        Set<OpinionWord> opinionWords = new HashSet<>();
//        Set<Map.Entry<String, Boolean>> lexiconES = lexicon.entrySet();
//        OpinionWord o;
//        for (Map.Entry<String, Boolean> ow : lexiconES) {
//            o = new OpinionWord();
//            o.setWord(ow.getKey());
//            o.setPolarity(ow.getValue());
//            opinionWords.add(o);
//        }
//        return opinionWords;
//    }

    public static String[] getOnlyWords(String text) {
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = (words[i].replaceAll("[^\\w]", ""));
        }
        return words;
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
    
    public static boolean containsString(String[] tokensSearch, String[] tokensSentence) {
        boolean containsString = false;

        //check if contains first token
        String firstTokenSearch = tokensSearch[0];
        List<Integer> listIndexFirstToken = new ArrayList<>();
        for (int i = 0; i < tokensSentence.length; i++) {
            String sentenceToken_i = tokensSentence[i];
            if (firstTokenSearch.equalsIgnoreCase(sentenceToken_i)) {
                listIndexFirstToken.add(i);
            }
        }
        boolean containsFirstToken = listIndexFirstToken.size() > 0;
        if (containsFirstToken) {
            int tokensSearchLength = tokensSearch.length;
            for (Integer indexTokenSearch : listIndexFirstToken) {
                boolean d = true;
                for (int i = 1; i < tokensSearchLength; i++) {
                    try {
                        String tokenSearch_i = tokensSearch[i];
                        String sentenceToken = tokensSentence[indexTokenSearch + 1];
                        if (tokenSearch_i.equalsIgnoreCase(sentenceToken)) {
                        } else {
                            d = false;
                            break;
                        }
                    } catch (Exception e) {
                        d = false;
                        break;
                    }
                }
                if (d) {
                    containsString = true;
                    break;
                }
            }
        }
        return containsString;
    }
    
//    public static int pegarIndiceDaPalavraRetirandoCharEspeciais(Frase frase, int wordIndex) {
//        int k = -1;
//        int l = 0;
//        int atual = 0;
//        //Pegando o índice da palavra opinativa na lista de palavras da frase
//        List<String> tokensWords = Utils.getTokensWords(frase.getTokens());
//        for (String token : tokensWords) {
//            token = token.replaceFirst("[^\\w]", "");
//            if (!token.isEmpty()) {
//                k++;
//            }
//            if (l == wordIndex) {
//                atual = k;
//                break;
//            }
//            l++;
//        }
//        atual--;
//        return atual;
//    }

    public static String[] getSurrounding5WindowWords(String myArray[], int indice) {
        int tamanhoArray = myArray.length;
        if (tamanhoArray <= 5) {
            return myArray;
        } else {
            String[] resultado = new String[5];
            if (indice == 0) {
                for (int i = 0; i < resultado.length; i++) {
                    resultado[i] = myArray[i];
                }
            } else if (indice == tamanhoArray - 1) {
                int j = 0;
                int parada = indice - 5;
                for (int i = indice; i > parada; i--) {
                    resultado[j] = myArray[i];
                    j++;
                }
            } else {
                int ultimoIndice = tamanhoArray - 1;
                resultado[0] = ultimoIndice - indice == 1 ? myArray[indice - 3]
                        : indice - 2 < 0 ? myArray[indice - 1] : myArray[indice - 2];
                resultado[1] = ultimoIndice - indice == 1 ? myArray[indice - 2]
                        : indice - 1 == 0 ? myArray[indice] : myArray[indice - 1];
                resultado[2] = ultimoIndice - indice == 1 ? myArray[indice - 1]
                        : indice - 2 < 0 ? myArray[indice + 1] : myArray[indice];
                resultado[3] = indice - 1 == 0 ? myArray[indice + 2]
                        : ultimoIndice - indice == 1 ? myArray[indice] : myArray[indice + 1];
                resultado[4] = indice - 1 == 0 ? myArray[indice + 3]
                        : ultimoIndice - indice == 1 ? myArray[indice + 1] : myArray[indice + 2];
            }
            return resultado;
        }
    }

}
