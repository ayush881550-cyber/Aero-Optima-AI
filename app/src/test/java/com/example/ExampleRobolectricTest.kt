package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.engine.OptimizationEngine
import com.example.model.PredefinedScenarios
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("AeroOptima AI", appName)
    }

    @Test
    fun `optimization engine generates valid plan`() {
        val engine = OptimizationEngine()
        val scenario = PredefinedScenarios.sih26250Demo
        val comparison = engine.runFullComparison(scenario.defaultTasks, scenario.defaultResources)

        assertNotNull(comparison)
        assertTrue(comparison.optimizedPlan.isNotEmpty())
        assertTrue(comparison.baselinePlan.isNotEmpty())
        assertTrue(comparison.efficiencyImprovementPercent >= 0.0)
    }
}
