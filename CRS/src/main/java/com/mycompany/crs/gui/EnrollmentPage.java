package com.mycompany.crs.gui;

import com.mycompany.crs.model.Student;
import com.mycompany.crs.model.Enrollment;
import com.mycompany.crs.service.EligibilityService;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EnrollmentPage extends JFrame {

    private EligibilityService eligibilityService;
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JLabel statsLabel;

    // 🔍 Search components declared
    private JTextField searchField;
    private JButton searchButton;

    public EnrollmentPage() {
        // Set Look and Feel to System Default for better consistency
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set System Look and Feel.");
        }
        
        eligibilityService = new EligibilityService();
        initComponents();
        loadEligibleStudents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Course Recovery System - Student Enrollment");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- TOP HEADER PANEL (GREEN) ---
        JPanel topHeaderPanel = new JPanel(new BorderLayout());
        topHeaderPanel.setBackground(new Color(40, 167, 69));
        topHeaderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("✓ Eligible Students - Ready for Enrollment");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topHeaderPanel.add(titleLabel, BorderLayout.NORTH);

        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        topHeaderPanel.add(statsLabel, BorderLayout.CENTER);

        // --- 🔍 SEARCH BAR PANEL (OFF-WHITE BACKGROUND - SAME AS RED WINDOW) ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(new Color(248, 249, 250)); // OFF-WHITE BACKGROUND
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 226, 230)));

        // 1. Search Label
        JLabel searchLabel = new JLabel("Search Student:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(searchField);

        // 2. Search Button (Uses the synced method)
        searchButton = createStyledSearchButton("Search", new Color(0, 123, 255)); // Bright Blue
        searchButton.setPreferredSize(new Dimension(100, 30)); 
        searchPanel.add(searchButton);

        // 3. Clear Button (Uses the synced method)
        JButton clearButton = createStyledSearchButton("Clear", new Color(108, 117, 125)); // Gray
        clearButton.setPreferredSize(new Dimension(100, 30));
        searchPanel.add(clearButton);

        // Action Listeners
        searchButton.addActionListener(e -> filterTable());

        clearButton.addActionListener(e -> {
            searchField.setText("");
            filterTable();
        });

        // Real-time filtering while typing
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        // Combine Header and Search Panel 
        JPanel combinedTopPanel = new JPanel(new BorderLayout());
        combinedTopPanel.add(topHeaderPanel, BorderLayout.NORTH);
        combinedTopPanel.add(searchPanel, BorderLayout.SOUTH);
        
        add(combinedTopPanel, BorderLayout.NORTH);
        // --- END OF SEARCH BAR ADDITION ---

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel instructionLabel = new JLabel("💡 Double-click any student row to enroll them for next semester");
        instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        instructionLabel.setForeground(new Color(108, 117, 125));
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        centerPanel.add(instructionLabel, BorderLayout.NORTH);

        String[] columns = {
            "Student ID", "Full Name", "Major", "Current Year",
            "CGPA", "Failed Courses", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentsTable = new JTable(tableModel);
        studentsTable.setRowHeight(32);
        studentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentsTable.getTableHeader().setBackground(new Color(248, 249, 250));
        studentsTable.setGridColor(new Color(222, 226, 230));
        studentsTable.setSelectionBackground(new Color(0, 123, 255));
        studentsTable.setSelectionForeground(Color.WHITE);

        studentsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        studentsTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        studentsTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        studentsTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        studentsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        studentsTable.getColumnModel().getColumn(5).setPreferredWidth(130);
        studentsTable.getColumnModel().getColumn(6).setPreferredWidth(120);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 4; i <= 6; i++) {
            studentsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        studentsTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                                                                 isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);

                if (!isSelected) {
                    if ("Enrolled".equals(value)) {
                        c.setBackground(new Color(217, 237, 247));
                        c.setForeground(new Color(31, 112, 179));
                        setFont(new Font("Segoe UI", Font.BOLD, 12));
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    }
                }

                return c;
            }
        });

        studentsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = studentsTable.getSelectedRow();
                    if (row != -1) {
                        processEnrollment(row);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentsTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottomPanel.setBackground(new Color(248, 249, 250));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)));

        // Uses the regular createButton helper method for the bottom panel
        JButton refreshButton = createButton("🔄 Refresh", new Color(108, 117, 125));
        refreshButton.addActionListener(e -> {
            loadEligibleStudents();
            JOptionPane.showMessageDialog(this, "✓ Data refreshed successfully!",
                                         "Refresh", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton enrollSelectedButton = createButton("📝 Enroll Selected Student", new Color(0, 123, 255));
        enrollSelectedButton.addActionListener(e -> {
            int row = studentsTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                    "Please select a student from the table first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                processEnrollment(row);
            }
        });

        JButton closeButton = createButton("✕ Close", new Color(220, 53, 69));
        closeButton.addActionListener(e -> dispose());

        bottomPanel.add(refreshButton);
        bottomPanel.add(enrollSelectedButton);
        bottomPanel.add(closeButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // --- HELPER METHOD SYNCED WITH EligibilityPage.java FOR SEARCH BAR BUTTONS ---

    /**
     * Creates a styled JButton identical to the one in EligibilityPage
     * (Used for Search/Clear buttons only).
     */
    private JButton createStyledSearchButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 30)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect identical to the red window
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    // --- Original createButton for the bottom panel ---
    
    // Helper method to create styled buttons for the bottom panel (Original one, slightly different size)
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void loadEligibleStudents() {
        tableModel.setRowCount(0);

        List<Student> eligibleStudents = eligibilityService.getEligibleStudents();

        for (Student student : eligibleStudents) {
            Object[] row = {
                student.getStudentId(),
                student.getFullName(),
                student.getMajor(),
                student.getYear(),
                String.format("%.2f", student.calculateCGPA()),
                student.countFailedCourses(),
                student.getEnrollmentStatus()
            };

            tableModel.addRow(row);
        }

        statsLabel.setText("Total Eligible Students: " + eligibleStudents.size());
    }

    private void processEnrollment(int row) {
        String studentId = (String) tableModel.getValueAt(row, 0);
        String studentName = (String) tableModel.getValueAt(row, 1);
        String currentYear = (String) tableModel.getValueAt(row, 3);
        String status = (String) tableModel.getValueAt(row, 6);

        if ("Enrolled".equals(status)) {
            JOptionPane.showMessageDialog(this,
                "✓ Student " + studentName + " is already enrolled for next semester!",
                "Already Enrolled",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog enrollDialog = new JDialog(this, "Enroll Student - " + studentName, true);
        enrollDialog.setLayout(new BorderLayout(15, 15));
        enrollDialog.setSize(520, 420);
        enrollDialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 18));
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(createBoldLabel("Student ID:"));
        formPanel.add(createValueLabel(studentId));

        formPanel.add(createBoldLabel("Student Name:"));
        formPanel.add(createValueLabel(studentName));

        formPanel.add(createBoldLabel("Current Year:"));
        formPanel.add(createValueLabel(currentYear));

        formPanel.add(createBoldLabel("Next Semester:"));
        String[] semesters = {"Spring", "Summer", "Fall"};
        JComboBox<String> semesterCombo = new JComboBox<>(semesters);
        semesterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(semesterCombo);

        formPanel.add(createBoldLabel("Next Year:"));
        String[] years = {"Freshman", "Sophomore", "Junior", "Senior"};
        JComboBox<String> yearCombo = new JComboBox<>(years);
        yearCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        int currentYearIndex = getYearIndex(currentYear);
        yearCombo.setSelectedIndex(Math.min(currentYearIndex + 1, 3));
        formPanel.add(yearCombo);

        formPanel.add(createBoldLabel("Enrolled By:"));
        JTextField enrolledByField = new JTextField("Academic Officer");
        enrolledByField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(enrolledByField);

        enrollDialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)));

        JButton confirmButton = createButton("✓ Confirm Enrollment", new Color(40, 167, 69));
        confirmButton.setPreferredSize(new Dimension(190, 38));
        confirmButton.addActionListener(e -> {
            String nextSemester = (String) semesterCombo.getSelectedItem();
            String nextYear = (String) yearCombo.getSelectedItem();
            String enrolledBy = enrolledByField.getText().trim();

            if (enrolledBy.isEmpty()) {
                JOptionPane.showMessageDialog(enrollDialog,
                    "⚠ Please enter who is processing this enrollment.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Enrollment enrollment = eligibilityService.processEnrollment(
                studentId, nextSemester, nextYear, enrolledBy
            );

            if (enrollment != null) {
                JOptionPane.showMessageDialog(enrollDialog,
                    String.format("✓ Successfully enrolled %s!\n\n" +
                                     "📋 Enrollment ID: %s\n" +
                                     "📅 Next Semester: %s %s\n" +
                                     "👤 Processed by: %s",
                                     studentName, enrollment.getEnrollmentId(),
                                     nextSemester, nextYear, enrolledBy),
                    "Enrollment Success",
                    JOptionPane.INFORMATION_MESSAGE);

                enrollDialog.dispose();
                loadEligibleStudents();
            } else {
                JOptionPane.showMessageDialog(enrollDialog,
                    "✗ Failed to enroll student. Please check the console for details.",
                    "Enrollment Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = createButton("✕ Cancel", new Color(108, 117, 125));
        cancelButton.setPreferredSize(new Dimension(130, 38));
        cancelButton.addActionListener(e -> enrollDialog.dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        enrollDialog.add(buttonPanel, BorderLayout.SOUTH);
        enrollDialog.setVisible(true);
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(73, 80, 87));
        return label;
    }

    private int getYearIndex(String year) {
        switch (year) {
            case "Freshman": return 0;
            case "Sophomore": return 1;
            case "Junior": return 2;
            case "Senior": return 3;
            default: return 0;
        }
    }

    // 🔍 SEARCH FILTER METHOD - NOW INCLUDES THE NO RESULTS NOTIFICATION
    private void filterTable() {
        String keyword = searchField.getText().trim();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        studentsTable.setRowSorter(sorter);

        if (keyword.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            // Apply the filter first
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }

        // Check if the search yielded any results
        // studentsTable.getRowCount() gives the currently displayed rows (after filtering)
        int visibleRows = studentsTable.getRowCount();

        if (visibleRows == 0 && !keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No students found matching: " + keyword,
                "Search Results",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}