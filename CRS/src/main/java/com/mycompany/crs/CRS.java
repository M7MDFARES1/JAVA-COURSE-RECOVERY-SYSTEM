package com.mycompany.crs;

import com.mycompany.crs.gui.EligibilityPage;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main entry point for Course Recovery System
 * 
 * @author konda
 */
public class CRS {
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch the Eligibility Check page
        SwingUtilities.invokeLater(() -> {
            EligibilityPage page = new EligibilityPage();
            page.setVisible(true);
        });
    }
}