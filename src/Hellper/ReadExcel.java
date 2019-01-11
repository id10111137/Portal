/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hellper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author PCIT1
 */
public class ReadExcel {

    private String fileInput;
    Connection connection;
    Statement stt;
    String sql;
    ResultSet res;
    PreparedStatement ps = null;

    public void setInputFile(String fileInputX) {
        fileInput = fileInputX;
    }

    public void ngeBaca() throws IOException, SQLException {

        File fileExcel = new File(fileInput);
        Workbook w;
        try {
            w = Workbook.getWorkbook(fileExcel);

            Sheet sheet = w.getSheet(0);
            Cell[] cell;
            for (int i = 1; i < sheet.getRows(); i++) {

                cell = sheet.getRow(i);
                String aset_no = cell[0].getContents();
                String aset_nama = cell[1].getContents();
                String cpu = cell[2].getContents();
                String ram = cell[3].getContents();
                String hdd = cell[4].getContents();
                String os = cell[5].getContents();
                String ip = cell[6].getContents();
                String status = cell[7].getContents();
                String pc_name = cell[8].getContents();
                String dept = cell[9].getContents();
                String location = cell[10].getContents();

//                System.out.println(aset_no + "  " + aset_nama);
                sql = "INSERT INTO [dbo].[tbl_assetlist]\n"
                        + "           ([asset_nomor]\n"
                        + "           ,[asset_name]\n"
                        + "           ,[cpu]\n"
                        + "           ,[ram]\n"
                        + "           ,[hdd]\n"
                        + "           ,[os]\n"
                        + "           ,[ipaddress]\n"
                        + "           ,[statuspc]\n"
                        + "           ,[computer_name]\n"
                        + "           ,[dept]\n"
                        + "           ,[location])\n"
                        + "     VALUES\n"
                        + "           ('"
                        + "" + aset_no + "',"
                        + "'" + cell[1].getContents() + "',"
                        + "'" + cell[2].getContents() + "',"
                        + "'" + cell[3].getContents() + "',"
                        + "'" + cell[4].getContents() + "',"
                        + "'" + cell[5].getContents() + "',"
                        + "'" + cell[6].getContents() + "',"
                        + "'" + cell[7].getContents() + "',"
                        + "'" + cell[8].getContents() + "',"
                        + "'" + cell[9].getContents() + "',"
                        + "'" + cell[10].getContents() + "')";

                connection = C_Connection.getConnection();
                ps = connection.prepareStatement(sql);
                ps.execute();
            }
        } catch (BiffException | IOException | IndexOutOfBoundsException e) {
        }

    }
}
