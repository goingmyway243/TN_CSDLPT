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
public class Branch {
    
    private String macs;
    private String tencs;
    private String diaChi;

    public Branch() {
    }

    public Branch(String macs, String tencs, String diaChi) {
        this.macs = macs;
        this.tencs = tencs;
        this.diaChi = diaChi;
    }

    public String getMacs() {
        return macs;
    }

    public void setMacs(String macs) {
        this.macs = macs;
    }

    public String getTencs() {
        return tencs;
    }

    public void setTencs(String tencs) {
        this.tencs = tencs;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Override
    public String toString() {
        return "Branch{" + "macs=" + macs + ", tencs=" + tencs + ", diaChi=" + diaChi + '}';
    }
    
    public Object[] toArray() {
        return new Object[]{macs, tencs, diaChi};
    }
        
}
