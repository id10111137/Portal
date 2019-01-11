/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Hellper.C_Connection;
import Hellper.C_FTP_Uploader;
import Hellper.FTPUtils;
import Hellper.Hellper_Portal;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author PCIT1
 */
public class JFrame_ManageContent extends javax.swing.JFrame {

    Connection connection;
    Statement stt;
    String sql;
    ResultSet res;
    PreparedStatement ps = null;
    public static int statusSearching = 0;
    public String NameFile = null;
    Hellper_Portal hellper_Portal;
    FTPClient ftpClient;
    public String nameTujuan = "/bizportal/public/files/policyandprocedureServices/";
    public String _Username;

    public String Doc_Id;
    public String Created_by;
    public String Uptdated_by;

    /**
     * Creates new form JFrame_ManageContent
     */
    public JFrame_ManageContent() {
        initComponents();
        this.hellper_Portal = new Hellper_Portal();
        this.ftpClient = new FTPClient();
        LoadData();
        LoadComboBox();
    }

    public JFrame_ManageContent(String Username) {
        initComponents();
        this.hellper_Portal = new Hellper_Portal();
        this.ftpClient = new FTPClient();
        this._Username = Username;
        LoadData();
        LoadComboBox();
        this.jTabbedPane1.remove(0);

        /*
            Delete
         */
//         try {
//            if (Doc_Id.isEmpty()) {
//                JOptionPane.showMessageDialog(null, "Document Tidak Dipilih");
//            } else {
//
//                int dialogButton = JOptionPane.YES_NO_OPTION;
//                int dialogResult = JOptionPane.showConfirmDialog(this, "Anda Akan Menghapus File Ini ?." + Doc_Id + ".pdf", "Hapus File", dialogButton);
//                if (dialogResult == 0) {
//
//                    FTPUtils.ftpConnect(ftpClient, "192.168.12.12", "uploads", "P4ssw0rd!");
//                    FTPUtils.deleteFile(ftpClient, Doc_Id + ".pdf", nameTujuan);
//                    System.out.println(nameTujuan);
//                    FTPUtils.ftpDisConnect(ftpClient);
//
//                    sql = "DELETE FROM policyandproceduremenu WHERE doc_id = '" + Doc_Id + "'";
//                    connection = C_Connection.getConnection();
//                    stt = connection.createStatement();
//                    res = stt.executeQuery(sql);
//
//                    if (res.next()) {
//
//                        sql = "DELETE FROM policyandprocedureservices WHERE id = '" + Doc_Id + "'";
//                        connection = C_Connection.getConnection();
//                        stt = connection.createStatement();
//                        res = stt.executeQuery(sql);
//                        if (res.next()) {
//                            JOptionPane.showMessageDialog(null, "Document " + Doc_Id + " Sukses Di Hapus ");
//                            LoadData();
//                        }
//                    }
//
//                } else {
//
//                    LoadData();
//                }
//            }
//        } catch (SQLException | HeadlessException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        LoadData();
    }

    private void LoadData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Doc Nomor");
        model.addColumn("Title");
        model.addColumn("Label");
        model.addColumn("Doc Type");
        model.addColumn("Sorting");
        model.addColumn("Description");
        model.addColumn("created at");
        model.addColumn("updated at");

