/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.metodo;

import com.acbv.propagacao_dupla.entidades.Categoria_Sintatica;
import com.acbv.propagacao_dupla.entidades.Corpus;
import com.acbv.propagacao_dupla.entidades.Frase;
import com.acbv.propagacao_dupla.entidades.Nodo;
import com.acbv.propagacao_dupla.entidades.Oracao;
import com.acbv.propagacao_dupla.entidades.Relacao_Dependencia;
import com.acbv.propagacao_dupla.entidades.Resenha;
import com.acbv.propagacao_dupla.entidades.Tipo_Relacao;
import com.acbv.propagacao_dupla.utils.Sintatico;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Arthur
 */
public class TargetPruning {

    private final Corpus corpus;
    private final List<Resenha> resenhas;
    public Set<String> aspectos, stopSubWords;
    private static final List<String> PREPS = new ArrayList<>(Arrays.asList(new String[]{"da", "de", "do"}));

    public TargetPruning(Corpus corpus, Set<String> aspectos, Set<String> stopSubsWords) {
        this.corpus = corpus;
        this.resenhas = corpus.resenhas;
        this.aspectos = aspectos;
        this.stopSubWords = stopSubsWords;
        Set<String> aspectosPruning3 = pruningMethod3(aspectos);
        Set<String> aspectosPruning4 = pruningMethod4(aspectos);
        Set<String> aspectosPruning5 = pruningMethod5(aspectos);
        this.aspectos.addAll(aspectosPruning3);
        this.aspectos.addAll(aspectosPruning4);
        this.aspectos.addAll(aspectosPruning5);
        this.aspectos = finalPruningMethod(this.aspectos);
//        this.aspectos = removeStopSubsWords(this.aspectos, this.stopSubWords);
    }

    private Set<String> pruningMethod3(Set<String> aspectos) {
        Set<String> targets_ = new HashSet<>();
        this.resenhas.forEach((resenha) -> {
            targets_.addAll(pruningMethod3(resenha, aspectos));
        });
//        Iterator<String> iterator = targets_.iterator();
//        while (iterator.hasNext()) {
//            String target = iterator.next();
//            int occorencia = 0;
//            for (Resenha resenha : this.resenhas) {
//                Frase[] frases_resenha = resenha.frases;
//                for (Frase frase : frases_resenha) {
//                    Nodo[] nodos = frase.nodos;
//                    String[] targetTokens = target.split(" ");
//                    String[] sentenceTokens = new String[nodos.length];
//                    String[] sentenceTokensLemma = new String[nodos.length];
//                    for (int i = 0; i < nodos.length; i++) {
//                        sentenceTokens[i] = nodos[i].texto;
//                        sentenceTokensLemma[i] = nodos[i].lemma;
//                    }
//                    boolean contains = containsString(targetTokens, sentenceTokens);
//                    boolean containsLemma = containsString(targetTokens, sentenceTokensLemma);
//                    if (contains || containsLemma) {
//                        occorencia++;
//                    }
//                }
//            }
//            if (occorencia <= 1) {
//                iterator.remove();
//            }
//        }
        return targets_;
    }

