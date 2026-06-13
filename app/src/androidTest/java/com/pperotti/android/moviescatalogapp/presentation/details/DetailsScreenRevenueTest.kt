package com.pperotti.android.moviescatalogapp.presentation.details

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pperotti.android.moviescatalogapp.presentation.details.DetailsUiData
import com.pperotti.android.moviescatalogapp.presentation.details.DetailsUiGenre
import com.pperotti.android.moviescatalogapp.presentation.details.DetailsUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.NumberFormat
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class DetailsScreenRevenueTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showsFormattedRevenue_onDetailsContent() {
        // Arrange
        val value = 462549154L
        Locale.setDefault(Locale("en", "US"))
        val expected = NumberFormat.getCurrencyInstance(Locale("en", "US")).let {
            it.maximumFractionDigits = 0
            it.format(value)
        }

        val details = DetailsUiData(
            id = 1,
            imdbId = "tt0000001",
            homepage = "https://example.com",
            overview = "Overview",
            posterPath = null,
            genres = listOf(DetailsUiGenre(1, "Drama")),
            title = "Title",
            revenue = value,
            status = "Released",
            voteAverage = 7.5f,
            voteCount = 100,
        )

        // Act
        composeTestRule.setContent {
            DrawDetailsContent(DetailsUiState.Success(details), modifier = androidx.compose.ui.Modifier)
        }

        // Assert: the formatted revenue should be present in the composed text
        composeTestRule.onNodeWithText(expected, substring = true).assertExists()
    }
}
