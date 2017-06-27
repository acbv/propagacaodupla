/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.preprocessamento;

import com.acbv.propagacao_dupla.entidades.Categoria_Sintatica;
import com.acbv.propagacao_dupla.entidades.Frase;
import com.acbv.propagacao_dupla.entidades.Nodo;
import com.acbv.propagacao_dupla.entidades.Relacao_Dependencia;
import com.acbv.propagacao_dupla.entidades.Resenha;
import com.acbv.propagacao_dupla.entidades.Tipo_Relacao;
import com.acbv.propagacao_dupla.utils.StemmerUtils;
import com.acbv.propagacao_dupla.utils.Utils;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Arthur
 */
public class StanfordUtils {

    protected MaxentTagger maxEntTagger;
    protected DependencyParser depParser;
    protected StemmerUtils stemmerUtils;
//    protected Set<String> sets;

    @SuppressWarnings("")
    public StanfordUtils() {
        config();
    }

    public List<Resenha> getResenhas(List<String> corpus) throws Exception {
//        sets = new HashSet<>();
        List<Resenha> resenhas = new ArrayList<>();
        Resenha resenha;
        for (String texto : corpus) {
            resenha = this.parse(texto);
            resenhas.add(resenha);
        }
//        for (String set : sets) {
//            System.out.println(set);
//        }
        return resenhas;
    }

    private Relacao_Dependencia[] toRelacaoDependencia(List<TaggedWord> tagged) {
        List<TypedDependency> typedDependencies = getTypedDependencies(tagged);
        List<Relacao_Dependencia> relacoes_dependencia = new ArrayList<>();
        Relacao_Dependencia relacao_dependencia;
        for (TypedDependency typedDependency : typedDependencies) {
            String relacao = typedDependency.reln().toString();
            if (!relacao.equalsIgnoreCase("root")) {
                Tipo_Relacao tipo_relacao = getTipoRelacao(relacao);
                String dependente_nodo = typedDependency.dep().word();
                String tag_dependente = typedDependency.dep().tag();
                Categoria_Sintatica dependente_categoria = getCategoriaSintatica(tag_dependente);
                String dependente_lemma = typedDependency.dep().word() != null ? (typedDependency.dep().word()) : "";
                String governante_nodo = typedDependency.gov().word();
                String governante_tag = typedDependency.gov().tag();
                Categoria_Sintatica governante_categoria = getCategoriaSintatica(governante_tag);
                String governante_lemma = typedDependency.gov().word() != null ? (typedDependency.gov().word()) : "";
                Nodo governante = new Nodo(governante_nodo, governante_categoria);
                governante.lemma = governante_lemma;
                Nodo dependente = new Nodo(dependente_nodo, dependente_categoria);
                dependente.lemma = dependente_lemma;
                relacao_dependencia = new Relacao_Dependencia(governante, dependente, tipo_relacao);
                relacoes_dependencia.add(relacao_dependencia);
            }
//            else {
//                System.out.println("DEP: "+typedDependency.dep().word());
//                System.out.println("GOV: "+typedDependency.gov().word());
//            }
        }
        Relacao_Dependencia[] outcome = new Relacao_Dependencia[relacoes_dependencia.size()];
        outcome = relacoes_dependencia.toArray(outcome);
        return outcome;
    }

    private List<TypedDependency> getTypedDependencies(List<TaggedWord> tagged) {
        GrammaticalStructure gs = this.depParser.predict(tagged);
        Collection<TypedDependency> tdl = gs.typedDependenciesCollapsedTree();
        List<TypedDependency> td = new ArrayList<>(tdl);
        return td;
    }

    @SuppressWarnings("")
    private void config() {
        if (this.maxEntTagger == null) {
            this.maxEntTagger = (MaxentTagger) MaxentTagger.loadModel("/home/arthur/Downloads/pt-model (2)/pos-tagger.dat");
//            MaxentTagger.lemmatize(sentence, morpha);
        }
        if (this.depParser == null) {
            this.depParser = DependencyParser.loadFromModelFile("/home/arthur/Downloads/pt-model/dep-parser");
        }
        if (this.stemmerUtils == null) {
            this.stemmerUtils = new StemmerUtils();
        }
    }

    @SuppressWarnings("")
    public Resenha parse(String texto) throws Exception {
        List<Frase> frases = getFrases(texto);
        Frase[] resenha_frases = frases.toArray(new Frase[frases.size()]);
        Resenha resenha = new Resenha(resenha_frases, texto);
        resenha.texto_pre_processado = this.stemmerUtils.stemmFrase(texto);
        return resenha;
    }

