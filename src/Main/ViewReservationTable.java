/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class ViewReservationTable extends javax.swing.JFrame {

   private DefaultTableModel model;
    public ViewReservationTable() {
        initComponents();
        
        
       // ✅ Initialize table model first
    model = new DefaultTableModel(
        new Object[][] {},
        new String[] {
            "Reservation ID", "Package", "Event Type", "Event Date", "Event Time",
            "Guests", "Venue", "Phone", "Downpayment", "Payment Method", "Status"
        }
    );
    tableReservations.setModel(model);

    // ✅ Now safely load reservations
    loadMyReservations();
    }

private void loadMyReservations() {

    if (Session.u_id == 0) {
        JOptionPane.showMessageDialog(this, "Please login first!");
        return;
    }

    model.setRowCount(0); // Clear previous rows safely

    String sql = "SELECT r.r_id, p.package_name, r.event_type, r.event_date, " +
                 "r.event_time, r.num_guests, r.venue, r.phone, r.downpayment, r.payment_method, r.status " +
                 "FROM tbl_reservations r " +
                 "JOIN tbl_packages p ON r.p_id = p.p_id " +
                 "WHERE r.u_id = ?";

    try (Connection conn = config.connectDB();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, Session.u_id);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("r_id"),
                rs.getString("package_name"),
                rs.getString("event_type"),
                rs.getString("event_date"),
                rs.getString("event_time"),
                rs.getInt("num_guests"),
                rs.getString("venue"),
                rs.getString("phone"),
                rs.getInt("downpayment"),
                rs.getString("payment_method"),
                rs.getString("status")
            });
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "You don't have any reservations yet.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading reservations: " + e.getMessage());
    }
}
private void searchReservations(String keyword) {

    model.setRowCount(0);

    String sql = "SELECT r.r_id, p.package_name, r.event_type, r.event_date, " +
                 "r.event_time, r.num_guests, r.venue, r.phone, r.downpayment, r.payment_method, r.status " +
                 "FROM tbl_reservations r " +
                 "JOIN tbl_packages p ON r.p_id = p.p_id " +
                 "WHERE r.u_id = ? AND (p.package_name LIKE ? OR r.event_type LIKE ? OR r.status LIKE ?)";

    try (Connection conn = config.connectDB();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, Session.u_id);
        pst.setString(2, "%" + keyword + "%");
        pst.setString(3, "%" + keyword + "%");
        pst.setString(4, "%" + keyword + "%");

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("r_id"),
                rs.getString("package_name"),
                rs.getString("event_type"),
                rs.getString("event_date"),
                rs.getString("event_time"),
                rs.getInt("num_guests"),
                rs.getString("venue"),
                rs.getString("phone"),
                rs.getInt("downpayment"),
                rs.getString("payment_method"),
                rs.getString("status")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Search Error: " + e.getMessage());
    }
}
private void cancelReservation() {

    int selectedRow = tableReservations.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a reservation to cancel.");
        return;
    }

    int reservationId = (int) model.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to cancel this reservation?",
            "Confirm Cancel",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {

        String sql = "UPDATE tbl_reservations SET status = 'Cancelled' WHERE r_id = ?";

        try (Connection conn = config.connectDB();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, reservationId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Reservation cancelled successfully!");
            loadMyReservations();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cancel Error: " + e.getMessage());
        }
    }
}





    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jpanel = new javax.swing.JPanel();
        Packages = new javax.swing.JButton();
        Reservations = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButtondashboard = new javax.swing.JButton();
        profile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableReservations = new javax.swing.JTable();
        jButtoncancelreservation = new javax.swing.JButton();
        jTextFieldsearchbar = new javax.swing.JTextField();
        jButtonsearch = new javax.swing.JButton();
        jButtonrefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jpanel.setBackground(new java.awt.Color(51, 51, 51));
        jpanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpanelMouseClicked(evt);
            }
        });
        jpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jpanel.add(Packages, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 140, -1));

        Reservations.setText("Reservations");
        Reservations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReservationsActionPerformed(evt);
            }
        });
        jpanel.add(Reservations, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 140, -1));

        Logout.setText("Logout");
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });
        jpanel.add(Logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 140, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/catering (1).png"))); // NOI18N
        jpanel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 130, 120));

        jButtondashboard.setText("Dashboard");
        jButtondashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtondashboardActionPerformed(evt);
            }
        });
        jpanel.add(jButtondashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 140, -1));

        profile.setText("Profile");
        profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileActionPerformed(evt);
            }
        });
        jpanel.add(profile, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 140, -1));

        jPanel1.add(jpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 490));

        tableReservations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tableReservations);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, 520, 370));

        jButtoncancelreservation.setText("Cancel Reservation");
        jButtoncancelreservation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtoncancelreservationActionPerformed(evt);
            }
        });
        jPanel1.add(jButtoncancelreservation, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, -1, -1));

        jTextFieldsearchbar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldsearchbarMouseClicked(evt);
            }
        });
        jPanel1.add(jTextFieldsearchbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 150, -1));

        jButtonsearch.setText("SEARCH");
        jButtonsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonsearchActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonsearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, -1, -1));

        jButtonrefresh.setText("Refresh");
        jButtonrefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonrefreshActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonrefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PackagesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PackagesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_PackagesMouseClicked

    private void PackagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PackagesActionPerformed
        ListPackages pkg = new ListPackages();
        pkg.setVisible(true);
        pkg.pack();
        pkg.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_PackagesActionPerformed

    private void ReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReservationsActionPerformed
       
    }//GEN-LAST:event_ReservationsActionPerformed

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

    private void jButtondashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtondashboardActionPerformed
        Dashboard db = new Dashboard();
        db.setVisible(true);
        db.pack();
        db.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jButtondashboardActionPerformed

    private void jpanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpanelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jpanelMouseClicked

    private void profileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileActionPerformed
        UsersProfile pf = new UsersProfile();
        pf.setVisible(true);
        pf.pack();
        pf.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_profileActionPerformed

    private void jButtonsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonsearchActionPerformed
         
        String keyword = jTextFieldsearchbar.getText().trim();
        searchReservations(keyword);
    
    }//GEN-LAST:event_jButtonsearchActionPerformed

    private void jTextFieldsearchbarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldsearchbarMouseClicked
         String keyword = jTextFieldsearchbar.getText().trim();
        searchReservations(keyword);
    
    }//GEN-LAST:event_jTextFieldsearchbarMouseClicked

    private void jButtonrefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonrefreshActionPerformed
       jTextFieldsearchbar.setText("");
        loadMyReservations();
    
    }//GEN-LAST:event_jButtonrefreshActionPerformed

    private void jButtoncancelreservationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtoncancelreservationActionPerformed
       cancelReservation();
    }//GEN-LAST:event_jButtoncancelreservationActionPerformed

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
            java.util.logging.Logger.getLogger(ViewReservationTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewReservationTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewReservationTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewReservationTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewReservationTable().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Logout;
    private javax.swing.JButton Packages;
    private javax.swing.JButton Reservations;
    private javax.swing.JButton jButtoncancelreservation;
    private javax.swing.JButton jButtondashboard;
    private javax.swing.JButton jButtonrefresh;
    private javax.swing.JButton jButtonsearch;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldsearchbar;
    private javax.swing.JPanel jpanel;
    private javax.swing.JButton profile;
    private javax.swing.JTable tableReservations;
    // End of variables declaration//GEN-END:variables
}
