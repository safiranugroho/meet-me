package safira.meetme.view

import android.content.Context
import android.support.annotation.LayoutRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import safira.meetme.R
import safira.meetme.model.Friend
import safira.meetme.model.FriendList

class FriendListAdapter(context: Context, @LayoutRes resource: Int) : ArrayAdapter<Friend>(context, resource) {
    companion object {
        private const val LOG_TAG: String = "FriendListAdapter"
    }

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return FriendList.size()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row: View? = null
        val nameView: TextView?

        if (this.count > 0) {
            row = inflater.inflate(R.layout.fragment_friend_list_row, parent, false)
            nameView = row.findViewById(R.id.name)

            try {
                nameView?.text = FriendList.list[position].name
            } catch (e: NullPointerException) {
                Log.e(LOG_TAG, e.message)
            }
        }

        return row!!
    }
}
