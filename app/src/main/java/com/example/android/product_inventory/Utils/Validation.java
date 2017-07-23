package com.example.android.product_inventory.Utils;

import android.widget.EditText;

public class Validation {

    public boolean isInteger(EditText inputText) {
        try {
            Integer.parseInt(inputText.getText().toString());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
    public boolean isBlank(EditText inputText) {
        if (inputText.getText().toString().trim().length() > 0)
            return false;
        else
            return true;
    }

    public boolean isFloat(EditText inputText) {
        try {
            Float.parseFloat(inputText.getText().toString());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
    public boolean isPositive(int value){
        if(value > 0){
            return true;
        }else{
            return false;
        }
    }
}
