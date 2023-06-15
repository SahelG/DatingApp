package com.example.datingapp

import android.text.TextUtils
import android.widget.EditText

class ValidateData {

    /*equivalente a tener una clase estatica*/

    companion object{
        fun Validate (editTexts: ArrayList<EditText>): Boolean {
        var result = true

        for (editText in editTexts) {
            if(TextUtils.isEmpty(editText.text.toString())) {
                editText.error = "Llene el campo"
                result = false
            }
        }
        return result
    }}
}