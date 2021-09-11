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
public class Question {
    
    private int cauHoi;
    private String mamh;
    private String trinhDo;
    private String noiDung;
    private String a;
    private String b;
    private String c;
    private String d;
    private String dapAn;
    private String magv;

    public Question() {
    }

    public Question(int cauHoi, String trinhDo) {
        this.cauHoi = cauHoi;
        this.trinhDo = trinhDo;
    }
    
    

    public Question(int cauHoi, String mamh, String trinhDo, String noiDung, String a, String b, String c, String d, String dapAn, String magv) {
        this.cauHoi = cauHoi;
        this.mamh = mamh;
        this.trinhDo = trinhDo;
        this.noiDung = noiDung;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.dapAn = dapAn;
        this.magv = magv;
    }

    public int getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(int cauHoi) {
        this.cauHoi = cauHoi;
    }

    public String getMamh() {
        return mamh;
    }

    public void setMamh(String mamh) {
        this.mamh = mamh;
    }

    public String getTrinhDo() {
        return trinhDo;
    }

    public void setTrinhDo(String trinhDo) {
        this.trinhDo = trinhDo;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getDapAn() {
        return dapAn;
    }

    public void setDapAn(String dapAn) {
        this.dapAn = dapAn;
    }

    public String getMagv() {
        return magv;
    }

    public void setMagv(String magv) {
        this.magv = magv;
    }
    
    
    
    public Object[] toArray() {
        return new Object[]{cauHoi, mamh, trinhDo, noiDung, a, b, c, d, dapAn, magv};
    }

    @Override
    public String toString() {
        return "Exam{" + "cauHoi=" + cauHoi + ", mamh=" + mamh + ", trinhDo=" + trinhDo + ", noiDung=" + noiDung + ", a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", dapAn=" + dapAn + ", magv=" + magv + '}';
    }

    
    
    
}
