/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

/**
 *
 * @author vivau
 */
public class Mark {

    public static float round(float mark) {
        return (float) Math.round(((float) Math.round(mark * 10) / 10) * 2) / 2;
    }
    
}
