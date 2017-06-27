package testes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.acbv.propagacao_dupla.entidades.Corpus;
import com.acbv.propagacao_dupla.entidades.Frase;
import com.acbv.propagacao_dupla.entidades.Nodo;
import com.acbv.propagacao_dupla.entidades.Resenha;
import com.acbv.propagacao_dupla.metodo.DoublePropagation;
import com.acbv.propagacao_dupla.preprocessamento.StanfordUtils;
import com.acbv.propagacao_dupla.utils.Avaliacao;
import com.acbv.propagacao_dupla.utils.Utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author arthur
 */
public class Testes {

    private static StanfordUtils stfUtils;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        stfUtils = new StanfordUtils();
        Set<String> lexico = getLexico();
        Corpus corpus = getCorpus();
        Set<String> stopSubsWords = stopSubsWords();
        DoublePropagation dp = new DoublePropagation();
        dp.runDP(corpus, lexico, stopSubsWords);
        Set<String> aspectosExtraidos = new HashSet<>();
        dp.aspectosExtraidos.forEach((alvosExtraido) -> {
            aspectosExtraidos.add(alvosExtraido.toLowerCase());
        });
//        Set<String> lexicoExpandido = dp.lexicoExpandido;
//        for (String string : lexico) {
//            System.out.println(string);
//        }
//        for (String alvosExtraido : alvosExtraidos) {
//            System.out.println(alvosExtraido);
//        }
//        for (String alvoExtraido : alvosExtraidos) {
//            if(alvoExtraido.split(" ").length == 1){
//                Integer get = corpus.bagOfWords.get(alvoExtraido);
//                System.out.println("ALVO: "+alvoExtraido+"\tFREQ: "+get);
//            }
//        }
//        for (String alvoExtraido : alvosExtraidos) {
//            if(alvoExtraido.split(" ").length == 2){
//                Integer get = corpus.bagOf2Grams.get(alvoExtraido);
//                System.out.println("ALVO: "+alvoExtraido+"\tFREQ: "+get);
//            }
//        }
//        for (String alvoExtraido : alvosExtraidos) {
//            if(alvoExtraido.split(" ").length == 3){
//                Integer get = corpus.bagOf3Grams.get(alvoExtraido);
//                System.out.println("ALVO: "+alvoExtraido+"\tFREQ: "+get);
//            }
//        }
//        for (String alvoExtraido : alvosExtraidos) {
//            if(alvoExtraido.split(" ").length == 4){
//                Integer get = corpus.bagOf4Grams.get(alvoExtraido);
//                System.out.println("ALVO: "+alvoExtraido+"\tFREQ: "+get);
//            }
//        }
//            System.out.println("ALVO: " + alvoExtraido + "\tFREQ: " + get);
        Set<String> aspectosEsperados = getExpectedAspects();
        HashMap<String, Integer> bagOfAllGrams = new HashMap<>();
        bagOfAllGrams.putAll(corpus.bagOfWords);
        bagOfAllGrams.putAll(corpus.bagOf2Grams);
        bagOfAllGrams.putAll(corpus.bagOf3Grams);
        bagOfAllGrams.putAll(corpus.bagOf4Grams);
        bagOfAllGrams.putAll(corpus.bagOf5Grams);
//        Set<String> maisFrequentes = getMaisFrequentes(alvosExtraidos, bagOfAllGrams, 95);
//        Set<String> diferente = getDiferente(alvosExtraidos, expectedAspects);
//        for (String string : diferente) {
//            System.out.println(string);
//        }
        System.out.println("NNNNNN");
        for (String aspectoEsperado : aspectosEsperados) {
            if (!aspectosExtraidos.contains(aspectoEsperado)) {
                searchForAparicoes(corpus, aspectoEsperado);
            }
        }
        Avaliacao avalicao = new Avaliacao(aspectosEsperados, aspectosExtraidos);
        System.out.println(avalicao);
    }

    private static Corpus getCorpus() throws Exception {
        Corpus corpus;
//        String fileText = new String(Files.readAllBytes(Paths.get("/home/arthur/Dropbox/Dissertacao/Reviews Portugues/Submarino - smartphone/Documento sem titulo")), "UTF-8").toLowerCase();
        String fileText = new String(Files.readAllBytes(Paths.get("/home/arthur/Dropbox/Dissertacao/Reviews Portugues/Submarino - IPhone/reviews.txt")), "UTF-8").toLowerCase();
        String[] reviewsStr = fileText.split("\\[r\\]");
        List<String> corpusAux = Arrays.asList(reviewsStr);
        List<Resenha> resenhas = stfUtils.getResenhas(corpusAux);
        Iterator<Resenha> iterator = resenhas.iterator();
        while (iterator.hasNext()) {
            Resenha resenha = iterator.next();
            if (resenha.texto.trim().isEmpty()) {
                iterator.remove();
            }
        }
        corpus = new Corpus(resenhas);
        corpus.bagOfWords = getBagOfWords(resenhas);
        corpus.bagOf2Grams = getBagNGrams(corpus.resenhas, 2);
        corpus.bagOf3Grams = getBagNGrams(corpus.resenhas, 3);
        corpus.bagOf4Grams = getBagNGrams(corpus.resenhas, 4);
        corpus.bagOf5Grams = getBagNGrams(corpus.resenhas, 5);
        return corpus;
    }

    private static Set<String> getLexico() throws IOException {
        Set<String> lexico = new HashSet<>();
        String fileText = new String(Files.readAllBytes(Paths.get("/home/arthur/Dropbox/Dissertacao/SentiLex-PT02/SentiLex-flex-PT02.txt")), "UTF-8");
        String[] lexicoStr = fileText.split("\n");
        for (String string : lexicoStr) {
            String lex = string.split(",")[0];
            if (lex.split(" ").length == 1) {
//                lexico.add(lex);
                lexico.add(Utils.removerAcentos(lex).toLowerCase());
            }

        }
        return lexico;
    }

    private static Set<String> getLexico2() throws IOException {
        Set<String> lexico = new HashSet<>();
        String fileText = new String(Files.readAllBytes(Paths.get("/home/arthur/Dropbox/Dissertacao/SentiLex-PT02/SentiLex-lem-PT02.txt")), "UTF-8");
        String[] lexicoStr = fileText.split("\n");
        for (String string : lexicoStr) {
            String lex = string.split("\\.")[0];
            if (lex.split(" ").length == 1) {
//                lexico.add(lex);
                lexico.add(Utils.removerAcentos(lex).toLowerCase());
            }

        }
        return lexico;
    }
    
    private static Set<String> getLexico3() throws IOException {
        Set<String> lexico = new HashSet<>();
        Set<String> lexico1 = getLexico();
        Set<String> lexico2 = getLexico2();
        lexico.addAll(lexico1);
        lexico.addAll(lexico2);
        return lexico;
    }

    private static Set<String> getExpectedAspects() throws IOException {
        String expectedStr = new String(Files.readAllBytes(Paths.get("/home/arthur/Dropbox/Dissertacao/Reviews Portugues/Submarino - IPhone/aspectos")), "UTF-8");
        List<String> expectedAux = Arrays.asList(expectedStr.split("\n"));
        Set<String> expectedAux_ = new HashSet<>();
        expectedAux.forEach((string) -> {
            expectedAux_.add(Utils.removerAcentos(string.trim().toLowerCase()));
        });
        return expectedAux_;
    }

    private static HashMap<String, Integer> getBagOfWords(List<Resenha> resenhas) {
        HashMap<String, Integer> bow = new HashMap<>();
        resenhas.stream().map((resenha) -> resenha.frases).forEachOrdered((frases) -> {
            for (Frase frase : frases) {
                Nodo[] nodos = frase.nodos;
                for (Nodo nodo : nodos) {
                    String token = nodo.texto.toLowerCase();
                    bow.putIfAbsent(token, 0);
                    Integer freqAnterior = bow.get(token) + 1;
                    bow.replace(token, freqAnterior);
                }
            }
        });
        return bow;
    }

    private static HashMap<String, Integer> getBagNGrams(List<Resenha> resenhas, int n) {
        HashMap<String, Integer> bow = new HashMap<>();
        resenhas.stream().map((resenha) -> resenha.frases).forEachOrdered((frases) -> {
            for (Frase frase : frases) {
                Nodo[] nodos = frase.nodos;
                for (int i = 0; i < nodos.length; i++) {
                    Nodo nodo_i = nodos[i];
                    String token = nodo_i.texto.toLowerCase();
                    if (!removerCaracteresEspeciais(token).trim().isEmpty()) {
                        for (int j = 1; j < n; j++) {
                            try {
                                String nodo_j = nodos[i + j].texto.toLowerCase();
                                if (!removerCaracteresEspeciais(nodo_j).trim().isEmpty()) {
                                    token = token + " " + nodo_j;
                                } else {
                                    break;
                                }
                            } catch (IndexOutOfBoundsException ex) {
                                break;
                            }
                        }
                        if (token.split(" ").length == n) {
                            bow.putIfAbsent(token, 0);
                            Integer freqAnterior = bow.get(token) + 1;
                            bow.replace(token, freqAnterior);
                        }
                    }
                }
            }
        });
        return bow;
    }

    private static Set<String> getMaisFrequentes(Set<String> alvosExtraidos, HashMap<String, Integer> bagOfWords, double perc) {
        TreeMap<String, Integer> freqAspectos = getFreqAspectos(alvosExtraidos, bagOfWords);
        int numSubstantivos = alvosExtraidos.size();
        int percAux = (int) (((double) numSubstantivos / 100) * perc);
        List<Map.Entry<String, Integer>> entriesSortedByValues = entriesSortedByValues(freqAspectos);
        List<Map.Entry<String, Integer>> subList = entriesSortedByValues.subList(0, percAux - 1);
        Set<String> substantivosFreq = new HashSet<>();
        subList.forEach((entry) -> {
            substantivosFreq.add(entry.getKey());
        });
        return substantivosFreq;
    }

    static <K, V extends Comparable<? super V>>
            List<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries, (Map.Entry<K, V> e1, Map.Entry<K, V> e2) -> e2.getValue().compareTo(e1.getValue()));

        return sortedEntries;
    }

    private static TreeMap<String, Integer> getFreqAspectos(Set<String> alvosExtraidos, HashMap<String, Integer> bagOfWords) {
        TreeMap<String, Integer> freqAspectos = new TreeMap<>();
        alvosExtraidos.forEach((alvo) -> {
            int ui = bagOfWords.get(alvo) != null ? bagOfWords.get(alvo) : 0;
            freqAspectos.put(alvo, ui);
        });
        return freqAspectos;
    }

    private static Set<String> getBestConfig(Set<String> aspectosExtraidos, Set<String> aspectosEsperados, HashMap<String, Integer> bagOfWords) {
        double perc;
        double precision = 0.0;
        double melhorConfig = 10;
        Set<String> aspects = new HashSet<>();
        Avaliacao avaliacaoFinal = null;
        for (int i = 1; i <= 10; i++) {
            perc = i * 10;
            Set<String> aspectosMaisFrequentes = getMaisFrequentes(aspectosExtraidos, bagOfWords, perc);
            Avaliacao avaliacao = new Avaliacao(aspectosEsperados, aspectosMaisFrequentes);
            if (avaliacao.precision > precision) {
                aspects = aspectosMaisFrequentes;
                melhorConfig = perc;
                avaliacaoFinal = avaliacao;
            }
        }
        System.out.println(aspects);
        System.out.println("A melhor configuração é: " + melhorConfig);
        System.out.println("Avaliação: " + avaliacaoFinal);
        return aspects;
    }

    private static String removerCaracteresEspeciais(String str) {
        Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
        Matcher match = pt.matcher(str);
        while (match.find()) {
            String s = match.group();
            str = str.replaceAll("\\" + s, "");
        }
        return str;
    }

    private static Set<String> getDiferente(Set<String> alvosExtraidos, Set<String> expectedAspects) {
        Set<String> swag = new HashSet<>();
        alvosExtraidos.stream().filter((alvoExtraido) -> (!expectedAspects.contains(alvoExtraido))).forEachOrdered((alvoExtraido) -> {
            swag.add(alvoExtraido);
        });
        return swag;
    }

    private static void searchForAparicoes(Corpus corpus, String alvoEsperado) {
        System.out.println("Alvo esperado: " + alvoEsperado);
        System.out.println("________________________________");
        Integer frequencia = corpus.bagOfWords.get(alvoEsperado) != null
                ? corpus.bagOfWords.get(alvoEsperado)
                : corpus.bagOf2Grams.get(alvoEsperado) != null
                ? corpus.bagOf2Grams.get(alvoEsperado)
                : corpus.bagOf3Grams.get(alvoEsperado) != null
                ? corpus.bagOf3Grams.get(alvoEsperado)
                : corpus.bagOf4Grams.get(alvoEsperado) != null
                ? corpus.bagOf4Grams.get(alvoEsperado)
                : corpus.bagOf5Grams.get(alvoEsperado) != null
                ? corpus.bagOf5Grams.get(alvoEsperado)
                : null;
        System.out.println("Frequência: " + frequencia);
        List<Resenha> resenhas = corpus.resenhas;
        System.out.println("APARECE EM: ");
        for (Resenha resenha : resenhas) {
            if (Utils.removerAcentos(resenha.texto).contains(alvoEsperado)) {
                System.out.println(resenha.texto);
            }
        }
        System.out.println("________________________________");
    }

    private static Set<String> stopSubsWords() {
        Set<String> stopSubsWords = new HashSet<>();
//        stopSubsWords.add("dias");
//        stopSubsWords.add("necessidades");
//        stopSubsWords.add("expectativas");
//        stopSubsWords.add("coisa");
        return stopSubsWords;
    }
}
