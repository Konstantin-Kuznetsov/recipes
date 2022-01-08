package com.example.recipes

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.concurrent.TimeUnit
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.RippleDrawable

import java.lang.IllegalArgumentException


fun ViewInteraction.isDisplayed(percentage: Int = 90): Boolean {
    var isDisplayed = true
    this.withFailureHandler { error, viewMatcher ->
        isDisplayed = false
    }.check(ViewAssertions.matches(ViewMatchers.isDisplayingAtLeast(percentage)))
    return isDisplayed
}

fun ViewInteraction.isNotDisplayed(percentage: Int = 90): Boolean = !this.isDisplayed(percentage)

fun ViewInteraction.waitForVisible(
    timeout: Long = 10,
    message: String = "Item is not displayed on page",
    percentage: Int = 90
): ViewInteraction {
    val startTime = System.currentTimeMillis()
    while (this.isNotDisplayed(percentage)) {
        if ((System.currentTimeMillis() - startTime) > TimeUnit.SECONDS.toMillis(timeout)) {
            throw Exception("$message for $timeout seconds")
        }
        Waiting.sleep(1)
    }
    return this
}

// action on item in RecyclerView
fun ViewInteraction.actionOnItem(matcher: Matcher<View>, action: ViewAction): ViewInteraction =
    perform(
        RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
            matcher,
            action
        )
    )

object Waiting {
    fun sleep(seconds: Long) {
        TimeUnit.SECONDS.sleep(seconds)
    }
}

// This is not a universal Matcher for ImageViews, as you can see I used here "background" for matching bitmaps
// For matching drawbles or other types(RippleDrawable for example) this code needs to be refactored
class DrawableMatcher private constructor(
    @param:DrawableRes private val expectedId: Int
) : TypeSafeMatcher<View>() {

    private var resourceName: String? = null
    override fun matchesSafely(target: View): Boolean {
        if (target !is ImageView) {
            return false
        }

        if (expectedId < 0) {
            return target.background == null
        }

        val resources = target.getContext().resources
        val expectedDrawable: Drawable? = resources.getDrawable(expectedId)

        resourceName = resources.getResourceEntryName(expectedId)
        if (expectedDrawable == null || target.background == null) {
            return false
        }
        val bitmap = getBitmap(target.background)
        val otherBitmap = getBitmap(expectedDrawable)
        return bitmap.sameAs(otherBitmap)
    }

    private fun getBitmap(drawable: Drawable): Bitmap {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else if (drawable is VectorDrawable || drawable is RippleDrawable) {
            getBitmapFromVectorDrawable(drawable)
        } else {
            throw IllegalArgumentException("unsupported drawable type " + drawable.javaClass)
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ")
        description.appendValue(expectedId)
        if (resourceName != null) {
            description.appendText("[")
            description.appendText(resourceName)
            description.appendText("]")
        }
    }

    companion object {
        private fun getBitmapFromVectorDrawable(drawable: Drawable): Bitmap {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        fun imageWithDrawableId(@DrawableRes expectedId: Int): DrawableMatcher {
            return DrawableMatcher(expectedId)
        }
    }
}
