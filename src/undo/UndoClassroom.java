/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undo;

import model.Classroom;

/**
 *
 * @author PC
 */
public class UndoClassroom {
    private int mode;
    private Classroom classroom;

    public UndoClassroom(int mode, Classroom classroom) {
        this.mode = mode;
        this.classroom = classroom;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }
    
    
}
