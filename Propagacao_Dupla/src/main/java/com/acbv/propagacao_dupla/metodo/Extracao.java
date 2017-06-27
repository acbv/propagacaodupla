/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.metodo;

import com.acbv.propagacao_dupla.entidades.Categoria_Sintatica;
import com.acbv.propagacao_dupla.entidades.Frase;
import com.acbv.propagacao_dupla.entidades.Nodo;
import com.acbv.propagacao_dupla.entidades.Relacao_Dependencia;
import com.acbv.propagacao_dupla.entidades.Tipo_Relacao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Arthur
 */
public class Extracao {

    List<Tipo_Relacao> MR = new ArrayList<>(Arrays.asList(new Tipo_Relacao[]{Tipo_Relacao.AMOD, Tipo_Relacao.NMOD, Tipo_Relacao.CSUBJ, Tipo_Relacao.DOBJ, Tipo_Relacao.IOBJ,
        Tipo_Relacao.NSUBJ, Tipo_Relacao.PREP, Tipo_Relacao.XSUBJ}));
    List<Categoria_Sintatica> NN = new ArrayList<>(Arrays.asList(new Categoria_Sintatica[]{Categoria_Sintatica.NN, Categoria_Sintatica.NNS}));
    List<Categoria_Sintatica> JJ = new ArrayList<>(Arrays.asList(new Categoria_Sintatica[]{Categoria_Sintatica.JJ, Categoria_Sintatica.JJS, Categoria_Sintatica.JJR}));

    public Set<String> extractAspectsUsingR1(Frase frase, Set<String> lexicoExpandido, Set<String> aspectosExtraidos) throws IOException {
        Set<String> aspectos = new HashSet<>();
        aspectos.addAll(extractAspectsUsingR1_1(frase, lexicoExpandido, aspectosExtraidos));
        aspectos.addAll(extractAspectsUsingR1_2(frase, lexicoExpandido, aspectosExtraidos));
        return aspectos;
    }

    public Set<String> extractOpinionWordsUsingR2(Frase frase, Set<String> aspects, Set<String> lexicoExpandido) throws IOException {
        Set<String> palavrasOpinativas = new HashSet<>();
        palavrasOpinativas.addAll(extractOpinionWordsUsingR2_1(frase, aspects, lexicoExpandido));
        palavrasOpinativas.addAll(extractOpinionWordsUsingR2_2(frase, aspects, lexicoExpandido));
        return palavrasOpinativas;
    }

    public Set<String> extractAspectsUsingR3(Frase frase, Set<String> aspectos, Set<String> aspectosExtraidos) throws IOException {
        Set<String> novosAspectos = new HashSet<>();
        novosAspectos.addAll(extractAspectsUsingR3_1(frase, aspectos, aspectosExtraidos));
        novosAspectos.addAll(extractAspectsUsingR3_2(frase, aspectos, aspectosExtraidos));
        return novosAspectos;
    }

    public Set<String> extractOpinionWordsUsingR4(Frase frase, Set<String> opinionWords) {
        Set<String> palavrasOpinativas = new HashSet<>();
        palavrasOpinativas.addAll(extractOpinionWordsUsingR4_1(frase, opinionWords));
        palavrasOpinativas.addAll(extractOpinionWordsUsingR4_2(frase, opinionWords));
        return palavrasOpinativas;
    }

    private Set<String> extractAspectsUsingR1_1(Frase frase, Set<String> lexicoExpandido, Set<String> aspectosExtraidos) throws IOException {
        Set<String> novosAspectos = new HashSet<>();
        Relacao_Dependencia[] relacoes = frase.relacoes;
        for (Relacao_Dependencia relacao : relacoes) {
            Nodo governante = relacao.governante;
            Nodo dependente = relacao.dependente;
            if (MR.contains(relacao.relacao)) {
                String aspecto = null;
                if (lexicoExpandido.contains(dependente.lemma)
                        && NN.contains(governante.categoria)) {
                    aspecto = governante.texto;
                }
//                else if (lexicoExpandido.contains(governante.lemma)
//                        && NN.contains(dependente.categoria)) {
//                    aspecto = dependente.texto;
//                }
                if (aspecto != null && !aspectosExtraidos.contains(aspecto)) {
//                    System.out.println("-------------------------------------------------------------------------------------");
//                    System.out.println("ASPECTS 1.1");
//                    System.out.println("FRASE: " + frase.texto);
//                    System.out.println("RELAÇÃO: " + relacao.relacao + "\tGov: " + governante.texto + "\tDep: " + dependente.texto);
//                    System.out.println("ASPECTO: " + aspecto);
//                    System.out.println("-------------------------------------------------------------------------------------");
                    novosAspectos.add(aspecto);
                }
            } else {

            }
        }
        return novosAspectos;
    }

