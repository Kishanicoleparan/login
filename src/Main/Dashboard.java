/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import com.toedter.calendar.JDateChooser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;




public class Dashboard extends javax.swing.JFrame {
 // Dynamic dashboard components
    private JLabel lblTotalPackages;
    private JLabel lblActiveReservations;
    private JLabel lblPendingReservations;
    private JTable upcomingTable;
    private JPanel dashboardArea; // only content panel
    private JDateChooser dateFilter;



    public Dashboard() {
        initComponents();
   

    jLabelWelcome.setText("Welcome! " + Session.fullname); // optional label
        setTitle("Customer Dashboard");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize the dashboard content panel
        setupDashboard();
        loadDashboardData();
       
    }
   

      // Setup dashboard in the placeholder panel
    private void setupDashboard() {
        // Create content panel
        dashboardArea = new JPanel(new BorderLayout());
        dashboardArea.setBackground(Color.WHITE);

        // Add dashboardArea into the NetBeans placeholder panel
        jPanelContent.removeAll();   // jPanelContent is a JPanel in your form
        jPanelContent.setLayout(new BorderLayout());
        jPanelContent.add(dashboardArea, BorderLayout.CENTER);
        jPanelContent.revalidate();
        jPanelContent.repaint();

        // ===== Top Stats =====
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblTotalPackages = createStatCard("Total Packages", "0");
        lblActiveReservations = createStatCard("Active Reservations", "0");
        lblPendingReservations = createStatCard("Pending Reservations", "0");

        topPanel.add(lblTotalPackages);
        topPanel.add(lblActiveReservations);
        topPanel.add(lblPendingReservations);

        dashboardArea.add(topPanel, BorderLayout.NORTH);

        // ===== Center Chart =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createLineChart(
                "Reservation Trend",
                "Date",
                "Reservations",
                dataset
        );
        centerPanel.add(new ChartPanel(chart));
        dashboardArea.add(centerPanel, BorderLayout.CENTER);

