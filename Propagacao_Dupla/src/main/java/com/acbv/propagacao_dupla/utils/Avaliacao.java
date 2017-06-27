/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acbv.propagacao_dupla.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author arthur
 */
public class Avaliacao {

    public double recall, precision, f_measure;

    public Avaliacao(Set relevant, Set retrieved) {
        double n_doc_ret = retrieved.size();
        double n_doc_rel = relevant.size();
        List rel_ret = new ArrayList<>(relevant);
        rel_ret.retainAll(retrieved);
        double n_doc_rel_ret = rel_ret.size();
        this.precision = n_doc_rel_ret / n_doc_ret;
        this.recall = n_doc_rel_ret / n_doc_rel;
        this.f_measure = 2 * ((precision * recall) / (precision + recall));
    }
    
    @Override
    public String toString() {
        return "Avaliacao{" + "recall=" + recall + ", precision=" + precision + ", f_measure=" + f_measure + '}';
    }

}
