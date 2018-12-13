/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hellper;

import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author PCIT1
 */
public class Hellper_Portal {

    Connection connection;
    Statement stt;
    String sql;
    ResultSet res;
    PreparedStatement ps = null;

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
        public ImageIcon ResizeImage(String ImagePath, byte[] pic, JLabel jLabel) {
        ImageIcon myImage = null;
        if (ImagePath != null) {
            myImage = new ImageIcon(ImagePath);
        } else {
            myImage = new ImageIcon(pic);
        }
        Image img = myImage.getImage();
        Image newImage = img.getScaledInstance(jLabel.getWidth(), jLabel.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImage);
        return image;

    }

    public int SetHarga(String A, String B) {
        int Total = 0;
        return Total = Integer.parseInt(A) * Integer.parseInt(B);
    }

    public int CheckAddressUser(String NameUser) {
        int Hasil = 0;
        try {
            sql = "SELECT count(*) as total FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_user] WHERE nama_cust = '" + NameUser + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Hasil;
    }

    public int checkLine(String A, String B, String C) {
        int Hasil = 0;

        try {
            sql = "select MAX(itemline) as item from [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] where doc_id = '" + A + "' and plant_id = '" + B + "' "
                    + "and fiscalyear = '" + C + "'";

//            System.out.println(sql);
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                if (res.getString(1) == null) {
                    Hasil = 2;
                } else {
                    Hasil = Integer.parseInt(res.getString(1)) + 1;
                }
            }
            res.close();
            stt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Hasil;
    }

    public int TotalNett(String Doc) {

        int Hasil = 0;
        try {

            sql = "SELECT sum(qty_pesan * harga - disc_value) as nett_value FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id = '" + Doc + "'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }

            res.close();
            stt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Hasil;

    }

    public int TotalDiscount(String Doc) {

        int Hasil = 0;
        try {

            sql = "SELECT SUM(disc_value) as jumlah FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id = '" + Doc + "';";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }

            res.close();
            stt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Hasil;

    }

    public int TotalOrder(String Doc) {

        int Hasil = 0;
        try {

            sql = "SELECT sum(qty_pesan * harga) as nett_value  FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id = '" + Doc + "'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }

            res.close();
            stt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Hasil;

    }

    public int TotalQty(String Doc) {

        int Hasil = 0;
        try {

            sql = "SELECT sum(qty_pesan) as qty_pesan  FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id = '" + Doc + "';";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }

            res.close();
            stt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Hasil;

    }

    public int TotalItem(String Doc) {

        int Hasil = 0;
        try {

            sql = "SELECT count(itemline) as total_items  FROM [192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_detail] WHERE doc_id = '" + Doc + "'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }

            res.close();
            stt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Hasil;

    }

    public String goPhone(String nama) {
        String Hasil = null;
        try {

            sql = "SELECT telp from [192.168.12.12].[dbstore_ho].[dbo].t_takeorder_user where nama_cust = '" + nama + "'";
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

    public String setPayType(String Masuk) {
        String Hasil = null;

        try {
            sql = "SELECT customer_id, customer_name FROM [192.168.12.12].[dbstore_ho].[dbo].[m_customer] WHERE customer_name ='" + Masuk + "'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1);
            }

            res.close();
            stt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Hasil;
    }

    public String checkProduct(String product) {
        String Hasil = null;

        try {

            sql = "SELECT material_id FROM [192.168.12.12].[dbstore_ho].[dbo].[m_material] WHERE material_id = '" + product.substring(0, 7) + "'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1).toString();
            }
            res.close();
            stt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Hasil;
    }

    public int hitung(String A, String B) {
        int Hasil = 0;
        Hasil = Integer.parseInt(A) * Integer.parseInt(B);
        return Hasil;
    }

    public int HitungNet(String A, String B, String C) {
        int Hasil = 0;
        Hasil = Integer.parseInt(A) * Integer.parseInt(B) - Integer.parseInt(C);
        return Hasil;
    }

    public String checkLog(String Log) {
        String Hasil = null;
        try {
            sql = "select count(doc_id) as total from [192.168.12.12].[dbstore_ho].[dbo].t_takeorder_log_header WHERE doc_id ='" + Log + "'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1).toString();
            }
            res.close();
            stt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Hasil;
    }

    public String getIDPayment() {
        String Hasil = null;

        try {
            sql = "SELECT\n"
                    + "max(doc_id)+1 as doc_id\n"
                    + "FROM\n"
                    + "[192.168.12.12].[dbstore_ho].[dbo].t_pos_header\n"
                    + "WHERE\n"
                    + "doc_id like '1%'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                if (res.getString(1) == null) {
                    Hasil = "10000001";
                } else {
                    Hasil = res.getString(1);
                }
            }

            res.close();
            stt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Hasil;
    }

    public int getIDPayment1(String docs) {
        int Hasil = 0;

        try {
            sql = "select count(*) from [192.168.12.12].[dbstore_ho].[dbo].[t_pos_detail] where doc_id like '" + docs + "%'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {

                Hasil = res.getInt(1);

            }

            res.close();
            stt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Hasil;
    }

    public int updateFlag(String Docid) {
        int nilai = 0;

        try {
            sql = "UPDATE \n"
                    + "[192.168.12.12].[dbstore_ho].[dbo].[t_takeorder_header] \n"
                    + "SET flag_proses = 'Y', process_date = 'convert(varchar, getdate(), 120)' \n"
                    + "WHERE doc_id = '" + Docid + "'";

            stt = connection.createStatement();
            int executeUpdate = stt.executeUpdate(sql);
            if (executeUpdate >= 1) {
                nilai = 1;
            } else {
                nilai = 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nilai;
    }

    public int AutoUsers(String Username, String Windows) {
        int Hasil = 0;
        try {
            sql = "SELECT nik, windows, status from AUTO_USERS WHERE nik = '" + Username + "' AND windows ='" + Windows + "'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = Integer.parseInt(res.getString(3));
            }

            res.close();
            stt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Hasil;
    }

}
