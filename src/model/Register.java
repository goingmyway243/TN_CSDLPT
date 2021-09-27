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
public class Register {
    
    private String magv;
    private String mamh;
    private String malop;
    private String trinhDo;
    private String ngayThi;
    private int lan;
    private int soCauThi;
    private int thoiGian;

    public Register() {
    }

    public Register(String magv, String mamh, String malop, String trinhDo, int lan, int soCauThi, int thoiGian) {
        this.magv = magv;
        this.mamh = mamh;
        this.malop = malop;
        this.trinhDo = trinhDo;
        this.lan = lan;
        this.soCauThi = soCauThi;
        this.thoiGian = thoiGian;
    }

    
    
    

    public Register(String magv, String mamh, String malop, String trinhDo, String ngayThi, int lan, int soCauThi, int thoiGian) {
        this.magv = magv;
        this.mamh = mamh;
        this.malop = malop;
        this.trinhDo = trinhDo;
        this.ngayThi = ngayThi;
        this.lan = lan;
        this.soCauThi = soCauThi;
        this.thoiGian = thoiGian;
    }

    public String getMagv() {
        return magv;
    }

    public void setMagv(String magv) {
        this.magv = magv;
    }

    public String getMamh() {
        return mamh;
    }

    public void setMamh(String mamh) {
        this.mamh = mamh;
    }

    public String getMalop() {
        return malop;
    }

    public void setMalop(String malop) {
        this.malop = malop;
    }

    public String getTrinhDo() {
        return trinhDo;
    }

    public void setTrinhDo(String trinhDo) {
        this.trinhDo = trinhDo;
    }

    public String getNgayThi() {
        return ngayThi;
    }

    public void setNgayThi(String ngayThi) {
        this.ngayThi = ngayThi;
    }

    public int getLan() {
        return lan;
    }

    public void setLan(int lan) {
        this.lan = lan;
    }

    public int getSoCauThi() {
        return soCauThi;
    }

    public void setSoCauThi(int soCauThi) {
        this.soCauThi = soCauThi;
    }

    public int getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(int thoiGian) {
        this.thoiGian = thoiGian;
    }

    @Override
    public String toString() {
        return "Regiter{" + "magv=" + magv + ", mamh=" + mamh + ", malop=" + malop + ", trinhDo=" + trinhDo + ", ngayThi=" + ngayThi + ", lan=" + lan + ", soCauThi=" + soCauThi + ", thoiGian=" + thoiGian + '}';
    }
    
    public Object[] toArray() {
        return new Object[]{magv, mamh, malop, trinhDo, ngayThi, lan, soCauThi, thoiGian};
    }
}
