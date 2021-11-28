/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undo;

import model.Subject;

/**
 *
 * @author PC
 */
public class UndoSubject {
    private int mode;
    private Subject subject;

    public UndoSubject(int mode, Subject subject) {
        this.mode = mode;
        this.subject = subject;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
    
    
}