    private Set<String> extractAspectsUsingR1_2(Frase frase, Set<String> lexicoExpandido, Set<String> aspectosExtraidos) throws IOException {
        Set<String> novosAspectos = new HashSet<>();
        Relacao_Dependencia[] relacoes = frase.relacoes;
        for (int i = 0; i < relacoes.length; i++) {
            for (int j = 0; j < relacoes.length; j++) {
                if (i < j) {
                    Relacao_Dependencia relacao_i = relacoes[i];
                    Relacao_Dependencia relacao_j = relacoes[j];
                    if (relacao_i.governante.equals(relacao_j.governante)
                            && (MR.contains(relacao_i.relacao) && MR.contains(relacao_j.relacao))) {
                        String aspecto = null;
                        if (lexicoExpandido.contains(relacao_i.dependente.lemma) && NN.contains(relacao_j.dependente.categoria)) {
                            aspecto = relacao_j.dependente.texto;
                        } else if (lexicoExpandido.contains(relacao_j.dependente.lemma) && NN.contains(relacao_i.dependente.categoria)) {
                            aspecto = relacao_i.dependente.texto;
                        }
                        if (aspecto != null && !aspectosExtraidos.contains(aspecto)) {
//                            System.out.println("-------------------------------------------------------------------------------------");
//                            System.out.println("ASPECTS 1.2");
//                            System.out.println("FRASE: " + frase.texto);
//                            System.out.println("RELAÇÃO_I: " + relacao_i.relacao + "\tGov: " + relacao_i.governante.texto + "\tDep: " + relacao_i.dependente.texto);
//                            System.out.println("RELAÇÃO_J: " + relacao_j.relacao + "\tGov: " + relacao_j.governante.texto + "\tDep: " + relacao_j.dependente.texto);
//                            System.out.println("ASPECTO: " + aspecto);
//                            System.out.println("-------------------------------------------------------------------------------------");
                            novosAspectos.add(aspecto);
                        }
                    }
                }
            }
        }
        return novosAspectos;
    }

    private Set<String> extractOpinionWordsUsingR2_1(Frase frase, Set<String> aspects, Set<String> lexicoExpandido) {
        Set<String> palavrasOpinativasExtraidas = new HashSet<>();
        Relacao_Dependencia[] relacoes = frase.relacoes;
        for (Relacao_Dependencia relacao : relacoes) {
            if (MR.contains(relacao.relacao)) {
                String palavraOpinativa = null;
                if (aspects.contains(relacao.governante.texto) && JJ.contains(relacao.dependente.categoria)
                        && !lexicoExpandido.contains(relacao.dependente.texto)) {
                    palavraOpinativa = relacao.dependente.texto;
                } else if (aspects.contains(relacao.governante.texto) && JJ.contains(relacao.governante.categoria)
                        && !lexicoExpandido.contains(relacao.governante.texto)) {
                    palavraOpinativa = relacao.governante.texto;
                }
                if (palavraOpinativa != null) {
                    palavrasOpinativasExtraidas.add(palavraOpinativa);
                }
            }
        }
        return palavrasOpinativasExtraidas;
    }

