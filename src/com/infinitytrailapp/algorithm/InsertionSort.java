/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infinitytrailapp.algorithm;

import com.infinitytrailapp.model.CandidateModel;
import java.time.LocalDate;
import java.util.List;

/**
 * Implements insertion sort to sort candidates by date of birth.
 *
 * @author Bimarsha
 */
public class InsertionSort {

    /**
     * Sorts a list of candidates by date of birth using insertion sort.
     *
     * @param candidates the list of candidates to sort
     * @param ascending true for ascending order, false for descending order
     */
    public static void insertionSortDate(List<CandidateModel> candidates, boolean ascending) {
        for (int i = 1; i < candidates.size(); i++) {
            CandidateModel key = candidates.get(i);
            LocalDate keyDate = LocalDate.parse(key.getDateOfBirth());
            int j = i - 1;

            while (j >= 0) {
                if (ascending) {
                    if (LocalDate.parse(candidates.get(j).getDateOfBirth()).isAfter(keyDate)) {
                        candidates.set(j + 1, candidates.get(j));
                        j--;
                    } else {
                        break; // Exit the loop when the condition is not met
                    }
                } else { // Descending order
                    if (LocalDate.parse(candidates.get(j).getDateOfBirth()).isBefore(keyDate)) {
                        candidates.set(j + 1, candidates.get(j));
                        j--;
                    } else {
                        break; // Exit the loop when the condition is not met
                    }
                }
            }
            candidates.set(j + 1, key);
        }
    }
}