        // ===== Upcoming Reservations Table =====
        String[] columns = {"Package", "Event Date", "Guests", "Status"};
        upcomingTable = new JTable(new DefaultTableModel(columns, 0));
        JScrollPane scroll = new JScrollPane(upcomingTable);
        scroll.setPreferredSize(new Dimension(760, 120));
        dashboardArea.add(scroll, BorderLayout.SOUTH);
        // ===== Date Filter Panel =====
JPanel filterPanel = new JPanel();
filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

dateFilter = new JDateChooser();
dateFilter.setDateFormatString("yyyy-MM-dd");

JButton btnFilter = new JButton("Filter");

btnFilter.addActionListener(e -> {
    loadDashboardData(); // reload data when clicked
});

filterPanel.add(new JLabel("Filter by Date: "));
filterPanel.add(dateFilter);
filterPanel.add(btnFilter);

dashboardArea.add(filterPanel, BorderLayout.BEFORE_FIRST_LINE);

    }

    // Create a stat card
    private JLabel createStatCard(String title, String value) {
        JLabel label = new JLabel("<html><center>" + title + "<br><h2>" + value + "</h2></center></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(100, 149, 237));
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    // Load dashboard data from database
    private void loadDashboardData() {
        try {
            java.sql.Connection conn = config.config.connectDB();

            // 1️⃣ Total Packages
            String totalPackagesQuery = "SELECT COUNT(*) FROM tbl_packages";
            java.sql.PreparedStatement pst1 = conn.prepareStatement(totalPackagesQuery);
            java.sql.ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                lblTotalPackages.setText("<html><center>Total Packages<br><h2>" + rs1.getInt(1) + "</h2></center></html>");
            }

            // 2️⃣ Active Reservations
            String activeReservationsQuery = "SELECT COUNT(*) FROM tbl_reservations WHERE u_id = ?";
            java.sql.PreparedStatement pst2 = conn.prepareStatement(activeReservationsQuery);
            pst2.setInt(1, Session.u_id); // current logged-in customer
            java.sql.ResultSet rs2 = pst2.executeQuery();
            if (rs2.next()) {
                lblActiveReservations.setText("<html><center>Active Reservations<br><h2>" + rs2.getInt(1) + "</h2></center></html>");
            }

            // 3️⃣ Pending Reservations
            String pendingQuery = "SELECT COUNT(*) FROM tbl_reservations WHERE u_id = ? AND status='PENDING'";
            java.sql.PreparedStatement pst3 = conn.prepareStatement(pendingQuery);
            pst3.setInt(1, Session.u_id);
            java.sql.ResultSet rs3 = pst3.executeQuery();
            if (rs3.next()) {
                lblPendingReservations.setText("<html><center>Pending Reservations<br><h2>" + rs3.getInt(1) + "</h2></center></html>");
            }

            // 4️⃣ Load upcoming reservations table
            DefaultTableModel model = (DefaultTableModel) upcomingTable.getModel();
            model.setRowCount(0); // Clear existing rows

String tableQuery = 
    "SELECT p.package_name, r.event_date, r.num_guests, r.status " +
    "FROM tbl_reservations r " +
    "JOIN tbl_packages p ON r.p_id = p.p_id " +
    "WHERE r.u_id = ? ";

if (dateFilter.getDate() != null) {
    tableQuery += " AND r.event_date = ? ";
}

tableQuery += " ORDER BY r.event_date ASC LIMIT 5";

PreparedStatement pst4 = conn.prepareStatement(tableQuery);
pst4.setInt(1, Session.u_id);

if (dateFilter.getDate() != null) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String selectedDate = sdf.format(dateFilter.getDate());
    pst4.setString(2, selectedDate);
}

ResultSet rs4 = pst4.executeQuery();

while (rs4.next()) {
    model.addRow(new Object[]{
        rs4.getString("package_name"),
        rs4.getString("event_date"),
        rs4.getInt("num_guests"),
        rs4.getString("status")
    });
}


            conn.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading dashboard data: " + e.getMessage());
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

        jPanel2 = new javax.swing.JPanel();
        jpanel = new javax.swing.JPanel();
        Packages = new javax.swing.JButton();
        Reservations = new javax.swing.JButton();
        profile = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        jButtondashboard = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanelContent = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabelWelcome = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpanel.setBackground(new java.awt.Color(0, 0, 0));
        jpanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpanelMouseClicked(evt);
            }
        });
        jpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Packages.setBackground(new java.awt.Color(0, 153, 153));
        Packages.setText("Packages");
        Packages.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PackagesMouseClicked(evt);
            }
        });
        Packages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PackagesActionPerformed(evt);
            }
        });
        jpanel.add(Packages, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 140, -1));

        Reservations.setBackground(new java.awt.Color(0, 153, 153));
        Reservations.setText("Reservations");
        Reservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReservationsActionPerformed(evt);
            }
        });
        jpanel.add(Reservations, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 140, -1));

        profile.setBackground(new java.awt.Color(0, 153, 153));
        profile.setText("Profile");
        profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileActionPerformed(evt);
            }
        });
        jpanel.add(profile, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 140, -1));

        Logout.setBackground(new java.awt.Color(0, 153, 153));
        Logout.setText("Logout");
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });
        jpanel.add(Logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 140, -1));

        jButtondashboard.setBackground(new java.awt.Color(0, 153, 153));
        jButtondashboard.setText("Dashboard");
        jButtondashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtondashboardActionPerformed(evt);
            }
        });
        jpanel.add(jButtondashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 140, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/redefined.png"))); // NOI18N
        jLabel2.setOpaque(true);
        jpanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, -1, 150));

        jPanel2.add(jpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 500));

        jButton1.setBackground(new java.awt.Color(0, 102, 204));
        jButton1.setText("Continue....");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 680, -1, -1));
        jPanel2.add(jPanelContent, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 500, 370));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblWelcome.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jPanel3.add(lblWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 19, -1, -1));

        jLabel6.setBackground(new java.awt.Color(0, 153, 153));
        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Customer Dashboard");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, -1, -1));

        jLabelWelcome.setBackground(new java.awt.Color(255, 255, 255));
        jLabelWelcome.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabelWelcome.setForeground(new java.awt.Color(255, 255, 255));
        jLabelWelcome.setText("jLabel1");
        jPanel3.add(jLabelWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, -1, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 560, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 500));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        RegisterForm rgf = new RegisterForm();
        rgf.setVisible(true);
        rgf.pack();
        rgf.setLocationRelativeTo(null);
        this.dispose();
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
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
    }//GEN-LAST:event_LogoutActionPerformed

    private void profileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileActionPerformed
        UsersProfile pf = new UsersProfile();
        pf.setVisible(true);
        pf.pack();
        pf.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_profileActionPerformed

    private void PackagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PackagesActionPerformed
       ListPackages pkg = new ListPackages();
       pkg.setVisible(true);
       pkg.pack();
        pkg.setLocationRelativeTo(null);
        this.dispose();  
    }//GEN-LAST:event_PackagesActionPerformed

    private void ReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReservationsActionPerformed
        ViewReservationTable rsv = new ViewReservationTable();
        rsv.setVisible(true);
        rsv.pack();
        rsv.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_ReservationsActionPerformed

    private void jpanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpanelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jpanelMouseClicked

    private void PackagesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PackagesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_PackagesMouseClicked

    private void jButtondashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtondashboardActionPerformed
     Dashboard db = new Dashboard();
    db.setVisible(true);
    db.pack();
    db.setLocationRelativeTo(null);
    this.dispose();
    }//GEN-LAST:event_jButtondashboardActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Logout;
    private javax.swing.JButton Packages;
    private javax.swing.JButton Reservations;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtondashboard;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelWelcome;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JPanel jpanel;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JButton profile;
    // End of variables declaration//GEN-END:variables
}
