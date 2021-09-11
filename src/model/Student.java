/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author vivau
 */
public class Student {
    
    private String masv;
    private String ho;
    private String ten;
    private Date ngaySinh;
    private String diaChi;
    private String maLop;
    private String matKhau;

    public Student() {
    }

    public Student(String masv, String ho, String ten) {
        this.masv = masv;
        this.ho = ho;
        this.ten = ten;
    }
    
    

    public Student(String masv, String ho, String ten, Date ngaySinh, String diaChi, String maLop, String matKhau) {
        this.masv = masv;
        this.ho = ho;
        this.ten = ten;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.maLop = maLop;
        this.matKhau = matKhau;
    }

    public String getMasv() {
        return masv;
    }

    public void setMasv(String masv) {
        this.masv = masv;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    
    public Object[] toArray() {
        return new Object[]{masv, ho, ten, ngaySinh, diaChi, maLop, matKhau};
    }

    @Override
    public String toString() {
        return "Student{" + "masv=" + masv + ", ho=" + ho + ", ten=" + ten + ", ngaySinh=" + ngaySinh + ", diaChi=" + diaChi + ", maLop=" + maLop + ", matKhau=" + matKhau + '}';
    }
    
    
}
