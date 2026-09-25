package com.paraskcd.spotlightsearch.sources.domain.calculator

import com.paraskcd.spotlightsearch.sources.domain.model.CalculationKind
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.Locale

class CalculatorTest {
    private val calculator = Calculator()
    private lateinit var previousLocale: Locale

    @Before
    fun useUsLocale() {
        previousLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)
    }

    @After
    fun restoreLocale() {
        Locale.setDefault(previousLocale)
    }

    @Test
    fun addsTwoNumbers() {
        val hit = calculator.evaluate("2+2")
        assertEquals("4.0", hit?.answer)
        assertEquals(CalculationKind.ARITHMETIC, hit?.kind)
    }

    @Test
    fun keepsTheFractionOfADivision() {
        assertEquals("2.5", calculator.evaluate("10 / 4")?.answer)
    }

    @Test
    fun readsXAsMultiplication() {
        assertEquals("12.0", calculator.evaluate("3 x 4")?.answer)
    }

    @Test
    fun respectsOperatorPrecedence() {
        assertEquals("14.0", calculator.evaluate("2 + 3 * 4")?.answer)
    }

    @Test
    fun convertsCelsiusToFahrenheit() {
        val hit = calculator.evaluate("100 c to f")
        assertEquals("212.00 °F", hit?.answer)
        assertEquals(CalculationKind.TEMPERATURE, hit?.kind)
    }

    @Test
    fun convertsKilometresToMiles() {
        val hit = calculator.evaluate("5 km to miles")
        assertEquals("3.11 miles", hit?.answer)
        assertEquals(CalculationKind.UNIT, hit?.kind)
    }

    @Test
    fun addsFeetAsDetailForCentimetresToInches() {
        val hit = calculator.evaluate("50 cm to inches")
        assertEquals("19.69 in", hit?.answer)
        assertEquals("1.64 ft", hit?.detail)
    }

    @Test
    fun resolvesToday() {
        assertEquals(CalculationKind.DATE, calculator.evaluate("today")?.kind)
    }

    @Test
    fun ignoresPlainWords() {
        assertNull(calculator.evaluate("hello"))
    }
}
