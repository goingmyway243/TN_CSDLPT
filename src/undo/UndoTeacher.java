/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undo;

import model.Teacher;

/**
 *
 * @author PC
 */
public class UndoTeacher {
    private int mode;
    private Teacher teacher;

    public UndoTeacher(int mode, Teacher teacher) {
        this.mode = mode;
        this.teacher = teacher;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
