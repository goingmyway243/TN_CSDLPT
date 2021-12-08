/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undo;

import model.Register;

/**
 *
 * @author PC
 */
public class UndoRegister {
    private int mode;
    private Register register;

    public UndoRegister(int mode, Register register) {
        this.mode = mode;
        this.register = register;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }
    
    
}