    private Set<String> pruningMethod3(Resenha resenha, Set<String> targets) {
        int q = 2, k = 1;
        Set<String> phraseTargets = new HashSet<>();
        Frase[] frases = resenha.frases;
        for (Frase frase : frases) {
            Nodo[] nodos = frase.nodos;
            for (int i = 0; i < nodos.length; i++) {
                Nodo token = nodos[i];
                boolean modificou = false;
                int primeiroTokenIndice = i;
                String target = token.texto;
                if (targets.contains(target)) {
                    boolean pegaAntes = true;
                    boolean pegaApos = true;
                    for (int j = 0; j < q; j++) {
                        int index_j = j + 1;
                        try {
                            if (pegaAntes) {
                                Nodo antes = nodos[i - index_j];
                                if (Sintatico.isSubstantivo(antes.categoria)) {
                                    target = antes.texto.concat(" " + target);
                                    modificou = true;
                                    primeiroTokenIndice = i - index_j;
                                } else {
                                    pegaAntes = false;
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            pegaAntes = false;
                        }
                    }
                    for (int j = 0; j < q; j++) {
                        int index_j = j + 1;
                        try {
                            if (pegaApos) {
                                Nodo apos = nodos[i + index_j];
                                if (Sintatico.isSubstantivo(apos.categoria)) {
                                    target = target.concat(" " + apos.texto);
                                    modificou = true;
                                } else {
                                    pegaApos = false;
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            pegaApos = false;
                        }
                    }
                    if (modificou) {
                        int numAdjetivos = 0;
                        boolean satisfezK = false;
                        for (int l = 0; l < primeiroTokenIndice; l++) {
                            Nodo t = nodos[l];
                            if (Sintatico.isAdjetivo(t.categoria)) {
                                numAdjetivos++;
                                if (numAdjetivos == k) {
                                    satisfezK = true;
                                    break;
                                }
                            }
                        }
                        if (satisfezK) {
                            phraseTargets.add(target);
                        } else {
                            phraseTargets.add(token.texto);
                        }
                    } else {
                        phraseTargets.add(token.texto);
                    }
                }
            }
        }
        return phraseTargets;
    }

    private Set<String> pruningMethod4(Set<String> aspectos) {
        Set<String> targets_ = new HashSet<>();
        this.resenhas.forEach((resenha) -> {
            targets_.addAll(pruningMethod4(resenha, aspectos));
        });
//        Iterator<String> iterator = targets_.iterator();
//        while (iterator.hasNext()) {
//            String target = iterator.next();
//            int occorencia = 0;
//            for (Resenha resenha : resenhas) {
//                Frase[] frases_resenha = resenha.frases;
//                for (Frase frase : frases_resenha) {
//                    String frase_resenha = frase.texto;
//                    if (frase_resenha.contains(target)) {
//                        occorencia++;
//                    }
//                }
//            }
//            if (occorencia <= 1) {
//                iterator.remove();
//            }
//        }
        return targets_;
    }

    private static Set<String> pruningMethod4(Resenha resenha, Set<String> targets) {
        int q = 2, k = 1;
        Set<String> phraseTargets = new HashSet<>();
        Frase[] frases = resenha.frases;
        for (Frase frase : frases) {
            Nodo[] nodos = frase.nodos;
            for (int i = 0; i < nodos.length; i++) {
                Nodo token = nodos[i];
                boolean modificou = false;
                int primeiroTokenIndice = i;
                int ultimoTokenIndice = i;
                String target = token.texto;
                if (targets.contains(target)) {
                    boolean pegaAntes = true;
                    boolean pegaApos = true;
                    for (int j = 0; j < q; j++) {
                        int index_j = j + 1;
                        try {
                            if (pegaAntes) {
                                Nodo antes = nodos[i - index_j];
                                if (Sintatico.isSubstantivo(antes.categoria)) {
                                    target = antes.texto.concat(" " + target);
                                    modificou = true;
                                    primeiroTokenIndice = i - index_j;
                                } else {
                                    pegaAntes = false;
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            pegaAntes = false;
                        }
                        try {
                            if (pegaApos) {
                                Nodo apos = nodos[i + index_j];
                                if (Sintatico.isSubstantivo(apos.categoria)) {
                                    target = target.concat(" " + apos.texto);
                                    modificou = true;
                                    ultimoTokenIndice = i + index_j;
                                } else {
                                    pegaApos = false;
                                }
                            }
                        } catch (IndexOutOfBoundsException e) {
                            pegaApos = false;
                        }
                    }
                    if (modificou) {
                        try {
                            int dt_indice = primeiroTokenIndice - 1;
                            Nodo prov_dt = nodos[dt_indice];
                            Nodo prov_verbo = nodos[ultimoTokenIndice + 1];
                            Nodo prov_adj = nodos[ultimoTokenIndice + 2];
                            boolean foi = false;
                            if (prov_dt.categoria == Categoria_Sintatica.DT && prov_verbo.categoria == Categoria_Sintatica.VB
                                    && Sintatico.isAdjetivo(prov_adj.categoria)) {
                                System.out.println("PRUNING METHOD 4: " + target);
                                phraseTargets.add(target);
                                foi = true;
//                                System.out.println("TESTE: " + target);
                            }
                            if (!foi) {
                                Nodo prov_adv = nodos[ultimoTokenIndice + 2];
                                prov_adj = nodos[ultimoTokenIndice + 3];
                                if (prov_dt.categoria == Categoria_Sintatica.DT && prov_verbo.categoria == Categoria_Sintatica.VB
                                        && Sintatico.isAdjetivo(prov_adj.categoria) && prov_adv.categoria == Categoria_Sintatica.RB) {
                                    System.out.println("PRUNING METHOD 4: " + target);
                                    phraseTargets.add(target);
//                                    System.out.println("TESTE: " + target);
                                }
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        phraseTargets.add(token.texto);
                    }
                }
            }
        }
        return phraseTargets;
    }

    private Set<String> pruningMethod5(Set<String> targets) {
        Set<String> targets_ = new HashSet<>();
        this.resenhas.forEach((resenha) -> {
            targets_.addAll(pruningMethod5(resenha, targets));
        });
        return targets_;
    }

    private static Set<String> pruningMethod5(Resenha resenha, Set<String> aspectos) {
        int q = 1, k = 1;
        Set<String> phraseTargets = new HashSet<>();
        Frase[] frases = resenha.frases;
        for (Frase frase : frases) {
            Nodo[] nodos = frase.nodos;
            for (int i = 0; i < nodos.length; i++) {
                Nodo token = nodos[i];
                boolean modificou = false;
                int primeiroTokenIndice = i;
                String target = token.texto;
                if (aspectos.contains(target)) {
                    int index_j = 1;
                    try {
                        Nodo antes = nodos[i - index_j];
                        Nodo bemAntes = nodos[i - index_j - 1];
                        if (PREPS.contains(antes.texto) && Sintatico.isSubstantivo(bemAntes.categoria)) {
                            target = bemAntes.texto + " " + antes.texto.concat(" " + target);
                            modificou = true;
                            primeiroTokenIndice = i - index_j;
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                    try {
                        Nodo apos = nodos[i + index_j];
                        Nodo bemApos = nodos[i + index_j + 1];
                        if (PREPS.contains(apos.texto) && Sintatico.isSubstantivo(bemApos.categoria)) {
                            target = target.concat(" " + apos.texto) + " " + bemApos.texto;
                            modificou = true;
                        }
                    } catch (IndexOutOfBoundsException e) {
                    }
                    if (modificou) {
                        int numAdjetivos = 0;
                        boolean satisfezK = false;
                        for (int l = 0; l < primeiroTokenIndice; l++) {
                            Nodo t = nodos[l];
                            if (Sintatico.isAdjetivo(t.categoria)) {
                                numAdjetivos++;
                                if (numAdjetivos == k) {
                                    satisfezK = true;
                                    break;
                                }
                            }
                        }
                        if (satisfezK) {
                            System.out.println("PRUNING METHOD 5: " + target);
                            phraseTargets.add(target);
                        } else {
                            phraseTargets.add(token.texto);
                        }
                    } else {
                        phraseTargets.add(token.texto);
                    }
                }
            }
        }
        return phraseTargets;
    }

    private static boolean containsString(String[] tokensSearch, String[] tokensSentence) {
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

    @Deprecated
    private HashMap<String, Integer> getBagOfNouns(List<Resenha> resenhas) {
        HashMap<String, Integer> bagOfWords = new HashMap<>();
        resenhas.stream().map((resenha) -> resenha.frases).forEachOrdered((frases) -> {
            for (Frase frase : frases) {
                Nodo[] nodos = frase.nodos;
                for (Nodo nodo : nodos) {
                    if (nodo.categoria == Categoria_Sintatica.NN || nodo.categoria == Categoria_Sintatica.NNS) {
                        String word = nodo.lemma;
                        bagOfWords.putIfAbsent(word, 0);
                        bagOfWords.put(word, bagOfWords.get(word) + 1);
                    }
                }
            }
        });
        return bagOfWords;
    }

    @Deprecated
    private Set<String> pruningMethod1(Set<String> targets, HashMap<String, Integer> bagOfNounWords) {
        for (Resenha resenha : this.resenhas) {
            for (Frase frase : resenha.frases) {
                for (Oracao oracao : frase.oracoes) {
                    List<String> oracaoTargets = new ArrayList<>();
                    for (Nodo nodo : oracao.nodos) {
                        String no = nodo.lemma;
                        if (targets.contains(no)) {
                            oracaoTargets.add(no);
                        }
                    }
                    if (oracaoTargets.size() > 1) {
                        for (int i = 0; i < oracaoTargets.size(); i++) {
                            String target_i = oracaoTargets.get(i);
                            for (int j = 0; j < oracaoTargets.size(); j++) {
                                if (i > j) {
                                    String target_j = oracaoTargets.get(j);
                                    for (Relacao_Dependencia relacao : oracao.relacoes) {
                                        try {
                                            if (((relacao.dependente.texto.equals(target_i) && relacao.governante.texto.equals(target_j))
                                                    || (relacao.dependente.texto.equals(target_j) && relacao.governante.texto.equals(target_i)))
                                                    && relacao.relacao != Tipo_Relacao.CONJ) {
                                                int freq_i = bagOfNounWords.get(target_i) != null ? bagOfNounWords.get(target_i) : 0;
                                                int freq_j = bagOfNounWords.get(target_j) != null ? bagOfNounWords.get(target_j) : 0;
                                                if (freq_i > freq_j) {
                                                    targets.remove(target_j);
                                                }
                                                if (freq_i < freq_j) {
                                                    targets.remove(target_i);
                                                }
                                            }
                                        } catch (NullPointerException e) {
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return targets;
    }

    @Deprecated
    private static Nodo[] getNodos(Relacao_Dependencia[] relacoes) {
        Set<Nodo> nodos_ = new HashSet<>();
        for (Relacao_Dependencia relacoe : relacoes) {
            nodos_.add(relacoe.dependente);
            nodos_.add(relacoe.governante);
        }
        Nodo[] nodos = nodos_.toArray(new Nodo[nodos_.size()]);
        return nodos;
    }

    private Set<String> finalPruningMethod(Set<String> aspects) {
        HashMap<String, Integer> bagOfWords = this.corpus.bagOfWords;
//        Set<Map.Entry<String, Integer>> entrySet = bagOfWords.entrySet();
//        Set<Map.Entry<String, Integer>> entrySet2 = this.corpus.bagOf2Grams.entrySet();
//        Set<Map.Entry<String, Integer>> entrySet3 = this.corpus.bagOf3Grams.entrySet();
//        Set<Map.Entry<String, Integer>> entrySet4 = this.corpus.bagOf4Grams.entrySet();
//        Set<Map.Entry<String, Integer>> entrySet5 = this.corpus.bagOf5Grams.entrySet();
        HashMap<String, Integer> bag2GramsAspects = new HashMap<>();
        HashMap<String, Integer> bag3GramsAspects = new HashMap<>();
        HashMap<String, Integer> bag4GramsAspects = new HashMap<>();
        HashMap<String, Integer> bag5GramsAspects = new HashMap<>();
        Set<Map.Entry<String, Integer>> entrySet2Aspects = bag2GramsAspects.entrySet();
        Set<Map.Entry<String, Integer>> entrySet3Aspects = bag3GramsAspects.entrySet();
        Set<Map.Entry<String, Integer>> entrySet4Aspects = bag4GramsAspects.entrySet();
        Set<Map.Entry<String, Integer>> entrySet5Aspects = bag5GramsAspects.entrySet();
        for (String aspect : aspects) {
            String[] aux = aspect.split(" ");
            int auxLength = aux.length;
            switch (auxLength) {
                case 2: {
                    int freq = this.corpus.bagOf2Grams.get(aspect);
                    bag2GramsAspects.put(aspect, freq);
                    break;
                }
                case 3: {
                    int freq = this.corpus.bagOf3Grams.get(aspect);
                    bag3GramsAspects.put(aspect, freq);
                    break;
                }
                case 4: {
                    int freq = this.corpus.bagOf4Grams.get(aspect);
                    bag4GramsAspects.put(aspect, freq);
                    break;
                }
                case 5: {
                    int freq = this.corpus.bagOf5Grams.get(aspect);
                    bag5GramsAspects.put(aspect, freq);
                    break;
                }
                default:
                    break;
            }
        }
        Iterator<String> iterator = aspects.iterator();
        while (iterator.hasNext()) {
            String aspect = iterator.next();
            int cont = 0;
            int freq = 0;
            String[] aux = aspect.split(" ");
            int auxLength = aux.length;
            switch (auxLength) {
                case 1:
                    freq = bagOfWords.get(aspect);
                    cont = getFreq(aspect, entrySet2Aspects, cont);
                    cont = getFreq(aspect, entrySet3Aspects, cont);
                    cont = getFreq(aspect, entrySet4Aspects, cont);
                    cont = getFreq(aspect, entrySet5Aspects, cont);
                    break;
                case 2:
                    freq = bag2GramsAspects.get(aspect);
                    cont = getFreq(aspect, entrySet3Aspects, cont);
                    cont = getFreq(aspect, entrySet4Aspects, cont);
                    cont = getFreq(aspect, entrySet5Aspects, cont);
                    break;
                case 3:
                    freq = bag3GramsAspects.get(aspect);
                    cont = getFreq(aspect, entrySet4Aspects, cont);
                    cont = getFreq(aspect, entrySet5Aspects, cont);
                    break;
                case 4:
                    freq = bag4GramsAspects.get(aspect);
                    cont = getFreq(aspect, entrySet5Aspects, cont);
                    break;
                default:
                    break;
            }
            if (cont == freq) {
                System.out.println("PRUNING METHOD FINAL: " + aspect);
                iterator.remove();
            }
        }
        return aspects;
    }

    private int getFreq(String aspect, Set<Map.Entry<String, Integer>> entrySet, int cont) {
        for (Map.Entry<String, Integer> entry : entrySet) {
            String keyTemp = entry.getKey();
            String[] split = keyTemp.split(" ");
            for (String token : split) {
                if (token.equalsIgnoreCase(aspect)) {
                    cont += entry.getValue();
                    break;
                }
            }
        }
        return cont;
    }

    private Set<String> deleteFrequence(Set<String> aspectos) {
        Iterator<String> iterator = aspectos.iterator();
        while (iterator.hasNext()) {
            String target = iterator.next();
            int occorencia = 0;
            for (Resenha resenha : this.resenhas) {
                Frase[] frases_resenha = resenha.frases;
                for (Frase frase : frases_resenha) {
                    Nodo[] nodos = frase.nodos;
                    String[] targetTokens = target.split(" ");
                    String[] sentenceTokens = new String[nodos.length];
                    String[] sentenceTokensLemma = new String[nodos.length];
                    for (int i = 0; i < nodos.length; i++) {
                        sentenceTokens[i] = nodos[i].texto;
                        sentenceTokensLemma[i] = nodos[i].lemma;
                    }
                    boolean contains = containsString(targetTokens, sentenceTokens);
                    boolean containsLemma = containsString(targetTokens, sentenceTokensLemma);
                    if (contains || containsLemma) {
                        occorencia++;
                    }
                }
            }
            if (occorencia <= 1) {
                iterator.remove();
            }
        }
        return aspectos;
    }

    private Set<String> removeStopSubsWords(Set<String> aspectos, Set<String> stopSubWords) {
        Iterator<String> aspectosItrt = aspectos.iterator();
        while (aspectosItrt.hasNext()) {
            String aspecto = aspectosItrt.next();
            for (String stopSubWord : stopSubWords) {
                if (aspecto.equalsIgnoreCase(stopSubWord)) {
                    aspectosItrt.remove();
                    break;
                }
            }
        }
        return aspectos;
    }
}
