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

    public String getNoLabel(String Label) {
        String Hasil = null;
        if (Label == null) {
            Hasil = "01";
        } else {
            Hasil = Label.substring(2, 1);
        }
        return Hasil;
    }

    public String checkDoc(String is_PDF) {
        String Hasil = null;

        if (is_PDF.equals("1")) {
            Hasil = "PDF";
        } else {
            Hasil = "Doc";
        }

        return Hasil;
    }

    public String checkStatus(Object Status) {
        String Hasil = null;

        if (Status.toString().equals("Publish")) {
            Hasil = "1";
        } else {
            Hasil = "0";
        }

        return Hasil;
    }

    public String GetNik(Object Nama) {
        String Hasil = null;
        try {
            sql = "select users.username \n"
                    + "                    from \n"
                    + "                    users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept \n"
                    + "                    JOIN organization ON organization.id = subOrganization.id_organization WHERE users.name = '" + Nama + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1);
            }

            res.close();
            stt.close();

        } catch (SQLException e) {
        }
        return Hasil;
    }

    public String getDept(String Nik) {
        String Hasil = null;

        try {
            sql = "select organization.name \n"
                    + "                    from \n"
                    + "                    users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept \n"
                    + "                    JOIN organization ON organization.id = subOrganization.id_organization WHERE users.username = '" + Nik + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1);
            }

            res.close();
            stt.close();

        } catch (SQLException e) {
        }
        return Hasil;
    }

    public String getIDParent(Object parents) {
        String Hasil = null;

        try {
            sql = "SELECT id from policyandproceduremenu WHERE label = '" + parents.toString() + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1);
            }

            res.close();
            stt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Hasil;
    }

    public String getMenuHeader() {

        String Hasil = null;
        try {
            sql = "	   SELECT\n"
                    + "                   CASE\n"
                    + "                   	WHEN MAX(substring(CONVERT(VARCHAR(10),doc_id,112),1,6))+1 is NULL\n"
                    + "                    THEN substring(CONVERT(VARCHAR(10),GETDATE(),112),3,8)+'01'\n"
                    + "                    else MAX(substring(CONVERT(VARCHAR(10),doc_id,112),1,6))+1\n"
                    + "                    end as maxs\n"
                    + "                    FROM \n"
                    + "                    policyandproceduremenu\n"
                    + "                    WHERE\n"
                    + "                    substring(CONVERT(VARCHAR(10),doc_id,112),1,6) = substring(substring(CONVERT(VARCHAR(10),GETDATE(),112),1,8),3,7)";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {

                Hasil = res.getString(1);

            }

            res.close();
            stt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Hasil;
    }

    public String getIDMENU() {

        String Hasil = null;
        try {
            sql = "SELECT \n"
                    + "CASE\n"
                    + "	WHEN MAX(id)+1 is NULL \n"
                    + "	THEN substring(CONVERT(VARCHAR(10),GETDATE(),112),3,8)+'0001'\n"
                    + "	else MAX(id)+1\n"
                    + "end as maxs\n"
                    + "FROM \n"
                    + "[policyandprocedureservices] \n"
                    + "WHERE \n"
                    + "substring(CONVERT(VARCHAR(10),created_at,112),1,8) = substring(CONVERT(VARCHAR(10),GETDATE(),112),1,8)";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {

                Hasil = res.getString(1);

            }

            res.close();
            stt.close();

        } catch (SQLException e) {
        }
        return Hasil;

    }

    public String getIDDoc() {

        String Hasil = null;
        try {
            sql = "SELECT max(doc_id)+1 AS doc_id FROM policyandproceduremenu";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                if (res.getString(1) == null) {
                    Hasil = "100000000";
                } else {
                    Hasil = res.getString(1);
                }
            }

            res.close();
            stt.close();

        } catch (SQLException e) {
        }
        return Hasil;

    }

    public String Progress(String Progress) {
        String Hasil = null;

        if (null == Progress) {
            Hasil = "Inprogress";
        } else {
            switch (Progress) {
                case "Diskusi":
                    Hasil = "Inprogress";
                    break;
                case "Solusi":
                    Hasil = "Inprogress";
                    break;
                case "closed":
                    Hasil = "Closed Task";
                    break;
                default:
                    Hasil = "Error";
                    break;
            }
        }

        return Hasil;
    }

    public String getNik(String Name) {
        String Hasil = null;

        try {
            sql = "select username from users where name ='" + Name + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public String getIDDelivery() {

        String Hasil = null;
        try {
            sql = "select max(ticket_delivery)+1 as ticket_delivery from [dbo].[ticket_master_delivery]";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                if (res.getString(1) == null) {
                    Hasil = "0001";
                } else {

                    switch (res.getString(1).length()) {
                        case 1:
                            Hasil = "000" + res.getString(1);
                            break;
                        case 2:
                            Hasil = "00" + res.getString(1);
                            break;
                        case 3:
                            Hasil = "0" + res.getString(1);
                            break;
                        case 4:
                            Hasil = "" + res.getString(1);
                            break;
                        default:
                            break;
                    }

                }
            }

            res.close();
            stt.close();

        } catch (SQLException e) {
        }
        return Hasil;

    }

    public String getLOGO() {
        String Hasil = null;

        try {
            sql = "SELECT image FROM tbl_portals";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public String getImage(String Id_Ticket) {
        String Hasil = null;

        try {
            sql = "SELECT image FROM ticket_master where ticket_no = '" + Id_Ticket + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public String getIDHelpDesk() {
        String Hasil = null;
        try {
            sql = " SELECT MAX(ticket_no)+1 as jml FROM  [dbo].[ticket_master]";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                if (res.getString(1) == null) {
                    Hasil = "10001";
                } else {
                    Hasil = res.getString(1);

//                    if (res.getString(1).length() == 10) {
//                        Hasil = "1000" + res.getString(1);
//                    } else if (res.getString(1).length() == 11) {
//                        Hasil = "00" + res.getString(1);
//                    } else if (res.getString(1).length() == 12) {
//                        Hasil = "0" + res.getString(1);
//                    } else if (res.getString(1).length() == 13) {
//                        Hasil = "2" + res.getString(1);
//                    }
                }
            }

            res.close();
            stt.close();

        } catch (SQLException e) {
        }
        return Hasil;
    }

    public int DeptCountClossed(String Nik) {
        int Hasil = 0;

        try {
            sql = "select count(tbl_status_pekerjaan.nomor_ticket) as jml_\n"
                    + "                    from \n"
                    + "                    users \n"
                    + "					JOIN profile ON profile.id = users.id \n"
                    + "					join subOrganization ON subOrganization.id = profile.sub_dept \n"
                    + "                    JOIN organization ON organization.id = subOrganization.id_organization\n"
                    + "					JOIN tbl_status_pekerjaan ON  nik_closed = users.username\n"
                    + "					WHERE users.username = '" + Nik + "' AND tbl_status_pekerjaan.status_pekerjaan = 'closed'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public int AllIsseDept(String Nik) {
        int Hasil = 0;
        try {
            sql = "SELECT count(ticket_no) FROM ticket_master WHERE nik IN (\n"
                    + "select users.username\n"
                    + "                    from\n"
                    + "                    users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept\n"
                    + "                    JOIN organization ON organization.id = subOrganization.id_organization WHERE organization.name = (select organization.name\n"
                    + "                    from\n"
                    + "                    users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept\n"
                    + "                    JOIN organization ON organization.id = subOrganization.id_organization WHERE users.username = '" + Nik + "')\n"
                    + ")";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public int AllIsseuDeptStatus(String Nik, String status) {
        int Hasil = 0;
        try {

            sql = "SELECT count(ticket_no) FROM ticket_master WHERE nik IN (\n"
                    + "select users.username\n"
                    + "                    from\n"
                    + "                    users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept\n"
                    + "                    JOIN organization ON organization.id = subOrganization.id_organization WHERE organization.name = (select organization.name\n"
                    + "                    from\n"
                    + "                    users JOIN profile ON profile.id = users.id join subOrganization ON subOrganization.id = profile.sub_dept\n"
                    + "                    JOIN organization ON organization.id = subOrganization.id_organization WHERE users.username = '" + Nik + "')\n"
                    + ") AND status_pengerjaan = '" + status + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public int AllCountClossed() {
        int Hasil = 0;
        try {
            sql = " SELECT count(nomor_ticket) as jml_ FROM [dbo].[ticket_master] where status_pekerjaan = 'closed'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public int AllCountInprogress() {
        int Hasil = 0;

        try {
            sql = "SELECT count(nomor_ticket) as jml_ FROM [dbo].[tbl_status_pekerjaan] where status_pekerjaan in ('Diskusi','Solusi')";

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

    public String IDMaterial() {
        String Hasil = null;
        try {
            sql = "SELECT SUBSTRING(MAX(material_id),3,5)+1 AS NEW FROM [192.168.12.12].[dbstore_ho].[dbo].[m_material]";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                if (res.getString(1).length() == 2) {
                    Hasil = "FG000" + res.getString(1);
                } else if (res.getString(1).length() == 3) {
                    Hasil = "FG00" + res.getString(1);
                } else if (res.getString(1).length() == 4) {
                    Hasil = "FG0" + res.getString(1);
                } else if (res.getString(1).length() == 5) {
                    Hasil = "FG" + res.getString(1);
                }
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Hasil;
    }

    public String IdMaterialGroupID(String getDataID) {
        String Hasil = null;
        try {
            sql = "SELECT materialgroup_id as id_material FROM [192.168.12.12].[dbstore_ho].[dbo].[m_material_group] WHERE materialgroup_name = '" + getDataID + "'";
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

    public int AllCountOutStanding() {
        int Hasil = 0;

        try {
            sql = "SELECT\n"
                    + "	COUNT(A.ticket_no) AS _JML \n"
                    + "FROM\n"
                    + "[dbo].[ticket_master] AS A  left join\n"
                    + "[dbo].[tbl_status_pekerjaan] AS B on A.ticket_no = B.nomor_ticket \n"
                    + "WHERE B.status_pekerjaan IS NULL";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public int SumMyIssue(String Nik) {
        int Hasil = 0;

        try {
            sql = "SELECT count(ticket_no) as jml_ FROM [dbo].[ticket_master] where nik = '" + Nik + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public int CountClossed(String Nik) {
        int Hasil = 0;

        try {
            sql = "SELECT count(ticket_no) as jml_ FROM [dbo].[ticket_master] where status_pengerjaan = 'closed' AND nik = '" + Nik + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public int CountInprogress(String Nik) {
        int Hasil = 0;

        try {
            sql = "SELECT count(ticket_no) as jml_ FROM [dbo].[ticket_master] where status_pengerjaan in ('Diskusi') AND nik = '" + Nik + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public int CountOutStanding(String Nik) {
        int Hasil = 0;

        try {
            sql = "SELECT\n"
                    + "	COUNT(ticket_no) AS _JML \n"
                    + "FROM\n"
                    + "[dbo].[ticket_master]\n"
                    + "WHERE nik = '" + Nik + "' AND status_pengerjaan IS NULL";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public int CountClose(String TicketNo) {
        int Hasil = 0;

        try {
            sql = "SELECT count(status_pengerjaan) as jml  FROM [dbo].[ticket_master] WHERE nomor_ticket = '" + TicketNo + "' and status_pekerjaan = 'closed'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public int CountData() {
        int Hasil = 0;

        try {
            sql = "SELECT COUNT(material_id) AS TOTALKAN FROM [192.168.12.12].[dbstore_ho].[dbo].[t_pos_log]";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getInt(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public String BankID(String NameBank) {
        String Hasil = null;
        try {
            sql = "SELECT bank_id FROM [192.168.12.12].[dbstore_ho].[dbo].[m_bank] WHERE bank_desc = '" + NameBank + "'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Hasil = res.getString(1);
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
        }

        return Hasil;
    }

    public String PaymentType(String PayType) {
        String Hasil = null;

        switch (PayType) {
            case "----Pilih----":
                Hasil = null;
                break;
            case "Debit Card":
                Hasil = "D";
                break;
            case "Kredit Card":
                Hasil = "K";
                break;
            case "Cash & Debit":
                Hasil = "CD";
                break;
            case "Cash & Kredit":
                Hasil = "CK";
                break;
            default:
                Hasil = null;
                break;
        }

        return Hasil;
    }

    public String GetIdTakeOrderHeader() {
        String Hasil = null;
        try {

            sql = "SELECT MAX(doc_id)+1 as doc_id FROM [192.168.12.12].[dbstore_ho].[dbo].[t_pos_header] where jenis_trans = 'OUT' AND doc_id like '1%'";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                if (res.getString(1) == null) {
                    Hasil = "1000001";
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

    public int GetIDLogTransaksi() {
        int Hasil = 0;
        try {

            sql = "SELECT MAX(nomor_log_transaksi)+1 as no_log from [192.168.12.12].[dbstore_ho].[dbo].[t_pos_log]";
            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                if (res.getString(1) == null) {
                    Hasil = 1;
                } else {
                    Hasil = Integer.parseInt(res.getString(1));
                }
            }
            res.close();
            stt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Hasil;
    }

    public int GetAmmount(String qty, String Harga) {

        int HargaNet = Integer.parseInt(qty) * Integer.parseInt(Harga);

        return HargaNet;
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

    public String CheckIDHelpDesk(String Nik) {

        String Nilai = null;

        try {
            sql = "SELECT MAX() as jml FROM [dbo].[t_takeorder_log_header] where doc_id = '" + Nik + "'";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                Nilai = res.getString(1).toString();
            }
            res.close();
            stt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Nilai;
    }

    public int checkNoId(String Doc_ID) {
        int Hasil = 0;

        try {
            sql = "SELECT count(doc_id) as jml FROM [dbo].[t_takeorder_log_header] where doc_id = '" + Doc_ID + "'";

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

    public String ID_Pekerjaan() {
        String Hasil = null;
        try {
            sql = "SELECT MAX(nomor_statuspekerjaan)+1 AS MAX_ID from [dbo].[tbl_status_pekerjaan]";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                if (res.getString(1) == null) {
                    Hasil = "0001";
                } else {

                    if (res.getString(1).length() == 1) {
                        Hasil = "000" + res.getString(1);
                    } else if (res.getString(1).length() == 2) {
                        Hasil = "00" + res.getString(1);
                    } else if (res.getString(1).length() == 3) {
                        Hasil = "0" + res.getString(1);
                    } else if (res.getString(1).length() == 4) {
                        Hasil = res.getString(1);
                    }

                }
            }

            res.close();
            stt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Hasil;
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

    public int CheckWarnaHeader(String Doc) {
        int Hasil = 0;
        try {
            sql = "select count(qty_update) as jml from [dbo].[t_takeorder_log_header] where doc_id = '" + Doc + "'";

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

    public int TotalOrders() {

        int Hasil = 0;
        try {

            sql = " SELECT sum(cast(amount as numeric)) as TOTAL_NETT  FROM [192.168.12.12].[dbstore_ho].[dbo].[t_pos_log]";
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

    public int TotalDisc() {

        int Hasil = 0;
        try {

            sql = " SELECT sum(cast(amount as numeric)) as TOTAL_NETT  FROM [192.168.12.12].[dbstore_ho].[dbo].[t_pos_log] WHERE item = '2'";
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

    public int TotalNett(int NilaiA, int NilaiB) {
        int Hasil = 0;
        return Hasil = NilaiA - NilaiB;

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

    public int TotalQtys() {

        int Hasil = 0;
        try {

            sql = " SELECT SUM(QTY) AS QTY  FROM [192.168.12.12].[dbstore_ho].[dbo].[t_pos_log]";
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

    public int TotalItems() {

        int Hasil = 0;
        try {

            sql = "SELECT count(QTY) AS QTY FROM [192.168.12.12].[dbstore_ho].[dbo].[t_pos_log]";
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

    public int checkLog(String Log, String itemline) {
        int Hasil = 0;
        try {
            sql = "select count(doc_id) as total from [192.168.12.12].[dbstore_ho].[dbo].t_takeorder_log_header WHERE doc_id ='" + Log + "' AND items = '" + itemline + "'";
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

    public String getIDMaterial() {
        String Hasil = null;

        try {
            sql = "SELECT max(SUBSTRING([material_id], 3, 8))+1 as MaxNilai FROM [dbo].[m_material]";

            connection = C_Connection.getConnection();
            stt = connection.createStatement();
            res = stt.executeQuery(sql);

            while (res.next()) {
                if (res.getString(1) == null) {
                    Hasil = "FG00000";
                } else {
                    Hasil = "FG00" + res.getString(1);
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
