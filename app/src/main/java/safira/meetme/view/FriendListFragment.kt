package safira.meetme.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

import safira.meetme.R
import safira.meetme.model.Friend
import safira.meetme.model.FriendList
import safira.meetme.services.ContactDataManager

class FriendListFragment : Fragment() {
    companion object{
        private const val LOG_TAG: String = "FriendListFragment"
        private const val REQUEST_READ_PERMISSION: Int = 800
        private const val READ_FROM_CONTACTS: Int = 900
    }

    private lateinit var friendList: ListView
    private lateinit var emptyView: TextView

    private lateinit var adapter: ArrayAdapter<Friend>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        friendList = view.findViewById(R.id.friendList)
        emptyView = view!!.findViewById(R.id.noFriendsPlaceholder)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        adapter = FriendListAdapter(this.context!!, android.R.layout.simple_list_item_1)

        friendList.adapter = adapter
        friendList.emptyView = emptyView

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.friend_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when (id) {
            R.id.addNewFriend -> readFromContacts()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_READ_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFromContacts()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == READ_FROM_CONTACTS && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                addFromContacts(this.context!!, data)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun readFromContacts() {
        val permission = ContextCompat.checkSelfPermission(this.context!!, Manifest.permission.READ_CONTACTS)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_PERMISSION)
        } else {
            startActivityForResult(Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), READ_FROM_CONTACTS)
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
