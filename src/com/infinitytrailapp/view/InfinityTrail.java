package com.infinitytrailapp.view;

import com.infinitytrailapp.algorithm.Binarysort;
import com.infinitytrailapp.algorithm.InsertionSort;
import com.infinitytrailapp.algorithm.MergeSort;
import com.infinitytrailapp.algorithm.SelectionSort;
import com.infinitytrailapp.controller.CandidateQueueProcessor;
import com.infinitytrailapp.controller.ValidationUtil;
import com.infinitytrailapp.model.CandidateModel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Main class for the Infinity Trail application. Handles UI components and user
 * interactions.
 *
 * @author Bimarsha
 */
public class InfinityTrail extends javax.swing.JFrame {

    private List<CandidateModel> CandidateList;
    private List<CandidateModel> originalCandidateList;
    private java.awt.CardLayout cardLayout;
    private final Color errorColor = new Color(255, 51, 0);
    private final Color yellowColor = new Color(0, 0, 0);
    private CandidateQueueProcessor queueProcessor;

    /**
     * Creates new form InfinityTrail.
     */
    public InfinityTrail() {
        initComponents();
        initializeLayout(); // Set up CardLayout and add screens
        queueProcessor = new CandidateQueueProcessor((DefaultTableModel) tblResults.getModel());
        initializeData(); // Initialize student data and table
        startProgress(); // Show loading screen and initiate progress

        initializeTrialQueue();
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show a confirmation dialog
                int response = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to clear the form?",
                        "Confirm Clear",
                        JOptionPane.YES_NO_OPTION
                );

