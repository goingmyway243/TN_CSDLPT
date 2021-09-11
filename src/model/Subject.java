/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author vivau
 */
public class Subject {
    
    private String mamh;
    private String tenmh;

    public Subject() {
    }

    public Subject(String mamh, String tenmh) {
        this.mamh = mamh;
        this.tenmh = tenmh;
    }

    public String getMamh() {
        return mamh;
    }

    public void setMamh(String mamh) {
        this.mamh = mamh;
    }

    public String getTenmh() {
        return tenmh;
    }

    public void setTenmh(String tenmh) {
        this.tenmh = tenmh;
    }
    
    public Object[] toArray() {
        return new Object[]{mamh, tenmh};
    }

    @Override
    public String toString() {
        return "Subject{" + "mamh=" + mamh + ", tenmh=" + tenmh + '}';
    }
    
    
    
}
