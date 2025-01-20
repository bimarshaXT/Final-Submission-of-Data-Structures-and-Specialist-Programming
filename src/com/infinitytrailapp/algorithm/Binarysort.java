/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.infinitytrailapp.algorithm;

import com.infinitytrailapp.model.CandidateModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides binary search algorithms to find candidates by various criteria.
 *
 * @author Bimarsha
 */
public class Binarysort {

    /**
     * Performs binary search to find a candidate by candidate number.
     *
     * @param candidateNo the candidate number to search for
     * @param candidates the sorted list of candidates
     * @param left the starting index of the search range
     * @param right the ending index of the search range
     * @return the matching CandidateModel or null if not found
     */
    public static CandidateModel binarySearchByCandidateNo(int candidateNo, List<CandidateModel> candidates, int left, int right) {
        if (right < left) {
            return null;
        }

        int mid = (left + right) / 2;

        if (candidates.get(mid).getCandidateNo() == candidateNo) {
            return candidates.get(mid);
        } else if (candidateNo < candidates.get(mid).getCandidateNo()) {
            return binarySearchByCandidateNo(candidateNo, candidates, left, mid - 1);
        } else {
            return binarySearchByCandidateNo(candidateNo, candidates, mid + 1, right);
        }
    }

    /**
     * Performs binary search to find candidates by name. Expands to find all
     * candidates with the same name.
     *
     * @param name the name to search for
     * @param candidates the sorted list of candidates
     * @param left the starting index of the search range
     * @param right the ending index of the search range
     * @return a list of matching CandidateModel objects
     */
    public static List<CandidateModel> binarySearchByName(String name, List<CandidateModel> candidates, int left, int right) {
        if (right < left) {
            return new ArrayList<>(); // Return an empty list if not found
        }

        int mid = (left + right) / 2;

        int comparison = name.compareToIgnoreCase(candidates.get(mid).getName());

        if (comparison == 0) {
            // Collect all matching candidates
            List<CandidateModel> result = new ArrayList<>();
            result.add(candidates.get(mid));

            // Expand to the left
            int leftPointer = mid - 1;
            while (leftPointer >= left && name.equalsIgnoreCase(candidates.get(leftPointer).getName())) {
                result.add(0, candidates.get(leftPointer)); // Add at the start to maintain order
                leftPointer--;
            }

            // Expand to the right
            int rightPointer = mid + 1;
            while (rightPointer <= right && name.equalsIgnoreCase(candidates.get(rightPointer).getName())) {
                result.add(candidates.get(rightPointer));
                rightPointer++;
            }

            return result;
        } else if (comparison < 0) {
            return binarySearchByName(name, candidates, left, mid - 1);
        } else {
            return binarySearchByName(name, candidates, mid + 1, right);
        }
    }

}
