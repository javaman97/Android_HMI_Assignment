package com.aman.simpleenergyassignment


import com.aman.simpleenergyassignment.domain.SignalStatus
import com.aman.simpleenergyassignment.domain.validateBooleanSignal
import com.aman.simpleenergyassignment.domain.validateSpeedKmh
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class VehicleSignalValidatorTest {

    @Test
    fun `valid speed is accepted`() {
        val result = validateSpeedKmh(25f)

        assertEquals(SignalStatus.VALID, result.status)
        assertEquals(25f, result.value!!)
    }

    @Test
    fun `invalid speed is rejected`() {
        val negative = validateSpeedKmh(-10f)
        val nan = validateSpeedKmh(Float.NaN)
        val infinite = validateSpeedKmh(Float.POSITIVE_INFINITY)

        assertEquals(SignalStatus.INVALID, negative.status)
        assertEquals(SignalStatus.INVALID, nan.status)
        assertEquals(SignalStatus.INVALID, infinite.status)

        assertNull(negative.value)
        assertNull(nan.value)
        assertNull(infinite.value)
    }

    @Test
    fun `missing signals are marked unavailable`() {
        val speed = validateSpeedKmh(null)
        val motor = validateBooleanSignal(null)
        val killSwitch = validateBooleanSignal(null)

        assertEquals(SignalStatus.UNAVAILABLE, speed.status)
        assertEquals(SignalStatus.UNAVAILABLE, motor.status)
        assertEquals(SignalStatus.UNAVAILABLE, killSwitch.status)
    }
}
