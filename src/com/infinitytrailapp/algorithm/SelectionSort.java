/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infinitytrailapp.algorithm;

import com.infinitytrailapp.model.CandidateModel;
import java.util.List;

/**
 * Implements selection sort to sort candidates by candidate number.
 *
 * @author Bimarsha
 */
public class SelectionSort {

    /**
     * Sorts a list of candidates by candidate number using selection sort.
     *
     * @param candidates the list of candidates to sort
     * @param ascending true for ascending order, false for descending order
     * @return the sorted list of candidates
     */
    public static List<CandidateModel> selectionSort(List<CandidateModel> candidates, boolean ascending) {
        for (int i = 0; i < candidates.size() - 1; i++) {
            int index = i;
            for (int j = i + 1; j < candidates.size(); j++) {
                if (ascending) {
                    if (candidates.get(j).getCandidateNo() < candidates.get(index).getCandidateNo()) {
                        index = j;
                    }
                } else {
                    if (candidates.get(j).getCandidateNo() > candidates.get(index).getCandidateNo()) {
                        index = j;
                    }
                }
            }
            CandidateModel temp = candidates.get(index);
            candidates.set(index, candidates.get(i));
            candidates.set(i, temp);
        }
        return candidates;
    }
}
