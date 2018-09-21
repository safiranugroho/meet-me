package safira.meetme.view

import org.junit.Assert.*
import org.junit.Test


class FriendListActivityTest {

    private var mTestActivity: FriendListActivity? = null

    @Throws(Exception::class)
    fun setUp() {
        mTestActivity = FriendListActivity()
    }

    @Test
    fun testPreconditions() {
        assertNotNull("mTestActivity is null", mTestActivity)
    }

}