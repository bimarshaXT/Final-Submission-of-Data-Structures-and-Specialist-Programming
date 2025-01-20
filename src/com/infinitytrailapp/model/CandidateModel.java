package com.infinitytrailapp.model;

/**
 * Represents a candidate in the Infinity Trail application. Contains all
 * relevant details about the candidate.
 *
 * @author Bimarsha
 */
public class CandidateModel {

    private int CandidateNo;
    private String Name;
    private String Contact;
    private String Category;
    private String Type;
    private String CitizenshipNo;
    private String DateOfBirth;

    /**
     * Default constructor for CandidateModel.
     */
    public CandidateModel() {
    }

    /**
     * Parameterized constructor for CandidateModel.
     *
     * @param CandidateNo the candidate's unique identification number
     * @param Name the full name of the candidate
     * @param Contact the contact information of the candidate
     * @param Category the category applied for by the candidate
     * @param Type the type of application (e.g., new license, retrial)
     * @param CitizenshipNo the candidate's citizenship number
     * @param DateOfBirth the candidate's date of birth
     */
    public CandidateModel(int CandidateNo, String Name, String Contact, String Category, String Type, String CitizenshipNo, String DateOfBirth) {
        this.CandidateNo = CandidateNo;
        this.Name = Name;
        this.Contact = Contact;
        this.Category = Category;
        this.Type = Type;
        this.CitizenshipNo = CitizenshipNo; // Added
        this.DateOfBirth = DateOfBirth;     // Added
    }

    /**
     * Gets the candidate's unique identification number.
     * 
     * @return the candidate's ID number
     */
    public int getCandidateNo() {
        return CandidateNo;
    }

    /**
     * Sets the candidate's unique identification number.
     * 
     * @param CandidateNo the new ID number for the candidate
     */
    public void setCandidateNo(int CandidateNo) {
        this.CandidateNo = CandidateNo;
    }

    /**
     * Gets the full name of the candidate.
     * 
     * @return the candidate's name
     */
    public String getName() {
        return Name;
    }

    /**
     * Sets the full name of the candidate.
     * 
     * @param Name the new name for the candidate
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * Gets the contact information of the candidate.
     * 
     * @return the candidate's contact details
     */
    public String getContact() {
        return Contact;
    }

    /**
     * Sets the contact information of the candidate.
     * 
     * @param Contact the new contact details for the candidate
     */
    public void setContact(String Contact) {
        this.Contact = Contact;
    }

    /**
     * Gets the category applied for by the candidate.
     * 
     * @return the category
     */
    public String getCategory() {
        return Category;
    }

    /**
     * Sets the category applied for by the candidate.
     * 
     * @param Category the new category for the candidate
     */
    public void setCategory(String Category) {
        this.Category = Category;
    }

    /**
     * Gets the type of application submitted by the candidate.
     * 
     * @return the application type
     */
    public String getType() {
        return Type;
    }

    /**
     * Sets the type of application submitted by the candidate.
     * 
     * @param Type the new application type for the candidate
     */
    public void setType(String Type) {
        this.Type = Type;
    }

    /**
     * Gets the citizenship number of the candidate.
     * 
     * @return the candidate's citizenship number
     */
    public String getCitizenshipNo() {
        return CitizenshipNo;
    }

    /**
     * Sets the citizenship number of the candidate.
     * 
     * @param CitizenshipNo the new citizenship number for the candidate
     */
    public void setCitizenshipNo(String CitizenshipNo) {
        this.CitizenshipNo = CitizenshipNo;
    }

    /**
     * Gets the date of birth of the candidate.
     * 
     * @return the candidate's date of birth
     */
    public String getDateOfBirth() {
        return DateOfBirth;
    }

    /**
     * Sets the date of birth of the candidate.
     * 
     * @param DateOfBirth the new date of birth for the candidate
     */
    public void setDateOfBirth(String DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }
}
