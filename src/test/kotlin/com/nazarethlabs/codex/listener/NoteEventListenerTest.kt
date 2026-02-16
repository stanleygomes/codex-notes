package com.nazarethlabs.codex.listener

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NoteEventListenerTest {
    private lateinit var noteEventListener: NoteEventListener

    @org.junit.Before
    fun setUp() {
        noteEventListener = NoteEventListener()
    }

    private class TestNoteListener : NoteListener {
        var createdCalled = false
        var updatedCalled = false
        var deletedCalled = false

        override fun onNoteCreated() {
            createdCalled = true
        }

        override fun onNoteUpdated() {
            updatedCalled = true
        }

        override fun onNoteDeleted() {
            deletedCalled = true
        }

        fun reset() {
            createdCalled = false
            updatedCalled = false
            deletedCalled = false
        }
    }

    @Test
    fun `should add listener to listeners list`() {
        val listener = TestNoteListener()
        noteEventListener.addListener(listener)
        noteEventListener.notifyNoteCreated()
        assert(listener.createdCalled)
    }

    @Test
    fun `should notify all listeners when note created`() {
        val listener1 = TestNoteListener()
        val listener2 = TestNoteListener()
        noteEventListener.addListener(listener1)
        noteEventListener.addListener(listener2)

        noteEventListener.notifyNoteCreated()

        assert(listener1.createdCalled)
        assert(listener2.createdCalled)
        assert(!listener1.updatedCalled)
        assert(!listener2.updatedCalled)
        assert(!listener1.deletedCalled)
        assert(!listener2.deletedCalled)
    }

    @Test
    fun `should notify all listeners when note updated`() {
        val listener1 = TestNoteListener()
        val listener2 = TestNoteListener()
        noteEventListener.addListener(listener1)
        noteEventListener.addListener(listener2)

        noteEventListener.notifyNoteUpdated()

        assert(listener1.updatedCalled)
        assert(listener2.updatedCalled)
        assert(!listener1.createdCalled)
        assert(!listener2.createdCalled)
        assert(!listener1.deletedCalled)
        assert(!listener2.deletedCalled)
    }

    @Test
    fun `should notify all listeners when note deleted`() {
        val listener1 = TestNoteListener()
        val listener2 = TestNoteListener()
        noteEventListener.addListener(listener1)
        noteEventListener.addListener(listener2)

        noteEventListener.notifyNoteDeleted()

        assert(listener1.deletedCalled)
        assert(listener2.deletedCalled)
        assert(!listener1.createdCalled)
        assert(!listener2.createdCalled)
        assert(!listener1.updatedCalled)
        assert(!listener2.updatedCalled)
    }

    @Test
    fun `should handle multiple notifications correctly`() {
        val listener = TestNoteListener()
        noteEventListener.addListener(listener)

        noteEventListener.notifyNoteCreated()
        assert(listener.createdCalled)
        assert(!listener.updatedCalled)
        assert(!listener.deletedCalled)

        listener.reset()

        noteEventListener.notifyNoteUpdated()
        assert(!listener.createdCalled)
        assert(listener.updatedCalled)
        assert(!listener.deletedCalled)

        listener.reset()

        noteEventListener.notifyNoteDeleted()
        assert(!listener.createdCalled)
        assert(!listener.updatedCalled)
        assert(listener.deletedCalled)
    }

    @Test
    fun `should not add duplicate listener when same listener added twice`() {
        val listener = TestNoteListener()
        noteEventListener.addListener(listener)
        noteEventListener.addListener(listener)

        var callCount = 0
        val countingListener =
            object : NoteListener {
                override fun onNoteCreated() {
                    callCount++
                }

                override fun onNoteUpdated() {}

                override fun onNoteDeleted() {}
            }

        noteEventListener.addListener(countingListener)
        noteEventListener.addListener(countingListener)
        noteEventListener.notifyNoteCreated()

        assert(callCount == 1)
    }

    @Test
    fun `should notify listener only once when added multiple times and note deleted`() {
        var deleteCallCount = 0
        val countingListener =
            object : NoteListener {
                override fun onNoteCreated() {}

                override fun onNoteUpdated() {}

                override fun onNoteDeleted() {
                    deleteCallCount++
                }
            }

        noteEventListener.addListener(countingListener)
        noteEventListener.addListener(countingListener)
        noteEventListener.notifyNoteDeleted()

        assert(deleteCallCount == 1)
    }
}
