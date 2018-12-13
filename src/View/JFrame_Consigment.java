/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Hellper.C_Connection;
import Hellper.Hellper_Portal;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PCIT1
 */
public class JFrame_Consigment extends javax.swing.JFrame {

    Connection connection;
    Statement stt;
    String sql;
    ResultSet res;
    PreparedStatement ps = null;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    Integer ValuesDefault = 0;
    Integer UpdateValuesDefault = 1;
    public String _Username;
    public String itemline;
    public String doc_id;
    public String Date;
    public String materialdisplay_id = null;
    public String tahun;

    Hellper_Portal hellper_Portal;

    /**
     * Creates new form JFrame_TakingOrder
     */
    public JFrame_Consigment() {
        initComponents();
        this.hellper_Portal = new Hellper_Portal();
        getDataHeaderTable(ValuesDefault);
        getDataHeaderDetailTable(ValuesDefault);
        CostumerType();
        CostumerName();
        Product(ValuesDefault);
        DetailHeaderForm(ValuesDefault);
        DetailHeaderForm(ValuesDefault);
        DownPayment(ValuesDefault);
    }

//        public JFrame_Consigment(String Username) {
//        initComponents();
//        _Username = Username;
//    }
    private void DownPayment(int Status) {
        if (Status == 0) {
            jCPayment.setEnabled(false);
            edt_cash.setEnabled(false);
            edt_noncash.setEnabled(false);
            edt_paynote.setEnabled(false);
            edt_cash.setText("0");
            edt_noncash.setText("0");
            edt_paynote.setText("0");

        } else {
            jCPayment.setEnabled(true);
            edt_cash.setEnabled(true);
            edt_noncash.setEnabled(true);
            edt_paynote.setEnabled(true);
        }
    }

    private void DetailHeaderForm(int Status) {
        if (Status == 0) {
            edt_dic_rp.setEnabled(false);
            edt_dic_rp.setText("5000");
            edt_detail_harga.setText("35000");
            edt_detail_harga.setEditable(false);
            edt_disc_percent.setText("0");
            edt_disc_percent.setEnabled(false);
        } else {
            Product(ValuesDefault);
            edt_dic_rp.setEnabled(false);
            edt_dic_rp.setText("5000");
            edt_detail_qty.setText("");
            edt_detail_harga.setText("35000");
            edt_disc_percent.setText("0");
            edt_disc_percent.setEnabled(false);
            getDataHeaderDetailTable(ValuesDefault);
            btn_detail_Add.setText("Add");
        }
    }

    /*
        jika status == 1 maka header form reset
     */
    private void HeaderForm(int Status) {
        if (Status == 1) {
            CostumerType();
            CostumerName();
            edt_remarks.setText("");
        }
    }

    /*
        perintah update get dari tabel detail
     */
    private void setHeader(int index) {
        jTableDetail.getValueAt(index, 4).toString();

    }

//    public int checkMaterialItems(String doc_id, String material_id) {
//        int Hasil = 0;
//
//        try {
//
//            sql = "SELECT COUNT(doc_id) as count from [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] "
//                    + "where "
//                    + "doc_id = '" + doc_id + "' AND materialdisplay_id = '" + material_id + "'";
//
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                Hasil = res.getInt(1);
//            }
//            res.close();
//            stt.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return Hasil;
//    }
//    
//    private void paymentType(int setValuesDefault) {
//        try {
//            if (setValuesDefault == 0) {
//                sql = "SELECT * FROM [192.168.12.12].[dbstore_ho].[dbo].[t_paytype]";
//            } else {
//                sql = "SELECT\n"
//                        + "C.Nama_type, A.doc_id, B.trans_id, c.code_type\n"
//                        + "FROM\n"
//                        + "[192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_header] AS A,\n"
//                        + "[192.168.12.12].[dbstore_ho].[dbo].[t_dp_header] AS B,\n"
//                        + "[192.168.12.12].[dbstore_ho].[dbo].[t_paytype] AS C\n"
//                        + "WHERE \n"
//                        + "A.doc_id = B.trans_id AND\n"
//                        + "A.doc_id ='" + JtHeader.getValueAt(setValuesDefault, 0).toString() + "' AND "
//                        + "B.type_bayar = C.code_type order by A.doc_id, B.trans_id";
//            }
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                jCPayment.addItem(res.getString(1));
//            }
//            res.close();
//            stt.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//    }
//
//    private String rePaytype(String valuesType) {
//        String Nilai = null;
//        try {
//            sql = "SELECT * FROM [192.168.12.12].[dbstore_ho].[dbo].[t_paytype] WHERE nama_type ='" + valuesType + "'";
//
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                Nilai = res.getString(2);
//            }
//            res.close();
//            stt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return Nilai;
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooserDialog1 = new datechooser.beans.DateChooserDialog();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jCCostumerType = new javax.swing.JComboBox<>();
        edt_date = new datechooser.beans.DateChooserCombo();
        edt_remarks = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jCPayment = new javax.swing.JComboBox<>();
        edt_cash = new javax.swing.JTextField();
        edt_noncash = new javax.swing.JTextField();
        edt_paynote = new javax.swing.JTextField();
        btn_save_header = new javax.swing.JButton();
        btn_neworder = new javax.swing.JButton();
        btn_log = new javax.swing.JButton();
        edt_costumer_name = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        edt_cariHeader = new javax.swing.JTextField();
        btn_CariHeader = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        JtHeader = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDetail = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        edt_detail_harga = new javax.swing.JTextField();
        edt_disc_percent = new javax.swing.JTextField();
        edt_detail_qty = new javax.swing.JTextField();
        edt_dic_rp = new javax.swing.JTextField();
        btn_Refresh = new javax.swing.JButton();
        btn_detail_Add = new javax.swing.JButton();
        JC_Product = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Consignment Order Customer ");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Consignment Order Customer "));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Header"));

        jLabel1.setText("Delivery Date");

        jCheckBox1.setText("This is the preferred order");

        jButton1.setText("Save");

        jButton2.setText("New Order");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(dateChooserCombo1, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Detail Items"));
        jPanel6.setToolTipText("");

        jLabel2.setText("Product");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Qty");

        jButton3.setText("Add");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Close");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox1, 0, 181, Short.MAX_VALUE)
                            .addComponent(jTextField1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Store Order"));

        jLabel4.setText("Plant :");

        jLabel5.setText("No Order");

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
        jScrollPane1.setViewportView(jTable1);

        jLabel6.setText("Date");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(156, 156, 156))))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(6, 6, 6))
        );

        jTabbedPane1.addTab("Regular Store Order", jPanel2);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Header"));

        jLabel7.setText("Costumer Type");

        jLabel8.setText("Costumer Name");

        jLabel10.setText("Delivery Date");

        jLabel12.setText("Remarks");

        jCCostumerType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCCostumerTypeActionPerformed(evt);
            }
        });

        edt_remarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_remarksActionPerformed(evt);
            }
        });

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Down Payment"));

        jLabel13.setText("Payment Type");

        jLabel14.setText("Cash");

        jLabel15.setText("Non Cash");

        jLabel16.setText("Pay Note");

        jCPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCPaymentActionPerformed(evt);
            }
        });

        edt_cash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_cashActionPerformed(evt);
            }
        });

        edt_noncash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_noncashActionPerformed(evt);
            }
        });

        btn_save_header.setText("Save");
        btn_save_header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_save_headerMouseClicked(evt);
            }
        });
        btn_save_header.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_save_headerActionPerformed(evt);
            }
        });

        btn_neworder.setText("New Order");
        btn_neworder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_neworderMouseClicked(evt);
            }
        });

        btn_log.setText("Log");
        btn_log.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_logMouseClicked(evt);
            }
        });
        btn_log.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel15)
                            .addComponent(jLabel14)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCPayment, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(edt_cash)
                            .addComponent(edt_noncash)
                            .addComponent(edt_paynote)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_log)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_neworder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_save_header)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jCPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(edt_cash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(edt_noncash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(edt_paynote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_save_header)
                    .addComponent(btn_neworder)
                    .addComponent(btn_log)))
        );

        edt_costumer_name.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                edt_costumer_nameItemStateChanged(evt);
            }
        });
        edt_costumer_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_costumer_nameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCCostumerType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(edt_remarks)
                    .addComponent(edt_date, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                    .addComponent(edt_costumer_name, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jCCostumerType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(edt_costumer_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(edt_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(edt_remarks)))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Costumer Order"));

        btn_CariHeader.setText("CARI");
        btn_CariHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_CariHeaderMouseClicked(evt);
            }
        });

        JtHeader.setModel(new javax.swing.table.DefaultTableModel(
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
        JtHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JtHeaderMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(JtHeader);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(edt_cariHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_CariHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edt_cariHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_CariHeader))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Consignment Order Costumer", jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Form Taking Order From FO"));

        jTableDetail.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDetailMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDetail);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Material"));
        jPanel11.setToolTipText("Selected Material");

        jLabel17.setText("Product");

        jLabel18.setText("Harga");

        jLabel19.setText("Disc %");

        jLabel20.setText("Qty");

        jLabel21.setText("Disc Rp");

        edt_detail_harga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_detail_hargaActionPerformed(evt);
            }
        });
        edt_detail_harga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                edt_detail_hargaKeyTyped(evt);
            }
        });

        edt_detail_qty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_detail_qtyActionPerformed(evt);
            }
        });
        edt_detail_qty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                edt_detail_qtyKeyTyped(evt);
            }
        });

        edt_dic_rp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_dic_rpActionPerformed(evt);
            }
        });

        btn_Refresh.setText("REFRESH");
        btn_Refresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_RefreshMouseClicked(evt);
            }
        });

        btn_detail_Add.setText("Add");
        btn_detail_Add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_detail_AddMouseClicked(evt);
            }
        });
        btn_detail_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detail_AddActionPerformed(evt);
            }
        });

        JC_Product.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JC_ProductMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edt_disc_percent)
                    .addComponent(edt_detail_harga)
                    .addComponent(JC_Product, 0, 435, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(btn_Refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_detail_Add, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edt_dic_rp)
                            .addComponent(edt_detail_qty))))
                .addGap(21, 21, 21))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel20)
                    .addComponent(edt_detail_qty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JC_Product, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel21)
                    .addComponent(edt_detail_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edt_dic_rp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(edt_disc_percent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Refresh)
                    .addComponent(btn_detail_Add))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void edt_cashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_cashActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_cashActionPerformed

    private void edt_noncashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_noncashActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_noncashActionPerformed

    private void edt_detail_hargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_detail_hargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_detail_hargaActionPerformed

    private void edt_detail_qtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_detail_qtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_detail_qtyActionPerformed

    private void edt_dic_rpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_dic_rpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_dic_rpActionPerformed

