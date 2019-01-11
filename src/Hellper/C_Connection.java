/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hellper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author PCIT1
 */
public class C_Connection {

    private static Connection connect;
    private static final String driverName = "net.sourceforge.jtds.jdbc.Driver"; // Driver Untuk Koneksi Ke SQLServer   
    private static final String jdbc = "jdbc:jtds:sqlserver://";
    private static final String host = "192.168.2.227:"; // Host ini Bisa Menggunakan IP Anda, Contoh : 192.168.100.100   
    private static final String port = "1433/"; // Port Default SQLServer   
    private static final String database = "bizportal"; // Ini Database yang akan digunakan   
    private static final String url = jdbc + host + port + database;
    private static final String username = "sa"; // username default SQLServer   
    private static final String password = "P4ssw0rd!";

    public static Connection getConnection() throws SQLException {
        if (connect == null) {
            try {
                Class.forName(driverName);
                try {
                    connect = DriverManager.getConnection(url, username, password);
                } catch (SQLException se) {
                    JOptionPane.showMessageDialog(null, "Periksa Koneksi Internet");
                    System.exit(0);
                }
            } catch (ClassNotFoundException cnfe) {
                System.out.println("Class Driver Tidak Ditemukan, Terjadi Kesalahan Pada : " + cnfe);
                System.exit(0);
            }
        }
        return connect;
    }
}
