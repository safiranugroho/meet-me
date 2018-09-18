package safira.meetme.model

import android.content.Context
import android.content.Intent
import android.util.Log

import safira.meetme.model.services.ContactDataManager

object FriendList {
    val LOG_TAG: String = this.javaClass.name

    var list: MutableList<Friend> = ArrayList()

    fun add(friend: Friend) = list.add(friend)

    fun addFromContacts(context: Context, data: Intent) {
        val contactsManager: ContactDataManager

        try {
            contactsManager = ContactDataManager(context, data)
            add(Friend(contactsManager.contactName!!, contactsManager.contactEmail!!))
            println(contactsManager.contactName!!) // Only for debugging, remove once name is displayed in list
        } catch (err: ContactDataManager.ContactQueryException) {
            Log.e(LOG_TAG, err.message)
        }
    }

    fun size(): Int = list.size
}