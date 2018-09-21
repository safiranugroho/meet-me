package safira.meetme.model

object FriendList {
    var list: MutableList<Friend> = ArrayList()

    fun add(friend: Friend) = list.add(friend)

    fun size(): Int = list.size
}