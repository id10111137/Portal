/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Hellper.C_Connection;
import Hellper.Hellper_Portal;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.net.util.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import sun.misc.BASE64Decoder;

/**
 *
 * @author PCIT1
 */
public class JFrame_Home extends javax.swing.JFrame {

    /**
     * Creates new form JFrame_Home
     */
    Connection connection;
    Statement stt;
    String sql;
    ResultSet res;
    PreparedStatement ps = null;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String _Username;
    public String username;
    public String email;
    public String name;
    public String is_active;
    Hellper_Portal hellper_Portal;
    File f;

    public JFrame_Home() {
        initComponents();
    }

    public JFrame_Home(String Username) {
        initComponents();
        _Username = Username;
        this.hellper_Portal = new Hellper_Portal();
        setPayType(_Username);
        getDataTable(0);
        Image image = null;
        getDept();
        getDataMyRequest(0);
        getDataName();

        ImageIcon imageIcon = new ImageIcon(decodeToImage(hellper_Portal.getLOGO()));
//        txt_image.getWidth()
        image = imageIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(1120, 120, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);  // transform it back
        txt_icon.setIcon(imageIcon);

        if (hellper_Portal.AutoUsers(_Username, "HometabPane2") == 1) {
            this.jTabbedPane1.setEnabledAt(2, true);
        } else {
            this.jTabbedPane1.setEnabledAt(2, false);
            this.jTabbedPane1.remove(2);
        }

        if (hellper_Portal.AutoUsers(_Username, "HometabPane3") == 1) {
            this.jTabbedPane1.setEnabledAt(2, true);
        } else {
            this.jTabbedPane1.setEnabledAt(2, false);
            this.jTabbedPane1.remove(2);
        }

        txt_pesan.setLineWrap(true);
        txt_total_myissue.setText("Total My Issue : " + hellper_Portal.SumMyIssue(_Username));
        txt_close.setText("Closed My Issue : " + hellper_Portal.CountClossed(_Username));
        txt_inprogress.setText("In Progress My Issue : " + hellper_Portal.CountInprogress(_Username));
        txt_outstanding.setText("OutStanding : " + hellper_Portal.CountOutStanding(_Username));

        txt_all_close.setText("All Closed :" + hellper_Portal.AllCountClossed());
        txt_all_inprogress.setText("All In Progress : " + hellper_Portal.AllCountInprogress());
        txt_all_outstanding.setText(" All OutStanding : " + hellper_Portal.AllCountOutStanding());

        txt_sum_Dept.setText("Total Dept Issue :" + hellper_Portal.AllIsseDept(_Username));
        txt_closeDept.setText("Dept Closed :" + hellper_Portal.AllIsseuDeptStatus(_Username, "closed"));
        txt_inprogressDept.setText("Dept In Progress : " + hellper_Portal.AllIsseuDeptStatus(_Username, "Diskusi"));
        txt_outstandingDept.setText("Dept OutStanding : " + hellper_Portal.AllIsseuDeptStatus(_Username, "Diskusi"));

        LoadGraiph();
        LoadGraiphAll();
        LoadGraphDept();
        LoadBarStatusTicket();
        load3DChart();
        loadLineChart();
        getDataDept(0);
    }

