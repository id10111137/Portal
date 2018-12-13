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
public class M_User {

    String id_nik;
    String Username;
    String Password;
    
    public M_User(String id_nik, String Username, String Password) {
        this.id_nik = id_nik;
        this.Username = Username;
        this.Password = Password;
    }
    
    
    public String getId_nik() {
        return id_nik;
    }

    public void setId_nik(String id_nik) {
        this.id_nik = id_nik;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

  
    

    
    
}
