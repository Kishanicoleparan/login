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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class ReservationsTable extends javax.swing.JFrame {

    /**
     * Creates new form ReservationsTable
     */
    public ReservationsTable() {
        initComponents();
        table_reservations.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        displayReservations();
        jButtonapproved.addActionListener(e -> updateReservationStatus("APPROVED"));
        jButtonreject.addActionListener(e -> updateReservationStatus("REJECTED"));
        jButtondelete.addActionListener(e -> deleteReservation());
        jButtonsearch.addActionListener(e -> searchReservation());
        jTextFieldsearchbar.setText("");
        jButtonrefresh.addActionListener(e -> refreshTable()); {
        

   
}




    }
   

    void displayReservations() {
    config conf = new config();

   String sql = "SELECT r_id, u_id, event_type, event_date, event_time, " + "venue, num_guests, status, special_request, phone, downpayment, payment_method " + "FROM tbl_reservations";

    conf.displayUser(sql, table_reservations);
}

     private void updateReservationStatus(String status) {
    int row = table_reservations.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a reservation first!");
        return;
    }

    int reservationId = Integer.parseInt(
            table_reservations.getValueAt(row, 0).toString()
    );

    try (Connection conn = config.connectDB();
         PreparedStatement pst = conn.prepareStatement(
                 "UPDATE tbl_reservations SET status=? WHERE r_id=?")) {

        pst.setString(1, status);
        pst.setInt(2, reservationId);
        pst.executeUpdate();

        JOptionPane.showMessageDialog(this, "Reservation " + status + " successfully!");

        displayReservations(); // 🔥 AUTO REFRESH

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}
     private void deleteReservation() {
    int row = table_reservations.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select reservation to delete!");
        return;
    }

    int reservationId = Integer.parseInt(
            table_reservations.getValueAt(row, 0).toString()
    );

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this reservation?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) return;

    try (Connection conn = config.connectDB();
         PreparedStatement pst = conn.prepareStatement(
                 "DELETE FROM tbl_reservations WHERE r_id=?")) {

        pst.setInt(1, reservationId);
        pst.executeUpdate();

        JOptionPane.showMessageDialog(this, "Reservation deleted!");

        displayReservations(); // 🔥 AUTO REFRESH

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}
     private void searchReservation() {
    String keyword = jTextFieldsearchbar.getText().trim();

    if (keyword.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Enter search keyword!");
        return;
    }

    String sql = "SELECT r_id, u_id, event_type, event_date, event_time, " +
                 "venue, num_guests, status, special_request, phone, downpayment, payment_method " +
                 "FROM tbl_reservations " +
                 "WHERE venue LIKE ? OR status LIKE ? OR phone LIKE ? OR payment_method LIKE ?";

    try (Connection conn = config.connectDB();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        String searchValue = "%" + keyword + "%";

        pst.setString(1, searchValue);
        pst.setString(2, searchValue);
        pst.setString(3, searchValue);
        pst.setString(4, searchValue);

        ResultSet rs = pst.executeQuery();

        // Assuming you are using DefaultTableModel for table_reservations
        DefaultTableModel model = (DefaultTableModel) table_reservations.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("r_id"),
                rs.getInt("u_id"),
                rs.getString("event_type"),
                rs.getString("event_date"),
                rs.getString("event_time"),
                rs.getString("venue"),
                rs.getInt("num_guests"),
                rs.getString("status"),
                rs.getString("special_request"),
                rs.getString("phone"),
                rs.getDouble("downpayment"),
                rs.getString("payment_method")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

   
  
    private void refreshTable() {
    jTextFieldsearchbar.setText("");
    displayReservations();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        table_reservations = new javax.swing.JTable();
        jButtonreject = new javax.swing.JButton();
        jButtonapproved = new javax.swing.JButton();
        jButtondelete = new javax.swing.JButton();
        jTextFieldsearchbar = new javax.swing.JTextField();
        jButtonsearch = new javax.swing.JButton();
        jButtonrefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
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
        jPanel2.add(jButton1ViewUsers, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 140, -1));

        UserProfile.setBackground(new java.awt.Color(255, 255, 255));
        UserProfile.setText("Profile");
        UserProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserProfileActionPerformed(evt);
            }
        });
        jPanel2.add(UserProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, 140, -1));

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
        jPanel2.add(jButton3Reservations, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 140, -1));

        jButton4Packages.setText("Packages");
        jButton4Packages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4PackagesActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4Packages, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 140, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/catering (1).png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 130, 120));

        jButton5dashboard.setText("Dashboard");
        jButton5dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5dashboardActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 140, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 540));

        table_reservations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table_reservations);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 570, -1));

        jButtonreject.setText("Reject Reservation");
        jPanel1.add(jButtonreject, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, -1, -1));

        jButtonapproved.setText("Approved Reservation");
        jButtonapproved.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonapprovedMouseClicked(evt);
            }
        });
        jButtonapproved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonapprovedActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonapproved, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 80, -1, -1));

        jButtondelete.setText("Delete Reservation");
        jPanel1.add(jButtondelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, 130, -1));

        jTextFieldsearchbar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldsearchbarActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldsearchbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, 140, -1));

        jButtonsearch.setText("Search");
        jButtonsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonsearchActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonsearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, -1, -1));

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
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ViewUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ViewUsersActionPerformed
        UsersTable utbl = new UsersTable();
        utbl.setVisible(true);
        utbl.pack();
        utbl.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jButton1ViewUsersActionPerformed

    private void UserProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserProfileActionPerformed
        UsersProfile2 users = new UsersProfile2();
        users.setVisible(true);
        users.pack();
        users.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_UserProfileActionPerformed

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

    private void jButton3ReservationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ReservationsActionPerformed
        ReservationsTable rf = new ReservationsTable();
        rf.setVisible(true);
        rf.pack();
        rf.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jButton3ReservationsActionPerformed

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

    private void jButtonsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonsearchActionPerformed
      searchReservation();
    }//GEN-LAST:event_jButtonsearchActionPerformed

    private void jButtonrefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonrefreshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonrefreshActionPerformed

    private void jButtonapprovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonapprovedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonapprovedActionPerformed

    private void jButtonapprovedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonapprovedMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonapprovedMouseClicked

    private void jTextFieldsearchbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldsearchbarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldsearchbarActionPerformed

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
            java.util.logging.Logger.getLogger(ReservationsTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReservationsTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReservationsTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReservationsTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReservationsTable().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton UserProfile;
    private javax.swing.JButton jButton1ViewUsers;
    private javax.swing.JButton jButton3Reservations;
    private javax.swing.JButton jButton4Packages;
    private javax.swing.JButton jButton5dashboard;
    private javax.swing.JButton jButtonapproved;
    private javax.swing.JButton jButtondelete;
    private javax.swing.JButton jButtonrefresh;
    private javax.swing.JButton jButtonreject;
    private javax.swing.JButton jButtonsearch;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldsearchbar;
    private javax.swing.JButton logout;
    private javax.swing.JTable table_reservations;
    // End of variables declaration//GEN-END:variables
}
