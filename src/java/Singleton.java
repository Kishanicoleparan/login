/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java;

/**
 *
 * @author Admin
 */
public class Singleton {
    private static Singleton instance;
    private int u_id;
    private String fullname;
    private String email;
    private String status;

    private Singleton() {
        // Private constructor to prevent instantiation
    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
    
    public static boolean isInstanceEmpty() {
        return instance == null;
    }

   public int getId() {
        return u_id;
    }

    public void setId(int id) {
        this.u_id = id;
    }

    public String getFname() {
        return fullname;
    }

    public void setFname(String fname) {
        this.fullname = fname;
    }

   

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

 
    
      public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}