//    private void goFormHeader(Integer id_get) {
//
//        if (id_get >= 0) {
//
//            edt_costumer_name.addItem(JtHeader.getValueAt(id_get, 5).toString());
//            edt_costumer_name.setEnabled(false);
//
//            edt_date.setEnabled(false);
//            jCPayment.setEnabled(false);
//            edt_cash.setEnabled(false);
//            edt_noncash.setEnabled(false);
//            jCCostumerType.setEnabled(false);
//            edt_paynote.setEnabled(false);
//            edt_remarks.setText(JtHeader.getValueAt(id_get, 9).toString());
//            edt_remarks.setEnabled(false);
//            btn_save_header.setEnabled(false);
//            btn_save_header.setText("Update");
//        } else {
//            setProfil();
//            jCCostumerType.removeAll();
//            jCCostumerType.setEnabled(true);
//            edt_remarks.setText("");
//            edt_costumer_name.setEnabled(true);
//            edt_date.setEnabled(true);
//            jCPayment.setEnabled(false);
//            edt_remarks.setEnabled(true);
//            btn_save_header.setEnabled(true);
//            btn_save_header.setText("Save");
//        }
//    }
    private void CostumerType() {
        try {
            sql = "SELECT customer_id, customer_name FROM [192.168.12.12].[dbstore_ho].[dbo].[m_customer]";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                jCCostumerType.addItem(res.getString(2));
            }

            res.close();
            stt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void CostumerName() {
        try {
            sql = "select id_cust, nama_cust, alamat, telp from [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_user]";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                edt_costumer_name.addItem(res.getString(2));
            }

            res.close();
            stt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void jTableDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDetailMouseClicked
        int baris = jTableDetail.rowAtPoint(evt.getPoint());

        itemline = jTableDetail.getValueAt(baris, 3).toString();
        materialdisplay_id = jTableDetail.getValueAt(baris, 4).toString();

//        JOptionPane.showMessageDialog(null, "Material Displays :" + materialdisplay_id+ " Item Line : "+itemline+" Tahun "+tahun);
        edt_detail_harga.setText(jTableDetail.getValueAt(baris, 7).toString());
        edt_disc_percent.setText(jTableDetail.getValueAt(baris, 8).toString());
        edt_detail_qty.setText(jTableDetail.getValueAt(baris, 6).toString());

        btn_detail_Add.setText("Update");
    }//GEN-LAST:event_jTableDetailMouseClicked

    private void Product(int id_material) {

        try {
            if (id_material == 0) {
                sql = "SELECT material_id, material_name FROM [192.168.12.12].[dbstore_ho].[dbo].[m_material] WHERE material_id in ('FG00248','FG00281')";
            } else {
                sql = "SELECT material_id, material_name FROM [192.168.12.12].[dbstore_ho].[dbo].[m_material] WHERE material_id = '" + id_material + "'";
            }
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                JC_Product.addItem(res.getString(1).toString() + " " + res.getString(2).toString());
            }
            res.close();
            stt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

//    private String checkProduct(String product) {
//        String Hasil = null;
//
//        try {
//
//            sql = "SELECT material_id FROM [192.168.12.12].[dbstore_ho].[dbo].[m_material] WHERE material_id = '" + product.substring(0, 7) + "'";
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                Hasil = res.getString(1).toString();
//            }
//            res.close();
//            stt.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return Hasil;
//    }

    private void jCCostumerTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCCostumerTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCCostumerTypeActionPerformed

    private void JC_ProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JC_ProductMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_JC_ProductMouseClicked

//    private void setHeaderDetail() {
////        edt_detail_harga.setText(getValueMaterial(checkProduct(JC_Product.getSelectedItem().toString())));
//    }
    private void btn_detail_AddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_detail_AddMouseClicked
        // TODO add your handling code here

//        JOptionPane.showMessageDialog(null, "Doc : " + doc_id);

        /*
            Check Dulu Click Dokument Heder
         */
        if (doc_id == null) {
            JOptionPane.showMessageDialog(null, "Ada Belum Memilih Document Header");
        } else {

            /*
                Check Kondisi Kosong Atau Tidak di database
             */
            if (materialdisplay_id != null) {
//                JOptionPane.showMessageDialog(null, "Ini Untuk Update" + materialdisplay_id);

                try {
                    sql = "UPDATE [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] SET "
                            + " qty_pesan ='" + edt_detail_qty.getText() + "', "
                            + " qty_pesan_conv ='" + edt_detail_qty.getText() + "', "
                            + "harga = '" + edt_detail_harga.getText() + "',"
                            + "disc1 ='" + edt_disc_percent.getText() + "', "
                            + "nett_value ='" + hellper_Portal.HitungNet(edt_detail_harga.getText(), edt_detail_qty.getText(), Integer.toString(hellper_Portal.hitung(edt_dic_rp.getText(), edt_detail_qty.getText()))) + "', "
                            + "disc_value = '" + hellper_Portal.hitung("5000", edt_detail_qty.getText()) + "' "
                            + "WHERE doc_id = '" + doc_id + "' AND "
                            + "itemline ='" + itemline + "'";

                    stt = connection.createStatement();
                    int executeUpdate = stt.executeUpdate(sql);
                    if (executeUpdate >= 1) {

                        if (hellper_Portal.checkLog(doc_id) != null) {
                            sql = "UPDATE [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_log_header]\n"
                                    + "   SET \n"
                                    + "      [doc_id] = '" + doc_id + "'\n"
                                    + "      ,[nik] = '182735'\n"
                                    + "      ,[update_date] = CONVERT(VARCHAR(26),GETDATE(),120)"
                                    + "      ,[qty_update] = '" + edt_detail_qty.getText() + "'\n"
                                    + "      ,[keterangan] = 'data sudah di update', items='" + itemline + "'"
                                    + " WHERE doc_id = '" + doc_id + "'";
                            stt = connection.createStatement();
                            int executeUpdateLog = stt.executeUpdate(sql);
                            if (executeUpdateLog >= 1) {

                                /*
                                        hIDE uPDATE KE DB STORE
                                 */
//                                sql = "UPDATE [192.168.12.12].[dbstore].[dbo].[t_takeorder_detail] SET "
//                                        + " qty_pesan ='" + edt_detail_qty.getText() + "', "
//                                        + " qty_pesan_conv ='" + edt_detail_qty.getText() + "', "
//                                        + "harga = '" + edt_detail_harga.getText() + "',"
//                                        + "disc1 ='" + edt_disc_percent.getText() + "', "
//                                        + "nett_value ='" + hellper_Portal.HitungNet(edt_detail_harga.getText(), edt_detail_qty.getText(), Integer.toString(hellper_Portal.hitung(edt_dic_rp.getText(), edt_detail_qty.getText()))) + "', "
//                                        + "disc_value = '" + hellper_Portal.hitung("5000", edt_detail_qty.getText()) + "' "
//                                        + "WHERE doc_id = '" + doc_id + "' AND "
//                                        + "plant_id = 'R009' AND materialdisplay_id = '" + hellper_Portal.checkProduct(JC_Product.getSelectedItem().toString()) + "' AND fiscalyear ='" + tahun + "' AND itemline = '" + itemline + "'";
//
//                                stt = connection.createStatement();
//                                int executeUpdateHO = stt.executeUpdate(sql);
//                                if (executeUpdateHO >= 1) {
                                sql = "UPDATE [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_header] set \n"
                                        + "nilai_order = (SELECT SUM(nett_value) as net FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id ='" + doc_id + "' "
                                        + "AND plant_id ='R009' AND fiscalyear = '" + tahun + "')\n"
                                        + "WHERE doc_id = '" + doc_id + "' AND plant_id ='R009' AND fiscalyear = '" + tahun + "'";

                                stt = connection.createStatement();
                                int execute = stt.executeUpdate(sql);
                                if (execute >= 1) {
                                    JOptionPane.showMessageDialog(null, "Dokument " + doc_id + " Berhasil Di Update");
                                    getDataHeaderTable(ValuesDefault);
                                    getDataHeaderDetailTable(ValuesDefault);
                                    /*
                                        hIDE uPDATE KE DB STORE
                                     */
//                                        sql = "UPDATE [192.168.12.12].[dbstore].[dbo].[t_takeorder_header] set \n"
//                                                + "nilai_order = (SELECT SUM(nett_value) as net FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id ='" + doc_id + "' "
//                                                + "AND plant_id ='R009' AND fiscalyear = '" + tahun + "')\n"
//                                                + "WHERE doc_id = '" + doc_id + "' AND plant_id ='R009' AND fiscalyear = '" + tahun + "'";
//
//                                        stt = connection.createStatement();
//                                        int executeST = stt.executeUpdate(sql);
//                                        if (executeST >= 1) {
//                                            JOptionPane.showMessageDialog(null, "Dokumen " + doc_id + " Berhasil Di Update");
//                                            getDataHeaderTable(ValuesDefault);
//                                            getDataHeaderDetailTable(UpdateValuesDefault);
//                                        }
                                }
//                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Dokument " + doc_id + " Gagal Di Update LOG");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Dokument " + doc_id + " Di Database Log Tidak Tersedia");
                        }
                        getDataHeaderDetailTable(UpdateValuesDefault);
                    } else {
                        JOptionPane.showMessageDialog(null, "Gagal Update, Silahkan Coba Lagi");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
//                JOptionPane.showMessageDialog(null, "Ini Untuk Insert " +tahun);

                try {
                    sql = "INSERT INTO [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail]\n"
                            + "           ([doc_id]\n"
                            + "           ,[plant_id]\n"
                            + "           ,[fiscalyear]\n"
                            + "           ,[itemline]\n"
                            + "           ,[materialdisplay_id]\n"
                            + "           ,[qty_pesan]\n"
                            + "           ,[harga]\n"
                            + "           ,[disc1]\n"
                            + "           ,[disc_value]\n"
                            + "           ,[unit_id]\n"
                            + "           ,[qty_pesan_conv]\n"
                            + "           ,[unit_conv]\n"
                            + "           ,[flag_prioritas]\n"
                            + "           ,[nett_value])\n"
                            + "     VALUES "
                            + "('" + doc_id + "','R009',YEAR(getdate()),'" + hellper_Portal.checkLine(doc_id, "R009", tahun) + "','" + hellper_Portal.checkProduct(JC_Product.getSelectedItem().toString()) + "',"
                            + "'" + edt_detail_qty.getText() + "','" + edt_detail_harga.getText() + "','" + edt_disc_percent.getText() + "'," + hellper_Portal.hitung(edt_dic_rp.getText(), edt_detail_qty.getText()) + ",'PC',"
                            + "'" + edt_detail_qty.getText() + "','PC',NULL,'" + hellper_Portal.HitungNet(edt_detail_harga.getText(), edt_detail_qty.getText(), Integer.toString(hellper_Portal.hitung(edt_dic_rp.getText(), edt_detail_qty.getText()))) + "')";

                    System.out.println(sql);
                    connection = C_Connection.getConnection();
                    ps = connection.prepareStatement(sql);
                    ps.execute();

                    /*
                            hIDE uPDATE KE DB STORE
                     */
//                    sql = "INSERT INTO [192.168.12.12].[dbstore].[dbo].[t_takeorder_detail]\n"
//                            + "           ([doc_id]\n"
//                            + "           ,[plant_id]\n"
//                            + "           ,[fiscalyear]\n"
//                            + "           ,[itemline]\n"
//                            + "           ,[materialdisplay_id]\n"
//                            + "           ,[qty_pesan]\n"
//                            + "           ,[harga]\n"
//                            + "           ,[disc1]\n"
//                            + "           ,[disc_value]\n"
//                            + "           ,[unit_id]\n"
//                            + "           ,[qty_pesan_conv]\n"
//                            + "           ,[unit_conv]\n"
//                            + "           ,[flag_prioritas]\n"
//                            + "           ,[nett_value])\n"
//                            + "     VALUES "
//                            + "('" + doc_id + "','R009',YEAR(getdate()),'" + tahun + "','" + hellper_Portal.checkProduct(JC_Product.getSelectedItem().toString()) + "',"
//                            + "'" + edt_detail_qty.getText() + "','" + edt_detail_harga.getText() + "','" + edt_disc_percent.getText() + "'," + hellper_Portal.hitung(edt_dic_rp.getText(), edt_detail_qty.getText()) + ",'PC',"
//                            + "'" + edt_detail_qty.getText() + "','PC',NULL,'" + hellper_Portal.HitungNet(edt_detail_harga.getText(), edt_detail_qty.getText(), Integer.toString(hellper_Portal.hitung(edt_dic_rp.getText(), edt_detail_qty.getText()))) + "')";
//
//                    connection = C_Connection.getConnection();
//                    ps = connection.prepareStatement(sql);
//                    ps.execute();
                    sql = "INSERT INTO [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_log_header]\n"
                            + "           ([doc_id]\n"
                            + "           ,[nik]\n"
                            + "           ,[create_date]\n"
                            + "           ,[qty_awal]\n"
                            + "           ,[update_date]\n"
                            + "           ,[qty_update]\n"
                            + "           ,[keterangan],[items])\n"
                            + "     VALUES ('" + doc_id + "','182735',CONVERT(VARCHAR(26),GETDATE(),120),'" + edt_detail_qty.getText() + "',NULL,NULL,'Document Insert','2')";
                    connection = C_Connection.getConnection();
                    ps = connection.prepareStatement(sql);
                    ps.execute();

                    /*
                            hIDE uPDATE KE DB STORE
                     */
//                    sql = "INSERT INTO [192.168.12.12].[dbstore].[dbo].[t_takeorder_log_header]\n"
//                            + "           ([doc_id]\n"
//                            + "           ,[nik]\n"
//                            + "           ,[create_date]\n"
//                            + "           ,[qty_awal]\n"
//                            + "           ,[update_date]\n"
//                            + "           ,[qty_update]\n"
//                            + "           ,[keterangan],[items])\n"
//                            + "     VALUES ('" + doc_id + "','182735',CONVERT(VARCHAR(26),GETDATE(),120),'" + edt_detail_qty.getText() + "',NULL,NULL,'Document Insert','2')";
//                    connection = C_Connection.getConnection();
//                    ps = connection.prepareStatement(sql);
//                    ps.execute();
                    sql = "UPDATE [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_header] set \n"
                            + "nilai_order = (SELECT SUM(nett_value) as net FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id ='" + doc_id + "' "
                            + "AND plant_id ='R009' AND fiscalyear = '" + tahun + "')\n"
                            + "WHERE doc_id = '" + doc_id + "' AND plant_id ='R009' AND fiscalyear = '" + tahun + "'";

                    stt = connection.createStatement();
                    int executeUpdate = stt.executeUpdate(sql);
                    if (executeUpdate >= 1) {

                        JOptionPane.showMessageDialog(null, "Sukses Berhasil di input :" + doc_id);
                        /*
                            hIDE uPDATE KE DB STORE
                         */
//                        sql = "UPDATE [192.168.12.12].[dbstore].[dbo].[t_takeorder_header] set \n"
//                                + "nilai_order = (SELECT SUM(nett_value) as net FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id ='" + doc_id + "' "
//                                + "AND plant_id ='R009' AND fiscalyear = '" + tahun + "')\n"
//                                + "WHERE doc_id = '" + doc_id + "' AND plant_id ='R009' AND fiscalyear = '" + tahun + "'";
//
//                        stt = connection.createStatement();
//                        int execute01 = stt.executeUpdate(sql);
//                        if (execute01 >= 1) {
//                            JOptionPane.showMessageDialog(null, "Sukses Berhasil di input :" + doc_id);
//                            getDataHeaderTable(ValuesDefault);
//                            getDataHeaderDetailTable(ValuesDefault);
//                        }

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

//         Plant_id = jTableDetail.getValueAt(baris, 5).toString();
//   tahun = jTableDetail.getValueAt(baris, 5).toString();
//    material = jTableDetail.getValueAt(baris, 5).toString();
//        System.out.println(checkDokument(doc_id));     private int checkDokument(String doc, String Plant, String tahun, String material) {
//        if (doc_id == null) {
//            JOptionPane.showMessageDialog(null, "Mohon Maaf Anda Belum Memilih Document Header Item");
//        } else {
//            if (checkMaterialItems(doc_id, checkProduct(JC_Product.getSelectedItem().toString())) >= 1) {
//                JOptionPane.showMessageDialog(null, "Mohon Maaf Anda Sudah Comput " + checkProduct(JC_Product.getSelectedItem().toString()) + " Dan No Doc " + doc_id);
////                resetHeaderDetail();
//                getDataHeaderDetailTable(ValuesDefault);
//            } else {
//            System.out.println(checkDokument(doc_id, Plant_id, txt_tahuns.getText(), checkProduct(JC_Product.getSelectedItem().toString())));
//            if (checkDokument(doc_id) == 0) {
//            if (checkDokument(doc_id, Plant_id, Tahuns, material) == 0) {
//            if (checkDokument(doc_id, Plant_id, Tahuns, material) == 0) {
//
//                try {
//
//                    sql = "INSERT INTO [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail]\n"
//                            + "           ([doc_id]\n"
//                            + "           ,[plant_id]\n"
//                            + "           ,[fiscalyear]\n"
//                            + "           ,[itemline]\n"
//                            + "           ,[materialdisplay_id]\n"
//                            + "           ,[qty_pesan]\n"
//                            + "           ,[harga]\n"
//                            + "           ,[disc1]\n"
//                            + "           ,[disc_value]\n"
//                            + "           ,[unit_id]\n"
//                            + "           ,[qty_pesan_conv]\n"
//                            + "           ,[unit_conv]\n"
//                            + "           ,[flag_prioritas]\n"
//                            + "           ,[nett_value])\n"
//                            + "     VALUES "
//                            + "('" + doc_id + "','R009',YEAR(getdate()),'" + txt_contoh.getText() + "','" + checkProduct(JC_Product.getSelectedItem().toString()) + "',"
//                            + "'" + edt_detail_qty.getText() + "','" + edt_detail_harga.getText() + "','" + edt_disc_percent.getText() + "'," + hitung(edt_dic_rp.getText(), edt_detail_qty.getText()) + ",'PC',"
//                            + "'" + edt_detail_qty.getText() + "','PC',NULL,'" + HitungNet(edt_detail_harga.getText(), edt_detail_qty.getText(), Integer.toString(hitung(edt_dic_rp.getText(), edt_detail_qty.getText()))) + "')";
//
//                    System.out.println("");
//                    connection = C_Connection.getConnection();
//                    ps = connection.prepareStatement(sql);
//                    ps.execute();
//
//                    sql = "INSERT INTO [192.168.12.12].[dbstore].[dbo].[t_takeorder_detail]\n"
//                            + "           ([doc_id]\n"
//                            + "           ,[plant_id]\n"
//                            + "           ,[fiscalyear]\n"
//                            + "           ,[itemline]\n"
//                            + "           ,[materialdisplay_id]\n"
//                            + "           ,[qty_pesan]\n"
//                            + "           ,[harga]\n"
//                            + "           ,[disc1]\n"
//                            + "           ,[disc_value]\n"
//                            + "           ,[unit_id]\n"
//                            + "           ,[qty_pesan_conv]\n"
//                            + "           ,[unit_conv]\n"
//                            + "           ,[flag_prioritas]\n"
//                            + "           ,[nett_value])\n"
//                            + "     VALUES "
//                            + "('" + doc_id + "','R009',YEAR(getdate()),'" + txt_contoh.getText() + "','" + checkProduct(JC_Product.getSelectedItem().toString()) + "',"
//                            + "'" + edt_detail_qty.getText() + "','" + edt_detail_harga.getText() + "','" + edt_disc_percent.getText() + "'," + hitung(edt_dic_rp.getText(), edt_detail_qty.getText()) + ",'PC',"
//                            + "'" + edt_detail_qty.getText() + "','PC',NULL,'" + HitungNet(edt_detail_harga.getText(), edt_detail_qty.getText(), Integer.toString(hitung(edt_dic_rp.getText(), edt_detail_qty.getText()))) + "')";
//
//                    connection = C_Connection.getConnection();
//                    ps = connection.prepareStatement(sql);
//                    ps.execute();
//
//                    sql = "INSERT INTO [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_log_header]\n"
//                            + "           ([doc_id]\n"
//                            + "           ,[nik]\n"
//                            + "           ,[create_date]\n"
//                            + "           ,[qty_awal]\n"
//                            + "           ,[update_date]\n"
//                            + "           ,[qty_update]\n"
//                            + "           ,[keterangan],[items])\n"
//                            + "     VALUES ('" + doc_id + "','182735',CONVERT(VARCHAR(26),GETDATE(),120),'" + edt_detail_qty.getText() + "',NULL,NULL,'Document Insert','2')";
//                    connection = C_Connection.getConnection();
//                    ps = connection.prepareStatement(sql);
//                    ps.execute();
//
//                    sql = "INSERT INTO [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_log_header]\n"
//                            + "           ([doc_id]\n"
//                            + "           ,[nik]\n"
//                            + "           ,[create_date]\n"
//                            + "           ,[qty_awal]\n"
//                            + "           ,[update_date]\n"
//                            + "           ,[qty_update]\n"
//                            + "           ,[keterangan],[items])\n"
//                            + "     VALUES ('" + doc_id + "','182735',CONVERT(VARCHAR(26),GETDATE(),120),'" + edt_detail_qty.getText() + "',NULL,NULL,'Document Insert','2')";
//                    connection = C_Connection.getConnection();
//                    ps = connection.prepareStatement(sql);
//                    ps.execute();
//
//                    sql = "UPDATE [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_header] set \n"
//                            + "nilai_order = (SELECT SUM(nett_value) as net FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id ='" + doc_id + "' "
//                            + "AND plant_id ='R009' AND fiscalyear = '" + txt_tahuns.getText() + "')\n"
//                            + "WHERE doc_id = '" + doc_id + "' AND plant_id ='R009' AND fiscalyear = '" + txt_tahuns.getText() + "'";
//
////                    System.out.println(sql);
//                    stt = connection.createStatement();
//                    int executeUpdate = stt.executeUpdate(sql);
//                    if (executeUpdate >= 1) {
//
//                        sql = "UPDATE [192.168.12.12].[dbstore].[dbo].[t_takeorder_header] set \n"
//                                + "nilai_order = (SELECT SUM(nett_value) as net FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id ='" + doc_id + "' "
//                                + "AND plant_id ='R009' AND fiscalyear = '" + txt_tahuns.getText() + "')\n"
//                                + "WHERE doc_id = '" + doc_id + "' AND plant_id ='R009' AND fiscalyear = '" + txt_tahuns.getText() + "'";
//
//                        stt = connection.createStatement();
//                        int execute01 = stt.executeUpdate(sql);
//                        if (execute01 >= 1) {
//                            JOptionPane.showMessageDialog(null, "Sukses Berhasil di input :" + doc_id);
//                            getDataHeaderTable(ValuesDefault);
//                            getDataHeaderDetailTable(ValuesDefault);
//                        }
//
//                    }
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                try {
//                    sql = "UPDATE [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] SET "
//                            + " qty_pesan ='" + edt_detail_qty.getText() + "', "
//                            + " qty_pesan_conv ='" + edt_detail_qty.getText() + "', "
//                            + "harga = '" + edt_detail_harga.getText() + "',"
//                            + "disc1 ='" + edt_disc_percent.getText() + "', "
//                            + "nett_value ='" + HitungNet(edt_detail_harga.getText(), edt_detail_qty.getText(), Integer.toString(hitung(edt_dic_rp.getText(), edt_detail_qty.getText()))) + "', "
//                            + "disc_value = '" + hitung("5000", edt_detail_qty.getText()) + "' "
//                            + "WHERE doc_id = '" + doc_id + "' AND "
//                            + "itemline ='" + itemline + "'";
//
//                    stt = connection.createStatement();
//                    int executeUpdate = stt.executeUpdate(sql);
//                    if (executeUpdate >= 1) {
//
//                        if (checkLog(doc_id) != null) {
//                            sql = "UPDATE [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_log_header]\n"
//                                    + "   SET \n"
//                                    + "      [doc_id] = '" + doc_id + "'\n"
//                                    + "      ,[nik] = '182735'\n"
//                                    + "      ,[update_date] = CONVERT(VARCHAR(26),GETDATE(),120)"
//                                    + "      ,[qty_update] = '" + edt_detail_qty.getText() + "'\n"
//                                    + "      ,[keterangan] = 'data sudah di update', items='" + itemline + "'"
//                                    + " WHERE doc_id = '" + doc_id + "'";
////                            System.out.println(sql);
//                            stt = connection.createStatement();
//                            int executeUpdateLog = stt.executeUpdate(sql);
//                            if (executeUpdateLog >= 1) {
////                                JOptionPane.showMessageDialog(null, "Dokument Berhasil Di Update" + doc_id + "");
//
//                                sql = "UPDATE [192.168.12.12].[dbstore].[dbo].[t_takeorder_detail] SET "
//                                        + " qty_pesan ='" + edt_detail_qty.getText() + "', "
//                                        + " qty_pesan_conv ='" + edt_detail_qty.getText() + "', "
//                                        + "harga = '" + edt_detail_harga.getText() + "',"
//                                        + "disc1 ='" + edt_disc_percent.getText() + "', "
//                                        + "nett_value ='" + HitungNet(edt_detail_harga.getText(), edt_detail_qty.getText(), Integer.toString(hitung(edt_dic_rp.getText(), edt_detail_qty.getText()))) + "', "
//                                        + "disc_value = '" + hitung("5000", edt_detail_qty.getText()) + "' "
//                                        + "WHERE doc_id = '" + doc_id + "' AND "
//                                        + "plant_id = 'R009' AND materialdisplay_id = '" + checkProduct(JC_Product.getSelectedItem().toString()) + "' AND fiscalyear ='" + Tahuns + "' AND itemline = '" + itemline + "'";
//
//                                stt = connection.createStatement();
//                                int executeUpdateHO = stt.executeUpdate(sql);
//                                if (executeUpdateHO >= 1) {
////                                    JOptionPane.showMessageDialog(null, "Dokument Berhasil Di Update" + doc_id + "");
//
//                                    sql = "UPDATE [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_header] set \n"
//                                            + "nilai_order = (SELECT SUM(nett_value) as net FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id ='" + doc_id + "' "
//                                            + "AND plant_id ='R009' AND fiscalyear = '" + txt_tahuns.getText() + "')\n"
//                                            + "WHERE doc_id = '" + doc_id + "' AND plant_id ='R009' AND fiscalyear = '" + txt_tahuns.getText() + "'";
//
//                                    stt = connection.createStatement();
//                                    int execute = stt.executeUpdate(sql);
//                                    if (execute >= 1) {
//
//                                        sql = "UPDATE [192.168.12.12].[dbstore].[dbo].[t_takeorder_header] set \n"
//                                                + "nilai_order = (SELECT SUM(nett_value) as net FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id ='" + doc_id + "' "
//                                                + "AND plant_id ='R009' AND fiscalyear = '" + txt_tahuns.getText() + "')\n"
//                                                + "WHERE doc_id = '" + doc_id + "' AND plant_id ='R009' AND fiscalyear = '" + txt_tahuns.getText() + "'";
//
//                                        stt = connection.createStatement();
//                                        int executeST = stt.executeUpdate(sql);
//                                        if (executeST >= 1) {
//                                            JOptionPane.showMessageDialog(null, "Dokumen " + doc_id + " Berhasil Di Update");
//                                            getDataHeaderTable(ValuesDefault);
//                                            getDataHeaderDetailTable(ValuesDefault);
//                                        }
//                                    }
//                                }
//                            } else {
//                                JOptionPane.showMessageDialog(null, "Dokument " + doc_id + " Gagal Di Update LOG");
//                            }
//                        } else {
//                            JOptionPane.showMessageDialog(null, "Dokument " + doc_id + " Di Database Log Tidak Tersedia");
//                        }
//                        getDataHeaderDetailTable(ValuesDefault);
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Gagal Update, Silahkan Coba Lagi");
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
////            }
//
//        }
    }//GEN-LAST:event_btn_detail_AddMouseClicked

//    private String returnValues() {
//        String Hasil = null;
//
//        try {
//            sql = "SELECT SUM(nett_value) as net FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id ='50000015'";
//
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                if (res.getString(1) == null) {
//                    Hasil = null;
//                } else {
//
//                    Hasil = res.getString(1);
//
//                }
//            }
//            res.close();
//            stt.close();
//
//        } catch (SQLException e) {
//        }
//        return Hasil;
//    }
//    private int checkLine(String A, String B, String C) {
//        int Hasil = 0;
//
//        try {
//            sql = "select MAX(itemline) as item from [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] where doc_id = '" + A + "' and plant_id = '" + B + "' "
//                    + "and fiscalyear = '" + C + "'";
//
////            System.out.println(sql);
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                if (res.getString(1) == null) {
//                    Hasil = 2;
//                } else {
//                    Hasil = Integer.parseInt(res.getString(1)) + 1;
//                }
//            }
//            res.close();
//            stt.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Hasil;
//    }
//
//    private int hitung(String A, String B) {
//        int Hasil = 0;
//        Hasil = Integer.parseInt(A) * Integer.parseInt(B);
//        return Hasil;
//    }
//    private int HitungNet(String A, String B, String C) {
//        int Hasil = 0;
//        Hasil = Integer.parseInt(A) * Integer.parseInt(B) - Integer.parseInt(C);
//        return Hasil;
//    }
//
//    private String checkItemLine(String Log) {
//        String Hasil = null;
//        try {
//            sql = "select max(items)+1 as items from [192.168.12.12].[dbstore_ho].[dbo].t_takeorder_log_header WHERE doc_id ='" + Log + "'";
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                if (res.getString(1).toString() == null) {
//                    Hasil = "1";
//                } else {
//                    Hasil = res.getString(1).toString();
//                }
//            }
//            res.close();
//            stt.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Hasil;
//    }
//    private String checkLog(String Log) {
//        String Hasil = null;
//        try {
//            sql = "select count(doc_id) as total from [192.168.12.12].[dbstore_ho].[dbo].t_takeorder_log_header WHERE doc_id ='" + Log + "'";
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                Hasil = res.getString(1).toString();
//            }
//            res.close();
//            stt.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Hasil;
//    }
//    private String AddLine(String iddoc) {
//        String Hasil = null;
//        try {
//
//            sql = "SELECT max(itemline)+1 as items FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id = '" + iddoc + "'";
//
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                Hasil = res.getString(1).toString();
//
//            }
//            res.close();
//            stt.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return Hasil;
//    }
//    private String getValueMaterial(String materialid) {
//        String Hasil = null;
//        try {
//            sql = "SELECT harga_jual FROM [192.168.12.12].[dbstore_ho].[dbo].[m_material_price] WHERE material_id = '" + materialid + "'";
//
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                Hasil = res.getString(1).toString();
//            }
//            res.close();
//            stt.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return Hasil;
//    }
//    private int checkDokument(String doc, String Plant, String tahun, String material) {
//        int Hasil = 0;
//        try {
//            sql = "SELECT COUNT([doc_id]) AS JUMLAH FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] "
//                    + "WHERE doc_id = '" + doc + "' AND plant_id ='R009' AND fiscalyear ='" + tahun + "' AND materialdisplay_id = '" + material + "'";
//
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                Hasil = res.getInt(1);
//            }
//            res.close();
//            stt.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return Hasil;
//    }
//
    private void edt_remarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_remarksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_remarksActionPerformed

    private void btn_save_headerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_save_headerMouseClicked

        doc_id = getStringID();
        try {

            sql = "INSERT INTO [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_header]"
                    + "([doc_id],[plant_id],[fiscalyear],[doc_date],[posting_date],[production_date],[delivery_date],[order_type],[customer_id]\n"
                    + ",[customer_name], [member_id], [tlp] ,[jenis_trans] ,[sloc_id] ,[counter_print] ,[flag_proses], [process_date] ,[nilai_order]\n"
                    + ",[dp] ,[remark] , [flag_produksi], [flag_bypass], [user_entry], [user_update], [create_date], [modify_date], [flag_prioritas]\n"
                    + ",[no_op], [flag_cancel], [cancel_remark], [cancel_date], [cancel_user], [flag_closed], [closed_date], [closed_remark], [closed_user])\n"
                    + "VALUES"
                    + "('" + doc_id + "',"
                    + "'R009',"
                    + "YEAR(getdate()),"
                    + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
                    + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
                    + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
                    + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
                    + "'TO',"
                    + "'" + hellper_Portal.setPayType(jCCostumerType.getSelectedItem().toString()) + "',"
                    + "'" + edt_costumer_name.getSelectedItem().toString() + "',"
                    + "NULL,"
                    + "'" + hellper_Portal.goPhone(edt_costumer_name.getSelectedItem().toString()) + "',"
                    + "'IN',"
                    + "'DP001',"
                    + "0,"
                    + "'N',"
                    + "NULL,"
                    + "NULL,"
                    + "" + edt_cash.getText() + ","
                    + "'" + edt_remarks.getText() + "',"
                    + "'N',"
                    + "'N',"
                    + "'HO-ADMIN',"
                    + "NULL,"
                    + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
                    + "NULL,"
                    + "'N',"
                    + "NULL,"
                    + "'N',"
                    + "NULL,"
                    + "NULL,NULL,'N',NULL,NULL,NULL)";

            connection = C_Connection.getConnection();
            ps = connection.prepareStatement(sql);
            ps.execute();

            sql = "INSERT INTO [192.168.12.12].[dbstore].[dbo].[t_takeorder_header]"
                    + "([doc_id],[plant_id],[fiscalyear],[doc_date],[posting_date],[production_date],[delivery_date],[order_type],[customer_id]\n"
                    + ",[customer_name], [member_id], [tlp] ,[jenis_trans] ,[sloc_id] ,[counter_print] ,[flag_proses], [process_date] ,[nilai_order]\n"
                    + ",[dp] ,[remark] , [flag_produksi], [flag_bypass], [user_entry], [user_update], [create_date], [modify_date], [flag_prioritas]\n"
                    + ",[no_op], [flag_cancel], [cancel_remark], [cancel_date], [cancel_user], [flag_closed], [closed_date], [closed_remark], [closed_user])\n"
                    + "VALUES"
                    + "('" + doc_id + "',"
                    + "'R009',"
                    + "YEAR(getdate()),"
                    + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
                    + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
                    + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
                    + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
                    + "'TO',"
                    + "'" + hellper_Portal.setPayType(jCCostumerType.getSelectedItem().toString()) + "',"
                    + "'" + edt_costumer_name.getSelectedItem().toString() + "',"
                    + "NULL,"
                    + "'" + hellper_Portal.goPhone(edt_costumer_name.getSelectedItem().toString()) + "',"
                    + "'IN',"
                    + "'DP001',"
                    + "0,"
                    + "'N',"
                    + "NULL,"
                    + "NULL,"
                    + "" + edt_cash.getText() + ","
                    + "'" + edt_remarks.getText() + "',"
                    + "'N',"
                    + "'N',"
                    + "'HO-ADMIN',"
                    + "NULL,"
                    + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
                    + "NULL,"
                    + "'N',"
                    + "NULL,"
                    + "'N',"
                    + "NULL,"
                    + "NULL,NULL,'N',NULL,NULL,NULL)";

            connection = C_Connection.getConnection();
            ps = connection.prepareStatement(sql);
            ps.execute();

            JOptionPane.showMessageDialog(null, "Terima Kasih Data Sudah Masuk");
            getDataHeaderTable(ValuesDefault);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn_save_headerMouseClicked

//    private int HitungAmout(String A, String B, String C) {
//        int total = 0;
//        total = Integer.parseInt(A) + Integer.parseInt(B) + Integer.parseInt(C);
//        return total;
//    }
//
//    private int valueHeader(String document) {
//        int hasil = 0;
//        try {
//
//            sql = "SELECT count(trans_id) AS banyaknya FROM [192.168.12.12].[dbstore_ho].[dbo].[t_dp_header] WHERE trans_id = '" + document + "'";
//
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                hasil = res.getInt(1);
//            }
//            res.close();
//            stt.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return hasil;
//
//    }
//
//    public void GoPayment(String doc_id) {
//
//        if (valueHeader(doc_id) == 0) {
//            try {
//                sql = "INSERT INTO [192.168.12.12].[dbstore_ho].[dbo].[t_dp_header]\n"
//                        + "           ([doc_id]\n"
//                        + "           ,[fiscalyear]\n"
//                        + "           ,[plant_id]\n"
//                        + "           ,[doc_date]\n"
//                        + "           ,[trans_id]\n"
//                        + "           ,[trans_date]\n"
//                        + "           ,[doc_type]\n"
//                        + "           ,[type_bayar]\n"
//                        + "           ,[amount_dp]\n"
//                        + "           ,[bayar_cash]\n"
//                        + "           ,[bayar_noncash]\n"
//                        + "           ,[pay_note]\n"
//                        + "           ,[flag_lunas]\n"
//                        + "           ,[user_entry]\n"
//                        + "           ,[create_date]\n"
//                        + "           ,[user_modify]\n"
//                        + "           ,[modify_date]\n"
//                        + "           ,[flag_batal])\n"
//                        + "     VALUES  " + "('" + getStringID() + "',YEAR(getdate()),'R009',"
//                        + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
//                        + "'" + getStringID() + "','" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
//                        + "NULL, '" + rePaytype(jCPayment.getSelectedItem().toString()) + "',"
//                        + "" + HitungAmout(edt_cash.getText(), edt_noncash.getText(), edt_paynote.getText()) + ","
//                        + "" + Integer.parseInt(edt_cash.getText()) + ","
//                        + "" + Integer.parseInt(edt_noncash.getText()) + ","
//                        + "" + Integer.parseInt(edt_paynote.getText()) + ","
//                        + "'N',"
//                        + "'Admin',"
//                        + "'" + sdf.format(edt_date.getSelectedDate().getTime()) + "',"
//                        + "NULL,"
//                        + "NULL,"
//                        + "'N')";
//                JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
//                connection = C_Connection.getConnection();
//                ps = connection.prepareStatement(sql);
//                ps.execute();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } else if (valueHeader(doc_id) >= 1) {
//
//            try {
//                sql = "UPDATE [192.168.12.12].[dbstore_ho].[dbo].[t_dp_header] SET "
//                        + "amount_dp =" + HitungAmout(edt_cash.getText(), edt_noncash.getText(), edt_paynote.getText()) + ", "
//                        + "bayar_cash = " + Integer.parseInt(edt_cash.getText()) + ","
//                        + "bayar_noncash =" + Integer.parseInt(edt_noncash.getText()) + ", "
//                        + "pay_note = " + Integer.parseInt(edt_paynote.getText()) + ", "
//                        + "type_bayar = '" + rePaytype(jCPayment.getSelectedItem().toString()) + "' "
//                        + "WHERE trans_id = '" + doc_id + "'";
//
//                stt = connection.createStatement();
//                int executeUpdate = stt.executeUpdate(sql);
//
//                if (executeUpdate >= 1) {
//                    JOptionPane.showMessageDialog(null, "Sukses Update");
//                    getDataHeaderTable(ValuesDefault);
//                    goFormHeader(-1);
//                } else {
//                    JOptionPane.showMessageDialog(null, "Gagal Update, Silahkan Coba Lagi");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String setPayType(String Masuk) {
//        String Hasil = null;
//
//        try {
//            sql = "SELECT customer_id, customer_name FROM [192.168.12.12].[dbstore_ho].[dbo].[m_customer] WHERE customer_name ='" + Masuk + "'";
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//                Hasil = res.getString(1);
//            }
//
//            res.close();
//            stt.close();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return Hasil;
//    }
//
    private String getStringID() {
        String Hasil = null;
        try {
            sql = "SELECT MAX(DOC_ID)+1 AS NILAI_TERTINGGI, MAX(DOC_ID) AS NILAI_ADA FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_header] WHERE fiscalyear = YEAR(getdate())";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1);
            }
            res.close();
            stt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Hasil;
    }

    private void btn_neworderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_neworderMouseClicked
        HeaderForm(UpdateValuesDefault);
    }//GEN-LAST:event_btn_neworderMouseClicked

    private void jCPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCPaymentActionPerformed
        // TODO add your handling code here:

//        if (jCPayment.getSelectedIndex() == 0) {
//            payValues = "C";
//            edt_cash.setEnabled(true);
//            edt_noncash.setEnabled(false);
//            edt_paynote.setEnabled(false);
//        } else if (jCPayment.getSelectedIndex() == 1) {
//            payValues = "T";
//            edt_cash.setEnabled(false);
//            edt_noncash.setEnabled(true);
//            edt_paynote.setEnabled(true);
//        } else if (jCPayment.getSelectedIndex() == 2) {
//            payValues = "D";
//            edt_cash.setEnabled(false);
//            edt_noncash.setEnabled(true);
//            edt_paynote.setEnabled(true);
//        } else if (jCPayment.getSelectedIndex() == 3) {
//            payValues = "K";
//            edt_cash.setEnabled(false);
//            edt_noncash.setEnabled(true);
//            edt_paynote.setEnabled(true);
//        } else if (jCPayment.getSelectedIndex() == 4) {
//            payValues = "CK";
//            edt_cash.setEnabled(true);
//            edt_noncash.setEnabled(true);
//            edt_paynote.setEnabled(true);
//        } else if (jCPayment.getSelectedIndex() == 5) {
//            payValues = "CD";
//            edt_cash.setEnabled(true);
//            edt_noncash.setEnabled(true);
//            edt_paynote.setEnabled(true);
//        }

    }//GEN-LAST:event_jCPaymentActionPerformed

    private void btn_logMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_logMouseClicked
        JFrame_Consigment_Log jFrame_TakingOrder_Log = new JFrame_Consigment_Log();
        jFrame_TakingOrder_Log.setVisible(true);
    }//GEN-LAST:event_btn_logMouseClicked

    private void btn_save_headerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_save_headerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_save_headerActionPerformed

    private void btn_detail_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detail_AddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_detail_AddActionPerformed

//    private String goPhone(String nama) {
//        String Hasil = null;
//        try {
//
//            sql = "SELECT telp from [192.168.12.12].[dbstore_ho].[dbo].t_takeorder_user where nama_cust = '" + nama + "'";
//            connection = C_Connection.getConnection();
//            stt = connection.createStatement();
//            res = stt.executeQuery(sql);
//
//            while (res.next()) {
//
//                Hasil = res.getString(1);
//            }
//            res.close();
//            stt.close();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return Hasil;
//    }

    private void edt_costumer_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_costumer_nameActionPerformed

//        edt_address.setText(edt_costumer_name.getSelectedItem().toString());
//        if (edt_costumer_name.getSelectedIndex() >= 0) {
//            try {
//                sql = "select id_cust, nama_cust, alamat, telp from [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_user] WHERE nama_cust ='" + edt_costumer_name.getSelectedItem().toString() + "'";
//
//                connection = C_Connection.getConnection();
//                stt = connection.createStatement();
//                res = stt.executeQuery(sql);
//
//                while (res.next()) {
////                edt_phones.setText(res.getString(4));
//                    edt_address.setText(res.getString(3).toString());
//                    System.out.println(sql);
//                }
//
//                res.close();
//                stt.close();
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

    }//GEN-LAST:event_edt_costumer_nameActionPerformed

    private void edt_costumer_nameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_edt_costumer_nameItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_edt_costumer_nameItemStateChanged

    private void btn_logActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_logActionPerformed

    private void btn_RefreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_RefreshMouseClicked

        DetailHeaderForm(2);
//        JC_Product.setEnabled(true);
//        edt_detail_harga.setText("");
////        edt_detail_harga.setVisible(true);
//        edt_detail_harga.setEnabled(true);
//        edt_detail_qty.setVisible(true);
//        edt_detail_qty.setText("");
//        btn_detail_Add.setText("Add");
    }//GEN-LAST:event_btn_RefreshMouseClicked

    private void edt_detail_hargaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edt_detail_hargaKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            evt.consume();
        }
    }//GEN-LAST:event_edt_detail_hargaKeyTyped

    private void edt_detail_qtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edt_detail_qtyKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            evt.consume();
        }
    }//GEN-LAST:event_edt_detail_qtyKeyTyped

    private void JtHeaderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JtHeaderMouseClicked

        int baris = JtHeader.rowAtPoint(evt.getPoint());

        /*
            set default Header Doc ID
         */
        doc_id = JtHeader.getValueAt(baris, 0).toString();
        tahun = JtHeader.getValueAt(baris, 1).toString();
        getDataHeaderDetailTable(baris);
    }//GEN-LAST:event_JtHeaderMouseClicked

    private void btn_CariHeaderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CariHeaderMouseClicked
        // TODO add your handling code here:
        if (edt_cariHeader.getText().isEmpty()) {
            getDataHeaderTable(ValuesDefault);
        } else {
            getDataHeaderTable(UpdateValuesDefault);
        }
    }//GEN-LAST:event_btn_CariHeaderMouseClicked

    private void getDataHeaderTable(int setValuesDefault) {

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nomor Doc");
        model.addColumn("Tahun");
        model.addColumn("Doc Date");
        model.addColumn("Delivery Date");
        model.addColumn("Type Customer");
        model.addColumn("Nama Customer");
        model.addColumn("Telp");
        model.addColumn("Nilai Order");
        model.addColumn("Dp");
        model.addColumn("Remark");
        model.addColumn("Costumer");

        try {
            if (setValuesDefault == 0) {
                sql = "SELECT \n"
                        + "A.doc_id, C.plant_name, A.fiscalyear, A.doc_date, A.posting_date, A.delivery_date , B.customer_name, A.customer_name as nama, A.tlp, A.flag_proses, A.nilai_order, A.dp, A.remark, A.customer_id \n"
                        + "FROM\n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_header] AS A,\n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[m_customer] AS B,\n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[m_plant] C\n"
                        + "WHERE\n"
                        + "A.customer_id = B.customer_id AND\n"
                        + "A.plant_id = C.plant_id and \n"
                        + "A.order_type = 'TO' and\n"
                        + "A.fiscalyear =  YEAR(getdate()) \n"
                        + "/*and\n"
                        + "A.flag_proses = 'N' and\n"
                        + "A.flag_cancel = 'N' and\n"
                        + "A.flag_closed = 'N' and\n"
                        + "A.plant_id = 'R009'\n"
                        + "*/\n"
                        + "order by A.doc_id DESC";

            } else {
                sql = "SELECT \n"
                        + "A.doc_id, C.plant_name, A.fiscalyear, A.doc_date, A.posting_date, A.delivery_date , B.customer_name, A.customer_name as nama, A.tlp, A.flag_proses, A.nilai_order, A.dp, A.remark, B.customer_id \n"
                        + "FROM\n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_header] AS A,\n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[m_customer] AS B,\n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[m_plant] C\n"
                        + "WHERE\n"
                        + "A.customer_id = B.customer_id AND\n"
                        + "A.plant_id = C.plant_id and \n"
                        + "A.order_type = 'TO' and\n"
                        + "A.fiscalyear =  YEAR(getdate()) \n"
                        + "/*and\n"
                        + "A.flag_proses = 'N' and\n"
                        + "A.flag_cancel = 'N' and\n"
                        + "A.flag_closed = 'N' and\n"
                        + "A.plant_id = 'R009'\n"
                        + "*/\n"
                        + "AND A.doc_id LIKE '%" + edt_cariHeader.getText() + "%' OR A.fiscalyear LIKE '%" + edt_cariHeader.getText() + "%'"
                        + "order by A.doc_id DESC";
            }
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[]{res.getString(1), res.getString(3), res.getString(4), res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(11), res.getString(12), res.getString(13), res.getString(14)});
            }
            res.close();
            stt.close();
            JtHeader.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getDataHeaderDetailTable(int setValuesDefault) {

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nomor Doc");
        model.addColumn("Plant Name");
        model.addColumn("Date");
        model.addColumn("Item Line");
        model.addColumn("Material id");
        model.addColumn("Material");
        model.addColumn("Qty");
        model.addColumn("Harga");
        model.addColumn("Disc");
        model.addColumn("Unit Values");
        model.addColumn("Qty Pesan");
        model.addColumn("Flag Proritas");
        model.addColumn("Net");

        try {
            if (setValuesDefault >= 0) {
                sql = "SELECT \n"
                        + "A.doc_id, B.plant_name, A.fiscalyear, A.itemline, C.material_id, C.material_name, A.qty_pesan, A.harga, A.disc1, A.disc_value, \n"
                        + "A.qty_pesan_conv, A.flag_prioritas, A.nett_value \n"
                        + "FROM \n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] AS A, \n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[m_plant] AS B,\n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[m_material] AS C\n"
                        + "WHERE\n"
                        + "A.plant_id = B.plant_id AND A.materialdisplay_id = C.material_id\n"
                        + "AND A.doc_id = '" + JtHeader.getValueAt(setValuesDefault, 0).toString() + "' AND A.fiscalyear = '" + JtHeader.getValueAt(setValuesDefault, 1).toString() + "'"
                        + "ORDER BY A.doc_id, A.fiscalyear asc";
            } else {
                sql = "SELECT \n"
                        + "A.doc_id, B.plant_name, A.fiscalyear, A.itemline, C.material_id, C.material_name, A.qty_pesan, A.harga, A.disc1, A.disc_value, \n"
                        + "A.qty_pesan_conv, A.flag_prioritas, A.nett_value \n"
                        + "FROM \n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] AS A, \n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[m_plant] AS B,\n"
                        + "[192.168.12.12].[dbstore_ho].[dbo].[m_material] AS C\n"
                        + "WHERE\n"
                        + "A.plant_id = B.plant_id AND A.materialdisplay_id = C.material_id\n"
                        + "AND A.doc_id = '" + JtHeader.getValueAt(setValuesDefault, 0).toString() + "' AND A.fiscalyear = '" + JtHeader.getValueAt(setValuesDefault, 1).toString() + "'"
                        + "ORDER BY A.doc_id, A.fiscalyear asc";
                System.out.println(sql);
            }
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[]{
                    res.getString(1),
                    res.getString(2),
                    res.getString(3),
                    res.getString(4),
                    res.getString(5),
                    res.getString(6),
                    res.getString(7),
                    res.getString(8),
                    res.getString(9),
                    res.getString(10),
                    res.getString(11),
                    res.getString(12),
                    res.getString(13)
                });
            }
            res.close();
            stt.close();
            jTableDetail.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

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
            java.util.logging.Logger.getLogger(JFrame_Consigment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrame_Consigment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrame_Consigment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrame_Consigment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrame_Consigment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> JC_Product;
    private javax.swing.JTable JtHeader;
    private javax.swing.JButton btn_CariHeader;
    private javax.swing.JButton btn_Refresh;
    private javax.swing.JButton btn_detail_Add;
    private javax.swing.JButton btn_log;
    private javax.swing.JButton btn_neworder;
    private javax.swing.JButton btn_save_header;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserDialog dateChooserDialog1;
    private javax.swing.JTextField edt_cariHeader;
    private javax.swing.JTextField edt_cash;
    private javax.swing.JComboBox<String> edt_costumer_name;
    private datechooser.beans.DateChooserCombo edt_date;
    private javax.swing.JTextField edt_detail_harga;
    private javax.swing.JTextField edt_detail_qty;
    private javax.swing.JTextField edt_dic_rp;
    private javax.swing.JTextField edt_disc_percent;
    private javax.swing.JTextField edt_noncash;
    private javax.swing.JTextField edt_paynote;
    private javax.swing.JTextField edt_remarks;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jCCostumerType;
    private javax.swing.JComboBox<String> jCPayment;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableDetail;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
