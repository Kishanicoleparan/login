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
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.awt.Color;



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
    private JDateChooser dateFilter;
private DefaultCategoryDataset dataset;
private JFreeChart chart;

    public AdminDashboard() {
         initComponents();
  

    // If logged in, continue loading dashboard
    setIconToLabel("/images/redefined.png", jLabel2);
    jLabelwel.setText("Welcome! " + Session.fullname);

    setTitle("Catering Reservation Dashboard");
   
    jPanel1.setLayout(new BorderLayout());

    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setupDashboard();
    loadDashboardData();
}

  

 private void setupDashboard() {

    // Clear old center components only
    jPanel1.removeAll();
    jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    // Sidebar (fixed)
    jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 540));

    // Top header
    jPanel1.add(Jpanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, 530, 60));

    // ===== MAIN DASHBOARD AREA =====
    JPanel dashboardArea = new JPanel();
    dashboardArea.setLayout(null);
    dashboardArea.setOpaque(true);
    dashboardArea.setBackground(Color.DARK_GRAY);


    jPanel1.add(dashboardArea,
            new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 60, 530, 480));

    // ===== STAT CARDS =====
    lblTotalReservations = createStatCard("Total Reservations", "0");
    lblActiveCustomers = createStatCard("Active Customers", "0");
    lblPendingReservations = createStatCard("Pending Reservations", "0");

    lblTotalReservations.setBounds(20, 20, 150, 100);
    lblActiveCustomers.setBounds(190, 20, 150, 100);
    lblPendingReservations.setBounds(360, 20, 150, 100);

    dashboardArea.add(lblTotalReservations);
    dashboardArea.add(lblActiveCustomers);
    dashboardArea.add(lblPendingReservations);

    // ===== DATE FILTER =====
    JLabel filterLabel = new JLabel("Filter by Date:");
    filterLabel.setForeground(Color.WHITE);
    filterLabel.setBounds(20, 140, 100, 25);

    dateFilter = new JDateChooser();
    dateFilter.setBounds(120, 140, 150, 25);

    JButton btnFilter = new JButton("Filter");
    btnFilter.setBounds(280, 140, 100, 25);
    btnFilter.addActionListener(e -> loadDashboardData());

    dashboardArea.add(filterLabel);
    dashboardArea.add(dateFilter);
    dashboardArea.add(btnFilter);

    // ===== CHART =====
    dataset = new DefaultCategoryDataset();

    chart = ChartFactory.createLineChart(
            "Reservation Trend",
            "Date",
            "Reservations",
            dataset
    );

    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setBounds(20, 190, 490, 260);

    dashboardArea.add(chartPanel);

    dashboardArea.revalidate();
    dashboardArea.repaint();
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

    
    private void loadDashboardData() {
    try {
        java.sql.Connection conn = config.config.connectDB();

        // =========================
        // TOTAL RESERVATIONS
        // =========================
        String totalQuery = "SELECT COUNT(*) FROM tbl_reservations";
        java.sql.PreparedStatement pst1 = conn.prepareStatement(totalQuery);
        java.sql.ResultSet rs1 = pst1.executeQuery();
        if (rs1.next()) {
            lblTotalReservations.setText(
                "<html><center>Total Reservations<br><h2>" 
                + rs1.getInt(1) + "</h2></center></html>"
            );
        }

        // =========================
        // ACTIVE CUSTOMERS
        // =========================
        String activeQuery = "SELECT COUNT(*) FROM tbl_accounts WHERE status='APPROVED'";
        java.sql.PreparedStatement pst2 = conn.prepareStatement(activeQuery);
        java.sql.ResultSet rs2 = pst2.executeQuery();
        if (rs2.next()) {
            lblActiveCustomers.setText(
                "<html><center>Active Customers<br><h2>" 
                + rs2.getInt(1) + "</h2></center></html>"
            );
        }

        // =========================
        // PENDING RESERVATIONS
        // =========================
        String pendingQuery = "SELECT COUNT(*) FROM tbl_reservations WHERE status='PENDING'";
        java.sql.PreparedStatement pst3 = conn.prepareStatement(pendingQuery);
        java.sql.ResultSet rs3 = pst3.executeQuery();
        if (rs3.next()) {
            lblPendingReservations.setText(
                "<html><center>Pending Reservations<br><h2>" 
                + rs3.getInt(1) + "</h2></center></html>"
            );
        }

        // =========================
        // CHART DATA
        // =========================
        dataset.clear();

        String chartQuery = 
            "SELECT event_date, COUNT(*) as total " +
            "FROM tbl_reservations ";

        if (dateFilter.getDate() != null) {
            chartQuery += "WHERE event_date = ? ";
        }

        chartQuery += "GROUP BY event_date ORDER BY event_date ASC";

        java.sql.PreparedStatement pstChart = conn.prepareStatement(chartQuery);

        if (dateFilter.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            pstChart.setString(1, sdf.format(dateFilter.getDate()));
        }

        java.sql.ResultSet rsChart = pstChart.executeQuery();

        while (rsChart.next()) {
            dataset.addValue(
                rsChart.getInt("total"),
                "Reservations",
                rsChart.getString("event_date")
            );
        }

        conn.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error loading dashboard data: " + e.getMessage());
    }
}
    private void setIconToLabel(String imagePath, javax.swing.JLabel label) {

    ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
    Image img = icon.getImage();

    Image scaledImg = img.getScaledInstance(
            label.getWidth(),
            label.getHeight(),
            Image.SCALE_SMOOTH
    );

    label.setIcon(new ImageIcon(scaledImg));
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
        jButton5dashboard = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        Jpanel3 = new javax.swing.JPanel();
        jLabelwelcome = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelwel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(java.awt.Color.darkGray);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1ViewUsers.setBackground(new java.awt.Color(0, 153, 153));
        jButton1ViewUsers.setText("Manage Users");
        jButton1ViewUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ViewUsersActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1ViewUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 140, -1));

        UserProfile.setBackground(new java.awt.Color(0, 153, 153));
        UserProfile.setText("Profile");
        UserProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserProfileActionPerformed(evt);
            }
        });
        jPanel2.add(UserProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 140, -1));

        logout.setBackground(new java.awt.Color(0, 153, 153));
        logout.setText("Logout");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });
        jPanel2.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 140, -1));

        jButton3Reservations.setBackground(new java.awt.Color(0, 153, 153));
        jButton3Reservations.setText("Reservations");
        jButton3Reservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ReservationsActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3Reservations, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 140, -1));

        jButton4Packages.setBackground(new java.awt.Color(0, 204, 204));
        jButton4Packages.setText("Packages");
        jButton4Packages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4PackagesActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4Packages, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 140, -1));

        jButton5dashboard.setBackground(new java.awt.Color(0, 204, 204));
        jButton5dashboard.setText("Dashboard");
        jButton5dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5dashboardActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 140, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/redefined.png"))); // NOI18N
        jLabel2.setOpaque(true);
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, 150));

        jButton1.setText("Manage Staff");
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 490, 140, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 540));

        Jpanel3.setBackground(java.awt.Color.darkGray);
        Jpanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelwelcome.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        Jpanel3.add(jLabelwelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 19, -1, -1));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("ADMIN DASHBOARD");
        Jpanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));

        jLabelwel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabelwel.setForeground(new java.awt.Color(255, 255, 255));
        jLabelwel.setText("jLabel1");
        Jpanel3.add(jLabelwel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jPanel1.add(Jpanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 580, 60));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 540));

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
            Session.u_id = 0;
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
      AdminDashboard adm = new AdminDashboard();
      adm.setVisible(true);
      this.dispose();

    }//GEN-LAST:event_jButton5dashboardActionPerformed

    private void jButton3ReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ReservationsActionPerformed
    ReservationsTable rf = new ReservationsTable();
    rf.setVisible(true);
    rf.pack();
    rf.setLocationRelativeTo(null);
    this.dispose();
    }//GEN-LAST:event_jButton3ReservationsActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
       // 🔐 CHECK LOGIN FIRST
    if (Session.u_id == 0) {

        JOptionPane.showMessageDialog(this, "Please login first!");

        // Open login form
        LoginForm login = new LoginForm();
        login.setVisible(true);
        login.pack();
        login.setLocationRelativeTo(null);

        // Close this dashboard immediately
        this.dispose();
        return;
    }
    }//GEN-LAST:event_formWindowActivated

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
    private javax.swing.JPanel Jpanel3;
    private javax.swing.JButton UserProfile;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton1ViewUsers;
    private javax.swing.JButton jButton3Reservations;
    private javax.swing.JButton jButton4Packages;
    private javax.swing.JButton jButton5dashboard;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelwel;
    private javax.swing.JLabel jLabelwelcome;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton logout;
    // End of variables declaration//GEN-END:variables
}
