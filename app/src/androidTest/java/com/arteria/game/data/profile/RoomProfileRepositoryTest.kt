package com.arteria.game.data.profile

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomProfileRepositoryTest {
    private lateinit var database: ProfileDatabase
    private lateinit var repository: RoomProfileRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ProfileDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = RoomProfileRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun createAndActivateProfile_persistsAndMarksActive() = runBlocking {
        val created = repository.createProfile("PilotOne").getOrThrow()
        val activeResult = repository.setActiveProfile(created.id, playedAt = 123L)

        assertTrue(activeResult.isSuccess)

        val active = repository.getActiveProfile()
        assertNotNull(active)
        assertEquals(created.id, active?.id)
        assertEquals(123L, active?.lastPlayedAt)
    }

    @Test
    fun observeProfiles_returnsInsertedProfiles() = runBlocking {
        repository.createProfile("PilotOne")
        repository.createProfile("PilotTwo")

        val profiles = repository.observeProfiles().first()
        assertEquals(2, profiles.size)
    }
}
