package safira.meetme.model

import org.junit.Assert.*
import org.junit.Test

class FriendListTest {

    private val firstFriend = Friend("Beth", "beth@email.com")
    private val secondFriend = Friend("Niloo", "niloo@email.com")

    @Test
    fun shouldCreateAndAddFriendToList() {
        FriendList.add(firstFriend)
        assertTrue("Friend is not created and stored in list!", FriendList.contains(firstFriend))
    }

    @Test
    fun shouldReturnExistingFriend() {
        FriendList.add(firstFriend)
        assertEquals(Friend("Beth", "beth@email.com"), FriendList.get(0))
    }

    @Test
    fun shouldReturnExistingFriendBasedOnIndex() {
        FriendList.add(firstFriend)
        FriendList.add(secondFriend)
        assertEquals(Friend("Niloo", "niloo@email.com"), FriendList.get(1))
    }
}
