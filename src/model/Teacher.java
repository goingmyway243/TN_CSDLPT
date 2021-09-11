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
public class Teacher {
    
    private String magv;
    private String ho;
    private String ten;
    private String hocVi;
    private String makh;

    public Teacher() {
    }

    public Teacher(String magv) {
        this.magv = magv;
    }
    
    

    public Teacher(String magv, String ho, String ten, String hocVi, String makh) {
        this.magv = magv;
        this.ho = ho;
        this.ten = ten;
        this.hocVi = hocVi;
        this.makh = makh;
    }

    public String getMagv() {
        return magv;
    }

    public void setMagv(String magv) {
        this.magv = magv;
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

    public String getHocVi() {
        return hocVi;
    }

    public void setHocVi(String hocVi) {
        this.hocVi = hocVi;
    }

    public String getMakh() {
        return makh;
    }

    public void setMakh(String makh) {
        this.makh = makh;
    }

    @Override
    public String toString() {
        return "Teacher{" + "magv=" + magv + ", ho=" + ho + ", ten=" + ten + ", hocVi=" + hocVi + ", makh=" + makh + '}';
    }
    
    public Object[] toArray() {
        return new Object[]{magv, ho, ten, hocVi, makh};
    }
    
}