    private Set<String> extractOpinionWordsUsingR2_2(Frase frase, Set<String> aspectos, Set<String> lexicoExpandido) {
        Set<String> palavrasOpinativasExtraidas = new HashSet<>();
        Relacao_Dependencia[] relacoes = frase.relacoes;
        for (int i = 0; i < relacoes.length; i++) {
            for (int j = 0; j < relacoes.length; j++) {
                if (i < j) {
                    Relacao_Dependencia relacao_i = relacoes[i];
                    Relacao_Dependencia relacao_j = relacoes[j];
                    if (relacao_i.governante.equals(relacao_j.governante)
                            && (MR.contains(relacao_i.relacao) && MR.contains(relacao_j.relacao))) {
                        String palavraOpinativa = null;
                        if (aspectos.contains(relacao_i.dependente.texto) && JJ.contains(relacao_j.dependente.categoria)) {
                            palavraOpinativa = relacao_j.dependente.texto;
                        } else if (aspectos.contains(relacao_j.dependente.texto) && JJ.contains(relacao_i.dependente.categoria)) {
                            palavraOpinativa = relacao_i.dependente.texto;
                        }
                        if (palavraOpinativa != null && !lexicoExpandido.contains(palavraOpinativa)) {
                            palavrasOpinativasExtraidas.add(palavraOpinativa);
                        }
                    }
                }
            }
        }
        return palavrasOpinativasExtraidas;
    }

    private Set<String> extractAspectsUsingR3_1(Frase frase, Set<String> aspects_i, Set<String> aspectsSet) throws IOException {
        Set<String> novosAspectos = new HashSet<>();
        Relacao_Dependencia[] relacoes = frase.relacoes;
        for (Relacao_Dependencia relacao : relacoes) {
            if (relacao.relacao == Tipo_Relacao.CONJ) {
                String aspecto = null;
                String targ_gov = relacao.governante.texto;
                String targ_dep = relacao.dependente.texto;
                if (aspects_i.contains(targ_gov) && NN.contains(relacao.dependente.categoria)) {
                    aspecto = targ_dep;
                } else if (aspects_i.contains(targ_dep) && NN.contains(relacao.governante.categoria)) {
                    aspecto = targ_gov;
                }
                if (aspecto != null && !aspectsSet.contains(aspecto)) {
//                    System.out.println("-------------------------------------------------------------------------------------");
//                    System.out.println("ASPECTS 3.1");
//                    System.out.println("FRASE: " + frase.texto);
//                    System.out.println("RELAÇÃO: " + relacao.relacao + "\tGov: " + targ_gov + "\tDep: " + targ_dep);
//                    System.out.println("ASPECTO: " + aspecto);
//                    System.out.println("-------------------------------------------------------------------------------------");
                    novosAspectos.add(aspecto);
                }
            }
        }
        return novosAspectos;
    }

    private Set<String> extractAspectsUsingR3_2(Frase frase, Set<String> aspects_i, Set<String> aspectsSet) throws IOException {
        Set<String> novosAspectos = new HashSet<>();
        Relacao_Dependencia[] relacoes = frase.relacoes;
        for (int i = 0; i < relacoes.length; i++) {
            Relacao_Dependencia relacao_i = relacoes[i];
            for (int j = 0; j < relacoes.length; j++) {
                if (i > j) {
                    Relacao_Dependencia relacao_j = relacoes[j];
                    if (relacao_i.governante.equals(relacao_j.governante)
                            && isRelacaoEquivalente(relacao_i, relacao_j)) {
                        String aspecto = null;
                        String tgt_dep_i = relacao_i.dependente.texto;
                        String tgt_dep_j = relacao_j.dependente.texto;
                        if (aspects_i.contains(tgt_dep_i) && NN.contains(relacao_j.dependente.categoria)) {
                            aspecto = tgt_dep_j;
                        } else if (aspects_i.contains(tgt_dep_j) && NN.contains(relacao_i.dependente.categoria)) {
                            aspecto = tgt_dep_i;
                        }
                        if (aspecto != null && !aspectsSet.contains(aspecto)) {
//                            System.out.println("-------------------------------------------------------------------------------------");
//                            System.out.println("ASPECTS 3.2");
//                            System.out.println("FRASE: " + frase.texto);
//                            System.out.println("RELAÇÃO_I: " + relacao_i.relacao + "\tGov: " + relacao_i.governante.texto + "\tDep: " + relacao_i.dependente.texto);
//                            System.out.println("RELAÇÃO_J: " + relacao_j.relacao + "\tGov: " + relacao_j.governante.texto + "\tDep: " + relacao_j.dependente.texto);
//                            System.out.println("ASPECTO: " + aspecto);
//                            System.out.println("-------------------------------------------------------------------------------------");
                            novosAspectos.add(aspecto);
                        }
                    }
                }
            }
        }
        return novosAspectos;
    }

