package safira.meetme.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import safira.meetme.R
import safira.meetme.model.Friend
import safira.meetme.model.FriendList
import safira.meetme.model.services.ContactDataManager

class FriendListActivity : AppCompatActivity() {
    private val LOG_TAG = FriendListActivity::class.java.name

    private lateinit var friendList: ListView
    private lateinit var emptyView: TextView

    private lateinit var context: Context
    private lateinit var adapter: ArrayAdapter<Friend>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        context = this.applicationContext
        adapter = FriendListAdapter(context, android.R.layout.simple_list_item_1)
        emptyView = findViewById(R.id.noFriendsPlaceholder)
        friendList = findViewById(R.id.friendList)

        friendList.adapter = this.adapter
        friendList.emptyView = this.emptyView

        readFromContacts()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 900 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                addFromContacts(context, data)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 800) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFromContacts()
            }
        }
    }

    private fun readFromContacts() {
        val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 800)
        } else {
            startActivityForResult(Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 900)
        }
    }

    private fun addFromContacts(context: Context, data: Intent) {
        try {
            val contactsManager = ContactDataManager(context, data)
            FriendList.add(Friend(contactsManager.contactName!!, contactsManager.contactEmail!!))
        } catch (e: ContactDataManager.ContactQueryException) {
            Log.e(LOG_TAG, e.message)
        }
    }
}