        try {
            sql = "SELECT \n"
                    + "A.doc_id, B.title, A.label, B.is_pdf, A.sort, B.descriptions, B.created_at, B.updated_at \n"
                    + "FROM \n"
                    + "policyandproceduremenu A,\n"
                    + "policyandprocedureservices B\n"
                    + "WHERE \n"
                    + "B.id = A.doc_id order by A.doc_id desc";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[]{res.getString(1), res.getString(2), res.getString(3), hellper_Portal.checkDoc(res.getString(4)), res.getString(5), res.getString(6), res.getString(7), res.getString(8)});
            }
            res.close();
            stt.close();
            jTable1.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void LoadComboBox() {
        try {
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            sql = "select label from policyandproceduremenu order by label asc";
            res = stt.executeQuery(sql);

            while (res.next()) {
                Object[] ob = new Object[3];
                ob[0] = res.getString(1);
                JCM_Parent.addItem(ob[0].toString());                                      // fungsi ini bertugas menampung isi dari database
                JC_Parents.addItem(ob[0].toString());
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        JCM_Parent = new javax.swing.JComboBox<>();
        edt_title = new javax.swing.JTextField();
        edt_file = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        edt_label = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        edt_sortings = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        JC_StatusD = new javax.swing.JComboBox<>();
        edt_cari = new javax.swing.JTextField();
        cari = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        JC_Parents = new javax.swing.JComboBox<>();
        edt_labels = new javax.swing.JTextField();
        edt_titles = new javax.swing.JTextField();
        edt_sorting = new javax.swing.JTextField();
        btn_save = new javax.swing.JButton();
        btn_updates = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        JT_Description = new javax.swing.JTextArea();
        JC_Status = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1005, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 637, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CREATE MENU", jPanel2);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Compute"));

        JCM_Parent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JCM_ParentActionPerformed(evt);
            }
        });

        edt_title.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_titleActionPerformed(evt);
            }
        });

        edt_file.setEditable(false);
        edt_file.setToolTipText("pilih file...");
        edt_file.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                edt_fileMouseClicked(evt);
            }
        });
        edt_file.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_fileActionPerformed(evt);
            }
        });

        jLabel4.setText("Upload");

        jLabel3.setText("Title");

        jLabel2.setText("Parent");

        btn_simpan.setText("SIMPAN");
        btn_simpan.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btn_simpanAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_update.setText("UPDATE");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });

        jLabel1.setText("Label");

        jLabel5.setText("Sorting");

        edt_sortings.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                edt_sortingsKeyTyped(evt);
            }
        });

        jLabel6.setText("Status");

        JC_StatusD.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Publish", "Draft" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edt_file)
                    .addComponent(JCM_Parent, 0, 149, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edt_label, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                    .addComponent(edt_title))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edt_sortings)
                    .addComponent(JC_StatusD, 0, 220, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_update, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edt_title)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JCM_Parent, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(jLabel5)
                        .addComponent(edt_sortings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edt_file, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(edt_label)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addGap(4, 4, 4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(JC_StatusD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(btn_simpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_update)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cari.setText("Cari");
        cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Create Menu"));

        jLabel10.setText("Parent");

        jLabel11.setText("Label");

        jLabel12.setText("Title");

        jLabel13.setText("Description");

        jLabel14.setText("Sorting No");

        jLabel15.setText("Status");

        JC_Parents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JC_ParentsActionPerformed(evt);
            }
        });

        edt_sorting.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                edt_sortingKeyTyped(evt);
            }
        });

        btn_save.setText("Simpan");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });

        btn_updates.setText("Update");
        btn_updates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updatesActionPerformed(evt);
            }
        });

        JT_Description.setColumns(20);
        JT_Description.setRows(5);
        jScrollPane2.setViewportView(JT_Description);

        JC_Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Publish", "Draft" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(edt_labels, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edt_titles, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JC_Parents, 0, 152, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(edt_sorting, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JC_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_updates, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(btn_save, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(edt_sorting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_save))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(btn_updates)
                            .addComponent(JC_Status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(JC_Parents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(edt_labels, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(edt_titles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(edt_cari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cari, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(edt_cari)
                    .addComponent(cari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("PDF FILE PDF", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariActionPerformed
        // TODO add your handling code here:

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Doc Nomor");
        model.addColumn("Title");
        model.addColumn("Label");
        model.addColumn("Doc Type");
        model.addColumn("Sorting");
        model.addColumn("created at");
        model.addColumn("updated at");

        try {

            sql = "SELECT A.doc_id, B.title, A.label, B.is_pdf, A.sort, B.descriptions, B.created_at, B.updated_at FROM policyandproceduremenu A, policyandprocedureservices B WHERE B.id = A.doc_id AND A.doc_id LIKE '%" + edt_cari.getText() + "%' OR A.label LIKE '%" + edt_cari.getText() + "%'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[]{res.getString(1), res.getString(2), res.getString(3), hellper_Portal.checkDoc(res.getString(4)), res.getString(5), res.getString(6), res.getString(7)});
            }
            res.close();
            stt.close();
            jTable1.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_cariActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed

        try {
            String Parents = hellper_Portal.getIDParent(JCM_Parent.getSelectedItem());
            String _Status = hellper_Portal.checkStatus(JC_StatusD.getSelectedItem());
            FTPUtils.ftpConnect(ftpClient, "192.168.12.12", "uploads", "P4ssw0rd!");
            FTPUtils.deleteFile(ftpClient, Doc_Id + ".pdf", nameTujuan);
            FTPUtils.uploadFile(ftpClient, edt_file.getText(), Doc_Id + ".pdf", nameTujuan);
            System.out.println(nameTujuan);

            FTPUtils.ftpDisConnect(ftpClient);
            sql = "UPDATE policyandproceduremenu "
                    + "SET parent = '" + Parents + "', "
                    + "doc_id = '" + Doc_Id + "', "
                    + "url = '/group/policyandprocedureservices/viewer/id/" + Doc_Id + "', "
                    + "label = '" + JCM_Parent.getSelectedItem() + "', "
                    + "stat = '" + _Status + "', "
                    + "sort = '" + edt_sortings.getText() + "' "
                    + "WHERE doc_id = '" + Doc_Id + "'";

            stt = connection.createStatement();
            int policyandproceduremenu = stt.executeUpdate(sql);
            if (policyandproceduremenu >= 1) {

                sql = "UPDATE policyandprocedureservices "
                        + "SET title = '" + edt_title.getText() + "', "
                        + "descriptions = '" + Doc_Id + ".pdf' , "
                        + "is_pdf = 1, "
                        + "flag =1, menu_id = 1, "
                        + "viewer_count = NULL, viewer_last= NULL, "
                        + "remark=NULL,created_by=400,"
                        + "updated_by=400,created_at='" + Created_by + "',"
                        + "updated_at= CONVERT(VARCHAR(19), GETDATE(), 120) "
                        + "where id ='" + Doc_Id + "'";

                stt = connection.createStatement();
                int policyandprocedureservices = stt.executeUpdate(sql);
                if (policyandprocedureservices >= 1) {
                    JOptionPane.showMessageDialog(null, "Sukses Update policyandproceduremenu & policyandprocedureservices");
                    LoadData();
                    refreshAll();
                    JCM_Parent.removeAllItems();
                    LoadComboBox();
                } else {
                    JOptionPane.showMessageDialog(null, "Gagal Update policyandproceduremenu");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Gagal Update policyandprocedureservices");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed

        try {
            String Parents = hellper_Portal.getIDParent(JCM_Parent.getSelectedItem());
            String _DocID = hellper_Portal.getIDMENU();
            String _Status = hellper_Portal.checkStatus(JC_StatusD.getSelectedItem());
            System.out.println(Parents);
            sql = "INSERT INTO [dbo].[policyandproceduremenu]\n"
                    + "           ([id]\n"
                    + "           ,[parent]\n"
                    + "           ,[doc_id]\n"
                    + "           ,[url]\n"
                    + "           ,[label]\n"
                    + "           ,[stat]\n"
                    + "           ,[sort])\n"
                    + "     VALUES\n"
                    + "           ('" + _DocID + "','" + Parents + "','" + _DocID + "','/group/policyandprocedureservices/viewer/id/" + _DocID + "','" + edt_label.getText() + "','" + _Status + "','" + edt_sortings.getText() + "')";

            System.out.println(sql);
            connection = C_Connection.getConnection();
            ps = connection.prepareStatement(sql);
            ps.execute();

            sql = "INSERT INTO [dbo].[policyandprocedureservices]\n"
                    + "           ([id]\n"
                    + "           ,[title]\n"
                    + "           ,[descriptions]\n"
                    + "           ,[is_pdf]\n"
                    + "           ,[flag]\n"
                    + "           ,[is_active]\n"
                    + "           ,[menu_id]\n"
                    + "           ,[viewer_count]\n"
                    + "           ,[viewer_last]\n"
                    + "           ,[remark]\n"
                    + "           ,[created_by]\n"
                    + "           ,[updated_by]\n"
                    + "           ,[created_at]\n"
                    + "           ,[updated_at])\n"
                    + "     VALUES\n"
                    + "           ('" + _DocID + "','" + edt_title.getText() + "','" + _DocID + ".pdf',1,1,1,NULL,NULL,NULL,NULL,400,400,CONVERT(VARCHAR(19), GETDATE(), 120),CONVERT(VARCHAR(19), GETDATE(), 120))";

            connection = C_Connection.getConnection();
            ps = connection.prepareStatement(sql);
            ps.execute();

            FTPUtils.ftpConnect(ftpClient, "192.168.12.12", "uploads", "P4ssw0rd!");
            FTPUtils.uploadFile(ftpClient, edt_file.getText(), _DocID + ".pdf", nameTujuan);
            System.out.println(nameTujuan);
            FTPUtils.ftpDisConnect(ftpClient);

            JOptionPane.showMessageDialog(null, "Upload Dokument Sukses !!!");
            LoadData();
            refreshAll();
            JCM_Parent.removeAllItems();
            LoadComboBox();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void edt_fileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_fileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_fileActionPerformed

    private void edt_fileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_edt_fileMouseClicked
        // TODO add your handling code here:
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.showOpenDialog(null);
        File file = jFileChooser.getSelectedFile();
        String dir = file.getAbsolutePath();
        NameFile = file.getName();
        edt_file.setText(dir);
    }//GEN-LAST:event_edt_fileMouseClicked

    private void edt_titleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_titleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_titleActionPerformed

    private void JCM_ParentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JCM_ParentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JCM_ParentActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int baris = jTable1.rowAtPoint(evt.getPoint());

        if (jTable1.getValueAt(baris, 3).toString().equals("PDF")) {
            String label = jTable1.getValueAt(baris, 2).toString();
            String title = jTable1.getValueAt(baris, 1).toString();
            String doc_id = jTable1.getValueAt(baris, 0).toString();
            edt_title.setText(title);
            edt_file.setText(doc_id);
            edt_label.setText(label);
            Doc_Id = doc_id;
            Created_by = jTable1.getValueAt(baris, 6).toString();
            Uptdated_by = jTable1.getValueAt(baris, 7).toString();
            edt_sortings.setText(jTable1.getValueAt(baris, 4).toString());
            refreshHeader();
        } else {
            Doc_Id = jTable1.getValueAt(baris, 0).toString();
            Created_by = jTable1.getValueAt(baris, 6).toString();
            Uptdated_by = jTable1.getValueAt(baris, 7).toString();
            edt_labels.setText(jTable1.getValueAt(baris, 2).toString());
            edt_titles.setText(jTable1.getValueAt(baris, 1).toString());
            JT_Description.setText(jTable1.getValueAt(baris, 5).toString());
            edt_sorting.setText(jTable1.getValueAt(baris, 4).toString());
            refreshDetail();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed

        if (edt_labels.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon Maaf Label Belum Di Isi !!!");
        } else if (edt_titles.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon Maaf Titles Belum Di Isi !!!");
        } else if (edt_sorting.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon Maaf Sorting Belum Di Isi !!!");
        } else {
            try {
                String Parents = hellper_Portal.getIDParent(JC_Parents.getSelectedItem());
                String id_menu = hellper_Portal.getMenuHeader();
                String _DocID = hellper_Portal.getIDMENU();
                String _Status = hellper_Portal.checkStatus(JC_StatusD.getSelectedItem());
                sql = "INSERT INTO [dbo].[policyandproceduremenu]\n"
                        + "           ([id]\n"
                        + "           ,[parent]\n"
                        + "           ,[doc_id]\n"
                        + "           ,[url]\n"
                        + "           ,[label]\n"
                        + "           ,[stat]\n"
                        + "           ,[sort])\n"
                        + "     VALUES\n"
                        + "           ('" + id_menu + "','" + Parents + "','" + _DocID + "','/group/policyandprocedureservices/view/id/" + _DocID + "','" + edt_labels.getText() + "','" + edt_sorting.getText() + "','" + _Status + "')";

                System.out.println(sql);
                connection = C_Connection.getConnection();
                ps = connection.prepareStatement(sql);
                ps.execute();

                sql = "INSERT INTO [dbo].[policyandprocedureservices]\n"
                        + "           ([id]\n"
                        + "           ,[title]\n"
                        + "           ,[descriptions]\n"
                        + "           ,[is_pdf]\n"
                        + "           ,[flag]\n"
                        + "           ,[is_active]\n"
                        + "           ,[menu_id]\n"
                        + "           ,[viewer_count]\n"
                        + "           ,[viewer_last]\n"
                        + "           ,[remark]\n"
                        + "           ,[created_by]\n"
                        + "           ,[updated_by]\n"
                        + "           ,[created_at]\n"
                        + "           ,[updated_at])\n"
                        + "     VALUES\n"
                        + "           ('" + _DocID + "','" + edt_titles.getText() + "','<p>" + JT_Description.getText() + "</p>',0,1,1,'" + id_menu + "',NULL,NULL,NULL,400,400,CONVERT(VARCHAR(19), GETDATE(), 120),CONVERT(VARCHAR(19), GETDATE(), 120))";

                connection = C_Connection.getConnection();
                ps = connection.prepareStatement(sql);
                ps.execute();

                JOptionPane.showMessageDialog(null, "Menambahkan Menu Dokument Sukses !!!");
                LoadData();
                refreshAll();
                JC_Parents.removeAllItems();
                JCM_Parent.removeAllItems();
                LoadComboBox();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btn_saveActionPerformed

    private void btn_updatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updatesActionPerformed

        if (edt_labels.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon Maaf Label Belum Di Isi !!!");
        } else if (edt_titles.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon Maaf Titles Belum Di Isi !!!");
        } else if (edt_sortings.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mohon Maaf Sorting Belum Di Isi !!!");
        } else {
            try {
                String Parents = hellper_Portal.getIDParent(JC_Parents.getSelectedItem());
                String _Status = hellper_Portal.checkStatus(JC_Status.getSelectedItem());
                sql = "UPDATE policyandproceduremenu "
                        + "SET parent = '" + Parents + "', "
                        + "doc_id = '" + Doc_Id + "', "
                        + "url = '/group/policyandprocedureservices/view/id/" + Doc_Id + "', "
                        + "label = '" + edt_labels.getText() + "', "
                        + "stat = '" + _Status + "', "
                        + "sort = '" + edt_sorting.getText() + "' "
                        + "WHERE doc_id = '" + Doc_Id + "'";

                stt = connection.createStatement();
                int policyandproceduremenu = stt.executeUpdate(sql);
                if (policyandproceduremenu >= 1) {

                    sql = "UPDATE policyandprocedureservices "
                            + "SET title = '" + edt_titles.getText() + "', "
                            + "descriptions = '<p>" + JT_Description.getText() + "</p>' , "
                            + "is_pdf = 0, "
                            + "flag =1, menu_id = 1, "
                            + "viewer_count = NULL, viewer_last= NULL, "
                            + "remark=NULL,created_by=400,"
                            + "updated_by=400,created_at='" + Created_by + "',"
                            + "updated_at= CONVERT(VARCHAR(19), GETDATE(), 120) "
                            + "where id ='" + Doc_Id + "'";
                    stt = connection.createStatement();
                    int policyandprocedureservices = stt.executeUpdate(sql);
                    if (policyandprocedureservices >= 1) {
                        JOptionPane.showMessageDialog(null, "Sukses Update policyandproceduremenu & policyandprocedureservices");
                        LoadData();
                        refreshHeader();
                        JC_Parents.removeAllItems();
                        LoadComboBox();
                    } else {
                        JOptionPane.showMessageDialog(null, "Gagal Update policyandproceduremenu");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Gagal Update policyandprocedureservices");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btn_updatesActionPerformed

    private void JC_ParentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JC_ParentsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JC_ParentsActionPerformed

    private void btn_simpanAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btn_simpanAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_simpanAncestorAdded

    private void edt_sortingKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edt_sortingKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            evt.consume();
        }
    }//GEN-LAST:event_edt_sortingKeyTyped

    private void edt_sortingsKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edt_sortingsKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            evt.consume();
        }
    }//GEN-LAST:event_edt_sortingsKeyTyped

    private void refreshHeader() {
        edt_labels.setText("");
        edt_titles.setText("");
        JT_Description.setText("");
        LoadData();
    }

    private void refreshDetail() {
        edt_sortings.setText("");
        edt_label.setText("");
        edt_sorting.setText("");
        edt_title.setText("");
        edt_file.setText("");
        JCM_Parent.removeAll();
        LoadData();
    }

    private void refreshAll() {

        edt_labels.setText("");
        edt_titles.setText("");
        JT_Description.setText("");
        edt_sorting.setText("");
        edt_title.setText("");
        edt_file.setText("");
        JCM_Parent.removeAll();
        LoadData();
    }

//    private static void showServerReply(FTPClient ftpClient) {
//        String[] replies = ftpClient.getReplyStrings();
//        if (replies != null && replies.length > 0) {
//            for (String aReply : replies) {
//                System.out.println("SERVER: " + aReply);
//            }
//        }
//    }
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
            java.util.logging.Logger.getLogger(JFrame_ManageContent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrame_ManageContent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrame_ManageContent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrame_ManageContent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrame_ManageContent().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> JCM_Parent;
    private javax.swing.JComboBox<String> JC_Parents;
    private javax.swing.JComboBox<String> JC_Status;
    private javax.swing.JComboBox<String> JC_StatusD;
    private javax.swing.JTextArea JT_Description;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_update;
    private javax.swing.JButton btn_updates;
    private javax.swing.JButton cari;
    private javax.swing.JTextField edt_cari;
    private javax.swing.JTextField edt_file;
    private javax.swing.JTextField edt_label;
    private javax.swing.JTextField edt_labels;
    private javax.swing.JTextField edt_sorting;
    private javax.swing.JTextField edt_sortings;
    private javax.swing.JTextField edt_title;
    private javax.swing.JTextField edt_titles;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
