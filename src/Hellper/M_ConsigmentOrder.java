/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hellper;

/**
 *
 * @author PCIT1
 */
public class M_ConsigmentOrder {

    public M_ConsigmentOrder() {

    }

    public String getDoc_no() {
        return Doc_no;
    }

    public void setDoc_no(String Doc_no) {
        this.Doc_no = Doc_no;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String Qty) {
        this.Qty = Qty;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }

    public String getHarga() {
        return Harga;
    }

    public void setHarga(String Harga) {
        this.Harga = Harga;
    }

    public String getDisc() {
        return Disc;
    }

    public void setDisc(String Disc) {
        this.Disc = Disc;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    public M_ConsigmentOrder(String Doc_no, String Name, String Qty, String Unit, String Harga, String Disc, String Total) {
        this.Doc_no = Doc_no;
        this.Name = Name;
        this.Qty = Qty;
        this.Unit = Unit;
        this.Harga = Harga;
        this.Disc = Disc;
        this.Total = Total;
    }

    String Doc_no;
    String Name;
    String Qty;
    String Unit;
    String Harga;
    String Disc;
    String Total;

}
