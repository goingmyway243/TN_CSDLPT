/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vivau
 */
public class ConvertAnswer {

    public static String answerToString(String[] answer, int length) {
        
        List<String> str = new ArrayList<>();
        
        
        for (int i = 0; i < length; i++) {
            str.add((i+1) + ":" + answer[i]);
        }
        return String.join("-", str);

    }

    public static void main(String[] args) {

        int length = 12;
        String answerStr = "1:A-2:B-3:A-4:D-5:C-6:A-7:B-8:D-9:D-10:C-11:A-12:B";
        String[] answer = {"A", "B", "C", "D", "A", "B", null, "D", null, "B", "C", null};
        System.out.println(answerToString(answer, length));
    }

}
