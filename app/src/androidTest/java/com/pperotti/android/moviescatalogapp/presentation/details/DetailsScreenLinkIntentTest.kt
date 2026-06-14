package com.pperotti.android.moviescatalogapp.presentation.details

import android.app.Instrumentation
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pperotti.android.moviescatalogapp.R
import android.net.Uri
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsScreenLinkIntentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun tappingHomepage_sendsActionViewIntent() {
        // Arrange
        val url = "https://www.example.com/movie/123"
        val details = DetailsUiData(
            id = 1,
            imdbId = "tt0000001",
            homepage = url,
            overview = "Overview",
            posterPath = null,
            genres = listOf(DetailsUiGenre(1, "Drama")),
            title = "Title",
            revenue = 0L,
            status = "Released",
            voteAverage = 7.5f,
            voteCount = 100,
        )

        // Act: set content
        composeTestRule.setContent {
            DrawDetailsContent(
                DetailsUiState.Success(details),
                modifier = androidx.compose.ui.Modifier
            )
        }

        // The LinkMessageItemComposable composes the host text (e.g., "www.example.com") along with label.
        // Find the node by substring of the host and click it.
        composeTestRule.onNodeWithText("www.example.com", substring = true).performClick()

        // Assert: an ACTION_VIEW intent was sent with the expected data
        Intents.intended(allOf(
            IntentMatchers.hasAction(Intent.ACTION_VIEW),
            IntentMatchers.hasData(Uri.parse(url))
        ))
    }
}