    public final void loadLineChart() {
        DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
        line_chart_dataset.addValue(15, "Ticket", "1970");
        line_chart_dataset.addValue(30, "Ticket", "1980");
        line_chart_dataset.addValue(60, "Ticket", "1990");
        line_chart_dataset.addValue(120, "Ticket", "2000");
        line_chart_dataset.addValue(240, "Ticket", "2010");
        line_chart_dataset.addValue(300, "Ticket", "2014");

        JFreeChart lineChartObject = ChartFactory.createLineChart(
                "Ticket Per Tahun", "Tahun",
                "Ticket Count",
                line_chart_dataset, PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel myChart = new ChartPanel(lineChartObject);
        lineDate.setLayout(new java.awt.BorderLayout());
        lineDate.setPreferredSize(new Dimension(200, 200));
        lineDate.add(myChart, BorderLayout.CENTER);
        lineDate.validate();
    }

    public final void load3DChart() {

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Middle", new Double(20));
        dataset.setValue("Low", new Double(20));
        dataset.setValue("Height", new Double(40));

        JFreeChart chart = ChartFactory.createPieChart3D(
                "Status Ticket", // chart title                   
                dataset, // data 
                true, // include legend                   
                true,
                false);

        ChartPanel myChart = new ChartPanel(chart);
        jPane3D.setLayout(new java.awt.BorderLayout());
        jPane3D.setPreferredSize(new Dimension(200, 200));
        jPane3D.add(myChart, BorderLayout.CENTER);
        jPane3D.validate();
    }

    public final BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            try (ByteArrayInputStream bis = new ByteArrayInputStream(imageByte)) {
                image = ImageIO.read(bis);
                image.getScaledInstance(30, 50, 30);
            }
        } catch (IOException e) {
        }
        return image;
    }

    private void LoadBarStatusTicket() {

        final String fiat = "Hight";
        final String audi = "Low";
        final String ford = "Midle";
        final String speed = "SAP";
        final String millage = "POS";
        final String userrating = "HRIS";
        final String safety = "Portal";
        final DefaultCategoryDataset Bardataset = new DefaultCategoryDataset();

        Bardataset.addValue(1.0, fiat, speed);
        Bardataset.addValue(3.0, fiat, userrating);
        Bardataset.addValue(5.0, fiat, millage);
        Bardataset.addValue(5.0, fiat, safety);

        Bardataset.addValue(5.0, audi, speed);
        Bardataset.addValue(6.0, audi, userrating);
        Bardataset.addValue(10.0, audi, millage);
        Bardataset.addValue(4.0, audi, safety);

        Bardataset.addValue(4.0, ford, speed);
        Bardataset.addValue(2.0, ford, userrating);
        Bardataset.addValue(3.0, ford, millage);
        Bardataset.addValue(6.0, ford, safety);

        JFreeChart barChart = ChartFactory.createBarChart(
                "System Type",
                "Category",
                "Score",
                Bardataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel myChart = new ChartPanel(barChart);
        jPane_Status_Ticket.setLayout(new java.awt.BorderLayout());
        jPane_Status_Ticket.setPreferredSize(new Dimension(200, 200));
        jPane_Status_Ticket.add(myChart, BorderLayout.CENTER);
        jPane_Status_Ticket.validate();
    }

    private void getDataDept(int nullNumber) {

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Ticket Nomor");
        model.addColumn("Judul Ticket");
        model.addColumn("Tgl Ticket");
        model.addColumn("Assign Dept");
        model.addColumn("Assign Name");
        model.addColumn("System Type");
        model.addColumn("Status");
        model.addColumn("Isi Pesan");
        model.addColumn("Nik");
        model.addColumn("Status Pekerjaan");

        try {
            if (nullNumber == 0) {

                sql = "SELECT \n"
                        + "[ticket_no],[judul_ticket],[tgl_ticket],[asign_dept_ticket],[asign_name_ticket],[system_type],[status],[isi_pesan],[nik],[status_pengerjaan]\n"
                        + " FROM ticket_master WHERE nik IN (\n"
                        + "select users.username\n"
                        + "                    from\n"
                        + "                    users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept\n"
                        + "                    JOIN organization ON organization.id = subOrganization.id_organization WHERE organization.name = (select organization.name\n"
                        + "                    from\n"
                        + "                    users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept\n"
                        + "                    JOIN organization ON organization.id = subOrganization.id_organization WHERE users.username = '" + _Username + "')\n"
                        + ")";

            } else {

                sql = "SELECT \n"
                        + "[ticket_no],[judul_ticket],[tgl_ticket],[asign_dept_ticket],[asign_name_ticket],[system_type],[status],[isi_pesan],[nik],[status_pengerjaan]\n"
                        + " FROM ticket_master WHERE nik IN (\n"
                        + "select users.username\n"
                        + "                    from\n"
                        + "                    users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept\n"
                        + "                    JOIN organization ON organization.id = subOrganization.id_organization WHERE organization.name = (select organization.name\n"
                        + "                    from\n"
                        + "                    users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept\n"
                        + "                    JOIN organization ON organization.id = subOrganization.id_organization WHERE users.username = '" + _Username + "')\n"
                        + ") AND ticket_no = '" + edt_cari.getText() + "'";
            }

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[]{res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10)});
            }

            res.close();
            stt.close();
            JT_Dept.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void LoadGraiph() {

        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Closed", hellper_Portal.CountClossed(_Username));
        pieDataset.setValue("In Progress", hellper_Portal.CountInprogress(_Username));
        pieDataset.setValue("OutStanding", hellper_Portal.CountOutStanding(_Username));

        JFreeChart chart = ChartFactory.createPieChart(
                "My Issue",
                pieDataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setExplodePercent("Java", 0.30);
        plot.setStartAngle(100);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);

        ChartPanel myChart = new ChartPanel(chart);

        jpanel_MyResume.setLayout(new java.awt.BorderLayout());
        jpanel_MyResume.setPreferredSize(new Dimension(200, 200));
        jpanel_MyResume.add(myChart, BorderLayout.CENTER);
        jpanel_MyResume.validate();
    }

    private void LoadGraphDept() {

        // membuat dataset
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Closed", hellper_Portal.AllIsseuDeptStatus(_Username, "closed"));
        pieDataset.setValue("In Progress", hellper_Portal.AllIsseuDeptStatus(_Username, "Diskusi"));
        pieDataset.setValue("OutStanding", hellper_Portal.AllIsseuDeptStatus(_Username, "Diskusi"));

        // membuat chart...
        JFreeChart chart = ChartFactory.createPieChart(
                "My Dept Issue",
                pieDataset,
                true, // tampilkan legend
                true, // tampilkan tooltips
                false // URLs
        );

        // sedikit modifikasi tampilan dengan menambahkan exploded section
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setExplodePercent("Java", 0.30);
        plot.setStartAngle(100);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);

        ChartPanel myChart = new ChartPanel(chart);

        jpanel_Dept.setLayout(new java.awt.BorderLayout());
        jpanel_Dept.setPreferredSize(new Dimension(200, 200));
        jpanel_Dept.add(myChart, BorderLayout.CENTER);
        jpanel_Dept.validate();
    }

    private void LoadGraiphAll() {

        // membuat dataset
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Closed", hellper_Portal.AllCountClossed());
        pieDataset.setValue("In Progress", hellper_Portal.AllCountInprogress());
        pieDataset.setValue("OutStanding", hellper_Portal.AllCountOutStanding());

        // membuat chart...
        JFreeChart chart = ChartFactory.createPieChart(
                "All Issue Chart",
                pieDataset,
                true, // tampilkan legend
                true, // tampilkan tooltips
                false // URLs
        );

        // sedikit modifikasi tampilan dengan menambahkan exploded section
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setExplodePercent("Java", 0.30);
        plot.setStartAngle(100);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);

        ChartPanel myChart = new ChartPanel(chart);

        all_resume.setLayout(new java.awt.BorderLayout());
        all_resume.setPreferredSize(new Dimension(200, 200));
        all_resume.add(myChart, BorderLayout.CENTER);
        all_resume.validate();
    }

    private void getDept() {
        try {
            sql = "select name from [dbo].[organization] where is_active = '1'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                JC_Asigment.addItem(res.getString(1));
            }
        } catch (SQLException e) {
        }
    }

    public final void setPayType(String Masuk) {
        try {
            sql = "SELECT [username]\n"
                    + "      ,[email]\n"
                    + "      ,[name]\n"
                    + "      ,[is_active]\n"
                    + "  FROM [dbo].[users] WHERE USERNAME = '" + Masuk + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                username = res.getString(1).toString();
                email = res.getString(2);
                name = res.getString(3);
                is_active = res.getString(4);

                id_nama.setText(name);
                id_email.setText(email);
                id_nik.setText(username);
                txt_Dept.setText("Invalid");

            }

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

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu11 = new javax.swing.JMenu();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem49 = new javax.swing.JMenuItem();
        jMenuItem50 = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        id_nama = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        id_email = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        id_nik = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txt_Dept = new javax.swing.JLabel();
        jp_logo = new javax.swing.JPanel();
        txt_icon = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        edt_judul = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        JC_SystemType = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txt_pesan = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        edt_file = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        btn_refresh = new javax.swing.JButton();
        JC_Asigment = new javax.swing.JComboBox<>();
        JC_Status = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        JC_Name = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        Due_Date = new datechooser.beans.DateChooserCombo();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        JPanel_MyRequest = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JC_MyRequest = new javax.swing.JTable();
        edt_cari_my_request = new javax.swing.JTextField();
        btn_cari_myrequest = new javax.swing.JButton();
        btn_load = new javax.swing.JButton();
        jpanel_MyResume = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        txt_outstanding = new javax.swing.JLabel();
        txt_inprogress = new javax.swing.JLabel();
        txt_close = new javax.swing.JLabel();
        txt_total_myissue = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        edt_cari = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        JT_Dept = new javax.swing.JTable();
        jpanel_Dept = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        txt_outstandingDept = new javax.swing.JLabel();
        txt_inprogressDept = new javax.swing.JLabel();
        txt_closeDept = new javax.swing.JLabel();
        txt_sum_Dept = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        all_resume = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        txt_all_close = new javax.swing.JLabel();
        txt_all_inprogress = new javax.swing.JLabel();
        txt_all_outstanding = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPane_Status_Ticket = new javax.swing.JPanel();
        jPane3D = new javax.swing.JPanel();
        lineDate = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        edt_Cari_all = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtHelpDesk = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenu16 = new javax.swing.JMenu();
        jMenu17 = new javax.swing.JMenu();
        jMenu19 = new javax.swing.JMenu();
        jMenu23 = new javax.swing.JMenu();
        jMenu20 = new javax.swing.JMenu();
        jMenu24 = new javax.swing.JMenu();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem41 = new javax.swing.JMenuItem();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem43 = new javax.swing.JMenuItem();
        jMenuItem44 = new javax.swing.JMenuItem();
        jMenuItem45 = new javax.swing.JMenuItem();
        jMenuItem46 = new javax.swing.JMenuItem();
        jMenuItem47 = new javax.swing.JMenuItem();
        jMenu25 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenu21 = new javax.swing.JMenu();
        jMenu22 = new javax.swing.JMenu();
        jMenu26 = new javax.swing.JMenu();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem48 = new javax.swing.JMenuItem();
        jMenuItem53 = new javax.swing.JMenuItem();
        jMenuItem54 = new javax.swing.JMenuItem();
        jMenuItem55 = new javax.swing.JMenuItem();
        jMenu27 = new javax.swing.JMenu();
        jMenuItem52 = new javax.swing.JMenuItem();
        jMenuItem51 = new javax.swing.JMenuItem();
        jMenu18 = new javax.swing.JMenu();

        jMenu4.setText("File");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("Edit");
        jMenuBar2.add(jMenu5);

        jMenu11.setText("File");
        jMenuBar3.add(jMenu11);

        jMenu12.setText("Edit");
        jMenuBar3.add(jMenu12);

        jMenuItem13.setText("jMenuItem13");

        jMenu13.setText("jMenu13");

        jMenuItem49.setText("jMenuItem49");

        jMenuItem50.setText("jMenuItem50");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Wellcome Portal STJM Desktop");

        jLabel2.setText("Nama");

        id_nama.setText("jLabel3");

        jLabel4.setText("Email");

        id_email.setText("id_nama");

        jLabel5.setText("Nik");

        id_nik.setText("id_nama");

        jLabel19.setText("Dept");

        txt_Dept.setText("dept");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(129, 129, 129))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_nama, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_nik, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(id_email, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_Dept))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(id_nama)
                        .addComponent(jLabel4)
                        .addComponent(id_email)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(id_nik)
                    .addComponent(jLabel19)
                    .addComponent(txt_Dept))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jp_logo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jp_logoLayout = new javax.swing.GroupLayout(jp_logo);
        jp_logo.setLayout(jp_logoLayout);
        jp_logoLayout.setHorizontalGroup(
            jp_logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txt_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jp_logoLayout.setVerticalGroup(
            jp_logoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txt_icon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Create Incident", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18))); // NOI18N

        jLabel3.setText("Subject");

        edt_judul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_judulActionPerformed(evt);
            }
        });

        jLabel7.setText("System Type :");

        JC_SystemType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "POS", "HRIS", "SAP", "PORTAL", "OTHER" }));
        JC_SystemType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JC_SystemTypeActionPerformed(evt);
            }
        });

        jLabel8.setText("Isi Pesan :");

        txt_pesan.setColumns(20);
        txt_pesan.setRows(5);
        txt_pesan.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txt_pesan);

        jLabel9.setText("Pilih File :");

        edt_file.setEditable(false);
        edt_file.setEnabled(false);
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

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("ImageView"));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addContainerGap())
        );

        btn_simpan.setText("Simpan");
        btn_simpan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_simpanMouseClicked(evt);
            }
        });
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });

        btn_refresh.setText("Refresh");
        btn_refresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_refreshMouseClicked(evt);
            }
        });

        JC_Asigment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JC_AsigmentItemStateChanged(evt);
            }
        });
        JC_Asigment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JC_AsigmentActionPerformed(evt);
            }
        });

        JC_Status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "High", "Middle", "Low" }));
        JC_Status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JC_StatusActionPerformed(evt);
            }
        });

        jLabel12.setText("Request Status");

        jLabel17.setText("Assign To");

        jLabel18.setText("Due Date");

        jLabel11.setText("Assign Dept");

        jButton2.setText("Load");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(338, 338, 338))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(JC_Status, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Due_Date, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(JC_SystemType, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(edt_judul, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(121, 121, 121)
                                .addComponent(JC_Name, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(JC_Asigment, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btn_refresh)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(edt_file, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(edt_judul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JC_Asigment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JC_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(Due_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JC_SystemType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JC_Status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edt_file, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_simpan)
                    .addComponent(btn_refresh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        JC_MyRequest.setModel(new javax.swing.table.DefaultTableModel(
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
        JC_MyRequest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JC_MyRequestMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(JC_MyRequest);

        edt_cari_my_request.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_cari_my_requestActionPerformed(evt);
            }
        });

        btn_cari_myrequest.setText("CARI");
        btn_cari_myrequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cari_myrequestActionPerformed(evt);
            }
        });

        btn_load.setText("Refresh");
        btn_load.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_loadMouseClicked(evt);
            }
        });

        jpanel_MyResume.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpanel_MyResume.setPreferredSize(new java.awt.Dimension(242, 91));
        jpanel_MyResume.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpanel_MyResumeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpanel_MyResumeLayout = new javax.swing.GroupLayout(jpanel_MyResume);
        jpanel_MyResume.setLayout(jpanel_MyResumeLayout);
        jpanel_MyResumeLayout.setHorizontalGroup(
            jpanel_MyResumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jpanel_MyResumeLayout.setVerticalGroup(
            jpanel_MyResumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 87, Short.MAX_VALUE)
        );

        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txt_outstanding.setText("Out Standing My Issue");

        txt_inprogress.setText("In progress My Issue");

        txt_close.setText("Close My Issue");

        txt_total_myissue.setText("Total My Issue");
        txt_total_myissue.setToolTipText("");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_outstanding, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .addComponent(txt_inprogress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_close, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_total_myissue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(txt_total_myissue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_close)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_inprogress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_outstanding))
        );

        javax.swing.GroupLayout JPanel_MyRequestLayout = new javax.swing.GroupLayout(JPanel_MyRequest);
        JPanel_MyRequest.setLayout(JPanel_MyRequestLayout);
        JPanel_MyRequestLayout.setHorizontalGroup(
            JPanel_MyRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanel_MyRequestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanel_MyRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanel_MyRequestLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JPanel_MyRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jpanel_MyResume, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(JPanel_MyRequestLayout.createSequentialGroup()
                        .addComponent(edt_cari_my_request, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cari_myrequest, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_load)))
                .addContainerGap())
        );
        JPanel_MyRequestLayout.setVerticalGroup(
            JPanel_MyRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanel_MyRequestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JPanel_MyRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edt_cari_my_request, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cari_myrequest)
                    .addComponent(btn_load))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanel_MyRequestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JPanel_MyRequestLayout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpanel_MyResume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("My Request", JPanel_MyRequest);

        jButton1.setText("CARI");

        JT_Dept.setModel(new javax.swing.table.DefaultTableModel(
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
        JT_Dept.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JT_DeptMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(JT_Dept);

        jpanel_Dept.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jpanel_Dept.setPreferredSize(new java.awt.Dimension(242, 80));
        jpanel_Dept.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jpanel_DeptMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jpanel_DeptLayout = new javax.swing.GroupLayout(jpanel_Dept);
        jpanel_Dept.setLayout(jpanel_DeptLayout);
        jpanel_DeptLayout.setHorizontalGroup(
            jpanel_DeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );
        jpanel_DeptLayout.setVerticalGroup(
            jpanel_DeptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 188, Short.MAX_VALUE)
        );

        jPanel11.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txt_outstandingDept.setText("Out Standing Dept");

        txt_inprogressDept.setText("In progress Dept");

        txt_closeDept.setText("Close Dept");

        txt_sum_Dept.setText("Total Dept Issue");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_outstandingDept, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .addComponent(txt_inprogressDept, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_closeDept, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_sum_Dept, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(txt_sum_Dept)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_closeDept)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_inprogressDept)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_outstandingDept))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jpanel_Dept, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(edt_cari, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(109, 109, 109)
                .addComponent(jpanel_Dept, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(287, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(52, 52, 52)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(52, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Dept Request", jPanel7);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("My Resume"));

        all_resume.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout all_resumeLayout = new javax.swing.GroupLayout(all_resume);
        all_resume.setLayout(all_resumeLayout);
        all_resumeLayout.setHorizontalGroup(
            all_resumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 127, Short.MAX_VALUE)
        );
        all_resumeLayout.setVerticalGroup(
            all_resumeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Count Summary"));

        txt_all_close.setText("Close");

        txt_all_inprogress.setText("In Progress");

        txt_all_outstanding.setText("Out Standing");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_all_inprogress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_all_close, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_all_outstanding, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(txt_all_close)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_all_inprogress)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_all_outstanding))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Count Status Ticket"));

        jLabel13.setText("Middle");

        jLabel14.setText("Low");

        jLabel15.setText("Hight");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel15))
        );

        jPane_Status_Ticket.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPane_Status_TicketLayout = new javax.swing.GroupLayout(jPane_Status_Ticket);
        jPane_Status_Ticket.setLayout(jPane_Status_TicketLayout);
        jPane_Status_TicketLayout.setHorizontalGroup(
            jPane_Status_TicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
        );
        jPane_Status_TicketLayout.setVerticalGroup(
            jPane_Status_TicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPane3D.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPane3DLayout = new javax.swing.GroupLayout(jPane3D);
        jPane3D.setLayout(jPane3DLayout);
        jPane3DLayout.setHorizontalGroup(
            jPane3DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPane3DLayout.setVerticalGroup(
            jPane3DLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 385, Short.MAX_VALUE)
        );

        lineDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout lineDateLayout = new javax.swing.GroupLayout(lineDate);
        lineDate.setLayout(lineDateLayout);
        lineDateLayout.setHorizontalGroup(
            lineDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        lineDateLayout.setVerticalGroup(
            lineDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(all_resume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPane3D, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPane_Status_Ticket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lineDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(all_resume, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPane_Status_Ticket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPane3D, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lineDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Dashboart", jPanel8);

        edt_Cari_all.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edt_Cari_allActionPerformed(evt);
            }
        });
        edt_Cari_all.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                edt_Cari_allKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                edt_Cari_allKeyTyped(evt);
            }
        });

        jtHelpDesk.setModel(new javax.swing.table.DefaultTableModel(
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
        jtHelpDesk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtHelpDeskMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtHelpDesk);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                    .addComponent(edt_Cari_all))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(edt_Cari_all, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Helpdesk", jPanel4);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        jMenu1.setText("File");

        jMenuItem1.setText("New File");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Wapro");

        jMenu3.setText("Transaksi");

        jMenuItem2.setText("Payment Request Form");
        jMenu3.add(jMenuItem2);

        jMenuItem3.setText("Advanced Settllement Form");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuItem4.setText("Kwitansi Online");
        jMenu3.add(jMenuItem4);

        jMenuItem5.setText("Memo Files");
        jMenu3.add(jMenuItem5);

        jMenuItem6.setText("E-Ticketing Files");
        jMenu3.add(jMenuItem6);

        jMenu2.add(jMenu3);

        jMenu6.setText("Master Form");

        jMenuItem14.setText("Aset Master Form");
        jMenu6.add(jMenuItem14);

        jMenuItem15.setText("Material Master Form");
        jMenu6.add(jMenuItem15);

        jMenuItem16.setText("SPPB Form");
        jMenu6.add(jMenuItem16);

        jMenuItem17.setText("Form Gallery");
        jMenu6.add(jMenuItem17);

        jMenu2.add(jMenu6);

        jMenu7.setText("New Open Outlet");

        jMenuItem18.setText("Dist / Agent Outlet");
        jMenu7.add(jMenuItem18);

        jMenu2.add(jMenu7);

        jMenu8.setText("Contract");

        jMenuItem19.setText("Contract Padimas");
        jMenu8.add(jMenuItem19);

        jMenuItem20.setText("Addendium Paddimas");
        jMenu8.add(jMenuItem20);

        jMenuItem21.setText("Contract Garmelia");
        jMenu8.add(jMenuItem21);

        jMenuItem22.setText("Addedium Garmelia");
        jMenu8.add(jMenuItem22);

        jMenu2.add(jMenu8);

        jMenu9.setText("Master Data");

        jMenuItem23.setText("Data Department");
        jMenu9.add(jMenuItem23);

        jMenuItem24.setText("Data Assets");
        jMenu9.add(jMenuItem24);

        jMenuItem25.setText("Data Contract");
        jMenu9.add(jMenuItem25);

        jMenuItem26.setText("Data Others");
        jMenu9.add(jMenuItem26);

        jMenu2.add(jMenu9);

        jMenu10.setText("Syspro Data");

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("Manage Content");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem8);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setText("Faq");
        jMenu10.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setText("FeedBack");
        jMenu10.add(jMenuItem10);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem11.setText("Form Gallery");
        jMenu10.add(jMenuItem11);

        jMenu2.add(jMenu10);

        jMenu15.setText("Users Administrator");

        jMenuItem27.setText("Register Users");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem27);

        jMenuItem28.setText("Roles");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem28);

        jMenuItem29.setText("Permisions");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem29);

        jMenuItem30.setText("Users & Roles");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem30);

        jMenuItem31.setText("Menus");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem31);

        jMenuItem32.setText("Profil Users");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem32);

        jMenu2.add(jMenu15);

        jMenuBar1.add(jMenu2);

        jMenu14.setText("Syspro");
        jMenuBar1.add(jMenu14);
        jMenuBar1.add(jMenu16);

        jMenu17.setText("Back Office");

        jMenu19.setText("Promo");

        jMenu23.setText("jMenu23");
        jMenu19.add(jMenu23);

        jMenu17.add(jMenu19);

        jMenu20.setText("Transaksi");

        jMenu24.setText("Pos");

        jMenuItem37.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem37.setText("Taking Order");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu24.add(jMenuItem37);

        jMenuItem38.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem38.setText("Adjustment");
        jMenu24.add(jMenuItem38);

        jMenuItem39.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem39.setText("Cancel Adjustment");
        jMenu24.add(jMenuItem39);

        jMenuItem40.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem40.setText("Transfer To Other Store");
        jMenu24.add(jMenuItem40);

        jMenuItem41.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem41.setText("Transfer Confirmasi");
        jMenu24.add(jMenuItem41);

        jMenuItem42.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem42.setText("GR Other");
        jMenu24.add(jMenuItem42);

        jMenuItem43.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem43.setText("Cancel GR Other");
        jMenu24.add(jMenuItem43);

        jMenuItem44.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem44.setText("Tp To HO");
        jMenu24.add(jMenuItem44);

        jMenuItem45.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem45.setText("Cancel Tp TO HO");
        jMenu24.add(jMenuItem45);

        jMenuItem46.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem46.setText("Tp Confirm From HO");
        jMenu24.add(jMenuItem46);

        jMenuItem47.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem47.setText("Cancel Tp Confirmasi");
        jMenu24.add(jMenuItem47);

        jMenu20.add(jMenu24);

        jMenu25.setText("Consigment");

        jMenuItem7.setText("GR Consigment Slip");
        jMenu25.add(jMenuItem7);

        jMenuItem12.setText("Cancel Consigment Slip ");
        jMenu25.add(jMenuItem12);

        jMenuItem33.setText("PO Consigment");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu25.add(jMenuItem33);

        jMenuItem34.setText("PI Consigment Slip");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu25.add(jMenuItem34);

        jMenuItem35.setText("Consigment Slip");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu25.add(jMenuItem35);

        jMenu20.add(jMenu25);

        jMenu17.add(jMenu20);

        jMenu21.setText("Report");
        jMenu17.add(jMenu21);

        jMenu22.setText("Chart");
        jMenu17.add(jMenu22);

        jMenu26.setText("Other");

        jMenuItem36.setText("Consignment Order Customer ");
        jMenuItem36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem36MouseClicked(evt);
            }
        });
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu26.add(jMenuItem36);

        jMenuItem48.setText("Payment Consigment Order Customer");
        jMenuItem48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem48ActionPerformed(evt);
            }
        });
        jMenu26.add(jMenuItem48);

        jMenuItem53.setText("Customer Address");
        jMenuItem53.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem53MouseClicked(evt);
            }
        });
        jMenuItem53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem53ActionPerformed(evt);
            }
        });
        jMenu26.add(jMenuItem53);

        jMenuItem54.setText("Add Material Other Retail By Procedure");
        jMenuItem54.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem54MouseClicked(evt);
            }
        });
        jMenuItem54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem54ActionPerformed(evt);
            }
        });
        jMenu26.add(jMenuItem54);

        jMenuItem55.setText("Asset Management");
        jMenuItem55.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem55MouseClicked(evt);
            }
        });
        jMenuItem55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem55ActionPerformed(evt);
            }
        });
        jMenu26.add(jMenuItem55);

        jMenu17.add(jMenu26);

        jMenuBar1.add(jMenu17);

        jMenu27.setText("Admin");

        jMenuItem52.setText("USERS");
        jMenu27.add(jMenuItem52);

        jMenuItem51.setText("ROLE AUTO");
        jMenu27.add(jMenuItem51);

        jMenuBar1.add(jMenu27);

        jMenu18.setText("Logout");
        jMenu18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu18MouseClicked(evt);
            }
        });
        jMenu18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu18ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu18);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jp_logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jp_logo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:

        hellper_Portal = new Hellper_Portal();
        if (hellper_Portal.AutoUsers(_Username, "manage_content") == 1) {
            JFrame_ManageContent jFrame_ManageContent = new JFrame_ManageContent(_Username);
            jFrame_ManageContent.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Mohon Maaf, Tidak Ada Akses");
        }

    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenu18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu18ActionPerformed
        // TODO add your handling code here:
        JFrame_Login frame_Login = new JFrame_Login();
        frame_Login.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jMenu18ActionPerformed

    private void jMenu18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu18MouseClicked
        // TODO add your handling code here:
        JFrame_Login frame_Login = new JFrame_Login();
        frame_Login.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jMenu18MouseClicked

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
        // TODO add your handling code here:
