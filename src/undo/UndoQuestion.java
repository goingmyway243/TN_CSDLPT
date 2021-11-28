/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package undo;

import model.Question;

/**
 *
 * @author PC
 */
public class UndoQuestion {
    private int mode;
    private Question question;

    public UndoQuestion(int mode, Question question) {
        this.mode = mode;
        this.question = question;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
            
    
}
