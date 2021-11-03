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
public class Answer {
    
    private int baiThi;
    private int stt;
    private int cauHoi;
    private String daChon;

    public Answer() {
    }

    public Answer(int baiThi, int stt, int cauHoi, String daChon) {
        this.baiThi = baiThi;
        this.stt = stt;
        this.cauHoi = cauHoi;
        this.daChon = daChon;
    }

    public int getBaiThi() {
        return baiThi;
    }

    public void setBaiThi(int baiThi) {
        this.baiThi = baiThi;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public int getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(int cauHoi) {
        this.cauHoi = cauHoi;
    }

    public String getDaChon() {
        return daChon;
    }

    public void setDaChon(String daChon) {
        this.daChon = daChon;
    }

    @Override
    public String toString() {
        return "Answer{" + "baiThi=" + baiThi + ", stt=" + stt + ", cauHoi=" + cauHoi + ", daChon=" + daChon + '}';
    }
    
    public Object[] toArray() {
        return new Object[]{baiThi, stt, cauHoi, daChon};
    }
    
}
