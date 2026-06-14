package com.pperotti.android.moviescatalogapp.presentation.common

import java.util.logging.Level
import java.util.logging.Logger

private const val METRICS_TAG = "UrlFormatterMetrics"
private val metricsLogger: Logger = Logger.getLogger(METRICS_TAG)

object UrlFormatterMetrics {
    fun recordInvalidUrl(rawUrl: String?) {
        metricsLogger.log(Level.INFO, "Metric: invalid_url -> $rawUrl")
    }

    fun recordNoActivityHandler(rawUrl: String) {
        metricsLogger.log(Level.INFO, "Metric: no_activity_handler -> $rawUrl")
    }

    fun recordLaunchFailure(rawUrl: String, e: Throwable) {
        metricsLogger.log(Level.INFO, "Metric: launch_failure -> $rawUrl", e)
    }
}
