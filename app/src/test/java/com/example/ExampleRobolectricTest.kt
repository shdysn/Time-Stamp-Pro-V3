package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.StreamMode
import com.example.data.model.StreamPlatform
import com.example.data.model.StreamPrivacy
import com.example.database.AppDatabase
import com.example.database.StreamHistoryEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("LiveCast Studio", appName)
    }

    @Test
    fun `test database stream history insertion and query`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = AppDatabase.getDatabase(context)
        val dao = db.streamHistoryDao()

        val sample = StreamHistoryEntity(
            platform = "FACEBOOK",
            title = "Testing Facebook Broadcast",
            description = "Robolectric Unit Test Stream",
            destination = "Alex Live Page",
            privacy = "Public",
            mode = "CAMERA",
            startTimeMillis = System.currentTimeMillis(),
            durationSeconds = 120,
            peakViewers = 450,
            totalComments = 80,
            resolution = "1080p",
            averageBitrateKbps = 4500,
            status = "COMPLETED"
        )

        val insertedId = dao.insertStream(sample)
        assertTrue(insertedId > 0)

        val historyList = dao.getAllHistory().first()
        assertTrue(historyList.any { it.title == "Testing Facebook Broadcast" })
    }

    @Test
    fun `test platform models and defaults`() {
        assertEquals("Facebook Live", StreamPlatform.FACEBOOK.displayName)
        assertEquals("YouTube Live", StreamPlatform.YOUTUBE.displayName)

        assertEquals("Camera Live", StreamMode.CAMERA.displayName)
        assertEquals("Screen Live", StreamMode.SCREEN.displayName)

        assertEquals("Public", StreamPrivacy.PUBLIC.displayName)
    }
}
