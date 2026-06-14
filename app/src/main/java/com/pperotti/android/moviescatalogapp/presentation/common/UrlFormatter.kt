package com.pperotti.android.moviescatalogapp.presentation.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.util.logging.Level
import java.util.logging.Logger
import java.net.URI
import androidx.core.net.toUri

private const val TAG = "UrlFormatter"
private val logger: Logger = Logger.getLogger(TAG)

data class FormattedUrl(
    val displayText: String,
    val rawUrl: String,
    val onClick: (Context) -> Unit,
)

object UrlFormatter {
    /**
     * Parses a raw URL string and returns a FormattedUrl on success, or null on invalid input.
     * Rules:
     * - null or blank input -> null
     * - trim whitespace
     * - must parse as URI and have scheme http or https
     * - must have a non-empty host
     */
    fun parse(rawUrl: String?): FormattedUrl? {
        if (rawUrl == null) return null
        val trimmed = rawUrl.trim()
        if (trimmed.isEmpty()) return null

        val uri = try {
            URI(trimmed)
        } catch (e: Exception) {
            logger.log(Level.WARNING, "Invalid URI: $rawUrl", e)
            UrlFormatterMetrics.recordInvalidUrl(rawUrl)
            return null
        }

        val scheme = uri.scheme?.lowercase()
        if (scheme != "http" && scheme != "https") {
            logger.warning("Unsupported scheme: $scheme for url=$trimmed")
            UrlFormatterMetrics.recordInvalidUrl(trimmed)
            return null
        }

        val host = uri.host
        if (host.isNullOrBlank()) {
            logger.warning("URI missing host: $trimmed")
            UrlFormatterMetrics.recordInvalidUrl(trimmed)
            return null
        }

        val display = host
        val normalized = trimmed

        val onClick: (Context) -> Unit = { ctx ->
            try {
                val intent = Intent(Intent.ACTION_VIEW, normalized.toUri()).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                val pm = ctx.packageManager
                try {
                    if (intent.resolveActivity(pm) != null) {
                        ctx.startActivity(intent)
                    } else {
                        logger.warning("No activity found to handle intent for url=$normalized")
                        UrlFormatterMetrics.recordNoActivityHandler(normalized)
                    }
                } catch (e: Exception) {
                    logger.log(Level.WARNING, "Failed to launch browser for url=$normalized", e)
                    UrlFormatterMetrics.recordLaunchFailure(normalized, e)
                }
            } catch (e: Exception) {
                // This outer catch guards the URI->Intent construction and other unexpected errors
                logger.log(Level.WARNING, "Failed to launch browser for url=$normalized", e)
                UrlFormatterMetrics.recordLaunchFailure(normalized, e)
            }
        }

        return FormattedUrl(displayText = display, rawUrl = normalized, onClick = onClick)
    }
}
