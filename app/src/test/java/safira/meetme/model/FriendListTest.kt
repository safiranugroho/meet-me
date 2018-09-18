package safira.meetme.model

import org.junit.Assert.*
import org.junit.Test

class FriendListTest {
    @Test
    fun shouldCreateAndAddFriendToList() {
        val friend = Friend("Beth", "beth@email.com")
        FriendList.add(friend)

        assertEquals(1, FriendList.size())
    }
}
