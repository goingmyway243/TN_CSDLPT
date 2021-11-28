/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undo;

import model.Student;

/**
 *
 * @author PC
 */
public class UndoStudent {
    private int mode;
    private Student student;

    public UndoStudent(int mode, Student student) {
        this.mode = mode;
        this.student = student;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
