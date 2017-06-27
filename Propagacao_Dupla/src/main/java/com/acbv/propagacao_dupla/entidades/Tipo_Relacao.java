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
public enum Tipo_Relacao {

    AMOD("AMOD"),
    NMOD("NMOD"),
    PREP("PREP"),
    NSUBJ("NSUBJ"),
    NSUBJPASS("NSUBJPASS"),
    CSUBJ("CSUBJ"),
    XSUBJ("XSUBJ"),
    DOBJ("DOBJ"),
    IOBJ("IOBJ"),
    CONJ("CONJ"),
    COMPMOD("COMPMOD"),
    PARTMOD("PARTMOD"),
    ADVCL("ADVCL"),
    AUXPASS("AUXPASS"),
    ADP("ADP"),
    P("P"),
    XCOMP("XCOMP"),
    ADPCOMP("ADPCOMP"),
    MWE("MWE"),
    APPOS("APPOS"),
    CCOMP("CCOMP"),
    CC("CC"),
    PARATAXIS("PARATAXIS"),
    ACOMP("ACOMP"),
    ADPOBJ("ADPOBJ"),
    AUX("AUX"),
    NUM("NUM"),
    MARK("MARK"),
    ATTR("ATTR"),
    NEG("NEG"),
    DET("DET"),
    RCMOD("RCMOD"),
    POSS("POSS"),
    DEP("DEP"),
    PRT("PRT"),
    ADPMOD("ADPMOD"),
    ADVMOD("ADVMOD"),
    OUTROS("OUTROS");

    private final String desc;

    Tipo_Relacao(String desc) {
        this.desc = desc;
    }

    private String getDesc() {
        return this.desc;
    }

    public static Tipo_Relacao getTipoRelacao(String tag) {
        if (tag.contains("CONJ")) {
            return CONJ;
        } else {
            for (Tipo_Relacao tipo : values()) {
                if (tipo.getDesc().equalsIgnoreCase(tag)) {
                    return tipo;
                }
            }
            System.out.println(tag);
            return OUTROS;
        }
    }

}
