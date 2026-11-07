package com.aman.simpleenergyassignment

import com.aman.simpleenergyassignment.data.FakeVehicleDataProvider
import com.aman.simpleenergyassignment.domain.VehicleScenario
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FakeVehicleDataProviderTest {

    @Test
    fun `simulation emits all five required scenarios`() = runTest {
        val provider = FakeVehicleDataProvider(
            intervalMillis = 1000L,
            clock = { 1234L }
        )

        val emissions = provider.observeVehicleData()
            .take(8)
            .toList()

        assertEquals(
            listOf(
                VehicleScenario.PARKED,
                VehicleScenario.READY,
                VehicleScenario.RIDING,
                VehicleScenario.ACCELERATING,
                VehicleScenario.MOTOR_DISABLED,
                VehicleScenario.COASTING,
                VehicleScenario.COASTING,
                VehicleScenario.STOPPED
            ),
            emissions.map { it.scenario }
        )

        assertEquals(0f, emissions[0].speedKmh!!)
        assertEquals(false, emissions[0].motorStatus)
        assertEquals(false, emissions[0].killSwitchStatus)

        assertEquals(25f, emissions[4].speedKmh!!)
        assertEquals(false, emissions[4].motorStatus)
        assertEquals(true, emissions[4].killSwitchStatus)
    }
}
