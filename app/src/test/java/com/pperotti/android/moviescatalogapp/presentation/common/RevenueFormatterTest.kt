package com.pperotti.android.moviescatalogapp.presentation.common

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat
import java.util.Locale

class RevenueFormatterTest {
    @Test
    fun `formats non-null revenue for en_US`() {
        val value = 462549154L
        val expected =
            NumberFormat.getCurrencyInstance(Locale("en", "US")).let {
                it.maximumFractionDigits = 0
                it.format(value)
            }
        val actual = formatRevenue(value, Locale("en", "US"), "N/A")
        assertEquals(expected, actual)
    }

    @Test
    fun `formats non-null revenue for es_ES`() {
        val value = 462549154L
        val expected =
            NumberFormat.getCurrencyInstance(Locale("es", "ES")).let {
                it.maximumFractionDigits = 0
                it.format(value)
            }
        val actual = formatRevenue(value, Locale("es", "ES"), "N/D")
        assertEquals(expected, actual)
    }

    @Test
    fun `returns placeholder for null revenue`() {
        val actual = formatRevenue(null, Locale("en", "US"), "N/A")
        assertEquals("N/A", actual)
    }

    @Test
    fun `formats zero revenue`() {
        val value = 0L
        val expected =
            NumberFormat.getCurrencyInstance(Locale("en", "US")).let {
                it.maximumFractionDigits = 0
                it.format(value)
            }
        val actual = formatRevenue(value, Locale("en", "US"), "N/A")
        assertEquals(expected, actual)
    }
}
