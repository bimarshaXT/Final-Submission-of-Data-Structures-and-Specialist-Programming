package com.infinitytrailapp.controller;

/**
 * Handles the processing of candidates in a queue for trials. Includes methods
 * to enqueue candidates, process trials, and manage results.
 *
 * @author Bimarsha
 */
import com.infinitytrailapp.model.CandidateModel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javax.swing.table.DefaultTableModel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javax.swing.table.DefaultTableModel;

public class CandidateQueueProcessor {

    private Queue<CandidateModel> trialQueue;
    private DefaultTableModel resultsTableModel;

    /**
     * Constructs a CandidateQueueProcessor with a results table model.
     *
     * @param resultsTableModel the table model where trial results are
     * displayed
     */
    public CandidateQueueProcessor(DefaultTableModel resultsTableModel) {
        this.trialQueue = new LinkedList<>();
        this.resultsTableModel = resultsTableModel;
    }

    /**
     * Adds a candidate to the trial queue.
     *
     * @param candidate the candidate to add to the queue
     * @throws IllegalArgumentException if the candidate is null
     */
    public void enqueueCandidate(CandidateModel candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate cannot be null.");
        }
        trialQueue.add(candidate);
    }

    /**
     * Retrieves the next candidate in the queue without removing them.
     * 
     * @return the next candidate in the queue
     * @throws IllegalStateException if the queue is empty
     */
    public CandidateModel peekCandidate() {
        if (trialQueue.isEmpty()) {
            throw new IllegalStateException("No candidates in the queue.");
        }
        return trialQueue.peek();
    }

    /**
     * Processes the next candidate in the queue, determines their trial result, and updates the results table.
     * Generates a random status ("Passed" or "Failed") and assigns a license number if passed.
     * 
     * @throws IllegalStateException if the queue is empty
     */
    public void startTrial() {
        if (trialQueue.isEmpty()) {
            throw new IllegalStateException("No candidates to process.");
        }

        Random random = new Random();
        CandidateModel candidate = trialQueue.poll(); // Remove the first candidate

        String status = random.nextBoolean() ? "Passed" : "Failed";
        String licenseNumber = status.equals("Passed")
                ? String.valueOf(100000 + random.nextInt(900000))
                : "NA";

        // Add the result to the table
        resultsTableModel.addRow(new Object[]{
            candidate.getCandidateNo(),
            candidate.getName(),
            candidate.getContact(),
            candidate.getCategory(),
            candidate.getType(),
            candidate.getCitizenshipNo(),
            candidate.getDateOfBirth(),
            status,
            licenseNumber
        });
    }

    /**
     * Checks if the trial queue is empty.
     * 
     * @return true if the queue is empty, false otherwise
     */
    public boolean isQueueEmpty() {
        return trialQueue.isEmpty();
    }
}
