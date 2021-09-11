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
public class Classroom {
    
    private String maLop;
    private String tenLop;
    private String makh;

    public Classroom() {
    }

    public Classroom(String maLop, String tenLop, String makh) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.makh = makh;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getMakh() {
        return makh;
    }

    public void setMakh(String makh) {
        this.makh = makh;
    }

    @Override
    public String toString() {
        return "Class{" + "maLop=" + maLop + ", tenLop=" + tenLop + ", makh=" + makh + '}';
    }
    
    public Object[] toArray() {
        return new Object[]{maLop, tenLop, makh};
    }
    
}
