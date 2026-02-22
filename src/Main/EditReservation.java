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
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Admin
 */
public class EditReservation extends javax.swing.JDialog {

      private int reservationId;
    private Runnable onUpdate; // Callback to refresh table
    private String status;

    public EditReservation(java.awt.Frame parent, boolean modal, int reservationId, Runnable onUpdate) {
        super(parent, modal);
         this.reservationId = reservationId;
           this.onUpdate = onUpdate;

        initComponents();
       

         // Setup time spinner correctly
    jSpinnertime.setModel(new SpinnerDateModel());
    JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(jSpinnertime, "hh:mm a"); // AM/PM
    jSpinnertime.setEditor(timeEditor);

    // Set default value to current time (optional)
    jSpinnertime.setValue(new Date());

    // Load reservation AFTER spinner is properly configured
    loadReservationData();

    // Buttons
    jButtoncancel.addActionListener(e -> dispose());
    jButtonsave.addActionListener(e -> saveReservation());
    }

 private void loadReservationData() {
        String sql = "SELECT event_date, event_time, num_guests, venue, phone, status FROM tbl_reservations WHERE r_id = ?";
        try (Connection conn = config.connectDB();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, reservationId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // --- Load Date ---
                java.sql.Date sqlDate = rs.getDate("event_date");
                if (sqlDate != null) jDateChooserdate.setDate(new Date(sqlDate.getTime()));

                // --- Load Time safely ---
                java.util.Date timeValue = null;

                try {
                    // Try as TIME column
                    Time sqlTime = rs.getTime("event_time");
                    if (sqlTime != null) timeValue = new Date(sqlTime.getTime());
                } catch (Exception ignored) {}

                if (timeValue == null) {
                    try {
                        // Fallback: DATETIME/TIMESTAMP
                        Timestamp ts = rs.getTimestamp("event_time");
                        if (ts != null) timeValue = new Date(ts.getTime());
                    } catch (Exception ignored) {}
                }

                if (timeValue != null) {
                    Calendar calDate = Calendar.getInstance();
                    if (sqlDate != null) calDate.setTime(sqlDate);

                    Calendar calTime = Calendar.getInstance();
                    calTime.setTime(timeValue);

                    calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
                    calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
                    calDate.set(Calendar.SECOND, 0);
                    calDate.set(Calendar.MILLISECOND, 0);

                    jSpinnertime.setValue(calDate.getTime());
                }

                // --- Other fields ---
                jSpinnerguests.setValue(rs.getInt("num_guests"));
                jTextFieldvenue.setText(rs.getString("venue"));
                jTextFieldphone.setText(rs.getString("phone"));

                // --- Disable editing if APPROVED ---
                status = rs.getString("status");
                if ("APPROVED".equalsIgnoreCase(status)) {
                    jDateChooserdate.setEnabled(false);
                    jSpinnertime.setEnabled(false);
                    jSpinnerguests.setEnabled(false);
                    jTextFieldvenue.setEnabled(false);
                    jTextFieldphone.setEnabled(false);
                    jButtonsave.setEnabled(false);
                    JOptionPane.showMessageDialog(this, "Reservation is APPROVED and cannot be edited.");
                }

            } else {
                JOptionPane.showMessageDialog(this, "Reservation not found.");
                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading reservation: " + e.getMessage());
            dispose();
        }
    }

