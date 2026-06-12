package com.pperotti.android.moviescatalogapp.presentation.common

import java.text.NumberFormat
import java.util.Locale

interface RevenueFormatter {
    fun format(value: Long?): String
}

class DefaultRevenueFormatter(
    private val locale: Locale? = null,
    private val unknownPlaceholder: String = "N/A",
) : RevenueFormatter {
    override fun format(value: Long?): String = formatRevenue(value, locale, unknownPlaceholder)
}

fun formatRevenue(
    value: Long?,
    locale: Locale? = null,
    unknownPlaceholder: String = "N/A",
): String {
    if (value == null) return unknownPlaceholder
    val l = locale ?: Locale.getDefault()
    val nf = NumberFormat.getCurrencyInstance(l)
    nf.maximumFractionDigits = 0
    return nf.format(value)
}