    private List<Frase> getFrases(String texto) throws Exception {
        List<Frase> frases = new ArrayList<>();
        List<List<HasWord>> tokenizedText = MaxentTagger.tokenizeText(new StringReader(texto));
        Frase frase;
        for (List<HasWord> hwSentence : tokenizedText) {
            List<TaggedWord> taggedSentence = (ArrayList<TaggedWord>) this.maxEntTagger.tagSentence(hwSentence);
            taggedSentence = preProccess(taggedSentence);
            List<Nodo> nodosFrase = new ArrayList<>();
            String fraseStr = "";
            for (TaggedWord taggedWord : taggedSentence) {
                String palavra = taggedWord.word();
                String tag = taggedWord.tag();
                Categoria_Sintatica categoria_sintatica = getCategoriaSintatica(tag);
//                System.out.println("NODO: " + palavra + "\tCLASSE GRAMATICAL: " + tag);
                Nodo nodo = new Nodo(palavra, categoria_sintatica);
                nodo.lemma = lemmatizar(palavra);
                if (fraseStr.isEmpty()) {
                    fraseStr = palavra;
                } else {
                    fraseStr = fraseStr.concat(" " + palavra);
                }
                nodosFrase.add(nodo);
            }
//            System.out.println(fraseStr);
            Relacao_Dependencia[] relacoes_dep = toRelacaoDependencia(taggedSentence);
            frase = new Frase(fraseStr, relacoes_dep);
//            Oracao[] oracoes = getOracoes(fraseStr);
//            frase.oracoes = oracoes
            frase.nodos = nodosFrase.toArray(new Nodo[nodosFrase.size()]);
            frase.texto_pre_processado = this.stemmerUtils.stemmFrase(fraseStr);
            frases.add(frase);
        }
        return frases;
    }

    private String lemmatizar(String frase) {
        return frase;
    }

    private List<TaggedWord> preProccess(List<TaggedWord> tagged) {
        for (int i = 0; i < tagged.size(); i++) {
            TaggedWord taggedWord = tagged.get(i);
            String tag = taggedWord.tag();
            String word = taggedWord.word();
            if (word.equalsIgnoreCase("-RRB-")
                    || word.equalsIgnoreCase("-LRB-")) {
                tag = ".";
            }
            word = word.equalsIgnoreCase("-RRB-") ? ")"
                    : word.equalsIgnoreCase("-LRB-") ? "("
                    : word;
            word = Utils.removerAcentos(word);
            taggedWord.setFromString(word);
            taggedWord.setTag(tag);
            tagged.set(i, taggedWord);
        }
        return tagged;
    }

    public static Categoria_Sintatica getCategoriaSintatica(String tag) {
        Categoria_Sintatica categoria_sintatica;
        if (tag.equalsIgnoreCase("ADJ")) {
            categoria_sintatica = Categoria_Sintatica.JJ;
        } else if (tag.contains("NOUN")) {
            categoria_sintatica = Categoria_Sintatica.NN;
        } else if (tag.contains("DET")) {
            categoria_sintatica = Categoria_Sintatica.DT;
        } else if (tag.contains("VERB")) {
            categoria_sintatica = Categoria_Sintatica.VB;
        } else if (tag.contains("ADP")) {
            categoria_sintatica = Categoria_Sintatica.PREP;
        } else {
            categoria_sintatica = Categoria_Sintatica.OUTRAS;
        }
        return categoria_sintatica;
    }

    public Tipo_Relacao getTipoRelacao(String relacao) {
        relacao = relacao.toUpperCase();
        Tipo_Relacao tipoRelacao = Tipo_Relacao.getTipoRelacao(relacao);
        return tipoRelacao;
//        Tipo_Relacao tipo_relacao;
//        if (relacao.contains("CONJ")) {
//            tipo_relacao = Tipo_Relacao.CONJ;
//        } else if (relacao.contains("AMOD")) {
//            tipo_relacao = Tipo_Relacao.AMOD;
//        } else if (relacao.contains("NMOD")) {
//            tipo_relacao = Tipo_Relacao.NMOD;
//        } else if (relacao.contains("CSUBJ")) {
//            tipo_relacao = Tipo_Relacao.CSUBJ;
//        } else if (relacao.contains("DOBJ")) {
//            tipo_relacao = Tipo_Relacao.DOBJ;
//        } else if (relacao.contains("IOBJ")) {
//            tipo_relacao = Tipo_Relacao.IOBJ;
//        } else if (relacao.contains("NSUBJ")) {
//            tipo_relacao = Tipo_Relacao.NSUBJ;
//        } else if (relacao.contains("PREP")) {
//            tipo_relacao = Tipo_Relacao.PREP;
//        } else if (relacao.contains("XSUBJ")) {
//            tipo_relacao = Tipo_Relacao.XSUBJ;
//        } else {
//            sets.add(relacao);
//            tipo_relacao = Tipo_Relacao.OUTROS;
//        }
//        return tipo_relacao;
    }

    @Deprecated
    private List<String> sentenceSplitter(String documentText) {
        List<String> sentences = new ArrayList<>();
        DocumentPreprocessor docPreProc = new DocumentPreprocessor(documentText);
        String sentence;
        for (List<HasWord> list : docPreProc) {
            String[] tokens = (String[]) list.toArray();
            sentence = "";
            for (String token : tokens) {
                if (sentence.isEmpty()) {
                    sentence = token;
                } else {
                    sentence = sentence.concat(" " + token);
                }
            }
            sentences.add(sentence);
        }
        return sentences;
    }

}
