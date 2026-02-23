/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER35
 */
public class StaffManagement extends javax.swing.JFrame {
     DefaultTableModel staffTableModel;
    DefaultTableModel assignmentTableModel;
    DefaultTableModel availableReservationsModel;
    
    private int selectedStaffId = -1;
    private String selectedStaffName = "";
    private int selectedReservationId = -1;
    public StaffManagement() {
        initComponents();
        setupTableModels();
        loadStaffData();
        loadUnassignedReservations();
        setLocationRelativeTo(null);
    }
  private void setupTableModels() {
        // Staff table columns
        staffTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        staffTableModel.addColumn("ID");
        staffTableModel.addColumn("Name");
        staffTableModel.addColumn("Email");
        staffTableModel.addColumn("Type");
        staffTableModel.addColumn("Status");
        tblstaff.setModel(staffTableModel);

        // Assignment table columns
        assignmentTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        assignmentTableModel.addColumn("Res. ID");
        assignmentTableModel.addColumn("Event Date");
        assignmentTableModel.addColumn("Venue");
        assignmentTableModel.addColumn("Role");
        assignmentTableModel.addColumn("Status");
        tblAssignments.setModel(assignmentTableModel);

        // Available reservations table columns
        availableReservationsModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableReservationsModel.addColumn("ID");
        availableReservationsModel.addColumn("Date");
        availableReservationsModel.addColumn("Time");
        availableReservationsModel.addColumn("Customer");
        availableReservationsModel.addColumn("Guests");
        availableReservationsModel.addColumn("Venue");
        tblavailablereservations.setModel(availableReservationsModel);
    }

    private void loadStaffData() {
        try {
            Connection conn = config.config.connectDB();
            String query = "SELECT u_id, fullname, email, type, " +
                          "is_active FROM tbl_accounts WHERE type = 'STAFF' ORDER BY fullname";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            staffTableModel.setRowCount(0);
            while (rs.next()) {
                staffTableModel.addRow(new Object[]{
                    rs.getInt("u_id"),
                    rs.getString("fullname"),
                    rs.getString("email"),
                    rs.getString("type"),
                    rs.getBoolean("is_active") ? "Active" : "Inactive"
                });
            }
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading staff: " + e.getMessage());
        }
    }