    private void saveReservation() {
        try {
            // --- Validate date ---
            Date selectedDate = jDateChooserdate.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a date.");
                return;
            }

            // --- Get time from spinner ---
            Date spinnerTime = (Date) jSpinnertime.getValue();
            Calendar calTime = Calendar.getInstance();
            calTime.setTime(spinnerTime);

            // --- Combine date + time ---
            Calendar combined = Calendar.getInstance();
            combined.setTime(selectedDate);
            combined.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
            combined.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
            combined.set(Calendar.SECOND, 0);
            combined.set(Calendar.MILLISECOND, 0);

            java.sql.Date sqlDate = new java.sql.Date(combined.getTimeInMillis());
            java.sql.Time sqlTime = new java.sql.Time(combined.getTimeInMillis());
            // If your DB column is DATETIME/TIMESTAMP, use:
            // java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(combined.getTimeInMillis());

            int guests = (int) jSpinnerguests.getValue();
            String venue = jTextFieldvenue.getText().trim();
            String phone = jTextFieldphone.getText().trim();

            String sql = "UPDATE tbl_reservations SET event_date=?, event_time=?, venue=?, num_guests=?, phone=? WHERE r_id=?";
            try (Connection conn = config.connectDB();
                 PreparedStatement pst = conn.prepareStatement(sql)) {

                pst.setDate(1, sqlDate);
                pst.setTime(2, sqlTime); // Use pst.setTimestamp(2, sqlTimestamp); if column is DATETIME/TIMESTAMP
                pst.setString(3, venue);
                pst.setInt(4, guests);
                pst.setString(5, phone);
                pst.setInt(6, reservationId);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Reservation updated successfully!");
                if (onUpdate != null) onUpdate.run();
                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating reservation: " + e.getMessage());
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButtoncancel = new javax.swing.JButton();
        jTextFieldphone = new javax.swing.JTextField();
        jDateChooserdate = new com.toedter.calendar.JDateChooser();
        jSpinnerguests = new javax.swing.JSpinner();
        jSpinnertime = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldvenue = new javax.swing.JTextField();
        jButtonsave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Event Date:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Number of Guests:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, -1, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Event Time:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Phone Number:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setText("Edit Reservation");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, 30));

        jButtoncancel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtoncancel.setText("Cancel");
        jPanel1.add(jButtoncancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 440, 160, -1));
        jPanel1.add(jTextFieldphone, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 350, 160, -1));

        jDateChooserdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateChooserdateMouseClicked(evt);
            }
        });
        jPanel1.add(jDateChooserdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 160, -1));
        jPanel1.add(jSpinnerguests, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 160, -1));

        jSpinnertime.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.AM_PM));
        jPanel1.add(jSpinnertime, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, 160, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Venue:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, -1, -1));
        jPanel1.add(jTextFieldvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, 160, -1));

        jButtonsave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButtonsave.setText("Save");
        jButtonsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonsaveActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonsave, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 400, 160, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonsaveActionPerformed
      try (Connection conn = config.connectDB();
         PreparedStatement pst = conn.prepareStatement(
                 "UPDATE tbl_reservations SET event_date=?, event_time=?, venue=?, num_guests=?, phone=? WHERE r_id=?")) {

        java.util.Date selectedDate = jDateChooserdate.getDate();
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

        java.util.Date selectedTime = (java.util.Date) jSpinnertime.getValue();
        java.sql.Time sqlTime = new java.sql.Time(selectedTime.getTime());

        pst.setDate(1, sqlDate);
        pst.setTime(2, sqlTime);
        pst.setString(3, jTextFieldvenue.getText());
        pst.setInt(4, (int) jSpinnerguests.getValue());
        pst.setString(5, jTextFieldphone.getText());
        pst.setInt(6, reservationId);

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Reservation updated successfully!");
        this.dispose();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error updating reservation: " + e.getMessage());
    }
    }//GEN-LAST:event_jButtonsaveActionPerformed

    private void jDateChooserdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jDateChooserdateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooserdateMouseClicked

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
            java.util.logging.Logger.getLogger(EditReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Replace 1 with a valid reservation ID in your database
            int testReservationId = 1;

                EditReservation dialog = new EditReservation(new javax.swing.JFrame(), true, testReservationId, () -> {});
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                 dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtoncancel;
    private javax.swing.JButton jButtonsave;
    private com.toedter.calendar.JDateChooser jDateChooserdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner jSpinnerguests;
    private javax.swing.JSpinner jSpinnertime;
    private javax.swing.JTextField jTextFieldphone;
    private javax.swing.JTextField jTextFieldvenue;
    // End of variables declaration//GEN-END:variables
}
