package com.mycompany.crs.gui;

import com.mycompany.crs.model.Student;
import com.mycompany.crs.model.CourseEnrollment;
import com.mycompany.crs.service.EligibilityService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * StudentDetailsPage - Shows student's CGPA calculation and course grades
 * 
 * OOP Concepts:
 * - Inheritance: Extends JFrame
 * - Composition: Uses Student and EligibilityService
 * - Encapsulation: Private helper methods
 * 
 * Requirement: "Shows student's CGPA calculation and course grades"
 * 
 * @author YourName
 * @version 1.0
 */
public class StudentDetailsPage extends JFrame {
    
    private EligibilityService eligibilityService;
    private String studentId;
    private Student student;
    
    private JTable coursesTable;
    private DefaultTableModel tableModel;
    
    /**
     * Constructor
     */
    public StudentDetailsPage(String studentId) {
        this.studentId = studentId;
        this.eligibilityService = new EligibilityService();
        
        initComponents();
        loadStudentData();
        setLocationRelativeTo(null);
    }
    
    /**
     * Initialize GUI components
     */
    private void initComponents() {
        setTitle("Student Details & CGPA Calculation");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Top panel - Student info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 123, 255), 2),
                "Student Information",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(0, 123, 255)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        infoPanel.setBackground(new Color(232, 244, 253));
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Center panel - Courses table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            "Course Grades & CGPA Calculation"
        ));
        
        String[] columns = {
            "Course Code", "Course Name", "Credits", 
            "Exam", "Assignment", "Total", "Grade", "Grade Point", "Quality Points"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        coursesTable = new JTable(tableModel);
        coursesTable.setRowHeight(26);
        coursesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        coursesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        coursesTable.getTableHeader().setBackground(new Color(248, 249, 250));
        
        // Center align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 2; i < 9; i++) {
            coursesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Highlight failed courses in red
        coursesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                                                                 isSelected, hasFocus, row, column);
                
                // Check if grade point < 2.0 (failed)
                try {
                    double gradePoint = Double.parseDouble(table.getValueAt(row, 7).toString());
                    if (gradePoint < 2.0) {
                        c.setBackground(new Color(255, 230, 230));
                        c.setForeground(new Color(220, 53, 69));
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } catch (Exception e) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                
                if (isSelected) {
                    c.setBackground(new Color(255, 193, 7));
                    c.setForeground(Color.BLACK);
                }
                
                if (column >= 2) {
                    ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Bottom panel - Summary
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Close button - VISIBLE STYLING
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton closeButton = new JButton("← Back");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(new Color(0, 123, 255)); // BRIGHT BLUE
        closeButton.setForeground(Color.BLACK); // BLACK TEXT
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(140, 40));
        closeButton.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 200), 3)); // THICK BORDER
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Load student data and display
     */
    private void loadStudentData() {
        student = eligibilityService.getStudentById(studentId);
        
        if (student == null) {
            JOptionPane.showMessageDialog(this,
                "Student not found: " + studentId,
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        // Update info panel
        JPanel infoPanel = (JPanel) ((JPanel) getContentPane().getComponent(0))
                                     .getComponent(0);
        infoPanel.removeAll();
        
        addInfoLabel(infoPanel, "Student ID:", student.getStudentId());
        addInfoLabel(infoPanel, "Full Name:", student.getFullName());
        addInfoLabel(infoPanel, "Major:", student.getMajor());
        addInfoLabel(infoPanel, "Year:", student.getYear());
        addInfoLabel(infoPanel, "Email:", student.getEmail());
        
        infoPanel.revalidate();
        infoPanel.repaint();
        
        // Load courses into table
        tableModel.setRowCount(0);
        
        List<CourseEnrollment> enrollments = student.getEnrolledCourses();
        double totalQualityPoints = 0.0;
        int totalCredits = 0;
        
        for (CourseEnrollment enrollment : enrollments) {
            int credits = enrollment.getCourse().getCredits();
            double gradePoint = enrollment.getGradePoint();
            double qualityPoints = credits * gradePoint;
            
            totalQualityPoints += qualityPoints;
            totalCredits += credits;
            
            Object[] row = {
                enrollment.getCourse().getCourseId(),
                enrollment.getCourse().getCourseName(),
                credits,
                enrollment.getExamScore(),
                enrollment.getAssignmentScore(),
                String.format("%.1f", enrollment.getTotalScore()),
                enrollment.getLetterGrade(),
                String.format("%.2f", gradePoint),
                String.format("%.2f", qualityPoints)
            };
            
            tableModel.addRow(row);
        }
        
        // Add summary row
        double cgpa = student.calculateCGPA();
        int failedCount = student.countFailedCourses();
        
        Object[] summaryRow = {
            "", "TOTAL →", totalCredits, "", "", "", "",
            String.format("%.2f", cgpa),
            String.format("%.2f", totalQualityPoints)
        };
        tableModel.addRow(summaryRow);
        
        // Add CGPA calculation explanation in bottom panel
        JPanel bottomPanel = (JPanel) ((JPanel) getContentPane().getComponent(0))
                                       .getComponent(2);
        bottomPanel.removeAll();
        
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("CGPA Calculation & Eligibility"));
        
        JLabel calcLabel = new JLabel(String.format(
            "CGPA = Total Quality Points ÷ Total Credits = %.2f ÷ %d = %.2f",
            totalQualityPoints, totalCredits, cgpa
        ));
        calcLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        calcLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel failedLabel = new JLabel(String.format(
            "Failed Courses (Grade Point < 2.0): %d courses",
            failedCount
        ));
        failedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        failedLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        if (failedCount > 3) {
            failedLabel.setForeground(new Color(220, 53, 69));
        }
        
        JLabel eligibilityLabel = new JLabel(
            "Eligibility Status: " + (student.isEligible() ? "✓ ELIGIBLE" : "✗ NOT ELIGIBLE")
        );
        eligibilityLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        eligibilityLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        eligibilityLabel.setForeground(student.isEligible() ? 
                                      new Color(40, 167, 69) : new Color(220, 53, 69));
        
        JLabel reasonLabel = new JLabel("Reason: " + student.getEligibilityReason());
        reasonLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        reasonLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        summaryPanel.add(calcLabel);
        summaryPanel.add(failedLabel);
        summaryPanel.add(eligibilityLabel);
        summaryPanel.add(reasonLabel);
        
        bottomPanel.add(summaryPanel, BorderLayout.CENTER);
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }
    
    /**
     * Add info label to panel
     */
    private void addInfoLabel(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel.setOpaque(false);
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLabel.setPreferredSize(new Dimension(100, 20));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        rowPanel.add(lblLabel);
        rowPanel.add(lblValue);
        panel.add(rowPanel);
    }
}