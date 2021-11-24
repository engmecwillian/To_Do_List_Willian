package com.example.todolist.extensions

import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

private val locale = Locale(language = "pt", country = "BR")

fun Date.format(): String {
    return SimpleDateFormat(pattern: "DD/MM/YYYY", locale).format(date:this)

}

var TextInputLayout.text: String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }
