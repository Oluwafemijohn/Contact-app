package com.decagon.android.sq007.validator

import com.google.common.truth.Truth
import junit.framework.TestCase
import org.junit.Test

class validateTest : TestCase() {
    @Test
    fun testCorectPhoneNumber1() {
        val samplePhoneNumber = "08130675563"
        val validator = validate.mobileValidate(samplePhoneNumber)
        Truth.assertThat(validator).isTrue()
    }

    @Test
    fun testCorectPhoneNumber2() {
        val samplePhoneNumber = "2348130675563"
        val validator = validate.mobileValidate(samplePhoneNumber)
        Truth.assertThat(validator).isTrue()
    }

    @Test
    fun testCorectPhoneNumber3() {
        val samplePhoneNumber = "2347030675563"
        val validator = validate.mobileValidate(samplePhoneNumber)
        Truth.assertThat(validator).isTrue()
    }
    @Test
    fun testCorectPhoneNumber4() {
        val samplePhoneNumber = "07030675563"
        val validator = validate.mobileValidate(samplePhoneNumber)
        Truth.assertThat(validator).isTrue()
    }
    @Test
    fun testCorectPhoneNumber5() {
        val samplePhoneNumber = "09030675563"
        val validator = validate.mobileValidate(samplePhoneNumber)
        Truth.assertThat(validator).isTrue()
    }
    @Test
    fun testInCorectPhoneNumber1() {
        val samplePhoneNumber = "7030675563"
        val validator = validate.mobileValidate(samplePhoneNumber)
        Truth.assertThat(validator).isFalse()
    }
    @Test
    fun testInCorectPhoneNumber2() {
        val samplePhoneNumber = "070306755631"
        val validator = validate.mobileValidate(samplePhoneNumber)
        Truth.assertThat(validator).isFalse()
    }
    @Test
    fun testInCorectPhoneNumber3() {
        val samplePhoneNumber = "080306755631"
        val validator = validate.mobileValidate(samplePhoneNumber)
        Truth.assertThat(validator).isFalse()
    }
    @Test
    fun testInCorectPhoneNumber4() {
        val samplePhoneNumber = "22480306755631"
        val validator = validate.mobileValidate(samplePhoneNumber)
        Truth.assertThat(validator).isFalse()
    }
}
