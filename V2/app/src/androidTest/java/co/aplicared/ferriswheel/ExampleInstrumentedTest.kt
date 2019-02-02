package co.aplicared.ferriswheel

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.locale.LocaleTestRule

@RunWith(JUnit4::class)
class ExampleInstrumentedTest {

    @Rule
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testTakeScreenshot() {
        Screengrab.screenshot("before_button_click")

        // Your custom onView...
        onView(withId(R.id.mSignUp)).perform(click())

        Screengrab.screenshot("after_button_click")
    }

    companion object {
        @ClassRule
        val localeTestRule = LocaleTestRule()
    }
}