    private Set<String> extractOpinionWordsUsingR4_1(Frase frase, Set<String> palavrasOpinativas) {
        Set<String> palavrasOpinativasExtraidas = new HashSet<>();
        Relacao_Dependencia[] relacoes = frase.relacoes;
        for (Relacao_Dependencia relacao : relacoes) {
            if (relacao.relacao == Tipo_Relacao.CONJ && palavrasOpinativas.contains(relacao.governante.texto)
                    && JJ.contains(relacao.dependente.categoria) && !palavrasOpinativas.contains(relacao.dependente.texto)) {
                palavrasOpinativasExtraidas.add(relacao.dependente.texto);
            }
        }
        return palavrasOpinativasExtraidas;
    }

    private Set<String> extractOpinionWordsUsingR4_2(Frase frase, Set<String> palavrasOpinativas) {
        Set<String> palavrasOpinativasExtraidas = new HashSet<>();
        Relacao_Dependencia[] relacoes = frase.relacoes;
        for (int i = 0; i < relacoes.length; i++) {
            for (int j = 0; j < relacoes.length; j++) {
                if (i < j) {
                    Relacao_Dependencia relacao_i = relacoes[i];
                    Relacao_Dependencia relacao_j = relacoes[j];
                    if (relacao_i.governante.equals(relacao_j.governante)
                            && isRelacaoEquivalente(relacao_i, relacao_j)) {
                        String palavraOpinativa = null;
                        if (palavrasOpinativas.contains(relacao_i.dependente.texto) && JJ.contains(relacao_j.dependente.categoria)) {
                            palavraOpinativa = relacao_j.dependente.texto;
                        } else if (palavrasOpinativas.contains(relacao_j.dependente.texto) && JJ.contains(relacao_i.dependente.categoria)) {
                            palavraOpinativa = relacao_i.dependente.texto;
                        }
                        if (palavraOpinativa != null && !palavrasOpinativas.contains(palavraOpinativa)) {
                            palavrasOpinativasExtraidas.add(palavraOpinativa);
                        }
                    }
                }
            }
        }
        return palavrasOpinativasExtraidas;
    }

    private boolean isRelacaoEquivalente(Relacao_Dependencia relacao_i, Relacao_Dependencia relacao_j) {
        boolean equivalente = false;
        if (relacao_i.relacao != Tipo_Relacao.OUTROS && relacao_j.relacao != Tipo_Relacao.OUTROS) {
            if (relacao_i.relacao == relacao_j.relacao) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.NSUBJ && relacao_j.relacao == Tipo_Relacao.DOBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.DOBJ && relacao_j.relacao == Tipo_Relacao.NSUBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.XSUBJ && relacao_j.relacao == Tipo_Relacao.DOBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.DOBJ && relacao_j.relacao == Tipo_Relacao.XSUBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.CSUBJ && relacao_j.relacao == Tipo_Relacao.DOBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.DOBJ && relacao_j.relacao == Tipo_Relacao.CSUBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.NSUBJ && relacao_j.relacao == Tipo_Relacao.IOBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.IOBJ && relacao_j.relacao == Tipo_Relacao.NSUBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.XSUBJ && relacao_j.relacao == Tipo_Relacao.IOBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.IOBJ && relacao_j.relacao == Tipo_Relacao.XSUBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.CSUBJ && relacao_j.relacao == Tipo_Relacao.IOBJ) {
                equivalente = true;
            } else if (relacao_i.relacao == Tipo_Relacao.IOBJ && relacao_j.relacao == Tipo_Relacao.CSUBJ) {
                equivalente = true;
            }
        }
        return equivalente;
    }

}
