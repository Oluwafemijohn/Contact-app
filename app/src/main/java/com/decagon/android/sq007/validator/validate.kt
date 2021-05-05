package com.decagon.android.sq007.validator

import java.util.regex.Matcher
import java.util.regex.Pattern

object validate {

    //Phone number validator
    fun mobileValidate(text: String?): Boolean {
        var regexPattern: Pattern = Pattern.compile("^(0|234)((70)|([89][01]))[0-9]{8}\$")
        var regexMatcher: Matcher = regexPattern.matcher(text)
        return regexMatcher.matches()
    }
}