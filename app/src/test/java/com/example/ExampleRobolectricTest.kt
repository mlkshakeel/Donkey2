package com.example

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun testClickPlay() {
    composeTestRule.setContent {
      com.example.ui.theme.MyApplicationTheme {
        DonkeyRunApp()
      }
    }
    composeTestRule.onNodeWithText("PLAY").performClick()
    composeTestRule.waitForIdle()
  }
}
