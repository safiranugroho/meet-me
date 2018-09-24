package safira.meetme.model

object FriendList {
    var list: MutableList<Friend> = ArrayList()

    fun add(friend: Friend) = list.add(friend)

    fun size(): Int = list.size

    fun get(index: Int): Friend = list[index]

    fun contains(friend: Friend): Boolean = list.contains(friend)
}