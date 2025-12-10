package com.mycompany.crs.gui;

import com.mycompany.crs.model.Student;
import com.mycompany.crs.service.EligibilityService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * EligibilityPage - WITH SEARCH FUNCTIONALITY
 * Lists students NOT eligible for progression
 * 
 * @author YourName
 * @version 5.0
 */
public class EligibilityPage extends JFrame {
    
    private EligibilityService eligibilityService;
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel statsLabel;
    private JLabel titleLabel;
    private JTextField searchField;
    private List<Student> allIneligibleStudents;
    
    public EligibilityPage() {
        eligibilityService = new EligibilityService();
        initComponents();
        loadIneligibleStudents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Course Recovery System - Eligibility Check");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // TOP PANEL
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(220, 53, 69));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        titleLabel = new JLabel("⚠️ Students NOT Eligible for Progression");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statsLabel.setForeground(Color.WHITE);
        statsLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        topPanel.add(statsLabel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // SEARCH PANEL
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchPanel.setBackground(new Color(248, 249, 250));
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 226, 230)));
        
        JLabel searchLabel = new JLabel("🔍 Search Student:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setToolTipText("Search by Student ID, Name, Major, or Year");
        
        JButton searchButton = createStyledButton("Search", new Color(0, 123, 255));
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.addActionListener(e -> performSearch());
        
        JButton clearButton = createStyledButton("Clear", new Color(108, 117, 125));
        clearButton.setPreferredSize(new Dimension(100, 30));
        clearButton.addActionListener(e -> clearSearch());
        
        // Real-time search as you type
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                performSearch();
            }
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        
        add(searchPanel, BorderLayout.PAGE_START);
        
        // Fix layout by adding topPanel below searchPanel
        JPanel combinedTopPanel = new JPanel(new BorderLayout());
        combinedTopPanel.add(topPanel, BorderLayout.NORTH);
        combinedTopPanel.add(searchPanel, BorderLayout.SOUTH);
        add(combinedTopPanel, BorderLayout.NORTH);
        
        // CENTER PANEL - Table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        String[] columns = {
            "Student ID", "Full Name", "Major", "Year", 
            "CGPA", "Failed Courses", "Reason", "Status"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        studentsTable = new JTable(tableModel);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsTable.setRowHeight(28);
        studentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        studentsTable.getTableHeader().setBackground(new Color(248, 249, 250));
        studentsTable.setSelectionBackground(new Color(255, 193, 7));
        studentsTable.setSelectionForeground(Color.BLACK);
        studentsTable.setGridColor(new Color(222, 226, 230));
        
        // Set up row sorter for search
        sorter = new TableRowSorter<>(tableModel);
        studentsTable.setRowSorter(sorter);
        
        // Column widths
        studentsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        studentsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        studentsTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        studentsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        studentsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        studentsTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        studentsTable.getColumnModel().getColumn(6).setPreferredWidth(300);
        studentsTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        
        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        studentsTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        studentsTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        
        // Double-click to view details
        studentsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewStudentDetails();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // RIGHT PANEL - Info
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(280, 0));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));
        
        // Eligibility Criteria Panel
        JPanel criteriaPanel = new JPanel();
        criteriaPanel.setLayout(new BoxLayout(criteriaPanel, BoxLayout.Y_AXIS));
        criteriaPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 123, 255), 2),
            "Eligibility Criteria",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 13),
            new Color(0, 123, 255)
        ));
        criteriaPanel.setBackground(new Color(232, 244, 253));
        
        JLabel criteria1 = new JLabel("✓ CGPA must be ≥ 2.0");
        JLabel criteria2 = new JLabel("✓ Failed courses must be ≤ 3");
        criteria1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        criteria2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        criteria1.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        criteria2.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        criteriaPanel.add(criteria1);
        criteriaPanel.add(criteria2);
        
        rightPanel.add(criteriaPanel);
        rightPanel.add(Box.createVerticalStrut(15));
        
        // Instructions Panel
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
        
        JLabel inst1 = new JLabel("• Use search to find students");
        JLabel inst2 = new JLabel("• Double-click row for details");
        JLabel inst3 = new JLabel("• Check eligible students");
        inst1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        inst2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        inst3.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        inst1.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
        inst2.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        inst3.setBorder(BorderFactory.createEmptyBorder(2, 10, 5, 10));
        
        instructionsPanel.add(inst1);
        instructionsPanel.add(inst2);
        instructionsPanel.add(inst3);
        
        rightPanel.add(instructionsPanel);
        
        add(rightPanel, BorderLayout.EAST);
        
        // BOTTOM PANEL - Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        bottomPanel.setBackground(new Color(248, 249, 250));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)));
        
        JButton refreshButton = createStyledButton("🔄 Refresh", new Color(108, 117, 125));
        refreshButton.addActionListener(e -> {
            loadIneligibleStudents();
            clearSearch();
            JOptionPane.showMessageDialog(this, 
                "✓ Data refreshed successfully!", 
                "Refresh", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton viewDetailsButton = createStyledButton("👁 View Details", new Color(0, 123, 255));
        viewDetailsButton.addActionListener(e -> viewStudentDetails());
        
        JButton eligibleButton = createStyledButton("✓ View Eligible Students", new Color(40, 167, 69));
        eligibleButton.addActionListener(e -> showEligibleStudents());
        
        JButton exitButton = createStyledButton("✕ Exit", new Color(220, 53, 69));
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        bottomPanel.add(refreshButton);
        bottomPanel.add(viewDetailsButton);
        bottomPanel.add(eligibleButton);
        bottomPanel.add(exitButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
    
    private void loadIneligibleStudents() {
        tableModel.setRowCount(0);
        
        allIneligibleStudents = eligibilityService.getIneligibleStudents();
        
        for (Student student : allIneligibleStudents) {
            double cgpa = student.calculateCGPA();
            int failedCourses = student.countFailedCourses();
            String reason = student.getEligibilityReason();
            String status = student.getEnrollmentStatus();
            
            Object[] row = {
                student.getStudentId(),
                student.getFullName(),
                student.getMajor(),
                student.getYear(),
                String.format("%.2f", cgpa),
                failedCourses,
                reason,
                status
            };
            
            tableModel.addRow(row);
        }
        
        updateStatistics();
    }
    
    /**
     * Perform search based on search field input
     */
    private void performSearch() {
        String searchText = searchField.getText().trim();
        
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null); // Show all
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
        
        // Update stats with filtered count
        int visibleRows = studentsTable.getRowCount();
        updateStatistics();
        
        if (visibleRows == 0 && !searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No students found matching: " + searchText,
                "Search Results",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Clear search and show all students
     */
    private void clearSearch() {
        searchField.setText("");
        sorter.setRowFilter(null);
        updateStatistics();
    }
    
    private void updateStatistics() {
        Object[] stats = eligibilityService.getEligibilityStatistics();
        int total = (int) stats[0];
        int eligible = (int) stats[1];
        int ineligible = (int) stats[2];
        double eligibilityRate = (double) stats[3];
        
        int displayedRows = studentsTable.getRowCount();
        
        String statsText;
        if (displayedRows < ineligible) {
            statsText = String.format(
                "Showing %d of %d Ineligible Students | Total: %d | Eligible: %d (%.1f%%)",
                displayedRows, ineligible, total, eligible, eligibilityRate
            );
        } else {
            statsText = String.format(
                "Total Students: %d | Eligible: %d (%.1f%%) | Ineligible: %d (%.1f%%)",
                total, eligible, eligibilityRate, ineligible, (100 - eligibilityRate)
            );
        }
        
        statsLabel.setText(statsText);
    }
    
    private void viewStudentDetails() {
        int selectedRow = studentsTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a student from the table first.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row index to model row index (important for sorted/filtered tables)
        int modelRow = studentsTable.convertRowIndexToModel(selectedRow);
        String studentId = (String) tableModel.getValueAt(modelRow, 0);
        
        StudentDetailsPage detailsPage = new StudentDetailsPage(studentId);
        detailsPage.setVisible(true);
    }
    
    private void showEligibleStudents() {
        EnrollmentPage enrollmentPage = new EnrollmentPage();
        enrollmentPage.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EligibilityPage page = new EligibilityPage();
            page.setVisible(true);
        });
    }
}