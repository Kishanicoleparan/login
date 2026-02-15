/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;


import java.util.HashSet;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Admin
 */
public class AdminDashboard extends javax.swing.JFrame {
// Stat labels for dynamic update
    private JLabel lblTotalReservations;
    private JLabel lblActiveCustomers;
    private JLabel lblPendingReservations;

    private JPanel topPanel;
    private JPanel centerPanel;
    private JTable upcomingTable;
    public AdminDashboard() {
         initComponents();
    
   
        
        setTitle("Catering Reservation Dashboard");
        setSize(1300, 800);
        jPanel1.setLayout(new BorderLayout());



        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize the dashboard panels
        
        setupDashboard();
        loadDashboardData();
        
    }
     // Setup dashboard after initComponents()
   private void setupDashboard() {

    // LEFT SIDE MENU stays
    jPanel1.removeAll();
    jPanel1.setLayout(new BorderLayout());

    // Add sidebar back
    jPanel1.add(jPanel2, BorderLayout.WEST);
    jPanel1.add(jPanel3, BorderLayout.NORTH);

    JPanel dashboardArea = new JPanel();
    dashboardArea.setLayout(new BorderLayout());
    dashboardArea.setBackground(Color.WHITE);

    jPanel1.add(dashboardArea, BorderLayout.CENTER);

    // ===== TOP STATS =====
    JPanel topPanel = new JPanel(new GridLayout(1, 3, 20, 20));
    topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    lblTotalReservations = createStatCard("Total Reservations", "0");
    lblActiveCustomers = createStatCard("Active Customers", "0");
    lblPendingReservations = createStatCard("Pending Reservations", "0");

    topPanel.add(lblTotalReservations);
    topPanel.add(lblActiveCustomers);
    topPanel.add(lblPendingReservations);

    dashboardArea.add(topPanel, BorderLayout.NORTH);
    // ===== Center Panel (Charts) =====
    JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    JFreeChart chart = ChartFactory.createLineChart(
            "Reservations Trend",
            "Date",
            "Reservations",
            dataset
    );

    centerPanel.add(new ChartPanel(chart));

    dashboardArea.add(centerPanel, BorderLayout.CENTER);

    // ===== Table =====
    String[] columns = {"Customer", "Event Date", "Guests", "Status"};
    upcomingTable = new JTable(new DefaultTableModel(columns, 0));

    JScrollPane scroll = new JScrollPane(upcomingTable);
    scroll.setPreferredSize(new Dimension(760, 120));

    dashboardArea.add(scroll, BorderLayout.SOUTH);
}



    // Helper: create stat card
    private JLabel createStatCard(String title, String value) {
        JLabel label = new JLabel("<html><center>" + title + "<br><h2>" + value + "</h2></center></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(100, 149, 237));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    // Wrap label in JPanel for spacing
    private JPanel wrapCard(JLabel label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return panel;
    }
    private void loadDashboardData() {
    try {
        java.sql.Connection conn = config.config.connectDB();

        // 1️⃣ Total Reservations
        String totalQuery = "SELECT COUNT(*) FROM tbl_reservations";
        java.sql.PreparedStatement pst1 = conn.prepareStatement(totalQuery);
        java.sql.ResultSet rs1 = pst1.executeQuery();
        if (rs1.next()) {
            lblTotalReservations.setText(
                "<html><center>Total Reservations<br><h2>" 
                + rs1.getInt(1) + "</h2></center></html>"
            );
        }

        // 2️⃣ Active Customers
        String activeQuery = "SELECT COUNT(*) FROM tbl_accounts WHERE status='APPROVED'";
        java.sql.PreparedStatement pst2 = conn.prepareStatement(activeQuery);
        java.sql.ResultSet rs2 = pst2.executeQuery();
        if (rs2.next()) {
            lblActiveCustomers.setText(
                "<html><center>Active Customers<br><h2>" 
                + rs2.getInt(1) + "</h2></center></html>"
            );
        }

        // 3️⃣ Pending Reservations
        String pendingQuery = "SELECT COUNT(*) FROM tbl_reservations WHERE status='PENDING'";
        java.sql.PreparedStatement pst3 = conn.prepareStatement(pendingQuery);
        java.sql.ResultSet rs3 = pst3.executeQuery();
        if (rs3.next()) {
            lblPendingReservations.setText(
                "<html><center>Pending Reservations<br><h2>" 
                + rs3.getInt(1) + "</h2></center></html>"
            );
        }

        // 4️⃣ Load Upcoming Reservations Table
        DefaultTableModel model = (DefaultTableModel) upcomingTable.getModel();
        model.setRowCount(0); // Clear existing rows

        String tableQuery = "SELECT customer_name, event_date, num_guests, status " +
                            "FROM tbl_reservations ORDER BY event_date ASC LIMIT 5";

        java.sql.PreparedStatement pst4 = conn.prepareStatement(tableQuery);
        java.sql.ResultSet rs4 = pst4.executeQuery();

        while (rs4.next()) {
            model.addRow(new Object[]{
                rs4.getString("customer_name"),
                rs4.getDate("event_date"),
                rs4.getInt("num_guests"),
                rs4.getString("status")
            });
        }

        conn.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Error loading dashboard data: " + e.getMessage());
    }
}


    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1ViewUsers = new javax.swing.JButton();
        UserProfile = new javax.swing.JButton();
        logout = new javax.swing.JButton();
        jButton3Reservations = new javax.swing.JButton();
        jButton4Packages = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton5dashboard = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1ViewUsers.setBackground(new java.awt.Color(255, 255, 255));
        jButton1ViewUsers.setText("View Users");
        jButton1ViewUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ViewUsersActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1ViewUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 140, -1));

