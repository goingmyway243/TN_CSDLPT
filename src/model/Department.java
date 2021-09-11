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
public class Department {
    
    private String makh;
    private String tenkh;
    private String macs;

    public Department() {
    }

    public Department(String makh, String tenkh, String macs) {
        this.makh = makh;
        this.tenkh = tenkh;
        this.macs = macs;
    }

    public String getMakh() {
        return makh;
    }

    public void setMakh(String makh) {
        this.makh = makh;
    }

    public String getTenkh() {
        return tenkh;
    }

    public void setTenkh(String tenkh) {
        this.tenkh = tenkh;
    }

    public String getMacs() {
        return macs;
    }

    public void setMacs(String macs) {
        this.macs = macs;
    }

    @Override
    public String toString() {
        return "Department{" + "makh=" + makh + ", tenkh=" + tenkh + ", macs=" + macs + '}';
    }
    
    public Object[] toArray() {
        return new Object[]{makh, tenkh, macs};
    }
    
}
