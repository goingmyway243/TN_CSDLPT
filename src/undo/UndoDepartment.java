/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undo;

import model.Department;

/**
 *
 * @author PC
 */
public class UndoDepartment {
    private int mode;
    private Department depart;

    public UndoDepartment(int mode, Department depart) {
        this.mode = mode;
        this.depart = depart;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Department getDepart() {
        return depart;
    }

    public void setDepart(Department depart) {
        this.depart = depart;
    }
    
    
}