        UserProfile.setBackground(new java.awt.Color(255, 255, 255));
        UserProfile.setText("Profile");
        UserProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserProfileActionPerformed(evt);
            }
        });
        jPanel2.add(UserProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 140, -1));

        logout.setText("Logout");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        jPanel2.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 140, -1));

        jButton3Reservations.setText("Reservations");
        jButton3Reservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ReservationsActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3Reservations, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 140, -1));

        jButton4Packages.setText("Packages");
        jButton4Packages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4PackagesActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4Packages, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 140, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/catering (1).png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 130, 120));

        jButton5dashboard.setText("Dashboard");
        jButton5dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5dashboardActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 140, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 540));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel6.setText("ADMIN DASHBOARD");
        jPanel3.add(jLabel6);

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 610, 90));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {

            // ✅ Clear session data
            Session.userId = 0;
            Session.fullname = null;
            Session.type = null;

            // ✅ Go back to login
            LoginForm lgf = new LoginForm();
            lgf.setVisible(true);
            lgf.pack();
            lgf.setLocationRelativeTo(null);

            // ✅ Close current window
            this.dispose();
        }
    }//GEN-LAST:event_logoutActionPerformed

    private void UserProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserProfileActionPerformed
        UsersProfile2 users = new UsersProfile2();
        users.setVisible(true);
        users.pack();
        users.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_UserProfileActionPerformed

    private void jButton1ViewUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ViewUsersActionPerformed
        UsersTable utbl = new UsersTable();
        utbl.setVisible(true);
        utbl.pack();
        utbl.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jButton1ViewUsersActionPerformed

    private void jButton4PackagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4PackagesActionPerformed
    PackagesTable pg = new PackagesTable();
    pg.setVisible(true);
    pg.pack();
    pg.setLocationRelativeTo(null);
    this.dispose();
    }//GEN-LAST:event_jButton4PackagesActionPerformed

    private void jButton5dashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5dashboardActionPerformed
       // Already on dashboard

    }//GEN-LAST:event_jButton5dashboardActionPerformed

    private void jButton3ReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ReservationsActionPerformed
    ReservationsTable rf = new ReservationsTable();
    rf.setVisible(true);
    rf.pack();
    rf.setLocationRelativeTo(null);
    this.dispose();
    }//GEN-LAST:event_jButton3ReservationsActionPerformed

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
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton UserProfile;
    private javax.swing.JButton jButton1ViewUsers;
    private javax.swing.JButton jButton3Reservations;
    private javax.swing.JButton jButton4Packages;
    private javax.swing.JButton jButton5dashboard;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton logout;
    // End of variables declaration//GEN-END:variables
}
