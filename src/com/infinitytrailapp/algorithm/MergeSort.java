/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infinitytrailapp.algorithm;

import com.infinitytrailapp.model.CandidateModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements merge sort to sort candidates by name.
 *
 * @author Bimarsha
 */
public class MergeSort {

    /**
     * Sorts a list of candidates by name using merge sort.
     *
     * @param candidates the list of candidates to sort
     * @param ascending true for ascending order, false for descending order
     */
    public static void sortNames(List<CandidateModel> candidates, boolean ascending) {
        if (candidates.size() <= 1) {
            return;
        }

        // Split the list into two halves
        int mid = candidates.size() / 2;
        List<CandidateModel> first = new ArrayList<>();
        List<CandidateModel> second = new ArrayList<>();

        for (int i = 0; i < mid; i++) {
            first.add(candidates.get(i));
        }
        for (int i = mid; i < candidates.size(); i++) {
            second.add(candidates.get(i));
        }

        // Sort each half recursively
        sortNames(first, ascending);
        sortNames(second, ascending);

        // Merge the sorted halves
        mergeNames(first, second, candidates, ascending);
    }

    /**
     * Merges two sorted lists of candidates into a single sorted list.
     *
     * @param first the first sorted list
     * @param second the second sorted list
     * @param result the list to store the merged result
     * @param ascending true for ascending order, false for descending order
     */
    private static void mergeNames(List<CandidateModel> first, List<CandidateModel> second, List<CandidateModel> result, boolean ascending) {
        int firstIndex = 0, secondIndex = 0, resultIndex = 0;

        // Compare and merge elements from both halves
        while (firstIndex < first.size() && secondIndex < second.size()) {
            int comparison = first.get(firstIndex).getName().compareToIgnoreCase(second.get(secondIndex).getName());
            if (ascending) {
                if (comparison <= 0) {
                    result.set(resultIndex++, first.get(firstIndex++));
                } else {
                    result.set(resultIndex++, second.get(secondIndex++));
                }
            } else {
                if (comparison > 0) {
                    result.set(resultIndex++, first.get(firstIndex++));
                } else {
                    result.set(resultIndex++, second.get(secondIndex++));
                }
            }
        }

        // Add remaining elements from the first half
        while (firstIndex < first.size()) {
            result.set(resultIndex++, first.get(firstIndex++));
        }

        // Add remaining elements from the second half
        while (secondIndex < second.size()) {
            result.set(resultIndex++, second.get(secondIndex++));
        }
    }
}