//        JFrame_TakingOrder jFrame_TakingOrder = new JFrame_TakingOrder();
//        jFrame_TakingOrder.setVisible(true);
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    private void jMenuItem36MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem36MouseClicked
//        JFrame_TakingOrder jFrame_TakingOrder = new JFrame_TakingOrder();
//        jFrame_TakingOrder.setVisible(true); 209
    }//GEN-LAST:event_jMenuItem36MouseClicked

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed

        hellper_Portal = new Hellper_Portal();
        if (hellper_Portal.AutoUsers(_Username, "consigment") == 1) {
            JFrame_Consigment jFrame_TakingOrder = new JFrame_Consigment(_Username);
            jFrame_TakingOrder.setVisible(true);
            jFrame_TakingOrder.setExtendedState(JFrame_Consigment.MAXIMIZED_BOTH);
        } else {
            JOptionPane.showMessageDialog(null, "Mohon Maaf, Tidak Ada Akses");
        }

    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem48ActionPerformed

        if (hellper_Portal.AutoUsers(_Username, "paymentconsigment") == 1) {
            JFrame_PaymentConsigment jFrame_PaymentConsigment = new JFrame_PaymentConsigment(null, 10);
            jFrame_PaymentConsigment.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Mohon Maaf, Tidak Ada Akses");
        }

    }//GEN-LAST:event_jMenuItem48ActionPerformed

    private void jMenuItem53MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem53MouseClicked

    }//GEN-LAST:event_jMenuItem53MouseClicked

    private void jMenuItem53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem53ActionPerformed
        if (hellper_Portal.AutoUsers(_Username, "address") == 1) {
            JFrame_Address jFrame_Address = new JFrame_Address();
            jFrame_Address.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Mohon Maaf, Tidak Ada Akses");
        }
    }//GEN-LAST:event_jMenuItem53ActionPerformed

    private void jMenuItem54MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem54MouseClicked

        JFrame_AddMaterial jFrame_AddMaterial = new JFrame_AddMaterial();
        jFrame_AddMaterial.setVisible(true);

    }//GEN-LAST:event_jMenuItem54MouseClicked

    private void jMenuItem54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem54ActionPerformed
        JFrame_AddMaterial jFrame_AddMaterial = new JFrame_AddMaterial();
        jFrame_AddMaterial.setVisible(true);
    }//GEN-LAST:event_jMenuItem54ActionPerformed

    private void edt_fileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_edt_fileMouseClicked

    }//GEN-LAST:event_edt_fileMouseClicked


    private void btn_simpanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_simpanMouseClicked
        try {

            String namesAssign = hellper_Portal.GetNik(JC_Name.getSelectedItem());

            sql = "INSERT INTO [dbo].[ticket_master]\n"
                    + "           ([ticket_no]\n"
                    + "           ,[judul_ticket]\n"
                    + "           ,[tgl_ticket]\n"
                    + "           ,[due_date]\n"
                    + "           ,[asign_dept_ticket]\n"
                    + "           ,[asign_name_ticket]\n"
                    + "           ,[system_type]\n"
                    + "           ,[status]\n"
                    + "           ,[isi_pesan]\n"
                    + "           ,[image], [nik], [status_pengerjaan])\n"
                    + "     VALUES\n"
                    + "           ('" + hellper_Portal.getIDHelpDesk() + "','" + edt_judul.getText() + "',GETDATE(),'" + sdf.format(Due_Date.getSelectedDate().getTime()) + "','" + JC_Asigment.getSelectedItem().toString() + "','" + namesAssign + "','" + JC_SystemType.getSelectedItem() + "','" + JC_Status.getSelectedItem().toString() + "','" + txt_pesan.getText() + "','" + encodeFileToBase64Binary(f) + "','" + _Username + "','Diskusi')";
            connection = C_Connection.getConnection();
            ps = connection.prepareStatement(sql);
            ps.execute();

            sql = "INSERT INTO [dbo].[tbl_portals]\n"
                    + "           ([image]\n"
                    + "           ,[icon]\n"
                    + "           ,[title])\n"
                    + "     VALUES\n"
                    + "           ('" + encodeFileToBase64Binary(f) + "',NULL,'ICON PORTAL')";
            connection = C_Connection.getConnection();
            ps = connection.prepareStatement(sql);
            ps.execute();

            JOptionPane.showMessageDialog(null, "Terima Kasih Permintaan Akan Kami Proses");
            getDataTable(0);
            getDataMyRequest(0);
            RefreshGet();

        } catch (HeadlessException | SQLException e) {
        }
    }//GEN-LAST:event_btn_simpanMouseClicked

    private static String encodeFileToBase64Binary(File file) {

        String UrlImage64 = null;
        try {

            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            UrlImage64 = new String(Base64.encodeBase64(bytes), "UTF-8");

        } catch (IOException e) {
        }
        return UrlImage64;
    }

    private void getDataTable(int nullNumber) {

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Ticket Nomor");
        model.addColumn("Judul Ticket");
        model.addColumn("Tanggal Ticket Date");
        model.addColumn("Due Date");
        model.addColumn("Assign Dept");
        model.addColumn("Assign Name");
        model.addColumn("System Type");
        model.addColumn("Status");
        model.addColumn("Isi Pesan");
        model.addColumn("Nik");
        model.addColumn("Status Pengerjaan");

        try {

            if (nullNumber == 0) {
                sql = "SELECT [ticket_no]\n"
                        + "      ,[judul_ticket]\n"
                        + "      ,[tgl_ticket]\n"
                        + "      ,[due_date]\n"
                        + "      ,[asign_dept_ticket]\n"
                        + "      ,[asign_name_ticket]\n"
                        + "      ,[system_type]\n"
                        + "      ,[status]\n"
                        + "      ,[isi_pesan]\n"
                        + "      ,[nik]\n"
                        + "      ,[status_pengerjaan]\n"
                        + "  FROM [dbo].[ticket_master]";
            } else {
                sql = "SELECT [ticket_no]\n"
                        + "      ,[judul_ticket]\n"
                        + "      ,[tgl_ticket]\n"
                        + "      ,[due_date]\n"
                        + "      ,[asign_dept_ticket]\n"
                        + "      ,[asign_name_ticket]\n"
                        + "      ,[system_type]\n"
                        + "      ,[status]\n"
                        + "      ,[isi_pesan]\n"
                        + "      ,[nik]\n"
                        + "      ,[status_pengerjaan]\n"
                        + "  FROM [dbo].[ticket_master]";

            }

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[]{res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8), hellper_Portal.Progress(res.getString(9)), res.getString(10), res.getString(11)});
            }

            res.close();
            stt.close();
            jtHelpDesk.setModel(model);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getDataName() {

        try {
            sql = "select users.name, organization.name \n"
                    + "from \n"
                    + "users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept \n"
                    + "JOIN organization ON organization.id = subOrganization.id_organization ";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                JC_Name.addItem(res.getString(1));
            }

        } catch (SQLException e) {
        }

    }

    private void getDataMyRequest(int nullNumber) {

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Ticket Nomor");
        model.addColumn("Ticket From");
        model.addColumn("Judul ticket");
        model.addColumn("Tanggal ticket");
        model.addColumn("Due Date");
        model.addColumn("Assign Dept");
        model.addColumn("Assign Name");
        model.addColumn("System Type");
        model.addColumn("Status");
        model.addColumn("Isi Pesan");
        model.addColumn("Status Pekerjaan");

        try {
            if (nullNumber == 0) {

                sql = "SELECT [ticket_no]\n"
                        + "      ,(SELECT name FROM users WHERE username = [nik] )\n"
                        + "      ,[judul_ticket]\n"
                        + "      ,[tgl_ticket]\n"
                        + "      ,[due_date]\n"
                        + "      ,[asign_dept_ticket]\n"
                        + "      , (SELECT name FROM users WHERE username = [asign_name_ticket] )\n"
                        + "      ,[system_type]\n"
                        + "      ,[status]\n"
                        + "      ,[isi_pesan]\n"
                        + "      ,[status_pengerjaan]\n"
                        + "  FROM [dbo].[ticket_master] WHERE nik = '" + _Username + "' OR asign_name_ticket = '" + _Username + "'";

            } else {
                sql = "SELECT [ticket_no]\n"
                        + "      , (SELECT name FROM users WHERE username = [nik] ) \n"
                        + "      ,[judul_ticket]\n"
                        + "      ,[tgl_ticket]\n"
                        + "      ,[due_date]\n"
                        + "      ,[asign_dept_ticket]\n"
                        + "      , (SELECT name FROM users WHERE username = [asign_name_ticket] )\n"
                        + "      ,[system_type]\n"
                        + "      ,[status]\n"
                        + "      ,[isi_pesan]\n"
                        + "      ,[status_pengerjaan]\n"
                        + "  FROM [dbo].[ticket_master] WHERE nik = '" + _Username + "' OR asign_name_ticket = '" + _Username + "' AND A.ticket_no = '" + edt_cari_my_request.getText() + "'";
            }

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[]{res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10), hellper_Portal.Progress(res.getString(11))});
            }

            res.close();
            stt.close();
            getNewRenderedTable(JC_MyRequest);
            JC_MyRequest.setModel(model);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static JTable getNewRenderedTable(final JTable table) {
        table.setDefaultRenderer(Object.class,
                new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String Doc_ = (String) table.getModel().getValueAt(row, 10);

                switch (Doc_) {
                    case "Inprogress":
                        setBackground(table.getBackground());
                        setForeground(table.getForeground());
                        break;
                    case "Closed Task":
                        setBackground(table.getBackground());
                        setForeground(Color.magenta);
                        break;
                    default:
                        setBackground(table.getBackground());
                        setForeground(table.getForeground());
                        break;
                }

                return this;
            }
        });
        return table;
    }

    private void RefreshGet() {
        edt_judul.setText("");
        txt_pesan.setText("");
        edt_file.setText("");
        jLabel10.setIcon(null);
    }

    private void btn_refreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_refreshMouseClicked
        RefreshGet();
    }//GEN-LAST:event_btn_refreshMouseClicked

    private void JC_SystemTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JC_SystemTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JC_SystemTypeActionPerformed

    private void JC_AsigmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JC_AsigmentActionPerformed

    }//GEN-LAST:event_JC_AsigmentActionPerformed

    private void JC_StatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JC_StatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JC_StatusActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void edt_judulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_judulActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_judulActionPerformed

    private void edt_fileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_fileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_fileActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("*.images", "jpg", "png");
        jFileChooser.addChoosableFileFilter(extensionFilter);
        int result = jFileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            f = jFileChooser.getSelectedFile();
            edt_file.setText(f.getAbsolutePath());
            jLabel10.setIcon(ResizeImage(f.getAbsolutePath(), null));

        } else {
            edt_file.setText("Tida Ada Image...");
        }


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jtHelpDeskMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtHelpDeskMouseClicked

        int baris = jtHelpDesk.rowAtPoint(evt.getPoint());
        JFrame_HelpDesk_Detail jFrame_HelpDesk_Detail = new JFrame_HelpDesk_Detail(jtHelpDesk.getValueAt(baris, 0).toString(), _Username);
        jFrame_HelpDesk_Detail.setVisible(true);
    }//GEN-LAST:event_jtHelpDeskMouseClicked

    private void edt_Cari_allKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edt_Cari_allKeyTyped

        // TODO add your handling code here:
    }//GEN-LAST:event_edt_Cari_allKeyTyped

    private void edt_Cari_allKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_edt_Cari_allKeyPressed

    }//GEN-LAST:event_edt_Cari_allKeyPressed

    private void edt_Cari_allActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_Cari_allActionPerformed

        if (edt_Cari_all.getText().isEmpty()) {
            getDataTable(0);
        } else {
            getDataTable(1);
        }
    }//GEN-LAST:event_edt_Cari_allActionPerformed

    private void btn_loadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_loadMouseClicked
        getDataTable(0);
        getDept();
        getDataMyRequest(0);
    }//GEN-LAST:event_btn_loadMouseClicked

    private void btn_cari_myrequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cari_myrequestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cari_myrequestActionPerformed

    private void edt_cari_my_requestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edt_cari_my_requestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edt_cari_my_requestActionPerformed

    private void JC_MyRequestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JC_MyRequestMouseClicked
        int baris = JC_MyRequest.rowAtPoint(evt.getPoint());
        JFrame_HelpDesk_Detail jFrame_HelpDesk_Detail = new JFrame_HelpDesk_Detail(JC_MyRequest.getValueAt(baris, 0).toString(), _Username);
        jFrame_HelpDesk_Detail.setVisible(true);
    }//GEN-LAST:event_JC_MyRequestMouseClicked

    private void jpanel_MyResumeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpanel_MyResumeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jpanel_MyResumeMouseClicked

    private void JT_DeptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JT_DeptMouseClicked
        int baris = JT_Dept.rowAtPoint(evt.getPoint());
        JFrame_HelpDesk_Detail jFrame_HelpDesk_Detail = new JFrame_HelpDesk_Detail(JT_Dept.getValueAt(baris, 0).toString(), _Username);
        jFrame_HelpDesk_Detail.setVisible(true);
    }//GEN-LAST:event_JT_DeptMouseClicked

    private void jpanel_DeptMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpanel_DeptMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jpanel_DeptMouseClicked

    private void JC_AsigmentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JC_AsigmentItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_JC_AsigmentItemStateChanged

    private void jMenuItem55MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem55MouseClicked
