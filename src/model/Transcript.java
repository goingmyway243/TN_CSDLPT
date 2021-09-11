/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Date;

/**
 *
 * @author vivau
 */
public class Transcript {
    
    private String masv;
    private String mamh;
    private int lan;
    private Date ngaythi;
    private float diem;
    private String baiThi;

    public Transcript() {
    }

    public Transcript(String masv, String mamh, int lan, float diem) {
        this.masv = masv;
        this.mamh = mamh;
        this.lan = lan;
        this.diem = diem;
    }
    
    

    public Transcript(String masv, String mamh, int lan, Date ngaythi, float diem, String baiThi) {
        this.masv = masv;
        this.mamh = mamh;
        this.lan = lan;
        this.ngaythi = ngaythi;
        this.diem = diem;
        this.baiThi = baiThi;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public String getMamh() {
        return mamh;
    }

    public void setMamh(String mamh) {
        this.mamh = mamh;
    }

    public int getLan() {
        return lan;
    }

    public void setLan(int lan) {
        this.lan = lan;
    }

    public Date getNgaythi() {
        return ngaythi;
    }

    public void setNgaythi(Date ngaythi) {
        this.ngaythi = ngaythi;
    }

    public float getDiem() {
        return diem;
    }

    public void setDiem(float diem) {
        this.diem = diem;
    }

    public String getBaiThi() {
        return baiThi;
    }

    public void setBaiThi(String baiThi) {
        this.baiThi = baiThi;
    }

    @Override
    public String toString() {
        return "Transcript{" + "masv=" + masv + ", mamh=" + mamh + ", lan=" + lan + ", ngaythi=" + ngaythi + ", diem=" + diem + ", baiThi=" + baiThi + '}';
    }
    
    
    
    public Object[] toArray() {
        return new Object[]{masv, mamh, lan};
    }
}