    private void loadUnassignedReservations() {
        try {
            Connection conn = config.config.connectDB();
            
            // Changed: Show APPROVED reservations that need staff assignment
            String query = "SELECT r.r_id, r.event_date, r.event_time, " +
                          "r.customer_name, r.num_guests, r.venue " +
                          "FROM tbl_reservations r " +
                          "WHERE r.status = 'APPROVED' " +  // Changed from PENDING/CONFIRMED
                          "AND r.event_date >= CURRENT_DATE " +
                          "ORDER BY r.event_date";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            availableReservationsModel.setRowCount(0);
            while (rs.next()) {
                availableReservationsModel.addRow(new Object[]{
                    rs.getInt("r_id"),
                    rs.getDate("event_date"),
                    rs.getTime("event_time"),
                    rs.getString("customer_name"),
                    rs.getInt("num_guests"),
                    rs.getString("venue")
                });
            }
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + e.getMessage());
        }
    }

  private void loadStaffAssignments(int staffId) {
    try {
        Connection conn = config.config.connectDB();
        
        // Use alias for r_id
        String query = "SELECT rs.r_id as reservation_staff_id, r.event_date, r.venue, " +
                      "rs.type, r.status " +
                      "FROM tbl_reservation_staff rs " +
                      "JOIN tbl_reservations r ON rs.r_id = r.r_id " +
                      "WHERE rs.u_id = ? " +
                      "ORDER BY r.event_date";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setInt(1, staffId);
        ResultSet rs = pst.executeQuery();

        assignmentTableModel.setRowCount(0);
        while (rs.next()) {
            assignmentTableModel.addRow(new Object[]{
                rs.getInt("reservation_staff_id"),  // Use alias
                rs.getDate("event_date"),
                rs.getString("venue"),
                rs.getString("type"),
                rs.getString("status")
            });
        }
        conn.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading assignments: " + e.getMessage());
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
        jButton5dashboard = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblstaff = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAssignments = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblavailablereservations = new javax.swing.JTable();
        btnAssign = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        lblSelectedReservation = new javax.swing.JLabel();
        cmbType = new javax.swing.JComboBox<>();
        lblSelectedStaff = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_reservations = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
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

        tblstaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblstaff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblstaffMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblstaff);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 240, 200));

        tblAssignments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblAssignments);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 240, 250));

        tblavailablereservations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblavailablereservations.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblavailablereservationsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblavailablereservations);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 20, 270, 310));

        btnAssign.setText("ASSIGN");
        btnAssign.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAssignMouseClicked(evt);
            }
        });
        jPanel1.add(btnAssign, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 50, -1, -1));

        btnRemove.setText("Remove");
        btnRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRemoveMouseClicked(evt);
            }
        });
        jPanel1.add(btnRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, -1, -1));

        btnRefresh.setText("Refresh");
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefreshMouseClicked(evt);
            }
        });
        jPanel1.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 170, -1, -1));

        lblSelectedReservation.setText("jLabel1");
        jPanel1.add(lblSelectedReservation, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 370, 40, -1));

        cmbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(cmbType, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 410, -1, -1));
        jPanel1.add(lblSelectedStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 340, 130, -1));

        table_reservations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(table_reservations);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 370, 280, 200));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
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
        AdminDashboard adm = new AdminDashboard();
        adm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton5dashboardActionPerformed

    private void tblstaffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblstaffMouseClicked
       int row = tblstaff.getSelectedRow();
    if (row != -1) {
        selectedStaffId = Integer.parseInt(staffTableModel.getValueAt(row, 0).toString());
           String selectedStaffName = staffTableModel.getValueAt(row, 1).toString();
        lblSelectedStaff.setText("Selected: " + selectedStaffName);
        
        // Load assignments for this staff
        loadStaffAssignments(selectedStaffId);
    }
    }//GEN-LAST:event_tblstaffMouseClicked

    private void tblavailablereservationsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblavailablereservationsMouseClicked
        int row = tblavailablereservations.getSelectedRow();
    if (row != -1) {
        selectedReservationId = Integer.parseInt(availableReservationsModel.getValueAt(row, 0).toString());
        String customer = availableReservationsModel.getValueAt(row, 3).toString();
        lblSelectedReservation.setText("Selected: Res#" + selectedReservationId + " - " + customer);
    }
    }//GEN-LAST:event_tblavailablereservationsMouseClicked

    private void btnAssignMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAssignMouseClicked
        if (selectedStaffId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a staff member first!");
        return;
    }
    
    if (selectedReservationId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a reservation to assign!");
        return;
    }
    
    try {
        Connection conn = config.config.connectDB();
        
        // Check if already assigned
        String checkQuery = "SELECT * FROM tbl_reservation_staff WHERE r_id = ? AND u_id = ?";
        PreparedStatement checkPst = conn.prepareStatement(checkQuery);
        checkPst.setInt(1, selectedReservationId);
        checkPst.setInt(2, selectedStaffId);
        ResultSet checkRs = checkPst.executeQuery();
        
        if (checkRs.next()) {
            JOptionPane.showMessageDialog(this, "This staff is already assigned!");
            conn.close();
            return;
        }
        
        // Insert assignment
        String insertQuery = "INSERT INTO tbl_reservation_staff (r_id, u_id, type, assigned_by) VALUES (?, ?, ?, ?)";
        PreparedStatement insertPst = conn.prepareStatement(insertQuery);
        insertPst.setInt(1, selectedReservationId);
        insertPst.setInt(2, selectedStaffId);
        insertPst.setString(3, cmbType.getSelectedItem().toString());
        insertPst.setInt(4, Session.u_id);
        insertPst.executeUpdate();
        
        // Update reservation status
        String updateQuery = "UPDATE tbl_reservations SET status = 'ASSIGNED' WHERE r_id = ?";
        PreparedStatement updatePst = conn.prepareStatement(updateQuery);
        updatePst.setInt(1, selectedReservationId);
        updatePst.executeUpdate();
        
        conn.close();
        
        JOptionPane.showMessageDialog(this, "Staff assigned successfully!");
        
        loadStaffAssignments(selectedStaffId);
        loadUnassignedReservations();
        
        selectedReservationId = -1;
        lblSelectedReservation.setText("Select a reservation →");
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btnAssignMouseClicked

    private void btnRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRemoveMouseClicked
       int selectedRow = tblAssignments.getSelectedRow();
    
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Select an assignment to remove!");
        return;
    }
    
    int resId = Integer.parseInt(assignmentTableModel.getValueAt(selectedRow, 0).toString());
    
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Remove this staff from the reservation?", 
        "Confirm", 
        JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            Connection conn = config.config.connectDB();
            
            String deleteQuery = "DELETE FROM tbl_reservation_staff WHERE r_id = ? AND u_id = ?";
            PreparedStatement pst = conn.prepareStatement(deleteQuery);
            pst.setInt(1, resId);
            pst.setInt(2, selectedStaffId);
            pst.executeUpdate();
            
            conn.close();
            
            JOptionPane.showMessageDialog(this, "Removed!");
            loadStaffAssignments(selectedStaffId);
            loadUnassignedReservations();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_btnRemoveMouseClicked

    private void btnRefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefreshMouseClicked
       loadStaffData();
    loadUnassignedReservations();
    if (selectedStaffId != -1) {
        loadStaffAssignments(selectedStaffId);
    }
    }//GEN-LAST:event_btnRefreshMouseClicked

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
            java.util.logging.Logger.getLogger(StaffManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StaffManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StaffManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StaffManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StaffManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton UserProfile;
    private javax.swing.JButton btnAssign;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRemove;
    private javax.swing.JComboBox<String> cmbType;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton1ViewUsers;
    private javax.swing.JButton jButton3Reservations;
    private javax.swing.JButton jButton4Packages;
    private javax.swing.JButton jButton5dashboard;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblSelectedReservation;
    private javax.swing.JLabel lblSelectedStaff;
    private javax.swing.JButton logout;
    private javax.swing.JTable table_reservations;
    private javax.swing.JTable tblAssignments;
    private javax.swing.JTable tblavailablereservations;
    private javax.swing.JTable tblstaff;
    // End of variables declaration//GEN-END:variables
}
