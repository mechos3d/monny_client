package com.example.gorg.monny;

public class VarStorage {
// make this file a global variable storage like there
// http://stackoverflow.com/questions/1944656/android-global-variable
    public static int current_sum = 0;
    public static String current_sign = "-";
    public static final String PREFS_FILE_NAME = "monny_data";

    // dirty-dirty-dirty HACK to reach MainActivity's counter from Button_1000GestureListener
    public static MainActivity mainActivity;

    public static void change_current_sign(){
        if(current_sign == "-"){
            current_sign = "+";
        } else {
            current_sign = "-";
        }
    }
}


