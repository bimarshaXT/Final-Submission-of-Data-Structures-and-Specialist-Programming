package com.infinitytrailapp.controller;

import java.util.regex.Pattern;
import java.util.Map;

/**
 * Utility class for validation with updated logic and optimized methods.
 */
public class ValidationUtil {

    // Updated patterns
    private static final Pattern CANDIDATE_NO_PATTERN = Pattern.compile("^(1[1-9]\\d{2,3})$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Z][a-z]+([-'][A-Z][a-z]+)?(\\s[A-Z][a-z]+([-'][A-Z][a-z]+)?){0,2}$"); // Supports hyphens and apostrophes
    private static final Pattern CONTACT_PATTERN = Pattern.compile("^(98|97)\\d{8}$"); // Starts with 98 or 97, 10 digits
    private static final Pattern DOB_PATTERN = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$"); // yyyy-mm-dd format
    private static final Pattern CITIZENSHIP_NO_PATTERN = Pattern.compile("^\\d{1,4}(-\\d{1,4}){2,3}$"); // Numbers with 3-4 hyphens

 

    /**
     * Validates if a string is null or empty.
     *
     * @param value the string to validate
     * @return true if the string is null or empty, otherwise false
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates the candidate number.
     *
     * @param candidateNo the candidate number to validate
     * @return true if valid and unique, otherwise false
     */
    public static boolean isValidCandidateNo(String candidateNo) {
        if (isNullOrEmpty(candidateNo) || !CANDIDATE_NO_PATTERN.matcher(candidateNo).matches()) {
            return false;
        }
        return true; // Removed uniqueness check for updates
    }

    /**
     * Validates the name.
     *
     * @param name the name to validate
     * @return true if valid, otherwise false
     */
    public static boolean isValidName(String name) {
        return !isNullOrEmpty(name) && NAME_PATTERN.matcher(name).matches();
    }

    /**
     * Validates the contact number.
     *
     * @param contact the contact number to validate
     * @return true if valid and unique, otherwise false
     */
    public static boolean isValidContact(String contact) {
        if (isNullOrEmpty(contact) || !CONTACT_PATTERN.matcher(contact).matches()) {
            return false;
        }
        return true; // Removed uniqueness check for updates
    }

    /**
     * Validates the date of birth.
     *
     * @param dob the date of birth to validate
     * @return true if valid, otherwise false
     */
    public static boolean isValidDateOfBirth(String dob) {
        if (isNullOrEmpty(dob) || !DOB_PATTERN.matcher(dob).matches()) {
            return false;
        }
        return true;
    }

    /**
     * Validates the citizenship number.
     *
     * @param citizenshipNo the citizenship number to validate
     * @return true if valid, otherwise false
     */
    public static boolean isValidCitizenshipNo(String citizenshipNo) {
        if (isNullOrEmpty(citizenshipNo) || !CITIZENSHIP_NO_PATTERN.matcher(citizenshipNo).matches()) {
            return false;
        }
        return true;
    }

    /**
     * Validates the category.
     *
     * @param category the category to validate
     * @return true if not null or empty, otherwise false
     */
    public static boolean isValidCategory(String category) {
        return category != null && !category.trim().isEmpty();
    }

    /**
     * Validates the type.
     *
     * @param type the type to validate
     * @return true if not null or empty, otherwise false
     */
    public static boolean isValidType(String type) {
        return type != null && !type.trim().isEmpty();
    }
}
