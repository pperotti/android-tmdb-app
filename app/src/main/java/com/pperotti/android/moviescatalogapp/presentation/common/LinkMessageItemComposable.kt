package com.pperotti.android.moviescatalogapp.presentation.common

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pperotti.android.moviescatalogapp.R
import androidx.compose.ui.res.stringResource

@Composable
fun LinkMessageItemComposable(
    @StringRes titleRes: Int,
    rawUrl: String?,
    defaultLabel: String = "-",
) {
    val ctx = LocalContext.current
    val title = stringResource(titleRes)
    val formatted = UrlFormatter.parse(rawUrl)

    if (formatted == null) {
        // Fallback to normal message rendering
        MessageItemComposable(titleRes, rawUrl ?: defaultLabel)
        return
    }

    val annotatedString =
        buildAnnotatedString {
            withStyle(
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
                        .toSpanStyle()
                        .copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            ) {
                append(title)
            }
            withStyle(
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
                        .toSpanStyle()
                        .copy(fontWeight = FontWeight.Normal, fontSize = 18.sp),
            ) {
                append(formatted.displayText)
            }
        }

    val contentDesc = stringResource(R.string.details_open_website)

    Text(
        text = annotatedString,
        modifier = Modifier
            .padding(PaddingValues(horizontal = 8.dp, vertical = 4.dp))
            .semantics { this.contentDescription = contentDesc }
            .clickable { formatted.onClick(ctx) },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