                // If the user clicks "Yes", clear the form
                if (response == JOptionPane.YES_OPTION) {
                    clearCandidateForm(); // Call the method to clear the form
                }
            }
        });
        btnAscending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String selectedItem = (String) ComboSort.getSelectedItem();
                if ("Candidate No".equals(selectedItem)) {
                    CandidateList = SelectionSort.selectionSort(CandidateList, true);
                    loadListToTable(CandidateList); // Refresh the table
                } else if ("Name".equals(selectedItem)) {
                    MergeSort.sortNames(CandidateList, true);
                    loadListToTable(CandidateList);
                } else if ("Date Of Birth".equals(selectedItem)) {
                    InsertionSort.insertionSortDate(CandidateList, true);
                    loadListToTable(CandidateList);
                }
            }
        });

        btnDescending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String selectedItem = (String) ComboSort.getSelectedItem();
                if ("Candidate No".equals(selectedItem)) {
                    CandidateList = SelectionSort.selectionSort(CandidateList, false);
                    loadListToTable(CandidateList); // Refresh the table
                } else if ("Name".equals(selectedItem)) {
                    MergeSort.sortNames(CandidateList, false);
                    loadListToTable(CandidateList);
                } else if ("Date Of Birth".equals(selectedItem)) {
                    InsertionSort.insertionSortDate(CandidateList, false);
                    loadListToTable(CandidateList);
                }
            }
        });

        btnSearch.addActionListener(evt -> {
            String searchValue = txtBinarySearch.getText().trim();
            String searchCriteria = (String) jComboBox1.getSelectedItem();

            if (searchValue.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Error: Please enter a value to search", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            switch (searchCriteria) {
                case "Candidate ID":
                    try {
                        int candidateNo = Integer.parseInt(searchValue);
                        SelectionSort.selectionSort(CandidateList, true); // Ensure sorted list
                        CandidateModel result = Binarysort.binarySearchByCandidateNo(candidateNo, CandidateList, 0, CandidateList.size() - 1);

                        if (result != null) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Candidate Found:\n"
                                    + "Candidate No: " + result.getCandidateNo() + "\n"
                                    + "Name: " + result.getName() + "\n"
                                    + "Contact: " + result.getContact() + "\n"
                                    + "Category: " + result.getCategory() + "\n"
                                    + "Type: " + result.getType() + "\n"
                                    + "Citizenship No: " + result.getCitizenshipNo() + "\n"
                                    + "Date of Birth: " + result.getDateOfBirth(),
                                    "Search Result",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(null, "CandidateNo Not Found.", "Search Result", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid Candidate ID format.", "Invalid Format", JOptionPane.WARNING_MESSAGE);
                    }
                    break;

                case "Name":
                    MergeSort.sortNames(CandidateList, true); // Ensure sorted list
                    List<CandidateModel> results = Binarysort.binarySearchByName(searchValue, CandidateList, 0, CandidateList.size() - 1);

                    if (!results.isEmpty()) {
                        StringBuilder message = new StringBuilder("Candidates Found:\n");

                        for (CandidateModel candidate : results) {
                            message.append(
                                    "Candidate No: " + candidate.getCandidateNo() + "\n"
                                    + "Name: " + candidate.getName() + "\n"
                                    + "Contact: " + candidate.getContact() + "\n"
                                    + "Category: " + candidate.getCategory() + "\n"
                                    + "Type: " + candidate.getType() + "\n"
                                    + "Citizenship No: " + candidate.getCitizenshipNo() + "\n"
                                    + "Date of Birth: " + candidate.getDateOfBirth() + "\n\n"
                            );
                        }

                        JOptionPane.showMessageDialog(
                                null,
                                message.toString(),
                                "Search Results",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(null, "No Candidates Found with the given name.", "Search Result", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Error: Please select a valid search criterion.", "Invalid Selection", JOptionPane.ERROR_MESSAGE);

                    break;
            }
        });

        btnReset.addActionListener(evt -> {
            // Reload the table with the original candidate list
            CandidateList = new LinkedList<>(originalCandidateList);
            loadListToTable(CandidateList);
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMainScreen = new javax.swing.JPanel();
        pnlMainBar = new javax.swing.JPanel();
        btnLogout = new javax.swing.JButton();
        lbllogo = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        tabPaneMain = new javax.swing.JTabbedPane();
        pnlHome = new javax.swing.JPanel();
        hometitle = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        hometxt = new javax.swing.JLabel();
        hometxt1 = new javax.swing.JLabel();
        lblimageHome = new javax.swing.JLabel();
        hometxt2 = new javax.swing.JLabel();
        lblMainBarSlogan = new javax.swing.JLabel();
        pnlCandidateList = new javax.swing.JPanel();
        spTblStudent = new javax.swing.JScrollPane();
        tblCandidate = new javax.swing.JTable();
        lblTblCandidateTitle = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtFldContact = new javax.swing.JTextField();
        txtFldName = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        ComboCategory = new javax.swing.JComboBox<>();
        ComboType = new javax.swing.JComboBox<>();
        txtCandidateNo = new javax.swing.JTextField();
        lblErrorMsgCategory = new javax.swing.JLabel();
        lblErrorMsgType = new javax.swing.JLabel();
        lblErrorMsgCandidateId = new javax.swing.JLabel();
        lblErrorMsgName = new javax.swing.JLabel();
        lblErrorMsgContact = new javax.swing.JLabel();
        lblErrorMsgCitizenshipNo = new javax.swing.JLabel();
        txtFldCitizenshipNo = new javax.swing.JTextField();
        lblErrorMsgDOB = new javax.swing.JLabel();
        txtFldDOB = new javax.swing.JTextField();
        ComboSort = new javax.swing.JComboBox<>();
        btnAscending = new javax.swing.JButton();
        btnDescending = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        txtBinarySearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        Results = new javax.swing.JPanel();
        spTblStudent1 = new javax.swing.JScrollPane();
        tblResults = new javax.swing.JTable();
        btnStartTrial = new javax.swing.JButton();
        pnlAdminControl = new javax.swing.JPanel();
        PnlServices1 = new javax.swing.JPanel();
        imgscooter = new javax.swing.JLabel();
        imgcar = new javax.swing.JLabel();
        imgBike = new javax.swing.JLabel();
        pnlourservices = new javax.swing.JPanel();
        imgservices = new javax.swing.JLabel();
        pnlservices2 = new javax.swing.JPanel();
        imgcone = new javax.swing.JLabel();
        pnlservices3 = new javax.swing.JPanel();
        services2 = new javax.swing.JLabel();
        lbltitleservices3 = new javax.swing.JLabel();
        txtservices3 = new javax.swing.JLabel();
        lbltitleservices = new javax.swing.JLabel();
        txtservices1 = new javax.swing.JLabel();
        lbltitleservices2 = new javax.swing.JLabel();
        txtservices2 = new javax.swing.JLabel();
        lbltitleservices4 = new javax.swing.JLabel();
        txtservices4 = new javax.swing.JLabel();
        pnlAboutUs = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        titleAboutUS = new javax.swing.JLabel();
        txtAboutUS = new javax.swing.JLabel();
        pnlmember1 = new javax.swing.JPanel();
        membername = new javax.swing.JLabel();
        titlemember1 = new javax.swing.JLabel();
        imgmember1 = new javax.swing.JLabel();
        pnlmember2 = new javax.swing.JPanel();
        membername2 = new javax.swing.JLabel();
        titlemember2 = new javax.swing.JLabel();
        imgmember2 = new javax.swing.JLabel();
        pnlmember3 = new javax.swing.JPanel();
        imgmember3 = new javax.swing.JLabel();
        membername3 = new javax.swing.JLabel();
        membertitle3 = new javax.swing.JLabel();
        AboutUSmembers = new javax.swing.JLabel();
        pnlLoginScreen = new javax.swing.JPanel();
        pnlLoginLeft = new javax.swing.JPanel();
        lblLoginForgotPwd = new javax.swing.JLabel();
        lblLoginError = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        txtFldLoginUsername = new javax.swing.JTextField();
        pwdFldLogin = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblLogInError = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pnlLoadingScreen = new javax.swing.JPanel();
        pgBarSplashScreen = new javax.swing.JProgressBar(0,100);
        lblLoading = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        pnlMainScreen.setBackground(new java.awt.Color(255, 255, 255));
        pnlMainScreen.setMaximumSize(new java.awt.Dimension(1130, 514));
        pnlMainScreen.setMinimumSize(new java.awt.Dimension(1130, 514));
        pnlMainScreen.setPreferredSize(new java.awt.Dimension(1130, 514));

        pnlMainBar.setBackground(new java.awt.Color(8, 31, 92));
        pnlMainBar.setForeground(new java.awt.Color(255, 255, 255));

        btnLogout.setBackground(new java.awt.Color(255, 0, 0));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Log out");
        btnLogout.setBorder(new javax.swing.border.MatteBorder(null));
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        lbllogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/logosmall.png"))); // NOI18N

        jLabel27.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 249, 240));
        jLabel27.setText("ONE LICENSE, INFINITE POSSIBILITIES");

        javax.swing.GroupLayout pnlMainBarLayout = new javax.swing.GroupLayout(pnlMainBar);
        pnlMainBar.setLayout(pnlMainBarLayout);
        pnlMainBarLayout.setHorizontalGroup(
            pnlMainBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainBarLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lbllogo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );
        pnlMainBarLayout.setVerticalGroup(
            pnlMainBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMainBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbllogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabPaneMain.setBackground(new java.awt.Color(255, 204, 204));
        tabPaneMain.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        pnlHome.setBackground(new java.awt.Color(255, 249, 240));
        pnlHome.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 255)));

        hometitle.setBackground(new java.awt.Color(255, 255, 255));
        hometitle.setFont(new java.awt.Font("Century", 1, 48)); // NOI18N
        hometitle.setForeground(new java.awt.Color(0, 51, 102));
        hometitle.setText("Welcome to Infinity Trial! ");

        jPanel3.setBackground(new java.awt.Color(237, 241, 246));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(8, 31, 92)));

        hometxt.setBackground(new java.awt.Color(237, 241, 246));
        hometxt.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        hometxt.setText("<html>At Infinty Trail, we are committed to providing a hassle-free experience for all your driving license needs. <br> <br>Whether you're a first-time applicant or looking to update your license, ");

        hometxt1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 24)); // NOI18N
        hometxt1.setText("WE'VE GOT YOU COVERED!!");

        lblimageHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/license11.png"))); // NOI18N

        hometxt2.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 24)); // NOI18N
        hometxt2.setText(" Your journey to the open road starts here!");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hometxt, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hometxt2, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(hometxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 302, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblimageHome)
                .addGap(43, 43, 43))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(lblimageHome, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(hometxt, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(hometxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hometxt2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        lblMainBarSlogan.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        lblMainBarSlogan.setForeground(new java.awt.Color(255, 51, 51));
        lblMainBarSlogan.setText("Drive Your Dreams: Fast, Easy, Reliable License Services!");

        javax.swing.GroupLayout pnlHomeLayout = new javax.swing.GroupLayout(pnlHome);
        pnlHome.setLayout(pnlHomeLayout);
        pnlHomeLayout.setHorizontalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hometitle, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMainBarSlogan, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        pnlHomeLayout.setVerticalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(hometitle, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblMainBarSlogan, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(246, Short.MAX_VALUE))
        );

        tabPaneMain.addTab("Home", pnlHome);

        pnlCandidateList.setBackground(new java.awt.Color(255, 249, 240));
        pnlCandidateList.setPreferredSize(new java.awt.Dimension(1400, 1000));

        tblCandidate.setBackground(new java.awt.Color(204, 204, 204));
        tblCandidate.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Candidate No.", "Full Name", "Contact", "Applied Category", "Type", "Citizenship Number", "Date Of Birth"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCandidate.setGridColor(new java.awt.Color(0, 0, 0));
        tblCandidate.setSelectionBackground(new java.awt.Color(0, 0, 0));
        tblCandidate.setSelectionForeground(new java.awt.Color(234, 192, 32));
        tblCandidate.setShowGrid(true);
        tblCandidate.getTableHeader().setReorderingAllowed(false);
        spTblStudent.setViewportView(tblCandidate);
        if (tblCandidate.getColumnModel().getColumnCount() > 0) {
            tblCandidate.getColumnModel().getColumn(0).setResizable(false);
            tblCandidate.getColumnModel().getColumn(1).setResizable(false);
            tblCandidate.getColumnModel().getColumn(2).setResizable(false);
            tblCandidate.getColumnModel().getColumn(3).setResizable(false);
            tblCandidate.getColumnModel().getColumn(4).setResizable(false);
            tblCandidate.getColumnModel().getColumn(4).setPreferredWidth(40);
            tblCandidate.getColumnModel().getColumn(5).setResizable(false);
            tblCandidate.getColumnModel().getColumn(6).setResizable(false);
        }

        lblTblCandidateTitle.setBackground(new java.awt.Color(204, 204, 255));
        lblTblCandidateTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTblCandidateTitle.setText("Candidate Information");
        lblTblCandidateTitle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 255)));

        jPanel2.setBackground(new java.awt.Color(247, 242, 235));

        txtFldContact.setBackground(new java.awt.Color(255, 249, 240));
        txtFldContact.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Contact", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txtFldContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFldContactActionPerformed(evt);
            }
        });

        txtFldName.setBackground(new java.awt.Color(255, 249, 240));
        txtFldName.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txtFldName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFldNameActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");

        btnDelete.setText("Delete");

        btnClear.setText("Clear");

        btnAdd.setText("Add");

        ComboCategory.setBackground(new java.awt.Color(255, 249, 240));
        ComboCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A (Bike, Scooter)", "B (Car, Jeep, Delivery Van)", "K (Scooter)" }));
        ComboCategory.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Category", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        ComboCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboCategoryActionPerformed(evt);
            }
        });

        ComboType.setBackground(new java.awt.Color(255, 249, 240));
        ComboType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "New License", "Add Category", "Retrial"}));
        ComboType.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        ComboType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboTypeActionPerformed(evt);
            }
        });

        txtCandidateNo.setBackground(new java.awt.Color(255, 249, 240));
        txtCandidateNo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Candidate Number", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txtCandidateNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCandidateNoActionPerformed(evt);
            }
        });

        lblErrorMsgCategory.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorMsgCategory.setText(".");

        lblErrorMsgType.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorMsgType.setText(".");

        lblErrorMsgCandidateId.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorMsgCandidateId.setText(".");

        lblErrorMsgName.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorMsgName.setText(".");

        lblErrorMsgContact.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorMsgContact.setText(".");

        lblErrorMsgCitizenshipNo.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorMsgCitizenshipNo.setText(".");

        txtFldCitizenshipNo.setBackground(new java.awt.Color(255, 249, 240));
        txtFldCitizenshipNo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Citizenship Number", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txtFldCitizenshipNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFldCitizenshipNoActionPerformed(evt);
            }
        });

        lblErrorMsgDOB.setForeground(new java.awt.Color(255, 51, 51));
        lblErrorMsgDOB.setText(".");

        txtFldDOB.setBackground(new java.awt.Color(255, 249, 240));
        txtFldDOB.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Date of Birth", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        txtFldDOB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFldDOBActionPerformed(evt);
            }
        });

        ComboSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sort By: ", "Candidate No", "Name", "Date Of Birth"}));

        btnAscending.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/arrow2.png"))); // NOI18N
        btnAscending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAscendingActionPerformed(evt);
            }
        });

        btnDescending.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/arrowdown1.png"))); // NOI18N

        btnReset.setText("Reset");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCandidateNo, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(txtFldName)
                    .addComponent(txtFldContact)
                    .addComponent(lblErrorMsgName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblErrorMsgCandidateId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblErrorMsgContact, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(124, 124, 124)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ComboType, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorMsgCitizenshipNo, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFldCitizenshipNo, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorMsgType, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblErrorMsgCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(lblErrorMsgDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ComboSort, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFldDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(107, 107, 107)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnAdd)
                            .addComponent(btnClear)
                            .addComponent(btnUpdate)
                            .addComponent(btnDelete))
                        .addGap(0, 21, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(323, 323, 323)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnAscending)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDescending, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblErrorMsgCandidateId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCandidateNo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblErrorMsgName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFldName, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblErrorMsgContact)
                .addGap(0, 0, 0)
                .addComponent(txtFldContact, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblErrorMsgType)
                    .addComponent(lblErrorMsgDOB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnAdd)
                                .addGap(18, 18, 18)
                                .addComponent(btnClear))
                            .addComponent(txtFldDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnUpdate)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(ComboSort))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(ComboType, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblErrorMsgCategory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ComboCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addComponent(lblErrorMsgCitizenshipNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtFldCitizenshipNo, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                        .addGap(214, 214, 214))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDescending, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAscending, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReset)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        txtBinarySearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBinarySearchActionPerformed(evt);
            }
        });

        btnSearch.setText("Search");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Search By:", "Name", "Candidate ID"}));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCandidateListLayout = new javax.swing.GroupLayout(pnlCandidateList);
        pnlCandidateList.setLayout(pnlCandidateListLayout);
        pnlCandidateListLayout.setHorizontalGroup(
            pnlCandidateListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCandidateListLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111))
            .addGroup(pnlCandidateListLayout.createSequentialGroup()
                .addGroup(pnlCandidateListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCandidateListLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(lblTblCandidateTitle)
                        .addGap(245, 245, 245)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(txtBinarySearch, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(btnSearch))
                    .addGroup(pnlCandidateListLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(spTblStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 968, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCandidateListLayout.setVerticalGroup(
            pnlCandidateListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCandidateListLayout.createSequentialGroup()
                .addGroup(pnlCandidateListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlCandidateListLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(lblTblCandidateTitle))
                    .addGroup(pnlCandidateListLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlCandidateListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBinarySearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(spTblStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabPaneMain.addTab("Admin Control", pnlCandidateList);
        pnlCandidateList.getAccessibleContext().setAccessibleName("");

        Results.setBackground(new java.awt.Color(255, 249, 240));

        tblResults.setBackground(new java.awt.Color(204, 204, 204));
        tblResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Candidate No.", "Full Name", "Contact", "Applied Category", "Type", "Citizenship No.", "Date Of Birth", "Status", "License No."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblResults.setGridColor(new java.awt.Color(0, 0, 0));
        tblResults.setSelectionBackground(new java.awt.Color(0, 0, 0));
        tblResults.setSelectionForeground(new java.awt.Color(234, 192, 32));
        tblResults.setShowGrid(true);
        tblResults.getTableHeader().setReorderingAllowed(false);
        spTblStudent1.setViewportView(tblResults);
        if (tblResults.getColumnModel().getColumnCount() > 0) {
            tblResults.getColumnModel().getColumn(0).setResizable(false);
            tblResults.getColumnModel().getColumn(1).setResizable(false);
            tblResults.getColumnModel().getColumn(2).setResizable(false);
            tblResults.getColumnModel().getColumn(3).setResizable(false);
            tblResults.getColumnModel().getColumn(4).setResizable(false);
            tblResults.getColumnModel().getColumn(4).setPreferredWidth(40);
            tblResults.getColumnModel().getColumn(5).setResizable(false);
            tblResults.getColumnModel().getColumn(6).setResizable(false);
            tblResults.getColumnModel().getColumn(7).setResizable(false);
            tblResults.getColumnModel().getColumn(8).setResizable(false);
        }

        btnStartTrial.setText("Start Trial");

        javax.swing.GroupLayout ResultsLayout = new javax.swing.GroupLayout(Results);
        Results.setLayout(ResultsLayout);
        ResultsLayout.setHorizontalGroup(
            ResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ResultsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spTblStudent1, javax.swing.GroupLayout.PREFERRED_SIZE, 1101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStartTrial, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        ResultsLayout.setVerticalGroup(
            ResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ResultsLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(btnStartTrial, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(spTblStudent1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(371, Short.MAX_VALUE))
        );

        tabPaneMain.addTab("Result", Results);

        pnlAdminControl.setBackground(new java.awt.Color(255, 249, 240));
        pnlAdminControl.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(234, 192, 32)));

        PnlServices1.setBackground(new java.awt.Color(158, 204, 250));

        imgscooter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/scooter.png"))); // NOI18N
        imgscooter.setText("jLabel12");

        imgcar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/car1.png"))); // NOI18N
        imgcar.setText("jLabel12");

        imgBike.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/bike-removebg-preview.png"))); // NOI18N
        imgBike.setText("jLabel12");

        javax.swing.GroupLayout PnlServices1Layout = new javax.swing.GroupLayout(PnlServices1);
        PnlServices1.setLayout(PnlServices1Layout);
        PnlServices1Layout.setHorizontalGroup(
            PnlServices1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlServices1Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(imgscooter, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(imgBike, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlServices1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imgcar, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101))
        );
        PnlServices1Layout.setVerticalGroup(
            PnlServices1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnlServices1Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(imgcar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PnlServices1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imgscooter, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imgBike, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        pnlourservices.setBackground(new java.awt.Color(231, 222, 202));

        imgservices.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/licenseImage.png"))); // NOI18N
        imgservices.setText("jLabel15");

        javax.swing.GroupLayout pnlourservicesLayout = new javax.swing.GroupLayout(pnlourservices);
        pnlourservices.setLayout(pnlourservicesLayout);
        pnlourservicesLayout.setHorizontalGroup(
            pnlourservicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlourservicesLayout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(imgservices, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        pnlourservicesLayout.setVerticalGroup(
            pnlourservicesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlourservicesLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(imgservices, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        pnlservices2.setBackground(new java.awt.Color(245, 245, 220));

        imgcone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/cone.png"))); // NOI18N
        imgcone.setText("jLabel16");

        javax.swing.GroupLayout pnlservices2Layout = new javax.swing.GroupLayout(pnlservices2);
        pnlservices2.setLayout(pnlservices2Layout);
        pnlservices2Layout.setHorizontalGroup(
            pnlservices2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlservices2Layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(imgcone, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(96, Short.MAX_VALUE))
        );
        pnlservices2Layout.setVerticalGroup(
            pnlservices2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlservices2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(imgcone)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pnlservices3.setBackground(new java.awt.Color(241, 227, 226));

        services2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/special.png"))); // NOI18N
        services2.setText("jLabel18");

        javax.swing.GroupLayout pnlservices3Layout = new javax.swing.GroupLayout(pnlservices3);
        pnlservices3.setLayout(pnlservices3Layout);
        pnlservices3Layout.setHorizontalGroup(
            pnlservices3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlservices3Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(services2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );
        pnlservices3Layout.setVerticalGroup(
            pnlservices3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlservices3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(services2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        lbltitleservices3.setBackground(new java.awt.Color(158, 204, 250));
        lbltitleservices3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbltitleservices3.setForeground(new java.awt.Color(112, 141, 169));
        lbltitleservices3.setText("Driving License Trials");

        txtservices3.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtservices3.setForeground(new java.awt.Color(112, 141, 169));
        txtservices3.setText("<html>We Conduct tests for cars, <br>bikes, and scooters with dedicated tracks<br> for two-wheelers and four-wheelers.");

        lbltitleservices.setBackground(new java.awt.Color(231, 222, 202));
        lbltitleservices.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbltitleservices.setForeground(new java.awt.Color(100, 82, 33));
        lbltitleservices.setText("Application Support");

        txtservices1.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtservices1.setForeground(new java.awt.Color(100, 82, 33));
        txtservices1.setText("<html> Assist with new license applications, document submissions, and renewals for expiring licenses.");

        lbltitleservices2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbltitleservices2.setForeground(new java.awt.Color(121, 121, 105));
        lbltitleservices2.setText("Training and Practice");

        txtservices2.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtservices2.setForeground(new java.awt.Color(121, 121, 105));
        txtservices2.setText("<html>Offer pre-trial practice sessions, mock tests, and <br>personalized coaching to improve driving skills.");
        txtservices2.setToolTipText("");

        lbltitleservices4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbltitleservices4.setForeground(new java.awt.Color(204, 192, 191));
        lbltitleservices4.setText("Specialized Services");

        txtservices4.setFont(new java.awt.Font("Trebuchet MS", 0, 12)); // NOI18N
        txtservices4.setForeground(new java.awt.Color(150, 142, 141));
        txtservices4.setText("<html>Provide vehicle rentals for trials, eco-driving<br>courses, and support for international driving <br>permits.");

        javax.swing.GroupLayout pnlAdminControlLayout = new javax.swing.GroupLayout(pnlAdminControl);
        pnlAdminControl.setLayout(pnlAdminControlLayout);
        pnlAdminControlLayout.setHorizontalGroup(
            pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdminControlLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAdminControlLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbltitleservices2)
                        .addGap(2, 2, 2))
                    .addComponent(txtservices2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtservices1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(pnlAdminControlLayout.createSequentialGroup()
                        .addComponent(lbltitleservices, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlservices2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlourservices, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAdminControlLayout.createSequentialGroup()
                        .addComponent(pnlservices3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbltitleservices4, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtservices4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlAdminControlLayout.createSequentialGroup()
                        .addComponent(PnlServices1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbltitleservices3, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtservices3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(39, 39, 39))
        );
        pnlAdminControlLayout.setVerticalGroup(
            pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdminControlLayout.createSequentialGroup()
                .addGroup(pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAdminControlLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(lbltitleservices3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtservices3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAdminControlLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlourservices, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlAdminControlLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(lbltitleservices)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtservices1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(PnlServices1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAdminControlLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(lbltitleservices2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtservices2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAdminControlLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(lbltitleservices4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtservices4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAdminControlLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlAdminControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlservices2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlservices3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(253, Short.MAX_VALUE))
        );

        tabPaneMain.addTab("Our Services", pnlAdminControl);

        pnlAboutUs.setBackground(new java.awt.Color(255, 249, 240));
        pnlAboutUs.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(234, 192, 32)));

        jPanel6.setBackground(new java.awt.Color(8, 31, 92));
        jPanel6.setForeground(new java.awt.Color(8, 31, 92));

        titleAboutUS.setBackground(new java.awt.Color(248, 206, 206));
        titleAboutUS.setFont(new java.awt.Font("Segoe UI Black", 0, 38)); // NOI18N
        titleAboutUS.setForeground(new java.awt.Color(255, 249, 240));
        titleAboutUS.setText("INFINITY TRIAL");

        txtAboutUS.setForeground(new java.awt.Color(255, 249, 240));
        txtAboutUS.setText("<html><div style=\"text-align: justify;\">Established in 2005, Infinity Trial was founded with the vision of creating a reliable platform to assist individuals in obtaining their driving licenses. Over the years, it has become a trusted name in the community, known for its state-of-the-art facilities and commitment to road safety.  Since its inception, Infinity Trial has helped over 50,000 individuals successfully pass their driving tests and earn their licenses for cars, bikes, and scooters. Its signature infinity-shaped track, designed to replicate real-world scenarios, has been pivotal in teaching drivers essential skills such as balance, precision, and confidence.");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtAboutUS, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(titleAboutUS, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
                .addContainerGap(104, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(titleAboutUS, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtAboutUS, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(202, Short.MAX_VALUE))
        );

        pnlmember1.setBackground(new java.awt.Color(8, 31, 92));

        membername.setBackground(new java.awt.Color(255, 255, 255));
        membername.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        membername.setForeground(new java.awt.Color(255, 249, 240));
        membername.setText("Bimarsha Raut");

        titlemember1.setBackground(new java.awt.Color(255, 249, 240));
        titlemember1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        titlemember1.setForeground(new java.awt.Color(255, 249, 240));
        titlemember1.setText("Founder/CEO");

        imgmember1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/boy.png"))); // NOI18N
        imgmember1.setText("jLabel38");

        javax.swing.GroupLayout pnlmember1Layout = new javax.swing.GroupLayout(pnlmember1);
        pnlmember1.setLayout(pnlmember1Layout);
        pnlmember1Layout.setHorizontalGroup(
            pnlmember1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlmember1Layout.createSequentialGroup()
                .addGroup(pnlmember1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlmember1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(pnlmember1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imgmember1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlmember1Layout.createSequentialGroup()
                                .addComponent(membername, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8))))
                    .addGroup(pnlmember1Layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(titlemember1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        pnlmember1Layout.setVerticalGroup(
            pnlmember1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlmember1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(imgmember1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(membername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titlemember1)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pnlmember2.setBackground(new java.awt.Color(8, 31, 92));

        membername2.setBackground(new java.awt.Color(255, 255, 255));
        membername2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        membername2.setForeground(new java.awt.Color(255, 249, 240));
        membername2.setText("Arshad Warsi");

        titlemember2.setBackground(new java.awt.Color(255, 249, 240));
        titlemember2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        titlemember2.setForeground(new java.awt.Color(255, 249, 240));
        titlemember2.setText("Excecutive Manager");

        imgmember2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/boy.png"))); // NOI18N
        imgmember2.setText("jLabel38");

        javax.swing.GroupLayout pnlmember2Layout = new javax.swing.GroupLayout(pnlmember2);
        pnlmember2.setLayout(pnlmember2Layout);
        pnlmember2Layout.setHorizontalGroup(
            pnlmember2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlmember2Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(pnlmember2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(imgmember2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlmember2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(titlemember2)
                        .addComponent(membername2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );
        pnlmember2Layout.setVerticalGroup(
            pnlmember2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlmember2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imgmember2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(membername2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titlemember2)
                .addGap(37, 37, 37))
        );

        pnlmember3.setBackground(new java.awt.Color(8, 31, 92));

        imgmember3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/boy.png"))); // NOI18N
        imgmember3.setText("jLabel38");

        membername3.setBackground(new java.awt.Color(255, 255, 255));
        membername3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        membername3.setForeground(new java.awt.Color(255, 249, 240));
        membername3.setText("Koyal Limbu");

        membertitle3.setBackground(new java.awt.Color(255, 249, 240));
        membertitle3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        membertitle3.setForeground(new java.awt.Color(255, 249, 240));
        membertitle3.setText("Marketing Head");

        javax.swing.GroupLayout pnlmember3Layout = new javax.swing.GroupLayout(pnlmember3);
        pnlmember3.setLayout(pnlmember3Layout);
        pnlmember3Layout.setHorizontalGroup(
            pnlmember3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlmember3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlmember3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imgmember3, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlmember3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(pnlmember3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlmember3Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(membertitle3))
                            .addComponent(membername3))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        pnlmember3Layout.setVerticalGroup(
            pnlmember3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlmember3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(imgmember3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(membername3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(membertitle3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        AboutUSmembers.setBackground(new java.awt.Color(255, 249, 240));
        AboutUSmembers.setFont(new java.awt.Font("Segoe UI Black", 0, 36)); // NOI18N
        AboutUSmembers.setForeground(new java.awt.Color(8, 31, 92));
        AboutUSmembers.setText("OUR CORE MEMBERS");

        javax.swing.GroupLayout pnlAboutUsLayout = new javax.swing.GroupLayout(pnlAboutUs);
        pnlAboutUs.setLayout(pnlAboutUsLayout);
        pnlAboutUsLayout.setHorizontalGroup(
            pnlAboutUsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAboutUsLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlAboutUsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAboutUsLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(pnlmember1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(pnlmember2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(pnlmember3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAboutUsLayout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addComponent(AboutUSmembers, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        pnlAboutUsLayout.setVerticalGroup(
            pnlAboutUsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAboutUsLayout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 174, Short.MAX_VALUE))
            .addGroup(pnlAboutUsLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(AboutUSmembers, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(pnlAboutUsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlmember2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlmember3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlmember1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabPaneMain.addTab("About Us", pnlAboutUs);

        javax.swing.GroupLayout pnlMainScreenLayout = new javax.swing.GroupLayout(pnlMainScreen);
        pnlMainScreen.setLayout(pnlMainScreenLayout);
        pnlMainScreenLayout.setHorizontalGroup(
            pnlMainScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMainBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlMainScreenLayout.createSequentialGroup()
                .addComponent(tabPaneMain, javax.swing.GroupLayout.PREFERRED_SIZE, 1173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
        );
        pnlMainScreenLayout.setVerticalGroup(
            pnlMainScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainScreenLayout.createSequentialGroup()
                .addComponent(pnlMainBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPaneMain, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE))
        );

        pnlLoginScreen.setBackground(new java.awt.Color(8, 31, 92));
        pnlLoginScreen.setMaximumSize(new java.awt.Dimension(1130, 514));
        pnlLoginScreen.setMinimumSize(new java.awt.Dimension(1130, 514));
        pnlLoginScreen.setPreferredSize(new java.awt.Dimension(1130, 514));

        pnlLoginLeft.setBackground(new java.awt.Color(102, 102, 102));
        pnlLoginLeft.setMaximumSize(new java.awt.Dimension(570, 514));
        pnlLoginLeft.setMinimumSize(new java.awt.Dimension(570, 514));
        pnlLoginLeft.setPreferredSize(new java.awt.Dimension(1130, 514));

        lblLoginForgotPwd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLoginForgotPwd.setForeground(new java.awt.Color(51, 0, 255));
        lblLoginForgotPwd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLoginForgotPwd.setText("Forgot your password?");

        javax.swing.GroupLayout pnlLoginLeftLayout = new javax.swing.GroupLayout(pnlLoginLeft);
        pnlLoginLeft.setLayout(pnlLoginLeftLayout);
        pnlLoginLeftLayout.setHorizontalGroup(
            pnlLoginLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLeftLayout.createSequentialGroup()
                .addGap(2134, 2134, 2134)
                .addComponent(lblLoginForgotPwd, javax.swing.GroupLayout.PREFERRED_SIZE, 953, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(3659, Short.MAX_VALUE))
        );
        pnlLoginLeftLayout.setVerticalGroup(
            pnlLoginLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginLeftLayout.createSequentialGroup()
                .addContainerGap(164, Short.MAX_VALUE)
                .addComponent(lblLoginForgotPwd)
                .addGap(492, 492, 492))
        );

        lblLoginError.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblLoginError.setForeground(new java.awt.Color(255, 0, 0));
        lblLoginError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 249, 240));

        jPanel4.setBackground(new java.awt.Color(255, 249, 240));

        txtFldLoginUsername.setBackground(new java.awt.Color(255, 249, 240));
        txtFldLoginUsername.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 204), 2), "Username", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 204, 204))); // NOI18N
        txtFldLoginUsername.setCaretColor(new java.awt.Color(51, 153, 255));
        txtFldLoginUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFldLoginUsernameActionPerformed(evt);
            }
        });

        pwdFldLogin.setBackground(new java.awt.Color(255, 249, 240));
        pwdFldLogin.setForeground(new java.awt.Color(8, 31, 92));
        pwdFldLogin.setText("admin");
        pwdFldLogin.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 204), 2), "Password", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 204, 204))); // NOI18N
        pwdFldLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwdFldLoginActionPerformed(evt);
            }
        });

        btnLogin.setBackground(new java.awt.Color(179, 235, 242));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLogin.setText("Login");
        btnLogin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        jLabel11.setBackground(new java.awt.Color(0, 0, 204));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 204));
        jLabel11.setText("Forgot your Password?");

        lblLogInError.setForeground(new java.awt.Color(255, 0, 0));
        lblLogInError.setText(".");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21))
                            .addComponent(lblLogInError, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(pwdFldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFldLoginUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(117, 117, 117))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(lblLogInError)
                .addGap(18, 18, 18)
                .addComponent(txtFldLoginUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pwdFldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        jLabel9.setFont(new java.awt.Font("Stencil", 0, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(45, 74, 88));
        jLabel9.setText("YOUR WAY TO LICENSING");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(jLabel9)))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/oklogo.png"))); // NOI18N

        jLabel7.setBackground(new java.awt.Color(137, 207, 240));
        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 48)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(137, 207, 240));
        jLabel7.setText("INFINITY TRIAL");

        jLabel8.setBackground(new java.awt.Color(255, 0, 0));
        jLabel8.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 44, 44));
        jLabel8.setText("ONE LICENSE, INFINITE POSSIBILITIES");

        javax.swing.GroupLayout pnlLoginScreenLayout = new javax.swing.GroupLayout(pnlLoginScreen);
        pnlLoginScreen.setLayout(pnlLoginScreenLayout);
        pnlLoginScreenLayout.setHorizontalGroup(
            pnlLoginScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginScreenLayout.createSequentialGroup()
                .addGap(6585, 6585, 6585)
                .addComponent(lblLoginError, javax.swing.GroupLayout.DEFAULT_SIZE, 881, Short.MAX_VALUE)
                .addGap(781, 781, 781))
            .addGroup(pnlLoginScreenLayout.createSequentialGroup()
                .addGroup(pnlLoginScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoginScreenLayout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addGroup(pnlLoginScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLoginScreenLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(121, 121, 121))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginScreenLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(177, 177, 177)))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(299, 299, 299)
                .addComponent(pnlLoginLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 6746, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlLoginScreenLayout.setVerticalGroup(
            pnlLoginScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginScreenLayout.createSequentialGroup()
                .addGroup(pnlLoginScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoginScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlLoginLeft, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
                    .addGroup(pnlLoginScreenLayout.createSequentialGroup()
                        .addGap(164, 164, 164)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLoginError))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1130, 514));
        setResizable(false);
        setSize(new java.awt.Dimension(1130, 514));

        pnlLoadingScreen.setBackground(new java.awt.Color(255, 249, 240));
        pnlLoadingScreen.setPreferredSize(new java.awt.Dimension(1130, 514));

        pgBarSplashScreen.setStringPainted(true);
        pgBarSplashScreen.setForeground(new java.awt.Color(0, 0, 0));
        pgBarSplashScreen.setBorder(null);
        pgBarSplashScreen.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);

        lblLoading.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lblLoading.setForeground(new java.awt.Color(153, 204, 255));
        lblLoading.setText("Loading...");
        lblLoading.setPreferredSize(new java.awt.Dimension(100, 20));

        jLabel1.setBackground(new java.awt.Color(8, 31, 92));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/infinitytrailapp/resources/gif_3final.gif"))); // NOI18N

        javax.swing.GroupLayout pnlLoadingScreenLayout = new javax.swing.GroupLayout(pnlLoadingScreen);
        pnlLoadingScreen.setLayout(pnlLoadingScreenLayout);
        pnlLoadingScreenLayout.setHorizontalGroup(
            pnlLoadingScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoadingScreenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLoadingScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoadingScreenLayout.createSequentialGroup()
                        .addComponent(lblLoading, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pgBarSplashScreen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoadingScreenLayout.createSequentialGroup()
                .addContainerGap(151, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 851, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128))
        );
        pnlLoadingScreenLayout.setVerticalGroup(
            pnlLoadingScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoadingScreenLayout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblLoading, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pgBarSplashScreen, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlLoadingScreen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlLoadingScreen, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /**
     * Initializes the layout and adds screens to the CardLayout.
     */

    private void initializeLayout() {
        cardLayout = new java.awt.CardLayout();
        getContentPane().setLayout(cardLayout);

        // Add panels with unique identifiers
        getContentPane().add(pnlLoadingScreen, "LoadingScreen");
        getContentPane().add(pnlLoginScreen, "LoginScreen");
        getContentPane().add(pnlMainScreen, "MainScreen");

        // Start with the loading screen
        cardLayout.show(getContentPane(), "LoadingScreen");
    }

    /**
     * Initializes the data, including student list and table
     */
    private void initializeData() {
        CandidateList = new LinkedList<>();

        // Manually adding candidates using the registerCandidate method
        registerCandidate(new CandidateModel(1001, "Sita Sharma", "9812345678", "A (Bike, Scooter)", "New License", "12-345-6", "1995-05-14"));
        registerCandidate(new CandidateModel(1002, "Ram Thapa", "9823456789", "B (Car, Jeep, Delivery Van)", "New License", "23-4-567", "1990-08-25"));
        registerCandidate(new CandidateModel(1003, "Krishna Bhandari", "9811122233", "A (Bike, Scooter)", "Add Category", "34-56-78", "1988-12-15"));
        registerCandidate(new CandidateModel(1004, "Laxmi Adhikari", "9822233445", "K (Scooter)", "New License", "4-5678-9", "1993-07-20"));
        registerCandidate(new CandidateModel(1005, "Gopal Shrestha", "9813344556", "B (Car, Jeep, Delivery Van)", "Retrial", "5-67-890", "1985-01-10"));
        registerCandidate(new CandidateModel(1006, "Sunita Raut", "9824455667", "A (Bike, Scooter)", "New License", "678-90-1", "1997-11-18"));
        registerCandidate(new CandidateModel(1007, "Manoj Karki", "9815566778", "K (Scooter)", "Add Category", "78-90-12", "1992-04-30"));
        registerCandidate(new CandidateModel(1008, "Pramila Gurung", "9826677889", "B (Car, Jeep, Delivery Van)", "New License", "8-90-123", "1989-09-05"));
        registerCandidate(new CandidateModel(1009, "Kishor Mahato", "9817788990", "A (Bike, Scooter)", "Retrial", "9-0-1234", "1996-06-11"));
        registerCandidate(new CandidateModel(1010, "Anita Lama", "9828899001", "K (Scooter)", "New License", "01-23-45", "1994-03-22"));

        originalCandidateList = new LinkedList<>(CandidateList);

        for (CandidateModel candidate : CandidateList) {
            queueProcessor.enqueueCandidate(candidate);
        }
    }

    /**
     * Stimulates the Loading Screen
     */
    private void startProgress() {
        // Set custom color for the progress bar (Steel Blue)
        pgBarSplashScreen.setUI(new javax.swing.plaf.basic.BasicProgressBarUI());
        pgBarSplashScreen.setForeground(new Color(70, 130, 180)); // Steel Blue
        pgBarSplashScreen.setBackground(new Color(240, 248, 255)); // Optional: Alice Blue (background)
        pgBarSplashScreen.setStringPainted(false); // Ensure no percentage or text is displayed

        javax.swing.SwingWorker<Void, Integer> worker = new javax.swing.SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(30); // Simulated delay for progress bar
                    publish(i); // Publish progress
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int progress = chunks.get(chunks.size() - 1);
                pgBarSplashScreen.setValue(progress);
            }

            @Override
            protected void done() {
                loadScreen("LoginScreen"); // Switch to login screen
            }
        };
        worker.execute(); // Start the worker thread
    }

    /**
     * Method to add student data and populate the table
     */
    private void registerCandidate(CandidateModel candidate) {
        CandidateList.add(candidate); // Add candidate to the list

        // Get the table model
        DefaultTableModel model = (DefaultTableModel) tblCandidate.getModel();

        // Add a new row with the candidate's details
        model.addRow(new Object[]{
            candidate.getCandidateNo(), // Candidate ID
            candidate.getName(), // Name
            candidate.getContact(), // Contact
            candidate.getCategory(), // Category
            candidate.getType(), // Type
            candidate.getCitizenshipNo(), // Citizenship No
            candidate.getDateOfBirth() // Date of Birth
        });
    }

    /**
     * Method to initialize the Queue
     */
    private void initializeTrialQueue() {

        btnStartTrial.addActionListener(evt -> {
            queueProcessor.startTrial();
            JOptionPane.showMessageDialog(null, "Trial process completed.");
        });
    }

    /**
     * Method to switch screens
     */
    private void loadScreen(String screenName) {
        cardLayout.show(getContentPane(), screenName);
    }

    /**
     * Handles the action performed when the Login button is clicked.
     *
     * @param evt ActionEvent triggered by the button click
     */
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // Get the username and password input
        String username = txtFldLoginUsername.getText();
        String password = new String(pwdFldLogin.getPassword());

        // Check if username or password is empty
        if (username.isEmpty() || password.isEmpty()) {
            lblLogInError.setText("Please enter your username and password.");
        } // Check if username and password are incorrect
        else if (!username.equals("admin") || !password.equals("admin")) {
            lblLogInError.setText("Username and password mismatch.");
        } // If credentials are correct, proceed to load the main screen
        else {
            lblLogInError.setText(""); // Clear any previous error messages
            loadScreen("MainScreen"); // Load the main screen
        }
    }//GEN-LAST:event_btnLoginActionPerformed
    /**
     * Handles the action performed when the Logout button is clicked.
     *
     * @param evt ActionEvent triggered by the button click
     */
    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        pwdFldLogin.setText("");
        txtFldLoginUsername.setText("");
        loadScreen("LoginScreen"); // Load the main screen
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void ComboCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboCategoryActionPerformed

    private void txtFldLoginUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFldLoginUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFldLoginUsernameActionPerformed

    private void ComboTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboTypeActionPerformed

    private void txtFldContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFldContactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFldContactActionPerformed

    private void txtCandidateNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCandidateNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCandidateNoActionPerformed

    private void txtFldNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFldNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFldNameActionPerformed

    private void txtFldCitizenshipNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFldCitizenshipNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFldCitizenshipNoActionPerformed

    private void txtFldDOBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFldDOBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFldDOBActionPerformed

    private void pwdFldLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdFldLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pwdFldLoginActionPerformed

    private void btnAscendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAscendingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAscendingActionPerformed

    private void txtBinarySearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBinarySearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBinarySearchActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    /**
     * Handles the action performed when the Add button is clicked.
     *
     * @param evt ActionEvent triggered by the button click
     */
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        boolean isValid = true;

        // Validate Candidate ID
        String candidateNo = txtCandidateNo.getText().trim();
        if (ValidationUtil.isNullOrEmpty(candidateNo)) {
            lblErrorMsgCandidateId.setText("Candidate ID cannot be empty.");
            txtCandidateNo.setBorder(createTitledBorder(errorColor, "Candidate ID"));
            isValid = false;
        } else if (!candidateNo.matches("\\d+")) {
            lblErrorMsgCandidateId.setText("Candidate ID must be numeric.");
            txtCandidateNo.setBorder(createTitledBorder(errorColor, "Candidate ID"));
            isValid = false;
        } else if (candidateNo.length() < 4 || candidateNo.length() > 5) {
            lblErrorMsgCandidateId.setText("Candidate ID must be 4 to 5 digits long.");
            txtCandidateNo.setBorder(createTitledBorder(errorColor, "Candidate ID"));
            isValid = false;
        } else if (!candidateNo.startsWith("1")) {
            lblErrorMsgCandidateId.setText("Candidate ID must start with '1'.");
            txtCandidateNo.setBorder(createTitledBorder(errorColor, "Candidate ID"));
            isValid = false;
        } else if (checkDuplicateField("candidateNo", candidateNo)) {
            lblErrorMsgCandidateId.setText("Candidate ID already exists.");
            txtCandidateNo.setBorder(createTitledBorder(errorColor, "Candidate ID"));
            isValid = false;
        } else {
            lblErrorMsgCandidateId.setText("");
            txtCandidateNo.setBorder(createTitledBorder(yellowColor, "Candidate ID"));
        }

        // Validate Name
        String name = txtFldName.getText().trim();
        if (ValidationUtil.isNullOrEmpty(name)) {
            lblErrorMsgName.setText("Name cannot be empty.");
            txtFldName.setBorder(createTitledBorder(errorColor, "Name"));
            isValid = false;
        } else if (name.chars().filter(ch -> ch == ' ').count() < 1) {
            lblErrorMsgName.setText("Name must have at least one space.");
            txtFldName.setBorder(createTitledBorder(errorColor, "Name"));
            isValid = false;
        } else if (name.chars().filter(ch -> ch == ' ').count() > 3) {
            lblErrorMsgName.setText("Name cannot have more than three spaces.");
            txtFldName.setBorder(createTitledBorder(errorColor, "Name"));
            isValid = false;
        } else if (name.matches(".*\\d.*")) {
            lblErrorMsgName.setText("Name cannot contain numbers.");
            txtFldName.setBorder(createTitledBorder(errorColor, "Name"));
            isValid = false;
        } else if (!name.matches("^[A-Z][a-z]+(\\s[A-Z][a-z]+)*$")) {
            lblErrorMsgName.setText("Each word must start with a capital letter.");
            txtFldName.setBorder(createTitledBorder(errorColor, "Name"));
            isValid = false;
        } else {
            lblErrorMsgName.setText("");
            txtFldName.setBorder(createTitledBorder(yellowColor, "Name"));
        }

        // Validate Contact
        String contact = txtFldContact.getText().trim();
        if (ValidationUtil.isNullOrEmpty(contact)) {
            lblErrorMsgContact.setText("Contact number cannot be empty.");
            txtFldContact.setBorder(createTitledBorder(errorColor, "Contact"));
            isValid = false;
        } else if (!contact.startsWith("98") && !contact.startsWith("97")) {
            lblErrorMsgContact.setText("Contact must start with '98' or '97'.");
            txtFldContact.setBorder(createTitledBorder(errorColor, "Contact"));
            isValid = false;
        } else if (contact.length() != 10) {
            lblErrorMsgContact.setText("Contact number must be 10 digits long.");
            txtFldContact.setBorder(createTitledBorder(errorColor, "Contact"));
            isValid = false;
        } else if (checkDuplicateField("contact", contact)) {
            lblErrorMsgContact.setText("Contact number already exists.");
            txtFldContact.setBorder(createTitledBorder(errorColor, "Contact"));
            isValid = false;
        } else {
            lblErrorMsgContact.setText("");
            txtFldContact.setBorder(createTitledBorder(yellowColor, "Contact"));
        }

        // Validate Citizenship Number
        String citizenshipNo = txtFldCitizenshipNo.getText().trim();
        if (ValidationUtil.isNullOrEmpty(citizenshipNo)) {
            lblErrorMsgCitizenshipNo.setText("Citizenship Number cannot be empty.");
            txtFldCitizenshipNo.setBorder(createTitledBorder(errorColor, "Citizenship Number"));
            isValid = false;
        } else if (!ValidationUtil.isValidCitizenshipNo(citizenshipNo)) {
            lblErrorMsgCitizenshipNo.setText("Citizenship Number must be numeric with 3-4 hyphens.");
            txtFldCitizenshipNo.setBorder(createTitledBorder(errorColor, "Citizenship Number"));
            isValid = false;
        } else if (checkDuplicateField("citizenshipNo", citizenshipNo)) {
            lblErrorMsgCitizenshipNo.setText("Citizenship Number already exists.");
            txtFldCitizenshipNo.setBorder(createTitledBorder(errorColor, "Citizenship Number"));
            isValid = false;
        } else {
            lblErrorMsgCitizenshipNo.setText("");
            txtFldCitizenshipNo.setBorder(createTitledBorder(yellowColor, "Citizenship Number"));
        }

        String dob = txtFldDOB.getText().trim();
        if (ValidationUtil.isNullOrEmpty(dob)) {
            lblErrorMsgDOB.setText("Date of Birth cannot be empty.");
            txtFldDOB.setBorder(createTitledBorder(errorColor, "Date of Birth"));
            isValid = false;
        } else if (!ValidationUtil.isValidDateOfBirth(dob)) {
            lblErrorMsgDOB.setText("Date of Birth must be in yyyy-mm-dd format.");
            txtFldDOB.setBorder(createTitledBorder(errorColor, "Date of Birth"));
            isValid = false;
        } else {
            LocalDate birthDate = LocalDate.parse(dob);
            LocalDate currentDate = LocalDate.now();
            int age = Period.between(birthDate, currentDate).getYears();

            if (age < 18) {
                lblErrorMsgDOB.setText("Age must be at least 18.");
                txtFldDOB.setBorder(createTitledBorder(errorColor, "Date of Birth"));
                isValid = false;
            } else if (age > 60) {
                lblErrorMsgDOB.setText("Age must not exceed 60.");
                txtFldDOB.setBorder(createTitledBorder(errorColor, "Date of Birth"));
                isValid = false;
            } else {
                lblErrorMsgDOB.setText("");
                txtFldDOB.setBorder(createTitledBorder(yellowColor, "Date of Birth"));
            }
        }

        // If all validations pass
        if (isValid) {
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to add this candidate?",
                    "Confirm Addition", JOptionPane.YES_NO_OPTION);

            if (confirmation != JOptionPane.YES_OPTION) {
                return; // Exit if the user cancels
            }

            CandidateModel newCandidate = new CandidateModel(
                    Integer.parseInt(candidateNo),
                    name,
                    contact,
                    (String) ComboCategory.getSelectedItem(),
                    (String) ComboType.getSelectedItem(),
                    citizenshipNo,
                    txtFldDOB.getText().trim()
            );

            CandidateList.add(newCandidate);
            originalCandidateList.add(newCandidate);
            queueProcessor.enqueueCandidate(newCandidate);
            clearCandidateForm();
            loadListToTable(CandidateList);
            showDialogBox("Candidate added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Loads the candidate list into the table model, replacing any existing
     * data.
     *
     * @param candidateList the list of CandidateModel objects to populate the
     * table
     */
    private void loadListToTable(List<CandidateModel> candidateList) {
        DefaultTableModel model = (DefaultTableModel) tblCandidate.getModel();
        model.setRowCount(0); // Clear existing rows

        // Populate the table with candidate data
        candidateList.forEach(candidate -> model.addRow(new Object[]{
            candidate.getCandidateNo(),
            candidate.getName(),
            candidate.getContact(),
            candidate.getCategory(),
            candidate.getType(),
            candidate.getCitizenshipNo(), // Citizenship No
            candidate.getDateOfBirth() // Date of Birth
        }));
    }

    /**
     * Validates a text field's input and updates its border and error label
     * based on validity.
     *
     * @param textField the JTextField to validate
     * @param fieldName the name of the field (used for border title)
     * @param errorLbl the JLabel to display error messages
     * @param errorMsg the error message to display if the format is invalid
     * @param errorColor the color to use for the border and text when an error
     * occurs
     * @param successColor the color to use for the border when the input is
     * valid
     * @param isValidFormat a boolean indicating if the input format is valid
     * @return true if the input is valid, false otherwise
     */
    private boolean validateField(JTextField textField, String fieldName, JLabel errorLbl, String errorMsg, Color errorColor, Color successColor, boolean isValidFormat) {
        if (ValidationUtil.isNullOrEmpty(textField.getText())) {
            textField.setBorder(createTitledBorder(errorColor, fieldName));
            errorLbl.setText("Field cannot be empty!");
            errorLbl.setVisible(true);
            return false;
        } else if (!isValidFormat) {
            textField.setBorder(createTitledBorder(errorColor, fieldName));
            errorLbl.setText(errorMsg);
            errorLbl.setVisible(true);
            return false;
        } else {
            textField.setBorder(createTitledBorder(successColor, fieldName));
            errorLbl.setVisible(false);
            return true;
        }
    }

    /**
     * Creates a titled border with a custom color and font.
     *
     * @param color the color of the border and title text
     * @param title the title to display on the border
     * @return a TitledBorder object with the specified attributes
     */
    private javax.swing.border.TitledBorder createTitledBorder(Color color, String title) {
        return javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createLineBorder(color, 2),
                title,
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Segoe UI", 1, 12),
                color
        );
    }

    /**
     * Displays a dialog box with a custom message, title, and message type.
     *
     * @param message the message to display in the dialog
     * @param title the title of the dialog box
     * @param messageType the type of message (e.g., INFORMATION_MESSAGE,
     * WARNING_MESSAGE)
     */
    private void showDialogBox(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    /**
     * Clears all input fields in the candidate form.
     */
    private void clearCandidateForm() {
        txtCandidateNo.setText("");
        txtFldName.setText("");
        txtFldContact.setText("");
        ComboCategory.setSelectedItem("");
        ComboType.setSelectedItem("");
        txtFldDOB.setText("YYYY-MM-DD");
        txtFldCitizenshipNo.setText("");

        // Clear error messages
        lblErrorMsgCandidateId.setText("");
        lblErrorMsgName.setText("");
        lblErrorMsgContact.setText("");
        lblErrorMsgCitizenshipNo.setText("");
        lblErrorMsgDOB.setText("");
        lblErrorMsgCategory.setText("");
        lblErrorMsgType.setText("");
    }

    private boolean checkDuplicateField(String field, String value) {
        switch (field) {
            case "candidateNo":
                return CandidateList.stream().anyMatch(candidate
                        -> Integer.toString(candidate.getCandidateNo()).equals(value));
            case "contact":
                return CandidateList.stream().anyMatch(candidate
                        -> candidate.getContact().equals(value));
            case "citizenshipNo":
                return CandidateList.stream().anyMatch(candidate
                        -> candidate.getCitizenshipNo().equals(value));
            default:
                throw new IllegalArgumentException("Invalid field for duplicate check: " + field);
        }
    }

    /**
     * Handles the action performed when the Update button is clicked.
     *
     * @param evt ActionEvent triggered by the button click
     */
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {
        boolean isValid = true;

        // Validate Candidate ID
        String candidateNoInput = txtCandidateNo.getText().trim();
        if (ValidationUtil.isNullOrEmpty(candidateNoInput)) {
            lblErrorMsgCandidateId.setText("Candidate ID cannot be empty.");
            txtCandidateNo.setBorder(createTitledBorder(errorColor, "Candidate ID"));
            isValid = false;
        } else if (!candidateNoInput.matches("\\d+")) {
            lblErrorMsgCandidateId.setText("Candidate ID must be numeric.");
            txtCandidateNo.setBorder(createTitledBorder(errorColor, "Candidate ID"));
            isValid = false;
        } else if (candidateNoInput.length() < 4 || candidateNoInput.length() > 5) {
            lblErrorMsgCandidateId.setText("Candidate ID must be 4 to 5 digits long.");
            txtCandidateNo.setBorder(createTitledBorder(errorColor, "Candidate ID"));
            isValid = false;
        } else if (!candidateNoInput.startsWith("1")) {
            lblErrorMsgCandidateId.setText("Candidate ID must start with '1'.");
            txtCandidateNo.setBorder(createTitledBorder(errorColor, "Candidate ID"));
            isValid = false;
        } else {
            lblErrorMsgCandidateId.setText("");
            txtCandidateNo.setBorder(createTitledBorder(yellowColor, "Candidate ID"));
        }

        // Check if the Candidate ID exists in the CandidateList
        int candidateNo = -1;
        int rowIndex = -1;

        if (isValid) {
            candidateNo = Integer.parseInt(candidateNoInput);
            for (int i = 0; i < CandidateList.size(); i++) {
                if (CandidateList.get(i).getCandidateNo() == candidateNo) {
                    rowIndex = i;
                    break;
                }
            }

            if (rowIndex == -1) {
                lblErrorMsgCandidateId.setText("Candidate with the entered ID does not exist.");
                txtCandidateNo.setBorder(createTitledBorder(errorColor, "Candidate ID"));
                return; // Stop if the candidate doesn't exist
            }
        }

        // Validate Name
        String name = txtFldName.getText().trim();
        if (ValidationUtil.isNullOrEmpty(name)) {
            lblErrorMsgName.setText("Name cannot be empty.");
            txtFldName.setBorder(createTitledBorder(errorColor, "Name"));
            isValid = false;
        } else if (name.chars().filter(ch -> ch == ' ').count() < 1) {
            lblErrorMsgName.setText("Name must have at least one space.");
            txtFldName.setBorder(createTitledBorder(errorColor, "Name"));
            isValid = false;
        } else if (name.chars().filter(ch -> ch == ' ').count() > 3) {
            lblErrorMsgName.setText("Name cannot have more than three spaces.");
            txtFldName.setBorder(createTitledBorder(errorColor, "Name"));
            isValid = false;
        } else if (name.matches(".*\\d.*")) {
            lblErrorMsgName.setText("Name cannot contain numbers.");
            txtFldName.setBorder(createTitledBorder(errorColor, "Name"));
            isValid = false;
        } else if (!name.matches("^[A-Z][a-z]+(\\s[A-Z][a-z]+)*$")) {
            lblErrorMsgName.setText("Each word must start with a capital letter.");
            txtFldName.setBorder(createTitledBorder(errorColor, "Name"));
            isValid = false;
        } else {
            lblErrorMsgName.setText("");
            txtFldName.setBorder(createTitledBorder(yellowColor, "Name"));
        }

        // Validate Contact
        String contact = txtFldContact.getText().trim();
        if (ValidationUtil.isNullOrEmpty(contact)) {
            lblErrorMsgContact.setText("Contact number cannot be empty.");
            txtFldContact.setBorder(createTitledBorder(errorColor, "Contact"));
            isValid = false;
        } else if (!contact.startsWith("98") && !contact.startsWith("97")) {
            lblErrorMsgContact.setText("Contact must start with '98' or '97'.");
            txtFldContact.setBorder(createTitledBorder(errorColor, "Contact"));
            isValid = false;
        } else if (contact.length() != 10) {
            lblErrorMsgContact.setText("Contact number must be 10 digits long.");
            txtFldContact.setBorder(createTitledBorder(errorColor, "Contact"));
            isValid = false;
        } else if (!ValidationUtil.isValidContact(contact)) {
            lblErrorMsgContact.setText("Contact number is already used.");
            txtFldContact.setBorder(createTitledBorder(errorColor, "Contact"));
            isValid = false;
        } else {
            lblErrorMsgContact.setText("");
            txtFldContact.setBorder(createTitledBorder(yellowColor, "Contact"));
        }

        // Validate Date of Birth
        String dob = txtFldDOB.getText().trim();
        if (ValidationUtil.isNullOrEmpty(dob)) {
            lblErrorMsgDOB.setText("Date of Birth cannot be empty.");
            txtFldDOB.setBorder(createTitledBorder(errorColor, "Date of Birth"));
            isValid = false;
        } else if (!ValidationUtil.isValidDateOfBirth(dob)) {
            lblErrorMsgDOB.setText("Date of Birth must be in yyyy-mm-dd format.");
            txtFldDOB.setBorder(createTitledBorder(errorColor, "Date of Birth"));
            isValid = false;
        } else {
            LocalDate birthDate = LocalDate.parse(dob);
            LocalDate currentDate = LocalDate.now();
            int age = Period.between(birthDate, currentDate).getYears();

            if (age < 18) {
                lblErrorMsgDOB.setText("Age must be at least 18.");
                txtFldDOB.setBorder(createTitledBorder(errorColor, "Date of Birth"));
                isValid = false;
            } else if (age > 60) {
                lblErrorMsgDOB.setText("Age must not exceed 60.");
                txtFldDOB.setBorder(createTitledBorder(errorColor, "Date of Birth"));
                isValid = false;
            } else {
                lblErrorMsgDOB.setText("");
                txtFldDOB.setBorder(createTitledBorder(yellowColor, "Date of Birth"));
            }
        }

        // Validate Citizenship Number
        String citizenshipNo = txtFldCitizenshipNo.getText().trim();
        if (ValidationUtil.isNullOrEmpty(citizenshipNo)) {
            lblErrorMsgCitizenshipNo.setText("Citizenship Number cannot be empty.");
            txtFldCitizenshipNo.setBorder(createTitledBorder(errorColor, "Citizenship Number"));
            isValid = false;
        } else if (!ValidationUtil.isValidCitizenshipNo(citizenshipNo)) {
            lblErrorMsgCitizenshipNo.setText("Citizenship Number must be numeric with 3-4 hyphens.");
            txtFldCitizenshipNo.setBorder(createTitledBorder(errorColor, "Citizenship Number"));
            isValid = false;
        } else {
            lblErrorMsgCitizenshipNo.setText("");
            txtFldCitizenshipNo.setBorder(createTitledBorder(yellowColor, "Citizenship Number"));
        }

        // If all validations pass
        if (isValid) {
            CandidateModel selectedCandidate = CandidateList.get(rowIndex);

            // Update the candidate details
            selectedCandidate.setName(name);
            selectedCandidate.setContact(contact);
            selectedCandidate.setDateOfBirth(dob);
            selectedCandidate.setCitizenshipNo(citizenshipNo);
            selectedCandidate.setCategory((String) ComboCategory.getSelectedItem());
            selectedCandidate.setType((String) ComboType.getSelectedItem());

            // Update the table
            loadListToTable(CandidateList);

            // Show success message
            showDialogBox("Candidate updated successfully.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);

            // Clear the form
            clearCandidateForm();
        }
    }

    /**
     * Handles the action performed when the Delete button is clicked.
     *
     * @param evt ActionEvent triggered by the button click
     */
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // Check if the candidate number text field is filled
        String candidateNoInput = txtCandidateNo.getText().trim();

        if (!ValidationUtil.isNullOrEmpty(candidateNoInput)) {
            // User entered a candidate number
            if (!candidateNoInput.matches("\\d+")) {
                lblErrorMsgCandidateId.setText("Candidate ID must be numeric.");
                return;
            }

            int candidateNo = Integer.parseInt(candidateNoInput);

            // Check if the candidate exists in the list
            CandidateModel candidateToDelete = null;
            for (CandidateModel candidate : CandidateList) {
                if (candidate.getCandidateNo() == candidateNo) {
                    candidateToDelete = candidate;
                    break;
                }
            }

            if (candidateToDelete == null) {
                // Candidate not found
                lblErrorMsgCandidateId.setText("Candidate with the entered ID does not exist.");
            } else {
                // Confirm the delete operation
                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete the candidate with ID: " + candidateNo + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    // Remove the candidate from the list
                    CandidateList.remove(candidateToDelete);

                    // Refresh the table
                    loadListToTable(CandidateList);

                    // Show success message
                    lblErrorMsgCandidateId.setText("Candidate deleted successfully.");

                    // Clear the text field
                    txtCandidateNo.setText("");
                }
            }
        } else {
            // No candidate number entered, proceed with table row deletion
            int selectedRow = tblCandidate.getSelectedRow();

            if (selectedRow == -1) {
                // No row selected, show an error message
                lblErrorMsgCandidateId.setText("Please select a candidate to delete.");
            } else {
                // Confirm the delete operation
                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete the selected candidate?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    // Remove the candidate from the list
                    CandidateList.remove(selectedRow);

                    // Refresh the table
                    loadListToTable(CandidateList);

                    // Show success message
                    lblErrorMsgCandidateId.setText("Candidate deleted successfully.");
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InfinityTrail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InfinityTrail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InfinityTrail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InfinityTrail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        InfinityTrail app = new InfinityTrail();


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            app.setVisible(true);
        });

        app.startProgress();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AboutUSmembers;
    private javax.swing.JComboBox<String> ComboCategory;
    private javax.swing.JComboBox<String> ComboSort;
    private javax.swing.JComboBox<String> ComboType;
    private javax.swing.JPanel PnlServices1;
    private javax.swing.JPanel Results;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAscending;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDescending;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnStartTrial;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel hometitle;
    private javax.swing.JLabel hometxt;
    private javax.swing.JLabel hometxt1;
    private javax.swing.JLabel hometxt2;
    private javax.swing.JLabel imgBike;
    private javax.swing.JLabel imgcar;
    private javax.swing.JLabel imgcone;
    private javax.swing.JLabel imgmember1;
    private javax.swing.JLabel imgmember2;
    private javax.swing.JLabel imgmember3;
    private javax.swing.JLabel imgscooter;
    private javax.swing.JLabel imgservices;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblErrorMsgCandidateId;
    private javax.swing.JLabel lblErrorMsgCategory;
    private javax.swing.JLabel lblErrorMsgCitizenshipNo;
    private javax.swing.JLabel lblErrorMsgContact;
    private javax.swing.JLabel lblErrorMsgDOB;
    private javax.swing.JLabel lblErrorMsgName;
    private javax.swing.JLabel lblErrorMsgType;
    private javax.swing.JLabel lblLoading;
    private javax.swing.JLabel lblLogInError;
    private javax.swing.JLabel lblLoginError;
    private javax.swing.JLabel lblLoginForgotPwd;
    private javax.swing.JLabel lblMainBarSlogan;
    private javax.swing.JLabel lblTblCandidateTitle;
    private javax.swing.JLabel lblimageHome;
    private javax.swing.JLabel lbllogo;
    private javax.swing.JLabel lbltitleservices;
    private javax.swing.JLabel lbltitleservices2;
    private javax.swing.JLabel lbltitleservices3;
    private javax.swing.JLabel lbltitleservices4;
    private javax.swing.JLabel membername;
    private javax.swing.JLabel membername2;
    private javax.swing.JLabel membername3;
    private javax.swing.JLabel membertitle3;
    private javax.swing.JProgressBar pgBarSplashScreen;
    private javax.swing.JPanel pnlAboutUs;
    private javax.swing.JPanel pnlAdminControl;
    private javax.swing.JPanel pnlCandidateList;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlLoadingScreen;
    private javax.swing.JPanel pnlLoginLeft;
    private javax.swing.JPanel pnlLoginScreen;
    private javax.swing.JPanel pnlMainBar;
    private javax.swing.JPanel pnlMainScreen;
    private javax.swing.JPanel pnlmember1;
    private javax.swing.JPanel pnlmember2;
    private javax.swing.JPanel pnlmember3;
    private javax.swing.JPanel pnlourservices;
    private javax.swing.JPanel pnlservices2;
    private javax.swing.JPanel pnlservices3;
    private javax.swing.JPasswordField pwdFldLogin;
    private javax.swing.JLabel services2;
    private javax.swing.JScrollPane spTblStudent;
    private javax.swing.JScrollPane spTblStudent1;
    private javax.swing.JTabbedPane tabPaneMain;
    private javax.swing.JTable tblCandidate;
    private javax.swing.JTable tblResults;
    private javax.swing.JLabel titleAboutUS;
    private javax.swing.JLabel titlemember1;
    private javax.swing.JLabel titlemember2;
    private javax.swing.JLabel txtAboutUS;
    private javax.swing.JTextField txtBinarySearch;
    private javax.swing.JTextField txtCandidateNo;
    private javax.swing.JTextField txtFldCitizenshipNo;
    private javax.swing.JTextField txtFldContact;
    private javax.swing.JTextField txtFldDOB;
    private javax.swing.JTextField txtFldLoginUsername;
    private javax.swing.JTextField txtFldName;
    private javax.swing.JLabel txtservices1;
    private javax.swing.JLabel txtservices2;
    private javax.swing.JLabel txtservices3;
    private javax.swing.JLabel txtservices4;
    // End of variables declaration//GEN-END:variables

}