//        JFrame_AssetManagement jFrame_AssetManagement = new JFrame_AssetManagement();
//        jFrame_AssetManagement.setVisible(true);
    }//GEN-LAST:event_jMenuItem55MouseClicked

    private void jMenuItem55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem55ActionPerformed
        JFrame_AssetManagement jFrame_AssetManagement = new JFrame_AssetManagement();
        jFrame_AssetManagement.setVisible(true);
    }//GEN-LAST:event_jMenuItem55ActionPerformed

    public ImageIcon ResizeImage(String ImagePath, byte[] pic) {
        ImageIcon myImage = null;
        if (ImagePath != null) {
            myImage = new ImageIcon(ImagePath);
        } else {
            myImage = new ImageIcon(pic);
        }
        Image img = myImage.getImage();
        Image newImage = img.getScaledInstance(jLabel10.getWidth(), jLabel10.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImage);
        return image;
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
            java.util.logging.Logger.getLogger(JFrame_Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrame_Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrame_Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrame_Home.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new JFrame_Home().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datechooser.beans.DateChooserCombo Due_Date;
    private javax.swing.JComboBox<String> JC_Asigment;
    private javax.swing.JTable JC_MyRequest;
    private javax.swing.JComboBox<String> JC_Name;
    private javax.swing.JComboBox<String> JC_Status;
    private javax.swing.JComboBox<String> JC_SystemType;
    private javax.swing.JPanel JPanel_MyRequest;
    private javax.swing.JTable JT_Dept;
    private javax.swing.JPanel all_resume;
    private javax.swing.JButton btn_cari_myrequest;
    private javax.swing.JButton btn_load;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JButton btn_simpan;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField edt_Cari_all;
    private javax.swing.JTextField edt_cari;
    private javax.swing.JTextField edt_cari_my_request;
    private javax.swing.JTextField edt_file;
    private javax.swing.JTextField edt_judul;
    private javax.swing.JLabel id_email;
    private javax.swing.JLabel id_nama;
    private javax.swing.JLabel id_nik;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu17;
    private javax.swing.JMenu jMenu18;
    private javax.swing.JMenu jMenu19;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu20;
    private javax.swing.JMenu jMenu21;
    private javax.swing.JMenu jMenu22;
    private javax.swing.JMenu jMenu23;
    private javax.swing.JMenu jMenu24;
    private javax.swing.JMenu jMenu25;
    private javax.swing.JMenu jMenu26;
    private javax.swing.JMenu jMenu27;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem41;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem43;
    private javax.swing.JMenuItem jMenuItem44;
    private javax.swing.JMenuItem jMenuItem45;
    private javax.swing.JMenuItem jMenuItem46;
    private javax.swing.JMenuItem jMenuItem47;
    private javax.swing.JMenuItem jMenuItem48;
    private javax.swing.JMenuItem jMenuItem49;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem50;
    private javax.swing.JMenuItem jMenuItem51;
    private javax.swing.JMenuItem jMenuItem52;
    private javax.swing.JMenuItem jMenuItem53;
    private javax.swing.JMenuItem jMenuItem54;
    private javax.swing.JMenuItem jMenuItem55;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPane3D;
    private javax.swing.JPanel jPane_Status_Ticket;
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
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel jp_logo;
    private javax.swing.JPanel jpanel_Dept;
    private javax.swing.JPanel jpanel_MyResume;
    private javax.swing.JTable jtHelpDesk;
    private javax.swing.JPanel lineDate;
    private javax.swing.JLabel txt_Dept;
    private javax.swing.JLabel txt_all_close;
    private javax.swing.JLabel txt_all_inprogress;
    private javax.swing.JLabel txt_all_outstanding;
    private javax.swing.JLabel txt_close;
    private javax.swing.JLabel txt_closeDept;
    private javax.swing.JLabel txt_icon;
    private javax.swing.JLabel txt_inprogress;
    private javax.swing.JLabel txt_inprogressDept;
    private javax.swing.JLabel txt_outstanding;
    private javax.swing.JLabel txt_outstandingDept;
    private javax.swing.JTextArea txt_pesan;
    private javax.swing.JLabel txt_sum_Dept;
    private javax.swing.JLabel txt_total_myissue;
    // End of variables declaration//GEN-END:variables

}
